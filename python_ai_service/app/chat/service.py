"""聊天业务: 对外接口 (调编译好的 graph)."""
from collections.abc import AsyncIterator

from langchain_core.messages import AIMessageChunk

from app import graph as graph_pkg


async def chat(user_msg: str, thread_id: str, enable_search: bool = False) -> str:
    """非流式: 等 LLM 完整答案返回."""
    config = {
        "configurable": {
            "thread_id": thread_id,
            "enable_search": enable_search,
        }
    }
    result = await graph_pkg.compiled_graph.ainvoke(
        {"messages": [("human", user_msg)]},
        config=config,
    )
    return result["messages"][-1].content


async def chat_stream(user_msg: str, thread_id: str, enable_search: bool = False) -> AsyncIterator[str]:
    """流式: 一个 token 一个 yield."""
    config = {
        "configurable": {
            "thread_id": thread_id,
            "enable_search": enable_search,
        }
    }
    async for chunk, _metadata in graph_pkg.compiled_graph.astream(
        {"messages": [("human", user_msg)]},
        config=config,
        stream_mode="messages",
    ):
        if isinstance(chunk, AIMessageChunk) and chunk.content:
            yield chunk.content
