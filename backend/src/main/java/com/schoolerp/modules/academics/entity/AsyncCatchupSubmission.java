package com.schoolerp.modules.academics.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "async_catchup_submissions", schema = "academics")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AsyncCatchupSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID sessionId;

    @Column(nullable = false)
    private UUID studentId;

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    private LocalDate submissionDate;

    @Column(nullable = false)
    private String materialReviewedTitle;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String proofOfLearningResponseText; // Summary of key concepts learned

    @Builder.Default
    private double quizScore = 100.0;

    private String verifiedByTeacherName;

    @Column(nullable = false)
    private String status; // SUBMITTED, VERIFIED_ATTENDANCE_NORMALIZED

    @Builder.Default
    private LocalDateTime submittedAt = LocalDateTime.now();

    private LocalDateTime verifiedAt;
}

