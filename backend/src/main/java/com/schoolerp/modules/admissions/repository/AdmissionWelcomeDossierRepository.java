package com.schoolerp.modules.admissions.repository;

import com.schoolerp.modules.admissions.entity.AdmissionWelcomeDossier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdmissionWelcomeDossierRepository extends JpaRepository<AdmissionWelcomeDossier, UUID> {
    List<AdmissionWelcomeDossier> findByTenantIdOrderByCreatedAtDesc(String tenantId);
    Optional<AdmissionWelcomeDossier> findByTenantIdAndStudentId(String tenantId, UUID studentId);
    Optional<AdmissionWelcomeDossier> findByTenantIdAndAdmissionNumber(String tenantId, String admissionNumber);
    Optional<AdmissionWelcomeDossier> findByTenantIdAndSmartCardRfidUid(String tenantId, String smartCardRfidUid);
}
