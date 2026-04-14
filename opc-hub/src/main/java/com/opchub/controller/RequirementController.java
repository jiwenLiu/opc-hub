package com.opchub.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.opchub.common.dto.PageResult;
import com.opchub.common.dto.Result;
import com.opchub.entity.EnterpriseRequirement;
import com.opchub.mapper.EnterpriseRequirementMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requirement")
@RequiredArgsConstructor
@Tag(name = "需求管理")
public class RequirementController {

    private final EnterpriseRequirementMapper requirementMapper;

    @PostMapping("/publish")
    @Operation(summary = "发布需求")
    public Result<Void> publishRequirement(@RequestBody EnterpriseRequirement requirement) {
        long userId = StpUtil.getLoginIdAsLong();
        requirement.setUserId(userId);
        requirement.setStatus("pending");
        requirementMapper.insert(requirement);
        return Result.success();
    }

    @GetMapping("/list")
    @Operation(summary = "需求列表")
    public Result<PageResult<EnterpriseRequirement>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long categoryId) {
        
        LambdaQueryWrapper<EnterpriseRequirement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EnterpriseRequirement::getStatus, "published");
        if (categoryId != null) {
            wrapper.eq(EnterpriseRequirement::getCategoryId, categoryId);
        }
        wrapper.orderByDesc(EnterpriseRequirement::getCreateTime);

        Page<EnterpriseRequirement> pageResult = requirementMapper.selectPage(new Page<>(page, size), wrapper);
        return Result.success(PageResult.of(pageResult.getTotal(), pageResult.getRecords()));
    }

    @GetMapping("/my")
    @Operation(summary = "我的需求")
    public Result<List<EnterpriseRequirement>> myRequirements() {
        long userId = StpUtil.getLoginIdAsLong();
        LambdaQueryWrapper<EnterpriseRequirement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EnterpriseRequirement::getUserId, userId);
        wrapper.orderByDesc(EnterpriseRequirement::getCreateTime);
        List<EnterpriseRequirement> list = requirementMapper.selectList(wrapper);
        return Result.success(list);
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "需求详情")
    public Result<EnterpriseRequirement> detail(@PathVariable Long id) {
        return Result.success(requirementMapper.selectById(id));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除需求")
    public Result<Void> deleteRequirement(@PathVariable Long id) {
        requirementMapper.deleteById(id);
        return Result.success();
    }
}