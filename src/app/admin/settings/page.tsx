"use client";

import * as React from "react";
import {
  Cloud,
  Database,
  Shield,
  CheckCircle2,
  Lock,
  History,
  Server,
  Layers,
  Search,
  Filter,
  Save,
  RefreshCw,
  Download,
  AlertTriangle,
  KeyRound,
  Sliders,
  Check,
  X,
  ExternalLink,
} from "lucide-react";
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Tabs, TabsList, TabsTrigger, TabsContent } from "@/components/ui/tabs";
import {
  Table,
  TableHeader,
  TableBody,
  TableRow,
  TableHead,
  TableCell,
} from "@/components/ui/table";
import { cn } from "@/lib/utils";

// 12 Logical Schemas matching PostgreSQL architecture
const schemaList = [
  { name: "identity", desc: "Authentication, users, Argon2id & action RBAC", tableCount: 6, records: "24,850", status: "Healthy" },
  { name: "institution", desc: "Campuses, academic years, calendars & terms", tableCount: 4, records: "18", status: "Healthy" },
  { name: "admissions", desc: "Pipeline, document uploads, application reviews", tableCount: 5, records: "234", status: "Healthy" },
  { name: "students", desc: "Student master, guardian relations & emergency contacts", tableCount: 8, records: "18,450", status: "Healthy" },
  { name: "academics", desc: "Curricula, grade levels, classes, sections & subjects", tableCount: 9, records: "1,240", status: "Healthy" },
  { name: "staff", desc: "Faculty profiles, departments & teacher assignments", tableCount: 6, records: "420", status: "Healthy" },
  { name: "attendance", desc: "Batch attendance sessions & audit correction log", tableCount: 4, records: "1.2M", status: "Healthy" },
  { name: "examinations", desc: "Assessments, theory/practical components & marks", tableCount: 7, records: "450k", status: "Healthy" },
  { name: "finance", desc: "Fee structures, invoices, double-entry ledger & receipts", tableCount: 11, records: "86,300", status: "Healthy" },
  { name: "communication", desc: "WhatsApp, email, SMS & web notifications", tableCount: 4, records: "145k", status: "Healthy" },
  { name: "operations", desc: "Transport vehicle routes, live GPS & library catalog", tableCount: 8, records: "34,000", status: "Healthy" },
  { name: "system", desc: "Audit trail, outbox events & background job records", tableCount: 5, records: "890k", status: "Healthy" },
];

// RBAC Roles Definition
const rbacRoles = [
  { id: "super_admin", label: "Super Admin", color: "bg-purple-100 text-purple-800 border-purple-200" },
  { id: "principal", label: "Principal", color: "bg-blue-100 text-blue-800 border-blue-200" },
  { id: "accountant", label: "Accountant", color: "bg-emerald-100 text-emerald-800 border-emerald-200" },
  { id: "teacher", label: "Faculty / Teacher", color: "bg-amber-100 text-amber-800 border-amber-200" },
  { id: "admissions", label: "Admissions Officer", color: "bg-sky-100 text-sky-800 border-sky-200" },
  { id: "transport", label: "Fleet / Operations", color: "bg-orange-100 text-orange-800 border-orange-200" },
];

// RBAC Permissions Grid grouped by domain
interface PermissionItem {
  id: string;
  label: string;
  module: string;
  description: string;
  defaultRoles: string[];
}

const permissionsList: PermissionItem[] = [
  // Students & Admissions
  { id: "student.view", label: "View Student Records", module: "Students", description: "Read student profiles and academic rosters", defaultRoles: ["super_admin", "principal", "accountant", "teacher", "admissions"] },
  { id: "student.enroll", label: "Enroll New Student", module: "Students", description: "Create student record and assign admission number", defaultRoles: ["super_admin", "principal", "admissions"] },
  { id: "student.edit", label: "Edit Student Profile", module: "Students", description: "Modify student demographics and parent details", defaultRoles: ["super_admin", "principal", "admissions"] },
  { id: "student.delete", label: "Archive / Purge Student", module: "Students", description: "Hard-delete or permanently archive student data", defaultRoles: ["super_admin"] },
  { id: "tc.issue", label: "Generate & Issue TC", module: "Students", description: "Issue CBSE statutory Transfer Certificate", defaultRoles: ["super_admin", "principal"] },
  { id: "admissions.pipeline", label: "Manage Admissions Pipeline", module: "Admissions", description: "Accept, reject, and advance applicant stages", defaultRoles: ["super_admin", "principal", "admissions"] },
  
  // Academics & Exams
  { id: "curriculum.manage", label: "Curriculum & Timetables", module: "Academics", description: "Create class schedules and period allocations", defaultRoles: ["super_admin", "principal"] },
  { id: "attendance.mark", label: "Submit Daily Attendance", module: "Attendance", description: "Record daily/period classroom attendance", defaultRoles: ["super_admin", "teacher"] },
  { id: "attendance.override", label: "Attendance Lock Override", module: "Attendance", description: "Modify locked attendance post-09:00 AM cutoff", defaultRoles: ["super_admin", "principal"] },
  { id: "marks.entry", label: "Enter Exam Marks", module: "Examinations", description: "Input theory, practical & internal test scores", defaultRoles: ["super_admin", "teacher"] },
  { id: "marks.publish", label: "Publish Official Report Cards", module: "Examinations", description: "Release term report cards to parent portal", defaultRoles: ["super_admin", "principal"] },
  
  // Finance & Ledger
  { id: "fee.structure.edit", label: "Configure Fee Slabs", module: "Finance", description: "Create fee heads, discount slabs, and penalties", defaultRoles: ["super_admin", "accountant"] },
  { id: "fee.invoice.generate", label: "Generate Term Invoices", module: "Finance", description: "Trigger bulk invoice generation runs", defaultRoles: ["super_admin", "accountant"] },
  { id: "fee.payment.collect", label: "Collect Counter Payments", module: "Finance", description: "Accept offline cash, POS, or cheque receipts", defaultRoles: ["super_admin", "accountant"] },
  { id: "fee.waiver.approve", label: "Authorize Fee Concession", module: "Finance", description: "Grant special discounts or partial waivers", defaultRoles: ["super_admin", "principal"] },
  { id: "ledger.export", label: "Audit Ledger Book Export", module: "Finance", description: "Export double-entry financial ledger reports", defaultRoles: ["super_admin", "accountant"] },
  
  // Operations & Communication
  { id: "broadcast.send", label: "Dispatch Mass Broadcast", module: "Communication", description: "Send multi-channel SMS, WhatsApp and emails", defaultRoles: ["super_admin", "principal"] },
  { id: "fleet.dispatch", label: "Manage Bus Routes & Fleet", module: "Operations", description: "Reassign vehicle GPS units and route stops", defaultRoles: ["super_admin", "transport"] },
  { id: "library.circulation", label: "Library Book Circulation", module: "Operations", description: "Check out and return library books", defaultRoles: ["super_admin", "principal"] },
  
  // Governance & Security
  { id: "rbac.manage", label: "Modify RBAC Role Matrix", module: "System", description: "Grant or revoke security permissions", defaultRoles: ["super_admin"] },
  { id: "system.audit.view", label: "Access Security Audit Trail", module: "System", description: "Inspect tamper-evident system audit logs", defaultRoles: ["super_admin", "principal"] },
  { id: "system.backup.trigger", label: "Trigger Snapshot Backup", module: "System", description: "Execute on-demand RDS database snapshot", defaultRoles: ["super_admin"] },
];

// Mock Security Audit Trail matching system.audit_logs
interface AuditLogEntry {
  id: string;
  timestamp: string;
  actorName: string;
  actorRole: string;
  action: "CREATE" | "UPDATE" | "DELETE" | "OVERRIDE" | "LOGIN" | "EXPORT";
  resourceType: string;
  resourceId: string;
  ipAddress: string;
  status: "SUCCESS" | "DENIED" | "FLAGGED";
  details: string;
}

const mockAuditLogs: AuditLogEntry[] = [
  {
    id: "aud-9021",
    timestamp: "2026-11-08 16:15:22 IST",
    actorName: "Dr. Rajesh Sharma",
    actorRole: "Super Admin",
    action: "UPDATE",
    resourceType: "attendance.correction",
    resourceId: "ATT-2026-10A-044",
    ipAddress: "103.21.144.12",
    status: "SUCCESS",
    details: "Overrode student Aarav Sharma attendance from Absent to Medical Excused with cert #MC-882",
  },
  {
    id: "aud-9020",
    timestamp: "2026-11-08 15:42:09 IST",
    actorName: "Vikram Seth (Accountant)",
    actorRole: "Accountant",
    action: "CREATE",
    resourceType: "finance.payment_receipt",
    resourceId: "REC-2026-0892",
    ipAddress: "10.0.4.88",
    status: "SUCCESS",
    details: "Recorded offline POS fee receipt ₹45,000 for student STU-10821 (Diya Patel)",
  },
  {
    id: "aud-9019",
    timestamp: "2026-11-08 14:30:00 IST",
    actorName: "Pooja Hegde (Teacher)",
    actorRole: "Teacher",
    action: "OVERRIDE",
    resourceType: "examinations.marks_entry",
    resourceId: "MRK-TERM1-PHY-12A",
    ipAddress: "10.0.12.45",
    status: "DENIED",
    details: "Unauthorized attempt to modify marks post-lock window without Principal bypass token",
  },
  {
    id: "aud-9018",
    timestamp: "2026-11-08 13:10:15 IST",
    actorName: "Siddharth Rao",
    actorRole: "Admissions Officer",
    action: "UPDATE",
    resourceType: "admissions.document_custody",
    resourceId: "DOC-2026-ADM-019",
    ipAddress: "10.0.4.15",
    status: "SUCCESS",
    details: "Marked Original Transfer Certificate & Birth Certificate as Deposited in Cabinet B-3",
  },
  {
    id: "aud-9017",
    timestamp: "2026-11-08 12:05:44 IST",
    actorName: "Dr. Rajesh Sharma",
    actorRole: "Super Admin",
    action: "EXPORT",
    resourceType: "finance.ledger_csv",
    resourceId: "LEDGER-Q2-2026",
    ipAddress: "103.21.144.12",
    status: "SUCCESS",
    details: "Exported Q2 Double-Entry General Ledger CSV (5,820 transactions)",
  },
  {
    id: "aud-9016",
    timestamp: "2026-11-08 10:14:30 IST",
    actorName: "system-batch-worker",
    actorRole: "Spring Batch Daemon",
    action: "CREATE",
    resourceType: "system.outbox_events",
    resourceId: "BATCH-ATT-20000",
    ipAddress: "172.31.12.99",
    status: "SUCCESS",
    details: "Processed 18,450 student daily attendance registers in 3.42 seconds via Spring Batch chunking",
  },
  {
    id: "aud-9015",
    timestamp: "2026-11-08 08:35:10 IST",
    actorName: "Unknown Session",
    actorRole: "External Client",
    action: "LOGIN",
    resourceType: "identity.auth_token",
    resourceId: "AUTH-FAIL-IP-99",
    ipAddress: "45.33.32.156",
    status: "FLAGGED",
    details: "5 consecutive failed authentication attempts against superadmin endpoint. IP rate-limited.",
  },
];

export default function AdminSettingsPage() {
  // State for RBAC Matrix
  const [rolePermissions, setRolePermissions] = React.useState<Record<string, Set<string>>>(() => {
    const initial: Record<string, Set<string>> = {};
    rbacRoles.forEach((role) => {
      initial[role.id] = new Set<string>();
    });
    permissionsList.forEach((perm) => {
      perm.defaultRoles.forEach((r) => {
        if (initial[r]) {
          initial[r].add(perm.id);
        }
      });
    });
    return initial;
  });

  const [activeModuleFilter, setActiveModuleFilter] = React.useState<string>("All");
  const [permissionSearch, setPermissionSearch] = React.useState("");
  const [isSavedNotice, setIsSavedNotice] = React.useState(false);

  // State for Audit Trail
  const [auditSearch, setAuditSearch] = React.useState("");
  const [statusFilter, setStatusFilter] = React.useState<string>("ALL");

  const togglePermission = (roleId: string, permId: string) => {
    setRolePermissions((prev) => {
      const next = { ...prev };
      const roleSet = new Set(next[roleId]);
      if (roleSet.has(permId)) {
        roleSet.delete(permId);
      } else {
        roleSet.add(permId);
      }
      next[roleId] = roleSet;
      return next;
    });
  };

  const handleSaveRBAC = () => {
    setIsSavedNotice(true);
    setTimeout(() => setIsSavedNotice(false), 3000);
  };

  const filteredPermissions = permissionsList.filter((perm) => {
    const matchesModule = activeModuleFilter === "All" || perm.module === activeModuleFilter;
    const matchesSearch =
      perm.label.toLowerCase().includes(permissionSearch.toLowerCase()) ||
      perm.id.toLowerCase().includes(permissionSearch.toLowerCase()) ||
      perm.description.toLowerCase().includes(permissionSearch.toLowerCase());
    return matchesModule && matchesSearch;
  });

  const filteredAuditLogs = mockAuditLogs.filter((log) => {
    const matchesStatus = statusFilter === "ALL" || log.status === statusFilter;
    const matchesSearch =
      log.actorName.toLowerCase().includes(auditSearch.toLowerCase()) ||
      log.resourceId.toLowerCase().includes(auditSearch.toLowerCase()) ||
      log.details.toLowerCase().includes(auditSearch.toLowerCase()) ||
      log.ipAddress.includes(auditSearch);
    return matchesStatus && matchesSearch;
  });

  const modules = ["All", "Students", "Admissions", "Academics", "Attendance", "Examinations", "Finance", "Communication", "Operations", "System"];

  return (
    <div className="space-y-6 max-w-7xl mx-auto">
      {/* Title & Overview Banner */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold tracking-tight text-slate-900 dark:text-slate-50 flex items-center gap-2.5">
            <Sliders className="h-6 w-6 text-blue-600" />
            System Governance & Topology
          </h1>
          <p className="text-sm font-normal text-slate-500 dark:text-slate-400 mt-1">
            Enterprise cloud architecture, 12-schema database telemetry, RBAC authority matrix & tamper-evident audit logs
          </p>
        </div>
        <div className="flex items-center gap-2">
          <Badge variant="outline" className="text-xs bg-emerald-50 text-emerald-700 border-emerald-200 py-1 px-2.5">
            <span className="h-2 w-2 rounded-full bg-emerald-500 animate-pulse mr-1.5" />
            PostgreSQL 16 Cluster: Healthy
          </Badge>
        </div>
      </div>

      {/* Tabs Layout */}
      <Tabs defaultValue="topology" className="space-y-6">
        <TabsList className="bg-slate-100 dark:bg-slate-800 p-1 rounded-xl h-11 border border-slate-200 dark:border-slate-700">
          <TabsTrigger value="topology" className="text-xs font-semibold rounded-lg data-[state=active]:bg-white dark:data-[state=active]:bg-slate-900 data-[state=active]:shadow-xs flex items-center gap-1.5 px-4 py-2">
            <Server className="h-3.5 w-3.5 text-blue-600" />
            <span>System Topology & 12 Schemas</span>
          </TabsTrigger>
          <TabsTrigger value="rbac" className="text-xs font-semibold rounded-lg data-[state=active]:bg-white dark:data-[state=active]:bg-slate-900 data-[state=active]:shadow-xs flex items-center gap-1.5 px-4 py-2">
            <Lock className="h-3.5 w-3.5 text-purple-600" />
            <span>Granular RBAC Matrix</span>
          </TabsTrigger>
          <TabsTrigger value="audit" className="text-xs font-semibold rounded-lg data-[state=active]:bg-white dark:data-[state=active]:bg-slate-900 data-[state=active]:shadow-xs flex items-center gap-1.5 px-4 py-2">
            <History className="h-3.5 w-3.5 text-amber-600" />
            <span>Security Audit Trail</span>
          </TabsTrigger>
        </TabsList>

        {/* ================= TAB 1: SYSTEM TOPOLOGY & 12 SCHEMAS ================= */}
        <TabsContent value="topology" className="space-y-6 m-0">
          {/* Cloud & Cluster Cards */}
          <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
            <Card className="shadow-xs border-slate-200/80">
              <CardContent className="p-4 flex items-center gap-3">
                <div className="p-2.5 rounded-xl bg-blue-50 text-blue-600 dark:bg-blue-950/60 dark:text-blue-400 border border-blue-200/60 shrink-0">
                  <Cloud className="h-5 w-5" />
                </div>
                <div>
                  <p className="text-[10px] font-bold uppercase tracking-wider text-slate-400">Cloud Target</p>
                  <p className="text-sm font-bold text-slate-900 dark:text-slate-50 mt-0.5">AWS Mumbai (ap-south-1)</p>
                  <p className="text-[11px] text-slate-500">ECS Fargate • Multi-AZ Active</p>
                </div>
              </CardContent>
            </Card>

            <Card className="shadow-xs border-slate-200/80">
              <CardContent className="p-4 flex items-center gap-3">
                <div className="p-2.5 rounded-xl bg-emerald-50 text-emerald-600 dark:bg-emerald-950/60 dark:text-emerald-400 border border-emerald-200/60 shrink-0">
                  <Database className="h-5 w-5" />
                </div>
                <div>
                  <p className="text-[10px] font-bold uppercase tracking-wider text-slate-400">Database Engine</p>
                  <p className="text-sm font-bold text-slate-900 dark:text-slate-50 mt-0.5">PostgreSQL 16</p>
                  <p className="text-[11px] text-emerald-600 font-medium">12 of 12 Schemas Synced</p>
                </div>
              </CardContent>
            </Card>

            <Card className="shadow-xs border-slate-200/80">
              <CardContent className="p-4 flex items-center gap-3">
                <div className="p-2.5 rounded-xl bg-purple-50 text-purple-600 dark:bg-purple-950/60 dark:text-purple-400 border border-purple-200/60 shrink-0">
                  <Server className="h-5 w-5" />
                </div>
                <div>
                  <p className="text-[10px] font-bold uppercase tracking-wider text-slate-400">Backend Daemon</p>
                  <p className="text-sm font-bold text-slate-900 dark:text-slate-50 mt-0.5">Spring Boot 3 (Java 21)</p>
                  <p className="text-[11px] text-purple-600 font-medium">Spring Batch Active (20k/run)</p>
                </div>
              </CardContent>
            </Card>

            <Card className="shadow-xs border-slate-200/80">
              <CardContent className="p-4 flex items-center gap-3">
                <div className="p-2.5 rounded-xl bg-amber-50 text-amber-600 dark:bg-amber-950/60 dark:text-amber-400 border border-amber-200/60 shrink-0">
                  <Shield className="h-5 w-5" />
                </div>
                <div>
                  <p className="text-[10px] font-bold uppercase tracking-wider text-slate-400">Disaster Recovery</p>
                  <p className="text-sm font-bold text-slate-900 dark:text-slate-50 mt-0.5">RPO: 15m • RTO: 4h</p>
                  <p className="text-[11px] text-slate-500">Point-in-Time Recovery Active</p>
                </div>
              </CardContent>
            </Card>
          </div>

          {/* 12-Schema Topology Grid */}
          <Card className="shadow-xs border-slate-200/80">
            <CardHeader className="pb-4 border-b border-slate-100 dark:border-slate-800 flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3">
              <div>
                <CardTitle className="text-base font-semibold text-slate-900 dark:text-slate-50">
                  PostgreSQL 12 Logical Schemas
                </CardTitle>
                <CardDescription className="text-xs text-slate-500 dark:text-slate-400">
                  Partitioned database schemas enforcing domain boundary isolation and high transaction performance
                </CardDescription>
              </div>
              <Badge variant="outline" className="text-xs font-mono font-medium border-slate-300">
                Total Tables: 76 • Total Partitioned Records: 2.8M
              </Badge>
            </CardHeader>
            <CardContent className="p-5">
              <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-3.5">
                {schemaList.map((sch) => (
                  <div
                    key={sch.name}
                    className="flex flex-col justify-between p-3.5 rounded-xl border border-slate-200/80 dark:border-slate-800 bg-slate-50/50 dark:bg-slate-800/30 hover:border-blue-300 transition-colors"
                  >
                    <div>
                      <div className="flex items-center justify-between">
                        <span className="text-xs font-bold text-blue-700 dark:text-blue-400 font-mono flex items-center gap-1.5">
                          <Layers className="h-3 w-3 text-slate-400" />
                          schema.{sch.name}
                        </span>
                        <span className="text-[11px] font-semibold text-emerald-600 dark:text-emerald-400 flex items-center gap-1">
                          <CheckCircle2 className="h-3 w-3" />
                          {sch.status}
                        </span>
                      </div>
                      <p className="text-xs text-slate-600 dark:text-slate-400 mt-2 leading-relaxed">
                        {sch.desc}
                      </p>
                    </div>
                    <div className="flex items-center justify-between mt-3 pt-2.5 border-t border-slate-200/60 dark:border-slate-700/60 text-[11px] text-slate-500 font-mono">
                      <span>{sch.tableCount} Tables</span>
                      <span className="font-semibold text-slate-700 dark:text-slate-300">{sch.records} records</span>
                    </div>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        {/* ================= TAB 2: GRANULAR RBAC PERMISSIONS MATRIX ================= */}
        <TabsContent value="rbac" className="space-y-6 m-0">
          <Card className="shadow-xs border-slate-200/80">
            <CardHeader className="pb-4 border-b border-slate-100 dark:border-slate-800 flex flex-col md:flex-row md:items-center md:justify-between gap-4">
              <div>
                <CardTitle className="text-base font-semibold text-slate-900 dark:text-slate-50 flex items-center gap-2">
                  <KeyRound className="h-4 w-4 text-purple-600" />
                  Action-Based Role Permissions Matrix (PostgreSQL identity.role_permissions)
                </CardTitle>
                <CardDescription className="text-xs text-slate-500 dark:text-slate-400">
                  Configure real-time authorization access rules across administrative and operational roles
                </CardDescription>
              </div>
              <div className="flex items-center gap-2">
                {isSavedNotice && (
                  <Badge variant="success" className="text-xs py-1 px-2.5 animate-in fade-in">
                    <Check className="h-3 w-3 mr-1" />
                    Permissions Saved
                  </Badge>
                )}
                <Button
                  onClick={handleSaveRBAC}
                  className="bg-blue-600 hover:bg-blue-700 text-white font-semibold text-xs h-8 px-3 shadow-xs gap-1.5"
                >
                  <Save className="h-3.5 w-3.5" />
                  Save Changes
                </Button>
              </div>
            </CardHeader>
            <CardContent className="p-5 space-y-4">
              {/* Filter and Search Bar */}
              <div className="flex flex-col sm:flex-row items-center justify-between gap-3">
                {/* Search */}
                <div className="relative w-full sm:w-72">
                  <Search className="absolute left-2.5 top-1/2 -translate-y-1/2 h-3.5 w-3.5 text-slate-400" />
                  <Input
                    placeholder="Search action or resource..."
                    value={permissionSearch}
                    onChange={(e) => setPermissionSearch(e.target.value)}
                    className="pl-8 h-8 text-xs bg-slate-50 dark:bg-slate-800 border-slate-200"
                  />
                </div>

                {/* Module Pill Filter */}
                <div className="flex items-center gap-1 overflow-x-auto w-full sm:w-auto pb-1 sm:pb-0">
                  {modules.map((mod) => (
                    <button
                      key={mod}
                      onClick={() => setActiveModuleFilter(mod)}
                      className={cn(
                        "text-[11px] px-2.5 py-1 rounded-md font-medium transition-colors shrink-0",
                        activeModuleFilter === mod
                          ? "bg-blue-600 text-white font-semibold"
                          : "bg-slate-100 hover:bg-slate-200 text-slate-600 dark:bg-slate-800 dark:text-slate-400"
                      )}
                    >
                      {mod}
                    </button>
                  ))}
                </div>
              </div>

              {/* RBAC Matrix Table */}
              <div className="rounded-xl border border-slate-200/90 dark:border-slate-800 overflow-x-auto">
                <Table>
                  <TableHeader className="bg-slate-50/90 dark:bg-slate-800/60">
                    <TableRow>
                      <TableHead className="w-[300px] text-xs font-bold text-slate-700 dark:text-slate-300">
                        Permission & Description
                      </TableHead>
                      <TableHead className="w-[100px] text-xs font-bold text-slate-700 dark:text-slate-300">
                        Module
                      </TableHead>
                      {rbacRoles.map((role) => (
                        <TableHead key={role.id} className="text-center text-xs font-bold text-slate-700 dark:text-slate-300 min-w-[100px]">
                          <span className={cn("inline-block px-2 py-0.5 rounded text-[10px] font-semibold border", role.color)}>
                            {role.label}
                          </span>
                        </TableHead>
                      ))}
                    </TableRow>
                  </TableHeader>
                  <TableBody>
                    {filteredPermissions.map((perm) => (
                      <TableRow key={perm.id} className="hover:bg-slate-50/50 dark:hover:bg-slate-800/30">
                        <TableCell className="py-2.5">
                          <div className="flex flex-col">
                            <span className="text-xs font-bold text-slate-900 dark:text-slate-100">
                              {perm.label}
                            </span>
                            <span className="font-mono text-[10px] text-slate-400">
                              {perm.id}
                            </span>
                            <span className="text-[11px] text-slate-500 dark:text-slate-400 mt-0.5">
                              {perm.description}
                            </span>
                          </div>
                        </TableCell>
                        <TableCell className="py-2.5">
                          <Badge variant="outline" className="text-[10px] font-medium border-slate-200">
                            {perm.module}
                          </Badge>
                        </TableCell>
                        {rbacRoles.map((role) => {
                          const isEnabled = rolePermissions[role.id]?.has(perm.id) ?? false;
                          const isSuperAdmin = role.id === "super_admin";
                          return (
                            <TableCell key={role.id} className="text-center py-2.5">
                              <input
                                type="checkbox"
                                checked={isEnabled}
                                disabled={isSuperAdmin}
                                onChange={() => togglePermission(role.id, perm.id)}
                                className={cn(
                                  "h-4 w-4 rounded border-slate-300 text-blue-600 focus:ring-blue-500 cursor-pointer transition-colors",
                                  isSuperAdmin && "cursor-not-allowed opacity-75"
                                )}
                                title={isSuperAdmin ? "Super Admin permissions are immutable" : `Toggle ${perm.label} for ${role.label}`}
                              />
                            </TableCell>
                          );
                        })}
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        {/* ================= TAB 3: SYSTEM SECURITY AUDIT TRAIL EXPLORER ================= */}
        <TabsContent value="audit" className="space-y-6 m-0">
          <Card className="shadow-xs border-slate-200/80">
            <CardHeader className="pb-4 border-b border-slate-100 dark:border-slate-800 flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
              <div>
                <CardTitle className="text-base font-semibold text-slate-900 dark:text-slate-50 flex items-center gap-2">
                  <Shield className="h-4 w-4 text-emerald-600" />
                  System Security & Audit Trail (PostgreSQL system.audit_logs)
                </CardTitle>
                <CardDescription className="text-xs text-slate-500 dark:text-slate-400">
                  Tamper-evident immutable ledger capturing all identity changes, data overrides, financial transactions, and login attempts
                </CardDescription>
              </div>
              <Button
                variant="outline"
                className="h-8 text-xs font-semibold gap-1.5 border-slate-200"
                onClick={() => alert("Audit log export generated: audit_trail_export_20261108.csv")}
              >
                <Download className="h-3.5 w-3.5 text-slate-500" />
                Export Audit Log (CSV)
              </Button>
            </CardHeader>
            <CardContent className="p-5 space-y-4">
              {/* Filter controls */}
              <div className="flex flex-col sm:flex-row items-center justify-between gap-3">
                <div className="relative w-full sm:w-80">
                  <Search className="absolute left-2.5 top-1/2 -translate-y-1/2 h-3.5 w-3.5 text-slate-400" />
                  <Input
                    placeholder="Search actor, resource ID, or IP..."
                    value={auditSearch}
                    onChange={(e) => setAuditSearch(e.target.value)}
                    className="pl-8 h-8 text-xs bg-slate-50 dark:bg-slate-800 border-slate-200"
                  />
                </div>

                <div className="flex items-center gap-2 w-full sm:w-auto">
                  <span className="text-xs text-slate-500 font-medium">Status:</span>
                  {["ALL", "SUCCESS", "DENIED", "FLAGGED"].map((st) => (
                    <button
                      key={st}
                      onClick={() => setStatusFilter(st)}
                      className={cn(
                        "text-[11px] px-2.5 py-1 rounded-md font-medium transition-colors",
                        statusFilter === st
                          ? "bg-slate-900 text-white font-semibold dark:bg-slate-100 dark:text-slate-900"
                          : "bg-slate-100 hover:bg-slate-200 text-slate-600 dark:bg-slate-800 dark:text-slate-400"
                      )}
                    >
                      {st}
                    </button>
                  ))}
                </div>
              </div>

              {/* Audit Table */}
              <div className="rounded-xl border border-slate-200/90 dark:border-slate-800 overflow-x-auto">
                <Table>
                  <TableHeader className="bg-slate-50/90 dark:bg-slate-800/60">
                    <TableRow>
                      <TableHead className="w-[160px] text-xs font-bold text-slate-700 dark:text-slate-300">Timestamp</TableHead>
                      <TableHead className="w-[180px] text-xs font-bold text-slate-700 dark:text-slate-300">Actor & Role</TableHead>
                      <TableHead className="w-[100px] text-xs font-bold text-slate-700 dark:text-slate-300">Action</TableHead>
                      <TableHead className="w-[190px] text-xs font-bold text-slate-700 dark:text-slate-300">Resource Target</TableHead>
                      <TableHead className="text-xs font-bold text-slate-700 dark:text-slate-300">Event Details</TableHead>
                      <TableHead className="w-[120px] text-xs font-bold text-slate-700 dark:text-slate-300">Client IP</TableHead>
                      <TableHead className="w-[90px] text-center text-xs font-bold text-slate-700 dark:text-slate-300">Status</TableHead>
                    </TableRow>
                  </TableHeader>
                  <TableBody>
                    {filteredAuditLogs.map((log) => (
                      <TableRow key={log.id} className="hover:bg-slate-50/50 dark:hover:bg-slate-800/30">
                        <TableCell className="py-2.5 text-[11px] font-mono text-slate-600 dark:text-slate-400 whitespace-nowrap">
                          {log.timestamp}
                        </TableCell>
                        <TableCell className="py-2.5">
                          <div className="flex flex-col">
                            <span className="text-xs font-bold text-slate-900 dark:text-slate-100">
                              {log.actorName}
                            </span>
                            <span className="text-[10px] text-slate-500 font-medium">
                              {log.actorRole}
                            </span>
                          </div>
                        </TableCell>
                        <TableCell className="py-2.5">
                          <Badge
                            variant={
                              log.action === "CREATE"
                                ? "success"
                                : log.action === "DELETE" || log.action === "OVERRIDE"
                                ? "destructive"
                                : log.action === "UPDATE"
                                ? "info"
                                : "outline"
                            }
                            className="text-[10px] font-mono font-bold"
                          >
                            {log.action}
                          </Badge>
                        </TableCell>
                        <TableCell className="py-2.5">
                          <div className="flex flex-col font-mono text-[11px]">
                            <span className="font-semibold text-slate-700 dark:text-slate-300">
                              {log.resourceType}
                            </span>
                            <span className="text-[10px] text-slate-400">
                              {log.resourceId}
                            </span>
                          </div>
                        </TableCell>
                        <TableCell className="py-2.5 text-xs text-slate-600 dark:text-slate-400 max-w-xs">
                          {log.details}
                        </TableCell>
                        <TableCell className="py-2.5 font-mono text-[11px] text-slate-500 whitespace-nowrap">
                          {log.ipAddress}
                        </TableCell>
                        <TableCell className="py-2.5 text-center">
                          <Badge
                            variant={
                              log.status === "SUCCESS"
                                ? "success"
                                : log.status === "DENIED"
                                ? "destructive"
                                : "warning"
                            }
                            className="text-[10px] font-bold"
                          >
                            {log.status}
                          </Badge>
                        </TableCell>
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
              </div>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  );
}
