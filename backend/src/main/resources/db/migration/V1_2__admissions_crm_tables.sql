-- ============================================================================
-- V1_2: Admissions CRM, Rapid Walk-In Desk, Interview Rubrics & Merit Offers
-- ============================================================================

CREATE TABLE IF NOT EXISTS admissions.admission_inquiries (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(100) NOT NULL,
    inquiry_number VARCHAR(100) NOT NULL,
    token_number VARCHAR(50),
    parent_name VARCHAR(255) NOT NULL,
    parent_phone VARCHAR(50) NOT NULL,
    parent_email VARCHAR(255),
    student_name VARCHAR(255) NOT NULL,
    grade_applying_for VARCHAR(100) NOT NULL,
    academic_year VARCHAR(50) NOT NULL,
    pipeline_stage VARCHAR(100) NOT NULL,
    lead_score INT NOT NULL DEFAULT 20,
    source_channel VARCHAR(100) NOT NULL,
    has_enrolled_sibling BOOLEAN NOT NULL DEFAULT FALSE,
    proximity_under_five_km BOOLEAN NOT NULL DEFAULT FALSE,
    assigned_counselor_name VARCHAR(255),
    counselor_notes TEXT,
    spot_fee_paid_amount NUMERIC(12, 2) NOT NULL DEFAULT 0.00,
    spot_fee_receipt_number VARCHAR(100),
    drop_reason VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS admissions.admission_interview_slots (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(100) NOT NULL,
    inquiry_id UUID NOT NULL,
    slot_type VARCHAR(100) NOT NULL,
    scheduled_at TIMESTAMP WITH TIME ZONE NOT NULL,
    interviewer_name VARCHAR(255),
    venue_or_room VARCHAR(255),
    status VARCHAR(50) NOT NULL DEFAULT 'SCHEDULED',
    evaluation_rating INT,
    interviewer_feedback TEXT,
    recommended_grade VARCHAR(100),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS admissions.admission_merit_offers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(100) NOT NULL,
    inquiry_id UUID NOT NULL,
    offer_number VARCHAR(100) NOT NULL,
    student_name VARCHAR(255) NOT NULL,
    grade_offered VARCHAR(100) NOT NULL,
    academic_year VARCHAR(50) NOT NULL,
    tuition_fee_total NUMERIC(12, 2) NOT NULL DEFAULT 0.00,
    scholarship_or_concession_amount NUMERIC(12, 2) NOT NULL DEFAULT 0.00,
    net_admission_payable NUMERIC(12, 2) NOT NULL DEFAULT 0.00,
    valid_until_date DATE,
    offer_status VARCHAR(50) NOT NULL DEFAULT 'ISSUED',
    converted_student_id UUID,
    converted_invoice_id UUID,
    issued_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    accepted_at TIMESTAMP WITH TIME ZONE
);

CREATE INDEX IF NOT EXISTS idx_inquiries_tenant_stage ON admissions.admission_inquiries(tenant_id, pipeline_stage);
CREATE INDEX IF NOT EXISTS idx_slots_inquiry ON admissions.admission_interview_slots(tenant_id, inquiry_id);
CREATE INDEX IF NOT EXISTS idx_offers_inquiry ON admissions.admission_merit_offers(tenant_id, inquiry_id);

