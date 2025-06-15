package com.movie.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("movie")
public class MovieBasicVO {
    private String id;
    private String title;
    private String description;
    private String url;
    private String coverUrl;
    private Boolean isVip;
    private Double averageRating;

    // 构造方法、getter和setter省略
}