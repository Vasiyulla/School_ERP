package com.schoolerp.modules.system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimulateFailoverRequest {

    @Builder.Default
    private String triggerReason = "SIMULATED_PRIMARY_NODE_CRASH";

    @Builder.Default
    private String targetPromotionNode = "db-secondary";

    private String remarks;
}

