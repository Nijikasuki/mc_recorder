from pydantic import BaseModel, Field

class ChatRequest(BaseModel):
    """聊天请求体."""
    message: str = Field(...,min_length=1, max_length=2000, description="用户输入")

    enable_search: bool = Field(default=False,description="联网搜索tool")
    enable_knowledge: bool = Field(default=False,description="知识库RAG")

    conversation_id: str | None = Field(default=None, description="会话 ID")

class ChatResponse(BaseModel):
    content: str = Field(..., description="LLM 完整回答")
    model: str = Field(..., description="使用的模型名 (返回给前端展示用)")