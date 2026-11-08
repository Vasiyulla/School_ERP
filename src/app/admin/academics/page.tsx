"use client";

import * as React from "react";
import {
  CalendarDays,
  PlusCircle,
  Users,
  Layers,
  Clock,
  ArrowRight,
  CheckCircle2,
  AlertTriangle,
  RotateCcw,
  Sparkles,
  BookOpen,
} from "lucide-react";
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import {
  Table,
  TableHeader,
  TableBody,
  TableRow,
  TableHead,
  TableCell,
} from "@/components/ui/table";
import { mockClasses } from "@/lib/mock/admin-mock";
import { cn } from "@/lib/utils";

const timetableDays = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday"];
const timetablePeriods = [
  { id: 1, time: "08:30 – 09:15", label: "P1" },
  { id: 2, time: "09:15 – 10:00", label: "P2" },
  { id: 3, time: "10:15 – 11:00", label: "P3" },
  { id: 4, time: "11:00 – 11:45", label: "P4" },
  { id: 5, time: "12:30 – 01:15", label: "P5" },
  { id: 6, time: "01:15 – 02:00", label: "P6" },
  { id: 7, time: "02:05 – 02:50", label: "P7" },
];

const mockSchedule: Record<string, { subject: string; teacher: string; room: string; color: string }[]> = {
  Monday: [
    { subject: "Mathematics", teacher: "Dr. Vikram Seth", room: "Room 301", color: "bg-blue-50 text-blue-700 border-blue-200" },
    { subject: "Physics (Theory)", teacher: "Meera Nair", room: "Room 301", color: "bg-purple-50 text-purple-700 border-purple-200" },
    { subject: "English Lit", teacher: "Sunita Roy", room: "Room 301", color: "bg-amber-50 text-amber-700 border-amber-200" },
    { subject: "Chemistry Lab", teacher: "Rajesh Kulkarni", room: "Chem Lab 2", color: "bg-emerald-50 text-emerald-700 border-emerald-200" },
    { subject: "Chemistry Lab", teacher: "Rajesh Kulkarni", room: "Chem Lab 2", color: "bg-emerald-50 text-emerald-700 border-emerald-200" },
    { subject: "Computer Science", teacher: "Pooja Hegde", room: "Lab A", color: "bg-indigo-50 text-indigo-700 border-indigo-200" },
    { subject: "Physical Ed", teacher: "Ranveer Singh", room: "Sports Field", color: "bg-orange-50 text-orange-700 border-orange-200" },
  ],
  Tuesday: [
    { subject: "Physics Lab", teacher: "Meera Nair", room: "Physics Lab", color: "bg-purple-50 text-purple-700 border-purple-200" },
    { subject: "Physics Lab", teacher: "Meera Nair", room: "Physics Lab", color: "bg-purple-50 text-purple-700 border-purple-200" },
    { subject: "Mathematics", teacher: "Dr. Vikram Seth", room: "Room 301", color: "bg-blue-50 text-blue-700 border-blue-200" },
    { subject: "Biology", teacher: "Anjali Rao", room: "Room 301", color: "bg-emerald-50 text-emerald-700 border-emerald-200" },
    { subject: "Social Sciences", teacher: "Amitabh Sen", room: "Room 301", color: "bg-teal-50 text-teal-700 border-teal-200" },
    { subject: "English Lang", teacher: "Sunita Roy", room: "Room 301", color: "bg-amber-50 text-amber-700 border-amber-200" },
    { subject: "Library / Study", teacher: "Librarian", room: "Central Lib", color: "bg-slate-50 text-slate-700 border-slate-200" },
  ],
  Wednesday: [
    { subject: "Mathematics", teacher: "Dr. Vikram Seth", room: "Room 301", color: "bg-blue-50 text-blue-700 border-blue-200" },
    { subject: "Mathematics", teacher: "Dr. Vikram Seth", room: "Room 301", color: "bg-blue-50 text-blue-700 border-blue-200" },
    { subject: "Chemistry", teacher: "Rajesh Kulkarni", room: "Room 301", color: "bg-emerald-50 text-emerald-700 border-emerald-200" },
    { subject: "Physics", teacher: "Meera Nair", room: "Room 301", color: "bg-purple-50 text-purple-700 border-purple-200" },
    { subject: "Hindi / Sanskrit", teacher: "Kavita Sharma", room: "Room 301", color: "bg-rose-50 text-rose-700 border-rose-200" },
    { subject: "Computer Science", teacher: "Pooja Hegde", room: "Lab A", color: "bg-indigo-50 text-indigo-700 border-indigo-200" },
    { subject: "Art & Design", teacher: "Kiran Bedi", room: "Studio 1", color: "bg-pink-50 text-pink-700 border-pink-200" },
  ],
  Thursday: [
    { subject: "English Lit", teacher: "Sunita Roy", room: "Room 301", color: "bg-amber-50 text-amber-700 border-amber-200" },
    { subject: "Chemistry", teacher: "Rajesh Kulkarni", room: "Room 301", color: "bg-emerald-50 text-emerald-700 border-emerald-200" },
    { subject: "Mathematics", teacher: "Dr. Vikram Seth", room: "Room 301", color: "bg-blue-50 text-blue-700 border-blue-200" },
    { subject: "Biology Practical", teacher: "Anjali Rao", room: "Bio Lab", color: "bg-emerald-50 text-emerald-700 border-emerald-200" },
    { subject: "Biology Practical", teacher: "Anjali Rao", room: "Bio Lab", color: "bg-emerald-50 text-emerald-700 border-emerald-200" },
    { subject: "Physics", teacher: "Meera Nair", room: "Room 301", color: "bg-purple-50 text-purple-700 border-purple-200" },
    { subject: "Social Sciences", teacher: "Amitabh Sen", room: "Room 301", color: "bg-teal-50 text-teal-700 border-teal-200" },
  ],
  Friday: [
    { subject: "Physics", teacher: "Meera Nair", room: "Room 301", color: "bg-purple-50 text-purple-700 border-purple-200" },
    { subject: "Chemistry", teacher: "Rajesh Kulkarni", room: "Room 301", color: "bg-emerald-50 text-emerald-700 border-emerald-200" },
    { subject: "Mathematics", teacher: "Dr. Vikram Seth", room: "Room 301", color: "bg-blue-50 text-blue-700 border-blue-200" },
    { subject: "English Lang", teacher: "Sunita Roy", room: "Room 301", color: "bg-amber-50 text-amber-700 border-amber-200" },
    { subject: "Robotics & AI", teacher: "Pooja Hegde", room: "AI Lab", color: "bg-indigo-50 text-indigo-700 border-indigo-200" },
    { subject: "Value Education", teacher: "Father Joseph", room: "Auditorium", color: "bg-slate-50 text-slate-700 border-slate-200" },
    { subject: "Sports & Games", teacher: "Ranveer Singh", room: "Sports Ground", color: "bg-orange-50 text-orange-700 border-orange-200" },
  ],
};

const promotionTiers = [
  { fromGrade: "Grade 12 (All Streams)", toGrade: "Graduated / Alumni Archive", enrolledCount: 1420, readyCount: 1412, repeatCount: 8, status: "READY" },
  { fromGrade: "Grade 11 (Science)", toGrade: "Grade 12-A & 12-B (Science)", enrolledCount: 520, readyCount: 512, repeatCount: 8, status: "READY" },
  { fromGrade: "Grade 11 (Commerce)", toGrade: "Grade 12-C & 12-D (Commerce)", enrolledCount: 480, readyCount: 474, repeatCount: 6, status: "READY" },
  { fromGrade: "Grade 10 (Secondary)", toGrade: "Grade 11 (Stream Allocation)", enrolledCount: 1680, readyCount: 1650, repeatCount: 30, status: "READY" },
  { fromGrade: "Grade 9 (Secondary)", toGrade: "Grade 10 (Secondary CBSE)", enrolledCount: 1720, readyCount: 1695, repeatCount: 25, status: "READY" },
  { fromGrade: "Grades 1 – 8 (Primary/Middle)", toGrade: "Next Class Tier Progression", enrolledCount: 12630, readyCount: 12580, repeatCount: 50, status: "READY" },
];

export default function AdminAcademicsPage() {
  const [activeTab, setActiveTab] = React.useState<"classes" | "timetable" | "promotion">("classes");
  const [selectedTimetableClass, setSelectedTimetableClass] = React.useState("Grade 10-A");
  const [isPromoting, setIsPromoting] = React.useState(false);
  const [promotionSuccess, setPromotionSuccess] = React.useState(false);

  const handleExecutePromotion = () => {
    setIsPromoting(true);
    setTimeout(() => {
      setIsPromoting(false);
      setPromotionSuccess(true);
      setTimeout(() => setPromotionSuccess(false), 3000);
    }, 1200);
  };

  return (
    <div className="space-y-6 max-w-7xl mx-auto">
      {/* Title */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 border-b border-slate-200/80 pb-5">
        <div>
          <div className="flex items-center gap-2.5">
            <h1 className="text-xl font-bold tracking-tight text-slate-900">
              Academic Governance & Curriculum
            </h1>
            <Badge variant="outline" className="text-[11px] font-semibold text-blue-700 bg-blue-50 border-blue-200">
              CBSE All-India
            </Badge>
          </div>
          <p className="text-xs text-slate-500 mt-1">
            Classes & sections, weekly master period timetable, and student annual academic rollover promotion engine.
          </p>
        </div>
        <div className="flex items-center gap-2">
          <Button size="sm" className="h-8 gap-1.5 text-xs bg-blue-600 hover:bg-blue-700 text-white shadow-xs">
            <PlusCircle className="h-3.5 w-3.5" />
            Add Class Section
          </Button>
        </div>
      </div>

      {/* Top Academic Info Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <Card className="border-slate-200/80 shadow-xs">
          <CardContent className="p-4 flex items-center gap-3.5">
            <div className="p-2.5 rounded-xl bg-blue-50 text-blue-600 shrink-0">
              <CalendarDays className="h-5 w-5" />
            </div>
            <div>
              <p className="text-[11px] font-semibold uppercase tracking-wider text-slate-500">Active Academic Session</p>
              <p className="text-base font-bold text-slate-900 mt-0.5">AY 2026-2027 • Term 2</p>
              <p className="text-[11px] text-slate-500">Nov 2026 – Mar 2027 (112 working days)</p>
            </div>
          </CardContent>
        </Card>

        <Card className="border-slate-200/80 shadow-xs">
          <CardContent className="p-4 flex items-center gap-3.5">
            <div className="p-2.5 rounded-xl bg-purple-50 text-purple-600 shrink-0">
              <Layers className="h-5 w-5" />
            </div>
            <div>
              <p className="text-[11px] font-semibold uppercase tracking-wider text-slate-500">Curriculum Framework</p>
              <p className="text-base font-bold text-slate-900 mt-0.5">CBSE Affiliation #113042</p>
              <p className="text-[11px] text-slate-500">Continuous Comprehensive Assessment</p>
            </div>
          </CardContent>
        </Card>

        <Card className="border-slate-200/80 shadow-xs">
          <CardContent className="p-4 flex items-center gap-3.5">
            <div className="p-2.5 rounded-xl bg-emerald-50 text-emerald-600 shrink-0">
              <Users className="h-5 w-5" />
            </div>
            <div>
              <p className="text-[11px] font-semibold uppercase tracking-wider text-slate-500">Class & Section Structure</p>
              <p className="text-base font-bold text-slate-900 mt-0.5">12 Grades • 48 Sections</p>
              <p className="text-[11px] text-slate-500">18,450 students enrolled (92.3% capacity)</p>
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Tabs Navigation */}
      <div className="flex border-b border-slate-200 text-xs font-semibold">
        <button
          onClick={() => setActiveTab("classes")}
          className={cn(
            "pb-2.5 px-3 border-b-2 transition-colors",
            activeTab === "classes"
              ? "border-blue-600 text-blue-600 font-bold"
              : "border-transparent text-slate-500 hover:text-slate-900"
          )}
        >
          Classes & Sections Allocations
        </button>
        <button
          onClick={() => setActiveTab("timetable")}
          className={cn(
            "pb-2.5 px-3 border-b-2 transition-colors",
            activeTab === "timetable"
              ? "border-blue-600 text-blue-600 font-bold"
              : "border-transparent text-slate-500 hover:text-slate-900"
          )}
        >
          Master Class Timetable Grid
        </button>
        <button
          onClick={() => setActiveTab("promotion")}
          className={cn(
            "pb-2.5 px-3 border-b-2 transition-colors",
            activeTab === "promotion"
              ? "border-blue-600 text-blue-600 font-bold"
              : "border-transparent text-slate-500 hover:text-slate-900"
          )}
        >
          Student Promotion & Rollover Engine
        </button>
      </div>

      {/* TAB 1: Classes & Sections Master Table */}
      {activeTab === "classes" && (
        <Card className="shadow-xs border-slate-200/80 overflow-hidden">
          <CardHeader className="flex flex-row items-center justify-between p-4 pb-3 border-b border-slate-100">
            <div>
              <CardTitle className="text-sm font-bold text-slate-900">
                Classes & Section Allocations
              </CardTitle>
              <CardDescription className="text-xs text-slate-500">
                Section capacities, room designations, and assigned class teachers
              </CardDescription>
            </div>
            <Badge variant="outline" className="text-xs font-semibold">
              48 Total Sections
            </Badge>
          </CardHeader>
          <Table>
            <TableHeader>
              <TableRow className="border-slate-100 bg-slate-50/50 text-[11px]">
                <TableHead className="font-semibold text-slate-700">Class Name</TableHead>
                <TableHead className="font-semibold text-slate-700">Section</TableHead>
                <TableHead className="font-semibold text-slate-700">Capacity</TableHead>
                <TableHead className="font-semibold text-slate-700">Enrolled Students</TableHead>
                <TableHead className="font-semibold text-slate-700">Designated Class Teacher</TableHead>
                <TableHead className="font-semibold text-slate-700">Assigned Room</TableHead>
                <TableHead className="text-right w-24 font-semibold text-slate-700">Actions</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {mockClasses.map((cls) => (
                <TableRow key={cls.id} className="text-xs hover:bg-slate-50/50 border-slate-100">
                  <TableCell className="font-bold text-slate-900">
                    {cls.className}
                  </TableCell>
                  <TableCell className="font-medium text-slate-700">
                    {cls.sectionName}
                  </TableCell>
                  <TableCell className="text-slate-600">
                    {cls.capacity} seats
                  </TableCell>
                  <TableCell className="font-mono font-bold text-blue-600">
                    {cls.enrolledCount} enrolled
                  </TableCell>
                  <TableCell className="text-slate-800 font-medium">
                    {cls.classTeacher}
                  </TableCell>
                  <TableCell className="text-slate-500 font-mono text-[11px]">
                    {cls.roomNumber}
                  </TableCell>
                  <TableCell className="text-right">
                    <Button variant="ghost" size="sm" className="h-7 text-[11px] text-blue-600 hover:bg-blue-50">
                      Configure
                    </Button>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </Card>
      )}

      {/* TAB 2: Master Weekly Timetable Grid */}
      {activeTab === "timetable" && (
        <Card className="shadow-xs border-slate-200/80">
          <CardHeader className="flex flex-col sm:flex-row sm:items-center sm:justify-between p-4 pb-3 border-b border-slate-100 gap-3">
            <div>
              <CardTitle className="text-sm font-bold text-slate-900">
                Weekly Class Routine & Period Schedule
              </CardTitle>
              <CardDescription className="text-xs text-slate-500">
                Zero teacher schedule collisions detected for {selectedTimetableClass}
              </CardDescription>
            </div>
            <div className="flex items-center gap-2">
              <span className="text-xs text-slate-500 font-medium">Select Class:</span>
              <select
                value={selectedTimetableClass}
                onChange={(e) => setSelectedTimetableClass(e.target.value)}
                className="h-8 rounded-md border border-slate-200 bg-white px-2.5 text-xs font-semibold text-slate-800"
              >
                <option value="Grade 10-A">Grade 10-A (Secondary CBSE)</option>
                <option value="Grade 10-B">Grade 10-B (Secondary CBSE)</option>
                <option value="Grade 11-A">Grade 11-A (Science Wing)</option>
                <option value="Grade 12-A">Grade 12-A (Science Wing)</option>
              </select>
            </div>
          </CardHeader>
          <CardContent className="p-4 overflow-x-auto">
            <div className="min-w-[700px] space-y-3">
              {/* Period Headers */}
              <div className="grid grid-cols-8 gap-2 text-center text-[11px] font-bold text-slate-500 uppercase pb-1 border-b">
                <div className="text-left pl-2">Day</div>
                {timetablePeriods.map((p) => (
                  <div key={p.id}>
                    <div>{p.label}</div>
                    <div className="text-[9px] font-normal font-mono text-slate-400">{p.time}</div>
                  </div>
                ))}
              </div>

              {/* Day Rows */}
              {timetableDays.map((day) => (
                <div key={day} className="grid grid-cols-8 gap-2 items-center">
                  <div className="font-bold text-xs text-slate-800 pl-2">{day}</div>
                  {mockSchedule[day].map((cell, idx) => (
                    <div
                      key={idx}
                      className={cn(
                        "p-2 rounded-lg border text-left transition-all hover:shadow-xs",
                        cell.color
                      )}
                    >
                      <div className="font-bold text-[11px] truncate leading-tight">{cell.subject}</div>
                      <div className="text-[10px] text-slate-600 truncate mt-0.5">{cell.teacher}</div>
                      <div className="text-[9px] text-slate-400 font-mono mt-0.5">{cell.room}</div>
                    </div>
                  ))}
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      )}

      {/* TAB 3: Student Promotion & Rollover Engine */}
      {activeTab === "promotion" && (
        <div className="space-y-4">
          <Card className="border-slate-200/80 shadow-xs">
            <CardHeader className="p-4 pb-3 border-b border-slate-100 flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3">
              <div>
                <CardTitle className="text-sm font-bold text-slate-900">
                  Annual Session Rollover & Batch Student Promotion Engine
                </CardTitle>
                <CardDescription className="text-xs text-slate-500">
                  Transition from AY 2026-27 to AY 2027-28 • Automatic grade progression & alumni graduation
                </CardDescription>
              </div>
              <Button
                size="sm"
                onClick={handleExecutePromotion}
                disabled={isPromoting}
                className="h-8 gap-1.5 text-xs bg-emerald-600 hover:bg-emerald-700 text-white shadow-xs"
              >
                <Sparkles className="h-3.5 w-3.5" />
                {isPromoting ? "Executing Rollover..." : "Execute Batch Promotion"}
              </Button>
            </CardHeader>

            {promotionSuccess && (
              <div className="m-4 p-3 rounded-lg bg-emerald-50 border border-emerald-200 text-emerald-800 text-xs flex items-center gap-2">
                <CheckCircle2 className="h-4 w-4 text-emerald-600 shrink-0" />
                <span>
                  <strong>Promotion Completed!</strong> 18,349 students advanced to next grade; 1,412 Grade 12 students graduated to Alumni.
                </span>
              </div>
            )}

            <Table>
              <TableHeader>
                <TableRow className="border-slate-100 bg-slate-50/50 text-[11px]">
                  <TableHead className="font-semibold text-slate-700">Source Class (Current AY)</TableHead>
                  <TableHead className="font-semibold text-slate-700">Target Progression Class (Next AY)</TableHead>
                  <TableHead className="font-semibold text-slate-700 text-center">Enrolled</TableHead>
                  <TableHead className="font-semibold text-slate-700 text-center">Eligible for Promotion</TableHead>
                  <TableHead className="font-semibold text-slate-700 text-center">Repeat / Review</TableHead>
                  <TableHead className="font-semibold text-slate-700">Engine Status</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {promotionTiers.map((tier, idx) => (
                  <TableRow key={idx} className="text-xs hover:bg-slate-50/50 border-slate-100">
                    <TableCell className="font-bold text-slate-900">{tier.fromGrade}</TableCell>
                    <TableCell className="font-semibold text-blue-700">
                      <span className="flex items-center gap-1.5">
                        <ArrowRight className="h-3 w-3 text-slate-400" />
                        {tier.toGrade}
                      </span>
                    </TableCell>
                    <TableCell className="text-center font-mono font-medium text-slate-800">
                      {tier.enrolledCount.toLocaleString("en-IN")}
                    </TableCell>
                    <TableCell className="text-center font-mono font-bold text-emerald-600">
                      {tier.readyCount.toLocaleString("en-IN")}
                    </TableCell>
                    <TableCell className="text-center font-mono font-medium text-amber-600">
                      {tier.repeatCount}
                    </TableCell>
                    <TableCell>
                      <Badge className="bg-emerald-50 text-emerald-700 border-emerald-200 text-[10px]">
                        Simulation Ready
                      </Badge>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </Card>
        </div>
      )}
    </div>
  );
}
