package com.dy.mcrecorder.mc_recorder.controller;


import com.dy.mcrecorder.mc_recorder.common.Result;
import com.dy.mcrecorder.mc_recorder.dto.LoginRequest;
import com.dy.mcrecorder.mc_recorder.dto.RegisterRequest;
import com.dy.mcrecorder.mc_recorder.entity.User;
import com.dy.mcrecorder.mc_recorder.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
