package com.movie.service;

import com.movie.entity.Genre;
import com.movie.entity.MovieRankingVO;
import com.movie.mapper.MovieMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChartService {
    //Chart1:电影类型表
    List<Genre> getGenreStats();

    //Chart2:电影排行表
    List<MovieRankingVO> getPlayTop10();
}
