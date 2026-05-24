# mc_recorder · 鸣潮共鸣者记录工具

一个基于 Spring Boot 全栈的「鸣潮」（Wuthering Waves）个人记录工具，集成主流后端技术栈与 AI 助手。

## 项目简介

帮你记录在游戏「鸣潮」里拥有的共鸣者（角色）、抽卡历史，并提供一个能调用你自己数据进行个性化建议的 AI 助手。

**项目定位**：个人学习项目。以「一个项目把后端常用技术栈完整过一遍」为目标，同时做出一个真正能用、能给朋友看的小工具。后端实战 + 一份能进简历的作品。

**学习方法**：重要的概念走「两遍法」——先手写极简实现把原理吃透，再用业界框架重写一遍，这样看公司项目代码不黑盒。已对认证（手写拦截器 → Spring Security）、缓存（手写 cache-aside → @Cacheable）采用此法。

---

## 主要功能

### 已完成

- 用户注册 / 登录，JWT 认证（Spring Security 标准实现）
- 当前用户信息接口 `GET /api/auth/me`
- 共鸣者（角色）增删改查
- 多用户数据隔离：每人只能查看 / 修改自己的角色
- Redis 缓存：角色列表自动缓存与失效（`@Cacheable` / `@CacheEvict`）
- 统一响应封装 `Result<T>` + 全局异常处理 `@RestControllerAdvice`
- 参数校验（`@Valid` + jakarta validation）
- DTO 与 Entity 职责分离（注册 / 登录差异化校验规则）
- 接口测试用例（`http-test/*.http`）

### 进行中 / 计划

- 部署上线（Docker Compose + Nginx，本机一键启动 + 云服务器真上线）
- API 文档自动生成（Swagger / SpringDoc）
- MyBatis-Plus 重构数据访问层
- 抽卡记录管理（gacha 模块）
- 个人统计可视化页面
- 异步处理（RabbitMQ：批量抽卡导入 / 行为日志 / 通知）
- 全文搜索（Elasticsearch：角色模糊搜 + 日志聚合）
- **AI 助手**（Spring AI：聊天 + RAG + Tool calling 调用项目接口给个性化建议）
- Vue3 前端

---

## 技术栈

### 已使用

| 类别 | 技术 |
|------|------|
| 语言 / 框架 | Java 25 / Spring Boot 4.x |
| 安全 | Spring Security + JWT (jjwt 0.12.6) |
| 数据访问 | MyBatis |
| 数据库 | MySQL 8.0（Docker） |
| 缓存 | Redis 7（Docker） + Spring Cache 抽象 |
| 构建 | Maven Wrapper |
| 工具 | Lombok / Jackson 3 |

### 计划引入

| 阶段 | 技术 |
|------|------|
| 部署 | Docker Compose + Nginx |
| 文档 | Springdoc OpenAPI |
| ORM 增强 | MyBatis-Plus |
| 消息队列 | RabbitMQ |
| 搜索 | Elasticsearch |
| AI 集成 | Spring AI（备选 LangChain4j） |
| 前端 | Vue3 + axios |

---

## 架构

```
浏览器 / 前端
   |  HTTP + Authorization: Bearer <JWT>
   v
Spring Boot 后端
   ├── Controller (REST API + 参数校验)
   ├── Service    (业务逻辑 + 缓存抽象 @Cacheable)
   └── Mapper     (MyBatis 接口，运行时代理)
                    |
                    +-- MySQL (永久存储)
                    +-- Redis (缓存层)
```

请求经过的核心链路：

```
请求 → Spring Security 过滤器链
       ├── JwtAuthenticationFilter   解析 token、登记当前用户进 SecurityContext
       └── 授权规则                   按路径决定放行 / 401
   → DispatcherServlet
   → Controller (@RequestMapping)
   → Service  (业务逻辑 + 缓存)
   → Mapper   (SQL)
   → DB
```

---

## 快速开始

### 前置要求

- JDK 17+（开发使用 JDK 25，编译目标 17）
- Docker Desktop
- IntelliJ IDEA（推荐 Ultimate，自带 Database 工具）

### 1. 启动依赖服务

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
```

### 2. 创建数据库表

在 IDEA Database 工具（或任意 MySQL 客户端）连接到 `wuwa_record` 库，执行：

```sql
CREATE TABLE user (
    id         BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    password   VARCHAR(100) NOT NULL COMMENT 'BCrypt 哈希',
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

### 3. 启动后端

```powershell
.\mvnw.cmd spring-boot:run
```

应用在 **http://localhost:8000** 启动（8080 落在 Windows 保留端口段，故改用 8000）。

### 4. 测试接口

打开 IDEA，找到 `http-test/` 目录：

- `auth.http` — 注册 / 登录测试
- `resonator.http` — 角色 CRUD + 多用户隔离测试
- `redis-test.http` — Redis API playground

按文件里的 `### 0` 起步注释依次执行即可。

---

## 项目结构

```
src/main/java/com/dy/mcrecorder/mc_recorder/
├── McRecorderApplication.java   启动入口
├── common/                      公共组件
│   ├── Result.java                  统一响应封装
│   ├── JwtUtil.java                 JWT 签发 / 验签
│   ├── JwtAuthenticationFilter      Spring Security JWT 过滤器
│   └── JwtAuthenticationEntryPoint  认证失败统一出口
├── config/
│   ├── SecurityConfig               Spring Security 过滤器链配置
│   └── CacheConfig                  Redis 缓存配置（TTL / 序列化）
├── controller/                  Web 层
│   ├── AuthController               注册 / 登录 / 当前用户
│   └── ResonatorController          角色 CRUD
├── dto/                         请求 / 响应数据传输对象
│   ├── RegisterRequest              注册请求（@Size(min=6) 强校验）
│   ├── LoginRequest                 登录请求（仅 @NotBlank）
│   └── UserResponse                 用户信息响应（隐藏 password）
├── entity/                      数据实体（持久化模型）
│   ├── User
│   └── Resonator
├── exception/
│   └── GlobalExceptionHandler       全局异常翻译为 Result 信封
├── mapper/                      MyBatis 接口
│   ├── UserMapper
│   └── ResonatorMapper
└── service/                     业务层
    ├── UserService
    └── ResonatorService
```

---

## 学习路线

按「工作中使用频率 + 学完不易忘」排序的渐进式路径：

| 阶段 | 内容 | 状态 |
|------|------|------|
| 1 | 三层架构 + 角色 CRUD | 完成 |
| 2 | 统一响应 + 参数校验 + 全局异常 | 完成 |
| 3 | 用户认证（手写 JWT 拦截器） | 完成 |
| 4 | Spring Security + JWT（业界标准重写） | 完成 |
| 5 | DTO 拆分（注册严 / 登录松） | 完成 |
| 6 | 数据归属（多用户隔离 owner_id） | 完成 |
| 7 | Redis 缓存（手写 cache-aside → @Cacheable） | 完成 |
| 8 | Docker Compose 部署上线 | 计划中（下一站） |
| 9 | Swagger API 文档 | 计划中 |
| 10 | MyBatis-Plus 重构 | 计划中 |
| 11 | RabbitMQ 异步处理 | 计划中 |
| 12 | Elasticsearch 搜索 | 计划中 |
| 13 | Spring AI Agent（项目灵魂功能） | 计划中 |
| 14 | Vue3 前端 | 计划中 |

每个阶段都以「先理解概念 → 在项目里实战 → 留下能 git 回看的实例」为标准。

---

## AI 助手设想（项目压轴功能）

最终的 AI 助手将具备三层能力：

1. **基础聊天** — 用户能直接和 LLM 对话讨论养成思路
2. **RAG（检索增强生成）** — 把鸣潮的角色技能 / 武器 / 声骸数据做成向量库，AI 回答时引用具体数据，不瞎编
3. **Tool calling（真 Agent）** — LLM 主动调用 `/api/resonators` 等接口，看到用户**真实拥有**的角色，给出个性化建议（例：「我该练谁？」→ LLM 知道你只有 X、Y、Z 三个 5★，给出适配你的练度方案）

技术选型：Spring AI（备选 LangChain4j）。

---

## 后续想法 / 待添加（自由扩展区）

> 这里可以随时加新想法，实现后挪到上面的「已完成」。

- 待添加：
- 待添加：
- 待添加：

---

## 设计与踩坑笔记

项目里几个值得记的认知点（详细见 `CLAUDE.md`）：

- **Spring Boot 4.0 包路径重组**：很多 autoconfigure 类换了位置。如 `DataSourceAutoConfiguration` 从 `boot.autoconfigure.jdbc` 搬到 `boot.jdbc.autoconfigure`
- **Jackson 3 包路径变化**：`tools.jackson.databind.ObjectMapper`（而非旧 `com.fasterxml.jackson.databind.ObjectMapper`）
- **`@RequestBody` ≠ 依赖注入**：DTO/Entity 永远不是 Bean，Jackson 每请求现造；依赖注入只用于「长期共享的组件」
- **生命周期错位坑**：把 `SecurityContext` 读取写进 Service 字段会失败 —— 单例 Bean 启动时初始化，而 SecurityContext 是请求作用域。修法：Controller 读、传给 Service
- **`@Param` 多参数坑**：Mapper 方法有 2+ 参数时，MyBatis 不会自动拆对象属性，必须 `@Param("xxx")` 显式命名
- **MySQL 保留字**：`CHARACTER` 是关键字，所以表名用 `resonator`
- **Windows 保留端口段**：8080 落在 `netsh excludedportrange protocol=tcp` 列出的 8010-8109 段，bind 失败但 netstat 查不到。改用 8000

---

## License

MIT — 学习项目，欢迎参考。
