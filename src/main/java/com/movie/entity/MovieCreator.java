package com.movie.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("movie_creator")
public class MovieCreator {

    private Long movieId;  // 电影ID（逻辑外键，关联movie.id）
    private Long genreId;  // 电影类型ID（逻辑外键，关联genre.id）
    private Long roleId;  // 电影创作者ID（逻辑外键，关联role.id）

}
