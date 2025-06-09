package com.movie.controller;

import com.movie.entity.Order;
import com.movie.entity.User;
import com.movie.utils.PayUtil;
import com.movie.common.Result;
import com.movie.entity.PaymentRequest;
import com.movie.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.alipay.api.AlipayApiException;

import java.math.BigDecimal;
import java.util.List;


@Controller
@RequestMapping("/api/payment")
public class AliPayController {
    private final PayUtil payUtil;
    private final OrderService orderService;

    @Autowired
    public AliPayController(PayUtil payUtil, OrderService orderService) {
        this.payUtil = payUtil;
        this.orderService = orderService;
    }

    //FIXME
// 请求需要提供两个参数分别为
// 消费金额 String amount
// 商品名称 String productName
    @ResponseBody
    @PostMapping("/create")
    public Result alipay( @RequestBody PaymentRequest request, HttpServletRequest httpServletRequest)
            throws AlipayApiException {
        //从请求体获得参数
        String token = request.getAuthorization();
        String amount = request.getAmount();
        String productName = request.getProductName();

        User user = (User) httpServletRequest.getAttribute("user");

        // 1. 获取当前用户ID（需要实现用户认证）
        Long userId = user.getId();

        BigDecimal countAmount = new BigDecimal(amount);

        // 2. 创建订单记录
        orderService.createOrder(userId,productName,countAmount);

        // 2. 选择订单记录
        Order order = orderService.selectOrder(userId, countAmount);

        // 3. 调用支付宝接口
        return Result.success(payUtil.sendRequestToAlipay(
                order.getOrderId(),
                order.getAmount().toString(),
                order.getProductName()
        ));
    }

    @RequestMapping("/toSuccess")
    public Result successPay() {
        return Result.success();
    }

}


