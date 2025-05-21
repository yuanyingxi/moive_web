package com.movie.service.Impl;

import com.movie.common.Result;
import com.movie.service.LogoutService;
import com.movie.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LogoutServiceImpl implements LogoutService {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Result logout(HttpServletRequest request) {
        // 直接获取并处理 Token（哈希）（拦截器已保证有效性）
        String token = request.getHeader("Authorization").substring(7);  // 截取 "Bearer " 后面的 Token
        String tokenHash = DigestUtils.sha256Hex(token); // 使用 Apache Commons Codec
        Claims claims = jwtUtil.parseToken(token);

        // 计算剩余有效期（必定 > 0）
        // 注销，将 Token 加入 Redis 黑名单
        long ttl = claims.getExpiration().getTime() - System.currentTimeMillis();
        redisTemplate.opsForValue().set(
                "blacklist:token:" + tokenHash,  // 黑名单 Key
                "blocked",
                ttl,
                TimeUnit.MILLISECONDS
        );

        return Result.success("注销成功");

    }
}
