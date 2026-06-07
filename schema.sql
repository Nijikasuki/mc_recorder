-- ============================================================
-- mc_recorder 完整 schema
-- 用法 (服务器):
--   scp 上传 schema.sql 到服务器 /root/
--   docker exec -i mysql-wuwa mysql -uroot -proot123 wuwa_record < /root/schema.sql
-- ============================================================

-- 用户表
CREATE TABLE IF NOT EXISTS user (
    id         BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    password   VARCHAR(100) NOT NULL COMMENT 'BCrypt 哈希',
    email      VARCHAR(100) COMMENT '注册邮箱',
    created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 共鸣者(角色)表
CREATE TABLE IF NOT EXISTS resonator (
    id                BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    owner_id          BIGINT UNSIGNED NOT NULL COMMENT '归属用户',
    name              VARCHAR(50)     NOT NULL,
    element           VARCHAR(10)     NOT NULL COMMENT '属性',
    star              TINYINT         NOT NULL COMMENT '星级 4/5',
    level             SMALLINT        NOT NULL DEFAULT 1,
    resonance_chain   TINYINT         NOT NULL DEFAULT 0 COMMENT '共鸣链 0-6',
    breach            INT             NULL DEFAULT 0 COMMENT '突破 0-6',
    is_main_role      BOOLEAN         NULL DEFAULT FALSE COMMENT '是否主角',
    weapon_type_name  VARCHAR(50)     NULL COMMENT '武器类型',
    skin_name         VARCHAR(100)    NULL COMMENT '皮肤名',
    skin_pic_url      VARCHAR(500)    NULL COMMENT '皮肤立绘',
    kuro_role_id      INT             NULL COMMENT '库街区角色 ID',
    source            VARCHAR(20)     DEFAULT 'manual' COMMENT 'manual / kurobbs',
    synced_at         DATETIME        NULL,
    role_icon_url     VARCHAR(255)    NULL,
    role_pic_url      VARCHAR(255)    NULL,
    created_at        DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_owner_kuro (owner_id, kuro_role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='鸣潮角色';

-- 库街区 token 表
CREATE TABLE IF NOT EXISTS game_token (
    user_id       BIGINT       NOT NULL PRIMARY KEY,
    bat_token     VARCHAR(255) NOT NULL,
    game_role_id  VARCHAR(50)  NOT NULL COMMENT '鸣潮玩家账号 ID',
    server_id     VARCHAR(100) NOT NULL DEFAULT '76402e5b20be2c39f095a152090afddc',
    bound_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库街区 token 持久化';
