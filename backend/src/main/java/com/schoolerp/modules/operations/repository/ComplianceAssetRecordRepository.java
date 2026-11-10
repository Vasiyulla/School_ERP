package com.schoolerp.modules.operations.repository;

import com.schoolerp.modules.operations.entity.ComplianceAssetRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ComplianceAssetRecordRepository extends JpaRepository<ComplianceAssetRecord, UUID> {
    List<ComplianceAssetRecord> findByTenantIdOrderByExpiryDateAsc(String tenantId);
}

