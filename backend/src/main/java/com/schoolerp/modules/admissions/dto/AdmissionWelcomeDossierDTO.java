package com.schoolerp.modules.admissions.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionWelcomeDossierDTO {
    private UUID id;
    private String tenantId;
    private UUID studentId;
    private String admissionNumber;
    private String studentName;
    private String gradeName;
    private String sectionName;
    private String rollNumber;
    private String houseName;
    private String smartCardRfidUid;
    private String smartCardStatus;
    private BigDecimal canteenInitialBalance;
    private Boolean welcomePackGenerated;
    private UUID mintedByUserId;
    private Instant mintedAt;
    
    // Printable Welcome Pack Dossier Components
    private String admissionLetterNumber;
    private String temporaryIdBadgeSlip;
    private String busPassClearanceToken;
    private String uniformBookstoreVoucher;
    private String studentPortalAccessPass;
}
