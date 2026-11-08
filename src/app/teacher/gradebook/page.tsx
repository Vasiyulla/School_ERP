"use client";

import * as React from "react";
import {
  Award,
  Save,
  Send,
  Lock,
  Download,
  CheckCircle2,
  AlertTriangle,
  Sparkles,
  Calculator,
  Search,
  Filter,
} from "lucide-react";
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from "@/components/ui/card";
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
import { mockGradebookEntries, StudentGradeEntry } from "@/lib/mock/teacher-mock";
import { cn } from "@/lib/utils";

// CBSE 9-point grading scale calculation
function calculateGradeBand(total: number): string {
  if (total >= 91) return "A1";
  if (total >= 81) return "A2";
  if (total >= 71) return "B1";
  if (total >= 61) return "B2";
  if (total >= 51) return "C1";
  if (total >= 41) return "C2";
  if (total >= 33) return "D";
  return "E";
}

export default function TeacherGradebookPage() {
  const [entries, setEntries] = React.useState<StudentGradeEntry[]>(mockGradebookEntries);
  const [activeClass, setActiveClass] = React.useState("Class 10-A • Mathematics");
  const [activeTerm, setActiveTerm] = React.useState("Term 1 Mid-Term Examination");
  const [searchQuery, setSearchQuery] = React.useState("");
  const [isSaved, setIsSaved] = React.useState(false);
  const [isSubmitted, setIsSubmitted] = React.useState(false);

  const handleScoreChange = (
    studentId: string,
    field: "theoryScore" | "practicalScore" | "internalScore" | "remarks",
    val: string | number
  ) => {
    if (isSubmitted) return;
    setEntries((prev) =>
      prev.map((entry) => {
        if (entry.studentId !== studentId) return entry;

        let updated = { ...entry };
        if (field === "theoryScore") {
          const num = Math.min(80, Math.max(0, Number(val) || 0));
          updated.theoryScore = num;
        } else if (field === "practicalScore") {
          const num = Math.min(20, Math.max(0, Number(val) || 0));
          updated.practicalScore = num;
        } else if (field === "internalScore") {
          const num = Math.min(10, Math.max(0, Number(val) || 0));
          updated.internalScore = num;
        } else if (field === "remarks") {
          updated.remarks = String(val);
        }

        // Recompute normalized total (Theory + Practical + Internal = 110 max, normalized /110 * 100)
        const rawTotal = updated.theoryScore + updated.practicalScore + updated.internalScore;
        const normalized = Math.round((rawTotal / 110) * 100);
        updated.totalScore = normalized;
        updated.gradeBand = calculateGradeBand(normalized);

        return updated;
      })
    );
  };

  const handleSaveDraft = () => {
    setIsSaved(true);
    setTimeout(() => setIsSaved(false), 3000);
  };

  const handleSubmitLock = () => {
    setIsSubmitted(true);
  };

  const filteredEntries = entries.filter(
    (e) =>
      e.fullName.toLowerCase().includes(searchQuery.toLowerCase()) ||
      e.rollNumber.toString().includes(searchQuery)
  );

  const classAverage = (
    entries.reduce((acc, curr) => acc + curr.totalScore, 0) / entries.length
  ).toFixed(1);

  return (
    <div className="space-y-6 max-w-7xl mx-auto">
      {/* Title & Term Context */}
      <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4 p-5 rounded-2xl bg-white dark:bg-slate-900 border border-slate-200/90 dark:border-slate-800 shadow-xs">
        <div className="space-y-1">
          <div className="flex items-center gap-2">
            <span className="text-xs font-bold text-emerald-600 dark:text-emerald-400 uppercase tracking-wider">
              Examinations & Assessment Engine
            </span>
            <span className="text-slate-300 dark:text-slate-700">•</span>
            <span className="text-xs font-medium text-slate-500">
              CBSE Continuous Evaluation Scale
            </span>
          </div>
          <h1 className="text-2xl font-bold tracking-tight text-slate-900 dark:text-slate-50 flex items-center gap-2">
            <Award className="h-6 w-6 text-purple-600" />
            Class Gradebook & Marks Entry
          </h1>
          <p className="text-xs text-slate-500 dark:text-slate-400">
            Multi-component evaluation: Theory (80), Practical (20), Internal Assessment (10) with automatic 9-point grade band translation.
          </p>
        </div>

        <div className="flex flex-wrap items-center gap-2.5">
          {isSaved && (
            <Badge variant="success" className="text-xs py-1 px-2.5 animate-in fade-in">
              <CheckCircle2 className="h-3 w-3 mr-1" />
              Draft Auto-Saved
            </Badge>
          )}

          <Button
            onClick={handleSaveDraft}
            disabled={isSubmitted}
            variant="outline"
            className="h-9 text-xs font-semibold border-slate-200 gap-1.5"
          >
            <Save className="h-3.5 w-3.5" />
            Save Draft
          </Button>

          <Button
            onClick={handleSubmitLock}
            disabled={isSubmitted}
            className="h-9 text-xs font-semibold bg-emerald-600 hover:bg-emerald-700 text-white shadow-xs gap-1.5 px-4"
          >
            <Lock className="h-3.5 w-3.5" />
            {isSubmitted ? "Locked & Submitted" : "Submit for Exam Lock"}
          </Button>
        </div>
      </div>

      {/* Selectors & Metrics Bar */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        {/* Class Selection Dropdown */}
        <Card className="shadow-xs border-slate-200/80 p-3.5 flex flex-col justify-between">
          <label className="text-[10px] font-bold uppercase text-slate-400">Assigned Class & Subject</label>
          <select
            value={activeClass}
            onChange={(e) => setActiveClass(e.target.value)}
            className="text-xs font-bold text-slate-900 dark:text-slate-100 bg-transparent border-0 p-0 focus:ring-0 cursor-pointer mt-1"
          >
            <option value="Class 10-A • Mathematics">Class 10-A • Mathematics (38 students)</option>
            <option value="Class 10-B • Mathematics">Class 10-B • Standard Math (40 students)</option>
            <option value="Class 11-A • Calculus">Class 11-A • Calculus & Algebra (42 students)</option>
          </select>
        </Card>

        {/* Term Selection */}
        <Card className="shadow-xs border-slate-200/80 p-3.5 flex flex-col justify-between">
          <label className="text-[10px] font-bold uppercase text-slate-400">Examination Term</label>
          <select
            value={activeTerm}
            onChange={(e) => setActiveTerm(e.target.value)}
            className="text-xs font-bold text-slate-900 dark:text-slate-100 bg-transparent border-0 p-0 focus:ring-0 cursor-pointer mt-1"
          >
            <option value="Term 1 Mid-Term Examination">Term 1 Mid-Term Examination (Active)</option>
            <option value="Unit Assessment 2">Unit Assessment 2</option>
            <option value="Unit Assessment 1">Unit Assessment 1 (Archived)</option>
          </select>
        </Card>

        {/* Class Average Score */}
        <Card className="shadow-xs border-slate-200/80 p-3.5 flex items-center justify-between">
          <div>
            <p className="text-[10px] font-bold uppercase text-slate-400">Class Average</p>
            <p className="text-xl font-bold font-mono text-emerald-600 mt-0.5">{classAverage}%</p>
          </div>
          <div className="text-right">
            <Badge variant="success" className="text-xs font-bold">Grade A2</Badge>
            <p className="text-[10px] text-slate-400 mt-0.5">High Performance</p>
          </div>
        </Card>

        {/* Completion Progress */}
        <Card className="shadow-xs border-slate-200/80 p-3.5 flex items-center justify-between">
          <div>
            <p className="text-[10px] font-bold uppercase text-slate-400">Evaluation Progress</p>
            <p className="text-xl font-bold font-mono text-slate-900 dark:text-slate-100 mt-0.5">
              12 / 12 <span className="text-xs font-normal text-slate-500">Graded</span>
            </p>
          </div>
          <div className="h-9 w-9 rounded-full bg-emerald-50 text-emerald-600 flex items-center justify-center font-bold text-xs">
            100%
          </div>
        </Card>
      </div>

      {/* Gradebook Table */}
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

          <div className="flex items-center gap-2">
            <span className="text-xs text-slate-500 font-medium">CBSE Weighting:</span>
            <Badge variant="outline" className="text-[10px] font-mono border-slate-200">
              Theory: 80M • Practical: 20M • Internal: 10M
            </Badge>
          </div>
        </CardHeader>
        <CardContent className="p-0">
          <div className="overflow-x-auto">
            <Table>
              <TableHeader className="bg-slate-50/90 dark:bg-slate-800/60">
                <TableRow>
                  <TableHead className="w-14 text-center text-xs font-bold text-slate-700 dark:text-slate-300">
                    Roll #
                  </TableHead>
                  <TableHead className="w-48 text-xs font-bold text-slate-700 dark:text-slate-300">
                    Student Name
                  </TableHead>
                  <TableHead className="w-28 text-center text-xs font-bold text-slate-700 dark:text-slate-300">
                    Theory (80)
                  </TableHead>
                  <TableHead className="w-28 text-center text-xs font-bold text-slate-700 dark:text-slate-300">
                    Practical (20)
                  </TableHead>
                  <TableHead className="w-28 text-center text-xs font-bold text-slate-700 dark:text-slate-300">
                    Internal (10)
                  </TableHead>
                  <TableHead className="w-24 text-center text-xs font-bold text-slate-700 dark:text-slate-300">
                    Total (%)
                  </TableHead>
                  <TableHead className="w-20 text-center text-xs font-bold text-slate-700 dark:text-slate-300">
                    Grade
                  </TableHead>
                  <TableHead className="text-xs font-bold text-slate-700 dark:text-slate-300">
                    Teacher Feedback & Anecdotal Remarks
                  </TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {filteredEntries.map((student) => (
                  <TableRow key={student.studentId} className="hover:bg-slate-50/50 dark:hover:bg-slate-800/30">
                    <TableCell className="text-center font-mono font-bold text-xs py-2.5">
                      #{student.rollNumber}
                    </TableCell>
                    <TableCell className="font-bold text-xs text-slate-900 dark:text-slate-100 py-2.5">
                      {student.fullName}
                    </TableCell>

                    {/* Theory Input */}
                    <TableCell className="py-2.5 text-center">
                      <Input
                        type="number"
                        min={0}
                        max={80}
                        value={student.theoryScore}
                        disabled={isSubmitted}
                        onChange={(e) =>
                          handleScoreChange(student.studentId, "theoryScore", e.target.value)
                        }
                        className="h-8 w-20 text-center font-mono text-xs mx-auto bg-slate-50 dark:bg-slate-800"
                      />
                    </TableCell>

                    {/* Practical Input */}
                    <TableCell className="py-2.5 text-center">
                      <Input
                        type="number"
                        min={0}
                        max={20}
                        value={student.practicalScore}
                        disabled={isSubmitted}
                        onChange={(e) =>
                          handleScoreChange(student.studentId, "practicalScore", e.target.value)
                        }
                        className="h-8 w-20 text-center font-mono text-xs mx-auto bg-slate-50 dark:bg-slate-800"
                      />
                    </TableCell>

                    {/* Internal Input */}
                    <TableCell className="py-2.5 text-center">
                      <Input
                        type="number"
                        min={0}
                        max={10}
                        value={student.internalScore}
                        disabled={isSubmitted}
                        onChange={(e) =>
                          handleScoreChange(student.studentId, "internalScore", e.target.value)
                        }
                        className="h-8 w-20 text-center font-mono text-xs mx-auto bg-slate-50 dark:bg-slate-800"
                      />
                    </TableCell>

                    {/* Total % */}
                    <TableCell className="py-2.5 text-center font-mono font-bold text-xs">
                      <span
                        className={cn(
                          student.totalScore >= 80
                            ? "text-emerald-600 font-bold"
                            : student.totalScore >= 60
                            ? "text-blue-600"
                            : "text-amber-600"
                        )}
                      >
                        {student.totalScore}%
                      </span>
                    </TableCell>

                    {/* Grade Band Badge */}
                    <TableCell className="py-2.5 text-center">
                      <Badge
                        variant={
                          student.gradeBand === "A1" || student.gradeBand === "A2"
                            ? "success"
                            : student.gradeBand === "B1" || student.gradeBand === "B2"
                            ? "info"
                            : "warning"
                        }
                        className="text-[11px] font-mono font-bold"
                      >
                        {student.gradeBand}
                      </Badge>
                    </TableCell>

                    {/* Remarks Input */}
                    <TableCell className="py-2.5">
                      <Input
                        type="text"
                        value={student.remarks}
                        disabled={isSubmitted}
                        onChange={(e) =>
                          handleScoreChange(student.studentId, "remarks", e.target.value)
                        }
                        placeholder="Add constructive feedback..."
                        className="h-8 text-xs bg-slate-50 dark:bg-slate-800 border-slate-200"
                      />
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}

