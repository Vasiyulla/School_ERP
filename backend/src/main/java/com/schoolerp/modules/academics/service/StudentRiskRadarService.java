package com.schoolerp.modules.academics.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.academics.entity.StudentRiskAssessment;
import com.schoolerp.modules.academics.repository.StudentRiskAssessmentRepository;
import com.schoolerp.modules.examinations.entity.StudentMark;
import com.schoolerp.modules.examinations.repository.StudentMarkRepository;
import com.schoolerp.modules.students.entity.BehaviorIncident;
import com.schoolerp.modules.students.entity.InfirmaryVisit;
import com.schoolerp.modules.students.entity.Student;
import com.schoolerp.modules.students.repository.BehaviorIncidentRepository;
import com.schoolerp.modules.students.repository.InfirmaryVisitRepository;
import com.schoolerp.modules.students.repository.StudentRepository;
import com.schoolerp.modules.system.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentRiskRadarService {

    private final StudentRepository studentRepository;
    private final StudentMarkRepository markRepository;
    private final InfirmaryVisitRepository infirmaryRepository;
    private final BehaviorIncidentRepository behaviorRepository;
    private final StudentRiskAssessmentRepository riskRepository;
    private final AuditLogService auditLogService;

    @Transactional
    public List<StudentRiskAssessment> runSchoolWideEvaluation() {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        List<Student> students = studentRepository.findAllByTenantId(tenantId);

        List<StudentRiskAssessment> results = new ArrayList<>();
        int criticalCount = 0;

        for (Student s : students) {
            int riskScore = 0;
            List<String> drivers = new ArrayList<>();

            // Signal 1: Attendance velocity & baseline
            double attendance = s.getAttendancePercentage();
            if (attendance < 75.0) {
                riskScore += 35;
                drivers.add("Attendance below CBSE 75% limit (" + attendance + "%)");
            } else if (attendance < 85.0) {
                riskScore += 18;
                drivers.add("Declining attendance trend (" + attendance + "%)");
            }

            // Signal 2: Academic marks trajectory
            List<StudentMark> marks = markRepository.findAllByTenantIdAndStudentId(tenantId, s.getId());
            double avgScore = marks.isEmpty() ? 78.0 : marks.stream().mapToInt(StudentMark::getTotalScore).average().orElse(78.0);
            if (avgScore < 45.0) {
                riskScore += 30;
                drivers.add("Critical academic score drop (Avg: " + Math.round(avgScore) + "%)");
            } else if (avgScore < 60.0) {
                riskScore += 15;
                drivers.add("Sub-optimal academic trajectory (Avg: " + Math.round(avgScore) + "%)");
            }

            // Signal 3: Infirmary clinical visit spike
            List<InfirmaryVisit> visits = infirmaryRepository.findAllByTenantIdAndStudentId(tenantId, s.getId());
            if (visits.size() >= 3) {
                riskScore += 20;
                drivers.add("Frequent clinic triage visits (" + visits.size() + " recent visits)");
            } else if (visits.size() == 2) {
                riskScore += 10;
                drivers.add("Repeated health clinic visits (" + visits.size() + ")");
            }

            // Signal 4: Behavior demerits & overdue finance distress
            List<BehaviorIncident> incidents = behaviorRepository.findAllByTenantIdAndStudentIdOrderByRecordedAtDesc(tenantId, s.getId());
            int netPoints = incidents.stream().mapToInt(BehaviorIncident::getPoints).sum();
            if (netPoints < 0) {
                riskScore += 15;
                drivers.add("Negative conduct balance (" + netPoints + " pts)");
            }
            if ("Overdue".equalsIgnoreCase(s.getFeeStatus()) || s.getBalanceAmount() > 10000) {
                riskScore += 10;
                drivers.add("Financial distress indicator (Balance: INR " + s.getBalanceAmount() + ")");
            }

            int clampedScore = Math.min(100, riskScore);
            String tier = clampedScore >= 65 ? "CRITICAL_INTERVENTION" : (clampedScore >= 35 ? "WATCHLIST" : "NORMAL");
            if ("CRITICAL_INTERVENTION".equals(tier)) {
                criticalCount++;
            }

            String recommendation = switch (tier) {
                case "CRITICAL_INTERVENTION" -> "Assign School Counselor + Class Teacher joint meeting & parent outreach within 48 hours.";
                case "WATCHLIST" -> "Place on bi-weekly faculty mentoring check-in and remedial peer group.";
                default -> "Student well-being healthy. Standard academic monitoring.";
            };

            StudentRiskAssessment assessment = StudentRiskAssessment.builder()
                    .tenantId(tenantId)
                    .studentId(s.getId())
                    .studentName(s.getFullName())
                    .className(s.getClassName() + " - " + s.getSectionName())
                    .compositeRiskScore(clampedScore)
                    .riskTier(tier)
                    .attendanceSignal(attendance)
                    .academicAverageSignal(avgScore)
                    .recentInfirmaryVisits(visits.size())
                    .netBehaviorScore(netPoints)
                    .primaryRiskDrivers(drivers.isEmpty() ? "No adverse telemetry signals detected" : String.join("; ", drivers))
                    .recommendedIntervention(recommendation)
                    .evaluatedAt(Instant.now())
                    .build();

            results.add(riskRepository.save(assessment));
        }

        auditLogService.recordEvent(
                "RUN_EARLY_WARNING_RISK_RADAR",
                "academics.student_risk_assessments",
                tenantId,
                String.format("Evaluated %d students across 4 live risk signals: %d flagged CRITICAL_INTERVENTION",
                        results.size(), criticalCount)
        );

        return results;
    }

    public List<StudentRiskAssessment> getHighRiskStudents() {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        List<StudentRiskAssessment> highRisk = riskRepository.findByTenantIdAndRiskTierInOrderByCompositeRiskScoreDesc(
                tenantId, List.of("CRITICAL_INTERVENTION", "WATCHLIST"));
        if (highRisk.isEmpty() && riskRepository.findByTenantIdOrderByCompositeRiskScoreDesc(tenantId).isEmpty()) {
            runSchoolWideEvaluation();
            return riskRepository.findByTenantIdAndRiskTierInOrderByCompositeRiskScoreDesc(
                    tenantId, List.of("CRITICAL_INTERVENTION", "WATCHLIST"));
        }
        return highRisk;
    }
}

