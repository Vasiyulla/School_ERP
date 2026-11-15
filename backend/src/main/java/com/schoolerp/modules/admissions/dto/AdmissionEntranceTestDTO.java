package com.schoolerp.modules.admissions.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionEntranceTestDTO {
    private UUID id;
    private UUID inquiryId;
    private String studentName;
    private String gradeApplyingFor;
    private String testCode;
    private LocalDateTime testDate;
    private String examHall;
    private String reportingTime;
    private BigDecimal mathScore;
    private BigDecimal englishScore;
    private BigDecimal scienceScore;
    private BigDecimal aptitudeScore;
    private BigDecimal totalScore;
    private BigDecimal maximumScore;
    private BigDecimal percentage;
    private BigDecimal qualifyingCutoffPercentage;
    private String resultStatus;
    private String evaluatorRemarks;
    private String admitCardToken;
}

