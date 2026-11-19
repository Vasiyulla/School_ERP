package com.schoolerp.modules.system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditHashChainVerificationDTO {
    private boolean valid;
    private long totalBlocksVerified;
    private String latestBlockHash;
    private String message;
    private Long tamperedBlockIndex;
    private List<AuditHashChainBlockDTO> sampleBlocks;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuditHashChainBlockDTO {
        private Long chainIndex;
        private String previousHash;
        private String currentHash;
        private String action;
        private String actor;
        private String entityName;
        private String entityId;
        private String timestamp;
    }
}
