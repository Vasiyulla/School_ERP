package com.schoolerp.modules.students.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewPortfolioArtifactRequest {
    private String teacherRemarks;
    private String rubricGradeBand; // EXEMPLARY, PROFICIENT, DEVELOPING
    private Boolean isFeaturedShowcase;
}

