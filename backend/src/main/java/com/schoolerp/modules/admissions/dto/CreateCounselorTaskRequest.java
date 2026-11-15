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
public class CreateCounselorTaskRequest {
    private UUID inquiryId;
    private String counselorName;
    private String taskType; // VERIFICATION_CALL, TOUR_REMINDER, MISSING_DOC_CHASER, OFFER_FOLLOWUP
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private String priority; // URGENT, HIGH, NORMAL
}

