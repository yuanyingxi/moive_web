package com.movie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.movie.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    @Select("SELECT * FROM `order` WHERE user_id = #{userId}")
    List<Order> selectByUserId(@Param("userId") Long userId);
}