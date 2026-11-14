package com.schoolerp.modules.students.dto;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitPortfolioArtifactRequest {
    private UUID studentId;
    private String studentName;
    private String className;
    private String title;
    private String description;
    private String artifactType; // PROJECT_PHOTO, VIDEO_DEMO, AUDIO_PODCAST, ESSAY_DOCUMENT, CODE_REPO, CERTIFICATE
    private String artifactUrl;
    private String thumbnailUrl;
    private String competencyCategory; // CRITICAL_THINKING, CREATIVITY_INNOVATION, COLLABORATION, SCIENTIFIC_INQUIRY, GLOBAL_CITIZENSHIP
}

