package com.schoolerp.modules.operations.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisitorApprovalRequest {
    @NotBlank
    private String approvalDecision; // APPROVED, REJECTED
}

