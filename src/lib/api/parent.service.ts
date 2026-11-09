import { apiClient } from "../api-client";

export interface ParentChildSummary {
  id: string;
  admissionNumber: string;
  fullName: string;
  gradeLevel: string;
  section: string;
  rollNumber: number;
  avatarUrl?: string;
  houseName?: string;
  housePoints?: number;
  walletBalance?: number;
  attendancePercent?: number;
  pendingFeeAmount?: number;
  busRoute?: string;
  busLiveStatus?: string;
  busDistanceKm?: number;
  busEtaMinutes?: number;
}

export interface ParentHomeDashboardData {
  guardianName: string;
  guardianEmail: string;
  guardianPhone: string;
  totalChildrenCount: number;
  familyTotalDue: number;
  children: ParentChildSummary[];
  activeBusLocation?: {
    vehicleNumber: string;
    latitude: number;
    longitude: number;
    speedKmph: number;
    distanceKm: number;
    etaMinutes: number;
    status: string;
  };
  upcomingPtmSession?: {
    id: string;
    title: string;
    meetingDate: string;
    startTime: string;
    endTime: string;
  };
}

export interface PtmSlotItem {
  id: string;
  sessionId: string;
  teacherId: string;
  teacherName: string;
  slotStartTime: string;
  slotEndTime: string;
  isBooked: boolean;
  bookedByStudentName?: string;
}

export const parentService = {
  async getDashboard(
    guardianEmail = "parent.rajesh@example.com"
  ): Promise<ParentHomeDashboardData> {
    const res = await apiClient.get<ParentHomeDashboardData>(
      `/api/v1/mobile/parent/home?guardianEmail=${encodeURIComponent(guardianEmail)}`
    );
    return res.data;
  },

  async createPtmSession(payload: {
    title: string;
    meetingDate: string;
    startTime: string;
    endTime: string;
    slotDurationMinutes: number;
  }): Promise<any> {
    const res = await apiClient.post("/api/v1/communication/ptm/sessions", payload);
    return res.data;
  },

  async getPtmSlots(sessionId: string): Promise<PtmSlotItem[]> {
    const res = await apiClient.get<PtmSlotItem[]>(
      `/api/v1/communication/ptm/sessions/${sessionId}/slots`
    );
    return res.data || [];
  },

  async bookPtmSlot(
    slotId: string,
    payload: {
      studentId: string;
      guardianName: string;
      parentNotes?: string;
    }
  ): Promise<any> {
    const res = await apiClient.post(
      `/api/v1/communication/ptm/slots/${slotId}/book`,
      payload
    );
    return res.data;
  },
};

