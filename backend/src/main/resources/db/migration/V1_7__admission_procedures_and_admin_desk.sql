-- V1_7__admission_procedures_and_admin_desk.sql
-- Schema: admissions

CREATE TABLE IF NOT EXISTS admissions.admission_queue_tokens (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(64) NOT NULL,
    token_number VARCHAR(64) NOT NULL,
    category VARCHAR(32) NOT NULL,
    visitor_name VARCHAR(255) NOT NULL,
    visitor_phone VARCHAR(64) NOT NULL,
    student_name VARCHAR(255),
    grade_applying VARCHAR(64),
    status VARCHAR(32) NOT NULL DEFAULT 'WAITING',
    assigned_counter VARCHAR(32),
    assigned_officer_id UUID,
    waiting_minutes INT DEFAULT 0,
    service_minutes INT DEFAULT 0,
    notes TEXT,
    called_at TIMESTAMPTZ,
    completed_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_admission_queue_tenant_status ON admissions.admission_queue_tokens(tenant_id, status);
CREATE INDEX IF NOT EXISTS idx_admission_queue_token_num ON admissions.admission_queue_tokens(token_number);

CREATE TABLE IF NOT EXISTS admissions.prospectus_kit_masters (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(64) NOT NULL,
    serial_barcode VARCHAR(64) NOT NULL UNIQUE,
    academic_year VARCHAR(32) NOT NULL,
    price NUMERIC(12, 2) NOT NULL,
    status VARCHAR(32) NOT NULL DEFAULT 'IN_STOCK',
    buyer_parent_name VARCHAR(255),
    buyer_phone VARCHAR(64),
    payment_mode VARCHAR(32),
    transaction_ref VARCHAR(128),
    sold_by_user_id UUID,
    sold_at TIMESTAMPTZ,
    linked_inquiry_id UUID,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_prospectus_tenant_status ON admissions.prospectus_kit_masters(tenant_id, status);
CREATE INDEX IF NOT EXISTS idx_prospectus_barcode ON admissions.prospectus_kit_masters(serial_barcode);

CREATE TABLE IF NOT EXISTS admissions.admission_document_undertakings (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(64) NOT NULL,
    undertaking_number VARCHAR(64) NOT NULL UNIQUE,
    application_id UUID NOT NULL,
    student_name VARCHAR(255) NOT NULL,
    parent_name VARCHAR(255) NOT NULL,
    missing_document_name VARCHAR(255) NOT NULL,
    reason_code VARCHAR(64) NOT NULL,
    deadline_date DATE NOT NULL,
    status VARCHAR(32) NOT NULL DEFAULT 'PENDING',
    verified_by_officer_id UUID NOT NULL,
    parent_signature_ack BOOLEAN NOT NULL DEFAULT TRUE,
    fulfillment_date DATE,
    fulfillment_remarks TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_undertaking_tenant_status ON admissions.admission_document_undertakings(tenant_id, status);
CREATE INDEX IF NOT EXISTS idx_undertaking_deadline ON admissions.admission_document_undertakings(deadline_date);

CREATE TABLE IF NOT EXISTS admissions.admission_welcome_dossiers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(64) NOT NULL,
    student_id UUID NOT NULL,
    admission_number VARCHAR(64) NOT NULL UNIQUE,
    student_name VARCHAR(255) NOT NULL,
    grade_name VARCHAR(64) NOT NULL,
    section_name VARCHAR(32) NOT NULL,
    roll_number VARCHAR(32) NOT NULL,
    house_name VARCHAR(64) NOT NULL,
    smart_card_rfid_uid VARCHAR(128),
    smart_card_status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE',
    canteen_initial_balance NUMERIC(12, 2) NOT NULL DEFAULT 0.00,
    welcome_pack_generated BOOLEAN NOT NULL DEFAULT TRUE,
    minted_by_user_id UUID NOT NULL,
    minted_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_welcome_dossier_tenant_student ON admissions.admission_welcome_dossiers(tenant_id, student_id);
CREATE INDEX IF NOT EXISTS idx_welcome_dossier_adm_no ON admissions.admission_welcome_dossiers(admission_number);

CREATE TABLE IF NOT EXISTS admissions.admission_seat_surrenders (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(64) NOT NULL,
    surrender_ref_number VARCHAR(64) NOT NULL UNIQUE,
    application_id UUID NOT NULL,
    student_name VARCHAR(255) NOT NULL,
    grade_name VARCHAR(64) NOT NULL,
    category_name VARCHAR(64) NOT NULL,
    surrender_reason VARCHAR(255) NOT NULL,
    surrender_date DATE NOT NULL,
    total_fees_paid NUMERIC(12, 2) NOT NULL,
    deduction_percentage NUMERIC(5, 2) NOT NULL,
    deduction_amount NUMERIC(12, 2) NOT NULL,
    refundable_amount NUMERIC(12, 2) NOT NULL,
    settlement_status VARCHAR(32) NOT NULL DEFAULT 'PENDING',
    waitlist_student_promoted_id UUID,
    general_ledger_voucher_id UUID,
    authorized_by_user_id UUID NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_seat_surrender_tenant_status ON admissions.admission_seat_surrenders(tenant_id, settlement_status);
