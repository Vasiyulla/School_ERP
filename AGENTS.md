# School ERP System - Agent Operational Guidelines & Rules

## 1. Core Operating Directives

### 1.1 Living Architecture & On-The-Spot Evolution
- The initial SRS and Database Blueprints (from `writing-block.md` and the 4 ERD diagrams) serve as the **foundational overview and engineering baseline**.
- **On-The-Spot Additions:** If new ideas, business rules, entity attributes, workflow nuances, or edge cases come up in discussions or development, they must be **added and integrated on the spot** into the living project memory (`.agents/PROJECT_OVERVIEW.md`) and the active codebase. Never reject or defer viable new requirements just because they were not in the initial baseline.

### 1.3 Enterprise & Real-World Feature Standard (No Toy Implementations)
- Whenever new features or modules are discussed, planned, or built, they must strictly follow **Tier-1 Enterprise and Real-World SaaS standards** (benchmarked against Odoo Enterprise, Workday, PowerSchool, and SAP Fiori).
- Never implement shallow, toy, or generic demo features. Every feature must include:
  - Real-world edge cases (e.g. gateway replay-attacks, idempotent webhooks, partial payments, original document locker custody, bi-temporal attendance audits, multi-component grade weighting).
  - Production-grade security (action-based RBAC, tenant isolation, immutable audit logging).
  - High concurrency and data integrity constraints (virtual threads, numeric currency precision, database indexing).

---

## 2. Technical Standards & Invariants

1. **Architecture:** Modular Monolith built with **Next.js** (Frontend) and **Java 21 + Spring Boot 3** (Backend).
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
- **Project Memory Custodian (`.agents/skills/project-memory-custodian`)**: Updates living blueprints on the spot; enforces memory retention.
- **Backend Core Architect (`.agents/skills/backend-architect`)**: Java 21 + Spring Boot 3 modular monolith, REST APIs (`/api/v1`), Spring Data JPA multi-schema PostgreSQL, Spring Security 6 RBAC, Spring Batch.
- **Database Schema Architect (`.agents/skills/database-architect`)**: PostgreSQL 12-schema modeling, Prisma ORM, migrations, indexing, constraints.
- **Finance & Payment Specialist (`.agents/skills/finance-payment-specialist`)**: Fee structures, invoices, payment gateways, webhook idempotency, ledger entries, refunds.
- **Academic & Attendance Specialist (`.agents/skills/academic-attendance-specialist`)**: Board-agnostic curriculum, batch attendance (20k capacity), exams, grading bands, report cards.
- **DevOps & Cloud Engineer (`.agents/skills/devops-cloud-engineer`)**: AWS Mumbai architecture (ECS Fargate, RDS Multi-AZ, ElastiCache Redis, S3, KMS), Terraform, security.
- **UI/UX Design Expert (`.agents/skills/ui-ux-design-expert`)**: Tailwind CSS, shadcn/ui, responsive dashboards for Admin, Teacher, Student, Parent, and Accountant portals.

