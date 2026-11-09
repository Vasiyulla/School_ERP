package com.schoolerp.modules.academics.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.academics.entity.MorningAssemblyRoster;
import com.schoolerp.modules.academics.repository.MorningAssemblyRosterRepository;
import com.schoolerp.modules.system.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MorningAssemblyService {

    private final MorningAssemblyRosterRepository rosterRepository;
    private final AuditLogService auditLogService;

    @Transactional
    public MorningAssemblyRoster createRoster(
            LocalDate weekStartDate,
            String houseOnDuty,
            String newsStudent,
            String thought,
            String thoughtStudent,
            String pledgeStudent,
            String anthemStudent,
            String teacher) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        LocalDate monday = weekStartDate != null ? weekStartDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)) : LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        MorningAssemblyRoster roster = MorningAssemblyRoster.builder()
                .tenantId(tenantId)
                .weekStartDate(monday)
                .houseOnDuty(houseOnDuty != null ? houseOnDuty : "Tagore House (Blue)")
                .newsReaderStudentName(newsStudent != null ? newsStudent : "Aarav Sharma (Class 10-A)")
                .thoughtForTheDay(thought != null ? thought : "Education is the most powerful weapon which you can use to change the world. — Nelson Mandela")
                .thoughtPresenterStudentName(thoughtStudent != null ? thoughtStudent : "Ananya Deshmukh (Class 10-A)")
                .pledgeLeaderStudentName(pledgeStudent != null ? pledgeStudent : "Ishaan Verma (Class 9-B)")
                .nationalAnthemLeadStudentName(anthemStudent != null ? anthemStudent : "Rhea Sharma (Class 7-C)")
                .teacherInChargeName(teacher != null ? teacher : "Sunita Rao (PGT English)")
                .status("ACTIVE")
                .createdAt(Instant.now())
                .build();

        MorningAssemblyRoster saved = rosterRepository.save(roster);

        auditLogService.recordEvent(
                "ASSIGN_MORNING_ASSEMBLY_ROSTER",
                "academics.morning_assemblies",
                saved.getId().toString(),
                String.format("Scheduled Morning Assembly for week of %s. House: %s, Teacher In-Charge: %s",
                        saved.getWeekStartDate(), saved.getHouseOnDuty(), saved.getTeacherInChargeName())
        );

        return saved;
    }

    public MorningAssemblyRoster getActiveWeekRoster() {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        LocalDate monday = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        return rosterRepository.findByTenantIdAndWeekStartDate(tenantId, monday)
                .orElseGet(() -> createRoster(
                        monday,
                        "Tagore House (Blue)",
                        "Aarav Sharma (Class 10-A)",
                        "Arise, awake, and stop not till the goal is reached. — Swami Vivekananda",
                        "Ananya Deshmukh (Class 10-A)",
                        "Ishaan Verma (Class 9-B)",
                        "Rhea Sharma (Class 7-C)",
                        "Sunita Rao (PGT English)"
                ));
    }

    public List<MorningAssemblyRoster> getAllRosters() {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        List<MorningAssemblyRoster> list = rosterRepository.findByTenantIdOrderByWeekStartDateDesc(tenantId);
        if (list.isEmpty()) {
            getActiveWeekRoster();
            return rosterRepository.findByTenantIdOrderByWeekStartDateDesc(tenantId);
        }
        return list;
    }
}

