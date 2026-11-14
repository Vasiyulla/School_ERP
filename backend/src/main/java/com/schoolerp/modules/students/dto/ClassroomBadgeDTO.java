package com.schoolerp.modules.students.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassroomBadgeDTO {
    private UUID id;
    private UUID studentId;
    private String studentName;
    private String className;
    private String sectionName;
    private String awardedByStaffName;
    private String badgeCode;
    private String badgeTitle;
    private String badgeCategory;
    private String iconEmoji;
    private String houseName;
    private int housePointsAwarded;
    private String teacherNotes;
    private LocalDateTime awardedAt;
}

