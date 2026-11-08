"use client";

import React from "react";
import { ParentProvider, useParentContext } from "./parent-context";
import { ParentHeader } from "./parent-header";
import { ParentBottomNav } from "./parent-bottom-nav";

function InnerShell({ children }: { children: React.ReactNode }) {
  const { activeChild, setActiveChild } = useParentContext();

  return (
    <div className="min-h-screen bg-background flex flex-col text-foreground antialiased selection:bg-emerald-500/20">
      <ParentHeader activeChild={activeChild} onChildChange={setActiveChild} />
      <ParentBottomNav />
      <main className="flex-1 pb-20 md:pb-12">
        {children}
      </main>
    </div>
  );
}

export function ParentShell({ children }: { children: React.ReactNode }) {
  return (
    <ParentProvider>
      <InnerShell>{children}</InnerShell>
    </ParentProvider>
  );
}

