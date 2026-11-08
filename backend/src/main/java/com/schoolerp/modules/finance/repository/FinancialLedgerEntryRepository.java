package com.schoolerp.modules.finance.repository;

import com.schoolerp.modules.finance.entity.FinancialLedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FinancialLedgerEntryRepository extends JpaRepository<FinancialLedgerEntry, UUID> {
    List<FinancialLedgerEntry> findAllByTenantIdOrderByPostedAtDesc(String tenantId);
}

