package com.schoolerp.modules.academics.dto;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitAsyncCatchupRequest {
    private UUID sessionId;
    private UUID studentId;
    private String studentName;
    private String materialReviewedTitle;
    private String proofOfLearningResponseText;
    private double quizScore;
}

