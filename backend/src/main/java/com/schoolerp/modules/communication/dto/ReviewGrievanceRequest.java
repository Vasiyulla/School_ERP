package com.schoolerp.modules.communication.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewGrievanceRequest {

    @NotBlank(message = "Action is mandatory (RESOLVE, ESCALATE_TO_HOD, ESCALATE_TO_PRINCIPAL)")
    private String action; // RESOLVE, ESCALATE_TO_HOD, ESCALATE_TO_PRINCIPAL

    private String resolutionNotes;
}

