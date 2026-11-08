"use client";

import * as React from "react";
import { Student } from "@/types";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
} from "@/components/ui/dialog";
import { Badge } from "@/components/ui/badge";
import { Tabs, TabsList, TabsTrigger, TabsContent } from "@/components/ui/tabs";
import { Button } from "@/components/ui/button";
import { formatCurrency } from "@/lib/utils";
import {
  User,
  Phone,
  Mail,
  Calendar,
  CreditCard,
  FileText,
  ShieldCheck,
  CheckCircle2,
  FolderLock,
  Archive,
} from "lucide-react";
import { DocumentCustodyModal } from "@/components/admin/document-custody-modal";
import { TransferCertificateModal } from "@/components/admin/modals/transfer-certificate-modal";

interface StudentDrawerProps {
  student: Student | null;
  isOpen: boolean;
  onClose: () => void;
}

export function StudentDrawer({ student, isOpen, onClose }: StudentDrawerProps) {
  const [isCustodyOpen, setIsCustodyOpen] = React.useState(false);
  const [isTcOpen, setIsTcOpen] = React.useState(false);
  if (!student) return null;

  return (
    <Dialog open={isOpen} onOpenChange={(open) => !open && onClose()}>
      <DialogContent className="max-w-2xl max-h-[90vh] overflow-y-auto rounded-2xl p-6">
        <DialogHeader>
          <div className="flex items-center justify-between w-full">
            <div className="flex items-center gap-4">
              <div className="h-14 w-14 rounded-2xl bg-primary/10 text-primary flex items-center justify-center font-bold text-xl shrink-0">
                {student.firstName[0]}
                {student.lastName[0]}
              </div>
              <div>
                <div className="flex items-center gap-2.5">
                  <DialogTitle className="text-xl font-bold text-slate-900 dark:text-slate-50">
                    {student.fullName}
                  </DialogTitle>
                  <Badge
                    variant={student.status === "Active" ? "success" : "secondary"}
                  >
                    <CheckCircle2 className="h-3 w-3 mr-1" />
                    {student.status}
                  </Badge>
                </div>
                <DialogDescription className="text-xs text-slate-500 dark:text-slate-400 mt-1 flex items-center gap-2">
                  <span className="font-semibold text-slate-700 dark:text-slate-300">
                    {student.admissionNumber}
                  </span>
                  <span>•</span>
                  <span>{student.class} ({student.section})</span>
                  <span>•</span>
                  <span>Roll #{student.rollNumber}</span>
                </DialogDescription>
              </div>
            </div>
            <Button
              variant="outline"
              size="sm"
              onClick={() => setIsTcOpen(true)}
              className="h-8 text-xs gap-1.5 border-slate-200 text-slate-700 hover:bg-slate-50 shadow-xs"
            >
              <FileText className="h-3.5 w-3.5 text-blue-600" />
              Issue TC
            </Button>
          </div>
        </DialogHeader>

        <Tabs defaultValue="overview" className="mt-5">
          <TabsList className="grid grid-cols-5 w-full h-10 p-1 bg-slate-100 dark:bg-slate-800 rounded-xl">
            <TabsTrigger value="overview" className="rounded-lg text-xs font-semibold">Overview</TabsTrigger>
            <TabsTrigger value="guardian" className="rounded-lg text-xs font-semibold">Guardian</TabsTrigger>
            <TabsTrigger value="documents" className="rounded-lg text-xs font-semibold">Custody</TabsTrigger>
            <TabsTrigger value="attendance" className="rounded-lg text-xs font-semibold">Attendance</TabsTrigger>
            <TabsTrigger value="finance" className="rounded-lg text-xs font-semibold">Fee Account</TabsTrigger>
          </TabsList>

          {/* Overview Tab */}
          <TabsContent value="overview" className="space-y-4 pt-4">
            <div className="grid grid-cols-2 gap-4 rounded-xl bg-slate-50 dark:bg-slate-800/50 p-5 border border-slate-200/60 dark:border-slate-700/60">
              <div>
                <p className="text-xs text-slate-500 dark:text-slate-400 font-medium">Date of Birth</p>
                <p className="text-sm font-semibold text-slate-900 dark:text-slate-50 mt-1">{student.dateOfBirth}</p>
              </div>
              <div>
                <p className="text-xs text-slate-500 dark:text-slate-400 font-medium">Gender</p>
                <p className="text-sm font-semibold text-slate-900 dark:text-slate-50 mt-1">{student.gender}</p>
              </div>
              <div>
                <p className="text-xs text-slate-500 dark:text-slate-400 font-medium">Enrolled Since</p>
                <p className="text-sm font-semibold text-slate-900 dark:text-slate-50 mt-1">{student.enrolledAt}</p>
              </div>
              <div>
                <p className="text-xs text-slate-500 dark:text-slate-400 font-medium">Class Section</p>
                <p className="text-sm font-semibold text-slate-900 dark:text-slate-50 mt-1">{student.class} ({student.section})</p>
              </div>
            </div>

            <div className="rounded-xl border border-slate-200/80 dark:border-slate-800 p-5 space-y-3">
              <h4 className="text-xs font-semibold uppercase tracking-wider text-slate-500 dark:text-slate-400">
                Academic Progress & Attendance
              </h4>
              <div className="flex items-center justify-between text-sm py-1.5 border-b border-slate-100 dark:border-slate-800">
                <span className="text-slate-600 dark:text-slate-400">Current Term Attendance</span>
                <span className="font-bold text-emerald-600 dark:text-emerald-400">
                  {student.attendancePercentage}% (On Track)
                </span>
              </div>
              <div className="flex items-center justify-between text-sm py-1.5">
                <span className="text-slate-600 dark:text-slate-400">Final Exam Eligibility</span>
                <span className="font-semibold text-slate-900 dark:text-slate-50">Cleared & Registered</span>
              </div>
            </div>
          </TabsContent>

          {/* Guardian Tab */}
          <TabsContent value="guardian" className="space-y-4 pt-4">
            <div className="rounded-xl border border-slate-200/80 dark:border-slate-800 p-5 space-y-4">
              <div className="flex items-center justify-between">
                <span className="text-xs font-semibold uppercase text-slate-500 dark:text-slate-400 tracking-wider">
                  Primary Guardian Information
                </span>
                <Badge variant="info" className="gap-1 text-xs">
                  <ShieldCheck className="h-3.5 w-3.5" /> Verified Contact
                </Badge>
              </div>

              <div className="space-y-3 text-sm pt-1">
                <div className="flex items-center gap-3">
                  <User className="h-4 w-4 text-slate-400" />
                  <span className="font-semibold text-slate-900 dark:text-slate-100">{student.guardian.name}</span>
                  <span className="text-xs text-slate-500">({student.guardian.relationship})</span>
                </div>
                <div className="flex items-center gap-3">
                  <Phone className="h-4 w-4 text-slate-400" />
                  <span className="text-slate-700 dark:text-slate-300 font-medium">{student.guardian.phone}</span>
                </div>
                <div className="flex items-center gap-3">
                  <Mail className="h-4 w-4 text-slate-400" />
                  <span className="text-slate-700 dark:text-slate-300 font-medium">{student.guardian.email}</span>
                </div>
              </div>
            </div>
          </TabsContent>

          {/* Attendance Tab */}
          <TabsContent value="attendance" className="space-y-4 pt-4">
            <div className="grid grid-cols-3 gap-3 text-center">
              <div className="p-4 rounded-xl bg-slate-50 dark:bg-slate-800/50 border border-slate-200/60 dark:border-slate-700/60">
                <p className="text-xs text-slate-500 dark:text-slate-400 font-medium">Overall Rate</p>
                <p className="text-2xl font-bold text-emerald-600 dark:text-emerald-400 mt-1">
                  {student.attendancePercentage}%
                </p>
              </div>
              <div className="p-4 rounded-xl bg-slate-50 dark:bg-slate-800/50 border border-slate-200/60 dark:border-slate-700/60">
                <p className="text-xs text-slate-500 dark:text-slate-400 font-medium">Total Present</p>
                <p className="text-2xl font-bold text-slate-900 dark:text-slate-50 mt-1">168 Days</p>
              </div>
              <div className="p-4 rounded-xl bg-slate-50 dark:bg-slate-800/50 border border-slate-200/60 dark:border-slate-700/60">
                <p className="text-xs text-slate-500 dark:text-slate-400 font-medium">Approved Leaves</p>
                <p className="text-2xl font-bold text-amber-600 dark:text-amber-400 mt-1">6 Days</p>
              </div>
            </div>
          </TabsContent>

          {/* Document Custody Tab */}
          <TabsContent value="documents" className="space-y-4 pt-4">
            <div className="rounded-xl border border-slate-200/80 dark:border-slate-800 p-4 space-y-3 bg-slate-50/50 dark:bg-slate-800/40">
              <div className="flex items-center justify-between">
                <div>
                  <h4 className="text-xs font-bold text-slate-900 dark:text-slate-100 uppercase tracking-wider">
                    Physical Document Custody Register
                  </h4>
                  <p className="text-[11px] text-slate-500">
                    Official archive verification & physical locker index
                  </p>
                </div>
                <Button
                  size="sm"
                  onClick={() => setIsCustodyOpen(true)}
                  className="h-7 text-xs bg-blue-600 hover:bg-blue-700 text-white gap-1.5 shadow-xs"
                >
                  <FolderLock className="h-3.5 w-3.5" />
                  Manage Custody & Locker
                </Button>
              </div>

              <div className="space-y-2 pt-1 font-mono text-xs">
                <div className="flex items-center justify-between p-2 rounded bg-white dark:bg-slate-800 border border-slate-200/70 dark:border-slate-700">
                  <div className="flex items-center gap-2">
                    <span className="h-2 w-2 rounded-full bg-emerald-500" />
                    <span className="font-sans font-medium text-slate-800 dark:text-slate-200">Transfer Certificate (Original)</span>
                  </div>
                  <Badge className="bg-emerald-50 text-emerald-700 border-emerald-200 text-[10px]">
                    Locker B-12 / File #0842
                  </Badge>
                </div>

                <div className="flex items-center justify-between p-2 rounded bg-white dark:bg-slate-800 border border-slate-200/70 dark:border-slate-700">
                  <div className="flex items-center gap-2">
                    <span className="h-2 w-2 rounded-full bg-emerald-500" />
                    <span className="font-sans font-medium text-slate-800 dark:text-slate-200">Previous Marksheet (Original)</span>
                  </div>
                  <Badge className="bg-emerald-50 text-emerald-700 border-emerald-200 text-[10px]">
                    Locker B-12 / File #0842
                  </Badge>
                </div>

                <div className="flex items-center justify-between p-2 rounded bg-white dark:bg-slate-800 border border-slate-200/70 dark:border-slate-700">
                  <div className="flex items-center gap-2">
                    <span className="h-2 w-2 rounded-full bg-blue-500" />
                    <span className="font-sans font-medium text-slate-800 dark:text-slate-200">Birth Certificate (Verified Copy)</span>
                  </div>
                  <Badge className="bg-blue-50 text-blue-700 border-blue-200 text-[10px]">
                    Archive Folder 08
                  </Badge>
                </div>
              </div>
            </div>
          </TabsContent>

          {/* Fee Account Tab */}
          <TabsContent value="finance" className="space-y-4 pt-4">
            <div className="flex items-center justify-between p-5 rounded-xl bg-slate-50 dark:bg-slate-800/50 border border-slate-200/60 dark:border-slate-700/60">
              <div>
                <p className="text-xs text-slate-500 dark:text-slate-400 font-medium">Current Balance Due</p>
                <p className="text-2xl font-bold text-slate-900 dark:text-slate-50 mt-1">
                  {formatCurrency(student.balanceAmount)}
                </p>
              </div>
              <Badge
                variant={
                  student.feeStatus === "Paid"
                    ? "success"
                    : student.feeStatus === "Partial"
                    ? "warning"
                    : "destructive"
                }
                className="text-xs px-3 py-1"
              >
                {student.feeStatus}
              </Badge>
            </div>

            <div className="flex justify-end gap-3 pt-2">
              <Button variant="outline" size="sm" className="gap-2">
                <FileText className="h-4 w-4" /> View Full Ledger
              </Button>
              <Button size="sm" className="gap-2 shadow-sm">
                <CreditCard className="h-4 w-4" /> Record Payment
              </Button>
            </div>
          </TabsContent>
        </Tabs>

        {/* Physical Document Custody Modal */}
        <DocumentCustodyModal
          open={isCustodyOpen}
          onOpenChange={setIsCustodyOpen}
          entityType="STUDENT"
          entityName={student.fullName}
          entityId={student.admissionNumber}
          classNameOrDepartment={`${student.class} (${student.section})`}
        />

        {/* Transfer Certificate (TC) Generator Modal */}
        <TransferCertificateModal
          open={isTcOpen}
          onOpenChange={setIsTcOpen}
          student={student}
        />
      </DialogContent>
    </Dialog>
  );
}
