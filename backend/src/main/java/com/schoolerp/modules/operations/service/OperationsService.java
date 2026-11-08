package com.schoolerp.modules.operations.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.operations.entity.*;
import com.schoolerp.modules.operations.repository.*;
import com.schoolerp.modules.system.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OperationsService {

    private final VehicleRepository vehicleRepository;
    private final TransportRouteRepository transportRouteRepository;
    private final GpsPingRepository gpsPingRepository;
    private final BookRepository bookRepository;
    private final BookIssueRecordRepository bookIssueRecordRepository;
    private final AuditLogService auditLogService;

    // --- Transport Operations ---

    public List<Vehicle> getVehicles() {
        return vehicleRepository.findByTenantId(TenantContext.getTenantId());
    }

    public List<TransportRoute> getRoutes() {
        return transportRouteRepository.findByTenantId(TenantContext.getTenantId());
    }

    @Transactional
    public GpsPing recordGpsPing(String vehicleRegistration, double lat, double lng, double speed, double heading) {
        String tenantId = TenantContext.getTenantId();
        GpsPing ping = GpsPing.builder()
                .tenantId(tenantId)
                .vehicleRegistration(vehicleRegistration)
                .latitude(lat)
                .longitude(lng)
                .speedKmph(speed)
                .headingDegrees(heading)
                .build();
        return gpsPingRepository.save(ping);
    }

    public Optional<GpsPing> getLatestVehicleLocation(String vehicleRegistration) {
        return gpsPingRepository.findTop1ByTenantIdAndVehicleRegistrationOrderByRecordedAtDesc(
                TenantContext.getTenantId(), vehicleRegistration);
    }

    // --- Library Operations ---

    public List<Book> getBooks() {
        return bookRepository.findByTenantId(TenantContext.getTenantId());
    }

    public List<BookIssueRecord> getIssueRecords() {
        return bookIssueRecordRepository.findByTenantId(TenantContext.getTenantId());
    }

    @Transactional
    public BookIssueRecord issueBook(UUID bookId, UUID studentId, String studentName, int loanDays) {
        String tenantId = TenantContext.getTenantId();
        Book book = bookRepository.findById(bookId)
                .filter(b -> b.getTenantId().equals(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("Book not found: " + bookId));

        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("No available copies remaining for: " + book.getTitle());
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        BookIssueRecord record = BookIssueRecord.builder()
                .tenantId(tenantId)
                .bookId(book.getId())
                .bookTitle(book.getTitle())
                .studentId(studentId)
                .studentName(studentName)
                .issuedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(loanDays > 0 ? loanDays : 14))
                .status("ISSUED")
                .fineAmount(BigDecimal.ZERO)
                .build();

        BookIssueRecord saved = bookIssueRecordRepository.save(record);

        auditLogService.recordEvent(
                "LIBRARY_ISSUE",
                "operations.library",
                saved.getId().toString(),
                "Book '" + book.getTitle() + "' issued to student " + studentName + " (Due: " + saved.getDueDate() + ")"
        );
        return saved;
    }

    @Transactional
    public BookIssueRecord returnBook(UUID issueRecordId) {
        String tenantId = TenantContext.getTenantId();
        BookIssueRecord record = bookIssueRecordRepository.findById(issueRecordId)
                .filter(r -> r.getTenantId().equals(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("Issue record not found: " + issueRecordId));

        if ("RETURNED".equals(record.getStatus())) {
            return record;
        }

        record.setStatus("RETURNED");
        record.setReturnedDate(LocalDate.now());

        bookRepository.findById(record.getBookId()).ifPresent(book -> {
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            bookRepository.save(book);
        });

        BookIssueRecord saved = bookIssueRecordRepository.save(record);

        auditLogService.recordEvent(
                "LIBRARY_RETURN",
                "operations.library",
                saved.getId().toString(),
                "Book '" + record.getBookTitle() + "' returned by " + record.getStudentName()
        );
        return saved;
    }
}

