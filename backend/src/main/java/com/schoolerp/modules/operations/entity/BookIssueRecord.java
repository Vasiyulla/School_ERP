package com.schoolerp.modules.operations.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "library_issue_records", schema = "operations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookIssueRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID bookId;

    @Column(nullable = false)
    private String bookTitle;

    @Column(nullable = false)
    private UUID studentId;

    @Column(nullable = false)
    private String studentName;

    @Builder.Default
    private LocalDate issuedDate = LocalDate.now();

    @Column(nullable = false)
    private LocalDate dueDate;

    private LocalDate returnedDate;

    @Builder.Default
    private String status = "ISSUED"; // ISSUED, RETURNED, OVERDUE, LOST

    @Builder.Default
    private BigDecimal fineAmount = BigDecimal.ZERO;

    @Builder.Default
    private Instant createdAt = Instant.now();
}

