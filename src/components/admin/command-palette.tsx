"use client";

import * as React from "react";
import { useRouter } from "next/navigation";
import {
  Search,
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
  FolderLock,
  ArrowRight,
  Sparkles,
} from "lucide-react";
import {
  Dialog,
  DialogContent,
} from "@/components/ui/dialog";
import { cn } from "@/lib/utils";

interface CommandPaletteProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  onTriggerEnroll?: () => void;
  onTriggerBroadcast?: () => void;
}

interface CommandItem {
  id: string;
  category: "PAGES" | "STUDENTS" | "STAFF" | "ACTIONS";
  title: string;
  subtitle: string;
  icon: React.ElementType;
  href?: string;
  action?: () => void;
}

export function CommandPalette({
  open,
  onOpenChange,
  onTriggerEnroll,
  onTriggerBroadcast,
}: CommandPaletteProps) {
  const router = useRouter();
  const [query, setQuery] = React.useState("");

  const items: CommandItem[] = [
    // Pages
    { id: "p-1", category: "PAGES", title: "Executive Command Center", subtitle: "Main administrative KPI dashboard", icon: LayoutDashboard, href: "/admin" },
    { id: "p-2", category: "PAGES", title: "Student Master Directory", subtitle: "18,450 enrolled student records", icon: Users, href: "/admin/students" },
    { id: "p-3", category: "PAGES", title: "Admissions Pipeline", subtitle: "234 applicant evaluations & interview reviews", icon: UserPlus, href: "/admin/admissions" },
    { id: "p-4", category: "PAGES", title: "Academic Structure & Curricula", subtitle: "CBSE grade levels, sections & teacher assignments", icon: BookOpen, href: "/admin/academics" },
    { id: "p-5", category: "PAGES", title: "Attendance & Compliance Hub", subtitle: "Batch section attendance, absences & audit logs", icon: CalendarCheck2, href: "/admin/attendance" },
    { id: "p-6", category: "PAGES", title: "Examinations & Assessments", subtitle: "Term exam schedules, mark entries & report cards", icon: Award, href: "/admin/examinations" },
    { id: "p-7", category: "PAGES", title: "Fee Management & Ledger", subtitle: "₹4.85 Cr collections, invoices & reconciliation", icon: Receipt, href: "/admin/finance" },
    { id: "p-8", category: "PAGES", title: "Operations, Fleet & Facilities", subtitle: "28 school buses GPS, facilities & library", icon: Bus, href: "/admin/operations" },
    { id: "p-9", category: "PAGES", title: "Faculty & Staff Directory", subtitle: "420 instructional and admin staff", icon: Briefcase, href: "/admin/staff" },
    { id: "p-10", category: "PAGES", title: "Communication & Broadcast", subtitle: "Multi-channel SMS, WhatsApp & portal circulars", icon: Send, href: "/admin/communication" },
    { id: "p-11", category: "PAGES", title: "System Governance & Schemas", subtitle: "PostgreSQL 12 schemas & AWS Mumbai telemetry", icon: Settings, href: "/admin/settings" },

    // Actions
    {
      id: "a-1",
      category: "ACTIONS",
      title: "Enroll New Student",
      subtitle: "Open full registration modal with document custody",
      icon: UserPlus,
      action: () => {
        onOpenChange(false);
        if (onTriggerEnroll) onTriggerEnroll();
        else router.push("/admin/students");
      },
    },
    {
      id: "a-2",
      category: "ACTIONS",
      title: "Broadcast Official Circular",
      subtitle: "Send SMS/Email/Portal alert to parents and staff",
      icon: Send,
      action: () => {
        onOpenChange(false);
        if (onTriggerBroadcast) onTriggerBroadcast();
        else router.push("/admin/communication");
      },
    },

    // Students
    { id: "s-1", category: "STUDENTS", title: "Aarav Sharma", subtitle: "ADM-2024-0012 • Grade 10-A • 96.2% Attendance", icon: Users, href: "/admin/students" },
    { id: "s-2", category: "STUDENTS", title: "Diya Patel", subtitle: "ADM-2023-0145 • Grade 12-B • Science Wing", icon: Users, href: "/admin/students" },
    { id: "s-3", category: "STUDENTS", title: "Rohan Deshmukh", subtitle: "ADM-2022-0312 • Grade 9-C • Absence Follow-up", icon: Users, href: "/admin/students" },
    { id: "s-4", category: "STUDENTS", title: "Ananya Iyer", subtitle: "ADM-2024-0098 • Grade 11-A • Fees Cleared", icon: Users, href: "/admin/students" },

    // Staff
    { id: "st-1", category: "STAFF", title: "Dr. Vikram Seth", subtitle: "Science Department HOD • EMP-084", icon: Briefcase, href: "/admin/staff" },
    { id: "st-2", category: "STAFF", title: "Pooja Hegde", subtitle: "Mathematics PGT • Grade 11-A Class Teacher • EMP-112", icon: Briefcase, href: "/admin/staff" },
  ];

  const filtered = items.filter((item) =>
    item.title.toLowerCase().includes(query.toLowerCase()) ||
    item.subtitle.toLowerCase().includes(query.toLowerCase())
  );

  const handleSelect = (item: CommandItem) => {
    onOpenChange(false);
    if (item.action) {
      item.action();
    } else if (item.href) {
      router.push(item.href);
    }
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-xl p-0 overflow-hidden shadow-2xl border-slate-200 dark:border-slate-800">
        <div className="flex items-center gap-2.5 border-b border-slate-100 dark:border-slate-800 px-4 py-3 bg-white dark:bg-slate-900">
          <Search className="h-4 w-4 text-slate-400 shrink-0" />
          <input
            autoFocus
            placeholder="Type a command, student, teacher, or page..."
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            className="w-full bg-transparent text-sm text-slate-900 dark:text-slate-100 placeholder:text-slate-400 focus:outline-none"
          />
          <kbd className="hidden sm:inline-block rounded border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 px-1.5 py-0.5 text-[10px] font-mono text-slate-400">
            ESC
          </kbd>
        </div>

        <div className="max-h-80 overflow-y-auto p-2 space-y-1">
          {filtered.length === 0 ? (
            <div className="py-8 text-center text-xs text-slate-400">
              No matching commands or records found for &ldquo;{query}&rdquo;
            </div>
          ) : (
            filtered.map((item) => (
              <button
                key={item.id}
                onClick={() => handleSelect(item)}
                className="w-full flex items-center justify-between p-2 rounded-lg hover:bg-slate-50 dark:hover:bg-slate-800 text-left transition-colors group cursor-pointer"
              >
                <div className="flex items-center gap-2.5 overflow-hidden">
                  <div className="h-7 w-7 rounded-md bg-slate-100 dark:bg-slate-800 flex items-center justify-center text-slate-600 dark:text-slate-300 group-hover:bg-blue-50 group-hover:text-blue-600 shrink-0 transition-colors">
                    <item.icon className="h-3.5 w-3.5" />
                  </div>
                  <div className="flex flex-col truncate">
                    <span className="text-xs font-semibold text-slate-900 dark:text-slate-100 group-hover:text-blue-600 transition-colors truncate">
                      {item.title}
                    </span>
                    <span className="text-[11px] text-slate-400 truncate">
                      {item.subtitle}
                    </span>
                  </div>
                </div>
                <div className="flex items-center gap-1.5 shrink-0 pl-2">
                  <span className="text-[9px] font-bold uppercase tracking-wider text-slate-400">
                    {item.category}
                  </span>
                  <ArrowRight className="h-3 w-3 text-slate-300 group-hover:text-blue-600 group-hover:translate-x-0.5 transition-all" />
                </div>
              </button>
            ))
          )}
        </div>

        <div className="flex items-center justify-between border-t border-slate-100 dark:border-slate-800 px-4 py-2 bg-slate-50/60 dark:bg-slate-900/60 text-[11px] text-slate-400">
          <span>Navigate with mouse or keyboard</span>
          <span>School ERP Spotlight (⌘K)</span>
        </div>
      </DialogContent>
    </Dialog>
  );
}

