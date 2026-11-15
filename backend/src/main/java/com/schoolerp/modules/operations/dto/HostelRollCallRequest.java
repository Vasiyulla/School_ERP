package com.schoolerp.modules.operations.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HostelRollCallRequest {
    @NotNull
    private UUID allocationId;

    @NotNull
    private LocalDate rollCallDate;

    @NotBlank
    private String sessionType; // MORNING_WAKEUP, NIGHT_LIGHTS_OUT

    @NotBlank
    private String status; // PRESENT, ABSENT, ON_EXEAT, INFIRMARY

    private String remarks;
    private String wardenName;
}

