package com.dy.mcrecorder.mc_recorder.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

// JWT 工具：负责"签发 token"和"解析+验签 token"两件事
// @Component：注册成 Bean，之后 UserService 等可构造注入它
@Component
public class JwtUtil {

    // 服务器密钥：JWT 安全的命根子，只在服务器、绝不下发
    // HS256 要求密钥至少 256 位（32 字节），所以这串故意写长
    // ⚠️ 真实项目密钥要放配置/环境变量，不能硬编码进源码（和 DB 密码同理）
    private static final String SECRET = "mc-recorder-super-secret-key-change-it-in-production-2026";

    // 把字符串密钥转成 jjwt 要的 SecretKey 对象
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

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
