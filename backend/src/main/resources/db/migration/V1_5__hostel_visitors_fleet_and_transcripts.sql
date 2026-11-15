-- ============================================================================
-- V1_5: Hostel & Boarding Operations, Campus Visitors VMS, Fleet Fuel & Official Transcripts
-- ============================================================================

-- 1. Hostel Rooms Inventory
CREATE TABLE IF NOT EXISTS operations.hostel_rooms (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(100) NOT NULL,
    block_name VARCHAR(100) NOT NULL,
    floor_number INT NOT NULL,
    room_number VARCHAR(50) NOT NULL,
    room_type VARCHAR(50) NOT NULL DEFAULT 'NON_AC',
    total_beds INT NOT NULL DEFAULT 4,
    occupied_beds INT NOT NULL DEFAULT 0,
    annual_fee NUMERIC(12, 2) DEFAULT 80000.00,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_tenant_block_room UNIQUE (tenant_id, block_name, room_number)
);

-- 2. Hostel Student Bed Allocations
CREATE TABLE IF NOT EXISTS operations.hostel_allocations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(100) NOT NULL,
    room_id UUID NOT NULL,
    student_id UUID NOT NULL,
    bed_number INT NOT NULL,
    academic_year VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    allocated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    vacated_at TIMESTAMP WITH TIME ZONE
);

-- 3. Hostel Roll Call Attendance (Morning & Night Lights-Out)
CREATE TABLE IF NOT EXISTS operations.hostel_roll_calls (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(100) NOT NULL,
    allocation_id UUID NOT NULL,
    student_id UUID NOT NULL,
    roll_call_date DATE NOT NULL,
    session_type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PRESENT',
    remarks TEXT,
    warden_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 4. Hostel Exeat / Outstation Night-Out Passes
CREATE TABLE IF NOT EXISTS operations.hostel_exeat_passes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(100) NOT NULL,
    student_id UUID NOT NULL,
    pass_number VARCHAR(100) NOT NULL,
    exeat_type VARCHAR(50) NOT NULL,
    departure_time TIMESTAMP WITH TIME ZONE NOT NULL,
    expected_return_time TIMESTAMP WITH TIME ZONE NOT NULL,
    actual_return_time TIMESTAMP WITH TIME ZONE,
    parent_authorized BOOLEAN DEFAULT TRUE,
    parent_contact_phone VARCHAR(50),
    status VARCHAR(50) NOT NULL DEFAULT 'APPROVED',
    gate_departure_scanned_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_tenant_exeat_number UNIQUE (tenant_id, pass_number)
);

-- 5. Campus Visitors & Gate Reception Desk (VMS)
CREATE TABLE IF NOT EXISTS operations.campus_visitors (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(100) NOT NULL,
    visitor_pass_number VARCHAR(100) NOT NULL,
    visitor_name VARCHAR(255) NOT NULL,
    visitor_phone VARCHAR(50) NOT NULL,
    id_proof_type VARCHAR(50) NOT NULL,
    id_proof_number VARCHAR(100) NOT NULL,
    purpose_of_visit VARCHAR(255) NOT NULL,
    host_staff_id UUID,
    host_staff_name VARCHAR(255) NOT NULL,
    host_approval_status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    entry_gate VARCHAR(100) NOT NULL,
    badge_qr_token VARCHAR(255) NOT NULL,
    checked_in_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    checked_out_at TIMESTAMP WITH TIME ZONE,
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE_ON_CAMPUS',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_tenant_visitor_pass UNIQUE (tenant_id, visitor_pass_number)
);

-- 6. Vehicle Fleet Fuel Log & Pilferage Sentinel
CREATE TABLE IF NOT EXISTS operations.vehicle_fuel_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(100) NOT NULL,
    vehicle_id UUID NOT NULL,
    vehicle_registration VARCHAR(50) NOT NULL,
    fill_date DATE NOT NULL,
    fuel_litres NUMERIC(6, 2) NOT NULL,
    fuel_rate_per_litre NUMERIC(6, 2) NOT NULL,
    total_cost NUMERIC(10, 2) NOT NULL,
    odometer_reading INT NOT NULL,
    previous_odometer_reading INT NOT NULL,
    kilometers_run INT NOT NULL,
    mileage_km_per_litre NUMERIC(5, 2) NOT NULL,
    fuel_station_name VARCHAR(255) NOT NULL,
    driver_name VARCHAR(255) NOT NULL,
    pilferage_alert BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 7. Vehicle Compliance Sentinel (RTO Fitness, Insurance, PUC)
CREATE TABLE IF NOT EXISTS operations.vehicle_compliance_sentinels (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(100) NOT NULL,
    vehicle_id UUID NOT NULL,
    vehicle_registration VARCHAR(50) NOT NULL,
    rto_fitness_expiry DATE NOT NULL,
    insurance_policy_expiry DATE NOT NULL,
    puc_certificate_expiry DATE NOT NULL,
    road_tax_expiry DATE NOT NULL,
    speed_governor_cert_expiry DATE,
    compliance_status VARCHAR(50) NOT NULL DEFAULT 'COMPLIANT',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_tenant_vehicle_compliance UNIQUE (tenant_id, vehicle_id)
);

-- 8. Official Multi-Year Cumulative Transcripts
CREATE TABLE IF NOT EXISTS students.official_transcripts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(100) NOT NULL,
    student_id UUID NOT NULL,
    transcript_number VARCHAR(100) NOT NULL,
    academic_stream VARCHAR(100) NOT NULL,
    cumulative_gpa NUMERIC(4, 2) NOT NULL,
    cumulative_percentage NUMERIC(5, 2) NOT NULL,
    total_credits INT NOT NULL DEFAULT 40,
    medium_of_instruction VARCHAR(100) NOT NULL DEFAULT 'English',
    verification_hash VARCHAR(255) NOT NULL,
    issued_date DATE NOT NULL,
    issued_by_principal VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_tenant_transcript_number UNIQUE (tenant_id, transcript_number)
);

-- Indices
CREATE INDEX IF NOT EXISTS idx_hostel_alloc_student ON operations.hostel_allocations(tenant_id, student_id);
CREATE INDEX IF NOT EXISTS idx_hostel_rollcall_date ON operations.hostel_roll_calls(tenant_id, roll_call_date, session_type);
CREATE INDEX IF NOT EXISTS idx_hostel_exeat_student ON operations.hostel_exeat_passes(tenant_id, student_id);
CREATE INDEX IF NOT EXISTS idx_visitors_status ON operations.campus_visitors(tenant_id, status);
CREATE INDEX IF NOT EXISTS idx_fuel_vehicle ON operations.vehicle_fuel_logs(tenant_id, vehicle_id);
CREATE INDEX IF NOT EXISTS idx_transcripts_student ON students.official_transcripts(tenant_id, student_id);

