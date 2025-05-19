package com.movie.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private String Authorization;
    private BigDecimal amount;    // 金额（若支付宝要求字符串格式）
    private String productName;   // 商品名称
}