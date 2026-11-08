import * as React from "react";
import { CheckCircle2, DollarSign, UserPlus, Award } from "lucide-react";
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from "@/components/ui/card";
import { mockRecentActivities } from "@/lib/mock/admin-mock";
import { cn } from "@/lib/utils";

export function RecentActivityCard() {
  const getIcon = (type: string) => {
    switch (type) {
      case "attendance":
        return <CheckCircle2 className="h-4 w-4 text-emerald-600 dark:text-emerald-400" />;
      case "finance":
        return <DollarSign className="h-4 w-4 text-blue-600 dark:text-blue-400" />;
      case "admission":
        return <UserPlus className="h-4 w-4 text-amber-600 dark:text-amber-400" />;
      case "exam":
        return <Award className="h-4 w-4 text-purple-600 dark:text-purple-400" />;
      default:
        return <CheckCircle2 className="h-4 w-4 text-slate-500" />;
    }
  };

  const getBg = (type: string) => {
    switch (type) {
      case "attendance":
        return "bg-emerald-50 dark:bg-emerald-950/50 border border-emerald-200/60 dark:border-emerald-900/60";
      case "finance":
        return "bg-blue-50 dark:bg-blue-950/50 border border-blue-200/60 dark:border-blue-900/60";
      case "admission":
        return "bg-amber-50 dark:bg-amber-950/50 border border-amber-200/60 dark:border-amber-900/60";
      case "exam":
        return "bg-purple-50 dark:bg-purple-950/50 border border-purple-200/60 dark:border-purple-900/60";
      default:
        return "bg-slate-100 dark:bg-slate-800";
    }
  };

  return (
    <Card className="col-span-1">
      <CardHeader className="pb-4">
        <CardTitle className="text-base font-semibold text-slate-900 dark:text-slate-50">
          Live Operational Stream
        </CardTitle>
        <CardDescription className="text-xs text-slate-500 dark:text-slate-400">
          Real-time audit log of school events
        </CardDescription>
      </CardHeader>
      <CardContent>
        <div className="space-y-4">
          {mockRecentActivities.map((act) => (
            <div key={act.id} className="flex items-start gap-3.5 group">
              <div
                className={cn(
                  "p-2 rounded-xl shrink-0 mt-0.5 transition-transform duration-150 group-hover:scale-105",
                  getBg(act.type)
                )}
              >
                {getIcon(act.type)}
              </div>
              <div className="flex-1 space-y-1">
                <div className="flex items-center justify-between">
                  <p className="text-xs font-semibold text-slate-900 dark:text-slate-100">
                    {act.title}
                  </p>
                  <span className="text-[11px] font-medium text-slate-400 dark:text-slate-500">
                    {act.timestamp}
                  </span>
                </div>
                <p className="text-xs text-slate-600 dark:text-slate-400 leading-relaxed">
                  {act.description}
                </p>
              </div>
            </div>
          ))}
        </div>
      </CardContent>
    </Card>
  );
}
