package com.schoolerp.modules.system.repository;

import com.schoolerp.modules.system.entity.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OutboxEventRepository extends JpaRepository<OutboxEvent, UUID> {
    List<OutboxEvent> findByTenantIdOrderByCreatedAtDesc(String tenantId);
    List<OutboxEvent> findByStatusOrderByCreatedAtAsc(String status);
    List<OutboxEvent> findByTenantIdAndStatus(String tenantId, String status);
}

