package com.schoolerp.modules.admissions.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DispenseQueueTokenRequest {

    @NotBlank(message = "Category is required (ENQUIRY, DOCUMENT_VERIFICATION, FEE_PAYMENT, PRINCIPAL_INTERVIEW)")
    private String category;

    @NotBlank(message = "Visitor name is required")
    private String visitorName;

    @NotBlank(message = "Visitor phone is required")
    private String visitorPhone;

    private String studentName;

    private String gradeApplying;

    private String notes;
}
