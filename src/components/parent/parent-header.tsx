"use client";

import React, { useState } from "react";
import Link from "next/link";
import { usePathname } from "next/navigation";
import {
  Bell,
  ChevronDown,
  GraduationCap,
  Moon,
  Sun,
  ShieldCheck,
  UserCheck,
  LogOut,
  Sparkles,
} from "lucide-react";
import { useTheme } from "next-themes";
import { Button } from "@/components/ui/button";
import { PARENT_USER_DATA, ChildProfile } from "@/lib/mock/parent-mock";

interface ParentHeaderProps {
  activeChild: ChildProfile;
  onChildChange: (child: ChildProfile) => void;
}

export function ParentHeader({ activeChild, onChildChange }: ParentHeaderProps) {
  const { theme, setTheme } = useTheme();
  const pathname = usePathname();
  const [showChildDropdown, setShowChildDropdown] = useState(false);

  return (
    <header className="sticky top-0 z-40 w-full border-b border-border/80 bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60">
      <div className="mx-auto flex h-16 max-w-7xl items-center justify-between px-4 sm:px-6 lg:px-8">
        {/* Left: Branding & Sibling Switcher */}
        <div className="flex items-center gap-3 sm:gap-6">
          <Link href="/parent" className="flex items-center gap-2 group">
            <div className="flex h-9 w-9 items-center justify-center rounded-lg bg-emerald-600 text-white shadow-sm transition-transform group-hover:scale-105">
              <GraduationCap className="h-5 w-5" />
            </div>
            <div className="hidden sm:block">
              <span className="text-sm font-semibold tracking-tight block">Apex Academy</span>
              <span className="text-[10px] text-muted-foreground uppercase tracking-widest font-mono">
                Parent & Family Portal
              </span>
            </div>
          </Link>

          {/* Sibling Switcher Dropdown */}
          <div className="relative">
            <button
              onClick={() => setShowChildDropdown(!showChildDropdown)}
              className="flex items-center gap-2.5 rounded-full border border-border/80 bg-secondary/40 px-3 py-1.5 text-xs font-medium transition-colors hover:bg-secondary/70 focus:outline-none focus:ring-1 focus:ring-emerald-500"
            >
              <img
                src={activeChild.avatarUrl}
                alt={activeChild.fullName}
                className="h-6 w-6 rounded-full object-cover border border-emerald-500/40"
              />
              <div className="text-left">
                <span className="font-semibold block leading-tight">{activeChild.fullName}</span>
                <span className="text-[10px] text-muted-foreground leading-none">
                  {activeChild.gradeLevel} • {activeChild.section}
                </span>
              </div>
              <ChevronDown className="h-3.5 w-3.5 text-muted-foreground ml-0.5" />
            </button>

            {/* Dropdown Menu */}
            {showChildDropdown && (
              <>
                <div
                  className="fixed inset-0 z-50"
                  onClick={() => setShowChildDropdown(false)}
                />
                <div className="absolute left-0 mt-2 z-50 w-64 rounded-xl border border-border bg-card p-1.5 shadow-xl animate-in fade-in zoom-in-95">
                  <div className="px-2.5 py-1.5 text-[11px] font-medium text-muted-foreground uppercase tracking-wider">
                    Select Ward / Child
                  </div>
                  {PARENT_USER_DATA.children.map((child) => (
                    <button
                      key={child.id}
                      onClick={() => {
                        onChildChange(child);
                        setShowChildDropdown(false);
                      }}
                      className={`flex w-full items-center gap-3 rounded-lg px-2.5 py-2 text-xs transition-colors ${
                        activeChild.id === child.id
                          ? "bg-emerald-500/10 text-emerald-600 dark:text-emerald-400 font-semibold"
                          : "text-foreground hover:bg-muted"
                      }`}
                    >
                      <img
                        src={child.avatarUrl}
                        alt={child.fullName}
                        className="h-8 w-8 rounded-full object-cover border border-border"
                      />
                      <div className="text-left flex-1">
                        <div className="font-medium">{child.fullName}</div>
                        <div className="text-[11px] text-muted-foreground font-mono">
                          {child.gradeLevel}-{child.section} • Roll #{child.rollNumber}
                        </div>
                      </div>
                      {activeChild.id === child.id && (
                        <div className="h-2 w-2 rounded-full bg-emerald-500" />
                      )}
                    </button>
                  ))}
                </div>
              </>
            )}
          </div>
        </div>

        {/* Right: Actions, Theme Toggle, Parent Profile */}
        <div className="flex items-center gap-2 sm:gap-3">
          {/* Quick Notice Pill */}
          <Link
            href="/parent/circulars"
            className="hidden md:flex items-center gap-1.5 rounded-full bg-amber-500/10 px-3 py-1 text-xs font-medium text-amber-600 dark:text-amber-400 border border-amber-500/20 hover:bg-amber-500/20 transition-colors"
          >
            <Sparkles className="h-3.5 w-3.5" />
            <span>PTM Notice This Saturday</span>
          </Link>

          {/* Theme Toggle */}
          <Button
            variant="ghost"
            size="icon"
            onClick={() => setTheme(theme === "dark" ? "light" : "dark")}
            className="h-9 w-9 text-muted-foreground hover:text-foreground"
          >
            <Sun className="h-4 w-4 rotate-0 scale-100 transition-all dark:-rotate-90 dark:scale-0" />
            <Moon className="absolute h-4 w-4 rotate-90 scale-0 transition-all dark:rotate-0 dark:scale-100" />
            <span className="sr-only">Toggle theme</span>
          </Button>

          {/* Notifications */}
          <Link href="/parent/circulars">
            <Button
              variant="ghost"
              size="icon"
              className="relative h-9 w-9 text-muted-foreground hover:text-foreground"
            >
              <Bell className="h-4 w-4" />
              <span className="absolute top-2 right-2 h-2 w-2 rounded-full bg-emerald-500 animate-pulse" />
            </Button>
          </Link>

          {/* Parent User Info */}
          <div className="hidden lg:flex items-center gap-2.5 pl-2 border-l border-border/80">
            <div className="text-right">
              <span className="text-xs font-semibold block leading-tight">{PARENT_USER_DATA.name}</span>
              <span className="text-[10px] text-muted-foreground leading-none">Father of Aarav & Rhea</span>
            </div>
            <div className="flex h-8 w-8 items-center justify-center rounded-full bg-emerald-100 text-emerald-700 dark:bg-emerald-950 dark:text-emerald-300 font-semibold text-xs border border-emerald-500/30">
              RS
            </div>
          </div>

          {/* Logout Link */}
          <Link href="/login">
            <Button
              variant="ghost"
              size="icon"
              className="h-9 w-9 text-muted-foreground hover:text-rose-500 transition-colors"
              title="Sign Out"
            >
              <LogOut className="h-4 w-4" />
            </Button>
          </Link>
        </div>
      </div>
    </header>
  );
}

