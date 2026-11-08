"use client";

import React, { useState } from "react";
import {
  CreditCard,
  CheckCircle2,
  AlertCircle,
  Download,
  Receipt,
  ShieldCheck,
  QrCode,
  ArrowRight,
  ExternalLink,
  Sparkles,
} from "lucide-react";
import { useParentContext } from "@/components/parent/parent-context";
import { MOCK_PARENT_FEES, FeeItem } from "@/lib/mock/parent-mock";
import { Button } from "@/components/ui/button";

export default function ParentFeesPage() {
  const { activeChild } = useParentContext();
  const [fees, setFees] = useState<FeeItem[]>(MOCK_PARENT_FEES);
  const [selectedInvoice, setSelectedInvoice] = useState<FeeItem | null>(null);
  const [isPaying, setIsPaying] = useState(false);
  const [paySuccess, setPaySuccess] = useState(false);

  const childFees = fees.filter((f) => f.childId === activeChild.id);
  const pendingInvoice = childFees.find((f) => f.status === "PENDING" || f.status === "OVERDUE");

  const handleSimulatePayment = () => {
    if (!selectedInvoice) return;
    setIsPaying(true);
    setTimeout(() => {
      setFees((prev) =>
        prev.map((f) =>
          f.id === selectedInvoice.id
            ? {
                ...f,
                status: "PAID",
                paidAmount: f.totalAmount,
                balanceAmount: 0,
                paidDate: new Date().toISOString().split("T")[0],
                receiptNumber: "REC-2026-" + Math.floor(1000 + Math.random() * 9000),
              }
            : f
        )
      );
      setIsPaying(false);
      setPaySuccess(true);
    }, 1200);
  };

  return (
    <div className="mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8 space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-xl sm:text-2xl font-bold tracking-tight">Fee Management & Online Receipts</h1>
          <p className="text-xs text-muted-foreground mt-0.5">
            Ward: <strong className="text-foreground">{activeChild.fullName}</strong> ({activeChild.gradeLevel}-{activeChild.section}) • Tax Exempted under Sec 80G
          </p>
        </div>
        <div className="flex items-center gap-2">
          <span className="flex items-center gap-1.5 rounded-full bg-emerald-500/10 px-3 py-1 text-xs font-medium text-emerald-600 dark:text-emerald-400 border border-emerald-500/20">
            <ShieldCheck className="h-3.5 w-3.5" />
            256-Bit Encrypted Payment Gateway
          </span>
        </div>
      </div>

      {/* Outstanding Due Callout Banner */}
      {pendingInvoice ? (
        <div className="rounded-2xl border border-amber-500/40 bg-gradient-to-r from-amber-500/10 via-card to-card p-5 sm:p-6 shadow-sm">
          <div className="flex flex-col md:flex-row items-start md:items-center justify-between gap-6">
            <div className="space-y-2">
              <div className="flex items-center gap-2">
                <span className="rounded-full bg-amber-500 text-white p-1">
                  <AlertCircle className="h-4 w-4" />
                </span>
                <span className="text-xs font-bold uppercase tracking-wider text-amber-600 dark:text-amber-400 font-mono">
                  Pending Invoice: {pendingInvoice.invoiceNumber}
                </span>
              </div>
              <h2 className="text-2xl font-bold tracking-tight">
                ₹{pendingInvoice.balanceAmount.toLocaleString()}{" "}
                <span className="text-xs font-normal text-muted-foreground font-sans">due by {pendingInvoice.dueDate}</span>
              </h2>
              <p className="text-xs text-muted-foreground">
                Term: <strong className="text-foreground">{pendingInvoice.term}</strong> • Surcharge of ₹50/day applicable after due date.
              </p>
            </div>

            <Button
              onClick={() => {
                setSelectedInvoice(pendingInvoice);
                setPaySuccess(false);
              }}
              size="lg"
              className="bg-emerald-600 hover:bg-emerald-700 text-white font-semibold text-sm px-6 shadow-md gap-2 w-full md:w-auto"
            >
              <CreditCard className="h-4 w-4" />
              Pay ₹{pendingInvoice.balanceAmount.toLocaleString()} Online
            </Button>
          </div>

          {/* Itemized Heads Breakdown */}
          <div className="mt-5 border-t border-border/80 pt-4">
            <div className="text-xs font-semibold text-muted-foreground uppercase tracking-wider mb-2.5">
              Itemized Fee Heads Breakdown
            </div>
            <div className="grid grid-cols-1 sm:grid-cols-3 gap-3">
              {pendingInvoice.heads.map((head, i) => (
                <div key={i} className="rounded-xl border border-border/60 bg-muted/30 p-3 flex justify-between items-center text-xs">
                  <span className="text-muted-foreground">{head.name}</span>
                  <span className="font-mono font-semibold">₹{head.amount.toLocaleString()}</span>
                </div>
              ))}
            </div>
          </div>
        </div>
      ) : (
        <div className="rounded-2xl border border-emerald-500/30 bg-emerald-500/5 p-6 text-center space-y-2">
          <div className="inline-flex h-12 w-12 items-center justify-center rounded-full bg-emerald-500/15 text-emerald-600 dark:text-emerald-400">
            <CheckCircle2 className="h-6 w-6" />
          </div>
          <h2 className="text-base font-bold text-foreground">Zero Outstanding Dues for {activeChild.fullName}</h2>
          <p className="text-xs text-muted-foreground max-w-md mx-auto">
            All registered academic terms and transport fees for the academic year 2026-27 are fully settled.
          </p>
        </div>
      )}

      {/* Payment History & Tax Receipts */}
      <div className="rounded-2xl border border-border/80 bg-card p-5 sm:p-6 shadow-sm">
        <div className="flex items-center justify-between mb-4">
          <div>
            <h2 className="text-base font-semibold tracking-tight flex items-center gap-2">
              <Receipt className="h-4 w-4 text-emerald-600 dark:text-emerald-400" />
              Payment History & Official Tax Receipts
            </h2>
            <p className="text-xs text-muted-foreground">Download stamped 80G tax-exempt receipts for income tax rebate</p>
          </div>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left text-xs">
            <thead>
              <tr className="border-b border-border/80 text-muted-foreground font-mono">
                <th className="pb-3 font-medium">Receipt No.</th>
                <th className="pb-3 font-medium">Term / Description</th>
                <th className="pb-3 font-medium">Paid Date</th>
                <th className="pb-3 font-medium">Amount</th>
                <th className="pb-3 font-medium">Payment Mode</th>
                <th className="pb-3 font-medium">Status</th>
                <th className="pb-3 font-medium text-right">Action</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-border/60">
              {childFees.map((fee) => (
                <tr key={fee.id} className="hover:bg-muted/30 transition-colors">
                  <td className="py-3.5 font-mono font-medium">
                    {fee.receiptNumber ? (
                      <span className="text-foreground">{fee.receiptNumber}</span>
                    ) : (
                      <span className="text-muted-foreground italic">Pending</span>
                    )}
                  </td>
                  <td className="py-3.5">
                    <div className="font-medium text-foreground">{fee.term}</div>
                    <div className="text-[11px] text-muted-foreground font-mono">{fee.invoiceNumber}</div>
                  </td>
                  <td className="py-3.5 font-mono text-muted-foreground">
                    {fee.paidDate || fee.dueDate}
                  </td>
                  <td className="py-3.5 font-mono font-semibold text-foreground">
                    ₹{fee.totalAmount.toLocaleString()}
                  </td>
                  <td className="py-3.5">
                    <span className="rounded bg-muted px-2 py-0.5 text-[10px] font-mono">
                      {fee.status === "PAID" ? "ONLINE (Razorpay)" : "Pending"}
                    </span>
                  </td>
                  <td className="py-3.5">
                    <span
                      className={`inline-flex items-center gap-1 rounded-full px-2.5 py-0.5 text-[10px] font-bold ${
                        fee.status === "PAID"
                          ? "bg-emerald-500/15 text-emerald-600 dark:text-emerald-400"
                          : "bg-amber-500/15 text-amber-600 dark:text-amber-400"
                      }`}
                    >
                      {fee.status}
                    </span>
                  </td>
                  <td className="py-3.5 text-right">
                    {fee.status === "PAID" ? (
                      <Button
                        variant="outline"
                        size="sm"
                        onClick={() => alert(`Downloading official stamped receipt ${fee.receiptNumber} for ${activeChild.fullName}...`)}
                        className="h-8 text-xs gap-1.5 border-border/80"
                      >
                        <Download className="h-3.5 w-3.5" /> Receipt PDF
                      </Button>
                    ) : (
                      <Button
                        size="sm"
                        onClick={() => {
                          setSelectedInvoice(fee);
                          setPaySuccess(false);
                        }}
                        className="h-8 text-xs bg-emerald-600 hover:bg-emerald-700 text-white"
                      >
                        Pay Now
                      </Button>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* 1-Click Pay Modal */}
      {selectedInvoice && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/60 backdrop-blur-sm p-4">
          <div className="w-full max-w-md rounded-2xl border border-border bg-card p-6 shadow-2xl space-y-4 animate-in fade-in zoom-in-95">
            <div className="flex items-center justify-between border-b border-border pb-3">
              <div>
                <h3 className="text-base font-bold text-foreground">Secure Fee Checkout</h3>
                <p className="text-xs text-muted-foreground">Apex Academy Payment Gateway</p>
              </div>
              <button
                onClick={() => setSelectedInvoice(null)}
                className="text-muted-foreground hover:text-foreground text-sm font-semibold"
              >
                ✕
              </button>
            </div>

            {!paySuccess ? (
              <div className="space-y-4">
                <div className="rounded-xl bg-muted/40 p-3.5 space-y-2">
                  <div className="flex justify-between text-xs">
                    <span className="text-muted-foreground">Student Name:</span>
                    <strong className="text-foreground">{activeChild.fullName}</strong>
                  </div>
                  <div className="flex justify-between text-xs">
                    <span className="text-muted-foreground">Class & Section:</span>
                    <span>{activeChild.gradeLevel}-{activeChild.section}</span>
                  </div>
                  <div className="flex justify-between text-xs">
                    <span className="text-muted-foreground">Invoice No:</span>
                    <span className="font-mono">{selectedInvoice.invoiceNumber}</span>
                  </div>
                  <div className="border-t border-border/60 pt-2 flex justify-between text-sm">
                    <span className="font-semibold">Total Payable:</span>
                    <strong className="text-emerald-600 dark:text-emerald-400 font-mono text-base">
                      ₹{selectedInvoice.balanceAmount.toLocaleString()}
                    </strong>
                  </div>
                </div>

                {/* Simulated Payment Modes */}
                <div className="space-y-2">
                  <div className="text-xs font-semibold text-muted-foreground uppercase tracking-wider">
                    Select Payment Method
                  </div>
                  <div className="grid grid-cols-2 gap-2">
                    <button className="flex items-center gap-2 rounded-xl border-2 border-emerald-500 bg-emerald-500/5 p-3 text-left transition-colors">
                      <QrCode className="h-5 w-5 text-emerald-600 dark:text-emerald-400" />
                      <div>
                        <div className="text-xs font-bold leading-tight">Instant UPI / QR</div>
                        <div className="text-[10px] text-muted-foreground">GPay, PhonePe, Paytm</div>
                      </div>
                    </button>
                    <button className="flex items-center gap-2 rounded-xl border border-border bg-card p-3 text-left hover:border-emerald-500/50 transition-colors">
                      <CreditCard className="h-5 w-5 text-muted-foreground" />
                      <div>
                        <div className="text-xs font-bold leading-tight">Cards / NetBanking</div>
                        <div className="text-[10px] text-muted-foreground">All major banks</div>
                      </div>
                    </button>
                  </div>
                </div>

                <div className="pt-2">
                  <Button
                    onClick={handleSimulatePayment}
                    disabled={isPaying}
                    className="w-full bg-emerald-600 hover:bg-emerald-700 text-white font-semibold py-2.5 text-sm"
                  >
                    {isPaying ? "Verifying with Bank..." : `Pay ₹${selectedInvoice.balanceAmount.toLocaleString()}`}
                  </Button>
                </div>
              </div>
            ) : (
              <div className="text-center py-4 space-y-3">
                <div className="inline-flex h-12 w-12 items-center justify-center rounded-full bg-emerald-500/15 text-emerald-600">
                  <CheckCircle2 className="h-7 w-7" />
                </div>
                <h4 className="text-base font-bold text-foreground">Payment Settled Successfully!</h4>
                <p className="text-xs text-muted-foreground">
                  Receipt has been generated and double-entry ledger updated idempotently. An SMS & WhatsApp confirmation has been dispatched.
                </p>
                <div className="pt-2">
                  <Button
                    onClick={() => setSelectedInvoice(null)}
                    className="w-full bg-emerald-600 hover:bg-emerald-700 text-white text-xs"
                  >
                    Close & View Receipt
                  </Button>
                </div>
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  );
}

