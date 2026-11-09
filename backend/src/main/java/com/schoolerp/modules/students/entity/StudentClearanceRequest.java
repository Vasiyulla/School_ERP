package com.schoolerp.modules.students.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "student_clearance_requests", schema = "students")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentClearanceRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID studentId;

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    private String admissionNumber;

    @Column(nullable = false)
    private String className;

    @Column(nullable = false)
    private LocalDateTime requestDate;

    // 1. Library Clearance
    @Builder.Default
    private boolean libraryCleared = false;
    private String libraryRemarks; // "All books returned" or "1 book overdue: HC Verma Physics"

    // 2. Bursar / Finance Clearance
    @Builder.Default
    private boolean bursarCleared = false;
    @Builder.Default
    private BigDecimal pendingFeeDues = BigDecimal.ZERO;
    private String bursarRemarks; // "All tuition dues settled" or "INR 15,000 pending for Term 2"

    // 3. Sports & Laboratory Equipment Clearance
    @Builder.Default
    private boolean sportsLabCleared = false;
    private String sportsLabRemarks; // "Athletics gear & lab glassware accounted"

    // 4. Class Teacher & Academic Conduct Clearance
    @Builder.Default
    private boolean academicCleared = false;
    private String academicRemarks; // "Promoted to Class 11. Conduct: Exemplary"

    @Column(nullable = false)
    @Builder.Default
    private String clearanceStatus = "IN_PROGRESS"; // IN_PROGRESS, CLEARED, BLOCKED

    private LocalDateTime completedAt;
}

