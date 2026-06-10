# 🎮 mc_recorder

> 全栈 AI 项目: 集成「鸣潮」(Wuthering Waves) 真实游戏数据 + LLM Agent 的个人记录工具

[![Vue 3.5](https://img.shields.io/badge/Vue-3.5-4FC08D?logo=vue.js&logoColor=white)](https://vuejs.org/)
[![Spring Boot 4](https://img.shields.io/badge/Spring_Boot-4.0-6DB33F?logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Java 17](https://img.shields.io/badge/Java-17-007396?logo=java&logoColor=white)](https://www.oracle.com/java/)
[![Python](https://img.shields.io/badge/Python-3.11+-3776AB?logo=python&logoColor=white)](https://www.python.org/)
[![LangChain](https://img.shields.io/badge/LangChain-LangGraph-1C3C3C?logo=langchain&logoColor=white)](https://www.langchain.com/)
[![FastAPI](https://img.shields.io/badge/FastAPI-009688?logo=fastapi&logoColor=white)](https://fastapi.tiangolo.com/)
[![Docker](https://img.shields.io/badge/Docker-2496ED?logo=docker&logoColor=white)](https://www.docker.com/)
[![GitHub Actions](https://img.shields.io/badge/CI%2FCD-GitHub_Actions-2088FF?logo=github-actions&logoColor=white)](https://github.com/features/actions)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

> 🐍 **Phase 5 进行中**: AI 模块从 Spring AI 迁移到 Python LangChain/LangGraph 微服务
> - 当前 `main` 分支 = Python 重构主线 (后端业务功能正常, AI 模块暂为占位)
> - 旧 Spring AI 完整版本归档在 [`archive/spring-ai`](https://github.com/Nijikasuki/mc_recorder/tree/archive/spring-ai) 分支
> - 进度见底部 [Phase 5 Roadmap](#-phase-5-roadmap)

**🌐 在线 demo**: [http://8.145.47.57](http://8.145.47.57)

> 注: 当前线上版本基于 `archive/spring-ai` 分支构建 (Phase 5 完成后才会切到 Python 微服务版)

---

## ✨ 功能亮点

- 🎯 **真实游戏数据接入** — 库街区 API 抓包集成, 一键同步真实鸣潮角色
- 🤖 **AI Agent** — Python LangChain + LangGraph 独立微服务 (Phase 5 重构中) · 完整能力见 [Roadmap](#-phase-5-roadmap)
- 🌊 **流式聊天** — ChatGPT 风格逐字蹦, 多会话侧边栏 + 工具开关
- 🔐 **JWT 认证** — 多用户隔离, 每人独立数据
- 📦 **完整 DevOps** — Docker 多服务编排 + GitHub Actions 自动部署
- 🎨 **现代 UI** — Vue 3 + Element Plus + Tailwind 4, 渐变 / 卡片 / 动效

## 📸 截图

> 截图待添加

## 🛠️ 技术栈

### 后端 (Java 主服务)
| 类别 | 技术 |
|------|------|
| 主框架 | Spring Boot 4.0.6 |
| 安全 | Spring Security + JWT (jjwt 0.12.6) |
| ORM | MyBatis-Plus 3.5.14 |
| 业务数据库 | MySQL 8.0 |
| 缓存 | Redis 7 (Spring Cache 抽象) |
| 消息队列 | RabbitMQ 3 |
| API 文档 | SpringDoc OpenAPI |

### AI 微服务 (Python, Phase 5 进行中)
| 类别 | 技术 |
|------|------|
| 主框架 | Python 3.11 + FastAPI |
| LLM 编排 | LangChain + LangGraph |
| 向量库 | PostgreSQL 16 + pgvector |
| LLM | 智谱 GLM-4-plus + embedding-3 (OpenAI 协议) |
| 通信 | Java 主服务 HTTP/SSE 转发到 Python :8001 |

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

## 🏗️ 架构 (Phase 5 目标态)

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
│   └── AI 网关 ── HTTP/SSE 转发 ─┐         │
└────┬─────────┬─────────┬───────┼─────────┘
     ↓         ↓         ↓       ↓
  MySQL     Redis    RabbitMQ ┌──────────────────────┐
  (业务)   (缓存)   (消息)    │ Python AI 微服务      │
                              │ (FastAPI, 端口 8001)  │
                              │  ├── LangChain        │
                              │  ├── LangGraph 编排    │
                              │  ├── RAG (鸣潮知识库)  │
                              │  └── Tools (业务集成) │
                              └──────────┬───────────┘
                                         ↓
                                      pgvector
                                      (向量)
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

## 🐍 Phase 5 Roadmap

后端 Java 端的 AI 模块迁移到独立 Python 微服务, 主因:
- LangChain/LangGraph 生态在 Python 远比 Java (Spring AI) 成熟
- 拆微服务练习 "跨语言通信 + Docker 编排" 工程实践
- Java 端聚焦业务 CRUD + 网关转发, 职责更清晰

| 节 | 任务 | 状态 |
|----|------|------|
| 1 | Java 端清理 Spring AI 全栈 + AiController 改占位 | ✅ |
| 2 | Python FastAPI 骨架 + Dockerfile | ⏳ |
| 3 | LangChain 基础对话 (GLM-4-plus) | ⏳ |
| 4 | LangChain Memory (Postgres 持久化) | ⏳ |
| 5 | LangChain Tools (Resonator/GitHub/Tavily/Kurobbs) | ⏳ |
| 6 | LangChain RAG (pgvector + Embeddings) | ⏳ |
| 7 | LangGraph 编排 + 工具开关 + 多会话 | ⏳ |
| 8 | Java AiController 改 HTTP 转发 + docker-compose + 上线 | ⏳ |

## 📂 分支说明

| 分支 | 用途 |
|------|------|
| `main` | Phase 5 Python 重构主线 (当前活跃开发) |
| `archive/spring-ai` | Phase 1-4 的 Spring AI 完整版本, 归档保留 (线上 demo 用此分支构建) |

## 🤝 关于这个项目

这是一个**学习 + 作品**两用的全栈项目, 不是教程项目, 是从零搭起的真上线应用:

- Java 端覆盖**主流后端栈** (Spring Boot 4 + Security + MyBatis-Plus + Redis + MQ)
- Python 端用 **LangChain + LangGraph** 做真 Agent (微服务架构)
- 前端从零搭 **Vue 3 + Vite + Element Plus**
- 部署走业界**生产级流程** (Docker + Nginx + ghcr + GitHub Actions + 12-factor)
- 真实接入**鸣潮游戏数据** (而不是 mock)
- AI 助手能用**真实玩家数据**给个性化建议 (Phase 5 完成后)
- 工程亮点: **从单体到微服务**的真实演进 (Phase 1-4 Spring AI 单体 → Phase 5 拆 Python 微服务)

## 📄 License

MIT
