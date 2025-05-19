package com.movie.service;


import com.movie.DTO.RegisterDTO;

public interface RegisterService {
    // 检查用户名是否已被注册
    Boolean isUserRegistered(RegisterDTO registerDTO);

    // 注册用户
    void insertUser(RegisterDTO registerDTO);
}
