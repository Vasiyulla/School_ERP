"use client";

import * as React from "react";
import { CheckCircle2, XCircle, Clock, Calendar } from "lucide-react";
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";

const weeklyData = [
  { day: "Mon", rate: 95.2, present: "17,560", absent: "890" },
  { day: "Tue", rate: 96.1, present: "17,730", absent: "720" },
  { day: "Wed", rate: 94.8, present: "17,490", absent: "960" },
  { day: "Thu", rate: 93.9, present: "17,320", absent: "1,130" },
  { day: "Fri", rate: 95.7, present: "17,650", absent: "800" },
  { day: "Sat", rate: 91.4, present: "16,860", absent: "1,590" },
];

export function AttendanceChartCard() {
  return (
    <Card className="col-span-1 lg:col-span-2">
      <CardHeader className="flex flex-row items-center justify-between pb-4">
        <div>
          <CardTitle className="text-base font-semibold text-slate-900 dark:text-slate-50">
            Attendance Throughput & Daily Trends
          </CardTitle>
          <CardDescription className="text-xs text-slate-500 dark:text-slate-400 mt-0.5">
            Live school-wide verification across 20,000 capacity
          </CardDescription>
        </div>
        <Badge variant="success" className="gap-1.5 py-1 px-3">
          <CheckCircle2 className="h-3.5 w-3.5" /> 94.8% Active Today
        </Badge>
      </CardHeader>
      <CardContent className="space-y-5">
        {/* Real-time stats row */}
        <div className="grid grid-cols-3 gap-3 rounded-xl bg-slate-50 dark:bg-slate-800/50 p-4 border border-slate-200/60 dark:border-slate-700/60">
          <div className="flex items-center gap-3">
            <div className="h-9 w-9 rounded-lg bg-emerald-100 text-emerald-700 dark:bg-emerald-950/60 dark:text-emerald-400 flex items-center justify-center shrink-0">
              <CheckCircle2 className="h-5 w-5" />
            </div>
            <div>
              <p className="text-xs font-medium text-slate-500 dark:text-slate-400">Present Today</p>
              <p className="text-base font-bold text-slate-900 dark:text-slate-50">17,490</p>
            </div>
          </div>
          <div className="flex items-center gap-3">
            <div className="h-9 w-9 rounded-lg bg-rose-100 text-rose-700 dark:bg-rose-950/60 dark:text-rose-400 flex items-center justify-center shrink-0">
              <XCircle className="h-5 w-5" />
            </div>
            <div>
              <p className="text-xs font-medium text-slate-500 dark:text-slate-400">Unexcused</p>
              <p className="text-base font-bold text-slate-900 dark:text-slate-50">960</p>
            </div>
          </div>
          <div className="flex items-center gap-3">
            <div className="h-9 w-9 rounded-lg bg-amber-100 text-amber-700 dark:bg-amber-950/60 dark:text-amber-400 flex items-center justify-center shrink-0">
              <Clock className="h-5 w-5" />
            </div>
            <div>
              <p className="text-xs font-medium text-slate-500 dark:text-slate-400">Approved Leaves</p>
              <p className="text-base font-bold text-slate-900 dark:text-slate-50">142</p>
            </div>
          </div>
        </div>

        {/* 6-Day Visual Attendance Graph */}
        <div className="space-y-3 pt-1">
          <div className="flex items-center justify-between text-xs text-slate-500 dark:text-slate-400 font-medium">
            <span className="flex items-center gap-1.5">
              <Calendar className="h-3.5 w-3.5 text-primary" /> Past 6 Days Batch Distribution
            </span>
            <span className="font-semibold text-slate-700 dark:text-slate-300">Target: 92%+</span>
          </div>

          <div className="grid grid-cols-6 gap-3 items-end h-32 pt-2">
            {weeklyData.map((item, idx) => (
              <div key={item.day} className="flex flex-col items-center h-full justify-end group">
                <span className="text-xs font-bold text-slate-500 dark:text-slate-400 group-hover:text-primary transition-colors pb-1.5">
                  {item.rate}%
                </span>
                <div className="w-full rounded-lg bg-slate-100 dark:bg-slate-800 h-full relative overflow-hidden flex items-end">
                  <div
                    style={{ height: `${item.rate}%` }}
                    className={`w-full rounded-lg transition-all duration-300 ${
                      idx === 2
                        ? "bg-primary shadow-xs shadow-primary/30"
                        : "bg-primary/50 group-hover:bg-primary/80"
                    }`}
                  />
                </div>
                <span className="text-xs font-semibold text-slate-700 dark:text-slate-300 mt-2">
                  {item.day}
                </span>
              </div>
            ))}
          </div>
        </div>
      </CardContent>
    </Card>
  );
}
