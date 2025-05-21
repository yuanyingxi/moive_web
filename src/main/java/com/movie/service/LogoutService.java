package com.movie.service;

import com.movie.common.Result;
import jakarta.servlet.http.HttpServletRequest;

public interface LogoutService {
    Result logout(HttpServletRequest request);
}
