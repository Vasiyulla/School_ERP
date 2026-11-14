package com.schoolerp.modules.finance.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.finance.dto.*;
import com.schoolerp.modules.finance.entity.SiblingConcessionPolicy;
import com.schoolerp.modules.finance.repository.SiblingConcessionPolicyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class SiblingConcessionService {

    private final SiblingConcessionPolicyRepository policyRepository;

    private String resolveTenant() {
        String tenantId = TenantContext.getTenantId();
        return (tenantId != null && !tenantId.isBlank()) ? tenantId : "tenant-default";
    }

    @Transactional
    public SiblingConcessionPolicy configurePolicy(ConfigureConcessionPolicyRequest request) {
        String tenantId = resolveTenant();
        String code = request.getPolicyCode() != null ? request.getPolicyCode() : "SIBLING_TIER_" + request.getSiblingRank();

        SiblingConcessionPolicy policy = policyRepository.findByTenantIdAndPolicyCode(tenantId, code)
                .orElse(SiblingConcessionPolicy.builder()
                        .tenantId(tenantId)
                        .policyCode(code)
                        .build());

        policy.setSiblingRank(request.getSiblingRank() != null ? request.getSiblingRank() : 2);
        policy.setConcessionPercentage(request.getConcessionPercentage() != null ? request.getConcessionPercentage() : new BigDecimal("15.00"));
        policy.setApplicableFeeHead(request.getApplicableFeeHead() != null ? request.getApplicableFeeHead() : "TUITION_FEE");
        policy.setActive(request.getIsActive() != null ? request.getIsActive() : true);

        return policyRepository.save(policy);
    }

    @Transactional(readOnly = true)
    public EvaluateFamilyConcessionResultDTO evaluateFamily(EvaluateFamilyConcessionRequest request) {
        String tenantId = resolveTenant();
        String familyId = request.getFamilyId() != null ? request.getFamilyId() : "FAM-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        List<StudentSiblingInputDTO> children = (request.getChildren() != null)
                ? new ArrayList<>(request.getChildren())
                : Collections.emptyList();

        // Sort children by date of birth ascending (oldest child first = Rank 1)
        children.sort(Comparator.comparing(StudentSiblingInputDTO::getDateOfBirth, Comparator.nullsLast(Comparator.naturalOrder())));

        List<SiblingConcessionPolicy> activePolicies = policyRepository.findAllByTenantId(tenantId);
        Map<Integer, BigDecimal> policyDiscountMap = new HashMap<>();
        for (SiblingConcessionPolicy p : activePolicies) {
            if (p.isActive()) {
                policyDiscountMap.put(p.getSiblingRank(), p.getConcessionPercentage());
            }
        }

        List<SiblingConcessionLineDTO> lines = new ArrayList<>();
        BigDecimal totalOriginal = BigDecimal.ZERO;
        BigDecimal totalConcessions = BigDecimal.ZERO;
        BigDecimal netPayable = BigDecimal.ZERO;

        int rank = 1;
        for (StudentSiblingInputDTO child : children) {
            BigDecimal originalFee = child.getStandardTuitionFee() != null ? child.getStandardTuitionFee() : new BigDecimal("35000.00");
            totalOriginal = totalOriginal.add(originalFee);

            BigDecimal discountPct = BigDecimal.ZERO;
            if (rank == 2) {
                discountPct = policyDiscountMap.getOrDefault(2, new BigDecimal("15.00"));
            } else if (rank >= 3) {
                discountPct = policyDiscountMap.getOrDefault(3, new BigDecimal("25.00"));
            }

            BigDecimal discountAmount = originalFee.multiply(discountPct)
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            BigDecimal netFee = originalFee.subtract(discountAmount).max(BigDecimal.ZERO);

            totalConcessions = totalConcessions.add(discountAmount);
            netPayable = netPayable.add(netFee);

            lines.add(SiblingConcessionLineDTO.builder()
                    .studentId(child.getStudentId() != null ? child.getStudentId() : UUID.randomUUID())
                    .studentName(child.getStudentName() != null ? child.getStudentName() : "Student " + rank)
                    .siblingRank(rank)
                    .standardTuitionFee(originalFee)
                    .concessionPercentage(discountPct)
                    .discountAmount(discountAmount)
                    .netPayableTuitionFee(netFee)
                    .build());

            rank++;
        }

        return EvaluateFamilyConcessionResultDTO.builder()
                .familyId(familyId)
                .totalChildren(children.size())
                .totalOriginalTuitionFee(totalOriginal)
                .totalConcessionsGranted(totalConcessions)
                .netFamilyTuitionPayable(netPayable)
                .siblingConcessions(lines)
                .build();
    }
}

