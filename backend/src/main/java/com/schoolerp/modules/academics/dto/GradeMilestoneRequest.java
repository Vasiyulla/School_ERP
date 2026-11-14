package com.schoolerp.modules.academics.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GradeMilestoneRequest {
    private double teacherScore;
    private double maxScore;
    private double peerContributionRating; // % (e.g., 95.0)
    private String status; // APPROVED, REVISION_REQUESTED
    private String teacherFeedback;
}

