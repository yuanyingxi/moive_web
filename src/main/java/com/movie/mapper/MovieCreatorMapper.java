package com.movie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.movie.entity.MovieCreator;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieCreatorMapper extends BaseMapper<MovieCreator> {
    @Insert("<script>" +
            "INSERT INTO movie_creator (movie_id, creator_id, role_id) VALUES " +
            "<foreach item='item' collection='list' separator=','>" +
            "(#{item.movieId}, #{item.creatorId}, #{item.roleId})" +
            "</foreach>" +
            "</script>")
    void batchInsertRelations(@Param("list") List<MovieCreator> list);

    @Select("SELECT 1 FROM movie_creator WHERE movie_id = #{movieId} AND creator_id = #{creatorId} AND role_id = #{roleId} LIMIT 1")
    Integer existRelation(@Param("movieId") Long movieId,
                          @Param("creatorId") Long creatorId,
                          @Param("roleId") Long roleId);
}
