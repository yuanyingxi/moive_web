package com.movie.controller;

import com.movie.entity.Genre;
import com.movie.entity.MovieRankingVO;
import com.movie.service.ChartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/chart/movie_stats")
@RequiredArgsConstructor
public class EChartController {
    private final ChartService chartService;

    @GetMapping("/Chart1")
    public ResponseEntity<List<Genre>> getChart1() {
        return ResponseEntity.ok(chartService.getGenreStats());
    }

    @GetMapping("/Chart2")
    public ResponseEntity<List<MovieRankingVO>> getChart2() {
        return ResponseEntity.ok(chartService.getPlayTop10());
    }
}
