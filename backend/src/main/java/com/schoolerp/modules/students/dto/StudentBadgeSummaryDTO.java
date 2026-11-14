package com.schoolerp.modules.students.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentBadgeSummaryDTO {
    private UUID studentId;
    private String studentName;
    private String className;
    private String houseName;
    private int totalBadgesCount;
    private int totalHousePointsContributed;
    private List<ClassroomBadgeDTO> recentBadges;
}

