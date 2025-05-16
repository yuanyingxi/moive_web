package com.movie.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 主创表实体类
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("creator")
public class Creator {

    @TableId(type = IdType.AUTO)
    private Integer id;  // 数据库自增主键
    private String name;  // 主创姓名
//    private String bio;  // 主创简介
//    private String photoUrl;  // 主创照片

}
