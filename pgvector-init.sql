-- pgvector 容器首次启动时自动执行 (volume 为空时)
-- 已有数据卷的容器不会再跑, 不影响本地已建好的数据库

-- LangGraph Memory 用的 database (节 4 起用)
CREATE DATABASE mc_ai_memory;

-- wuwa_vectors database 由 POSTGRES_DB 环境变量自动建, 不用在这里写
