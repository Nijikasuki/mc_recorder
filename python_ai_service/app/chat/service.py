"""聊天业务: 对外接口 (调编译好的 graph)."""
from collections.abc import AsyncIterator

from langchain_core.messages import AIMessageChunk

from app import graph as graph_pkg


async def chat(
    user_msg: str,
    thread_id: str,
    enable_search: bool = False,
    enable_knowledge: bool = False,
    user_id: str = "anonymous",
) -> str:
    """非流式: 等 LLM 完整答案返回."""
    config = {
        "configurable": {
            "thread_id": thread_id,
            "enable_search": enable_search,
            "enable_knowledge": enable_knowledge,
            "user_id": user_id,    # ⭐ 业务工具能从 RunnableConfig 拿
        }
    }
    result = await graph_pkg.compiled_graph.ainvoke(
        {"messages": [("human", user_msg)]},
        config=config,
    )
    return result["messages"][-1].content


async def chat_stream(
    user_msg: str,
    thread_id: str,
    enable_search: bool = False,
    enable_knowledge: bool = False,
    user_id: str = "anonymous",
) -> AsyncIterator[tuple[str, str]]:
    """流式: yield (event_type, payload) 元组."""
    config = {
        "configurable": {
            "thread_id": thread_id,
            "enable_search": enable_search,
            "enable_knowledge": enable_knowledge,
            "user_id": user_id,    # ⭐ 业务工具能从 RunnableConfig 拿
        }
    }
    async for mode, chunk in graph_pkg.compiled_graph.astream(
        {"messages": [("human", user_msg)]},
        config=config,
        stream_mode=["messages", "updates"],
    ):
        if mode == "messages":
            # chunk 是 (AIMessageChunk, metadata) 元组
            msg_chunk, _meta = chunk
            if isinstance(msg_chunk, AIMessageChunk) and msg_chunk.content:
                yield ("token", msg_chunk.content)
        elif mode == "updates":
            # chunk 是 dict {"node_name": {state 更新字段}}
            for node_name in chunk:
                yield ("node", node_name)
