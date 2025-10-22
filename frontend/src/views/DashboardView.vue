<template>
  <section class="dashboard">
    <header class="dashboard__summary">
      <article class="summary-card">
        <h2>本周旅行</h2>
        <p class="summary-card__value">东京 5 日游</p>
        <p class="summary-card__hint">预算消耗 35% · 3 个行程更新待处理</p>
      </article>
      <article class="summary-card">
        <h2>下一步建议</h2>
        <ul>
          <li>为 Day 2 添加餐饮备注</li>
          <li>补充儿童友好景点推荐</li>
          <li>上传东京地铁通票凭证</li>
        </ul>
      </article>
    </header>

    <section class="dashboard__grid">
      <article class="panel">
        <div class="panel__header">
          <h3>行程时间线</h3>
          <button class="link-button" type="button">查看详细</button>
        </div>
        <ul class="timeline">
          <li>
            <span class="timeline__time">08:00</span>
            <span class="timeline__detail">羽田机场抵达，酒店专车接送</span>
          </li>
          <li>
            <span class="timeline__time">11:30</span>
            <span class="timeline__detail">浅草寺 & 仲见世通逛街</span>
          </li>
          <li>
            <span class="timeline__time">14:00</span>
            <span class="timeline__detail">晴空塔天空庭园预约</span>
          </li>
        </ul>
      </article>

      <article class="panel">
        <div class="panel__header">
          <h3>预算概览</h3>
          <button class="link-button" type="button">前往预算中心</button>
        </div>
        <div class="budget-chart">
          <div class="budget-chart__value">¥ 6,800</div>
          <p>已使用预算，占比 35%</p>
        </div>
      </article>
    </section>
  </section>
</template>

<style scoped>
.dashboard {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.dashboard__summary {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: 20px;
}

.summary-card {
  background-color: white;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 12px 36px -24px rgba(20, 36, 60, 0.25);
}

.summary-card h2 {
  margin: 0 0 12px;
  font-size: 16px;
  color: #6b7280;
  font-weight: 500;
}

.summary-card__value {
  margin: 0;
  font-size: 22px;
  font-weight: 600;
  color: #16213e;
}

.summary-card__hint {
  margin: 16px 0 0;
  color: #6b7280;
}

.summary-card ul {
  margin: 0;
  padding-left: 18px;
  color: #4b5563;
  line-height: 1.6;
}

.dashboard__grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 20px;
}

.panel {
  background: white;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 12px 36px -24px rgba(20, 36, 60, 0.25);
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.panel__header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.panel__header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #16213e;
}

.link-button {
  border: none;
  background: transparent;
  color: #5271ff;
  font-size: 14px;
  cursor: pointer;
}

.timeline {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.timeline__time {
  font-weight: 600;
  color: #5271ff;
  margin-right: 12px;
}

.timeline__detail {
  color: #4b5563;
}

.budget-chart {
  display: flex;
  flex-direction: column;
  gap: 12px;
  color: #4b5563;
}

.budget-chart__value {
  font-size: 28px;
  font-weight: 600;
  color: #16213e;
}
</style>
