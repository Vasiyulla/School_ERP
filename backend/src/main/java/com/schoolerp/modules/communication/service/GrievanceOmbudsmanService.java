package com.schoolerp.modules.communication.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.communication.dto.ReviewGrievanceRequest;
import com.schoolerp.modules.communication.dto.SubmitGrievanceRequest;
import com.schoolerp.modules.communication.entity.GrievanceTicket;
import com.schoolerp.modules.communication.repository.GrievanceTicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GrievanceOmbudsmanService {

    private final GrievanceTicketRepository grievanceRepository;

    private String resolveTenant() {
        String tenantId = TenantContext.getTenantId();
        return (tenantId != null && !tenantId.isBlank()) ? tenantId : "tenant-default";
    }

    @Transactional
    public GrievanceTicket submitGrievance(SubmitGrievanceRequest request, String submitterName) {
        String tenantId = resolveTenant();

        long count = grievanceRepository.countByTenantId(tenantId);
        String ticketNum = String.format("GRV-%d-%04d", LocalDate.now().getYear(), count + 1);

        GrievanceTicket ticket = GrievanceTicket.builder()
                .tenantId(tenantId)
                .ticketNumber(ticketNum)
                .parentName(request.getParentName() != null ? request.getParentName() : submitterName)
                .parentPhone(request.getParentPhone())
                .studentName(request.getStudentName())
                .className(request.getClassName())
                .category(request.getCategory().toUpperCase())
                .subject(request.getSubject())
                .description(request.getDescription())
                .currentTier("TIER_1_TEACHER")
                .status("OPEN")
                .slaDeadline(LocalDateTime.now().plusHours(48))
                .isSlaBreached(false)
                .build();

        log.info("Parent grievance submitted: {} [{}] by {}", ticketNum, ticket.getCategory(), ticket.getParentName());
        return grievanceRepository.save(ticket);
    }

    @Transactional(readOnly = true)
    public List<GrievanceTicket> getGrievances(String status, String tier, Boolean principalAttentionOnly) {
        String tenantId = resolveTenant();

        List<GrievanceTicket> list = grievanceRepository.findAllByTenantIdOrderByCreatedAtDesc(tenantId);

        // Update SLA breach flags in-memory dynamically
        LocalDateTime now = LocalDateTime.now();
        list.forEach(t -> {
            if (!"RESOLVED".equalsIgnoreCase(t.getStatus()) && !"CLOSED".equalsIgnoreCase(t.getStatus()) && now.isAfter(t.getSlaDeadline())) {
                t.setSlaBreached(true);
            }
        });

        if (principalAttentionOnly != null && principalAttentionOnly) {
            return list.stream()
                    .filter(t -> "TIER_3_PRINCIPAL_DESK".equalsIgnoreCase(t.getCurrentTier()) ||
                                 "ESCALATED_TO_PRINCIPAL".equalsIgnoreCase(t.getStatus()) ||
                                 t.isSlaBreached())
                    .collect(Collectors.toList());
        }

        if (status != null && !status.isBlank() && !"ALL".equalsIgnoreCase(status)) {
            return list.stream().filter(t -> status.equalsIgnoreCase(t.getStatus())).collect(Collectors.toList());
        }

        if (tier != null && !tier.isBlank() && !"ALL".equalsIgnoreCase(tier)) {
            return list.stream().filter(t -> tier.equalsIgnoreCase(t.getCurrentTier())).collect(Collectors.toList());
        }

        return list;
    }

    @Transactional
    public GrievanceTicket reviewOrEscalate(UUID ticketId, ReviewGrievanceRequest request, String reviewerStaffName) {
        String tenantId = resolveTenant();

        GrievanceTicket ticket = grievanceRepository.findByIdAndTenantId(ticketId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Grievance ticket not found with ID: " + ticketId));

        String action = request.getAction().toUpperCase();

        if ("RESOLVE".equals(action)) {
            ticket.setStatus("RESOLVED");
            ticket.setResolutionNotes(request.getResolutionNotes());
            ticket.setResolvedByStaffName(reviewerStaffName);
            ticket.setResolvedAt(LocalDateTime.now());
            log.info("Grievance {} resolved by {}", ticket.getTicketNumber(), reviewerStaffName);
        } else if ("ESCALATE_TO_HOD".equals(action)) {
            ticket.setCurrentTier("TIER_2_HOD");
            ticket.setStatus("IN_PROGRESS");
            ticket.setResolutionNotes("Escalated to Head of Department for intervention: " + request.getResolutionNotes());
            log.info("Grievance {} escalated to TIER 2 (HOD)", ticket.getTicketNumber());
        } else if ("ESCALATE_TO_PRINCIPAL".equals(action)) {
            ticket.setCurrentTier("TIER_3_PRINCIPAL_DESK");
            ticket.setStatus("ESCALATED_TO_PRINCIPAL");
            ticket.setResolutionNotes("Urgent escalation to Principal Ombudsman Desk: " + request.getResolutionNotes());
            log.warn("Grievance {} escalated directly to Principal Desk!", ticket.getTicketNumber());
        }

        return grievanceRepository.save(ticket);
    }
}

