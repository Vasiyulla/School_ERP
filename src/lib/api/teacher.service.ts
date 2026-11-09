import { apiClient } from "../api-client";

export interface AttendanceRecordPayload {
  studentId: string;
  status: "PRESENT" | "ABSENT" | "LATE" | "EXCUSED" | "HALF_DAY";
  remarks?: string;
}

export interface BatchAttendanceRequest {
  classSectionId: string;
  academicYearId: string;
  attendanceDate: string;
  records: AttendanceRecordPayload[];
}

export interface BatchAttendanceResponse {
  totalProcessed: number;
  presentCount: number;
  absentCount: number;
  lateCount: number;
  excusedCount: number;
  processedAt: string;
}

export interface HomeworkItem {
  id: string;
  title: string;
  subjectName: string;
  classSection: string;
  assignedDate: string;
  dueDate: string;
  description: string;
  maxScore: number;
  status: string;
  submissionCount?: number;
}

export const teacherService = {
  // Batch Attendance
  async recordBatchAttendance(
    payload: BatchAttendanceRequest
  ): Promise<BatchAttendanceResponse> {
    const res = await apiClient.post<BatchAttendanceResponse>(
      "/api/v1/attendance/batch",
      payload
    );
    return res.data;
  },

  async syncHardwarePunch(payload: {
    rfidCardNumber: string;
    terminalId: string;
    punchTimestamp: string;
    gateDirection: "IN" | "OUT";
  }): Promise<any> {
    const res = await apiClient.post("/api/v1/attendance/hardware-punches", payload);
    return res.data;
  },

  async getAttendanceAudits(): Promise<any[]> {
    const res = await apiClient.get<any[]>("/api/v1/attendance/audits");
    return res.data || [];
  },

  // CBSE Examinations & 70/20/10 Standards
  async getGradebook(classSectionId?: string): Promise<any> {
    const query = classSectionId ? `?classSectionId=${classSectionId}` : "";
    const res = await apiClient.get<any>(`/api/v1/examinations/gradebook${query}`);
    return res.data;
  },

  async calculateWeightedAssessment(payload: {
    studentId: string;
    subjectId: string;
    theoryScore: number;
    practicalScore: number;
    internalScore: number;
  }): Promise<any> {
    const res = await apiClient.post(
      "/api/v1/examinations/assessments/calculate-weighted",
      payload
    );
    return res.data;
  },

  async generateReportCard(studentId: string): Promise<any> {
    const res = await apiClient.get(
      `/api/v1/examinations/report-cards/generate?studentId=${studentId}`
    );
    return res.data;
  },

  // Toddle Digital Homework
  async getHomework(classSection?: string): Promise<HomeworkItem[]> {
    const query = classSection ? `?classSection=${classSection}` : "";
    const res = await apiClient.get<HomeworkItem[]>(`/api/v1/academics/homework${query}`);
    return res.data || [];
  },

  async createHomework(payload: {
    title: string;
    subjectName: string;
    classSection: string;
    dueDate: string;
    description: string;
    maxScore: number;
    assignedByTeacherId: string;
  }): Promise<HomeworkItem> {
    const res = await apiClient.post<HomeworkItem>("/api/v1/academics/homework", payload);
    return res.data;
  },

  async gradeHomework(
    submissionId: string,
    payload: {
      scoreAwarded: number;
      feedback: string;
      gradedBy: string;
    }
  ): Promise<any> {
    const res = await apiClient.post(
      `/api/v1/academics/homework/submissions/${submissionId}/grade`,
      payload
    );
    return res.data;
  },
};

