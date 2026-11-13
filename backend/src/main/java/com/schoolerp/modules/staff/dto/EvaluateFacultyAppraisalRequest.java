package com.schoolerp.modules.staff.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluateFacultyAppraisalRequest {

    @NotNull(message = "Teacher ID is mandatory")
    private UUID teacherId;

    @NotBlank(message = "Academic year is mandatory")
    @Builder.Default
    private String academicYear = "2026-2027";

    @Min(value = 0, message = "Self appraisal score must be >= 0")
    @Max(value = 25, message = "Self appraisal score cannot exceed 25")
    @Builder.Default
    private double selfAppraisalScore = 20.0;

    @Min(value = 0, message = "Academic results score must be >= 0")
    @Max(value = 35, message = "Academic results score cannot exceed 35")
    @Builder.Default
    private double academicResultsScore = 30.0;

    @Min(value = 0, message = "Institutional duty score must be >= 0")
    @Max(value = 20, message = "Institutional duty score cannot exceed 20")
    @Builder.Default
    private double institutionalDutyScore = 18.0;

    @Min(value = 0, message = "Principal observation score must be >= 0")
    @Max(value = 20, message = "Principal observation score cannot exceed 20")
    @Builder.Default
    private double principalObservationScore = 18.0;

    private String principalEvaluationNotes;
}

