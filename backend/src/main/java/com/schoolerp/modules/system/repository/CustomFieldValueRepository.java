package com.schoolerp.modules.system.repository;

import com.schoolerp.modules.system.entity.CustomFieldValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomFieldValueRepository extends JpaRepository<CustomFieldValue, UUID> {
    List<CustomFieldValue> findByTenantIdAndTargetEntityTypeAndTargetEntityId(String tenantId, String targetEntityType, UUID targetEntityId);
    Optional<CustomFieldValue> findFirstByTenantIdAndTargetEntityTypeAndTargetEntityIdAndFieldCodeOrderByUpdatedAtDesc(
            String tenantId, String targetEntityType, UUID targetEntityId, String fieldCode);
    Optional<CustomFieldValue> findByTenantIdAndTargetEntityTypeAndTargetEntityIdAndFieldCode(
            String tenantId, String targetEntityType, UUID targetEntityId, String fieldCode);
}

