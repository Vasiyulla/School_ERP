"use client";

import * as React from "react";
import Link from "next/link";
import { usePathname } from "next/navigation";
import {
  GraduationCap,
  LayoutDashboard,
  CalendarCheck2,
  Award,
  BookOpen,
  Users,
  Calendar,
  Coffee,
  ChevronLeft,
  ChevronRight,
  ShieldCheck,
  CheckCircle2,
  Clock,
  Sparkles,
} from "lucide-react";
import { cn } from "@/lib/utils";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";

interface NavItem {
  title: string;
  href: string;
  icon: React.ElementType;
  badge?: string;
  badgeVariant?: "default" | "amber" | "emerald";
}

const teacherNavItems: NavItem[] = [
  {
    title: "Faculty Cockpit",
    href: "/teacher",
    icon: LayoutDashboard,
  },
  {
    title: "Class Attendance",
    href: "/teacher/attendance",
    icon: CalendarCheck2,
    badge: "09:00 AM",
    badgeVariant: "amber",
  },
  {
    title: "Gradebook & Marks",
    href: "/teacher/gradebook",
    icon: Award,
    badge: "Term 1",
    badgeVariant: "default",
  },
  {
    title: "Homework & Tasks",
    href: "/teacher/assignments",
    icon: BookOpen,
    badge: "3 Active",
    badgeVariant: "default",
  },
  {
    title: "Class 10-A Roster",
    href: "/teacher/students",
    icon: Users,
    badge: "38",
    badgeVariant: "emerald",
  },
  {
    title: "My Timetable",
    href: "/teacher/schedule",
    icon: Calendar,
  },
  {
    title: "Leaves & Proxy",
    href: "/teacher/leaves",
    icon: Coffee,
  },
];

export function TeacherSidebar() {
  const pathname = usePathname();
  const [isCollapsed, setIsCollapsed] = React.useState(false);

  return (
    <aside
      className={cn(
        "relative flex flex-col bg-white dark:bg-slate-900 text-slate-800 dark:text-slate-200 transition-all duration-200 z-30 shrink-0 border-r border-slate-200/90 dark:border-slate-800 shadow-xs",
        isCollapsed ? "w-14" : "w-52"
      )}
    >
      {/* Brand Header */}
      <div
        className={cn(
          "flex h-14 items-center border-b border-slate-200/80 dark:border-slate-800",
          isCollapsed ? "justify-center px-1" : "justify-between px-3"
        )}
      >
        {isCollapsed ? (
          <button
            onClick={() => setIsCollapsed(false)}
            className="flex h-8 w-8 shrink-0 items-center justify-center rounded-lg bg-emerald-600 text-white shadow-xs hover:bg-emerald-700 transition-colors"
            title="Faculty Portal - Click to expand sidebar"
            aria-label="Expand sidebar"
          >
            <GraduationCap className="h-4 w-4" />
          </button>
        ) : (
          <>
            <Link
              href="/teacher"
              className="flex items-center gap-2.5 overflow-hidden"
            >
              <div className="flex h-8 w-8 shrink-0 items-center justify-center rounded-lg bg-emerald-600 text-white shadow-xs">
                <GraduationCap className="h-4 w-4" />
              </div>
              <div className="flex flex-col truncate leading-tight">
                <span className="font-bold text-xs tracking-tight text-slate-900 dark:text-white truncate">
                  Faculty Portal
                </span>
                <span className="text-[10px] font-medium text-slate-500 truncate flex items-center gap-1">
                  <span>Class Teacher 10-A</span>
                  <span>•</span>
                  <span className="text-emerald-600 font-semibold">Online</span>
                </span>
              </div>
            </Link>
            <Button
              variant="ghost"
              size="icon"
              onClick={() => setIsCollapsed(true)}
              className="h-6 w-6 text-slate-400 hover:text-slate-700 hover:bg-slate-100 dark:hover:bg-slate-800 hidden md:flex rounded-md p-0"
              aria-label="Collapse sidebar"
            >
              <ChevronLeft className="h-3.5 w-3.5" />
            </Button>
          </>
        )}
      </div>

      {/* Navigation Items */}
      <div
        className={cn(
          "flex-1 overflow-y-auto py-3 space-y-1",
          isCollapsed ? "px-1.5" : "px-2"
        )}
      >
        {!isCollapsed && (
          <p className="px-2 pb-1 text-[9px] font-bold uppercase tracking-wider text-slate-400 dark:text-slate-500 select-none">
            CLASSROOM OPERATIONS
          </p>
        )}
        {teacherNavItems.map((item) => {
          const isActive =
            pathname === item.href ||
            (item.href !== "/teacher" && pathname.startsWith(item.href));

          if (isCollapsed) {
            return (
              <Link
                key={item.href}
                href={item.href}
                title={item.title}
                className={cn(
                  "flex h-8 w-8 mx-auto items-center justify-center rounded-lg transition-colors relative my-1",
                  isActive
                    ? "bg-emerald-600 text-white shadow-xs"
                    : "text-slate-500 hover:bg-slate-100 hover:text-slate-900 dark:text-slate-400 dark:hover:bg-slate-800 dark:hover:text-slate-200"
                )}
              >
                <item.icon className="h-4 w-4 shrink-0" />
                {item.badge && (
                  <span
                    className={cn(
                      "absolute top-0.5 right-0.5 h-1.5 w-1.5 rounded-full ring-1 ring-white dark:ring-slate-900",
                      item.badgeVariant === "amber"
                        ? "bg-amber-500"
                        : "bg-emerald-500",
                      isActive && "bg-white"
                    )}
                  />
                )}
              </Link>
            );
          }

          return (
            <Link
              key={item.href}
              href={item.href}
              className={cn(
                "group flex items-center gap-2 rounded-md px-2 py-1.5 text-xs font-medium transition-colors",
                isActive
                  ? "bg-emerald-50 text-emerald-800 font-semibold dark:bg-emerald-950/60 dark:text-emerald-300"
                  : "text-slate-600 hover:bg-slate-100/80 hover:text-slate-900 dark:text-slate-400 dark:hover:bg-slate-800 dark:hover:text-slate-200"
              )}
            >
              <item.icon
                className={cn(
                  "h-4 w-4 shrink-0 transition-colors",
                  isActive
                    ? "text-emerald-600 dark:text-emerald-400"
                    : "text-slate-400 group-hover:text-slate-600 dark:group-hover:text-slate-300"
                )}
              />
              <span className="truncate flex-1">{item.title}</span>
              {item.badge && (
                <span
                  className={cn(
                    "rounded px-1.5 py-0.2 text-[10px] font-bold shrink-0 tracking-tight",
                    item.badgeVariant === "amber"
                      ? "bg-amber-100 text-amber-800 dark:bg-amber-950 dark:text-amber-300 border border-amber-200/60"
                      : item.badgeVariant === "emerald"
                      ? "bg-emerald-100 text-emerald-800 dark:bg-emerald-950 dark:text-emerald-300 border border-emerald-200/60"
                      : "bg-slate-100 text-slate-700 dark:bg-slate-800 dark:text-slate-300 border border-slate-200/60"
                  )}
                >
                  {item.badge}
                </span>
              )}
            </Link>
          );
        })}
      </div>

      {/* Footer Profile & Switcher */}
      <div className="border-t border-slate-200/80 dark:border-slate-800 p-2 space-y-1">
        {!isCollapsed ? (
          <div className="flex flex-col gap-1.5">
            <div className="flex items-center gap-2 p-1.5 rounded-lg bg-slate-50 dark:bg-slate-800/50 border border-slate-200/60">
              <div className="h-7 w-7 rounded-full bg-emerald-100 text-emerald-800 font-bold text-xs flex items-center justify-center shrink-0">
                VS
              </div>
              <div className="truncate flex flex-col leading-tight">
                <span className="text-xs font-bold text-slate-900 dark:text-slate-100 truncate">
                  Dr. Vikram Seth
                </span>
                <span className="text-[10px] text-slate-500 font-mono truncate">
                  EMP-MATH-042
                </span>
              </div>
            </div>
            <Link
              href="/admin"
              className="flex items-center justify-center gap-1.5 py-1 text-[11px] font-semibold text-slate-500 hover:text-blue-600 hover:bg-blue-50 dark:hover:bg-slate-800 rounded transition-colors"
            >
              <ShieldCheck className="h-3 w-3 text-blue-600" />
              <span>Switch to Admin View</span>
            </Link>
          </div>
        ) : (
          <div className="flex flex-col items-center gap-1">
            <Link
              href="/admin"
              className="flex h-8 w-8 items-center justify-center rounded-lg text-slate-400 hover:bg-slate-100 hover:text-blue-600 dark:hover:bg-slate-800 transition-colors"
              title="Switch to Admin View"
            >
              <ShieldCheck className="h-4 w-4" />
            </Link>
          </div>
        )}
      </div>
    </aside>
  );
}

