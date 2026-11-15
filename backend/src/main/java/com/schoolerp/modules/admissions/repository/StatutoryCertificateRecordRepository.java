package com.schoolerp.modules.admissions.repository;

import com.schoolerp.modules.admissions.entity.StatutoryCertificateRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StatutoryCertificateRecordRepository extends JpaRepository<StatutoryCertificateRecord, UUID> {
    List<StatutoryCertificateRecord> findByTenantIdAndInquiryId(String tenantId, UUID inquiryId);
    Optional<StatutoryCertificateRecord> findByTenantIdAndInquiryIdAndCertificateType(String tenantId, UUID inquiryId, String certificateType);
}

