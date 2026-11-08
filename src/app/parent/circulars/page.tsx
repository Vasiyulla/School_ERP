"use client";

import React, { useState } from "react";
import {
  FileText,
  Download,
  AlertCircle,
  Bell,
  Sparkles,
  Search,
  Calendar,
  ExternalLink,
  ShieldCheck,
  CheckCircle2,
} from "lucide-react";
import { useParentContext } from "@/components/parent/parent-context";
import { Button } from "@/components/ui/button";

interface CircularNotice {
  id: string;
  title: string;
  summary: string;
  publishedBy: string;
  publishedDate: string;
  priority: "URGENT" | "IMPORTANT" | "NORMAL";
  targetAudience: string;
  attachmentName?: string;
  category: "ACADEMICS" | "EVENTS" | "ADMINISTRATION" | "PTM";
}

const MOCK_NOTICES: CircularNotice[] = [
  {
    id: "circ-01",
    title: "Term 1 Mandatory Parent-Teacher Meeting (PTM) Schedule",
    summary: "Parents of Classes 9 and 10 are invited to discuss continuous assessment performance, lab practical files, and board registration details. Individual slots allocated.",
    publishedBy: "Office of the Principal",
    publishedDate: "2026-11-08",
    priority: "URGENT",
    targetAudience: "Classes 9 & 10 Parents",
    attachmentName: "PTM_Schedule_Slot_Allocations.pdf",
    category: "PTM",
  },
  {
    id: "circ-02",
    title: "Annual Inter-School CBSE Science Olympiad Qualifiers 2026",
    summary: "Registration for the national preliminary round is now open. Students with Grade A1/A2 in Science may register through the school academic department by Nov 20.",
    publishedBy: "Dr. Vikram Seth (Head of Science)",
    publishedDate: "2026-11-06",
    priority: "IMPORTANT",
    targetAudience: "Classes 8 to 11",
    attachmentName: "CBSE_Science_Olympiad_Syllabus.pdf",
    category: "ACADEMICS",
  },
  {
    id: "circ-03",
    title: "Annual Sports Day & Athletic Meet Registration",
    summary: "Track and field trial schedules for 100m, 400m, long jump, and relay events. House masters will conduct selections during sports zero hours.",
    publishedBy: "Department of Physical Education",
    publishedDate: "2026-11-02",
    priority: "NORMAL",
    targetAudience: "All Students & Parents",
    attachmentName: "Sports_Day_Rulebook_2026.pdf",
    category: "EVENTS",
  },
  {
    id: "circ-04",
    title: "Winter School Uniform & Timetable Transition Guidelines",
    summary: "Effective December 1st, all students must transition to the official winter blazer and cardigan uniform. Morning assembly start time adjusted to 08:00 AM.",
    publishedBy: "Administration & Student Affairs",
    publishedDate: "2026-10-28",
    priority: "NORMAL",
    targetAudience: "All Parents",
    category: "ADMINISTRATION",
  },
];

export default function ParentCircularsPage() {
  const { activeChild } = useParentContext();
  const [filter, setFilter] = useState<string>("ALL");
  const [search, setSearch] = useState("");

  const filteredNotices = MOCK_NOTICES.filter((n) => {
    const matchesFilter = filter === "ALL" || n.category === filter;
    const matchesSearch =
      n.title.toLowerCase().includes(search.toLowerCase()) ||
      n.summary.toLowerCase().includes(search.toLowerCase());
    return matchesFilter && matchesSearch;
  });

  return (
    <div className="mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8 space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-xl sm:text-2xl font-bold tracking-tight">Official School Circulars & Broadcasts</h1>
          <p className="text-xs text-muted-foreground mt-0.5">
            Verified Announcements from Principal Office & Academic Heads • Affiliation #113042
          </p>
        </div>

        <div className="flex items-center gap-2">
          <span className="flex items-center gap-1.5 rounded-full bg-emerald-500/10 px-3 py-1 text-xs font-semibold text-emerald-600 dark:text-emerald-400 border border-emerald-500/20">
            <CheckCircle2 className="h-3.5 w-3.5" /> WhatsApp & SMS Synced
          </span>
        </div>
      </div>

      {/* Filter Tabs & Search */}
      <div className="flex flex-col sm:flex-row items-stretch sm:items-center justify-between gap-3">
        <div className="flex items-center gap-1.5 overflow-x-auto pb-1 sm:pb-0">
          {["ALL", "PTM", "ACADEMICS", "EVENTS", "ADMINISTRATION"].map((cat) => (
            <button
              key={cat}
              onClick={() => setFilter(cat)}
              className={`rounded-lg px-3 py-1.5 text-xs font-medium transition-all whitespace-nowrap ${
                filter === cat
                  ? "bg-emerald-600 text-white shadow-sm"
                  : "bg-muted/60 text-muted-foreground hover:bg-muted hover:text-foreground"
              }`}
            >
              {cat === "ALL" ? "All Notices" : cat}
            </button>
          ))}
        </div>

        <div className="relative">
          <Search className="absolute left-3 top-2.5 h-3.5 w-3.5 text-muted-foreground" />
          <input
            type="text"
            placeholder="Search circulars..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="w-full sm:w-64 rounded-xl border border-border bg-card pl-9 pr-3 py-1.5 text-xs focus:ring-1 focus:ring-emerald-500"
          />
        </div>
      </div>

      {/* Circulars List */}
      <div className="space-y-4">
        {filteredNotices.map((notice) => (
          <div
            key={notice.id}
            className={`rounded-2xl border p-5 sm:p-6 transition-all hover:shadow-sm ${
              notice.priority === "URGENT"
                ? "border-rose-500/40 bg-gradient-to-r from-rose-500/5 via-card to-card"
                : notice.priority === "IMPORTANT"
                ? "border-amber-500/30 bg-gradient-to-r from-amber-500/5 via-card to-card"
                : "border-border/80 bg-card"
            }`}
          >
            <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
              <div className="space-y-2">
                <div className="flex items-center gap-2 flex-wrap">
                  <span
                    className={`rounded-full px-2.5 py-0.5 text-[10px] font-bold ${
                      notice.priority === "URGENT"
                        ? "bg-rose-500/15 text-rose-600 dark:text-rose-400"
                        : notice.priority === "IMPORTANT"
                        ? "bg-amber-500/15 text-amber-600 dark:text-amber-400"
                        : "bg-muted text-muted-foreground"
                    }`}
                  >
                    {notice.priority}
                  </span>
                  <span className="rounded bg-muted px-2 py-0.5 text-[10px] font-mono text-muted-foreground">
                    Audience: {notice.targetAudience}
                  </span>
                  <span className="text-[11px] text-muted-foreground font-mono">
                    🗓 {notice.publishedDate}
                  </span>
                </div>

                <h2 className="text-base font-semibold tracking-tight text-foreground">{notice.title}</h2>
                <p className="text-xs text-muted-foreground leading-relaxed max-w-3xl">{notice.summary}</p>
                <div className="text-[11px] text-muted-foreground font-medium">
                  Signed & Issued By: <span className="text-foreground">{notice.publishedBy}</span>
                </div>
              </div>

              {notice.attachmentName && (
                <div className="pt-2 md:pt-0">
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={() => alert(`Downloading official PDF circular ${notice.attachmentName}...`)}
                    className="w-full md:w-auto text-xs gap-1.5 border-border/80"
                  >
                    <Download className="h-3.5 w-3.5" /> Download Circular PDF
                  </Button>
                </div>
              )}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

