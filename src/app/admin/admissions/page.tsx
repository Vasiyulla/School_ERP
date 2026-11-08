"use client";

import * as React from "react";
import {
  UserPlus,
  Clock,
  CheckCircle2,
  CalendarCheck2,
  Search,
  ArrowRight,
  ShieldCheck,
  FolderLock,
} from "lucide-react";
import { mockAdmissions } from "@/lib/mock/admin-mock";
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { DocumentCustodyModal } from "@/components/admin/document-custody-modal";

export default function AdminAdmissionsPage() {
  const [search, setSearch] = React.useState("");
  const [selectedApplicant, setSelectedApplicant] = React.useState<typeof mockAdmissions[0] | null>(null);

  const filtered = mockAdmissions.filter((app) =>
    app.applicantName.toLowerCase().includes(search.toLowerCase()) ||
    app.applicationNumber.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div className="space-y-6 max-w-7xl mx-auto">
      {/* Title */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold tracking-tight text-slate-900 dark:text-slate-50">
            Admissions Pipeline
          </h1>
          <p className="text-sm font-normal text-slate-500 dark:text-slate-400 mt-1">
            Manage incoming applicants, document evaluations, interviews, and enrollment conversion
          </p>
        </div>
        <div className="flex items-center gap-3">
          <Button size="sm" className="gap-2 shadow-sm shadow-primary/20">
            <UserPlus className="h-4 w-4" /> New Admission Registration
          </Button>
        </div>
      </div>

      {/* Pipeline Status Summary */}
      <div className="grid grid-cols-2 md:grid-cols-4 gap-6">
        <Card>
          <CardContent className="p-5 flex items-center gap-4">
            <div className="p-3 rounded-xl bg-blue-50 text-blue-600 dark:bg-blue-950/60 dark:text-blue-400 border border-blue-200/60 shrink-0">
              <Clock className="h-5 w-5" />
            </div>
            <div>
              <p className="text-xs font-semibold uppercase tracking-wider text-slate-500">Under Review</p>
              <p className="text-2xl font-bold text-slate-900 dark:text-slate-50 mt-1">112</p>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-5 flex items-center gap-4">
            <div className="p-3 rounded-xl bg-amber-50 text-amber-600 dark:bg-amber-950/60 dark:text-amber-400 border border-amber-200/60 shrink-0">
              <CalendarCheck2 className="h-5 w-5" />
            </div>
            <div>
              <p className="text-xs font-semibold uppercase tracking-wider text-slate-500">Interview Scheduled</p>
              <p className="text-2xl font-bold text-slate-900 dark:text-slate-50 mt-1">34</p>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-5 flex items-center gap-4">
            <div className="p-3 rounded-xl bg-emerald-50 text-emerald-600 dark:bg-emerald-950/60 dark:text-emerald-400 border border-emerald-200/60 shrink-0">
              <CheckCircle2 className="h-5 w-5" />
            </div>
            <div>
              <p className="text-xs font-semibold uppercase tracking-wider text-slate-500">Approved / Ready</p>
              <p className="text-2xl font-bold text-slate-900 dark:text-slate-50 mt-1">58</p>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-5 flex items-center gap-4">
            <div className="p-3 rounded-xl bg-purple-50 text-purple-600 dark:bg-purple-950/60 dark:text-purple-400 border border-purple-200/60 shrink-0">
              <UserPlus className="h-5 w-5" />
            </div>
            <div>
              <p className="text-xs font-semibold uppercase tracking-wider text-slate-500">Converted to Enrolled</p>
              <p className="text-2xl font-bold text-slate-900 dark:text-slate-50 mt-1">412</p>
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Search Bar */}
      <div className="relative max-w-md">
        <Search className="absolute left-3.5 top-1/2 -translate-y-1/2 h-4 w-4 text-slate-400" />
        <Input
          placeholder="Filter applications by applicant name or number..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          className="pl-10 h-10 text-sm"
        />
      </div>

      {/* Applications Cards Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {filtered.map((app) => (
          <Card key={app.id} className="hover:shadow-sm transition-all duration-200">
            <CardHeader className="pb-4">
              <div className="flex items-start justify-between">
                <div>
                  <span className="text-xs font-semibold text-primary">
                    {app.applicationNumber}
                  </span>
                  <CardTitle className="text-base font-semibold text-slate-900 dark:text-slate-50 mt-1">
                    {app.applicantName}
                  </CardTitle>
                  <CardDescription className="text-xs mt-1">
                    Target: <strong className="text-slate-800 dark:text-slate-200">{app.targetClass}</strong> • Submitted on {app.submissionDate}
                  </CardDescription>
                </div>
                <Badge
                  variant={
                    app.status === "Approved"
                      ? "success"
                      : app.status === "Interview Scheduled"
                      ? "warning"
                      : "info"
                  }
                >
                  {app.status}
                </Badge>
              </div>
            </CardHeader>
            <CardContent className="space-y-4 pt-0">
              <div className="rounded-xl bg-slate-50 dark:bg-slate-800/50 p-4 text-xs space-y-2 border border-slate-200/60 dark:border-slate-700/60">
                <div className="flex justify-between">
                  <span className="text-slate-500 font-medium">Parent / Guardian:</span>
                  <span className="font-semibold text-slate-900 dark:text-slate-100">{app.parentName}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-slate-500 font-medium">Contact Phone:</span>
                  <span className="text-slate-800 dark:text-slate-200">{app.parentPhone}</span>
                </div>
                {app.entranceScore && (
                  <div className="flex justify-between">
                    <span className="text-slate-500 font-medium">Entrance Assessment:</span>
                    <span className="font-bold text-emerald-600 dark:text-emerald-400">
                      {app.entranceScore} / 100
                    </span>
                  </div>
                )}
              </div>

              <div className="flex items-center justify-end gap-2 pt-1">
                <Button
                  variant="outline"
                  size="sm"
                  onClick={() => setSelectedApplicant(app)}
                  className="text-xs h-7 gap-1 border-slate-200 hover:bg-slate-50 text-slate-700"
                >
                  <FolderLock className="h-3 w-3 text-blue-600" />
                  Doc Custody
                </Button>
                {app.status === "Approved" ? (
                  <Button size="sm" className="text-xs h-7 gap-1.5 bg-emerald-600 hover:bg-emerald-700 text-white shadow-xs">
                    <CheckCircle2 className="h-3.5 w-3.5" /> Convert to Enrolled
                  </Button>
                ) : (
                  <Button size="sm" className="text-xs h-7 gap-1.5 bg-blue-600 hover:bg-blue-700 text-white shadow-xs">
                    Process <ArrowRight className="h-3 w-3" />
                  </Button>
                )}
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      {/* Document Custody Modal for Applicant */}
      {selectedApplicant && (
        <DocumentCustodyModal
          open={!!selectedApplicant}
          onOpenChange={(open) => !open && setSelectedApplicant(null)}
          entityType="STUDENT"
          entityName={selectedApplicant.applicantName}
          entityId={selectedApplicant.applicationNumber}
          classNameOrDepartment={`Target Grade: ${selectedApplicant.targetClass}`}
        />
      )}
    </div>
  );
}
