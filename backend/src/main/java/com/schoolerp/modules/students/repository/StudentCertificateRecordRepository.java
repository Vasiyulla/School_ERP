package com.schoolerp.modules.students.repository;

import com.schoolerp.modules.students.entity.StudentCertificateRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentCertificateRecordRepository extends JpaRepository<StudentCertificateRecord, UUID> {
    List<StudentCertificateRecord> findByTenantIdOrderByIssuedAtDesc(String tenantId);
    List<StudentCertificateRecord> findByTenantIdAndStudentId(String tenantId, UUID studentId);
    Optional<StudentCertificateRecord> findByTenantIdAndQrVerificationToken(String tenantId, String qrVerificationToken);
    Optional<StudentCertificateRecord> findByQrVerificationToken(String qrVerificationToken);
}

