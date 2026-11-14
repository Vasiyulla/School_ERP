package com.schoolerp.modules.institution.dto;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitSafetyCheckInRequest {
    private UUID userId;
    private String userName;
    private String userRole; // STUDENT, STAFF, PARENT
    private String safetyStatus; // SAFE_AT_HOME, EVACUATED_TO_SHELTER, NEED_URGENT_ASSISTANCE, MEDICAL_EMERGENCY
    private String currentLocation;
    private String emergencyContactPhone;
    private String assistanceDetails;
}

