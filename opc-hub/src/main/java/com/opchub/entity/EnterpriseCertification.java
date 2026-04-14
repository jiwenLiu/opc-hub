package com.opchub.entity;

import com.opchub.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class EnterpriseCertification extends BaseEntity {
    private Long userId;
    private String companyName;
    private String unifiedSocialCreditCode;
    private String legalPerson;
    private String businessLicense;
    private String status;
    private String rejectReason;
}