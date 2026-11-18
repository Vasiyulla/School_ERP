package com.schoolerp.modules.admissions.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompleteTokenRequest {
    private String outcomeStatus; // COMPLETED, NO_SHOW, CANCELLED
    private Integer serviceMinutes;
    private String remarks;
}
