package com.schoolerp.modules.operations.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisitorCheckInRequest {
    @NotBlank
    private String visitorName;

    @NotBlank
    private String visitorPhone;

    @NotBlank
    private String idProofType; // AADHAAR, DRIVING_LICENSE, VOTER_ID, PASSPORT

    @NotBlank
    private String idProofNumber;

    @NotBlank
    private String purposeOfVisit;

    private UUID hostStaffId;

    @NotBlank
    private String hostStaffName;

    @NotBlank
    private String entryGate; // Gate 1 - Main Arch, Gate 2 - Sports Complex
}

