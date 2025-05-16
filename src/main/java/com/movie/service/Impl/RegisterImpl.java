package com.movie.service.Impl;

import com.movie.DTO.RegisterDTO;
import com.movie.mapper.UserMapper;
import com.movie.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterImpl implements RegisterService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public void register(RegisterDTO registerDTO) {
        // 检查用户名是否重复
        if (userMapper.existsByUsername(registerDTO.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

    }
}
