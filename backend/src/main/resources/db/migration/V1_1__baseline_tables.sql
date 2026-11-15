-- =========================================================================
-- Flyway Migration V1_1: Production Baseline Tables Across 12 Schemas
-- Target: PostgreSQL 16 LTS (AWS RDS Multi-AZ / Dedicated Pods)
-- Standard: UUIDs everywhere, strict DECIMAL(12,2) finance, TIMESTAMPTZ
-- =========================================================================

-- Enable pgcrypto for gen_random_uuid() if needed
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- 1. IDENTITY SCHEMA
CREATE TABLE IF NOT EXISTS identity.roles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    role_name VARCHAR(64) NOT NULL UNIQUE,
    description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS identity.users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(64) NOT NULL,
    email VARCHAR(128) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(128) NOT NULL,
    phone VARCHAR(32),
    status VARCHAR(32) DEFAULT 'ACTIVE',
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_user_tenant_email UNIQUE (tenant_id, email)
);

CREATE TABLE IF NOT EXISTS identity.user_roles (
    user_id UUID NOT NULL REFERENCES identity.users(id) ON DELETE CASCADE,
    role_id UUID NOT NULL REFERENCES identity.roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE IF NOT EXISTS identity.user_sessions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(64) NOT NULL,
    user_id UUID NOT NULL,
    email VARCHAR(128) NOT NULL,
    refresh_token VARCHAR(255) NOT NULL UNIQUE,
    client_ip VARCHAR(64),
    user_agent VARCHAR(255),
    is_revoked BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMPTZ NOT NULL
);

-- 2. INSTITUTION SCHEMA
CREATE TABLE IF NOT EXISTS institution.tenants (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_identifier VARCHAR(64) NOT NULL UNIQUE,
    school_name VARCHAR(255) NOT NULL,
    subdomain VARCHAR(64) NOT NULL UNIQUE,
    affiliation_number VARCHAR(64),
    status VARCHAR(32) DEFAULT 'ACTIVE',
    subscription_tier VARCHAR(32) DEFAULT 'ENTERPRISE',
    student_capacity INT DEFAULT 20000,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS institution.academic_calendar_events (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(64) NOT NULL,
    title VARCHAR(255) NOT NULL,
    event_type VARCHAR(64) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    is_holiday BOOLEAN DEFAULT FALSE,
    bell_schedule_preset VARCHAR(64) DEFAULT 'REGULAR',
    description TEXT,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS institution.emergency_campus_modes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(64) NOT NULL,
    operating_mode VARCHAR(64) NOT NULL,
    reason VARCHAR(128) NOT NULL,
    reason_details TEXT,
    activated_by_user VARCHAR(128) NOT NULL,
    activated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    bus_transport_suspended BOOLEAN DEFAULT FALSE,
    auto_virtual_classroom_provisioned BOOLEAN DEFAULT FALSE,
    student_safety_checkin_mandatory BOOLEAN DEFAULT FALSE,
    concluded_at TIMESTAMPTZ
);

CREATE TABLE IF NOT EXISTS institution.emergency_safety_checkins (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(64) NOT NULL,
    emergency_mode_id UUID,
    user_id UUID NOT NULL,
    user_name VARCHAR(128) NOT NULL,
    user_role VARCHAR(64) NOT NULL,
    safety_status VARCHAR(64) NOT NULL,
    current_location VARCHAR(255),
    assistance_details TEXT,
    checkin_timestamp TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- 3. ADMISSIONS SCHEMA
CREATE TABLE IF NOT EXISTS admissions.admissions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(64) NOT NULL,
    application_number VARCHAR(64) NOT NULL UNIQUE,
    student_name VARCHAR(128) NOT NULL,
    grade_applying_for VARCHAR(32) NOT NULL,
    guardian_name VARCHAR(128) NOT NULL,
    guardian_phone VARCHAR(32) NOT NULL,
    guardian_email VARCHAR(128),
    status VARCHAR(32) DEFAULT 'SUBMITTED',
    submission_date DATE DEFAULT CURRENT_DATE,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- 4. STUDENTS SCHEMA
CREATE TABLE IF NOT EXISTS students.students (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(64) NOT NULL,
    admission_number VARCHAR(64) NOT NULL,
    full_name VARCHAR(128) NOT NULL,
    gender VARCHAR(16) NOT NULL,
    date_of_birth DATE NOT NULL,
    class_name VARCHAR(32) NOT NULL,
    section_name VARCHAR(16) NOT NULL,
    roll_number INT,
    house_name VARCHAR(64),
    status VARCHAR(32) DEFAULT 'ACTIVE',
    father_name VARCHAR(128),
    mother_name VARCHAR(128),
    primary_contact_phone VARCHAR(32),
    primary_contact_email VARCHAR(128),
    address TEXT,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_student_tenant_admission UNIQUE (tenant_id, admission_number)
);

CREATE TABLE IF NOT EXISTS students.student_documents (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(64) NOT NULL,
    student_id UUID NOT NULL REFERENCES students.students(id) ON DELETE CASCADE,
    document_type VARCHAR(64) NOT NULL,
    custody_status VARCHAR(64) DEFAULT 'VERIFIED_COPY',
    locker_tag VARCHAR(64),
    verified_by VARCHAR(128),
    verified_at TIMESTAMPTZ,
    document_file_url VARCHAR(512),
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS students.student_wallets (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(64) NOT NULL,
    student_id UUID NOT NULL UNIQUE,
    balance NUMERIC(12,2) DEFAULT 0.00,
    daily_spend_limit NUMERIC(12,2) DEFAULT 500.00,
    rfid_card_uid VARCHAR(64),
    status VARCHAR(32) DEFAULT 'ACTIVE',
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS students.wallet_transactions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    wallet_id UUID NOT NULL,
    amount NUMERIC(12,2) NOT NULL,
    type VARCHAR(32) NOT NULL,
    category VARCHAR(64),
    receipt_number VARCHAR(64),
    description VARCHAR(255),
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- 5. ACADEMICS SCHEMA
CREATE TABLE IF NOT EXISTS academics.class_sections (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(64) NOT NULL,
    class_name VARCHAR(32) NOT NULL,
    section_name VARCHAR(16) NOT NULL,
    class_teacher_name VARCHAR(128),
    room_number VARCHAR(32),
    capacity INT DEFAULT 40,
    academic_year VARCHAR(32) DEFAULT '2026-2027',
    CONSTRAINT uq_class_section UNIQUE (tenant_id, class_name, section_name, academic_year)
);

CREATE TABLE IF NOT EXISTS academics.timetable_periods (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(64) NOT NULL,
    class_name VARCHAR(32) NOT NULL,
    section_name VARCHAR(16) NOT NULL,
    day_of_week VARCHAR(16) NOT NULL,
    period_number INT NOT NULL,
    subject_name VARCHAR(64) NOT NULL,
    teacher_name VARCHAR(128) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL
);

CREATE TABLE IF NOT EXISTS academics.lesson_materials (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(64) NOT NULL,
    teacher_id UUID NOT NULL,
    teacher_name VARCHAR(128) NOT NULL,
    subject_name VARCHAR(64) NOT NULL,
    class_name VARCHAR(32) NOT NULL,
    section_name VARCHAR(16) NOT NULL,
    chapter_name VARCHAR(255) NOT NULL,
    topic_name VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    material_type VARCHAR(64) NOT NULL,
    file_url VARCHAR(512) NOT NULL,
    thumbnail_url VARCHAR(512),
    file_size_bytes BIGINT DEFAULT 0,
    file_extension VARCHAR(16),
    lesson_date DATE NOT NULL,
    view_count INT DEFAULT 0,
    download_count INT DEFAULT 0,
    is_downloadable BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS academics.virtual_classroom_sessions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(64) NOT NULL,
    class_name VARCHAR(32) NOT NULL,
    section_name VARCHAR(16) NOT NULL,
    subject_name VARCHAR(64) NOT NULL,
    period_number INT NOT NULL,
    teacher_name VARCHAR(128) NOT NULL,
    session_date DATE NOT NULL,
    scheduled_start_time TIME NOT NULL,
    scheduled_end_time TIME NOT NULL,
    session_status VARCHAR(64) DEFAULT 'SCHEDULED',
    meeting_provider VARCHAR(64) DEFAULT 'JITSI_WEBRTC',
    meeting_room_code VARCHAR(128) NOT NULL,
    join_meeting_url VARCHAR(512) NOT NULL,
    actual_started_at TIMESTAMPTZ,
    actual_ended_at TIMESTAMPTZ,
    recording_url VARCHAR(512),
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- 6. STAFF SCHEMA
CREATE TABLE IF NOT EXISTS staff.employees (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(64) NOT NULL,
    employee_code VARCHAR(32) NOT NULL,
    full_name VARCHAR(128) NOT NULL,
    email VARCHAR(128) NOT NULL,
    phone VARCHAR(32),
    designation VARCHAR(64) NOT NULL,
    department VARCHAR(64) NOT NULL,
    employment_type VARCHAR(32) DEFAULT 'FULL_TIME',
    status VARCHAR(32) DEFAULT 'ACTIVE',
    basic_salary NUMERIC(12,2) DEFAULT 0.00,
    date_of_joining DATE NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_emp_tenant_code UNIQUE (tenant_id, employee_code)
);

CREATE TABLE IF NOT EXISTS staff.designations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(64) NOT NULL,
    designation_code VARCHAR(64) NOT NULL,
    title VARCHAR(128) NOT NULL,
    department VARCHAR(64) NOT NULL,
    hierarchy_tier INT DEFAULT 3,
    reports_to_designation_code VARCHAR(64),
    base_role VARCHAR(64) DEFAULT 'ROLE_TEACHER',
    financial_approval_limit NUMERIC(12,2) DEFAULT 0.00,
    default_cpc_level VARCHAR(32),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_desig_tenant_code UNIQUE (tenant_id, designation_code)
);

-- 7. ATTENDANCE SCHEMA
CREATE TABLE IF NOT EXISTS attendance.attendance_sessions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(64) NOT NULL,
    class_name VARCHAR(32) NOT NULL,
    section_name VARCHAR(16) NOT NULL,
    session_date DATE NOT NULL,
    submitted_by VARCHAR(128) NOT NULL,
    status VARCHAR(32) DEFAULT 'LOCKED',
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_att_session UNIQUE (tenant_id, class_name, section_name, session_date)
);

CREATE TABLE IF NOT EXISTS attendance.attendance_records (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(64) NOT NULL,
    session_id UUID NOT NULL REFERENCES attendance.attendance_sessions(id) ON DELETE CASCADE,
    student_id UUID NOT NULL,
    status VARCHAR(32) NOT NULL,
    remarks VARCHAR(255),
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- 8. EXAMINATIONS SCHEMA
CREATE TABLE IF NOT EXISTS examinations.student_marks (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(64) NOT NULL,
    student_id UUID NOT NULL,
    student_name VARCHAR(128) NOT NULL,
    exam_name VARCHAR(64) NOT NULL,
    subject_name VARCHAR(64) NOT NULL,
    theory_marks NUMERIC(5,2) DEFAULT 0.00,
    practical_marks NUMERIC(5,2) DEFAULT 0.00,
    internal_marks NUMERIC(5,2) DEFAULT 0.00,
    total_marks NUMERIC(5,2) NOT NULL,
    grade VARCHAR(8),
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- 9. FINANCE SCHEMA
CREATE TABLE IF NOT EXISTS finance.invoices (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(64) NOT NULL,
    invoice_number VARCHAR(64) NOT NULL,
    student_id UUID NOT NULL,
    academic_year VARCHAR(32) NOT NULL,
    term VARCHAR(32) NOT NULL,
    total_amount NUMERIC(12,2) NOT NULL,
    paid_amount NUMERIC(12,2) DEFAULT 0.00,
    balance_amount NUMERIC(12,2) NOT NULL,
    status VARCHAR(32) DEFAULT 'ISSUED',
    due_date DATE NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_inv_tenant_num UNIQUE (tenant_id, invoice_number)
);

CREATE TABLE IF NOT EXISTS finance.general_ledger_entries (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(64) NOT NULL,
    account_code VARCHAR(32) NOT NULL,
    account_name VARCHAR(128) NOT NULL,
    debit_amount NUMERIC(12,2) DEFAULT 0.00,
    credit_amount NUMERIC(12,2) DEFAULT 0.00,
    description VARCHAR(255) NOT NULL,
    reference_id VARCHAR(64),
    transaction_date DATE NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- 10. SYSTEM SCHEMA
CREATE TABLE IF NOT EXISTS system.audit_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(64) NOT NULL,
    actor_name VARCHAR(128) NOT NULL,
    actor_role VARCHAR(64) NOT NULL,
    action VARCHAR(64) NOT NULL,
    resource_type VARCHAR(64) NOT NULL,
    resource_id VARCHAR(64),
    details TEXT,
    client_ip VARCHAR(64),
    status VARCHAR(32) DEFAULT 'SUCCESS',
    timestamp TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS system.tenant_modules (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(64) NOT NULL,
    module_key VARCHAR(64) NOT NULL,
    module_name VARCHAR(128) NOT NULL,
    tier VARCHAR(32) DEFAULT 'STARTER',
    is_enabled BOOLEAN DEFAULT TRUE,
    expires_at TIMESTAMPTZ NOT NULL,
    updated_by VARCHAR(128),
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_tenant_module UNIQUE (tenant_id, module_key)
);

CREATE TABLE IF NOT EXISTS system.tenant_feature_flags (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(64) NOT NULL,
    feature_key VARCHAR(64) NOT NULL,
    feature_name VARCHAR(128) NOT NULL,
    category VARCHAR(64) NOT NULL,
    is_enabled BOOLEAN DEFAULT TRUE,
    tier VARCHAR(32) DEFAULT 'CORE',
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_tenant_feature UNIQUE (tenant_id, feature_key)
);

CREATE TABLE IF NOT EXISTS system.custom_field_definitions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(64) NOT NULL,
    target_entity_type VARCHAR(64) NOT NULL,
    field_code VARCHAR(64) NOT NULL,
    field_label VARCHAR(128) NOT NULL,
    field_type VARCHAR(32) NOT NULL,
    options_json TEXT,
    default_value VARCHAR(255),
    validation_regex VARCHAR(255),
    is_mandatory BOOLEAN DEFAULT FALSE,
    display_order INT DEFAULT 1,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS system.custom_field_values (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(64) NOT NULL,
    target_entity_type VARCHAR(64) NOT NULL,
    target_entity_id UUID NOT NULL,
    field_code VARCHAR(64) NOT NULL,
    field_definition_id UUID NOT NULL,
    field_value TEXT,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS system.custom_entities (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(64) NOT NULL,
    entity_code VARCHAR(64) NOT NULL,
    display_name VARCHAR(128) NOT NULL,
    plural_name VARCHAR(128) NOT NULL,
    description TEXT,
    category VARCHAR(64) DEFAULT 'SPECIALTY',
    icon_name VARCHAR(64) DEFAULT 'Layers',
    fields_schema_json TEXT NOT NULL,
    is_published BOOLEAN DEFAULT TRUE,
    record_count INT DEFAULT 0,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS system.custom_entity_records (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(64) NOT NULL,
    custom_entity_definition_id UUID NOT NULL,
    entity_code VARCHAR(64) NOT NULL,
    record_title VARCHAR(255) NOT NULL,
    record_data_json TEXT NOT NULL,
    status VARCHAR(32) DEFAULT 'ACTIVE',
    created_by_name VARCHAR(128) NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS system.tenant_webhook_subscriptions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(64) NOT NULL,
    webhook_name VARCHAR(128) NOT NULL,
    target_url VARCHAR(512) NOT NULL,
    event_topic VARCHAR(64) NOT NULL,
    secret_key VARCHAR(128) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    retry_limit INT DEFAULT 3,
    timeout_seconds INT DEFAULT 5,
    successful_dispatches BIGINT DEFAULT 0,
    failed_dispatches BIGINT DEFAULT 0,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    last_dispatched_at TIMESTAMPTZ
);

-- CREATE PERFORMANCE INDEXES
CREATE INDEX IF NOT EXISTS idx_student_tenant ON students.students(tenant_id, class_name, section_name);
CREATE INDEX IF NOT EXISTS idx_attendance_session ON attendance.attendance_records(session_id);
CREATE INDEX IF NOT EXISTS idx_ledger_account ON finance.general_ledger_entries(tenant_id, account_code);
CREATE INDEX IF NOT EXISTS idx_audit_tenant_time ON system.audit_logs(tenant_id, timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_custom_field_vals ON system.custom_field_values(tenant_id, target_entity_type, target_entity_id);
CREATE INDEX IF NOT EXISTS idx_custom_records ON system.custom_entity_records(tenant_id, entity_code, status);

