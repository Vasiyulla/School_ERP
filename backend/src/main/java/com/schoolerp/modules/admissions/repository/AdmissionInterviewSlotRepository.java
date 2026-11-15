package com.schoolerp.modules.admissions.repository;

import com.schoolerp.modules.admissions.entity.AdmissionInterviewSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdmissionInterviewSlotRepository extends JpaRepository<AdmissionInterviewSlot, UUID> {
    List<AdmissionInterviewSlot> findByTenantIdAndInquiryIdOrderByScheduledAtAsc(String tenantId, UUID inquiryId);
    List<AdmissionInterviewSlot> findByTenantIdOrderByScheduledAtDesc(String tenantId);
    Optional<AdmissionInterviewSlot> findByIdAndTenantId(UUID id, String tenantId);
}

