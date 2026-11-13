package com.schoolerp.modules.institution.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogSafetyIncidentRequest {

    @NotBlank(message = "Classification is mandatory (POCSO_STATUTORY, POSH_STAFF, PEER_BULLYING_SEVERE, CYBER_HARASSMENT, SUBSTANCE_ABUSE)")
    private String classification;

    @Builder.Default
    private String severityLevel = "HIGH_AMBER"; // CRITICAL_RED, HIGH_AMBER, MEDIUM_YELLOW

    private UUID involvedStudentOrStaffId;

    @NotBlank(message = "Involved party name is mandatory")
    private String involvedPartyName;

    @Builder.Default
    private String roleInvolved = "STUDENT";

    @NotBlank(message = "Incident summary is mandatory")
    private String incidentSummary;

    private String actionTakenInterim;

    @Builder.Default
    private boolean statutoryNotificationRequired = false;

    private String statutoryAuthorityRef;

    private String inquiryCommitteeMembers;
}

