package com.movie.interceptor;


import com.movie.common.Result;
import com.movie.entity.User;
import com.movie.mapper.PlayMapper;
import com.movie.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private PlayMapper playMapper;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             @Nullable Object handler)
            throws Exception {

        // Token 提取与验证
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            sendError(response, Result.error(401, "请求头未携带 Token"));
            return false;
        }

        String token = header.substring(7);
        String tokenHash = DigestUtils.sha256Hex(token);  // 计算哈希 Token 以便存储到 Redis
        try {
            Claims claims = jwtUtil.parseToken(token);
            String username = claims.getSubject();

            // 黑名单校验 （注销）
            String blacklistKey = "blacklist:token:" + tokenHash;
            if (Boolean.TRUE.equals(redisTemplate.hasKey(blacklistKey))) {
                sendError(response, Result.error(401, "用户已注销"));
                return false;
            }
            // TODO: 将当前 user 存入 request 中, 供后续业务使用
            User user = playMapper.getUserByUsername(username);
            request.setAttribute("user", user);
            return true;
        } catch (ExpiredJwtException e) {
            sendError(response, Result.error(401, "Token 已过期"));
            return false;
        } catch (JwtException e) {
            sendError(response, Result.error(401, "无效的 Token"));
            return false;
        }
    }

    private void sendError(HttpServletResponse response, Result result) throws IOException {
        response.setStatus(result.getCode()); // 设置HTTP状态码
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        // 将 Result 对象序列化为JSON
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}