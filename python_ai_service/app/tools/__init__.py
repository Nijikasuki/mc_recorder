"""工具集中注册. 加新工具就在这里 import + append."""
from app.tools.time_tool import get_current_time
from app.tools.search_tool import tavily_search
from app.tools.github_tool import get_repo_activities,get_repo_info

TOOLS = [
    get_current_time,
    tavily_search,
    get_repo_info,
    get_repo_activities,
]
