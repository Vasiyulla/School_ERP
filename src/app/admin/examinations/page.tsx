"use client";

import * as React from "react";
import {
  Award,
  Calendar,
  FileSpreadsheet,
  CheckCircle2,
  Clock,
  Printer,
  FileText,
  Upload,
  Download,
  Plus,
  ArrowUpRight,
  Sparkles,
  Layers,
} from "lucide-react";
import { Button } from "@/components/ui/button";
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
import { ScheduleExamModal } from "@/components/admin/modals/schedule-exam-modal";

interface ExamTerm {
  id: string;
  name: string;
  academicTerm: string;
  startDate: string;
  endDate: string;
  gradeLevels: string;
  status: "SCHEDULED" | "IN_PROGRESS" | "EVALUATING" | "PUBLISHED";
  marksVerifiedPercent: number;
}

const examTerms: ExamTerm[] = [
  {
    id: "ex-1",
    name: "Term 1 Mid-Term Assessments",
    academicTerm: "AY 2026-27 • Semester 1",
    startDate: "2026-09-15",
    endDate: "2026-09-28",
    gradeLevels: "Grades 1 – 12",
    status: "PUBLISHED",
    marksVerifiedPercent: 100,
  },
  {
    id: "ex-2",
    name: "Pre-Board Examination I (CBSE)",
    academicTerm: "AY 2026-27 • Semester 2",
    startDate: "2026-11-20",
    endDate: "2026-12-05",
    gradeLevels: "Grades 10 & 12",
    status: "SCHEDULED",
    marksVerifiedPercent: 0,
  },
  {
    id: "ex-3",
    name: "Term 2 Mid-Semester Assessments",
    academicTerm: "AY 2026-27 • Semester 2",
    startDate: "2026-11-01",
    endDate: "2026-11-12",
    gradeLevels: "Grades 6 – 9 & 11",
    status: "EVALUATING",
    marksVerifiedPercent: 78,
  },
];

const mockSubjectMarks = [
  { classSubject: "Grade 10-A • Mathematics", teacher: "Dr. Vikram Seth", totalStudents: 42, entered: 42, status: "VERIFIED", avgScore: "84.2%" },
  { classSubject: "Grade 10-A • Science (Theory+Lab)", teacher: "Pooja Hegde", totalStudents: 42, entered: 42, status: "VERIFIED", avgScore: "81.6%" },
  { classSubject: "Grade 10-B • Mathematics", teacher: "Ananya Deshmukh", totalStudents: 40, entered: 32, status: "IN_PROGRESS", avgScore: "76.4%" },
  { classSubject: "Grade 12-A • Physics (Theory+Practical)", teacher: "Meera Nair", totalStudents: 36, entered: 36, status: "VERIFIED", avgScore: "88.1%" },
  { classSubject: "Grade 12-A • Chemistry", teacher: "Rajesh Kulkarni", totalStudents: 36, entered: 28, status: "IN_PROGRESS", avgScore: "82.0%" },
  { classSubject: "Grade 11-B • Accountancy", teacher: "Sunil Gavaskar", totalStudents: 44, entered: 44, status: "VERIFIED", avgScore: "79.5%" },
];

const gradeBands = [
  { grade: "A1", minScore: 91, maxScore: 100, gpaPoint: "10.0", description: "Outstanding performance" },
  { grade: "A2", minScore: 81, maxScore: 90, gpaPoint: "9.0", description: "Excellent analytical mastery" },
  { grade: "B1", minScore: 71, maxScore: 80, gpaPoint: "8.0", description: "Very good comprehension" },
  { grade: "B2", minScore: 61, maxScore: 70, gpaPoint: "7.0", description: "Good subject competence" },
  { grade: "C1", minScore: 51, maxScore: 60, gpaPoint: "6.0", description: "Satisfactory standard" },
  { grade: "C2", minScore: 41, maxScore: 50, gpaPoint: "5.0", description: "Average standard" },
  { grade: "D", minScore: 33, maxScore: 40, gpaPoint: "4.0", description: "Pass mark threshold" },
  { grade: "E", minScore: 0, maxScore: 32, gpaPoint: "0.0", description: "Needs essential remediation" },
];

export default function AdminExaminationsPage() {
  const [activeTab, setActiveTab] = React.useState<"exams" | "markEntry" | "gradeBands">("exams");
  const [isScheduleOpen, setIsScheduleOpen] = React.useState(false);

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between border-b border-slate-200/80 pb-5">
        <div>
          <div className="flex items-center gap-2.5">
            <h1 className="text-xl font-bold tracking-tight text-slate-900">
              Examinations & Assessment Center
            </h1>
            <Badge variant="outline" className="text-[11px] font-semibold text-blue-700 bg-blue-50 border-blue-200">
              CBSE Framework
            </Badge>
          </div>
          <p className="text-xs text-slate-500 mt-1">
            Term exam date sheets, multi-component assessment weighting (Theory + Practical), and automated report cards.
          </p>
        </div>
        <div className="flex items-center gap-2">
          <Button variant="outline" size="sm" className="h-8 gap-1.5 text-xs text-slate-700">
            <Download className="h-3.5 w-3.5" />
            Export Tabulation Sheets
          </Button>
          <Button
            size="sm"
            onClick={() => setIsScheduleOpen(true)}
            className="h-8 gap-1.5 text-xs bg-blue-600 hover:bg-blue-700 text-white shadow-xs"
          >
            <Plus className="h-3.5 w-3.5" />
            Schedule Exam Term
          </Button>
        </div>
      </div>

      {/* KPI Stats */}
      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
        <Card className="border-slate-200/80 shadow-xs">
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <span className="text-[11px] font-semibold uppercase tracking-wider text-slate-500">
              Active Exam Cycle
            </span>
            <div className="flex h-7 w-7 items-center justify-center rounded-md bg-blue-50 text-blue-600">
              <Calendar className="h-3.5 w-3.5" />
            </div>
          </CardHeader>
          <CardContent>
            <div className="text-base font-bold tracking-tight text-slate-900">Term 2 Mid-Semester</div>
            <p className="text-[11px] text-slate-500 mt-1">Grades 6–9 & 11 • In Evaluation</p>
          </CardContent>
        </Card>

        <Card className="border-slate-200/80 shadow-xs">
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <span className="text-[11px] font-semibold uppercase tracking-wider text-slate-500">
              Marks Verified
            </span>
            <div className="flex h-7 w-7 items-center justify-center rounded-md bg-emerald-50 text-emerald-600">
              <CheckCircle2 className="h-3.5 w-3.5" />
            </div>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold tracking-tight text-emerald-700">78.4%</div>
            <p className="text-[11px] text-slate-500 mt-1">32 of 41 class subjects fully signed off</p>
          </CardContent>
        </Card>

        <Card className="border-slate-200/80 shadow-xs">
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <span className="text-[11px] font-semibold uppercase tracking-wider text-slate-500">
              Upcoming Pre-Boards
            </span>
            <div className="flex h-7 w-7 items-center justify-center rounded-md bg-amber-50 text-amber-600">
              <Clock className="h-3.5 w-3.5" />
            </div>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold tracking-tight text-slate-900">Nov 20</div>
            <p className="text-[11px] text-amber-600 font-medium mt-1">12 days until Board Mock cycle</p>
          </CardContent>
        </Card>

        <Card className="border-slate-200/80 shadow-xs">
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <span className="text-[11px] font-semibold uppercase tracking-wider text-slate-500">
              Report Cards Ready
            </span>
            <div className="flex h-7 w-7 items-center justify-center rounded-md bg-indigo-50 text-indigo-600">
              <Award className="h-3.5 w-3.5" />
            </div>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold tracking-tight text-slate-900">18,450</div>
            <p className="text-[11px] text-emerald-600 font-medium mt-1">Term 1 archive published</p>
          </CardContent>
        </Card>
      </div>

      {/* Tabs */}
      <div className="flex border-b border-slate-200 text-xs font-semibold">
        <button
          onClick={() => setActiveTab("exams")}
          className={cn(
            "pb-2.5 px-3 border-b-2 transition-colors",
            activeTab === "exams"
              ? "border-blue-600 text-blue-600 font-bold"
              : "border-transparent text-slate-500 hover:text-slate-900"
          )}
        >
          Scheduled Exam Terms
        </button>
        <button
          onClick={() => setActiveTab("markEntry")}
          className={cn(
            "pb-2.5 px-3 border-b-2 transition-colors",
            activeTab === "markEntry"
              ? "border-blue-600 text-blue-600 font-bold"
              : "border-transparent text-slate-500 hover:text-slate-900"
          )}
        >
          Teacher Mark Submission Status
        </button>
        <button
          onClick={() => setActiveTab("gradeBands")}
          className={cn(
            "pb-2.5 px-3 border-b-2 transition-colors",
            activeTab === "gradeBands"
              ? "border-blue-600 text-blue-600 font-bold"
              : "border-transparent text-slate-500 hover:text-slate-900"
          )}
        >
          CBSE Grade Bands & Grading Rules
        </button>
      </div>

      {/* TAB 1: Scheduled Exams */}
      {activeTab === "exams" && (
        <Card className="border-slate-200/80 shadow-xs">
          <CardHeader className="p-4 pb-3 border-b border-slate-100 flex flex-row items-center justify-between">
            <div>
              <CardTitle className="text-sm font-bold text-slate-900">
                Academic Examination Terms
              </CardTitle>
              <CardDescription className="text-xs text-slate-500">
                Examination schedules across all registered curricula frameworks
              </CardDescription>
            </div>
          </CardHeader>
          <CardContent className="p-0">
            <Table>
              <TableHeader>
                <TableRow className="border-slate-100 bg-slate-50/50 text-[11px]">
                  <TableHead className="font-semibold text-slate-600">Exam Title</TableHead>
                  <TableHead className="font-semibold text-slate-600">Academic Term</TableHead>
                  <TableHead className="font-semibold text-slate-600">Target Grades</TableHead>
                  <TableHead className="font-semibold text-slate-600">Date Range</TableHead>
                  <TableHead className="font-semibold text-slate-600">Marks Verified</TableHead>
                  <TableHead className="font-semibold text-slate-600">Status</TableHead>
                  <TableHead className="font-semibold text-slate-600 text-right">Actions</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {examTerms.map((exam) => (
                  <TableRow key={exam.id} className="border-slate-100 hover:bg-slate-50/60 text-xs">
                    <TableCell className="font-semibold text-slate-900">{exam.name}</TableCell>
                    <TableCell className="text-slate-600 text-[11px]">{exam.academicTerm}</TableCell>
                    <TableCell className="text-slate-700">{exam.gradeLevels}</TableCell>
                    <TableCell className="font-mono text-[11px] text-slate-600">
                      {exam.startDate} → {exam.endDate}
                    </TableCell>
                    <TableCell>
                      <div className="flex items-center gap-2">
                        <div className="h-1.5 w-16 bg-slate-100 rounded-full overflow-hidden">
                          <div
                            className={cn(
                              "h-full rounded-full",
                              exam.marksVerifiedPercent === 100 ? "bg-emerald-500" : "bg-blue-500"
                            )}
                            style={{ width: `${exam.marksVerifiedPercent}%` }}
                          />
                        </div>
                        <span className="font-mono text-[11px] font-medium text-slate-700">
                          {exam.marksVerifiedPercent}%
                        </span>
                      </div>
                    </TableCell>
                    <TableCell>
                      {exam.status === "PUBLISHED" && (
                        <Badge className="bg-emerald-50 text-emerald-700 border-emerald-200 text-[10px] px-2 py-0 font-medium">
                          Published
                        </Badge>
                      )}
                      {exam.status === "EVALUATING" && (
                        <Badge className="bg-blue-50 text-blue-700 border-blue-200 text-[10px] px-2 py-0 font-medium">
                          Evaluating
                        </Badge>
                      )}
                      {exam.status === "SCHEDULED" && (
                        <Badge className="bg-amber-50 text-amber-700 border-amber-200 text-[10px] px-2 py-0 font-medium">
                          Scheduled
                        </Badge>
                      )}
                    </TableCell>
                    <TableCell className="text-right">
                      <Button
                        variant="ghost"
                        size="sm"
                        className="h-7 text-[11px] text-blue-600 hover:text-blue-700 hover:bg-blue-50 px-2"
                      >
                        Date Sheet
                      </Button>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </CardContent>
        </Card>
      )}

      {/* TAB 2: Mark Entry Progress */}
      {activeTab === "markEntry" && (
        <Card className="border-slate-200/80 shadow-xs">
          <CardHeader className="p-4 pb-3 border-b border-slate-100 flex flex-row items-center justify-between">
            <div>
              <CardTitle className="text-sm font-bold text-slate-900">
                Teacher Marks Submission & Verification Status
              </CardTitle>
              <CardDescription className="text-xs text-slate-500">
                Term 2 Mid-Semester Assessments
              </CardDescription>
            </div>
            <Button size="sm" className="h-7 text-xs bg-emerald-600 hover:bg-emerald-700 text-white gap-1">
              <CheckCircle2 className="h-3 w-3" />
              Batch Lock Verified Marks
            </Button>
          </CardHeader>
          <CardContent className="p-0">
            <Table>
              <TableHeader>
                <TableRow className="border-slate-100 bg-slate-50/50 text-[11px]">
                  <TableHead className="font-semibold text-slate-600">Class & Subject</TableHead>
                  <TableHead className="font-semibold text-slate-600">Evaluating Teacher</TableHead>
                  <TableHead className="font-semibold text-slate-600 text-center">Marks Entered</TableHead>
                  <TableHead className="font-semibold text-slate-600 text-right">Class Average</TableHead>
                  <TableHead className="font-semibold text-slate-600">Status</TableHead>
                  <TableHead className="font-semibold text-slate-600 text-right">Actions</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {mockSubjectMarks.map((sm, idx) => (
                  <TableRow key={idx} className="border-slate-100 hover:bg-slate-50/60 text-xs">
                    <TableCell className="font-semibold text-slate-900">{sm.classSubject}</TableCell>
                    <TableCell className="text-slate-700">{sm.teacher}</TableCell>
                    <TableCell className="text-center font-mono font-medium text-slate-800">
                      {sm.entered} / {sm.totalStudents}
                    </TableCell>
                    <TableCell className="text-right font-mono font-bold text-blue-600">
                      {sm.avgScore}
                    </TableCell>
                    <TableCell>
                      {sm.status === "VERIFIED" ? (
                        <Badge className="bg-emerald-50 text-emerald-700 border-emerald-200 text-[10px] px-1.5 py-0 font-medium">
                          Verified
                        </Badge>
                      ) : (
                        <Badge className="bg-amber-50 text-amber-700 border-amber-200 text-[10px] px-1.5 py-0 font-medium">
                          In Progress
                        </Badge>
                      )}
                    </TableCell>
                    <TableCell className="text-right">
                      <Button
                        variant="ghost"
                        size="sm"
                        className="h-7 text-[11px] text-slate-600 hover:text-slate-900 px-2"
                      >
                        Inspect Marks
                      </Button>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </CardContent>
        </Card>
      )}

      {/* TAB 3: Grade Bands */}
      {activeTab === "gradeBands" && (
        <Card className="border-slate-200/80 shadow-xs">
          <CardHeader className="p-4 pb-3 border-b border-slate-100">
            <CardTitle className="text-sm font-bold text-slate-900">
              CBSE 9-Point Grading Scale Scheme
            </CardTitle>
            <CardDescription className="text-xs text-slate-500">
              Automatic score-to-grade translation for final report cards
            </CardDescription>
          </CardHeader>
          <CardContent className="p-0">
            <Table>
              <TableHeader>
                <TableRow className="border-slate-100 bg-slate-50/50 text-[11px]">
                  <TableHead className="font-semibold text-slate-600">Grade Letter</TableHead>
                  <TableHead className="font-semibold text-slate-600">Score Range</TableHead>
                  <TableHead className="font-semibold text-slate-600">Grade Point</TableHead>
                  <TableHead className="font-semibold text-slate-600">Academic Assessment Standard</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {gradeBands.map((gb) => (
                  <TableRow key={gb.grade} className="border-slate-100 hover:bg-slate-50/60 text-xs">
                    <TableCell className="font-bold text-blue-700 font-mono text-sm">{gb.grade}</TableCell>
                    <TableCell className="font-mono text-slate-800">{gb.minScore}% – {gb.maxScore}%</TableCell>
                    <TableCell className="font-mono font-medium text-slate-700">{gb.gpaPoint}</TableCell>
                    <TableCell className="text-slate-600 text-xs">{gb.description}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </CardContent>
        </Card>
      )}
      {/* Schedule Exam Modal */}
      <ScheduleExamModal
        open={isScheduleOpen}
        onOpenChange={setIsScheduleOpen}
      />
    </div>
  );
}

