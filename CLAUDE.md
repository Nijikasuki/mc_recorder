# CLAUDE.md

本文件为 Claude Code 在本仓库工作时提供指引。这是一个 **Java 后端学习项目**，使用者是 JavaWeb 初学者、职业目标写 Java，偏好学业界常用做法。教学节奏与协作偏好见 auto-memory，不在此重复。

## 项目背景

`mc_recorder` 是「鸣潮」游戏记录工具的**后端**：记录自己拥有的角色、（未来）抽卡记录。前后端分离，Vue3 前端在**同级目录** `mc_recorder_web/`（暂未建，计划由 Claude 生成简易版，用户暂不深究前端）。后端通过 HTTP/JSON 提供接口。

## 常用命令

用 Maven Wrapper，无需全局装 Maven：

```powershell
.\mvnw.cmd spring-boot:run               # 运行（内嵌 Tomcat :8000）
.\mvnw.cmd clean package                 # 打包 jar 到 target/
.\mvnw.cmd test                          # 跑全部测试
.\mvnw.cmd test -Dtest=ClassName         # 跑单个测试类
```

IDEA 里点 `McRecorderApplication.main()` 旁绿色 ▶ 也可运行。**改代码后手动停再启**（devtools 已移除，不再自动重启）。

## 数据库

MySQL 只在 **Docker** 跑，绝不装宿主机。**重建容器务必带时区参数**（否则容器是 UTC，时间差 8 小时）：

```powershell
docker run -d --name mysql-wuwa -e MYSQL_ROOT_PASSWORD=root123 -e MYSQL_DATABASE=wuwa_record -e TZ=Asia/Shanghai -p 3306:3306 -v wuwa_mysql_data:/var/lib/mysql mysql:8.0 --default-time-zone=+08:00
```

- 库 `wuwa_record`，用户 `root`，密码 `root123`，与 `application.yaml` 一致
- 数据存命名卷 `wuwa_mysql_data`，删容器不丢；重建挂回同卷即恢复
- 无迁移工具（Flyway/Liquibase）；表结构手写 SQL 在 IDEA Database 工具执行
- JDBC URL 必须带 `serverTimezone=Asia/Shanghai`；时区根治靠上面容器参数
- 表：`resonator`（角色）、`user`（用户）

## 架构与分包（当前真实结构）

标准三层 + 基础设施分包。`@SpringBootApplication` 组件扫描只认 `com.dy.mcrecorder.mc_recorder` 下的包，新包必须放这下面。

```
controller/   @RestController，只管 HTTP，返回统一 Result 信封
service/      业务逻辑（查重、校验、签发 token 等），返回朴素结果
mapper/       MyBatis @Mapper 接口，@Select/@Insert/@Update/@Delete
entity/       数据库行对应的 POJO，Lombok @Data，带 jakarta 校验注解
common/       Result（统一响应泛型类）、JwtUtil（JWT 签发/验签 @Component）
exception/    GlobalExceptionHandler（@RestControllerAdvice 全局异常翻译）
config/       SecurityConfig（@Bean 暴露 BCryptPasswordEncoder）
```

**分包原则**：包按职责切、按需生长；一个文件不单开包，过早分包是反面教材。

## 统一响应 / 校验 / 异常

- **统一响应** `common/Result<T>`：所有接口返 `{code, message, data}`；`Result.success(data)` / `success()` / `fail(code,msg)` 静态工厂。code=0 成功，非 0 失败（统一信封风格，HTTP 状态默认 200，前端看 body.code）
- **参数校验**：实体字段贴 `@NotBlank/@NotNull/@Min/@Max`，Controller 的 `@RequestBody` 前加 `@Valid` 触发；空/越界在入口被拦
- **全局异常**：`GlobalExceptionHandler` 接 `MethodArgumentNotValidException`（校验失败→400+字段提示）和兜底 `Exception`（→500，不泄露堆栈）
- **业务失败约定**：Service 返朴素结果（boolean / 对象 / null），Controller 翻译成 `Result.success/fail`。如 update/delete 影响 0 行 → fail(404)；状态码有语义，别乱填

## 认证模块（手写第一遍，进行中）

- 依赖：`spring-security-crypto`（仅 BCrypt 工具，未引入整套 Spring Security）、`jjwt 0.12.6`（api/impl/jackson 三件套）
- 密码：**绝不存明文**，`BCryptPasswordEncoder.encode()` 存哈希，`matches(明文,哈希)` 验证（不可逆，非解密）
- JWT：`JwtUtil` 用服务器密钥 HS256 签发/验签；密钥现硬编码在 `JwtUtil.SECRET`（真实项目应外置）。Payload 只放 userId，明文可读不放敏感信息
- 已完成：注册 `POST /api/auth/register`（查重→encode→入库）
- 进行中：登录 `POST /api/auth/login`（验密码→签发 JWT），之后做拦截器验签、保护 resonator 接口
- 安全点：响应**绝不返回 password 字段**（哪怕哈希）

## 领域命名：Resonator 不是 Character

鸣潮角色叫 Resonator。表名/类名用 **`resonator`/`Resonator`**，因为 **`CHARACTER` 是 MySQL 保留字**，用它当表名得到处加反引号。新代码保持一致。

## 关键坑

- **Spring Boot 4.0 重组了 autoconfigure 包路径**：网上教程多为 3.x。如 `org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration` 在 4.0 是 `org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration`。报"包不存在"时，类多半是挪位置了——让 IDEA 自动 import，别照抄旧路径
- **编译目标 vs 运行时**：`pom.xml` 写 `<java.version>17</java.version>`（字节码级别），运行时是 Java 25，二者都是有意的，别"修"
- **MyBatis 下划线↔驼峰自动映射开着**（`map-underscore-to-camel-case: true`）：列 snake_case、字段 camelCase，无需手写 `@Result`
- **`@Mapper` 接口无实现文件**：MyBatis 运行时生成动态代理并注册为 Bean，构造注入，别 `new` 接口
- **SQL 参数用 `#{}` 不用 `${}`**：`#{}` 参数化（安全），`${}` 字符串拼接（注入风险），仅动态表名/列名才用 `${}`
- **Lombok 是注解处理器**（pom 的 maven-compiler-plugin 配置），实体用 `@Data`，别手写 getter/setter
- **多参数 Mapper 绑定坑**：方法多个参数时 `#{xxx}` 不自动拆对象属性，会报 `Parameter not found`；单对象参数或加 `@Param`
- **写操作 Mapper 返回 `int`**（受影响行数），别图省事改 `void`——上层 Service 要靠它判断"是否真的改/删到了"
- **端口用 8000 不用 8080**：本机 8080 落在 Windows/Docker 的保留端口段（`netsh interface ipv4 show excludedportrange protocol=tcp` 可见 8010-8109），任何程序都 bind 不了、`netstat` 还查不到占用。`server.port: 8000` 已配（8000 不在任何保留段）。若 8000 哪天也被保留，挑列表空档另选即可

## API 测试

`http-test/*.http`（项目约定的非标准位置），IDEA HTTP Client 文件，行内 ▶ 运行。现有 `hello.http`（入门 demo）、`resonator.http`（角色 CRUD + 校验/异常用例）、`auth.http`（注册用例）。新接口在此加用例。

## 当前进度小结

**已完成**：项目骨架 → 数据库连通（时区已修）→ 角色 `resonator` 完整 CRUD（list/getOne/create/update/delete）→ 重构出完整三层 → 统一响应 `Result<T>` → 参数校验 `@Valid` → 全局异常 `GlobalExceptionHandler` → 规范分包（exception/、config/）→ 认证：密码 BCrypt + 注册接口 + JwtUtil 工具。

**进行中**：登录接口（验密码 + 签发 JWT）。

**后续路线**（与用户对齐）：登录两遍法（手写极简 → 之后用 Spring Security + JWT 重写）→ Redis → 接口文档 Swagger → 消息队列 → Elasticsearch → Docker 部署。当前**聚焦把登录彻底做透**再下一站。
