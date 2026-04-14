package com.opchub.service;

import com.opchub.entity.User;

public interface UserService {
    User getUserInfo(Long userId);
    void updateUser(User user);
}