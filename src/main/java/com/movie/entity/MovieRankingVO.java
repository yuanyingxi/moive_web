package com.movie.entity;

import lombok.Data;

@Data
public class MovieRankingVO {
    private Integer rank;      // 排名（由业务逻辑生成）
    private String title;      // 电影名称
    private Integer playCount; // 播放次数

    // 兼容数据库查询结果的构造器
    public MovieRankingVO(String title, Integer playCount) {
        this.title = title;
        this.playCount = playCount;
    }
}