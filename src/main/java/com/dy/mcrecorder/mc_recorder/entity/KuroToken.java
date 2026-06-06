package com.dy.mcrecorder.mc_recorder.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("game_token")
public class KuroToken {
    @TableId(type = IdType.INPUT)
    private Long userId;

    private String batToken;

    private String gameRoleId;

    private String serverId;

    private LocalDateTime boundAt;

    private LocalDateTime updatedAt;
}
