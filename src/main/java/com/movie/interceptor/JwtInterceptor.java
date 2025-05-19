//package com.movie.interceptor;
//
//
//import com.movie.common.Result;
//import com.movie.utils.JwtUtil;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.JwtException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.lang.Nullable;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import java.io.IOException;
//
//@Component
//public class JwtInterceptor implements HandlerInterceptor {
//
//    @Autowired
//    private JwtUtil jwtUtil;
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Override
//    public boolean preHandle(HttpServletRequest request,
//                             HttpServletResponse response,
//                             @Nullable Object handler)
//            throws Exception {
//        // Token 提取与验证
//        String header = request.getHeader("Authorization");
//        if (header == null || !header.startsWith("Bearer ")) {
//            sendError(response, Result.error(401, "请求头未携带 Token"));
//            return false;
//        }
//
//        String token = header.substring(7);
//        try {
//            Claims claims = jwtUtil.parseToken(token);
//            request.setAttribute("username", claims.getSubject());
//            return true;
//        } catch (ExpiredJwtException e) {
//            sendError(response, Result.error(401, "Token 已过期"));
//            return false;
//        } catch (JwtException e) {
//            sendError(response, Result.error(401, "无效的 Token"));
//            return false;
//        }
//    }
//
//    private void sendError(HttpServletResponse response, Result result) throws IOException {
//        response.setStatus(result.getCode()); // 设置HTTP状态码
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        // 将 Result 对象序列化为JSON
//        response.getWriter().write(objectMapper.writeValueAsString(result));
//    }
//}