package com.movie.service.Impl;

import com.movie.common.Result;
import com.movie.entity.Movie;
import com.movie.entity.User;
import com.movie.mapper.PlayMapper;
import com.movie.service.PlayService;
import com.movie.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PlayServiceImpl implements PlayService {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PlayMapper playMapper;

    @Override
    public Result getMovie(String movieId, HttpServletRequest request) {
        Movie movie = playMapper.getMovieById(movieId);
        if (movie == null) {
            return Result.error(404, "该电影不存在");
        }
        if (movie.getUrl() == null) {
            return Result.error(404, "该电影暂无播放源");
        }

        // 进行 vip 权限校验
        User user = (User) request.getAttribute("user");
        if (user.getIsVip() < movie.getIsVip()) {  // 说明用户的 vip 权限不足
            return Result.error(403, "该电影需要 VIP 权限");
        } else {  // 说明用户的 vip 权限足够
            Map<String, Object> map = new HashMap<>();
            map.put("title", movie.getTitle());
            map.put("url", movie.getUrl());

            return Result.success(map);
        }

    }
}
