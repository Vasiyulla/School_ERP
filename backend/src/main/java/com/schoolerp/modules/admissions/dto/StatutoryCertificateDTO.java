package com.schoolerp.modules.admissions.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatutoryCertificateDTO {
    private UUID id;
    private UUID inquiryId;
    private String studentName;
    private String certificateType;
    private String certificateNumber;
    private String issuingAuthority;
    private LocalDate issueDate;
    private String validFinancialYear;
    private String verifiedStatus;
    private String verificationRemarks;
    private String verifiedBy;
    private LocalDateTime verifiedAt;
}

