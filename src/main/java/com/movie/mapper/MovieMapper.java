package com.movie.mapper;

import com.movie.entity.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface MovieMapper extends BaseMapper<Movie> {
    // Chart1：统计各类型电影数量
    @Select("SELECT g.name AS name, COUNT(mg.movie_id) AS value FROM movie_genre mg JOIN genre g ON mg.genre_id = g.id GROUP BY g.name")
    List<Genre> selectGenreStats();

    // Chart2：查询播放量TOP10电影
    @Select("SELECT title, play_count AS playCount FROM movie ORDER BY play_count DESC LIMIT 10")
    List<MovieRankingVO> selectPlayTop10();

    @Results(id = "rankingMap", value = {
            @Result(column = "title", property = "title"),
            @Result(column = "play_count", property = "playCount")
    })
    @Select("SELECT title, play_count FROM movie ORDER BY play_count DESC LIMIT 10")
    List<MovieRankingVO> selectPlayRankings();

    @Results({
            @Result(column = "title", property = "title"),
            @Result(column = "url", property = "url"),
            @Result(column = "cover_url", property = "coverUrl"),
            @Result(column = "is_vip", property = "isVip"),
            @Result(column = "average_rating", property = "averageRating")
    })
    @Select("SELECT title, url, cover_url, is_vip, average_rating FROM movie")
    List<MovieBasicVO> selectAllMovieBasics();
}
