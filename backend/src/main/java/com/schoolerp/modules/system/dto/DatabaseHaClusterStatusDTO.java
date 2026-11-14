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
public class DatabaseHaClusterStatusDTO {

    private String tenantId;
    private String schoolDatabaseName;
    private String primaryNodeHost;
    private int primaryNodePort;
    private String primaryNodeStatus; // ONLINE_ACTIVE, CRASHED_OFFLINE, REBOOTING
    private String secondaryNodeHost;
    private int secondaryNodePort;
    private String secondaryNodeStatus; // HOT_STANDBY_IN_SYNC, PROMOTED_PRIMARY, REBUILDING
    private String activePrimaryHost; // db-primary or db-secondary
    private String replicationMode; // STREAMING_SYNCHRONOUS, ASYNC_CATCHUP
    private long replicationLagBytes;
    private long replicationLagMs;
    private String clusterHealthTier; // HIGH_AVAILABILITY_OPTIMAL, FAILOVER_ACTIVE, DEGRADED
    private String failoverDriverMode; // MULTI_HOST_JDBC_AUTO_FAILOVER
    private int activeClientConnections;
    private Instant lastFailoverEventTimestamp;
    private String rpoTarget; // "0 seconds (Zero data loss)"
    private String rtoTarget; // "< 3 seconds (Automated promotion)"
}
