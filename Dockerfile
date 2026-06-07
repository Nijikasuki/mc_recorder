# ============================================================
# Spring Boot 后端镜像 — 多阶段构建
# 阶段 1: 用 Maven 镜像编译生成 jar
# 阶段 2: 用 JRE 镜像跑这个 jar (最终镜像不含源码/Maven, 体积小)
# ============================================================

# ---------- 阶段 1: 编译 ----------
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /build

# 先 copy pom.xml 单独下依赖, 利用 docker 层缓存
# (源码改动不会触发 mvn 重新下依赖, 加速重建)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 再 copy 源码和构建配置
COPY src ./src
COPY mvnw .
COPY .mvn .mvn

# 编译打包, 跳过测试 (build 阶段不跑单测, CI 里另外跑)
RUN mvn clean package -DskipTests -B

# ---------- 阶段 2: 运行 ----------
FROM eclipse-temurin:17-jre
WORKDIR /app

# 从阶段 1 把打好的 jar 复制过来 (改名 app.jar 方便 ENTRYPOINT)
COPY --from=builder /build/target/*.jar app.jar

# 告诉 docker 这容器要用 8000 端口 (文档作用, 不实际绑定)
EXPOSE 8000

# 启动命令 — 容器跑起来就执行这条
ENTRYPOINT ["java", "-jar", "app.jar"]
