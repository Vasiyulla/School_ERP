import { apiClient } from "../api-client";

export interface StudentListItem {
  id: string;
  admissionNumber: string;
  firstName: string;
  lastName: string;
  currentGradeLevel: string;
  currentSection: string;
  status: string;
  guardianName?: string;
  guardianPhone?: string;
  guardianEmail?: string;
  dob?: string;
  gender?: string;
  houseName?: string;
}

export interface DocumentLockerItem {
  id: string;
  studentId: string;
  documentType: string;
  originalFileName: string;
  custodyLocation: string;
  sha256Checksum: string;
  status: string;
  createdAt: string;
}

export interface VehicleTelemetryItem {
  vehicleId: string;
  vehicleNumber: string;
  driverName: string;
  driverPhone: string;
  latitude: number;
  longitude: number;
  speedKmph: number;
  status: string;
  etaMinutes: number;
  routeNumber: string;
  distanceKm: number;
}

export interface AuditLogItem {
  id: string;
  actorEmail: string;
  actorRole: string;
  action: string;
  targetEntity: string;
  targetEntityId: string;
  details: string;
  ipAddress: string;
  timestamp: string;
}

export interface HouseScoreItem {
  id: string;
  name: string;
  colorHex: string;
  totalPoints: number;
  rank: number;
  motto: string;
}

export interface GatePassItem {
  id: string;
  passCode: string;
  visitorName: string;
  visitorPhone: string;
  purpose: string;
  hostName: string;
  status: string;
  expiresAt: string;
  qrPayload: string;
}

export const adminService = {
  // Students & Admissions
  async getStudents(): Promise<StudentListItem[]> {
    const res = await apiClient.get<StudentListItem[]>("/api/v1/students");
    return res.data || [];
  },

  async createStudent(payload: {
    admissionNumber: string;
    firstName: string;
    lastName: string;
    dob: string;
    gender: string;
    currentGradeLevel: string;
    currentSection: string;
    guardianName: string;
    guardianPhone: string;
    guardianEmail: string;
  }): Promise<StudentListItem> {
    const res = await apiClient.post<StudentListItem>("/api/v1/students", payload);
    return res.data;
  },

  // Document Locker Custody
  async getStudentDocuments(studentId: string): Promise<DocumentLockerItem[]> {
    const res = await apiClient.get<DocumentLockerItem[]>(
      `/api/v1/students/${studentId}/documents`
    );
    return res.data || [];
  },

  async depositDocument(
    studentId: string,
    payload: {
      documentType: string;
      originalFileName: string;
      fileSizeBytes: number;
      custodyLocation: string;
      fileData?: string;
    }
  ): Promise<DocumentLockerItem> {
    const res = await apiClient.post<DocumentLockerItem>(
      `/api/v1/students/${studentId}/documents`,
      payload
    );
    return res.data;
  },

  // Multi-Department TC Clearance & Transfer Certificates
  async evaluateClearance(studentId: string): Promise<any> {
    const res = await apiClient.get(`/api/v1/students/${studentId}/clearance/evaluate`);
    return res.data;
  },

  async signOffClearance(
    studentId: string,
    payload: {
      department: string;
      approvedBy: string;
      remarks: string;
    }
  ): Promise<any> {
    const res = await apiClient.post(
      `/api/v1/students/${studentId}/clearance/sign-off`,
      payload
    );
    return res.data;
  },

  async generateTransferCertificate(
    studentId: string,
    payload: {
      reasonForLeaving: string;
      conductRating: string;
      promotedToGrade: string;
      issuedBy: string;
    }
  ): Promise<any> {
    const res = await apiClient.post(
      `/api/v1/students/${studentId}/transfer-certificate/generate`,
      payload
    );
    return res.data;
  },

  // Infirmary Clinical Visits
  async recordInfirmaryVisit(
    studentId: string,
    payload: {
      symptoms: string;
      treatmentGiven: string;
      temperatureFahrenheit?: number;
      attendedBy: string;
      admittedToRestRoom: boolean;
      parentNotified: boolean;
    }
  ): Promise<any> {
    const res = await apiClient.post(
      `/api/v1/students/${studentId}/infirmary-visits`,
      payload
    );
    return res.data;
  },

  // Behavior Incidents & House Merit
  async recordBehaviorIncident(
    studentId: string,
    payload: {
      incidentType: "MERIT" | "DEMERIT";
      pointsDelta: number;
      category: string;
      description: string;
      recordedBy: string;
    }
  ): Promise<any> {
    const res = await apiClient.post(
      `/api/v1/students/${studentId}/behavior-incident`,
      payload
    );
    return res.data;
  },

  async getHouseLeaderboard(): Promise<HouseScoreItem[]> {
    const res = await apiClient.get<HouseScoreItem[]>("/api/v1/students/house-leaderboard");
    return res.data || [];
  },

  // Verkada Gate Passes
  async generateGatePass(payload: {
    visitorName: string;
    visitorPhone: string;
    purpose: string;
    hostName: string;
    durationMinutes: number;
  }): Promise<GatePassItem> {
    const res = await apiClient.post<GatePassItem>(
      "/api/v1/operations/gate-passes/generate",
      payload
    );
    return res.data;
  },

  async scanGatePass(
    passCode: string,
    payload: {
      scannedAtGate: string;
      guardName: string;
      action: "CHECK_IN" | "CHECK_OUT";
    }
  ): Promise<any> {
    const res = await apiClient.post(
      `/api/v1/operations/gate-passes/${passCode}/scan`,
      payload
    );
    return res.data;
  },

  // Samsara Vehicle Telematics
  async getVehicles(): Promise<any[]> {
    const res = await apiClient.get<any[]>("/api/v1/operations/vehicles");
    return res.data || [];
  },

  async getVehicleTelematics(vehicleId: string): Promise<VehicleTelemetryItem> {
    const res = await apiClient.get<VehicleTelemetryItem>(
      `/api/v1/operations/vehicles/${vehicleId}/telematics`
    );
    return res.data;
  },

  // System Audit & Dynamic Tenant Licensing
  async getAuditLogs(): Promise<AuditLogItem[]> {
    const res = await apiClient.get<AuditLogItem[]>("/api/v1/system/audit-logs");
    return res.data || [];
  },

  async getLicenses(): Promise<any[]> {
    const res = await apiClient.get<any[]>("/api/v1/system/licenses");
    return res.data || [];
  },

  async activateLicense(moduleKey: string): Promise<any> {
    const res = await apiClient.post("/api/v1/system/licenses/activate", { moduleKey });
    return res.data;
  },

  // Staff & Payroll
  async getStaff(): Promise<any[]> {
    const res = await apiClient.get<any[]>("/api/v1/staff");
    return res.data || [];
  },

  async calculatePayroll(employeeCode: string, academicYearId?: string): Promise<any> {
    const res = await apiClient.post("/api/v1/staff/payroll/calculate", {
      employeeCode,
      academicYearId: academicYearId || "ay_2026_27",
    });
    return res.data;
  },

  async getSubstitutionRecommendations(
    absentTeacherId: string,
    date: string,
    periodNumber: number
  ): Promise<any> {
    const res = await apiClient.get(
      `/api/v1/staff/substitutions/recommendations?absentTeacherId=${absentTeacherId}&date=${date}&periodNumber=${periodNumber}`
    );
    return res.data;
  },
};

