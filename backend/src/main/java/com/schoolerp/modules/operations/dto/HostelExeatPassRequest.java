package com.schoolerp.modules.operations.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HostelExeatPassRequest {
    @NotNull
    private UUID studentId;

    @NotBlank
    private String exeatType; // WEEKEND_HOME, MEDICAL_TOWN, EMERGENCY_OUTSTATION

    @NotNull
    private LocalDateTime departureTime;

    @NotNull
    private LocalDateTime expectedReturnTime;

    private boolean parentAuthorized;
    private String parentContactPhone;
}

