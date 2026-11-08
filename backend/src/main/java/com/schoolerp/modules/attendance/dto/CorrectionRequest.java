package com.schoolerp.modules.attendance.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CorrectionRequest {

    @NotBlank
    private String newStatus; // PRESENT, ABSENT, LATE, EXCUSED

    @NotBlank
    private String reason; // e.g. "Medical certificate submitted post-facto #MC-882"
}

