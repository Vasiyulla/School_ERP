package com.schoolerp.modules.students.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "classroom_badge_awards", schema = "students")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassroomBadgeAward {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID studentId;

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    private String className;

    @Column(nullable = false)
    private String sectionName;

    @Column(nullable = false)
    private String awardedByStaffName;

    @Column(nullable = false)
    private String badgeCode; // BRILLIANT_INSIGHT, PEER_HELPER, ACTIVE_PARTICIPANT, DEEP_THINKER, INTEGRITY_HONESTY, LAB_SAFETY_CHAMPION

    @Column(nullable = false)
    private String badgeTitle; // "💡 Brilliant Insight", "🤝 Helping a Peer", etc.

    @Column(nullable = false)
    private String badgeCategory; // ACADEMIC, CHARACTER, LEADERSHIP, TEAMWORK

    private String iconEmoji;

    private String houseName;

    @Builder.Default
    private int housePointsAwarded = 10;

    @Column(columnDefinition = "TEXT")
    private String teacherNotes;

    @Builder.Default
    private LocalDateTime awardedAt = LocalDateTime.now();
}

