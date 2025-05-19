package com.movie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.movie.entity.MovieGenreRelation;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MovieGenreMapper extends BaseMapper<MovieGenreRelation> {
    @Insert("<script>" +
            "INSERT INTO movie_genre (movie_id, genre_id) VALUES " +
            "<foreach item='item' collection='list' separator=','>" +
            "(#{item.movieId}, #{item.genreId})" +
            "</foreach>" +
            "</script>")
    void batchInsertRelations(List<MovieGenreRelation> list);
}