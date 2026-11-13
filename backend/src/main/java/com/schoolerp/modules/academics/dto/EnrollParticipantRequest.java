package com.schoolerp.modules.academics.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollParticipantRequest {

    @NotNull(message = "Participant items list cannot be null")
    private List<ParticipantItem> participants;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParticipantItem {
        @NotNull(message = "Student ID is required")
        private UUID studentId;

        @Builder.Default
        private String role = "PARTICIPANT"; // PARTICIPANT, TEAM_CAPTAIN, SOLO_PERFORMER, MC_ANCHOR, VOLUNTEER_ORGANIZER
    }
}

