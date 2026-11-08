"use client";

import * as React from "react";
import {
  Users,
  Search,
  Plus,
  Award,
  AlertTriangle,
  FolderLock,
  Phone,
  Eye,
  CheckCircle2,
  Calendar,
  Sparkles,
  ShieldCheck,
  Smartphone,
} from "lucide-react";
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
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
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
  DialogFooter,
} from "@/components/ui/dialog";
import {
  mockClassRoster,
  mockBehavioralObservations,
  BehavioralObservation,
  ClassStudentAttendance,
} from "@/lib/mock/teacher-mock";
import { cn } from "@/lib/utils";

export default function TeacherStudentsPage() {
  const [students, setStudents] = React.useState<ClassStudentAttendance[]>(mockClassRoster);
  const [observations, setObservations] = React.useState<BehavioralObservation[]>(mockBehavioralObservations);
  const [searchQuery, setSearchQuery] = React.useState("");
  const [isLogObservationOpen, setIsLogObservationOpen] = React.useState(false);

  // New observation state
  const [selectedStudentId, setSelectedStudentId] = React.useState(mockClassRoster[0].id);
  const [obsType, setObsType] = React.useState<"MERIT" | "DISCIPLINARY" | "ACADEMIC_INTERVENTION">("MERIT");
  const [obsTitle, setObsTitle] = React.useState("");
  const [obsNotes, setObsNotes] = React.useState("");
  const [visibleToParent, setVisibleToParent] = React.useState(true);

  const handleAddObservation = (e: React.FormEvent) => {
    e.preventDefault();
    if (!obsTitle.trim()) return;

    const studentObj = students.find((s) => s.id === selectedStudentId);
    const newEntry: BehavioralObservation = {
      id: `obs-${Date.now()}`,
      studentId: selectedStudentId,
      studentName: studentObj?.fullName || "Student",
      date: new Date().toISOString().split("T")[0],
      type: obsType,
      title: obsTitle,
      notes: obsNotes,
      visibleToParent,
      status: "LOGGED",
    };

    setObservations([newEntry, ...observations]);
    setIsLogObservationOpen(false);
    setObsTitle("");
    setObsNotes("");
  };

  const filteredStudents = students.filter(
    (s) =>
      s.fullName.toLowerCase().includes(searchQuery.toLowerCase()) ||
      s.admissionNumber.toLowerCase().includes(searchQuery.toLowerCase()) ||
      s.rollNumber.toString().includes(searchQuery)
  );

  return (
    <div className="space-y-6 max-w-7xl mx-auto">
      {/* Top Banner */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 p-5 rounded-2xl bg-white dark:bg-slate-900 border border-slate-200/90 dark:border-slate-800 shadow-xs">
        <div className="space-y-1">
          <div className="flex items-center gap-2">
            <span className="text-xs font-bold text-emerald-600 dark:text-emerald-400 uppercase tracking-wider">
              Class 10 - Section A Dossiers
            </span>
            <span className="text-slate-300 dark:text-slate-700">•</span>
            <span className="text-xs font-medium text-slate-500">
              38 Enrolled Students
            </span>
          </div>
          <h1 className="text-2xl font-bold tracking-tight text-slate-900 dark:text-slate-50 flex items-center gap-2">
            <Users className="h-6 w-6 text-emerald-600" />
            Class Student Master & Behavioral Log
          </h1>
          <p className="text-xs text-slate-500 dark:text-slate-400">
            Student directory, guardian contacts, document physical locker tags, and anecdotal behavioral observations.
          </p>
        </div>

        <Button
          onClick={() => setIsLogObservationOpen(true)}
          className="h-9 text-xs font-semibold bg-emerald-600 hover:bg-emerald-700 text-white shadow-xs gap-1.5 px-3.5"
        >
          <Plus className="h-4 w-4" />
          Log Behavioral Note
        </Button>
      </div>

      {/* Tabs Layout */}
      <Tabs defaultValue="roster" className="space-y-6">
        <TabsList className="bg-slate-100 dark:bg-slate-800 p-1 rounded-xl h-11 border border-slate-200 dark:border-slate-700">
          <TabsTrigger value="roster" className="text-xs font-semibold rounded-lg data-[state=active]:bg-white dark:data-[state=active]:bg-slate-900 data-[state=active]:shadow-xs flex items-center gap-1.5 px-4 py-2">
            <Users className="h-3.5 w-3.5 text-emerald-600" />
            <span>Class 10-A Roster & Custody</span>
          </TabsTrigger>
          <TabsTrigger value="observations" className="text-xs font-semibold rounded-lg data-[state=active]:bg-white dark:data-[state=active]:bg-slate-900 data-[state=active]:shadow-xs flex items-center gap-1.5 px-4 py-2">
            <Award className="h-3.5 w-3.5 text-purple-600" />
            <span>Behavioral & Anecdotal Log</span>
          </TabsTrigger>
        </TabsList>

        {/* Tab 1: Student Roster */}
        <TabsContent value="roster" className="space-y-4 m-0">
          <Card className="shadow-xs border-slate-200/80">
            <CardHeader className="pb-3 border-b border-slate-100 dark:border-slate-800 flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3">
              <div className="relative w-full sm:w-72">
                <Search className="absolute left-2.5 top-1/2 -translate-y-1/2 h-3.5 w-3.5 text-slate-400" />
                <Input
                  placeholder="Search student or roll #..."
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  className="pl-8 h-8 text-xs bg-slate-50 dark:bg-slate-800 border-slate-200"
                />
              </div>
              <Badge variant="outline" className="text-xs font-mono border-slate-200">
                Total Class Strength: 38
              </Badge>
            </CardHeader>
            <CardContent className="p-0">
              <div className="overflow-x-auto">
                <Table>
                  <TableHeader className="bg-slate-50/90 dark:bg-slate-800/60">
                    <TableRow>
                      <TableHead className="w-16 text-center text-xs font-bold text-slate-700 dark:text-slate-300">Roll #</TableHead>
                      <TableHead className="w-48 text-xs font-bold text-slate-700 dark:text-slate-300">Student Name</TableHead>
                      <TableHead className="w-36 text-xs font-bold text-slate-700 dark:text-slate-300">Admission No</TableHead>
                      <TableHead className="w-48 text-xs font-bold text-slate-700 dark:text-slate-300">Guardian Contact</TableHead>
                      <TableHead className="w-32 text-center text-xs font-bold text-slate-700 dark:text-slate-300">Attendance %</TableHead>
                      <TableHead className="w-44 text-xs font-bold text-slate-700 dark:text-slate-300">Document Custody</TableHead>
                      <TableHead className="text-right text-xs font-bold text-slate-700 dark:text-slate-300">Locker Index</TableHead>
                    </TableRow>
                  </TableHeader>
                  <TableBody>
                    {filteredStudents.map((s) => (
                      <TableRow key={s.id} className="hover:bg-slate-50/50 dark:hover:bg-slate-800/30">
                        <TableCell className="text-center font-mono font-bold text-xs py-3">
                          #{s.rollNumber}
                        </TableCell>
                        <TableCell className="font-bold text-xs text-slate-900 dark:text-slate-100 py-3">
                          {s.fullName}
                        </TableCell>
                        <TableCell className="font-mono text-xs text-slate-500 py-3">
                          {s.admissionNumber}
                        </TableCell>
                        <TableCell className="py-3 text-xs">
                          <div className="flex flex-col">
                            <span className="font-medium text-slate-800 dark:text-slate-200">{s.guardianName}</span>
                            <span className="text-[10px] text-slate-500 flex items-center gap-1 font-mono">
                              <Smartphone className="h-2.5 w-2.5" /> {s.guardianPhone}
                            </span>
                          </div>
                        </TableCell>
                        <TableCell className="text-center py-3 font-mono font-semibold text-xs">
                          <span className={cn(s.attendancePct >= 90 ? "text-emerald-600" : "text-amber-600")}>
                            {s.attendancePct}%
                          </span>
                        </TableCell>
                        <TableCell className="py-3">
                          <Badge
                            variant={
                              s.documentStatus === "COMPLETE"
                                ? "success"
                                : "warning"
                            }
                            className="text-[10px] font-medium"
                          >
                            <FolderLock className="h-3 w-3 mr-1" />
                            {s.documentStatus === "COMPLETE"
                              ? "Originals Verified"
                              : s.documentStatus === "PENDING_TC"
                              ? "Pending TC Original"
                              : "Pending Medical Cert"}
                          </Badge>
                        </TableCell>
                        <TableCell className="text-right py-3 font-mono text-xs text-slate-500">
                          {s.lockerIndex}
                        </TableCell>
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        {/* Tab 2: Behavioral Observations */}
        <TabsContent value="observations" className="space-y-4 m-0">
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {observations.map((obs) => (
              <Card key={obs.id} className="shadow-xs border-slate-200/80 p-4 space-y-3">
                <div className="flex items-center justify-between">
                  <Badge
                    variant={
                      obs.type === "MERIT"
                        ? "success"
                        : obs.type === "DISCIPLINARY"
                        ? "destructive"
                        : "info"
                    }
                    className="text-[10px] font-bold"
                  >
                    {obs.type}
                  </Badge>
                  <span className="text-[11px] font-mono text-slate-400">{obs.date}</span>
                </div>

                <div>
                  <h3 className="text-sm font-bold text-slate-900 dark:text-slate-100">
                    {obs.studentName}
                  </h3>
                  <p className="text-xs font-semibold text-slate-700 dark:text-slate-300 mt-0.5">
                    {obs.title}
                  </p>
                  <p className="text-xs text-slate-500 dark:text-slate-400 mt-1 leading-relaxed">
                    {obs.notes}
                  </p>
                </div>

                <div className="pt-2 border-t border-slate-100 dark:border-slate-800 flex items-center justify-between text-[11px]">
                  <span className={cn("font-medium", obs.visibleToParent ? "text-emerald-600" : "text-slate-400")}>
                    {obs.visibleToParent ? "Visible on Parent App" : "Internal Staff Only"}
                  </span>
                  <Badge variant="outline" className="text-[10px] border-slate-200">
                    {obs.status.replace("_", " ")}
                  </Badge>
                </div>
              </Card>
            ))}
          </div>
        </TabsContent>
      </Tabs>

      {/* Log Observation Modal */}
      <Dialog open={isLogObservationOpen} onOpenChange={setIsLogObservationOpen}>
        <DialogContent className="max-w-md">
          <DialogHeader>
            <DialogTitle className="text-base font-bold text-slate-900 flex items-center gap-2">
              <Award className="h-5 w-5 text-purple-600" />
              Log Anecdotal Observation
            </DialogTitle>
            <DialogDescription className="text-xs text-slate-500">
              Record a student merit, academic intervention, or disciplinary remark.
            </DialogDescription>
          </DialogHeader>

          <form onSubmit={handleAddObservation} className="space-y-3.5">
            <div>
              <label className="text-xs font-bold text-slate-700 dark:text-slate-300">Select Student</label>
              <select
                value={selectedStudentId}
                onChange={(e) => setSelectedStudentId(e.target.value)}
                className="w-full mt-1 h-9 rounded-md border border-slate-200 text-xs px-2 bg-white dark:bg-slate-800"
              >
                {students.map((s) => (
                  <option key={s.id} value={s.id}>
                    #{s.rollNumber} - {s.fullName} ({s.admissionNumber})
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label className="text-xs font-bold text-slate-700 dark:text-slate-300">Observation Type</label>
              <div className="grid grid-cols-3 gap-2 mt-1">
                {(["MERIT", "DISCIPLINARY", "ACADEMIC_INTERVENTION"] as const).map((t) => (
                  <button
                    type="button"
                    key={t}
                    onClick={() => setObsType(t)}
                    className={cn(
                      "py-1.5 px-2 rounded-lg text-xs font-semibold border text-center transition-all",
                      obsType === t
                        ? "bg-purple-50 text-purple-700 border-purple-300 dark:bg-purple-950 dark:text-purple-300 shadow-xs"
                        : "border-slate-200 text-slate-600 hover:bg-slate-50 dark:border-slate-700"
                    )}
                  >
                    {t.split("_")[0]}
                  </button>
                ))}
              </div>
            </div>

            <div>
              <label className="text-xs font-bold text-slate-700 dark:text-slate-300">Headline / Title</label>
              <Input
                value={obsTitle}
                onChange={(e) => setObsTitle(e.target.value)}
                placeholder="e.g. Outstanding Performance in Math Olympiad"
                required
                className="mt-1 text-xs"
              />
            </div>

            <div>
              <label className="text-xs font-bold text-slate-700 dark:text-slate-300">Detailed Notes</label>
              <textarea
                value={obsNotes}
                onChange={(e) => setObsNotes(e.target.value)}
                placeholder="Context, specific actions observed, recommendations..."
                rows={3}
                className="w-full mt-1 p-2 rounded-md border border-slate-200 text-xs bg-white dark:bg-slate-800"
              />
            </div>

            <div className="flex items-center gap-2 pt-1">
              <input
                type="checkbox"
                id="visParent"
                checked={visibleToParent}
                onChange={(e) => setVisibleToParent(e.target.checked)}
                className="h-4 w-4 rounded border-slate-300 text-purple-600 focus:ring-purple-500"
              />
              <label htmlFor="visParent" className="text-xs text-slate-700 dark:text-slate-300 font-medium">
                Make visible to parent on Parent Portal & Mobile App
              </label>
            </div>

            <DialogFooter className="gap-2 sm:gap-0 pt-2">
              <Button type="button" variant="outline" size="sm" onClick={() => setIsLogObservationOpen(false)}>
                Cancel
              </Button>
              <Button type="submit" size="sm" className="bg-emerald-600 hover:bg-emerald-700 text-white font-semibold">
                Save Observation
              </Button>
            </DialogFooter>
          </form>
        </DialogContent>
      </Dialog>
    </div>
  );
}

