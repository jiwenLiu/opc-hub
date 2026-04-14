package com.opchub.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.opchub.common.dto.PageResult;
import com.opchub.common.dto.Result;
import com.opchub.entity.OpcService;
import com.opchub.mapper.OpcServiceMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service")
@RequiredArgsConstructor
@Tag(name = "服务管理")
public class ServiceController {

    private final OpcServiceMapper opcServiceMapper;

    @PostMapping("/publish")
    @Operation(summary = "发布服务")
    public Result<Void> publishService(@RequestBody OpcService service) {
        long userId = StpUtil.getLoginIdAsLong();
        service.setUserId(userId);
        service.setStatus("pending");
        service.setViewCount(0);
        opcServiceMapper.insert(service);
        return Result.success();
    }

    @GetMapping("/list")
    @Operation(summary = "服务列表")
    public Result<PageResult<OpcService>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword) {
        
        LambdaQueryWrapper<OpcService> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OpcService::getStatus, "published");
        if (categoryId != null) {
            wrapper.eq(OpcService::getCategoryId, categoryId);
        }
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(OpcService::getServiceName, keyword);
        }
        wrapper.orderByDesc(OpcService::getCreateTime);

        Page<OpcService> pageResult = opcServiceMapper.selectPage(new Page<>(page, size), wrapper);
        return Result.success(PageResult.of(pageResult.getTotal(), pageResult.getRecords()));
    }

    @GetMapping("/my")
    @Operation(summary = "我的服务")
    public Result<List<OpcService>> myServices() {
        long userId = StpUtil.getLoginIdAsLong();
        LambdaQueryWrapper<OpcService> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OpcService::getUserId, userId);
        wrapper.orderByDesc(OpcService::getCreateTime);
        List<OpcService> list = opcServiceMapper.selectList(wrapper);
        return Result.success(list);
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "服务详情")
    public Result<OpcService> detail(@PathVariable Long id) {
        OpcService service = opcServiceMapper.selectById(id);
        if (service != null) {
            service.setViewCount(service.getViewCount() + 1);
            opcServiceMapper.updateById(service);
        }
        return Result.success(service);
    }

    @PutMapping("/update")
    @Operation(summary = "更新服务")
    public Result<Void> updateService(@RequestBody OpcService service) {
        opcServiceMapper.updateById(service);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除服务")
    public Result<Void> deleteService(@PathVariable Long id) {
        opcServiceMapper.deleteById(id);
        return Result.success();
    }
}