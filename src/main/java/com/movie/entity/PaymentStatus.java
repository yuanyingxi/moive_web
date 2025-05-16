package com.movie.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("payment_status")
public class PaymentStatus {
    public static final Integer UNPAID = 0;
    public static final Integer PAID = 1;

    private Integer id;  // 主键
    private String name;  // 支付状态
}
