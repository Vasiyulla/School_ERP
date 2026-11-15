package com.schoolerp.modules.admissions.repository;

import com.schoolerp.modules.admissions.entity.AdmissionEntranceTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdmissionEntranceTestRepository extends JpaRepository<AdmissionEntranceTest, UUID> {
    List<AdmissionEntranceTest> findByTenantIdAndInquiryId(String tenantId, UUID inquiryId);
    Optional<AdmissionEntranceTest> findByTenantIdAndTestCode(String tenantId, String testCode);
    Optional<AdmissionEntranceTest> findByTenantIdAndAdmitCardToken(String tenantId, String admitCardToken);
}

