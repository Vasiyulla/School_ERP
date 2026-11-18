package com.schoolerp.modules.admissions.dto;

import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionDocumentUndertakingDTO {
    private UUID id;
    private String tenantId;
    private String undertakingNumber;
    private UUID applicationId;
    private String studentName;
    private String parentName;
    private String missingDocumentName;
    private String reasonCode;
    private LocalDate deadlineDate;
    private String status;
    private UUID verifiedByOfficerId;
    private Boolean parentSignatureAck;
    private LocalDate fulfillmentDate;
    private String fulfillmentRemarks;
    private Boolean isOverdue;
    private Instant createdAt;
}
