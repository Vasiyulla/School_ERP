package com.schoolerp.modules.admissions.dto;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionQueueTokenDTO {
    private UUID id;
    private String tenantId;
    private String tokenNumber;
    private String category;
    private String visitorName;
    private String visitorPhone;
    private String studentName;
    private String gradeApplying;
    private String status;
    private String assignedCounter;
    private UUID assignedOfficerId;
    private Integer waitingMinutes;
    private Integer serviceMinutes;
    private String notes;
    private Instant calledAt;
    private Instant completedAt;
    private Instant createdAt;
}
