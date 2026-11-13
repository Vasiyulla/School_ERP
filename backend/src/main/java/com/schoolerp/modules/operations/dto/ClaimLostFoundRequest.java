package com.schoolerp.modules.operations.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClaimLostFoundRequest {

    private UUID claimedByStudentId;

    @NotBlank(message = "Claimant name is mandatory")
    private String claimedByName;

    @NotBlank(message = "Verification note / proof of ownership is mandatory")
    private String claimVerificationNote;
}

