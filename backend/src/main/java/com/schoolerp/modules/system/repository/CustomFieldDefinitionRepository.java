package com.schoolerp.modules.system.repository;

import com.schoolerp.modules.system.entity.CustomFieldDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomFieldDefinitionRepository extends JpaRepository<CustomFieldDefinition, UUID> {
    List<CustomFieldDefinition> findByTenantIdAndTargetEntityTypeOrderByDisplayOrderAsc(String tenantId, String targetEntityType);
    Optional<CustomFieldDefinition> findFirstByTenantIdAndTargetEntityTypeAndFieldCodeOrderByCreatedAtDesc(String tenantId, String targetEntityType, String fieldCode);
    Optional<CustomFieldDefinition> findByTenantIdAndTargetEntityTypeAndFieldCode(String tenantId, String targetEntityType, String fieldCode);
    List<CustomFieldDefinition> findByTenantIdOrderByCreatedAtDesc(String tenantId);
}

