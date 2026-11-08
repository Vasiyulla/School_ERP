"use client";

import React, { useState } from "react";
import {
  CalendarCheck2,
  CheckCircle2,
  XCircle,
  Clock,
  Plus,
  FileText,
  AlertCircle,
  UploadCloud,
  ChevronLeft,
  ChevronRight,
} from "lucide-react";
import { useParentContext } from "@/components/parent/parent-context";
import { MOCK_LEAVE_RECORDS, LeaveRequestRecord } from "@/lib/mock/parent-mock";
import { Button } from "@/components/ui/button";

export default function ParentAttendancePage() {
  const { activeChild } = useParentContext();
  const [leaves, setLeaves] = useState<LeaveRequestRecord[]>(MOCK_LEAVE_RECORDS);
  const [showModal, setShowModal] = useState(false);
  const [leaveType, setLeaveType] = useState<"SICK" | "CASUAL" | "EMERGENCY">("SICK");
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [reason, setReason] = useState("");
  const [submitted, setSubmitted] = useState(false);

  const childLeaves = leaves.filter((l) => l.childId === activeChild.id);

  // Generate simulated calendar days for November 2026
  const daysInMonth = Array.from({ length: 30 }, (_, i) => {
    const day = i + 1;
    const isWeekend = (day % 7 === 1) || (day % 7 === 0);
    // Mark day 12 as absent/leave
    const isLeave = day === 12;
    const isHoliday = day === 14; // Diwali/School Holiday
    return {
      day,
      status: isWeekend ? "WEEKEND" : isHoliday ? "HOLIDAY" : isLeave ? "ABSENT" : "PRESENT",
    };
  });

  const handleApplyLeave = (e: React.FormEvent) => {
    e.preventDefault();
    if (!startDate || !endDate || !reason) return;

    const newLeave: LeaveRequestRecord = {
      id: "leave-" + Date.now(),
      childId: activeChild.id,
      childName: activeChild.fullName,
      leaveType,
      startDate,
      endDate,
      reason,
      status: "PENDING",
      appliedDate: new Date().toISOString().split("T")[0],
    };

    setLeaves([newLeave, ...leaves]);
    setSubmitted(true);
    setTimeout(() => {
      setShowModal(false);
      setSubmitted(false);
      setStartDate("");
      setEndDate("");
      setReason("");
    }, 1200);
  };

  return (
    <div className="mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8 space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-xl sm:text-2xl font-bold tracking-tight">Attendance Register & Leave Notes</h1>
          <p className="text-xs text-muted-foreground mt-0.5">
            Ward: <strong className="text-foreground">{activeChild.fullName}</strong> ({activeChild.gradeLevel}-{activeChild.section}) • Bi-Temporal Audit Compliant
          </p>
        </div>

        <Button
          onClick={() => setShowModal(true)}
          size="sm"
          className="bg-emerald-600 hover:bg-emerald-700 text-white text-xs gap-1.5 h-8 shadow-sm"
        >
          <Plus className="h-3.5 w-3.5" /> Submit Leave Application
        </Button>
      </div>

      {/* Monthly Attendance Metrics */}
      <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
        <div className="rounded-2xl border border-border/80 bg-card p-4 shadow-sm">
          <div className="text-xs text-muted-foreground font-medium mb-1">Attendance Ratio</div>
          <div className="text-2xl font-bold font-mono text-emerald-600 dark:text-emerald-400">
            {activeChild.attendancePercentage}%
          </div>
          <div className="text-xs text-muted-foreground mt-1">
            Compliant with CBSE 75% rule
          </div>
        </div>

        <div className="rounded-2xl border border-border/80 bg-card p-4 shadow-sm">
          <div className="text-xs text-muted-foreground font-medium mb-1">Days Present</div>
          <div className="text-2xl font-bold font-mono text-foreground">
            21 <span className="text-sm font-normal text-muted-foreground">/ 22 Sessions</span>
          </div>
          <div className="text-xs text-muted-foreground mt-1">
            Working days this month
          </div>
        </div>

        <div className="rounded-2xl border border-border/80 bg-card p-4 shadow-sm">
          <div className="text-xs text-muted-foreground font-medium mb-1">Excused Leaves</div>
          <div className="text-2xl font-bold font-mono text-foreground">
            1 Day
          </div>
          <div className="text-xs text-muted-foreground mt-1">
            Doctor sick leave approved
          </div>
        </div>

        <div className="rounded-2xl border border-border/80 bg-card p-4 shadow-sm">
          <div className="text-xs text-muted-foreground font-medium mb-1">Unexcused Absences</div>
          <div className="text-2xl font-bold font-mono text-emerald-600 dark:text-emerald-400">
            0 Days
          </div>
          <div className="text-xs text-muted-foreground mt-1">
            Zero unexcused absence
          </div>
        </div>
      </div>

      {/* Interactive Monthly Attendance Grid */}
      <div className="rounded-2xl border border-border/80 bg-card p-5 sm:p-6 shadow-sm space-y-4">
        <div className="flex items-center justify-between border-b border-border pb-3">
          <div className="flex items-center gap-2">
            <CalendarCheck2 className="h-4 w-4 text-emerald-600 dark:text-emerald-400" />
            <h2 className="text-sm font-semibold tracking-tight">November 2026 Attendance Calendar</h2>
          </div>
          <div className="flex items-center gap-4 text-xs">
            <span className="flex items-center gap-1.5"><span className="h-2.5 w-2.5 rounded-full bg-emerald-500" /> Present</span>
            <span className="flex items-center gap-1.5"><span className="h-2.5 w-2.5 rounded-full bg-rose-500" /> Leave</span>
            <span className="flex items-center gap-1.5"><span className="h-2.5 w-2.5 rounded-full bg-blue-500" /> Holiday</span>
            <span className="flex items-center gap-1.5"><span className="h-2.5 w-2.5 rounded-full bg-muted-foreground/40" /> Weekend</span>
          </div>
        </div>

        {/* 7-column calendar grid */}
        <div className="grid grid-cols-7 gap-2 text-center text-xs">
          {["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"].map((day) => (
            <div key={day} className="font-mono text-muted-foreground font-semibold py-1">
              {day}
            </div>
          ))}

          {daysInMonth.map((item) => {
            const isPresent = item.status === "PRESENT";
            const isAbsent = item.status === "ABSENT";
            const isHoliday = item.status === "HOLIDAY";
            const isWeekend = item.status === "WEEKEND";

            return (
              <div
                key={item.day}
                className={`rounded-xl border p-2.5 flex flex-col items-center justify-between min-h-[58px] transition-all ${
                  isPresent
                    ? "border-emerald-500/30 bg-emerald-500/5 hover:bg-emerald-500/10"
                    : isAbsent
                    ? "border-rose-500/40 bg-rose-500/10 text-rose-600 dark:text-rose-400 font-bold"
                    : isHoliday
                    ? "border-blue-500/30 bg-blue-500/10 text-blue-600 dark:text-blue-400"
                    : "border-border/40 bg-muted/20 text-muted-foreground"
                }`}
              >
                <span className="font-mono font-medium text-xs">{item.day}</span>
                <span className="text-[10px] font-semibold mt-1">
                  {isPresent ? "Present" : isAbsent ? "Medical" : isHoliday ? "Holiday" : "Off"}
                </span>
              </div>
            );
          })}
        </div>
      </div>

      {/* Leave Application History Table */}
      <div className="rounded-2xl border border-border/80 bg-card p-5 sm:p-6 shadow-sm">
        <div className="flex items-center justify-between mb-4">
          <div>
            <h2 className="text-base font-semibold tracking-tight flex items-center gap-2">
              <FileText className="h-4 w-4 text-emerald-600 dark:text-emerald-400" />
              Digital Leave Notes History
            </h2>
            <p className="text-xs text-muted-foreground">Audit records reviewed by Class Teacher</p>
          </div>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left text-xs">
            <thead>
              <tr className="border-b border-border text-muted-foreground font-mono">
                <th className="pb-3 font-medium">Leave Type</th>
                <th className="pb-3 font-medium">Date Range</th>
                <th className="pb-3 font-medium">Reason Submitted</th>
                <th className="pb-3 font-medium">Reviewed By</th>
                <th className="pb-3 font-medium text-right">Status</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-border/60">
              {childLeaves.map((leave) => (
                <tr key={leave.id} className="hover:bg-muted/30 transition-colors">
                  <td className="py-3 font-semibold text-foreground">{leave.leaveType}</td>
                  <td className="py-3 font-mono text-muted-foreground">
                    {leave.startDate} to {leave.endDate}
                  </td>
                  <td className="py-3 text-muted-foreground max-w-sm">{leave.reason}</td>
                  <td className="py-3 text-muted-foreground">{leave.reviewedBy || "Pending Review"}</td>
                  <td className="py-3 text-right">
                    <span
                      className={`inline-block rounded-full px-2.5 py-0.5 text-[10px] font-bold ${
                        leave.status === "APPROVED"
                          ? "bg-emerald-500/15 text-emerald-600 dark:text-emerald-400"
                          : "bg-amber-500/15 text-amber-600 dark:text-amber-400"
                      }`}
                    >
                      {leave.status}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Apply Leave Modal */}
      {showModal && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/60 backdrop-blur-sm p-4">
          <div className="w-full max-w-md rounded-2xl border border-border bg-card p-6 shadow-2xl space-y-4 animate-in fade-in zoom-in-95">
            <div className="flex items-center justify-between border-b border-border pb-3">
              <div>
                <h3 className="text-base font-bold text-foreground">Apply Student Leave Note</h3>
                <p className="text-xs text-muted-foreground">For {activeChild.fullName} ({activeChild.gradeLevel})</p>
              </div>
              <button
                onClick={() => setShowModal(false)}
                className="text-muted-foreground hover:text-foreground text-sm font-semibold"
              >
                ✕
              </button>
            </div>

            {!submitted ? (
              <form onSubmit={handleApplyLeave} className="space-y-4">
                <div className="space-y-1.5">
                  <label className="text-xs font-semibold text-muted-foreground uppercase tracking-wider">Leave Type</label>
                  <select
                    value={leaveType}
                    onChange={(e: any) => setLeaveType(e.target.value)}
                    className="w-full rounded-xl border border-border bg-muted/40 p-2.5 text-xs focus:ring-1 focus:ring-emerald-500"
                  >
                    <option value="SICK">Medical / Sick Leave</option>
                    <option value="CASUAL">Personal / Family Emergency</option>
                    <option value="EMERGENCY">Urgent Extenuating Circumstances</option>
                  </select>
                </div>

                <div className="grid grid-cols-2 gap-3">
                  <div className="space-y-1.5">
                    <label className="text-xs font-semibold text-muted-foreground uppercase tracking-wider">Start Date</label>
                    <input
                      type="date"
                      required
                      value={startDate}
                      onChange={(e) => setStartDate(e.target.value)}
                      className="w-full rounded-xl border border-border bg-muted/40 p-2.5 text-xs font-mono"
                    />
                  </div>
                  <div className="space-y-1.5">
                    <label className="text-xs font-semibold text-muted-foreground uppercase tracking-wider">End Date</label>
                    <input
                      type="date"
                      required
                      value={endDate}
                      onChange={(e) => setEndDate(e.target.value)}
                      className="w-full rounded-xl border border-border bg-muted/40 p-2.5 text-xs font-mono"
                    />
                  </div>
                </div>

                <div className="space-y-1.5">
                  <label className="text-xs font-semibold text-muted-foreground uppercase tracking-wider">Reason</label>
                  <textarea
                    rows={3}
                    required
                    value={reason}
                    onChange={(e) => setReason(e.target.value)}
                    placeholder="Provide details for class teacher review..."
                    className="w-full rounded-xl border border-border bg-muted/40 p-2.5 text-xs focus:ring-1 focus:ring-emerald-500"
                  />
                </div>

                <div className="rounded-xl border border-dashed border-border p-3 text-center space-y-1">
                  <UploadCloud className="h-5 w-5 mx-auto text-muted-foreground" />
                  <div className="text-xs text-muted-foreground">Attach Doctor's Prescription (Optional)</div>
                  <div className="text-[10px] text-muted-foreground font-mono">PDF, PNG, JPG up to 5MB</div>
                </div>

                <div className="pt-2">
                  <Button type="submit" className="w-full bg-emerald-600 hover:bg-emerald-700 text-white text-xs py-2.5">
                    Submit Application to Dr. Vikram Seth
                  </Button>
                </div>
              </form>
            ) : (
              <div className="text-center py-6 space-y-2">
                <CheckCircle2 className="h-8 w-8 text-emerald-500 mx-auto" />
                <h4 className="text-base font-bold">Leave Submitted!</h4>
                <p className="text-xs text-muted-foreground">Your request has been routed to the class teacher for bi-temporal attendance auditing.</p>
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  );
}

