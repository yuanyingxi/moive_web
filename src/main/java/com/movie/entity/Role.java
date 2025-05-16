package com.movie.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 主创类型表实体类
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("role")
public class Role {

    @TableId(type = IdType.AUTO)
    private Integer id;  // 数据库自增主键
    private String name;  // 角色名称

}
