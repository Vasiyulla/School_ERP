package com.schoolerp.modules.academics.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "exit_ticket_sessions", schema = "academics")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExitTicketSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String teacherId;

    @Column(nullable = false)
    private String teacherName;

    @Column(nullable = false)
    private String className;

    @Column(nullable = false)
    private String sectionName;

    @Column(nullable = false)
    private String subjectName;

    @Column(nullable = false)
    private String topicName;

    @Column(nullable = false, length = 10)
    private String sessionPin; // 6-digit classroom PIN e.g. "EX8821"

    @Column(columnDefinition = "TEXT")
    private String questionText;

    @Column(nullable = false)
    private String questionType; // MCQ, CONFIDENCE_ONLY, SHORT_TEXT

    @Column(columnDefinition = "TEXT")
    private String optionsJson; // JSON array of options

    private String correctOption;

    @Column(nullable = false)
    private String status; // ACTIVE, CLOSED

    @Builder.Default
    private int totalResponses = 0;

    @Builder.Default
    private int understoodCount = 0;

    @Builder.Default
    private int unsureCount = 0;

    @Builder.Default
    private int strugglingCount = 0;

    @Builder.Default
    private double comprehensionRate = 0.0;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime closedAt;
}

