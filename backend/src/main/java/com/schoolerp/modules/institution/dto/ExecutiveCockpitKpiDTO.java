package com.schoolerp.modules.institution.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecutiveCockpitKpiDTO {

    private long totalEnrolledStudents;
    private int totalSanctionedCapacity;
    private double occupancyRatePercentage;

    private long totalFacultyCount;
    private String teacherStudentRatio;

    private BigDecimal totalFeeBilledInr;
    private BigDecimal totalFeeCollectedInr;
    private double feeRealizationRatePercentage;
    private BigDecimal outstandingFeeDeficitInr;

    private double schoolWideAcademicAverageMarks;
    private long studentsOnAcademicRiskRadar;

    private double campusSafetyComplianceScore; // 0 - 100
    private double studentAttendancePercentageToday;
    private double facultyAttendancePercentageToday;

    private String institutionalHealthTier; // TIER_1_EXEMPLARY, TIER_2_STABLE, TIER_3_NEEDS_ATTENTION
}

