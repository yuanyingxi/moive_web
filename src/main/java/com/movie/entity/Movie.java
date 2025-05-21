package com.movie.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("movie")
public class Movie {
    
    @TableId(type = IdType.AUTO)
    private Long id;  // 数据库自增主键
    @TableField("movie_no")
    private String movieNo;  // 电影编号
    private String title;  // 电影标题
    private String url;    // 电影url
    private String description;  // 剧情简介
    private String coverUrl;  // 电影海报URL
    private LocalDateTime releaseDate;  // 上映时间
    private String region;  // 上映地区
    private Integer isVip;  // 是否会员
    private Long playCount;  // 播放量, 用于热播排行
    private Integer duration;  // 片长
    private Long ratingCount;  // 评分人数
    private BigDecimal averageRating;  // 平均评分
}
