package com.movie.controller;

import com.movie.DTO.LoginDTO;
import com.movie.DTO.RegisterDTO;
import com.movie.common.Result;
import com.movie.service.LoginService;
import com.movie.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class LoginController {
    @Autowired
    private RegisterService registerService;
    @Autowired
    private LoginService loginService;

    @PostMapping("register")
    public Result register(@RequestBody RegisterDTO registerDTO) {
        registerService.register(registerDTO);
        return Result.success("注册成功");
    }

    @PostMapping("login")
    public String login(@RequestBody LoginDTO loginDTO) {
        return "登录成功";
    }

}
