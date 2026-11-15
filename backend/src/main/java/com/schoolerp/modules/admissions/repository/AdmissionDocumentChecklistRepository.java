package com.schoolerp.modules.admissions.repository;

import com.schoolerp.modules.admissions.entity.AdmissionDocumentChecklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdmissionDocumentChecklistRepository extends JpaRepository<AdmissionDocumentChecklist, UUID> {
    List<AdmissionDocumentChecklist> findByTenantIdAndInquiryIdOrderByCreatedAtAsc(String tenantId, UUID inquiryId);
    Optional<AdmissionDocumentChecklist> findByIdAndTenantId(UUID id, String tenantId);
    long countByTenantIdAndInquiryIdAndStatus(String tenantId, UUID inquiryId, String status);
    long countByTenantIdAndInquiryId(String tenantId, UUID inquiryId);
}

