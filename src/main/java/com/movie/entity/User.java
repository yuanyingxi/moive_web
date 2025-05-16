package com.movie.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
public class User {

    @TableId(type = IdType.AUTO)  // 让数据库自己生成主键自增
    private Long id;  // 标识 ID 为主键自增
    private String userNo;  // 用户编号
    private String username;  // 用户名
    private String password;  // 密码
    private String phone;  // 手机号
    private String email;  // 邮箱
    private Integer isVip;  // 是否是 VIP 用户
    private LocalDateTime createTime;  // 创建时间
    private LocalDateTime updateTime;  // 更新时间
    private LocalDateTime lastLoginTime;  // 最后登录时间

}
