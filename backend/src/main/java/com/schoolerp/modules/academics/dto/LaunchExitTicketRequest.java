package com.schoolerp.modules.academics.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LaunchExitTicketRequest {
    private String className;
    private String sectionName;
    private String subjectName;
    private String topicName;
    private String questionText;
    private String questionType; // MCQ, CONFIDENCE_ONLY, SHORT_TEXT
    private List<String> options;
    private String correctOption;
}

