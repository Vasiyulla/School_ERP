"use client";

import * as React from "react";
import {
  Search,
  PlusCircle,
  Download,
  Mail,
  Phone,
  CheckCircle2,
  FolderLock,
  ShieldCheck,
} from "lucide-react";
import { mockStaff } from "@/lib/mock/admin-mock";
import { Card } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Input } from "@/components/ui/input";
import {
  Table,
  TableHeader,
  TableBody,
  TableRow,
  TableHead,
  TableCell,
} from "@/components/ui/table";
import { DocumentCustodyModal, CustodyDocument } from "@/components/admin/document-custody-modal";
import { AddStaffModal } from "@/components/admin/modals/add-staff-modal";

const defaultStaffDocs: CustodyDocument[] = [
  {
    id: "sdoc-1",
    documentName: "Postgraduate / Master's Degree Certificate (Original)",
    isMandatory: true,
    status: "ORIGINAL_IN_CUSTODY",
    physicalLocation: "HR Cabinet 01 / Shelf B",
    collectedDate: "2024-06-10",
    verifiedBy: "Principal Sharma",
  },
  {
    id: "sdoc-2",
    documentName: "B.Ed / State Teaching Certification (Verified Copy)",
    isMandatory: true,
    status: "ORIGINAL_IN_CUSTODY",
    physicalLocation: "HR Cabinet 01 / Shelf B",
    collectedDate: "2024-06-10",
    verifiedBy: "Principal Sharma",
  },
  {
    id: "sdoc-3",
    documentName: "Police Background Verification & Character Certificate",
    isMandatory: true,
    status: "ORIGINAL_IN_CUSTODY",
    physicalLocation: "HR Security Binder",
    collectedDate: "2024-06-15",
    verifiedBy: "HR Director",
  },
  {
    id: "sdoc-4",
    documentName: "Previous Employment Relieving & Experience Letter",
    isMandatory: true,
    status: "VERIFIED_COPY",
    physicalLocation: "HR Cabinet 01 / Shelf B",
    collectedDate: "2024-06-10",
    verifiedBy: "HR Director",
  },
  {
    id: "sdoc-5",
    documentName: "PAN & Aadhaar Identity Proofs",
    isMandatory: true,
    status: "VERIFIED_COPY",
    physicalLocation: "Digital HR Vault + Binder 2",
    collectedDate: "2024-06-10",
    verifiedBy: "HR Director",
  },
];

export default function AdminStaffPage() {
  const [search, setSearch] = React.useState("");
  const [selectedStaff, setSelectedStaff] = React.useState<typeof mockStaff[0] | null>(null);
  const [isAddStaffOpen, setIsAddStaffOpen] = React.useState(false);

  const filtered = mockStaff.filter((emp) =>
    emp.fullName.toLowerCase().includes(search.toLowerCase()) ||
    emp.employeeCode.toLowerCase().includes(search.toLowerCase()) ||
    emp.department.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div className="space-y-6 max-w-7xl mx-auto">
      {/* Title */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold tracking-tight text-slate-900 dark:text-slate-50">
            Staff & Faculty Directory
          </h1>
          <p className="text-sm font-normal text-slate-500 dark:text-slate-400 mt-1">
            Manage 420 instructional and administrative personnel across all academic departments
          </p>
        </div>
        <div className="flex items-center gap-3">
          <Button variant="outline" size="sm" className="gap-2">
            <Download className="h-4 w-4" /> Export Roster
          </Button>
          <Button
            size="sm"
            onClick={() => setIsAddStaffOpen(true)}
            className="gap-2 shadow-sm shadow-primary/20 bg-blue-600 hover:bg-blue-700 text-white"
          >
            <PlusCircle className="h-4 w-4" /> Onboard Employee
          </Button>
        </div>
      </div>

      {/* Search Input */}
      <div className="relative max-w-md">
        <Search className="absolute left-3.5 top-1/2 -translate-y-1/2 h-4 w-4 text-slate-400" />
        <Input
          placeholder="Search by teacher name, code, or department..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          className="pl-10 h-10 text-sm"
        />
      </div>

      {/* Staff Table */}
      <Card className="overflow-hidden shadow-xs">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead className="w-[140px]">Employee Code</TableHead>
              <TableHead>Full Name</TableHead>
              <TableHead>Designation & Department</TableHead>
              <TableHead>Contact</TableHead>
              <TableHead>Assigned Classes</TableHead>
              <TableHead className="text-center">Status</TableHead>
              <TableHead className="text-right w-24">Actions</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {filtered.map((emp) => (
              <TableRow key={emp.id} className="hover:bg-slate-50/80 dark:hover:bg-slate-800/40 transition-colors">
                <TableCell className="font-semibold text-primary text-xs">
                  {emp.employeeCode}
                </TableCell>
                <TableCell>
                  <p className="font-semibold text-slate-900 dark:text-slate-100 text-sm leading-tight">
                    {emp.fullName}
                  </p>
                  <p className="text-xs text-slate-500 dark:text-slate-400 mt-0.5">
                    Joined {emp.joinedDate}
                  </p>
                </TableCell>
                <TableCell>
                  <p className="text-sm font-medium text-slate-800 dark:text-slate-200 leading-tight">
                    {emp.designation}
                  </p>
                  <p className="text-xs text-slate-500 dark:text-slate-400 mt-0.5">
                    {emp.department}
                  </p>
                </TableCell>
                <TableCell>
                  <div className="flex flex-col text-xs text-slate-600 dark:text-slate-400 gap-0.5">
                    <span className="flex items-center gap-1.5 font-medium">
                      <Phone className="h-3 w-3 text-slate-400" /> {emp.phone}
                    </span>
                    <span className="flex items-center gap-1.5">
                      <Mail className="h-3 w-3 text-slate-400" /> {emp.email}
                    </span>
                  </div>
                </TableCell>
                <TableCell>
                  <div className="flex flex-wrap gap-1.5">
                    {emp.assignedClasses.map((cls) => (
                      <span
                        key={cls}
                        className="rounded-md bg-slate-100 dark:bg-slate-800 px-2 py-0.5 text-xs font-medium text-slate-600 dark:text-slate-300"
                      >
                        {cls}
                      </span>
                    ))}
                  </div>
                </TableCell>
                <TableCell className="text-center">
                  <Badge variant="success">
                    <CheckCircle2 className="h-3 w-3 mr-1" />
                    {emp.status}
                  </Badge>
                </TableCell>
                <TableCell className="text-right">
                  <div className="flex items-center justify-end gap-1.5">
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={() => setSelectedStaff(emp)}
                      className="h-7 text-[11px] gap-1 text-slate-700 hover:bg-slate-50 border-slate-200"
                    >
                      <FolderLock className="h-3 w-3 text-blue-600" />
                      Custody
                    </Button>
                    <Button variant="ghost" size="sm" className="h-7 text-[11px] text-slate-600">
                      Profile
                    </Button>
                  </div>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </Card>

      {/* Staff Credentials Custody Modal */}
      {selectedStaff && (
        <DocumentCustodyModal
          open={!!selectedStaff}
          onOpenChange={(open) => !open && setSelectedStaff(null)}
          entityType="STAFF"
          entityName={selectedStaff.fullName}
          entityId={selectedStaff.employeeCode}
          classNameOrDepartment={`${selectedStaff.designation} • ${selectedStaff.department}`}
          initialDocuments={defaultStaffDocs}
        />
      )}
      {/* Onboard Staff Modal */}
      <AddStaffModal
        open={isAddStaffOpen}
        onOpenChange={setIsAddStaffOpen}
      />
    </div>
  );
}
