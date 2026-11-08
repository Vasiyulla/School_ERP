"use client";

import * as React from "react";
import {
  FileText,
  ShieldCheck,
  CheckCircle2,
  Clock,
  Archive,
  RotateCcw,
  Building,
  User,
  Plus,
  Save,
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
import { Input } from "@/components/ui/input";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { cn } from "@/lib/utils";

export type CustodyStatus =
  | "ORIGINAL_IN_CUSTODY"
  | "VERIFIED_COPY"
  | "PENDING"
  | "RETURNED";

export interface CustodyDocument {
  id: string;
  documentName: string;
  isMandatory: boolean;
  status: CustodyStatus;
  physicalLocation: string; // e.g. "Locker B-14 / Box 02"
  collectedDate?: string;
  verifiedBy?: string;
}

interface DocumentCustodyModalProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  entityType: "STUDENT" | "STAFF";
  entityName: string;
  entityId: string;
  classNameOrDepartment?: string;
  initialDocuments?: CustodyDocument[];
  onSave?: (docs: CustodyDocument[]) => void;
}

const defaultStudentDocs: CustodyDocument[] = [
  {
    id: "doc-1",
    documentName: "Transfer Certificate (TC - Original)",
    isMandatory: true,
    status: "ORIGINAL_IN_CUSTODY",
    physicalLocation: "Locker B-12 / File #2026-0842",
    collectedDate: "2026-10-15",
    verifiedBy: "Admin Sunita Sharma",
  },
  {
    id: "doc-2",
    documentName: "Previous Grade Report Card / Marksheet (Original)",
    isMandatory: true,
    status: "ORIGINAL_IN_CUSTODY",
    physicalLocation: "Locker B-12 / File #2026-0842",
    collectedDate: "2026-10-15",
    verifiedBy: "Admin Sunita Sharma",
  },
  {
    id: "doc-3",
    documentName: "Birth Certificate (Original / Notarized)",
    isMandatory: true,
    status: "VERIFIED_COPY",
    physicalLocation: "Archive Folder 08",
    collectedDate: "2026-10-14",
    verifiedBy: "Admin Sunita Sharma",
  },
  {
    id: "doc-4",
    documentName: "Student Medical Fitness & Blood Group Record",
    isMandatory: true,
    status: "ORIGINAL_IN_CUSTODY",
    physicalLocation: "Infirmary Box #A-04",
    collectedDate: "2026-10-18",
    verifiedBy: "Dr. Anjali Rao",
  },
  {
    id: "doc-5",
    documentName: "Caste / Category Certificate (if applicable)",
    isMandatory: false,
    status: "VERIFIED_COPY",
    physicalLocation: "Archive Folder 08",
    collectedDate: "2026-10-14",
    verifiedBy: "Admin Sunita Sharma",
  },
  {
    id: "doc-6",
    documentName: "Aadhaar Card Copy (Student & Guardians)",
    isMandatory: true,
    status: "VERIFIED_COPY",
    physicalLocation: "Digital Vault + Binder 3",
    collectedDate: "2026-10-14",
    verifiedBy: "Admin Sunita Sharma",
  },
  {
    id: "doc-7",
    documentName: "Migration Certificate (CBSE Board)",
    isMandatory: false,
    status: "PENDING",
    physicalLocation: "Awaiting State Board Dispatch",
  },
];

export function DocumentCustodyModal({
  open,
  onOpenChange,
  entityType,
  entityName,
  entityId,
  classNameOrDepartment,
  initialDocuments,
  onSave,
}: DocumentCustodyModalProps) {
  const [documents, setDocuments] = React.useState<CustodyDocument[]>(
    initialDocuments || defaultStudentDocs
  );
  const [isSaved, setIsSaved] = React.useState(false);

  const handleStatusChange = (docId: string, newStatus: CustodyStatus) => {
    setDocuments((prev) =>
      prev.map((doc) =>
        doc.id === docId
          ? {
              ...doc,
              status: newStatus,
              collectedDate:
                newStatus !== "PENDING"
                  ? doc.collectedDate || new Date().toISOString().split("T")[0]
                  : undefined,
              verifiedBy:
                newStatus !== "PENDING"
                  ? doc.verifiedBy || "Super Admin (You)"
                  : undefined,
            }
          : doc
      )
    );
  };

  const handleLocationChange = (docId: string, newLocation: string) => {
    setDocuments((prev) =>
      prev.map((doc) =>
        doc.id === docId ? { ...doc, physicalLocation: newLocation } : doc
      )
    );
  };

  const handleSave = () => {
    setIsSaved(true);
    if (onSave) onSave(documents);
    setTimeout(() => {
      setIsSaved(false);
      onOpenChange(false);
    }, 800);
  };

  const inCustodyCount = documents.filter(
    (d) => d.status === "ORIGINAL_IN_CUSTODY"
  ).length;
  const verifiedCopyCount = documents.filter(
    (d) => d.status === "VERIFIED_COPY"
  ).length;
  const pendingCount = documents.filter((d) => d.status === "PENDING").length;

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-3xl max-h-[85vh] flex flex-col p-0 overflow-hidden">
        <DialogHeader className="p-5 pb-3 border-b border-slate-100 dark:border-slate-800">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-2.5">
              <div className="h-8 w-8 rounded-lg bg-blue-600 text-white flex items-center justify-center">
                <ShieldCheck className="h-4.5 w-4.5" />
              </div>
              <div>
                <DialogTitle className="text-base font-bold text-slate-900 dark:text-white">
                  Physical Document Custody Register
                </DialogTitle>
                <DialogDescription className="text-xs text-slate-500">
                  {entityType === "STUDENT" ? "Student" : "Staff Member"}:{" "}
                  <span className="font-semibold text-slate-800 dark:text-slate-200">
                    {entityName}
                  </span>{" "}
                  • ID: <span className="font-mono">{entityId}</span>
                  {classNameOrDepartment && ` • ${classNameOrDepartment}`}
                </DialogDescription>
              </div>
            </div>
            <div className="flex items-center gap-1.5">
              <Badge className="bg-emerald-50 text-emerald-700 border-emerald-200 text-[10px]">
                {inCustodyCount} Originals In Custody
              </Badge>
              {pendingCount > 0 && (
                <Badge className="bg-amber-50 text-amber-700 border-amber-200 text-[10px]">
                  {pendingCount} Pending
                </Badge>
              )}
            </div>
          </div>
        </DialogHeader>

        {/* Document Checklist Table */}
        <div className="flex-1 overflow-y-auto p-5">
          <div className="rounded-lg border border-slate-200/90 dark:border-slate-800 overflow-hidden">
            <Table>
              <TableHeader>
                <TableRow className="bg-slate-50/70 dark:bg-slate-800/50 text-[11px]">
                  <TableHead className="font-semibold text-slate-700 dark:text-slate-300">
                    Document Title
                  </TableHead>
                  <TableHead className="font-semibold text-slate-700 dark:text-slate-300">
                    Custody Status
                  </TableHead>
                  <TableHead className="font-semibold text-slate-700 dark:text-slate-300">
                    Physical Storage Location (Locker/Cabinet)
                  </TableHead>
                  <TableHead className="font-semibold text-slate-700 dark:text-slate-300 text-right">
                    Verified By & Date
                  </TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {documents.map((doc) => (
                  <TableRow
                    key={doc.id}
                    className="hover:bg-slate-50/50 dark:hover:bg-slate-800/40 text-xs"
                  >
                    <TableCell className="font-medium">
                      <div className="flex items-center gap-2">
                        <FileText className="h-3.5 w-3.5 text-slate-400 shrink-0" />
                        <div>
                          <span className="text-slate-900 dark:text-slate-100">
                            {doc.documentName}
                          </span>
                          {doc.isMandatory && (
                            <span className="ml-1.5 text-[9px] font-bold text-rose-500 uppercase">
                              Required
                            </span>
                          )}
                        </div>
                      </div>
                    </TableCell>

                    <TableCell>
                      <select
                        value={doc.status}
                        onChange={(e) =>
                          handleStatusChange(
                            doc.id,
                            e.target.value as CustodyStatus
                          )
                        }
                        className={cn(
                          "h-7 rounded border text-[11px] font-medium px-2 py-0.5 focus:outline-none focus:ring-1",
                          doc.status === "ORIGINAL_IN_CUSTODY" &&
                            "bg-emerald-50 text-emerald-800 border-emerald-200 dark:bg-emerald-950/40 dark:text-emerald-300",
                          doc.status === "VERIFIED_COPY" &&
                            "bg-blue-50 text-blue-800 border-blue-200 dark:bg-blue-950/40 dark:text-blue-300",
                          doc.status === "PENDING" &&
                            "bg-amber-50 text-amber-800 border-amber-200 dark:bg-amber-950/40 dark:text-amber-300",
                          doc.status === "RETURNED" &&
                            "bg-slate-100 text-slate-700 border-slate-200 dark:bg-slate-800 dark:text-slate-300"
                        )}
                      >
                        <option value="ORIGINAL_IN_CUSTODY">
                          🟢 Original in Custody
                        </option>
                        <option value="VERIFIED_COPY">
                          🔵 Verified Copy Only
                        </option>
                        <option value="PENDING">🟡 Pending Submission</option>
                        <option value="RETURNED">⚪ Returned to Guardian</option>
                      </select>
                    </TableCell>

                    <TableCell>
                      <Input
                        value={doc.physicalLocation}
                        onChange={(e) =>
                          handleLocationChange(doc.id, e.target.value)
                        }
                        placeholder="e.g. Locker B-12 / File #04"
                        className="h-7 text-xs bg-slate-50/50 dark:bg-slate-800/50 border-slate-200 dark:border-slate-700"
                      />
                    </TableCell>

                    <TableCell className="text-right text-[11px] text-slate-500">
                      {doc.verifiedBy ? (
                        <div>
                          <div className="font-medium text-slate-700 dark:text-slate-300 truncate">
                            {doc.verifiedBy}
                          </div>
                          <div className="text-[10px] text-slate-400 font-mono">
                            {doc.collectedDate}
                          </div>
                        </div>
                      ) : (
                        <span className="text-amber-600 font-medium">Pending</span>
                      )}
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </div>
        </div>

        <DialogFooter className="p-4 border-t border-slate-100 dark:border-slate-800 bg-slate-50/50 dark:bg-slate-900/40 flex sm:justify-between items-center">
          <span className="text-[11px] text-slate-500">
            Physical archives audit compliant with CBSE affiliation standards.
          </span>
          <div className="flex gap-2">
            <Button
              variant="outline"
              size="sm"
              onClick={() => onOpenChange(false)}
              className="h-8 text-xs"
            >
              Cancel
            </Button>
            <Button
              size="sm"
              onClick={handleSave}
              className={cn(
                "h-8 gap-1.5 text-xs text-white",
                isSaved ? "bg-emerald-600" : "bg-blue-600 hover:bg-blue-700"
              )}
            >
              {isSaved ? (
                <>
                  <Check className="h-3.5 w-3.5" />
                  Custody Saved!
                </>
              ) : (
                <>
                  <Save className="h-3.5 w-3.5" />
                  Save Custody Records
                </>
              )}
            </Button>
          </div>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}

