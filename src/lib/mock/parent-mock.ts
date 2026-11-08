export interface ChildProfile {
  id: string;
  admissionNumber: string;
  fullName: string;
  gender: string;
  gradeLevel: string;
  section: string;
  rollNumber: number;
  avatarUrl: string;
  classTeacher: string;
  attendancePercentage: number;
  bloodGroup: string;
  busRoute: string;
  busNumber: string;
  lockerIndex: string;
  feeStatus: "PAID" | "PENDING" | "OVERDUE";
  pendingFeeAmount: number;
  recentGradeAverage: string;
  overallGpa: number;
}

export interface ParentUser {
  id: string;
  name: string;
  phone: string;
  email: string;
  relation: "Father" | "Mother" | "Guardian";
  address: string;
  children: ChildProfile[];
}

export interface FeeItem {
  id: string;
  invoiceNumber: string;
  childId: string;
  childName: string;
  term: string;
  dueDate: string;
  totalAmount: number;
  paidAmount: number;
  balanceAmount: number;
  status: "PAID" | "PENDING" | "OVERDUE";
  heads: { name: string; amount: number }[];
  receiptNumber?: string;
  paidDate?: string;
}

export interface LiveBusTracking {
  childId: string;
  vehicleRegistration: string;
  routeNumber: string;
  routeName: string;
  driverName: string;
  driverPhone: string;
  currentLocationName: string;
  latitude: number;
  longitude: number;
  speedKmph: number;
  etaMinutes: number;
  status: "ON_ROUTE" | "DELAYED" | "ARRIVED" | "IDLE";
  stops: {
    stopName: string;
    scheduledTime: string;
    actualTime?: string;
    isCompleted: boolean;
    isChildStop: boolean;
  }[];
}

export interface ChildMark {
  subject: string;
  subjectCode: string;
  theoryScore: number;
  practicalScore: number;
  internalScore: number;
  totalScore: number;
  maxScore: number;
  gradeBand: string;
  teacherRemarks: string;
}

export interface HomeworkItem {
  id: string;
  childId: string;
  subject: string;
  title: string;
  assignedDate: string;
  dueDate: string;
  status: "PENDING" | "SUBMITTED" | "EVALUATED";
  teacherName: string;
  attachmentName?: string;
}

export interface LeaveRequestRecord {
  id: string;
  childId: string;
  childName: string;
  leaveType: "SICK" | "CASUAL" | "EMERGENCY";
  startDate: string;
  endDate: string;
  reason: string;
  status: "APPROVED" | "PENDING" | "REJECTED";
  appliedDate: string;
  reviewedBy?: string;
}

export const PARENT_USER_DATA: ParentUser = {
  id: "parent-01",
  name: "Rajesh Sharma",
  phone: "+91 98765 43210",
  email: "rajesh.sharma@example.com",
  relation: "Father",
  address: "Flat 402, Sea Green Heights, Lokhandwala, Andheri West, Mumbai 400053",
  children: [
    {
      id: "child-aarav",
      admissionNumber: "ADM-2024-0012",
      fullName: "Aarav Sharma",
      gender: "Male",
      gradeLevel: "Class 10",
      section: "Section A",
      rollNumber: 1,
      avatarUrl: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150&auto=format&fit=crop&q=80",
      classTeacher: "Dr. Vikram Seth",
      attendancePercentage: 96.5,
      bloodGroup: "O+",
      busRoute: "Route 7 - Andheri West to Campus",
      busNumber: "MH-02-CB-4012 (Bus #04)",
      lockerIndex: "Locker B-12 / File #0842",
      feeStatus: "PAID",
      pendingFeeAmount: 0,
      recentGradeAverage: "94%",
      overallGpa: 9.6,
    },
    {
      id: "child-rhea",
      admissionNumber: "ADM-2024-0098",
      fullName: "Rhea Sharma",
      gender: "Female",
      gradeLevel: "Class 6",
      section: "Section B",
      rollNumber: 14,
      avatarUrl: "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=150&auto=format&fit=crop&q=80",
      classTeacher: "Sunita Rao",
      attendancePercentage: 98.1,
      bloodGroup: "O+",
      busRoute: "Route 7 - Andheri West to Campus",
      busNumber: "MH-02-CB-4012 (Bus #04)",
      lockerIndex: "Locker C-04 / File #1105",
      feeStatus: "PENDING",
      pendingFeeAmount: 22500,
      recentGradeAverage: "91%",
      overallGpa: 9.2,
    },
  ],
};

export const MOCK_PARENT_FEES: FeeItem[] = [
  {
    id: "inv-01",
    invoiceNumber: "INV-2026-0842",
    childId: "child-aarav",
    childName: "Aarav Sharma",
    term: "Term 1 (Academic Year 2026-27)",
    dueDate: "2026-08-15",
    totalAmount: 45000,
    paidAmount: 45000,
    balanceAmount: 0,
    status: "PAID",
    receiptNumber: "REC-2026-0842",
    paidDate: "2026-08-10",
    heads: [
      { name: "Tuition Fee", amount: 32000 },
      { name: "Science & Computer Lab Fee", amount: 6500 },
      { name: "Transport Facility (Route 7)", amount: 5000 },
      { name: "Library & Sports Development", amount: 1500 },
    ],
  },
  {
    id: "inv-02",
    invoiceNumber: "INV-2026-0914",
    childId: "child-rhea",
    childName: "Rhea Sharma",
    term: "Term 2 (Academic Year 2026-27)",
    dueDate: "2026-11-25",
    totalAmount: 22500,
    paidAmount: 0,
    balanceAmount: 22500,
    status: "PENDING",
    heads: [
      { name: "Tuition Fee (Junior Section)", amount: 16000 },
      { name: "Activity & Co-Curricular Fee", amount: 2500 },
      { name: "Transport Facility (Route 7)", amount: 4000 },
    ],
  },
];

export const MOCK_BUS_TRACKING: LiveBusTracking = {
  childId: "child-aarav",
  vehicleRegistration: "MH-02-CB-4012",
  routeNumber: "Route 7",
  routeName: "Andheri West to Campus",
  driverName: "Ramesh Yadav",
  driverPhone: "+91 98334 11223",
  currentLocationName: "Near Infinity Mall Signal (Link Road)",
  latitude: 19.1415,
  longitude: 72.8312,
  speedKmph: 38.5,
  etaMinutes: 12,
  status: "ON_ROUTE",
  stops: [
    {
      stopName: "Lokhandwala Complex Gate 2",
      scheduledTime: "06:45 AM",
      actualTime: "06:47 AM",
      isCompleted: true,
      isChildStop: false,
    },
    {
      stopName: "Infinity Mall / Link Road",
      scheduledTime: "07:05 AM",
      actualTime: "07:06 AM",
      isCompleted: true,
      isChildStop: true, // Aarav's pickup stop
    },
    {
      stopName: "DN Nagar Metro Interchange",
      scheduledTime: "07:20 AM",
      isCompleted: false,
      isChildStop: false,
    },
    {
      stopName: "Apex Academy Main Campus Gate 1",
      scheduledTime: "07:45 AM",
      isCompleted: false,
      isChildStop: false,
    },
  ],
};

export const MOCK_AARAV_MARKS: ChildMark[] = [
  {
    subject: "Mathematics",
    subjectCode: "MATH-041",
    theoryScore: 74,
    practicalScore: 19,
    internalScore: 20,
    totalScore: 94,
    maxScore: 100,
    gradeBand: "A1",
    teacherRemarks: "Exceptional analytical thinking in coordinate geometry and quadratic equations.",
  },
  {
    subject: "Science & Physics",
    subjectCode: "SCI-086",
    theoryScore: 72,
    practicalScore: 20,
    internalScore: 19,
    totalScore: 92,
    maxScore: 100,
    gradeBand: "A1",
    teacherRemarks: "Strong experimental execution in optics and circuit board experiments.",
  },
  {
    subject: "English Language & Literature",
    subjectCode: "ENG-184",
    theoryScore: 70,
    practicalScore: 0,
    internalScore: 18,
    totalScore: 88,
    maxScore: 100,
    gradeBand: "A2",
    teacherRemarks: "Fluent articulation and creative prose in terminal essay composition.",
  },
  {
    subject: "Social Sciences (History/Civics)",
    subjectCode: "SST-087",
    theoryScore: 71,
    practicalScore: 0,
    internalScore: 19,
    totalScore: 90,
    maxScore: 100,
    gradeBand: "A1",
    teacherRemarks: "High factual precision on contemporary democratic governance and map skills.",
  },
];

export const MOCK_HOMEWORK: HomeworkItem[] = [
  {
    id: "hw-01",
    childId: "child-aarav",
    subject: "Mathematics",
    title: "Exercise 8.4 - Trigonometric Identities (Q1 to Q10)",
    assignedDate: "2026-11-07",
    dueDate: "2026-11-10",
    status: "PENDING",
    teacherName: "Dr. Vikram Seth",
    attachmentName: "NCERT_Chapter_8_Trig.pdf",
  },
  {
    id: "hw-02",
    childId: "child-aarav",
    subject: "Physics",
    title: "Ray Optics Reflection & Refraction Lab Record Submission",
    assignedDate: "2026-11-06",
    dueDate: "2026-11-09",
    status: "SUBMITTED",
    teacherName: "Dr. Vikram Seth",
    attachmentName: "Lab_Template_Optics.pdf",
  },
  {
    id: "hw-03",
    childId: "child-rhea",
    subject: "Environmental Science",
    title: "Water Conservation Chart & Rainwater Harvesting Diagram",
    assignedDate: "2026-11-08",
    dueDate: "2026-11-12",
    status: "PENDING",
    teacherName: "Sunita Rao",
  },
];

export const MOCK_LEAVE_RECORDS: LeaveRequestRecord[] = [
  {
    id: "leave-01",
    childId: "child-aarav",
    childName: "Aarav Sharma",
    leaveType: "SICK",
    startDate: "2026-10-12",
    endDate: "2026-10-14",
    reason: "Viral fever and acute throat infection (Doctor advised 3 days bed rest).",
    status: "APPROVED",
    appliedDate: "2026-10-11",
    reviewedBy: "Dr. Vikram Seth (Class Teacher)",
  },
];

