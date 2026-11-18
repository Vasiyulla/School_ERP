package com.schoolerp.modules.admissions.repository;

import com.schoolerp.modules.admissions.entity.AdmissionDocumentUndertaking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdmissionDocumentUndertakingRepository extends JpaRepository<AdmissionDocumentUndertaking, UUID> {
    List<AdmissionDocumentUndertaking> findByTenantIdOrderByCreatedAtDesc(String tenantId);
    Optional<AdmissionDocumentUndertaking> findByTenantIdAndUndertakingNumber(String tenantId, String undertakingNumber);
    List<AdmissionDocumentUndertaking> findByTenantIdAndApplicationId(String tenantId, UUID applicationId);
    List<AdmissionDocumentUndertaking> findByTenantIdAndStatusAndDeadlineDateBefore(String tenantId, String status, LocalDate cutoffDate);
    long countByTenantIdAndStatus(String tenantId, String status);
}
