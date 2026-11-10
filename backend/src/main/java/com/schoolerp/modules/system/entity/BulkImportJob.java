package com.schoolerp.modules.system.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "bulk_import_jobs", schema = "system")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulkImportJob {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String entityType; // STUDENTS, STAFF, FEE_OPENING_BALANCES

    @Column(nullable = false)
    private String fileName;

    private int totalRows;
    private int successCount;
    private int failedCount;

    @Column(nullable = false)
    @Builder.Default
    private String status = "COMPLETED"; // PROCESSING, COMPLETED, COMPLETED_WITH_ERRORS, FAILED

    @Column(length = 4000)
    private String errorDetails;

    @Column(nullable = false)
    private String initiatedBy;

    @Builder.Default
    private Instant createdAt = Instant.now();
}

