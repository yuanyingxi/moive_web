package com.movie.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret; // 从配置文件中读取密钥

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // 生成 Access Token（短）
    public String generateAccessToken(String username) {
        return Jwts.builder()
                .setSubject(username)  // 令牌主体（用户唯一标识）
                .setExpiration(new Date(System.currentTimeMillis() + 3600_000)) // 1小时
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)  // 签名算法和密钥
                .compact();  // 生成令牌
    }

    // 生成 Refresh Token（长）
    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 3600_000)) // 7天
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 解析 Token
    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}