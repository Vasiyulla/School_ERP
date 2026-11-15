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
public class AdmissionDocumentChecklistDTO {
    private UUID id;
    private String tenantId;
    private UUID inquiryId;
    private String documentType;
    private String documentName;
    private String storageUrl;
    private String status;
    private String rejectionReason;
    private String verifiedByName;
    private LocalDateTime verifiedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

