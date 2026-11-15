package com.schoolerp.modules.admissions.repository;

import com.schoolerp.modules.admissions.entity.AdmissionScholarshipApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdmissionScholarshipApplicationRepository extends JpaRepository<AdmissionScholarshipApplication, UUID> {
    List<AdmissionScholarshipApplication> findByTenantIdAndInquiryId(String tenantId, UUID inquiryId);
    List<AdmissionScholarshipApplication> findByTenantIdAndCommitteeStatus(String tenantId, String committeeStatus);
}

