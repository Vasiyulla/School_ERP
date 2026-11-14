package com.schoolerp.modules.finance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluateFamilyConcessionRequest {
    private String familyId;
    private String primaryGuardianPhone;
    private List<StudentSiblingInputDTO> children;
}

