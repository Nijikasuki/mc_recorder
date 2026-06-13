"""时间工具: 让 LLM 知道当前时间 (训练数据有截止日期, 它自己不知道)."""
from datetime import datetime
from zoneinfo import ZoneInfo

from langchain_core.tools import tool


@tool
def get_current_time() -> str:
    """获取当前时间 (上海时区).

    当用户问"现在几点"/"今天是几号"/任何涉及当前时间的问题时调用.
    LLM 训练数据有截止日期, 不知道实时时间, 必须用这个工具.

    Returns:
        ISO 格式时间字符串, 含时区, 例如 "2026-06-11T14:30:00+08:00"
    """
    return datetime.now(ZoneInfo("Asia/Shanghai")).isoformat()
