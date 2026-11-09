package com.schoolerp.modules.examinations.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "exam_seating_slots", schema = "examinations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamSeatingSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID hallId;

    @Column(nullable = false)
    private String hallName;

    @Column(nullable = false)
    private LocalDate examDate;

    @Column(nullable = false)
    private String examSession; // MORNING, AFTERNOON

    @Column(nullable = false)
    private int deskNumber;

    private int rowNumber;

    private int columnNumber;

    @Column(nullable = false)
    private UUID studentId;

    @Column(nullable = false)
    private String studentName;

    private String rollNumber;

    @Column(nullable = false)
    private String className;

    @Column(nullable = false)
    private String subject;
}

