package com.movie.service;

import com.movie.entity.MovieRankingVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MovieService {

    List<MovieRankingVO> getPlayRankings();
}