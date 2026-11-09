package com.schoolerp.modules.examinations.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportCardDTO {
    private UUID reportCardId;
    private String verificationHash;
    private String verificationUrl;

    // School Affiliation Metadata
    private String tenantId;
    private String schoolName;
    private String affiliationNumber;
    private String academicYear;
    private String termName;

    // Student Master Info
    private UUID studentId;
    private String studentName;
    private String admissionNumber;
    private String className;
    private String sectionName;
    private Integer rollNumber;

    // Subject Scores & Performance Breakdown
    private List<SubjectPerformanceDTO> subjectPerformances;

    // Cumulative Aggregates
    private int totalMaxMarks;
    private int totalMarksObtained;
    private BigDecimal percentage;
    private BigDecimal cgpa; // Cumulative Grade Point Average out of 10.0
    private String overallGrade;
    private String resultStatus; // PASSED - DISTINCTION, PASSED - FIRST CLASS, PROMOTED, ESSENTIAL REPEAT

    // Class Positioning
    private int classRank;
    private int totalStudentsInClass;

    // Attendance Standing
    private Double attendancePercentage;

    // Certification Status
    private String principalSignatureStatus; // DIGITALLY_SIGNED_SHA256
    private Instant issuedAt;
}

