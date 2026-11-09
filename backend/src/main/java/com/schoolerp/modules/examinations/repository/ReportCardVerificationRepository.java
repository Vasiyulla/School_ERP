package com.schoolerp.modules.examinations.repository;

import com.schoolerp.modules.examinations.entity.ReportCardVerificationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReportCardVerificationRepository extends JpaRepository<ReportCardVerificationRecord, UUID> {
    Optional<ReportCardVerificationRecord> findByVerificationHash(String verificationHash);
    Optional<ReportCardVerificationRecord> findByTenantIdAndStudentIdAndTermName(String tenantId, UUID studentId, String termName);
}
