package com.schoolerp.modules.admissions.service;

import com.schoolerp.modules.admissions.dto.*;
import com.schoolerp.modules.admissions.entity.*;
import com.schoolerp.modules.admissions.repository.*;
import com.schoolerp.modules.finance.entity.FeeInvoice;
import com.schoolerp.modules.finance.repository.FeeInvoiceRepository;
import com.schoolerp.modules.students.entity.Student;
import com.schoolerp.modules.students.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdmissionsCrmService {

    private final AdmissionInquiryRepository inquiryRepository;
    private final AdmissionInterviewSlotRepository slotRepository;
    private final AdmissionMeritOfferRepository offerRepository;
    private final StudentRepository studentRepository;
    private final FeeInvoiceRepository feeInvoiceRepository;
    private final AdmissionDocumentChecklistRepository checklistRepository;
    private final GradeSeatCapacityRepository capacityRepository;
    private final CounselorTaskRepository counselorTaskRepository;
    private final AdmissionPointCriteriaRepository criteriaRepository;
    private final StatutoryCertificateRecordRepository certificateRecordRepository;
    private final AdmissionEntranceTestRepository entranceTestRepository;
    private final AdmissionScholarshipApplicationRepository scholarshipApplicationRepository;

    @Transactional
    public AdmissionInquiryDTO createInquiry(String tenantId, CreateInquiryRequest req) {
        String inquiryNumber = "INQ-2026-" + String.format("%04d", new Random().nextInt(10000));
        int initialScore = calculateLeadScore(
                req.getSourceChannel(),
                req.isHasEnrolledSibling(),
                req.isProximityUnderFiveKm(),
                req.getParentEmail() != null && !req.getParentEmail().isBlank()
        );

        AdmissionInquiry inquiry = AdmissionInquiry.builder()
                .tenantId(tenantId)
                .inquiryNumber(inquiryNumber)
                .parentName(req.getParentName())
                .parentPhone(req.getParentPhone())
                .parentEmail(req.getParentEmail())
                .studentName(req.getStudentName())
                .gradeApplyingFor(req.getGradeApplyingFor() != null ? req.getGradeApplyingFor() : "Grade 1")
                .academicYear(req.getAcademicYear() != null ? req.getAcademicYear() : "2026-2027")
                .pipelineStage("INQUIRY_RECEIVED")
                .leadScore(initialScore)
                .sourceChannel(req.getSourceChannel() != null ? req.getSourceChannel() : "WEBSITE_FORM")
                .hasEnrolledSibling(req.isHasEnrolledSibling())
                .proximityUnderFiveKm(req.isProximityUnderFiveKm())
                .assignedCounselorName(req.getAssignedCounselorName() != null ? req.getAssignedCounselorName() : "Admissions Lead")
                .counselorNotes(req.getCounselorNotes())
                .spotFeePaidAmount(BigDecimal.ZERO)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        AdmissionInquiry saved = inquiryRepository.save(inquiry);
        log.info("Created standard inquiry {} for student {}", saved.getInquiryNumber(), saved.getStudentName());
        return mapToInquiryDTO(saved);
    }

    @Transactional
    public AdmissionInquiryDTO rapidWalkInIntake(String tenantId, RapidWalkInIntakeRequest req) {
        String inquiryNumber = "INQ-2026-W" + String.format("%03d", new Random().nextInt(1000));
        String tokenNumber = "TOKEN-W" + (10 + new Random().nextInt(90));

        int initialScore = calculateLeadScore(
                "WALK_IN",
                req.isHasEnrolledSibling(),
                req.isProximityUnderFiveKm(),
                req.getParentEmail() != null && !req.getParentEmail().isBlank()
        );

        AdmissionInquiry inquiry = AdmissionInquiry.builder()
                .tenantId(tenantId)
                .inquiryNumber(inquiryNumber)
                .tokenNumber(tokenNumber)
                .parentName(req.getParentName())
                .parentPhone(req.getParentPhone())
                .parentEmail(req.getParentEmail())
                .studentName(req.getStudentName())
                .gradeApplyingFor(req.getGradeApplyingFor() != null ? req.getGradeApplyingFor() : "Grade 1")
                .academicYear(req.getAcademicYear() != null ? req.getAcademicYear() : "2026-2027")
                .pipelineStage("INQUIRY_RECEIVED")
                .leadScore(Math.max(initialScore, 45)) // Physical presence guarantees high engagement
                .sourceChannel("WALK_IN")
                .hasEnrolledSibling(req.isHasEnrolledSibling())
                .proximityUnderFiveKm(req.isProximityUnderFiveKm())
                .assignedCounselorName(req.getAssignedCounselorName() != null ? req.getAssignedCounselorName() : "Walk-in Desk Officer")
                .counselorNotes(req.getInitialNotes() != null ? req.getInitialNotes() : "Walk-in registration generated at campus front desk.")
                .spotFeePaidAmount(BigDecimal.ZERO)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        AdmissionInquiry saved = inquiryRepository.save(inquiry);
        log.info("Rapid physical walk-in intake registered token {} inquiry {}", tokenNumber, saved.getInquiryNumber());
        return mapToInquiryDTO(saved);
    }

    @Transactional
    public AdmissionInquiryDTO collectSpotFee(String tenantId, UUID inquiryId, CollectSpotFeeRequest req) {
        AdmissionInquiry inquiry = inquiryRepository.findByIdAndTenantId(inquiryId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Admission inquiry not found for ID: " + inquiryId));

        BigDecimal currentPaid = inquiry.getSpotFeePaidAmount() != null ? inquiry.getSpotFeePaidAmount() : BigDecimal.ZERO;
        BigDecimal newTotalPaid = currentPaid.add(req.getAmount() != null ? req.getAmount() : BigDecimal.ZERO);
        inquiry.setSpotFeePaidAmount(newTotalPaid);

        String receiptNumber = "REC-2026-" + String.format("%04d", new Random().nextInt(10000));
        inquiry.setSpotFeeReceiptNumber(receiptNumber);

        // Advance pipeline stage if early
        if ("INQUIRY_RECEIVED".equalsIgnoreCase(inquiry.getPipelineStage())) {
            inquiry.setPipelineStage("APPLICATION_SUBMITTED");
        }

        // Boost lead score
        inquiry.setLeadScore(Math.min(100, inquiry.getLeadScore() + 20));

        String paymentNote = String.format(" [Spot Fee Paid: Rs.%s via %s, Receipt: %s]",
                req.getAmount(),
                req.getPaymentMethod() != null ? req.getPaymentMethod() : "CASH",
                receiptNumber);

        inquiry.setCounselorNotes((inquiry.getCounselorNotes() != null ? inquiry.getCounselorNotes() : "") + paymentNote);
        inquiry.setUpdatedAt(LocalDateTime.now());

        AdmissionInquiry saved = inquiryRepository.save(inquiry);
        log.info("Collected spot prospectus fee of {} for inquiry {}, receipt {}", req.getAmount(), inquiry.getInquiryNumber(), receiptNumber);
        return mapToInquiryDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<AdmissionInquiryDTO> getInquiries(String tenantId, String pipelineStage) {
        List<AdmissionInquiry> list;
        if (pipelineStage != null && !pipelineStage.isBlank()) {
            list = inquiryRepository.findByTenantIdAndPipelineStage(tenantId, pipelineStage);
        } else {
            list = inquiryRepository.findByTenantIdOrderByCreatedAtDesc(tenantId);
        }
        return list.stream().map(this::mapToInquiryDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AdmissionInquiryDTO getInquiryById(String tenantId, UUID inquiryId) {
        AdmissionInquiry inquiry = inquiryRepository.findByIdAndTenantId(inquiryId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Admission inquiry not found: " + inquiryId));
        return mapToInquiryDTO(inquiry);
    }

    @Transactional
    public AdmissionInquiryDTO updatePipelineStage(String tenantId, UUID inquiryId, String newStage, String notes, String dropReason) {
        AdmissionInquiry inquiry = inquiryRepository.findByIdAndTenantId(inquiryId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Admission inquiry not found: " + inquiryId));

        inquiry.setPipelineStage(newStage);
        if (notes != null && !notes.isBlank()) {
            inquiry.setCounselorNotes((inquiry.getCounselorNotes() != null ? inquiry.getCounselorNotes() + " | " : "") + notes);
        }
        if (dropReason != null && !dropReason.isBlank()) {
            inquiry.setDropReason(dropReason);
        }
        inquiry.setUpdatedAt(LocalDateTime.now());

        AdmissionInquiry saved = inquiryRepository.save(inquiry);
        log.info("Updated inquiry {} pipeline stage to {}", saved.getInquiryNumber(), newStage);
        return mapToInquiryDTO(saved);
    }

    @Transactional
    public AdmissionInterviewSlotDTO bookInterviewSlot(String tenantId, BookInterviewSlotRequest req) {
        AdmissionInquiry inquiry = inquiryRepository.findByIdAndTenantId(req.getInquiryId(), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Admission inquiry not found: " + req.getInquiryId()));

        AdmissionInterviewSlot slot = AdmissionInterviewSlot.builder()
                .tenantId(tenantId)
                .inquiryId(req.getInquiryId())
                .slotType(req.getSlotType() != null ? req.getSlotType() : "CAMPUS_TOUR")
                .scheduledAt(req.getScheduledAt() != null ? req.getScheduledAt() : LocalDateTime.now().plusDays(2))
                .interviewerName(req.getInterviewerName() != null ? req.getInterviewerName() : "Vice Principal / Tour Guide")
                .venueOrRoom(req.getVenueOrRoom() != null ? req.getVenueOrRoom() : "Admissions Conference Room A")
                .status("SCHEDULED")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        AdmissionInterviewSlot savedSlot = slotRepository.save(slot);

        // Advance inquiry stage contextually
        if ("CAMPUS_TOUR".equalsIgnoreCase(slot.getSlotType())) {
            inquiry.setPipelineStage("CAMPUS_TOUR_BOOKED");
        } else if ("ENTRANCE_TEST".equalsIgnoreCase(slot.getSlotType())) {
            inquiry.setPipelineStage("ENTRANCE_EVALUATION");
        } else if ("PRINCIPAL_INTERVIEW".equalsIgnoreCase(slot.getSlotType())) {
            inquiry.setPipelineStage("PRINCIPAL_INTERVIEW");
        }
        inquiry.setUpdatedAt(LocalDateTime.now());
        inquiryRepository.save(inquiry);

        log.info("Booked {} slot for inquiry {}", slot.getSlotType(), inquiry.getInquiryNumber());
        return mapToSlotDTO(savedSlot);
    }

    @Transactional
    public AdmissionInterviewSlotDTO evaluateInterviewSlot(String tenantId, UUID slotId, EvaluateInterviewSlotRequest req) {
        AdmissionInterviewSlot slot = slotRepository.findByIdAndTenantId(slotId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Interview slot not found: " + slotId));

        slot.setStatus(req.getStatus() != null ? req.getStatus() : "COMPLETED");
        slot.setEvaluationRating(req.getEvaluationRating() != null ? req.getEvaluationRating() : 5);
        slot.setInterviewerFeedback(req.getInterviewerFeedback());
        slot.setRecommendedGrade(req.getRecommendedGrade());
        slot.setUpdatedAt(LocalDateTime.now());

        AdmissionInterviewSlot saved = slotRepository.save(slot);

        // Also bump parent inquiry lead score if rating is high
        inquiryRepository.findByIdAndTenantId(slot.getInquiryId(), tenantId).ifPresent(inq -> {
            if (req.getEvaluationRating() != null && req.getEvaluationRating() >= 4) {
                inq.setLeadScore(Math.min(100, inq.getLeadScore() + 15));
                inquiryRepository.save(inq);
            }
        });

        log.info("Evaluated slot {} with rating {}/5", slotId, slot.getEvaluationRating());
        return mapToSlotDTO(saved);
    }

    @Transactional
    public AdmissionMeritOfferDTO issueMeritOffer(String tenantId, IssueOfferRequest req) {
        AdmissionInquiry inquiry = inquiryRepository.findByIdAndTenantId(req.getInquiryId(), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Admission inquiry not found: " + req.getInquiryId()));

        BigDecimal tuition = req.getTuitionFeeTotal() != null ? req.getTuitionFeeTotal() : new BigDecimal("75000.00");
        BigDecimal concession = req.getScholarshipOrConcessionAmount() != null ? req.getScholarshipOrConcessionAmount() : BigDecimal.ZERO;
        BigDecimal netPayable = tuition.subtract(concession);

        String offerNumber = "OFFER-2026-" + String.format("%04d", new Random().nextInt(10000));

        AdmissionMeritOffer offer = AdmissionMeritOffer.builder()
                .tenantId(tenantId)
                .inquiryId(inquiry.getId())
                .offerNumber(offerNumber)
                .studentName(inquiry.getStudentName())
                .gradeOffered(inquiry.getGradeApplyingFor())
                .academicYear(inquiry.getAcademicYear())
                .tuitionFeeTotal(tuition)
                .scholarshipOrConcessionAmount(concession)
                .netAdmissionPayable(netPayable)
                .validUntilDate(req.getValidUntilDate() != null ? req.getValidUntilDate() : LocalDate.now().plusDays(14))
                .offerStatus("ISSUED")
                .issuedAt(LocalDateTime.now())
                .build();

        AdmissionMeritOffer savedOffer = offerRepository.save(offer);

        capacityRepository.findByTenantIdAndAcademicYearAndGradeName(tenantId, inquiry.getAcademicYear(), inquiry.getGradeApplyingFor())
                .ifPresent(cap -> {
                    cap.setActiveOffersCount(cap.getActiveOffersCount() + 1);
                    capacityRepository.save(cap);
                });

        inquiry.setPipelineStage("OFFER_ISSUED");
        inquiry.setLeadScore(Math.min(100, inquiry.getLeadScore() + 15));
        inquiry.setUpdatedAt(LocalDateTime.now());
        inquiryRepository.save(inquiry);

        log.info("Issued admission offer {} for student {} (Net Payable: Rs. {})", offerNumber, inquiry.getStudentName(), netPayable);
        return mapToOfferDTO(savedOffer);
    }

    @Transactional
    public AdmissionMeritOfferDTO convertToActiveStudent(String tenantId, UUID offerId) {
        AdmissionMeritOffer offer = offerRepository.findByIdAndTenantId(offerId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Merit offer not found: " + offerId));

        AdmissionInquiry inquiry = inquiryRepository.findByIdAndTenantId(offer.getInquiryId(), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Admission inquiry not found for offer: " + offerId));

        String admissionNumber = "ADM-2026-" + String.format("%04d", new Random().nextInt(10000));

        // 1. Create Student record
        Student student = Student.builder()
                .tenantId(tenantId)
                .admissionNumber(admissionNumber)
                .fullName(inquiry.getStudentName())
                .className(offer.getGradeOffered())
                .sectionName("Section A")
                .rollNumber(1)
                .status("Active")
                .guardianName(inquiry.getParentName())
                .guardianRelationship("Parent")
                .guardianPhone(inquiry.getParentPhone())
                .guardianEmail(inquiry.getParentEmail())
                .attendancePercentage(100.0)
                .feeStatus("Pending")
                .balanceAmount(offer.getNetAdmissionPayable().doubleValue())
                .build();

        Student savedStudent = studentRepository.save(student);

        // 2. Create Initial Fee Invoice
        String invoiceNumber = "INV-2026-" + String.format("%04d", new Random().nextInt(10000));
        BigDecimal spotPaid = inquiry.getSpotFeePaidAmount() != null ? inquiry.getSpotFeePaidAmount() : BigDecimal.ZERO;
        BigDecimal invoiceBalance = offer.getNetAdmissionPayable().subtract(spotPaid);
        if (invoiceBalance.compareTo(BigDecimal.ZERO) < 0) {
            invoiceBalance = BigDecimal.ZERO;
        }

        FeeInvoice invoice = FeeInvoice.builder()
                .tenantId(tenantId)
                .invoiceNumber(invoiceNumber)
                .studentId(savedStudent.getId())
                .studentName(savedStudent.getFullName())
                .totalAmount(offer.getNetAdmissionPayable())
                .paidAmount(spotPaid)
                .balanceAmount(invoiceBalance)
                .dueDate(LocalDate.now().plusDays(15))
                .status(invoiceBalance.compareTo(BigDecimal.ZERO) <= 0 ? "PAID" : "PENDING")
                .build();

        FeeInvoice savedInvoice = feeInvoiceRepository.save(invoice);

        // 3. Update Offer & Inquiry
        offer.setOfferStatus("CONVERTED_TO_STUDENT");
        offer.setConvertedStudentId(savedStudent.getId());
        offer.setConvertedInvoiceId(savedInvoice.getId());
        offer.setAcceptedAt(LocalDateTime.now());
        AdmissionMeritOffer updatedOffer = offerRepository.save(offer);

        inquiry.setPipelineStage("ENROLLED");
        inquiry.setLeadScore(100);
        inquiry.setUpdatedAt(LocalDateTime.now());
        inquiryRepository.save(inquiry);

        capacityRepository.findByTenantIdAndAcademicYearAndGradeName(tenantId, inquiry.getAcademicYear(), offer.getGradeOffered())
                .ifPresent(cap -> {
                    if (cap.getActiveOffersCount() > 0) {
                        cap.setActiveOffersCount(cap.getActiveOffersCount() - 1);
                    }
                    cap.setEnrolledCount(cap.getEnrolledCount() + 1);
                    capacityRepository.save(cap);
                });

        log.info("Successfully converted offer {} to active Student ID {} and Invoice ID {}",
                offer.getOfferNumber(), savedStudent.getId(), savedInvoice.getId());

        return mapToOfferDTO(updatedOffer);
    }

    @Transactional(readOnly = true)
    public AdmissionsFunnelAnalyticsDTO getFunnelAnalytics(String tenantId) {
        List<AdmissionInquiry> inquiries = inquiryRepository.findByTenantIdOrderByCreatedAtDesc(tenantId);
        long total = inquiries.size();

        Map<String, Long> stageCounts = inquiries.stream()
                .collect(Collectors.groupingBy(AdmissionInquiry::getPipelineStage, Collectors.counting()));

        long walkInCount = inquiries.stream()
                .filter(i -> "WALK_IN".equalsIgnoreCase(i.getSourceChannel()))
                .count();

        long websiteCount = inquiries.stream()
                .filter(i -> "WEBSITE_FORM".equalsIgnoreCase(i.getSourceChannel()))
                .count();

        BigDecimal spotFeesTotal = inquiries.stream()
                .map(AdmissionInquiry::getSpotFeePaidAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long offersIssued = offerRepository.countByTenantIdAndOfferStatus(tenantId, "ISSUED")
                + offerRepository.countByTenantIdAndOfferStatus(tenantId, "CONVERTED_TO_STUDENT");

        long enrolledCount = inquiries.stream()
                .filter(i -> "ENROLLED".equalsIgnoreCase(i.getPipelineStage()))
                .count();

        double conversionRate = total > 0 ? ((double) enrolledCount / total) * 100.0 : 0.0;

        return AdmissionsFunnelAnalyticsDTO.builder()
                .totalInquiries(total)
                .stageCounts(stageCounts)
                .walkInCount(walkInCount)
                .websiteCount(websiteCount)
                .spotFeesCollectedTotal(spotFeesTotal)
                .offersIssuedCount(offersIssued)
                .studentsEnrolledCount(enrolledCount)
                .conversionRatePercent(Math.round(conversionRate * 10.0) / 10.0)
                .build();
    }

    private int calculateLeadScore(String channel, boolean hasSibling, boolean proximityUnder5Km, boolean hasEmail) {
        int score = 25;
        if ("WALK_IN".equalsIgnoreCase(channel)) score += 20;
        else if ("WEBSITE_FORM".equalsIgnoreCase(channel)) score += 10;
        else if ("ALUMNI_REFERRAL".equalsIgnoreCase(channel)) score += 25;

        if (hasSibling) score += 25;
        if (proximityUnder5Km) score += 15;
        if (hasEmail) score += 10;

        return Math.min(score, 100);
    }

    private AdmissionInquiryDTO mapToInquiryDTO(AdmissionInquiry entity) {
        return AdmissionInquiryDTO.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .inquiryNumber(entity.getInquiryNumber())
                .tokenNumber(entity.getTokenNumber())
                .parentName(entity.getParentName())
                .parentPhone(entity.getParentPhone())
                .parentEmail(entity.getParentEmail())
                .studentName(entity.getStudentName())
                .gradeApplyingFor(entity.getGradeApplyingFor())
                .academicYear(entity.getAcademicYear())
                .pipelineStage(entity.getPipelineStage())
                .leadScore(entity.getLeadScore())
                .sourceChannel(entity.getSourceChannel())
                .hasEnrolledSibling(entity.isHasEnrolledSibling())
                .proximityUnderFiveKm(entity.isProximityUnderFiveKm())
                .socialCategory(entity.getSocialCategory())
                .admissionQuota(entity.getAdmissionQuota())
                .assignedCounselorName(entity.getAssignedCounselorName())
                .counselorNotes(entity.getCounselorNotes())
                .spotFeePaidAmount(entity.getSpotFeePaidAmount())
                .spotFeeReceiptNumber(entity.getSpotFeeReceiptNumber())
                .dropReason(entity.getDropReason())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private AdmissionInterviewSlotDTO mapToSlotDTO(AdmissionInterviewSlot entity) {
        return AdmissionInterviewSlotDTO.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .inquiryId(entity.getInquiryId())
                .slotType(entity.getSlotType())
                .scheduledAt(entity.getScheduledAt())
                .interviewerName(entity.getInterviewerName())
                .venueOrRoom(entity.getVenueOrRoom())
                .status(entity.getStatus())
                .evaluationRating(entity.getEvaluationRating())
                .interviewerFeedback(entity.getInterviewerFeedback())
                .recommendedGrade(entity.getRecommendedGrade())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private AdmissionMeritOfferDTO mapToOfferDTO(AdmissionMeritOffer entity) {
        return AdmissionMeritOfferDTO.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .inquiryId(entity.getInquiryId())
                .offerNumber(entity.getOfferNumber())
                .studentName(entity.getStudentName())
                .gradeOffered(entity.getGradeOffered())
                .academicYear(entity.getAcademicYear())
                .tuitionFeeTotal(entity.getTuitionFeeTotal())
                .scholarshipOrConcessionAmount(entity.getScholarshipOrConcessionAmount())
                .netAdmissionPayable(entity.getNetAdmissionPayable())
                .validUntilDate(entity.getValidUntilDate())
                .offerStatus(entity.getOfferStatus())
                .convertedStudentId(entity.getConvertedStudentId())
                .convertedInvoiceId(entity.getConvertedInvoiceId())
                .issuedAt(entity.getIssuedAt())
                .acceptedAt(entity.getAcceptedAt())
                .build();
    }

    @Transactional(readOnly = true)
    public ParentApplicationTrackingDTO getPublicTrackingStatus(String tenantId, String inquiryNumber, String parentPhone) {
        AdmissionInquiry inquiry = inquiryRepository.findByTenantIdAndInquiryNumber(tenantId, inquiryNumber)
                .orElseThrow(() -> new IllegalArgumentException("Admission inquiry not found for inquiry number: " + inquiryNumber));

        if (parentPhone != null && !parentPhone.isBlank()) {
            String sanitizedExpected = inquiry.getParentPhone().replaceAll("[^0-9]", "");
            String sanitizedActual = parentPhone.replaceAll("[^0-9]", "");
            if (!sanitizedExpected.endsWith(sanitizedActual) && !sanitizedActual.endsWith(sanitizedExpected)) {
                throw new IllegalArgumentException("Parent phone number does not match registered inquiry records");
            }
        }

        List<AdmissionDocumentChecklist> checklistEntities = checklistRepository.findByTenantIdAndInquiryIdOrderByCreatedAtAsc(tenantId, inquiry.getId());
        List<AdmissionDocumentChecklistDTO> checklistDTOs = checklistEntities.stream()
                .map(this::mapToChecklistDTO)
                .collect(Collectors.toList());

        List<AdmissionInterviewSlot> slots = slotRepository.findByTenantIdAndInquiryIdOrderByScheduledAtAsc(tenantId, inquiry.getId());
        List<AdmissionInterviewSlotDTO> slotDTOs = slots.stream().map(this::mapToSlotDTO).collect(Collectors.toList());

        AdmissionMeritOfferDTO offerDTO = offerRepository.findByTenantIdAndInquiryId(tenantId, inquiry.getId())
                .map(this::mapToOfferDTO)
                .orElse(null);

        int progress = calculateStageProgress(inquiry.getPipelineStage());
        int verifiedCount = (int) checklistEntities.stream().filter(c -> "VERIFIED".equalsIgnoreCase(c.getStatus())).count();

        return ParentApplicationTrackingDTO.builder()
                .inquiryId(inquiry.getId())
                .inquiryNumber(inquiry.getInquiryNumber())
                .tokenNumber(inquiry.getTokenNumber())
                .studentName(inquiry.getStudentName())
                .parentName(inquiry.getParentName())
                .parentPhone(inquiry.getParentPhone())
                .gradeApplyingFor(inquiry.getGradeApplyingFor())
                .academicYear(inquiry.getAcademicYear())
                .pipelineStage(inquiry.getPipelineStage())
                .stageProgressPercent(progress)
                .totalRequiredDocs(Math.max(5, checklistDTOs.size()))
                .verifiedDocsCount(verifiedCount)
                .checklistDocuments(checklistDTOs)
                .offerDetails(offerDTO)
                .interviewSlots(slotDTOs)
                .build();
    }

    @Transactional
    public AdmissionDocumentChecklistDTO uploadChecklistDocument(String tenantId, UUID inquiryId, UploadDocumentRequest req) {
        AdmissionInquiry inquiry = inquiryRepository.findByIdAndTenantId(inquiryId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Admission inquiry not found: " + inquiryId));

        List<AdmissionDocumentChecklist> existing = checklistRepository.findByTenantIdAndInquiryIdOrderByCreatedAtAsc(tenantId, inquiryId);
        AdmissionDocumentChecklist doc = existing.stream()
                .filter(d -> d.getDocumentType().equalsIgnoreCase(req.getDocumentType()))
                .findFirst()
                .orElse(AdmissionDocumentChecklist.builder()
                        .tenantId(tenantId)
                        .inquiryId(inquiryId)
                        .documentType(req.getDocumentType())
                        .createdAt(LocalDateTime.now())
                        .build());

        doc.setDocumentName(req.getDocumentName() != null ? req.getDocumentName() : req.getDocumentType());
        doc.setStorageUrl(req.getStorageUrl());
        doc.setStatus("UNDER_REVIEW");
        doc.setRejectionReason(null);
        doc.setUpdatedAt(LocalDateTime.now());

        AdmissionDocumentChecklist saved = checklistRepository.save(doc);
        log.info("Uploaded document {} ({}) for inquiry {}", doc.getDocumentName(), doc.getDocumentType(), inquiry.getInquiryNumber());
        return mapToChecklistDTO(saved);
    }

    @Transactional
    public AdmissionDocumentChecklistDTO verifyChecklistDocument(String tenantId, UUID documentId, VerifyDocumentRequest req) {
        AdmissionDocumentChecklist doc = checklistRepository.findByIdAndTenantId(documentId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Checklist document not found: " + documentId));

        String newStatus = req.getStatus() != null ? req.getStatus() : "VERIFIED";
        doc.setStatus(newStatus);
        doc.setRejectionReason(req.getRejectionReason());
        doc.setVerifiedByName(req.getVerifiedByName() != null ? req.getVerifiedByName() : "Admissions Verifier");
        doc.setVerifiedAt(LocalDateTime.now());
        doc.setUpdatedAt(LocalDateTime.now());

        AdmissionDocumentChecklist saved = checklistRepository.save(doc);
        log.info("Verified document {} status: {}", doc.getId(), doc.getStatus());
        return mapToChecklistDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<AdmissionDocumentChecklistDTO> getChecklistDocuments(String tenantId, UUID inquiryId) {
        return checklistRepository.findByTenantIdAndInquiryIdOrderByCreatedAtAsc(tenantId, inquiryId).stream()
                .map(this::mapToChecklistDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public GradeSeatCapacityDTO configureGradeCapacity(String tenantId, ConfigureCapacityRequest req) {
        GradeSeatCapacity capacity = capacityRepository.findByTenantIdAndAcademicYearAndGradeName(tenantId, req.getAcademicYear(), req.getGradeName())
                .orElse(GradeSeatCapacity.builder()
                        .tenantId(tenantId)
                        .academicYear(req.getAcademicYear())
                        .gradeName(req.getGradeName())
                        .createdAt(LocalDateTime.now())
                        .build());

        capacity.setTotalSanctionedSeats(req.getTotalSanctionedSeats());
        capacity.setGeneralQuotaSeats(req.getGeneralQuotaSeats());
        capacity.setRteQuotaSeats(req.getRteQuotaSeats());
        capacity.setSiblingStaffQuotaSeats(req.getSiblingStaffQuotaSeats());
        capacity.setManagementQuotaSeats(req.getManagementQuotaSeats());
        capacity.setUpdatedAt(LocalDateTime.now());

        GradeSeatCapacity saved = capacityRepository.save(capacity);
        log.info("Configured seat capacity for {} ({}): {} seats", req.getGradeName(), req.getAcademicYear(), req.getTotalSanctionedSeats());
        return mapToCapacityDTO(saved, tenantId);
    }

    @Transactional(readOnly = true)
    public List<GradeSeatCapacityDTO> getGradeCapacities(String tenantId, String academicYear) {
        List<GradeSeatCapacity> list;
        if (academicYear != null && !academicYear.isBlank()) {
            list = capacityRepository.findByTenantIdAndAcademicYear(tenantId, academicYear);
        } else {
            list = capacityRepository.findByTenantId(tenantId);
        }
        return list.stream().map(c -> mapToCapacityDTO(c, tenantId)).collect(Collectors.toList());
    }

    @Transactional
    public AdmissionMeritOfferDTO promoteWaitlistApplicant(String tenantId, UUID inquiryId) {
        AdmissionInquiry inquiry = inquiryRepository.findByIdAndTenantId(inquiryId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Admission inquiry not found: " + inquiryId));

        capacityRepository.findByTenantIdAndAcademicYearAndGradeName(tenantId, inquiry.getAcademicYear(), inquiry.getGradeApplyingFor())
                .ifPresent(cap -> {
                    cap.setActiveOffersCount(cap.getActiveOffersCount() + 1);
                    capacityRepository.save(cap);
                });

        inquiry.setPipelineStage("OFFER_ISSUED");
        inquiry.setLeadScore(Math.min(100, inquiry.getLeadScore() + 25));
        inquiry.setCounselorNotes((inquiry.getCounselorNotes() != null ? inquiry.getCounselorNotes() + " | " : "") +
                "[Promoted from Waitlist: Official merit offer issued]");
        inquiryRepository.save(inquiry);

        // Check if offer already issued or create new
        AdmissionMeritOffer offer = offerRepository.findByTenantIdAndInquiryId(tenantId, inquiryId)
                .orElseGet(() -> {
                    String offerNumber = "OFFER-2026-W" + String.format("%03d", new Random().nextInt(1000));
                    BigDecimal tuition = new BigDecimal("80000.00");
                    BigDecimal net = tuition.subtract(inquiry.getSpotFeePaidAmount() != null ? inquiry.getSpotFeePaidAmount() : BigDecimal.ZERO);
                    return AdmissionMeritOffer.builder()
                            .tenantId(tenantId)
                            .inquiryId(inquiry.getId())
                            .offerNumber(offerNumber)
                            .studentName(inquiry.getStudentName())
                            .gradeOffered(inquiry.getGradeApplyingFor())
                            .academicYear(inquiry.getAcademicYear())
                            .tuitionFeeTotal(tuition)
                            .scholarshipOrConcessionAmount(BigDecimal.ZERO)
                            .netAdmissionPayable(net)
                            .validUntilDate(LocalDate.now().plusDays(10))
                            .offerStatus("ISSUED")
                            .issuedAt(LocalDateTime.now())
                            .build();
                });

        AdmissionMeritOffer savedOffer = offerRepository.save(offer);
        log.info("Promoted waitlisted applicant {} to active offer {}", inquiry.getStudentName(), savedOffer.getOfferNumber());
        return mapToOfferDTO(savedOffer);
    }

    @Transactional
    public CounselorTaskDTO createCounselorTask(String tenantId, CreateCounselorTaskRequest req) {
        inquiryRepository.findByIdAndTenantId(req.getInquiryId(), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Admission inquiry not found: " + req.getInquiryId()));

        CounselorTask task = CounselorTask.builder()
                .tenantId(tenantId)
                .inquiryId(req.getInquiryId())
                .counselorName(req.getCounselorName() != null ? req.getCounselorName() : "Admissions Lead")
                .taskType(req.getTaskType() != null ? req.getTaskType() : "VERIFICATION_CALL")
                .title(req.getTitle() != null ? req.getTitle() : "Follow-up with Parent")
                .description(req.getDescription())
                .dueDate(req.getDueDate() != null ? req.getDueDate() : LocalDateTime.now().plusDays(1))
                .priority(req.getPriority() != null ? req.getPriority() : "NORMAL")
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .build();

        CounselorTask saved = counselorTaskRepository.save(task);
        log.info("Created counselor task '{}' for counselor {}", saved.getTitle(), saved.getCounselorName());
        return mapToTaskDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<CounselorTaskDTO> getCounselorTasks(String tenantId, String counselorName, String status) {
        List<CounselorTask> list;
        if (counselorName != null && !counselorName.isBlank()) {
            list = counselorTaskRepository.findByTenantIdAndCounselorNameOrderByDueDateAsc(tenantId, counselorName);
        } else if (status != null && !status.isBlank()) {
            list = counselorTaskRepository.findByTenantIdAndStatusOrderByDueDateAsc(tenantId, status);
        } else {
            list = counselorTaskRepository.findByTenantIdOrderByDueDateAsc(tenantId);
        }
        return list.stream().map(this::mapToTaskDTO).collect(Collectors.toList());
    }

    @Transactional
    public CounselorTaskDTO completeCounselorTask(String tenantId, UUID taskId, CompleteCounselorTaskRequest req) {
        CounselorTask task = counselorTaskRepository.findByIdAndTenantId(taskId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Counselor task not found: " + taskId));

        task.setStatus("COMPLETED");
        task.setOutcomeNotes(req.getOutcomeNotes());
        task.setCompletedAt(LocalDateTime.now());

        CounselorTask saved = counselorTaskRepository.save(task);

        // Also append note to inquiry
        inquiryRepository.findByIdAndTenantId(task.getInquiryId(), tenantId).ifPresent(inq -> {
            String note = " [Task Completed: " + task.getTitle() + " - " + req.getOutcomeNotes() + "]";
            inq.setCounselorNotes((inq.getCounselorNotes() != null ? inq.getCounselorNotes() : "") + note);
            inquiryRepository.save(inq);
        });

        log.info("Completed counselor task ID {} with outcome notes", taskId);
        return mapToTaskDTO(saved);
    }

    private int calculateStageProgress(String stage) {
        if (stage == null) return 10;
        switch (stage.toUpperCase()) {
            case "INQUIRY_RECEIVED": return 15;
            case "CAMPUS_TOUR_BOOKED": return 30;
            case "APPLICATION_SUBMITTED": return 45;
            case "ENTRANCE_EVALUATION": return 60;
            case "PRINCIPAL_INTERVIEW": return 75;
            case "OFFER_ISSUED": return 90;
            case "ENROLLED": return 100;
            case "WAITLISTED": return 50;
            case "LOST_OR_DROPPED": return 0;
            default: return 20;
        }
    }

    private AdmissionDocumentChecklistDTO mapToChecklistDTO(AdmissionDocumentChecklist entity) {
        return AdmissionDocumentChecklistDTO.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .inquiryId(entity.getInquiryId())
                .documentType(entity.getDocumentType())
                .documentName(entity.getDocumentName())
                .storageUrl(entity.getStorageUrl())
                .status(entity.getStatus())
                .rejectionReason(entity.getRejectionReason())
                .verifiedByName(entity.getVerifiedByName())
                .verifiedAt(entity.getVerifiedAt())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private GradeSeatCapacityDTO mapToCapacityDTO(GradeSeatCapacity entity, String tenantId) {
        int available = Math.max(0, entity.getTotalSanctionedSeats() - entity.getEnrolledCount() - entity.getActiveOffersCount());
        long waitlist = inquiryRepository.countByTenantIdAndGradeApplyingForAndPipelineStage(tenantId, entity.getGradeName(), "WAITLISTED");

        return GradeSeatCapacityDTO.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .academicYear(entity.getAcademicYear())
                .gradeName(entity.getGradeName())
                .totalSanctionedSeats(entity.getTotalSanctionedSeats())
                .generalQuotaSeats(entity.getGeneralQuotaSeats())
                .rteQuotaSeats(entity.getRteQuotaSeats())
                .siblingStaffQuotaSeats(entity.getSiblingStaffQuotaSeats())
                .managementQuotaSeats(entity.getManagementQuotaSeats())
                .enrolledCount(entity.getEnrolledCount())
                .activeOffersCount(entity.getActiveOffersCount())
                .availableSeats(available)
                .waitlistCount((int) waitlist)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private CounselorTaskDTO mapToTaskDTO(CounselorTask entity) {
        return CounselorTaskDTO.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .inquiryId(entity.getInquiryId())
                .counselorName(entity.getCounselorName())
                .taskType(entity.getTaskType())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .dueDate(entity.getDueDate())
                .priority(entity.getPriority())
                .status(entity.getStatus())
                .outcomeNotes(entity.getOutcomeNotes())
                .completedAt(entity.getCompletedAt())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    // =========================================================================
    // Indian Quota, 100-Point Criteria, Entrance Tests & Scholarship Subsystems
    // =========================================================================

    @Transactional
    public AdmissionPointCriteriaDTO evaluate100PointCriteria(String tenantId, EvaluatePointsRequest req) {
        AdmissionInquiry inq = inquiryRepository.findByIdAndTenantId(req.getInquiryId(), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Inquiry not found: " + req.getInquiryId()));

        int distPts = 10;
        if (req.getDistanceKm() != null) {
            double km = req.getDistanceKm().doubleValue();
            if (km <= 3.0) distPts = 40;
            else if (km <= 6.0) distPts = 30;
            else if (km <= 10.0) distPts = 20;
        }

        int sibPts = req.isHasSibling() ? 30 : 0;
        int alumPts = req.isHasAlumniParent() ? 10 : 0;
        int staffPts = req.isStaffWard() ? 10 : 0;
        int girlPts = req.isSingleGirlChild() ? 5 : 0;
        int firstBornPts = req.isFirstBorn() ? 5 : 0;

        int total = Math.min(100, distPts + sibPts + alumPts + staffPts + girlPts + firstBornPts);

        AdmissionPointCriteria criteria = criteriaRepository.findByTenantIdAndInquiryId(tenantId, inq.getId())
                .orElse(AdmissionPointCriteria.builder()
                        .tenantId(tenantId)
                        .inquiryId(inq.getId())
                        .build());

        criteria.setDistanceKm(req.getDistanceKm() != null ? req.getDistanceKm() : BigDecimal.ZERO);
        criteria.setDistancePoints(distPts);
        criteria.setHasSibling(req.isHasSibling());
        criteria.setSiblingPoints(sibPts);
        criteria.setSiblingStudentId(req.getSiblingStudentId());
        criteria.setHasAlumniParent(req.isHasAlumniParent());
        criteria.setAlumniPoints(alumPts);
        criteria.setAlumniPassingYear(req.getAlumniPassingYear());
        criteria.setStaffWard(req.isStaffWard());
        criteria.setStaffWardPoints(staffPts);
        criteria.setStaffEmployeeId(req.getStaffEmployeeId());
        criteria.setSingleGirlChild(req.isSingleGirlChild());
        criteria.setGirlChildPoints(girlPts);
        criteria.setFirstBorn(req.isFirstBorn());
        criteria.setFirstBornPoints(firstBornPts);
        criteria.setTotalCalculatedPoints(total);
        criteria.setTieBreakerDrawNumber(1000 + new Random().nextInt(9000));
        criteria.setUpdatedAt(LocalDateTime.now());

        AdmissionPointCriteria saved = criteriaRepository.save(criteria);

        inq.setLeadScore(Math.max(inq.getLeadScore(), total));
        if (req.isHasSibling()) inq.setHasEnrolledSibling(true);
        if (req.isSingleGirlChild()) inq.setAdmissionQuota("SINGLE_GIRL_CHILD");
        inquiryRepository.save(inq);

        return mapToCriteriaDTO(saved, inq.getStudentName(), inq.getGradeApplyingFor());
    }

    @Transactional
    public StatutoryCertificateDTO recordStatutoryCertificate(String tenantId, StatutoryCertificateRequest req) {
        AdmissionInquiry inq = inquiryRepository.findByIdAndTenantId(req.getInquiryId(), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Inquiry not found: " + req.getInquiryId()));

        StatutoryCertificateRecord record = certificateRecordRepository
                .findByTenantIdAndInquiryIdAndCertificateType(tenantId, inq.getId(), req.getCertificateType())
                .orElse(StatutoryCertificateRecord.builder()
                        .tenantId(tenantId)
                        .inquiryId(inq.getId())
                        .certificateType(req.getCertificateType())
                        .build());

        record.setCertificateNumber(req.getCertificateNumber());
        record.setIssuingAuthority(req.getIssuingAuthority());
        record.setIssueDate(req.getIssueDate() != null ? req.getIssueDate() : LocalDate.now());
        record.setValidFinancialYear(req.getValidFinancialYear() != null ? req.getValidFinancialYear() : "2026-2027");
        record.setVerificationRemarks(req.getVerificationRemarks());

        if (req.isMarkVerifiedImmediately()) {
            record.setVerifiedStatus("VERIFIED");
            record.setVerifiedBy("Admission Authority");
            record.setVerifiedAt(LocalDateTime.now());

            if (req.getCertificateType().startsWith("CASTE_")) {
                inq.setSocialCategory(req.getCertificateType().replace("CASTE_", ""));
                inquiryRepository.save(inq);
            } else if (req.getCertificateType().equals("EWS_INCOME")) {
                inq.setSocialCategory("EWS");
                inquiryRepository.save(inq);
            } else if (req.getCertificateType().equals("CWSN_DISABILITY")) {
                inq.setSocialCategory("CWSN_DIVYANG");
                inquiryRepository.save(inq);
            }
        } else {
            record.setVerifiedStatus("PENDING");
        }

        record.setUpdatedAt(LocalDateTime.now());
        StatutoryCertificateRecord saved = certificateRecordRepository.save(record);
        return mapToStatutoryCertDTO(saved, inq.getStudentName());
    }

    @Transactional(readOnly = true)
    public MeritListResponseDTO getCategoryMeritList(String tenantId, String gradeName) {
        List<AdmissionInquiry> inquiries = inquiryRepository.findByTenantIdAndGradeApplyingFor(tenantId, gradeName);
        List<AdmissionPointCriteriaDTO> ranked = new ArrayList<>();

        for (AdmissionInquiry inq : inquiries) {
            criteriaRepository.findByTenantIdAndInquiryId(tenantId, inq.getId()).ifPresent(c -> {
                ranked.add(mapToCriteriaDTO(c, inq.getStudentName(), inq.getGradeApplyingFor()));
            });
        }

        ranked.sort((a, b) -> Integer.compare(b.getTotalCalculatedPoints(), a.getTotalCalculatedPoints()));

        return MeritListResponseDTO.builder()
                .gradeName(gradeName)
                .academicYear("2026-2027")
                .totalApplicants(ranked.size())
                .rankedCandidates(ranked)
                .build();
    }

    @Transactional
    public CategorySeatOverviewDTO configureCategoryMatrix(String tenantId, ConfigureCategoryMatrixRequest req) {
        GradeSeatCapacity capacity = capacityRepository
                .findByTenantIdAndAcademicYearAndGradeName(tenantId, req.getAcademicYear(), req.getGradeName())
                .orElse(GradeSeatCapacity.builder()
                        .tenantId(tenantId)
                        .academicYear(req.getAcademicYear())
                        .gradeName(req.getGradeName())
                        .build());

        capacity.setTotalSanctionedSeats(req.getTotalSanctionedSeats());
        capacity.setGeneralQuotaSeats(req.getGeneralQuotaSeats());
        capacity.setRteQuotaSeats(req.getRteQuotaSeats());
        capacity.setObcQuotaSeats(req.getObcQuotaSeats());
        capacity.setScQuotaSeats(req.getScQuotaSeats());
        capacity.setStQuotaSeats(req.getStQuotaSeats());
        capacity.setCwsnQuotaSeats(req.getCwsnQuotaSeats());
        capacity.setSiblingStaffQuotaSeats(req.getSiblingQuotaSeats() + req.getStaffWardQuotaSeats());
        capacity.setStaffWardQuotaSeats(req.getStaffWardQuotaSeats());
        capacity.setSingleGirlChildQuotaSeats(req.getSingleGirlChildQuotaSeats());
        capacity.setManagementQuotaSeats(req.getManagementQuotaSeats());
        capacity.setUpdatedAt(LocalDateTime.now());

        GradeSeatCapacity saved = capacityRepository.save(capacity);
        return mapToCategorySeatOverview(saved, tenantId);
    }

    @Transactional(readOnly = true)
    public CategorySeatOverviewDTO getCategorySeatOverview(String tenantId, String gradeName) {
        GradeSeatCapacity cap = capacityRepository
                .findByTenantIdAndAcademicYearAndGradeName(tenantId, "2026-2027", gradeName)
                .orElseGet(() -> GradeSeatCapacity.builder()
                        .tenantId(tenantId)
                        .academicYear("2026-2027")
                        .gradeName(gradeName)
                        .totalSanctionedSeats(120)
                        .generalQuotaSeats(60)
                        .rteQuotaSeats(30)
                        .obcQuotaSeats(15)
                        .scQuotaSeats(8)
                        .stQuotaSeats(4)
                        .cwsnQuotaSeats(3)
                        .siblingStaffQuotaSeats(10)
                        .staffWardQuotaSeats(5)
                        .singleGirlChildQuotaSeats(5)
                        .managementQuotaSeats(10)
                        .build());

        return mapToCategorySeatOverview(cap, tenantId);
    }

    @Transactional
    public AdmissionInquiryDTO promoteCategoryWaitlist(String tenantId, String quotaName, UUID inquiryId) {
        AdmissionInquiry inq = inquiryRepository.findByIdAndTenantId(inquiryId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Inquiry not found: " + inquiryId));

        inq.setPipelineStage("OFFER_ISSUED");
        inq.setAdmissionQuota(quotaName);
        inq.setUpdatedAt(LocalDateTime.now());
        AdmissionInquiry saved = inquiryRepository.save(inq);

        log.info("Promoted candidate {} from {} category waitlist to OFFER_ISSUED", saved.getInquiryNumber(), quotaName);
        return mapToInquiryDTO(saved);
    }

    @Transactional
    public AdmissionEntranceTestDTO scheduleEntranceTest(String tenantId, ScheduleEntranceTestRequest req) {
        AdmissionInquiry inq = inquiryRepository.findByIdAndTenantId(req.getInquiryId(), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Inquiry not found: " + req.getInquiryId()));

        String gradeDigits = inq.getGradeApplyingFor().replaceAll("[^0-9]", "");
        String testCode = "TEST-2026-G" + (gradeDigits.isEmpty() ? "01" : gradeDigits) + "-" + String.format("%03d", new Random().nextInt(1000));
        String admitCardToken = "ADMIT-2026-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        BigDecimal cutoff = req.getQualifyingCutoffPercentage() != null
                ? req.getQualifyingCutoffPercentage()
                : (inq.getSocialCategory().equals("GENERAL") ? BigDecimal.valueOf(60.0) : BigDecimal.valueOf(50.0));

        AdmissionEntranceTest test = AdmissionEntranceTest.builder()
                .tenantId(tenantId)
                .inquiryId(inq.getId())
                .testCode(testCode)
                .testDate(req.getTestDate())
                .examHall(req.getExamHall())
                .reportingTime(req.getReportingTime())
                .qualifyingCutoffPercentage(cutoff)
                .resultStatus("PENDING")
                .admitCardToken(admitCardToken)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        AdmissionEntranceTest saved = entranceTestRepository.save(test);

        inq.setPipelineStage("ENTRANCE_EVALUATION");
        inquiryRepository.save(inq);

        return mapToEntranceTestDTO(saved, inq.getStudentName(), inq.getGradeApplyingFor());
    }

    @Transactional(readOnly = true)
    public AdmitCardDTO getPublicAdmitCard(String tenantId, String inquiryNumber) {
        AdmissionInquiry inq = inquiryRepository.findByTenantIdAndInquiryNumber(tenantId, inquiryNumber)
                .orElseThrow(() -> new IllegalArgumentException("Inquiry not found: " + inquiryNumber));

        List<AdmissionEntranceTest> tests = entranceTestRepository.findByTenantIdAndInquiryId(tenantId, inq.getId());
        if (tests.isEmpty()) {
            throw new IllegalStateException("No entrance test scheduled for inquiry: " + inquiryNumber);
        }

        AdmissionEntranceTest test = tests.get(0);
        return AdmitCardDTO.builder()
                .admitCardToken(test.getAdmitCardToken())
                .inquiryNumber(inq.getInquiryNumber())
                .studentName(inq.getStudentName())
                .parentName(inq.getParentName())
                .gradeApplyingFor(inq.getGradeApplyingFor())
                .academicYear(inq.getAcademicYear())
                .testCode(test.getTestCode())
                .testDate(test.getTestDate())
                .examHall(test.getExamHall())
                .reportingTime(test.getReportingTime())
                .testInstructions("Please carry printed admit card, 2 passport size photos, and HB pencils. Electronic devices prohibited.")
                .digitalVerificationHash(Integer.toHexString(test.getAdmitCardToken().hashCode()))
                .build();
    }

    @Transactional
    public AdmissionEntranceTestDTO evaluateEntranceTest(String tenantId, UUID testId, EvaluateEntranceTestRequest req) {
        AdmissionEntranceTest test = entranceTestRepository.findById(testId)
                .orElseThrow(() -> new IllegalArgumentException("Entrance test not found: " + testId));

        BigDecimal max = req.getMaximumScore() != null ? req.getMaximumScore() : BigDecimal.valueOf(100.0);
        BigDecimal total = req.getMathScore().add(req.getEnglishScore()).add(req.getScienceScore()).add(req.getAptitudeScore());
        BigDecimal pct = total.divide(max, 4, java.math.RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100.0));

        test.setMathScore(req.getMathScore());
        test.setEnglishScore(req.getEnglishScore());
        test.setScienceScore(req.getScienceScore());
        test.setAptitudeScore(req.getAptitudeScore());
        test.setTotalScore(total);
        test.setMaximumScore(max);
        test.setPercentage(pct);
        test.setEvaluatorRemarks(req.getEvaluatorRemarks());

        boolean qualified = pct.compareTo(test.getQualifyingCutoffPercentage()) >= 0;
        test.setResultStatus(qualified ? "QUALIFIED" : "NOT_QUALIFIED");
        test.setUpdatedAt(LocalDateTime.now());

        AdmissionEntranceTest saved = entranceTestRepository.save(test);

        inquiryRepository.findByIdAndTenantId(test.getInquiryId(), tenantId).ifPresent(inq -> {
            if (qualified) {
                inq.setPipelineStage("PRINCIPAL_INTERVIEW");
                inq.setLeadScore(Math.min(100, inq.getLeadScore() + 20));
            } else {
                inq.setPipelineStage("LOST_OR_DROPPED");
                inq.setDropReason("Did not qualify entrance assessment cut-off (" + pct + "% vs cutoff " + test.getQualifyingCutoffPercentage() + "%)");
            }
            inquiryRepository.save(inq);
        });

        AdmissionInquiry inq = inquiryRepository.findByIdAndTenantId(test.getInquiryId(), tenantId).orElse(null);
        return mapToEntranceTestDTO(saved, inq != null ? inq.getStudentName() : "Student", inq != null ? inq.getGradeApplyingFor() : "Grade");
    }

    @Transactional
    public SiblingConcessionPreviewDTO linkVerifiedSibling(String tenantId, UUID inquiryId, LinkSiblingRequest req) {
        AdmissionInquiry inq = inquiryRepository.findByIdAndTenantId(inquiryId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Inquiry not found: " + inquiryId));

        Student sibling = studentRepository.findById(req.getSiblingStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Sibling student not found: " + req.getSiblingStudentId()));

        inq.setHasEnrolledSibling(true);
        inq.setSiblingStudentId(sibling.getId());
        inq.setAdmissionQuota("SIBLING");
        inq.setLeadScore(Math.min(100, inq.getLeadScore() + 20));
        inquiryRepository.save(inq);

        BigDecimal standardTuition = BigDecimal.valueOf(120000.00);
        BigDecimal concessionPct = BigDecimal.valueOf(15.00);
        BigDecimal savings = standardTuition.multiply(concessionPct).divide(BigDecimal.valueOf(100.0), 2, java.math.RoundingMode.HALF_UP);
        BigDecimal net = standardTuition.subtract(savings);

        return SiblingConcessionPreviewDTO.builder()
                .inquiryId(inq.getId())
                .applicantStudentName(inq.getStudentName())
                .siblingStudentId(sibling.getId())
                .siblingStudentName(sibling.getFullName())
                .siblingGrade(sibling.getClassName())
                .siblingAdmissionNumber(sibling.getAdmissionNumber())
                .standardAnnualTuitionFee(standardTuition)
                .concessionPercentage(concessionPct)
                .estimatedConcessionSavings(savings)
                .netEstimatedTuitionFee(net)
                .updatedLeadScore(inq.getLeadScore())
                .build();
    }

    @Transactional
    public ScholarshipApplicationDTO applyForScholarship(String tenantId, ApplyScholarshipRequest req) {
        AdmissionInquiry inq = inquiryRepository.findByIdAndTenantId(req.getInquiryId(), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Inquiry not found: " + req.getInquiryId()));

        AdmissionScholarshipApplication app = AdmissionScholarshipApplication.builder()
                .tenantId(tenantId)
                .inquiryId(inq.getId())
                .scholarshipType(req.getScholarshipType())
                .claimedAchievementOrIncome(req.getClaimedAchievementOrIncome())
                .requestedConcessionPercentage(req.getRequestedConcessionPercentage() != null ? req.getRequestedConcessionPercentage() : BigDecimal.valueOf(25.0))
                .committeeStatus("SUBMITTED")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        AdmissionScholarshipApplication saved = scholarshipApplicationRepository.save(app);
        return mapToScholarshipDTO(saved, inq.getStudentName());
    }

    @Transactional
    public ScholarshipApplicationDTO decideScholarship(String tenantId, UUID applicationId, DecideScholarshipRequest req) {
        AdmissionScholarshipApplication app = scholarshipApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Scholarship application not found: " + applicationId));

        app.setCommitteeStatus(req.getDecision());
        if ("APPROVED".equalsIgnoreCase(req.getDecision())) {
            app.setApprovedConcessionPercentage(req.getApprovedConcessionPercentage() != null ? req.getApprovedConcessionPercentage() : app.getRequestedConcessionPercentage());
            app.setApprovedAmount(req.getApprovedAmount() != null ? req.getApprovedAmount() : BigDecimal.valueOf(30000.0));
        }
        app.setCommitteeReviewerNotes(req.getCommitteeReviewerNotes());
        app.setReviewedBy(req.getReviewedBy() != null ? req.getReviewedBy() : "Scholarship Committee Chair");
        app.setDecidedAt(LocalDateTime.now());
        app.setUpdatedAt(LocalDateTime.now());

        AdmissionScholarshipApplication saved = scholarshipApplicationRepository.save(app);
        AdmissionInquiry inq = inquiryRepository.findByIdAndTenantId(app.getInquiryId(), tenantId).orElse(null);
        return mapToScholarshipDTO(saved, inq != null ? inq.getStudentName() : "Applicant");
    }

    @Transactional
    public OfferAcceptanceResultDTO acceptOfferAndLockSeat(String tenantId, UUID offerId, AcceptOfferAndLockRequest req) {
        AdmissionMeritOffer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new IllegalArgumentException("Offer not found: " + offerId));

        offer.setOfferStatus("ACCEPTED");
        offer.setParentSignatureName(req.getParentSignatureName());
        offer.setParentIpAddress(req.getParentIpAddress() != null ? req.getParentIpAddress() : "127.0.0.1");
        offer.setAcceptedAt(LocalDateTime.now());

        BigDecimal deposit = req.getSeatLockDepositAmount() != null && req.getSeatLockDepositAmount().compareTo(BigDecimal.ZERO) > 0
                ? req.getSeatLockDepositAmount()
                : BigDecimal.valueOf(25000.00);

        offer.setSeatLockDepositAmount(deposit);
        offer.setDepositStatus("PAID");
        offer.setDepositTransactionId("TXN-UPI-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        AdmissionMeritOffer saved = offerRepository.save(offer);

        inquiryRepository.findByIdAndTenantId(offer.getInquiryId(), tenantId).ifPresent(inq -> {
            inq.setPipelineStage("OFFER_ACCEPTED");
            inq.setCounselorNotes((inq.getCounselorNotes() != null ? inq.getCounselorNotes() : "") + " [Offer Accepted & Seat Lock Deposit of ₹" + deposit + " Verified]");
            inquiryRepository.save(inq);
        });

        return OfferAcceptanceResultDTO.builder()
                .offerId(saved.getId())
                .offerNumber(saved.getOfferNumber())
                .studentName(saved.getStudentName())
                .gradeOffered(saved.getGradeOffered())
                .offerStatus(saved.getOfferStatus())
                .parentSignatureName(saved.getParentSignatureName())
                .acceptedAt(saved.getAcceptedAt())
                .seatLockDepositAmount(saved.getSeatLockDepositAmount())
                .depositTransactionId(saved.getDepositTransactionId())
                .depositStatus(saved.getDepositStatus())
                .confirmationMessage("Admission offer officially accepted and seat locked with token deposit!")
                .build();
    }

    private AdmissionPointCriteriaDTO mapToCriteriaDTO(AdmissionPointCriteria c, String studentName, String grade) {
        String verdict = c.getTotalCalculatedPoints() >= 70 ? "TIER_1_DIRECT_CALL" : "LOTTERY_POOL";
        return AdmissionPointCriteriaDTO.builder()
                .id(c.getId())
                .inquiryId(c.getInquiryId())
                .studentName(studentName)
                .gradeApplyingFor(grade)
                .distanceKm(c.getDistanceKm())
                .distancePoints(c.getDistancePoints())
                .hasSibling(c.isHasSibling())
                .siblingPoints(c.getSiblingPoints())
                .hasAlumniParent(c.isHasAlumniParent())
                .alumniPoints(c.getAlumniPoints())
                .isStaffWard(c.isStaffWard())
                .staffWardPoints(c.getStaffWardPoints())
                .isSingleGirlChild(c.isSingleGirlChild())
                .girlChildPoints(c.getGirlChildPoints())
                .isFirstBorn(c.isFirstBorn())
                .firstBornPoints(c.getFirstBornPoints())
                .totalCalculatedPoints(c.getTotalCalculatedPoints())
                .tieBreakerDrawNumber(c.getTieBreakerDrawNumber())
                .criteriaVerdict(verdict)
                .build();
    }

    private StatutoryCertificateDTO mapToStatutoryCertDTO(StatutoryCertificateRecord r, String studentName) {
        return StatutoryCertificateDTO.builder()
                .id(r.getId())
                .inquiryId(r.getInquiryId())
                .studentName(studentName)
                .certificateType(r.getCertificateType())
                .certificateNumber(r.getCertificateNumber())
                .issuingAuthority(r.getIssuingAuthority())
                .issueDate(r.getIssueDate())
                .validFinancialYear(r.getValidFinancialYear())
                .verifiedStatus(r.getVerifiedStatus())
                .verificationRemarks(r.getVerificationRemarks())
                .verifiedBy(r.getVerifiedBy())
                .verifiedAt(r.getVerifiedAt())
                .build();
    }

    private CategorySeatOverviewDTO mapToCategorySeatOverview(GradeSeatCapacity cap, String tenantId) {
        int avail = Math.max(0, cap.getTotalSanctionedSeats() - cap.getEnrolledCount() - cap.getActiveOffersCount());

        Map<String, Integer> waitlist = new HashMap<>();
        waitlist.put("GENERAL", (int) inquiryRepository.countByTenantIdAndGradeApplyingForAndPipelineStage(tenantId, cap.getGradeName(), "WAITLISTED"));
        waitlist.put("OBC_NCL", (int) inquiryRepository.findByTenantIdAndGradeApplyingForAndAdmissionQuotaAndPipelineStage(tenantId, cap.getGradeName(), "OBC_NCL", "WAITLISTED").size());
        waitlist.put("SC", (int) inquiryRepository.findByTenantIdAndGradeApplyingForAndAdmissionQuotaAndPipelineStage(tenantId, cap.getGradeName(), "SC", "WAITLISTED").size());
        waitlist.put("ST", (int) inquiryRepository.findByTenantIdAndGradeApplyingForAndAdmissionQuotaAndPipelineStage(tenantId, cap.getGradeName(), "ST", "WAITLISTED").size());
        waitlist.put("RTE", (int) inquiryRepository.findByTenantIdAndGradeApplyingForAndAdmissionQuotaAndPipelineStage(tenantId, cap.getGradeName(), "RTE_25_PERCENT", "WAITLISTED").size());
        waitlist.put("SIBLING", (int) inquiryRepository.findByTenantIdAndGradeApplyingForAndAdmissionQuotaAndPipelineStage(tenantId, cap.getGradeName(), "SIBLING", "WAITLISTED").size());

        return CategorySeatOverviewDTO.builder()
                .id(cap.getId())
                .gradeName(cap.getGradeName())
                .academicYear(cap.getAcademicYear())
                .totalSanctionedSeats(cap.getTotalSanctionedSeats())
                .enrolledCount(cap.getEnrolledCount())
                .activeOffersCount(cap.getActiveOffersCount())
                .availableSeatsTotal(avail)
                .generalQuotaSeats(cap.getGeneralQuotaSeats())
                .rteQuotaSeats(cap.getRteQuotaSeats())
                .obcQuotaSeats(cap.getObcQuotaSeats())
                .scQuotaSeats(cap.getScQuotaSeats())
                .stQuotaSeats(cap.getStQuotaSeats())
                .cwsnQuotaSeats(cap.getCwsnQuotaSeats())
                .siblingQuotaSeats(cap.getSiblingStaffQuotaSeats())
                .staffWardQuotaSeats(cap.getStaffWardQuotaSeats())
                .singleGirlChildQuotaSeats(cap.getSingleGirlChildQuotaSeats())
                .managementQuotaSeats(cap.getManagementQuotaSeats())
                .quotaWaitlistCounts(waitlist)
                .build();
    }

    private AdmissionEntranceTestDTO mapToEntranceTestDTO(AdmissionEntranceTest t, String studentName, String grade) {
        return AdmissionEntranceTestDTO.builder()
                .id(t.getId())
                .inquiryId(t.getInquiryId())
                .studentName(studentName)
                .gradeApplyingFor(grade)
                .testCode(t.getTestCode())
                .testDate(t.getTestDate())
                .examHall(t.getExamHall())
                .reportingTime(t.getReportingTime())
                .mathScore(t.getMathScore())
                .englishScore(t.getEnglishScore())
                .scienceScore(t.getScienceScore())
                .aptitudeScore(t.getAptitudeScore())
                .totalScore(t.getTotalScore())
                .maximumScore(t.getMaximumScore())
                .percentage(t.getPercentage())
                .qualifyingCutoffPercentage(t.getQualifyingCutoffPercentage())
                .resultStatus(t.getResultStatus())
                .evaluatorRemarks(t.getEvaluatorRemarks())
                .admitCardToken(t.getAdmitCardToken())
                .build();
    }

    private ScholarshipApplicationDTO mapToScholarshipDTO(AdmissionScholarshipApplication s, String studentName) {
        return ScholarshipApplicationDTO.builder()
                .id(s.getId())
                .inquiryId(s.getInquiryId())
                .studentName(studentName)
                .scholarshipType(s.getScholarshipType())
                .claimedAchievementOrIncome(s.getClaimedAchievementOrIncome())
                .requestedConcessionPercentage(s.getRequestedConcessionPercentage())
                .committeeStatus(s.getCommitteeStatus())
                .approvedConcessionPercentage(s.getApprovedConcessionPercentage())
                .approvedAmount(s.getApprovedAmount())
                .committeeReviewerNotes(s.getCommitteeReviewerNotes())
                .reviewedBy(s.getReviewedBy())
                .decidedAt(s.getDecidedAt())
                .createdAt(s.getCreatedAt())
                .build();
    }
}

