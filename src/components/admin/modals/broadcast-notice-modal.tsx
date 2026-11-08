"use client";

import * as React from "react";
import { Send, Check, Radio } from "lucide-react";
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

interface BroadcastNoticeModalProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  onSuccess?: (noticeData: any) => void;
}

export function BroadcastNoticeModal({
  open,
  onOpenChange,
  onSuccess,
}: BroadcastNoticeModalProps) {
  const [formData, setFormData] = React.useState({
    title: "",
    audience: "ALL",
    body: "",
    channels: {
      portal: true,
      sms: true,
      email: true,
    },
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
      <DialogContent className="max-w-lg">
        <DialogHeader className="border-b pb-3">
          <div className="flex items-center gap-2.5">
            <div className="h-8 w-8 rounded-lg bg-blue-600 text-white flex items-center justify-center">
              <Send className="h-4.5 w-4.5" />
            </div>
            <div>
              <DialogTitle className="text-base font-bold text-slate-900">
                Broadcast Official Notice / Circular
              </DialogTitle>
              <DialogDescription className="text-xs text-slate-500">
                Multi-channel dispatch via BullMQ communication workers
              </DialogDescription>
            </div>
          </div>
        </DialogHeader>

        <form onSubmit={handleSubmit} className="space-y-4 py-2 text-xs">
          <div>
            <label className="text-slate-600 font-medium">Circular Title *</label>
            <Input
              required
              placeholder="e.g. Schedule for Annual Science Fair 2026..."
              value={formData.title}
              onChange={(e) =>
                setFormData({ ...formData, title: e.target.value })
              }
              className="h-8 text-xs mt-1"
            />
          </div>

          <div className="grid grid-cols-2 gap-3">
            <div>
              <label className="text-slate-600 font-medium">Target Audience</label>
              <select
                value={formData.audience}
                onChange={(e) =>
                  setFormData({ ...formData, audience: e.target.value })
                }
                className="h-8 w-full rounded-md border border-slate-200 bg-white px-2 mt-1 text-xs"
              >
                <option value="ALL">All School (18,450 Guardians & Students)</option>
                <option value="SENIOR">Senior Wing (Grades 9 to 12)</option>
                <option value="MIDDLE">Middle Wing (Grades 6 to 8)</option>
                <option value="PRIMARY">Primary Wing (Grades 1 to 5)</option>
                <option value="STAFF">Faculty & Staff Only (420 Employees)</option>
              </select>
            </div>

            <div>
              <label className="text-slate-600 font-medium">Dispatch Channels</label>
              <div className="flex items-center gap-3 pt-2 text-xs text-slate-600">
                <label className="flex items-center gap-1.5 cursor-pointer">
                  <input
                    type="checkbox"
                    checked={formData.channels.portal}
                    onChange={(e) =>
                      setFormData({
                        ...formData,
                        channels: {
                          ...formData.channels,
                          portal: e.target.checked,
                        },
                      })
                    }
                    className="rounded border-slate-300"
                  />
                  <span>Portal</span>
                </label>
                <label className="flex items-center gap-1.5 cursor-pointer">
                  <input
                    type="checkbox"
                    checked={formData.channels.sms}
                    onChange={(e) =>
                      setFormData({
                        ...formData,
                        channels: {
                          ...formData.channels,
                          sms: e.target.checked,
                        },
                      })
                    }
                    className="rounded border-slate-300"
                  />
                  <span>SMS</span>
                </label>
                <label className="flex items-center gap-1.5 cursor-pointer">
                  <input
                    type="checkbox"
                    checked={formData.channels.email}
                    onChange={(e) =>
                      setFormData({
                        ...formData,
                        channels: {
                          ...formData.channels,
                          email: e.target.checked,
                        },
                      })
                    }
                    className="rounded border-slate-300"
                  />
                  <span>Email</span>
                </label>
              </div>
            </div>
          </div>

          <div>
            <label className="text-slate-600 font-medium">Notice Content *</label>
            <textarea
              required
              rows={4}
              placeholder="Type official notification text here..."
              value={formData.body}
              onChange={(e) =>
                setFormData({ ...formData, body: e.target.value })
              }
              className="w-full rounded-md border border-slate-200 bg-white p-2.5 text-xs text-slate-800 mt-1 focus:outline-none focus:ring-1 focus:ring-blue-600"
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
                  <Check className="h-3.5 w-3.5" /> Dispatched to 18,450!
                </>
              ) : (
                <>
                  <Send className="h-3.5 w-3.5" /> Publish Broadcast
                </>
              )}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}

