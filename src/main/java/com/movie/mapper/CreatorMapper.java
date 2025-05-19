package com.movie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.movie.entity.Creator;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CreatorMapper extends BaseMapper<Creator> {
    @Select("SELECT id FROM creator WHERE name = #{name}")
    Long findCreatorIdByName(String name);

    @Select("SELECT id FROM creator WHERE name = #{name}")
    Long findIdByName(String name);
}