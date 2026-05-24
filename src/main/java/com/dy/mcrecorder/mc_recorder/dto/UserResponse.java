package com.dy.mcrecorder.mc_recorder.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "用户信息响应（不含密码）")
public class UserResponse {
    @Schema(description = "用户 ID")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "注册时间")
    private LocalDateTime createdAt;
}
