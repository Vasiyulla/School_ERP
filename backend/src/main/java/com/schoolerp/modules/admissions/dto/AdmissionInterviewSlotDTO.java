package com.schoolerp.modules.admissions.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionInterviewSlotDTO {
    private UUID id;
    private String tenantId;
    private UUID inquiryId;
    private String slotType;
    private LocalDateTime scheduledAt;
    private String interviewerName;
    private String venueOrRoom;
    private String status;
    private Integer evaluationRating;
    private String interviewerFeedback;
    private String recommendedGrade;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

