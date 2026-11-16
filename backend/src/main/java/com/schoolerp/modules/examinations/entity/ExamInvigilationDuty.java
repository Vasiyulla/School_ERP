package com.schoolerp.modules.examinations.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "exam_invigilation_duties", schema = "examinations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamInvigilationDuty {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID examScheduleId;

    @Column(nullable = false)
    private String examName;

    @Column(nullable = false)
    private String subjectName;

    @Column(nullable = false)
    private LocalDate examDate;

    @Column(nullable = false)
    private String hallRoomNumber;

    @Column(nullable = false)
    private UUID invigilatorStaffId;

    @Column(nullable = false)
    private String invigilatorStaffName;

    @Builder.Default
    private String dutyRole = "PRIMARY_INVIGILATOR"; // PRIMARY_INVIGILATOR, RELIEVER, FLYING_SQUAD

    @Builder.Default
    private boolean paperSealVerified = true;

    private LocalDateTime sealVerificationTimestamp;

    private String witnessStaffName;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
