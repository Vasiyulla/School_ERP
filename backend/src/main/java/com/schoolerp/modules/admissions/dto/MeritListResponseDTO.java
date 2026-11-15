package com.schoolerp.modules.admissions.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeritListResponseDTO {
    private String gradeName;
    private String academicYear;
    private int totalApplicants;
    private List<AdmissionPointCriteriaDTO> rankedCandidates;
}

