package com.schoolerp.modules.system.repository;

import com.schoolerp.modules.system.entity.CustomEntityDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomEntityDefinitionRepository extends JpaRepository<CustomEntityDefinition, UUID> {
    Optional<CustomEntityDefinition> findFirstByTenantIdAndEntityCodeOrderByCreatedAtDesc(String tenantId, String entityCode);
    Optional<CustomEntityDefinition> findByTenantIdAndEntityCode(String tenantId, String entityCode);
    List<CustomEntityDefinition> findByTenantIdOrderByCreatedAtDesc(String tenantId);
    List<CustomEntityDefinition> findByTenantIdAndCategoryOrderByDisplayNameAsc(String tenantId, String category);
}

