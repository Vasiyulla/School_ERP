package com.schoolerp.modules.finance.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.finance.entity.StudentWallet;
import com.schoolerp.modules.finance.entity.WalletTransaction;
import com.schoolerp.modules.finance.repository.StudentWalletRepository;
import com.schoolerp.modules.finance.repository.WalletTransactionRepository;
import com.schoolerp.modules.students.entity.Student;
import com.schoolerp.modules.students.repository.StudentRepository;
import com.schoolerp.modules.system.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentWalletService {

    private final StudentWalletRepository walletRepository;
    private final WalletTransactionRepository transactionRepository;
    private final StudentRepository studentRepository;
    private final AuditLogService auditLogService;

    @Transactional
    public StudentWallet registerOrGetWallet(UUID studentId, String rfidCardNumber, BigDecimal dailyLimit) {
        final String activeTenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";

        return walletRepository.findByTenantIdAndStudentId(activeTenantId, studentId)
                .orElseGet(() -> {
                    Student student = studentRepository.findById(studentId)
                            .filter(s -> s.getTenantId().equals(activeTenantId))
                            .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));

                    String rfid = (rfidCardNumber != null && !rfidCardNumber.isBlank())
                            ? rfidCardNumber
                            : "RFID-" + student.getAdmissionNumber().replace("-", "") + "-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();

                    StudentWallet newWallet = StudentWallet.builder()
                            .tenantId(activeTenantId)
                            .studentId(studentId)
                            .studentName(student.getFullName())
                            .admissionNumber(student.getAdmissionNumber())
                            .rfidCardNumber(rfid)
                            .balance(BigDecimal.ZERO)
                            .dailySpendingLimit(dailyLimit != null ? dailyLimit : new BigDecimal("200.00"))
                            .spentToday(BigDecimal.ZERO)
                            .lastSpentDate(LocalDate.now())
                            .status("ACTIVE")
                            .build();

                    return walletRepository.save(newWallet);
                });
    }

    @Transactional
    public StudentWallet topupWallet(UUID studentId, BigDecimal amount, String paymentMethod) {
        final String activeTenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Top-up amount must be strictly greater than zero");
        }

        StudentWallet wallet = walletRepository.findByTenantIdAndStudentId(activeTenantId, studentId)
                .orElseGet(() -> registerOrGetWallet(studentId, null, null));

        wallet.setBalance(wallet.getBalance().add(amount));
        wallet.setLastTopupAt(LocalDateTime.now());
        StudentWallet saved = walletRepository.save(wallet);

        long txCount = transactionRepository.countByTenantId(activeTenantId);
        String receiptNumber = String.format("REC-%s-%04d",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMM")), txCount + 1);

        WalletTransaction tx = WalletTransaction.builder()
                .tenantId(activeTenantId)
                .walletId(wallet.getId())
                .studentId(studentId)
                .studentName(wallet.getStudentName())
                .receiptNumber(receiptNumber)
                .transactionType("TOPUP")
                .amount(amount)
                .balanceAfter(wallet.getBalance())
                .itemDescription("Parent Online Prepaid Wallet Top-up via " + (paymentMethod != null ? paymentMethod : "UPI/Razorpay"))
                .terminalId("PARENT_APP_GATEWAY")
                .cashierName("Online Payment Gateway")
                .timestamp(LocalDateTime.now())
                .build();
        transactionRepository.save(tx);

        auditLogService.recordEvent(
                "WALLET_TOPUP",
                "finance.student_wallets",
                wallet.getId().toString(),
                String.format("Wallet credited INR %s for student %s (%s). New balance: INR %s",
                        amount, wallet.getStudentName(), wallet.getAdmissionNumber(), wallet.getBalance())
        );

        log.info("Student wallet topped up: {} by INR {}", wallet.getStudentName(), amount);
        return saved;
    }

    @Transactional
    public WalletTransaction transactCanteen(String rfidCardNumber, BigDecimal amount, String itemDescription,
                                             String terminalId, String cashierName) {
        final String activeTenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transaction amount must be strictly positive");
        }

        StudentWallet wallet = walletRepository.findByTenantIdAndRfidCardNumber(activeTenantId, rfidCardNumber)
                .orElseThrow(() -> new IllegalArgumentException("Unrecognized RFID smart card: " + rfidCardNumber));

        if (!"ACTIVE".equalsIgnoreCase(wallet.getStatus())) {
            throw new IllegalStateException("Student wallet is suspended or blocked: " + wallet.getStatus());
        }

        // Reset spentToday if new calendar day
        LocalDate today = LocalDate.now();
        if (wallet.getLastSpentDate() == null || !wallet.getLastSpentDate().equals(today)) {
            wallet.setSpentToday(BigDecimal.ZERO);
            wallet.setLastSpentDate(today);
        }

        // Check daily spending limit
        BigDecimal projectedDaily = wallet.getSpentToday().add(amount);
        if (projectedDaily.compareTo(wallet.getDailySpendingLimit()) > 0) {
            throw new IllegalStateException(String.format(
                    "Transaction declined: Daily spending limit of INR %s exceeded. Spent today: INR %s, Requested: INR %s (Remaining allowance: INR %s)",
                    wallet.getDailySpendingLimit(), wallet.getSpentToday(), amount,
                    wallet.getDailySpendingLimit().subtract(wallet.getSpentToday()).max(BigDecimal.ZERO)));
        }

        // Check wallet balance
        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException(String.format(
                    "Transaction declined: Insufficient wallet balance. Available: INR %s, Required: INR %s",
                    wallet.getBalance(), amount));
        }

        // Deduct balance and update daily spend
        wallet.setBalance(wallet.getBalance().subtract(amount));
        wallet.setSpentToday(projectedDaily);
        walletRepository.save(wallet);

        long txCount = transactionRepository.countByTenantId(activeTenantId);
        String receiptNumber = String.format("POS-%s-%04d",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMM")), txCount + 1);

        WalletTransaction tx = WalletTransaction.builder()
                .tenantId(activeTenantId)
                .walletId(wallet.getId())
                .studentId(wallet.getStudentId())
                .studentName(wallet.getStudentName())
                .receiptNumber(receiptNumber)
                .transactionType("DEBIT_CANTEEN")
                .amount(amount)
                .balanceAfter(wallet.getBalance())
                .itemDescription(itemDescription != null ? itemDescription : "Cafeteria Nutrition Meal")
                .terminalId(terminalId != null ? terminalId : "POS-CANTEEN-01")
                .cashierName(cashierName != null ? cashierName : "Cafeteria Cashier")
                .timestamp(LocalDateTime.now())
                .build();

        WalletTransaction savedTx = transactionRepository.save(tx);

        auditLogService.recordEvent(
                "CANTEEN_WALLET_PURCHASE",
                "finance.wallet_transactions",
                savedTx.getId().toString(),
                String.format("RFID POS Debit INR %s for %s: '%s' (Receipt: %s). Remaining balance: INR %s",
                        amount, wallet.getStudentName(), itemDescription, receiptNumber, wallet.getBalance())
        );

        log.info("Canteen POS transaction successful: {} - INR {}", wallet.getStudentName(), amount);
        return savedTx;
    }

    @Transactional(readOnly = true)
    public StudentWallet getWallet(UUID studentId) {
        final String activeTenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        return walletRepository.findByTenantIdAndStudentId(activeTenantId, studentId)
                .orElseGet(() -> registerOrGetWallet(studentId, null, null));
    }

    @Transactional(readOnly = true)
    public List<WalletTransaction> getStudentTransactions(UUID studentId) {
        final String activeTenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        return transactionRepository.findAllByTenantIdAndStudentIdOrderByTimestampDesc(activeTenantId, studentId);
    }
}

