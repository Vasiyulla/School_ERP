package com.schoolerp.modules.finance.repository;

import com.schoolerp.modules.finance.entity.FeeInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FeeInvoiceRepository extends JpaRepository<FeeInvoice, UUID> {
    List<FeeInvoice> findAllByTenantId(String tenantId);
    Optional<FeeInvoice> findByIdAndTenantId(UUID id, String tenantId);
    Optional<FeeInvoice> findByInvoiceNumberAndTenantId(String invoiceNumber, String tenantId);
}

