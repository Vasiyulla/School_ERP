"use client";

import React from "react";
import Link from "next/link";
import {
  Calendar,
  Clock,
  CreditCard,
  GraduationCap,
  Bus,
  CheckCircle2,
  AlertCircle,
  FileText,
  ChevronRight,
  TrendingUp,
  MapPin,
  Sparkles,
  ArrowUpRight,
  PhoneCall,
  Download,
} from "lucide-react";
import { useParentContext } from "@/components/parent/parent-context";
import {
  MOCK_BUS_TRACKING,
  MOCK_HOMEWORK,
  MOCK_PARENT_FEES,
  MOCK_AARAV_MARKS,
} from "@/lib/mock/parent-mock";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { parentService, ParentHomeDashboardData } from "@/lib/api/parent.service";

export default function ParentDashboardPage() {
  const { activeChild } = useParentContext();
  const [liveDashboard, setLiveDashboard] = React.useState<ParentHomeDashboardData | null>(null);
  const [isLiveConnected, setIsLiveConnected] = React.useState(false);

  React.useEffect(() => {
    let isMounted = true;
    parentService
      .getDashboard("parent.rajesh@example.com")
      .then((data) => {
        if (isMounted && data) {
          setLiveDashboard(data);
          setIsLiveConnected(true);
        }
      })
      .catch(() => {
        // Safe fallback to mock
      });
    return () => {
      isMounted = false;
    };
  }, []);

  const activeFees = MOCK_PARENT_FEES.filter((f) => f.childId === activeChild.id);
  const pendingFee = activeFees.find((f) => f.status === "PENDING" || f.status === "OVERDUE");
  const activeHomework = MOCK_HOMEWORK.filter((h) => h.childId === activeChild.id);

  return (
    <div className="mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8 space-y-6">
      {/* 1. Student Identity Hero Card */}
      <div className="relative overflow-hidden rounded-2xl border border-border/80 bg-gradient-to-r from-card via-card to-emerald-500/5 p-5 sm:p-6 shadow-sm">
        <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
          <div className="flex items-center gap-4">
            <div className="relative">
              <img
                src={activeChild.avatarUrl}
                alt={activeChild.fullName}
                className="h-16 w-16 sm:h-18 sm:w-18 rounded-2xl object-cover border-2 border-emerald-500 shadow-md"
              />
              <span className="absolute -bottom-1 -right-1 flex h-5 w-5 items-center justify-center rounded-full bg-emerald-500 text-[10px] text-white font-bold">
                ✓
              </span>
            </div>
            <div>
              <div className="flex items-center gap-2 flex-wrap">
                <h1 className="text-xl sm:text-2xl font-bold tracking-tight">{activeChild.fullName}</h1>
                <span className="rounded-full bg-emerald-500/15 px-2.5 py-0.5 text-xs font-semibold text-emerald-600 dark:text-emerald-400">
                  {activeChild.gradeLevel} • {activeChild.section}
                </span>
                {isLiveConnected && (
                  <Badge variant="success" className="text-[10px] font-mono py-0">
                    ● Parent Gateway Connected
                  </Badge>
                )}
              </div>
              <p className="text-xs text-muted-foreground mt-0.5">
                Admission No: <span className="font-mono font-medium text-foreground">{activeChild.admissionNumber}</span> • Roll #{activeChild.rollNumber} • Class Teacher: <span className="font-medium text-foreground">{activeChild.classTeacher}</span>
              </p>
              <div className="flex items-center gap-4 text-xs text-muted-foreground mt-2">
                <span>Blood: <strong className="text-foreground">{activeChild.bloodGroup}</strong></span>
                <span>•</span>
                <span>Locker: <strong className="font-mono text-foreground">{activeChild.lockerIndex}</strong></span>
              </div>
            </div>
          </div>

          <div className="flex items-center gap-2 w-full sm:w-auto">
            <Link href="/parent/academics" className="w-full sm:w-auto">
              <Button variant="outline" size="sm" className="w-full text-xs gap-1.5 border-border/80">
                <GraduationCap className="h-3.5 w-3.5" />
                View Report Card
              </Button>
            </Link>
            <Link href="/parent/attendance" className="w-full sm:w-auto">
              <Button size="sm" className="w-full text-xs gap-1.5 bg-emerald-600 hover:bg-emerald-700 text-white">
                Apply Leave
              </Button>
            </Link>
          </div>
        </div>
      </div>

      {/* 2. Key Operational Metrics (4-Column Grid) */}
      <div className="grid grid-cols-2 lg:grid-cols-4 gap-3 sm:gap-4">
        {/* Attendance Metric */}
        <Link href="/parent/attendance" className="block group">
          <div className="rounded-xl border border-border/80 bg-card p-4 transition-all hover:border-emerald-500/50 hover:shadow-sm">
            <div className="flex items-center justify-between text-muted-foreground mb-2">
              <span className="text-xs font-medium">Monthly Attendance</span>
              <div className="rounded-lg bg-emerald-500/10 p-1.5 text-emerald-600 dark:text-emerald-400">
                <CheckCircle2 className="h-4 w-4" />
              </div>
            </div>
            <div className="text-2xl font-bold font-mono tracking-tight text-foreground">
              {activeChild.attendancePercentage}%
            </div>
            <div className="flex items-center gap-1.5 text-[11px] text-emerald-600 dark:text-emerald-400 mt-1">
              <TrendingUp className="h-3 w-3" />
              <span>Above CBSE 75% Cutoff</span>
            </div>
          </div>
        </Link>

        {/* Academic Standing */}
        <Link href="/parent/academics" className="block group">
          <div className="rounded-xl border border-border/80 bg-card p-4 transition-all hover:border-emerald-500/50 hover:shadow-sm">
            <div className="flex items-center justify-between text-muted-foreground mb-2">
              <span className="text-xs font-medium">Academic GPA</span>
              <div className="rounded-lg bg-blue-500/10 p-1.5 text-blue-600 dark:text-blue-400">
                <GraduationCap className="h-4 w-4" />
              </div>
            </div>
            <div className="text-2xl font-bold font-mono tracking-tight text-foreground">
              {activeChild.overallGpa} <span className="text-sm font-normal text-muted-foreground">/ 10</span>
            </div>
            <div className="text-[11px] text-muted-foreground mt-1">
              Recent Average: <strong className="text-foreground">{activeChild.recentGradeAverage} (Grade A1)</strong>
            </div>
          </div>
        </Link>

        {/* Fee Payment Standing */}
        <Link href="/parent/fees" className="block group">
          <div className="rounded-xl border border-border/80 bg-card p-4 transition-all hover:border-emerald-500/50 hover:shadow-sm">
            <div className="flex items-center justify-between text-muted-foreground mb-2">
              <span className="text-xs font-medium">Term Fee Status</span>
              <div className="rounded-lg bg-amber-500/10 p-1.5 text-amber-600 dark:text-amber-400">
                <CreditCard className="h-4 w-4" />
              </div>
            </div>
            <div className="text-2xl font-bold font-mono tracking-tight text-foreground">
              {pendingFee ? (
                <span className="text-amber-600 dark:text-amber-400">₹{pendingFee.balanceAmount.toLocaleString()}</span>
              ) : (
                <span className="text-emerald-600 dark:text-emerald-400">₹0 Dues</span>
              )}
            </div>
            <div className="text-[11px] text-muted-foreground mt-1">
              {pendingFee ? (
                <span className="text-amber-600 dark:text-amber-400 font-medium">Due by {pendingFee.dueDate}</span>
              ) : (
                <span className="text-emerald-600 dark:text-emerald-400 font-medium">All Invoices Settled</span>
              )}
            </div>
          </div>
        </Link>

        {/* Bus Live Telemetry */}
        <Link href="/parent/tracking" className="block group">
          <div className="rounded-xl border border-border/80 bg-card p-4 transition-all hover:border-emerald-500/50 hover:shadow-sm">
            <div className="flex items-center justify-between text-muted-foreground mb-2">
              <span className="text-xs font-medium">Live School Bus</span>
              <div className="flex items-center gap-1 rounded-full bg-emerald-500/15 text-emerald-600 dark:text-emerald-400 px-1.5 py-0.5 text-[10px] font-bold">
                <span className="h-1.5 w-1.5 rounded-full bg-emerald-500 animate-pulse" />
                Live
              </div>
            </div>
            <div className="text-2xl font-bold font-mono tracking-tight text-foreground">
              {MOCK_BUS_TRACKING.etaMinutes} mins
            </div>
            <div className="text-[11px] text-muted-foreground mt-1 truncate">
              Stop: <strong className="text-foreground">Infinity Mall (07:05 AM)</strong>
            </div>
          </div>
        </Link>
      </div>

      {/* 3. Middle Section: Live Bus Telemetry Card + Urgent Notice */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Live GPS Telemetry Mini Widget (2 cols) */}
        <div className="lg:col-span-2 rounded-2xl border border-border/80 bg-card p-5 sm:p-6 shadow-sm">
          <div className="flex items-center justify-between mb-4">
            <div>
              <h2 className="text-base font-semibold tracking-tight flex items-center gap-2">
                <Bus className="h-4 w-4 text-emerald-600 dark:text-emerald-400" />
                Live Transit Tracking ({MOCK_BUS_TRACKING.routeNumber})
              </h2>
              <p className="text-xs text-muted-foreground">
                Vehicle: {MOCK_BUS_TRACKING.vehicleRegistration} • Driver: {MOCK_BUS_TRACKING.driverName} ({MOCK_BUS_TRACKING.driverPhone})
              </p>
            </div>
            <Link
              href="/parent/tracking"
              className="text-xs font-semibold text-emerald-600 dark:text-emerald-400 flex items-center gap-1 hover:underline"
            >
              Open Full Map <ChevronRight className="h-3.5 w-3.5" />
            </Link>
          </div>

          {/* Route Milestones Progress Bar */}
          <div className="space-y-4">
            <div className="flex items-center justify-between text-xs font-medium">
              <span className="text-muted-foreground">Current Location:</span>
              <span className="text-foreground font-semibold flex items-center gap-1">
                <MapPin className="h-3.5 w-3.5 text-emerald-500" />
                {MOCK_BUS_TRACKING.currentLocationName} ({MOCK_BUS_TRACKING.speedKmph} km/h)
              </span>
            </div>

            <div className="relative pt-2">
              <div className="grid grid-cols-4 gap-2 text-center text-xs">
                {MOCK_BUS_TRACKING.stops.map((stop, idx) => (
                  <div key={idx} className="space-y-1.5">
                    <div className="flex items-center justify-center">
                      <div
                        className={`h-4 w-4 rounded-full border-2 flex items-center justify-center text-[9px] font-bold ${
                          stop.isCompleted
                            ? "bg-emerald-500 border-emerald-500 text-white"
                            : stop.isChildStop
                            ? "border-emerald-500 bg-emerald-500/20 text-emerald-600 animate-pulse"
                            : "border-border bg-muted text-muted-foreground"
                        }`}
                      >
                        {stop.isCompleted ? "✓" : idx + 1}
                      </div>
                    </div>
                    <div className="font-medium text-[11px] leading-tight line-clamp-1">{stop.stopName}</div>
                    <div className="text-[10px] text-muted-foreground font-mono">{stop.scheduledTime}</div>
                  </div>
                ))}
              </div>
            </div>

            <div className="flex items-center justify-between rounded-xl bg-muted/40 p-3 text-xs">
              <span className="text-muted-foreground">
                Next Stop: <strong className="text-foreground">Infinity Mall (Child Pickup)</strong>
              </span>
              <a
                href={`tel:${MOCK_BUS_TRACKING.driverPhone}`}
                className="flex items-center gap-1.5 text-xs font-semibold text-emerald-600 dark:text-emerald-400 hover:underline"
              >
                <PhoneCall className="h-3.5 w-3.5" /> Call Driver
              </a>
            </div>
          </div>
        </div>

        {/* PTM & Important Circular Banner (1 col) */}
        <div className="rounded-2xl border border-amber-500/30 bg-gradient-to-b from-amber-500/10 via-card to-card p-5 sm:p-6 shadow-sm flex flex-col justify-between">
          <div className="space-y-3">
            <div className="flex items-center gap-2">
              <span className="rounded-full bg-amber-500 text-white p-1">
                <Sparkles className="h-3.5 w-3.5" />
              </span>
              <span className="text-xs font-bold uppercase tracking-wider text-amber-600 dark:text-amber-400">
                Action Required
              </span>
            </div>
            <h3 className="text-base font-semibold tracking-tight text-foreground">
              Term 1 Parent-Teacher Meeting (PTM)
            </h3>
            <p className="text-xs text-muted-foreground leading-relaxed">
              Mandatory academic review for {activeChild.fullName}. Teachers will walk through continuous assessment marks and board project submissions.
            </p>
            <div className="rounded-lg bg-card/80 border border-border p-2.5 text-xs font-mono space-y-1">
              <div>🗓 Date: <strong>This Saturday (Nov 14)</strong></div>
              <div>⏰ Time: <strong>09:00 AM – 01:00 PM</strong></div>
              <div>📍 Venue: <strong>Auditorium & Class 10 Labs</strong></div>
            </div>
          </div>

          <div className="pt-4">
            <Link href="/parent/circulars">
              <Button size="sm" variant="outline" className="w-full text-xs gap-1.5 border-amber-500/40 text-amber-600 dark:text-amber-400 hover:bg-amber-500/10">
                View Official Circular PDF <ArrowUpRight className="h-3.5 w-3.5" />
              </Button>
            </Link>
          </div>
        </div>
      </div>

      {/* 4. Bottom Grid: Today's Homework & Continuous Academic Standing */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Homework & Due Submissions */}
        <div className="rounded-2xl border border-border/80 bg-card p-5 sm:p-6 shadow-sm">
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-base font-semibold tracking-tight flex items-center gap-2">
              <FileText className="h-4 w-4 text-emerald-600 dark:text-emerald-400" />
              Assignments & Homework
            </h2>
            <Link href="/parent/academics" className="text-xs text-emerald-600 dark:text-emerald-400 hover:underline">
              View All
            </Link>
          </div>

          <div className="space-y-3">
            {activeHomework.length > 0 ? (
              activeHomework.map((hw) => (
                <div
                  key={hw.id}
                  className="flex items-start justify-between rounded-xl border border-border/60 bg-muted/20 p-3.5 hover:bg-muted/40 transition-colors"
                >
                  <div className="space-y-1">
                    <div className="flex items-center gap-2">
                      <span className="text-xs font-semibold">{hw.subject}</span>
                      <span
                        className={`rounded-full px-2 py-0.2 text-[10px] font-semibold ${
                          hw.status === "SUBMITTED"
                            ? "bg-emerald-500/15 text-emerald-600 dark:text-emerald-400"
                            : "bg-amber-500/15 text-amber-600 dark:text-amber-400"
                        }`}
                      >
                        {hw.status}
                      </span>
                    </div>
                    <p className="text-xs text-foreground font-medium">{hw.title}</p>
                    <p className="text-[11px] text-muted-foreground">
                      Teacher: {hw.teacherName} • Due: <span className="font-mono">{hw.dueDate}</span>
                    </p>
                  </div>
                  {hw.attachmentName && (
                    <Button variant="ghost" size="icon" className="h-8 w-8 text-muted-foreground hover:text-foreground">
                      <Download className="h-4 w-4" />
                    </Button>
                  )}
                </div>
              ))
            ) : (
              <p className="text-xs text-muted-foreground py-4 text-center">No pending assignments for {activeChild.fullName}.</p>
            )}
          </div>
        </div>

        {/* Continuous Assessment Subject Highlights */}
        <div className="rounded-2xl border border-border/80 bg-card p-5 sm:p-6 shadow-sm">
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-base font-semibold tracking-tight flex items-center gap-2">
              <GraduationCap className="h-4 w-4 text-emerald-600 dark:text-emerald-400" />
              Continuous Subject Evaluation (CBSE 9-Point)
            </h2>
            <Link href="/parent/academics" className="text-xs text-emerald-600 dark:text-emerald-400 hover:underline">
              Detailed Marksheet
            </Link>
          </div>

          <div className="space-y-3">
            {MOCK_AARAV_MARKS.slice(0, 3).map((mark) => (
              <div
                key={mark.subjectCode}
                className="flex items-center justify-between rounded-xl border border-border/60 bg-muted/20 p-3.5"
              >
                <div>
                  <div className="text-xs font-semibold">{mark.subject}</div>
                  <div className="text-[11px] text-muted-foreground">
                    Theory: {mark.theoryScore}/80 • Pract/Internal: {mark.practicalScore + mark.internalScore}/20
                  </div>
                </div>
                <div className="text-right">
                  <div className="text-sm font-bold font-mono">
                    {mark.totalScore} <span className="text-xs font-normal text-muted-foreground">/ 100</span>
                  </div>
                  <span className="inline-block rounded bg-emerald-500/15 text-emerald-600 dark:text-emerald-400 font-mono text-[10px] font-bold px-1.5 py-0.2">
                    Grade {mark.gradeBand}
                  </span>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}

