package com.movie.service.Impl;

import com.movie.DTO.RegisterDTO;
import com.movie.mapper.AuthMapper;
import com.movie.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private AuthMapper authMapper;

    // 检查用户名是否重复
    @Override
    public Boolean isUserRegistered(RegisterDTO registerDTO) {
        return authMapper.existsByUsername(registerDTO);
    }

    // 写入用户信息
    @Override
    public void insertUser(RegisterDTO registerDTO) {
        authMapper.insertUser(registerDTO);
    }
}
