"""工具集中注册. 加新工具就在这里 import + append."""
from app.tools.time_tool import get_current_time
from app.tools.search_tool import tavily_search

TOOLS = [
    get_current_time,
    tavily_search,
]
