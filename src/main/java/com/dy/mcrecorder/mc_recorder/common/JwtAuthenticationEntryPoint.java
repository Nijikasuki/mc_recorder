package com.dy.mcrecorder.mc_recorder.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

// 认证失败入口：Spring Security 遇到"未认证访问受保护接口"时调用 commence
// 作用：把默认的空 403 换成项目统一的 Result 信封（和手写 JwtInterceptor.writeUnauthorized 同套路）
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);   // 统一信封风格：HTTP 200，结果看 body.code
        Result<Void> body = Result.fail(401, "未登录或 token 无效");
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
