"use client";

import React, { useState } from "react";
import {
  Bus,
  MapPin,
  PhoneCall,
  Clock,
  ShieldCheck,
  AlertTriangle,
  Navigation,
  CheckCircle2,
  BellRing,
  ArrowRight,
  Compass,
  Gauge,
} from "lucide-react";
import { useParentContext } from "@/components/parent/parent-context";
import { MOCK_BUS_TRACKING } from "@/lib/mock/parent-mock";
import { Button } from "@/components/ui/button";

export default function ParentBusTrackingPage() {
  const { activeChild } = useParentContext();
  const [shift, setShift] = useState<"MORNING" | "EVENING">("MORNING");
  const [alertsEnabled, setAlertsEnabled] = useState(true);

  return (
    <div className="mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8 space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <div className="flex items-center gap-2">
            <h1 className="text-xl sm:text-2xl font-bold tracking-tight">Real-Time School Bus GPS Tracker</h1>
            <span className="flex items-center gap-1 rounded-full bg-emerald-500/15 text-emerald-600 dark:text-emerald-400 px-2 py-0.5 text-xs font-bold uppercase tracking-wider">
              <span className="h-2 w-2 rounded-full bg-emerald-500 animate-ping" />
              Live Telemetry
            </span>
          </div>
          <p className="text-xs text-muted-foreground mt-0.5">
            Student: <strong className="text-foreground">{activeChild.fullName}</strong> • Assigned Route: <strong className="text-foreground">{activeChild.busRoute}</strong>
          </p>
        </div>

        {/* Morning / Evening Shift Toggle */}
        <div className="flex items-center rounded-xl border border-border bg-muted/40 p-1 text-xs">
          <button
            onClick={() => setShift("MORNING")}
            className={`rounded-lg px-3 py-1.5 font-medium transition-all ${
              shift === "MORNING"
                ? "bg-card text-foreground shadow-sm"
                : "text-muted-foreground hover:text-foreground"
            }`}
          >
            Morning Pickup (06:45 AM)
          </button>
          <button
            onClick={() => setShift("EVENING")}
            className={`rounded-lg px-3 py-1.5 font-medium transition-all ${
              shift === "EVENING"
                ? "bg-card text-foreground shadow-sm"
                : "text-muted-foreground hover:text-foreground"
            }`}
          >
            Evening Drop-off (03:15 PM)
          </button>
        </div>
      </div>

      {/* Live Status Cards (3-Column Top Grid) */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        {/* ETA Countdown Card */}
        <div className="rounded-2xl border border-emerald-500/30 bg-gradient-to-br from-emerald-500/10 via-card to-card p-5 shadow-sm space-y-2">
          <div className="flex items-center justify-between text-xs text-emerald-600 dark:text-emerald-400 font-semibold">
            <span>Estimated Arrival Time (ETA)</span>
            <Clock className="h-4 w-4" />
          </div>
          <div className="text-3xl font-extrabold font-mono tracking-tight text-foreground">
            {MOCK_BUS_TRACKING.etaMinutes} mins
          </div>
          <p className="text-xs text-muted-foreground">
            At your assigned stop: <strong className="text-foreground">Infinity Mall / Link Road</strong>
          </p>
        </div>

        {/* Vehicle Telematics Card */}
        <div className="rounded-2xl border border-border/80 bg-card p-5 shadow-sm space-y-2">
          <div className="flex items-center justify-between text-xs text-muted-foreground font-semibold">
            <span>Live Vehicle Telemetry</span>
            <Gauge className="h-4 w-4 text-blue-500" />
          </div>
          <div className="text-2xl font-bold font-mono tracking-tight text-foreground">
            {MOCK_BUS_TRACKING.speedKmph} <span className="text-sm font-normal text-muted-foreground">km/h</span>
          </div>
          <p className="text-xs text-muted-foreground truncate">
            Near: <strong className="text-foreground">{MOCK_BUS_TRACKING.currentLocationName}</strong>
          </p>
        </div>

        {/* Assigned Driver Card */}
        <div className="rounded-2xl border border-border/80 bg-card p-5 shadow-sm space-y-2">
          <div className="flex items-center justify-between text-xs text-muted-foreground font-semibold">
            <span>Bus Crew & Contact</span>
            <ShieldCheck className="h-4 w-4 text-emerald-500" />
          </div>
          <div className="flex items-center justify-between">
            <div>
              <div className="text-sm font-bold text-foreground">{MOCK_BUS_TRACKING.driverName}</div>
              <div className="text-[11px] text-muted-foreground font-mono">{MOCK_BUS_TRACKING.vehicleRegistration}</div>
            </div>
            <a href={`tel:${MOCK_BUS_TRACKING.driverPhone}`}>
              <Button size="sm" className="bg-emerald-600 hover:bg-emerald-700 text-white text-xs gap-1.5 h-8">
                <PhoneCall className="h-3.5 w-3.5" /> Call Driver
              </Button>
            </a>
          </div>
        </div>
      </div>

      {/* Main Map View & Route Timeline */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Interactive Simulated Transit Map (2 cols) */}
        <div className="lg:col-span-2 rounded-2xl border border-border/80 bg-card overflow-hidden shadow-sm flex flex-col">
          <div className="border-b border-border p-4 bg-muted/20 flex items-center justify-between">
            <div className="flex items-center gap-2">
              <Navigation className="h-4 w-4 text-emerald-600 dark:text-emerald-400" />
              <span className="text-xs font-semibold">Live Transit Corridor Map</span>
            </div>
            <span className="text-[11px] text-muted-foreground font-mono">
              GPS Lat: {MOCK_BUS_TRACKING.latitude}° N, Lng: {MOCK_BUS_TRACKING.longitude}° E
            </span>
          </div>

          {/* Graphical Map Representation */}
          <div className="relative h-[340px] sm:h-[400px] bg-slate-950 p-6 flex flex-col justify-between overflow-hidden">
            {/* Background Grid Lines simulating streets */}
            <div className="absolute inset-0 opacity-15 bg-[radial-gradient(#10b981_1px,transparent_1px)] [background-size:24px_24px]" />

            {/* Simulated Road Path */}
            <svg className="absolute inset-0 w-full h-full" xmlns="http://www.w3.org/2000/svg">
              <path
                d="M 50 80 Q 250 120 400 200 T 750 300"
                fill="none"
                stroke="#1e293b"
                strokeWidth="18"
                strokeLinecap="round"
              />
              <path
                d="M 50 80 Q 250 120 400 200 T 750 300"
                fill="none"
                stroke="#10b981"
                strokeWidth="4"
                strokeDasharray="8 8"
                className="animate-pulse"
              />
            </svg>

            {/* Top Left: Stop 1 */}
            <div className="relative z-10 flex items-center gap-2">
              <div className="h-8 w-8 rounded-full bg-slate-800 border-2 border-emerald-500 text-white flex items-center justify-center text-xs font-bold shadow-lg">
                ✓
              </div>
              <div className="rounded-lg bg-slate-900/90 border border-slate-700 p-2 text-white text-[11px] backdrop-blur">
                <div className="font-semibold">Lokhandwala Gate 2</div>
                <div className="text-[10px] text-emerald-400">Departed at 06:47 AM</div>
              </div>
            </div>

            {/* Middle: Active Vehicle Bus Pin */}
            <div className="relative z-10 self-center flex flex-col items-center animate-bounce">
              <div className="rounded-xl bg-emerald-600 text-white px-3 py-1 text-xs font-bold shadow-xl flex items-center gap-1.5 border border-emerald-400">
                <Bus className="h-4 w-4" />
                <span>Bus #04 • {MOCK_BUS_TRACKING.speedKmph} km/h</span>
              </div>
              <div className="h-3 w-3 rotate-45 bg-emerald-600 -mt-1.5" />
            </div>

            {/* Target Child Pickup Stop */}
            <div className="relative z-10 self-end flex items-center gap-2">
              <div className="rounded-lg bg-slate-900/90 border border-amber-500/60 p-2 text-white text-[11px] backdrop-blur text-right">
                <div className="font-semibold text-amber-300">Your Stop: Infinity Mall</div>
                <div className="text-[10px] text-amber-400">ETA: {MOCK_BUS_TRACKING.etaMinutes} mins away</div>
              </div>
              <div className="h-9 w-9 rounded-full bg-amber-500 border-2 border-white text-white flex items-center justify-center shadow-lg animate-pulse">
                <MapPin className="h-5 w-5" />
              </div>
            </div>
          </div>

          <div className="p-3.5 bg-muted/30 border-t border-border flex items-center justify-between text-xs">
            <span className="text-muted-foreground">
              Geofence: <strong className="text-foreground">Within Normal Transit Zone (Speed Limit: 40 km/h)</strong>
            </span>
            <span className="flex items-center gap-1 text-emerald-600 dark:text-emerald-400 font-semibold">
              <CheckCircle2 className="h-3.5 w-3.5" /> GPS Hardware Signal Strong
            </span>
          </div>
        </div>

        {/* Route Stops Sequence (1 col) */}
        <div className="rounded-2xl border border-border/80 bg-card p-5 shadow-sm space-y-4">
          <div className="flex items-center justify-between border-b border-border pb-3">
            <h2 className="text-sm font-semibold tracking-tight">Scheduled Route Stops</h2>
            <button
              onClick={() => setAlertsEnabled(!alertsEnabled)}
              className="flex items-center gap-1 text-[11px] text-emerald-600 dark:text-emerald-400 font-medium hover:underline"
            >
              <BellRing className="h-3.5 w-3.5" />
              {alertsEnabled ? "Alerts On" : "Muted"}
            </button>
          </div>

          <div className="relative pl-6 space-y-6 before:absolute before:left-2.5 before:top-2 before:bottom-2 before:w-0.5 before:bg-border">
            {MOCK_BUS_TRACKING.stops.map((stop, idx) => (
              <div key={idx} className="relative">
                <div
                  className={`absolute -left-6 top-0.5 h-5 w-5 rounded-full border-2 flex items-center justify-center text-[10px] font-bold ${
                    stop.isCompleted
                      ? "bg-emerald-500 border-emerald-500 text-white"
                      : stop.isChildStop
                      ? "bg-amber-500 border-white text-white ring-2 ring-amber-500/40"
                      : "bg-card border-border text-muted-foreground"
                  }`}
                >
                  {stop.isCompleted ? "✓" : idx + 1}
                </div>
                <div>
                  <div className="flex items-center gap-1.5 flex-wrap">
                    <span className="text-xs font-semibold text-foreground">{stop.stopName}</span>
                    {stop.isChildStop && (
                      <span className="rounded bg-amber-500/20 text-amber-600 dark:text-amber-400 font-semibold text-[9px] px-1.5 py-0.2">
                        Your Stop
                      </span>
                    )}
                  </div>
                  <div className="text-[11px] text-muted-foreground font-mono mt-0.5">
                    Scheduled: {stop.scheduledTime}
                    {stop.actualTime && (
                      <span className="text-emerald-600 dark:text-emerald-400 ml-1.5 font-sans font-medium">
                        (Departed: {stop.actualTime})
                      </span>
                    )}
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}

