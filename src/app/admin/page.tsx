"use client";

import * as React from "react";
import Link from "next/link";
import {
  Users,
  CheckCircle2,
  DollarSign,
  UserPlus,
  PlusCircle,
  FileCheck2,
  GraduationCap,
  ArrowRight,
  Receipt,
} from "lucide-react";
import { MetricCard } from "@/components/admin/metric-card";
import { AttendanceChartCard } from "@/components/admin/attendance-chart-card";
import { RecentActivityCard } from "@/components/admin/recent-activity-card";
import { Button } from "@/components/ui/button";
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Server } from "lucide-react";
import { mockAdminKPIs, mockClasses } from "@/lib/mock/admin-mock";
import { formatCurrency } from "@/lib/utils";
import { adminService, financeService } from "@/lib/api";

export default function AdminDashboardPage() {
  const [liveStudentsCount, setLiveStudentsCount] = React.useState<number | null>(null);
  const [liveTrialBalance, setLiveTrialBalance] = React.useState<{ total: number; isBalanced: boolean } | null>(null);
  const [backendStatus, setBackendStatus] = React.useState<"LIVE" | "CONNECTING" | "OFFLINE">("CONNECTING");

  React.useEffect(() => {
    let isMounted = true;
    Promise.allSettled([
      adminService.getStudents(),
      financeService.getTrialBalance(),
    ]).then(([studentsRes, tbRes]) => {
      if (!isMounted) return;
      let connected = false;
      if (studentsRes.status === "fulfilled" && studentsRes.value) {
        setLiveStudentsCount(studentsRes.value.length);
        connected = true;
      }
      if (tbRes.status === "fulfilled" && tbRes.value) {
        setLiveTrialBalance({ total: tbRes.value.totalDebit, isBalanced: tbRes.value.isBalanced });
        connected = true;
      }
      setBackendStatus(connected ? "LIVE" : "OFFLINE");
    }).catch(() => {
      if (isMounted) setBackendStatus("OFFLINE");
    });

    return () => {
      isMounted = false;
    };
  }, []);

  return (
    <div className="space-y-6 max-w-7xl mx-auto">
      {/* Top Header & Action Row */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <div className="flex items-center gap-2">
            <h1 className="text-2xl font-bold tracking-tight text-slate-900 dark:text-slate-50">
              Executive Command Center
            </h1>
            <Badge
              variant={backendStatus === "LIVE" ? "success" : "secondary"}
              className="text-[10px] font-mono py-0.5 ml-2"
            >
              <Server className="h-3 w-3 mr-1" />
              {backendStatus === "LIVE" ? "Spring Boot Live (38 APIs)" : "Standby Mode"}
            </Badge>
          </div>
          <p className="text-sm font-normal text-slate-500 dark:text-slate-400 mt-1">
            Real-time operational, academic, and financial overview across 20,000 student capacity
          </p>
        </div>
        <div className="flex items-center gap-3">
          <Button asChild variant="outline" size="sm" className="gap-2">
            <Link href="/admin/admissions">
              <UserPlus className="h-4 w-4 text-slate-500" /> Review Admissions (234)
            </Link>
          </Button>
          <Button asChild size="sm" className="gap-2 bg-blue-600 hover:bg-blue-700 text-white shadow-sm shadow-blue-500/20">
            <Link href="/admin/students">
              <PlusCircle className="h-4 w-4" /> Enroll Student
            </Link>
          </Button>
        </div>
      </div>

      {/* Bento Grid: 4 Top Restructured Metric Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
        <MetricCard
          title="Total Enrollment"
          value={mockAdminKPIs.totalStudents.toLocaleString()}
          subtitle={`${((mockAdminKPIs.totalStudents / mockAdminKPIs.studentCapacity) * 100).toFixed(1)}% of 20k target`}
          progressPercent={(mockAdminKPIs.totalStudents / mockAdminKPIs.studentCapacity) * 100}
          icon={Users}
          trend={{ value: "+3.2% vs last term", isPositive: true }}
          accentColor="blue"
          href="/admin/students"
        />
        <MetricCard
          title="Today's Attendance"
          value={`${mockAdminKPIs.todayAttendancePercent}%`}
          subtitle={`${mockAdminKPIs.presentStudents.toLocaleString()} present • ${mockAdminKPIs.absentStudents} absent`}
          progressPercent={mockAdminKPIs.todayAttendancePercent}
          icon={CheckCircle2}
          trend={{ value: "+0.8% this week", isPositive: true }}
          accentColor="emerald"
          href="/admin/attendance"
        />
        <MetricCard
          title="Fee Collections (Nov)"
          value={formatCurrency(mockAdminKPIs.feeCollectionMonth)}
          subtitle={`Target: ${formatCurrency(mockAdminKPIs.feeCollectionTarget)}`}
          progressPercent={93.2}
          icon={DollarSign}
          trend={{ value: "93.2% Target", isPositive: true }}
          accentColor="indigo"
          href="/admin/finance"
        />
        <MetricCard
          title="Admissions Inflow"
          value={mockAdminKPIs.pendingAdmissions}
          subtitle="34 interviews scheduled this week"
          progressPercent={65}
          icon={UserPlus}
          trend={{ value: "+18 today", isPositive: true }}
          accentColor="amber"
          href="/admin/admissions"
        />
      </div>

      {/* Middle Grid: Attendance Throughput & Operations Shortcuts */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <AttendanceChartCard />

        {/* Quick Operations Console */}
        <Card className="flex flex-col justify-between">
          <div>
            <CardHeader className="pb-4">
              <CardTitle className="text-base font-semibold text-slate-900 dark:text-slate-50">
                Quick Administrative Actions
              </CardTitle>
              <CardDescription className="text-xs text-slate-500 dark:text-slate-400">
                Frequent operational shortcuts
              </CardDescription>
            </CardHeader>
            <CardContent className="space-y-3">
              <Link
                href="/admin/students"
                className="flex items-center justify-between p-3.5 rounded-xl border border-slate-200/80 dark:border-slate-800 hover:bg-slate-50 dark:hover:bg-slate-800/60 transition-colors group"
              >
                <div className="flex items-center gap-3">
                  <div className="p-2 rounded-lg bg-blue-50 text-blue-600 dark:bg-blue-950/60">
                    <PlusCircle className="h-4 w-4" />
                  </div>
                  <div>
                    <p className="text-sm font-semibold text-slate-800 dark:text-slate-200">
                      Enroll New Student
                    </p>
                    <p className="text-xs text-slate-500 dark:text-slate-400">
                      Generate UUID & admission number
                    </p>
                  </div>
                </div>
                <ArrowRight className="h-4 w-4 text-slate-400 group-hover:text-blue-600 transition-transform group-hover:translate-x-1" />
              </Link>

              <Link
                href="/admin/admissions"
                className="flex items-center justify-between p-3.5 rounded-xl border border-slate-200/80 dark:border-slate-800 hover:bg-slate-50 dark:hover:bg-slate-800/60 transition-colors group"
              >
                <div className="flex items-center gap-3">
                  <div className="p-2 rounded-lg bg-amber-50 text-amber-600 dark:bg-amber-950/60">
                    <FileCheck2 className="h-4 w-4" />
                  </div>
                  <div>
                    <p className="text-sm font-semibold text-slate-800 dark:text-slate-200">
                      Review Admissions
                    </p>
                    <p className="text-xs text-slate-500 dark:text-slate-400">
                      234 applications awaiting decision
                    </p>
                  </div>
                </div>
                <ArrowRight className="h-4 w-4 text-slate-400 group-hover:text-amber-600 transition-transform group-hover:translate-x-1" />
              </Link>

              <Link
                href="/admin/academics"
                className="flex items-center justify-between p-3.5 rounded-xl border border-slate-200/80 dark:border-slate-800 hover:bg-slate-50 dark:hover:bg-slate-800/60 transition-colors group"
              >
                <div className="flex items-center gap-3">
                  <div className="p-2 rounded-lg bg-purple-50 text-purple-600 dark:bg-purple-950/60">
                    <GraduationCap className="h-4 w-4" />
                  </div>
                  <div>
                    <p className="text-sm font-semibold text-slate-800 dark:text-slate-200">
                      Classes & Sections
                    </p>
                    <p className="text-xs text-slate-500 dark:text-slate-400">
                      Assign class teachers & capacities
                    </p>
                  </div>
                </div>
                <ArrowRight className="h-4 w-4 text-slate-400 group-hover:text-purple-600 transition-transform group-hover:translate-x-1" />
              </Link>

              <Link
                href="/admin/finance"
                className="flex items-center justify-between p-3.5 rounded-xl border border-slate-200/80 dark:border-slate-800 hover:bg-slate-50 dark:hover:bg-slate-800/60 transition-colors group"
              >
                <div className="flex items-center gap-3">
                  <div className="p-2 rounded-lg bg-emerald-50 text-emerald-600 dark:bg-emerald-950/60">
                    <Receipt className="h-4 w-4" />
                  </div>
                  <div>
                    <p className="text-sm font-semibold text-slate-800 dark:text-slate-200">
                      Fee Ledger & Invoicing
                    </p>
                    <p className="text-xs text-slate-500 dark:text-slate-400">
                      ₹4.85 Cr collected • Reconcile dues
                    </p>
                  </div>
                </div>
                <ArrowRight className="h-4 w-4 text-slate-400 group-hover:text-emerald-600 transition-transform group-hover:translate-x-1" />
              </Link>
            </CardContent>
          </div>
        </Card>
      </div>

      {/* Bottom Grid: Live Stream & Section Capacity */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <RecentActivityCard />

        {/* Section Capacity Monitoring */}
        <Card className="col-span-1 lg:col-span-2">
          <CardHeader className="flex flex-row items-center justify-between pb-4">
            <div>
              <CardTitle className="text-base font-semibold text-slate-900 dark:text-slate-50">
                Section Capacity & Teacher Allocations
              </CardTitle>
              <CardDescription className="text-xs text-slate-500 dark:text-slate-400">
                Live monitoring of enrolled student count vs capacity limit
              </CardDescription>
            </div>
            <Button asChild variant="ghost" size="sm" className="text-xs font-semibold text-blue-600 hover:text-blue-700">
              <Link href="/admin/academics">View All 48 Sections</Link>
            </Button>
          </CardHeader>
          <CardContent className="p-0">
            <div className="overflow-x-auto border-t border-slate-100 dark:border-slate-800">
              <table className="w-full text-sm text-left">
                <thead className="bg-slate-50/80 dark:bg-slate-900/80 border-b border-slate-200/80 dark:border-slate-800 text-xs uppercase font-semibold text-slate-500">
                  <tr>
                    <th className="py-3 px-4">Class & Section</th>
                    <th className="py-3 px-4">Occupancy</th>
                    <th className="py-3 px-4">Capacity Bar</th>
                    <th className="py-3 px-4">Class Teacher</th>
                    <th className="py-3 px-4 text-right">Room</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-100 dark:divide-slate-800/60 text-sm">
                  {mockClasses.map((cls) => {
                    const pct = Math.round((cls.enrolledCount / cls.capacity) * 100);
                    return (
                      <tr key={cls.id} className="hover:bg-slate-50/80 dark:hover:bg-slate-800/40 transition-colors">
                        <td className="py-3.5 px-4 font-semibold text-slate-900 dark:text-slate-100">
                          {cls.className} - {cls.sectionName}
                        </td>
                        <td className="py-3.5 px-4 font-medium text-slate-600 dark:text-slate-400">
                          {cls.enrolledCount} / {cls.capacity} students
                        </td>
                        <td className="py-3.5 px-4">
                          <div className="flex items-center gap-2.5">
                            <div className="w-24 h-2 rounded-full bg-slate-100 dark:bg-slate-800 overflow-hidden">
                              <div
                                style={{ width: `${pct}%` }}
                                className={`h-full rounded-full transition-all duration-300 ${
                                  pct >= 95 ? "bg-amber-500" : "bg-blue-600"
                                }`}
                              />
                            </div>
                            <span className="text-xs font-bold text-slate-700 dark:text-slate-300">{pct}%</span>
                          </div>
                        </td>
                        <td className="py-3.5 px-4 text-slate-600 dark:text-slate-400">
                          {cls.classTeacher}
                        </td>
                        <td className="py-3.5 px-4 text-right font-medium text-slate-500">
                          {cls.roomNumber}
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
