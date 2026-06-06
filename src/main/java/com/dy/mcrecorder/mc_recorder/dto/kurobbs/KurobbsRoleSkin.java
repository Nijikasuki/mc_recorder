package com.dy.mcrecorder.mc_recorder.dto.kurobbs;

public record KurobbsRoleSkin(
        Boolean isAddition,        // 是否追加
        String picUrl,             // 皮肤大图
        Integer priority,          // 优先级
        Integer quality,           // 品质 ID
        String qualityName,        // "初始服饰" / "高级定制"
        Integer roleId,            // 1402 (跟外层重复, 但全映射)
        String skinIcon,           // 皮肤图标
        Integer skinId,            // 81001402
        String skinName            // "裁羽行歌"
) {}