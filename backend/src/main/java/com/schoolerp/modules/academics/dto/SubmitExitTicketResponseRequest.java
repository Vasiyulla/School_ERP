package com.schoolerp.modules.academics.dto;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitExitTicketResponseRequest {
    private UUID sessionId;
    private String sessionPin;
    private UUID studentId;
    private String studentName;
    private String selectedAnswer;
    private int confidenceScore; // 1 = Struggling, 2 = Unsure, 3 = Understood
    private String studentNotes;
}

