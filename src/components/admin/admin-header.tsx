"use client";

import * as React from "react";
import Link from "next/link";
import { usePathname } from "next/navigation";
import { useTheme } from "next-themes";
import {
  Search,
  Sun,
  Moon,
  Bell,
  ChevronRight,
  Shield,
  LogOut,
  User,
  CalendarDays,
  Plus,
  UserPlus,
  Receipt,
  GraduationCap,
  Sparkles,
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
import { CommandPalette } from "@/components/admin/command-palette";
import { EnrollStudentModal } from "@/components/admin/modals/enroll-student-modal";
import { BroadcastNoticeModal } from "@/components/admin/modals/broadcast-notice-modal";
import { cn } from "@/lib/utils";

export function AdminHeader() {
  const pathname = usePathname();
  const { theme, setTheme } = useTheme();
  const [isCommandOpen, setIsCommandOpen] = React.useState(false);
  const [isEnrollOpen, setIsEnrollOpen] = React.useState(false);
  const [isBroadcastOpen, setIsBroadcastOpen] = React.useState(false);
  const [activeCampus, setActiveCampus] = React.useState("Main Campus (Mumbai)");
  const [activeSession, setActiveSession] = React.useState("AY 2026-27 • Semester 2");

  React.useEffect(() => {
    const handleKeyDown = (e: KeyboardEvent) => {
      if ((e.metaKey || e.ctrlKey) && e.key === "k") {
        e.preventDefault();
        setIsCommandOpen((prev) => !prev);
      }
    };
    window.addEventListener("keydown", handleKeyDown);
    return () => window.removeEventListener("keydown", handleKeyDown);
  }, []);

  const getSectionTitle = () => {
    if (pathname.includes("/students")) return "Student Master Directory";
    if (pathname.includes("/admissions")) return "Admissions Pipeline";
    if (pathname.includes("/academics")) return "Academic Structure";
    if (pathname.includes("/attendance")) return "Attendance & Compliance";
    if (pathname.includes("/examinations")) return "Examinations & Assessments";
    if (pathname.includes("/finance")) return "Fee Management & Ledger";
    if (pathname.includes("/operations")) return "Operations, Fleet & Facilities";
    if (pathname.includes("/communication")) return "Communication & Broadcast";
    if (pathname.includes("/staff")) return "Faculty & Staff";
    if (pathname.includes("/settings")) return "System Topology & Settings";
    return "Executive Command Center";
  };

  return (
    <header className="sticky top-0 z-20 flex h-14 w-full items-center justify-between border-b border-slate-200/90 dark:border-slate-800 bg-white dark:bg-slate-900 px-4 md:px-6">
      {/* Left: Breadcrumb Navigation */}
      <div className="flex items-center gap-2 text-xs shrink-0 max-w-[220px] sm:max-w-xs overflow-hidden">
        <span className="text-slate-400 font-medium hidden sm:inline">
          Admin
        </span>
        <ChevronRight className="h-3.5 w-3.5 text-slate-300 dark:text-slate-600 hidden sm:inline shrink-0" />
        <span className="font-bold text-slate-900 dark:text-slate-50 tracking-tight truncate">
          {getSectionTitle()}
        </span>
      </div>

      {/* Middle: Global Search Input */}
      <div className="flex items-center mx-2 sm:mx-4 shrink-0">
        <button
          onClick={() => setIsCommandOpen(true)}
          className="relative w-44 sm:w-56 md:w-64 lg:w-72 text-left"
          aria-label="Search records"
        >
          <Search className="absolute left-2.5 top-1/2 -translate-y-1/2 h-3.5 w-3.5 text-slate-400" />
          <div className="w-full h-8 pl-8 pr-10 text-xs rounded-lg border border-slate-200 dark:border-slate-700 bg-slate-50/90 dark:bg-slate-800/60 text-slate-400 flex items-center overflow-hidden hover:border-blue-400 hover:bg-white dark:hover:bg-slate-900 transition-all cursor-pointer">
            <span className="truncate whitespace-nowrap">Search records...</span>
          </div>
          <kbd className="absolute right-2 top-1/2 -translate-y-1/2 inline-flex h-4 items-center rounded border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-800 px-1 font-sans text-[9px] font-semibold text-slate-400">
            ⌘K
          </kbd>
        </button>
      </div>

      {/* Right Controls */}
      <div className="flex items-center gap-1.5 sm:gap-2.5 shrink-0">
        {/* Interactive Campus & Academic Session Dropdown */}
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <button className="hidden xl:flex items-center gap-1.5 px-2.5 py-1 rounded-full bg-slate-100 hover:bg-slate-200 dark:bg-slate-800 text-slate-700 dark:text-slate-300 text-[11px] font-semibold border border-slate-200 dark:border-slate-700 transition-colors whitespace-nowrap shrink-0">
              <CalendarDays className="h-3 w-3 text-blue-600 dark:text-blue-400 shrink-0" />
              <span className="whitespace-nowrap">AY 2026-27 (Sem 2)</span>
              <span className="text-slate-400">•</span>
              <span className="text-slate-500 font-medium whitespace-nowrap truncate max-w-[90px]">
                {activeCampus.includes("Navi") ? "Navi Mumbai" : "Mumbai"}
              </span>
            </button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end" className="w-64 p-1.5">
            <DropdownMenuLabel className="text-[10px] uppercase text-slate-400 font-bold px-2 py-1">
              Active Campus
            </DropdownMenuLabel>
            <DropdownMenuItem
              onClick={() => setActiveCampus("Main Campus (Mumbai)")}
              className={cn("text-xs py-1.5 cursor-pointer font-medium", activeCampus.includes("Mumbai") && "font-bold text-blue-600 bg-blue-50/50")}
            >
              Main Campus (Mumbai)
            </DropdownMenuItem>
            <DropdownMenuItem
              onClick={() => setActiveCampus("North Campus (Navi Mumbai)")}
              className={cn("text-xs py-1.5 cursor-pointer font-medium", activeCampus.includes("Navi") && "font-bold text-blue-600 bg-blue-50/50")}
            >
              North Campus (Navi Mumbai)
            </DropdownMenuItem>
            <DropdownMenuSeparator />
            <DropdownMenuLabel className="text-[10px] uppercase text-slate-400 font-bold px-2 py-1">
              Academic Session
            </DropdownMenuLabel>
            <DropdownMenuItem
              onClick={() => setActiveSession("AY 2026-27 • Semester 2")}
              className="text-xs py-1.5 cursor-pointer font-medium"
            >
              AY 2026-27 • Semester 2 (Active)
            </DropdownMenuItem>
            <DropdownMenuItem
              onClick={() => setActiveSession("AY 2026-27 • Semester 1")}
              className="text-xs py-1.5 cursor-pointer font-medium"
            >
              AY 2026-27 • Semester 1 (Past)
            </DropdownMenuItem>
            <DropdownMenuItem
              onClick={() => setActiveSession("AY 2025-26 Archive")}
              className="text-xs py-1.5 cursor-pointer font-medium"
            >
              AY 2025-26 (Past Year Archive)
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>

        {/* Quick Action Launcher Dropdown */}
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button size="sm" className="h-8 gap-1.5 bg-blue-600 hover:bg-blue-700 text-white shadow-xs text-xs font-semibold px-2.5 shrink-0">
              <Plus className="h-3.5 w-3.5" /> Quick Action
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end" className="w-56 p-1.5 rounded-xl">
            <DropdownMenuLabel className="text-xs uppercase text-slate-400 font-semibold px-2 py-1">
              Direct Actions
            </DropdownMenuLabel>
            <DropdownMenuItem
              onClick={() => setIsEnrollOpen(true)}
              className="cursor-pointer text-xs py-2 flex items-center gap-2"
            >
              <UserPlus className="h-4 w-4 text-blue-600" />
              <span>New Student Enrollment</span>
            </DropdownMenuItem>
            <DropdownMenuItem asChild>
              <Link href="/admin/admissions" className="cursor-pointer text-xs py-2 flex items-center gap-2">
                <Sparkles className="h-4 w-4 text-amber-600" />
                <span>Review Admissions (234)</span>
              </Link>
            </DropdownMenuItem>
            <DropdownMenuItem asChild>
              <Link href="/admin/attendance" className="cursor-pointer text-xs py-2 flex items-center gap-2">
                <CalendarDays className="h-4 w-4 text-emerald-600" />
                <span>Daily Attendance Hub</span>
              </Link>
            </DropdownMenuItem>
            <DropdownMenuItem asChild>
              <Link href="/admin/finance" className="cursor-pointer text-xs py-2 flex items-center gap-2">
                <Receipt className="h-4 w-4 text-indigo-600" />
                <span>Fee Ledger & Invoicing</span>
              </Link>
            </DropdownMenuItem>
            <DropdownMenuSeparator />
            <DropdownMenuItem
              onClick={() => setIsBroadcastOpen(true)}
              className="cursor-pointer text-xs py-2 flex items-center gap-2"
            >
              <Plus className="h-4 w-4 text-blue-600" />
              <span>Broadcast Notice / Circular</span>
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>

        {/* Theme Switcher */}
        <Button
          variant="ghost"
          size="icon"
          onClick={() => setTheme(theme === "dark" ? "light" : "dark")}
          className="h-8 w-8 text-slate-500 hover:text-slate-900 dark:hover:text-slate-100 shrink-0"
          aria-label="Toggle theme"
        >
          <Sun className="h-4 w-4 rotate-0 scale-100 transition-all dark:-rotate-90 dark:scale-0" />
          <Moon className="absolute h-4 w-4 rotate-90 scale-0 transition-all dark:rotate-0 dark:scale-100" />
        </Button>

        {/* Notifications */}
        <Button
          variant="ghost"
          size="icon"
          className="h-8 w-8 text-slate-500 hover:text-slate-900 dark:hover:text-slate-100 relative shrink-0"
          aria-label="Notifications"
        >
          <Bell className="h-4 w-4" />
          <span className="absolute top-1.5 right-1.5 h-2 w-2 rounded-full bg-blue-600 ring-2 ring-white dark:ring-slate-900" />
        </Button>

        <div className="h-5 w-px bg-slate-200 dark:bg-slate-700 mx-1" />

        {/* User Profile */}
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <button className="flex items-center gap-2.5 p-1 rounded-xl hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors">
              <Avatar className="h-8 w-8 ring-2 ring-blue-600/20">
                <AvatarFallback className="bg-blue-600/10 text-blue-600 font-bold text-xs">
                  RS
                </AvatarFallback>
              </Avatar>
              <div className="flex flex-col text-left hidden sm:flex">
                <span className="text-xs font-semibold leading-tight text-slate-900 dark:text-slate-100">
                  Dr. Rajesh Sharma
                </span>
                <span className="text-[11px] text-slate-500 dark:text-slate-400">
                  Super Admin
                </span>
              </div>
            </button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end" className="w-56 p-1.5 rounded-xl">
            <DropdownMenuLabel className="font-normal px-2 py-1.5">
              <div className="flex flex-col space-y-0.5">
                <p className="text-xs font-semibold text-slate-900 dark:text-slate-100">Dr. Rajesh Sharma</p>
                <p className="text-xs text-slate-500 dark:text-slate-400">
                  admin@apexacademy.edu.in
                </p>
              </div>
            </DropdownMenuLabel>
            <DropdownMenuSeparator />
            <DropdownMenuItem className="text-xs py-2 rounded-lg cursor-pointer">
              <User className="mr-2 h-4 w-4 text-slate-500" /> Profile Settings
            </DropdownMenuItem>
            <DropdownMenuItem className="text-xs py-2 rounded-lg cursor-pointer">
              <Shield className="mr-2 h-4 w-4 text-slate-500" /> RBAC Permissions
            </DropdownMenuItem>
            <DropdownMenuSeparator />
            <DropdownMenuItem className="text-xs py-2 rounded-lg text-rose-600 focus:text-rose-600 cursor-pointer">
              <LogOut className="mr-2 h-4 w-4" /> Sign out
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
      </div>

      {/* Global Command Palette (⌘K) */}
      <CommandPalette
        open={isCommandOpen}
        onOpenChange={setIsCommandOpen}
        onTriggerEnroll={() => setIsEnrollOpen(true)}
        onTriggerBroadcast={() => setIsBroadcastOpen(true)}
      />

      {/* Fast Action Modals */}
      <EnrollStudentModal
        open={isEnrollOpen}
        onOpenChange={setIsEnrollOpen}
      />

      <BroadcastNoticeModal
        open={isBroadcastOpen}
        onOpenChange={setIsBroadcastOpen}
      />
    </header>
  );
}
