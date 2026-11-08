"use client";

import * as React from "react";
import {
  CalendarCheck2,
  Search,
  Filter,
  CheckCircle2,
  XCircle,
  Clock,
  AlertCircle,
  Phone,
  MessageSquare,
  ShieldAlert,
  Send,
  History,
  ArrowRight,
  UserCheck,
  Building2,
} from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Badge } from "@/components/ui/badge";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { cn } from "@/lib/utils";

interface SectionSubmission {
  id: string;
  gradeSection: string;
  classTeacher: string;
  totalStudents: number;
  present: number;
  absent: number;
  status: "SUBMITTED" | "PENDING";
  submittedAt?: string;
}

const sectionSubmissions: SectionSubmission[] = [
  { id: "s-1", gradeSection: "Grade 10-A", classTeacher: "Dr. Vikram Seth", totalStudents: 42, present: 41, absent: 1, status: "SUBMITTED", submittedAt: "08:35 AM" },
  { id: "s-2", gradeSection: "Grade 10-B", classTeacher: "Ananya Deshmukh", totalStudents: 40, present: 38, absent: 2, status: "SUBMITTED", submittedAt: "08:42 AM" },
  { id: "s-3", gradeSection: "Grade 10-C", classTeacher: "Rajesh Kulkarni", totalStudents: 45, present: 0, absent: 0, status: "PENDING" },
  { id: "s-4", gradeSection: "Grade 11-A (Sci)", classTeacher: "Pooja Hegde", totalStudents: 38, present: 36, absent: 2, status: "SUBMITTED", submittedAt: "08:30 AM" },
  { id: "s-5", gradeSection: "Grade 11-B (Comm)", classTeacher: "Sunil Gavaskar", totalStudents: 44, present: 42, absent: 2, status: "SUBMITTED", submittedAt: "08:48 AM" },
  { id: "s-6", gradeSection: "Grade 12-A (Sci)", classTeacher: "Meera Nair", totalStudents: 36, present: 35, absent: 1, status: "SUBMITTED", submittedAt: "08:25 AM" },
  { id: "s-7", gradeSection: "Grade 12-B (Comm)", classTeacher: "Kavita Krishnan", totalStudents: 40, present: 0, absent: 0, status: "PENDING" },
  { id: "s-8", gradeSection: "Grade 9-A", classTeacher: "Amitabh Sen", totalStudents: 45, present: 43, absent: 2, status: "SUBMITTED", submittedAt: "08:50 AM" },
];

const mockAbsentees = [
  {
    id: "abs-1",
    studentName: "Rohan Deshmukh",
    admissionNumber: "ADM-2022-0312",
    className: "Grade 9-C",
    guardianName: "Sanjay Deshmukh",
    guardianPhone: "+91 98201 44521",
    consecutiveDays: 2,
    smsStatus: "Delivered (09:05 AM)",
  },
  {
    id: "abs-2",
    studentName: "Kabir Khan",
    admissionNumber: "ADM-2023-0421",
    className: "Grade 10-B",
    guardianName: "Tariq Khan",
    guardianPhone: "+91 98190 22345",
    consecutiveDays: 1,
    smsStatus: "Delivered (09:05 AM)",
  },
  {
    id: "abs-3",
    studentName: "Pooja Hegde",
    admissionNumber: "ADM-2024-0551",
    className: "Grade 11-A",
    guardianName: "Ramesh Hegde",
    guardianPhone: "+91 99203 11892",
    consecutiveDays: 1,
    smsStatus: "Delivered (09:05 AM)",
  },
  {
    id: "abs-4",
    studentName: "Varun Mehta",
    admissionNumber: "ADM-2023-0199",
    className: "Grade 8-B",
    guardianName: "Deepak Mehta",
    guardianPhone: "+91 98220 77610",
    consecutiveDays: 3,
    smsStatus: "Delivered (09:05 AM)",
  },
];

const mockCorrections = [
  {
    id: "corr-101",
    studentName: "Diya Patel",
    className: "Grade 12-B",
    date: "2026-11-06",
    previousStatus: "ABSENT",
    correctedStatus: "PRESENT",
    correctedBy: "Kavita Krishnan (Class Teacher)",
    reason: "Late arrival due to official inter-school debate competition representation.",
    timestamp: "2026-11-06 11:20:14",
  },
  {
    id: "corr-102",
    studentName: "Aarav Sharma",
    className: "Grade 10-A",
    date: "2026-11-05",
    previousStatus: "ABSENT",
    correctedStatus: "EXCUSED",
    correctedBy: "Dr. Vikram Seth (Class Teacher)",
    reason: "Medical fitness certificate verified by school infirmary.",
    timestamp: "2026-11-05 14:05:42",
  },
  {
    id: "corr-103",
    studentName: "Ishaan Gupta",
    className: "Grade 11-B",
    date: "2026-11-04",
    previousStatus: "PRESENT",
    correctedStatus: "LATE",
    correctedBy: "Admin System (Biometric Sync)",
    reason: "Gate RFID logged entry at 08:35 AM (School start time 08:15 AM).",
    timestamp: "2026-11-04 09:00:00",
  },
];

export default function AdminAttendancePage() {
  const [activeTab, setActiveTab] = React.useState<"sections" | "absentees" | "audit">("sections");
  const [searchQuery, setSearchQuery] = React.useState("");

  const submittedSections = sectionSubmissions.filter((s) => s.status === "SUBMITTED").length;
  const totalSections = sectionSubmissions.length;
  const submissionPercent = Math.round((submittedSections / totalSections) * 100);

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between border-b border-slate-200/80 pb-5">
        <div>
          <div className="flex items-center gap-2.5">
            <h1 className="text-xl font-bold tracking-tight text-slate-900">
              Attendance & Compliance Hub
            </h1>
            <Badge variant="outline" className="text-[11px] font-semibold text-blue-700 bg-blue-50 border-blue-200">
              Today: Friday, Nov 8
            </Badge>
          </div>
          <p className="text-xs text-slate-500 mt-1">
            Batch attendance recording for 18,450 students, real-time section submission tracker, and immutable correction logs.
          </p>
        </div>
        <div className="flex items-center gap-2">
          <Button variant="outline" size="sm" className="h-8 gap-1.5 text-xs text-slate-700">
            <Send className="h-3.5 w-3.5" />
            Send Absence SMS Alerts
          </Button>
          <Button size="sm" className="h-8 gap-1.5 text-xs bg-blue-600 hover:bg-blue-700 text-white shadow-xs">
            <CalendarCheck2 className="h-3.5 w-3.5" />
            Lock Daily Attendance
          </Button>
        </div>
      </div>

      {/* KPI Stats */}
      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
        <Card className="border-slate-200/80 shadow-xs">
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <span className="text-[11px] font-semibold uppercase tracking-wider text-slate-500">
              Today's Attendance Rate
            </span>
            <div className="flex h-7 w-7 items-center justify-center rounded-md bg-emerald-50 text-emerald-600">
              <CheckCircle2 className="h-3.5 w-3.5" />
            </div>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold tracking-tight text-emerald-700">94.8%</div>
            <p className="text-[11px] text-slate-500 mt-1">
              17,490 present out of 18,450 enrolled
            </p>
          </CardContent>
        </Card>

        <Card className="border-slate-200/80 shadow-xs">
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <span className="text-[11px] font-semibold uppercase tracking-wider text-slate-500">
              Today's Absences
            </span>
            <div className="flex h-7 w-7 items-center justify-center rounded-md bg-rose-50 text-rose-600">
              <XCircle className="h-3.5 w-3.5" />
            </div>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold tracking-tight text-rose-700">960</div>
            <p className="text-[11px] text-slate-500 mt-1">
              5.2% unexcused & medical absences
            </p>
          </CardContent>
        </Card>

        <Card className="border-slate-200/80 shadow-xs">
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <span className="text-[11px] font-semibold uppercase tracking-wider text-slate-500">
              Section Submissions
            </span>
            <div className="flex h-7 w-7 items-center justify-center rounded-md bg-blue-50 text-blue-600">
              <Clock className="h-3.5 w-3.5" />
            </div>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold tracking-tight text-slate-900">
              {submittedSections} / {totalSections} ({submissionPercent}%)
            </div>
            <p className="text-[11px] text-amber-600 font-medium mt-1">
              {totalSections - submittedSections} class teachers pending submission
            </p>
          </CardContent>
        </Card>

        <Card className="border-slate-200/80 shadow-xs">
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <span className="text-[11px] font-semibold uppercase tracking-wider text-slate-500">
              Audit Corrections
            </span>
            <div className="flex h-7 w-7 items-center justify-center rounded-md bg-indigo-50 text-indigo-600">
              <History className="h-3.5 w-3.5" />
            </div>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold tracking-tight text-slate-900">3</div>
            <p className="text-[11px] text-slate-500 mt-1">
              Logged in immutable audit trail today
            </p>
          </CardContent>
        </Card>
      </div>

      {/* Tabs */}
      <div className="flex border-b border-slate-200 text-xs font-semibold">
        <button
          onClick={() => setActiveTab("sections")}
          className={cn(
            "pb-2.5 px-3 border-b-2 transition-colors",
            activeTab === "sections"
              ? "border-blue-600 text-blue-600 font-bold"
              : "border-transparent text-slate-500 hover:text-slate-900"
          )}
        >
          Section Roster & Submissions
        </button>
        <button
          onClick={() => setActiveTab("absentees")}
          className={cn(
            "pb-2.5 px-3 border-b-2 transition-colors",
            activeTab === "absentees"
              ? "border-blue-600 text-blue-600 font-bold"
              : "border-transparent text-slate-500 hover:text-slate-900"
          )}
        >
          Today's Absentee Roster (SMS Tracker)
        </button>
        <button
          onClick={() => setActiveTab("audit")}
          className={cn(
            "pb-2.5 px-3 border-b-2 transition-colors",
            activeTab === "audit"
              ? "border-blue-600 text-blue-600 font-bold"
              : "border-transparent text-slate-500 hover:text-slate-900"
          )}
        >
          Immutable Correction Audit Log
        </button>
      </div>

      {/* TAB 1: Section Submissions */}
      {activeTab === "sections" && (
        <Card className="border-slate-200/80 shadow-xs">
          <CardHeader className="p-4 pb-3 border-b border-slate-100 flex flex-row items-center justify-between">
            <div>
              <CardTitle className="text-sm font-bold text-slate-900">
                Class Attendance Submission Status
              </CardTitle>
              <CardDescription className="text-xs text-slate-500">
                Attendance must be submitted by class teachers before 09:00 AM daily
              </CardDescription>
            </div>
            <Badge variant="outline" className="text-xs font-medium text-slate-600">
              Cut-off: 09:00 AM
            </Badge>
          </CardHeader>
          <CardContent className="p-0">
            <Table>
              <TableHeader>
                <TableRow className="border-slate-100 bg-slate-50/50 text-[11px]">
                  <TableHead className="font-semibold text-slate-600">Class & Section</TableHead>
                  <TableHead className="font-semibold text-slate-600">Assigned Class Teacher</TableHead>
                  <TableHead className="font-semibold text-slate-600 text-center">Strength</TableHead>
                  <TableHead className="font-semibold text-slate-600 text-center">Present</TableHead>
                  <TableHead className="font-semibold text-slate-600 text-center">Absent</TableHead>
                  <TableHead className="font-semibold text-slate-600">Status</TableHead>
                  <TableHead className="font-semibold text-slate-600 text-right">Actions</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {sectionSubmissions.map((sec) => (
                  <TableRow key={sec.id} className="border-slate-100 hover:bg-slate-50/60 text-xs">
                    <TableCell className="font-semibold text-slate-900">
                      {sec.gradeSection}
                    </TableCell>
                    <TableCell className="text-slate-700">{sec.classTeacher}</TableCell>
                    <TableCell className="text-center font-mono font-medium text-slate-800">
                      {sec.totalStudents}
                    </TableCell>
                    <TableCell className="text-center font-mono font-medium text-emerald-600">
                      {sec.status === "SUBMITTED" ? sec.present : "-"}
                    </TableCell>
                    <TableCell className="text-center font-mono font-medium text-rose-600">
                      {sec.status === "SUBMITTED" ? sec.absent : "-"}
                    </TableCell>
                    <TableCell>
                      {sec.status === "SUBMITTED" ? (
                        <div className="flex items-center gap-1.5">
                          <Badge className="bg-emerald-50 text-emerald-700 border-emerald-200 text-[10px] px-1.5 py-0 font-medium">
                            Submitted
                          </Badge>
                          <span className="text-[10px] text-slate-400">{sec.submittedAt}</span>
                        </div>
                      ) : (
                        <Badge className="bg-amber-50 text-amber-700 border-amber-200 text-[10px] px-1.5 py-0 font-medium animate-pulse">
                          Pending
                        </Badge>
                      )}
                    </TableCell>
                    <TableCell className="text-right">
                      {sec.status === "PENDING" ? (
                        <Button
                          variant="outline"
                          size="sm"
                          className="h-7 text-[11px] text-amber-700 border-amber-300 hover:bg-amber-50 px-2"
                        >
                          Ping Teacher
                        </Button>
                      ) : (
                        <Button
                          variant="ghost"
                          size="sm"
                          className="h-7 text-[11px] text-slate-600 hover:text-slate-900 px-2"
                        >
                          View Sheet
                        </Button>
                      )}
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </CardContent>
        </Card>
      )}

      {/* TAB 2: Absentee Roster */}
      {activeTab === "absentees" && (
        <Card className="border-slate-200/80 shadow-xs">
          <CardHeader className="p-4 pb-3 border-b border-slate-100 flex flex-row items-center justify-between">
            <div>
              <CardTitle className="text-sm font-bold text-slate-900">
                Daily Absentee Dispatch & Notification Roster
              </CardTitle>
              <CardDescription className="text-xs text-slate-500">
                Automated SMS dispatch via BullMQ communication queue
              </CardDescription>
            </div>
            <Button size="sm" className="h-7 text-xs bg-blue-600 hover:bg-blue-700 text-white gap-1">
              <MessageSquare className="h-3 w-3" />
              Broadcast Absence Notice
            </Button>
          </CardHeader>
          <CardContent className="p-0">
            <Table>
              <TableHeader>
                <TableRow className="border-slate-100 bg-slate-50/50 text-[11px]">
                  <TableHead className="font-semibold text-slate-600">Student & Admission #</TableHead>
                  <TableHead className="font-semibold text-slate-600">Class</TableHead>
                  <TableHead className="font-semibold text-slate-600">Guardian Contact</TableHead>
                  <TableHead className="font-semibold text-slate-600">Consecutive Days</TableHead>
                  <TableHead className="font-semibold text-slate-600">SMS Notification</TableHead>
                  <TableHead className="font-semibold text-slate-600 text-right">Actions</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {mockAbsentees.map((abs) => (
                  <TableRow key={abs.id} className="border-slate-100 hover:bg-slate-50/60 text-xs">
                    <TableCell>
                      <div className="font-medium text-slate-900">{abs.studentName}</div>
                      <div className="text-[11px] text-slate-400 font-mono">{abs.admissionNumber}</div>
                    </TableCell>
                    <TableCell className="font-medium text-slate-700">{abs.className}</TableCell>
                    <TableCell>
                      <div className="text-slate-800 font-medium">{abs.guardianName}</div>
                      <div className="text-[11px] text-slate-500 flex items-center gap-1">
                        <Phone className="h-2.5 w-2.5" />
                        {abs.guardianPhone}
                      </div>
                    </TableCell>
                    <TableCell>
                      <Badge
                        variant="outline"
                        className={cn(
                          "text-[10px] font-semibold",
                          abs.consecutiveDays > 2
                            ? "bg-rose-50 text-rose-700 border-rose-300"
                            : "bg-slate-50 text-slate-600"
                        )}
                      >
                        {abs.consecutiveDays} {abs.consecutiveDays === 1 ? "Day" : "Days"}
                      </Badge>
                    </TableCell>
                    <TableCell>
                      <div className="flex items-center gap-1.5 text-emerald-600 font-medium text-[11px]">
                        <CheckCircle2 className="h-3 w-3 shrink-0" />
                        <span>{abs.smsStatus}</span>
                      </div>
                    </TableCell>
                    <TableCell className="text-right">
                      <Button
                        variant="outline"
                        size="sm"
                        className="h-7 text-[11px] text-slate-700 hover:bg-slate-50 px-2"
                      >
                        Call Guardian
                      </Button>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </CardContent>
        </Card>
      )}

      {/* TAB 3: Correction Audit Log */}
      {activeTab === "audit" && (
        <Card className="border-slate-200/80 shadow-xs">
          <CardHeader className="p-4 border-b border-slate-100">
            <div className="flex items-center justify-between">
              <div>
                <CardTitle className="text-sm font-bold text-slate-900">
                  Attendance Correction Audit Trail
                </CardTitle>
                <CardDescription className="text-xs text-slate-500">
                  Immutable PostgreSQL records with before/after state and mandatory justification reasons
                </CardDescription>
              </div>
              <Badge className="bg-blue-50 text-blue-700 border border-blue-200 text-[10px]">
                PostgreSQL Schema: attendance
              </Badge>
            </div>
          </CardHeader>
          <CardContent className="p-0">
            <Table>
              <TableHeader>
                <TableRow className="border-slate-100 bg-slate-50/50 text-[11px]">
                  <TableHead className="font-semibold text-slate-600">Student & Class</TableHead>
                  <TableHead className="font-semibold text-slate-600">State Transition</TableHead>
                  <TableHead className="font-semibold text-slate-600">Corrected By</TableHead>
                  <TableHead className="font-semibold text-slate-600">Reason</TableHead>
                  <TableHead className="font-semibold text-slate-600 text-right">Timestamp</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {mockCorrections.map((corr) => (
                  <TableRow key={corr.id} className="border-slate-100 hover:bg-slate-50/60 text-xs">
                    <TableCell>
                      <div className="font-medium text-slate-900">{corr.studentName}</div>
                      <div className="text-[11px] text-slate-400">{corr.className}</div>
                    </TableCell>
                    <TableCell>
                      <div className="flex items-center gap-1.5 font-mono text-[11px]">
                        <span className="text-rose-600 font-semibold">{corr.previousStatus}</span>
                        <ArrowRight className="h-3 w-3 text-slate-400" />
                        <span className="text-emerald-600 font-semibold">{corr.correctedStatus}</span>
                      </div>
                    </TableCell>
                    <TableCell className="text-slate-800 font-medium">
                      {corr.correctedBy}
                    </TableCell>
                    <TableCell className="text-slate-600 max-w-xs truncate text-[11px]">
                      {corr.reason}
                    </TableCell>
                    <TableCell className="text-right font-mono text-[11px] text-slate-500">
                      {corr.timestamp}
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </CardContent>
        </Card>
      )}
    </div>
  );
}

