"""FastAPI 入口. lifespan 中按顺序初始化 checkpointer → 图."""
from contextlib import asynccontextmanager

from fastapi import FastAPI

from app import graph as graph_pkg
from app.chat.router import router as chat_router
from app.graph.builder import build_graph
from app.memory.checkpointer import init_checkpointer


@asynccontextmanager
async def lifespan(app: FastAPI):
    # 启动顺序: checkpointer → graph → 注入全局
    checkpointer = await init_checkpointer()
    graph_pkg.compiled_graph = build_graph(checkpointer)
    yield
    # 关闭: 暂不做, 节 8 处理优雅关闭


app = FastAPI(
    title="mc-recorder AI Service",
    version="0.1.0",
    description="Python AI microservice (LangChain + LangGraph)",
    lifespan=lifespan,
)
app.include_router(chat_router)


@app.get("/health")
async def health():
    return {"status": "ok", "service": "mc-recorder-ai", "version": "0.1.0"}
