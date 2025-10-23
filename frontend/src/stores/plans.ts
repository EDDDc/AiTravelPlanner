import { defineStore } from "pinia";
import { ref, computed } from "vue";
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

export const usePlanStore = defineStore("plans", () => {
  const plans = ref<TravelPlanSummary[]>([]);
  const currentPlan = ref<TravelPlanDetail | null>(null);
  const loading = ref(false);
  const creating = ref(false);
  const lastError = ref("");

  const hasPlans = computed(() => plans.value.length > 0);

  async function fetchPlans() {
    loading.value = true;
    lastError.value = "";
    try {
      const response = await http.get<TravelPlanSummary[]>("/api/plans");
      plans.value = response.data;
    } catch (error) {
      console.error("Failed to load plans", error);
      lastError.value = "无法获取行程列表，请稍后重试。";
    } finally {
      loading.value = false;
    }
  }

  async function fetchPlanById(planId: string) {
    loading.value = true;
    lastError.value = "";
    try {
      const response = await http.get<TravelPlanDetail>(`/api/plans/${planId}`);
      currentPlan.value = response.data;
      return response.data;
    } catch (error) {
      console.error("Failed to load plan", planId, error);
      lastError.value = "无法获取行程详情，请稍后重试。";
      throw error;
    } finally {
      loading.value = false;
    }
  }

  async function createPlan(payload: CreatePlanPayload) {
    creating.value = true;
    lastError.value = "";
    try {
      const response = await http.post<TravelPlanDetail>("/api/plans", payload);
      const created = response.data;
      plans.value = [created, ...plans.value];
      currentPlan.value = created;
      return created;
    } catch (error) {
      console.error("Failed to create plan", error);
      lastError.value = "创建行程失败，请检查填写内容或稍后重试。";
      throw error;
    } finally {
      creating.value = false;
    }
  }

  return {
    plans,
    currentPlan,
    loading,
    creating,
    lastError,
    hasPlans,
    fetchPlans,
    fetchPlanById,
    createPlan,
  };
});
