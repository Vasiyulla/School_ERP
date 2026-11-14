package com.schoolerp.modules.academics.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "material_access_logs", schema = "academics")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialAccessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID materialId;

    @Column(nullable = false)
    private UUID studentId;

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    @Builder.Default
    private Instant accessedAt = Instant.now();

    @Builder.Default
    private Integer durationSeconds = 0; // Total time spent reading or watching

    @Builder.Default
    private boolean isCompleted = false; // Marked finished by student
}

