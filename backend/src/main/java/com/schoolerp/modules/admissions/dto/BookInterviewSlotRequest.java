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
public class BookInterviewSlotRequest {
    private UUID inquiryId;
    private String slotType; // CAMPUS_TOUR, ENTRANCE_TEST, PRINCIPAL_INTERVIEW
    private LocalDateTime scheduledAt;
    private String interviewerName;
    private String venueOrRoom;
}

