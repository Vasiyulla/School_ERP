package com.schoolerp.modules.institution.repository;

import com.schoolerp.modules.institution.entity.ConfidentialSafetyIncident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConfidentialSafetyIncidentRepository extends JpaRepository<ConfidentialSafetyIncident, UUID> {

    List<ConfidentialSafetyIncident> findAllByTenantIdOrderByReportedAtDesc(String tenantId);

    List<ConfidentialSafetyIncident> findAllByTenantIdAndStatusOrderByReportedAtDesc(String tenantId, String status);

    List<ConfidentialSafetyIncident> findAllByTenantIdAndClassificationOrderByReportedAtDesc(String tenantId, String classification);

    Optional<ConfidentialSafetyIncident> findByIdAndTenantId(UUID id, String tenantId);

    long countByTenantId(String tenantId);
}

