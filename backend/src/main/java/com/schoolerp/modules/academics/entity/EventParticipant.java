package com.schoolerp.modules.academics.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "event_participants", schema = "academics")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID eventId;

    @Column(nullable = false)
    private UUID studentId;

    private String studentAdmissionNo;

    @Column(nullable = false)
    private String studentName;

    private String className;

    private String sectionName;

    @Column(nullable = false)
    @Builder.Default
    private String role = "PARTICIPANT"; // PARTICIPANT, TEAM_CAPTAIN, SOLO_PERFORMER, ENSEMBLE_MEMBER, MC_ANCHOR, VOLUNTEER_ORGANIZER

    @Column(nullable = false)
    @Builder.Default
    private String participationStatus = "CONFIRMED"; // REGISTERED, CONFIRMED, ATTENDED, ABSENT, WITHDRAWN

    @Builder.Default
    private String achievementTier = "NONE"; // NONE, FIRST_PLACE, SECOND_PLACE, THIRD_PLACE, CONSOLATION, SPECIAL_RECOGNITION

    @Builder.Default
    private int housePointsAwarded = 0;

    @Builder.Default
    private boolean certificateIssued = false;

    private String certificateNumber; // e.g. "CERT-EVT-2026-X89A"

    private String teacherRemarks;

    @Builder.Default
    private LocalDateTime enrolledAt = LocalDateTime.now();
}

