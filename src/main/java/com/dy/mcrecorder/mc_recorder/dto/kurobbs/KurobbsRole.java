package com.dy.mcrecorder.mc_recorder.dto.kurobbs;

/**
 * 鸣潮单个角色信息
 * 字段名跟 API 完全一致, Jackson 自动映射
 */
public record KurobbsRole(
        String acronym,            // "yy" 英文缩写
        Integer attributeId,       // 4
        String attributeName,      // "气动"
        Integer breach,            // 0-6 突破
        Integer chainUnlockNum,    // 0-6 共鸣链
        Boolean isMainRole,        // 是否主角
        Integer level,             // 1-90
        String roleIconUrl,        // 头像
        Integer roleId,            // 1402
        String roleName,           // "秧秧"
        String rolePicUrl,         // 立绘
        KurobbsRoleSkin roleSkin,  // ⚠️ 嵌套 object → 另一个 record
        Integer starLevel,         // 4 / 5
        Integer totalSkillLevel,   // 7-62
        Integer weaponTypeId,      // 1-5
        String weaponTypeName      // "迅刀"
) {}

