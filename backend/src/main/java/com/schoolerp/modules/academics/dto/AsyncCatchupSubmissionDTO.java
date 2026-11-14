package com.schoolerp.modules.academics.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AsyncCatchupSubmissionDTO {
    private UUID id;
    private UUID sessionId;
    private UUID studentId;
    private String studentName;
    private LocalDate submissionDate;
    private String materialReviewedTitle;
    private String proofOfLearningResponseText;
    private double quizScore;
    private String verifiedByTeacherName;
    private String status;
    private LocalDateTime submittedAt;
    private LocalDateTime verifiedAt;
}

