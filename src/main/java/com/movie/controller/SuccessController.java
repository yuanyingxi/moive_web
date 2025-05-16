package com.movie.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SuccessController {
    @RequestMapping("/api/alipay/toSuccess")
    public String index() {
        return "Success";
    }
}
