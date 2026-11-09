package com.schoolerp.modules.system.repository;

import com.schoolerp.modules.system.entity.TenantFeatureFlag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TenantFeatureFlagRepository extends JpaRepository<TenantFeatureFlag, UUID> {
    List<TenantFeatureFlag> findByTenantId(String tenantId);
    Optional<TenantFeatureFlag> findByTenantIdAndFeatureKey(String tenantId, String featureKey);
    boolean existsByTenantIdAndFeatureKey(String tenantId, String featureKey);
}

