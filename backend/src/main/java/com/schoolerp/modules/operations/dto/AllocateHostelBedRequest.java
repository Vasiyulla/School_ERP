package com.schoolerp.modules.operations.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllocateHostelBedRequest {
    @NotNull
    private UUID roomId;

    @NotNull
    private UUID studentId;

    private int bedNumber;
    private String academicYear; // "2026-2027"
}

