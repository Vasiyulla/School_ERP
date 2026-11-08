package com.schoolerp.modules.communication.repository;

import com.schoolerp.modules.communication.entity.Circular;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CircularRepository extends JpaRepository<Circular, UUID> {
    List<Circular> findByTenantIdOrderByPublishedDateDesc(String tenantId);
    List<Circular> findByTenantIdAndTargetAudience(String tenantId, String targetAudience);
}

