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

    @Select("<script>" +
            "SELECT movie_no AS id, title, url, cover_url, is_vip, average_rating, description FROM movie" +
            "<where>" +
            "   <choose>" +
            "       <when test='isVip == 0'>" +
            "           AND is_vip = 0" +
            "       </when>" +
            "       <when test='isVip == 1'>" +
            "           AND is_vip = 1" +
            "       </when>" +
            "       <otherwise>" +
            "           AND (is_vip = 0 OR is_vip = 1)" +
            "       </otherwise>" +
            "   </choose>" +
            "</where>" +
            "</script>")
    @Results({
            @Result(property = "movie_no", column = "movie_no"),
            @Result(property = "title", column = "title"),
            @Result(property = "description", column = "description"),
            @Result(property = "url", column = "url"),
            @Result(property = "coverUrl", column = "cover_url"),
            @Result(property = "isVip", column = "is_vip"),
            @Result(property = "averageRating", column = "average_rating")
    })
    List<MovieBasicVO> selectMovieList(@Param("isVip") int isVip);
}
