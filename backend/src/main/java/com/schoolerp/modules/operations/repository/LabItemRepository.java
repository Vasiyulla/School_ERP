package com.schoolerp.modules.operations.repository;

import com.schoolerp.modules.operations.entity.LabItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LabItemRepository extends JpaRepository<LabItem, UUID> {

    List<LabItem> findAllByTenantIdOrderByLabNameAsc(String tenantId);

    List<LabItem> findAllByTenantIdAndLabName(String tenantId, String labName);

    Optional<LabItem> findByIdAndTenantId(UUID id, String tenantId);
}

