package com.opchub.entity;

import com.opchub.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OpcCertification extends BaseEntity {
    private Long userId;
    private String realName;
    private String idCard;
    private String faceId;
    private String status;
    private String rejectReason;
}