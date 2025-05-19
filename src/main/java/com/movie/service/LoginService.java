package com.movie.service;

import com.movie.DTO.LoginDTO;
import com.movie.common.Result;
import jakarta.validation.constraints.NotBlank;

import java.util.Map;

public interface LoginService {
    // 检查用户的登录信息是否正确
    boolean checkUser(LoginDTO loginDTO);
    // 更新用户的登录信息
    void updateLoginInfo(LoginDTO loginDTO);
    // 获取 jwt token
    Map<String, Object> getUserInfo(@NotBlank String username);
    // 刷新token
    Result refreshToken(String refreshToken);
}
