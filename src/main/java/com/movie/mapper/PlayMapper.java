package com.movie.mapper;

import com.movie.entity.Movie;
import com.movie.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PlayMapper {
    Movie getMovieById(String movie_no);

    User getUserByUsername(String username);
}
