package com.schoolerp.modules.system.repository;

import com.schoolerp.modules.system.entity.CustomEntityRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomEntityRecordRepository extends JpaRepository<CustomEntityRecord, UUID> {
    List<CustomEntityRecord> findByTenantIdAndEntityCodeAndStatusOrderByUpdatedAtDesc(String tenantId, String entityCode, String status);
    List<CustomEntityRecord> findByTenantIdAndEntityCodeOrderByUpdatedAtDesc(String tenantId, String entityCode);
    Optional<CustomEntityRecord> findByTenantIdAndEntityCodeAndId(String tenantId, String entityCode, UUID id);
    long countByTenantIdAndEntityCodeAndStatus(String tenantId, String entityCode, String status);
}

