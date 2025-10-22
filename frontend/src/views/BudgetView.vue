<template>
  <section class="budget">
    <header class="budget__header">
      <div>
        <h1>预算中心</h1>
        <p>掌握实时开销情况，支持语音记账与分类管理。</p>
      </div>
      <div class="header-actions">
        <button class="ghost-btn" type="button">语音记账</button>
        <button class="primary-btn" type="button">导出 CSV</button>
      </div>
    </header>

    <section class="budget__grid">
      <article class="panel">
        <h2>预算概览</h2>
        <div class="overview">
          <div>
            <div class="overview__label">总预算</div>
            <div class="overview__value">¥ 18,000</div>
          </div>
          <div>
            <div class="overview__label">已用</div>
            <div class="overview__value text-warning">¥ 6,300</div>
          </div>
          <div>
            <div class="overview__label">剩余</div>
            <div class="overview__value text-success">¥ 11,700</div>
          </div>
        </div>
      </article>

      <article class="panel">
        <h2>分类统计</h2>
        <ul class="category-list">
          <li><span>交通</span> <span>¥ 2,100</span></li>
          <li><span>住宿</span> <span>¥ 2,800</span></li>
          <li><span>餐饮</span> <span>¥ 1,050</span></li>
          <li><span>娱乐</span> <span>¥ 350</span></li>
        </ul>
      </article>
    </section>

    <section class="panel">
      <div class="panel__header">
        <h2>记账明细</h2>
      </div>
      <table class="record-table">
        <thead>
          <tr>
            <th>日期</th>
            <th>类别</th>
            <th>金额</th>
            <th>来源</th>
            <th>备注</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="record in mockRecords" :key="record.id">
            <td>{{ record.date }}</td>
            <td>{{ record.category }}</td>
            <td>¥ {{ record.amount.toLocaleString() }}</td>
            <td>{{ record.method }}</td>
            <td>{{ record.notes }}</td>
          </tr>
        </tbody>
      </table>
    </section>
  </section>
</template>

<script setup lang="ts">
const mockRecords = [
  {
    id: "1",
    date: "2025-02-01",
    category: "交通",
    amount: 560,
    method: "语音",
    notes: "羽田机场到酒店接驳车",
  },
  {
    id: "2",
    date: "2025-02-01",
    category: "餐饮",
    amount: 280,
    method: "手动",
    notes: "浅草寺午餐",
  },
  {
    id: "3",
    date: "2025-02-02",
    category: "住宿",
    amount: 1400,
    method: "语音",
    notes: "新宿酒店两晚",
  },
];
</script>

<style scoped>
.budget {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.budget__header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 24px;
}

.budget__header h1 {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
  color: #16213e;
}

.budget__header p {
  margin: 8px 0 0;
  color: #6b7280;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.ghost-btn,
.primary-btn {
  padding: 10px 16px;
  border-radius: 10px;
  border: 1px solid transparent;
  font-size: 14px;
  cursor: pointer;
}

.ghost-btn {
  border-color: #cbd5f5;
  color: #3b52e6;
  background: rgba(82, 113, 255, 0.08);
}

.primary-btn {
  background: #5271ff;
  color: white;
}

.budget__grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: 20px;
}

.panel {
  background: white;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 12px 36px -24px rgba(20, 36, 60, 0.25);
}

.panel__header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.panel__header h2 {
  margin: 0;
}

.overview {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
  gap: 16px;
  margin-top: 20px;
}

.overview__label {
  color: #6b7280;
  font-size: 13px;
}

.overview__value {
  font-size: 20px;
  font-weight: 600;
  color: #16213e;
}

.text-warning {
  color: #d97706;
}

.text-success {
  color: #059669;
}

.category-list {
  list-style: none;
  margin: 16px 0 0;
  padding: 0;
  display: flex;
  flex-direction: column;
  gap: 12px;
  color: #4b5563;
}

.category-list li {
  display: flex;
  justify-content: space-between;
}

.record-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 16px;
  font-size: 14px;
}

.record-table th,
.record-table td {
  padding: 12px 8px;
  text-align: left;
  border-bottom: 1px solid #e5e7eb;
}

.record-table th {
  font-weight: 600;
  color: #6b7280;
}
</style>
