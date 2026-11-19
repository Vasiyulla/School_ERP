package com.schoolerp.modules.system.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.system.dto.AuditHashChainVerificationDTO;
import com.schoolerp.modules.system.entity.AuditHashChain;
import com.schoolerp.modules.system.repository.AuditHashChainRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditHashChainService {

    public static final String GENESIS_PREV_HASH = "0000000000000000000000000000000000000000000000000000000000000000";

    private final AuditHashChainRepository auditHashChainRepository;

    @Transactional
    public AuditHashChain logHashedAction(String action, String actor, String entityName, String entityId, String payload) {
        String tenantId = TenantContext.getTenantId();
        if (tenantId == null || tenantId.isBlank()) {
            tenantId = "apex";
        }

        var latestOpt = auditHashChainRepository.findTopByTenantIdOrderByChainIndexDesc(tenantId);
        long nextIndex = latestOpt.map(e -> e.getChainIndex() + 1).orElse(0L);
        String prevHash = latestOpt.map(AuditHashChain::getCurrentHash).orElse(GENESIS_PREV_HASH);

        String payloadSha256 = sha256Hex(payload != null ? payload : "");
        Instant now = Instant.now();
        String currentHash = computeHash(prevHash, tenantId, nextIndex, action, actor, entityName, entityId, payloadSha256, now);

        AuditHashChain entry = AuditHashChain.builder()
                .tenantId(tenantId)
                .chainIndex(nextIndex)
                .previousHash(prevHash)
                .currentHash(currentHash)
                .action(action)
                .actor(actor != null ? actor : "system")
                .entityName(entityName)
                .entityId(entityId)
                .payloadSha256(payloadSha256)
                .createdAt(now)
                .build();

        return auditHashChainRepository.save(entry);
    }

    @Transactional(readOnly = true)
    public AuditHashChainVerificationDTO verifyChainIntegrity() {
        String tenantId = TenantContext.getTenantId();
        if (tenantId == null || tenantId.isBlank()) {
            tenantId = "apex";
        }

        List<AuditHashChain> chain = auditHashChainRepository.findAllByTenantIdOrderByChainIndexAsc(tenantId);
        if (chain.isEmpty()) {
            // Seed a genesis block so verification is instantly demonstrable
            return AuditHashChainVerificationDTO.builder()
                    .valid(true)
                    .totalBlocksVerified(0)
                    .latestBlockHash(GENESIS_PREV_HASH)
                    .message("No records found in audit chain. Chain integrity is pristine (empty state).")
                    .build();
        }

        String expectedPrevHash = GENESIS_PREV_HASH;
        for (int i = 0; i < chain.size(); i++) {
            AuditHashChain block = chain.get(i);
            if (!block.getPreviousHash().equals(expectedPrevHash)) {
                return AuditHashChainVerificationDTO.builder()
                        .valid(false)
                        .totalBlocksVerified(i)
                        .tamperedBlockIndex(block.getChainIndex())
                        .message("Tampering detected at block index " + block.getChainIndex() + ": previous hash mismatch!")
                        .build();
            }

            String recomputedHash = computeHash(
                    block.getPreviousHash(),
                    block.getTenantId(),
                    block.getChainIndex(),
                    block.getAction(),
                    block.getActor(),
                    block.getEntityName(),
                    block.getEntityId(),
                    block.getPayloadSha256(),
                    block.getCreatedAt()
            );

            if (!block.getCurrentHash().equals(recomputedHash)) {
                return AuditHashChainVerificationDTO.builder()
                        .valid(false)
                        .totalBlocksVerified(i)
                        .tamperedBlockIndex(block.getChainIndex())
                        .message("Tampering detected at block index " + block.getChainIndex() + ": current hash digest does not match payload!")
                        .build();
            }

            expectedPrevHash = block.getCurrentHash();
        }

        List<AuditHashChainVerificationDTO.AuditHashChainBlockDTO> sampleBlocks = chain.stream()
                .skip(Math.max(0, chain.size() - 5))
                .map(b -> AuditHashChainVerificationDTO.AuditHashChainBlockDTO.builder()
                        .chainIndex(b.getChainIndex())
                        .previousHash(b.getPreviousHash())
                        .currentHash(b.getCurrentHash())
                        .action(b.getAction())
                        .actor(b.getActor())
                        .entityName(b.getEntityName())
                        .entityId(b.getEntityId())
                        .timestamp(b.getCreatedAt().toString())
                        .build())
                .collect(Collectors.toList());

        return AuditHashChainVerificationDTO.builder()
                .valid(true)
                .totalBlocksVerified(chain.size())
                .latestBlockHash(expectedPrevHash)
                .message("Cryptographic audit hash chain verified 100% valid. Zero tampering detected across " + chain.size() + " blocks.")
                .sampleBlocks(sampleBlocks)
                .build();
    }

    public static String computeHash(String prevHash, String tenantId, Long index, String action, String actor,
                                     String entityName, String entityId, String payloadSha256, Instant createdAt) {
        String data = String.format("%s:%s:%d:%s:%s:%s:%s:%s:%d",
                prevHash,
                tenantId != null ? tenantId : "",
                index != null ? index : 0L,
                action != null ? action : "",
                actor != null ? actor : "",
                entityName != null ? entityName : "",
                entityId != null ? entityId : "",
                payloadSha256 != null ? payloadSha256 : "",
                createdAt != null ? createdAt.toEpochMilli() : 0L
        );
        return sha256Hex(data);
    }

    public static String sha256Hex(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
}
