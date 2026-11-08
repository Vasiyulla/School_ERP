"use client";

import * as React from "react";
import {
  Bus,
  MapPin,
  Calendar,
  BookOpen,
  Phone,
  CheckCircle2,
  AlertTriangle,
  Clock,
  Plus,
  Download,
  Search,
  Navigation,
  ShieldCheck,
  Building,
} from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Badge } from "@/components/ui/badge";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { cn } from "@/lib/utils";

interface BusFleet {
  id: string;
  busNumber: string;
  vehiclePlate: string;
  routeName: string;
  driverName: string;
  driverPhone: string;
  capacity: string;
  gpsStatus: "ON_ROUTE" | "ARRIVED" | "DELAYED" | "MAINTENANCE";
  currentLocation: string;
  speed: string;
  lastPing: string;
}

const mockFleet: BusFleet[] = [
  {
    id: "bus-1",
    busNumber: "Bus #01",
    vehiclePlate: "MH-02-CB-4819",
    routeName: "Route 01 (Andheri – Bandra – Campus)",
    driverName: "Suresh Patil",
    driverPhone: "+91 98200 11234",
    capacity: "48 / 52 Seats",
    gpsStatus: "ARRIVED",
    currentLocation: "Campus Bus Bay #01",
    speed: "0 km/h",
    lastPing: "Just now",
  },
  {
    id: "bus-2",
    busNumber: "Bus #04",
    vehiclePlate: "MH-02-CB-4822",
    routeName: "Route 04 (Powai – Hiranandani – Campus)",
    driverName: "Manoj Kumar",
    driverPhone: "+91 98201 55678",
    capacity: "50 / 52 Seats",
    gpsStatus: "ON_ROUTE",
    currentLocation: "JVLR Junction (2.4 km away)",
    speed: "34 km/h",
    lastPing: "30s ago",
  },
  {
    id: "bus-3",
    busNumber: "Bus #07",
    vehiclePlate: "MH-02-CB-4825",
    routeName: "Route 07 (Thane West – Mulund – Campus)",
    driverName: "Ramesh Pawar",
    driverPhone: "+91 98199 44321",
    capacity: "52 / 52 Seats",
    gpsStatus: "DELAYED",
    currentLocation: "Goregaon Flyover (Traffic bottleneck)",
    speed: "12 km/h",
    lastPing: "1m ago",
  },
  {
    id: "bus-4",
    busNumber: "Bus #11",
    vehiclePlate: "MH-02-CB-4829",
    routeName: "Route 11 (Borivali – Malad – Campus)",
    driverName: "Ganesh Shinde",
    driverPhone: "+91 98205 99876",
    capacity: "44 / 52 Seats",
    gpsStatus: "ON_ROUTE",
    currentLocation: "Western Express Hwy (Near Hub Mall)",
    speed: "42 km/h",
    lastPing: "15s ago",
  },
  {
    id: "bus-5",
    busNumber: "Bus #16",
    vehiclePlate: "MH-02-CB-4834",
    routeName: "Route 16 (Dadar – Prabhadevi – Campus)",
    driverName: "Kishore Jadhav",
    driverPhone: "+91 98210 33456",
    capacity: "40 / 52 Seats",
    gpsStatus: "ARRIVED",
    currentLocation: "Campus Bus Bay #04",
    speed: "0 km/h",
    lastPing: "Just now",
  },
];

const mockFacilities = [
  {
    name: "Main Auditorium (Dr. APJ Abdul Kalam Hall)",
    capacity: "1,200 Seats",
    currentBooking: "Annual Inter-School Science Fair (Grade 9-12)",
    bookedBy: "Dr. Vikram Seth (Science Dept)",
    timeSlot: "09:00 AM – 03:30 PM",
    status: "OCCUPIED",
  },
  {
    name: "Synthetic Athletics Track & Football Arena",
    capacity: "500 Spectators",
    currentBooking: "Under-16 Football Team Trials",
    bookedBy: "Coach Ranveer Singh (Sports)",
    timeSlot: "02:00 PM – 05:00 PM",
    status: "SCHEDULED",
  },
  {
    name: "Advanced Robotics & AI Innovation Lab",
    capacity: "45 Workstations",
    currentBooking: "STEM Practical Session (Grade 10-A)",
    bookedBy: "Pooja Hegde (IT Dept)",
    timeSlot: "11:00 AM – 12:30 PM",
    status: "OCCUPIED",
  },
  {
    name: "Open Air Amphitheatre",
    capacity: "400 Seats",
    currentBooking: "No active reservation",
    bookedBy: "—",
    timeSlot: "Available for booking",
    status: "AVAILABLE",
  },
];

const mockLibrary = [
  { title: "Concepts of Physics (Vol 1 & 2)", author: "Dr. H.C. Verma", isbn: "978-8177091878", totalCopies: 45, onLoan: 42, shelf: "Science • Stack 3B" },
  { title: "Problems in General Physics", author: "I.E. Irodov", isbn: "978-8123902167", totalCopies: 20, onLoan: 18, shelf: "Science • Stack 3C" },
  { title: "To Kill a Mockingbird", author: "Harper Lee", isbn: "978-0060935467", totalCopies: 35, onLoan: 29, shelf: "Literature • Stack 1A" },
  { title: "India's Ancient Past", author: "R.S. Sharma", isbn: "978-0195687859", totalCopies: 25, onLoan: 14, shelf: "History • Stack 4B" },
];

export default function AdminOperationsPage() {
  const [activeTab, setActiveTab] = React.useState<"fleet" | "facilities" | "library">("fleet");

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between border-b border-slate-200/80 pb-5">
        <div>
          <div className="flex items-center gap-2.5">
            <h1 className="text-xl font-bold tracking-tight text-slate-900">
              Operations, Transport & Facilities
            </h1>
            <Badge variant="outline" className="text-[11px] font-semibold text-emerald-700 bg-emerald-50 border-emerald-200">
              Fleet GPS Active
            </Badge>
          </div>
          <p className="text-xs text-slate-500 mt-1">
            Real-time telemetry for 28 school buses, transport routes, campus facility booking, and library circulation.
          </p>
        </div>
        <div className="flex items-center gap-2">
          <Button variant="outline" size="sm" className="h-8 gap-1.5 text-xs text-slate-700">
            <Navigation className="h-3.5 w-3.5" />
            Live Map View
          </Button>
          <Button size="sm" className="h-8 gap-1.5 text-xs bg-blue-600 hover:bg-blue-700 text-white shadow-xs">
            <Plus className="h-3.5 w-3.5" />
            Book Campus Facility
          </Button>
        </div>
      </div>

      {/* KPI Stats */}
      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
        <Card className="border-slate-200/80 shadow-xs">
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <span className="text-[11px] font-semibold uppercase tracking-wider text-slate-500">
              School Bus Fleet
            </span>
            <div className="flex h-7 w-7 items-center justify-center rounded-md bg-blue-50 text-blue-600">
              <Bus className="h-3.5 w-3.5" />
            </div>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold tracking-tight text-slate-900">28 Buses</div>
            <p className="text-[11px] text-emerald-600 font-medium mt-1">
              27 active • 1 scheduled service
            </p>
          </CardContent>
        </Card>

        <Card className="border-slate-200/80 shadow-xs">
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <span className="text-[11px] font-semibold uppercase tracking-wider text-slate-500">
              Transport Ridership
            </span>
            <div className="flex h-7 w-7 items-center justify-center rounded-md bg-emerald-50 text-emerald-600">
              <CheckCircle2 className="h-3.5 w-3.5" />
            </div>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold tracking-tight text-slate-900">1,380 Students</div>
            <p className="text-[11px] text-slate-500 mt-1">
              Morning pickup completed safely
            </p>
          </CardContent>
        </Card>

        <Card className="border-slate-200/80 shadow-xs">
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <span className="text-[11px] font-semibold uppercase tracking-wider text-slate-500">
              Route Delays
            </span>
            <div className="flex h-7 w-7 items-center justify-center rounded-md bg-amber-50 text-amber-600">
              <AlertTriangle className="h-3.5 w-3.5" />
            </div>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold tracking-tight text-amber-700">1 Bus</div>
            <p className="text-[11px] text-amber-600 font-medium mt-1">
              Bus #07 delayed 10m (Goregaon traffic)
            </p>
          </CardContent>
        </Card>

        <Card className="border-slate-200/80 shadow-xs">
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <span className="text-[11px] font-semibold uppercase tracking-wider text-slate-500">
              Facility Bookings
            </span>
            <div className="flex h-7 w-7 items-center justify-center rounded-md bg-indigo-50 text-indigo-600">
              <Building className="h-3.5 w-3.5" />
            </div>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold tracking-tight text-slate-900">3 In Use</div>
            <p className="text-[11px] text-slate-500 mt-1">
              Auditorium & Labs active
            </p>
          </CardContent>
        </Card>
      </div>

      {/* Tabs */}
      <div className="flex border-b border-slate-200 text-xs font-semibold">
        <button
          onClick={() => setActiveTab("fleet")}
          className={cn(
            "pb-2.5 px-3 border-b-2 transition-colors",
            activeTab === "fleet"
              ? "border-blue-600 text-blue-600 font-bold"
              : "border-transparent text-slate-500 hover:text-slate-900"
          )}
        >
          School Bus Fleet & Live GPS
        </button>
        <button
          onClick={() => setActiveTab("facilities")}
          className={cn(
            "pb-2.5 px-3 border-b-2 transition-colors",
            activeTab === "facilities"
              ? "border-blue-600 text-blue-600 font-bold"
              : "border-transparent text-slate-500 hover:text-slate-900"
          )}
        >
          Campus Facilities & Spaces
        </button>
        <button
          onClick={() => setActiveTab("library")}
          className={cn(
            "pb-2.5 px-3 border-b-2 transition-colors",
            activeTab === "library"
              ? "border-blue-600 text-blue-600 font-bold"
              : "border-transparent text-slate-500 hover:text-slate-900"
          )}
        >
          Central Library Catalog
        </button>
      </div>

      {/* TAB 1: Bus Fleet Table */}
      {activeTab === "fleet" && (
        <Card className="border-slate-200/80 shadow-xs">
          <CardHeader className="p-4 pb-3 border-b border-slate-100 flex flex-row items-center justify-between">
            <div>
              <CardTitle className="text-sm font-bold text-slate-900">
                School Transport Fleet Status
              </CardTitle>
              <CardDescription className="text-xs text-slate-500">
                Live AIS-140 GPS sensor feeds with automated delay warnings
              </CardDescription>
            </div>
          </CardHeader>
          <CardContent className="p-0">
            <Table>
              <TableHeader>
                <TableRow className="border-slate-100 bg-slate-50/50 text-[11px]">
                  <TableHead className="font-semibold text-slate-600">Bus # & Plate</TableHead>
                  <TableHead className="font-semibold text-slate-600">Assigned Route</TableHead>
                  <TableHead className="font-semibold text-slate-600">Driver & Contact</TableHead>
                  <TableHead className="font-semibold text-slate-600">Current Location / Speed</TableHead>
                  <TableHead className="font-semibold text-slate-600">Occupancy</TableHead>
                  <TableHead className="font-semibold text-slate-600">GPS Status</TableHead>
                  <TableHead className="font-semibold text-slate-600 text-right">Actions</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {mockFleet.map((bus) => (
                  <TableRow key={bus.id} className="border-slate-100 hover:bg-slate-50/60 text-xs">
                    <TableCell>
                      <div className="font-bold text-slate-900">{bus.busNumber}</div>
                      <div className="text-[11px] text-slate-400 font-mono">{bus.vehiclePlate}</div>
                    </TableCell>
                    <TableCell className="text-slate-700 font-medium">{bus.routeName}</TableCell>
                    <TableCell>
                      <div className="text-slate-900 font-medium">{bus.driverName}</div>
                      <div className="text-[11px] text-slate-500 flex items-center gap-1 font-mono">
                        <Phone className="h-2.5 w-2.5" />
                        {bus.driverPhone}
                      </div>
                    </TableCell>
                    <TableCell>
                      <div className="text-slate-800 text-xs font-medium">{bus.currentLocation}</div>
                      <div className="text-[10px] text-slate-400 font-mono">
                        {bus.speed} • Ping: {bus.lastPing}
                      </div>
                    </TableCell>
                    <TableCell className="font-mono text-slate-700 text-xs">{bus.capacity}</TableCell>
                    <TableCell>
                      {bus.gpsStatus === "ARRIVED" && (
                        <Badge className="bg-emerald-50 text-emerald-700 border-emerald-200 text-[10px] px-2 py-0">
                          At Campus
                        </Badge>
                      )}
                      {bus.gpsStatus === "ON_ROUTE" && (
                        <Badge className="bg-blue-50 text-blue-700 border-blue-200 text-[10px] px-2 py-0">
                          On Route
                        </Badge>
                      )}
                      {bus.gpsStatus === "DELAYED" && (
                        <Badge className="bg-amber-50 text-amber-700 border-amber-200 text-[10px] px-2 py-0 animate-pulse">
                          Delayed 10m
                        </Badge>
                      )}
                    </TableCell>
                    <TableCell className="text-right">
                      <Button
                        variant="outline"
                        size="sm"
                        className="h-7 text-[11px] text-blue-600 hover:bg-blue-50 border-slate-200 px-2"
                      >
                        Track GPS
                      </Button>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </CardContent>
        </Card>
      )}

      {/* TAB 2: Facilities */}
      {activeTab === "facilities" && (
        <Card className="border-slate-200/80 shadow-xs">
          <CardHeader className="p-4 pb-3 border-b border-slate-100 flex flex-row items-center justify-between">
            <div>
              <CardTitle className="text-sm font-bold text-slate-900">
                Campus Facilities & Venue Bookings
              </CardTitle>
              <CardDescription className="text-xs text-slate-500">
                Auditoriums, sports tracks, and specialized laboratory schedules
              </CardDescription>
            </div>
          </CardHeader>
          <CardContent className="p-0">
            <Table>
              <TableHeader>
                <TableRow className="border-slate-100 bg-slate-50/50 text-[11px]">
                  <TableHead className="font-semibold text-slate-600">Facility / Space</TableHead>
                  <TableHead className="font-semibold text-slate-600">Capacity</TableHead>
                  <TableHead className="font-semibold text-slate-600">Current Booking</TableHead>
                  <TableHead className="font-semibold text-slate-600">Reserved By</TableHead>
                  <TableHead className="font-semibold text-slate-600">Time Window</TableHead>
                  <TableHead className="font-semibold text-slate-600">Status</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {mockFacilities.map((fac, idx) => (
                  <TableRow key={idx} className="border-slate-100 hover:bg-slate-50/60 text-xs">
                    <TableCell className="font-bold text-slate-900">{fac.name}</TableCell>
                    <TableCell className="font-mono text-slate-700">{fac.capacity}</TableCell>
                    <TableCell className="text-slate-800 font-medium">{fac.currentBooking}</TableCell>
                    <TableCell className="text-slate-600">{fac.bookedBy}</TableCell>
                    <TableCell className="font-mono text-[11px] text-slate-500">{fac.timeSlot}</TableCell>
                    <TableCell>
                      {fac.status === "OCCUPIED" && (
                        <Badge className="bg-rose-50 text-rose-700 border-rose-200 text-[10px] px-2 py-0">
                          In Use
                        </Badge>
                      )}
                      {fac.status === "SCHEDULED" && (
                        <Badge className="bg-blue-50 text-blue-700 border-blue-200 text-[10px] px-2 py-0">
                          Scheduled
                        </Badge>
                      )}
                      {fac.status === "AVAILABLE" && (
                        <Badge className="bg-emerald-50 text-emerald-700 border-emerald-200 text-[10px] px-2 py-0">
                          Available
                        </Badge>
                      )}
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </CardContent>
        </Card>
      )}

      {/* TAB 3: Library */}
      {activeTab === "library" && (
        <Card className="border-slate-200/80 shadow-xs">
          <CardHeader className="p-4 pb-3 border-b border-slate-100">
            <CardTitle className="text-sm font-bold text-slate-900">
              Central Library Collection & Circulation
            </CardTitle>
            <CardDescription className="text-xs text-slate-500">
              14,500 cataloged volumes with automated RFID barcode circulation
            </CardDescription>
          </CardHeader>
          <CardContent className="p-0">
            <Table>
              <TableHeader>
                <TableRow className="border-slate-100 bg-slate-50/50 text-[11px]">
                  <TableHead className="font-semibold text-slate-600">Book Title</TableHead>
                  <TableHead className="font-semibold text-slate-600">Author</TableHead>
                  <TableHead className="font-semibold text-slate-600">ISBN Code</TableHead>
                  <TableHead className="font-semibold text-slate-600">Stock Status</TableHead>
                  <TableHead className="font-semibold text-slate-600">Physical Shelf Location</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {mockLibrary.map((b, idx) => (
                  <TableRow key={idx} className="border-slate-100 hover:bg-slate-50/60 text-xs">
                    <TableCell className="font-bold text-slate-900">{b.title}</TableCell>
                    <TableCell className="text-slate-700">{b.author}</TableCell>
                    <TableCell className="font-mono text-[11px] text-slate-500">{b.isbn}</TableCell>
                    <TableCell className="font-mono">
                      <span className="font-bold text-emerald-600">{b.totalCopies - b.onLoan} Available</span>{" "}
                      <span className="text-[11px] text-slate-400">({b.onLoan} on loan)</span>
                    </TableCell>
                    <TableCell className="text-slate-600 text-xs">{b.shelf}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </CardContent>
        </Card>
      )}
    </div>
  );
}

