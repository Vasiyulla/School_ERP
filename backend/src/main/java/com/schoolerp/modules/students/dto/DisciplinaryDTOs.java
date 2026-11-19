package com.schoolerp.modules.students.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class DisciplinaryDTOs {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConveneHearingRequest {
        private UUID studentId;
        private String studentName;
        private String gradeLevel;
        private LocalDate incidentDate;
        private String infractionCategory;
        private String chargeDescription;
        private LocalDate hearingDate;
        private String committeeMembers;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecordSanctionRequest {
        private String findingsSummary;
        private String sanctionType;
        private int durationDays;
        private LocalDate startDate;
        private LocalDate endDate;
        private String sanctionTerms;
        private String reinstatementConditions;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudentDisciplinaryHistoryDTO {
        private UUID studentId;
        private String studentName;
        private int totalHearings;
        private int activeSanctions;
        private List<HearingSummaryDTO> hearings;
        private List<SanctionSummaryDTO> sanctions;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HearingSummaryDTO {
        private UUID id;
        private String docketNumber;
        private LocalDate hearingDate;
        private String infractionCategory;
        private String status;
        private String findingsSummary;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SanctionSummaryDTO {
        private UUID id;
        private String sanctionType;
        private int durationDays;
        private LocalDate startDate;
        private LocalDate endDate;
        private String sanctionTerms;
        private String authorizedBy;
    }
}
