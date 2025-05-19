package com.movie.service;

import com.movie.entity.Order;
import com.movie.entity.PaymentStatus;
import com.movie.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Service
public interface OrderService {

    /**
     * 创建新订单
     */
    @Transactional
    public Order createOrder(Long userId, BigDecimal amount);

    /**
     * 处理支付成功
     */
    @Transactional
    public boolean handlePaymentSuccess(String orderId, String aliPayTradeNo);

}