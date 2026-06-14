package com.dy.mcrecorder.mc_recorder.controller;

import com.dy.mcrecorder.mc_recorder.entity.Resonator;
import com.dy.mcrecorder.mc_recorder.service.ResonatorService;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * 内部接口: 给 Python AI 微服务的 LLM 工具调用.
 *
 * 不走 JWT 认证 (Python 不持有用户 token), 改用 X-Internal-Token 共享密钥校验.
 * SecurityConfig 把 /api/internal/** permitAll, 校验逻辑在本 controller 手写.
 */
@RestController
@RequestMapping("/api/internal")
@Hidden    // 不在 Swagger UI 显示, 只给内部用
public class InternalController {

    private final String internalToken;
    private final ResonatorService resonatorService;

    public InternalController(@Value("${internal.token}") String internalToken, ResonatorService resonatorService) {
        this.internalToken = internalToken;
        this.resonatorService = resonatorService;
    }

    /**
     * 查指定用户的角色列表.
     * 调用方: Python AI 微服务的 @tool get_my_resonators
     */
    @GetMapping("/resonators/{userId}")
    public List<Resonator> getResonatorsByUser(@PathVariable Long userId, @RequestHeader(value = "X-Internal-Token", required = false) String token) {
        if (token == null || !internalToken.equals(token)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "无效的内部 token");
        }
        return resonatorService.findAll(userId);
    }
}
