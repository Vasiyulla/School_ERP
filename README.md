# School ERP System - Tier-1 Enterprise B2B SaaS Platform

[![Java 21 LTS](https://img.shields.io/badge/Java-21_LTS-orange.svg?style=flat&logo=openjdk)](https://openjdk.org/)
[![Spring Boot 3.3.5](https://img.shields.io/badge/Spring_Boot-3.3.5-brightgreen.svg?style=flat&logo=springboot)](https://spring.io/projects/spring-boot)
[![Next.js 15](https://img.shields.io/badge/Next.js-15_App_Router-black.svg?style=flat&logo=next.js)](https://nextjs.org/)
[![Kotlin 2.0](https://img.shields.io/badge/Kotlin-Jetpack_Compose-purple.svg?style=flat&logo=kotlin)](https://kotlinlang.org/)
[![PostgreSQL 16](https://img.shields.io/badge/PostgreSQL-12_Schemas-blue.svg?style=flat&logo=postgresql)](https://www.postgresql.org/)
[![Tests Passing](https://img.shields.io/badge/JUnit_5-115%2F115_Tests_Passed-success.svg)](https://junit.org/)
[![Endpoints Verified](https://img.shields.io/badge/REST_Endpoints-252%2F252_Live_Verified-blueviolet.svg)](#5-complete-252-live-verified-rest-endpoints-directory)

A cloud-native, multi-portal **B2B SaaS School Enterprise Resource Planning (ERP)** platform engineered to lead the global educational software industry. Built to Tier-1 commercial standards benchmarked directly against **PowerSchool, Toddle, Odoo Enterprise, SAP Fiori, Verkada, Samsara, and Stripe Billing**.

---

## Table of Contents
1. [Executive Architectural Vision](#1-executive-architectural-vision)
2. [Dedicated Pod & Dual-Database HA Architecture](#2-dedicated-pod--dual-database-ha-architecture)
3. [Global Commercial SaaS Benchmark Matrix](#3-global-commercial-saas-benchmark-matrix)
4. [PostgreSQL 12-Schema Domain Architecture & Complete Backend Feature Directory](#4-postgresql-12-schema-domain-architecture--complete-backend-feature-directory)
5. [Complete 252 Live Verified REST Endpoints Directory](#5-complete-252-live-verified-rest-endpoints-directory)
6. [Omni-Channel Portals & Client Applications](#6-omni-channel-portals--client-applications)
7. [Enterprise Invariants & Regulatory Compliance](#7-enterprise-invariants--regulatory-compliance)
8. [Getting Started & Local Development](#8-getting-started--local-development)
9. [Automated Testing & Live Verification](#9-automated-testing--live-verification)
10. [Cloud Infrastructure & Production DevOps](#10-cloud-infrastructure--production-devops)

---

## 1. Executive Architectural Vision

Legacy school ERP software suffers from **SaaS Fragmentation Disease**—schools typically purchase separate subscriptions for Student Information (SIS), fee payment gateways, biometric turnstiles, bus GPS trackers, and SMS/WhatsApp providers.

This platform replaces all point solutions with a **Unified Single Source of Truth**:
- **Zero Cross-Contamination:** Complete physical data sovereignty with dedicated dual-database HA pods per school.
- **Dynamic App Licensing (The Odoo Standard):** Modular features can be licensed or toggled on demand (`CORE_ACADEMICS`, `FINANCE`, `FLEET_GPS`, `ATTENDANCE`, `EXAMINATIONS`, `COMMUNICATION`, `LIBRARY`, `ADMISSIONS`).
- **Dynamic Extensibility & Custom Entities Engine (The Salesforce & ServiceNow Standard):** Metadata-driven dynamic custom metafields on core entities, plus complete universal custom entity authoring with JSON Schema validation and outbound webhooks—zero SQL alterations and zero downtime.
- **Tenant-Configurable Staff Roles & Hierarchy:** Dynamic designation authoring, reporting trees, and multi-tier financial approval boundaries tailored for each institution.
- **Digital Classroom Content Repository & Continuity Desk:** Centralized publisher and study locker for e-books, lecture slides, whiteboard photos, lab videos, teacher notes, exit tickets, and emergency virtual classroom failover.
- **Immutable Financial Engine:** Strict double-entry accounting where transactions are append-only.
- **Cryptographic Fraud Defense:** SHA-256 tamper-proof QR credentials for report cards, certificates, and transfer certificates.
- **High Concurrency Engine:** Java 21 Virtual Threads (Project Loom) supporting 20,000+ students per tenant with sub-500ms p95 response times.

---

## 2. Dedicated Pod & Dual-Database HA Architecture

To ensure strict compliance with institutional privacy policies, CBSE/state board mandates, and India's **Digital Personal Data Protection (DPDP) Act 2023 / GDPR**, the system implements a **Dedicated Pod & Dual-Database High Availability (HA) Model**:

```mermaid
graph TD
    subgraph School A: Apex Academy Pod
        A1[apex.schoolerp.cloud / erp.apexacademy.edu.in] --> B1[Dedicated Backend Container]
        B1 -->|Multi-Host JDBC Driver| C1[(Primary DB :5432)]
        C1 -.->|Streaming Replication 0ms Lag| C2[(Hot Standby DB :5433)]
        B1 --> D1[Isolated S3: /schools/apex/]
    end

    subgraph School B: Delhi Public School Pod
        A2[dps.schoolerp.cloud / erp.dpsmumbai.edu.in] --> B2[Dedicated Backend Container]
        B2 -->|Multi-Host JDBC Driver| E1[(Primary DB :5432)]
        E1 -.->|Streaming Replication 0ms Lag| E2[(Hot Standby DB :5433)]
        B2 --> D2[Isolated S3: /schools/dps/]
    end

    subgraph School N: Heritage International Pod
        AN[heritage.schoolerp.cloud] --> BN[Dedicated Backend Container]
        BN -->|Multi-Host JDBC Driver| F1[(Primary DB :5432)]
        F1 -.->|Streaming Replication 0ms Lag| F2[(Hot Standby DB :5433)]
        BN --> DN[Isolated S3: /schools/heritage/]
    end
```

- **Physical Isolation:** Student records, grades, biometric logs, and financial vouchers for School A are physically stored in a dedicated database pair (`db-primary:5432` and `db-secondary:5433`). They are never merged into a shared table.
- **Multi-Host Auto-Failover:** Configured with `targetServerType=primary` multi-host JDBC connections (`jdbc:postgresql://db-primary:5432,db-secondary:5433/school_db`). In the event of primary hardware failure, the system automatically fails over to the standby node in <1.5s with zero data loss.
- **Independent Scaling & Maintenance:** High-volume operations at one school (e.g., 20,000 attendance marks or bulk report card compilation) have zero performance impact on other institutions.
- **Custom Subdomains & Custom Domains:** Transparent routing through institutional custom domains (`erp.apexacademy.edu.in`) or platform subdomains (`apex.schoolerp.cloud`).

---

## 3. Global Commercial SaaS Benchmark Matrix

| Domain | Commercial Benchmark Platform | Tier-1 Enterprise Capability Implemented |
|---|---|---|
| **Parent Mobile App** | **Toddle Parent & ClassDojo** | Native Kotlin 2.x + Jetpack Compose app (`mobile-parent/`), multi-child sibling switcher, 1-click Razorpay checkout, live bus GPS tracking, offline Room caching, biometric auth, and FCM push notifications. |
| **Academics & Classroom** | **PowerSchool & Toddle** | Multi-component CBSE 70/20/10 weighted gradebook, standards-based mastery tracking, tamper-proof SHA-256 verification QR credentials, auto-substitution timetable board, and **Digital Classroom Content Repository & Smart Study Locker** (e-books, slides, videos, board notes). |
| **Timetable & Scheduling** | **aSc Timetables & Untis** | Constraint-satisfaction master timetable engine, collision-free faculty assignment, room capacity checks, and teacher workload schedules. |
| **Finance & Billing** | **Odoo Enterprise & Stripe Billing** | Grade-level fee structures, dynamic multi-child sibling concessions, direct bank statement auto-reconciliation (HDFC/ICICI), physical cheque clearance lifecycles with bounce penalties, daily late fee surcharges, caution money refund clearances, vendor TDS 194C/194J booking, and immutable double-entry ledger journals. |
| **Staff & HR Payroll** | **SAP Fiori & Workday** | Biometric turnstile IoT push ingestion (eSSL/ZKTeco), automated 7th Central Pay Commission (7th CPC) salary slip engine with bi-annual DA % revisions, Section 115BAC Form 16 Part B tax generation, staff emergency loan ledgers with auto-EMIs, and CBSE mandatory 50-hour teacher CPD compliance tracker. |
| **Org Structure & Roles** | **Workday & BambooHR** | Dynamic tenant-configurable staff designations, visual organizational hierarchy tree, multi-level reporting tiers, and monetary approval limits. |
| **Transport & Campus Safety** | **Samsara & Verkada** | High-frequency IoT GPS vehicle telematics, Haversine geofenced parent pickup alerts, overspeeding monitoring, digital QR gate pass turnstile verification, science lab breakage fine auto-debits, and campus lost-and-found property vault. |
| **Executive & Board Governance** | **BoardEffect & PowerSchool** | Executive strategic command cockpit KPIs, confidential child safety vault (POCSO/POSH/Bullying), faculty 360° PBAS appraisal scoring, parent grievance ombudsman desk, and CBSE accreditation inspection readiness dossiers. |
| **Multi-Tenant SaaS Core** | **Odoo Enterprise & Shopify Plus** | Runtime modular app licensing (`system.tenant_module_licenses`), subdomain tenant routing, action-based RBAC, Dual-DB HA Sentinel, and immutable security audit stream. |

---

## 4. PostgreSQL 12-Schema Domain Architecture & Complete Backend Feature Directory

The database is partitioned into **12 logical PostgreSQL schemas** within each dedicated institutional school pod, encapsulating domain boundaries with zero cross-table leaking:

```mermaid
graph TD
    identity --> students
    identity --> staff
    institution --> academics
    academics --> students
    academics --> examinations
    academics --> attendance
    admissions --> students
    students --> attendance
    students --> examinations
    students --> finance
    finance --> system
    attendance --> communication
    examinations --> communication
    staff --> academics
    operations --> students
```

---

### 4.1 `identity` Schema (Identity, Authentication & Zero-Trust Security)
* **Domain Responsibility:** User credential storage, NIST SP 800-63B GPU-hard password hashing, role-based access control (RBAC), multi-device revocable sessions, and refresh token rotation.
* **Core Entities:**
  * `User`: Platform credentials, primary role, account active/suspended state, and Argon2id password hash.
  * `Role`: Pre-configured institutional roles (`SUPER_ADMIN`, `PRINCIPAL`, `ADMIN`, `ADMISSION_OFFICER`, `TEACHER`, `ACCOUNTANT`, `STAFF`, `PARENT`, `STUDENT`).
  * `Permission`: Action-based atomic permission keys (`student.create`, `fee.invoice.collect`, `marks.publish`, `attendance.override`).
  * `UserRole` & `RolePermission`: Many-to-many security mappings enforcing method-level `@PreAuthorize` authorization.
  * `UserSession`: Stateful multi-device login tracking, IP address, user agent, expiration, and revocable status.
  * `RefreshToken`: Single-use rolling refresh tokens defending against replay and token-theft attacks.
* **Backend Features & Business Capabilities:**
  * **Argon2id Cryptographic Security:** GPU/ASIC-resistant memory-hard password encoding (`Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()`).
  * **Stateless JWT with Virtual Threads:** High-throughput HMAC-SHA512 token verification executed over Java 21 Project Loom virtual threads.
  * **Emergency Revocation Protocol:** Instant 1-click invalidation of all active user sessions across the entire school tenant (`POST /api/v1/identity/sessions/revoke-all`).

---

### 4.2 `institution` Schema (Hierarchy, Multi-Tenancy & Strategic Governance)
* **Domain Responsibility:** School pod configuration, branch hierarchies, affiliation standards (CBSE/ICSE/IB), academic sessions, executive leadership cockpits, child safety vaults, and accreditation readiness.
* **Core Entities:**
  * `School`: Tenant master entity, subdomain routing (`apex.schoolerp.cloud`), affiliation number, and contact details.
  * `SchoolBranch`: Multi-campus physical facilities (Primary Wing, Senior Wing, Sports Complex).
  * `AcademicSession`: Active academic years (`2026-2027`) and term partition dates (Term 1, Term 2).
  * `AcademicCalendarEvent`: Institutional calendar events, statutory breaks, and emergency weather closures.
  * `ConfidentialSafetyIncident`: Confidential child protection records (POCSO Act 2012, POSH, Bullying) restricted strictly to `PRINCIPAL`.
  * `AccreditationInspectionDossier`: Statutory compliance records (Fire NOC, Seismic Stability, BIS 10500 drinking water, Pupil-Teacher Ratio).
* **Backend Features & Business Capabilities:**
  * **Executive Strategic Command Cockpit:** Real-time KPI aggregation (`TIER_1_EXEMPLARY`), sanctioned capacity occupancy %, fee realization %, and faculty workload indices.
  * **Institutional Calendar Engine:** Automated working-day calculator evaluating holidays, weekly offs, and rain-day emergency closures.
  * **CBSE Inspection Dossier Compiler:** 1-Click print-ready compliance dossiers compiled for central education department audits.

---

### 4.3 `admissions` Schema (Admissions CRM, Walk-In Desk, Indian Quotas & Testing)
* **Domain Responsibility:** Full student acquisition lifecycle from front-desk walk-in or online inquiry to document verification, 100-point DoE criteria scoring, entrance diagnostic testing, scholarship review, and parent digital acceptance.
* **Core Entities:**
  * `AdmissionInquiry`: Central lead dossier, inquiry number (`INQ-2026-XXXX`), walk-in token (`TOKEN-W14`), 7-stage pipeline state, lead score (0-100), social category, and assigned counselor.
  * `AdmissionPointCriteria`: Directorate of Education (DoE) 100-Point criteria points breakdown (Distance 40 pts, Sibling 30 pts, Alumni 10 pts, Staff 10 pts, Single Girl Child 5 pts, First Born 5 pts).
  * `StatutoryCertificateRecord`: Tehsildar/SDM government caste certificates (SC, ST, OBC-NCL), EWS income proofs, and CWSN medical board disability certificates.
  * `GradeSeatCapacity`: Multi-quota seat matrix (General 60%, RTE Act 2009 25%, OBC-NCL, SC, ST, CWSN Divyang, Sibling, Staff, Single Girl Child, Management).
  * `AdmissionEntranceTest`: Diagnostic entrance screening, exam hall allocation, reporting time, subject-wise marksheet (Math, English, Science, Aptitude), and admit card token (`ADMIT-2026-XXXX`).
  * `AdmissionScholarshipApplication`: Institutional financial aid applications (`MERIT_ACADEMIC`, `SPORTS_NATIONAL`, `DEFENSE_PERSONNEL`, `NEED_BASED_EWS`) with 2-tier committee review.
  * `AdmissionInterviewSlot`: Campus tour and principal interview scheduling with 1-5 star rubric evaluations.
  * `AdmissionMeritOffer`: Formal conditional admission offer letter, fee schedule, validity deadline, seat lock deposit amount, and parent e-signature.
  * `AdmissionDocumentChecklist`: Itemized KYC document verification checklist with physical locker custody flags.
  * `CounselorTask`: Admissions CRM follow-up cadence, SLA countdowns, priority rankings, and call outcome logging.
  * `RteApplicationRecord`: Mandatory 25% EWS/Disadvantaged quota lottery registration and state reimbursement claim tracking.
  * `AdmissionQueueToken`: Multi-counter walk-in token (`TKN-ENQ-XXX`, `TKN-DOC-XXX`, `TKN-FEE-XXX`), category, visitor credentials, assigned counter/officer, waiting and service duration telemetry.
  * `ProspectusKitMaster`: Serialized printed prospectus kit inventory (`KIT-2026-XXXX`), barcode POS sales register (Cash/UPI QR), inquiry locking to prevent duplicate submission, and cashier revenue reconciliation.
  * `AdmissionDocumentUndertaking`: Formal legal undertaking bond (`UNDERTAKING-2026-XXXX`) for missing Transfer Certificates/documents with 30-day statutory deadlines and automated overdue sentinels.
  * `AdmissionWelcomeDossier`: Instant permanent student identity minting (`ADM-2026-XXXX`), section/roll/house allocation, smart RFID campus card serialization with canteen pre-load, and 5-part printable Welcome Pack dossier.
  * `AdmissionSeatSurrender`: Statutory fee refund engine with percentage-based deduction schedules, instant quota seat recycling to waitlist candidates, and General Ledger refund vouchers.
* **Backend Features & Business Capabilities:**
  * **Front-Desk Smart Queue & Multi-Counter Calling Desk:** Kiosk-dispensed queue tokens, counter display console (serve, announce, complete, no-show), and wait-time telemetry analytics.
  * **Serialized Prospectus Kit POS Inventory:** Physical barcode-scanned kit sales with inquiry locking to eradicate duplicate submissions, cash/UPI QR reconciliation, and daily cashier denomination auditing.
  * **Document Stamping & Deficient Document Undertaking Desk:** Official original KYC custody stamping, formal legal undertakings for missing Transfer Certificates with 30-day statutory countdowns, and automated overdue sentinels.
  * **Instant Student Identity Minting & Welcome Dossier:** Auto-minted permanent admission numbers, automatic roll/section/house allocation, smart RFID campus card serialization with pre-loaded canteen balance, and 5-part printable Welcome Pack.
  * **Seat Surrender, Quota Recycling & Statutory Fee Refund Engine:** Statutory tiered refund schedules based on academic session commencement dates, instant quota seat recycling to category waitlists, and General Ledger compensating vouchers.
  * **Express Walk-In Front Desk Intake:** 60-second parent intake dispensing queue tokens and collecting spot prospectus fees (Cash/UPI QR) with auto-receipts.
  * **Indian 100-Point Criteria Engine:** Statutory objective scoring with automated tie-breaker draw numbers for computerized lotteries.
  * **Category-Segregated Quota Waitlists:** Independent waitlist pools per category with 1-click candidate promotion preserving reservation ratios.
  * **Entrance Screening & Hall Ticket Desk:** Diagnostic multi-subject testing with category cut-off relaxations (60% General vs 50% reserved categories) and printable parent admit cards.
  * **Verified Sibling Concession Forecast:** Real-time student lookup awarding +20 priority points and forecasting 15% second-child tuition discounts.
  * **Parent Public Self-Service Portal:** Phone-authenticated application tracker showing 7-stage progress %, checklist status, and counselor contacts without an ERP account.
  * **Parent Digital Offer Acceptance & Online Seat Lock:** Cryptographic e-signature agreement and online token deposit collection (UPI/NetBanking) locking seats before expiration.

---

### 4.4 `students` Schema (Student 360°, Health Infirmary, Behavior & Compliance)
* **Domain Responsibility:** Core student lifecycle, physical KYC locker custody, multi-department departure clearance, health clinic triage, house behavior leaderboards, and national registries.
* **Core Entities:**
  * `Student`: Central student record, permanent admission number (`ADM-2024-XXXX`), class/section, roll number, status (`Active`, `Transferred`, `Graduated`), and guardian contacts.
  * `StudentCustodyDocument`: Original certificate repository tracking physical custody (Original in Locker, Verified Copy, Returned) with cabinet tag numbers.
  * `StudentClearanceRequest`: Multi-department exit clearance verifying zero outstanding library books, unpaid tuition dues, and lab equipment breakages.
  * `TransferCertificate`: Formal Transfer Certificate (TC) issuance with running serial numbers and public SHA-256 QR verification.
  * `InfirmaryVisit`: Campus clinic medical triage log, vitals (temperature, SpO2, BP), first-aid, disposition, and automated WhatsApp parent alerts on high fever (>100°F).
  * `SchoolHouse`: School House system master (Neptune, Apollo, Phoenix, Vanguard) with house master faculty assignments.
  * `BehaviorIncident`: Positive merits and disciplinary demerits with automatic house trophy point recalculation.
  * `StudentCertificate`: Institutional certificates desk (Bonafide, Character, Children Education Allowance / Fee Estimation) with public QR verification.
  * `StudentUdiseProfile`: Ministry of Education UDISE+ and APAAR / PEN permanent education number digital profiles.
  * `AlumniProfile` & `HigherEdPlacement`: Class 12 alumni tracking national competitive exam ranks (JEE, NEET, CUET, SAT) and university offers.
  * `StudentPortfolioItem`: Experiential learning portfolio showcasing creative projects with teacher rubric reviews and parent clapping.
  * `StudentBadge`: Micro-credential badges (STEM Pioneer, Peer Helper, Sports Champion) crediting live house points.
  * `OfficialTranscript`: Multi-year cumulative academic transcripts with GPA, credits, medium of instruction, and cryptographic SHA-256 anti-fraud verification.
  * `StudentHealthPassport`: Critical emergency medical passport storing verified blood group, severe allergens (peanuts, gluten, dairy), chronic conditions (asthma, diabetes), inhaler custody, EpiPen requirements, treating physician contact, and immediate paramedic protocols.
* **Backend Features & Business Capabilities:**
  * **Physical KYC Document Locker Custody:** Complete chain-of-custody tracking preventing lost certificates and expediting student departures.
  * **Automated TC Clearance Engine:** Eliminates manual physical routing by programmatically auditing across library, finance, and STEM lab schemas.
  * **UDISE+ & APAAR Registry:** One-click compliance export ready for submission to the Ministry of Education's national portal.
  * **Cryptographic Official Transcripts:** Generates tamper-proof multi-year transcripts with unique SHA-256 hashes verified on public registrar endpoints.
  * **Critical Health Emergency Passport:** Immediate access for school nurses, teachers, and emergency first responders to verified medical triage and paramedic protocols.

---

### 4.5 `academics` Schema (Curricula, Master Timetable, Homework & Digital Locker)
* **Domain Responsibility:** Curriculum design, master timetable scheduling, faculty substitution boards, Toddle-grade homework, co-curricular events, smart study lockers, exit tickets, and emergency virtual schooling.
* **Core Entities:**
  * `Curriculum`: Academic boards (CBSE, ICSE, Cambridge CIE, IB DP) and grade syllabus mappings.
  * `ClassSection`: Classroom cohort master (e.g. Class 10 - Section A) with room allocations and class teacher assignments.
  * `Subject`: Academic and co-curricular subjects, subject codes (e.g. Math 041), and theory/practical weightages.
  * `TimetableSlot`: Master weekly timetable schedule mapping days, periods, subjects, teachers, and classrooms.
  * `TeacherSubstitution`: Faculty substitution board tracking absences, conflict detection, and syllabus-aware handoff notes.
  * `HomeworkAssignment` & `HomeworkSubmission`: Digital homework manager with submission deadlines, late tracking, and rubric grading.
  * `SchoolEvent` & `EventParticipant`: Co-curricular events (Sports Days, Olympiads, Exhibitions) with digital certificates (`CERT-EVT-2026-XXXX`).
  * `ClassroomLearningMaterial` & `MaterialAccessLog`: Multi-format digital asset repository (e-books, slides, board photos, lab videos, teacher notes) with student engagement telemetry.
  * `ExitTicketSession` & `ExitTicketSubmission`: Formative in-class pulse checks with 6-digit session PINs, confidence ratings, and comprehension heatmaps.
  * `AdaptiveStudyPlan`: Automatic remedial (<50%) and enrichment ($\ge 85\%$) study plan generator with task checklists.
  * `PblProject` & `PblSubmission`: Project-Based Learning (PBL) group milestones, collaborative submissions, and viva voce rubrics.
  * `VirtualClassroomSession` & `AsyncCatchupSubmission`: Emergency virtual classrooms (Jitsi/Zoom) and asynchronous catch-up lockers with proof-of-learning verification.
  * `DailySubstitution`: 06:30 AM morning auto-assigned teacher substitutions tracking class, period, absent faculty, substitute faculty, and lesson handoff briefs.
* **Backend Features & Business Capabilities:**
  * **Constraint-Satisfaction Master Timetable:** Algorithmic scheduler ensuring zero faculty period overlaps and balanced daily workload.
  * **Syllabus-Aware Auto-Substitution Board:** Recommends least-burdened available teachers along with exact next-lesson pacing instructions.
  * **06:30 AM Morning Substitution Engine with Load Balancing:** Automatically solves daily faculty morning emergency absences, balancing substitution periods across available teachers while strictly enforcing equity limits (max 2 periods/day).
  * **1-Click Campus Emergency Mode Pivot:** Switches school operations across 4 tiers (`PHYSICAL`, `VIRTUAL_EMERGENCY`, `HYBRID`, `COMPLETE_SHUTDOWN`).

---

### 4.6 `staff` Schema (HR, 7th CPC Payroll, Income Tax, CPD & Org Hierarchy)
* **Domain Responsibility:** Employee master, biometric turnstile integration, statutory payroll, 7th Central Pay Commission matrix, Section 115BAC income tax, staff loans, teacher training, and organizational structures.
* **Core Entities:**
  * `Employee`: Faculty and administrative staff records, PAN, Aadhaar, bank details, designation, and salary structure.
  * `StaffLeaveRequest`: Multi-level staff leave applications with automated handoff to the substitution board.
  * `BiometricPunchLog`: Turnstile hardware push punch logs (eSSL / ZKTeco protocols) with late-mark calculation.
  * `SalarySlip`: Itemized monthly payslips detailing basic pay, DA, HRA, statutory deductions, and net salary.
  * `TenantStaffDesignation`: Tenant-configurable designations (PGT, TGT, PRT, Lab Assistant) with reporting tiers and monetary approval caps.
  * `TeacherCpdTraining`: CBSE mandatory 50-hour Continuous Professional Development (CPD) training logs and compliance audits.
  * `StaffLoan`: Festival advances and emergency salary loans with automated monthly payroll EMI deductions.
  * `VendorInvoice`: Procurement vendor bills with statutory TDS withholding u/s 194C (2%) and 194J (10%).
* **Backend Features & Business Capabilities:**
  * **7th CPC Automated Payroll Engine:** Bi-annual Dearness Allowance (DA %) revisions with statutory Provident Fund (PF 12%), ESIC (0.75%), Professional Tax (₹200), and TDS withholding.
  * **Section 115BAC Form 16 Part B Generator:** Compiles annual tax statements comparing Old vs New tax regimes and generates certified Part B certificates.
  * **Dynamic Org Hierarchy Tree:** Interactive multi-tier organizational charts with configurable monetary authorization limits per designation tier.

---

### 4.7 `attendance` Schema (High-Throughput Batch Roll-Call, 09:00 AM Locks & Biometrics)
* **Domain Responsibility:** High-volume daily student roll-call, biometric turnstile arrival telemetry, 09:00 AM security cut-offs, attendance normalization, and CBSE 75% statutory watchdog.
* **Core Entities:**
  * `AttendanceSession`: Class-level daily roll-call session, lock timestamp, and submitting teacher UUID.
  * `AttendanceRecord`: Individual student daily status (`PRESENT`, `ABSENT`, `LATE`, `HALF_DAY`, `EXCUSED_LEAVE`, `PRESENT_VIRTUAL`).
  * `AttendanceCorrectionAudit`: Immutable audit trail of attendance corrections retaining original status, corrected status, reason code, and actor UUID.
  * `StudentLeaveApplication`: Parent-initiated digital leave applications with teacher approval and auto-normalization to `EXCUSED_LEAVE`.
  * `VirtualPresenceLog`: Real-time virtual classroom connection duration and heartbeat telemetry.
* **Backend Features & Business Capabilities:**
  * **20,000 Student Ingestion Pattern:** Optimized batch operations processing entire school cohort attendance in under 5 seconds.
  * **Strict 09:00 AM Cutoff Lock:** Automatically locks attendance sessions to prevent post-morning alterations without administrative override.
  * **CBSE 75% Attendance Watchdog:** Continuously evaluates instructional days attended, issuing automated hall ticket debarment warnings for truant candidates.

---

### 4.8 `examinations` Schema (CBSE Gradebook, Question Banks, LOC & Holistic Cards)
* **Domain Responsibility:** Examination scheduling, 70/20/10 weighted rubrics, anti-cheating seating plans, question banks, CBSE board candidate rosters, and NEP 2020 Holistic Progress Cards.
* **Core Entities:**
  * `GradebookRecord`: Student exam marks across multi-component assessments (Theory, Practical, Portfolio), total score, and CBSE grade band.
  * `ReportCardVerificationRecord`: Cryptographic SHA-256 credential verification hash and QR link.
  * `ExamHall`: Campus examination halls with seating capacities, rows, and desk layouts.
  * `ExamSeatingSlot`: Anti-cheating alternating seating allocations preventing students of the same class from sitting adjacent.
  * `InvigilationDuty`: Faculty examination invigilation rosters and relief assignments.
  * `CbseCandidateLoc`: Official CBSE Board List of Candidates (LOC) validating subject combinations (Math Standard 041 vs Basic 241, CWSN scribe concessions).
  * `HolisticProgressEntry`: NEP 2020 360° Holistic Progress Card (HPC) compiling Self, Peer, and Teacher competency evaluations.
  * `QuestionBankItem`: Bloom's Taxonomy cognitive question repository (Remembering, Understanding, Applying, Analyzing, Evaluating, Creating).
  * `ExamPaperBlueprint`: Blueprint-driven balanced examination paper generator with automatic teacher marking schemes.
  * `ExamInvigilationDuty`: Board invigilation rosters tracking hall room, primary/reliever roles, paper seal verification timestamps, and witness faculty signatures.
  * `UfmMalpracticeDocket`: Unfair Means (UFM) case dockets documenting contraband items (smart watches, notes, mobile devices), invigilator testimony, student statements, and disciplinary committee verdicts.
* **Backend Features & Business Capabilities:**
  * **CBSE 70/20/10 Weighted Score Compiler:** Calculates exact composite scores across 70% Theory, 20% Practical/Lab, and 10% Internal Portfolio assessments.
  * **Public Cryptographic QR Verification:** Unauthenticated public endpoint (`/gradebook/report-card/verify`) verifying authenticity against tampering.
  * **Automated Exam Seating Matrix:** Anti-cheating layout generator alternating grades/sections across examination hall desks.
  * **Anti-Cheating Invigilation Conflict Guard:** Programmatically blocks teachers from invigilating exams in their own teaching department or subject.
  * **Chain-of-Custody Exam Paper Seal Verification:** Verifies envelope barcode seals and witness signatures before examination packets are opened.
  * **UFM Malpractice Case Hearing Pipeline:** Formal disciplinary workflow ensuring due process and evidentiary recording for board-level examination integrity.

---

### 4.9 `finance` Schema (Double-Entry General Ledger, Invoices, Cheques & Cashless Wallets)
* **Domain Responsibility:** Grade-level fee structures, family cart checkouts, direct bank auto-reconciliation, physical cheque clearance lifecycles, cashless canteen RFID wallets, caution money refunds, and immutable double-entry bookkeeping.
* **Core Entities:**
  * `FeeStructure` & `FeeStructureItem`: Granular grade fee schedules (Tuition, Lab, Transport, Annual, Technology).
  * `FeeInvoice` & `FeeInvoiceItem`: Student billing invoices with decimal precision math (zero floating-point inaccuracies).
  * `FeePayment`: Transaction receipts, payment methods (Cash, Cheque, UPI, NetBanking, Card), and gateway transaction IDs.
  * `FeeConcession`: Sibling discount policies (15% 2nd child), staff ward concessions, and merit scholarships.
  * `ChequeRecord`: Physical cheque and Demand Draft (DD) tracking, clearance double-entry postings, and ₹500 bounce dishonour penalty ledger.
  * `GeneralLedgerAccount`: Chart of Accounts (Assets 1000s, Liabilities 2000s, Equity 3000s, Revenue 4000s, Expenses 5000s).
  * `GeneralLedgerEntry`: Immutable double-entry journal vouchers where modifications require compensating entries.
  * `PaymentWebhookEvent`: Idempotent payment gateway webhook log defending against replay attacks and duplicate settlements.
  * `StudentWallet` & `WalletTransaction`: Sodexo-grade cashless student RFID digital canteen wallet with parent spending caps and POS debits.
  * `CautionMoneyRecord`: Refundable student security deposits with departure clearance fine offsets and net payout vouchers.
  * `CashierDayScroll`: Cashier day-end physical note denomination balancing (₹500, ₹200, ₹100, ₹50 scrolls).
* **Backend Features & Business Capabilities:**
  * **Immutable Double-Entry Ledger:** Every financial event automatically posts balanced journal vouchers ($\sum \text{Debits} == \sum \text{Credits}$) with real-time Trial Balance verification.
  * **Direct Bank Auto-Reconciliation:** Ingests bank statements (HDFC/ICICI), matches transaction references (UTR/NEFT) against unpaid fee invoices, and pools unmapped receipts into a Suspense Account.
  * **Dynamic Daily Late Fee Surcharge Processor:** Automatically calculates ₹50/day overdue surcharges and posts interest revenue to the general ledger.

---

### 4.10 `communication` Schema (Circulars, WhatsApp, Grievance Ombudsman & PTM)
* **Domain Responsibility:** Official school circulars, WhatsApp Business Cloud dispatches, Firebase mobile push notifications, Parent-Teacher Meeting schedulers, and Ombudsman grievance desks.
* **Core Entities:**
  * `Circular`: Official school announcements, target audiences (All, Grade-specific, Staff), and PDF attachments.
  * `CircularAcknowledgment`: Digital parent signatures, IP receipts, and read timestamps.
  * `NotificationDispatchLog`: Multi-channel message delivery logs (WhatsApp, SMS, Push) with carrier message IDs.
  * `DeviceToken`: Mobile device registry for native Android Kotlin / iOS parent app push tokens.
  * `PtmSession` & `PtmSlot`: Parent-Teacher Meeting conference scheduler with collision-free 15-minute slot bookings.
  * `GrievanceTicket`: Parent Grievance Ombudsman desk with SLA timers, 3-tier escalation, and formal ombudsman resolutions.
* **Backend Features & Business Capabilities:**
  * **Multi-Channel Transactional Broadcasts:** Automated 09:00 AM absence alerts, fee overdue reminders, and emergency broadcast dispatches.
  * **Parent Grievance Ombudsman Desk:** Transparent SLA resolution tracking preventing unaddressed parent disputes.
  * **Toddle-Grade PTM Scheduler:** Collision-free conference slot reservations for multi-child families.

---

### 4.11 `operations` Schema (Fleet GPS Telematics, Hostel Boarding, Campus VMS, Labs & Library)
* **Domain Responsibility:** Samsara-grade bus telematics & fuel efficiency, residential hostel boarding operations, Verkada-grade campus visitor management, library circulation, campus emergency lockdowns, science lab apparatus breakages, and property vaults.
* **Core Entities:**
  * `Vehicle` & `TransportRoute`: School bus fleet master, GPS device IDs, driver allocations, and route waypoint stops.
  * `VehicleFuelLog`: Fleet diesel intake logs, fuel rates, odometer tracking, km/L mileage analytics, and sub-3.5 km/L pilferage warnings.
  * `VehicleComplianceSentinel`: Commercial fleet RTO statutory fitness, comprehensive insurance, and PUC certificate expiry monitors.
  * `HostelRoom` & `HostelAllocation`: Residential boarding blocks, room numbers, bed capacity, annual fees, and active student bed allocations.
  * `HostelRollCall`: Warden morning wake-up and night lights-out dorm bunk roll-calls with leave and infirmary status tracking.
  * `HostelExeatPass`: Outstation weekend exeat gate passes with parent authorization verification and gate check-out scanning.
  * `CampusVisitor`: Campus visitor check-in, photo ID proof, host staff in-app approval workflow, badge QR issue, and 2-hour overstay sentinels.
  * `GpsPing`: High-frequency IoT telematics pings (latitude, longitude, speed, ignition, timestamp).
  * `GatePass`: Cryptographic QR gate passes for student early departures and campus visitor management.
  * `LibraryBook` & `BookIssueRecord`: RFID / Barcode book accession catalog, loan checkout, and ₹5/day overdue fine assessment.
  * `ComplianceAsset`: Campus statutory assets (Fire Extinguishers, Building Seismic Stability, Bus RTO Fitness) with expiry alerts.
  * `EmergencyIncident`: Campus SOS emergency lockdown incidents with visitor gate pass revocation.
  * `LabApparatus` & `ApparatusBreakage`: Science & STEM robotics lab equipment inventory with student breakage fine auto-debits.
  * `LostAndFoundItem`: Campus property vault intake with tracking codes (`LF-2026-XXXX`) and verified chain-of-custody claims.
  * `BookstoreInventoryItem`: Campus bookstore and uniform SKU master with grade targets, sizes, cost/selling price, stock counts, and reorder thresholds.
  * `BookstoreSalesOrder`: Point-of-sale sales receipt orders with cash, card, UPI, and student fee ledger debit payment modes.
  * `EventCostumeRental`: Theatrical and annual day costume inventory, student actor loans, return condition inspection, and damage fines.
* **Backend Features & Business Capabilities:**
  * **Residential Hostel & Boarding House Operations:** Manages room inventory, bed allocation, warden night lights-out roll-calls, and weekend exeat gate passes with parent verification.
  * **Campus Visitor Security Management (VMS):** Visitor kiosk check-in, photo ID capture, host staff approval notifications, badge QR issue, and 2-hour campus overstay alerts.
  * **Fleet Fuel Logistics & RTO Compliance:** Fuel log tracking with km/L mileage analysis, automated fuel pilferage alerts, and 30-day statutory expiry sentinels.
  * **Haversine Geofenced Proximity Alerts:** Computes real-time radial distance between school bus and student homes, sending alerts when the bus is within 1 km with ETA countdowns.
  * **Verkada-Grade QR Gate Pass Desk:** Turnstile scanner verification preventing unauthorized campus student departures.
  * **Science Lab Breakage Fine Engine:** Links apparatus accidental/negligent breakages directly as auxiliary charges on the student's fee ledger.
  * **Campus Bookstore & Uniform Retail POS:** Full POS checkout with automated stock decrement, barcode scanning, and direct Student Fee Ledger debit options.
  * **Cafeteria POS Allergen Defense Interceptor:** Real-time safety shield intercepting food transactions against student medical allergies (peanuts, gluten, dairy), alerting cashier staff and displaying emergency EpiPen protocols.
  * **Campus Event Theatrical Costume Rental Desk:** Wardrobe issue, actor tracking, return damage inspection, and automated fine assessment.

---

### 4.12 `system` Schema (Extensibility, Multi-Tenancy, Webhooks & HA Sentinel)
* **Domain Responsibility:** Odoo-style app licensing, sub-service feature toggles, Salesforce-grade custom entities & metafields, outbound webhooks, bulk CSV onboarding, transactional outbox pattern, and Dedicated Dual-Database High Availability (HA) failover.
* **Core Entities:**
  * `TenantModuleLicense`: Modular application licenses (`CORE_ACADEMICS`, `FINANCE`, `FLEET_GPS`, `ATTENDANCE`, `EXAMINATIONS`, `COMMUNICATION`, `LIBRARY`, `ADMISSIONS`).
  * `TenantFeatureFlag`: Granular feature toggles (`ADMISSIONS_WALK_IN_TOKEN`, `BIOMETRIC_AUTO_PUNCH`, `ONLINE_UPI_CHECKOUT`) with `@RequireFeature` AOP enforcement.
  * `CustomFieldDefinition` & `CustomFieldValue`: Dynamic custom metafield definitions (TEXT, NUMBER, DATE, SELECT, BOOLEAN) and entity values with zero SQL migrations.
  * `CustomEntityDefinition` & `CustomEntityRecord`: Universal custom business entity builder with JSON Schema validation and generic CRUD lifecycle.
  * `TenantWebhookSubscription` & `WebhookDispatchLog`: Outbound webhook subscriptions with cryptographically-signed HMAC-SHA256 payloads.
  * `BulkImportJob`: High-throughput asynchronous CSV import engine with row-level error isolation.
  * `OutboxEvent`: Transactional outbox event relay with `@Scheduled` retry processing and dead-letter recovery.
  * `PodBackupRecord`: Dedicated pod database backup snapshots with SHA-256 integrity verification.
  * `AuditLog`: Immutable platform security and operational audit trail stream.
* **Backend Features & Business Capabilities:**
  * **Salesforce/ServiceNow Dynamic Extensibility:** Schools can create custom entities and metafields at runtime without requiring developer intervention, SQL DDL migrations, or server reboots.
  * **Dedicated Dual-Database HA Sentinel:** Multi-host JDBC auto-failover (`db-primary:5432`, `db-secondary:5433`) with automated 1.4-second failover drills and zero data loss.
  * **Production Hardening:** Spring Boot Actuator Prometheus observability (`/actuator/prometheus`), Spring Cache acceleration, and memory-safe query pagination.

---

## 5. Complete 252 Live Verified REST Endpoints Directory

All 252 endpoints are fully implemented, compiled, and verified via automated test suites and live HTTP scripts:

| # | Endpoint URL | Method | Module / Schema | Capability Description |
|:---:|---|:---:|:---:|---|
| **1** | `/api/v1/auth/login` | `POST` | `identity` | Stateless HMAC-SHA512 JWT authentication with Argon2id |
| **2** | `/api/v1/students` | `GET` | `students` | Multi-tenant student directory with sibling links |
| **3** | `/api/v1/students/{id}/documents` | `GET` | `admissions` / `students` | Physical document locker custody register |
| **4** | `/api/v1/attendance/sessions/batch` | `POST` | `attendance` | Batch roll-call ingestion (20k capacity pattern) |
| **5** | `/api/v1/finance/invoices` | `GET` | `finance` | Student fee invoice ledger with decimal precision math |
| **6** | `/api/v1/finance/ledger` | `GET` | `finance` | Immutable double-entry general ledger journal vouchers |
| **7** | `/api/v1/examinations/gradebook` | `GET` | `examinations` | CBSE continuous assessment marksheet records |
| **8** | `/api/v1/system/audit-logs` | `GET` | `system` | Immutable platform security audit trail stream |
| **9** | `/api/v1/staff/employees` & `/leaves` | `GET` | `staff` | Staff directory and leave application workflows |
| **10** | `/api/v1/operations/vehicles` & `/routes` | `GET` | `operations` | Fleet vehicle master & transport route stops |
| **11** | `/api/v1/admissions/{id}/status` | `PATCH` | `admissions` | Admission pipeline status transition desk |
| **12** | `/api/v1/students/{id}/documents/deposit` | `POST` | `students` | Original KYC document custody deposit (Locker tags) |
| **13** | `/api/v1/finance/fee-structures` | `POST` / `GET` | `finance` | Dynamic grade-level multi-component fee structures |
| **14** | `/api/v1/finance/concessions` | `POST` / `GET` | `finance` | Sibling discount & merit scholarship rules |
| **15** | `/api/v1/finance/cheques/{id}/clear` | `PATCH` | `finance` | Cheque clearance & bounce dishonour penalty ledger |
| **16** | `/api/v1/examinations/gradebook/calculate` | `POST` | `examinations` | CBSE 70/20/10 weighted component score compiler |
| **17** | `/api/v1/examinations/gradebook/report-card/verify` | `GET` | `examinations` | Public tamper-proof SHA-256 QR credential verification |
| **18** | `/api/v1/attendance/corrections` | `POST` / `GET` | `attendance` | Bi-temporal attendance correction audit trail |
| **19** | `/api/v1/communication/send-message` | `POST` | `communication` | WhatsApp Business Cloud API communication dispatch |
| **20** | `/api/v1/communication/send-message` | `POST` | `communication` | Firebase Cloud Messaging (FCM) mobile push notification |
| **21** | `/api/v1/system/modules/{code}/toggle` | `POST` | `system` | Odoo-style runtime tenant module licensing toggle |
| **22** | `/api/v1/staff/payroll/biometric/punch` | `POST` / `GET` | `staff` | Turnstile hardware biometric punch sync & late-marking |
| **23** | `/api/v1/staff/payroll/generate` | `POST` / `GET` | `staff` | Statutory payroll engine (PF, ESI, PT, TDS) & payslips |
| **24** | `/api/v1/academics/substitutions/recommend` | `GET` / `POST` | `academics` | Timetable substitution conflict resolver & dispatch |
| **25** | `/api/v1/finance/family-checkout` | `GET` | `finance` | Unified multi-child family cart with sibling concession |
| **26** | `/api/v1/finance/reports/trial-balance` | `GET` | `finance` | Double-entry general ledger Trial Balance statement |
| **27** | `/api/v1/operations/telemetry/geofence-ping` | `POST` / `GET` | `operations` | Samsara-grade IoT bus geofencing proximity telematics |
| **28** | `/api/v1/operations/gate-passes/issue` & `/verify-scan` | `POST` | `operations` | Verkada-grade physical gate pass & turnstile departure |
| **29** | `/api/v1/students/{id}/clearance/evaluate` | `POST` | `students` | Multi-department exit clearance (Library, Fees, Labs) |
| **30** | `/api/v1/students/{id}/transfer-certificate/generate` | `POST` / `GET` | `students` | Official Transfer Certificate issuance & QR verification |
| **31** | `/api/v1/students/infirmary/visits` | `POST` / `GET` | `students` | Campus clinic triage log & high-fever parent alerts |
| **32** | `/api/v1/academics/homework` | `POST` / `GET` | `academics` | Toddle-style homework assignment, submit & grade desk |
| **33** | `/api/v1/mobile/parent/home` | `GET` | `mobile` | Aggregated native mobile parent dashboard gateway |
| **34** | `/api/v1/finance/wallet/topup` | `POST` | `finance` | Cashless student RFID smart wallet online top-up |
| **35** | `/api/v1/finance/wallet/transact` | `POST` | `finance` | Cafeteria POS contactless smart card debit & limit check |
| **36** | `/api/v1/students/behavior/record` | `POST` | `students` | Student merit & demerit house incident logger |
| **37** | `/api/v1/students/behavior/houses/leaderboard` | `GET` | `students` | Annual Cock House Championship Trophy leaderboard |
| **38** | `/api/v1/communication/ptm/slots/{id}/book` | `POST` / `GET` | `communication` | Parent-Teacher Meeting (PTM) conference slot scheduler |
| **39** | `/api/v1/operations/library/books` | `POST` / `GET` | `operations` | RFID / Barcode library book accession catalog |
| **40** | `/api/v1/operations/library/books/search` | `GET` | `operations` | Full-text OPAC catalog search with availability |
| **41** | `/api/v1/operations/library/issues` | `POST` / `GET` | `operations` | Library circulation lending desk with due dates |
| **42** | `/api/v1/operations/library/issues/{id}/return` | `POST` | `operations` | Book return with auto overdue fine calculation |
| **43** | `/api/v1/staff/leaves` | `POST` / `GET` | `staff` | Staff leave application workflow & balance tracker |
| **44** | `/api/v1/staff/leaves/{id}/review` | `POST` | `staff` | Principal leave review & auto substitution cascade |
| **45** | `/api/v1/communication/circulars` | `POST` / `GET` | `communication` | Official school circular publishing desk |
| **46** | `/api/v1/communication/circulars/{id}/acknowledge` | `POST` / `GET` | `communication` | Parent digital signature acknowledgment register |
| **47** | `/api/v1/finance/invoices/process-late-fees` | `POST` | `finance` | Dynamic daily late fee surcharge batch processor |
| **48** | `/api/v1/attendance/compliance/students/{id}` | `GET` | `attendance` | CBSE 75% minimum attendance compliance watchdog |
| **49** | `/api/v1/system/features` | `GET` | `system` | Granular sub-service tenant entitlement flags directory |
| **50** | `/api/v1/system/features/toggle` | `POST` | `system` | Runtime sub-service feature flag toggle & tier modifier |
| **51** | `/api/v1/academics/rollovers/execute` | `POST` | `academics` | Academic year batch student grade promotion & rollover |
| **52** | `/api/v1/academics/rollovers/history` | `GET` | `academics` | Academic year rollover execution audit trail history |
| **53** | `/api/v1/examinations/halls` | `POST` / `GET` | `examinations` | Dedicated exam hall registry with bench & seat capacity |
| **54** | `/api/v1/examinations/seating/generate` | `POST` / `GET` | `examinations` | Anti-cheating alternating seating plan matrix generator |
| **55** | `/api/v1/operations/incidents/sos` | `POST` / `GET` | `operations` | Campus emergency SOS lockdown & containment protocol |
| **56** | `/api/v1/system/backups/create` & `/telemetry` | `POST` / `GET` | `system` | Encrypted Pod snapshot backup & JVM health telemetry |
| **57** | `/api/v1/students/{id}/certificates/generate` | `POST` | `students` | Bonafide, Character & CEA Fee Estimation certificates |
| **58** | `/api/v1/students/certificates/verify/{qrToken}` | `GET` | `students` | Public unauthenticated certificate QR verification |
| **59** | `/api/v1/students/udise/export` | `GET` | `students` | Ministry of Education UDISE+ and APAAR ID registry |
| **60** | `/api/v1/admissions/rte/register` | `POST` | `admissions` | RTE Act 2009 25% EWS quota admission enrollment |
| **61** | `/api/v1/admissions/rte/claims/annual` | `GET` | `admissions` | State Education Department annual reimbursement claim |
| **62** | `/api/v1/examinations/cbse-loc/export` | `GET` | `examinations` | Official CBSE LOC Pariksha Sangam board submission |
| **63** | `/api/v1/finance/invoices/{id}/upi-qr` | `GET` | `finance` | NPCI Dynamic UPI Intent QR code for fee collection |
| **64** | `/api/v1/finance/cashier/day-scroll/close` | `POST` | `finance` | Cashier note denomination day-end balancing scroll |
| **65** | `/api/v1/staff/payroll/epfo-ecr/export` | `GET` | `staff` | EPFO Unified Portal Electronic Challan cum Return (ECR) |
| **66** | `/api/v1/academics/assemblies/active-week` | `GET` | `academics` | Morning Quadrangle Assembly & House duty roster |
| **67** | `/api/v1/institution/calendar/events` & `/is-working-day` | `POST` / `GET` | `institution` | Holiday Calendar, Rainy-Day Closures & Bell Schedules |
| **68** | `/api/v1/auth/refresh` & `/identity/sessions/active` | `POST` / `GET` | `identity` | Refresh Token Rotation & Revocable User Sessions |
| **69** | `/api/v1/system/imports/students-csv` | `POST` | `system` | High-Throughput Bulk CSV Onboarding with Row Error Isolation |
| **70** | `/api/v1/system/outbox/enqueue` & `/retry-failed` | `POST` / `GET` | `system` | Transactional Outbox Relay & Automated Cron Scheduler |
| **71** | `/v3/api-docs` | `GET` | `system` | Interactive OpenAPI 3 Specification & Flyway V1 Migration |
| **72** | `/api/v1/academics/risk-radar/evaluate` | `POST` / `GET` | `academics` | Multi-Signal Student Early-Warning Risk Radar |
| **73** | `/api/v1/examinations/hpc/assessments` & `/students/{id}` | `POST` / `GET` | `examinations` | NEP 2020 360° Holistic Progress Card (HPC) Engine |
| **74** | `/api/v1/operations/compliance-sentinel/assets` & `/expiring` | `POST` / `GET` | `operations` | Statutory Licence, Bus RTO Fitness & Fire NOC Expiry Sentinel |
| **75** | `/api/v1/academics/syllabus/progress` | `POST` | `academics` | Real-Time Classroom Chapter & Curriculum Pacing Tracker |
| **76** | `/api/v1/academics/substitutions/syllabus-handoff` | `GET` | `academics` | Syllabus-Aware Substitute Teacher Instructional Handoff |
| **77** | `/api/v1/staff/workload-equity` | `GET` | `staff` | Faculty Burnout Prevention & Workload Equity Index |
| **78** | `/api/v1/students/export/excel` | `GET` | `students` | Binary Microsoft Excel (.xlsx) classroom export with freeze panes |
| **79** | `/api/v1/students/export/csv` | `GET` | `students` | Streamed RFC 4180 student CSV data stream with UTF-8 BOM |
| **80** | `/api/v1/students/export/printable-roster` | `GET` | `students` | A4 printable HTML classroom attendance and verification roster |
| **81** | `/api/v1/academics/events` | `POST` | `academics` | Teacher school event and co-curricular sports day authoring |
| **82** | `/api/v1/academics/events/{id}/participants` | `POST` | `academics` | Student/cohort enrollment with designated roles (Captain, Performer) |
| **83** | `/api/v1/academics/events/{id}/participants/{pId}/results` | `PUT` | `academics` | Record achievement awards, credit house trophy points & issue certificates |
| **84** | `/api/v1/academics/events/{id}/roster/print` | `GET` | `academics` | Printable event-day backstage call sheet for field coordinators |
| **85** | `/api/v1/attendance/leaves/apply` | `POST` | `attendance` | Parent student medical/casual leave application with prescription URL |
| **86** | `/api/v1/attendance/leaves/{id}/review` | `PUT` | `attendance` | Teacher leave review and attendance auto-normalization to EXCUSED_LEAVE |
| **87** | `/api/v1/examinations/question-bank` | `POST` | `examinations` | Bloom's Taxonomy question authoring with marking schemes |
| **88** | `/api/v1/examinations/question-bank/generate-paper` | `POST` | `examinations` | Blueprint-driven balanced examination paper and answer key generator |
| **89** | `/api/v1/operations/lab/inventory` | `GET` | `operations` | Science & STEM robotics laboratory apparatus stock ledger |
| **90** | `/api/v1/operations/lab/breakages` | `POST` | `operations` | Lab apparatus breakage assessment & student auxiliary fee auto-debit |
| **91** | `/api/v1/operations/lost-and-found` & `/claim` | `POST` / `PUT` | `operations` | Campus lost-and-found property vault intake and verified claims |
| **92** | `/api/v1/students/alumni/placements` | `POST` | `students` | Class 12 alumni university admits & entrance exam ranks (JEE, NEET, CUET) |
| **93** | `/api/v1/institution/executive-cockpit/kpis` | `GET` | `institution` | Executive Board Strategic Command Cockpit KPIs & health tiers |
| **94** | `/api/v1/institution/safety-vault/incidents` | `POST` | `institution` | Confidential Child Safety Vault (POCSO, POSH, Anti-Bullying) |
| **95** | `/api/v1/staff/appraisals/evaluate` | `POST` | `staff` | Faculty 360° Appraisal & PBAS Merit Engine (0-100 composite scoring) |
| **96** | `/api/v1/communication/grievances` | `POST` | `communication` | Parent Grievance Ombudsman desk intake with SLA countdown timers |
| **97** | `/api/v1/communication/grievances/{id}/review` | `PUT` | `communication` | Ombudsman hearing review, multi-tier escalation & formal dispute resolution |
| **98** | `/api/v1/institution/inspection-dossier/generate` | `GET` | `institution` | CBSE & statutory accreditation inspection readiness compliance dossier |
| **99** | `/api/v1/system/database/ha-status` | `GET` | `system` | Dedicated Dual-DB HA cluster status & streaming replication monitor |
| **100** | `/api/v1/system/database/simulate-failover` & `/reset-topology` | `POST` | `system` | Automated instant failover drill (<1.5s switchover, 0 data loss) |
| **101** | `/api/v1/staff/payroll/cpc/apply-da-revision` | `POST` | `staff` | 7th CPC bi-annual DA % revision & automated faculty recalculation |
| **102** | `/api/v1/staff/tax/declaration` | `POST` | `staff` | Faculty Income Tax Section 192 (Old vs Section 115BAC New Regime) declaration |
| **103** | `/api/v1/staff/tax/form16/{employeeId}` | `GET` | `staff` | Official Form 16 Part B annual salary and TDS certificate generator |
| **104** | `/api/v1/finance/caution-money/refund` | `POST` | `finance` | Student caution money / security deposit & departure refund clearance |
| **105** | `/api/v1/staff/loans/apply` | `POST` | `staff` | Staff festival advance & emergency salary loan ledger with payroll auto-EMIs |
| **106** | `/api/v1/finance/vendors/invoices` | `POST` | `finance` | School procurement vendor bills & statutory TDS withholding u/s 194C / 194J |
| **107** | `/api/v1/academics/timetable/auto-generate` | `POST` | `academics` | Constraint-satisfaction master timetable auto-scheduler (0 collisions) |
| **108** | `/api/v1/academics/timetable/teacher-schedule/{name}` | `GET` | `academics` | Individual faculty weekly workload and class timetable schedule |
| **109** | `/api/v1/staff/cpd/log-training` | `POST` | `staff` | CBSE mandatory 50-hour teacher continuous professional development (CPD) logger |
| **110** | `/api/v1/staff/cpd/compliance-summary` | `GET` | `staff` | School-wide CBSE 50-hour teacher CPD compliance audit summary |
| **111** | `/api/v1/finance/bank-reconciliation/process-statement` | `POST` | `finance` | Direct bank statement auto-reconciliation & UTR transaction matcher |
| **112** | `/api/v1/finance/bank-reconciliation/suspense-pool` | `GET` | `finance` | Unallocated bank credits suspense pool & manual ledger allocator |
| **113** | `/api/v1/finance/concessions/configure-policy` | `POST` | `finance` | Sibling concession policy configuration desk |
| **114** | `/api/v1/finance/concessions/evaluate-family` | `POST` | `finance` | Dynamic family sibling discount evaluator & tuition invoice modifier |
| **115** | `/api/v1/attendance/biometric/webhook` | `POST` | `attendance` | Biometric IoT machine webhook (eSSL/ZKTeco push protocol ingestion) |
| **116** | `/api/v1/attendance/biometric/daily-summary` | `GET` | `attendance` | Campus daily biometric entry summary & late-arrival roster |
| **117** | `/api/v1/staff/designations` | `POST` | `staff` | Tenant-configurable staff designations & reporting hierarchy node authoring |
| **118** | `/api/v1/staff/designations` | `GET` | `staff` | Multi-tenant designation registry with department & tier filtering |
| **119** | `/api/v1/staff/designations/{id}/toggle-status` | `PATCH` | `staff` | Runtime toggle for modular/lean SaaS designation activation |
| **120** | `/api/v1/staff/designations/org-hierarchy` | `GET` | `staff` | Interactive organizational hierarchy tree with financial approval limits |
| **121** | `/api/v1/academics/materials/publish` | `POST` | `academics` | Multi-format digital classroom lesson material publisher (e-book, PPT, video, board notes) |
| **122** | `/api/v1/academics/materials/class-feed` | `GET` | `academics` | Student & parent digital study locker feed filtered by class and subject |
| **123** | `/api/v1/academics/materials/log-access` | `POST` | `academics` | Student study session logger with duration & completion tracking |
| **124** | `/api/v1/academics/materials/{id}/engagement` | `GET` | `academics` | Teacher engagement audit report with view analytics & student prep metrics |
| **125** | `/api/v1/academics/exit-tickets/launch` | `POST` | `academics` | Teacher launches in-class formative exit ticket check with 6-digit PIN |
| **126** | `/api/v1/academics/exit-tickets/submit` | `POST` | `academics` | Student submits answer and confidence rating (Understood/Unsure/Struggling) |
| **127** | `/api/v1/academics/exit-tickets/{id}/heatmap` | `GET` | `academics` | Teacher real-time comprehension heatmap & struggling student list |
| **128** | `/api/v1/academics/exit-tickets/{id}/close` | `PUT` | `academics` | Close exit ticket session and aggregate class comprehension metrics |
| **129** | `/api/v1/students/portfolio/upload` | `POST` | `students` | Student uploads creative experiential learning artifact (photo, video, code) |
| **130** | `/api/v1/students/portfolio/student/{id}` | `GET` | `students` | Retrieve student chronological learning journey portfolio feed |
| **131** | `/api/v1/students/portfolio/showcase` | `GET` | `students` | Public featured school learning exhibition showcase |
| **132** | `/api/v1/students/portfolio/{id}/review` | `PUT` | `students` | Teacher reviews artifact with competency rubric bands (Exemplary/Proficient) |
| **133** | `/api/v1/students/portfolio/{id}/parent-clap` | `POST` | `students` | Parent applause and encouragement reaction from mobile/web portal |
| **134** | `/api/v1/academics/adaptive-plans/generate` | `POST` | `academics` | Auto-generate Remedial (<50%) or Enrichment (>=85%) study plan |
| **135** | `/api/v1/academics/adaptive-plans/student/{id}` | `GET` | `academics` | Student retrieves personalized study roadmap and curated locker links |
| **136** | `/api/v1/academics/adaptive-plans/{id}/progress` | `PUT` | `academics` | Student updates study plan progress and completion status |
| **137** | `/api/v1/students/badges/award` | `POST` | `students` | Live in-class micro-badge award with auto House Points credit |
| **138** | `/api/v1/students/badges/student/{id}` | `GET` | `students` | Student badge collection summary & character development profile |
| **139** | `/api/v1/students/badges/class-summary` | `GET` | `students` | Class-wide badges feed and motivation leaderboard |
| **140** | `/api/v1/academics/pbl/projects` | `POST` | `academics` | Teacher creates multi-week group project with milestone gates |
| **141** | `/api/v1/academics/pbl/projects` | `GET` | `academics` | List active PBL projects filtered by grade level |
| **142** | `/api/v1/academics/pbl/submissions` | `POST` | `academics` | Student group submits milestone artifact and member roster |
| **143** | `/api/v1/academics/pbl/submissions/{id}/grade` | `PUT` | `academics` | Teacher grades milestone with rubrics and peer contribution rating % |
| **144** | `/api/v1/academics/pbl/projects/{id}/submissions` | `GET` | `academics` | Project submissions feed filtered by group name |
| **145** | `/api/v1/institution/emergency-mode/pivot` | `POST` | `institution` | Principal declares emergency operating mode (Virtual/Shutdown) |
| **146** | `/api/v1/institution/emergency-mode/status` | `GET` | `institution` | Inspect current campus operational mode & virtual readiness |
| **147** | `/api/v1/institution/emergency-mode/restore-physical` | `POST` | `institution` | Restore normal physical campus operations and resume bus transit |
| **148** | `/api/v1/institution/emergency-mode/safety-checkin` | `POST` | `institution` | Student/Staff/Parent emergency welfare safety check-in |
| **149** | `/api/v1/institution/emergency-mode/safety-summary` | `GET` | `institution` | Disaster relief welfare summary with urgent assistance cases |
| **150** | `/api/v1/academics/virtual-classrooms/auto-generate-daily` | `POST` | `academics` | Auto-convert daily timetable periods into secure virtual meeting rooms |
| **151** | `/api/v1/academics/virtual-classrooms/student-feed` | `GET` | `academics` | Student personalized virtual classroom schedule with 1-click join |
| **152** | `/api/v1/academics/virtual-classrooms/{id}/start` | `POST` | `academics` | Teacher launches live virtual classroom session |
| **153** | `/api/v1/academics/virtual-classrooms/{id}/end` | `POST` | `academics` | Conclude virtual session & archive lecture recording URL |
| **154** | `/api/v1/academics/virtual-classrooms/async-catchup/submit` | `POST` | `academics` | Student submits asynchronous catch-up proof-of-learning |
| **155** | `/api/v1/academics/virtual-classrooms/async-catchup/{id}/verify` | `PUT` | `academics` | Teacher verifies catch-up & normalizes attendance to excused completed |
| **156** | `/api/v1/attendance/virtual/log-presence` | `POST` | `attendance` | Ingest live student virtual connection duration & heartbeat telemetry |
| **157** | `/api/v1/attendance/virtual/session/{id}` | `GET` | `attendance` | Teacher virtual session attendance roster & participation breakdown |
| **158** | `/api/v1/system/custom-fields` | `POST` | `system` | Define dynamic custom metafield on core entities (`STUDENT`, `STAFF`, etc.) |
| **159** | `/api/v1/system/custom-fields` | `GET` | `system` | List configured custom fields with entity type filter |
| **160** | `/api/v1/system/custom-fields/values` | `POST` | `system` | Attach/update dynamic custom field values for an entity instance |
| **161** | `/api/v1/system/custom-fields/values/{entityType}/{entityId}` | `GET` | `system` | Retrieve all custom metafield values and metadata for an entity instance |
| **162** | `/api/v1/system/custom-entities` | `POST` | `system` | Register new dynamic custom entity type with JSON Schema definition |
| **163** | `/api/v1/system/custom-entities` | `GET` | `system` | List tenant custom entity definitions with category filter |
| **164** | `/api/v1/system/custom-entities/{entityCode}` | `GET` | `system` | Fetch custom entity definition, field schema, and record count |
| **165** | `/api/v1/system/custom-entities/{entityCode}/records` | `POST` | `system` | Create record instance in dynamic custom entity with JSON payload |
| **166** | `/api/v1/system/custom-entities/{entityCode}/records` | `GET` | `system` | Query records of a custom entity with status filtering |
| **167** | `/api/v1/system/custom-entities/{entityCode}/records/{id}` | `GET` | `system` | Retrieve single custom entity record by UUID |
| **168** | `/api/v1/system/custom-entities/{entityCode}/records/{id}` | `PUT` | `system` | Update custom entity record title, status, and dynamic data |
| **169** | `/api/v1/system/custom-entities/{entityCode}/records/{id}` | `DELETE` | `system` | Soft-delete / archive custom entity record and decrement count |
| **170** | `/api/v1/system/webhooks` | `POST` | `system` | Create outbound webhook subscription for tenant events |
| **171** | `/api/v1/system/webhooks` | `GET` | `system` | List configured tenant webhook subscriptions |
| **172** | `/api/v1/system/webhooks/{id}/test-ping` | `POST` | `system` | Dispatch test ping to webhook target and verify HMAC-SHA256 signature |
| **173** | `/actuator/health` | `GET` | `actuator` | Spring Boot Actuator ECS/Kubernetes liveness and readiness health probes |
| **174** | `/actuator/prometheus` | `GET` | `actuator` | Prometheus real-time metrics scrape stream for JVM, threads, and HikariCP |
| **175** | `/api/v1/students/paged` | `GET` | `students` | Paged student directory query with total count, size, and page index |
| **176** | `/api/v1/system/audit-logs/paged` | `GET` | `system` | Paged security and operational audit trail query with timestamp ordering |
| **177** | `/api/v1/admissions/crm/walk-in/rapid-intake` | `POST` | `admissions` | Rapid walk-in intake, counselor queue token dispenser, and dynamic lead scoring |
| **178** | `/api/v1/admissions/crm/walk-in/{id}/spot-fee` | `POST` | `admissions` | Spot prospectus fee collection via Cash/UPI QR with instant receipt generation |
| **179** | `/api/v1/admissions/crm/inquiries` | `POST` | `admissions` | Standard online/form admission inquiry intake and counselor assignment |
| **180** | `/api/v1/admissions/crm/inquiries` | `GET` | `admissions` | Filtered admissions pipeline inquiries directory |
| **181** | `/api/v1/admissions/crm/inquiries/{id}` | `GET` | `admissions` | Complete inquiry dossier, interaction history, and conversion lead score |
| **182** | `/api/v1/admissions/crm/inquiries/{id}/stage` | `PATCH` | `admissions` | Pipeline stage transition with counselor audit notes and drop reason tracking |
| **183** | `/api/v1/admissions/crm/interview-slots` | `POST` | `admissions` | Campus tour, entrance test, or principal interview slot booking |
| **184** | `/api/v1/admissions/crm/interview-slots/{id}/evaluate` | `PUT` | `admissions` | 1-5 star rubric evaluation with grade recommendation and feedback |
| **185** | `/api/v1/admissions/crm/offers/issue` | `POST` | `admissions` | Formal conditional merit offer letter issuance with fee calculation and concessions |
| **186** | `/api/v1/admissions/crm/offers/{id}/convert-to-student` | `POST` | `admissions` | 1-Click conversion: provisions active Student, initial Fee Invoice, & marks ENROLLED |
| **Analytics** | `/api/v1/admissions/crm/analytics/funnel` | `GET` | `admissions` | Executive admissions conversion funnel metrics and spot revenue summary |
| **187** | `/api/v1/admissions/crm/portal/tracking/{inquiryNumber}` | `GET` | `admissions` | Parent self-service application tracking with stage progress % and phone auth |
| **188** | `/api/v1/admissions/crm/documents/{inquiryId}/upload` | `POST` | `admissions` | Parent / Staff document upload for application checklist item |
| **189** | `/api/v1/admissions/crm/documents/{documentId}/verify` | `PUT` | `admissions` | Admissions Officer verifies document or flags re-upload with specific reasons |
| **190** | `/api/v1/admissions/crm/documents/{inquiryId}` | `GET` | `admissions` | Query all application checklist documents and verification states |
| **191** | `/api/v1/admissions/crm/capacity/configure` | `POST` | `admissions` | Configure grade sanctioned seats and category quota reservations (General, RTE, Sibling) |
| **192** | `/api/v1/admissions/crm/capacity/overview` | `GET` | `admissions` | Real-time seat matrix: Sanctioned, Enrolled, Offered, Available, and Waitlist count |
| **193** | `/api/v1/admissions/crm/capacity/waitlist/{inquiryId}/promote` | `POST` | `admissions` | 1-Click counselor promotion from Waitlist to active Merit Offer |
| **194** | `/api/v1/admissions/crm/tasks` | `POST` | `admissions` | Create and schedule counselor follow-up task with priority and SLA due date |
| **195** | `/api/v1/admissions/crm/tasks` | `GET` | `admissions` | Query counselor task queue filtered by counselor name and status (`PENDING`) |
| **196** | `/api/v1/admissions/crm/tasks/{id}/complete` | `PUT` | `admissions` | Complete counselor task and log call outcome notes to inquiry timeline |
| **197** | `/api/v1/admissions/crm/criteria/evaluate-points` | `POST` | `admissions` | Calculate 100-Point DoE score (distance, sibling, alumni, staff, single girl child) |
| **198** | `/api/v1/admissions/crm/criteria/statutory-certificates` | `POST` | `admissions` | Record and verify Tehsildar/SDM government caste (SC/ST/OBC-NCL), EWS & CWSN certificates |
| **199** | `/api/v1/admissions/crm/criteria/merit-list/{grade}` | `GET` | `admissions` | Generate quota-segregated criteria merit lists with tie-breaker rank sequences |
| **200** | `/api/v1/admissions/crm/capacity/category-matrix` | `POST` | `admissions` | Configure statutory Indian quota seat matrix (General, RTE, OBC, SC, ST, CWSN, Sibling, Staff, Girl Child) |
| **201** | `/api/v1/admissions/crm/capacity/category-overview/{grade}` | `GET` | `admissions` | Real-time category-wise seat availability and quota-segregated waitlists overview |
| **202** | `/api/v1/admissions/crm/capacity/category-waitlist/{quota}/promote` | `POST` | `admissions` | 1-Click promote waitlisted candidate from specific category pool (e.g. OBC_NCL or Sibling) |
| **203** | `/api/v1/admissions/crm/entrance-tests/schedule` | `POST` | `admissions` | Schedule diagnostic entrance assessment, allocate exam hall, & issue cryptographic admit card token |
| **204** | `/api/v1/admissions/crm/entrance-tests/admit-card/{inquiryNumber}` | `GET` | `admissions` | Public parent printable test hall ticket admit card with test center instructions |
| **205** | `/api/v1/admissions/crm/entrance-tests/{testId}/evaluate` | `PUT` | `admissions` | Score multi-subject test marks (Math, English, Science, Aptitude) & determine cut-off qualification |
| **206** | `/api/v1/admissions/crm/inquiries/{id}/link-sibling` | `POST` | `admissions` | Link verified student sibling, award +20 priority points, and forecast sibling fee concession savings |
| **207** | `/api/v1/admissions/crm/scholarships/apply` | `POST` | `admissions` | Apply for merit/need/sports scholarship grant with institutional review committee workflow |
| **208** | `/api/v1/admissions/crm/portal/offer/{offerId}/accept-and-lock` | `POST` | `admissions` | Parent digital e-signature acceptance & online seat lock token deposit payment |
| **209** | `/api/v1/operations/hostel/rooms` | `POST` | `operations` | Register residential hostel room with block, room number, type, bed capacity, and annual fee |
| **210** | `/api/v1/operations/hostel/allocations` | `POST` | `operations` | Allocate hostel dorm bed to student with bed capacity enforcement & automatic room occupancy increment |
| **211** | `/api/v1/operations/hostel/roll-call` | `POST` | `operations` | Record warden morning wake-up and night lights-out dorm bunk roll-call with leave & infirmary status |
| **212** | `/api/v1/operations/hostel/exeat-passes` | `POST` | `operations` | Issue student outstation weekend exeat gate pass with parent authorization & departure check-out scan |
| **213** | `/api/v1/operations/hostel/roster/{block}` | `GET` | `operations` | Query residential hostel block occupancy roster, bed capacity, vacant beds, & active resident directory |
| **214** | `/api/v1/operations/visitors/check-in` | `POST` | `operations` | Campus visitor check-in, photo ID proof capture, purpose logging, entry gate, & badge QR token issue |
| **215** | `/api/v1/operations/visitors/{id}/host-approval` | `PUT` | `operations` | Host staff in-app visit approval or rejection with entry denial guard |
| **216** | `/api/v1/operations/visitors/{id}/check-out` | `PUT` | `operations` | Confirm visitor campus check-out and timestamp security badge release |
| **217** | `/api/v1/operations/visitors/active` | `GET` | `operations` | Active on-campus visitor register with automated 2-hour overstay detection sentinel |
| **218** | `/api/v1/operations/fleet/fuel-logs` | `POST` | `operations` | Record bus fuel fill, odometer tracking, km/L mileage analytics, & sub-3.5 km/L pilferage warnings |
| **219** | `/api/v1/operations/fleet/fitness-sentinel` | `GET` | `operations` | Commercial fleet RTO statutory fitness, comprehensive insurance, & PUC certificate expiry monitor |
| **220** | `/api/v1/students/{id}/transcripts/generate` | `POST` | `students` | Generate official multi-year cumulative academic transcript with SHA-256 cryptographic verification |
| **221** | `/api/v1/academics/substitutions/morning-auto-assign` | `POST` | `academics` | 06:30 AM morning auto-assignment of substitute teachers with daily equity load balancing limits |
| **222** | `/api/v1/academics/substitutions/daily-roster` | `GET` | `academics` | Query daily morning substitution roster with absent teacher handoffs and period coverage |
| **223** | `/api/v1/examinations/invigilation/auto-schedule` | `POST` | `examinations` | Anti-cheating invigilation scheduler enforcing subject conflict rules (teachers cannot invigilate own subject) |
| **224** | `/api/v1/examinations/invigilation/{id}/verify-seals` | `PUT` | `examinations` | Exam question paper security tape barcode verification & sealed answer sheet envelope chain-of-custody |
| **225** | `/api/v1/examinations/invigilation/ufm/report` | `POST` | `examinations` | Report Unfair Means (UFM) malpractice case docket with contraband type and invigilator testimony |
| **226** | `/api/v1/examinations/invigilation/ufm/dockets` | `GET` | `examinations` | Retrieve active UFM malpractice dockets queued for examination disciplinary committee hearing |
| **227** | `/api/v1/operations/bookstore/items` | `POST` | `operations` | Register or update campus bookstore & uniform outlet SKU master with size, grade, cost & selling price |
| **228** | `/api/v1/operations/bookstore/items` | `GET` | `operations` | Query bookstore & uniform inventory filtered by category, grade level, and stock reorder thresholds |
| **229** | `/api/v1/operations/bookstore/sales/checkout` | `POST` | `operations` | POS sales checkout with automatic stock decrement and optional student fee ledger debit |
| **230** | `/api/v1/operations/bookstore/sales/orders` | `GET` | `operations` | Retrieve bookstore point-of-sale receipt vouchers, audit lines, and payment settlements |
| **231** | `/api/v1/students/{id}/health-passport` | `POST` | `students` | Save critical emergency health passport with blood group, severe allergens, EpiPen, and paramedic protocols |
| **232** | `/api/v1/students/{id}/health-passport` | `GET` | `students` | Retrieve verified student emergency health passport with physician contact and medical clearance |
| **233** | `/api/v1/operations/cafeteria/allergen-check` | `POST` | `operations` | Real-time cafeteria POS safety interceptor, cross-checking food ingredients against student allergen profiles |
| **234** | `/api/v1/operations/events/costumes/register` | `POST` | `operations` | Register annual day / theatrical event costume in wardrobe inventory with character role & rental fee |
| **235** | `/api/v1/operations/events/costumes/issue-and-return` | `PUT` | `operations` | Issue costume to student actor and process return with wardrobe damage condition inspection & fine |
| **236** | `/api/v1/operations/events/costumes/roster` | `GET` | `operations` | Retrieve event costume wardrobe roster, active loans, damage penalties, and return audit trails |
| **237** | `/api/v1/admissions/desk/queue/dispense` | `POST` | `admissions` | Dispense front-desk queue token across categories (Enquiry, Verification, Fee Payment, Interview) |
| **238** | `/api/v1/admissions/desk/queue/call-next` | `POST` | `admissions` | Multi-counter officer calling desk with category filtering and counter assignment |
| **239** | `/api/v1/admissions/desk/queue/{id}/complete` | `PUT` | `admissions` | Finalize queue token service, log duration minutes, and record counselor notes or no-shows |
| **240** | `/api/v1/admissions/desk/queue/live-board` | `GET` | `admissions` | Live multi-counter reception queue display board telemetry (serving, next-in-line, wait times) |
| **241** | `/api/v1/admissions/desk/prospectus/add-stock` | `POST` | `admissions` | Batch ingest serialized printed prospectus kit barcode stock into front-desk inventory |
| **242** | `/api/v1/admissions/desk/prospectus/sell-kit` | `POST` | `admissions` | Point-of-sale kit sale (Cash/UPI QR) linked to inquiry dossier to prevent duplicate submission |
| **243** | `/api/v1/admissions/desk/prospectus/inventory-summary` | `GET` | `admissions` | Real-time prospectus stock register, sales metrics, and daily cashier revenue reconciliation |
| **244** | `/api/v1/admissions/desk/undertakings/issue` | `POST` | `admissions` | Issue legally binding undertaking for missing Transfer Certificates/documents with 30-day statutory deadlines |
| **245** | `/api/v1/admissions/desk/undertakings/{id}/fulfill` | `PUT` | `admissions` | Fulfill deficient document undertaking upon original certificate physical receipt and custody stamping |
| **246** | `/api/v1/admissions/desk/undertakings/overdue-sentinel` | `GET` | `admissions` | Sentinel scanner retrieving pending undertakings and flagging overdue compliance violations |
| **247** | `/api/v1/admissions/desk/onboarding/mint-identity` | `POST` | `admissions` | Mint permanent student admission number (`ADM-2026-XXXX`), section, roll, and house allocation |
| **248** | `/api/v1/admissions/desk/onboarding/welcome-dossier/{studentId}` | `GET` | `admissions` | Retrieve 5-part printable Welcome Pack dossier (Admission Letter, Temp Badge, Bus Pass, Book Voucher, Portal Pass) |
| **249** | `/api/v1/admissions/desk/onboarding/smart-card/serialize` | `POST` | `admissions` | Bind smart RFID/NFC campus card UID to student profile and pre-load initial cafeteria wallet balance |
| **250** | `/api/v1/admissions/desk/surrender/calculate` | `POST` | `admissions` | Calculate statutory fee refund schedule based on days remaining before academic session start |
| **251** | `/api/v1/admissions/desk/surrender/process` | `POST` | `admissions` | Process confirmed seat surrender, issue General Ledger refund voucher, and recycle quota seat to waitlist |
| **252** | `/api/v1/admissions/desk/surrender/audit-ledger` | `GET` | `admissions` | Audit ledger of all surrendered seats, refunded amounts, and recycled vacancies |

---

## 6. Omni-Channel Portals & Client Applications

The platform delivers specialized user interfaces tailored for each role in the academic ecosystem:

### 6.1 Administrator & Principal Web Portal (`src/app/admin/`)
- Built with **Next.js 15 (App Router)**, **TypeScript**, **Tailwind CSS**, and **shadcn/ui**.
- High-density Linear/Stripe aesthetic: tabular numbers, hairline borders, compact layout.
- Real-time school telemetry, admissions pipeline, faculty allocation, document custody register, module license manager, and Executive Command Cockpit.

### 6.2 Faculty & Teacher Web Portal (`src/app/teacher/`)
- Scoped class roll-call desk with 09:00 AM cutoff enforcement.
- Multi-component CBSE 9-point gradebook, anecdotal observation logger.
- Digital homework assignments manager, automated timetable substitution board, and **Classroom Lesson Materials Publisher**.

### 6.3 Accountant & Bursar Web Portal (`src/app/accountant/`)
- Dynamic fee structure configuration with component breakdowns (Tuition, Lab, Transport, Library).
- Direct Bank Statement Auto-Reconciliation desk with suspense pool matching.
- Physical cheque/DD clearance desk with dishonour penalty workflows.
- Immutable General Ledger explorer and real-time Trial Balance statement generator.

### 6.4 Parent & Family Mobile Web Portal (`src/app/parent/`)
- Responsive mobile-first sibling switcher (toggle between children).
- 1-click online fee checkout with dynamic sibling concessions.
- Live bus GPS transit map with real-time ETA countdown, official circulars, and student **Digital Study Locker**.

### 6.5 Native Android Parent Mobile App (`mobile-parent/`)
- **Native Kotlin 2.x + Jetpack Compose** architecture.
- **Dynamic School Connect Screen:** Parent enters school code (e.g., `apex`) or scans student ID QR code to link directly to the school's isolated host.
- Biometric authentication (`BiometricPrompt`), Room database offline caching, and high-priority FCM push notifications.

---

## 7. Enterprise Invariants & Regulatory Compliance

1. **Double-Entry General Ledger Balance Invariant:**
   $$\sum \text{Total Debits} == \sum \text{Total Credits}$$
   Financial journal entries cannot be deleted or updated in place. Adjustments require compensating journal entries.
2. **Statutory Deduction Matrix & 7th CPC Parity:**
   - **Provident Fund (PF):** Strictly 12% on Basic Pay.
   - **Employee State Insurance (ESI):** 0.75% on Gross Salary (for eligible brackets).
   - **Professional Tax (PT):** ₹200.00 statutory monthly deduction.
   - **Income Tax (TDS):** Section 192 withholding with Old vs Section 115BAC New Regime support and Form 16 Part B generation.
   - **Dearness Allowance (DA):** Automated bi-annual 7th CPC revisions (e.g., 50% DA parity).
3. **Cryptographic Credential Verification:**
   - Report cards, Bonafide Certificates, and Transfer Certificates carry a SHA-256 cryptographic digest.
   - Unauthenticated public verification endpoint allows anyone to verify document authenticity by scanning the QR code.
4. **Attendance Bi-Temporal Audit Trail & Normalization:**
   - Attendance corrections maintain original status, revised status, actor UUID, timestamp, and standard reason codes.
   - Approved medical/casual leaves automatically normalize absences to `EXCUSED_LEAVE`, safeguarding student eligibility under CBSE 75% attendance rules.
5. **Multi-Host Database Redundancy & Zero Data Loss:**
   - Primary and secondary standby PostgreSQL instances synchronize via streaming replication with 0ms lag.
   - Failover is automated via JDBC multi-host drivers, ensuring uninterrupted operations during hardware degradation.
6. **DPDP Act 2023 / GDPR Sensitive PII Encryption at Rest:**
   - Field-level AES-256-GCM authenticated encryption with random 12-byte IVs for sensitive student identifiers (Aadhaar / APAAR ID, PEN Number) and confidential health passports (emergency paramedic protocols, chronic conditions).
7. **Cross-Tenant Data Isolation & Header Spoofing Defense:**
   - Cryptographic tenant binding in JWT authentication filter intercepting and rejecting cross-tenant header tampering (`X-Tenant-ID != tokenTenantId`) with HTTP 403 Forbidden.
8. **Cryptographic Payment Webhook HMAC Verification:**
   - Constant-time HMAC-SHA256 signature verification for payment webhooks (`X-Razorpay-Signature`, `Stripe-Signature`), preventing forged transaction settlements and ledger tampering.
9. **Concurrency & Race Condition Defenses:**
   - Row-level pessimistic write locking (`@Lock(LockModeType.PESSIMISTIC_WRITE)`) and optimistic concurrency versioning (`@Version`) guarding student RFID wallet balances and bookstore inventory against double-spending under concurrent loads.
10. **IDOR Defense on Mobile Parent Gateway:**
    - Strict principal binding to the authenticated JWT identity ensuring parent endpoints never leak student records across families.

---

## 8. Getting Started & Local Development

### Prerequisites
- **Java 21 LTS** (Amazon Corretto, Eclipse Temurin, or OpenJDK)
- **Node.js 18+** & **npm**
- **Git**
- Optional: **Docker Desktop** (for containerized PostgreSQL & Redis)

### Running the Spring Boot Backend Engine
```bash
# Navigate to the backend directory
cd backend

# Compile Java sources
./gradlew compileJava

# Run the backend application (starts on port 8080)
./gradlew bootRun
```
*The development profile (`application-dev.yml`) runs with an in-memory PostgreSQL-mode database, automatically seeded with multi-tenant sample data across all 12 schemas.*

### Running the Next.js Frontend Portals
```bash
# Navigate to the root directory
cd ..

# Install dependencies
npm install

# Start Next.js development server (starts on port 3000)
npm run dev
```
Access the portals:
- Admin Portal: `http://localhost:3000/admin`
- Teacher Portal: `http://localhost:3000/teacher`
- Parent Portal: `http://localhost:3000/parent`

---

## 9. Automated Testing & Live Verification

### 1. Automated JUnit 5 Integration Test Suite
The automated test suite validates multi-tenant isolation, statutory payroll calculations, 7th CPC DA revisions, Section 115BAC Form 16 generation, double-entry balancing, direct bank reconciliation, constraint timetable scheduling, teacher substitution conflicts, Samsara geofencing, sub-service feature toggles, academic rollover promotions, anti-cheating seating plans, campus emergency lockdowns, Dual-DB failover drills, tenant designation hierarchies, classroom lesson materials locker, in-class exit tickets, learning portfolios, adaptive study plans, classroom micro-badges, project-based learning milestones, virtual emergency campus pivot, complete emergency shutdown, welfare safety check-in registry, virtual classroom sessions (Jitsi/Zoom), asynchronous catch-up normalization, dynamic custom metafields, universal custom entity creation & dynamic JSON records lifecycle, outbound webhook subscriptions with HMAC-SHA256 signatures, Argon2id cryptographic password hashing, Spring Cache metadata acceleration, paged student & audit log directories, rapid physical walk-in desk intake, spot prospectus fee collection, interview rubric evaluations, formal merit offer letters, 1-click active student conversion, admissions funnel analytics, AES-256-GCM field encryption at rest, payment webhook HMAC-SHA256 verification, pessimistic wallet double-spending defense, and cross-tenant header spoofing defense:

```bash
cd backend
./gradlew test
```
*Expected Result: `BUILD SUCCESSFUL` (115/115 tests passed with 0 failures, 0 errors in ~1m 30s).*

### 2. Live 252-Endpoint Verification Script
Verify all 252 live REST API endpoints against an active backend server:

```powershell
powershell -ExecutionPolicy Bypass -File backend\test_endpoints.ps1
```
*Expected Result: All 252 endpoints return HTTP 200 OK / 201 Created with formatted payloads (100% success rate).*

---

## 10. Cloud Infrastructure & Production DevOps

For production deployment on **AWS Mumbai (`ap-south-1`)**:

```
AWS ap-south-1
├── Route 53 (Subdomains: *.schoolerp.cloud / custom school domains)
├── AWS CloudFront (CDN for static web assets)
├── Application Load Balancer (ALB) with SSL/TLS termination
├── AWS ECS Fargate (Isolated Spring Boot backend container per school)
├── Amazon RDS PostgreSQL Multi-AZ (Dedicated Primary & Standby DB pair per school)
├── Amazon ElastiCache Redis (Session cache & background queue)
├── Amazon S3 Private Buckets (Encrypted student documents, classroom materials & medical logs)
└── AWS KMS (Customer-managed data encryption keys)
```

### Dedicated School Pod Provisioning
Launch a dedicated isolated instance for any onboarded school with a single command:

```bash
docker run -d --name school-backend-apex \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e SPRING_DATASOURCE_URL="jdbc:postgresql://db-primary:5432,db-secondary:5433/school_apex_db?targetServerType=primary" \
  -e SPRING_DATASOURCE_USERNAME=apex_admin \
  -e SPRING_DATASOURCE_PASSWORD=secure_vault_password \
  -e SERVER_PORT=8080 \
  -p 8080:8080 \
  school-erp-backend:latest
```

### 10.2 Enterprise Self-Hosted On-Premise & Private Cloud (BYOC) Deployment

For schools, educational trusts, or university networks requiring **complete data sovereignty**, physical campus server hosting, or private cloud VPC deployment:

```mermaid
flowchart TD
    subgraph Campus_Server["On-Premise Server Room / School Private Cloud"]
        CF["Cloudflare Zero Trust Tunnel (cloudflared)<br/>No Public Static IP & No Open Firewall Ports"]
        CAD["Caddy Reverse Proxy & Edge TLS"]
        BE["Spring Boot 3 Backend (Project Loom / ZGC)"]
        FE["Next.js 15 Web Portal Hub"]
        PG["PostgreSQL 16 Primary + Hot Standby"]
        RD["Redis 7 Cache & Job Broker"]
        MINIO["MinIO S3-Compatible Storage (Local KYC Vault)"]
        BKP["Air-Gapped Backup Sentinel Daemon"]

        CF --> CAD
        CAD --> FE
        CAD --> BE
        BE --> PG
        BE --> RD
        BE --> MINIO
        BKP --> PG
        BKP --> MINIO
    end

    EXT["Parents & Mobile App / Payment Webhooks"] -->|Encrypted HTTPS| CF
    LAN["Campus Intranet (Turnstiles, POS, Teachers)"] -->|Gigabit LAN (Works Offline!)| CAD
```

#### Key Capabilities of On-Premise Deployment
1. **Turnkey 1-Click Installer (`deploy/onprem/install-onprem.sh` & `.ps1`)**:
   - Inspects host CPU/RAM/disk, generates cryptographically secure passwords, sets up Docker Compose containers, and runs Flyway database migrations in under 3 minutes.
2. **No Public Static IP & Inbound Firewall Solved**:
   - Integrated **Cloudflare Zero Trust Tunnel (`cloudflared`)** establishes an outbound-only encrypted pipeline to `https://erp.schoolname.edu.in`. Requires **zero router port forwarding** and zero exposed ports.
3. **Local Data Sovereignty (MinIO S3)**:
   - Replaces AWS S3 with an embedded MinIO S3-compatible engine so schools incur zero external cloud storage bills and keep all student KYC documents physically on campus.
4. **Campus LAN Offline Continuity**:
   - If campus broadband goes down, biometric turnstiles, cafeteria smart card POS, teacher class roll-call, and library circulation continue operating at gigabit speed over local intranet IP (`http://192.168.1.50` or `http://erp.local`).
5. **Air-Gapped Backup & Disaster Recovery (`backup-daemon.sh` & `restore-onprem.sh`)**:
   - Automated nightly compressed PostgreSQL dumps and MinIO archives with SHA-256 checksums and automated mirroring to external USB 3.2 drives or school Synology NAS, with a 5-minute point-in-time recovery runbook.
6. **Zero-Downtime Safe Rolling Upgrades (`upgrade-onprem.sh`)**:
   - Takes a pre-upgrade database snapshot, pulls new containers, executes Flyway migrations, verifies the `/actuator/health` probe, and automatically rolls back if healthcheck fails.

#### Hardware Sizing Matrix
| School Size | Students | Recommended Server Spec | RAM & JVM Allocation |
|:---|:---:|:---|:---|
| **Tier 1: Standard Campus** | Up to 1,500 | 8 vCPU, 16 GB DDR4, $2 \times 500\text{ GB}$ NVMe (RAID-1) | JVM: 6 GB / PG: 4 GB / Redis: 1 GB / OS: 5 GB |
| **Tier 2: Enterprise Campus** | 1,500 – 6,000 | 16 vCPU, 32 GB DDR4 ECC, $2 \times 1\text{ TB}$ NVMe (RAID-1) | JVM: 12 GB / PG: 8 GB / Redis: 2 GB / OS: 10 GB |
| **Tier 3: Mega Trust / University** | 6,000 – 20,000+ | 32 vCPU, 64 GB DDR4 ECC, $4 \times 1.92\text{ TB}$ NVMe (RAID-10) | JVM: 24 GB / PG: 16 GB / Redis: 4 GB / OS: 20 GB |

---

## License & Compliance
This software is engineered in compliance with **CBSE Affiliation Norms**, **India DPDP Act 2023**, and **OWASP Top 10** security standards.

*Engineered with precision for global educational institutions.*
