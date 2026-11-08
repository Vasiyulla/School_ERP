import React from "react";
import { ParentShell } from "@/components/parent/parent-shell";

export const metadata = {
  title: "Parent & Family Portal | Apex Academy",
  description: "Real-time student progress, fee payments, bus tracking, and school communication.",
};

export default function ParentLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return <ParentShell>{children}</ParentShell>;
}

