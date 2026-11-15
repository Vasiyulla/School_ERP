-- ============================================================================
-- V1_3: Admissions Parent Portal Checklist, Seat Capacities & Counselor Tasks
-- ============================================================================

CREATE TABLE IF NOT EXISTS admissions.admission_documents_checklist (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(100) NOT NULL,
    inquiry_id UUID NOT NULL,
    document_type VARCHAR(100) NOT NULL,
    document_name VARCHAR(255) NOT NULL,
    storage_url TEXT,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING_UPLOAD',
    rejection_reason TEXT,
    verified_by_name VARCHAR(255),
    verified_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS admissions.grade_seat_capacities (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(100) NOT NULL,
    academic_year VARCHAR(50) NOT NULL,
    grade_name VARCHAR(100) NOT NULL,
    total_sanctioned_seats INT NOT NULL,
    general_quota_seats INT NOT NULL DEFAULT 0,
    rte_quota_seats INT NOT NULL DEFAULT 0,
    sibling_staff_quota_seats INT NOT NULL DEFAULT 0,
    management_quota_seats INT NOT NULL DEFAULT 0,
    enrolled_count INT NOT NULL DEFAULT 0,
    active_offers_count INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_tenant_year_grade UNIQUE (tenant_id, academic_year, grade_name)
);

CREATE TABLE IF NOT EXISTS admissions.counselor_tasks (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(100) NOT NULL,
    inquiry_id UUID NOT NULL,
    counselor_name VARCHAR(255) NOT NULL,
    task_type VARCHAR(100) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    due_date TIMESTAMP WITH TIME ZONE NOT NULL,
    priority VARCHAR(50) NOT NULL DEFAULT 'NORMAL',
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    outcome_notes TEXT,
    completed_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_checklist_inquiry ON admissions.admission_documents_checklist(tenant_id, inquiry_id);
CREATE INDEX IF NOT EXISTS idx_capacities_year_grade ON admissions.grade_seat_capacities(tenant_id, academic_year, grade_name);
CREATE INDEX IF NOT EXISTS idx_counselor_tasks_status ON admissions.counselor_tasks(tenant_id, counselor_name, status);

