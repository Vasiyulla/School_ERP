package com.schoolerp.modules.academics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublishLessonMaterialRequest {
    private String subjectName;
    private String className;
    private String sectionName; // A, B, or ALL
    private String chapterName;
    private String topicName;
    private String title;
    private String description;
    private String materialType; // E_BOOK_PDF, PRESENTATION_SLIDES, WHITEBOARD_IMAGE, VIDEO_LECTURE, AUDIO_NOTE, DOCUMENT_NOTES, WEB_LINK
    private String fileUrl;
    private String thumbnailUrl;
    private Long fileSizeBytes;
    private String fileExtension;
    private LocalDate lessonDate;
    private Boolean isDownloadable;
}

