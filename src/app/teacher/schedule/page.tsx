"use client";

import * as React from "react";
import {
  Calendar,
  Clock,
  MapPin,
  Sparkles,
  Download,
  BookOpen,
} from "lucide-react";
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { cn } from "@/lib/utils";

const periods = [
  { id: 1, time: "08:30 – 09:15", label: "P1 (Homeroom)" },
  { id: 2, time: "09:15 – 10:00", label: "P2" },
  { id: 3, time: "10:15 – 11:00", label: "P3" },
  { id: 4, time: "11:00 – 11:45", label: "P4" },
  { id: 5, time: "12:30 – 01:15", label: "P5" },
  { id: 6, time: "01:15 – 02:00", label: "P6" },
  { id: 7, time: "02:05 – 02:50", label: "P7" },
];

const days = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday"];

const weeklySchedule: Record<string, { subject: string; class: string; room: string; isFree?: boolean; isLab?: boolean }[]> = {
  Monday: [
    { subject: "Homeroom & Roll Call", class: "Class 10-A", room: "Room 301" },
    { subject: "Adv Mathematics", class: "Class 10-A", room: "Room 301" },
    { subject: "Planning Period", class: "-", room: "Staff Room", isFree: true },
    { subject: "Calculus & Algebra", class: "Class 11-A", room: "Room 402" },
    { subject: "Standard Math", class: "Class 10-B", room: "Room 303" },
    { subject: "Math Lab Practical", class: "Class 10-A", room: "Comp Lab 1", isLab: true },
    { subject: "Substitute Physics", class: "Class 9-C", room: "Room 205" },
  ],
  Tuesday: [
    { subject: "Homeroom & Roll Call", class: "Class 10-A", room: "Room 301" },
    { subject: "Standard Math", class: "Class 10-B", room: "Room 303" },
    { subject: "Adv Mathematics", class: "Class 10-A", room: "Room 301" },
    { subject: "Higher Math", class: "Class 12-A", room: "Room 501" },
    { subject: "Planning Period", class: "-", room: "Staff Room", isFree: true },
    { subject: "Calculus & Algebra", class: "Class 11-A", room: "Room 402" },
    { subject: "Remedial Math Clinic", class: "Class 10-A", room: "Room 301" },
  ],
  Wednesday: [
    { subject: "Homeroom & Roll Call", class: "Class 10-A", room: "Room 301" },
    { subject: "Calculus & Algebra", class: "Class 11-A", room: "Room 402" },
    { subject: "Higher Math", class: "Class 12-A", room: "Room 501" },
    { subject: "Adv Mathematics", class: "Class 10-A", room: "Room 301" },
    { subject: "Standard Math", class: "Class 10-B", room: "Room 303" },
    { subject: "Planning Period", class: "-", room: "Staff Room", isFree: true },
    { subject: "Department Meeting", class: "Faculty", room: "Conf Room B" },
  ],
  Thursday: [
    { subject: "Homeroom & Roll Call", class: "Class 10-A", room: "Room 301" },
    { subject: "Adv Mathematics", class: "Class 10-A", room: "Room 301" },
    { subject: "Planning Period", class: "-", room: "Staff Room", isFree: true },
    { subject: "Calculus & Algebra", class: "Class 11-A", room: "Room 402" },
    { subject: "Standard Math", class: "Class 10-B", room: "Room 303" },
    { subject: "Math Lab Practical", class: "Class 10-B", room: "Comp Lab 1", isLab: true },
    { subject: "Higher Math", class: "Class 12-A", room: "Room 501" },
  ],
  Friday: [
    { subject: "Homeroom & Roll Call", class: "Class 10-A", room: "Room 301" },
    { subject: "Higher Math", class: "Class 12-A", room: "Room 501" },
    { subject: "Standard Math", class: "Class 10-B", room: "Room 303" },
    { subject: "Adv Mathematics", class: "Class 10-A", room: "Room 301" },
    { subject: "Calculus & Algebra", class: "Class 11-A", room: "Room 402" },
    { subject: "Weekly Quiz / Test", class: "Class 10-A", room: "Room 301" },
    { subject: "Staff Assembly", class: "All School", room: "Auditorium" },
  ],
};

export default function TeacherSchedulePage() {
  const [activeDay, setActiveDay] = React.useState("Monday");

  return (
    <div className="space-y-6 max-w-7xl mx-auto">
      {/* Top Banner */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 p-5 rounded-2xl bg-white dark:bg-slate-900 border border-slate-200/90 dark:border-slate-800 shadow-xs">
        <div className="space-y-1">
          <div className="flex items-center gap-2">
            <span className="text-xs font-bold text-emerald-600 dark:text-emerald-400 uppercase tracking-wider">
              Faculty Workload
            </span>
            <span className="text-slate-300 dark:text-slate-700">•</span>
            <span className="text-xs font-medium text-slate-500">
              22 Teaching Hours / Week
            </span>
          </div>
          <h1 className="text-2xl font-bold tracking-tight text-slate-900 dark:text-slate-50 flex items-center gap-2">
            <Calendar className="h-6 w-6 text-blue-600" />
            Master Weekly Teaching Timetable
          </h1>
          <p className="text-xs text-slate-500 dark:text-slate-400">
            Assigned classroom periods, lab sessions, planning hours, and scheduled substitution duties.
          </p>
        </div>

        <Button
          variant="outline"
          className="h-9 text-xs font-semibold border-slate-200 gap-1.5"
          onClick={() => alert("Timetable PDF exported for Dr. Vikram Seth")}
        >
          <Download className="h-3.5 w-3.5" />
          Export Timetable (PDF)
        </Button>
      </div>

      {/* Day Selector */}
      <div className="flex items-center gap-1.5 overflow-x-auto pb-1">
        {days.map((day) => (
          <button
            key={day}
            onClick={() => setActiveDay(day)}
            className={cn(
              "px-4 py-2 text-xs font-semibold rounded-xl transition-all shrink-0",
              activeDay === day
                ? "bg-slate-900 text-white dark:bg-slate-100 dark:text-slate-900 shadow-xs"
                : "bg-white dark:bg-slate-800 text-slate-600 hover:bg-slate-100 border border-slate-200 dark:border-slate-700"
            )}
          >
            {day}
            {day === "Monday" && (
              <span className="ml-1.5 px-1.5 py-0.2 rounded text-[10px] bg-emerald-500 text-white font-bold">
                Today
              </span>
            )}
          </button>
        ))}
      </div>

      {/* Daily Periods Grid */}
      <Card className="shadow-xs border-slate-200/80">
        <CardHeader className="pb-3 border-b border-slate-100 dark:border-slate-800">
          <CardTitle className="text-base font-semibold text-slate-900 dark:text-slate-50">
            {activeDay}&apos;s Period Schedule
          </CardTitle>
          <CardDescription className="text-xs text-slate-500">
            Period timings aligned with Apex Academy CBSE Academic Calendar 2026–27
          </CardDescription>
        </CardHeader>
        <CardContent className="p-4 space-y-3">
          {weeklySchedule[activeDay]?.map((slot, idx) => {
            const pInfo = periods[idx];
            return (
              <div
                key={idx}
                className={cn(
                  "flex items-center justify-between p-3.5 rounded-xl border transition-all",
                  slot.isLab
                    ? "bg-emerald-50/50 border-emerald-200 dark:bg-emerald-950/20"
                    : slot.isFree
                    ? "bg-slate-50/60 border-slate-200 dark:bg-slate-800/30 border-dashed"
                    : "bg-white dark:bg-slate-900 border-slate-200/80 hover:border-blue-300"
                )}
              >
                <div className="flex items-center gap-4">
                  <div className="flex flex-col items-center justify-center h-11 w-14 rounded-lg bg-slate-100 dark:bg-slate-800 font-mono text-xs font-bold text-slate-700 dark:text-slate-300 border border-slate-200 dark:border-slate-700">
                    <span>{pInfo.label.split(" ")[0]}</span>
                    <span className="text-[10px] text-slate-400 font-normal">P{idx + 1}</span>
                  </div>

                  <div>
                    <div className="flex items-center gap-2">
                      <span className="text-sm font-bold text-slate-900 dark:text-slate-100">
                        {slot.subject}
                      </span>
                      {slot.isLab && (
                        <Badge variant="success" className="text-[10px] py-0 px-1.5">
                          Practical Lab
                        </Badge>
                      )}
                      {slot.isFree && (
                        <Badge variant="outline" className="text-[10px] py-0 px-1.5 text-slate-400">
                          Free Slot
                        </Badge>
                      )}
                    </div>
                    <div className="flex items-center gap-2 text-xs text-slate-500 mt-1">
                      <span className="font-mono">{pInfo.time}</span>
                      <span>•</span>
                      <span className="font-semibold text-slate-700 dark:text-slate-300">
                        {slot.class}
                      </span>
                      <span>•</span>
                      <span className="flex items-center gap-1 text-slate-600 dark:text-slate-400">
                        <MapPin className="h-3 w-3 text-slate-400" />
                        {slot.room}
                      </span>
                    </div>
                  </div>
                </div>

                <div>
                  <Badge variant="outline" className="text-xs font-mono border-slate-200">
                    45 mins
                  </Badge>
                </div>
              </div>
            );
          })}
        </CardContent>
      </Card>
    </div>
  );
}

