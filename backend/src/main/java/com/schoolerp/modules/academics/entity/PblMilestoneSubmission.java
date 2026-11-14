package com.schoolerp.modules.academics.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pbl_milestone_submissions", schema = "academics")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PblMilestoneSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID projectId;

    @Column(nullable = false)
    private String groupName; // e.g. "Eco Warriors Group #4"

    @Column(nullable = false)
    private String groupLeaderName;

    @Column(columnDefinition = "TEXT")
    private String memberNames; // Comma-separated or JSON list of members

    @Column(nullable = false)
    private int milestoneNumber; // 1, 2, 3, 4

    @Column(nullable = false)
    private String milestoneTitle;

    @Column(nullable = false)
    private String submissionUrl;

    @Builder.Default
    private double teacherScore = 0.0;

    @Builder.Default
    private double maxScore = 25.0;

    @Builder.Default
    private double peerContributionRating = 100.0; // Peer rating % (e.g. 95% = full participation)

    @Column(nullable = false)
    @Builder.Default
    private String status = "SUBMITTED"; // SUBMITTED, APPROVED, REVISION_REQUESTED

    @Column(columnDefinition = "TEXT")
    private String teacherFeedback;

    private String gradedByTeacherName;

    @Builder.Default
    private LocalDateTime submittedAt = LocalDateTime.now();

    private LocalDateTime gradedAt;
}

