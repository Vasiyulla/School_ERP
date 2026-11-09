import { apiClient } from "../api-client";

export interface FeeStructureItem {
  id: string;
  name: string;
  gradeLevel: string;
  academicYearId: string;
  totalAmount: number;
  components: {
    name: string;
    amount: number;
    frequency: string;
  }[];
}

export interface InvoiceItem {
  id: string;
  invoiceNumber: string;
  studentId: string;
  studentName: string;
  totalAmount: number;
  paidAmount: number;
  dueAmount: number;
  dueDate: string;
  status: "PAID" | "PARTIAL" | "OVERDUE" | "PENDING";
}

export interface TrialBalanceItem {
  accountCode: string;
  accountName: string;
  accountCategory: string;
  debitTotal: number;
  creditTotal: number;
}

export interface TrialBalanceReport {
  asOfDate: string;
  totalDebit: number;
  totalCredit: number;
  isBalanced: boolean;
  accounts: TrialBalanceItem[];
}

export interface WalletDetails {
  walletId: string;
  studentId: string;
  currentBalance: number;
  dailySpendingLimit: number;
  status: "ACTIVE" | "FROZEN";
  recentTransactions: {
    id: string;
    type: "CREDIT_TOPUP" | "DEBIT_POS";
    amount: number;
    balanceAfter: number;
    description: string;
    timestamp: string;
  }[];
}

export const financeService = {
  // Fee Management & Invoices
  async getFeeStructures(): Promise<FeeStructureItem[]> {
    const res = await apiClient.get<FeeStructureItem[]>("/api/v1/finance/fee-structures");
    return res.data || [];
  },

  async getInvoices(): Promise<InvoiceItem[]> {
    const res = await apiClient.get<InvoiceItem[]>("/api/v1/finance/invoices");
    return res.data || [];
  },

  async applyConcession(payload: {
    studentId: string;
    concessionCategoryId: string;
    discountPercent: number;
    reason: string;
    approvedBy: string;
  }): Promise<any> {
    const res = await apiClient.post("/api/v1/finance/concessions", payload);
    return res.data;
  },

  // Cheque Processing Workflows
  async clearCheque(
    chequeId: string,
    payload: { clearedDate: string; bankReference: string }
  ): Promise<any> {
    const res = await apiClient.post(`/api/v1/finance/cheques/${chequeId}/clear`, payload);
    return res.data;
  },

  async dishonourCheque(
    chequeId: string,
    payload: { dishonourReason: string; bouncePenaltyFee: number }
  ): Promise<any> {
    const res = await apiClient.post(
      `/api/v1/finance/cheques/${chequeId}/dishonour`,
      payload
    );
    return res.data;
  },

  // Multi-Child Sibling Cart Checkout
  async getFamilyCheckout(guardianEmail: string): Promise<any> {
    const res = await apiClient.get(
      `/api/v1/finance/family-checkout?guardianEmail=${encodeURIComponent(guardianEmail)}`
    );
    return res.data;
  },

  // Immutable Double-Entry Ledger
  async getLedgerEntries(): Promise<any[]> {
    const res = await apiClient.get<any[]>("/api/v1/finance/ledger");
    return res.data || [];
  },

  async getTrialBalance(): Promise<TrialBalanceReport> {
    const res = await apiClient.get<TrialBalanceReport>("/api/v1/finance/ledger/trial-balance");
    return res.data;
  },

  // Cashless RFID Student Wallet
  async getStudentWallet(studentId: string): Promise<WalletDetails> {
    const res = await apiClient.get<WalletDetails>(`/api/v1/finance/wallets/${studentId}`);
    return res.data;
  },

  async topupWallet(
    studentId: string,
    payload: {
      amount: number;
      paymentMethod: string;
      referenceNumber: string;
    }
  ): Promise<any> {
    const res = await apiClient.post(`/api/v1/finance/wallets/${studentId}/topup`, payload);
    return res.data;
  },

  async posChargeWallet(
    studentId: string,
    payload: {
      amount: number;
      itemDescription: string;
      merchantTerminalId: string;
    }
  ): Promise<any> {
    const res = await apiClient.post(
      `/api/v1/finance/wallets/${studentId}/pos-charge`,
      payload
    );
    return res.data;
  },
};

