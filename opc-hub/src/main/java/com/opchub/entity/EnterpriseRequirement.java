package com.opchub.entity;

import com.opchub.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
public class EnterpriseRequirement extends BaseEntity {
    private Long userId;
    private String title;
    private Long categoryId;
    private String description;
    private BigDecimal budget;
    private String deadline;
    private String status;
}