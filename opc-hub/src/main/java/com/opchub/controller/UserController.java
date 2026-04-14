package com.opchub.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.opchub.common.dto.Result;
import com.opchub.entity.User;
import com.opchub.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "用户管理")
public class UserController {

    private final UserService userService;

    @GetMapping("/info")
    @Operation(summary = "获取用户信息")
    public Result<User> getUserInfo() {
        long userId = StpUtil.getLoginIdAsLong();
        User user = userService.getUserInfo(userId);
        user.setPassword(null);
        return Result.success(user);
    }

    @PutMapping("/update")
    @Operation(summary = "更新用户信息")
    public Result<Void> updateUser(@RequestBody User user) {
        long userId = StpUtil.getLoginIdAsLong();
        user.setId(userId);
        userService.updateUser(user);
        return Result.success();
    }

    @PostMapping("/logout")
    @Operation(summary = "登出")
    public Result<Void> logout() {
        StpUtil.logout();
        return Result.success();
    }
}