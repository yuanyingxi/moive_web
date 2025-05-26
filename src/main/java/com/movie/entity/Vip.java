package com.movie.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("vip")
public class Vip {
    private Long vipId;      //会员商品ID
    private String vipName;  //会员商品名字
    private BigDecimal vipPrice; //会员商品价格
    private int vipDuration; //会员持续时间
}
