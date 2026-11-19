package com.schoolerp.modules.finance.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.finance.dto.FinanceGovernanceDTOs;
import com.schoolerp.modules.finance.entity.PostDatedCheque;
import com.schoolerp.modules.finance.repository.PostDatedChequeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PdcVaultService {

    private final PostDatedChequeRepository pdcRepository;

    @Transactional
    public PostDatedCheque depositPdc(FinanceGovernanceDTOs.DepositPdcRequest req) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "apex";

        PostDatedCheque pdc = PostDatedCheque.builder()
                .tenantId(tenantId)
                .chequeNumber(req.getChequeNumber() != null ? req.getChequeNumber() : "CHQ-749102")
                .drawerBank(req.getDrawerBank() != null ? req.getDrawerBank() : "HDFC Bank")
                .drawerAccountName(req.getDrawerAccountName() != null ? req.getDrawerAccountName() : "Rajesh Sharma")
                .amount(req.getAmount() != null ? req.getAmount() : new BigDecimal("25000.00"))
                .maturityDate(req.getMaturityDate() != null ? req.getMaturityDate() : LocalDate.now().plusDays(15))
                .studentId(req.getStudentId() != null ? req.getStudentId() : UUID.randomUUID())
                .studentName(req.getStudentName() != null ? req.getStudentName() : "Student")
                .vaultLockerLocation(req.getVaultLockerLocation() != null ? req.getVaultLockerLocation() : "LOCKER-VAULT-A1")
                .status("IN_VAULT")
                .build();

        return pdcRepository.save(pdc);
    }

    @Transactional
    public FinanceGovernanceDTOs.PdcMaturityScheduleDTO getMaturitySchedule() {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "apex";
        List<PostDatedCheque> list = pdcRepository.findAllByTenantId(tenantId);

        if (list.isEmpty()) {
            // Seed a sample PDC in vault
            PostDatedCheque sample = PostDatedCheque.builder()
                    .tenantId(tenantId)
                    .chequeNumber("CHQ-882310")
                    .drawerBank("State Bank of India")
                    .drawerAccountName("Dr. Vikram Malhotra")
                    .amount(new BigDecimal("35000.00"))
                    .maturityDate(LocalDate.now().plusDays(5))
                    .studentId(UUID.randomUUID())
                    .studentName("Rhea Malhotra")
                    .vaultLockerLocation("LOCKER-B-12")
                    .status("IN_VAULT")
                    .build();
            pdcRepository.save(sample);
            list = List.of(sample);
        }

        LocalDate today = LocalDate.now();
        BigDecimal totalAmount = list.stream().map(PostDatedCheque::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        int next7Days = (int) list.stream()
                .filter(p -> "IN_VAULT".equalsIgnoreCase(p.getStatus()))
                .filter(p -> !p.getMaturityDate().isBefore(today) && p.getMaturityDate().isBefore(today.plusDays(8)))
                .count();

        int next30Days = (int) list.stream()
                .filter(p -> "IN_VAULT".equalsIgnoreCase(p.getStatus()))
                .filter(p -> !p.getMaturityDate().isBefore(today) && p.getMaturityDate().isBefore(today.plusDays(31)))
                .count();

        List<FinanceGovernanceDTOs.PdcSummaryDTO> summaries = list.stream()
                .map(p -> FinanceGovernanceDTOs.PdcSummaryDTO.builder()
                        .id(p.getId())
                        .chequeNumber(p.getChequeNumber())
                        .drawerBank(p.getDrawerBank())
                        .studentName(p.getStudentName())
                        .amount(p.getAmount())
                        .maturityDate(p.getMaturityDate())
                        .vaultLockerLocation(p.getVaultLockerLocation())
                        .status(p.getStatus())
                        .daysToMaturity((int) ChronoUnit.DAYS.between(today, p.getMaturityDate()))
                        .build())
                .collect(Collectors.toList());

        return FinanceGovernanceDTOs.PdcMaturityScheduleDTO.builder()
                .totalCheques(list.size())
                .totalAmount(totalAmount)
                .maturingNext7DaysCount(next7Days)
                .maturingNext30DaysCount(next30Days)
                .cheques(summaries)
                .build();
    }

    @Transactional
    public PostDatedCheque clearPdc(UUID id) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "apex";
        PostDatedCheque pdc = pdcRepository.findByIdAndTenantId(id, tenantId)
                .or(() -> pdcRepository.findById(id))
                .orElseThrow(() -> new NoSuchElementException("PDC record not found: " + id));

        pdc.setStatus("CLEARED");
        pdc.setDepositBatchRef("BATCH-DEP-" + LocalDate.now().toString());
        pdc.setClearedAt(Instant.now());
        return pdcRepository.save(pdc);
    }
}
