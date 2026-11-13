package com.schoolerp.modules.attendance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "student_leave_applications", schema = "attendance")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentLeaveApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID studentId;

    private String studentAdmissionNo;

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    private String className;

    @Column(nullable = false)
    private String sectionName;

    private UUID parentUserId;

    private String parentName;

    private String parentContactPhone;

    @Column(nullable = false)
    private String leaveType; // SICK_LEAVE, CASUAL, FAMILY_EMERGENCY, SPORTS_DUTY, PLANNED_TRAVEL

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private int totalDays;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String reason;

    private String medicalCertificateUrl;

    @Column(nullable = false)
    @Builder.Default
    private String status = "PENDING"; // PENDING, APPROVED, REJECTED, WITHDRAWN

    private UUID reviewedByStaffId;

    private String reviewedByStaffName;

    private String reviewRemarks;

    private LocalDateTime reviewedAt;

    @Builder.Default
    private LocalDateTime appliedAt = LocalDateTime.now();
}

