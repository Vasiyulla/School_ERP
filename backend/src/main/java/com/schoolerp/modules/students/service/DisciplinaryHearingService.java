package com.schoolerp.modules.students.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.students.dto.DisciplinaryDTOs;
import com.schoolerp.modules.students.entity.DisciplinaryHearing;
import com.schoolerp.modules.students.entity.DisciplinarySanction;
import com.schoolerp.modules.students.repository.DisciplinaryHearingRepository;
import com.schoolerp.modules.students.repository.DisciplinarySanctionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DisciplinaryHearingService {

    private final DisciplinaryHearingRepository hearingRepository;
    private final DisciplinarySanctionRepository sanctionRepository;

    @Transactional
    public DisciplinaryHearing conveneHearing(DisciplinaryDTOs.ConveneHearingRequest req) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "apex";
        long count = hearingRepository.countByTenantId(tenantId) + 1;
        String docketNo = String.format("DISC-2026-%04d", count);

        DisciplinaryHearing hearing = DisciplinaryHearing.builder()
                .tenantId(tenantId)
                .docketNumber(docketNo)
                .studentId(req.getStudentId() != null ? req.getStudentId() : UUID.randomUUID())
                .studentName(req.getStudentName() != null ? req.getStudentName() : "Student")
                .gradeLevel(req.getGradeLevel() != null ? req.getGradeLevel() : "10")
                .incidentDate(req.getIncidentDate() != null ? req.getIncidentDate() : LocalDate.now().minusDays(2))
                .infractionCategory(req.getInfractionCategory() != null ? req.getInfractionCategory() : "BULLYING_HARASSMENT")
                .chargeDescription(req.getChargeDescription() != null ? req.getChargeDescription() : "Allegation of cyber-bullying and verbal harassment on school grounds")
                .hearingDate(req.getHearingDate() != null ? req.getHearingDate() : LocalDate.now().plusDays(3))
                .committeeMembers(req.getCommitteeMembers() != null ? req.getCommitteeMembers() : "Vice Principal, School Counselor, Academic Coordinator")
                .parentNoticeServedAt(Instant.now())
                .status("SCHEDULED")
                .build();

        log.info("DISCIPLINARY HEARING CONVENED: Docket [{}] for student [{}] scheduled on [{}]",
                docketNo, hearing.getStudentName(), hearing.getHearingDate());

        return hearingRepository.save(hearing);
    }

    @Transactional
    public DisciplinarySanction recordSanction(UUID hearingId, DisciplinaryDTOs.RecordSanctionRequest req, String authorizedBy) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "apex";
        DisciplinaryHearing hearing = hearingRepository.findByIdAndTenantId(hearingId, tenantId)
                .or(() -> hearingRepository.findById(hearingId))
                .orElseThrow(() -> new NoSuchElementException("Disciplinary hearing docket not found: " + hearingId));

        hearing.setStatus("SANCTIONED");
        hearing.setFindingsSummary(req.getFindingsSummary() != null ? req.getFindingsSummary() : "Committee confirmed behavioral violation after hearing student and witness statements");
        hearingRepository.save(hearing);

        LocalDate start = req.getStartDate() != null ? req.getStartDate() : LocalDate.now();
        int days = req.getDurationDays() > 0 ? req.getDurationDays() : 3;
        LocalDate end = req.getEndDate() != null ? req.getEndDate() : start.plusDays(days);

        DisciplinarySanction sanction = DisciplinarySanction.builder()
                .tenantId(tenantId)
                .hearingDocketId(hearing.getId())
                .studentId(hearing.getStudentId())
                .studentName(hearing.getStudentName())
                .sanctionType(req.getSanctionType() != null ? req.getSanctionType() : "IN_SCHOOL_SUSPENSION")
                .durationDays(days)
                .startDate(start)
                .endDate(end)
                .sanctionTerms(req.getSanctionTerms() != null ? req.getSanctionTerms() : "Suspension from regular classes; assigned to supervised study hall with reflective counseling")
                .reinstatementConditions(req.getReinstatementConditions() != null ? req.getReinstatementConditions() : "Written letter of reflection and behavioral contract signed by parents")
                .authorizedBy(authorizedBy != null ? authorizedBy : "Principal")
                .build();

        return sanctionRepository.save(sanction);
    }

    @Transactional
    public DisciplinaryDTOs.StudentDisciplinaryHistoryDTO getStudentDisciplinaryHistory(UUID studentId) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "apex";
        List<DisciplinaryHearing> hearings = hearingRepository.findAllByTenantIdAndStudentId(tenantId, studentId);
        List<DisciplinarySanction> sanctions = sanctionRepository.findAllByTenantIdAndStudentId(tenantId, studentId);

        String studentName = !hearings.isEmpty() ? hearings.get(0).getStudentName()
                : (!sanctions.isEmpty() ? sanctions.get(0).getStudentName() : "Student");

        List<DisciplinaryDTOs.HearingSummaryDTO> hearingDTOs = hearings.stream()
                .map(h -> DisciplinaryDTOs.HearingSummaryDTO.builder()
                        .id(h.getId())
                        .docketNumber(h.getDocketNumber())
                        .hearingDate(h.getHearingDate())
                        .infractionCategory(h.getInfractionCategory())
                        .status(h.getStatus())
                        .findingsSummary(h.getFindingsSummary())
                        .build())
                .collect(Collectors.toList());

        List<DisciplinaryDTOs.SanctionSummaryDTO> sanctionDTOs = sanctions.stream()
                .map(s -> DisciplinaryDTOs.SanctionSummaryDTO.builder()
                        .id(s.getId())
                        .sanctionType(s.getSanctionType())
                        .durationDays(s.getDurationDays())
                        .startDate(s.getStartDate())
                        .endDate(s.getEndDate())
                        .sanctionTerms(s.getSanctionTerms())
                        .authorizedBy(s.getAuthorizedBy())
                        .build())
                .collect(Collectors.toList());

        return DisciplinaryDTOs.StudentDisciplinaryHistoryDTO.builder()
                .studentId(studentId)
                .studentName(studentName)
                .totalHearings(hearings.size())
                .activeSanctions(sanctions.size())
                .hearings(hearingDTOs)
                .sanctions(sanctionDTOs)
                .build();
    }
}
