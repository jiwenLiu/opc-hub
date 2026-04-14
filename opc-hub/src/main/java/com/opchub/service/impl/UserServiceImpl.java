package com.opchub.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opchub.entity.User;
import com.opchub.mapper.UserMapper;
import com.opchub.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User getUserInfo(Long userId) {
        return getById(userId);
    }

    @Override
    public void updateUser(User user) {
        updateById(user);
    }
}