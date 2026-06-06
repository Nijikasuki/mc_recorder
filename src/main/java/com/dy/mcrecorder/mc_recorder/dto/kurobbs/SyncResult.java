package com.dy.mcrecorder.mc_recorder.dto.kurobbs;

import java.time.LocalDateTime;

public record SyncResult(
        int count,                    // 同步了几个角色
        LocalDateTime syncedAt        // 同步时间
) {}