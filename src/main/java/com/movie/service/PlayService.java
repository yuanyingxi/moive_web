package com.movie.service;

import com.movie.common.Result;
import jakarta.servlet.http.HttpServletRequest;

public interface PlayService {
    Result getMovie(String movieId, HttpServletRequest request);
}
