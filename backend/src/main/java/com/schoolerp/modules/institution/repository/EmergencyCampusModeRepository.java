package com.schoolerp.modules.institution.repository;

import com.schoolerp.modules.institution.entity.EmergencyCampusMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmergencyCampusModeRepository extends JpaRepository<EmergencyCampusMode, UUID> {
    Optional<EmergencyCampusMode> findTopByTenantIdOrderByDeclaredAtDesc(String tenantId);
    List<EmergencyCampusMode> findByTenantIdOrderByDeclaredAtDesc(String tenantId);
}

