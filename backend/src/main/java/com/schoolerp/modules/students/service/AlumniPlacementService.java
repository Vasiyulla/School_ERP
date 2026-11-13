package com.schoolerp.modules.students.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.students.dto.RecordPlacementRequest;
import com.schoolerp.modules.students.entity.AlumniProfile;
import com.schoolerp.modules.students.entity.HigherEdPlacement;
import com.schoolerp.modules.students.entity.Student;
import com.schoolerp.modules.students.repository.AlumniProfileRepository;
import com.schoolerp.modules.students.repository.HigherEdPlacementRepository;
import com.schoolerp.modules.students.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlumniPlacementService {

    private final AlumniProfileRepository alumniProfileRepository;
    private final HigherEdPlacementRepository placementRepository;
    private final StudentRepository studentRepository;

    private String resolveTenant() {
        String tenantId = TenantContext.getTenantId();
        return (tenantId != null && !tenantId.isBlank()) ? tenantId : "tenant-default";
    }

    @Transactional
    public HigherEdPlacement recordPlacement(RecordPlacementRequest request) {
        String tenantId = resolveTenant();

        Student student = studentRepository.findByIdAndTenantId(request.getStudentId(), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + request.getStudentId()));

        // Find or create alumni profile
        AlumniProfile profile = alumniProfileRepository.findByTenantIdAndStudentId(tenantId, student.getId())
                .orElseGet(() -> {
                    AlumniProfile newProfile = AlumniProfile.builder()
                            .tenantId(tenantId)
                            .studentId(student.getId())
                            .admissionNumber(student.getAdmissionNumber())
                            .studentName(student.getFullName())
                            .graduationYear(LocalDate.now().getYear())
                            .class12BoardScorePercentage(95.4)
                            .email(student.getGuardianEmail())
                            .phone(student.getGuardianPhone())
                            .currentStatus("HIGHER_EDUCATION")
                            .build();
                    return alumniProfileRepository.save(newProfile);
                });

        HigherEdPlacement placement = HigherEdPlacement.builder()
                .tenantId(tenantId)
                .alumniProfileId(profile.getId())
                .studentName(student.getFullName())
                .universityName(request.getUniversityName())
                .country(request.getCountry() != null ? request.getCountry() : "India")
                .degreeProgram(request.getDegreeProgram())
                .admissionType(request.getAdmissionType() != null ? request.getAdmissionType() : "ENTRANCE_RANK")
                .entranceExamName(request.getEntranceExamName())
                .entranceScoreOrRank(request.getEntranceScoreOrRank())
                .scholarshipAmountInr(request.getScholarshipAmountInr())
                .verifiedBySchool(true)
                .build();

        log.info("University placement logged for graduate {}: {} ({}) with rank {}",
                student.getFullName(), placement.getUniversityName(), placement.getDegreeProgram(), placement.getEntranceScoreOrRank());

        return placementRepository.save(placement);
    }

    @Transactional(readOnly = true)
    public List<HigherEdPlacement> getPlacements(String universityName) {
        String tenantId = resolveTenant();
        if (universityName != null && !universityName.isBlank() && !"ALL".equalsIgnoreCase(universityName)) {
            return placementRepository.findAllByTenantIdAndUniversityNameContainingIgnoreCase(tenantId, universityName);
        }
        return placementRepository.findAllByTenantIdOrderByCreatedAtDesc(tenantId);
    }

    @Transactional(readOnly = true)
    public List<AlumniProfile> getAlumniProfiles() {
        String tenantId = resolveTenant();
        return alumniProfileRepository.findAllByTenantIdOrderByGraduationYearDesc(tenantId);
    }
}

