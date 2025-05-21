package com.movie.service;

import com.movie.DTO.LoginDTO;
import com.movie.common.Result;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface LoginService {
    boolean checkUser(LoginDTO loginDTO);

    void updateLoginInfo(LoginDTO loginDTO);

    Map<String, Object> getUserInfo(String username);

    // 刷新 Token
    Result refreshToken(String refreshToken);
}
