package com.schoolerp.modules.academics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonMaterialDTO {
    private UUID id;
    private String teacherName;
    private String subjectName;
    private String className;
    private String sectionName;
    private String chapterName;
    private String topicName;
    private String title;
    private String description;
    private String materialType;
    private String fileUrl;
    private String thumbnailUrl;
    private Long fileSizeBytes;
    private String formattedFileSize; // e.g. "15.2 MB"
    private String fileExtension;
    private LocalDate lessonDate;
    private boolean isDownloadable;
    private Integer viewCount;
    private String status;
    private Instant createdAt;
}

