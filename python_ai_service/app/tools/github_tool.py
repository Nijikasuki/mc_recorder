"""GitHub 仓库查询工具."""
import httpx
from langchain_core.tools import tool

from app.config import settings


# ============ 共用辅助 ============

def _headers() -> dict[str, str]:
    return {
        "Authorization": f"Bearer {settings.github_token}",
        "Accept": "application/vnd.github+json",
    }


def _check_error(response: httpx.Response, owner: str, repo: str) -> str | None:
    """状态码错误返回错误描述字符串, 正常返回 None."""
    if response.status_code == 404:
        return f"仓库 {owner}/{repo} 不存在或为私有"
    if response.status_code == 401:
        return "GitHub token 无效, 请检查 .env 配置"
    if response.status_code != 200:
        return f"GitHub API 错误: HTTP {response.status_code}"
    return None


# ============ 工具 ============

@tool
async def get_repo_info(owner: str, repo: str) -> str:
    """查询 GitHub 仓库的基本信息 (star 数 / fork 数 / 主要语言 / 描述 / 默认分支等).

    适用场景: 用户问"某个仓库怎么样" / "多少 star" / "什么语言写的" 等问题时调用.

    Args:
        owner: 仓库所有者的 GitHub 用户名或组织名 (如 'spring-projects', 'vuejs')
        repo: 仓库名 (如 'spring-ai', 'core')

    Returns:
        Markdown 格式的仓库信息字符串.
    """
    url = f"https://api.github.com/repos/{owner}/{repo}"
    async with httpx.AsyncClient(timeout=10.0) as client:
        response = await client.get(url, headers=_headers())

    err = _check_error(response, owner, repo)
    if err:
        return err

    data = response.json()
    return f"""# {data['full_name']}

📝 描述: {data.get('description') or '无'}
⭐ Star: {data['stargazers_count']:,}
🍴 Fork: {data['forks_count']:,}
🐛 Open Issues: {data['open_issues_count']:,}
💻 主要语言: {data.get('language') or '未指定'}
🏷️ 标签: {', '.join(data.get('topics') or []) or '无'}
🌿 默认分支: {data['default_branch']}
📅 创建: {data['created_at']}
🕐 最近推送: {data['pushed_at']}
🔗 {data['html_url']}
"""


@tool
async def get_repo_activities(owner: str, repo: str) -> str:
    """查询 GitHub 仓库最近的活动历史 (推送 / 合并 / 分支创建/删除等事件).

    适用场景: 用户问"最近这个项目在搞什么" / "最新动态" / "最近 commit" 等问题时调用.

    Args:
        owner: 仓库所有者
        repo: 仓库名

    Returns:
        最近 5 条活动记录的 Markdown 字符串, 每条含时间 / 类型 / 操作人 / 相关分支.
    """
    url = f"https://api.github.com/repos/{owner}/{repo}/activity"
    async with httpx.AsyncClient(timeout=10.0) as client:
        response = await client.get(url, headers=_headers())

    err = _check_error(response, owner, repo)
    if err:
        return err

    data = response.json()  # list
    if not data:
        return f"仓库 {owner}/{repo} 暂无活动记录"

    recent = data[:5]
    lines = [f"# {owner}/{repo} 最近 {len(recent)} 条活动\n"]
    for item in recent:
        actor = item.get("actor") or {}
        lines.append(
            f"- `{item['timestamp']}` | **{item['activity_type']}** | "
            f"@{actor.get('login', 'unknown')} | `{item.get('ref', '-')}`"
        )
    return "\n".join(lines)
