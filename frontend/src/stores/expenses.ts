import { defineStore } from "pinia";
import { computed, ref } from "vue";
import http from "../services/http";

export interface ExpenseItem {
  id: string;
  planId: string;
  amount: number;
  currency: string;
  category: string;
  method: string;
  recordedAt: string;
  notes?: string | null;
  transcript?: string | null;
}

export interface ExpenseSummaryCategory {
  category: string;
  amount: number;
}

export interface ExpenseSummary {
  totalAmount: number;
  currency: string;
  categories: ExpenseSummaryCategory[];
}

interface RawExpenseItem {
  id: string;
  planId: string;
  amount: string | number;
  currency: string;
  category: string;
  method: string;
  recordedAt: string;
  notes?: string | null;
  transcript?: string | null;
}

interface RawSummaryCategory {
  category: string;
  amount: string | number;
}

interface RawExpenseSummary {
  totalAmount: string | number;
  currency: string;
  categories: RawSummaryCategory[];
}

export const useExpenseStore = defineStore("expenses", () => {
  const expenses = ref<ExpenseItem[]>([]);
  const summary = ref<ExpenseSummary | null>(null);
  const loadingList = ref(false);
  const loadingSummary = ref(false);
  const lastError = ref("");

  const hasExpenses = computed(() => expenses.value.length > 0);
  const loading = computed(() => loadingList.value || loadingSummary.value);

  function mapExpense(raw: RawExpenseItem): ExpenseItem {
    return {
      id: raw.id,
      planId: raw.planId,
      amount:
        typeof raw.amount === "string"
          ? Number.parseFloat(raw.amount)
          : raw.amount,
      currency: raw.currency,
      category: raw.category,
      method: raw.method,
      recordedAt: raw.recordedAt,
      notes: raw.notes,
      transcript: raw.transcript,
    };
  }

  function mapSummary(raw: RawExpenseSummary): ExpenseSummary {
    return {
      totalAmount:
        typeof raw.totalAmount === "string"
          ? Number.parseFloat(raw.totalAmount)
          : raw.totalAmount,
      currency: raw.currency,
      categories: raw.categories.map((item) => ({
        category: item.category,
        amount:
          typeof item.amount === "string"
            ? Number.parseFloat(item.amount)
            : item.amount,
      })),
    };
  }

  async function fetchExpenses(
    planId: string,
    params: Record<string, string> = {},
  ) {
    loadingList.value = true;
    lastError.value = "";
    try {
      const response = await http.get<RawExpenseItem[]>(
        `/api/plans/${planId}/expenses`,
        { params },
      );
      expenses.value = response.data.map(mapExpense);
    } catch (error) {
      console.error("Failed to fetch expenses", error);
      lastError.value = "无法获取费用记录，请稍后重试。";
      throw error;
    } finally {
      loadingList.value = false;
    }
  }

  async function fetchSummary(
    planId: string,
    params: Record<string, string> = {},
  ) {
    loadingSummary.value = true;
    lastError.value = "";
    try {
      const response = await http.get<RawExpenseSummary>(
        `/api/plans/${planId}/expenses/summary`,
        { params },
      );
      summary.value = mapSummary(response.data);
    } catch (error) {
      console.error("Failed to fetch expense summary", error);
      lastError.value = "无法获取费用汇总，请稍后重试。";
      summary.value = null;
      throw error;
    } finally {
      loadingSummary.value = false;
    }
  }

  function clear() {
    expenses.value = [];
    summary.value = null;
    lastError.value = "";
  }

  return {
    expenses,
    summary,
    hasExpenses,
    loading,
    lastError,
    fetchExpenses,
    fetchSummary,
    clear,
  };
});
