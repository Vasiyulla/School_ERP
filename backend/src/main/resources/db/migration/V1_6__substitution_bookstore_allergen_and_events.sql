-- ==============================================================================
-- Migration V1_6: Morning Substitution, Anti-Cheating Exam Invigilation,
-- Bookstore/Uniform POS, Student Health Passports & Event Costume Rentals
-- ==============================================================================

-- 1. Academics: Daily Morning Teacher Substitution Records
CREATE TABLE IF NOT EXISTS academics.daily_substitutions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(64) NOT NULL,
    substitution_date DATE NOT NULL,
    period_number INT NOT NULL,
    classroom_id UUID NOT NULL,
    class_name VARCHAR(64) NOT NULL,
    section_name VARCHAR(16) NOT NULL,
    absent_teacher_id UUID NOT NULL,
    absent_teacher_name VARCHAR(128) NOT NULL,
    substitute_teacher_id UUID NOT NULL,
    substitute_teacher_name VARCHAR(128) NOT NULL,
    subject_name VARCHAR(64) NOT NULL,
    lesson_handoff_brief TEXT,
    status VARCHAR(32) DEFAULT 'ASSIGNED', -- ASSIGNED, NOTIFIED, COMPLETED, CANCELLED
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_daily_sub_tenant_date ON academics.daily_substitutions (tenant_id, substitution_date);
CREATE INDEX IF NOT EXISTS idx_daily_sub_teacher ON academics.daily_substitutions (tenant_id, substitute_teacher_id);

-- 2. Examinations: Exam Invigilation Duty Roster & Anti-Cheating Subject Conflict Guard
CREATE TABLE IF NOT EXISTS examinations.exam_invigilation_duties (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(64) NOT NULL,
    exam_schedule_id UUID NOT NULL,
    exam_name VARCHAR(128) NOT NULL,
    subject_name VARCHAR(64) NOT NULL,
    exam_date DATE NOT NULL,
    hall_room_number VARCHAR(32) NOT NULL,
    invigilator_staff_id UUID NOT NULL,
    invigilator_staff_name VARCHAR(128) NOT NULL,
    duty_role VARCHAR(32) DEFAULT 'PRIMARY_INVIGILATOR', -- PRIMARY_INVIGILATOR, RELIEVER, FLYING_SQUAD
    paper_seal_verified BOOLEAN DEFAULT TRUE,
    seal_verification_timestamp TIMESTAMPTZ,
    witness_staff_name VARCHAR(128),
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_invig_tenant_date ON examinations.exam_invigilation_duties (tenant_id, exam_date);

-- 3. Examinations: Unfair Means (UFM) Malpractice Case Dockets
CREATE TABLE IF NOT EXISTS examinations.ufm_malpractice_dockets (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(64) NOT NULL,
    docket_number VARCHAR(64) NOT NULL,
    exam_schedule_id UUID NOT NULL,
    student_id UUID NOT NULL,
    student_name VARCHAR(128) NOT NULL,
    admission_number VARCHAR(64) NOT NULL,
    subject_name VARCHAR(64) NOT NULL,
    incident_timestamp TIMESTAMPTZ NOT NULL,
    contraband_type VARCHAR(64) NOT NULL, -- UNAUTHORIZED_NOTES, MOBILE_DEVICE, SMART_WATCH, IMPERSONATION
    invigilator_remarks TEXT NOT NULL,
    student_statement TEXT,
    verdict_status VARCHAR(32) DEFAULT 'UNDER_COMMITTEE_REVIEW', -- UNDER_COMMITTEE_REVIEW, WARNING_ISSUED, PAPER_CANCELLED, EXPELLED
    verdict_date DATE,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_tenant_ufm_docket UNIQUE (tenant_id, docket_number)
);

-- 4. Operations: Bookstore & Uniform SKU Master
CREATE TABLE IF NOT EXISTS operations.bookstore_inventory_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(64) NOT NULL,
    sku_code VARCHAR(64) NOT NULL,
    item_category VARCHAR(32) NOT NULL, -- TEXTBOOK_BUNDLE, NOTEBOOK_PACK, UNIFORM_SUMMER, UNIFORM_WINTER, UNIFORM_SPORTS, ACCESSORY
    item_name VARCHAR(128) NOT NULL,
    target_grade VARCHAR(32),
    size_specification VARCHAR(32), -- e.g. "Size 32", "Size 36", "Standard Pack"
    unit_selling_price NUMERIC(12, 2) NOT NULL,
    cost_price NUMERIC(12, 2) NOT NULL,
    stock_quantity INT DEFAULT 0,
    reorder_threshold INT DEFAULT 10,
    supplier_vendor_name VARCHAR(128),
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_tenant_bookstore_sku UNIQUE (tenant_id, sku_code)
);

-- 5. Operations: Bookstore & Uniform POS Sales Transactions
CREATE TABLE IF NOT EXISTS operations.bookstore_sales_orders (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(64) NOT NULL,
    order_receipt_number VARCHAR(64) NOT NULL,
    student_id UUID,
    student_name VARCHAR(128),
    admission_number VARCHAR(64),
    payment_mode VARCHAR(32) NOT NULL, -- CASH, UPI, STUDENT_LEDGER_DEBIT
    total_amount NUMERIC(12, 2) NOT NULL,
    tendered_amount NUMERIC(12, 2),
    items_json TEXT NOT NULL, -- JSON array of itemized purchase lines
    cashier_staff_name VARCHAR(128) NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_tenant_bookstore_receipt UNIQUE (tenant_id, order_receipt_number)
);

-- 6. Students: Critical Medical Emergency Passports & Allergen Register
CREATE TABLE IF NOT EXISTS students.student_health_passports (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(64) NOT NULL,
    student_id UUID NOT NULL,
    blood_group VARCHAR(16) NOT NULL,
    has_severe_allergies BOOLEAN DEFAULT FALSE,
    allergen_list TEXT, -- Comma-separated: "PEANUTS,GLUTEN,DAIRY"
    chronic_conditions TEXT, -- "ASTHMA,TYPE_1_DIABETES"
    inhaler_in_infirmary BOOLEAN DEFAULT FALSE,
    epipen_prescribed BOOLEAN DEFAULT FALSE,
    emergency_contact_phone VARCHAR(32) NOT NULL,
    treating_physician_name VARCHAR(128),
    paramedic_emergency_protocol TEXT NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_tenant_student_passport UNIQUE (tenant_id, student_id)
);

-- 7. Operations: Campus Event Production & Costume Rental Vault
CREATE TABLE IF NOT EXISTS operations.event_costume_rentals (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(64) NOT NULL,
    event_id UUID,
    event_name VARCHAR(128) NOT NULL,
    costume_code VARCHAR(64) NOT NULL,
    costume_description VARCHAR(128) NOT NULL,
    vendor_supplier_name VARCHAR(128) NOT NULL,
    assigned_student_id UUID,
    assigned_student_name VARCHAR(128),
    rental_rate_per_day NUMERIC(12, 2) DEFAULT 0,
    issued_date DATE,
    expected_return_date DATE,
    actual_return_date DATE,
    return_condition VARCHAR(32) DEFAULT 'ISSUED_OUT', -- ISSUED_OUT, RETURNED_PRISTINE, RETURNED_DAMAGED, LOST
    damage_fine_amount NUMERIC(12, 2) DEFAULT 0,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_costume_tenant_event ON operations.event_costume_rentals (tenant_id, event_name);
