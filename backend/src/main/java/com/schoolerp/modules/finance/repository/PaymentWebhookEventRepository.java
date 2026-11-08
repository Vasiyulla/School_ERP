package com.schoolerp.modules.finance.repository;

import com.schoolerp.modules.finance.entity.PaymentWebhookEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentWebhookEventRepository extends JpaRepository<PaymentWebhookEvent, UUID> {
    Optional<PaymentWebhookEvent> findByTenantIdAndGatewayEventId(String tenantId, String gatewayEventId);
    boolean existsByTenantIdAndGatewayEventId(String tenantId, String gatewayEventId);
    List<PaymentWebhookEvent> findByTenantIdOrderByReceivedAtDesc(String tenantId);
}

