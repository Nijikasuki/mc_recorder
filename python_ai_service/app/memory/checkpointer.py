"""Postgres checkpointer: LangGraph chat memory 持久化."""
from langgraph.checkpoint.postgres.aio import AsyncPostgresSaver

from app.config import settings


_checkpointer_cm = None


async def init_checkpointer():
    """启动时调用一次: 建连接池 + 自动建表. 返回 checkpointer 实例."""
    global _checkpointer_cm
    _checkpointer_cm = AsyncPostgresSaver.from_conn_string(settings.postgres_url)
    checkpointer = await _checkpointer_cm.__aenter__()
    await checkpointer.setup()
    return checkpointer
