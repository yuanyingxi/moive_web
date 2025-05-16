package com.movie.controller;

import com.movie.entity.Order;
import com.movie.utils.PayUtil;
import com.movie.entity.PaymentRequest;
import com.movie.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.alipay.api.AlipayApiException;

import java.math.BigDecimal;


@Controller
@RequestMapping("/api/payment")
public class AliPayController {
    private final PayUtil payUtil;
    private final OrderService orderService;

    @Autowired
    public AliPayController(PayUtil payUtil, OrderService orderService) {
        this.payUtil = payUtil;
        this.orderService = orderService; // 正确初始化
    }

    //FIXME
// 请求需要提供两个参数分别为
// 消费金额 String amount
// 商品名称 String productName
    @ResponseBody
    @PostMapping("/create")
    public String alipay( @RequestBody PaymentRequest request)
            throws AlipayApiException {
        //从请求体获得参数
        String token = request.getAuthorization();
        BigDecimal amount = request.getAmount();
        String productName = request.getProductName();

        // 1. 获取当前用户ID（需要实现用户认证）
        Long userId = getCurrentUserId(token);

        // 2. 创建订单记录
        Order order = orderService.createOrder(userId, amount);

        // 3. 调用支付宝接口
        return payUtil.sendRequestToAlipay(
                order.getOrderId(),
                amount.toString(),
                productName
        );
    }

    //FIXME 根据token格式进行调整
    private Long getCurrentUserId(String token) {
        // 实现根据token解析用户ID的逻辑
        return 1L; // 示例值
    }
}
