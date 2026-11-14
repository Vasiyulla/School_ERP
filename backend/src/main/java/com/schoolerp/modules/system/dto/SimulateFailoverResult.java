package com.schoolerp.modules.system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimulateFailoverResult {

    private String tenantId;
    private String failoverEventId;
    private String previousPrimaryHost;
    private String newPromotedPrimaryHost;
    private String failoverStatus; // FAILOVER_SUCCESSFUL_PROMOTED, FAILED
    private long failoverDurationMs;
    private long transactionsLostCount; // 0
    private String dataIntegrityAudit; // 100% ZERO_DATA_LOSS_CONFIRMED
    private String driverRoutingStatus; // TRAFFIC_ROUTED_TO_PROMOTED_PRIMARY
    private Instant executedAt;
    private String message;
}

