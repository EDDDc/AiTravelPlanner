<template>
  <section v-if="plan" class="plan-detail">
    <header class="plan-detail__header">
      <div>
        <h1>{{ plan.title }}</h1>
        <p>
          {{ plan.destinations.join(" · ") }} ·
          {{ formatDate(plan.startDate) }} 至 {{ formatDate(plan.endDate) }}
        </p>
      </div>
      <RouterLink class="ghost-btn" to="/plans">返回列表</RouterLink>
    </header>

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
import { useRoute } from "vue-router";
import { usePlanStore } from "../stores/plans";

const planStore = usePlanStore();
const route = useRoute();
const planId = route.params.planId as string;

onMounted(() => {
  if (!planStore.currentPlan || planStore.currentPlan.id !== planId) {
    planStore.fetchPlanById(planId).catch(() => {
      /* 错误已在 store 中处理 */
    });
  }
});

watch(
  () => route.params.planId,
  (newId) => {
    if (typeof newId === "string" && newId !== planStore.currentPlan?.id) {
      planStore.fetchPlanById(newId);
    }
  },
);

const plan = computed(() => planStore.currentPlan);

function formatDate(value: string) {
  return new Date(value).toLocaleDateString();
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
</style>
