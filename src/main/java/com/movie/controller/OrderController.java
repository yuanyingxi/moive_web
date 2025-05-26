package com.movie.controller;

import com.movie.entity.User;
import com.movie.entity.Vip;
import com.movie.mapper.VipMapper;
import com.movie.service.OrderService;
import com.movie.interceptor.JwtInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.net.http.HttpRequest;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;
    private final VipMapper vipMapper;
    @Autowired
    public OrderController(OrderService orderService, VipMapper vipMapper) {
        this.orderService = orderService;
        this.vipMapper = vipMapper;
    }

    @GetMapping
    public void OrderVip(HttpServletRequest request, Long vipId){
        Vip vip = vipMapper.selectById(vipId);
        BigDecimal amount = vip.getVipPrice();
        User user = (User) request.getAttribute("user");
        Long userId = user.getId();
        String productName = vip.getVipName();
        orderService.createOrder(userId,productName,amount);
    }
}
