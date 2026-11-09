package com.schoolerp.modules.academics.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "homework_submissions", schema = "academics")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomeworkSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID assignmentId;

    @Column(nullable = false)
    private UUID studentId;

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    private LocalDateTime submittedAt;

    @Column(nullable = false)
    @Builder.Default
    private String status = "SUBMITTED"; // PENDING, SUBMITTED, LATE, GRADED

    private String submissionUrl; // link to scanned PDF or photos

    private Double marksAwarded; // e.g. 19.0 / 20.0

    private String teacherFeedback; // "Excellent step-by-step discriminant working"

    private LocalDateTime gradedAt;

    private String gradedByTeacherName;
}

