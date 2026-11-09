package com.schoolerp.modules.examinations.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "invigilation_duties", schema = "examinations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvigilationDuty {

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
    private String timeSlot;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private UUID teacherId;

    @Column(nullable = false)
    private String teacherName;

    @Builder.Default
    private String status = "ASSIGNED";
}

