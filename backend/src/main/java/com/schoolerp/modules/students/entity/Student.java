package com.schoolerp.modules.students.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "students", schema = "students")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String admissionNumber; // e.g. ADM-2024-0012

    @Column(nullable = false)
    private String fullName;

    private String gender; // Male, Female

    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private String className; // e.g. Class 10

    @Column(nullable = false)
    private String sectionName; // e.g. Section A

    private int rollNumber;

    @Builder.Default
    private String status = "Active"; // Active, Transferred, Graduated

    private String guardianName;
    private String guardianRelationship;
    private String guardianPhone;
    private String guardianEmail;

    @Builder.Default
    private double attendancePercentage = 95.0;

    @Builder.Default
    private String feeStatus = "Paid"; // Paid, Partial, Overdue

    @Builder.Default
    private double balanceAmount = 0.0;

    private String lockerIndex; // e.g. Locker B-12
}

