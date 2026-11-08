"use client";

import React from "react";
import Link from "next/link";
import { usePathname } from "next/navigation";
import {
  Home,
  GraduationCap,
  CreditCard,
  Bus,
  CalendarCheck2,
  FileText,
} from "lucide-react";

const NAV_ITEMS = [
  { label: "Overview", href: "/parent", icon: Home },
  { label: "Academics", href: "/parent/academics", icon: GraduationCap },
  { label: "Pay Fees", href: "/parent/fees", icon: CreditCard, badge: "Due" },
  { label: "Bus GPS", href: "/parent/tracking", icon: Bus, live: true },
  { label: "Attendance", href: "/parent/attendance", icon: CalendarCheck2 },
  { label: "Circulars", href: "/parent/circulars", icon: FileText },
];

export function ParentBottomNav() {
  const pathname = usePathname();

  return (
    <>
      {/* Mobile Fixed Bottom Navigation (Visible on < md) */}
      <nav className="fixed bottom-0 left-0 right-0 z-40 block md:hidden border-t border-border/80 bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/80 safe-area-inset-bottom">
        <div className="grid grid-cols-6 h-16 items-center px-1">
          {NAV_ITEMS.map((item) => {
            const isActive = pathname === item.href;
            const Icon = item.icon;

            return (
              <Link
                key={item.href}
                href={item.href}
                className={`relative flex flex-col items-center justify-center gap-1 py-1.5 transition-colors ${
                  isActive
                    ? "text-emerald-600 dark:text-emerald-400 font-semibold"
                    : "text-muted-foreground hover:text-foreground"
                }`}
              >
                <div className="relative">
                  <Icon className={`h-5 w-5 ${isActive ? "scale-110" : ""}`} />
                  {item.live && (
                    <span className="absolute -top-1 -right-1 flex h-2 w-2">
                      <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-emerald-400 opacity-75" />
                      <span className="relative inline-flex rounded-full h-2 w-2 bg-emerald-500" />
                    </span>
                  )}
                  {item.badge && (
                    <span className="absolute -top-1.5 -right-3 rounded-full bg-amber-500 text-[9px] font-bold text-white px-1 py-0.2 leading-tight">
                      {item.badge}
                    </span>
                  )}
                </div>
                <span className="text-[10px] tracking-tight leading-none truncate max-w-[56px]">
                  {item.label}
                </span>
                {isActive && (
                  <span className="absolute bottom-0 h-0.5 w-6 rounded-full bg-emerald-600 dark:bg-emerald-400" />
                )}
              </Link>
            );
          })}
        </div>
      </nav>

      {/* Desktop Top Sub-Navigation Bar (Visible on >= md) */}
      <div className="hidden md:block border-b border-border/60 bg-muted/20">
        <div className="mx-auto flex max-w-7xl items-center gap-1 px-4 sm:px-6 lg:px-8 h-11">
          {NAV_ITEMS.map((item) => {
            const isActive = pathname === item.href;
            const Icon = item.icon;

            return (
              <Link
                key={item.href}
                href={item.href}
                className={`relative flex items-center gap-2 px-3.5 py-1.5 rounded-lg text-xs font-medium transition-all ${
                  isActive
                    ? "bg-emerald-500/10 text-emerald-600 dark:text-emerald-400 shadow-sm"
                    : "text-muted-foreground hover:text-foreground hover:bg-muted/60"
                }`}
              >
                <Icon className="h-3.5 w-3.5" />
                <span>{item.label}</span>
                {item.live && (
                  <span className="flex items-center gap-1 rounded-full bg-emerald-500/15 text-[10px] text-emerald-600 dark:text-emerald-400 px-1.5 py-0.5 font-bold uppercase tracking-wider">
                    <span className="h-1.5 w-1.5 rounded-full bg-emerald-500 animate-pulse" />
                    Live
                  </span>
                )}
                {item.badge && (
                  <span className="rounded-full bg-amber-500/20 text-amber-600 dark:text-amber-400 text-[10px] px-1.5 py-0.2 font-semibold">
                    {item.badge}
                  </span>
                )}
              </Link>
            );
          })}
        </div>
      </div>
    </>
  );
}

