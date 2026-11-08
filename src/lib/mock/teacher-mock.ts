export interface TeacherProfile {
  id: string;
  name: string;
  title: string;
  employeeCode: string;
  department: string;
  email: string;
  phone: string;
  isClassTeacher: boolean;
  assignedClass: string;
  assignedSection: string;
  totalStudentsInClass: number;
  subjectsTaught: {
    className: string;
    sectionName: string;
    subjectName: string;
    periodsPerWeek: number;
  }[];
}

export const mockTeacherProfile: TeacherProfile = {
  id: "emp-201",
  name: "Dr. Vikram Seth",
  title: "PGT Senior Mathematics Faculty",
  employeeCode: "EMP-MATH-042",
  department: "Mathematics & Applied Sciences",
  email: "vikram.seth@apexacademy.edu.in",
  phone: "+91 98201 54321",
  isClassTeacher: true,
  assignedClass: "Class 10",
  assignedSection: "Section A",
  totalStudentsInClass: 38,
  subjectsTaught: [
    { className: "Class 10", sectionName: "Section A", subjectName: "Advanced Mathematics", periodsPerWeek: 6 },
    { className: "Class 10", sectionName: "Section B", subjectName: "Standard Mathematics", periodsPerWeek: 6 },
    { className: "Class 11", sectionName: "Section A", subjectName: "Calculus & Algebra", periodsPerWeek: 5 },
    { className: "Class 12", sectionName: "Section A", subjectName: "Higher Mathematics", periodsPerWeek: 5 },
  ],
};

export interface TeacherSchedulePeriod {
  periodNumber: number;
  timeSlot: string;
  subject: string;
  className: string;
  sectionName: string;
  roomNumber: string;
  isCurrent?: boolean;
  isFree?: boolean;
  isProxy?: boolean;
  notes?: string;
}

export const mockTodaySchedule: TeacherSchedulePeriod[] = [
  { periodNumber: 1, timeSlot: "08:30 – 09:15", subject: "Morning Homeroom & Roll Call", className: "Class 10", sectionName: "Section A", roomNumber: "Room 301", isCurrent: false },
  { periodNumber: 2, timeSlot: "09:15 – 10:00", subject: "Advanced Mathematics", className: "Class 10", sectionName: "Section A", roomNumber: "Room 301", isCurrent: false },
  { periodNumber: 3, timeSlot: "10:15 – 11:00", subject: "Free / Lesson Planning", className: "-", sectionName: "-", roomNumber: "Math Dept Room", isFree: true },
  { periodNumber: 4, timeSlot: "11:00 – 11:45", subject: "Calculus & Algebra", className: "Class 11", sectionName: "Section A", roomNumber: "Room 402", isCurrent: true },
  { periodNumber: 5, timeSlot: "12:30 – 01:15", subject: "Standard Mathematics", className: "Class 10", sectionName: "Section B", roomNumber: "Room 303", isCurrent: false },
  { periodNumber: 6, timeSlot: "01:15 – 02:00", subject: "Mathematics Lab Practical", className: "Class 10", sectionName: "Section A", roomNumber: "Computer Lab 1", isCurrent: false },
  { periodNumber: 7, timeSlot: "02:05 – 02:50", subject: "Substitution (Physics - Meera Nair on leave)", className: "Class 9", sectionName: "Section C", roomNumber: "Room 205", isProxy: true },
];

export interface ClassStudentAttendance {
  id: string;
  rollNumber: number;
  admissionNumber: string;
  fullName: string;
  gender: "Male" | "Female";
  guardianName: string;
  guardianPhone: string;
  status: "PRESENT" | "ABSENT" | "LATE" | "EXCUSED";
  preApprovedReason?: string;
  documentStatus: "COMPLETE" | "PENDING_TC" | "PENDING_MED";
  lockerIndex: string;
  attendancePct: number;
}

export const mockClassRoster: ClassStudentAttendance[] = [
  { id: "stu-1001", rollNumber: 1, admissionNumber: "ADM-2024-0012", fullName: "Aarav Sharma", gender: "Male", guardianName: "Rajesh Sharma", guardianPhone: "+91 98765 43210", status: "PRESENT", documentStatus: "COMPLETE", lockerIndex: "Locker B-12", attendancePct: 96.5 },
  { id: "stu-1002", rollNumber: 2, admissionNumber: "ADM-2024-0045", fullName: "Ananya Deshmukh", gender: "Female", guardianName: "Sunil Deshmukh", guardianPhone: "+91 98111 22334", status: "PRESENT", documentStatus: "COMPLETE", lockerIndex: "Locker B-14", attendancePct: 98.2 },
  { id: "stu-1003", rollNumber: 3, admissionNumber: "ADM-2024-0089", fullName: "Diya Patel", gender: "Female", guardianName: "Bhavesh Patel", guardianPhone: "+91 98222 33445", status: "EXCUSED", preApprovedReason: "Representing school at Inter-District Math Olympiad", documentStatus: "COMPLETE", lockerIndex: "Locker B-15", attendancePct: 95.0 },
  { id: "stu-1004", rollNumber: 4, admissionNumber: "ADM-2024-0102", fullName: "Ishaan Verma", gender: "Male", guardianName: "Alok Verma", guardianPhone: "+91 98333 44556", status: "ABSENT", documentStatus: "PENDING_TC", lockerIndex: "Locker B-18", attendancePct: 88.4 },
  { id: "stu-1005", rollNumber: 5, admissionNumber: "ADM-2024-0125", fullName: "Kavya Iyer", gender: "Female", guardianName: "Ramesh Iyer", guardianPhone: "+91 98444 55667", status: "PRESENT", documentStatus: "COMPLETE", lockerIndex: "Locker B-20", attendancePct: 99.1 },
  { id: "stu-1006", rollNumber: 6, admissionNumber: "ADM-2024-0144", fullName: "Rohan Kulkarni", gender: "Male", guardianName: "Girish Kulkarni", guardianPhone: "+91 98555 66778", status: "LATE", preApprovedReason: "School bus #14 delayed in traffic", documentStatus: "COMPLETE", lockerIndex: "Locker B-22", attendancePct: 91.8 },
  { id: "stu-1007", rollNumber: 7, admissionNumber: "ADM-2024-0167", fullName: "Sanya Kapoor", gender: "Female", guardianName: "Deepak Kapoor", guardianPhone: "+91 98666 77889", status: "PRESENT", documentStatus: "COMPLETE", lockerIndex: "Locker B-25", attendancePct: 97.4 },
  { id: "stu-1008", rollNumber: 8, admissionNumber: "ADM-2024-0189", fullName: "Vihaan Joshi", gender: "Male", guardianName: "Makarand Joshi", guardianPhone: "+91 98777 88990", status: "ABSENT", documentStatus: "PENDING_MED", lockerIndex: "Locker B-27", attendancePct: 84.6 },
  { id: "stu-1009", rollNumber: 9, admissionNumber: "ADM-2024-0205", fullName: "Zoya Khan", gender: "Female", guardianName: "Tariq Khan", guardianPhone: "+91 98888 99001", status: "PRESENT", documentStatus: "COMPLETE", lockerIndex: "Locker B-30", attendancePct: 94.3 },
  { id: "stu-1010", rollNumber: 10, admissionNumber: "ADM-2024-0220", fullName: "Aditya Nair", gender: "Male", guardianName: "Kishore Nair", guardianPhone: "+91 98999 00112", status: "PRESENT", documentStatus: "COMPLETE", lockerIndex: "Locker B-32", attendancePct: 95.8 },
  { id: "stu-1011", rollNumber: 11, admissionNumber: "ADM-2024-0245", fullName: "Bhavya Gupta", gender: "Female", guardianName: "Manish Gupta", guardianPhone: "+91 98123 45678", status: "PRESENT", documentStatus: "COMPLETE", lockerIndex: "Locker B-34", attendancePct: 93.0 },
  { id: "stu-1012", rollNumber: 12, admissionNumber: "ADM-2024-0268", fullName: "Devendra Patil", gender: "Male", guardianName: "Ashok Patil", guardianPhone: "+91 98234 56789", status: "PRESENT", documentStatus: "COMPLETE", lockerIndex: "Locker B-36", attendancePct: 92.5 },
];

export interface StudentGradeEntry {
  studentId: string;
  rollNumber: number;
  fullName: string;
  theoryScore: number; // max 80
  practicalScore: number; // max 20
  internalScore: number; // max 10
  totalScore: number; // max 110 normalized to 100
  gradeBand: string; // A1, A2, B1, B2, C1, C2, D, E
  remarks: string;
  status: "DRAFT" | "SUBMITTED" | "LOCKED";
}

export const mockGradebookEntries: StudentGradeEntry[] = [
  { studentId: "stu-1001", rollNumber: 1, fullName: "Aarav Sharma", theoryScore: 74, practicalScore: 19, internalScore: 10, totalScore: 94, gradeBand: "A1", remarks: "Exceptional mastery of quadratic equations", status: "DRAFT" },
  { studentId: "stu-1002", rollNumber: 2, fullName: "Ananya Deshmukh", theoryScore: 78, practicalScore: 20, internalScore: 10, totalScore: 98, gradeBand: "A1", remarks: "Flawless geometric proofs and lab records", status: "DRAFT" },
  { studentId: "stu-1003", rollNumber: 3, fullName: "Diya Patel", theoryScore: 71, practicalScore: 18, internalScore: 9, totalScore: 89, gradeBand: "A2", remarks: "Strong algebraic concepts, minor calculation slip", status: "DRAFT" },
  { studentId: "stu-1004", rollNumber: 4, fullName: "Ishaan Verma", theoryScore: 52, practicalScore: 15, internalScore: 7, totalScore: 67, gradeBand: "B2", remarks: "Needs more practice in trigonometry identities", status: "DRAFT" },
  { studentId: "stu-1005", rollNumber: 5, fullName: "Kavya Iyer", theoryScore: 76, practicalScore: 19, internalScore: 10, totalScore: 95, gradeBand: "A1", remarks: "Exemplary precision in coordinate geometry", status: "DRAFT" },
  { studentId: "stu-1006", rollNumber: 6, fullName: "Rohan Kulkarni", theoryScore: 61, practicalScore: 16, internalScore: 8, totalScore: 77, gradeBand: "B1", remarks: "Good conceptual grasp, time management needs work", status: "DRAFT" },
  { studentId: "stu-1007", rollNumber: 7, fullName: "Sanya Kapoor", theoryScore: 68, practicalScore: 17, internalScore: 9, totalScore: 85, gradeBand: "A2", remarks: "Well organized lab record and neat working steps", status: "DRAFT" },
  { studentId: "stu-1008", rollNumber: 8, fullName: "Vihaan Joshi", theoryScore: 41, practicalScore: 12, internalScore: 6, totalScore: 54, gradeBand: "C1", remarks: "Recommended for remedial mathematics clinic", status: "DRAFT" },
  { studentId: "stu-1009", rollNumber: 9, fullName: "Zoya Khan", theoryScore: 65, practicalScore: 18, internalScore: 9, totalScore: 83, gradeBand: "A2", remarks: "Consistent effort and active in classroom discussions", status: "DRAFT" },
  { studentId: "stu-1010", rollNumber: 10, fullName: "Aditya Nair", theoryScore: 70, practicalScore: 18, internalScore: 9, totalScore: 88, gradeBand: "A2", remarks: "Solid analytical problem solving skills", status: "DRAFT" },
  { studentId: "stu-1011", rollNumber: 11, fullName: "Bhavya Gupta", theoryScore: 59, practicalScore: 15, internalScore: 8, totalScore: 74, gradeBand: "B1", remarks: "Steady improvement in linear equations", status: "DRAFT" },
  { studentId: "stu-1012", rollNumber: 12, fullName: "Devendra Patil", theoryScore: 58, practicalScore: 14, internalScore: 8, totalScore: 72, gradeBand: "B1", remarks: "Good effort, revision required in surface areas", status: "DRAFT" },
];

export interface TeacherAssignment {
  id: string;
  title: string;
  subject: string;
  className: string;
  sectionName: string;
  assignedDate: string;
  dueDate: string;
  totalMarks: number;
  totalSubmissions: number;
  totalStudents: number;
  evaluatedCount: number;
  attachmentName?: string;
  status: "ACTIVE" | "PAST_DUE" | "EVALUATED";
}

export const mockTeacherAssignments: TeacherAssignment[] = [
  {
    id: "asg-101",
    title: "Chapter 4: Quadratic Equations Problem Set 4.2",
    subject: "Advanced Mathematics",
    className: "Class 10",
    sectionName: "Section A",
    assignedDate: "2026-11-06",
    dueDate: "2026-11-10",
    totalMarks: 25,
    totalSubmissions: 31,
    totalStudents: 38,
    evaluatedCount: 24,
    attachmentName: "NCERT_Ex_4.2_Supplement.pdf",
    status: "ACTIVE",
  },
  {
    id: "asg-102",
    title: "Calculus Limits & Continuity Exercise 2.1",
    subject: "Calculus & Algebra",
    className: "Class 11",
    sectionName: "Section A",
    assignedDate: "2026-11-04",
    dueDate: "2026-11-08",
    totalMarks: 30,
    totalSubmissions: 40,
    totalStudents: 42,
    evaluatedCount: 40,
    attachmentName: "Limits_Continuity_Problems.pdf",
    status: "PAST_DUE",
  },
  {
    id: "asg-103",
    title: "Trigonometric Heights & Distances Case Study",
    subject: "Advanced Mathematics",
    className: "Class 10",
    sectionName: "Section A",
    assignedDate: "2026-10-28",
    dueDate: "2026-11-02",
    totalMarks: 20,
    totalSubmissions: 38,
    totalStudents: 38,
    evaluatedCount: 38,
    attachmentName: "Heights_Surveying_CaseStudy.pdf",
    status: "EVALUATED",
  },
];

export interface BehavioralObservation {
  id: string;
  studentId: string;
  studentName: string;
  date: string;
  type: "MERIT" | "DISCIPLINARY" | "ACADEMIC_INTERVENTION";
  title: string;
  notes: string;
  visibleToParent: boolean;
  status: "LOGGED" | "ACKNOWLEDGED_BY_PARENT";
}

export const mockBehavioralObservations: BehavioralObservation[] = [
  {
    id: "obs-501",
    studentId: "stu-1001",
    studentName: "Aarav Sharma",
    date: "2026-11-07",
    type: "MERIT",
    title: "Peer Tutoring & Academic Leadership",
    notes: "Demonstrated great initiative by assisting struggling classmates during the quadratic equations problem lab.",
    visibleToParent: true,
    status: "ACKNOWLEDGED_BY_PARENT",
  },
  {
    id: "obs-502",
    studentId: "stu-1004",
    studentName: "Ishaan Verma",
    date: "2026-11-05",
    type: "DISCIPLINARY",
    title: "Incomplete Lab Record & Deficit Focus",
    notes: "Did not submit trigonometry practical sheets on third consecutive reminder. Parent requested for consultation.",
    visibleToParent: true,
    status: "LOGGED",
  },
  {
    id: "obs-503",
    studentId: "stu-1008",
    studentName: "Vihaan Joshi",
    date: "2026-11-04",
    type: "ACADEMIC_INTERVENTION",
    title: "Enrolled in After-School Remedial Math",
    notes: "Assigned 30-minute foundational clinic on Tuesdays and Thursdays to bridge algebraic factorization gaps.",
    visibleToParent: false,
    status: "LOGGED",
  },
];

export interface TeacherLeaveRecord {
  id: string;
  type: "CASUAL" | "MEDICAL" | "DUTY_LEAVE";
  startDate: string;
  endDate: string;
  days: number;
  reason: string;
  status: "APPROVED" | "PENDING" | "REJECTED";
  substituteTeacherName: string;
}

export const mockTeacherLeaves: TeacherLeaveRecord[] = [
  {
    id: "lev-101",
    type: "CASUAL",
    startDate: "2026-11-14",
    endDate: "2026-11-14",
    days: 1,
    reason: "Family personal engagement",
    status: "APPROVED",
    substituteTeacherName: "Rajesh Kulkarni (Math PGT)",
  },
  {
    id: "lev-102",
    type: "DUTY_LEAVE",
    startDate: "2026-10-18",
    endDate: "2026-10-19",
    days: 2,
    reason: "CBSE Master Trainer Workshop at Regional Center",
    status: "APPROVED",
    substituteTeacherName: "Sunita Roy (Science PGT)",
  },
];

