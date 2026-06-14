from fastapi import APIRouter, Header
from fastapi.responses import StreamingResponse

from app.chat import service
from app.chat.schemas import ChatRequest, ChatResponse
from app.config import settings

router = APIRouter(prefix="/chat", tags=["chat"])


def _build_thread_id(req: ChatRequest, user_id: str) -> str:
    """Build LangGraph thread_id from user + conversation.

    Format: "u{user_id}-c{conversation_id}"
    确保不同用户的同一 conversation_id 不串.

    user_id 来源: Java 网关解析 JWT 后通过 X-User-Id header 透传;
    本地直连测试 (无 Java 网关) 时默认 "anonymous".
    """
    cid = req.conversation_id or "default"
    return f"u{user_id}-c{cid}"


@router.post("", response_model=ChatResponse)
async def chat_endpoint(
    req: ChatRequest,
    x_user_id: str = Header(default="anonymous", alias="X-User-Id"),
) -> ChatResponse:
    """非流式聊天: 等完整答案再返回."""
    thread_id = _build_thread_id(req, user_id=x_user_id)
    content = await service.chat(
        user_msg=req.message,
        thread_id=thread_id,
        enable_search=req.enable_search,
        enable_knowledge=req.enable_knowledge,
        user_id=x_user_id,
    )
    return ChatResponse(content=content, model=settings.llm_model)


@router.post("/stream")
async def chat_stream_endpoint(
    req: ChatRequest,
    x_user_id: str = Header(default="anonymous", alias="X-User-Id"),
):
    """流式聊天: SSE 一边生成一边推."""
    thread_id = _build_thread_id(req, user_id=x_user_id)

    async def event_generator():
        async for event_type, payload in service.chat_stream(
            user_msg=req.message,
            thread_id=thread_id,
            enable_search=req.enable_search,
            enable_knowledge=req.enable_knowledge,
            user_id=x_user_id,
        ):
            yield f"event: {event_type}\ndata: {payload}\n\n"
        # 流结束信号
        yield "event: done\ndata: [DONE]\n\n"

    return StreamingResponse(
        event_generator(),
        media_type="text/event-stream; charset=utf-8",
    )
