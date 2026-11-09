package com.schoolerp.modules.system.repository;

import com.schoolerp.modules.system.entity.TenantModuleLicense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TenantModuleLicenseRepository extends JpaRepository<TenantModuleLicense, UUID> {
    List<TenantModuleLicense> findByTenantId(String tenantId);
    Optional<TenantModuleLicense> findByTenantIdAndModuleKey(String tenantId, String moduleKey);
}
