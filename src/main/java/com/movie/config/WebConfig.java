package com.movie.config;

import com.movie.interceptor.AccessLimitInterceptor;
import com.movie.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;
    @Autowired
    private AccessLimitInterceptor accessLimitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/**")          // 拦截所有API路径
                .excludePathPatterns(
                        "/api/user/login",
                        "/api/user/register",
                        "/api/user/refresh",
                        "/api/excel/import",
                        "/api/user/test",
                        "/api/report/ranking"
                ); // 排除登录和刷新接口

        registry.addInterceptor(accessLimitInterceptor)
                .addPathPatterns("/api/**");         // 拦截所有API路径
    }
}