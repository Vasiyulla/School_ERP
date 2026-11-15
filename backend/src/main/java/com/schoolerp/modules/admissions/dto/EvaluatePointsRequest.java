package com.schoolerp.modules.admissions.dto;

import jakarta.validation.constraints.NotNull;
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
public class EvaluatePointsRequest {
    @NotNull
    private UUID inquiryId;

    private BigDecimal distanceKm; // e.g., 2.5 km
    private boolean hasSibling;
    private UUID siblingStudentId;
    private boolean hasAlumniParent;
    private Integer alumniPassingYear;
    private boolean isStaffWard;
    private UUID staffEmployeeId;
    private boolean isSingleGirlChild;
    private boolean isFirstBorn;
}

