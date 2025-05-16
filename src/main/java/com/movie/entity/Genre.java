package com.movie.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// 电影类型实体类
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("genre")
public class Genre {

    @TableId(type = IdType.AUTO)
    private Integer id;  // 数据库自增主键
    private String name;  // 类型名称

}
