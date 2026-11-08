"use client";

import * as React from "react";
import {
  Send,
  Bell,
  MessageSquare,
  Mail,
  Smartphone,
  CheckCircle2,
  AlertCircle,
  Plus,
  Users,
  Eye,
  Calendar,
  FileText,
  Clock,
  Radio,
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
import { cn } from "@/lib/utils";

interface Circular {
  id: string;
  circularNumber: string;
  title: string;
  targetAudience: string;
  channels: string[];
  publishedDate: string;
  readCount: number;
  totalRecipients: number;
  priority: "HIGH" | "NORMAL" | "URGENT";
}

const mockCirculars: Circular[] = [
  {
    id: "circ-1",
    circularNumber: "CIR-2026-042",
    title: "CBSE Class 10 & 12 Pre-Board Schedule & Admit Card Guidelines",
    targetAudience: "Grades 10 & 12 (Parents & Students)",
    channels: ["SMS", "Email", "Portal"],
    publishedDate: "2026-11-05",
    readCount: 1420,
    totalRecipients: 1540,
    priority: "HIGH",
  },
  {
    id: "circ-2",
    circularNumber: "CIR-2026-041",
    title: "Annual Sports Meet 2026: House Selection & Track Trials Notice",
    targetAudience: "All School (Grades 1 – 12)",
    channels: ["Portal", "Email"],
    publishedDate: "2026-11-02",
    readCount: 16890,
    totalRecipients: 18450,
    priority: "NORMAL",
  },
  {
    id: "circ-3",
    circularNumber: "CIR-2026-040",
    title: "Term 2 Fee Installment Due Date Reminder",
    targetAudience: "Parents with Outstanding Dues",
    channels: ["SMS", "WhatsApp"],
    publishedDate: "2026-10-25",
    readCount: 580,
    totalRecipients: 612,
    priority: "URGENT",
  },
  {
    id: "circ-4",
    circularNumber: "CIR-2026-039",
    title: "Diwali Vacation Campus Closure & Re-opening Dates",
    targetAudience: "Staff & Students",
    channels: ["Email", "Portal"],
    publishedDate: "2026-10-18",
    readCount: 18100,
    totalRecipients: 18450,
    priority: "NORMAL",
  },
];

export default function AdminCommunicationPage() {
  const [activeTab, setActiveTab] = React.useState<"circulars" | "compose" | "gateway">("circulars");
  const [noticeTitle, setNoticeTitle] = React.useState("");
  const [noticeTarget, setNoticeTarget] = React.useState("ALL");

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between border-b border-slate-200/80 pb-5">
        <div>
          <div className="flex items-center gap-2.5">
            <h1 className="text-xl font-bold tracking-tight text-slate-900">
              Communication & Broadcast Hub
            </h1>
            <Badge variant="outline" className="text-[11px] font-semibold text-blue-700 bg-blue-50 border-blue-200">
              Multi-Channel Dispatch
            </Badge>
          </div>
          <p className="text-xs text-slate-500 mt-1">
            Dispatch official school circulars, emergency announcements, and targeted SMS/Email broadcasts.
          </p>
        </div>
        <div className="flex items-center gap-2">
          <Button
            size="sm"
            onClick={() => setActiveTab("compose")}
            className="h-8 gap-1.5 text-xs bg-blue-600 hover:bg-blue-700 text-white shadow-xs"
          >
            <Plus className="h-3.5 w-3.5" />
            Compose Broadcast
          </Button>
        </div>
      </div>

      {/* KPI Stats */}
      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
        <Card className="border-slate-200/80 shadow-xs">
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <span className="text-[11px] font-semibold uppercase tracking-wider text-slate-500">
              Active Circulars
            </span>
            <div className="flex h-7 w-7 items-center justify-center rounded-md bg-blue-50 text-blue-600">
              <FileText className="h-3.5 w-3.5" />
            </div>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold tracking-tight text-slate-900">14</div>
            <p className="text-[11px] text-slate-500 mt-1">Published this academic term</p>
          </CardContent>
        </Card>

        <Card className="border-slate-200/80 shadow-xs">
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <span className="text-[11px] font-semibold uppercase tracking-wider text-slate-500">
              SMS Dispatches Today
            </span>
            <div className="flex h-7 w-7 items-center justify-center rounded-md bg-emerald-50 text-emerald-600">
              <Smartphone className="h-3.5 w-3.5" />
            </div>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold tracking-tight text-emerald-700">1,842</div>
            <p className="text-[11px] text-emerald-600 font-medium mt-1">99.8% gateway delivery rate</p>
          </CardContent>
        </Card>

        <Card className="border-slate-200/80 shadow-xs">
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <span className="text-[11px] font-semibold uppercase tracking-wider text-slate-500">
              Parent Read Rate
            </span>
            <div className="flex h-7 w-7 items-center justify-center rounded-md bg-indigo-50 text-indigo-600">
              <Eye className="h-3.5 w-3.5" />
            </div>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold tracking-tight text-slate-900">92.4%</div>
            <p className="text-[11px] text-slate-500 mt-1">Average notice read within 24 hours</p>
          </CardContent>
        </Card>

        <Card className="border-slate-200/80 shadow-xs">
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <span className="text-[11px] font-semibold uppercase tracking-wider text-slate-500">
              Broadcast Queue
            </span>
            <div className="flex h-7 w-7 items-center justify-center rounded-md bg-slate-100 text-slate-700">
              <Radio className="h-3.5 w-3.5" />
            </div>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold tracking-tight text-slate-900">Idle</div>
            <p className="text-[11px] text-slate-500 mt-1">0 pending BullMQ communication jobs</p>
          </CardContent>
        </Card>
      </div>

      {/* Tabs */}
      <div className="flex border-b border-slate-200 text-xs font-semibold">
        <button
          onClick={() => setActiveTab("circulars")}
          className={cn(
            "pb-2.5 px-3 border-b-2 transition-colors",
            activeTab === "circulars"
              ? "border-blue-600 text-blue-600 font-bold"
              : "border-transparent text-slate-500 hover:text-slate-900"
          )}
        >
          Official Circulars
        </button>
        <button
          onClick={() => setActiveTab("compose")}
          className={cn(
            "pb-2.5 px-3 border-b-2 transition-colors",
            activeTab === "compose"
              ? "border-blue-600 text-blue-600 font-bold"
              : "border-transparent text-slate-500 hover:text-slate-900"
          )}
        >
          Compose Broadcast Notice
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
          SMS & Email Gateway Telemetry
        </button>
      </div>

      {/* TAB 1: Circulars Table */}
      {activeTab === "circulars" && (
        <Card className="border-slate-200/80 shadow-xs">
          <CardHeader className="p-4 pb-3 border-b border-slate-100 flex flex-row items-center justify-between">
            <div>
              <CardTitle className="text-sm font-bold text-slate-900">
                School Circulars & Notifications
              </CardTitle>
              <CardDescription className="text-xs text-slate-500">
                Audited school announcements with read receipt tracking
              </CardDescription>
            </div>
          </CardHeader>
          <CardContent className="p-0">
            <Table>
              <TableHeader>
                <TableRow className="border-slate-100 bg-slate-50/50 text-[11px]">
                  <TableHead className="font-semibold text-slate-600">Circular #</TableHead>
                  <TableHead className="font-semibold text-slate-600">Title & Topic</TableHead>
                  <TableHead className="font-semibold text-slate-600">Target Audience</TableHead>
                  <TableHead className="font-semibold text-slate-600">Channels</TableHead>
                  <TableHead className="font-semibold text-slate-600">Published</TableHead>
                  <TableHead className="font-semibold text-slate-600">Read Receipts</TableHead>
                  <TableHead className="font-semibold text-slate-600 text-right">Actions</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {mockCirculars.map((circ) => {
                  const readPct = Math.round((circ.readCount / circ.totalRecipients) * 100);
                  return (
                    <TableRow key={circ.id} className="border-slate-100 hover:bg-slate-50/60 text-xs">
                      <TableCell className="font-mono text-[11px] font-semibold text-blue-600">
                        {circ.circularNumber}
                      </TableCell>
                      <TableCell>
                        <div className="font-semibold text-slate-900">{circ.title}</div>
                      </TableCell>
                      <TableCell className="text-slate-600 text-[11px]">{circ.targetAudience}</TableCell>
                      <TableCell>
                        <div className="flex gap-1">
                          {circ.channels.map((ch) => (
                            <Badge
                              key={ch}
                              variant="outline"
                              className="text-[9px] px-1.5 py-0 font-medium text-slate-600 bg-slate-50"
                            >
                              {ch}
                            </Badge>
                          ))}
                        </div>
                      </TableCell>
                      <TableCell className="text-slate-500 font-mono text-[11px]">
                        {circ.publishedDate}
                      </TableCell>
                      <TableCell>
                        <div className="flex items-center gap-1.5 text-xs">
                          <span className="font-bold text-slate-800">{readPct}%</span>
                          <span className="text-[10px] text-slate-400">
                            ({circ.readCount.toLocaleString("en-IN")})
                          </span>
                        </div>
                      </TableCell>
                      <TableCell className="text-right">
                        <Button
                          variant="ghost"
                          size="sm"
                          className="h-7 text-[11px] text-slate-600 hover:text-slate-900 px-2"
                        >
                          View Notice
                        </Button>
                      </TableCell>
                    </TableRow>
                  );
                })}
              </TableBody>
            </Table>
          </CardContent>
        </Card>
      )}

      {/* TAB 2: Compose Broadcast */}
      {activeTab === "compose" && (
        <Card className="border-slate-200/80 shadow-xs max-w-2xl">
          <CardHeader className="p-4 border-b border-slate-100">
            <CardTitle className="text-sm font-bold text-slate-900">
              Draft & Broadcast Official Notice
            </CardTitle>
            <CardDescription className="text-xs text-slate-500">
              Notice will be dispatched to the selected audience via high-throughput BullMQ queue
            </CardDescription>
          </CardHeader>
          <CardContent className="p-5 space-y-4">
            <div className="space-y-1.5">
              <label className="text-xs font-semibold text-slate-700">Circular Title</label>
              <Input
                placeholder="e.g. Schedule for Annual Science Fair 2026..."
                className="h-8 text-xs"
                value={noticeTitle}
                onChange={(e) => setNoticeTitle(e.target.value)}
              />
            </div>

            <div className="grid grid-cols-2 gap-4">
              <div className="space-y-1.5">
                <label className="text-xs font-semibold text-slate-700">Target Audience</label>
                <select
                  className="h-8 w-full rounded-md border border-slate-200 bg-white px-2.5 text-xs text-slate-700"
                  value={noticeTarget}
                  onChange={(e) => setNoticeTarget(e.target.value)}
                >
                  <option value="ALL">All School (18,450 Students & Guardians)</option>
                  <option value="SENIOR">Senior Wing (Grades 9 to 12)</option>
                  <option value="MIDDLE">Middle Wing (Grades 6 to 8)</option>
                  <option value="PRIMARY">Primary Wing (Grades 1 to 5)</option>
                  <option value="STAFF">All Faculty & Staff Only</option>
                </select>
              </div>

              <div className="space-y-1.5">
                <label className="text-xs font-semibold text-slate-700">Dispatch Channels</label>
                <div className="flex items-center gap-3 pt-1 text-xs text-slate-600">
                  <label className="flex items-center gap-1.5 cursor-pointer">
                    <input type="checkbox" defaultChecked className="rounded border-slate-300" />
                    <span>Portal</span>
                  </label>
                  <label className="flex items-center gap-1.5 cursor-pointer">
                    <input type="checkbox" defaultChecked className="rounded border-slate-300" />
                    <span>SMS</span>
                  </label>
                  <label className="flex items-center gap-1.5 cursor-pointer">
                    <input type="checkbox" defaultChecked className="rounded border-slate-300" />
                    <span>Email</span>
                  </label>
                </div>
              </div>
            </div>

            <div className="space-y-1.5">
              <label className="text-xs font-semibold text-slate-700">Notice Body</label>
              <textarea
                rows={5}
                placeholder="Write circular content here..."
                className="w-full rounded-md border border-slate-200 bg-white p-2.5 text-xs text-slate-800 focus:outline-none focus:ring-1 focus:ring-blue-600"
              />
            </div>

            <div className="flex justify-end gap-2 pt-2 border-t">
              <Button variant="outline" size="sm" onClick={() => setActiveTab("circulars")} className="h-8 text-xs">
                Cancel
              </Button>
              <Button size="sm" className="h-8 gap-1.5 text-xs bg-blue-600 hover:bg-blue-700 text-white">
                <Send className="h-3 w-3" />
                Publish & Dispatch Broadcast
              </Button>
            </div>
          </CardContent>
        </Card>
      )}

      {/* TAB 3: Gateway Telemetry */}
      {activeTab === "gateway" && (
        <Card className="border-slate-200/80 shadow-xs">
          <CardHeader className="p-4 border-b border-slate-100">
            <CardTitle className="text-sm font-bold text-slate-900">
              SMS & Communication Gateway Health
            </CardTitle>
            <CardDescription className="text-xs text-slate-500">
              Telecom DLT registration: APEX-ACADEMY-MUM • Sender ID: APEXAC
            </CardDescription>
          </CardHeader>
          <CardContent className="p-4 space-y-3 font-mono text-[11px]">
            <div className="flex items-center justify-between p-2.5 rounded border border-slate-200 bg-slate-50/60">
              <div className="flex items-center gap-3">
                <span className="flex h-2 w-2 rounded-full bg-emerald-500" />
                <span className="font-bold text-slate-900">SMS Gateway (DLT Route)</span>
                <span className="text-slate-500">Carrier: Bharti Airtel / Jio DLT</span>
              </div>
              <div className="flex items-center gap-3 text-slate-500">
                <span>Latency: 420ms</span>
                <Badge variant="outline" className="text-[10px] text-emerald-600 bg-emerald-50">Operational</Badge>
              </div>
            </div>

            <div className="flex items-center justify-between p-2.5 rounded border border-slate-200 bg-slate-50/60">
              <div className="flex items-center gap-3">
                <span className="flex h-2 w-2 rounded-full bg-emerald-500" />
                <span className="font-bold text-slate-900">Transactional Email (SES / SendGrid)</span>
                <span className="text-slate-500">DKIM & SPF: Verified</span>
              </div>
              <div className="flex items-center gap-3 text-slate-500">
                <span>Bounce Rate: 0.04%</span>
                <Badge variant="outline" className="text-[10px] text-emerald-600 bg-emerald-50">Operational</Badge>
              </div>
            </div>
          </CardContent>
        </Card>
      )}
    </div>
  );
}

