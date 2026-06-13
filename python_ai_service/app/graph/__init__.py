"""LangGraph 图模块.

compiled_graph 启动后由 main.py 的 lifespan 赋值. 业务模块通过
`from app import graph as graph_pkg` + `graph_pkg.compiled_graph` 访问.
"""
compiled_graph = None
