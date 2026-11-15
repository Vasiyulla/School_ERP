package com.schoolerp.modules.admissions.repository;

import com.schoolerp.modules.admissions.entity.AdmissionInquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdmissionInquiryRepository extends JpaRepository<AdmissionInquiry, UUID> {
    List<AdmissionInquiry> findByTenantIdOrderByCreatedAtDesc(String tenantId);
    List<AdmissionInquiry> findByTenantIdAndPipelineStage(String tenantId, String pipelineStage);
    List<AdmissionInquiry> findByTenantIdAndAcademicYear(String tenantId, String academicYear);
    List<AdmissionInquiry> findByTenantIdAndGradeApplyingFor(String tenantId, String gradeApplyingFor);
    List<AdmissionInquiry> findByTenantIdAndGradeApplyingForAndAdmissionQuotaAndPipelineStage(String tenantId, String gradeApplyingFor, String admissionQuota, String pipelineStage);
    Optional<AdmissionInquiry> findByIdAndTenantId(UUID id, String tenantId);
    Optional<AdmissionInquiry> findByTenantIdAndInquiryNumber(String tenantId, String inquiryNumber);
    long countByTenantIdAndPipelineStage(String tenantId, String pipelineStage);
    long countByTenantIdAndGradeApplyingForAndPipelineStage(String tenantId, String gradeApplyingFor, String pipelineStage);
    long countByTenantId(String tenantId);
}

