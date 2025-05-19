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

    /**
     * 以下为excel表数据导入数据库所需功能：
     */

    // 批量插入主创-电影关联
    void batchInsertMovieCreators(@Param("list") List<Creator> list);
}
