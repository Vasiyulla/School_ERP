-- ============================================================================
-- V1_4: Indian Statutory Quota Matrix, 100-Point Criteria, Entrance Tests & Scholarships
-- ============================================================================

-- 1. Criteria Evaluation Points (DoE 100-Point Formula)
CREATE TABLE IF NOT EXISTS admissions.admission_point_criteria (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(100) NOT NULL,
    inquiry_id UUID NOT NULL,
    distance_km NUMERIC(5, 2) DEFAULT 0.00,
    distance_points INT DEFAULT 0,
    has_sibling BOOLEAN DEFAULT FALSE,
    sibling_points INT DEFAULT 0,
    sibling_student_id UUID,
    has_alumni_parent BOOLEAN DEFAULT FALSE,
    alumni_points INT DEFAULT 0,
    alumni_passing_year INT,
    is_staff_ward BOOLEAN DEFAULT FALSE,
    staff_ward_points INT DEFAULT 0,
    staff_employee_id UUID,
    is_single_girl_child BOOLEAN DEFAULT FALSE,
    girl_child_points INT DEFAULT 0,
    is_first_born BOOLEAN DEFAULT FALSE,
    first_born_points INT DEFAULT 0,
    total_calculated_points INT DEFAULT 0,
    tie_breaker_draw_number INT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_tenant_inquiry_criteria UNIQUE (tenant_id, inquiry_id)
);

-- 2. Statutory Certificates Desk (Caste SC/ST/OBC-NCL, EWS, CWSN/RPwD, Affidavits)
CREATE TABLE IF NOT EXISTS admissions.statutory_certificate_records (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(100) NOT NULL,
    inquiry_id UUID NOT NULL,
    certificate_type VARCHAR(100) NOT NULL,
    certificate_number VARCHAR(100) NOT NULL,
    issuing_authority VARCHAR(255) NOT NULL,
    issue_date DATE,
    valid_financial_year VARCHAR(50),
    verified_status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    verification_remarks TEXT,
    verified_by VARCHAR(255),
    verified_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 3. Entrance Assessments & Diagnostic Hall Tickets
CREATE TABLE IF NOT EXISTS admissions.admission_entrance_tests (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(100) NOT NULL,
    inquiry_id UUID NOT NULL,
    test_code VARCHAR(100) NOT NULL,
    test_date TIMESTAMP WITH TIME ZONE NOT NULL,
    exam_hall VARCHAR(100) NOT NULL,
    reporting_time VARCHAR(50) NOT NULL,
    math_score NUMERIC(5, 2) DEFAULT 0.00,
    english_score NUMERIC(5, 2) DEFAULT 0.00,
    science_score NUMERIC(5, 2) DEFAULT 0.00,
    aptitude_score NUMERIC(5, 2) DEFAULT 0.00,
    total_score NUMERIC(5, 2) DEFAULT 0.00,
    maximum_score NUMERIC(5, 2) DEFAULT 100.00,
    percentage NUMERIC(5, 2) DEFAULT 0.00,
    qualifying_cutoff_percentage NUMERIC(5, 2) DEFAULT 60.00,
    result_status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    evaluator_remarks TEXT,
    admit_card_token VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_tenant_test_code UNIQUE (tenant_id, test_code)
);

-- 4. Scholarship & Financial Aid Committee Applications
CREATE TABLE IF NOT EXISTS admissions.admission_scholarship_applications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(100) NOT NULL,
    inquiry_id UUID NOT NULL,
    scholarship_type VARCHAR(100) NOT NULL,
    claimed_achievement_or_income TEXT,
    requested_concession_percentage NUMERIC(5, 2) DEFAULT 0.00,
    committee_status VARCHAR(50) NOT NULL DEFAULT 'SUBMITTED',
    approved_concession_percentage NUMERIC(5, 2) DEFAULT 0.00,
    approved_amount NUMERIC(12, 2) DEFAULT 0.00,
    committee_reviewer_notes TEXT,
    reviewed_by VARCHAR(255),
    decided_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 5. Extend Grade Seat Capacities with Indian Quota Breakdown
ALTER TABLE admissions.grade_seat_capacities ADD COLUMN IF NOT EXISTS obc_quota_seats INT DEFAULT 0;
ALTER TABLE admissions.grade_seat_capacities ADD COLUMN IF NOT EXISTS sc_quota_seats INT DEFAULT 0;
ALTER TABLE admissions.grade_seat_capacities ADD COLUMN IF NOT EXISTS st_quota_seats INT DEFAULT 0;
ALTER TABLE admissions.grade_seat_capacities ADD COLUMN IF NOT EXISTS cwsn_quota_seats INT DEFAULT 0;
ALTER TABLE admissions.grade_seat_capacities ADD COLUMN IF NOT EXISTS staff_ward_quota_seats INT DEFAULT 0;
ALTER TABLE admissions.grade_seat_capacities ADD COLUMN IF NOT EXISTS single_girl_child_quota_seats INT DEFAULT 0;

-- 6. Extend Admission Inquiries with Social Category and Indian Quota
ALTER TABLE admissions.admission_inquiries ADD COLUMN IF NOT EXISTS social_category VARCHAR(50) DEFAULT 'GENERAL';
ALTER TABLE admissions.admission_inquiries ADD COLUMN IF NOT EXISTS admission_quota VARCHAR(50) DEFAULT 'OPEN_GENERAL';
ALTER TABLE admissions.admission_inquiries ADD COLUMN IF NOT EXISTS sibling_student_id UUID;

-- 7. Extend Merit Offers with Parent Digital Acceptance & Seat Lock Deposit
ALTER TABLE admissions.admission_merit_offers ADD COLUMN IF NOT EXISTS seat_lock_deposit_amount NUMERIC(12, 2) DEFAULT 0.00;
ALTER TABLE admissions.admission_merit_offers ADD COLUMN IF NOT EXISTS parent_signature_name VARCHAR(255);
ALTER TABLE admissions.admission_merit_offers ADD COLUMN IF NOT EXISTS parent_ip_address VARCHAR(100);
ALTER TABLE admissions.admission_merit_offers ADD COLUMN IF NOT EXISTS deposit_transaction_id VARCHAR(100);
ALTER TABLE admissions.admission_merit_offers ADD COLUMN IF NOT EXISTS deposit_status VARCHAR(50) DEFAULT 'PENDING';

-- Indices
CREATE INDEX IF NOT EXISTS idx_point_criteria_inquiry ON admissions.admission_point_criteria(tenant_id, inquiry_id);
CREATE INDEX IF NOT EXISTS idx_statutory_cert_inquiry ON admissions.statutory_certificate_records(tenant_id, inquiry_id);
CREATE INDEX IF NOT EXISTS idx_entrance_test_inquiry ON admissions.admission_entrance_tests(tenant_id, inquiry_id);
CREATE INDEX IF NOT EXISTS idx_entrance_test_token ON admissions.admission_entrance_tests(tenant_id, admit_card_token);
CREATE INDEX IF NOT EXISTS idx_scholarship_inquiry ON admissions.admission_scholarship_applications(tenant_id, inquiry_id);

