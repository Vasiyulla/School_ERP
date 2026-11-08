import * as React from "react";
import Link from "next/link";
import { LucideIcon, TrendingUp, TrendingDown } from "lucide-react";
import { Card, CardContent } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { cn } from "@/lib/utils";

interface MetricCardProps {
  title: string;
  value: string | number;
  subtitle: string;
  progressPercent?: number;
  icon: LucideIcon;
  trend?: {
    value: string;
    isPositive: boolean;
  };
  accentColor?: "blue" | "emerald" | "amber" | "indigo";
  href?: string;
}

export function MetricCard({
  title,
  value,
  subtitle,
  progressPercent,
  icon: Icon,
  trend,
  accentColor = "blue",
  href,
}: MetricCardProps) {
  const iconBgClasses = {
    blue: "bg-blue-50 text-blue-600 dark:bg-blue-950/60 dark:text-blue-400 border border-blue-200/60 dark:border-blue-900/60",
    emerald: "bg-emerald-50 text-emerald-600 dark:bg-emerald-950/60 dark:text-emerald-400 border border-emerald-200/60 dark:border-emerald-900/60",
    amber: "bg-amber-50 text-amber-600 dark:bg-amber-950/60 dark:text-amber-400 border border-amber-200/60 dark:border-amber-900/60",
    indigo: "bg-indigo-50 text-indigo-600 dark:bg-indigo-950/60 dark:text-indigo-400 border border-indigo-200/60 dark:border-indigo-900/60",
  }[accentColor];

  const barColorClasses = {
    blue: "bg-blue-600 dark:bg-blue-500",
    emerald: "bg-emerald-600 dark:bg-emerald-500",
    amber: "bg-amber-500 dark:bg-amber-400",
    indigo: "bg-indigo-600 dark:bg-indigo-500",
  }[accentColor];

  const content = (
    <Card className={cn(
      "hover:shadow-md transition-all duration-200 border border-slate-200/90 dark:border-slate-800 bg-white dark:bg-slate-900 h-full",
      href && "cursor-pointer hover:border-blue-300 dark:hover:border-blue-700"
    )}>
      <CardContent className="p-5 flex flex-col justify-between h-full space-y-3">
        {/* Row 1: Category Label & Icon */}
        <div className="flex items-center justify-between">
          <span className="text-xs font-semibold uppercase tracking-wider text-slate-500 dark:text-slate-400">
            {title}
          </span>
          <div className={cn("p-2 rounded-xl shrink-0", iconBgClasses)}>
            <Icon className="h-4 w-4" />
          </div>
        </div>

        {/* Row 2: Primary Large Metric Value */}
        <div className="space-y-1">
          <div className="text-3xl font-extrabold tracking-tight text-slate-900 dark:text-slate-50">
            {value}
          </div>
        </div>

        {/* Row 3: Progress Bar (if provided) */}
        {progressPercent !== undefined && (
          <div className="w-full space-y-1 pt-1">
            <div className="h-1.5 w-full rounded-full bg-slate-100 dark:bg-slate-800 overflow-hidden">
              <div
                style={{ width: `${Math.min(100, Math.max(0, progressPercent))}%` }}
                className={cn("h-full rounded-full transition-all duration-500", barColorClasses)}
              />
            </div>
          </div>
        )}

        {/* Row 4: Clean Separated Comparison Subtext + Trend Pill */}
        <div className="flex items-center justify-between pt-1 text-xs border-t border-slate-100 dark:border-slate-800/80">
          <span className="text-slate-500 dark:text-slate-400 font-medium truncate pr-2">
            {subtitle}
          </span>
          {trend && (
            <Badge
              variant={trend.isPositive ? "success" : "warning"}
              className="text-[11px] font-medium py-0.5 px-2 shrink-0"
            >
              {trend.isPositive ? (
                <TrendingUp className="h-3 w-3 inline mr-1" />
              ) : (
                <TrendingDown className="h-3 w-3 inline mr-1" />
              )}
              {trend.value}
            </Badge>
          )}
        </div>
      </CardContent>
    </Card>
  );

  if (href) {
    return <Link href={href} className="block h-full">{content}</Link>;
  }

  return content;
}
