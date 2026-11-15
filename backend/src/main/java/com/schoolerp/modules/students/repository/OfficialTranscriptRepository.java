package com.schoolerp.modules.students.repository;

import com.schoolerp.modules.students.entity.OfficialTranscript;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OfficialTranscriptRepository extends JpaRepository<OfficialTranscript, UUID> {
    List<OfficialTranscript> findByTenantIdAndStudentId(String tenantId, UUID studentId);
    Optional<OfficialTranscript> findByTenantIdAndTranscriptNumber(String tenantId, String transcriptNumber);
    Optional<OfficialTranscript> findByTenantIdAndVerificationHash(String tenantId, String verificationHash);
}

