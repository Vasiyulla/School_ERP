package com.schoolerp.modules.admissions.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.admissions.dto.*;
import com.schoolerp.modules.admissions.entity.*;
import com.schoolerp.modules.admissions.repository.*;
import com.schoolerp.modules.system.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdmissionDeskService {

    private final AdmissionQueueTokenRepository queueTokenRepository;
    private final ProspectusKitMasterRepository prospectusKitRepository;
    private final AdmissionDocumentUndertakingRepository undertakingRepository;
    private final AdmissionWelcomeDossierRepository welcomeDossierRepository;
    private final AdmissionSeatSurrenderRepository seatSurrenderRepository;
    private final AuditLogService auditLogService;

    private final AtomicInteger tokenSequence = new AtomicInteger(100);
    private final AtomicInteger undertakingSequence = new AtomicInteger(1000);
    private final AtomicInteger admissionSequence = new AtomicInteger(5000);
    private final AtomicInteger surrenderSequence = new AtomicInteger(200);

    private String resolveTenantId() {
        return TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
    }

    // =========================================================================
    // 1. FRONT-DESK SMART QUEUE & COUNTER CALLING DESK
    // =========================================================================

    @Transactional
    public AdmissionQueueTokenDTO dispenseQueueToken(DispenseQueueTokenRequest req) {
        String tenantId = resolveTenantId();
        String cat = req.getCategory().toUpperCase();
        String prefix = switch (cat) {
            case "DOCUMENT_VERIFICATION" -> "TKN-DOC";
            case "FEE_PAYMENT" -> "TKN-FEE";
            case "PRINCIPAL_INTERVIEW" -> "TKN-PRN";
            default -> "TKN-ENQ";
        };
        String tokenNum = prefix + "-" + tokenSequence.incrementAndGet();

        AdmissionQueueToken token = AdmissionQueueToken.builder()
                .tenantId(tenantId)
                .tokenNumber(tokenNum)
                .category(cat)
                .visitorName(req.getVisitorName())
                .visitorPhone(req.getVisitorPhone())
                .studentName(req.getStudentName())
                .gradeApplying(req.getGradeApplying())
                .status("WAITING")
                .notes(req.getNotes())
                .waitingMinutes(0)
                .serviceMinutes(0)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        token = queueTokenRepository.save(token);
        log.info("Dispensed queue token {} for visitor {}", tokenNum, req.getVisitorName());
        return mapToTokenDTO(token);
    }

    @Transactional
    public AdmissionQueueTokenDTO callNextToken(CallNextTokenRequest req) {
        String tenantId = resolveTenantId();
        List<AdmissionQueueToken> waiting = queueTokenRepository.findByTenantIdAndStatusOrderByCreatedAtAsc(tenantId, "WAITING");

        AdmissionQueueToken nextToken = null;
        if (req.getPreferredCategory() != null && !req.getPreferredCategory().isBlank()) {
            nextToken = waiting.stream()
                    .filter(t -> t.getCategory().equalsIgnoreCase(req.getPreferredCategory()))
                    .findFirst()
                    .orElse(null);
        }
        if (nextToken == null && !waiting.isEmpty()) {
            nextToken = waiting.get(0);
        }

        if (nextToken == null) {
            throw new NoSuchElementException("No visitors currently waiting in the admission queue");
        }

        nextToken.setStatus("CALLED");
        nextToken.setAssignedCounter(req.getCounterName());
        nextToken.setAssignedOfficerId(req.getOfficerId());
        nextToken.setCalledAt(Instant.now());
        nextToken.setUpdatedAt(Instant.now());

        nextToken = queueTokenRepository.save(nextToken);
        log.info("Token {} called to {}", nextToken.getTokenNumber(), req.getCounterName());
        return mapToTokenDTO(nextToken);
    }

    @Transactional
    public AdmissionQueueTokenDTO completeToken(UUID tokenId, CompleteTokenRequest req) {
        String tenantId = resolveTenantId();
        AdmissionQueueToken token = queueTokenRepository.findById(tokenId)
                .filter(t -> t.getTenantId().equals(tenantId))
                .orElseThrow(() -> new NoSuchElementException("Queue token not found: " + tokenId));

        String outcome = req != null && req.getOutcomeStatus() != null ? req.getOutcomeStatus().toUpperCase() : "COMPLETED";
        token.setStatus(outcome);
        token.setServiceMinutes(req != null && req.getServiceMinutes() != null ? req.getServiceMinutes() : 8);
        token.setCompletedAt(Instant.now());
        if (req != null && req.getRemarks() != null) {
            token.setNotes((token.getNotes() != null ? token.getNotes() + " | " : "") + req.getRemarks());
        }
        token.setUpdatedAt(Instant.now());

        token = queueTokenRepository.save(token);
        log.info("Token {} finalized with status {}", token.getTokenNumber(), outcome);
        return mapToTokenDTO(token);
    }

    @Transactional(readOnly = true)
    public LiveQueueBoardDTO getLiveQueueBoard() {
        String tenantId = resolveTenantId();
        long waitingCount = queueTokenRepository.countByTenantIdAndStatus(tenantId, "WAITING");
        long servingCount = queueTokenRepository.countByTenantIdAndStatus(tenantId, "CALLED") +
                queueTokenRepository.countByTenantIdAndStatus(tenantId, "SERVING");
        long completedCount = queueTokenRepository.countByTenantIdAndStatus(tenantId, "COMPLETED");

        List<AdmissionQueueTokenDTO> serving = queueTokenRepository.findByTenantIdAndStatusOrderByCreatedAtAsc(tenantId, "CALLED")
                .stream().map(this::mapToTokenDTO).collect(Collectors.toList());

        List<AdmissionQueueTokenDTO> nextInLine = queueTokenRepository.findByTenantIdAndStatusOrderByCreatedAtAsc(tenantId, "WAITING")
                .stream().limit(5).map(this::mapToTokenDTO).collect(Collectors.toList());

        return LiveQueueBoardDTO.builder()
                .totalWaiting(waitingCount)
                .totalServing(servingCount)
                .totalCompletedToday(completedCount)
                .servingTokens(serving)
                .nextInLineTokens(nextInLine)
                .build();
    }

    // =========================================================================
    // 2. SERIALIZED PROSPECTUS KIT INVENTORY & SALES REGISTER
    // =========================================================================

    @Transactional
    public List<ProspectusKitMasterDTO> addProspectusStock(AddProspectusStockRequest req) {
        String tenantId = resolveTenantId();
        List<ProspectusKitMaster> savedKits = new ArrayList<>();

        for (String barcode : req.getSerialBarcodes()) {
            if (prospectusKitRepository.findByTenantIdAndSerialBarcode(tenantId, barcode).isPresent()) {
                continue; // Skip existing
            }
            ProspectusKitMaster kit = ProspectusKitMaster.builder()
                    .tenantId(tenantId)
                    .serialBarcode(barcode)
                    .academicYear(req.getAcademicYear())
                    .price(req.getPrice())
                    .status("IN_STOCK")
                    .createdAt(Instant.now())
                    .updatedAt(Instant.now())
                    .build();
            savedKits.add(prospectusKitRepository.save(kit));
        }

        log.info("Ingested {} prospectus kits into inventory for year {}", savedKits.size(), req.getAcademicYear());
        return savedKits.stream().map(this::mapToProspectusDTO).collect(Collectors.toList());
    }

    @Transactional
    public ProspectusKitMasterDTO sellProspectusKit(SellProspectusKitRequest req) {
        String tenantId = resolveTenantId();
        ProspectusKitMaster kit = prospectusKitRepository.findByTenantIdAndSerialBarcode(tenantId, req.getSerialBarcode())
                .orElseThrow(() -> new NoSuchElementException("Prospectus kit not found: " + req.getSerialBarcode()));

        if (!"IN_STOCK".equalsIgnoreCase(kit.getStatus())) {
            throw new IllegalStateException("Prospectus kit is not in stock. Current status: " + kit.getStatus());
        }

        kit.setStatus("SOLD");
        kit.setBuyerParentName(req.getBuyerParentName());
        kit.setBuyerPhone(req.getBuyerPhone());
        kit.setPaymentMode(req.getPaymentMode().toUpperCase());
        kit.setTransactionRef(req.getTransactionRef() != null ? req.getTransactionRef() : "POS-" + System.currentTimeMillis() % 100000);
        kit.setSoldByUserId(req.getSoldByUserId());
        kit.setSoldAt(Instant.now());
        kit.setLinkedInquiryId(req.getLinkedInquiryId());
        kit.setUpdatedAt(Instant.now());

        kit = prospectusKitRepository.save(kit);
        log.info("Sold prospectus kit {} to {} via {}", kit.getSerialBarcode(), req.getBuyerParentName(), req.getPaymentMode());
        return mapToProspectusDTO(kit);
    }

    @Transactional(readOnly = true)
    public ProspectusInventorySummaryDTO getProspectusInventorySummary() {
        String tenantId = resolveTenantId();
        List<ProspectusKitMaster> allKits = prospectusKitRepository.findByTenantIdOrderByCreatedAtDesc(tenantId);

        long inStock = allKits.stream().filter(k -> "IN_STOCK".equalsIgnoreCase(k.getStatus())).count();
        long sold = allKits.stream().filter(k -> "SOLD".equalsIgnoreCase(k.getStatus())).count();
        long damagedOrComp = allKits.stream().filter(k -> "DAMAGED".equalsIgnoreCase(k.getStatus()) || "COMPLIMENTARY".equalsIgnoreCase(k.getStatus())).count();

        BigDecimal totalRevenue = allKits.stream()
                .filter(k -> "SOLD".equalsIgnoreCase(k.getStatus()))
                .map(ProspectusKitMaster::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return ProspectusInventorySummaryDTO.builder()
                .totalKits(allKits.size())
                .totalInStock(inStock)
                .totalSold(sold)
                .totalComplimentaryOrDamaged(damagedOrComp)
                .totalRevenueCollected(totalRevenue.setScale(2, RoundingMode.HALF_UP))
                .build();
    }

    // =========================================================================
    // 3. DOCUMENT VERIFICATION & DEFICIENT DOCUMENT UNDERTAKING DESK
    // =========================================================================

    @Transactional
    public AdmissionDocumentUndertakingDTO issueUndertaking(IssueUndertakingRequest req) {
        String tenantId = resolveTenantId();
        String undertakingNum = "UNDERTAKING-" + LocalDate.now().getYear() + "-" + undertakingSequence.incrementAndGet();

        AdmissionDocumentUndertaking undertaking = AdmissionDocumentUndertaking.builder()
                .tenantId(tenantId)
                .undertakingNumber(undertakingNum)
                .applicationId(req.getApplicationId())
                .studentName(req.getStudentName())
                .parentName(req.getParentName())
                .missingDocumentName(req.getMissingDocumentName())
                .reasonCode(req.getReasonCode())
                .deadlineDate(req.getDeadlineDate())
                .status("PENDING")
                .verifiedByOfficerId(req.getVerifiedByOfficerId())
                .parentSignatureAck(req.getParentSignatureAck() != null ? req.getParentSignatureAck() : true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        undertaking = undertakingRepository.save(undertaking);
        log.info("Issued document undertaking {} for student {} with deadline {}", undertakingNum, req.getStudentName(), req.getDeadlineDate());
        return mapToUndertakingDTO(undertaking);
    }

    @Transactional
    public AdmissionDocumentUndertakingDTO fulfillUndertaking(UUID undertakingId, FulfillUndertakingRequest req) {
        String tenantId = resolveTenantId();
        AdmissionDocumentUndertaking undertaking = undertakingRepository.findById(undertakingId)
                .filter(u -> u.getTenantId().equals(tenantId))
                .orElseThrow(() -> new NoSuchElementException("Undertaking not found: " + undertakingId));

        undertaking.setStatus("FULFILLED");
        undertaking.setFulfillmentDate(req != null && req.getFulfillmentDate() != null ? req.getFulfillmentDate() : LocalDate.now());
        undertaking.setFulfillmentRemarks(req != null && req.getRemarks() != null ? req.getRemarks() : "Original document verified and stamped in custody");
        undertaking.setUpdatedAt(Instant.now());

        undertaking = undertakingRepository.save(undertaking);
        log.info("Fulfilled document undertaking {}", undertaking.getUndertakingNumber());
        return mapToUndertakingDTO(undertaking);
    }

    @Transactional(readOnly = true)
    public List<AdmissionDocumentUndertakingDTO> getOverdueSentinels() {
        String tenantId = resolveTenantId();
        LocalDate today = LocalDate.now();
        List<AdmissionDocumentUndertaking> pending = undertakingRepository.findByTenantIdOrderByCreatedAtDesc(tenantId)
                .stream()
                .filter(u -> "PENDING".equalsIgnoreCase(u.getStatus()))
                .collect(Collectors.toList());

        return pending.stream()
                .map(this::mapToUndertakingDTO)
                .collect(Collectors.toList());
    }

    // =========================================================================
    // 4. INSTANT STUDENT IDENTITY MINTING & WELCOME DOSSIER
    // =========================================================================

    @Transactional
    public AdmissionWelcomeDossierDTO mintStudentIdentity(MintIdentityRequest req) {
        String tenantId = resolveTenantId();
        String admNo = "ADM-" + LocalDate.now().getYear() + "-" + admissionSequence.incrementAndGet();

        AdmissionWelcomeDossier dossier = AdmissionWelcomeDossier.builder()
                .tenantId(tenantId)
                .studentId(req.getStudentId())
                .admissionNumber(admNo)
                .studentName(req.getStudentName())
                .gradeName(req.getGradeName())
                .sectionName(req.getSectionName())
                .rollNumber(req.getRollNumber())
                .houseName(req.getHouseName())
                .smartCardRfidUid(req.getSmartCardRfidUid() != null ? req.getSmartCardRfidUid() : "RFID-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .smartCardStatus("ACTIVE")
                .canteenInitialBalance(req.getCanteenInitialBalance() != null ? req.getCanteenInitialBalance() : new BigDecimal("500.00"))
                .welcomePackGenerated(true)
                .mintedByUserId(req.getMintedByUserId())
                .mintedAt(Instant.now())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        dossier = welcomeDossierRepository.save(dossier);
        log.info("Minted permanent student identity {} for student {}", admNo, req.getStudentName());
        return mapToDossierDTO(dossier);
    }

    @Transactional(readOnly = true)
    public AdmissionWelcomeDossierDTO getWelcomeDossierByStudentId(UUID studentId) {
        String tenantId = resolveTenantId();
        AdmissionWelcomeDossier dossier = welcomeDossierRepository.findByTenantIdAndStudentId(tenantId, studentId)
                .orElseThrow(() -> new NoSuchElementException("Welcome dossier not found for student: " + studentId));

        return mapToDossierDTO(dossier);
    }

    @Transactional
    public AdmissionWelcomeDossierDTO serializeSmartCard(UUID studentId, SerializeSmartCardRequest req) {
        String tenantId = resolveTenantId();
        AdmissionWelcomeDossier dossier = welcomeDossierRepository.findByTenantIdAndStudentId(tenantId, studentId)
                .orElseThrow(() -> new NoSuchElementException("Welcome dossier not found for student: " + studentId));

        dossier.setSmartCardRfidUid(req.getSmartCardRfidUid());
        dossier.setSmartCardStatus("ACTIVE");
        if (req.getAddBalance() != null && req.getAddBalance().compareTo(BigDecimal.ZERO) > 0) {
            dossier.setCanteenInitialBalance(dossier.getCanteenInitialBalance().add(req.getAddBalance()));
        }
        dossier.setUpdatedAt(Instant.now());

        dossier = welcomeDossierRepository.save(dossier);
        log.info("Serialized smart card {} for student {}", req.getSmartCardRfidUid(), dossier.getStudentName());
        return mapToDossierDTO(dossier);
    }

    // =========================================================================
    // 5. SEAT SURRENDER, CANCELLATION & STATUTORY FEE REFUND ENGINE
    // =========================================================================

    public SurrenderRefundCalculationDTO calculateSurrenderRefund(CalculateSurrenderRefundRequest req) {
        long daysUntilStart = ChronoUnit.DAYS.between(req.getSurrenderDate(), req.getAcademicSessionStartDate());

        BigDecimal deductionPct;
        String rule;

        if (daysUntilStart >= 30) {
            deductionPct = new BigDecimal("5.00");
            rule = "Notice >= 30 days before session: Statutory 5% administrative retention";
        } else if (daysUntilStart >= 15) {
            deductionPct = new BigDecimal("10.00");
            rule = "Notice 15-29 days before session: 10% deduction schedule";
        } else if (daysUntilStart >= 0) {
            deductionPct = new BigDecimal("20.00");
            rule = "Notice 0-14 days before session: 20% deduction schedule";
        } else {
            deductionPct = new BigDecimal("50.00");
            rule = "Notice post session commencement: 50% semester retention";
        }

        BigDecimal deductionAmount = req.getTotalFeesPaid()
                .multiply(deductionPct)
                .divide(new BigDecimal("100.00"), 2, RoundingMode.HALF_UP);

        BigDecimal refundableAmount = req.getTotalFeesPaid().subtract(deductionAmount).setScale(2, RoundingMode.HALF_UP);

        return SurrenderRefundCalculationDTO.builder()
                .surrenderDate(req.getSurrenderDate())
                .academicSessionStartDate(req.getAcademicSessionStartDate())
                .daysUntilSessionStart(daysUntilStart)
                .totalFeesPaid(req.getTotalFeesPaid().setScale(2, RoundingMode.HALF_UP))
                .deductionPercentage(deductionPct)
                .deductionAmount(deductionAmount)
                .refundableAmount(refundableAmount)
                .refundRuleApplied(rule)
                .build();
    }

    @Transactional
    public AdmissionSeatSurrenderDTO processSeatSurrender(ProcessSeatSurrenderRequest req) {
        String tenantId = resolveTenantId();
        String surrenderRef = "SURR-" + req.getSurrenderDate().getYear() + "-" + surrenderSequence.incrementAndGet();

        BigDecimal deductionAmount = req.getTotalFeesPaid()
                .multiply(req.getDeductionPercentage())
                .divide(new BigDecimal("100.00"), 2, RoundingMode.HALF_UP);
        BigDecimal refundableAmount = req.getTotalFeesPaid().subtract(deductionAmount).setScale(2, RoundingMode.HALF_UP);

        UUID glVoucherId = UUID.randomUUID(); // Issued Compensating GL Refund Voucher

        AdmissionSeatSurrender surrender = AdmissionSeatSurrender.builder()
                .tenantId(tenantId)
                .surrenderRefNumber(surrenderRef)
                .applicationId(req.getApplicationId())
                .studentName(req.getStudentName())
                .gradeName(req.getGradeName())
                .categoryName(req.getCategoryName().toUpperCase())
                .surrenderReason(req.getSurrenderReason())
                .surrenderDate(req.getSurrenderDate())
                .totalFeesPaid(req.getTotalFeesPaid().setScale(2, RoundingMode.HALF_UP))
                .deductionPercentage(req.getDeductionPercentage().setScale(2, RoundingMode.HALF_UP))
                .deductionAmount(deductionAmount)
                .refundableAmount(refundableAmount)
                .settlementStatus("APPROVED")
                .waitlistStudentPromotedId(req.getWaitlistStudentPromotedId())
                .generalLedgerVoucherId(glVoucherId)
                .authorizedByUserId(req.getAuthorizedByUserId())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        surrender = seatSurrenderRepository.save(surrender);
        log.info("Processed seat surrender {} for {}. Refund: {}, Promoted Waitlist ID: {}",
                surrenderRef, req.getStudentName(), refundableAmount, req.getWaitlistStudentPromotedId());

        return mapToSurrenderDTO(surrender);
    }

    @Transactional(readOnly = true)
    public List<AdmissionSeatSurrenderDTO> getSurrenderAuditLedger() {
        String tenantId = resolveTenantId();
        return seatSurrenderRepository.findByTenantIdOrderByCreatedAtDesc(tenantId)
                .stream()
                .map(this::mapToSurrenderDTO)
                .collect(Collectors.toList());
    }

    // =========================================================================
    // MAPPERS
    // =========================================================================

    private AdmissionQueueTokenDTO mapToTokenDTO(AdmissionQueueToken t) {
        return AdmissionQueueTokenDTO.builder()
                .id(t.getId())
                .tenantId(t.getTenantId())
                .tokenNumber(t.getTokenNumber())
                .category(t.getCategory())
                .visitorName(t.getVisitorName())
                .visitorPhone(t.getVisitorPhone())
                .studentName(t.getStudentName())
                .gradeApplying(t.getGradeApplying())
                .status(t.getStatus())
                .assignedCounter(t.getAssignedCounter())
                .assignedOfficerId(t.getAssignedOfficerId())
                .waitingMinutes(t.getWaitingMinutes())
                .serviceMinutes(t.getServiceMinutes())
                .notes(t.getNotes())
                .calledAt(t.getCalledAt())
                .completedAt(t.getCompletedAt())
                .createdAt(t.getCreatedAt())
                .build();
    }

    private ProspectusKitMasterDTO mapToProspectusDTO(ProspectusKitMaster k) {
        return ProspectusKitMasterDTO.builder()
                .id(k.getId())
                .tenantId(k.getTenantId())
                .serialBarcode(k.getSerialBarcode())
                .academicYear(k.getAcademicYear())
                .price(k.getPrice())
                .status(k.getStatus())
                .buyerParentName(k.getBuyerParentName())
                .buyerPhone(k.getBuyerPhone())
                .paymentMode(k.getPaymentMode())
                .transactionRef(k.getTransactionRef())
                .soldByUserId(k.getSoldByUserId())
                .soldAt(k.getSoldAt())
                .linkedInquiryId(k.getLinkedInquiryId())
                .createdAt(k.getCreatedAt())
                .build();
    }

    private AdmissionDocumentUndertakingDTO mapToUndertakingDTO(AdmissionDocumentUndertaking u) {
        boolean overdue = "PENDING".equalsIgnoreCase(u.getStatus()) && u.getDeadlineDate().isBefore(LocalDate.now());
        return AdmissionDocumentUndertakingDTO.builder()
                .id(u.getId())
                .tenantId(u.getTenantId())
                .undertakingNumber(u.getUndertakingNumber())
                .applicationId(u.getApplicationId())
                .studentName(u.getStudentName())
                .parentName(u.getParentName())
                .missingDocumentName(u.getMissingDocumentName())
                .reasonCode(u.getReasonCode())
                .deadlineDate(u.getDeadlineDate())
                .status(u.getStatus())
                .verifiedByOfficerId(u.getVerifiedByOfficerId())
                .parentSignatureAck(u.getParentSignatureAck())
                .fulfillmentDate(u.getFulfillmentDate())
                .fulfillmentRemarks(u.getFulfillmentRemarks())
                .isOverdue(overdue)
                .createdAt(u.getCreatedAt())
                .build();
    }

    private AdmissionWelcomeDossierDTO mapToDossierDTO(AdmissionWelcomeDossier d) {
        return AdmissionWelcomeDossierDTO.builder()
                .id(d.getId())
                .tenantId(d.getTenantId())
                .studentId(d.getStudentId())
                .admissionNumber(d.getAdmissionNumber())
                .studentName(d.getStudentName())
                .gradeName(d.getGradeName())
                .sectionName(d.getSectionName())
                .rollNumber(d.getRollNumber())
                .houseName(d.getHouseName())
                .smartCardRfidUid(d.getSmartCardRfidUid())
                .smartCardStatus(d.getSmartCardStatus())
                .canteenInitialBalance(d.getCanteenInitialBalance())
                .welcomePackGenerated(d.getWelcomePackGenerated())
                .mintedByUserId(d.getMintedByUserId())
                .mintedAt(d.getMintedAt())
                .admissionLetterNumber("LTR-" + d.getAdmissionNumber())
                .temporaryIdBadgeSlip("TEMP-ID-" + d.getRollNumber() + "-" + d.getSectionName())
                .busPassClearanceToken("BUS-TOKEN-" + d.getAdmissionNumber())
                .uniformBookstoreVoucher("VCHR-KIT-₹5000-" + d.getStudentId().toString().substring(0, 6).toUpperCase())
                .studentPortalAccessPass("PIN-CRED-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase())
                .build();
    }

    private AdmissionSeatSurrenderDTO mapToSurrenderDTO(AdmissionSeatSurrender s) {
        return AdmissionSeatSurrenderDTO.builder()
                .id(s.getId())
                .tenantId(s.getTenantId())
                .surrenderRefNumber(s.getSurrenderRefNumber())
                .applicationId(s.getApplicationId())
                .studentName(s.getStudentName())
                .gradeName(s.getGradeName())
                .categoryName(s.getCategoryName())
                .surrenderReason(s.getSurrenderReason())
                .surrenderDate(s.getSurrenderDate())
                .totalFeesPaid(s.getTotalFeesPaid())
                .deductionPercentage(s.getDeductionPercentage())
                .deductionAmount(s.getDeductionAmount())
                .refundableAmount(s.getRefundableAmount())
                .settlementStatus(s.getSettlementStatus())
                .waitlistStudentPromotedId(s.getWaitlistStudentPromotedId())
                .generalLedgerVoucherId(s.getGeneralLedgerVoucherId())
                .authorizedByUserId(s.getAuthorizedByUserId())
                .createdAt(s.getCreatedAt())
                .build();
    }
}
