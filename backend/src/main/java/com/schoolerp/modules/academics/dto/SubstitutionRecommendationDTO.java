package com.schoolerp.modules.academics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubstitutionRecommendationDTO {
    private int periodNumber;
    private String timeSlot;
    private String className;
    private String sectionName;
    private String subject;
    private String roomNumber;
    private String absentTeacherName;
    private UUID absentTeacherId;
    private List<AvailableSubstituteDTO> candidateSubstitutes;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AvailableSubstituteDTO {
        private UUID teacherId;
        private String teacherName;
        private String department;
        private String designation;
        private String matchSuitability; // SAME_DEPARTMENT_EXPERT, FREE_SENIOR_FACULTY
        private int currentDailyPeriods;
    }
}

