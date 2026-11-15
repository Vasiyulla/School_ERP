package com.schoolerp.modules.students.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.students.dto.CustodyUpdateRequest;
import com.schoolerp.modules.students.dto.EnrollStudentRequest;
import com.schoolerp.modules.students.entity.Student;
import com.schoolerp.modules.students.entity.StudentDocument;
import com.schoolerp.modules.students.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Student>>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(ApiResponse.ok(students));
    }

    @GetMapping("/paged")
    public ResponseEntity<ApiResponse<Page<Student>>> getPagedStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<Student> paged = studentService.getPagedStudents(page, size);
        return ResponseEntity.ok(ApiResponse.ok(paged));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Student>> getStudentById(@PathVariable UUID id) {
        Student student = studentService.getStudentById(id);
        return ResponseEntity.ok(ApiResponse.ok(student));
    }

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PRINCIPAL')")
    public ResponseEntity<ApiResponse<Student>> enrollStudent(@Valid @RequestBody EnrollStudentRequest request) {
        Student enrolled = studentService.enrollStudent(request);
        return ResponseEntity.ok(ApiResponse.ok("Student enrolled successfully", enrolled));
    }

    @GetMapping("/{id}/documents")
    public ResponseEntity<ApiResponse<List<StudentDocument>>> getDocuments(@PathVariable UUID id) {
        List<StudentDocument> docs = studentService.getStudentDocuments(id);
        return ResponseEntity.ok(ApiResponse.ok(docs));
    }

    @PatchMapping("/documents/{documentId}/custody")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PRINCIPAL')")
    public ResponseEntity<ApiResponse<StudentDocument>> updateCustody(
            @PathVariable UUID documentId,
            @RequestBody CustodyUpdateRequest request) {
        StudentDocument updated = studentService.updateCustody(documentId, request);
        return ResponseEntity.ok(ApiResponse.ok("Document custody updated", updated));
    }

    @PostMapping("/{id}/transcripts/generate")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PRINCIPAL')")
    public ResponseEntity<ApiResponse<com.schoolerp.modules.students.dto.OfficialTranscriptDTO>> generateTranscript(
            @PathVariable UUID id,
            @RequestBody(required = false) com.schoolerp.modules.students.dto.GenerateTranscriptRequest request) {
        var transcript = studentService.generateOfficialTranscript(id, request != null ? request : new com.schoolerp.modules.students.dto.GenerateTranscriptRequest());
        return ResponseEntity.ok(ApiResponse.ok("Official cumulative multi-year transcript generated with tamper-proof cryptographic verification", transcript));
    }
}

