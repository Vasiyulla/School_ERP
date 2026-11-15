package com.schoolerp.modules.system.repository;

import com.schoolerp.modules.system.entity.TenantWebhookSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TenantWebhookSubscriptionRepository extends JpaRepository<TenantWebhookSubscription, UUID> {
    List<TenantWebhookSubscription> findByTenantIdOrderByCreatedAtDesc(String tenantId);
    List<TenantWebhookSubscription> findByTenantIdAndEventTopicAndIsActiveTrue(String tenantId, String eventTopic);
}

