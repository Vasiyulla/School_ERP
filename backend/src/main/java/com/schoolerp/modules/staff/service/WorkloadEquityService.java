package com.schoolerp.modules.staff.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.academics.entity.TeacherSubstitution;
import com.schoolerp.modules.academics.entity.TimetablePeriod;
import com.schoolerp.modules.academics.repository.TeacherSubstitutionRepository;
import com.schoolerp.modules.academics.repository.TimetablePeriodRepository;
import com.schoolerp.modules.examinations.entity.InvigilationDuty;
import com.schoolerp.modules.examinations.repository.InvigilationDutyRepository;
import com.schoolerp.modules.staff.entity.Employee;
import com.schoolerp.modules.staff.repository.EmployeeRepository;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkloadEquityService {

    private final EmployeeRepository employeeRepository;
    private final TimetablePeriodRepository timetableRepository;
    private final TeacherSubstitutionRepository substitutionRepository;
    private final InvigilationDutyRepository invigilationRepository;

    public List<FacultyWorkloadDTO> analyzeFacultyWorkloadEquity() {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        List<Employee> employees = employeeRepository.findAllByTenantId(tenantId);
        List<TimetablePeriod> allPeriods = timetableRepository.findAllByTenantId(tenantId);
        List<TeacherSubstitution> todaySubs = substitutionRepository
                .findByTenantIdAndSubstitutionDateOrderByPeriodNumberAsc(tenantId, LocalDate.now());
        List<InvigilationDuty> allInvigilations = invigilationRepository.findAll();

        List<FacultyWorkloadDTO> report = new ArrayList<>();

        for (Employee emp : employees) {
            long regularPeriods = allPeriods.stream()
                    .filter(p -> !p.isFree() && p.getTeacherName() != null && p.getTeacherName().equalsIgnoreCase(emp.getFullName()))
                    .count();

            long substitutionCount = todaySubs.stream()
                    .filter(s -> s.getSubstituteTeacherId().equals(emp.getId()))
                    .count();

            long invigilationCount = allInvigilations.stream()
                    .filter(d -> d.getTenantId().equals(tenantId) && d.getTeacherId().equals(emp.getId()))
                    .count();

            // Formula: (Regular * 1.0) + (Substitutions * 1.5) + (Invigilations * 1.5)
            double workloadIndex = (regularPeriods * 1.0) + (substitutionCount * 1.5) + (invigilationCount * 1.5);
            String burnoutBand = workloadIndex >= 12.0 ? "HIGH_BURNOUT_RISK" : (workloadIndex >= 6.0 ? "EQUITABLE" : "AVAILABLE_CAPACITY");

            report.add(FacultyWorkloadDTO.builder()
                    .employeeId(emp.getId())
                    .teacherName(emp.getFullName())
                    .department(emp.getDepartment())
                    .designation(emp.getDesignation())
                    .scheduledPeriodsCount((int) regularPeriods)
                    .activeSubstitutionsCount((int) substitutionCount)
                    .examInvigilationsCount((int) invigilationCount)
                    .compositeWorkloadIndex(workloadIndex)
                    .burnoutRiskBand(burnoutBand)
                    .recommendedForNextSubstitution(!"HIGH_BURNOUT_RISK".equals(burnoutBand))
                    .build());
        }

        // Sort ascending by workload index so Principal assigns least-burdened teacher first
        report.sort(Comparator.comparingDouble(FacultyWorkloadDTO::getCompositeWorkloadIndex));
        return report;
    }

    @Data
    @Builder
    public static class FacultyWorkloadDTO {
        private UUID employeeId;
        private String teacherName;
        private String department;
        private String designation;
        private int scheduledPeriodsCount;
        private int activeSubstitutionsCount;
        private int examInvigilationsCount;
        private double compositeWorkloadIndex;
        private String burnoutRiskBand;
        private boolean recommendedForNextSubstitution;
    }
}

