package com.schoolerp.modules.students.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.students.entity.InfirmaryVisit;
import com.schoolerp.modules.students.entity.Student;
import com.schoolerp.modules.students.repository.InfirmaryVisitRepository;
import com.schoolerp.modules.students.repository.StudentRepository;
import com.schoolerp.modules.system.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InfirmaryService {

    private final InfirmaryVisitRepository infirmaryVisitRepository;
    private final StudentRepository studentRepository;
    private final AuditLogService auditLogService;

    @Transactional
    public InfirmaryVisit recordVisit(UUID studentId, String chiefComplaint, Double temperatureFahrenheit,
                                     String bloodPressure, Integer pulseBpm, String firstAidGiven,
                                     int restDurationMinutes, String disposition, String attendingNurse) {
        final String activeTenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";

        Student student = studentRepository.findById(studentId)
                .filter(s -> s.getTenantId().equals(activeTenantId))
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));

        boolean shouldNotifyParent = (temperatureFahrenheit != null && temperatureFahrenheit >= 100.0)
                || "SENT_HOME_WITH_PARENT".equalsIgnoreCase(disposition)
                || "REFERRED_TO_HOSPITAL".equalsIgnoreCase(disposition);

        InfirmaryVisit visit = InfirmaryVisit.builder()
                .tenantId(activeTenantId)
                .studentId(studentId)
                .studentName(student.getFullName())
                .className(student.getClassName() + " - " + student.getSectionName())
                .visitTimestamp(LocalDateTime.now())
                .chiefComplaint(chiefComplaint != null ? chiefComplaint : "GENERAL_MALAISE")
                .temperatureFahrenheit(temperatureFahrenheit)
                .bloodPressure(bloodPressure != null ? bloodPressure : "110/70")
                .pulseBpm(pulseBpm != null ? pulseBpm : 75)
                .firstAidGiven(firstAidGiven != null ? firstAidGiven : "Triage assessment, rest, oral hydration")
                .restDurationMinutes(restDurationMinutes > 0 ? restDurationMinutes : 30)
                .disposition(disposition != null ? disposition : "RETURNED_TO_CLASS")
                .attendingNurse(attendingNurse != null ? attendingNurse : "School Clinic Duty Nurse")
                .parentNotified(shouldNotifyParent)
                .parentAlertChannel(shouldNotifyParent ? "WHATSAPP_HEALTH_ALERT" : null)
                .build();

        InfirmaryVisit saved = infirmaryVisitRepository.save(visit);

        auditLogService.recordEvent(
                "INFIRMARY_CLINIC_VISIT",
                "students.infirmary",
                saved.getId().toString(),
                String.format("Student %s presented to infirmary: Complaint=%s, Temp=%s°F, FirstAid='%s', Disposition=%s, ParentAlerted=%s",
                        student.getFullName(), chiefComplaint, temperatureFahrenheit, firstAidGiven, disposition, shouldNotifyParent)
        );

        log.info("Infirmary triage logged for student: {} ({})", student.getFullName(), chiefComplaint);
        return saved;
    }

    @Transactional(readOnly = true)
    public List<InfirmaryVisit> getStudentVisits(UUID studentId) {
        final String activeTenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        return infirmaryVisitRepository.findAllByTenantIdAndStudentId(activeTenantId, studentId);
    }

    @Transactional(readOnly = true)
    public List<InfirmaryVisit> getAllVisits() {
        final String activeTenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        return infirmaryVisitRepository.findAllByTenantId(activeTenantId);
    }
}

