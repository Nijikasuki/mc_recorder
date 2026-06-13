"""联网搜索工具: Tavily 官方 LangChain 集成."""
from langchain_tavily import TavilySearch

from app.config import settings


tavily_search = TavilySearch(
    max_results=3,
    tavily_api_key=settings.tavily_api_key,
    search_depth="basic",
)
