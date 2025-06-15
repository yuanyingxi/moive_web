package com.movie.controller;

import com.movie.common.Result;
import com.movie.entity.MovieBasicVO;
import com.movie.entity.PageResult;
import com.movie.mapper.MovieMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/home")
public class HomeController {
    @Autowired
    private MovieMapper movieMapper;

    @GetMapping
    public Result home(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "25") int pageSize,
            @RequestParam(defaultValue = "2") int movieType) {  // 默认值改为2

        // 传入movieType参数到Mapper
        List<MovieBasicVO> allMovies = movieMapper.selectMovieList(movieType);

        // 分页逻辑保持不变
        int total = allMovies.size();
        int start = (pageNum - 1) * pageSize;
        int end = Math.min(start + pageSize, total);

        List<MovieBasicVO> pageList = allMovies.subList(start, end);

        PageResult<MovieBasicVO> pageResult = new PageResult<>(
                pageList, total, pageNum, pageSize
        );

        return Result.success(pageResult);
    }

}