<template>
  <section v-if="plan" class="plan-detail">
    <header class="plan-detail__header">
      <div>
        <h1>{{ plan.title }}</h1>
        <p>{{ plan.destinations.join(" · ") }}</p>
      </div>
      <RouterLink class="ghost-btn" to="/plans">返回列表</RouterLink>
    </header>

    <section class="plan-detail__meta">
      <div class="meta-item">
        <span class="meta-label">行程日期</span>
        <span class="meta-value">
          {{ formatDate(plan.startDate) }} – {{ formatDate(plan.endDate) }} （{{
            durationText(plan.startDate, plan.endDate)
          }}）
        </span>
      </div>
      <div v-if="plan.budgetTotal" class="meta-item">
        <span class="meta-label">预计总预算</span>
        <span class="meta-value">¥{{ formatCurrency(plan.budgetTotal) }}</span>
      </div>
      <div v-if="preferenceChips.length" class="meta-item meta-item--chips">
        <span class="meta-label">旅客偏好</span>
        <ul class="chip-list">
          <li v-for="chip in preferenceChips" :key="chip">{{ chip }}</li>
        </ul>
      </div>
    </section>

    <section v-if="plan.days.length" class="timeline">
      <article
        v-for="day in plan.days"
        :key="day.dayIndex"
        class="timeline__day"
      >
        <header>
          <h2>Day {{ day.dayIndex }}</h2>
          <span>{{ day.date ? formatDate(day.date) : "日期待定" }}</span>
        </header>
        <p class="timeline__summary">
          {{ day.summary || "等待 AI 生成详细安排" }}
        </p>
        <ul class="timeline__activities">
          <li v-for="(activity, index) in day.activities" :key="index">
            <div class="activity-time">
              <span>{{ activity.startTime || "--:--" }}</span>
              <span>{{ activity.endTime || "--:--" }}</span>
            </div>
            <div class="activity-content">
              <h3>{{ activity.name }}</h3>
              <p>{{ activity.description || "内容待生成" }}</p>
            </div>
          </li>
        </ul>
      </article>
    </section>

    <p v-else class="empty">当前行程尚未生成详细日程。</p>
  </section>
  <div v-else class="loading">正在加载行程详情...</div>
</template>

<script setup lang="ts">
import { computed, onMounted, watch } from "vue";
import { storeToRefs } from "pinia";
import { useRoute } from "vue-router";
import { usePlanStore } from "../stores/plans";

const planStore = usePlanStore();
const route = useRoute();
const { currentPlan } = storeToRefs(planStore);

const planId = computed(() => route.params.planId as string);

onMounted(() => {
  ensurePlanLoaded(planId.value);
});

watch(
  () => planId.value,
  (newId) => {
    ensurePlanLoaded(newId);
  },
);

const plan = computed(() => currentPlan.value);

const preferenceLabels: Record<string, string> = {
  travelers: "旅伴",
  interests: "偏好",
  notes: "备注",
};

const preferenceChips = computed(() => {
  const current = plan.value;
  if (!current) {
    return [] as string[];
  }
  const prefs = current.preferences ?? {};
  const chips: string[] = [];
  Object.entries(prefs as Record<string, unknown>).forEach(
    ([key, rawValue]) => {
      if (rawValue == null) {
        return;
      }
      const label = preferenceLabels[key] ?? key;
      if (Array.isArray(rawValue)) {
        rawValue
          .map((item) => (item == null ? "" : String(item).trim()))
          .filter(Boolean)
          .forEach((item) => chips.push(`${label} · ${item}`));
        return;
      }
      const text = String(rawValue).trim();
      if (!text) {
        return;
      }
      if (key === "notes") {
        chips.push(`${label} · ${text}`);
        return;
      }
      const segments = text
        .split(/[、,，/]/)
        .map((segment) => segment.trim())
        .filter(Boolean);
      if (!segments.length) {
        chips.push(`${label} · ${text}`);
        return;
      }
      segments.forEach((segment) => chips.push(`${label} · ${segment}`));
    },
  );
  return chips;
});

function ensurePlanLoaded(id: string) {
  if (!id) {
    return;
  }
  planStore.setCurrentPlan(id);
  if (!planStore.currentPlan || planStore.currentPlan.id !== id) {
    planStore.fetchPlanById(id).catch(() => {
      /* 错误已在 store 中处理 */
    });
  }
}

function formatDate(value: string) {
  return new Date(value).toLocaleDateString();
}

function durationText(start: string, end: string) {
  const startDate = new Date(start);
  const endDate = new Date(end);
  const diff =
    Math.round((endDate.getTime() - startDate.getTime()) / 86400000) + 1;
  return `${Math.max(diff, 1)} 天`;
}

function formatCurrency(value: number | string) {
  const numeric = typeof value === "number" ? value : Number.parseFloat(value);
  if (!Number.isFinite(numeric)) {
    return "";
  }
  return numeric.toLocaleString("zh-CN", {
    minimumFractionDigits: 0,
    maximumFractionDigits: 0,
  });
}
</script>

<style scoped>
.plan-detail {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.plan-detail__header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 24px;
}

.plan-detail__header h1 {
  margin: 0;
  font-size: 26px;
  font-weight: 600;
  color: #16213e;
}

.plan-detail__header p {
  margin: 6px 0 0;
  color: #6b7280;
}

.plan-detail__meta {
  background: white;
  border-radius: 16px;
  padding: 20px 24px;
  box-shadow: 0 12px 36px -24px rgba(20, 36, 60, 0.25);
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
}

.meta-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
  color: #4b5563;
  min-width: 200px;
}

.meta-item--chips {
  flex: 1 1 100%;
}

.meta-label {
  font-size: 12px;
  font-weight: 600;
  color: #16213e;
  text-transform: uppercase;
  letter-spacing: 0.02em;
}

.meta-value {
  font-size: 14px;
}

.chip-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin: 0;
  padding: 0;
  list-style: none;
}

.chip-list li {
  background: rgba(82, 113, 255, 0.12);
  color: #3b52e6;
  font-size: 12px;
  padding: 4px 8px;
  border-radius: 999px;
}

.timeline {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.timeline__day {
  background: white;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 12px 36px -24px rgba(20, 36, 60, 0.25);
}

.timeline__day header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.timeline__day h2 {
  margin: 0;
  font-size: 20px;
}

.timeline__summary {
  margin: 12px 0;
  color: #4b5563;
}

.timeline__activities {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.timeline__activities li {
  display: flex;
  gap: 16px;
  align-items: flex-start;
}

.activity-time {
  width: 70px;
  font-size: 13px;
  color: #6b7280;
  display: flex;
  flex-direction: column;
}

.activity-content h3 {
  margin: 0;
  font-size: 16px;
  color: #16213e;
}

.activity-content p {
  margin: 4px 0 0;
  color: #6b7280;
}

.loading,
.empty {
  text-align: center;
  color: #6b7280;
}

@media (max-width: 768px) {
  .plan-detail__header {
    flex-direction: column;
    align-items: flex-start;
  }

  .plan-detail__meta {
    flex-direction: column;
  }
}
</style>
