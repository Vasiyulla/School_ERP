package com.schoolerp.modules.admissions.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluateInterviewSlotRequest {
    private String status; // COMPLETED, NO_SHOW, RESCHEDULED
    private Integer evaluationRating; // 1 to 5
    private String interviewerFeedback;
    private String recommendedGrade;
}

