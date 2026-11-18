package com.schoolerp.modules.admissions.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueUndertakingRequest {

    @NotNull(message = "Application ID is required")
    private UUID applicationId;

    @NotBlank(message = "Student name is required")
    private String studentName;

    @NotBlank(message = "Parent name is required")
    private String parentName;

    @NotBlank(message = "Missing document name is required (e.g. Previous School TC with DEO Counter-Signature)")
    private String missingDocumentName;

    @NotBlank(message = "Reason code is required (e.g. PENDING_PREVIOUS_SCHOOL_CLEARANCE, INTERSTATE_TRANSFER_PROCESSING)")
    private String reasonCode;

    @NotNull(message = "Deadline date is required")
    private LocalDate deadlineDate;

    @NotNull(message = "Verifying officer ID is required")
    private UUID verifiedByOfficerId;

    @Builder.Default
    private Boolean parentSignatureAck = true;
}
