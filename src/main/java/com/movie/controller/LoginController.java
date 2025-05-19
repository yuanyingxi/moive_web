package com.movie.controller;

import com.movie.DTO.LoginDTO;
import com.movie.DTO.RegisterDTO;
import com.movie.common.Result;
import com.movie.service.LoginService;
import com.movie.service.RegisterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class LoginController {
    @Autowired
    private RegisterService registerService;
    @Autowired
    private LoginService loginService;

    @PostMapping("/register")
    public Result register(@RequestBody @Valid RegisterDTO registerDTO) {
        // 判断用户名是否已存在
        if (!registerService.isUserRegistered(registerDTO)) {
            registerService.insertUser(registerDTO);  // 注册用户
            return Result.success("注册成功!");
        }
        return Result.error("该用户名已存在!");
    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginDTO loginDTO) {
        // 判断用户名和密码是否正确
        if (loginService.checkUser(loginDTO)) {
            loginService.updateLoginInfo(loginDTO);  // 更新登录信息
            Map<String, Object> map = loginService.getUserInfo(loginDTO.getUsername());  // 获取用户包装信息

            return Result.success(map);
        }

        return Result.error("用户名或密码错误!");
    }

    @PostMapping("/refresh")
    public Result refreshToken(@RequestBody Map<String, String> request) {
        // 尝试刷新 token
        return loginService.refreshToken(request.get("refresh_token"));
    }

    @GetMapping("/test")
    public Result test() {
        return Result.success(417);
    }
}
