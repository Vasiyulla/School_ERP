package com.schoolerp.modules.system.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "audit_hash_chain", schema = "system", indexes = {
        @Index(name = "idx_audit_chain_tenant_idx", columnList = "tenant_id, chain_index")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditHashChain {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "chain_index", nullable = false)
    private Long chainIndex;

    @Column(name = "previous_hash", nullable = false, length = 64)
    private String previousHash;

    @Column(name = "current_hash", nullable = false, length = 64)
    private String currentHash;

    @Column(name = "action", nullable = false)
    private String action;

    @Column(name = "actor", nullable = false)
    private String actor;

    @Column(name = "entity_name")
    private String entityName;

    @Column(name = "entity_id")
    private String entityId;

    @Column(name = "payload_sha256", length = 64)
    private String payloadSha256;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
