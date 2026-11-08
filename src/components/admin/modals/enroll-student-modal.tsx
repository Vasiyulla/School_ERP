"use client";

import * as React from "react";
import {
  UserPlus,
  Calendar,
  Phone,
  Mail,
  User,
  Check,
  ShieldCheck,
  GraduationCap,
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
import { Input } from "@/components/ui/input";
import { Badge } from "@/components/ui/badge";

interface EnrollStudentModalProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  onSuccess?: (studentData: any) => void;
}

export function EnrollStudentModal({
  open,
  onOpenChange,
  onSuccess,
}: EnrollStudentModalProps) {
  const [formData, setFormData] = React.useState({
    firstName: "",
    lastName: "",
    dob: "",
    gender: "Male",
    grade: "Grade 10",
    section: "Section A",
    guardianName: "",
    guardianPhone: "",
    guardianEmail: "",
    tcOriginalCollected: true,
    lockerLocation: "Locker B-12 / File #2026-0914",
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
      <DialogContent className="max-w-2xl max-h-[90vh] overflow-y-auto">
        <DialogHeader className="border-b pb-3">
          <div className="flex items-center gap-2.5">
            <div className="h-8 w-8 rounded-lg bg-blue-600 text-white flex items-center justify-center">
              <UserPlus className="h-4.5 w-4.5" />
            </div>
            <div>
              <DialogTitle className="text-base font-bold text-slate-900">
                Enroll New Student
              </DialogTitle>
              <DialogDescription className="text-xs text-slate-500">
                Direct enrollment with automated UUID, roll number, and physical document custody index
              </DialogDescription>
            </div>
          </div>
        </DialogHeader>

        <form onSubmit={handleSubmit} className="space-y-4 py-3 text-xs">
          {/* Section 1: Student Information */}
          <div className="space-y-2">
            <h4 className="font-bold text-slate-800 uppercase tracking-wider text-[10px]">
              1. Student Basic Details
            </h4>
            <div className="grid grid-cols-2 gap-3">
              <div>
                <label className="text-slate-600 font-medium">First Name *</label>
                <Input
                  required
                  placeholder="e.g. Arjun"
                  value={formData.firstName}
                  onChange={(e) =>
                    setFormData({ ...formData, firstName: e.target.value })
                  }
                  className="h-8 text-xs mt-1"
                />
              </div>
              <div>
                <label className="text-slate-600 font-medium">Last Name *</label>
                <Input
                  required
                  placeholder="e.g. Verma"
                  value={formData.lastName}
                  onChange={(e) =>
                    setFormData({ ...formData, lastName: e.target.value })
                  }
                  className="h-8 text-xs mt-1"
                />
              </div>
            </div>

            <div className="grid grid-cols-3 gap-3">
              <div>
                <label className="text-slate-600 font-medium">Date of Birth *</label>
                <Input
                  required
                  type="date"
                  value={formData.dob}
                  onChange={(e) =>
                    setFormData({ ...formData, dob: e.target.value })
                  }
                  className="h-8 text-xs mt-1"
                />
              </div>
              <div>
                <label className="text-slate-600 font-medium">Gender</label>
                <select
                  value={formData.gender}
                  onChange={(e) =>
                    setFormData({ ...formData, gender: e.target.value })
                  }
                  className="h-8 w-full rounded-md border border-slate-200 bg-white px-2 mt-1 text-xs"
                >
                  <option value="Male">Male</option>
                  <option value="Female">Female</option>
                  <option value="Other">Other</option>
                </select>
              </div>
              <div>
                <label className="text-slate-600 font-medium">Class & Section *</label>
                <select
                  value={`${formData.grade} • ${formData.section}`}
                  onChange={(e) => {
                    const [g, s] = e.target.value.split(" • ");
                    setFormData({ ...formData, grade: g, section: s });
                  }}
                  className="h-8 w-full rounded-md border border-slate-200 bg-white px-2 mt-1 text-xs"
                >
                  <option value="Grade 10 • Section A">Grade 10-A (CBSE)</option>
                  <option value="Grade 10 • Section B">Grade 10-B (CBSE)</option>
                  <option value="Grade 11 • Section A">Grade 11-A (Science)</option>
                  <option value="Grade 11 • Section B">Grade 11-B (Commerce)</option>
                  <option value="Grade 12 • Section A">Grade 12-A (Science)</option>
                </select>
              </div>
            </div>
          </div>

          {/* Section 2: Guardian Details */}
          <div className="space-y-2 border-t pt-3">
            <h4 className="font-bold text-slate-800 uppercase tracking-wider text-[10px]">
              2. Guardian & Emergency Contacts
            </h4>
            <div className="grid grid-cols-3 gap-3">
              <div>
                <label className="text-slate-600 font-medium">Primary Guardian Name *</label>
                <Input
                  required
                  placeholder="e.g. Ramesh Verma"
                  value={formData.guardianName}
                  onChange={(e) =>
                    setFormData({ ...formData, guardianName: e.target.value })
                  }
                  className="h-8 text-xs mt-1"
                />
              </div>
              <div>
                <label className="text-slate-600 font-medium">Contact Phone *</label>
                <Input
                  required
                  placeholder="+91 98200 00000"
                  value={formData.guardianPhone}
                  onChange={(e) =>
                    setFormData({ ...formData, guardianPhone: e.target.value })
                  }
                  className="h-8 text-xs mt-1"
                />
              </div>
              <div>
                <label className="text-slate-600 font-medium">Email Address</label>
                <Input
                  type="email"
                  placeholder="parent@example.com"
                  value={formData.guardianEmail}
                  onChange={(e) =>
                    setFormData({ ...formData, guardianEmail: e.target.value })
                  }
                  className="h-8 text-xs mt-1"
                />
              </div>
            </div>
          </div>

          {/* Section 3: Physical Document Custody */}
          <div className="space-y-2 border-t pt-3">
            <h4 className="font-bold text-slate-800 uppercase tracking-wider text-[10px]">
              3. Physical Document Custody Verification
            </h4>
            <div className="p-3 rounded-lg bg-slate-50 border border-slate-200/80 space-y-2">
              <label className="flex items-center gap-2 cursor-pointer font-medium text-slate-700">
                <input
                  type="checkbox"
                  checked={formData.tcOriginalCollected}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      tcOriginalCollected: e.target.checked,
                    })
                  }
                  className="rounded border-slate-300"
                />
                <span>Transfer Certificate (TC Original) received and retained in school custody</span>
              </label>

              <div className="grid grid-cols-2 gap-3 pt-1">
                <div>
                  <label className="text-slate-500 font-medium text-[11px]">Physical Locker / File Tag</label>
                  <Input
                    value={formData.lockerLocation}
                    onChange={(e) =>
                      setFormData({
                        ...formData,
                        lockerLocation: e.target.value,
                      })
                    }
                    className="h-7 text-xs mt-0.5 bg-white"
                  />
                </div>
                <div className="flex items-end">
                  <Badge className="bg-emerald-50 text-emerald-700 border-emerald-200 text-[10px]">
                    Verified by Admin Sunita Sharma
                  </Badge>
                </div>
              </div>
            </div>
          </div>

          <DialogFooter className="border-t pt-3 sm:justify-between">
            <span className="text-[11px] text-slate-400">
              Admission # will be auto-generated in format ADM-2026-XXXX.
            </span>
            <div className="flex gap-2">
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
                    <Check className="h-3.5 w-3.5" /> Enrolled Successfully!
                  </>
                ) : (
                  <>
                    <UserPlus className="h-3.5 w-3.5" /> Complete Enrollment
                  </>
                )}
              </Button>
            </div>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}

