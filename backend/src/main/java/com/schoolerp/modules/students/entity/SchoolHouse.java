package com.schoolerp.modules.students.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "school_houses", schema = "students")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchoolHouse {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false, unique = true)
    private String houseName; // e.g. "Mars (Red)", "Neptune (Blue)", "Terra (Green)", "Apollo (Yellow)"

    @Column(nullable = false)
    private String houseColorHex; // e.g. #EF4444, #3B82F6, #10B981, #F59E0B

    private String houseMotto; // e.g. "Strength & Valor", "Wisdom & Integrity"

    private String houseMasterStaffName; // "Dr. Vikram Seth"

    private String houseCaptainStudentName; // "Aarav Sharma"

    @Column(nullable = false)
    @Builder.Default
    private int totalPoints = 0;
}

