package com.schoolerp.modules.institution.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmergencySafetyCheckInDTO {
    private UUID id;
    private UUID userId;
    private String userName;
    private String userRole;
    private String safetyStatus;
    private String currentLocation;
    private String emergencyContactPhone;
    private String assistanceDetails;
    private LocalDateTime reportedAt;
}

