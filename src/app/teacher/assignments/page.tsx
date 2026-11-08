"use client";

import * as React from "react";
import {
  BookOpen,
  Plus,
  Calendar,
  Clock,
  FileText,
  Upload,
  CheckCircle2,
  AlertCircle,
  Users,
  Search,
  Paperclip,
} from "lucide-react";
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Input } from "@/components/ui/input";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
  DialogFooter,
} from "@/components/ui/dialog";
import { mockTeacherAssignments, TeacherAssignment } from "@/lib/mock/teacher-mock";
import { cn } from "@/lib/utils";

export default function TeacherAssignmentsPage() {
  const [assignments, setAssignments] = React.useState<TeacherAssignment[]>(mockTeacherAssignments);
  const [activeTab, setActiveTab] = React.useState<"ALL" | "ACTIVE" | "PAST_DUE" | "EVALUATED">("ALL");
  const [isCreateOpen, setIsCreateOpen] = React.useState(false);

  // Form states
  const [newTitle, setNewTitle] = React.useState("");
  const [newSubject, setNewSubject] = React.useState("Advanced Mathematics");
  const [newClass, setNewClass] = React.useState("Class 10 - Section A");
  const [newDueDate, setNewDueDate] = React.useState("2026-11-15");
  const [newTotalMarks, setNewTotalMarks] = React.useState("25");

  const handleCreateAssignment = (e: React.FormEvent) => {
    e.preventDefault();
    if (!newTitle.trim()) return;

    const created: TeacherAssignment = {
      id: `asg-${Date.now()}`,
      title: newTitle,
      subject: newSubject,
      className: newClass.split(" - ")[0],
      sectionName: newClass.split(" - ")[1],
      assignedDate: new Date().toISOString().split("T")[0],
      dueDate: newDueDate,
      totalMarks: Number(newTotalMarks) || 20,
      totalSubmissions: 0,
      totalStudents: 38,
      evaluatedCount: 0,
      attachmentName: "Worksheet_Handout.pdf",
      status: "ACTIVE",
    };

    setAssignments([created, ...assignments]);
    setIsCreateOpen(false);
    setNewTitle("");
  };

  const filtered = assignments.filter((a) => {
    if (activeTab === "ALL") return true;
    return a.status === activeTab;
  });

  return (
    <div className="space-y-6 max-w-7xl mx-auto">
      {/* Header Banner */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 p-5 rounded-2xl bg-white dark:bg-slate-900 border border-slate-200/90 dark:border-slate-800 shadow-xs">
        <div className="space-y-1">
          <div className="flex items-center gap-2">
            <span className="text-xs font-bold text-emerald-600 dark:text-emerald-400 uppercase tracking-wider">
              Curricular Assignments
            </span>
            <span className="text-slate-300 dark:text-slate-700">•</span>
            <span className="text-xs font-medium text-slate-500">
              Student Submissions & Evaluation
            </span>
          </div>
          <h1 className="text-2xl font-bold tracking-tight text-slate-900 dark:text-slate-50 flex items-center gap-2">
            <BookOpen className="h-6 w-6 text-blue-600" />
            Homework & Tasks Dispatcher
          </h1>
          <p className="text-xs text-slate-500 dark:text-slate-400">
            Publish homework worksheets, review digital submissions, and enter assignment scores.
          </p>
        </div>

        <Button
          onClick={() => setIsCreateOpen(true)}
          className="h-9 text-xs font-semibold bg-emerald-600 hover:bg-emerald-700 text-white shadow-xs gap-1.5 px-3.5"
        >
          <Plus className="h-4 w-4" />
          Create New Homework
        </Button>
      </div>

      {/* Filter Tabs */}
      <div className="flex items-center gap-1.5 border-b border-slate-200 dark:border-slate-800 pb-2">
        {(["ALL", "ACTIVE", "PAST_DUE", "EVALUATED"] as const).map((tab) => (
          <button
            key={tab}
            onClick={() => setActiveTab(tab)}
            className={cn(
              "text-xs px-3 py-1.5 rounded-lg font-semibold transition-colors",
              activeTab === tab
                ? "bg-slate-900 text-white dark:bg-slate-100 dark:text-slate-900 shadow-xs"
                : "text-slate-500 hover:bg-slate-100 dark:hover:bg-slate-800"
            )}
          >
            {tab.replace("_", " ")}
          </button>
        ))}
      </div>

      {/* Assignment Cards List */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {filtered.map((item) => (
          <Card key={item.id} className="shadow-xs border-slate-200/80 flex flex-col justify-between">
            <CardHeader className="pb-3 space-y-1.5">
              <div className="flex items-center justify-between">
                <Badge
                  variant={
                    item.status === "ACTIVE"
                      ? "info"
                      : item.status === "EVALUATED"
                      ? "success"
                      : "warning"
                  }
                  className="text-[10px] font-bold"
                >
                  {item.status.replace("_", " ")}
                </Badge>
                <span className="text-[11px] font-mono font-semibold text-slate-500">
                  Max: {item.totalMarks} Marks
                </span>
              </div>
              <CardTitle className="text-sm font-bold text-slate-900 dark:text-slate-50 line-clamp-2">
                {item.title}
              </CardTitle>
              <CardDescription className="text-xs text-slate-500">
                {item.subject} • {item.className} {item.sectionName}
              </CardDescription>
            </CardHeader>

            <CardContent className="space-y-3 pt-0">
              {/* Submission Bar */}
              <div className="space-y-1">
                <div className="flex justify-between text-xs font-semibold">
                  <span>Submissions:</span>
                  <span className="font-mono text-slate-900 dark:text-slate-100">
                    {item.totalSubmissions} / {item.totalStudents} ({Math.round((item.totalSubmissions / item.totalStudents) * 100)}%)
                  </span>
                </div>
                <div className="w-full bg-slate-100 dark:bg-slate-800 h-2 rounded-full overflow-hidden">
                  <div
                    className="bg-emerald-500 h-full rounded-full transition-all"
                    style={{ width: `${(item.totalSubmissions / item.totalStudents) * 100}%` }}
                  />
                </div>
              </div>

              {/* Due Date & Attachment */}
              <div className="pt-2 border-t border-slate-100 dark:border-slate-800 flex items-center justify-between text-xs text-slate-500">
                <span className="flex items-center gap-1 font-mono text-[11px]">
                  <Calendar className="h-3 w-3 text-slate-400" />
                  Due: {item.dueDate}
                </span>
                {item.attachmentName && (
                  <span className="flex items-center gap-1 text-[11px] text-blue-600 dark:text-blue-400 font-medium">
                    <Paperclip className="h-3 w-3" />
                    PDF
                  </span>
                )}
              </div>

              <Button
                variant="outline"
                size="sm"
                className="w-full h-8 text-xs font-semibold border-slate-200 mt-2"
              >
                View Submissions & Grade
              </Button>
            </CardContent>
          </Card>
        ))}
      </div>

      {/* Create Assignment Modal */}
      <Dialog open={isCreateOpen} onOpenChange={setIsCreateOpen}>
        <DialogContent className="max-w-md">
          <DialogHeader>
            <DialogTitle className="text-base font-bold text-slate-900 flex items-center gap-2">
              <BookOpen className="h-5 w-5 text-emerald-600" />
              Publish Homework Task
            </DialogTitle>
            <DialogDescription className="text-xs text-slate-500">
              Create a homework task with due date, scoring rubric, and attachments for students.
            </DialogDescription>
          </DialogHeader>

          <form onSubmit={handleCreateAssignment} className="space-y-3.5">
            <div>
              <label className="text-xs font-bold text-slate-700 dark:text-slate-300">
                Assignment Title
              </label>
              <Input
                value={newTitle}
                onChange={(e) => setNewTitle(e.target.value)}
                placeholder="e.g. Chapter 5: Trigonometry Identities Set B"
                required
                className="mt-1 text-xs"
              />
            </div>

            <div className="grid grid-cols-2 gap-3">
              <div>
                <label className="text-xs font-bold text-slate-700 dark:text-slate-300">Class & Section</label>
                <select
                  value={newClass}
                  onChange={(e) => setNewClass(e.target.value)}
                  className="w-full mt-1 h-9 rounded-md border border-slate-200 text-xs px-2 bg-white dark:bg-slate-800"
                >
                  <option value="Class 10 - Section A">Class 10 - Section A</option>
                  <option value="Class 10 - Section B">Class 10 - Section B</option>
                  <option value="Class 11 - Section A">Class 11 - Section A</option>
                </select>
              </div>

              <div>
                <label className="text-xs font-bold text-slate-700 dark:text-slate-300">Subject</label>
                <Input value={newSubject} readOnly className="mt-1 text-xs bg-slate-50" />
              </div>
            </div>

            <div className="grid grid-cols-2 gap-3">
              <div>
                <label className="text-xs font-bold text-slate-700 dark:text-slate-300">Due Date</label>
                <Input
                  type="date"
                  value={newDueDate}
                  onChange={(e) => setNewDueDate(e.target.value)}
                  className="mt-1 text-xs"
                />
              </div>

              <div>
                <label className="text-xs font-bold text-slate-700 dark:text-slate-300">Max Marks</label>
                <Input
                  type="number"
                  value={newTotalMarks}
                  onChange={(e) => setNewTotalMarks(e.target.value)}
                  className="mt-1 text-xs"
                />
              </div>
            </div>

            <div className="p-3 rounded-lg border border-dashed border-slate-300 dark:border-slate-700 text-center">
              <Upload className="h-5 w-5 text-slate-400 mx-auto" />
              <p className="text-xs font-semibold text-slate-700 mt-1">Upload Handout Worksheet (PDF)</p>
              <p className="text-[10px] text-slate-400">Drag and drop or browse files</p>
            </div>

            <DialogFooter className="gap-2 sm:gap-0 pt-2">
              <Button type="button" variant="outline" size="sm" onClick={() => setIsCreateOpen(false)}>
                Cancel
              </Button>
              <Button type="submit" size="sm" className="bg-emerald-600 hover:bg-emerald-700 text-white font-semibold">
                Publish to Students
              </Button>
            </DialogFooter>
          </form>
        </DialogContent>
      </Dialog>
    </div>
  );
}

