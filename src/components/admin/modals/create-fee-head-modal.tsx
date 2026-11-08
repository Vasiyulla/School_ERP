"use client";

import * as React from "react";
import { Receipt, Check, Plus } from "lucide-react";
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

interface CreateFeeHeadModalProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  onSuccess?: (feeHead: any) => void;
}

export function CreateFeeHeadModal({
  open,
  onOpenChange,
  onSuccess,
}: CreateFeeHeadModalProps) {
  const [formData, setFormData] = React.useState({
    title: "",
    category: "Tuition",
    gradeLevel: "All Grades",
    amount: "",
    frequency: "Termly",
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
            <div className="h-8 w-8 rounded-lg bg-emerald-600 text-white flex items-center justify-center">
              <Receipt className="h-4.5 w-4.5" />
            </div>
            <div>
              <DialogTitle className="text-base font-bold text-slate-900">
                Create Fee Head / Structure
              </DialogTitle>
              <DialogDescription className="text-xs text-slate-500">
                Configure double-entry accounting fee ledger head
              </DialogDescription>
            </div>
          </div>
        </DialogHeader>

        <form onSubmit={handleSubmit} className="space-y-4 py-2 text-xs">
          <div>
            <label className="text-slate-600 font-medium">Fee Head Title *</label>
            <Input
              required
              placeholder="e.g. Science Laboratory & Robotics Fee"
              value={formData.title}
              onChange={(e) =>
                setFormData({ ...formData, title: e.target.value })
              }
              className="h-8 text-xs mt-1"
            />
          </div>

          <div className="grid grid-cols-2 gap-3">
            <div>
              <label className="text-slate-600 font-medium">Category</label>
              <select
                value={formData.category}
                onChange={(e) =>
                  setFormData({ ...formData, category: e.target.value })
                }
                className="h-8 w-full rounded-md border border-slate-200 bg-white px-2 mt-1 text-xs"
              >
                <option value="Tuition">Tuition Fee</option>
                <option value="Lab">Laboratory & Digital</option>
                <option value="Transport">Transport / Bus</option>
                <option value="Library">Library & Publications</option>
                <option value="Sports">Sports & Activities</option>
              </select>
            </div>

            <div>
              <label className="text-slate-600 font-medium">Billing Cycle</label>
              <select
                value={formData.frequency}
                onChange={(e) =>
                  setFormData({ ...formData, frequency: e.target.value })
                }
                className="h-8 w-full rounded-md border border-slate-200 bg-white px-2 mt-1 text-xs"
              >
                <option value="Termly">Termly (3 Installments)</option>
                <option value="Annual">Annual (One-time)</option>
                <option value="Monthly">Monthly Recurring</option>
              </select>
            </div>
          </div>

          <div className="grid grid-cols-2 gap-3">
            <div>
              <label className="text-slate-600 font-medium">Applicable Grades</label>
              <select
                value={formData.gradeLevel}
                onChange={(e) =>
                  setFormData({ ...formData, gradeLevel: e.target.value })
                }
                className="h-8 w-full rounded-md border border-slate-200 bg-white px-2 mt-1 text-xs"
              >
                <option value="All Grades">All Grades (1 – 12)</option>
                <option value="Grades 11-12">Grades 11 & 12 Only</option>
                <option value="Grades 9-10">Grades 9 & 10 Only</option>
                <option value="Grades 6-8">Grades 6 to 8 Only</option>
                <option value="Grades 1-5">Grades 1 to 5 (Primary)</option>
              </select>
            </div>

            <div>
              <label className="text-slate-600 font-medium">Amount (₹) *</label>
              <Input
                required
                type="number"
                placeholder="e.g. 15000"
                value={formData.amount}
                onChange={(e) =>
                  setFormData({ ...formData, amount: e.target.value })
                }
                className="h-8 text-xs mt-1 font-mono"
              />
            </div>
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
              className="h-8 text-xs bg-emerald-600 hover:bg-emerald-700 text-white gap-1.5"
            >
              {isDone ? (
                <>
                  <Check className="h-3.5 w-3.5" /> Fee Head Added!
                </>
              ) : (
                <>
                  <Plus className="h-3.5 w-3.5" /> Save Fee Structure
                </>
              )}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}

