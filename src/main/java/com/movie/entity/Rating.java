package com.movie.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 评分表实体类
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("rating")
public class Rating {

    private Long userId;  // 主创ID（逻辑外键，关联creator.id）
    private Long movieId;  // 电影ID（逻辑外键，关联movie.id）
    private Integer score;  // 评分（0-10分）
    private LocalDateTime createTime;  // 评分时间

}
