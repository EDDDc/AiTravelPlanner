<template>
  <section class="plans">
    <header class="plans__header">
      <div>
        <h1>我的行程</h1>
        <p>连接后端行程列表，查看和创建新的规划。</p>
      </div>
      <button class="primary-btn" type="button" @click="toggleForm">
        {{ showForm ? "取消" : "新建行程" }}
      </button>
    </header>

    <transition name="fade">
      <form v-if="showForm" class="plan-form" @submit.prevent="handleSubmit">
        <div class="form-row">
          <label>
            标题
            <input
              v-model.trim="form.title"
              type="text"
              placeholder="例如：东京亲子三日行"
              required
            />
          </label>
          <label>
            目的地（用逗号分隔）
            <input
              v-model.trim="form.destinations"
              type="text"
              placeholder="东京, 横滨"
              required
            />
          </label>
        </div>
        <div class="form-row">
          <label>
            开始日期
            <input v-model="form.startDate" type="date" required />
          </label>
          <label>
            结束日期
            <input v-model="form.endDate" type="date" required />
          </label>
        </div>
        <div class="form-actions">
          <span v-if="planStore.creating" class="hint"
            >正在创建占位行程...</span
          >
          <span v-else-if="planStore.lastError" class="error">{{
            planStore.lastError
          }}</span>
          <button
            class="primary-btn"
            type="submit"
            :disabled="planStore.creating"
          >
            创建并查看详情
          </button>
        </div>
      </form>
    </transition>

    <div v-if="planStore.loading" class="loading">正在加载行程列表...</div>

    <p v-else-if="!planStore.hasPlans" class="empty">
      暂无行程，点击上方“新建行程”按钮开始规划。
    </p>

    <div v-else class="plans__grid">
      <article class="plan-card" v-for="plan in planStore.plans" :key="plan.id">
        <div class="plan-card__head">
          <h2>{{ plan.title }}</h2>
          <span :class="['status', `status--${plan.status.toLowerCase()}`]">
            {{ statusLabel(plan.status) }}
          </span>
        </div>
        <p class="plan-card__meta">
          {{ plan.destinations.join(" · ") }} ·
          {{ formatDate(plan.startDate) }} 至
          {{ formatDate(plan.endDate) }}
        </p>
        <footer class="plan-card__footer">
          <button type="button" class="ghost-btn" @click="goDetail(plan.id)">
            查看详情
          </button>
        </footer>
      </article>
    </div>
  </section>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import { usePlanStore } from "../stores/plans";

const planStore = usePlanStore();
const router = useRouter();
const showForm = ref(false);

const form = reactive({
  title: "",
  destinations: "",
  startDate: "",
  endDate: "",
});

onMounted(() => {
  planStore.fetchPlans();
});

function toggleForm() {
  showForm.value = !showForm.value;
  if (!showForm.value) {
    resetForm();
  }
}

function resetForm() {
  form.title = "";
  form.destinations = "";
  form.startDate = "";
  form.endDate = "";
}

function formatDate(value: string) {
  return new Date(value).toLocaleDateString();
}

function statusLabel(status: string) {
  switch (status) {
    case "ACTIVE":
      return "进行中";
    case "ARCHIVED":
      return "已归档";
    default:
      return "草稿";
  }
}

async function handleSubmit() {
  const destinations = form.destinations
    .split(",")
    .map((item) => item.trim())
    .filter(Boolean);

  if (destinations.length === 0) {
    planStore.lastError = "请至少输入一个目的地。";
    return;
  }

  try {
    const created = await planStore.createPlan({
      title: form.title,
      destinations,
      startDate: form.startDate,
      endDate: form.endDate,
    });
    resetForm();
    showForm.value = false;
    router.push({ name: "plan-detail", params: { planId: created.id } });
  } catch (error) {
    // 错误信息已由 store 记录
  }
}

function goDetail(planId: string) {
  router.push({ name: "plan-detail", params: { planId } });
}
</script>

<style scoped>
.plans {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.plans__header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 24px;
}

.plans__header h1 {
  margin: 0 0 8px;
  font-size: 24px;
  font-weight: 600;
  color: #16213e;
}

.plans__header p {
  margin: 0;
  color: #6b7280;
}

.primary-btn {
  background-color: #5271ff;
  color: white;
  border: none;
  border-radius: 10px;
  padding: 10px 18px;
  font-size: 14px;
  cursor: pointer;
  transition: opacity 0.2s ease;
}

.primary-btn:hover {
  opacity: 0.92;
}

.plan-form {
  background: white;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 12px 36px -24px rgba(20, 36, 60, 0.25);
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.form-row {
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
}

.form-row label {
  flex: 1 1 240px;
  display: flex;
  flex-direction: column;
  gap: 8px;
  color: #374151;
  font-size: 14px;
}

.form-row input {
  padding: 12px 14px;
  border-radius: 10px;
  border: 1px solid #d0d5dd;
  font-size: 14px;
}

.form-actions {
  display: flex;
  align-items: center;
  gap: 16px;
}

.hint {
  color: #3b52e6;
}

.error {
  color: #dc2626;
}

.loading,
.empty {
  text-align: center;
  color: #6b7280;
}

.plans__grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 20px;
}

.plan-card {
  background: white;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 12px 36px -24px rgba(20, 36, 60, 0.25);
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.plan-card__head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
}

.plan-card__head h2 {
  margin: 0;
  font-size: 18px;
  color: #16213e;
}

.status {
  font-size: 12px;
  padding: 4px 8px;
  border-radius: 999px;
  text-transform: uppercase;
  background: #e5e7eb;
  color: #374151;
}

.status--active {
  background: rgba(82, 113, 255, 0.15);
  color: #3b52e6;
}

.status--draft {
  background: rgba(250, 204, 21, 0.18);
  color: #c07f00;
}

.status--archived {
  background: rgba(148, 163, 184, 0.25);
  color: #475569;
}

.plan-card__meta {
  margin: 0;
  color: #6b7280;
  font-size: 14px;
}

.plan-card__footer {
  margin-top: auto;
  display: flex;
  gap: 12px;
}

.ghost-btn {
  background: transparent;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  padding: 8px 12px;
  color: #374151;
  cursor: pointer;
  font-size: 13px;
  transition: border-color 0.2s ease;
}

.ghost-btn:hover {
  border-color: #5271ff;
  color: #5271ff;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
