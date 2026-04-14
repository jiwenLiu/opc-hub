package com.opchub.entity;

import com.opchub.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ServiceCategory extends BaseEntity {
    private String name;
    private String icon;
    private Integer sortOrder;
}