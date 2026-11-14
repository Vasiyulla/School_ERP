package com.schoolerp.modules.staff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CpdComplianceSummaryDTO {
    private UUID staffId;
    private String teacherName;
    private int totalCompletedHours;
    private int mandatoryTargetHours; // 50
    private int remainingHours;
    private double compliancePercentage;
    private String status; // COMPLIANT, IN_PROGRESS, DEFICIT
}

