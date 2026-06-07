package com.dy.mcrecorder.mc_recorder.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

// JWT 工具：负责"签发 token"和"解析+验签 token"两件事
// @Component：注册成 Bean，之后 UserService 等可构造注入它
@Component
public class JwtUtil {

    // ⭐ 从 application.yaml 的 jwt.secret 读, 外部化避免硬编码泄漏
    @Value("${jwt.secret}")
    private String secret;

    private SecretKey key;

    // 注入完成后初始化 key (因为 @Value 在构造后才生效)
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    // token 有效期：这里设 24 小时（学习方便；真实项目常更短 + 配刷新机制）
    private static final long EXPIRE_MS = 24 * 60 * 60 * 1000L;

    // 签发：把 userId 装进 token，签名，返回 "xxx.yyy.zzz" 字符串
    public String generateToken(Long userId) {
        Date now = new Date();
        return Jwts.builder()
                .subject(String.valueOf(userId))              // 载荷里放 userId（subject 标准字段）
                .issuedAt(now)                                // 签发时间
                .expiration(new Date(now.getTime() + EXPIRE_MS)) // 过期时间
                .signWith(key)                                // 用服务器密钥签名（第三段）
                .compact();                                   // 拼成最终字符串
    }

    // 解析+验签：合法则返回里面的 userId；被篡改/过期/伪造会抛异常（上层处理）
    public Long parseUserId(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)            // 用同一把密钥验签——签名对不上这里就抛异常
                .build()
                .parseSignedClaims(token)   // 解析；过期也会在这抛异常
                .getPayload();              // 拿到载荷
        return Long.valueOf(claims.getSubject());  // 取出当初放进去的 userId
    }
}
