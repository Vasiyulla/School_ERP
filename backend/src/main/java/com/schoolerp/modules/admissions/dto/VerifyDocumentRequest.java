package com.schoolerp.modules.admissions.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifyDocumentRequest {
    private String status; // VERIFIED, REJECTED_REUPLOAD_REQUIRED
    private String rejectionReason;
    private String verifiedByName;
}

