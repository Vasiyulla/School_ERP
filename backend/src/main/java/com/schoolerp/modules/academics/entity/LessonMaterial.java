package com.schoolerp.modules.academics.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "lesson_materials", schema = "academics")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    private UUID teacherStaffId;

    @Column(nullable = false)
    private String teacherName;

    @Column(nullable = false)
    private String subjectName; // Physics, Mathematics, History

    @Column(nullable = false)
    private String className; // Grade 10

    @Column(nullable = false)
    private String sectionName; // A, B, or ALL

    @Column(nullable = false)
    private String chapterName; // e.g. Chapter 10: Light - Reflection and Refraction

    private String topicName; // e.g. Ray diagrams for Concave Mirrors

    @Column(nullable = false)
    private String title; // Class Slides & NCERT Solved Ray Diagrams

    @Column(length = 3000)
    private String description;

    @Column(nullable = false)
    private String materialType; // E_BOOK_PDF, PRESENTATION_SLIDES, WHITEBOARD_IMAGE, VIDEO_LECTURE, AUDIO_NOTE, DOCUMENT_NOTES, WEB_LINK

    @Column(nullable = false, length = 1000)
    private String fileUrl; // S3 URI, CDN URL, or external streaming link

    @Column(length = 1000)
    private String thumbnailUrl;

    @Builder.Default
    private Long fileSizeBytes = 0L;

    private String fileExtension; // pdf, pptx, mp4, jpg

    @Column(nullable = false)
    private LocalDate lessonDate;

    @Column(nullable = false)
    @Builder.Default
    private boolean isDownloadable = true;

    @Column(nullable = false)
    @Builder.Default
    private Integer viewCount = 0;

    @Column(nullable = false)
    @Builder.Default
    private String status = "PUBLISHED"; // PUBLISHED, DRAFT, ARCHIVED

    @Column(nullable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();
}

