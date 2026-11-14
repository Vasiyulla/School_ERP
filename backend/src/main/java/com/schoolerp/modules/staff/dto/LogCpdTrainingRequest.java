package com.schoolerp.modules.staff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogCpdTrainingRequest {
    private UUID staffId;
    private String teacherName;
    private String trainingYear; // e.g. "2026-2027"
    private String agency;       // CBSE_COE, DIKSHA, NISHTHA, SAHODAYA, IN_HOUSE
    private String trainingTopic;
    private String domain;       // PEDAGOGY, INCLUSIVE_EDUCATION, CYBER_SAFETY, NEP_IMPLEMENTATION
    private Integer durationHours;
    private LocalDate completionDate;
    private String certificateRef;
    private Double evaluationScore;
}

