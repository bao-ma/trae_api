package com.example.traeapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.traeapi.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Update("UPDATE public.user SET fail_count = fail_count + 1 WHERE user_id = #{userId}")
    int incrementFailCount(String userId);

    @Update("UPDATE public.user SET fail_count = 0 WHERE user_id = #{userId}")
    int resetFailCount(String userId);
}