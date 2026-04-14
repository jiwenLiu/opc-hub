package com.opchub.entity;

import com.opchub.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class OpcService extends BaseEntity {
    private Long userId;
    private String serviceName;
    private Long categoryId;
    private String description;
    private BigDecimal price;
    private String unit;
    private Integer deliveryDays;
    private String tags;
    private String status;
    private Integer viewCount;
    private String cover;
    private String images;
}