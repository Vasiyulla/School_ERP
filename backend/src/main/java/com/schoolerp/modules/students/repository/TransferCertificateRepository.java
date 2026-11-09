package com.schoolerp.modules.students.repository;

import com.schoolerp.modules.students.entity.TransferCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransferCertificateRepository extends JpaRepository<TransferCertificate, UUID> {
    List<TransferCertificate> findAllByTenantId(String tenantId);
    Optional<TransferCertificate> findByTenantIdAndTcNumber(String tenantId, String tcNumber);
    Optional<TransferCertificate> findByTenantIdAndStudentId(String tenantId, UUID studentId);
    Optional<TransferCertificate> findByVerificationHash(String verificationHash);
    long countByTenantId(String tenantId);
}

