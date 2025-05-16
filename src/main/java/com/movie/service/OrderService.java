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
public class OrderService {
    private final OrderMapper orderMapper;

    @Autowired
    public OrderService(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    /**
     * 创建新订单
     */
    @Transactional
    public Order createOrder(Long userId, BigDecimal amount) {
        Order order = new Order();
        order.setOrderId(generateOrderId(userId)); // 商户订单号, 主键
        order.setUserId(userId);             // 用户id, （逻辑外键，关联user.id）
        order.setAmount(amount);             // 支付金额 //FIXME 注意：支付宝支付金额严格限定保留两位小数
        order.setPaymentStatusId(PaymentStatus.UNPAID); // 支付状态id, （逻辑外键，关联payment_statue.id)
        order.setPaymentTime(LocalDateTime.now());       // 创建订单时间
        orderMapper.insert(order);
        return order;
    }

    /**
     * 处理支付成功
     */
    @Transactional
    public boolean handlePaymentSuccess(String orderId, String aliPayTradeNo) {
        Order order = orderMapper.selectById(orderId);
        if (order == null || !order.getPaymentStatusId().equals(PaymentStatus.UNPAID)) {
            return false;
        }

        order.setPaymentStatusId(PaymentStatus.PAID);
        order.setPayTime(LocalDateTime.now());
        order.setAliPayTradeNo(aliPayTradeNo);
        return orderMapper.updateById(order) > 0;
    }

    /**
     * 生成唯一订单号
     */
    private String generateOrderId(Long userId) {
        // 生成唯一订单号
        // 用户ID处理：取后4位
        String userIdPart = String.format("%04d", userId % 10000);
        // 时间部分：精确到秒
        String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        // 随机部分：取UUID前6位
        String randomPart = UUID.randomUUID().toString().replace("-", "").substring(0, 6);
        return time + userIdPart + randomPart;
    }
}