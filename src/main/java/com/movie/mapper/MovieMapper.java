package com.movie.mapper;

import com.movie.entity.Movie;
import com.movie.entity.MovieRankingVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface MovieMapper extends BaseMapper<Movie> {
    @Results(id = "rankingMap", value = {
            @Result(column = "title", property = "title"),
            @Result(column = "play_count", property = "playCount")
    })
    @Select("SELECT title, play_count FROM movie ORDER BY play_count DESC LIMIT 10")
    List<MovieRankingVO> selectPlayRankings();
}
