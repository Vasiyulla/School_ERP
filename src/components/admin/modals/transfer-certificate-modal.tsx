"use client";

import * as React from "react";
import {
  FileText,
  Printer,
  AlertTriangle,
  CheckCircle2,
  ShieldCheck,
  Building,
  GraduationCap,
} from "lucide-react";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Student } from "@/types";
import { formatCurrency } from "@/lib/utils";

interface TransferCertificateModalProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  student: Student;
  onSuccess?: () => void;
}

export function TransferCertificateModal({
  open,
  onOpenChange,
  student,
  onSuccess,
}: TransferCertificateModalProps) {
  const [step, setStep] = React.useState<"VERIFY" | "PREVIEW">("VERIFY");
  const [reason, setReason] = React.useState("Parent Job Relocation to New City");
  const [conduct, setConduct] = React.useState("Exemplary");
  const [dischargeDate, setDischargeDate] = React.useState("2026-11-08");
  const [feeWaivedOverride, setFeeWaivedOverride] = React.useState(false);
  const [documentsHandedOver, setDocumentsHandedOver] = React.useState(false);

  const hasOutstandingFees = student.balanceAmount > 0;
  const canGenerate = (!hasOutstandingFees || feeWaivedOverride) && documentsHandedOver;

  const tcNumber = `TC-2026-${student.admissionNumber.replace("ADM-", "")}`;

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-2xl max-h-[90vh] overflow-y-auto">
        <DialogHeader className="border-b pb-3">
          <div className="flex items-center gap-2.5">
            <div className="h-8 w-8 rounded-lg bg-blue-600 text-white flex items-center justify-center">
              <FileText className="h-4.5 w-4.5" />
            </div>
            <div>
              <DialogTitle className="text-base font-bold text-slate-900">
                Official Transfer Certificate (TC) Engine
              </DialogTitle>
              <DialogDescription className="text-xs text-slate-500">
                CBSE Board Affiliation #113042 • Statutory school discharge certificate
              </DialogDescription>
            </div>
          </div>
        </DialogHeader>

        {step === "VERIFY" ? (
          <div className="space-y-4 py-2 text-xs">
            {/* Student Dossier Summary */}
            <div className="p-3.5 rounded-xl bg-slate-50 border border-slate-200/80 space-y-2">
              <div className="flex justify-between items-center">
                <span className="font-bold text-sm text-slate-900">{student.fullName}</span>
                <Badge variant="outline" className="font-mono text-xs">
                  {student.admissionNumber}
                </Badge>
              </div>
              <div className="grid grid-cols-3 gap-2 text-slate-600 text-[11px]">
                <div>Class: <span className="font-semibold text-slate-800">{student.class} ({student.section})</span></div>
                <div>Roll #: <span className="font-semibold text-slate-800">{student.rollNumber}</span></div>
                <div>DOB: <span className="font-semibold text-slate-800">{student.dateOfBirth}</span></div>
              </div>
            </div>

            {/* Check 1: Fee Clearance Verification */}
            <div className="p-3.5 rounded-xl border border-slate-200 space-y-2">
              <div className="flex items-center justify-between">
                <span className="font-bold text-slate-800 flex items-center gap-1.5">
                  <ShieldCheck className="h-4 w-4 text-blue-600" />
                  1. Financial Dues & Ledger Clearance
                </span>
                {hasOutstandingFees ? (
                  <Badge className="bg-rose-50 text-rose-700 border-rose-200 text-[10px]">
                    Pending Dues: {formatCurrency(student.balanceAmount)}
                  </Badge>
                ) : (
                  <Badge className="bg-emerald-50 text-emerald-700 border-emerald-200 text-[10px]">
                    Dues Cleared (₹0)
                  </Badge>
                )}
              </div>

              {hasOutstandingFees && (
                <div className="p-2.5 rounded bg-amber-50/70 border border-amber-200 text-[11px] text-amber-800 space-y-1.5">
                  <p className="flex items-center gap-1.5 font-medium">
                    <AlertTriangle className="h-3.5 w-3.5 text-amber-600 shrink-0" />
                    Student has outstanding fee balance of {formatCurrency(student.balanceAmount)}.
                  </p>
                  <label className="flex items-center gap-2 cursor-pointer font-semibold text-slate-800 pt-1">
                    <input
                      type="checkbox"
                      checked={feeWaivedOverride}
                      onChange={(e) => setFeeWaivedOverride(e.target.checked)}
                      className="rounded border-amber-400"
                    />
                    <span>Principal Administrative Waiver (Allow TC Generation)</span>
                  </label>
                </div>
              )}
            </div>

            {/* Check 2: Physical Document Custody Handover */}
            <div className="p-3.5 rounded-xl border border-slate-200 space-y-2">
              <span className="font-bold text-slate-800 flex items-center gap-1.5">
                <ShieldCheck className="h-4 w-4 text-blue-600" />
                2. Physical Documents Return Verification
              </span>
              <p className="text-[11px] text-slate-500">
                Verify original documents currently in Locker B-12 / File #{student.admissionNumber} are returned to parent.
              </p>
              <label className="flex items-center gap-2 cursor-pointer text-slate-800 font-medium">
                <input
                  type="checkbox"
                  checked={documentsHandedOver}
                  onChange={(e) => setDocumentsHandedOver(e.target.checked)}
                  className="rounded border-slate-300"
                />
                <span>Original Transfer Certificate & Previous Marksheets handed back to parent</span>
              </label>
            </div>

            {/* Form Fields: Discharge Reason & Conduct */}
            <div className="grid grid-cols-2 gap-3 pt-1">
              <div>
                <label className="text-slate-600 font-medium">Reason for Leaving School</label>
                <select
                  value={reason}
                  onChange={(e) => setReason(e.target.value)}
                  className="h-8 w-full rounded-md border border-slate-200 bg-white px-2 mt-1 text-xs"
                >
                  <option value="Parent Job Relocation to New City">Parent Job Relocation to New City</option>
                  <option value="Completed Senior Secondary (Grade 12)">Completed Senior Secondary (Grade 12)</option>
                  <option value="Admission to Specialized Junior College">Admission to Specialized Junior College</option>
                  <option value="Personal / Family Reasons">Personal / Family Reasons</option>
                </select>
              </div>

              <div>
                <label className="text-slate-600 font-medium">General Conduct & Character</label>
                <select
                  value={conduct}
                  onChange={(e) => setConduct(e.target.value)}
                  className="h-8 w-full rounded-md border border-slate-200 bg-white px-2 mt-1 text-xs"
                >
                  <option value="Exemplary">Exemplary</option>
                  <option value="Very Good">Very Good</option>
                  <option value="Good">Good</option>
                  <option value="Satisfactory">Satisfactory</option>
                </select>
              </div>
            </div>

            <DialogFooter className="border-t pt-3">
              <Button variant="outline" size="sm" onClick={() => onOpenChange(false)} className="h-8 text-xs">
                Cancel
              </Button>
              <Button
                size="sm"
                disabled={!canGenerate}
                onClick={() => setStep("PREVIEW")}
                className="h-8 text-xs bg-blue-600 hover:bg-blue-700 text-white"
              >
                Generate Official TC Preview
              </Button>
            </DialogFooter>
          </div>
        ) : (
          /* STEP 2: Official Printable TC Preview */
          <div className="space-y-4 py-2">
            <div className="p-6 rounded-xl border-2 border-slate-800 bg-amber-50/20 text-slate-900 font-serif space-y-4 text-xs shadow-inner">
              {/* Institutional Header */}
              <div className="text-center space-y-1 border-b-2 border-slate-800 pb-3">
                <h2 className="text-base font-bold uppercase tracking-wider text-slate-900">
                  Apex Academy Senior Secondary School
                </h2>
                <p className="text-[10px] font-sans text-slate-600 uppercase tracking-widest">
                  Affiliated to CBSE, New Delhi • Affiliation No. 113042 • School Code: 30198
                </p>
                <p className="text-[10px] font-sans text-slate-500">
                  Campus Road, Powai, Mumbai – 400076 • Web: www.apexacademy.edu.in
                </p>
                <div className="pt-2">
                  <span className="inline-block px-3 py-1 bg-slate-900 text-white font-sans text-[11px] font-bold uppercase tracking-wider rounded">
                    TRANSFER CERTIFICATE
                  </span>
                </div>
              </div>

              {/* TC Metadata */}
              <div className="flex justify-between font-sans text-[11px] font-semibold border-b pb-2">
                <span>TC Serial No: <span className="font-mono text-blue-700">{tcNumber}</span></span>
                <span>Admission No: <span className="font-mono">{student.admissionNumber}</span></span>
                <span>Date: {dischargeDate}</span>
              </div>

              {/* Statutory TC Clauses */}
              <div className="space-y-2 text-[11px] leading-relaxed">
                <p>1. Name of the Pupil: <span className="font-bold underline">{student.fullName}</span></p>
                <p>2. Mother&apos;s Name: <span className="font-semibold underline">Sunita {student.lastName}</span></p>
                <p>3. Father&apos;s / Guardian&apos;s Name: <span className="font-semibold underline">{student.guardian.name}</span></p>
                <p>4. Nationality: <span className="font-semibold">Indian</span></p>
                <p>5. Date of Birth (in figures): <span className="font-mono font-bold">{student.dateOfBirth}</span></p>
                <p>6. Class in which the pupil last studied: <span className="font-bold">{student.class}</span></p>
                <p>7. School / Board Annual Examination last taken: <span className="font-semibold">CBSE Secondary Examination</span></p>
                <p>8. Whether qualified for promotion to higher class: <span className="font-bold text-emerald-700">YES, QUALIFIED</span></p>
                <p>9. Month up to which the school dues have been paid: <span className="font-semibold">All Dues Paid in Full</span></p>
                <p>10. Total working days in academic session: <span className="font-semibold">220 Days</span></p>
                <p>11. Total working days pupil attended: <span className="font-semibold">198 Days ({student.attendancePercentage}%)</span></p>
                <p>12. General Conduct: <span className="font-bold text-emerald-800">{conduct}</span></p>
                <p>13. Reason for leaving the school: <span className="font-semibold italic">{reason}</span></p>
              </div>

              {/* Signatures */}
              <div className="grid grid-cols-3 text-center font-sans text-[10px] font-bold pt-8 border-t border-slate-300">
                <div className="border-t border-slate-400 pt-1">Class Teacher</div>
                <div className="border-t border-slate-400 pt-1">Accountant</div>
                <div className="border-t border-slate-400 pt-1">Principal (with Seal)</div>
              </div>
            </div>

            <DialogFooter className="border-t pt-3 sm:justify-between">
              <Button variant="outline" size="sm" onClick={() => setStep("VERIFY")} className="h-8 text-xs">
                Back to Verification
              </Button>
              <Button
                size="sm"
                onClick={() => {
                  if (onSuccess) onSuccess();
                  onOpenChange(false);
                }}
                className="h-8 text-xs bg-blue-600 hover:bg-blue-700 text-white gap-1.5"
              >
                <Printer className="h-3.5 w-3.5" /> Print & Archive Official TC
              </Button>
            </DialogFooter>
          </div>
        )}
      </DialogContent>
    </Dialog>
  );
}

