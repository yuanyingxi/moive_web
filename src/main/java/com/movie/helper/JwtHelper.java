package com.movie.helper;

import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.security.auth.message.AuthException;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;


@Component
public class JwtHelper {
    private final SecretKey key;
    private final Duration expiration = Duration.ofHours(2);

    public JwtHelper(@Value("${jwt.secret}") String secret) {
        // 密钥长度验证
        if (secret.length() < 32) {
            throw new IllegalArgumentException("JWT密钥长度必须至少32位");
        }
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public boolean validateToken(String token) {
        try {
            // 复用cleanToken方法处理令牌格式
            String cleanToken = cleanToken(token);

            // 解析验证令牌
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(cleanToken);

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // 捕获所有JWT相关异常
            return false;
        }
    }

    public Long getUserId(String token)
            throws AuthException {
        try {
            return Long.parseLong(
                    Jwts.parserBuilder()
                            .setSigningKey(key)
                            .build()
                            .parseClaimsJws(cleanToken(token))
                            .getBody()
                            .getSubject()
            );
        } catch (JwtException e) {
            throw new AuthException("令牌验证失败", e);
        }
    }

    public String generateToken(Long userId) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .setExpiration(new Date(System.currentTimeMillis() + expiration.toMillis()))
                .signWith(key)
                .compact();
    }

    private String cleanToken(String token) {
        return token.replace("Bearer ", "").trim();
    }
}