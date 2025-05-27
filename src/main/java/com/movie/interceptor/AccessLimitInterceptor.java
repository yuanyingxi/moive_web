package com.movie.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.common.Result;
import com.movie.common.annotation.AccessLimit;
import com.movie.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.time.Duration;

@Component
public class AccessLimitInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) return true;  // 如果不是方法（比如访问静态资源），直接放行

        HandlerMethod hm = (HandlerMethod) handler;
        AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);  // 从 HandlerMethod 中获取 AccessLimit 注解
        if (accessLimit == null) return true;  // 只有带有 @AccessLimit 注解的方法才会被拦截和处理。其他方法会被直接放行。

        int seconds = accessLimit.seconds();  // 有效时间
        int maxCount = accessLimit.maxCount();  // 最大访问次数
        boolean needLogin = accessLimit.needLogin();  // 是否需要登录

        String key = request.getRequestURI();  // 获取请求的URI路径，用于构建Redis键以存储访问次数。
        if (needLogin) {  // 如果需要登录
            User user = (User) request.getAttribute("user");  // 从请求中获取当前登录用户
            if (user == null) {
                response.setStatus(401);
                response.getWriter().write("请先登录");
                return false;
            }
            key += ":" + user.getId();  // 每个用户单独限流
        } else {  // 如果不需要登录
            String ip = request.getRemoteAddr();  // 获取IP地址
            key += ":" + ip;
        }

        Integer count = (Integer) redisTemplate.opsForValue().get(key);  // 从Redis中获取当前键的访问次数。
        if (count == null) {
            redisTemplate.opsForValue().set(key, 1, Duration.ofSeconds(seconds));  // 如果 count 为 null，表示这是第一次访问该键。
        } else if (count < maxCount) {
            redisTemplate.opsForValue().increment(key);  // 如果 count 小于 maxCount，表示该用户还可以访问该键。
        } else {   // 如果 count 大于等于 maxCount，表示该用户已经达到访问上限。
            sendError(response, Result.error(429, "请求频率过高，请稍后再试"));
            return false;
        }

        return true;
    }

    private void sendError(HttpServletResponse response, Result result) throws IOException {
        response.setStatus(result.getCode());
        response.setCharacterEncoding("UTF-8");
        // 将 Result 对象序列化为 JSON
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
