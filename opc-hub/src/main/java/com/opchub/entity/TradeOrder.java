package com.opchub.entity;

import com.opchub.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class TradeOrder extends BaseEntity {
    private String orderNo;
    private Long buyerId;
    private Long sellerId;
    private Long serviceId;
    private BigDecimal amount;
    private String status;
    private LocalDateTime payTime;
    private LocalDateTime deliveryTime;
    private LocalDateTime completeTime;
    private String remark;
}