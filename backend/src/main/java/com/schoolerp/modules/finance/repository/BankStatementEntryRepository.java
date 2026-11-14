package com.schoolerp.modules.finance.repository;

import com.schoolerp.modules.finance.entity.BankStatementEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BankStatementEntryRepository extends JpaRepository<BankStatementEntry, UUID> {
    List<BankStatementEntry> findAllByTenantIdAndStatus(String tenantId, String status);
    List<BankStatementEntry> findAllByTenantId(String tenantId);
    Optional<BankStatementEntry> findByTenantIdAndUtrNumber(String tenantId, String utrNumber);
}

