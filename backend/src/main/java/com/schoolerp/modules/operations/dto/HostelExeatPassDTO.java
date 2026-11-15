package com.schoolerp.modules.operations.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HostelExeatPassDTO {
    private UUID id;
    private UUID studentId;
    private String studentName;
    private String studentAdmissionNumber;
    private String passNumber;
    private String exeatType;
    private LocalDateTime departureTime;
    private LocalDateTime expectedReturnTime;
    private LocalDateTime actualReturnTime;
    private boolean parentAuthorized;
    private String parentContactPhone;
    private String status;
    private LocalDateTime gateDepartureScannedAt;
}

