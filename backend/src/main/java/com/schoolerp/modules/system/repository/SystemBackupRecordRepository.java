package com.schoolerp.modules.system.repository;

import com.schoolerp.modules.system.entity.SystemBackupRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SystemBackupRecordRepository extends JpaRepository<SystemBackupRecord, UUID> {
    List<SystemBackupRecord> findByTenantIdOrderByCreatedAtDesc(String tenantId);
}

