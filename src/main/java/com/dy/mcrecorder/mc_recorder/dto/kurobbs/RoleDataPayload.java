package com.dy.mcrecorder.mc_recorder.dto.kurobbs;

import java.util.List;

/**
 * 库街区 data 字段反序列化后的真实结构
 * 对应 JSON: { "roleList": [...], "showToGuest": true }
 */
public record RoleDataPayload(
        List<KurobbsRole> roleList,
        Boolean showToGuest
) {}