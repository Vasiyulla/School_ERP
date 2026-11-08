export type RoleName =
  | "Super Admin"
  | "Principal"
  | "School Admin"
  | "Admission Officer"
  | "Teacher"
  | "Accountant"
  | "Parent"
  | "Student";

export interface User {
  id: string;
  name: string;
  email: string;
  avatarUrl?: string;
  role: RoleName;
  status: "active" | "inactive" | "suspended";
}

export interface Student {
  id: string;
  admissionNumber: string;
  firstName: string;
  lastName: string;
  fullName: string;
  gender: "Male" | "Female" | "Other";
  dateOfBirth: string;
  avatarUrl?: string;
  status: "Active" | "Inactive" | "Graduated" | "Transferred";
  class: string;
  section: string;
  rollNumber: number;
  guardian: {
    name: string;
    relationship: "Father" | "Mother" | "Legal Guardian";
    phone: string;
    email: string;
  };
  attendancePercentage: number;
  feeStatus: "Paid" | "Partial" | "Overdue";
  balanceAmount: number;
  enrolledAt: string;
}

export interface AdmissionApplication {
  id: string;
  applicationNumber: string;
  applicantName: string;
  targetClass: string;
  dateOfBirth: string;
  submissionDate: string;
  status: "Submitted" | "Under Review" | "Interview Scheduled" | "Approved" | "Rejected" | "Enrolled";
  parentName: string;
  parentPhone: string;
  parentEmail: string;
  previousSchool?: string;
  entranceScore?: number;
}

export interface ClassSection {
  id: string;
  className: string;
  sectionName: string;
  capacity: number;
  enrolledCount: number;
  classTeacher: string;
  roomNumber: string;
}

export interface AcademicYear {
  id: string;
  name: string;
  startDate: string;
  endDate: string;
  isActive: boolean;
  terms: {
    id: string;
    name: string;
    startDate: string;
    endDate: string;
  }[];
}

export interface StaffMember {
  id: string;
  employeeCode: string;
  fullName: string;
  designation: string;
  department: string;
  email: string;
  phone: string;
  status: "Active" | "On Leave" | "Resigned";
  assignedClasses: string[];
  joinedDate: string;
}

export interface AdminDashboardKPIs {
  totalStudents: number;
  studentCapacity: number;
  todayAttendancePercent: number;
  presentStudents: number;
  absentStudents: number;
  totalStaff: number;
  activeStaffPresent: number;
  pendingAdmissions: number;
  feeCollectionMonth: number;
  feeCollectionTarget: number;
  feeOverdueAmount: number;
}

