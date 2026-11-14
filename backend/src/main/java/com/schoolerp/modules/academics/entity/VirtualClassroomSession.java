package com.schoolerp.modules.academics.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "virtual_classroom_sessions", schema = "academics")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VirtualClassroomSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String className;

    @Column(nullable = false)
    private String sectionName;

    @Column(nullable = false)
    private String subjectName;

    @Column(nullable = false)
    private String teacherName;

    @Column(nullable = false)
    private LocalDate sessionDate;

    @Column(nullable = false)
    private int periodNumber;

    @Column(nullable = false)
    private String startTime; // e.g. "09:00"

    @Column(nullable = false)
    private String endTime; // e.g. "09:45"

    @Column(nullable = false)
    private String meetingProvider; // JITSI_WEBRTC, ZOOM_EDUCATION, GOOGLE_MEET

    @Column(nullable = false)
    private String joinMeetingUrl;

    @Column(nullable = false)
    private String meetingRoomCode; // e.g. "APEX-G10A-MATH-P1"

    @Column(nullable = false)
    private String sessionStatus; // SCHEDULED, LIVE_IN_PROGRESS, CONCLUDED, CANCELLED

    @Builder.Default
    private int totalStudentsJoined = 0;

    private String recordingUrl;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime startedAt;

    private LocalDateTime concludedAt;
}

