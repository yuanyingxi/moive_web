package com.movie.controller;

import com.movie.common.Result;
import com.movie.entity.MovieBasicVO;
import com.movie.mapper.MovieMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController // 替换@Controller
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private MovieMapper movieMapper;

    @GetMapping
    public Result home() {
        List<MovieBasicVO> movies = movieMapper.selectAllMovieBasics();
        return Result.success(movies);
    }
}
