# SCHOOL ERP SYSTEM
## Software Requirements Specification & Technical Database Blueprint

**Document Version:** 1.0  
**Project:** Dedicated Cloud-Based School ERP  
**Initial Deployment:** Single School  
**Target Capacity:** 20,000 Students  
**Deployment Region:** AWS Mumbai, India  
**Application:** Responsive Web Application  
**Architecture:** Modular Monolith  
**Status:** Engineering Baseline

---

# SECTION 1 – PRODUCT OVERVIEW

## 1.1 Product Vision

The School ERP is an integrated school management platform designed to digitize and manage the complete operational, academic, administrative and financial activities of a school.

The system will provide a centralized platform for administrators, principals, teachers, accountants, parents and students.

The system must support a maximum initial planning capacity of 20,000 students and allow future infrastructure scaling without major architectural redesign.

## 1.2 Project Objectives

1. Centralize student and staff information.
2. Automate attendance management.
3. Manage academic years, classes, subjects and examinations.
4. Provide configurable academic board and grading structures.
5. Automate fee collection and online payment processing.
6. Provide parents with access to their children's information.
7. Automate WhatsApp, email and other notifications.
8. Maintain historical academic and financial records.
9. Provide detailed operational and management reports.
10. Ensure secure access to school information.
11. Provide reliable backups and disaster recovery.
12. Maintain complete school ownership of its data.

## 1.3 Project Scope

### Included in initial product

- Authentication and authorization.
- School configuration.
- Student admission and management.
- Guardian management.
- Staff and teacher management.
- Academic year management.
- Class and section management.
- Subject management.
- Attendance.
- Leave management.
- Timetable.
- Examinations.
- Marks and report cards.
- Fee management.
- Online payments.
- Receipts and refunds.
- Parent portal.
- Student portal.
- Teacher portal.
- Administrative dashboard.
- Communication and notifications.
- Reports and audit logs.

### Additional modules

- Transport management.
- Library management.
- Homework and assignments.
- HR and payroll.
- Inventory management.
- Visitor management.
- Advanced analytics.

These additional modules should use the same architectural and authorization conventions.

### Excluded from initial release

- Native Android application.
- Native iOS application.
- Multi-school shared SaaS infrastructure.
- AI-based predictive analytics.
- Biometric hardware integration.
- Automated vehicle GPS tracking.

These may be introduced in later releases.

---

# SECTION 2 – USER ROLES AND PERMISSIONS

The system must implement Role-Based Access Control (RBAC), with additional resource-level authorization.

## 2.1 User roles

| Role | Responsibilities |
|---|---|
| Super Admin | Complete ERP configuration and administration |
| Principal | School-wide academic and administrative oversight |
| School Admin | Student, staff and operational administration |
| Admission Officer | Admission applications and enrollment |
| Teacher | Assigned classes, attendance, marks and homework |
| Accountant | Fees, invoices, payments and refunds |
| HR Manager | Employee records and payroll |
| Librarian | Library operations |
| Transport Manager | Vehicles, routes and student transport |
| Parent | Linked children's information |
| Student | Own academic and administrative information |

## 2.2 Permission structure

Permissions should be action-based rather than relying only on role names.

Examples:

- `student.create`
- `student.read`
- `student.update`
- `student.archive`
- `attendance.mark`
- `attendance.correct`
- `attendance.approve`
- `exam.create`
- `marks.enter`
- `marks.approve`
- `result.publish`
- `fee.invoice.create`
- `payment.verify`
- `refund.approve`
- `report.export`
- `user.manage`
- `settings.manage`

## 2.3 Authorization rules

1. Teachers can access only their assigned academic resources.
2. Parents can access only children linked to their verified guardian account.
3. Students can access only their own records.
4. Accountants can access authorized financial modules.
5. Only authorized personnel can publish examination results.
6. Financial refunds require separate approval permissions.
7. Administrative actions must be recorded in audit logs.
8. Privileged accounts must support MFA.
9. Permissions must be verified by the backend on every protected operation.

---

# SECTION 3 – FUNCTIONAL REQUIREMENTS

## 3.1 Authentication and identity management

**FR-AUTH-001:** Users must be able to log in using their registered credentials.

**FR-AUTH-002:** The system must support password reset.

**FR-AUTH-003:** The system must support secure session management.

**FR-AUTH-004:** Refresh tokens must be rotated and revocable.

**FR-AUTH-005:** Privileged users must support multifactor authentication.

**FR-AUTH-006:** Administrators must be able to deactivate user accounts.

**FR-AUTH-007:** Login attempts must be rate-limited and monitored.

**FR-AUTH-008:** User access must be controlled through roles and permissions.

## 3.2 Student management

**FR-STU-001:** Administrators must be able to register students.

**FR-STU-002:** Every student must receive a unique internal UUID.

**FR-STU-003:** Each student must have a school admission number.

**FR-STU-004:** The system must maintain student personal information.

**FR-STU-005:** The system must support profile photographs and documents.

**FR-STU-006:** Students must be associated with guardians.

**FR-STU-007:** The system must support multiple guardians per student.

**FR-STU-008:** Administrators must be able to transfer and promote students.

**FR-STU-009:** Historical academic enrollments must be retained.

**FR-STU-010:** Student records must support active, inactive, graduated, transferred and archived states.

## 3.3 Admission management

- Online or administrative admission registration.
- Admission application number generation.
- Document uploads.
- Application review.
- Approval and rejection.
- Admission fee recording.
- Enrollment conversion.
- Admission history.

Admission applications and active student records must be separate entities.

## 3.4 Academic management

- Academic year creation.
- Academic term configuration.
- Class and grade management.
- Section management.
- Subject management.
- Teacher assignment.
- Curriculum configuration.
- Academic calendar.
- School holidays.
- Student promotion.
- Student transfer.

The academic configuration must not depend on a hardcoded CBSE, ICSE or State Board structure.

## 3.5 Attendance management

- Daily student attendance.
- Class-wise attendance.
- Section-wise attendance.
- Present, absent, late and excused statuses.
- Leave requests.
- Leave approval.
- Attendance corrections.
- Attendance reports.
- Parent notifications.

Every attendance correction must preserve the original record, corrected value, user and timestamp.

## 3.6 Examination management

- Examination creation.
- Examination scheduling.
- Subject-wise assessments.
- Theory and practical components.
- Maximum marks configuration.
- Marks entry.
- Marks verification.
- Grading configuration.
- Result approval.
- Result publication.
- Report-card generation.
- Student performance history.

The system must support different grading and assessment structures for different academic levels.

## 3.7 Fee management

- Fee category creation.
- Fee structure configuration.
- Class-wise fee structures.
- Student-specific fee concessions.
- Installment configuration.
- Invoice generation.
- Due-date management.
- Late fees.
- Partial payments.
- Multiple payment allocations.
- Refunds.
- Payment receipts.
- Outstanding fee reports.
- Payment reconciliation.

## 3.8 Online payment management

The ERP must integrate with a payment gateway through a provider-independent adapter.

Payment workflow:

1. Parent selects outstanding invoice.
2. Backend validates payable amount.
3. Backend creates a payment order.
4. Gateway processes payment.
5. Gateway sends signed webhook.
6. Backend verifies the webhook.
7. Payment is recorded in a database transaction.
8. Invoice balance is updated.
9. Receipt is generated.
10. Parent is notified.

Browser redirects must never be treated as proof of payment.

## 3.9 Communication management

Supported channels:

- WhatsApp Business Platform.
- Email.
- SMS.
- Web notifications.

Supported notification events:

- Student absent.
- Fee due.
- Fee payment successful.
- Examination results published.
- Homework assigned.
- School announcements.
- Holidays.
- Emergency notifications.

Notification delivery must be asynchronous and retryable.

## 3.10 Reporting

The system must support:

- Student strength reports.
- Class-wise student reports.
- Attendance reports.
- Teacher attendance reports.
- Examination reports.
- Student performance reports.
- Fee collection reports.
- Outstanding payment reports.
- Refund reports.
- Admission reports.
- Staff reports.
- Audit reports.

Reports must support filtering, pagination and authorized export.

---

# SECTION 4 – NON-FUNCTIONAL REQUIREMENTS

## 4.1 Performance

Initial engineering targets:

| Metric | Target |
|---|---|
| Student capacity | 20,000 |
| Concurrent users | 1,000–2,000 planning target |
| Normal API response | p95 below 500 ms |
| Complex reports | Asynchronous where appropriate |
| Attendance batch | 20,000 records |
| Notification processing | Horizontally scalable |
| Payment integrity | No duplicate successful transaction |
| Availability objective | 99.9% monthly target |

These targets must be validated through representative load testing. External payment gateway and messaging provider delays are measured separately from ERP response times.

## 4.2 Scalability

- Application services must be horizontally scalable.
- Background workers must support independent scaling.
- Database connections must be pooled.
- Frequently accessed reference data may be cached.
- Large reports must not block normal API requests.
- Database partitioning should be applied selectively.

## 4.3 Reliability

- Automated backups.
- Database point-in-time recovery.
- Multi-AZ database configuration.
- Idempotent financial processing.
- Retryable background jobs.
- Transactional outbox.
- Monitoring and alerting.
- Tested disaster recovery.

Initial proposed recovery objectives:

- RPO: 15 minutes.
- RTO: 4 hours.

These are service targets requiring operational validation.

## 4.4 Security

- TLS for all external connections.
- Encryption at rest.
- AWS KMS-managed keys.
- Password hashing using Argon2id.
- Secure cookies.
- Refresh token rotation.
- API rate limiting.
- MFA for privileged users.
- Private database subnets.
- Private object storage.
- Audit logging.
- Secrets management.
- Regular vulnerability assessments.

## 4.5 Privacy

The system must support:

- Data minimization.
- Guardian relationship verification.
- Parental consent workflows where legally required.
- Retention policies.
- Data export.
- Authorized correction requests.
- Data deletion workflows where applicable.
- Breach response.
- Access history.

The production privacy design must undergo legal review against applicable Indian data protection requirements.

---

# SECTION 5 – TECHNICAL ARCHITECTURE

## 5.1 Frontend

- Next.js.
- TypeScript.
- Tailwind CSS.
- shadcn/ui.
- Responsive web interface.
- PWA support where appropriate.

Frontend domains:

- Admin dashboard.
- Principal dashboard.
- Teacher dashboard.
- Accountant dashboard.
- Parent portal.
- Student portal.

## 5.2 Backend

- NestJS.
- TypeScript.
- REST API.
- Prisma ORM.
- PostgreSQL.
- Redis.
- BullMQ.

Architecture: Modular monolith.

Each business module owns its business rules and persistence access through defined service boundaries.

## 5.3 Background processing

Separate worker processes should handle:

- Notification delivery.
- Invoice generation.
- Report-card PDF generation.
- Bulk imports.
- Bulk exports.
- Payment reconciliation.
- Scheduled reminders.
- Data archival.

Critical database events must use the transactional outbox pattern.

## 5.4 Cloud infrastructure

AWS Mumbai region.

| Service | Purpose |
|---|---|
| Route 53 | DNS |
| CloudFront | Content delivery |
| WAF | Web protection |
| ALB | Traffic distribution |
| ECS Fargate | Container execution |
| RDS PostgreSQL | Primary database |
| ElastiCache Redis | Cache and queues |
| S3 | Private file storage |
| KMS | Encryption |
| Secrets Manager | Secret management |
| CloudWatch | Monitoring |
| AWS Backup | Recovery |

Infrastructure should be managed using Terraform.

---

# SECTION 6 – DATABASE DESIGN STANDARDS

## 6.1 Database rules

1. PostgreSQL is the authoritative data source.
2. Primary keys should use UUIDs.
3. Financial amounts must use `NUMERIC`, not floating-point values.
4. Timestamps should use `TIMESTAMPTZ`.
5. Academic dates must be represented consistently using the school's configured timezone.
6. Important tables must include `created_at` and `updated_at`.
7. Sensitive operations must record the actor.
8. Foreign keys must be enforced.
9. Unique constraints must be used for business identifiers.
10. Historical records must not be overwritten when academic history is important.
11. Financial transactions must not be physically deleted.
12. JSONB should be reserved for genuinely flexible configuration, not ordinary relational data.

## 6.2 PostgreSQL schemas

Organize the database into logical schemas:

| Schema | Purpose |
|---|---|
| `identity` | Users, roles and permissions |
| `institution` | School configuration |
| `admissions` | Admission applications |
| `students` | Students and guardians |
| `academics` | Curriculum and enrollment |
| `staff` | Employees and teachers |
| `attendance` | Attendance and leave |
| `examinations` | Exams and results |
| `finance` | Fees and payments |
| `communication` | Notifications |
| `operations` | Transport and library |
| `system` | Audit, events and integration records |

This is one database with logical schema separation, not separate databases.

---

# SECTION 7 – DATABASE ENTITY INVENTORY

## 7.1 Identity

- users
- roles
- permissions
- user_roles
- role_permissions
- user_sessions
- password_reset_tokens
- login_attempts
- mfa_methods

## 7.2 Institution

- schools
- campuses
- school_settings
- academic_years
- academic_terms
- school_calendars
- school_holidays

## 7.3 Admissions

- admission_applications
- admission_documents
- admission_reviews
- admission_status_history

## 7.4 Students

- students
- student_profiles
- student_documents
- guardians
- student_guardians
- student_emergency_contacts
- student_enrollments
- student_status_history

## 7.5 Academics

- academic_frameworks
- curriculum_versions
- grade_levels
- classes
- sections
- subjects
- curriculum_subjects
- academic_enrollments
- teacher_assignments
- class_subject_assignments
- grading_schemes
- grade_bands
- promotion_rules

## 7.6 Staff

- employees
- departments
- designations
- employee_documents
- staff_attendance
- staff_leave_requests
- employment_history

## 7.7 Attendance

- attendance_sessions
- attendance_records
- leave_types
- leave_requests
- attendance_corrections

## 7.8 Examinations

- exams
- exam_subjects
- assessment_components
- marks
- grade_results
- report_cards
- result_publications

## 7.9 Finance

- fee_categories
- fee_structures
- fee_structure_items
- student_fee_assignments
- fee_invoices
- fee_invoice_items
- payment_orders
- payments
- payment_allocations
- refunds
- receipts
- financial_ledger_entries
- gateway_webhook_events
- gateway_settlements

## 7.10 Communication

- announcements
- notification_templates
- notifications
- notification_deliveries
- parent_preferences
- communication_consents

## 7.11 System

- audit_logs
- outbox_events
- background_job_records
- file_metadata
- integration_accounts
- system_settings

---

# SECTION 8 – CORE DATABASE RELATIONSHIPS

The following relationships are mandatory.

1. A user may have multiple roles.
2. A role may have multiple permissions.
3. A student may have multiple guardians.
4. A guardian may have multiple students.
5. A student may have multiple academic enrollments over time.
6. An academic year contains multiple terms.
7. A class may contain multiple sections.
8. A subject may be associated with multiple grade levels.
9. A teacher may be assigned to multiple subjects and sections.
10. An attendance session contains multiple attendance records.
11. An examination contains multiple subject assessments.
12. An assessment contains multiple student marks.
13. A student may receive multiple fee invoices.
14. An invoice contains multiple fee items.
15. A payment may be allocated across multiple invoices.
16. An invoice may receive multiple partial payments.
17. A payment may have multiple refund or reversal records.
18. A guardian may receive notifications for multiple linked children.
19. Every sensitive administrative operation must have an audit record.

---

# SECTION 9 – PAYMENT DATA INTEGRITY

Financial records require stricter rules than ordinary operational data.

## 9.1 Payment states

Payment order states:

- CREATED
- PENDING
- PROCESSING
- SUCCEEDED
- FAILED
- EXPIRED
- CANCELLED

Refund states:

- REQUESTED
- APPROVED
- PROCESSING
- SUCCEEDED
- FAILED
- REJECTED

## 9.2 Transaction requirements

A successful payment must be processed atomically:

1. Verify gateway event.
2. Check event idempotency.
3. Verify order and amount.
4. Lock or safely update relevant financial records.
5. Record payment.
6. Allocate payment.
7. Update invoice balance.
8. Record ledger entries.
9. Mark event processed.
10. Commit transaction.

The payment provider's webhook must be the authoritative confirmation channel, supplemented by provider-side status verification where necessary.

Never assume that one payment equals one invoice.

## 9.3 Financial ledger

Use immutable financial ledger entries.

Corrections must be performed through compensating entries, not by silently editing previously posted financial records.

---

# SECTION 10 – API DESIGN STANDARDS

Base API path:

`/api/v1`

## 10.1 API modules

| API prefix | Responsibility |
|---|---|
| `/auth` | Authentication |
| `/users` | User administration |
| `/students` | Student records |
| `/guardians` | Guardian management |
| `/admissions` | Admissions |
| `/academics` | Academic configuration |
| `/enrollments` | Academic enrollment |
| `/staff` | Staff |
| `/attendance` | Attendance |
| `/examinations` | Examinations |
| `/results` | Results |
| `/fees` | Fee structures and invoices |
| `/payments` | Online payments |
| `/refunds` | Refund processing |
| `/notifications` | Notifications |
| `/reports` | Reporting |
| `/settings` | School settings |
| `/audit` | Authorized audit queries |

## 10.2 Example endpoints

### Student management

- `POST /api/v1/students`
- `GET /api/v1/students`
- `GET /api/v1/students/:id`
- `PATCH /api/v1/students/:id`
- `POST /api/v1/students/:id/enrollments`

### Attendance

- `POST /api/v1/attendance/sessions`
- `POST /api/v1/attendance/sessions/:id/records/bulk`
- `GET /api/v1/attendance/reports`
- `POST /api/v1/attendance/corrections`

### Examinations

- `POST /api/v1/examinations`
- `POST /api/v1/examinations/:id/subjects`
- `POST /api/v1/examinations/:id/marks/bulk`
- `POST /api/v1/results/:id/approve`
- `POST /api/v1/results/:id/publish`

### Finance

- `POST /api/v1/fees/structures`
- `POST /api/v1/fees/invoices/generate`
- `GET /api/v1/fees/invoices`
- `POST /api/v1/payments/orders`
- `POST /api/v1/payments/webhooks/:provider`
- `POST /api/v1/refunds`
- `GET /api/v1/payments/history`

## 10.3 API standards

- JSON request and response bodies.
- Consistent HTTP status codes.
- DTO validation.
- Pagination.
- Filtering and sorting.
- API versioning.
- Request correlation IDs.
- Idempotency keys for sensitive financial operations.
- Structured error responses.
- OpenAPI/Swagger documentation.

---

# SECTION 11 – DATA IMPORT AND MIGRATION

The system must support initial school data migration.

Required import types:

- Students.
- Guardians.
- Staff.
- Classes.
- Sections.
- Subjects.
- Academic enrollments.
- Fee balances.
- Historical records where available.

Import process:

1. Upload spreadsheet.
2. Validate columns and data types.
3. Detect duplicates.
4. Show validation errors.
5. Generate import preview.
6. Obtain administrator confirmation.
7. Execute import in batches.
8. Record import results.
9. Provide error report.

Never import unvalidated spreadsheet data directly into production tables.

---

# SECTION 12 – TESTING AND ACCEPTANCE

## 12.1 Testing categories

- Unit tests.
- Integration tests.
- API tests.
- Database constraint tests.
- End-to-end tests.
- Security tests.
- Load tests.
- Payment sandbox tests.
- Backup restoration tests.
- Disaster recovery tests.

## 12.2 Production acceptance criteria

The first production release must meet these conditions:

1. Authorized and unauthorized access tests pass.
2. Student and guardian relationships are validated.
3. Academic enrollment history is retained.
4. Attendance batch processing passes load tests.
5. Examination results are correctly calculated.
6. Payment webhooks cannot create duplicate transactions.
7. Refund and reconciliation workflows are tested.
8. Notification failures can be retried.
9. Audit records are generated for sensitive operations.
10. Database backup restoration is demonstrated.
11. Monitoring and alerting are operational.
12. Privacy and security reviews are completed.
13. School administrators approve user acceptance testing.

---

# SECTION 13 – IMPLEMENTATION PHASES

| Phase | Deliverable |
|---|---|
| 1 | Infrastructure and authentication |
| 2 | School configuration and RBAC |
| 3 | Admissions, students and guardians |
| 4 | Academic management |
| 5 | Staff and attendance |
| 6 | Examinations and report cards |
| 7 | Fees and online payments |
| 8 | Parent and student portals |
| 9 | Communication integrations |
| 10 | Additional school operations |
| 11 | Security, load testing and launch |

---

# SECTION 14 – FINAL ENGINEERING PRINCIPLES

1. One school receives its own dedicated deployment.
2. PostgreSQL is the source of truth.
3. No critical business logic belongs exclusively in the frontend.
4. Financial records must be auditable and immutable after posting.
5. Academic configurations must be versioned.
6. Student history must survive promotion and transfers.
7. Parent access must be relationship-based.
8. Background jobs must be retryable and idempotent.
9. All sensitive operations must be audited.
10. Infrastructure must be reproducible through Terraform.
11. Database migrations must be version-controlled.
12. Performance must be measured rather than assumed.
13. Security and privacy must be designed from the beginning.
14. The application should support future growth without requiring a complete rewrite.

**END OF SRS VERSION 1.0**