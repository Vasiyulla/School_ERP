package com.schoolerp.modules.communication.repository;

import com.schoolerp.modules.communication.entity.NotificationDispatchLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationDispatchLogRepository extends JpaRepository<NotificationDispatchLog, UUID> {
    List<NotificationDispatchLog> findByTenantIdOrderByDispatchedAtDesc(String tenantId);
    List<NotificationDispatchLog> findByTenantIdAndChannel(String tenantId, String channel);
    List<NotificationDispatchLog> findByTenantIdAndTriggerEvent(String tenantId, String triggerEvent);
}

