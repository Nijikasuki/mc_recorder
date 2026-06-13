from contextlib import asynccontextmanager

from fastapi import FastAPI

from app.chat.router import router as chat_router
from app.chat.service import init_graph


@asynccontextmanager
async def lifespan(app: FastAPI):
    # 启动时跑一次: 建 checkpointer + 建图 + 自动建 4 张表
    await init_graph()
    yield
    # 关闭时: 暂不优雅关闭连接池, 节 8 再处理


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