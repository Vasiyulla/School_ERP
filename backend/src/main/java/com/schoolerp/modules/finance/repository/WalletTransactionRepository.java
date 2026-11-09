package com.schoolerp.modules.finance.repository;

import com.schoolerp.modules.finance.entity.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, UUID> {
    List<WalletTransaction> findAllByTenantId(String tenantId);
    List<WalletTransaction> findAllByTenantIdAndStudentIdOrderByTimestampDesc(String tenantId, UUID studentId);
    List<WalletTransaction> findAllByTenantIdAndWalletIdOrderByTimestampDesc(String tenantId, UUID walletId);
    long countByTenantId(String tenantId);
}

