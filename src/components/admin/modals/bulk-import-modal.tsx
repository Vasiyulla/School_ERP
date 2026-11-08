"use client";

import * as React from "react";
import {
  Upload,
  FileSpreadsheet,
  CheckCircle2,
  AlertTriangle,
  Download,
  X,
  ArrowRight,
  RefreshCw,
  FileText,
  Check,
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
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { cn } from "@/lib/utils";

interface BulkImportModalProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  entityType?: "STUDENT" | "STAFF";
  onSuccess?: (importedCount: number) => void;
}

interface ParsedRow {
  rowNum: number;
  admissionNo: string;
  name: string;
  gradeSection: string;
  guardianName: string;
  phone: string;
  status: "VALID" | "DUPLICATE" | "INVALID";
  notes?: string;
}

const mockParsedData: ParsedRow[] = [
  { rowNum: 1, admissionNo: "ADM-2026-1041", name: "Vihaan Agarwal", gradeSection: "Grade 10-A", guardianName: "Manish Agarwal", phone: "+91 98200 44102", status: "VALID" },
  { rowNum: 2, admissionNo: "ADM-2026-1042", name: "Sanya Malhotra", gradeSection: "Grade 10-A", guardianName: "Pooja Malhotra", phone: "+91 98199 88201", status: "VALID" },
  { rowNum: 3, admissionNo: "ADM-2024-0012", name: "Aarav Sharma", gradeSection: "Grade 10-A", guardianName: "Rajesh Sharma", phone: "+91 98201 11200", status: "DUPLICATE", notes: "Admission # already registered in DB" },
  { rowNum: 4, admissionNo: "ADM-2026-1043", name: "Aditya Nair", gradeSection: "Grade 11-B", guardianName: "Gopal Nair", phone: "+91 98205 33210", status: "VALID" },
  { rowNum: 5, admissionNo: "ADM-2026-1044", name: "Zara Khan", gradeSection: "Grade 12-A", guardianName: "Imran Khan", phone: "98200", status: "INVALID", notes: "Invalid phone number length" },
];

export function BulkImportModal({
  open,
  onOpenChange,
  entityType = "STUDENT",
  onSuccess,
}: BulkImportModalProps) {
  const [step, setStep] = React.useState<"UPLOAD" | "PREVIEW" | "IMPORTING" | "COMPLETED">("UPLOAD");
  const [fileName, setFileName] = React.useState<string | null>(null);
  const [isProcessing, setIsProcessing] = React.useState(false);

  const validCount = mockParsedData.filter((r) => r.status === "VALID").length;
  const issueCount = mockParsedData.filter((r) => r.status !== "VALID").length;

  const handleSimulateFileSelect = () => {
    setIsProcessing(true);
    setTimeout(() => {
      setFileName("students_enrollment_batch_2026_q2.csv");
      setIsProcessing(false);
      setStep("PREVIEW");
    }, 700);
  };

  const handleExecuteImport = () => {
    setStep("IMPORTING");
    setTimeout(() => {
      setStep("COMPLETED");
      if (onSuccess) onSuccess(validCount);
    }, 1200);
  };

  const handleClose = () => {
    setStep("UPLOAD");
    setFileName(null);
    onOpenChange(false);
  };

  return (
    <Dialog open={open} onOpenChange={handleClose}>
      <DialogContent className="max-w-3xl max-h-[85vh] flex flex-col p-0 overflow-hidden">
        <DialogHeader className="p-5 pb-3 border-b border-slate-100 dark:border-slate-800">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-2.5">
              <div className="h-8 w-8 rounded-lg bg-blue-600 text-white flex items-center justify-center">
                <FileSpreadsheet className="h-4.5 w-4.5" />
              </div>
              <div>
                <DialogTitle className="text-base font-bold text-slate-900 dark:text-white">
                  Bulk CSV / Excel Ingestion Wizard
                </DialogTitle>
                <DialogDescription className="text-xs text-slate-500">
                  Batch enrollment with pre-flight duplicate checking and validation preview
                </DialogDescription>
              </div>
            </div>
            <div className="flex items-center gap-2 text-xs">
              <span className={cn("px-2 py-0.5 rounded font-bold text-[10px]", step === "UPLOAD" ? "bg-blue-600 text-white" : "bg-slate-100 text-slate-600")}>
                1. Upload
              </span>
              <ArrowRight className="h-3 w-3 text-slate-400" />
              <span className={cn("px-2 py-0.5 rounded font-bold text-[10px]", step === "PREVIEW" ? "bg-blue-600 text-white" : "bg-slate-100 text-slate-600")}>
                2. Validate
              </span>
              <ArrowRight className="h-3 w-3 text-slate-400" />
              <span className={cn("px-2 py-0.5 rounded font-bold text-[10px]", step === "COMPLETED" ? "bg-emerald-600 text-white" : "bg-slate-100 text-slate-600")}>
                3. Commit
              </span>
            </div>
          </div>
        </DialogHeader>

        {/* STEP 1: Upload Dropzone */}
        {step === "UPLOAD" && (
          <div className="p-6 space-y-5">
            <div
              onClick={handleSimulateFileSelect}
              className="border-2 border-dashed border-slate-200 dark:border-slate-700 hover:border-blue-500 rounded-xl p-8 flex flex-col items-center justify-center text-center cursor-pointer transition-all bg-slate-50/50 hover:bg-blue-50/20"
            >
              <div className="h-12 w-12 rounded-full bg-blue-50 dark:bg-slate-800 flex items-center justify-center text-blue-600 mb-3">
                <Upload className="h-6 w-6" />
              </div>
              <p className="text-sm font-bold text-slate-900 dark:text-slate-100">
                Click to browse or drag and drop your spreadsheet
              </p>
              <p className="text-xs text-slate-400 mt-1">
                Supports CSV, XLSX, or XLS files (up to 10,000 records per batch)
              </p>
              <Button
                variant="outline"
                size="sm"
                className="mt-4 text-xs gap-1.5 pointer-events-none"
              >
                {isProcessing ? "Validating Spreadsheet..." : "Select File From Computer"}
              </Button>
            </div>

            <div className="flex items-center justify-between p-3 rounded-lg bg-blue-50/60 dark:bg-slate-800 border border-blue-100 dark:border-slate-700">
              <div className="flex items-center gap-2">
                <FileText className="h-4 w-4 text-blue-600" />
                <span className="text-xs text-slate-700 dark:text-slate-300 font-medium">
                  Need the required column format? Download the official CSV template.
                </span>
              </div>
              <Button variant="ghost" size="sm" className="h-7 text-xs text-blue-600 gap-1 hover:bg-blue-100/60">
                <Download className="h-3 w-3" />
                Template.csv
              </Button>
            </div>
          </div>
        )}

        {/* STEP 2: Pre-Flight Validation Preview */}
        {step === "PREVIEW" && (
          <div className="flex-1 overflow-y-auto p-5 space-y-4">
            <div className="flex items-center justify-between p-3 rounded-lg bg-slate-50 dark:bg-slate-800 border border-slate-200/80">
              <div className="flex items-center gap-2">
                <FileSpreadsheet className="h-4 w-4 text-blue-600" />
                <span className="text-xs font-bold text-slate-800 dark:text-slate-200 font-mono">
                  {fileName}
                </span>
              </div>
              <div className="flex items-center gap-2">
                <Badge className="bg-emerald-50 text-emerald-700 border-emerald-200 text-[10px]">
                  {validCount} Rows Ready
                </Badge>
                {issueCount > 0 && (
                  <Badge className="bg-rose-50 text-rose-700 border-rose-200 text-[10px]">
                    {issueCount} Issues Found
                  </Badge>
                )}
              </div>
            </div>

            <div className="rounded-lg border border-slate-200 dark:border-slate-800 overflow-hidden">
              <Table>
                <TableHeader>
                  <TableRow className="bg-slate-50/60 dark:bg-slate-800/60 text-[11px]">
                    <TableHead className="w-12">#</TableHead>
                    <TableHead>Admission #</TableHead>
                    <TableHead>Student Name</TableHead>
                    <TableHead>Class</TableHead>
                    <TableHead>Guardian & Phone</TableHead>
                    <TableHead>Validation Status</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {mockParsedData.map((row) => (
                    <TableRow key={row.rowNum} className="text-xs hover:bg-slate-50/40">
                      <TableCell className="font-mono text-slate-400 text-[11px]">{row.rowNum}</TableCell>
                      <TableCell className="font-mono font-medium text-slate-800">{row.admissionNo}</TableCell>
                      <TableCell className="font-medium text-slate-900">{row.name}</TableCell>
                      <TableCell>{row.gradeSection}</TableCell>
                      <TableCell>
                        <div className="text-slate-800">{row.guardianName}</div>
                        <div className="text-[10px] text-slate-400 font-mono">{row.phone}</div>
                      </TableCell>
                      <TableCell>
                        {row.status === "VALID" ? (
                          <Badge className="bg-emerald-50 text-emerald-700 border-emerald-200 text-[10px] px-1.5 py-0 font-medium">
                            <Check className="h-2.5 w-2.5 mr-1" /> Ready
                          </Badge>
                        ) : (
                          <div className="space-y-0.5">
                            <Badge className="bg-rose-50 text-rose-700 border-rose-200 text-[10px] px-1.5 py-0 font-medium">
                              {row.status}
                            </Badge>
                            <p className="text-[10px] text-rose-600 font-medium">{row.notes}</p>
                          </div>
                        )}
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </div>
          </div>
        )}

        {/* STEP 3 & 4: Importing / Completed */}
        {(step === "IMPORTING" || step === "COMPLETED") && (
          <div className="p-8 flex flex-col items-center justify-center text-center space-y-4">
            {step === "IMPORTING" ? (
              <>
                <div className="h-12 w-12 rounded-full bg-blue-50 flex items-center justify-center text-blue-600 animate-spin">
                  <RefreshCw className="h-6 w-6" />
                </div>
                <div>
                  <h3 className="text-base font-bold text-slate-900">Ingesting Student Batch...</h3>
                  <p className="text-xs text-slate-400 mt-1">
                    Inserting records into PostgreSQL `students` & `admissions` schemas with transactional integrity
                  </p>
                </div>
              </>
            ) : (
              <>
                <div className="h-12 w-12 rounded-full bg-emerald-50 flex items-center justify-center text-emerald-600">
                  <CheckCircle2 className="h-6 w-6" />
                </div>
                <div>
                  <h3 className="text-base font-bold text-slate-900">Batch Ingestion Complete!</h3>
                  <p className="text-xs text-slate-500 mt-1">
                    Successfully enrolled <span className="font-bold text-emerald-600">{validCount} students</span> into active classes.
                  </p>
                  <p className="text-[11px] text-slate-400 mt-0.5">
                    1 duplicate was skipped and 1 invalid row was flagged in the ingestion audit log.
                  </p>
                </div>
              </>
            )}
          </div>
        )}

        <DialogFooter className="p-4 border-t border-slate-100 dark:border-slate-800 bg-slate-50/50 dark:bg-slate-900/40 flex sm:justify-between items-center">
          <span className="text-[11px] text-slate-400">
            {step === "PREVIEW" && `${validCount} valid records will be committed; ${issueCount} flagged rows will be skipped.`}
          </span>
          <div className="flex gap-2">
            <Button
              variant="outline"
              size="sm"
              onClick={handleClose}
              className="h-8 text-xs"
            >
              {step === "COMPLETED" ? "Close" : "Cancel"}
            </Button>
            {step === "PREVIEW" && (
              <Button
                size="sm"
                onClick={handleExecuteImport}
                className="h-8 text-xs bg-blue-600 hover:bg-blue-700 text-white gap-1.5"
              >
                Commit {validCount} Students <ArrowRight className="h-3 w-3" />
              </Button>
            )}
          </div>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}

