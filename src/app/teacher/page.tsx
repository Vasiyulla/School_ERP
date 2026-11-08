"use client";

import * as React from "react";
import Link from "next/link";
import {
  CalendarCheck2,
  Clock,
  Award,
  BookOpen,
  Users,
  AlertTriangle,
  CheckCircle2,
  ArrowRight,
  TrendingUp,
  FileText,
  Sparkles,
  MapPin,
  Calendar,
  MessageSquare,
} from "lucide-react";
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import {
  mockTeacherProfile,
  mockTodaySchedule,
  mockClassRoster,
  mockTeacherAssignments,
} from "@/lib/mock/teacher-mock";
import { cn } from "@/lib/utils";

export default function TeacherCockpitPage() {
  const currentPeriod = mockTodaySchedule.find((p) => p.isCurrent);
  const presentCount = mockClassRoster.filter((s) => s.status === "PRESENT").length;
  const absentCount = mockClassRoster.filter((s) => s.status === "ABSENT").length;
  const lateCount = mockClassRoster.filter((s) => s.status === "LATE").length;
  const excusedCount = mockClassRoster.filter((s) => s.status === "EXCUSED").length;

  return (
    <div className="space-y-6 max-w-7xl mx-auto">
      {/* Welcome Banner & Class Teacher Status */}
      <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4 p-5 rounded-2xl bg-gradient-to-r from-emerald-900 via-slate-900 to-slate-900 text-white shadow-xs">
        <div className="space-y-1">
          <div className="flex items-center gap-2">
            <span className="text-xs font-bold uppercase tracking-wider text-emerald-400">
              Faculty Workspace
            </span>
            <span className="text-slate-500">•</span>
            <span className="text-xs text-slate-300 font-medium">AY 2026-27 • Semester 2</span>
          </div>
          <h1 className="text-xl sm:text-2xl font-bold tracking-tight">
            Welcome back, {mockTeacherProfile.name}
          </h1>
          <p className="text-xs sm:text-sm text-slate-300 font-normal">
            Class Teacher for <strong className="text-emerald-300">Class 10 - Section A</strong> ({mockTeacherProfile.totalStudentsInClass} students) • Senior PGT Mathematics
          </p>
        </div>

        <div className="flex flex-wrap items-center gap-2.5">
          <Link href="/teacher/attendance">
            <Button className="bg-emerald-600 hover:bg-emerald-500 text-white font-semibold text-xs h-9 px-4 shadow-sm gap-1.5">
              <CalendarCheck2 className="h-4 w-4" />
              Take Roll Call (10-A)
            </Button>
          </Link>
          <Link href="/teacher/gradebook">
            <Button variant="outline" className="text-white border-slate-700 hover:bg-slate-800 text-xs h-9 px-3 gap-1.5">
              <Award className="h-3.5 w-3.5 text-amber-400" />
              Input Exam Marks
            </Button>
          </Link>
        </div>
      </div>

      {/* KPI Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        {/* Attendance KPI */}
        <Card className="shadow-xs border-slate-200/80">
          <CardContent className="p-4 flex items-center justify-between">
            <div>
              <p className="text-[10px] font-bold uppercase tracking-wider text-slate-400">
                10-A Today&apos;s Roll Call
              </p>
              <div className="flex items-baseline gap-2 mt-1">
                <span className="text-2xl font-bold font-mono text-slate-900 dark:text-slate-50">
                  {presentCount} / {mockClassRoster.length}
                </span>
                <span className="text-xs font-bold text-emerald-600">
                  {((presentCount / mockClassRoster.length) * 100).toFixed(0)}% Present
                </span>
              </div>
              <p className="text-[11px] text-slate-500 mt-1">
                {absentCount} Absent • {lateCount} Late • {excusedCount} Excused
              </p>
            </div>
            <div className="p-3 rounded-xl bg-emerald-50 text-emerald-600 dark:bg-emerald-950/60 dark:text-emerald-400 border border-emerald-200/60">
              <CalendarCheck2 className="h-5 w-5" />
            </div>
          </CardContent>
        </Card>

        {/* Current Period KPI */}
        <Card className="shadow-xs border-slate-200/80">
          <CardContent className="p-4 flex items-center justify-between">
            <div>
              <p className="text-[10px] font-bold uppercase tracking-wider text-slate-400">
                Current Class Period
              </p>
              <div className="flex items-baseline gap-2 mt-1">
                <span className="text-base font-bold text-slate-900 dark:text-slate-50 truncate">
                  Period {currentPeriod?.periodNumber}: Calculus
                </span>
              </div>
              <p className="text-[11px] text-blue-600 dark:text-blue-400 font-semibold mt-1 flex items-center gap-1">
                <MapPin className="h-3 w-3" />
                {currentPeriod?.roomNumber} • {currentPeriod?.className} {currentPeriod?.sectionName}
              </p>
            </div>
            <div className="p-3 rounded-xl bg-blue-50 text-blue-600 dark:bg-blue-950/60 dark:text-blue-400 border border-blue-200/60">
              <Clock className="h-5 w-5" />
            </div>
          </CardContent>
        </Card>

        {/* Active Assignments */}
        <Card className="shadow-xs border-slate-200/80">
          <CardContent className="p-4 flex items-center justify-between">
            <div>
              <p className="text-[10px] font-bold uppercase tracking-wider text-slate-400">
                Homework Submissions
              </p>
              <div className="flex items-baseline gap-2 mt-1">
                <span className="text-2xl font-bold font-mono text-slate-900 dark:text-slate-50">
                  {mockTeacherAssignments.reduce((acc, a) => acc + a.totalSubmissions, 0)}
                </span>
                <span className="text-xs font-semibold text-slate-500">
                  across 3 tasks
                </span>
              </div>
              <p className="text-[11px] text-amber-600 font-medium mt-1">
                17 pending evaluation
              </p>
            </div>
            <div className="p-3 rounded-xl bg-amber-50 text-amber-600 dark:bg-amber-950/60 dark:text-amber-400 border border-amber-200/60">
              <BookOpen className="h-5 w-5" />
            </div>
          </CardContent>
        </Card>

        {/* Gradebook Status */}
        <Card className="shadow-xs border-slate-200/80">
          <CardContent className="p-4 flex items-center justify-between">
            <div>
              <p className="text-[10px] font-bold uppercase tracking-wider text-slate-400">
                Term 1 Mathematics
              </p>
              <div className="flex items-baseline gap-2 mt-1">
                <span className="text-2xl font-bold font-mono text-slate-900 dark:text-slate-50">
                  38 / 38
                </span>
                <span className="text-xs font-bold text-emerald-600">
                  Draft Saved
                </span>
              </div>
              <p className="text-[11px] text-slate-500 mt-1">
                Class Mean: 83.2% (Grade A2)
              </p>
            </div>
            <div className="p-3 rounded-xl bg-purple-50 text-purple-600 dark:bg-purple-950/60 dark:text-purple-400 border border-purple-200/60">
              <Award className="h-5 w-5" />
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Main 2-Column Grid: Today's Schedule & Class 10-A Attention Feed */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Left Column (2 Cols): Today's Period Track */}
        <Card className="lg:col-span-2 shadow-xs border-slate-200/80">
          <CardHeader className="pb-3 border-b border-slate-100 dark:border-slate-800 flex flex-row items-center justify-between">
            <div>
              <CardTitle className="text-base font-semibold text-slate-900 dark:text-slate-50 flex items-center gap-2">
                <Clock className="h-4 w-4 text-blue-600" />
                Today&apos;s Teaching Schedule (Monday)
              </CardTitle>
              <CardDescription className="text-xs text-slate-500">
                Real-time period progression, room allocations & assigned substitutions
              </CardDescription>
            </div>
            <Link href="/teacher/schedule">
              <Button variant="ghost" size="sm" className="text-xs text-blue-600 gap-1 h-7">
                Full Week <ArrowRight className="h-3 w-3" />
              </Button>
            </Link>
          </CardHeader>
          <CardContent className="p-4 space-y-2.5">
            {mockTodaySchedule.map((period) => (
              <div
                key={period.periodNumber}
                className={cn(
                  "flex items-center justify-between p-3 rounded-xl border transition-all",
                  period.isCurrent
                    ? "bg-blue-50/70 border-blue-300 dark:bg-blue-950/40 dark:border-blue-800 ring-1 ring-blue-400/50"
                    : period.isProxy
                    ? "bg-amber-50/50 border-amber-200 dark:bg-amber-950/30"
                    : period.isFree
                    ? "bg-slate-50/50 border-slate-200/70 dark:bg-slate-800/30 border-dashed"
                    : "bg-white dark:bg-slate-900 border-slate-200/80 hover:border-slate-300"
                )}
              >
                <div className="flex items-center gap-3">
                  <div
                    className={cn(
                      "flex flex-col items-center justify-center h-10 w-12 rounded-lg font-mono text-xs font-bold border",
                      period.isCurrent
                        ? "bg-blue-600 text-white border-blue-600 shadow-xs"
                        : "bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 border-slate-200 dark:border-slate-700"
                    )}
                  >
                    <span>P{period.periodNumber}</span>
                  </div>
                  <div>
                    <div className="flex items-center gap-2">
                      <span className="text-xs font-bold text-slate-900 dark:text-slate-100">
                        {period.subject}
                      </span>
                      {period.isCurrent && (
                        <Badge variant="info" className="text-[10px] py-0 px-1.5 animate-pulse">
                          Active Now
                        </Badge>
                      )}
                      {period.isProxy && (
                        <Badge variant="warning" className="text-[10px] py-0 px-1.5">
                          Substitution
                        </Badge>
                      )}
                      {period.isFree && (
                        <Badge variant="outline" className="text-[10px] py-0 px-1.5 text-slate-400">
                          Planning Hour
                        </Badge>
                      )}
                    </div>
                    <div className="flex items-center gap-2 text-[11px] text-slate-500 mt-0.5">
                      <span className="font-mono">{period.timeSlot}</span>
                      <span>•</span>
                      <span>{period.className} {period.sectionName}</span>
                      <span>•</span>
                      <span className="text-slate-600 dark:text-slate-400 font-medium">
                        {period.roomNumber}
                      </span>
                    </div>
                  </div>
                </div>

                <div className="flex items-center gap-2">
                  {!period.isFree && (
                    <Link
                      href={
                        period.periodNumber === 1
                          ? "/teacher/attendance"
                          : "/teacher/gradebook"
                      }
                    >
                      <Button
                        variant="outline"
                        size="sm"
                        className="h-7 text-[11px] font-semibold border-slate-200"
                      >
                        {period.periodNumber === 1 ? "Roll Call" : "Coursework"}
                      </Button>
                    </Link>
                  )}
                </div>
              </div>
            ))}
          </CardContent>
        </Card>

        {/* Right Column: Class 10-A Urgent Action & Notice Board */}
        <div className="space-y-6">
          {/* Urgent Actions Card */}
          <Card className="shadow-xs border-slate-200/80">
            <CardHeader className="pb-3 border-b border-slate-100 dark:border-slate-800">
              <CardTitle className="text-sm font-semibold text-slate-900 dark:text-slate-50 flex items-center gap-1.5">
                <AlertTriangle className="h-4 w-4 text-amber-500" />
                Urgent Action Items
              </CardTitle>
            </CardHeader>
            <CardContent className="p-4 space-y-3">
              <div className="p-3 rounded-xl bg-amber-50/80 dark:bg-amber-950/40 border border-amber-200/80 text-xs space-y-1.5">
                <div className="flex items-center justify-between">
                  <span className="font-bold text-amber-900 dark:text-amber-200">
                    Daily Attendance Cutoff
                  </span>
                  <span className="font-mono text-[10px] font-bold text-amber-700 bg-amber-200/70 px-1.5 py-0.5 rounded">
                    09:00 AM
                  </span>
                </div>
                <p className="text-[11px] text-amber-800/90 dark:text-amber-300">
                  Class 10-A daily attendance must be locked to dispatch automated absence SMS alerts.
                </p>
                <Link href="/teacher/attendance" className="block pt-1">
                  <Button size="sm" className="w-full h-7 text-xs bg-amber-600 hover:bg-amber-700 text-white font-semibold">
                    Submit Roll Call Now
                  </Button>
                </Link>
              </div>

              <div className="p-3 rounded-xl bg-blue-50/80 dark:bg-blue-950/40 border border-blue-200/80 text-xs space-y-1.5">
                <div className="flex items-center justify-between">
                  <span className="font-bold text-blue-900 dark:text-blue-200">
                    Homework Evaluation
                  </span>
                  <span className="text-[10px] text-blue-700 font-semibold">
                    Due Today
                  </span>
                </div>
                <p className="text-[11px] text-blue-800/90 dark:text-blue-300">
                  17 submissions pending review for &quot;Chapter 4: Quadratic Equations Problem Set&quot;.
                </p>
                <Link href="/teacher/assignments" className="block pt-1">
                  <Button variant="outline" size="sm" className="w-full h-7 text-xs border-blue-300 text-blue-700 hover:bg-blue-100">
                    Review Submissions
                  </Button>
                </Link>
              </div>
            </CardContent>
          </Card>

          {/* Today's Absentees Roster */}
          <Card className="shadow-xs border-slate-200/80">
            <CardHeader className="pb-3 border-b border-slate-100 dark:border-slate-800 flex items-center justify-between">
              <CardTitle className="text-sm font-semibold text-slate-900 dark:text-slate-50 flex items-center gap-1.5">
                <Users className="h-4 w-4 text-slate-500" />
                Class 10-A Absentees Today
              </CardTitle>
              <Badge variant="outline" className="text-[10px] font-mono">
                {absentCount + excusedCount} Students
              </Badge>
            </CardHeader>
            <CardContent className="p-3 space-y-2">
              {mockClassRoster
                .filter((s) => s.status === "ABSENT" || s.status === "EXCUSED")
                .map((student) => (
                  <div
                    key={student.id}
                    className="flex items-center justify-between p-2 rounded-lg bg-slate-50 dark:bg-slate-800/50 text-xs border border-slate-200/60"
                  >
                    <div className="flex flex-col">
                      <span className="font-bold text-slate-900 dark:text-slate-100">
                        {student.fullName}
                      </span>
                      <span className="text-[10px] text-slate-500">
                        Roll #{student.rollNumber} • {student.guardianPhone}
                      </span>
                      {student.preApprovedReason && (
                        <span className="text-[10px] text-emerald-600 font-medium truncate max-w-[200px]">
                          {student.preApprovedReason}
                        </span>
                      )}
                    </div>
                    <Badge
                      variant={student.status === "EXCUSED" ? "info" : "destructive"}
                      className="text-[10px] font-bold"
                    >
                      {student.status}
                    </Badge>
                  </div>
                ))}
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  );
}

