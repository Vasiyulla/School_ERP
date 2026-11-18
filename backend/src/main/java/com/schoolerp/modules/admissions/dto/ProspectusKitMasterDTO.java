package com.schoolerp.modules.admissions.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProspectusKitMasterDTO {
    private UUID id;
    private String tenantId;
    private String serialBarcode;
    private String academicYear;
    private BigDecimal price;
    private String status;
    private String buyerParentName;
    private String buyerPhone;
    private String paymentMode;
    private String transactionRef;
    private UUID soldByUserId;
    private Instant soldAt;
    private UUID linkedInquiryId;
    private Instant createdAt;
}
