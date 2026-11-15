package com.schoolerp.modules.operations.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CampusVisitorDTO {
    private UUID id;
    private String visitorPassNumber;
    private String visitorName;
    private String visitorPhone;
    private String idProofType;
    private String idProofNumber;
    private String purposeOfVisit;
    private UUID hostStaffId;
    private String hostStaffName;
    private String hostApprovalStatus;
    private String entryGate;
    private String badgeQrToken;
    private LocalDateTime checkedInAt;
    private LocalDateTime checkedOutAt;
    private String status;
    private boolean isOverstayed;
}

