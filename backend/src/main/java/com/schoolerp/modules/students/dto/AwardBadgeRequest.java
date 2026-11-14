package com.schoolerp.modules.students.dto;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AwardBadgeRequest {
    private UUID studentId;
    private String studentName;
    private String className;
    private String sectionName;
    private String badgeCode; // BRILLIANT_INSIGHT, PEER_HELPER, ACTIVE_PARTICIPANT, DEEP_THINKER, INTEGRITY_HONESTY, LAB_SAFETY_CHAMPION
    private String houseName;
    private String teacherNotes;
}

