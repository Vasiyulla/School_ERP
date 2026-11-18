package com.schoolerp.modules.students.entity;

import com.schoolerp.common.security.AesGcmAttributeEncryptor;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "student_udise_profiles", schema = "students")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentUdiseProfile {

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

    @Convert(converter = AesGcmAttributeEncryptor.class)
    @Column(length = 255)
    private String apaarId; // Automated Permanent Academic Account Registry (12 digits)

    @Convert(converter = AesGcmAttributeEncryptor.class)
    @Column(length = 255)
    private String penNumber; // Permanent Education Number (11 digits issued by MoE)

    @Column(nullable = false)
    @Builder.Default
    private String socialCategory = "GENERAL"; // GENERAL, SC, ST, OBC_NCL, OBC_CL, EWS

    @Column(nullable = false)
    @Builder.Default
    private String religion = "HINDU";

    @Column(nullable = false)
    @Builder.Default
    private String motherTongue = "HINDI";

    @Builder.Default
    private boolean bplStatus = false;

    @Column(length = 50)
    @Builder.Default
    private String cwsnDisabilityCode = "NONE"; // Children with Special Needs (RPwD Act)

    @Builder.Default
    private boolean rteAdmission = false;

    @Builder.Default
    private Instant updatedAt = Instant.now();
}

