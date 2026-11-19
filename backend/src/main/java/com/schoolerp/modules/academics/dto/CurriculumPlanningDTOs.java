package com.schoolerp.modules.academics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

public class CurriculumPlanningDTOs {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateUnitRequest {
        private String gradeLevel;
        private String subjectName;
        private String unitTitle;
        private int allocatedPeriods;
        private String learningObjectives;
        private String cbseCompetencies;
        private String term;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApproveUnitRequest {
        private String status; // APPROVED, REVISION_REQUESTED
        private String remarks;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubmitLessonPlanRequest {
        private UUID curriculumUnitId;
        private String gradeLevel;
        private String subjectName;
        private int weekNumber;
        private String topicsPlanned;
        private String teachingPedagogy;
        private String assessmentMethod;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PacingRadarDTO {
        private String gradeLevel;
        private String subjectName;
        private int totalUnits;
        private int totalAllocatedPeriods;
        private int totalCompletedPeriods;
        private double actualPacingPercentage;
        private double targetPacingPercentage;
        private String pacingStatus; // AHEAD, ON_TRACK, BEHIND_SCHEDULE
        private String alertNotice;
        private List<UnitProgressDTO> units;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UnitProgressDTO {
        private UUID id;
        private String unitCode;
        private String unitTitle;
        private int allocatedPeriods;
        private int completedPeriods;
        private String status;
        private double completionRate;
    }
}
