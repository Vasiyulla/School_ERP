package com.schoolerp.modules.admissions.repository;

import com.schoolerp.modules.admissions.entity.AdmissionPointCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdmissionPointCriteriaRepository extends JpaRepository<AdmissionPointCriteria, UUID> {
    Optional<AdmissionPointCriteria> findByTenantIdAndInquiryId(String tenantId, UUID inquiryId);
    List<AdmissionPointCriteria> findByTenantIdOrderByTotalCalculatedPointsDesc(String tenantId);
}

