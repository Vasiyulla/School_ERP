"use client";

import React, { useState } from "react";
import {
  GraduationCap,
  Award,
  Download,
  FileCheck2,
  BookOpen,
  Sparkles,
  Printer,
  ShieldCheck,
  QrCode,
} from "lucide-react";
import { useParentContext } from "@/components/parent/parent-context";
import { MOCK_AARAV_MARKS } from "@/lib/mock/parent-mock";
import { Button } from "@/components/ui/button";

export default function ParentAcademicsPage() {
  const { activeChild } = useParentContext();
  const [selectedTerm, setSelectedTerm] = useState("Term 1 Mid-Term");

  const totalObtained = MOCK_AARAV_MARKS.reduce((acc, m) => acc + m.totalScore, 0);
  const totalMax = MOCK_AARAV_MARKS.reduce((acc, m) => acc + m.maxScore, 0);
  const percentage = ((totalObtained / totalMax) * 100).toFixed(1);

  return (
    <div className="mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8 space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-xl sm:text-2xl font-bold tracking-tight">Academic Progress & Continuous Evaluation</h1>
          <p className="text-xs text-muted-foreground mt-0.5">
            Ward: <strong className="text-foreground">{activeChild.fullName}</strong> ({activeChild.gradeLevel}-{activeChild.section}) • CBSE Affiliation #113042
          </p>
        </div>

        <div className="flex items-center gap-2">
          <select
            value={selectedTerm}
            onChange={(e) => setSelectedTerm(e.target.value)}
            className="rounded-lg border border-border bg-card px-3 py-1.5 text-xs font-semibold focus:outline-none focus:ring-1 focus:ring-emerald-500"
          >
            <option>Term 1 Mid-Term Examination</option>
            <option>Periodic Assessment 1 (July 2026)</option>
            <option>Final Term 2 Board Mock</option>
          </select>
          <Button
            onClick={() => alert(`Printing official digital report card for ${activeChild.fullName}...`)}
            size="sm"
            className="bg-emerald-600 hover:bg-emerald-700 text-white text-xs gap-1.5 h-8"
          >
            <Download className="h-3.5 w-3.5" /> Download Report Card PDF
          </Button>
        </div>
      </div>

      {/* Aggregate Score & Grade Summary Cards */}
      <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
        <div className="rounded-2xl border border-border/80 bg-card p-4 shadow-sm">
          <div className="text-xs text-muted-foreground font-medium mb-1">Aggregate Marks</div>
          <div className="text-2xl font-bold font-mono text-foreground">
            {totalObtained} <span className="text-sm font-normal text-muted-foreground">/ {totalMax}</span>
          </div>
          <div className="text-xs text-emerald-600 dark:text-emerald-400 font-semibold mt-1">
            Overall: {percentage}%
          </div>
        </div>

        <div className="rounded-2xl border border-border/80 bg-card p-4 shadow-sm">
          <div className="text-xs text-muted-foreground font-medium mb-1">CBSE Cumulative GPA</div>
          <div className="text-2xl font-bold font-mono text-foreground">
            {activeChild.overallGpa} <span className="text-sm font-normal text-muted-foreground">/ 10</span>
          </div>
          <div className="text-xs text-emerald-600 dark:text-emerald-400 font-semibold mt-1">
            Grade A1 (Outstanding)
          </div>
        </div>

        <div className="rounded-2xl border border-border/80 bg-card p-4 shadow-sm">
          <div className="text-xs text-muted-foreground font-medium mb-1">Class Standing</div>
          <div className="text-2xl font-bold font-mono text-foreground">
            Rank #1 <span className="text-sm font-normal text-muted-foreground">in 10-A</span>
          </div>
          <div className="text-xs text-muted-foreground mt-1">
            Cohort size: 38 Students
          </div>
        </div>

        <div className="rounded-2xl border border-border/80 bg-card p-4 shadow-sm">
          <div className="text-xs text-muted-foreground font-medium mb-1">Board Evaluation Status</div>
          <div className="text-xl font-bold text-emerald-600 dark:text-emerald-400 flex items-center gap-1.5 mt-0.5">
            <FileCheck2 className="h-5 w-5" />
            Verified & Stamped
          </div>
          <div className="text-xs text-muted-foreground mt-1">
            Principal Digital Seal Active
          </div>
        </div>
      </div>

      {/* CBSE Subject Score Matrix Table */}
      <div className="rounded-2xl border border-border/80 bg-card overflow-hidden shadow-sm">
        <div className="border-b border-border p-4 bg-muted/20 flex items-center justify-between">
          <div>
            <h2 className="text-sm font-semibold tracking-tight">Scholastic Subject Assessment Matrix</h2>
            <p className="text-[11px] text-muted-foreground">Theory (80M) + Practical / Internal Assessment (20M)</p>
          </div>
          <span className="rounded bg-emerald-500/10 text-emerald-600 dark:text-emerald-400 px-2.5 py-0.5 text-xs font-mono font-semibold">
            CBSE 9-Point Scale
          </span>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left text-xs">
            <thead>
              <tr className="border-b border-border/80 text-muted-foreground font-mono bg-muted/10">
                <th className="py-3 px-4 font-medium">Subject Code & Name</th>
                <th className="py-3 px-4 font-medium text-center">Theory (80)</th>
                <th className="py-3 px-4 font-medium text-center">Practical (20)</th>
                <th className="py-3 px-4 font-medium text-center">Internal (20)</th>
                <th className="py-3 px-4 font-medium text-center">Total (100)</th>
                <th className="py-3 px-4 font-medium text-center">Grade Band</th>
                <th className="py-3 px-4 font-medium">Faculty Observation & Remarks</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-border/60">
              {MOCK_AARAV_MARKS.map((mark) => (
                <tr key={mark.subjectCode} className="hover:bg-muted/30 transition-colors">
                  <td className="py-3.5 px-4">
                    <div className="font-semibold text-foreground">{mark.subject}</div>
                    <div className="text-[10px] text-muted-foreground font-mono">{mark.subjectCode}</div>
                  </td>
                  <td className="py-3.5 px-4 text-center font-mono">{mark.theoryScore}</td>
                  <td className="py-3.5 px-4 text-center font-mono">
                    {mark.practicalScore > 0 ? mark.practicalScore : "—"}
                  </td>
                  <td className="py-3.5 px-4 text-center font-mono">{mark.internalScore}</td>
                  <td className="py-3.5 px-4 text-center font-mono font-bold text-foreground">
                    {mark.totalScore}
                  </td>
                  <td className="py-3.5 px-4 text-center">
                    <span className="inline-block rounded bg-emerald-500/15 text-emerald-600 dark:text-emerald-400 font-mono font-bold px-2 py-0.5 text-xs">
                      {mark.gradeBand}
                    </span>
                  </td>
                  <td className="py-3.5 px-4 text-muted-foreground italic text-[11px] max-w-xs">
                    "{mark.teacherRemarks}"
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Official Certificate Verification Card */}
      <div className="rounded-2xl border border-border/80 bg-card p-5 sm:p-6 shadow-sm flex flex-col sm:flex-row items-center justify-between gap-4">
        <div className="flex items-center gap-4">
          <div className="flex h-14 w-14 items-center justify-center rounded-2xl bg-emerald-500/10 text-emerald-600 dark:text-emerald-400 border border-emerald-500/20">
            <QrCode className="h-8 w-8" />
          </div>
          <div>
            <h3 className="text-sm font-semibold text-foreground">Cryptographic QR Verification Active</h3>
            <p className="text-xs text-muted-foreground mt-0.5 max-w-lg">
              This report card includes a tamper-proof SHA-256 digital signature token. External educational institutions and universities can verify authenticity via the public portal link.
            </p>
          </div>
        </div>

        <Button
          variant="outline"
          size="sm"
          onClick={() => alert(`Opening verification token for student ${activeChild.admissionNumber}...`)}
          className="text-xs gap-1.5 whitespace-nowrap border-border/80"
        >
          <ShieldCheck className="h-3.5 w-3.5" /> View Digital Seal
        </Button>
      </div>
    </div>
  );
}

