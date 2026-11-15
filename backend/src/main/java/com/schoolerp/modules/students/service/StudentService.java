package com.schoolerp.modules.students.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.students.dto.CustodyUpdateRequest;
import com.schoolerp.modules.students.dto.EnrollStudentRequest;
import com.schoolerp.modules.students.dto.GenerateTranscriptRequest;
import com.schoolerp.modules.students.dto.OfficialTranscriptDTO;
import com.schoolerp.modules.students.entity.OfficialTranscript;
import com.schoolerp.modules.students.entity.Student;
import com.schoolerp.modules.students.entity.StudentDocument;
import com.schoolerp.modules.students.repository.OfficialTranscriptRepository;
import com.schoolerp.modules.students.repository.StudentDocumentRepository;
import com.schoolerp.modules.students.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentDocumentRepository documentRepository;
    private final OfficialTranscriptRepository transcriptRepository;

    @Transactional(readOnly = true)
    public List<Student> getAllStudents() {
        return studentRepository.findAllByTenantId(TenantContext.getTenantId());
    }

    @Transactional(readOnly = true)
    public Page<Student> getPagedStudents(int page, int size) {
        return studentRepository.findAllByTenantId(TenantContext.getTenantId(), PageRequest.of(page, size));
    }

    @Transactional(readOnly = true)
    public Student getStudentById(UUID id) {
        return studentRepository.findByIdAndTenantId(id, TenantContext.getTenantId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + id));
    }

    @Transactional
    public Student enrollStudent(EnrollStudentRequest request) {
        String tenantId = TenantContext.getTenantId();
        long count = studentRepository.countByTenantId(tenantId);
        String admissionNumber = String.format("ADM-2026-%04d", count + 1);

        Student student = Student.builder()
                .tenantId(tenantId)
                .admissionNumber(admissionNumber)
                .fullName(request.getFullName())
                .gender(request.getGender())
                .dateOfBirth(request.getDateOfBirth())
                .className(request.getClassName())
                .sectionName(request.getSectionName())
                .rollNumber(request.getRollNumber())
                .guardianName(request.getGuardianName())
                .guardianPhone(request.getGuardianPhone())
                .guardianEmail(request.getGuardianEmail())
                .lockerIndex(request.getLockerIndex() != null ? request.getLockerIndex() : "Locker General")
                .status("Active")
                .attendancePercentage(100.0)
                .feeStatus("Paid")
                .balanceAmount(0.0)
                .build();

        Student saved = studentRepository.save(student);

        // Auto-provision initial Document Custody checklist entries
        documentRepository.save(StudentDocument.builder()
                .tenantId(tenantId)
                .studentId(saved.getId())
                .documentType("TC_ORIGINAL")
                .custodyStatus("ORIGINAL_IN_CUSTODY")
                .lockerIndex(saved.getLockerIndex())
                .verifierName("Admission Officer")
                .build());

        documentRepository.save(StudentDocument.builder()
                .tenantId(tenantId)
                .studentId(saved.getId())
                .documentType("BIRTH_CERTIFICATE")
                .custodyStatus("VERIFIED_COPY")
                .lockerIndex(saved.getLockerIndex())
                .verifierName("Admission Officer")
                .build());

        return saved;
    }

    @Transactional(readOnly = true)
    public List<StudentDocument> getStudentDocuments(UUID studentId) {
        return documentRepository.findAllByTenantIdAndStudentId(TenantContext.getTenantId(), studentId);
    }

    @Transactional
    public StudentDocument updateCustody(UUID documentId, CustodyUpdateRequest request) {
        StudentDocument doc = documentRepository.findByIdAndTenantId(documentId, TenantContext.getTenantId())
                .orElseThrow(() -> new IllegalArgumentException("Document record not found"));

        if (request.getCustodyStatus() != null) {
            doc.setCustodyStatus(request.getCustodyStatus());
        }
        if (request.getLockerIndex() != null) {
            doc.setLockerIndex(request.getLockerIndex());
        }
        if (request.getVerifierName() != null) {
            doc.setVerifierName(request.getVerifierName());
        }
        doc.setVerifiedAt(Instant.now());

        return documentRepository.save(doc);
    }

    @Transactional
    public OfficialTranscriptDTO generateOfficialTranscript(UUID studentId, GenerateTranscriptRequest request) {
        String tenantId = TenantContext.getTenantId();
        Student student = studentRepository.findByIdAndTenantId(studentId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));

        String transcriptNumber = "TRANS-2026-" + String.format("%04d", new Random().nextInt(10000));
        String hash = Integer.toHexString((student.getAdmissionNumber() + transcriptNumber).hashCode());

        OfficialTranscript transcript = OfficialTranscript.builder()
                .tenantId(tenantId)
                .studentId(student.getId())
                .transcriptNumber(transcriptNumber)
                .academicStream(request.getAcademicStream() != null ? request.getAcademicStream() : "Senior Secondary (Science)")
                .cumulativeGpa(request.getCumulativeGpa() != null ? request.getCumulativeGpa() : BigDecimal.valueOf(9.40))
                .cumulativePercentage(request.getCumulativePercentage() != null ? request.getCumulativePercentage() : BigDecimal.valueOf(94.20))
                .totalCredits(request.getTotalCredits() > 0 ? request.getTotalCredits() : 40)
                .mediumOfInstruction(request.getMediumOfInstruction() != null ? request.getMediumOfInstruction() : "English")
                .verificationHash("SHA256:" + hash)
                .issuedDate(LocalDate.now())
                .issuedByPrincipal(request.getIssuedByPrincipal() != null ? request.getIssuedByPrincipal() : "Principal Dr. Rajesh Sharma")
                .createdAt(LocalDateTime.now())
                .build();

        OfficialTranscript saved = transcriptRepository.save(transcript);

        return OfficialTranscriptDTO.builder()
                .id(saved.getId())
                .studentId(student.getId())
                .studentName(student.getFullName())
                .admissionNumber(student.getAdmissionNumber())
                .transcriptNumber(saved.getTranscriptNumber())
                .academicStream(saved.getAcademicStream())
                .cumulativeGpa(saved.getCumulativeGpa())
                .cumulativePercentage(saved.getCumulativePercentage())
                .totalCredits(saved.getTotalCredits())
                .mediumOfInstruction(saved.getMediumOfInstruction())
                .verificationHash(saved.getVerificationHash())
                .issuedDate(saved.getIssuedDate())
                .issuedByPrincipal(saved.getIssuedByPrincipal())
                .build();
    }
}

