package com.movie.service;

import com.movie.entity.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public interface OrderService {

    /**
     * 创建新订单
     */
    @Transactional
    void createOrder(Long userId, String productName, BigDecimal amount);

    /**
     * 获取订单
     */
    @Transactional
    Order selectOrder(Long userId, BigDecimal amount);

    /**
     * 处理支付成功
     */
    @Transactional
    boolean handlePaymentSuccess(String orderId, String aliPayTradeNo);

}