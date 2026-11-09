package com.schoolerp.modules.system.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pod_backups", schema = "system")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemBackupRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "backup_type", nullable = false)
    private String backupType; // FULL_POD_SNAPSHOT, DIFFERENTIAL, SCHEMA_ONLY

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_size_bytes", nullable = false)
    private Long fileSizeBytes;

    @Column(name = "sha256_checksum", nullable = false)
    private String sha256Checksum;

    @Column(name = "status", nullable = false)
    private String status; // COMPLETED, IN_PROGRESS, FAILED

    @Column(name = "storage_location", nullable = false)
    private String storageLocation;

    @Column(name = "initiated_by", nullable = false)
    private String initiatedBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}

