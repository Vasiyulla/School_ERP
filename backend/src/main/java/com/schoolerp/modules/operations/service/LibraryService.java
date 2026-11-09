package com.schoolerp.modules.operations.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.operations.entity.Book;
import com.schoolerp.modules.operations.entity.BookIssueRecord;
import com.schoolerp.modules.operations.repository.BookIssueRecordRepository;
import com.schoolerp.modules.operations.repository.BookRepository;
import com.schoolerp.modules.students.entity.Student;
import com.schoolerp.modules.students.repository.StudentRepository;
import com.schoolerp.modules.system.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LibraryService {

    private final BookRepository bookRepository;
    private final BookIssueRecordRepository bookIssueRecordRepository;
    private final StudentRepository studentRepository;
    private final AuditLogService auditLogService;

    private static final BigDecimal DAILY_OVERDUE_FINE_INR = new BigDecimal("5.00");

    @Transactional
    public Book registerBook(String title, String author, String isbn, String category, int totalCopies, String rackLocation, String barcodeTag) {
        final String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";

        Book book = Book.builder()
                .tenantId(tenantId)
                .title(title)
                .author(author)
                .isbn(isbn)
                .category(category != null ? category : "General")
                .totalCopies(totalCopies > 0 ? totalCopies : 1)
                .availableCopies(totalCopies > 0 ? totalCopies : 1)
                .rackLocation(rackLocation != null ? rackLocation : "Main Library")
                .barcodeTag(barcodeTag != null ? barcodeTag : "TAG-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .build();

        Book saved = bookRepository.save(book);
        auditLogService.logAction("LIBRARIAN", "REGISTER_BOOK", "operations.library_books", saved.getId().toString(),
                "Registered catalog book: " + saved.getTitle() + " (ISBN: " + saved.getIsbn() + ")");
        return saved;
    }

    public List<Book> getAllBooks() {
        final String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        return bookRepository.findByTenantId(tenantId);
    }

    public List<Book> searchBooks(String query) {
        final String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        if (query == null || query.isBlank()) {
            return bookRepository.findByTenantId(tenantId);
        }
        final String q = query.toLowerCase().trim();
        return bookRepository.findByTenantId(tenantId).stream()
                .filter(b -> (b.getTitle() != null && b.getTitle().toLowerCase().contains(q)) ||
                             (b.getAuthor() != null && b.getAuthor().toLowerCase().contains(q)) ||
                             (b.getIsbn() != null && b.getIsbn().toLowerCase().contains(q)) ||
                             (b.getCategory() != null && b.getCategory().toLowerCase().contains(q)))
                .collect(Collectors.toList());
    }

    @Transactional
    public BookIssueRecord issueBook(UUID bookId, UUID studentId, int loanDays) {
        final String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";

        Book book = bookRepository.findById(bookId)
                .filter(b -> b.getTenantId().equals(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("Book not found: " + bookId));

        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("No available copies of book '" + book.getTitle() + "' remaining in library");
        }

        Student student = studentRepository.findById(studentId)
                .filter(s -> s.getTenantId().equals(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));

        int duration = loanDays > 0 ? loanDays : 14;
        LocalDate now = LocalDate.now();
        LocalDate dueDate = now.plusDays(duration);

        BookIssueRecord record = BookIssueRecord.builder()
                .tenantId(tenantId)
                .bookId(book.getId())
                .bookTitle(book.getTitle())
                .studentId(student.getId())
                .studentName(student.getFullName())
                .issuedDate(now)
                .dueDate(dueDate)
                .status("ISSUED")
                .fineAmount(BigDecimal.ZERO)
                .build();

        // Decrement available copies
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        BookIssueRecord savedRecord = bookIssueRecordRepository.save(record);
        auditLogService.logAction("LIBRARIAN", "ISSUE_BOOK", "operations.library_issue_records", savedRecord.getId().toString(),
                "Issued '" + book.getTitle() + "' to student " + student.getFullName() + " (Due: " + dueDate + ")");

        return savedRecord;
    }

    @Transactional
    public BookIssueRecord returnBook(UUID issueRecordId) {
        final String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";

        BookIssueRecord record = bookIssueRecordRepository.findById(issueRecordId)
                .filter(r -> r.getTenantId().equals(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("Book issue record not found: " + issueRecordId));

        if ("RETURNED".equalsIgnoreCase(record.getStatus())) {
            return record; // Idempotent
        }

        LocalDate now = LocalDate.now();
        record.setReturnedDate(now);
        record.setStatus("RETURNED");

        // Calculate overdue fine if returned past dueDate
        if (now.isAfter(record.getDueDate())) {
            long daysOverdue = ChronoUnit.DAYS.between(record.getDueDate(), now);
            BigDecimal fine = DAILY_OVERDUE_FINE_INR.multiply(BigDecimal.valueOf(daysOverdue));
            record.setFineAmount(fine);
            log.info("Overdue fine calculated for record {}: ₹{} for {} days overdue", issueRecordId, fine, daysOverdue);
        } else {
            record.setFineAmount(BigDecimal.ZERO);
        }

        // Restore book available copy
        bookRepository.findById(record.getBookId()).ifPresent(b -> {
            b.setAvailableCopies(Math.min(b.getTotalCopies(), b.getAvailableCopies() + 1));
            bookRepository.save(b);
        });

        BookIssueRecord saved = bookIssueRecordRepository.save(record);
        auditLogService.logAction("LIBRARIAN", "RETURN_BOOK", "operations.library_issue_records", saved.getId().toString(),
                "Returned book '" + record.getBookTitle() + "' from " + record.getStudentName() + ". Overdue Fine: ₹" + record.getFineAmount());

        return saved;
    }

    public List<BookIssueRecord> getStudentIssues(UUID studentId) {
        final String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        return bookIssueRecordRepository.findByTenantIdAndStudentId(tenantId, studentId);
    }
}

