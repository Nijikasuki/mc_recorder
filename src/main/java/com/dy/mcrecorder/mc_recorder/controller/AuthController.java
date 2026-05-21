package com.dy.mcrecorder.mc_recorder.controller;


import com.dy.mcrecorder.mc_recorder.common.Result;
import com.dy.mcrecorder.mc_recorder.dto.LoginRequest;
import com.dy.mcrecorder.mc_recorder.dto.RegisterRequest;
import com.dy.mcrecorder.mc_recorder.dto.UserResponse;
import com.dy.mcrecorder.mc_recorder.entity.User;
import com.dy.mcrecorder.mc_recorder.mapper.UserMapper;
import com.dy.mcrecorder.mc_recorder.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService service;

    @Autowired
    public AuthController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(registerRequest.getPassword());
        boolean ok =  service.register(user);
        return ok ? Result.success() : Result.fail(400,"用户名已存在");
    }

    @PostMapping("/login")
    public Result<String> login(@Valid @RequestBody LoginRequest loginRequest) {
        User user = new User();
        user.setUsername(loginRequest.getUsername());
        user.setPassword(loginRequest.getPassword());
        String token = service.login(user);
        return token!=null ? Result.success(token):Result.fail(401,"用户名或密码错误");
    }

    @GetMapping("/me")
    public Result<UserResponse> me() {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User dbuser = service.findById(userId);
        UserResponse user = new UserResponse();
        user.setId(dbuser.getId());
        user.setUsername(dbuser.getUsername());
        user.setCreatedAt(dbuser.getCreatedAt());
        return Result.success(user);
    }
}
