package com.dy.mcrecorder.mc_recorder.common;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

// Spring Security 的自定义 JWT 过滤器，替代手写的 JwtInterceptor
// 职责：验 token → 把"你是谁"登记进 SecurityContext。它【不】决定放行/拦截
// extends OncePerRequestFilter：Spring 提供的过滤器基类，保证每个请求只过一次
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 取 Authorization: Bearer xxx
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                Long userId = jwtUtil.parseUserId(token);   // 验签，合法则拿到 userId

                // 造一个 Spring Security 的"已认证"凭证对象
                //   参数：principal(谁=userId)、credentials(密码=null,已用JWT验过)、authorities(权限=暂空)
                //   用这个三参构造 → 该对象被标记为"已认证"
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());

                // 把身份登记进 SecurityContext —— 这是过滤器的核心动作
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                // token 无效/过期：不登记身份。不拦、不抛——后续授权规则会因"无身份"而拒
            }
        }
        // 不管认没认证成功，都要放行到过滤器链下一环（拦不拦由授权规则决定，不是这里）
        filterChain.doFilter(request, response);
    }
}
