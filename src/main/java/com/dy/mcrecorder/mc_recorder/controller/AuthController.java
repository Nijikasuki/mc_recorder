package com.dy.mcrecorder.mc_recorder.controller;


import com.dy.mcrecorder.mc_recorder.common.Result;
import com.dy.mcrecorder.mc_recorder.dto.LoginRequest;
import com.dy.mcrecorder.mc_recorder.dto.RegisterRequest;
import com.dy.mcrecorder.mc_recorder.dto.UserResponse;
import com.dy.mcrecorder.mc_recorder.entity.User;
import com.dy.mcrecorder.mc_recorder.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "1. 用户认证", description = "注册、登录、当前用户")
public class AuthController {
    private final UserService service;

    public AuthController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    @Operation(summary = "注册账号", description = "用户名唯一；密码至少 6 位")
    @SecurityRequirements   // 空 @SecurityRequirements = 覆盖全局 bearerAuth，这个接口无需登录
    public Result<Void> register(@Valid @RequestBody RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(registerRequest.getPassword());
        boolean ok =  service.register(user);
        return ok ? Result.success() : Result.fail(400,"用户名已存在");
    }

    @PostMapping("/login")
    @Operation(summary = "登录", description = "成功时 token 在响应的 data 字段")
    @SecurityRequirements
    public Result<String> login(@Valid @RequestBody LoginRequest loginRequest) {
        User user = new User();
        user.setUsername(loginRequest.getUsername());
        user.setPassword(loginRequest.getPassword());
        String token = service.login(user);
        return token!=null ? Result.success(token):Result.fail(401,"用户名或密码错误");
    }

    @GetMapping("/me")
    @Operation(summary = "当前登录用户信息", description = "从 token 解析出 userId，返回用户基本信息（不含密码）")
    public Result<UserResponse> me(@AuthenticationPrincipal Long userId) {
        User dbuser = service.findById(userId);
        UserResponse user = new UserResponse();
        user.setId(dbuser.getId());
        user.setUsername(dbuser.getUsername());
        user.setCreatedAt(dbuser.getCreatedAt());
        return Result.success(user);
    }
}
