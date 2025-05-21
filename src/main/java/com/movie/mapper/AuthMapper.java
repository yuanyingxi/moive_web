package com.movie.mapper;

import com.movie.DTO.LoginDTO;
import com.movie.DTO.RegisterDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthMapper {
    // 查看用户名是否存在
    boolean existsByUsername(RegisterDTO registerDTO);

    // 插入用户信息
    void insertUser(RegisterDTO registerDTO);

    // 验证用户名和密码是否匹配
    boolean existsByUsernameAndPassword(LoginDTO loginDTO);
    // 更新最后登录时间
    void updateLastLoginTime(String username);

}
