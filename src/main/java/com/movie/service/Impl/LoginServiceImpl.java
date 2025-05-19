package com.movie.service.Impl;

import com.movie.DTO.LoginDTO;
import com.movie.common.Result;
import com.movie.mapper.UserMapper;
import com.movie.service.LoginService;
import com.movie.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtUtil jwtUtils;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean checkUser(LoginDTO loginDTO) {
        return userMapper.existsByUsernameAndPassword(loginDTO);
    }

    @Override
    public void updateLoginInfo(LoginDTO loginDTO) {
        userMapper.updateLastLoginTime(loginDTO.getUsername());
    }

    @Override
    public Map<String, Object> getUserInfo(String username) {
        Map<String, Object> map = new HashMap<>();

        // 生成双 Token
        String accessToken = jwtUtils.generateAccessToken(username);
        String refreshToken = jwtUtils.generateRefreshToken(username);

        // 将长 Token 存入 Redis
        redisTemplate.opsForValue().set(
                "refresh_token_" + username,
                refreshToken,
                7,   // 7 天过期
                TimeUnit.DAYS
        );

        map.put("access_token", accessToken);
        map.put("refresh_token", refreshToken);
        return map;
    }

    // 刷新 Token
    @Override
    public Result refreshToken(String refreshToken) {
        try {
            Claims claims = jwtUtils.parseToken(refreshToken); // 解析 Token 获取 Claims
            String username = claims.getSubject();  // 获取用户名
            String redisRefrexhToken = redisTemplate.opsForValue().get("refresh_token_" + username);  // 从 Redis 获取长 Token

            // 比较两个 Token 是否一致
            if (refreshToken.equals(redisRefrexhToken)) {
                // 刷新 Token
                String NewAccessToken = jwtUtils.generateAccessToken(username);
                return Result.success(Collections.singletonMap("access_token", NewAccessToken));
            } else {
                return Result.error(401, Collections.singletonMap("access_token", "无效的 Token"));
            }
        } catch (ExpiredJwtException e) {
            return Result.error(401, Collections.singletonMap("access_token", "Token 已过期"));
        } catch (JwtException e) {
            return Result.error(401, Collections.singletonMap("access_token", "无效的 Token"));
        }
    }
}
