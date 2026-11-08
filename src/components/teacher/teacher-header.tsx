"use client";

import * as React from "react";
import Link from "next/link";
import { usePathname } from "next/navigation";
import { useTheme } from "next-themes";
import {
  Sun,
  Moon,
  Bell,
  ChevronRight,
  Clock,
  CalendarCheck2,
  CalendarDays,
  Sparkles,
  Award,
  Layers,
} from "lucide-react";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { cn } from "@/lib/utils";

export function TeacherHeader() {
  const pathname = usePathname();
  const { theme, setTheme } = useTheme();

  const getSectionTitle = () => {
    if (pathname.includes("/attendance")) return "Class Attendance Roster";
    if (pathname.includes("/gradebook")) return "Continuous Evaluation Gradebook";
    if (pathname.includes("/assignments")) return "Homework & Tasks";
    if (pathname.includes("/students")) return "Class 10-A Student Dossiers";
    if (pathname.includes("/schedule")) return "Weekly Teaching Timetable";
    if (pathname.includes("/leaves")) return "Faculty Leaves & Substitution";
    return "Faculty Cockpit";
  };

  return (
    <header className="sticky top-0 z-20 flex h-14 w-full items-center justify-between border-b border-slate-200/90 dark:border-slate-800 bg-white dark:bg-slate-900 px-4 md:px-6">
      {/* Left: Breadcrumbs */}
      <div className="flex items-center gap-2 text-xs shrink-0 max-w-[240px] sm:max-w-xs overflow-hidden">
        <span className="text-slate-400 font-medium hidden sm:inline">
          Faculty
        </span>
        <ChevronRight className="h-3.5 w-3.5 text-slate-300 dark:text-slate-600 hidden sm:inline shrink-0" />
        <span className="font-bold text-slate-900 dark:text-slate-50 tracking-tight truncate">
          {getSectionTitle()}
        </span>
      </div>

      {/* Middle: Active Class Teacher Indicator & Current Period */}
      <div className="hidden lg:flex items-center gap-3">
        <div className="flex items-center gap-1.5 px-3 py-1 rounded-full bg-emerald-50 dark:bg-emerald-950/60 border border-emerald-200/70 text-emerald-800 dark:text-emerald-300 text-xs font-semibold">
          <span className="h-2 w-2 rounded-full bg-emerald-500 animate-pulse" />
          <span>Class Teacher: Class 10-A</span>
          <span className="text-emerald-400">•</span>
          <span className="text-emerald-700 dark:text-emerald-400 font-medium">38 Enrolled</span>
        </div>

        <div className="flex items-center gap-1.5 px-2.5 py-1 rounded-full bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 text-xs font-medium border border-slate-200 dark:border-slate-700">
          <Clock className="h-3 w-3 text-blue-600" />
          <span>Period 4: Calculus (11-A)</span>
          <span className="text-slate-400">•</span>
          <span className="font-mono text-[11px] text-slate-500">11:00 – 11:45</span>
        </div>
      </div>

      {/* Right Controls */}
      <div className="flex items-center gap-2 shrink-0">
        {/* Quick Attendance Pill */}
        <Link href="/teacher/attendance">
          <Button
            size="sm"
            className="h-8 text-xs font-semibold bg-emerald-600 hover:bg-emerald-700 text-white shadow-xs gap-1.5 px-3"
          >
            <CalendarCheck2 className="h-3.5 w-3.5" />
            <span className="hidden sm:inline">Daily Attendance</span>
            <span className="sm:hidden">Roll Call</span>
          </Button>
        </Link>

        {/* Theme Toggle */}
        <Button
          variant="ghost"
          size="icon"
          onClick={() => setTheme(theme === "dark" ? "light" : "dark")}
          className="h-8 w-8 text-slate-500 hover:text-slate-800 dark:text-slate-400"
          title="Toggle Theme"
        >
          <Sun className="h-4 w-4 rotate-0 scale-100 transition-all dark:-rotate-90 dark:scale-0" />
          <Moon className="absolute h-4 w-4 rotate-90 scale-0 transition-all dark:rotate-0 dark:scale-100" />
        </Button>

        {/* Notifications */}
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button
              variant="ghost"
              size="icon"
              className="relative h-8 w-8 text-slate-500 hover:text-slate-800 dark:text-slate-400"
              title="Notifications"
            >
              <Bell className="h-4 w-4" />
              <span className="absolute top-1.5 right-1.5 h-2 w-2 rounded-full bg-amber-500 ring-2 ring-white dark:ring-slate-900" />
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end" className="w-80 p-2">
            <DropdownMenuLabel className="text-xs font-bold text-slate-800 dark:text-slate-200 flex items-center justify-between">
              <span>Staff Room Alerts</span>
              <Badge variant="outline" className="text-[10px] bg-amber-50 text-amber-700 border-amber-200">
                2 Unread
              </Badge>
            </DropdownMenuLabel>
            <DropdownMenuSeparator />
            <div className="space-y-1 py-1">
              <div className="p-2 rounded-lg bg-amber-50/60 dark:bg-amber-950/30 text-xs">
                <p className="font-bold text-amber-900 dark:text-amber-200 flex items-center gap-1">
                  <Clock className="h-3 w-3" /> Attendance Cutoff Approaching
                </p>
                <p className="text-[11px] text-amber-800/80 dark:text-amber-300 mt-0.5">
                  Class 10-A daily roll call must be locked before 09:00 AM.
                </p>
              </div>
              <div className="p-2 rounded-lg bg-slate-50 dark:bg-slate-800/50 text-xs">
                <p className="font-bold text-slate-900 dark:text-slate-100">
                  Proxy Assigned: Period 7
                </p>
                <p className="text-[11px] text-slate-500 mt-0.5">
                  Assigned substitution for Physics (Class 9-C, Room 205).
                </p>
              </div>
            </div>
          </DropdownMenuContent>
        </DropdownMenu>

        {/* User Avatar */}
        <div className="flex items-center gap-2 pl-1 border-l border-slate-200 dark:border-slate-800">
          <Avatar className="h-8 w-8 ring-1 ring-emerald-500/30">
            <AvatarFallback className="bg-emerald-100 text-emerald-800 font-bold text-xs">
              VS
            </AvatarFallback>
          </Avatar>
          <div className="hidden xl:flex flex-col text-left leading-tight">
            <span className="text-xs font-bold text-slate-900 dark:text-slate-100">
              Dr. Vikram Seth
            </span>
            <span className="text-[10px] text-slate-500 font-medium">
              Mathematics PGT
            </span>
          </div>
        </div>
      </div>
    </header>
  );
}

