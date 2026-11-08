"use client";

import * as React from "react";
import {
  Briefcase,
  Check,
  Building,
  Mail,
  Phone,
  ShieldCheck,
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

interface AddStaffModalProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  onSuccess?: (staffData: any) => void;
}

export function AddStaffModal({
  open,
  onOpenChange,
  onSuccess,
}: AddStaffModalProps) {
  const [formData, setFormData] = React.useState({
    fullName: "",
    department: "Science",
    designation: "PGT - Physics",
    email: "",
    phone: "",
    qualification: "M.Sc, B.Ed",
    employeeCode: "EMP-421",
    hrCabinet: "Cabinet HR-02 / Shelf A",
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
      <DialogContent className="max-w-xl">
        <DialogHeader className="border-b pb-3">
          <div className="flex items-center gap-2.5">
            <div className="h-8 w-8 rounded-lg bg-blue-600 text-white flex items-center justify-center">
              <Briefcase className="h-4.5 w-4.5" />
            </div>
            <div>
              <DialogTitle className="text-base font-bold text-slate-900">
                Onboard Faculty / Staff Member
              </DialogTitle>
              <DialogDescription className="text-xs text-slate-500">
                Staff profile, departmental allocation, and physical HR dossier tracking
              </DialogDescription>
            </div>
          </div>
        </DialogHeader>

        <form onSubmit={handleSubmit} className="space-y-4 py-2 text-xs">
          <div className="grid grid-cols-2 gap-3">
            <div>
              <label className="text-slate-600 font-medium">Full Name *</label>
              <Input
                required
                placeholder="e.g. Dr. Ramesh Gupta"
                value={formData.fullName}
                onChange={(e) =>
                  setFormData({ ...formData, fullName: e.target.value })
                }
                className="h-8 text-xs mt-1"
              />
            </div>
            <div>
              <label className="text-slate-600 font-medium">Employee Code *</label>
              <Input
                required
                value={formData.employeeCode}
                onChange={(e) =>
                  setFormData({ ...formData, employeeCode: e.target.value })
                }
                className="h-8 text-xs mt-1 font-mono"
              />
            </div>
          </div>

          <div className="grid grid-cols-2 gap-3">
            <div>
              <label className="text-slate-600 font-medium">Department *</label>
              <select
                value={formData.department}
                onChange={(e) =>
                  setFormData({ ...formData, department: e.target.value })
                }
                className="h-8 w-full rounded-md border border-slate-200 bg-white px-2 mt-1 text-xs"
              >
                <option value="Science">Science (Physics, Chem, Bio)</option>
                <option value="Mathematics">Mathematics</option>
                <option value="Humanities">Humanities & Social Sciences</option>
                <option value="Languages">Languages & Literature</option>
                <option value="Administration">Administration & Accounts</option>
              </select>
            </div>
            <div>
              <label className="text-slate-600 font-medium">Designation / Role *</label>
              <Input
                required
                placeholder="e.g. PGT Physics"
                value={formData.designation}
                onChange={(e) =>
                  setFormData({ ...formData, designation: e.target.value })
                }
                className="h-8 text-xs mt-1"
              />
            </div>
          </div>

          <div className="grid grid-cols-2 gap-3">
            <div>
              <label className="text-slate-600 font-medium">Email Address *</label>
              <Input
                required
                type="email"
                placeholder="faculty@apex.edu.in"
                value={formData.email}
                onChange={(e) =>
                  setFormData({ ...formData, email: e.target.value })
                }
                className="h-8 text-xs mt-1"
              />
            </div>
            <div>
              <label className="text-slate-600 font-medium">Contact Phone *</label>
              <Input
                required
                placeholder="+91 98200 00000"
                value={formData.phone}
                onChange={(e) =>
                  setFormData({ ...formData, phone: e.target.value })
                }
                className="h-8 text-xs mt-1"
              />
            </div>
          </div>

          <div className="space-y-1">
            <label className="text-slate-600 font-medium">Physical HR Cabinet Filing Location</label>
            <Input
              value={formData.hrCabinet}
              onChange={(e) =>
                setFormData({ ...formData, hrCabinet: e.target.value })
              }
              className="h-8 text-xs mt-1 font-mono bg-slate-50"
            />
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
                  <Check className="h-3.5 w-3.5" /> Staff Onboarded!
                </>
              ) : (
                <>
                  <Briefcase className="h-3.5 w-3.5" /> Complete Onboarding
                </>
              )}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}

