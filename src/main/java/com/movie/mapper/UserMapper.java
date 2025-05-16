package com.movie.mapper;

import jakarta.validation.constraints.NotBlank;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    boolean existsByUsername(@NotBlank String username);
}
