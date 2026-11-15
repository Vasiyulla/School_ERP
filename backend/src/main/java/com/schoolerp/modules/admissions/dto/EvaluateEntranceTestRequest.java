package com.schoolerp.modules.admissions.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluateEntranceTestRequest {
    @NotNull
    private BigDecimal mathScore;

    @NotNull
    private BigDecimal englishScore;

    @NotNull
    private BigDecimal scienceScore;

    @NotNull
    private BigDecimal aptitudeScore;

    private BigDecimal maximumScore; // defaults to 100.0
    private String evaluatorRemarks;
}

