package com.schoolerp.modules.system.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "audit_logs", schema = "system")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String actorName; // e.g. Dr. Rajesh Sharma

    @Column(nullable = false)
    private String actorRole; // SUPER_ADMIN, TEACHER, PRINCIPAL

    @Column(nullable = false)
    private String action; // CREATE, UPDATE, DELETE, OVERRIDE, LOGIN, EXPORT

    @Column(nullable = false)
    private String resourceType; // students.student_master, attendance.correction, finance.ledger

    private String resourceId; // e.g. STU-1001, ATT-10A

    @Column(length = 2000)
    private String details;

    private String clientIp;

    @Builder.Default
    private String status = "SUCCESS"; // SUCCESS, DENIED, FLAGGED

    @Builder.Default
    private Instant timestamp = Instant.now();
}

