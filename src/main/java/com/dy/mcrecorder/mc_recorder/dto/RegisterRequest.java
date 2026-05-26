package com.dy.mcrecorder.mc_recorder.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "注册请求")
public class RegisterRequest {
    @Schema(description = "用户名（唯一）", example = "tester1")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @Schema(description = "密码，至少 6 位", example = "123456")
    @NotBlank(message = "密码不能为空")
    @Size(min=6,message = "密码至少6位")
    private String password;

    @Schema(description = "邮箱", example = "tester@example.com")
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不对")
    private String email;
}
