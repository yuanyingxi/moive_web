package com.movie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.movie.entity.Genre;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface GenreMapper extends BaseMapper<Genre> {
    @Select("SELECT id FROM genre WHERE name = #{name}")
    Integer selectIdByName(String name);
}
