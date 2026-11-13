package com.schoolerp.modules.academics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordParticipantResultRequest {

    @Builder.Default
    private String participationStatus = "ATTENDED"; // ATTENDED, ABSENT, WITHDRAWN

    @Builder.Default
    private String achievementTier = "NONE"; // NONE, FIRST_PLACE, SECOND_PLACE, THIRD_PLACE, CONSOLATION, SPECIAL_RECOGNITION

    @Builder.Default
    private int housePointsAwarded = 0;

    @Builder.Default
    private boolean issueCertificate = false;

    private String teacherRemarks;
}

