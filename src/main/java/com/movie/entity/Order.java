package com.movie.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("order")
public class Order {

    @TableId("id")
    private String orderId;  // 商户订单号, 主键
    private Long userId;  // 用户id, （逻辑外键，关联user.id）
    private BigDecimal amount;  // 支付金额 //FIXME 注意：支付宝支付金额严格限定保留两位小数
    private Integer paymentStatusId;  // 支付状态id, （逻辑外键，关联payment_status.id)
    private LocalDateTime paymentTime;  // 创建订单时间
    private LocalDateTime payTime;  // 支付时间
    private String aliPayTradeNo;  // 支付宝交易号

}
