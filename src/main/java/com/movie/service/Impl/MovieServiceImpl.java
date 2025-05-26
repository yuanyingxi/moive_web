package com.movie.service.Impl;

import com.movie.entity.MovieRankingVO;
import com.movie.mapper.MovieMapper;
import com.movie.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class MovieServiceImpl implements MovieService {

    private final MovieMapper movieMapper;

    @Autowired
    public MovieServiceImpl(MovieMapper movieMapper) {
        this.movieMapper = movieMapper;
    }

    @Override
    public List<MovieRankingVO> getPlayRankings() {
        // 查询数据库
        List<MovieRankingVO> rankings = movieMapper.selectPlayRankings();
        log.info("从数据库获取到{}条播放排行数据", rankings.size());

        // 添加排名序号
        for (int i = 0; i < rankings.size(); i++) {
            rankings.get(i).setRank(i + 1);
        }
        return rankings;
    }
}