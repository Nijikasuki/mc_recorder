"""RAG 向量库: embedding 实例 + PGVector 实例 + 检索函数."""
from langchain_openai import OpenAIEmbeddings
from langchain_postgres import PGVector

from app.config import settings


embeddings = OpenAIEmbeddings(
    model=settings.embedding_model,
    api_key=settings.llm_api_key,
    base_url=settings.llm_base_url,
)

vector_store = PGVector(
    embeddings=embeddings,
    collection_name="wuwa_knowledge",
    connection=settings.rag_pg_url,
    use_jsonb=True,
    async_mode=True,  # 启用异步 engine, 才能用 aadd_documents / asimilarity_search
)


async def retrieve_docs(query: str, k: int = 3) -> list[str]:
    """检索最相关的 k 条文档片段, 返回纯文本列表."""
    results = await vector_store.asimilarity_search(query, k=k)
    return [doc.page_content for doc in results]
