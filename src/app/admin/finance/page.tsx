"use client";

import * as React from "react";
import {
  Receipt,
  Download,
  Filter,
  Search,
  Plus,
  ArrowUpRight,
  CheckCircle2,
  AlertTriangle,
  Clock,
  Printer,
  CreditCard,
  Building,
  RefreshCw,
  ExternalLink,
  ChevronDown,
} from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Badge } from "@/components/ui/badge";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { cn } from "@/lib/utils";
import { CreateFeeHeadModal } from "@/components/admin/modals/create-fee-head-modal";

interface Invoice {
  id: string;
  invoiceNumber: string;
  studentName: string;
  admissionNumber: string;
  className: string;
  totalAmount: number;
  paidAmount: number;
  balanceAmount: number;
  dueDate: string;
  status: "PAID" | "PARTIAL" | "OVERDUE" | "WAIVED";
  paymentMethod?: string;
  paidAt?: string;
}

const mockInvoices: Invoice[] = [
  {
    id: "inv-101",
    invoiceNumber: "INV-2026-0842",
    studentName: "Aarav Sharma",
    admissionNumber: "ADM-2024-0012",
    className: "Grade 10-A",
    totalAmount: 35000,
    paidAmount: 35000,
    balanceAmount: 0,
    dueDate: "2026-10-31",
    status: "PAID",
    paymentMethod: "UPI (Razorpay)",
    paidAt: "2026-10-28",
  },
  {
    id: "inv-102",
    invoiceNumber: "INV-2026-0843",
    studentName: "Diya Patel",
    admissionNumber: "ADM-2023-0145",
    className: "Grade 12-B",
    totalAmount: 42000,
    paidAmount: 25000,
    balanceAmount: 17000,
    dueDate: "2026-11-05",
    status: "PARTIAL",
    paymentMethod: "Net Banking",
    paidAt: "2026-11-02",
  },
  {
    id: "inv-103",
    invoiceNumber: "INV-2026-0844",
    studentName: "Rohan Deshmukh",
    admissionNumber: "ADM-2022-0312",
    className: "Grade 9-C",
    totalAmount: 32000,
    paidAmount: 0,
    balanceAmount: 32000,
    dueDate: "2026-10-15",
    status: "OVERDUE",
  },
  {
    id: "inv-104",
    invoiceNumber: "INV-2026-0845",
    studentName: "Ananya Iyer",
    admissionNumber: "ADM-2024-0098",
    className: "Grade 11-A",
    totalAmount: 45000,
    paidAmount: 45000,
    balanceAmount: 0,
    dueDate: "2026-10-31",
    status: "PAID",
    paymentMethod: "Credit Card (HDFC)",
    paidAt: "2026-10-30",
  },
  {
    id: "inv-105",
    invoiceNumber: "INV-2026-0846",
    studentName: "Kabir Khan",
    admissionNumber: "ADM-2023-0421",
    className: "Grade 10-B",
    totalAmount: 35000,
    paidAmount: 0,
    balanceAmount: 35000,
    dueDate: "2026-10-20",
    status: "OVERDUE",
  },
  {
    id: "inv-106",
    invoiceNumber: "INV-2026-0847",
    studentName: "Sara Thomas",
    admissionNumber: "ADM-2024-0231",
    className: "Grade 8-A",
    totalAmount: 28000,
    paidAmount: 28000,
    balanceAmount: 0,
    dueDate: "2026-11-01",
    status: "PAID",
    paymentMethod: "UPI",
    paidAt: "2026-10-31",
  },
  {
    id: "inv-107",
    invoiceNumber: "INV-2026-0848",
    studentName: "Ishaan Gupta",
    admissionNumber: "ADM-2025-0019",
    className: "Grade 11-B",
    totalAmount: 45000,
    paidAmount: 20000,
    balanceAmount: 25000,
    dueDate: "2026-11-10",
    status: "PARTIAL",
    paymentMethod: "Demand Draft",
    paidAt: "2026-11-04",
  },
];

const feeStructures = [
  {
    gradeRange: "Grades 11-12 (Senior Secondary - Science)",
    tuitionFee: "₹55,000",
    labFee: "₹18,000",
    libraryFee: "₹5,000",
    examFee: "₹4,000",
    totalAnnual: "₹82,000",
    frequency: "Termly (3 Installments)",
  },
  {
    gradeRange: "Grades 11-12 (Commerce / Humanities)",
    tuitionFee: "₹50,000",
    labFee: "₹6,000",
    libraryFee: "₹5,000",
    examFee: "₹4,000",
    totalAnnual: "₹65,000",
    frequency: "Termly (3 Installments)",
  },
  {
    gradeRange: "Grades 9-10 (Secondary CBSE)",
    tuitionFee: "₹48,000",
    labFee: "₹8,000",
    libraryFee: "₹4,000",
    examFee: "₹3,500",
    totalAnnual: "₹63,500",
    frequency: "Termly (3 Installments)",
  },
  {
    gradeRange: "Grades 6-8 (Middle School)",
    tuitionFee: "₹42,000",
    labFee: "₹4,000",
    libraryFee: "₹3,500",
    examFee: "₹3,000",
    totalAnnual: "₹52,500",
    frequency: "Termly (3 Installments)",
  },
  {
    gradeRange: "Grades 1-5 (Primary Wing)",
    tuitionFee: "₹38,000",
    labFee: "₹2,000",
    libraryFee: "₹2,500",
    examFee: "₹2,500",
    totalAnnual: "₹45,000",
    frequency: "Termly (3 Installments)",
  },
];

const gradeAgingBreakdown = [
  { grade: "Grade 12", collected: "96.4%", collectedAmt: "₹1.48 Cr", overdueAmt: "₹5.5 L", overdueCount: 14 },
  { grade: "Grade 11", collected: "94.2%", collectedAmt: "₹1.36 Cr", overdueAmt: "₹8.4 L", overdueCount: 22 },
  { grade: "Grade 10", collected: "93.8%", collectedAmt: "₹1.22 Cr", overdueAmt: "₹8.1 L", overdueCount: 26 },
  { grade: "Grade 9", collected: "92.1%", collectedAmt: "₹1.15 Cr", overdueAmt: "₹9.8 L", overdueCount: 31 },
  { grade: "Grade 6-8", collected: "91.5%", collectedAmt: "₹2.10 Cr", overdueAmt: "₹19.5 L", overdueCount: 68 },
  { grade: "Grade 1-5", collected: "94.0%", collectedAmt: "₹2.54 Cr", overdueAmt: "₹16.2 L", overdueCount: 54 },
];

export default function AdminFinancePage() {
  const [activeTab, setActiveTab] = React.useState<"invoices" | "structures" | "aging" | "gateway">("invoices");
  const [searchQuery, setSearchQuery] = React.useState("");
  const [statusFilter, setStatusFilter] = React.useState<string>("ALL");
  const [selectedInvoice, setSelectedInvoice] = React.useState<Invoice | null>(null);
  const [isCreateHeadOpen, setIsCreateHeadOpen] = React.useState(false);

  const filteredInvoices = mockInvoices.filter((inv) => {
    const matchesSearch =
      inv.studentName.toLowerCase().includes(searchQuery.toLowerCase()) ||
      inv.admissionNumber.toLowerCase().includes(searchQuery.toLowerCase()) ||
      inv.invoiceNumber.toLowerCase().includes(searchQuery.toLowerCase()) ||
      inv.className.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesStatus = statusFilter === "ALL" || inv.status === statusFilter;
    return matchesSearch && matchesStatus;
  });

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between border-b border-slate-200/80 pb-5">
        <div>
          <div className="flex items-center gap-2.5">
            <h1 className="text-xl font-bold tracking-tight text-slate-900">
              Fee Management & Ledger
            </h1>
            <Badge variant="outline" className="text-[11px] font-semibold text-emerald-700 bg-emerald-50 border-emerald-200">
              AY 2026-27 Active
            </Badge>
          </div>
          <p className="text-xs text-slate-500 mt-1">
            Automated student invoicing, idempotent webhook reconciliation, and double-entry accounting ledger.
          </p>
        </div>
        <div className="flex flex-wrap items-center gap-2">
          <Button variant="outline" size="sm" className="h-8 gap-1.5 text-xs text-slate-700">
            <Download className="h-3.5 w-3.5" />
            Export Ledger
          </Button>
          <Button
            size="sm"
            onClick={() => setIsCreateHeadOpen(true)}
            className="h-8 gap-1.5 text-xs bg-blue-600 hover:bg-blue-700 text-white shadow-xs"
          >
            <Plus className="h-3.5 w-3.5" />
            Create Fee Head
          </Button>
        </div>
      </div>

      {/* Financial KPI Cards */}
      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
        <Card className="border-slate-200/80 shadow-xs">
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <span className="text-[11px] font-semibold uppercase tracking-wider text-slate-500">
              Annual Fee Demand
            </span>
            <div className="flex h-7 w-7 items-center justify-center rounded-md bg-blue-50 text-blue-600">
              <Receipt className="h-3.5 w-3.5" />
            </div>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold tracking-tight text-slate-900">₹5,20,00,000</div>
            <p className="text-[11px] text-slate-500 mt-1 flex items-center gap-1">
              <span>Target for 18,450 enrolled students</span>
            </p>
          </CardContent>
        </Card>

        <Card className="border-slate-200/80 shadow-xs">
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <span className="text-[11px] font-semibold uppercase tracking-wider text-slate-500">
              Collected to Date
            </span>
            <div className="flex h-7 w-7 items-center justify-center rounded-md bg-emerald-50 text-emerald-600">
              <CheckCircle2 className="h-3.5 w-3.5" />
            </div>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold tracking-tight text-emerald-700">₹4,85,00,000</div>
            <div className="flex items-center gap-2 mt-1">
              <Badge className="bg-emerald-50 text-emerald-700 border border-emerald-200 text-[10px] px-1.5 py-0 font-medium">
                93.2% Realized
              </Badge>
              <span className="text-[11px] text-slate-500">+8.4% YoY</span>
            </div>
          </CardContent>
        </Card>

        <Card className="border-slate-200/80 shadow-xs">
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <span className="text-[11px] font-semibold uppercase tracking-wider text-slate-500">
              Overdue Arrears
            </span>
            <div className="flex h-7 w-7 items-center justify-center rounded-md bg-rose-50 text-rose-600">
              <AlertTriangle className="h-3.5 w-3.5" />
            </div>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold tracking-tight text-rose-700">₹35,00,000</div>
            <p className="text-[11px] text-slate-500 mt-1">
              Across 612 student accounts • 6.8% balance
            </p>
          </CardContent>
        </Card>

        <Card className="border-slate-200/80 shadow-xs">
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <span className="text-[11px] font-semibold uppercase tracking-wider text-slate-500">
              Today's Settlements
            </span>
            <div className="flex h-7 w-7 items-center justify-center rounded-md bg-indigo-50 text-indigo-600">
              <CreditCard className="h-3.5 w-3.5" />
            </div>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold tracking-tight text-slate-900">₹6,42,500</div>
            <p className="text-[11px] text-emerald-600 font-medium mt-1 flex items-center gap-1">
              <ArrowUpRight className="h-3 w-3" />
              18 transactions settled via Razorpay
            </p>
          </CardContent>
        </Card>
      </div>

      {/* Tabs Navigation */}
      <div className="flex border-b border-slate-200 text-xs font-semibold">
        <button
          onClick={() => setActiveTab("invoices")}
          className={cn(
            "pb-2.5 px-3 border-b-2 transition-colors",
            activeTab === "invoices"
              ? "border-blue-600 text-blue-600 font-bold"
              : "border-transparent text-slate-500 hover:text-slate-900"
          )}
        >
          Student Invoices & Receipts
        </button>
        <button
          onClick={() => setActiveTab("aging")}
          className={cn(
            "pb-2.5 px-3 border-b-2 transition-colors",
            activeTab === "aging"
              ? "border-blue-600 text-blue-600 font-bold"
              : "border-transparent text-slate-500 hover:text-slate-900"
          )}
        >
          Grade Collection Aging
        </button>
        <button
          onClick={() => setActiveTab("structures")}
          className={cn(
            "pb-2.5 px-3 border-b-2 transition-colors",
            activeTab === "structures"
              ? "border-blue-600 text-blue-600 font-bold"
              : "border-transparent text-slate-500 hover:text-slate-900"
          )}
        >
          Fee Structures & Heads
        </button>
        <button
          onClick={() => setActiveTab("gateway")}
          className={cn(
            "pb-2.5 px-3 border-b-2 transition-colors",
            activeTab === "gateway"
              ? "border-blue-600 text-blue-600 font-bold"
              : "border-transparent text-slate-500 hover:text-slate-900"
          )}
        >
          Payment Gateway Webhooks
        </button>
      </div>

      {/* TAB 1: Invoices Table */}
      {activeTab === "invoices" && (
        <Card className="border-slate-200/80 shadow-xs">
          <CardHeader className="p-4 pb-3 border-b border-slate-100 flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3">
            <div className="relative flex-1 max-w-sm">
              <Search className="absolute left-2.5 top-2.5 h-3.5 w-3.5 text-slate-400" />
              <Input
                placeholder="Search by student, admission # or invoice..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="h-8 pl-8 text-xs bg-slate-50/50 border-slate-200"
              />
            </div>
            <div className="flex items-center gap-2">
              <div className="flex rounded-md border border-slate-200 p-0.5 text-xs bg-slate-50/50">
                {(["ALL", "PAID", "PARTIAL", "OVERDUE"] as const).map((st) => (
                  <button
                    key={st}
                    onClick={() => setStatusFilter(st)}
                    className={cn(
                      "px-2.5 py-1 rounded text-[11px] font-medium transition-colors",
                      statusFilter === st
                        ? "bg-white text-slate-900 shadow-xs font-semibold"
                        : "text-slate-600 hover:text-slate-900"
                    )}
                  >
                    {st === "ALL" ? "All" : st.charAt(0) + st.slice(1).toLowerCase()}
                  </button>
                ))}
              </div>
            </div>
          </CardHeader>

          <CardContent className="p-0">
            <Table>
              <TableHeader>
                <TableRow className="border-slate-100 bg-slate-50/50 hover:bg-slate-50/50 text-[11px]">
                  <TableHead className="font-semibold text-slate-600">Invoice #</TableHead>
                  <TableHead className="font-semibold text-slate-600">Student & Class</TableHead>
                  <TableHead className="font-semibold text-slate-600 text-right">Total Demand</TableHead>
                  <TableHead className="font-semibold text-slate-600 text-right">Paid</TableHead>
                  <TableHead className="font-semibold text-slate-600 text-right">Balance</TableHead>
                  <TableHead className="font-semibold text-slate-600">Due Date</TableHead>
                  <TableHead className="font-semibold text-slate-600">Status</TableHead>
                  <TableHead className="font-semibold text-slate-600 text-right">Actions</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {filteredInvoices.map((inv) => (
                  <TableRow key={inv.id} className="border-slate-100 hover:bg-slate-50/60 text-xs">
                    <TableCell className="font-mono text-[11px] font-medium text-blue-600">
                      {inv.invoiceNumber}
                    </TableCell>
                    <TableCell>
                      <div className="font-medium text-slate-900">{inv.studentName}</div>
                      <div className="text-[11px] text-slate-400">
                        {inv.admissionNumber} • {inv.className}
                      </div>
                    </TableCell>
                    <TableCell className="text-right font-medium text-slate-800">
                      ₹{inv.totalAmount.toLocaleString("en-IN")}
                    </TableCell>
                    <TableCell className="text-right font-medium text-emerald-600">
                      ₹{inv.paidAmount.toLocaleString("en-IN")}
                    </TableCell>
                    <TableCell className="text-right font-semibold text-slate-900">
                      ₹{inv.balanceAmount.toLocaleString("en-IN")}
                    </TableCell>
                    <TableCell className="text-slate-500 text-[11px]">{inv.dueDate}</TableCell>
                    <TableCell>
                      {inv.status === "PAID" && (
                        <Badge className="bg-emerald-50 text-emerald-700 border-emerald-200 text-[10px] px-2 py-0 font-medium">
                          Paid
                        </Badge>
                      )}
                      {inv.status === "PARTIAL" && (
                        <Badge className="bg-amber-50 text-amber-700 border-amber-200 text-[10px] px-2 py-0 font-medium">
                          Partial
                        </Badge>
                      )}
                      {inv.status === "OVERDUE" && (
                        <Badge className="bg-rose-50 text-rose-700 border-rose-200 text-[10px] px-2 py-0 font-medium">
                          Overdue
                        </Badge>
                      )}
                    </TableCell>
                    <TableCell className="text-right">
                      <Button
                        variant="ghost"
                        size="sm"
                        onClick={() => setSelectedInvoice(inv)}
                        className="h-7 text-[11px] text-blue-600 hover:text-blue-700 hover:bg-blue-50 px-2"
                      >
                        Receipt
                      </Button>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </CardContent>
        </Card>
      )}

      {/* TAB 2: Grade Collection Aging */}
      {activeTab === "aging" && (
        <div className="grid gap-4 md:grid-cols-2">
          <Card className="border-slate-200/80 shadow-xs">
            <CardHeader className="p-4 border-b border-slate-100">
              <CardTitle className="text-sm font-bold text-slate-900">
                Grade-wise Collection Rates
              </CardTitle>
              <CardDescription className="text-xs text-slate-500">
                Collection progress against semester fee targets
              </CardDescription>
            </CardHeader>
            <CardContent className="p-4 space-y-4">
              {gradeAgingBreakdown.map((row) => (
                <div key={row.grade} className="space-y-1.5">
                  <div className="flex items-center justify-between text-xs">
                    <span className="font-semibold text-slate-800">{row.grade}</span>
                    <span className="font-mono text-emerald-700 font-bold">
                      {row.collectedAmt} ({row.collected})
                    </span>
                  </div>
                  <div className="h-2 w-full bg-slate-100 rounded-full overflow-hidden">
                    <div
                      className="h-full bg-emerald-500 rounded-full"
                      style={{ width: row.collected }}
                    />
                  </div>
                  <div className="flex justify-between text-[10px] text-slate-400">
                    <span>Arrears: {row.overdueAmt}</span>
                    <span>{row.overdueCount} students overdue</span>
                  </div>
                </div>
              ))}
            </CardContent>
          </Card>

          <Card className="border-slate-200/80 shadow-xs">
            <CardHeader className="p-4 border-b border-slate-100">
              <CardTitle className="text-sm font-bold text-slate-900">
                Arrears Aging Breakdown
              </CardTitle>
              <CardDescription className="text-xs text-slate-500">
                Overdue duration risk categorization (Total: ₹35,00,000)
              </CardDescription>
            </CardHeader>
            <CardContent className="p-4 space-y-4">
              <div className="flex items-center justify-between p-3 rounded-lg bg-emerald-50/50 border border-emerald-100">
                <div>
                  <div className="text-xs font-bold text-emerald-900">1 – 30 Days Overdue</div>
                  <div className="text-[11px] text-emerald-700">Low risk • First reminder dispatched</div>
                </div>
                <div className="text-right">
                  <div className="text-sm font-bold text-emerald-900">₹14,20,000</div>
                  <div className="text-[10px] text-emerald-600">240 students</div>
                </div>
              </div>

              <div className="flex items-center justify-between p-3 rounded-lg bg-amber-50/50 border border-amber-100">
                <div>
                  <div className="text-xs font-bold text-amber-900">31 – 60 Days Overdue</div>
                  <div className="text-[11px] text-amber-700">Moderate risk • WhatsApp & SMS notice active</div>
                </div>
                <div className="text-right">
                  <div className="text-sm font-bold text-amber-900">₹12,50,000</div>
                  <div className="text-[10px] text-amber-600">198 students</div>
                </div>
              </div>

              <div className="flex items-center justify-between p-3 rounded-lg bg-rose-50/50 border border-rose-100">
                <div>
                  <div className="text-xs font-bold text-rose-900">60+ Days Overdue</div>
                  <div className="text-[11px] text-rose-700">High risk • Administrative follow-up required</div>
                </div>
                <div className="text-right">
                  <div className="text-sm font-bold text-rose-900">₹8,30,000</div>
                  <div className="text-[10px] text-rose-600">174 students</div>
                </div>
              </div>
            </CardContent>
          </Card>
        </div>
      )}

      {/* TAB 3: Fee Structures */}
      {activeTab === "structures" && (
        <Card className="border-slate-200/80 shadow-xs">
          <CardHeader className="p-4 pb-3 border-b border-slate-100 flex flex-row items-center justify-between">
            <div>
              <CardTitle className="text-sm font-bold text-slate-900">
                Approved Fee Structures (AY 2026-27)
              </CardTitle>
              <CardDescription className="text-xs text-slate-500">
                Board-agnostic fee slabs configured for CBSE curriculum
              </CardDescription>
            </div>
            <Button size="sm" className="h-7 text-xs bg-blue-600 hover:bg-blue-700 text-white gap-1">
              <Plus className="h-3 w-3" />
              Add Structure
            </Button>
          </CardHeader>
          <CardContent className="p-0">
            <Table>
              <TableHeader>
                <TableRow className="border-slate-100 bg-slate-50/50 text-[11px]">
                  <TableHead className="font-semibold text-slate-600">Grade Level / Tier</TableHead>
                  <TableHead className="font-semibold text-slate-600">Tuition</TableHead>
                  <TableHead className="font-semibold text-slate-600">Lab & Digital</TableHead>
                  <TableHead className="font-semibold text-slate-600">Library</TableHead>
                  <TableHead className="font-semibold text-slate-600">Exam Fee</TableHead>
                  <TableHead className="font-semibold text-slate-600 font-bold">Total Annual</TableHead>
                  <TableHead className="font-semibold text-slate-600">Billing Cycle</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {feeStructures.map((fs, idx) => (
                  <TableRow key={idx} className="border-slate-100 hover:bg-slate-50/60 text-xs">
                    <TableCell className="font-semibold text-slate-900">{fs.gradeRange}</TableCell>
                    <TableCell className="text-slate-600">{fs.tuitionFee}</TableCell>
                    <TableCell className="text-slate-600">{fs.labFee}</TableCell>
                    <TableCell className="text-slate-600">{fs.libraryFee}</TableCell>
                    <TableCell className="text-slate-600">{fs.examFee}</TableCell>
                    <TableCell className="font-bold text-blue-700">{fs.totalAnnual}</TableCell>
                    <TableCell className="text-[11px] text-slate-500">{fs.frequency}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </CardContent>
        </Card>
      )}

      {/* TAB 4: Gateway Webhook Reconciliation */}
      {activeTab === "gateway" && (
        <Card className="border-slate-200/80 shadow-xs">
          <CardHeader className="p-4 border-b border-slate-100">
            <div className="flex items-center justify-between">
              <div>
                <CardTitle className="text-sm font-bold text-slate-900">
                  Payment Gateway Webhooks (Idempotent Settlement Log)
                </CardTitle>
                <CardDescription className="text-xs text-slate-500">
                  Direct server-to-server webhook verification with HMAC SHA-256 signatures
                </CardDescription>
              </div>
              <Badge className="bg-emerald-50 text-emerald-700 border border-emerald-200 text-[10px] px-2 py-0.5">
                Razorpay Webhook: Active
              </Badge>
            </div>
          </CardHeader>
          <CardContent className="p-4 space-y-3 font-mono text-[11px]">
            <div className="flex items-center justify-between p-2.5 rounded border border-slate-200 bg-slate-50/60">
              <div className="flex items-center gap-3">
                <span className="flex h-2 w-2 rounded-full bg-emerald-500" />
                <span className="font-bold text-slate-900">order.paid</span>
                <span className="text-slate-500">evt_rzp_9941029</span>
                <span className="text-slate-700 font-semibold">₹35,000 (Aarav Sharma)</span>
              </div>
              <div className="flex items-center gap-3 text-slate-400">
                <span>Signature Verified</span>
                <span>2026-10-28 14:22:10 UTC</span>
                <Badge variant="outline" className="text-[10px] text-emerald-600 bg-emerald-50">Settled</Badge>
              </div>
            </div>

            <div className="flex items-center justify-between p-2.5 rounded border border-slate-200 bg-slate-50/60">
              <div className="flex items-center gap-3">
                <span className="flex h-2 w-2 rounded-full bg-emerald-500" />
                <span className="font-bold text-slate-900">order.paid</span>
                <span className="text-slate-500">evt_rzp_9941030</span>
                <span className="text-slate-700 font-semibold">₹45,000 (Ananya Iyer)</span>
              </div>
              <div className="flex items-center gap-3 text-slate-400">
                <span>Signature Verified</span>
                <span>2026-10-30 09:15:44 UTC</span>
                <Badge variant="outline" className="text-[10px] text-emerald-600 bg-emerald-50">Settled</Badge>
              </div>
            </div>

            <div className="flex items-center justify-between p-2.5 rounded border border-slate-200 bg-slate-50/60">
              <div className="flex items-center gap-3">
                <span className="flex h-2 w-2 rounded-full bg-emerald-500" />
                <span className="font-bold text-slate-900">order.paid</span>
                <span className="text-slate-500">evt_rzp_9941031</span>
                <span className="text-slate-700 font-semibold">₹28,000 (Sara Thomas)</span>
              </div>
              <div className="flex items-center gap-3 text-slate-400">
                <span>Signature Verified</span>
                <span>2026-10-31 16:48:02 UTC</span>
                <Badge variant="outline" className="text-[10px] text-emerald-600 bg-emerald-50">Settled</Badge>
              </div>
            </div>
          </CardContent>
        </Card>
      )}

      {/* Official Receipt Modal */}
      {selectedInvoice && (
        <Dialog open={!!selectedInvoice} onOpenChange={() => setSelectedInvoice(null)}>
          <DialogContent className="max-w-md">
            <DialogHeader>
              <div className="flex items-center justify-between border-b pb-3">
                <div className="flex items-center gap-2">
                  <div className="h-6 w-6 rounded bg-blue-600 text-white flex items-center justify-center font-bold text-xs">
                    A
                  </div>
                  <span className="font-bold text-sm text-slate-900">Apex Academy • Fee Receipt</span>
                </div>
                <Badge className="bg-emerald-50 text-emerald-700 border-emerald-200 text-[10px]">
                  {selectedInvoice.status}
                </Badge>
              </div>
            </DialogHeader>

            <div className="space-y-4 text-xs py-2">
              <div className="flex justify-between text-slate-500">
                <span>Receipt / Invoice:</span>
                <span className="font-mono font-bold text-slate-900">{selectedInvoice.invoiceNumber}</span>
              </div>
              <div className="flex justify-between text-slate-500">
                <span>Student Name:</span>
                <span className="font-semibold text-slate-900">{selectedInvoice.studentName}</span>
              </div>
              <div className="flex justify-between text-slate-500">
                <span>Admission No & Class:</span>
                <span className="text-slate-800">{selectedInvoice.admissionNumber} ({selectedInvoice.className})</span>
              </div>
              <div className="flex justify-between text-slate-500">
                <span>Payment Method:</span>
                <span className="text-slate-800">{selectedInvoice.paymentMethod || "Pending"}</span>
              </div>

              <div className="border-t border-b py-2 space-y-1.5 font-mono">
                <div className="flex justify-between text-slate-600">
                  <span>Tuition & Digital Labs:</span>
                  <span>₹{selectedInvoice.totalAmount.toLocaleString("en-IN")}</span>
                </div>
                <div className="flex justify-between font-bold text-emerald-700">
                  <span>Amount Paid:</span>
                  <span>₹{selectedInvoice.paidAmount.toLocaleString("en-IN")}</span>
                </div>
                <div className="flex justify-between font-bold text-slate-900">
                  <span>Remaining Balance:</span>
                  <span>₹{selectedInvoice.balanceAmount.toLocaleString("en-IN")}</span>
                </div>
              </div>

              <div className="text-[10px] text-slate-400 text-center">
                Double-Entry Ledger Ref: #GL-2026-FEE-{selectedInvoice.id} • Verified via PostgreSQL Finance Schema
              </div>
            </div>

            <DialogFooter className="gap-2 sm:justify-between border-t pt-3">
              <Button variant="outline" size="sm" onClick={() => setSelectedInvoice(null)} className="h-8 text-xs">
                Close
              </Button>
              <Button size="sm" className="h-8 gap-1 text-xs bg-blue-600 hover:bg-blue-700 text-white">
                <Printer className="h-3.5 w-3.5" />
                Print Official Receipt
              </Button>
            </DialogFooter>
          </DialogContent>
        </Dialog>
      )}
      {/* Create Fee Head Modal */}
      <CreateFeeHeadModal
        open={isCreateHeadOpen}
        onOpenChange={setIsCreateHeadOpen}
      />
    </div>
  );
}

