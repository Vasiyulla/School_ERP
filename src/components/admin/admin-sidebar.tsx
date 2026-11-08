"use client";

import * as React from "react";
import Link from "next/link";
import { usePathname } from "next/navigation";
import {
  GraduationCap,
  LayoutDashboard,
  Users,
  UserPlus,
  BookOpen,
  CalendarCheck2,
  Award,
  Receipt,
  Bus,
  Briefcase,
  Send,
  Settings,
  ChevronLeft,
  ChevronRight,
  Building2,
} from "lucide-react";
import { cn } from "@/lib/utils";
import { Button } from "@/components/ui/button";

interface NavGroup {
  groupName?: string;
  items: {
    title: string;
    href: string;
    icon: React.ElementType;
    badge?: string;
    badgeVariant?: "default" | "amber" | "emerald";
  }[];
}

const navGroups: NavGroup[] = [
  {
    groupName: "CORE",
    items: [
      {
        title: "Command Center",
        href: "/admin",
        icon: LayoutDashboard,
      },
    ],
  },
  {
    groupName: "STUDENT LIFECYCLE",
    items: [
      {
        title: "Student Directory",
        href: "/admin/students",
        icon: Users,
        badge: "18.4k",
        badgeVariant: "default",
      },
      {
        title: "Admissions Pipeline",
        href: "/admin/admissions",
        icon: UserPlus,
        badge: "234",
        badgeVariant: "amber",
      },
    ],
  },
  {
    groupName: "ACADEMICS",
    items: [
      {
        title: "Academic Structure",
        href: "/admin/academics",
        icon: BookOpen,
      },
      {
        title: "Attendance Hub",
        href: "/admin/attendance",
        icon: CalendarCheck2,
        badge: "94.8%",
        badgeVariant: "emerald",
      },
      {
        title: "Examinations",
        href: "/admin/examinations",
        icon: Award,
      },
    ],
  },
  {
    groupName: "FINANCE & OPERATIONS",
    items: [
      {
        title: "Fees & Ledger",
        href: "/admin/finance",
        icon: Receipt,
        badge: "₹4.8Cr",
        badgeVariant: "emerald",
      },
      {
        title: "Operations & Fleet",
        href: "/admin/operations",
        icon: Bus,
      },
      {
        title: "Staff & Teachers",
        href: "/admin/staff",
        icon: Briefcase,
        badge: "420",
        badgeVariant: "default",
      },
      {
        title: "Communication",
        href: "/admin/communication",
        icon: Send,
      },
    ],
  },
  {
    groupName: "GOVERNANCE",
    items: [
      {
        title: "System & Schemas",
        href: "/admin/settings",
        icon: Settings,
      },
    ],
  },
];

export function AdminSidebar() {
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
            className="flex h-8 w-8 shrink-0 items-center justify-center rounded-lg bg-blue-600 text-white shadow-xs hover:bg-blue-700 transition-colors"
            title="Apex Academy - Click to expand sidebar"
            aria-label="Expand sidebar"
          >
            <GraduationCap className="h-4 w-4" />
          </button>
        ) : (
          <>
            <Link
              href="/admin"
              className="flex items-center gap-2.5 overflow-hidden"
            >
              <div className="flex h-8 w-8 shrink-0 items-center justify-center rounded-lg bg-blue-600 text-white shadow-xs">
                <GraduationCap className="h-4 w-4" />
              </div>
              <div className="flex flex-col truncate leading-tight">
                <span className="font-bold text-xs tracking-tight text-slate-900 dark:text-white truncate">
                  Apex Academy
                </span>
                <span className="text-[10px] font-medium text-slate-500 truncate flex items-center gap-1">
                  <span>CBSE #113042</span>
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

      {/* Navigation Groups */}
      <div
        className={cn(
          "flex-1 overflow-y-auto py-3 space-y-4",
          isCollapsed ? "px-1.5" : "px-2"
        )}
      >
        {navGroups.map((group, gIdx) => (
          <div key={gIdx} className="space-y-0.5">
            {!isCollapsed && group.groupName && (
              <p className="px-2 pb-1 text-[9px] font-bold uppercase tracking-wider text-slate-400 dark:text-slate-500 select-none">
                {group.groupName}
              </p>
            )}
            {group.items.map((item) => {
              const isActive =
                pathname === item.href ||
                (item.href !== "/admin" && pathname.startsWith(item.href));

              if (isCollapsed) {
                return (
                  <Link
                    key={item.href}
                    href={item.href}
                    title={item.title}
                    className={cn(
                      "flex h-8 w-8 mx-auto items-center justify-center rounded-lg transition-colors relative my-1",
                      isActive
                        ? "bg-blue-600 text-white shadow-xs"
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
                            : "bg-blue-500",
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
                      ? "bg-blue-50 text-blue-700 font-semibold dark:bg-blue-950/60 dark:text-blue-300"
                      : "text-slate-600 hover:bg-slate-100/80 hover:text-slate-900 dark:text-slate-400 dark:hover:bg-slate-800 dark:hover:text-slate-200"
                  )}
                >
                  <item.icon
                    className={cn(
                      "h-3.5 w-3.5 shrink-0 transition-colors",
                      isActive
                        ? "text-blue-600 dark:text-blue-400"
                        : "text-slate-400 group-hover:text-slate-600 dark:group-hover:text-slate-300"
                    )}
                  />
                  <div className="flex flex-1 items-center justify-between overflow-hidden">
                    <span className="truncate">{item.title}</span>
                    {item.badge && (
                      <span
                        className={cn(
                          "rounded px-1.5 py-0.2 text-[9px] font-semibold tracking-tight",
                          isActive
                            ? "bg-blue-100 text-blue-700 dark:bg-blue-900/60 dark:text-blue-200"
                            : item.badgeVariant === "amber"
                            ? "bg-amber-50 text-amber-700 border border-amber-200/60 dark:bg-amber-950/40 dark:text-amber-300 dark:border-amber-800"
                            : "bg-slate-100 text-slate-600 border border-slate-200/60 dark:bg-slate-800 dark:text-slate-400 dark:border-slate-700"
                        )}
                      >
                        {item.badge}
                      </span>
                    )}
                  </div>
                </Link>
              );
            })}
          </div>
        ))}
      </div>

      {/* Expand Button for Collapsed Mode (Bottom Bar) */}
      {isCollapsed && (
        <div className="p-1 border-t border-slate-200/80 dark:border-slate-800 flex justify-center">
          <Button
            variant="ghost"
            size="icon"
            onClick={() => setIsCollapsed(false)}
            className="h-7 w-7 text-slate-400 hover:text-slate-700 hover:bg-slate-100 dark:hover:bg-slate-800 rounded-md p-0"
            title="Expand sidebar"
            aria-label="Expand sidebar"
          >
            <ChevronRight className="h-3.5 w-3.5" />
          </Button>
        </div>
      )}

      {/* Enterprise Deployment Footer */}
      <div
        className={cn(
          "border-t border-slate-200/80 dark:border-slate-800 bg-slate-50/60 dark:bg-slate-900/50",
          isCollapsed ? "p-1.5 flex justify-center" : "p-2"
        )}
      >
        {isCollapsed ? (
          <div
            title="Main Campus (Mumbai) • AY 2026-27"
            className="flex h-7 w-7 items-center justify-center rounded-md bg-white dark:bg-slate-800 border border-slate-200/80 dark:border-slate-700 text-blue-600 shadow-xs"
          >
            <Building2 className="h-3.5 w-3.5 shrink-0" />
          </div>
        ) : (
          <div className="flex items-center gap-2 rounded-md bg-white dark:bg-slate-800/90 p-1.5 border border-slate-200/70 dark:border-slate-700/70 shadow-xs">
            <Building2 className="h-3.5 w-3.5 shrink-0 text-blue-600 dark:text-blue-400" />
            <div className="flex flex-col text-[11px] truncate leading-tight">
              <span className="font-semibold text-slate-800 dark:text-slate-200 truncate">
                Main Campus
              </span>
              <span className="text-[10px] text-slate-500 dark:text-slate-400">
                AY 2026-27 • Sem 2
              </span>
            </div>
          </div>
        )}
      </div>
    </aside>
  );
}
