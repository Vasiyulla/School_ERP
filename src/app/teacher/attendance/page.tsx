"use client";

import * as React from "react";
import {
  CalendarCheck2,
  Clock,
  CheckCircle2,
  XCircle,
  AlertCircle,
  ShieldAlert,
  Send,
  Save,
  Check,
  Search,
  Filter,
  Users,
  Smartphone,
  Info,
} from "lucide-react";
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Input } from "@/components/ui/input";
import {
  Table,
  TableHeader,
  TableBody,
  TableRow,
  TableHead,
  TableCell,
} from "@/components/ui/table";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
  DialogFooter,
} from "@/components/ui/dialog";
import { mockClassRoster, ClassStudentAttendance } from "@/lib/mock/teacher-mock";
import { cn } from "@/lib/utils";

export default function TeacherAttendancePage() {
  const [roster, setRoster] = React.useState<ClassStudentAttendance[]>(mockClassRoster);
  const [searchQuery, setSearchQuery] = React.useState("");
  const [statusFilter, setStatusFilter] = React.useState<string>("ALL");
  const [isSubmitDialogOpen, setIsSubmitDialogOpen] = React.useState(false);
  const [isLocked, setIsLocked] = React.useState(false);
  const [submitSuccess, setSubmitSuccess] = React.useState(false);

  // Status counters
  const presentCount = roster.filter((s) => s.status === "PRESENT").length;
  const absentCount = roster.filter((s) => s.status === "ABSENT").length;
  const lateCount = roster.filter((s) => s.status === "LATE").length;
  const excusedCount = roster.filter((s) => s.status === "EXCUSED").length;

  const handleMarkAllPresent = () => {
    if (isLocked) return;
    setRoster((prev) =>
      prev.map((s) => ({
        ...s,
        // keep pre-approved excused leaves intact
        status: s.preApprovedReason ? "EXCUSED" : "PRESENT",
      }))
    );
  };

  const handleStatusChange = (
    studentId: string,
    newStatus: "PRESENT" | "ABSENT" | "LATE" | "EXCUSED"
  ) => {
    if (isLocked) return;
    setRoster((prev) =>
      prev.map((s) => (s.id === studentId ? { ...s, status: newStatus } : s))
    );
  };

  const handleConfirmSubmit = () => {
    setIsLocked(true);
    setIsSubmitDialogOpen(false);
    setSubmitSuccess(true);
    setTimeout(() => setSubmitSuccess(false), 5000);
  };

  const filteredRoster = roster.filter((student) => {
    const matchesSearch =
      student.fullName.toLowerCase().includes(searchQuery.toLowerCase()) ||
      student.admissionNumber.toLowerCase().includes(searchQuery.toLowerCase()) ||
      student.rollNumber.toString().includes(searchQuery);
    const matchesStatus = statusFilter === "ALL" || student.status === statusFilter;
    return matchesSearch && matchesStatus;
  });

  return (
    <div className="space-y-6 max-w-7xl mx-auto">
      {/* Top Banner & Cutoff Timer */}
      <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4 p-5 rounded-2xl bg-white dark:bg-slate-900 border border-slate-200/90 dark:border-slate-800 shadow-xs">
        <div className="space-y-1">
          <div className="flex items-center gap-2">
            <span className="text-xs font-bold text-emerald-600 dark:text-emerald-400 uppercase tracking-wider">
              Class 10 - Section A
            </span>
            <span className="text-slate-300 dark:text-slate-700">•</span>
            <span className="text-xs font-medium text-slate-500">
              Monday, 08 November 2026
            </span>
          </div>
          <h1 className="text-2xl font-bold tracking-tight text-slate-900 dark:text-slate-50">
            Daily Classroom Roll Call
          </h1>
          <p className="text-xs text-slate-500 dark:text-slate-400">
            High-throughput attendance entry for Class 10-A. Submissions lock automatically at 09:00 AM.
          </p>
        </div>

        <div className="flex flex-wrap items-center gap-2.5">
          {/* Cutoff countdown pill */}
          <div className="flex items-center gap-2 px-3 py-1.5 rounded-xl bg-amber-50 dark:bg-amber-950/40 border border-amber-200/80 text-amber-900 dark:text-amber-200 text-xs font-bold">
            <Clock className="h-4 w-4 text-amber-600 animate-spin" />
            <span>09:00 AM Cutoff:</span>
            <span className="font-mono text-amber-700 dark:text-amber-300">22m remaining</span>
          </div>

          <Button
            onClick={handleMarkAllPresent}
            disabled={isLocked}
            variant="outline"
            className="h-9 text-xs font-semibold border-slate-200 gap-1.5"
          >
            <Check className="h-4 w-4 text-emerald-600" />
            Mark All Present
          </Button>

          <Button
            onClick={() => setIsSubmitDialogOpen(true)}
            disabled={isLocked}
            className="h-9 text-xs font-semibold bg-emerald-600 hover:bg-emerald-700 text-white shadow-xs gap-1.5 px-4"
          >
            <Send className="h-3.5 w-3.5" />
            {isLocked ? "Attendance Locked" : "Submit Official Attendance"}
          </Button>
        </div>
      </div>

      {/* Success Banner */}
      {submitSuccess && (
        <div className="p-4 rounded-xl bg-emerald-50 border border-emerald-200 text-emerald-800 text-xs flex items-center justify-between animate-in fade-in">
          <div className="flex items-center gap-2 font-semibold">
            <CheckCircle2 className="h-4 w-4 text-emerald-600" />
            <span>Official attendance registered for Class 10-A. Automated SMS alerts dispatched to {absentCount} absentee guardians.</span>
          </div>
          <Badge variant="success" className="text-[10px]">
            Locked & Audited
          </Badge>
        </div>
      )}

      {/* Live Metric Badges */}
      <div className="grid grid-cols-2 sm:grid-cols-4 gap-3">
        <div className="p-3 rounded-xl bg-emerald-50/70 border border-emerald-200/80 flex items-center justify-between">
          <div>
            <p className="text-[10px] font-bold uppercase text-emerald-700">Present</p>
            <p className="text-xl font-bold font-mono text-emerald-900">{presentCount}</p>
          </div>
          <CheckCircle2 className="h-5 w-5 text-emerald-600" />
        </div>

        <div className="p-3 rounded-xl bg-rose-50/70 border border-rose-200/80 flex items-center justify-between">
          <div>
            <p className="text-[10px] font-bold uppercase text-rose-700">Absent</p>
            <p className="text-xl font-bold font-mono text-rose-900">{absentCount}</p>
          </div>
          <XCircle className="h-5 w-5 text-rose-600" />
        </div>

        <div className="p-3 rounded-xl bg-amber-50/70 border border-amber-200/80 flex items-center justify-between">
          <div>
            <p className="text-[10px] font-bold uppercase text-amber-700">Late Arrival</p>
            <p className="text-xl font-bold font-mono text-amber-900">{lateCount}</p>
          </div>
          <Clock className="h-5 w-5 text-amber-600" />
        </div>

        <div className="p-3 rounded-xl bg-blue-50/70 border border-blue-200/80 flex items-center justify-between">
          <div>
            <p className="text-[10px] font-bold uppercase text-blue-700">Excused / Olympiad</p>
            <p className="text-xl font-bold font-mono text-blue-900">{excusedCount}</p>
          </div>
          <Info className="h-5 w-5 text-blue-600" />
        </div>
      </div>

      {/* Attendance Table Card */}
      <Card className="shadow-xs border-slate-200/80">
        <CardHeader className="pb-4 border-b border-slate-100 dark:border-slate-800 flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3">
          <div className="relative w-full sm:w-72">
            <Search className="absolute left-2.5 top-1/2 -translate-y-1/2 h-3.5 w-3.5 text-slate-400" />
            <Input
              placeholder="Search roll, name, admission #..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="pl-8 h-8 text-xs bg-slate-50 dark:bg-slate-800 border-slate-200"
            />
          </div>

          <div className="flex items-center gap-1.5 overflow-x-auto pb-1 sm:pb-0">
            {["ALL", "PRESENT", "ABSENT", "LATE", "EXCUSED"].map((st) => (
              <button
                key={st}
                onClick={() => setStatusFilter(st)}
                className={cn(
                  "text-[11px] px-2.5 py-1 rounded-md font-medium transition-colors shrink-0",
                  statusFilter === st
                    ? "bg-emerald-600 text-white font-semibold"
                    : "bg-slate-100 hover:bg-slate-200 text-slate-600 dark:bg-slate-800 dark:text-slate-400"
                )}
              >
                {st}
              </button>
            ))}
          </div>
        </CardHeader>
        <CardContent className="p-0">
          <div className="overflow-x-auto">
            <Table>
              <TableHeader className="bg-slate-50/90 dark:bg-slate-800/60">
                <TableRow>
                  <TableHead className="w-16 text-center text-xs font-bold text-slate-700 dark:text-slate-300">
                    Roll #
                  </TableHead>
                  <TableHead className="text-xs font-bold text-slate-700 dark:text-slate-300">
                    Student Details
                  </TableHead>
                  <TableHead className="w-48 text-xs font-bold text-slate-700 dark:text-slate-300">
                    Guardian & Contact
                  </TableHead>
                  <TableHead className="w-32 text-xs font-bold text-slate-700 dark:text-slate-300">
                    Overall Attn %
                  </TableHead>
                  <TableHead className="w-64 text-center text-xs font-bold text-slate-700 dark:text-slate-300">
                    Today&apos;s Status
                  </TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {filteredRoster.map((student) => (
                  <TableRow
                    key={student.id}
                    className={cn(
                      "hover:bg-slate-50/50 dark:hover:bg-slate-800/30 transition-colors",
                      student.status === "ABSENT" && "bg-rose-50/30 dark:bg-rose-950/20",
                      student.status === "LATE" && "bg-amber-50/30 dark:bg-amber-950/20",
                      student.status === "EXCUSED" && "bg-blue-50/30 dark:bg-blue-950/20"
                    )}
                  >
                    {/* Roll Number */}
                    <TableCell className="text-center font-mono font-bold text-xs text-slate-700 dark:text-slate-300 py-3">
                      #{student.rollNumber}
                    </TableCell>

                    {/* Student Info */}
                    <TableCell className="py-3">
                      <div className="flex flex-col">
                        <span className="text-xs font-bold text-slate-900 dark:text-slate-100">
                          {student.fullName}
                        </span>
                        <div className="flex items-center gap-1.5 text-[10px] text-slate-500 font-mono mt-0.5">
                          <span>{student.admissionNumber}</span>
                          <span>•</span>
                          <span>{student.lockerIndex}</span>
                        </div>
                        {student.preApprovedReason && (
                          <span className="text-[10px] text-blue-600 dark:text-blue-400 font-medium mt-0.5 flex items-center gap-1">
                            <Info className="h-2.5 w-2.5" />
                            {student.preApprovedReason}
                          </span>
                        )}
                      </div>
                    </TableCell>

                    {/* Guardian Contact */}
                    <TableCell className="py-3 text-xs">
                      <div className="flex flex-col">
                        <span className="font-medium text-slate-800 dark:text-slate-200">
                          {student.guardianName}
                        </span>
                        <span className="text-[10px] font-mono text-slate-500 flex items-center gap-1">
                          <Smartphone className="h-2.5 w-2.5 text-slate-400" />
                          {student.guardianPhone}
                        </span>
                      </div>
                    </TableCell>

                    {/* Attendance Pct */}
                    <TableCell className="py-3 text-xs font-mono font-semibold">
                      <span
                        className={cn(
                          student.attendancePct >= 90
                            ? "text-emerald-600"
                            : student.attendancePct >= 75
                            ? "text-amber-600"
                            : "text-rose-600"
                        )}
                      >
                        {student.attendancePct}%
                      </span>
                    </TableCell>

                    {/* Status Toggles */}
                    <TableCell className="py-3 text-center">
                      <div className="inline-flex rounded-lg p-0.5 bg-slate-100 dark:bg-slate-800 border border-slate-200 dark:border-slate-700">
                        <button
                          onClick={() => handleStatusChange(student.id, "PRESENT")}
                          disabled={isLocked}
                          className={cn(
                            "px-2.5 py-1 text-xs font-bold rounded-md transition-all",
                            student.status === "PRESENT"
                              ? "bg-emerald-600 text-white shadow-xs"
                              : "text-slate-600 hover:text-slate-900 dark:text-slate-400"
                          )}
                          title="Mark Present"
                        >
                          P
                        </button>
                        <button
                          onClick={() => handleStatusChange(student.id, "ABSENT")}
                          disabled={isLocked}
                          className={cn(
                            "px-2.5 py-1 text-xs font-bold rounded-md transition-all",
                            student.status === "ABSENT"
                              ? "bg-rose-600 text-white shadow-xs"
                              : "text-slate-600 hover:text-slate-900 dark:text-slate-400"
                          )}
                          title="Mark Absent"
                        >
                          A
                        </button>
                        <button
                          onClick={() => handleStatusChange(student.id, "LATE")}
                          disabled={isLocked}
                          className={cn(
                            "px-2.5 py-1 text-xs font-bold rounded-md transition-all",
                            student.status === "LATE"
                              ? "bg-amber-600 text-white shadow-xs"
                              : "text-slate-600 hover:text-slate-900 dark:text-slate-400"
                          )}
                          title="Mark Late"
                        >
                          L
                        </button>
                        <button
                          onClick={() => handleStatusChange(student.id, "EXCUSED")}
                          disabled={isLocked}
                          className={cn(
                            "px-2.5 py-1 text-xs font-bold rounded-md transition-all",
                            student.status === "EXCUSED"
                              ? "bg-blue-600 text-white shadow-xs"
                              : "text-slate-600 hover:text-slate-900 dark:text-slate-400"
                          )}
                          title="Mark Excused"
                        >
                          E
                        </button>
                      </div>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </div>
        </CardContent>
      </Card>

      {/* Confirmation & SMS Dispatch Modal */}
      <Dialog open={isSubmitDialogOpen} onOpenChange={setIsSubmitDialogOpen}>
        <DialogContent className="max-w-md">
          <DialogHeader>
            <DialogTitle className="text-base font-bold text-slate-900 flex items-center gap-2">
              <ShieldAlert className="h-5 w-5 text-emerald-600" />
              Lock & Dispatch Class 10-A Attendance?
            </DialogTitle>
            <DialogDescription className="text-xs text-slate-500 mt-1">
              Confirming this submission will post immutable records to PostgreSQL <code className="text-emerald-700 font-mono">attendance.attendance_sessions</code> and trigger automated parent SMS alerts.
            </DialogDescription>
          </DialogHeader>

          <div className="p-3 rounded-xl bg-slate-50 dark:bg-slate-800/60 border border-slate-200 text-xs space-y-2">
            <div className="flex justify-between font-semibold">
              <span>Present Students:</span>
              <span className="text-emerald-600 font-bold">{presentCount}</span>
            </div>
            <div className="flex justify-between font-semibold">
              <span>Absent Students (Alerts queued):</span>
              <span className="text-rose-600 font-bold">{absentCount}</span>
            </div>
            <div className="flex justify-between font-semibold">
              <span>Late Arrivals:</span>
              <span className="text-amber-600 font-bold">{lateCount}</span>
            </div>
            <div className="flex justify-between font-semibold">
              <span>Excused / Competitions:</span>
              <span className="text-blue-600 font-bold">{excusedCount}</span>
            </div>
          </div>

          <DialogFooter className="gap-2 sm:gap-0">
            <Button variant="outline" size="sm" onClick={() => setIsSubmitDialogOpen(false)}>
              Cancel
            </Button>
            <Button
              size="sm"
              onClick={handleConfirmSubmit}
              className="bg-emerald-600 hover:bg-emerald-700 text-white font-semibold"
            >
              Confirm & Lock Attendance
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
}

