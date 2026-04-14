package com.opchub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.opchub.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}