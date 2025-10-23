import { defineStore } from "pinia";
import { computed, reactive } from "vue";
import http from "../services/http";

export interface TravelPlanSummary {
  id: string;
  title: string;
  destinations: string[];
  startDate: string;
  endDate: string;
  budgetTotal?: number | null;
  status: string;
}

export interface TravelPlanDetail extends TravelPlanSummary {
  preferences: Record<string, unknown>;
  days: Array<{
    dayIndex: number;
    date: string | null;
    summary?: string | null;
    notes?: string | null;
    activities: Array<{
      type: string;
      name: string;
      description?: string | null;
      startTime?: string | null;
      endTime?: string | null;
      costEstimate?: number | null;
      latitude?: number | null;
      longitude?: number | null;
      address?: string | null;
    }>;
  }>;
}

export interface CreatePlanPayload {
  title: string;
  destinations: string[];
  startDate: string;
  endDate: string;
  budgetTotal?: number | null;
  preferences?: Record<string, unknown>;
}

interface PlanState {
  summaries: TravelPlanSummary[];
  details: Record<string, TravelPlanDetail>;
  currentPlanId: string | null;
  loading: boolean;
  creating: boolean;
  lastError: string;
}

function detailToSummary(detail: TravelPlanDetail): TravelPlanSummary {
  return {
    id: detail.id,
    title: detail.title,
    destinations: detail.destinations,
    startDate: detail.startDate,
    endDate: detail.endDate,
    budgetTotal: detail.budgetTotal,
    status: detail.status,
  };
}

export const usePlanStore = defineStore("plans", () => {
  const state = reactive<PlanState>({
    summaries: [],
    details: {},
    currentPlanId: null,
    loading: false,
    creating: false,
    lastError: "",
  });

  const planList = computed(() => state.summaries);
  const hasPlans = computed(() => state.summaries.length > 0);

  const currentPlan = computed(() => {
    if (!state.currentPlanId) {
      return null;
    }
    return state.details[state.currentPlanId] ?? null;
  });

  function setCurrentPlan(planId: string | null) {
    state.currentPlanId = planId;
  }

  function clearError() {
    state.lastError = "";
  }

  async function fetchPlans() {
    state.loading = true;
    state.lastError = "";
    try {
      const response = await http.get<TravelPlanSummary[]>("/api/plans");
      state.summaries = response.data;
      for (const summary of response.data) {
        const detail = state.details[summary.id];
        if (detail) {
          state.details[summary.id] = {
            ...detail,
            ...summary,
          };
        }
      }
    } catch (error) {
      console.error("Failed to load plans", error);
      state.lastError = "无法获取行程列表，请稍后重试。";
    } finally {
      state.loading = false;
    }
  }

  async function fetchPlanById(planId: string) {
    state.loading = true;
    state.lastError = "";
    try {
      const response = await http.get<TravelPlanDetail>(`/api/plans/${planId}`);
      state.details[planId] = response.data;
      state.currentPlanId = planId;
      return response.data;
    } catch (error) {
      console.error("Failed to load plan", planId, error);
      state.lastError = "无法获取行程详情，请稍后重试。";
      throw error;
    } finally {
      state.loading = false;
    }
  }

  async function createPlan(payload: CreatePlanPayload) {
    state.creating = true;
    state.lastError = "";
    try {
      const response = await http.post<TravelPlanDetail>("/api/plans", payload);
      const created = response.data;
      state.details[created.id] = created;
      state.summaries = [
        detailToSummary(created),
        ...state.summaries.filter((plan) => plan.id !== created.id),
      ];
      state.currentPlanId = created.id;
      return created;
    } catch (error) {
      console.error("Failed to create plan", error);
      state.lastError = "创建行程失败，请检查填写内容或稍后重试。";
      throw error;
    } finally {
      state.creating = false;
    }
  }

  return {
    planList,
    hasPlans,
    currentPlan,
    loading: computed(() => state.loading),
    creating: computed(() => state.creating),
    lastError: computed({
      get: () => state.lastError,
      set: (value: string) => {
        state.lastError = value;
      },
    }),
    clearError,
    setCurrentPlan,
    fetchPlans,
    fetchPlanById,
    createPlan,
  };
});
