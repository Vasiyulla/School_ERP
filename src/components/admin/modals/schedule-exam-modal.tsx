"use client";

import * as React from "react";
import { Award, Check, Calendar } from "lucide-react";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";

interface ScheduleExamModalProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  onSuccess?: (examData: any) => void;
}

export function ScheduleExamModal({
  open,
  onOpenChange,
  onSuccess,
}: ScheduleExamModalProps) {
  const [formData, setFormData] = React.useState({
    title: "",
    academicTerm: "AY 2026-27 • Semester 2",
    startDate: "",
    endDate: "",
    targetGrades: "Grades 10 & 12",
  });
  const [isSubmitting, setIsSubmitting] = React.useState(false);
  const [isDone, setIsDone] = React.useState(false);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setIsSubmitting(true);
    setTimeout(() => {
      setIsSubmitting(false);
      setIsDone(true);
      if (onSuccess) onSuccess(formData);
      setTimeout(() => {
        setIsDone(false);
        onOpenChange(false);
      }, 1000);
    }, 600);
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-md">
        <DialogHeader className="border-b pb-3">
          <div className="flex items-center gap-2.5">
            <div className="h-8 w-8 rounded-lg bg-blue-600 text-white flex items-center justify-center">
              <Award className="h-4.5 w-4.5" />
            </div>
            <div>
              <DialogTitle className="text-base font-bold text-slate-900">
                Schedule Academic Exam Term
              </DialogTitle>
              <DialogDescription className="text-xs text-slate-500">
                Assessment date sheets and automated report card initialization
              </DialogDescription>
            </div>
          </div>
        </DialogHeader>

        <form onSubmit={handleSubmit} className="space-y-4 py-2 text-xs">
          <div>
            <label className="text-slate-600 font-medium">Examination Title *</label>
            <Input
              required
              placeholder="e.g. CBSE Board Pre-Mock Assessment II"
              value={formData.title}
              onChange={(e) =>
                setFormData({ ...formData, title: e.target.value })
              }
              className="h-8 text-xs mt-1"
            />
          </div>

          <div>
            <label className="text-slate-600 font-medium">Academic Term</label>
            <Input
              disabled
              value={formData.academicTerm}
              className="h-8 text-xs mt-1 bg-slate-50 text-slate-500"
            />
          </div>

          <div className="grid grid-cols-2 gap-3">
            <div>
              <label className="text-slate-600 font-medium">Start Date *</label>
              <Input
                required
                type="date"
                value={formData.startDate}
                onChange={(e) =>
                  setFormData({ ...formData, startDate: e.target.value })
                }
                className="h-8 text-xs mt-1"
              />
            </div>
            <div>
              <label className="text-slate-600 font-medium">End Date *</label>
              <Input
                required
                type="date"
                value={formData.endDate}
                onChange={(e) =>
                  setFormData({ ...formData, endDate: e.target.value })
                }
                className="h-8 text-xs mt-1"
              />
            </div>
          </div>

          <div>
            <label className="text-slate-600 font-medium">Target Grade Levels</label>
            <select
              value={formData.targetGrades}
              onChange={(e) =>
                setFormData({ ...formData, targetGrades: e.target.value })
              }
              className="h-8 w-full rounded-md border border-slate-200 bg-white px-2 mt-1 text-xs"
            >
              <option value="Grades 10 & 12">Grades 10 & 12 (Board Examination Classes)</option>
              <option value="Grades 9 & 11">Grades 9 & 11 (Senior Secondary)</option>
              <option value="Grades 6 to 8">Grades 6 to 8 (Middle Wing)</option>
              <option value="All Grades">All Grades (School-wide Assessment)</option>
            </select>
          </div>

          <DialogFooter className="border-t pt-3">
            <Button
              type="button"
              variant="outline"
              size="sm"
              onClick={() => onOpenChange(false)}
              className="h-8 text-xs"
            >
              Cancel
            </Button>
            <Button
              type="submit"
              size="sm"
              disabled={isSubmitting}
              className="h-8 text-xs bg-blue-600 hover:bg-blue-700 text-white gap-1.5"
            >
              {isDone ? (
                <>
                  <Check className="h-3.5 w-3.5" /> Exam Scheduled!
                </>
              ) : (
                <>
                  <Calendar className="h-3.5 w-3.5" /> Publish Date Sheet
                </>
              )}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}

