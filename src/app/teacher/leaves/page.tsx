"use client";

import * as React from "react";
import {
  Coffee,
  Plus,
  Calendar,
  CheckCircle2,
  Clock,
  AlertTriangle,
  UserCheck,
  FileText,
  Sparkles,
} from "lucide-react";
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Input } from "@/components/ui/input";
import { Tabs, TabsList, TabsTrigger, TabsContent } from "@/components/ui/tabs";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
  DialogFooter,
} from "@/components/ui/dialog";
import {
  Table,
  TableHeader,
  TableBody,
  TableRow,
  TableHead,
  TableCell,
} from "@/components/ui/table";
import { mockTeacherLeaves, TeacherLeaveRecord } from "@/lib/mock/teacher-mock";
import { cn } from "@/lib/utils";

export default function TeacherLeavesPage() {
  const [leaves, setLeaves] = React.useState<TeacherLeaveRecord[]>(mockTeacherLeaves);
  const [isApplyOpen, setIsApplyOpen] = React.useState(false);

  // Form states
  const [leaveType, setLeaveType] = React.useState<"CASUAL" | "MEDICAL" | "DUTY_LEAVE">("CASUAL");
  const [startDate, setStartDate] = React.useState("2026-11-20");
  const [endDate, setEndDate] = React.useState("2026-11-20");
  const [leaveReason, setLeaveReason] = React.useState("");

  const handleApply = (e: React.FormEvent) => {
    e.preventDefault();
    if (!leaveReason.trim()) return;

    const newLeave: TeacherLeaveRecord = {
      id: `lev-${Date.now()}`,
      type: leaveType,
      startDate,
      endDate,
      days: 1,
      reason: leaveReason,
      status: "PENDING",
      substituteTeacherName: "Auto-suggested: Rajesh Kulkarni (Math PGT)",
    };

    setLeaves([newLeave, ...leaves]);
    setIsApplyOpen(false);
    setLeaveReason("");
  };

  return (
    <div className="space-y-6 max-w-7xl mx-auto">
      {/* Top Banner */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 p-5 rounded-2xl bg-white dark:bg-slate-900 border border-slate-200/90 dark:border-slate-800 shadow-xs">
        <div className="space-y-1">
          <div className="flex items-center gap-2">
            <span className="text-xs font-bold text-emerald-600 dark:text-emerald-400 uppercase tracking-wider">
              Faculty Welfare & Leaves
            </span>
            <span className="text-slate-300 dark:text-slate-700">•</span>
            <span className="text-xs font-medium text-slate-500">
              Staff Substitution Management
            </span>
          </div>
          <h1 className="text-2xl font-bold tracking-tight text-slate-900 dark:text-slate-50 flex items-center gap-2">
            <Coffee className="h-6 w-6 text-amber-600" />
            Leaves & Substitution Board
          </h1>
          <p className="text-xs text-slate-500 dark:text-slate-400">
            Apply for casual, sick, or duty leave and view assigned peer class proxy duties.
          </p>
        </div>

        <Button
          onClick={() => setIsApplyOpen(true)}
          className="h-9 text-xs font-semibold bg-emerald-600 hover:bg-emerald-700 text-white shadow-xs gap-1.5 px-3.5"
        >
          <Plus className="h-4 w-4" />
          Apply for Leave
        </Button>
      </div>

      {/* Leave Balances Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
        <Card className="shadow-xs border-slate-200/80 p-4">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-[10px] font-bold uppercase text-slate-400">Casual Leave (CL)</p>
              <div className="flex items-baseline gap-2 mt-1">
                <span className="text-2xl font-bold font-mono text-slate-900 dark:text-slate-100">8</span>
                <span className="text-xs text-slate-500">/ 12 days remaining</span>
              </div>
            </div>
            <div className="p-3 rounded-xl bg-blue-50 text-blue-600 dark:bg-blue-950/60 dark:text-blue-400">
              <Calendar className="h-5 w-5" />
            </div>
          </div>
        </Card>

        <Card className="shadow-xs border-slate-200/80 p-4">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-[10px] font-bold uppercase text-slate-400">Medical / Sick Leave (ML)</p>
              <div className="flex items-baseline gap-2 mt-1">
                <span className="text-2xl font-bold font-mono text-slate-900 dark:text-slate-100">6</span>
                <span className="text-xs text-slate-500">/ 10 days remaining</span>
              </div>
            </div>
            <div className="p-3 rounded-xl bg-emerald-50 text-emerald-600 dark:bg-emerald-950/60 dark:text-emerald-400">
              <CheckCircle2 className="h-5 w-5" />
            </div>
          </div>
        </Card>

        <Card className="shadow-xs border-slate-200/80 p-4">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-[10px] font-bold uppercase text-slate-400">Duty Leave (DL)</p>
              <div className="flex items-baseline gap-2 mt-1">
                <span className="text-2xl font-bold font-mono text-slate-900 dark:text-slate-100">4</span>
                <span className="text-xs text-slate-500">days utilized (Workshops)</span>
              </div>
            </div>
            <div className="p-3 rounded-xl bg-purple-50 text-purple-600 dark:bg-purple-950/60 dark:text-purple-400">
              <FileText className="h-5 w-5" />
            </div>
          </div>
        </Card>
      </div>

      {/* Tabs Layout */}
      <Tabs defaultValue="history" className="space-y-6">
        <TabsList className="bg-slate-100 dark:bg-slate-800 p-1 rounded-xl h-11 border border-slate-200 dark:border-slate-700">
          <TabsTrigger value="history" className="text-xs font-semibold rounded-lg data-[state=active]:bg-white dark:data-[state=active]:bg-slate-900 data-[state=active]:shadow-xs flex items-center gap-1.5 px-4 py-2">
            <Clock className="h-3.5 w-3.5 text-blue-600" />
            <span>My Leave History</span>
          </TabsTrigger>
          <TabsTrigger value="proxies" className="text-xs font-semibold rounded-lg data-[state=active]:bg-white dark:data-[state=active]:bg-slate-900 data-[state=active]:shadow-xs flex items-center gap-1.5 px-4 py-2">
            <UserCheck className="h-3.5 w-3.5 text-amber-600" />
            <span>Assigned Proxy Duties (Today)</span>
          </TabsTrigger>
        </TabsList>

        {/* Tab 1: Leave History */}
        <TabsContent value="history" className="space-y-4 m-0">
          <Card className="shadow-xs border-slate-200/80">
            <CardHeader className="pb-3 border-b border-slate-100 dark:border-slate-800">
              <CardTitle className="text-base font-semibold text-slate-900 dark:text-slate-50">
                Leave Applications & Approvals
              </CardTitle>
            </CardHeader>
            <CardContent className="p-0">
              <Table>
                <TableHeader className="bg-slate-50/90 dark:bg-slate-800/60">
                  <TableRow>
                    <TableHead className="w-32 text-xs font-bold text-slate-700">Leave Type</TableHead>
                    <TableHead className="w-48 text-xs font-bold text-slate-700">Dates</TableHead>
                    <TableHead className="w-20 text-center text-xs font-bold text-slate-700">Days</TableHead>
                    <TableHead className="text-xs font-bold text-slate-700">Reason</TableHead>
                    <TableHead className="w-56 text-xs font-bold text-slate-700">Covering Faculty</TableHead>
                    <TableHead className="w-28 text-center text-xs font-bold text-slate-700">Status</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {leaves.map((l) => (
                    <TableRow key={l.id} className="hover:bg-slate-50/50">
                      <TableCell className="py-3">
                        <Badge variant="outline" className="text-[10px] font-bold">
                          {l.type.replace("_", " ")}
                        </Badge>
                      </TableCell>
                      <TableCell className="py-3 text-xs font-mono">
                        {l.startDate} {l.startDate !== l.endDate && `to ${l.endDate}`}
                      </TableCell>
                      <TableCell className="py-3 text-center text-xs font-bold">
                        {l.days}
                      </TableCell>
                      <TableCell className="py-3 text-xs text-slate-600 dark:text-slate-400">
                        {l.reason}
                      </TableCell>
                      <TableCell className="py-3 text-xs text-slate-700 dark:text-slate-300 font-medium">
                        {l.substituteTeacherName}
                      </TableCell>
                      <TableCell className="py-3 text-center">
                        <Badge
                          variant={l.status === "APPROVED" ? "success" : "warning"}
                          className="text-[10px] font-bold"
                        >
                          {l.status}
                        </Badge>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </CardContent>
          </Card>
        </TabsContent>

        {/* Tab 2: Assigned Proxies */}
        <TabsContent value="proxies" className="space-y-4 m-0">
          <Card className="shadow-xs border-slate-200/80 p-4 space-y-3">
            <div className="flex items-center justify-between pb-3 border-b border-slate-100">
              <div>
                <h3 className="text-sm font-bold text-slate-900 dark:text-slate-100">
                  Today&apos;s Substitution Allocation: Period 7
                </h3>
                <p className="text-xs text-slate-500 mt-0.5">
                  Assigned by Academic Coordinator due to faculty leave: Meera Nair (Physics)
                </p>
              </div>
              <Badge variant="warning" className="text-xs font-bold">
                Assigned to Dr. Vikram Seth
              </Badge>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-4 gap-3 text-xs p-3 rounded-xl bg-amber-50/50 border border-amber-200">
              <div>
                <span className="text-[10px] uppercase text-slate-400 font-bold">Time Slot</span>
                <p className="font-mono font-bold text-slate-800 mt-0.5">02:05 – 02:50 PM</p>
              </div>
              <div>
                <span className="text-[10px] uppercase text-slate-400 font-bold">Class & Section</span>
                <p className="font-bold text-slate-800 mt-0.5">Class 9 - Section C</p>
              </div>
              <div>
                <span className="text-[10px] uppercase text-slate-400 font-bold">Venue</span>
                <p className="font-bold text-slate-800 mt-0.5">Room 205 (Science Wing)</p>
              </div>
              <div>
                <span className="text-[10px] uppercase text-slate-400 font-bold">Activity Planned</span>
                <p className="text-slate-700 mt-0.5">Supervised Numerical Practice</p>
              </div>
            </div>
          </Card>
        </TabsContent>
      </Tabs>

      {/* Apply Leave Modal */}
      <Dialog open={isApplyOpen} onOpenChange={setIsApplyOpen}>
        <DialogContent className="max-w-md">
          <DialogHeader>
            <DialogTitle className="text-base font-bold text-slate-900 flex items-center gap-2">
              <Calendar className="h-5 w-5 text-emerald-600" />
              Apply for Leave
            </DialogTitle>
            <DialogDescription className="text-xs text-slate-500">
              Submit leave request with dates. The system will auto-allocate free faculty for your periods.
            </DialogDescription>
          </DialogHeader>

          <form onSubmit={handleApply} className="space-y-3.5">
            <div>
              <label className="text-xs font-bold text-slate-700">Leave Type</label>
              <select
                value={leaveType}
                onChange={(e) => setLeaveType(e.target.value as any)}
                className="w-full mt-1 h-9 rounded-md border border-slate-200 text-xs px-2 bg-white"
              >
                <option value="CASUAL">Casual Leave (8 available)</option>
                <option value="MEDICAL">Medical Leave (6 available)</option>
                <option value="DUTY_LEAVE">Duty Leave (Official training)</option>
              </select>
            </div>

            <div className="grid grid-cols-2 gap-3">
              <div>
                <label className="text-xs font-bold text-slate-700">Start Date</label>
                <Input
                  type="date"
                  value={startDate}
                  onChange={(e) => setStartDate(e.target.value)}
                  className="mt-1 text-xs"
                />
              </div>

              <div>
                <label className="text-xs font-bold text-slate-700">End Date</label>
                <Input
                  type="date"
                  value={endDate}
                  onChange={(e) => setEndDate(e.target.value)}
                  className="mt-1 text-xs"
                />
              </div>
            </div>

            <div>
              <label className="text-xs font-bold text-slate-700">Reason for Leave</label>
              <textarea
                value={leaveReason}
                onChange={(e) => setLeaveReason(e.target.value)}
                placeholder="State reason..."
                rows={3}
                required
                className="w-full mt-1 p-2 rounded-md border border-slate-200 text-xs bg-white"
              />
            </div>

            <div className="p-2.5 rounded-lg bg-slate-50 border border-slate-200 text-[11px] text-slate-600">
              <p className="font-semibold text-slate-800">Auto-Substitution Match:</p>
              <p className="mt-0.5">Colleague Rajesh Kulkarni (Math PGT) is free during Periods 2 & 6 and will be scheduled to cover.</p>
            </div>

            <DialogFooter className="gap-2 sm:gap-0 pt-2">
              <Button type="button" variant="outline" size="sm" onClick={() => setIsApplyOpen(false)}>
                Cancel
              </Button>
              <Button type="submit" size="sm" className="bg-emerald-600 hover:bg-emerald-700 text-white font-semibold">
                Submit Application
              </Button>
            </DialogFooter>
          </form>
        </DialogContent>
      </Dialog>
    </div>
  );
}

