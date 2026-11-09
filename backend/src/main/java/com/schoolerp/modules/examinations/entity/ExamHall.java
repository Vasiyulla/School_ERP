package com.schoolerp.modules.examinations.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "exam_halls", schema = "examinations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamHall {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String hallName;

    @Column(nullable = false)
    private String roomNumber;

    @Builder.Default
    private int totalBenches = 30;

    @Builder.Default
    private int seatsPerBench = 2;

    @Builder.Default
    private int totalCapacity = 60;

    @Builder.Default
    private String status = "ACTIVE";
}

