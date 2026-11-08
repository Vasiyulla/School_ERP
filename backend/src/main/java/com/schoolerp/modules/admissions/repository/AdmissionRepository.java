package com.schoolerp.modules.admissions.repository;

import com.schoolerp.modules.admissions.entity.AdmissionApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdmissionRepository extends JpaRepository<AdmissionApplication, UUID> {
    List<AdmissionApplication> findAllByTenantId(String tenantId);
    Optional<AdmissionApplication> findByIdAndTenantId(UUID id, String tenantId);
    List<AdmissionApplication> findAllByTenantIdAndStatus(String tenantId, String status);
}

