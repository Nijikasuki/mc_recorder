# mc_recorder · 鸣潮共鸣者记录工具

一个基于 Spring Boot 全栈的「鸣潮」（Wuthering Waves）个人记录工具，集成主流后端技术栈与 AI 助手。

## 项目简介

帮你记录在游戏「鸣潮」里拥有的共鸣者（角色）、抽卡历史，并提供一个能调用你自己数据进行个性化建议的 AI 助手。

**项目定位**：个人学习项目。以「一个项目把后端常用技术栈完整过一遍」为目标，同时做出一个真正能用、能给朋友看的小工具。后端实战 + 一份能进简历的作品。

**学习方法**：重要的概念走「两遍法」——先手写极简实现把原理吃透，再用业界框架重写一遍。已对认证（手写拦截器 → Spring Security）、缓存（手写 cache-aside → @Cacheable）采用此法。

---

## 主要功能

### 已完成

- 用户注册 / 登录，JWT 认证（Spring Security 标准实现）
- 注册带邮箱（`@Email` 校验），为后续异步发欢迎邮件做铺垫
- 当前用户信息接口 `GET /api/auth/me`
- 共鸣者（角色）增删改查 + 分页（`MyBatis-Plus` 的 `selectPage`）
- 多用户数据隔离：每人只能查看 / 修改自己的角色
- Redis 缓存：角色列表自动缓存与失效（`@Cacheable` / `@CacheEvict`）
- 统一响应封装 `Result<T>` + 全局异常处理 `@RestControllerAdvice`
- 参数校验（`@Valid` + jakarta validation）
- DTO 与 Entity 职责分离（注册 / 登录差异化校验规则）
- **Docker 化部署**：Dockerfile + docker-compose 一键启动（app + MySQL + Redis）
- **API 文档自动化**：SpringDoc / Swagger UI 集成 JWT Bearer 认证
- **数据访问层升级**：从 MyBatis 重构为 MyBatis-Plus（BaseMapper + LambdaQueryWrapper）
- **RabbitMQ 基础设施**：Exchange + Queue + Binding 声明 + JSON 消息转换器（业务场景见下方）
- 接口测试用例（`http-test/*.http`）

### 进行中

- **AI 助手第一遍 - Spring AI**（main 分支）：
  - ✅ 基础聊天（智谱 GLM-4.5-air）
  - ✅ Tool Calling（LLM 主动调 `getMyResonators` 看用户真实角色）
  - ✅ ChatMemory 多轮对话（MySQL 持久化 + 装饰器绕开 milestone bug）
  - ✅ RAG 手写版（智谱 embedding-3 + 内存向量库 + 手算余弦相似度 + augment prompt + system/user 分离抗 prompt injection）
  - ✅ Spring AI 框架版 RAG（`SimpleVectorStore` + `QuestionAnswerAdvisor` + 自定义 prompt 模板，统一 `/chat` 端点同时启用 RAG + Memory + Tool 三件套）
  - ✅ pgvector 持久化向量库（Polyglot Persistence：MySQL 主 + Postgres 副 双 DataSource，PgVectorStore 替代 SimpleVectorStore，启动幂等检查）
  - ✅ 第三方 API 集成（GitHub API 完整对接：RestClient + Properties + DTO + Service + @Tool 暴露给 AI；模型升级到 GLM-4-plus 解决 Tool Calling 循环）
  - 🚧 下一步：Vue3 前端整合（终篇）

### 计划

- AI 第二遍：Python LangChain 微服务版（python-microservice 分支）
- 抽卡记录管理（gacha 模块）
- 个人统计可视化页面
- 全文搜索（Elasticsearch：角色模糊搜 + 日志聚合）
- **Vue3 前端**（终篇——所有功能整合到一个像样的 web UI）

---

## 技术栈

| 类别 | 技术 |
|------|------|
| 语言 / 框架 | Java 25 / Spring Boot 4.0.6 |
| 安全 | Spring Security + JWT (jjwt 0.12.6) |
| 数据访问 | MyBatis-Plus 3.5.14（含分页插件 jsqlparser）|
| 数据库 | MySQL 8.0（业务数据） + PostgreSQL 16 + pgvector（向量库），均 Docker |
| 缓存 | Redis 7（Docker） + Spring Cache 抽象 |
| 消息队列 | RabbitMQ 3-management（Docker） |
| API 文档 | SpringDoc OpenAPI 2.8.13 |
| AI 集成 | Spring AI 2.0.0-M8（OpenAI 兼容协议 → 智谱 GLM-4-flash） |
| 部署 | Docker / Docker Compose |
| 构建 | Maven Wrapper |
| 工具 | Lombok / Jackson 3 |

### 计划引入

| 阶段 | 技术 |
|------|------|
| 搜索 | Elasticsearch |
| 前端 | Vue3 + axios |

---

## 架构

```
浏览器 / 前端
   |  HTTP + Authorization: Bearer <JWT>
   v
Spring Boot 后端
   ├── Controller (REST API + 参数校验)
   ├── Service    (业务逻辑 + 缓存抽象 @Cacheable + MQ 事件发布)
   └── Mapper     (MyBatis-Plus BaseMapper)
                    |
                    +-- MySQL (永久存储)
                    +-- Redis (缓存层)
                    +-- RabbitMQ (异步事件)
```

请求经过的核心链路：

```
请求 → Spring Security 过滤器链
       ├── JwtAuthenticationFilter   解析 token、登记当前用户进 SecurityContext
       └── 授权规则                   按路径决定放行 / 401
   → DispatcherServlet
   → Controller (@RequestMapping)
   → Service  (业务逻辑 + 缓存 + 发 MQ 事件)
   → Mapper   (SQL)
   → DB
```

异步事件链路：

```
Service.register 成功 → Producer.convertAndSend → RabbitMQ Exchange
                                                       ↓ routing key 匹配
                                                       Queue
                                                       ↓
                                                 @RabbitListener Consumer
                                                       ↓
                                              「假装发欢迎邮件」（日志）
```

---

## 快速开始

### 前置要求

- JDK 17+（开发使用 JDK 25，编译目标 17）
- Docker Desktop
- IntelliJ IDEA（推荐 Ultimate，自带 Database 工具）

### 推荐方式：Docker Compose 一键启动

```powershell
docker compose up -d
```

会启动 MySQL + Redis + 应用三个容器。访问 `http://localhost:8000/swagger-ui.html` 看 API 文档。

### 或单容器手动启动（用于本地开发）

```powershell
# MySQL（注意 TZ=Asia/Shanghai 避免时区差 8 小时）
docker run -d --name mysql-wuwa `
  -e MYSQL_ROOT_PASSWORD=root123 `
  -e MYSQL_DATABASE=wuwa_record `
  -e TZ=Asia/Shanghai `
  -p 3306:3306 `
  -v wuwa_mysql_data:/var/lib/mysql `
  mysql:8.0 --default-time-zone=+08:00

# Redis
docker run -d --name redis-wuwa -p 6379:6379 redis:7

# RabbitMQ（含管理 UI，http://localhost:15672, guest/guest 登录）
docker run -d --name rabbitmq-wuwa -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```

### 创建数据库表

```sql
CREATE TABLE user (
    id         BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    password   VARCHAR(100) NOT NULL COMMENT 'BCrypt 哈希',
    email      VARCHAR(100) COMMENT '注册邮箱，用于异步欢迎邮件等',
    created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE resonator (
    id              BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    owner_id        BIGINT UNSIGNED NOT NULL COMMENT '归属用户',
    name            VARCHAR(50)     NOT NULL,
    element         VARCHAR(10)     NOT NULL COMMENT '属性',
    star            TINYINT         NOT NULL COMMENT '星级 4/5',
    level           SMALLINT        NOT NULL DEFAULT 1,
    resonance_chain TINYINT         NOT NULL DEFAULT 0 COMMENT '共鸣链 0-6',
    created_at      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='鸣潮角色收集记录';
```

> 表名用 `resonator` 而非 `character`，因为 `CHARACTER` 是 MySQL 保留字。

### 启动后端（IDE 开发模式）

```powershell
.\mvnw.cmd spring-boot:run
```

应用在 **http://localhost:8000** 启动（8080 落在 Windows 保留端口段，故改用 8000）。

### 测试接口

- 浏览器：`http://localhost:8000/swagger-ui.html`（API 可视化文档 + 在线调用）
- IDEA HTTP Client：`http-test/*.http` 各场景测试用例
- RabbitMQ Management UI：`http://localhost:15672`（guest/guest）

---

## 项目结构

```
src/main/java/com/dy/mcrecorder/mc_recorder/
├── McRecorderApplication.java   启动入口（含 @MapperScan）
├── common/
│   ├── Result.java                  统一响应封装
│   ├── JwtUtil.java                 JWT 签发 / 验签
│   ├── JwtAuthenticationFilter      Spring Security JWT 过滤器
│   ├── JwtAuthenticationEntryPoint  认证失败统一出口
│   └── MqConstants                  MQ 字符串常量集中点（队列名 / 交换机 / routing key）
├── config/
│   ├── SecurityConfig               Spring Security 过滤器链配置
│   ├── CacheConfig                  Redis 缓存配置（TTL / 序列化）
│   ├── MybatisPlusConfig            MyBatis-Plus 分页插件配置
│   ├── OpenApiConfig                Swagger Bearer JWT 集成
│   ├── RabbitMqConfig               RabbitMQ Queue / Exchange / Binding 声明 + JSON 消息转换器
│   ├── AiConfig                     @Bean 暴露 `PgVectorStore`（注入 pgvectorJdbcTemplate + EmbeddingModel）
│   ├── DatabaseConfig               双 DataSource 配置：MySQL @Primary（业务数据/ChatMemory）+ pgvector（向量库）
│   ├── GitHubProperties             @ConfigurationProperties 装 GitHub base-url + token
│   └── TextOnlyChatMemoryRepository 装饰器：包装 JdbcChatMemoryRepository，过滤 tool 消息绕开 Spring AI M7/M8 bug
├── controller/
│   ├── AuthController               注册 / 登录 / 当前用户
│   ├── ResonatorController          角色 CRUD + 分页
│   ├── AiController                 AI 聊天入口（含多轮对话 + Tool Calling + RAG）
│   └── GitHubController             GitHub 代理端点（HTTP 直接调，便于测试和前端复用）
├── dto/
│   ├── RegisterRequest              注册请求（含 @Email 邮箱字段）
│   ├── LoginRequest                 登录请求
│   ├── UserResponse                 用户信息响应（隐藏 password）
│   ├── UserRegisteredEvent          MQ 事件 DTO（用户注册）
│   ├── GitHubRepo                   GitHub 仓库信息 record（@JsonNaming(SnakeCaseStrategy)）
│   └── GitHubRepoActivity           GitHub 仓库活动 record（含嵌套 Actor）
├── entity/
│   ├── User                         含 @TableName / @TableId（MBP）+ email 字段
│   └── Resonator                    含 @TableName / @TableId（MBP）
├── exception/
│   └── GlobalExceptionHandler       全局异常翻译为 Result 信封
├── mapper/
│   ├── UserMapper                   extends BaseMapper<User>（MBP 接管 CRUD）
│   └── ResonatorMapper              extends BaseMapper<Resonator>
└── service/
    ├── UserService                  注册 / 登录 / 查询
    ├── ResonatorService             角色业务 + LambdaQueryWrapper
    ├── WelcomeEmailProducer         MQ 生产者
    ├── WelcomeEmailConsumer         MQ 消费者（@RabbitListener）
    ├── AiService                    ChatClient + 三 advisor 链（Memory + RAG + Tool）+ system prompt 分离架构
    ├── ResonatorTools               @Tool 类，暴露给 LLM 调用（含 ToolContext 隐藏 userId）
    ├── KnowledgeLoader              @PostConstruct 启动加载 wuwa.md → 切块 → 包成 Document → vectorStore.add()（框架自动 embed）
    ├── GitHubService                调 GitHub REST API 的核心 Service（RestClient + Bearer Token）
    └── GitHubTools                  @Tool 类，包装 GitHubService 暴露给 LLM 调用

src/main/resources/
└── knowledge/
    └── wuwa.md                      鸣潮共鸣者百科（手写 5 个角色，按 --- 分隔块）

http-test/                           各业务测试用例（IDEA HTTP Client）
Dockerfile                           应用镜像构建
docker-compose.yml                   多容器编排
```

---

## 学习路线

按「工作中使用频率 + 学完不易忘」排序的渐进式路径：

| 阶段 | 内容 | 状态 |
|------|------|------|
| 1 | 三层架构 + 角色 CRUD | ✅ 完成 |
| 2 | 统一响应 + 参数校验 + 全局异常 | ✅ 完成 |
| 3 | 用户认证（手写 JWT 拦截器） | ✅ 完成 |
| 4 | Spring Security + JWT（业界标准重写） | ✅ 完成 |
| 5 | DTO 拆分（注册严 / 登录松） | ✅ 完成 |
| 6 | 数据归属（多用户隔离 owner_id） | ✅ 完成 |
| 7 | Redis 缓存（手写 cache-aside → @Cacheable） | ✅ 完成 |
| 8 | Docker Compose 部署上线 | ✅ 完成 |
| 9 | Swagger / SpringDoc API 文档 | ✅ 完成 |
| 10 | MyBatis-Plus 重构 + 分页插件 | ✅ 完成 |
| 11 | RabbitMQ：基础设施 + 注册异步发欢迎邮件 | ✅ 完成 |
| 12 | AI - Spring AI 基础（聊天 + Tool Calling）| ✅ 完成 |
| 13 | AI - Spring AI 多轮对话（ChatMemory）| ✅ 完成 |
| 14 | AI - Spring AI 知识库（RAG 手写版：embedding + 余弦 + augment）| ✅ 完成 |
| 14b | AI - Spring AI 框架版 RAG（`QuestionAnswerAdvisor` 三件套合一）| ✅ 完成 |
| 14c | AI - 持久化向量库（pgvector + 双 DataSource）| ✅ 完成 |
| 14d | 第三方 API 集成（GitHub: RestClient + DTO + @Tool）| ✅ 完成 |
| 18 | **Vue3 前端整合（终篇）**| 🚧 下一站 |
| 15 | AI -（可选）MCP 探索 | ⏳ 可选 |
| 16 | AI - Python LangChain 微服务版（独立分支）| ⏳ 计划中 |
| 17 | Elasticsearch 搜索 | ⏳ 计划中 |
| 18 | **Vue3 前端（终篇）**| ⏳ 计划中 |

每个阶段都以「先理解概念 → 在项目里实战 → 留下能 git 回看的实例」为标准。

---

## AI 助手（项目压轴功能）—— 两遍法路线

### 能力目标（最终形态）

1. **基础聊天** — 用户能直接和 LLM 对话讨论养成思路
2. **RAG（检索增强生成）** — 把鸣潮的角色技能 / 武器 / 声骸数据做成向量库，AI 回答时引用具体数据，不瞎编
3. **Tool calling（真 Agent）** — LLM 主动调用 `/api/resonators` 等接口，看到用户**真实拥有**的角色，给出个性化建议

### 学习路线：两遍法 + 分支隔离

用户背景：**Python + LangChain 已会**（属于舒适区），**Java/Spring 是当前学习重点**。两个角度都做：

#### 第一遍：Spring AI（**main 分支**）

- 在当前 Spring Boot 项目里加 Spring AI starter，直接集成
- 学到「Java 后端怎么集成 AI」——求职 Java 岗的实操能力
- 选 `spring-ai-starter-model-openai`（OpenAI 兼容协议，可调 OpenAI / DeepSeek / 智谱 / Ollama 等多家）
- 预计 2-3 个 session 出能用的 AI 功能

#### 第二遍：Python LangChain 微服务（**python-microservice 分支**）

- Spring AI 完成后 commit + 打 tag → 从 main 切新分支
- 在新分支拆出独立 Python FastAPI 服务，跑 LangChain
- Java 端改成 HTTP 或复用 RabbitMQ 调 Python 服务
- 学到「微服务架构 + 跨语言通信」——业界真实样子
- 预计 2-3 个 session 完成迁移

### 与 MQ 的联动

LLM 调用通常很慢（5-10 秒），AI 助手可以**复用现有的 RabbitMQ 基础设施**：

```
用户提问 → Controller 收到 → 发"AI 提问"事件到 MQ → 立刻返回"正在思考"
                                              ↓
                                       AI Consumer 拉消息
                                              ↓
                                       调 LLM（耗时操作）
                                              ↓
                                       结果存数据库 / 推送给前端
```

**MqConstants 里可预留事件类型**（如 `ai.question.asked`），新增 AI 事件只是加 Queue + Binding + Consumer，不动现有基础设施。这就是「共享 Exchange + 多 Queue」架构的远期价值——**今天为欢迎邮件搭的，明天 AI 直接复用**。

第二遍 Python 微服务方案中，**Java ↔ Python 通信也可走 RabbitMQ**（Java 发 → Python Consumer 拉 → 处理 → 结果回写），就是上面这个图，只是消息消费方变成 Python。

### LLM 提供商

候选（**真正调 API 时再定**）：
- **DeepSeek**：便宜 + 中文好 + OpenAI 兼容协议，国内项目首选
- **Anthropic Claude**：质量最高，新用户 $5 免费额度（需国外卡）
- **Ollama 本地**：完全免费，需本机 16GB+ 内存
- **OpenAI**：最主流，国内访问不稳

技术选型已定：**Spring AI（第一遍） + LangChain Python（第二遍）**。

---

## 后续想法 / 待添加（自由扩展区）

> 这里可以随时加新想法，实现后挪到上面的「已完成」。

- **第三方 API 集成练习**（独立 mini-章节，等 RAG 后插入）：在项目里找一个需求，对接一个外部公开 API（如天气、汇率、热点新闻），完整走"读 API docs → 看认证方式 → 用 Spring `RestClient` 或 `WebClient` 调用 → 处理错误码与限流 → 包成自己的 Service"。目的：练"对接别人服务"这一工业刚需技能，而不是一直当服务方
- **业界硬化项（路上择机加，别一次做完）**：
  - API key 走环境变量 / `.env` 文件（当前 `application.yaml` 里硬编码）
  - AI 调用流式输出（SSE / EventSource）——配合 Vue 前端一起做
  - AI 调用 token 用量追踪（每次调用记 promptTokens / completionTokens，做成本可见性）
  - LLM API 熔断 / 重试 / 限流（Resilience4j）
  - 可观测性：Prometheus + Micrometer 指标
  - ChatMemory 多窗口（ChatGPT 侧栏 UX）—— Vue 前端章节做
  - ChatMemory 老消息 RAG 召回（滑动窗口 + 历史向量检索）
  - 单机部署 → 集群（学习项目可不做）

---

## 设计与踩坑笔记

项目里几个值得记的认知点（详细见 `CLAUDE.md`）：

- **Spring Boot 4.0 包路径重组**：很多 autoconfigure 类换了位置（如 `DataSourceAutoConfiguration` / `RabbitAutoConfiguration` 都搬到 `boot.<feature>.autoconfigure`）
- **Jackson 3 包路径变化**：`tools.jackson.databind.ObjectMapper`（而非旧 `com.fasterxml.jackson.databind.ObjectMapper`）
- **Spring AMQP 4.x 新增 `JacksonJsonMessageConverter`**（无 "2"，对应 Jackson 3），旧 `Jackson2JsonMessageConverter` 已过时
- **`@RequestBody` ≠ 依赖注入**：DTO/Entity 永远不是 Bean，Jackson 每请求现造；依赖注入只用于「长期共享的组件」
- **生命周期错位坑**：把 `SecurityContext` 读取写进 Service 字段会失败 —— 单例 Bean 启动时初始化，而 SecurityContext 是请求作用域
- **`@Param` 多参数坑**：Mapper 方法有 2+ 参数时，MyBatis 不会自动拆对象属性，必须 `@Param("xxx")` 显式命名
- **MySQL 保留字**：`CHARACTER` 是关键字，所以表名用 `resonator`
- **MyBatis-Plus 分页插件单独依赖**：3.5.10+ 把 `PaginationInnerInterceptor` 拆到 `mybatis-plus-jsqlparser` 独立模块
- **MBP 按 Spring Boot 版本分 starter**：`spring-boot3-starter` vs `spring-boot4-starter`，对 SB4 必须用后者
- **Windows 保留端口段（动态分配）**：8080、3306 等常用端口可能落在 Hyper-V/WSL2 的保留段，netstat 查不到但 bind 失败。根治方案：`netsh int ipv4 set dynamic tcp start=49152 num=16384` 把动态保留段挪到 49152+
- **MQ 字符串集中管理**：用 `MqConstants` 类把队列名 / 交换机 / routing key 都放一起，避免散落各处
- **Spring AI 与 Spring Boot 4 的版本对应**：Spring AI 1.x ↔ Spring Boot 3.x（稳定 GA），Spring AI 2.x ↔ Spring Boot 4.x（截至 2026-05 仍在 milestone，本项目用 `2.0.0-M8`）。生态滞后是 Spring 大版本升级的常态，约需 6-12 个月稳定
- **GlobalExceptionHandler 的反模式**：`@ExceptionHandler(Exception.class)` 兜底**必须** `log.error("...", e)` 打 stack trace。光返回 `Result.fail(500)` 而不 log，会导致"用户报 500、控制台没异常"的灾难——业界铁律：前端友好 ≠ 开发者也瞎
- **Spring AI M7/M8 ChatMemory + Tool Calling 持久化 bug**：`JdbcChatMemoryRepository` 的表 schema 没有 `tool_call_id` 列，tool 消息持久化后丢字段，下一轮 rehydrate 时 OpenAI SDK 抛 `toolCallId is required`。解法：**装饰器模式** —— 写 `TextOnlyChatMemoryRepository implements ChatMemoryRepository` 包装原 Jdbc 仓库，`saveAll` 时过滤掉 `ToolResponseMessage` 与带 `toolCalls` 的 `AssistantMessage`。代价：跨轮引用 tool 原始结果失效，但 LLM 拿到 tool 后的文字总结仍保留，实用上几乎无影响
- **Spring 全家桶 `initialize-schema` 三档值**：`never`（默认，生产）/ `embedded`（仅内嵌库）/ `always`（每次启动跑建表脚本）。默认保守，要框架代管 schema 必须显式 opt-in。同套路出现在 `spring.sql.init.mode` / `spring.session.jdbc.initialize-schema` 等多处
- **Maven 传递依赖版本冲突**：Spring AI M8 拖的 `openai-java-core` 依赖 `swagger-annotations:2.2.31`（旧版无 `$dynamicRef`），与 SpringDoc 想要的新版冲突，导致 Swagger 启动后调 API 时 `NoSuchMethodError`。解法：pom 直接声明 `swagger-annotations:2.2.38`（直接依赖优先于传递依赖）。Maven 仲裁规则：直接 > 传递，传递里"距离近"的赢，所以打架时优先在 pom 里显式声明权威版本
- **Windows CRLF 换行符坑 RAG 切块**：`wuwa.md` 在 Windows 下默认 CRLF（`\r\n`），用正则 `\n---\n` 切块永远匹配不上 → 整个文件被当成 1 块。解法：用 Java regex 的 `\\R`（匹配任意换行）—— `content.split("\\R---\\R")`。同套路适用所有跨平台文本处理
- **Prompt Injection 多层防御**：RAG 把检索资料 + 用户问题塞同一个 user message 时，用户可以"忽略上面所有指令"覆盖。解法：把任务规则塞 `.system(...)`，动态内容塞 `.user(...)`，OpenAI 协议下 system 权重 > user。但 100% 防御是开放问题，业界做法是"defense in depth"：① provider 内置 moderation（智谱已内置） + ② 你的 system prompt 显式禁止泄露 + ③ 输出后处理。商用 LLM 上想测自己的 prompt 抗性，要小心 provider moderation 抢先拦截（这本身也是好事，多一层防御）
- **YAGNI（"你不需要它"）原则**：开发时不要为"可能将来用"的参数预留。如 `ragChat(query, topK, userId)` 里 `userId` 用不上就别留——参数列表是 API 契约的一部分，今天的多余明天就是包袱
- **Spring AI 2.0 模块拆分**：1.x 时代 VectorStore + advisors 都打在一个大 starter 里；2.0 拆成 `spring-ai-vector-store`（接口 + `SimpleVectorStore`）和 `spring-ai-advisors-vector-store`（`QuestionAnswerAdvisor`）等独立模块。用啥加啥，干净但要记得显式声明
- **Spring AI 2.0 配置结构变化**：`spring.ai.openai.chat.options.model` 是 1.x 路径，2.0-M8 直接 inline 成 `spring.ai.openai.chat.model`，IDE 会标 `options.*` 已过时——按 IDE 提示走，老经验失效
- **Field-only-in-constructor 反模式**：构造器参数用一次就用不到时（如 `VectorStore` 只在构造器里给 advisor，`chat()` 方法不再用），**不要建字段**，直接用构造器参数。规则：**字段是给跨方法共享状态用的**，一次性连线不算
- **弱模型遇上复杂 prompt 的退化**：GLM-4-flash（免费 tier）遇到"三件套 + 长 system prompt"会**自己幻觉"我没权限"拒绝调 tool**，不是 prompt 写错，是模型能力上限。业界经验：**80% 的"AI 应用不好用"问题升一档模型就解决**，prompt 优化的边际收益远不如换模型。学习项目里 `glm-4.5-air`（¥0.5/M tokens）足够
- **三类问题分流模板（RAG / Tool / Memory）**：默认 `QuestionAnswerAdvisor` 模板"必须依据上下文回答"太霸道，会让 LLM 把所有问题（包括"我叫什么"这种 Memory 问题）都答"不知道"。解法：自定义 `PromptTemplate`，显式三段引导——百科问题走 RAG、个人数据问题用 Tool、对话信息走 Memory。**通用引导胜过逐工具枚举**（一句"用户数据用工具"比列举每个 @Tool 强）
- **Provider SDK vs 框架抽象**：智谱有自家 `ai.z.openapi:zai-sdk`，但用了就锁死智谱、失去 Spring AI 的 VectorStore/ChatMemory/Advisor 抽象；用 Spring AI + OpenAI 兼容协议 → 换 DeepSeek/Claude/Ollama 改 yaml 即可。学习/生产几乎都该选框架抽象一边
- **Polyglot Persistence（多语言持久化）**：业界做法把不同数据放最合适的存储——MySQL 业务数据（强事务）+ Redis 缓存（速度）+ Postgres+pgvector 向量（相似度搜索）。学习项目 docker 各起一个容器即可，部署阶段 docker-compose 统一编排
- **Spring Boot 多 DataSource 配置三板斧**：① 显式声明每个 `DataSourceProperties` + `DataSource` Bean（手写覆盖 auto-config）；② 主 DataSource 加 `@Primary`，副的不加；③ **每个注入处用 `@Qualifier("xxx")` 显式指定**——不依赖参数名匹配（字节码默认不保留参数名，靠不住）。**学习项目最易踩的坑：依赖 @Primary 自动选 JdbcTemplate**——多源场景下不可靠，应显式声明 `@Primary JdbcTemplate` Bean 给 MySQL 用
- **`@ConditionalOnMissingBean` 的连锁效应**：Spring Boot 自动配的 DataSource 有 `@ConditionalOnMissingBean(DataSource.class)`——只要你定义任何 DataSource Bean（哪怕是副的），主 DataSource auto-config 就被跳过。后果：MyBatis/ChatMemory 注入 DataSource 时拿到副库（pgvector），SQL 跑 MySQL 语法报错。修法：显式声明主 DataSource + 标 @Primary
- **starter vs 非 starter 依赖选择**：starter 含 auto-config，99% 场景省事；但**手动控制 Bean 创建**（如多 DataSource、自定义索引参数）时 auto-config 会**抢着创建 Bean 名冲突**。两条出路——① `spring.autoconfigure.exclude` 排除 auto-config；② 改用非 starter 依赖（如 `spring-ai-pgvector-store` 替代 `spring-ai-starter-vector-store-pgvector`）。后者更干净
- **pgvector HNSW 索引 2000 维上限**：智谱 embedding-3 输出 2048 维，超过 pgvector HNSW 索引硬限（2000）→ `CREATE INDEX` 报错。解法：① 用 IVFFLAT 索引（上限 ~16000 维，速度略慢）；② 用 `PgVectorStore.PgIndexType.NONE` 不建索引（小数据集足够）；③ 改用 embedding-2（1024 维）。学习项目 5 个文档选 NONE 最省事
- **PG schema 层在 IDEA 默认不展示**：PG 结构是 server → database → schema → table，比 MySQL 多一层。IDEA 连接 PG 后默认只显示连接 URL 里指定的那个数据库，且 schema 也要在 Properties → Schemas 勾选 `public`。命令行 `\dt` 能看到的表，IDEA 里要展开 `database → schemas → public → tables` 才看得到
- **VectorStore 接口的接口隔离原则（ISP）**：Spring AI 的 `VectorStore extends DocumentWriter, VectorStoreRetriever`——把"写入"和"检索"拆成独立小接口组合而成。好处：只读服务依赖 `VectorStoreRetriever`，只写索引 batch 依赖 `DocumentWriter`，编译期就能限制权限。SOLID 五原则中 "I" 的实例
- **Maven artifact 命名不规律**：Spring AI 2.0 系列里，pgvector 的 starter 叫 `spring-ai-starter-vector-store-pgvector`（多层分类式），非 starter 叫 `spring-ai-pgvector-store`（平铺式）。**命名规律不可猜**，拿不准就上 Maven Central 搜确认
- **第三方 REST API 集成的通用 6 步模板**：① 读 docs 找 base-url/认证/endpoint/响应 → ② 写 `Properties` 类装配置 → ③ 写 `DTO record` 匹配响应 → ④ 写 `Service` 用 `RestClient` 调 → ⑤ 写 `Controller` 暴露 HTTP（可选）+ 写 `@Tool` 暴露给 AI（可选）→ ⑥ 测试。这套模板对任何 RESTful API 通用（GitHub / 和风天气 / Tavily / Stripe / 智谱 都套这个）
- **API docs 滞后于实际 API**：GitHub `/repos/{owner}/{repo}/activity` 文档示例的 `pushed_at`/`push_type`/`pusher`，实际响应是 `timestamp`/`activity_type`/`actor`。业界铁律：**写 DTO 前先 curl 抓真实响应**，不要 100% 信文档。curl 在 PowerShell 报 SSL 错时换 `Invoke-RestMethod` 原生命令
- **Jackson 2 vs Jackson 3 双版本陷阱**：Spring AI 2.0.0-M8 + Jackson 3 时代，annotations 包 (`@JsonProperty`) 仍在 Jackson 2 的 `com.fasterxml.jackson.annotation.*`（向后兼容），databind 已在 `tools.jackson.*`。IDE auto-import 时候要识别这种"半 Jackson 3" 情况。`@JsonNaming` 在 `tools.jackson.databind.annotation`
- **LLM Tool Calling 死循环的真凶层级**：模型能力 > 框架 > prompt。① 弱模型 (glm-4.5-air) 遇到复合跨源问题反复调同工具直到超时；② 升级到 glm-4-plus 一次调用就停，**1 元/M tokens 性价比极高**；③ 业界铁律："**80% 的 AI 应用问题用升级模型一招解决**"，prompt 工程是在选定模型上榨能力，模型选型才是核心杠杆
- **架构干净 vs 跑得稳的取舍**：理论上 system prompt 应独立 (`.defaultSystem()`) 跟 RAG (用 QA advisor `.promptTemplate()`) 解耦，是 OpenAI 协议设计意图。但 Spring AI 2.0.0-M8 milestone 上，弱模型 + 干净架构容易死循环，强模型 + 干净架构则完美。结论：**架构选择跟模型能力联动**——强模型 → 协议级干净分离；弱模型 → 全塞 QA template 保险（设计妥协换功能）
- **GitHub PAT 最小权限原则**：学习项目 PAT 选 "**no scope**"（不勾任何权限），享受认证后的 5000 次/小时限流提升，但即便 token 泄露**啥都干不了**（只能读公开数据，网上谁都能读）。比勾 `public_repo` 多余权限的 5 倍安全

---

## License

MIT — 学习项目，欢迎参考。
