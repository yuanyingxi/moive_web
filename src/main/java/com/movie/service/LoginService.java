package com.movie.service;

import com.movie.DTO.LoginDTO;
import com.movie.common.Result;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public interface LoginService {
    boolean checkUser(LoginDTO loginDTO);

    void updateLoginInfo(LoginDTO loginDTO);

    Map<String, Object> getUserInfo(String username);

    // 刷新 Token
    Result refreshToken(String refreshToken);
}
