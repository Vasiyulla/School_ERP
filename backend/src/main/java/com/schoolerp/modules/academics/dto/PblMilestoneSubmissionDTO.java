package com.schoolerp.modules.academics.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PblMilestoneSubmissionDTO {
    private UUID id;
    private UUID projectId;
    private String groupName;
    private String groupLeaderName;
    private String memberNames;
    private int milestoneNumber;
    private String milestoneTitle;
    private String submissionUrl;
    private double teacherScore;
    private double maxScore;
    private double peerContributionRating;
    private String status;
    private String teacherFeedback;
    private String gradedByTeacherName;
    private LocalDateTime submittedAt;
    private LocalDateTime gradedAt;
}

