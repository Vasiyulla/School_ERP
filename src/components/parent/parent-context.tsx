"use client";

import React, { createContext, useContext, useState } from "react";
import { PARENT_USER_DATA, ChildProfile } from "@/lib/mock/parent-mock";

interface ParentContextType {
  activeChild: ChildProfile;
  setActiveChild: (child: ChildProfile) => void;
}

const ParentContext = createContext<ParentContextType | undefined>(undefined);

export function ParentProvider({ children }: { children: React.ReactNode }) {
  const [activeChild, setActiveChild] = useState<ChildProfile>(PARENT_USER_DATA.children[0]);

  return (
    <ParentContext.Provider value={{ activeChild, setActiveChild }}>
      {children}
    </ParentContext.Provider>
  );
}

export function useParentContext() {
  const context = useContext(ParentContext);
  if (!context) {
    throw new Error("useParentContext must be used within a ParentProvider");
  }
  return context;
}

