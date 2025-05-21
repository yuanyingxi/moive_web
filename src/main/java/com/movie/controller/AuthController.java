package com.movie.controller;

import com.movie.DTO.LoginDTO;
import com.movie.DTO.RegisterDTO;
import com.movie.common.Result;
import com.movie.service.LoginService;
import com.movie.service.LogoutService;
import com.movie.service.RegisterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class AuthController {
    @Autowired
    private RegisterService registerService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private LogoutService logoutService;

    // 注册接口
    @PostMapping("/register")
    public Result register(@RequestBody @Valid RegisterDTO registerDTO) {
        // 判断用户名是否已存在
        if (!registerService.isUserRegistered(registerDTO)) {
            registerService.insertUser(registerDTO);  // 注册用户
            return Result.success("注册成功!");
        }
        return Result.error("该用户名已存在!");
    }

    // 登录接口
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

    // 刷新 token 接口
    @PostMapping("/refresh")
    public Result refreshToken(@RequestBody Map<String, String> request) {
        // 尝试刷新 token
        return loginService.refreshToken(request.get("refresh_token"));
    }

    // 注销接口
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request) {
        return logoutService.logout(request);  // 注销登录
    }

    @GetMapping("/test")
    public Result test() {
        return Result.success(417);
    }
}
