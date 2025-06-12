package com.movie.controller;

import com.movie.common.Result;
import com.movie.entity.MovieBasicVO;
import com.movie.entity.PageResult;
import com.movie.mapper.MovieMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private MovieMapper movieMapper;

    @GetMapping
    public Result home(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "25") int pageSize) {

        // 获取完整电影列表
        List<MovieBasicVO> allMovies = movieMapper.selectMovieList();

        // 计算分页参数
        int total = allMovies.size();
        int start = (pageNum - 1) * pageSize;
        int end = Math.min(start + pageSize, total);

        // 截取当前页数据
        List<MovieBasicVO> pageList = allMovies.subList(start, end);

        // 封装分页结果
        PageResult<MovieBasicVO> pageResult = new PageResult<>(
                pageList, total, pageNum, pageSize
        );

        return Result.success(pageResult);
    }
}