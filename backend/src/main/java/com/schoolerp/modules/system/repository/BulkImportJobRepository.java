package com.schoolerp.modules.system.repository;

import com.schoolerp.modules.system.entity.BulkImportJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BulkImportJobRepository extends JpaRepository<BulkImportJob, UUID> {
    List<BulkImportJob> findByTenantIdOrderByCreatedAtDesc(String tenantId);
}

