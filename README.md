# 🎮 mc_recorder

> 全栈 AI 项目: 集成「鸣潮」(Wuthering Waves) 真实游戏数据 + LLM Agent 的个人记录工具

[![Vue 3.5](https://img.shields.io/badge/Vue-3.5-4FC08D?logo=vue.js&logoColor=white)](https://vuejs.org/)
[![Spring Boot 4](https://img.shields.io/badge/Spring_Boot-4.0-6DB33F?logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Java 17](https://img.shields.io/badge/Java-17-007396?logo=java&logoColor=white)](https://www.oracle.com/java/)
[![Spring AI](https://img.shields.io/badge/Spring_AI-2.0_M8-6DB33F?logo=spring&logoColor=white)](https://spring.io/projects/spring-ai)
[![Docker](https://img.shields.io/badge/Docker-2496ED?logo=docker&logoColor=white)](https://www.docker.com/)
[![GitHub Actions](https://img.shields.io/badge/CI%2FCD-GitHub_Actions-2088FF?logo=github-actions&logoColor=white)](https://github.com/features/actions)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

**🌐 在线 demo**: [http://8.145.47.57](http://8.145.47.57)

---

## ✨ 功能亮点

- 🎯 **真实游戏数据接入** — 库街区 API 抓包集成, 一键同步真实鸣潮角色
- 🤖 **AI Agent** — RAG (鸣潮百科) + 5 个工具 (角色查询/GitHub/搜索/...) + 多轮对话, 智谱 GLM-4-plus
- 🌊 **流式聊天** — ChatGPT 风格逐字蹦
- 🔐 **JWT 认证** — 多用户隔离, 每人独立数据
- 📦 **完整 DevOps** — Docker 多服务编排 + GitHub Actions 自动部署
- 🎨 **现代 UI** — Vue 3 + Element Plus + Tailwind 4, 渐变 / 卡片 / 动效

## 📸 截图

> 截图待添加

## 🛠️ 技术栈

### 后端
| 类别 | 技术 |
|------|------|
| 框架 | Spring Boot 4.0.6 / Spring AI 2.0.0-M8 |
| 安全 | Spring Security + JWT (jjwt 0.12.6) |
| ORM | MyBatis-Plus 3.5.14 |
| 业务数据库 | MySQL 8.0 |
| 向量库 | PostgreSQL 16 + pgvector |
| 缓存 | Redis 7 (Spring Cache 抽象) |
| 消息队列 | RabbitMQ 3 |
| API 文档 | SpringDoc OpenAPI |
| AI | 智谱 GLM-4-plus + embedding-3 (OpenAI 协议) |

### 前端
| 类别 | 技术 |
|------|------|
| 框架 | Vue 3.5 + Vite 8 |
| 状态 | Pinia + vue-router |
| UI | Element Plus + Tailwind CSS 4 |
| 网络 | axios (拦截器 + JWT) |
| 流式 | fetch + SSE + 假流式打字效果 |

### DevOps
| 类别 | 技术 |
|------|------|
| 容器 | Docker (多阶段构建) |
| 编排 | docker-compose (6 服务) |
| 反代 | Nginx (SPA 路由 + SSE 配置) |
| 镜像仓库 | GitHub Container Registry (ghcr.io) |
| CI/CD | GitHub Actions |
| 服务器 | 阿里云轻量 (Ubuntu 22.04) |
| 配置 | 12-factor app (env 外部化) |

## 🏗️ 架构

```
┌─────────────────────────────────────────┐
│  用户浏览器                              │
└────────────────┬────────────────────────┘
                 ↓ HTTPS
┌─────────────────────────────────────────┐
│  Nginx (前端容器, 端口 80)               │
│   /api/* → backend                       │
│   /*     → Vue 静态文件                  │
└────────────────┬────────────────────────┘
                 ↓
┌─────────────────────────────────────────┐
│  Spring Boot 后端 (端口 8000)            │
│   ├── 认证 (JWT)                         │
│   ├── 业务 CRUD                          │
│   ├── 库街区集成                         │
│   └── AI Agent (Memory + RAG + 5 Tools)  │
└────┬────────┬────────┬────────┬─────────┘
     ↓        ↓        ↓        ↓
  MySQL    pgvector  Redis  RabbitMQ
  (业务)   (向量)    (缓存) (消息)
```

## 🚀 快速开始

### 在线体验

直接访问: [http://8.145.47.57](http://8.145.47.57)
> 注册暂关闭, 联系作者开通账号体验

### 本地运行

```bash
# 1. clone 代码
git clone https://github.com/Nijikasuki/mc_recorder.git
cd mc_recorder

# 2. 准备 .env (智谱 / GitHub PAT / Tavily key)
cp .env.example .env
# 编辑 .env 填真实 key

# 3. 一键启动全栈
docker compose up -d

# 4. 浏览器访问
open http://localhost
```

### 开发模式

```bash
# 后端 (需 Java 17 + Maven)
./mvnw spring-boot:run

# 前端
cd mc_recorder_web
npm install
npm run dev
```

## 📚 文档

- **API 文档**: [Swagger UI](http://8.145.47.57:8000/swagger-ui.html)
- **库街区集成**: 抓 Android App 的 `b-at` token, 通过表单提交绑定
- **CI/CD 流程**: push 到 main → 自动 build + push ghcr → SSH 服务器 + docker compose pull/up

## 🤝 关于这个项目

这是一个**学习 + 作品**两用的全栈项目, 不是教程项目, 是从零搭起的真上线应用:

- 后端覆盖 Java 后端**主流栈** (Spring Boot 4 + Security + MyBatis-Plus + Redis + MQ + AI)
- 前端从零搭 **Vue 3 + Vite + Element Plus**
- 部署走业界**生产级流程** (Docker + Nginx + ghcr + GitHub Actions + 12-factor)
- 真实接入**鸣潮游戏数据** (而不是 mock)
- AI 助手能用**真实玩家数据**给个性化建议

## 📄 License

MIT
