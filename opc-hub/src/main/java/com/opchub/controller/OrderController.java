package com.opchub.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.opchub.common.dto.Result;
import com.opchub.entity.OpcService;
import com.opchub.entity.TradeOrder;
import com.opchub.mapper.OpcServiceMapper;
import com.opchub.mapper.TradeOrderMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Tag(name = "订单管理")
public class OrderController {

    private final TradeOrderMapper orderMapper;
    private final OpcServiceMapper serviceMapper;

    @PostMapping("/create")
    @Operation(summary = "创建订单")
    public Result<TradeOrder> createOrder(@RequestParam Long serviceId) {
        long userId = StpUtil.getLoginIdAsLong();
        
        OpcService service = serviceMapper.selectById(serviceId);
        if (service == null) {
            return Result.error("服务不存在");
        }
        if (service.getUserId().equals(userId)) {
            return Result.error("不能购买自己的服务");
        }

        TradeOrder order = new TradeOrder();
        order.setOrderNo(UUID.randomUUID().toString().replace("-", ""));
        order.setBuyerId(userId);
        order.setSellerId(service.getUserId());
        order.setServiceId(serviceId);
        order.setAmount(service.getPrice());
        order.setStatus("pending");
        
        orderMapper.insert(order);
        return Result.success(order);
    }

    @GetMapping("/list")
    @Operation(summary = "订单列表")
    public Result<List<TradeOrder>> list(@RequestParam(required = false) String role) {
        long userId = StpUtil.getLoginIdAsLong();
        LambdaQueryWrapper<TradeOrder> wrapper = new LambdaQueryWrapper<>();
        
        if ("seller".equals(role)) {
            wrapper.eq(TradeOrder::getSellerId, userId);
        } else {
            wrapper.eq(TradeOrder::getBuyerId, userId);
        }
        wrapper.orderByDesc(TradeOrder::getCreateTime);
        
        List<TradeOrder> list = orderMapper.selectList(wrapper);
        return Result.success(list);
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "订单详情")
    public Result<TradeOrder> detail(@PathVariable Long id) {
        return Result.success(orderMapper.selectById(id));
    }

    @PostMapping("/pay/{id}")
    @Operation(summary = "支付订单")
    public Result<Void> pay(@PathVariable Long id) {
        TradeOrder order = orderMapper.selectById(id);
        if (order == null) {
            return Result.error("订单不存在");
        }
        if (!"pending".equals(order.getStatus())) {
            return Result.error("订单状态不正确");
        }

        order.setStatus("paid");
        order.setPayTime(LocalDateTime.now());
        orderMapper.updateById(order);
        return Result.success();
    }

    @PostMapping("/delivery/{id}")
    @Operation(summary = "交付订单")
    public Result<Void> delivery(@PathVariable Long id, @RequestParam String remark) {
        long userId = StpUtil.getLoginIdAsLong();
        TradeOrder order = orderMapper.selectById(id);
        
        if (order == null) {
            return Result.error("订单不存在");
        }
        if (!order.getSellerId().equals(userId)) {
            return Result.error("无权操作");
        }
        if (!"paid".equals(order.getStatus())) {
            return Result.error("订单状态不正确");
        }

        order.setStatus("delivered");
        order.setDeliveryTime(LocalDateTime.now());
        order.setRemark(remark);
        orderMapper.updateById(order);
        return Result.success();
    }

    @PostMapping("/confirm/{id}")
    @Operation(summary = "确认完成")
    public Result<Void> confirm(@PathVariable Long id) {
        long userId = StpUtil.getLoginIdAsLong();
        TradeOrder order = orderMapper.selectById(id);
        
        if (order == null) {
            return Result.error("订单不存在");
        }
        if (!order.getBuyerId().equals(userId)) {
            return Result.error("无权操作");
        }
        if (!"delivered".equals(order.getStatus())) {
            return Result.error("订单状态不正确");
        }

        order.setStatus("completed");
        order.setCompleteTime(LocalDateTime.now());
        orderMapper.updateById(order);
        return Result.success();
    }
}