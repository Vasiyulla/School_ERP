package com.schoolerp.modules.staff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CpdSchoolAuditReportDTO {
    private String tenantId;
    private String trainingYear;
    private int totalTeachersAudited;
    private int compliantCount;
    private int inProgressCount;
    private int deficitCount;
    private double schoolComplianceRate;
    private List<CpdComplianceSummaryDTO> teacherSummaries;
}

