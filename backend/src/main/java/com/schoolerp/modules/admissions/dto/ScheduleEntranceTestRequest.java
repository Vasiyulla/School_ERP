package com.schoolerp.modules.admissions.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ScheduleEntranceTestRequest {
    @NotNull
    private UUID inquiryId;

    @NotNull
    private LocalDateTime testDate;

    @NotBlank
    private String examHall; // e.g. "Hall C / Senior Wing"

    @NotBlank
    private String reportingTime; // e.g. "09:00 AM"

    private BigDecimal qualifyingCutoffPercentage; // defaults to 60.0% if null
}

