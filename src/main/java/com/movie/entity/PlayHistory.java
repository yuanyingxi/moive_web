package com.movie.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("play_history")
public class PlayHistory {

    @TableId(type = IdType.AUTO)
    public Long id;  // 数据库自增主键
    public Long userId;  // 用户ID（逻辑外键，关联user.id）
    public Long movieId;  // 电影ID（逻辑外键，关联movie.id）
    public LocalDateTime playTime;  // 播放时间

}
