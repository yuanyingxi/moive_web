package com.movie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.movie.entity.Genre;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GenreMapper extends BaseMapper<Genre> {
    @Select("SELECT id FROM genre WHERE name = #{name}")
    Integer selectIdByName(String name);

    // 添加固定排序的查询方法
    @Select("SELECT * FROM genre ORDER BY id ASC")
    List<Genre> selectAllOrderByIdDesc();
}
