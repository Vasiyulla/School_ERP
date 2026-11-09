# School ERP System - Agent Operational Guidelines & Rules

## 1. Core Operating Directives

### 1.1 Living Architecture & On-The-Spot Evolution
- The initial SRS and Database Blueprints serve as the **foundational overview and engineering baseline**.
- **On-The-Spot Additions:** If new ideas, business rules, entity attributes, workflow nuances, or edge cases come up in discussions or development, they must be **added and integrated on the spot** into the living project memory (`.agents/PROJECT_OVERVIEW.md`) and the active codebase. Never reject or defer viable new requirements just because they were not in the initial baseline.

### 1.2 Ban on Stagnant Structural Review (Advance Beyond the Baseline)
- **Zero Structural Stagnation:** The baseline project architecture (12 PostgreSQL schemas, Spring Boot modular monolith, Next.js portals) is completely established, compiled, and verified across 21 live endpoints.
- Agents must **NEVER stall, loop, or redundantly review/re-analyze the project structure**.
- Every turn must focus directly on delivering **advanced, cutting-edge, market-leading capabilities** that leap beyond basic ERP structures.

### 1.3 Enterprise & Top-Tier Market Benchmark (Match Commercial Industry Leaders)
- Every feature or module built must strictly follow **Tier-1 Commercial SaaS standards**, directly benchmarked against global industry leaders:
  - **PowerSchool & Toddle**: Standards-based grading, CBSE 70/20/10 weighted rubrics, continuous evaluation, digital document locker custody, tamper-proof SHA-256 QR credentials.
  - **Odoo Enterprise & Workday**: Dynamic tenant modular app licensing, multi-level approval hierarchies, immutable double-entry ledger journals, idempotent payment settlement.
  - **SAP Fiori & SuccessFactors**: Biometric punch sync, automated staff payroll engine with statutory deductions (PF, ESI, TDS, Form 16), auto-substitution boards.
  - **Samsara & Uber Fleet**: High-frequency IoT GPS vehicle telematics, route optimization, geofenced parent pickup alerts, ETA countdowns.
  - **Stripe Billing & Razorpay**: Multi-child sibling concessions, cheque dishonour penalty workflows, dynamic daily late fee surcharges, idempotent webhook defense.
- Never implement shallow, toy, or generic demo features. Real-world edge cases, production-grade security, and high concurrency are mandatory.

### 1.4 Native Mobile Parity (Kotlin Jetpack Compose)
- The Parent & Family Mobile App is engineered with **Native Kotlin + Jetpack Compose** (`mobile-parent/`), utilizing FCM device registration, biometric authentication, offline Room caching, and deep-linked parent actions.

---

## 2. Technical Standards & Invariants

1. **Architecture:** Modular Monolith built with **Next.js** (Frontend Portals), **Java 21 + Spring Boot 3** (Backend Engine), and **Kotlin Jetpack Compose** (Native Parent Mobile App).
2. **Database Authority:** PostgreSQL is the single source of truth.
   - 12 Logical Schemas: `identity`, `institution`, `admissions`, `students`, `academics`, `staff`, `attendance`, `examinations`, `finance`, `communication`, `operations`, `system`.
   - Primary Keys: UUIDs everywhere (`gen_random_uuid()` / `cuid`).
   - Currency & Financial Math: Strict `NUMERIC` / `DECIMAL(12, 2)` (never floats).
   - Timezones: All timestamps stored in `TIMESTAMPTZ` (UTC / configured school timezone).
   - Financial Ledger: Immutable double-entry bookkeeping. Financial postings must never be deleted or updated in place; corrections require compensating entries.
   - Attendance Audits: Attendance corrections must retain original values, corrected values, actor UUID, and timestamps.
3. **Capacity Target:** 20,000 students, high concurrent loads, p95 API latency < 500ms, batch attendance processing.
4. **Security & Privacy:**
   - Action-based RBAC with strict resource-level boundaries (parents see only linked children; teachers see only assigned classes).
   - Password hashing via Argon2id.
   - Refresh token rotation and revocable sessions.
   - Webhook idempotency for payment gateways (browser redirects are never treated as payment proof).
5. **Background Jobs:** Asynchronous processing via Redis and BullMQ (notifications, PDF report cards, bulk imports, fee invoices, transactional outbox).

---

## 3. Agent Roles & Delegation in `.agents/`

When tackling tasks, adopt the specialized perspective or invoke the relevant role:
- **Project Memory Custodian (`.agents/skills/project-memory-custodian`)**: Enforces forward momentum; bans redundant structural reviews; records cutting-edge market additions on the spot.
- **Backend Core Architect (`.agents/skills/backend-architect`)**: Java 21 + Spring Boot 3 modular monolith, REST APIs (`/api/v1`), Spring Data JPA multi-schema PostgreSQL, Spring Security 6 RBAC, Spring Batch.
- **Mobile Native Architect (`.agents/skills/mobile-native-architect`)**: Native Android development in Kotlin, Jetpack Compose, Coroutines, Flow, Retrofit2, Room, and Firebase Cloud Messaging (FCM).
- **Database Schema Architect (`.agents/skills/database-architect`)**: PostgreSQL 12-schema modeling, Prisma ORM, migrations, indexing, constraints.
- **Finance & Payment Specialist (`.agents/skills/finance-payment-specialist`)**: Fee structures, concessions, cheques, payment gateways, webhook idempotency, double-entry ledger, refunds.
- **Academic & Attendance Specialist (`.agents/skills/academic-attendance-specialist`)**: Board-agnostic curriculum, batch attendance (20k capacity), CBSE 70/20/10 exams, grading bands, QR report cards.
- **DevOps & Cloud Engineer (`.agents/skills/devops-cloud-engineer`)**: AWS Mumbai architecture (ECS Fargate, RDS Multi-AZ, ElastiCache Redis, S3, KMS), Terraform, security.
- **UI/UX Design Expert (`.agents/skills/ui-ux-design-expert`)**: Tailwind CSS, shadcn/ui, responsive dashboards for Admin, Teacher, Student, Parent, and Accountant portals.
