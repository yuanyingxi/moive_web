package com.movie.service;

import com.movie.entity.MovieRankingVO;
import com.movie.mapper.MovieMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class MovieService {

    private final MovieMapper movieMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public MovieService(MovieMapper movieMapper,
                        RedisTemplate<String, Object> redisTemplate) {
        this.movieMapper = movieMapper;
        this.redisTemplate = redisTemplate;
    }

    public List<MovieRankingVO> getPlayRankings() {
        String cacheKey = "movie:play_rankings";

        // 使用类型安全的方式获取缓存
        Object cachedData = redisTemplate.opsForValue().get(cacheKey);
        if (cachedData instanceof List) {
            try {
                @SuppressWarnings("unchecked")
                List<MovieRankingVO> cached = (List<MovieRankingVO>) cachedData;
                return cached;
            } catch (ClassCastException e) {
                log.warn("缓存数据类型不匹配，将重新查询数据库");
            }
        }

        // 查询数据库
        List<MovieRankingVO> rankings = movieMapper.selectPlayRankings();

        // 添加排名序号
        for (int i = 0; i < rankings.size(); i++) {
            rankings.get(i).setRank(i + 1);
        }

        // 存入缓存（类型安全存储）
        redisTemplate.opsForValue().set(cacheKey, rankings, 1, TimeUnit.HOURS);
        return rankings;
    }
}