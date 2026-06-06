package com.dy.mcrecorder.mc_recorder.dto.kurobbs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "库街区绑定请求")
public class BindRequest {
    @NotBlank(message = "Token不能为空")
    private String batToken;

    @NotBlank(message = "用户ID不能为空")
    private String gameRoleId;
}
