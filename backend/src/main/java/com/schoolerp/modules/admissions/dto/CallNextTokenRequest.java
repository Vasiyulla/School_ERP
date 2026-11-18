package com.schoolerp.modules.admissions.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CallNextTokenRequest {

    @NotBlank(message = "Assigned counter is required (e.g. Counter 1, Counter 2)")
    private String counterName;

    private String preferredCategory; // Optional category filter: ENQUIRY, DOCUMENT_VERIFICATION, etc.

    private UUID officerId;
}
