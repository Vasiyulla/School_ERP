package com.schoolerp.modules.admissions.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionPointCriteriaDTO {
    private UUID id;
    private UUID inquiryId;
    private String studentName;
    private String gradeApplyingFor;
    private BigDecimal distanceKm;
    private int distancePoints; // Max 40
    private boolean hasSibling;
    private int siblingPoints; // Max 30
    private boolean hasAlumniParent;
    private int alumniPoints; // Max 10
    private boolean isStaffWard;
    private int staffWardPoints; // Max 10
    private boolean isSingleGirlChild;
    private int girlChildPoints; // Max 5
    private boolean isFirstBorn;
    private int firstBornPoints; // Max 5
    private int totalCalculatedPoints; // Out of 100
    private Integer tieBreakerDrawNumber;
    private String criteriaVerdict; // e.g. "TIER_1_DIRECT_CALL" or "LOTTERY_POOL"
}

