<template>
  <section class="plans">
    <header class="plans__header">
      <div>
        <h1>我的行程</h1>
        <p>管理不同目的地的旅行计划，随时调整每日路线与偏好。</p>
      </div>
      <button class="primary-btn" type="button">新建行程</button>
    </header>

    <div class="plans__grid">
      <article class="plan-card" v-for="plan in mockPlans" :key="plan.id">
        <div class="plan-card__head">
          <h2>{{ plan.title }}</h2>
          <span :class="['status', `status--${plan.status}`]">{{
            plan.statusLabel
          }}</span>
        </div>
        <p class="plan-card__meta">
          {{ plan.destination }} · {{ plan.days }} 天 · 预算 ¥{{
            plan.budget.toLocaleString()
          }}
        </p>
        <p class="plan-card__desc">{{ plan.description }}</p>
        <footer class="plan-card__footer">
          <button type="button" class="ghost-btn">查看详情</button>
          <button type="button" class="ghost-btn">复制</button>
        </footer>
      </article>
    </div>
  </section>
</template>

<script setup lang="ts">
const mockPlans = [
  {
    id: "tokyo-family",
    title: "东京亲子五日游",
    destination: "日本 · 东京",
    days: 5,
    budget: 12000,
    status: "active",
    statusLabel: "进行中",
    description: "聚焦亲子主题，包含全天候儿童乐园与动漫体验。",
  },
  {
    id: "sicily",
    title: "西西里慢旅行",
    destination: "意大利 · 西西里",
    days: 7,
    budget: 18000,
    status: "draft",
    statusLabel: "草稿",
    description: "在地文化探索，覆盖酒庄、小镇和地中海海岸。",
  },
];
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
  opacity: 0.9;
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

.plan-card__meta {
  margin: 0;
  color: #6b7280;
  font-size: 14px;
}

.plan-card__desc {
  margin: 0;
  color: #4b5563;
  line-height: 1.6;
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
</style>
