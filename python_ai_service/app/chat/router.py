from fastapi import APIRouter
from fastapi.responses import StreamingResponse

from app.chat import service
from app.chat.schemas import ChatRequest, ChatResponse
from app.config import settings

router = APIRouter(prefix="/chat", tags=["chat"])

def _build_thread_id(req: ChatRequest, user_id: str = "anonymous") -> str:
    """Build LangGraph thread_id from user + conversation.

    Format: "u{user_id}-c{conversation_id}"
    确保不同用户的同一 conversation_id 不串.
    """
    cid = req.conversation_id or "default"
    return f"u{user_id}-c{cid}"


@router.post("", response_model=ChatResponse)
async def chat_endpoint(req: ChatRequest) -> ChatResponse:
    """非流式聊天: 等完整答案再返回."""
    thread_id = _build_thread_id(req)
    content = await service.chat(
        user_msg=req.message,
        thread_id=thread_id,
        enable_search=req.enable_search,
        enable_knowledge=req.enable_knowledge,
    )
    return ChatResponse(content=content, model=settings.llm_model)


@router.post("/stream")
async def chat_stream_endpoint(req: ChatRequest):
    """流式聊天: SSE 一边生成一边推."""
    thread_id = _build_thread_id(req)

    async def event_generator():
        async for chunk in service.chat_stream(
            user_msg=req.message,
            thread_id=thread_id,
            enable_search=req.enable_search,
            enable_knowledge=req.enable_knowledge,
        ):
            yield f"data: {chunk}\n\n"

    return StreamingResponse(event_generator(), media_type="text/event-stream")
    # ↑ media_type="text/event-stream" 告诉浏览器/客户端这是 SSE