package com.movie.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("movie_genre")
public class MovieGenreRelation {

    private Long movieId;  // 电影ID（逻辑外键，关联movie.id）
    private Integer genreId;  // 电影类型ID（逻辑外键，关联genre.id）

}
