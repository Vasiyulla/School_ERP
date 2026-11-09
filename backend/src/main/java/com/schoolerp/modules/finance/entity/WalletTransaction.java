package com.schoolerp.modules.finance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "wallet_transactions", schema = "finance")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID walletId;

    @Column(nullable = false)
    private UUID studentId;

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false, unique = true)
    private String receiptNumber; // e.g. POS-202611-0081

    @Column(nullable = false)
    private String transactionType; // TOPUP, DEBIT_CANTEEN, DEBIT_STATIONERY, REFUND

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal balanceAfter;

    private String itemDescription; // e.g. "Nutritious Thali Meal + Fresh Orange Juice"

    private String terminalId; // "POS-CANTEEN-01"

    private String cashierName; // "Ramesh (Cafeteria In-Charge)"

    @Column(nullable = false)
    private LocalDateTime timestamp;
}

