package com.schoolerp.modules.system.repository;

import com.schoolerp.modules.system.entity.BatchJobExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BatchJobExecutionRepository extends JpaRepository<BatchJobExecution, UUID> {
    Optional<BatchJobExecution> findByJobIdAndTenantId(String jobId, String tenantId);
    Optional<BatchJobExecution> findByJobId(String jobId);
    long countByTenantId(String tenantId);
}
