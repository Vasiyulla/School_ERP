package com.schoolerp.modules.admissions.repository;

import com.schoolerp.modules.admissions.entity.AdmissionQueueToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdmissionQueueTokenRepository extends JpaRepository<AdmissionQueueToken, UUID> {
    List<AdmissionQueueToken> findByTenantIdOrderByCreatedAtAsc(String tenantId);
    List<AdmissionQueueToken> findByTenantIdAndStatusOrderByCreatedAtAsc(String tenantId, String status);
    Optional<AdmissionQueueToken> findByTenantIdAndTokenNumber(String tenantId, String tokenNumber);
    long countByTenantIdAndStatus(String tenantId, String status);
}
