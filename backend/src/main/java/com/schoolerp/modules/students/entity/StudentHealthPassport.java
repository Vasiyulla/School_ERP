package com.schoolerp.modules.students.entity;

import com.schoolerp.common.security.AesGcmAttributeEncryptor;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "student_health_passports", schema = "students",
       uniqueConstraints = @UniqueConstraint(name = "uq_tenant_student_passport", columnNames = {"tenantId", "studentId"}))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentHealthPassport {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID studentId;

    @Column(nullable = false)
    private String bloodGroup; // e.g. "O+", "A-", "B+"

    @Builder.Default
    private boolean hasSevereAllergies = false;

    @Column(columnDefinition = "TEXT")
    private String allergenList; // Comma-separated: "PEANUTS,GLUTEN,DAIRY"

    @Convert(converter = AesGcmAttributeEncryptor.class)
    @Column(columnDefinition = "TEXT")
    private String chronicConditions; // "ASTHMA,TYPE_1_DIABETES"

    @Builder.Default
    private boolean inhalerInInfirmary = false;

    @Builder.Default
    private boolean epipenPrescribed = false;

    @Column(nullable = false)
    private String emergencyContactPhone;

    private String treatingPhysicianName;

    @Convert(converter = AesGcmAttributeEncryptor.class)
    @Column(nullable = false, columnDefinition = "TEXT")
    private String paramedicEmergencyProtocol;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
