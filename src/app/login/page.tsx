"use client";

import * as React from "react";
import { useRouter } from "next/navigation";
import {
  GraduationCap,
  Lock,
  Mail,
  ArrowRight,
  ShieldCheck,
  Server,
  Sparkles,
  Building2,
  AlertCircle,
  CheckCircle2,
} from "lucide-react";
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Badge } from "@/components/ui/badge";
import { cn } from "@/lib/utils";

import { authService } from "@/lib/api/auth.service";
import { authStorage } from "@/lib/auth-storage";

export default function LoginPage() {
  const router = useRouter();
  const [email, setEmail] = React.useState("admin@apexacademy.edu.in");
  const [password, setPassword] = React.useState("Admin123!");
  const [tenantId, setTenantId] = React.useState("tenant_apex_mumbai_01");
  const [loading, setLoading] = React.useState(false);
  const [errorMsg, setErrorMsg] = React.useState<string | null>(null);
  const [backendStatus, setBackendStatus] = React.useState<"CONNECTED" | "OFFLINE">("CONNECTED");

  // Check health on load
  React.useEffect(() => {
    authService
      .checkHealth()
      .then((data) => {
        if (data.status === "UP" || (data as any).success) {
          setBackendStatus("CONNECTED");
        } else {
          setBackendStatus("OFFLINE");
        }
      })
      .catch(() => setBackendStatus("OFFLINE"));
  }, []);

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setErrorMsg(null);

    try {
      const data = await authService.login(email, password, tenantId);

      // Route based on role
      const roles: string[] = data.user.roles || [];
      if (roles.includes("TEACHER")) {
        router.push("/teacher");
      } else if (roles.includes("PARENT") || email.includes("parent") || email.includes("sharma")) {
        router.push("/parent");
      } else {
        router.push("/admin");
      }
    } catch (err: any) {
      setErrorMsg(err.message || "Failed to connect to Spring Boot backend");
    } finally {
      setLoading(false);
    }
  };

  const setPreset = (role: "ADMIN" | "TEACHER" | "PARENT") => {
    if (role === "ADMIN") {
      setEmail("admin@apexacademy.edu.in");
      setPassword("Admin123!");
    } else if (role === "TEACHER") {
      setEmail("teacher@apexacademy.edu.in");
      setPassword("Teacher123!");
    } else {
      setEmail("parent.rajesh@example.com");
      setPassword("Parent123!");
      // Directly route to parent portal on preset click
      router.push("/parent");
    }
  };

  return (
    <div className="min-h-screen bg-slate-50 dark:bg-slate-950 flex flex-col justify-center items-center p-4">
      <div className="w-full max-w-md space-y-6">
        {/* Brand Header */}
        <div className="text-center space-y-2">
          <div className="inline-flex h-12 w-12 rounded-2xl bg-blue-600 text-white items-center justify-center shadow-md">
            <GraduationCap className="h-7 w-7" />
          </div>
          <h1 className="text-2xl font-bold tracking-tight text-slate-900 dark:text-slate-50">
            Apex Academy ERP
          </h1>
          <p className="text-xs text-slate-500 font-medium">
            Multi-Tenant B2B SaaS Enterprise Portal • CBSE #113042
          </p>

          <div className="flex items-center justify-center gap-2 pt-1">
            <Badge
              variant={backendStatus === "CONNECTED" ? "success" : "destructive"}
              className="text-[10px] font-mono py-0.5"
            >
              <Server className="h-3 w-3 mr-1" />
              Java 21 Spring Boot Backend: {backendStatus}
            </Badge>
          </div>
        </div>

        {/* Card */}
        <Card className="shadow-xs border-slate-200/90 dark:border-slate-800">
          <CardHeader className="pb-4">
            <CardTitle className="text-base font-bold text-slate-900 dark:text-slate-50">
              Sign In to Your Workspace
            </CardTitle>
            <CardDescription className="text-xs text-slate-500">
              Enter your credentials to access your administrative or teaching dashboard
            </CardDescription>
          </CardHeader>
          <CardContent className="space-y-4">
            {errorMsg && (
              <div className="p-3 rounded-lg bg-rose-50 border border-rose-200 text-rose-800 text-xs flex items-center gap-2">
                <AlertCircle className="h-4 w-4 shrink-0 text-rose-600" />
                <span>{errorMsg}</span>
              </div>
            )}

            <form onSubmit={handleLogin} className="space-y-3.5">
              <div>
                <label className="text-xs font-bold text-slate-700 dark:text-slate-300">
                  School Tenant ID
                </label>
                <div className="relative mt-1">
                  <Building2 className="absolute left-2.5 top-1/2 -translate-y-1/2 h-3.5 w-3.5 text-slate-400" />
                  <Input
                    value={tenantId}
                    onChange={(e) => setTenantId(e.target.value)}
                    className="pl-8 text-xs font-mono h-9"
                    required
                  />
                </div>
              </div>

              <div>
                <label className="text-xs font-bold text-slate-700 dark:text-slate-300">
                  Email Address
                </label>
                <div className="relative mt-1">
                  <Mail className="absolute left-2.5 top-1/2 -translate-y-1/2 h-3.5 w-3.5 text-slate-400" />
                  <Input
                    type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    className="pl-8 text-xs h-9"
                    required
                  />
                </div>
              </div>

              <div>
                <label className="text-xs font-bold text-slate-700 dark:text-slate-300">
                  Password
                </label>
                <div className="relative mt-1">
                  <Lock className="absolute left-2.5 top-1/2 -translate-y-1/2 h-3.5 w-3.5 text-slate-400" />
                  <Input
                    type="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    className="pl-8 text-xs h-9"
                    required
                  />
                </div>
              </div>

              <Button
                type="submit"
                disabled={loading}
                className="w-full h-9 bg-blue-600 hover:bg-blue-700 text-white font-semibold text-xs shadow-xs gap-1.5 mt-2"
              >
                {loading ? "Authenticating..." : "Sign In to Portal"}
                <ArrowRight className="h-3.5 w-3.5" />
              </Button>
            </form>

            {/* Quick Presets */}
            <div className="pt-3 border-t border-slate-100 dark:border-slate-800 space-y-2">
              <p className="text-[10px] font-bold uppercase tracking-wider text-slate-400 text-center">
                One-Click Role Presets
              </p>
              <div className="grid grid-cols-3 gap-2">
                <button
                  type="button"
                  onClick={() => setPreset("ADMIN")}
                  className="p-2 rounded-lg border border-slate-200 hover:border-blue-400 hover:bg-blue-50/50 text-left transition-all"
                >
                  <p className="text-[11px] font-bold text-slate-800">Super Admin</p>
                  <p className="text-[9px] text-slate-500 font-mono truncate">Dr. Rajesh Sharma</p>
                </button>
                <button
                  type="button"
                  onClick={() => setPreset("TEACHER")}
                  className="p-2 rounded-lg border border-slate-200 hover:border-emerald-400 hover:bg-emerald-50/50 text-left transition-all"
                >
                  <p className="text-[11px] font-bold text-slate-800">Faculty (10-A)</p>
                  <p className="text-[9px] text-slate-500 font-mono truncate">Dr. Vikram Seth</p>
                </button>
                <button
                  type="button"
                  onClick={() => setPreset("PARENT")}
                  className="p-2 rounded-lg border border-slate-200 hover:border-amber-400 hover:bg-amber-50/50 text-left transition-all"
                >
                  <p className="text-[11px] font-bold text-slate-800">Parent / Family</p>
                  <p className="text-[9px] text-slate-500 font-mono truncate">Rajesh Sharma</p>
                </button>
              </div>
            </div>
          </CardContent>
        </Card>

        {/* Footer */}
        <p className="text-center text-[11px] text-slate-400">
          Powered by Java 21 LTS + Spring Boot 3 & Next.js 15 Modular Monolith
        </p>
      </div>
    </div>
  );
}

