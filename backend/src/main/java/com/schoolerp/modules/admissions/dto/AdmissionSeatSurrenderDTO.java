package com.schoolerp.modules.admissions.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionSeatSurrenderDTO {
    private UUID id;
    private String tenantId;
    private String surrenderRefNumber;
    private UUID applicationId;
    private String studentName;
    private String gradeName;
    private String categoryName;
    private String surrenderReason;
    private LocalDate surrenderDate;
    private BigDecimal totalFeesPaid;
    private BigDecimal deductionPercentage;
    private BigDecimal deductionAmount;
    private BigDecimal refundableAmount;
    private String settlementStatus;
    private UUID waitlistStudentPromotedId;
    private UUID generalLedgerVoucherId;
    private UUID authorizedByUserId;
    private Instant createdAt;
}
