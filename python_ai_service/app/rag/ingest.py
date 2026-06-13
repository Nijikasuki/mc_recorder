"""灌库脚本: 读 wuwa.md → 切片 → embed → 存 pgvector.

用法:
    uv run python -m app.rag.ingest               # 幂等灌库 (重复跑不增重复条目)
    uv run python -m app.rag.ingest --truncate    # 先清空 collection 再灌 (大改时用)

幂等原理: 每个 chunk 的 ID = SHA256(内容), 相同内容 → 相同 ID → PGVector 自动 upsert.
"""
import argparse
import asyncio
import hashlib
import sys
from pathlib import Path

# Windows 上 psycopg async 要求 SelectorEventLoop (默认 ProactorEventLoop 不兼容)
if sys.platform == "win32":
    asyncio.set_event_loop_policy(asyncio.WindowsSelectorEventLoopPolicy())

from langchain_text_splitters import MarkdownTextSplitter

from app.rag.vector_store import vector_store


KNOWLEDGE_DIR = Path(__file__).parent.parent.parent / "knowledge"
WUWA_MD = KNOWLEDGE_DIR / "wuwa.md"


async def main(truncate: bool = False) -> None:
    print(f"📖 读取 {WUWA_MD}")
    text = WUWA_MD.read_text(encoding="utf-8")
    print(f"   原文 {len(text)} 字符")

    print("✂️ 切片 (chunk_size=1000, overlap=100, 按 Markdown 标题智能切)...")
    splitter = MarkdownTextSplitter(chunk_size=1000, chunk_overlap=100)
    docs = splitter.create_documents([text])
    print(f"   得到 {len(docs)} 个 chunk")

    # 内容 hash 作 ID, 实现 upsert 幂等
    ids = [hashlib.sha256(doc.page_content.encode("utf-8")).hexdigest() for doc in docs]

    if truncate:
        print("🗑️ --truncate 模式: 清空 wuwa_knowledge collection...")
        await vector_store.adelete_collection()

    print(f"🧠 调智谱 embedding-3 转向量 + upsert 到 pgvector (ID 用内容 hash)...")
    await vector_store.aadd_documents(docs, ids=ids)

    print(f"✅ 灌库完成: {len(docs)} 个 chunk → collection 'wuwa_knowledge'")
    if not truncate:
        print("   (重复跑同样文档不会增重复条目, 想完全清空重灌请加 --truncate)")


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="灌 wuwa.md 到 pgvector 向量库")
    parser.add_argument(
        "--truncate",
        action="store_true",
        help="先清空整个 collection 再灌 (大改文档时用)",
    )
    args = parser.parse_args()
    asyncio.run(main(truncate=args.truncate))
