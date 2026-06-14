# ============================================================
# Python AI Microservice Dockerfile
#
# 多阶段构建:
#   builder 阶段: 用 uv 装依赖到 .venv
#   runtime 阶段: 只保留 Python + .venv + 源代码, 体积最小
#
# 用法 (本地):
#   cd python_ai_service
#   docker build -t mc-ai:latest .
# ============================================================

# === 1 阶段: builder, 装依赖 ===
FROM ghcr.io/astral-sh/uv:python3.11-bookworm-slim AS builder

WORKDIR /app

# 先只复制依赖描述文件, 利用 Docker layer 缓存:
# pyproject.toml / uv.lock 不变时, 这一层不重新装依赖, build 飞快
COPY pyproject.toml uv.lock ./

# 装依赖 (但不装项目本身, 等下面 COPY app 后再装)
# --frozen: 严格按 uv.lock 的精确版本, 不解析新版本
# --no-dev: 跳过 dev 依赖 (pytest 等, 生产不需要)
# --no-install-project: 只装依赖, 不装当前项目 (此时 app/ 还没复制进来)
RUN uv sync --frozen --no-dev --no-install-project

# 复制源代码 + RAG 知识库 (运行时需要 wuwa.md)
COPY app/ ./app/
COPY knowledge/ ./knowledge/

# 装项目本身 (此时 app/ 已经在了)
RUN uv sync --frozen --no-dev


# === 2 阶段: runtime, 最小化 ===
FROM python:3.11-slim-bookworm AS runtime

WORKDIR /app

# 从 builder 阶段复制装好的 .venv + 源代码 + 知识库
# builder 阶段的 uv 工具自己不需要带到 runtime, 节省体积
COPY --from=builder /app /app

# 让 Python 默认走 .venv 里的解释器和包
ENV PATH="/app/.venv/bin:$PATH"

# 内部监听 8001 (跟 Java 8000 错开)
EXPOSE 8001

# 启动: 0.0.0.0 让容器外能访问, 不用 --reload (生产)
CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8001"]
