package com.movie.service.Impl;

import com.movie.entity.Genre;
import com.movie.entity.MovieRankingVO;
import com.movie.mapper.MovieMapper;
import com.movie.service.ChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChartImpl implements ChartService {
    private final MovieMapper movieMapper;
    @Autowired
    public ChartImpl(MovieMapper movieMapper) {
        this.movieMapper = movieMapper;
    }

    @Override
    public List<Genre> getGenreStats(){
        return movieMapper.selectGenreStats();
    }

    @Override
    public List<MovieRankingVO> getPlayTop10() {
        return movieMapper.selectPlayTop10();
    }
}
