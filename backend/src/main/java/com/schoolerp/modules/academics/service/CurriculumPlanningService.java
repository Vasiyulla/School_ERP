package com.schoolerp.modules.academics.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.academics.dto.CurriculumPlanningDTOs;
import com.schoolerp.modules.academics.entity.CurriculumUnit;
import com.schoolerp.modules.academics.entity.WeeklyLessonPlan;
import com.schoolerp.modules.academics.repository.CurriculumUnitRepository;
import com.schoolerp.modules.academics.repository.WeeklyLessonPlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurriculumPlanningService {

    private final CurriculumUnitRepository curriculumUnitRepository;
    private final WeeklyLessonPlanRepository weeklyLessonPlanRepository;

    @Transactional
    public CurriculumUnit createUnit(CurriculumPlanningDTOs.CreateUnitRequest req, String submittedBy) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "apex";
        long count = curriculumUnitRepository.countByTenantId(tenantId) + 1;
        String cleanSub = req.getSubjectName() != null ? req.getSubjectName().replaceAll("[^A-Za-z]", "").toUpperCase() : "SUB";
        String unitCode = String.format("UNIT-%s-%s-%02d", req.getGradeLevel(), cleanSub.substring(0, Math.min(4, cleanSub.length())), count);

        CurriculumUnit unit = CurriculumUnit.builder()
                .tenantId(tenantId)
                .unitCode(unitCode)
                .gradeLevel(req.getGradeLevel() != null ? req.getGradeLevel() : "10")
                .subjectName(req.getSubjectName() != null ? req.getSubjectName() : "Mathematics")
                .unitTitle(req.getUnitTitle() != null ? req.getUnitTitle() : "Algebraic Equations")
                .allocatedPeriods(req.getAllocatedPeriods() > 0 ? req.getAllocatedPeriods() : 12)
                .completedPeriods(0)
                .learningObjectives(req.getLearningObjectives() != null ? req.getLearningObjectives() : "Master quadratic formulae and problem-solving")
                .cbseCompetencies(req.getCbseCompetencies() != null ? req.getCbseCompetencies() : "Critical Thinking, Analytical Reasoning")
                .term(req.getTerm() != null ? req.getTerm() : "Term 1")
                .status("SUBMITTED_FOR_REVIEW")
                .submittedBy(submittedBy != null ? submittedBy : "Teacher")
                .build();

        return curriculumUnitRepository.save(unit);
    }

    @Transactional
    public List<CurriculumUnit> getUnits(String grade, String subject) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "apex";
        List<CurriculumUnit> list = curriculumUnitRepository.findAllByTenantIdAndGradeLevelAndSubjectName(tenantId, grade, subject);
        if (list.isEmpty()) {
            // Seed sample foundational unit for grade & subject
            CurriculumUnit sample = CurriculumUnit.builder()
                    .tenantId(tenantId)
                    .unitCode("UNIT-" + grade + "-SAMPLE-01")
                    .gradeLevel(grade)
                    .subjectName(subject)
                    .unitTitle("Foundations & Core Principles")
                    .allocatedPeriods(16)
                    .completedPeriods(12)
                    .learningObjectives("Core concepts, real-world examples, and problem sets")
                    .cbseCompetencies("NCERT Learning Outcomes 2026")
                    .term("Term 1")
                    .status("APPROVED")
                    .submittedBy("HOD")
                    .reviewedBy("Academic Coordinator")
                    .approvedAt(Instant.now())
                    .build();
            curriculumUnitRepository.save(sample);
            list = List.of(sample);
        }
        return list;
    }

    @Transactional
    public CurriculumUnit approveUnit(UUID unitId, CurriculumPlanningDTOs.ApproveUnitRequest req, String reviewerName) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "apex";
        CurriculumUnit unit = curriculumUnitRepository.findByIdAndTenantId(unitId, tenantId)
                .or(() -> curriculumUnitRepository.findById(unitId))
                .orElseThrow(() -> new NoSuchElementException("Curriculum unit not found: " + unitId));

        String newStatus = req != null && req.getStatus() != null ? req.getStatus() : "APPROVED";
        unit.setStatus(newStatus);
        unit.setReviewedBy(reviewerName != null ? reviewerName : "Principal");
        if ("APPROVED".equalsIgnoreCase(newStatus)) {
            unit.setApprovedAt(Instant.now());
        }
        return curriculumUnitRepository.save(unit);
    }

    @Transactional(readOnly = true)
    public CurriculumPlanningDTOs.PacingRadarDTO getPacingRadar(String grade, String subject) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "apex";
        List<CurriculumUnit> units = curriculumUnitRepository.findAllByTenantIdAndGradeLevelAndSubjectName(tenantId, grade, subject);
        if (units.isEmpty()) {
            units = curriculumUnitRepository.findAllByTenantId(tenantId);
        }

        int totalAllocated = units.stream().mapToInt(CurriculumUnit::getAllocatedPeriods).sum();
        int totalCompleted = units.stream().mapToInt(CurriculumUnit::getCompletedPeriods).sum();
        double actualPct = totalAllocated > 0 ? (totalCompleted * 100.0) / totalAllocated : 0.0;
        double targetPct = 70.0; // Benchmark target based on current term point

        String status;
        String notice;
        if (actualPct >= targetPct) {
            status = actualPct > targetPct + 10 ? "AHEAD" : "ON_TRACK";
            notice = "Curriculum pacing is healthy. Pacing is aligned with academic calendar milestones.";
        } else {
            status = "BEHIND_SCHEDULE";
            notice = String.format("Subject is lagging by %.1f%% against expected pacing. Remedial weekend lecture recommended.", (targetPct - actualPct));
        }

        List<CurriculumPlanningDTOs.UnitProgressDTO> unitDTOs = units.stream()
                .map(u -> CurriculumPlanningDTOs.UnitProgressDTO.builder()
                        .id(u.getId())
                        .unitCode(u.getUnitCode())
                        .unitTitle(u.getUnitTitle())
                        .allocatedPeriods(u.getAllocatedPeriods())
                        .completedPeriods(u.getCompletedPeriods())
                        .status(u.getStatus())
                        .completionRate(u.getAllocatedPeriods() > 0 ? (u.getCompletedPeriods() * 100.0) / u.getAllocatedPeriods() : 0.0)
                        .build())
                .collect(Collectors.toList());

        return CurriculumPlanningDTOs.PacingRadarDTO.builder()
                .gradeLevel(grade)
                .subjectName(subject)
                .totalUnits(units.size())
                .totalAllocatedPeriods(totalAllocated)
                .totalCompletedPeriods(totalCompleted)
                .actualPacingPercentage(Math.round(actualPct * 10.0) / 10.0)
                .targetPacingPercentage(targetPct)
                .pacingStatus(status)
                .alertNotice(notice)
                .units(unitDTOs)
                .build();
    }

    @Transactional
    public WeeklyLessonPlan submitLessonPlan(CurriculumPlanningDTOs.SubmitLessonPlanRequest req, String teacherName) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "apex";

        WeeklyLessonPlan plan = WeeklyLessonPlan.builder()
                .tenantId(tenantId)
                .curriculumUnitId(req.getCurriculumUnitId() != null ? req.getCurriculumUnitId() : UUID.randomUUID())
                .teacherName(teacherName != null ? teacherName : "Teacher")
                .gradeLevel(req.getGradeLevel() != null ? req.getGradeLevel() : "10")
                .subjectName(req.getSubjectName() != null ? req.getSubjectName() : "Mathematics")
                .weekNumber(req.getWeekNumber() > 0 ? req.getWeekNumber() : 1)
                .topicsPlanned(req.getTopicsPlanned() != null ? req.getTopicsPlanned() : "Detailed chapter breakdown and lab simulations")
                .teachingPedagogy(req.getTeachingPedagogy() != null ? req.getTeachingPedagogy() : "Flipped classroom and group exercises")
                .assessmentMethod(req.getAssessmentMethod() != null ? req.getAssessmentMethod() : "Exit ticket and peer quiz")
                .status("SUBMITTED")
                .build();

        return weeklyLessonPlanRepository.save(plan);
    }
}
