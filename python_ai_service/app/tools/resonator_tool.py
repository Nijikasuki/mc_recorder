"""鸣潮角色查询工具: 调 Java 后端 internal 接口拿当前用户的角色列表."""
import httpx
from langchain_core.runnables import RunnableConfig
from langchain_core.tools import tool

from app.config import settings


@tool
async def get_my_resonators(config: RunnableConfig) -> str:
    """查询当前用户拥有的鸣潮角色列表 (从用户的业务数据库).

    适用场景: 用户问"我有哪些角色"/"我拥有什么角色"/"推荐我培养谁"/
              "我的鸣潮账号"等涉及"用户自己的角色数据"的问题时调用.

    注意: 这是用户私有数据, 不要混淆百科类查询 (那个用 RAG 知识库).

    Returns:
        Markdown 格式的角色列表 (含名字 / 星级 / 元素 / 武器 / 等级 / 共鸣链)
    """
    # config 是 RunnableConfig 类型, LangChain 看到自动注入, LLM 看不到这个参数
    configurable = config.get("configurable", {})
    user_id = configurable.get("user_id", "anonymous")

    if user_id == "anonymous":
        return "未登录用户, 无法查询角色 (请登录后再问)"

    # 调 Java internal endpoint
    url = f"{settings.java_base_url}/api/internal/resonators/{user_id}"
    headers = {"X-Internal-Token": settings.internal_token}

    async with httpx.AsyncClient(timeout=10.0) as client:
        response = await client.get(url, headers=headers)

    # 状态码处理
    if response.status_code == 403:
        return "内部 token 配置错误, Java 端拒绝访问 (检查 Python .env 和 Java application.yaml 的 INTERNAL_TOKEN 是否一致)"
    if response.status_code != 200:
        return f"Java 业务接口错误: HTTP {response.status_code}"

    data = response.json()
    if not data:
        return f"用户 {user_id} 暂无角色 (建议先在前端绑定库街区账号同步)"

    # 格式化成 Markdown
    lines = [f"# 用户 {user_id} 的鸣潮角色 (共 {len(data)} 个)\n"]
    for r in data:
        lines.append(
            f"- **{r['name']}** ({r['star']}★)"
            f" - {r['element']}属性 / {r['weaponTypeName']} / Lv.{r['level']}"
            f" / 共鸣链 {r['resonanceChain']}/6"
        )
    return "\n".join(lines)
