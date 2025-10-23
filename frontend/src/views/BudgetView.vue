<template>
  <section class="budget">
    <header class="budget__header">
      <div>
        <h1>预算中心</h1>
        <p>
          连接后端费用数据，掌握实时支出情况（语音记账与导出功能将在后续迭代提供）。
        </p>
      </div>
      <div class="header-actions">
        <button class="ghost-btn" type="button" disabled>
          语音记账（开发中）
        </button>
        <button class="primary-btn" type="button" disabled>
          导出 CSV（开发中）
        </button>
      </div>
    </header>

    <section class="plan-selector" v-if="planOptions.length">
      <label>
        选择行程
        <select v-model="selectedPlanId">
          <option v-for="plan in planOptions" :key="plan.id" :value="plan.id">
            {{ plan.title }}（{{ formatDate(plan.startDate) }} -
            {{ formatDate(plan.endDate) }}）
          </option>
        </select>
      </label>
    </section>
    <p v-else class="empty">暂无行程，请先创建行程后再使用预算功能。</p>

    <section v-if="planError" class="error">{{ planError }}</section>
    <section v-if="expenseError" class="error">{{ expenseError }}</section>

    <section v-if="isLoading" class="panel loading-panel">
      正在加载数据...
    </section>

    <template v-else-if="selectedPlanId">
      <section class="budget__grid">
        <article class="panel">
          <h2>预算概览</h2>
          <div class="overview">
            <div>
              <div class="overview__label">汇总支出</div>
              <div class="overview__value">
                {{
                  formatCurrency(summaryData.totalAmount, summaryData.currency)
                }}
              </div>
            </div>
            <div>
              <div class="overview__label">币种</div>
              <div class="overview__value">{{ summaryData.currency }}</div>
            </div>
          </div>
        </article>

        <article class="panel">
          <h2>分类统计</h2>
          <ul class="category-list" v-if="summaryData.categories.length">
            <li v-for="item in summaryData.categories" :key="item.category">
              <span>{{ categoryLabel(item.category) }}</span>
              <span>{{
                formatCurrency(item.amount, summaryData.currency)
              }}</span>
            </li>
          </ul>
          <p v-else class="empty">暂无分类数据。</p>
        </article>
      </section>

      <section class="panel">
        <div class="panel__header">
          <h2>新增费用</h2>
        </div>
        <form class="expense-form" @submit.prevent="handleSubmit">
          <div class="form-row">
            <label>
              金额
              <input
                v-model="form.amount"
                type="number"
                min="0"
                step="0.01"
                placeholder="例如：120.50"
                required
              />
            </label>
            <label>
              币种
              <input v-model="form.currency" maxlength="10" placeholder="CNY" />
            </label>
            <label>
              分类
              <select v-model="form.category" required>
                <option
                  v-for="option in categoryOptions"
                  :key="option.value"
                  :value="option.value"
                >
                  {{ option.label }}
                </option>
              </select>
            </label>
            <label>
              记录方式
              <select v-model="form.method" required>
                <option
                  v-for="option in methodOptions"
                  :key="option.value"
                  :value="option.value"
                >
                  {{ option.label }}
                </option>
              </select>
            </label>
            <label>
              记录时间
              <input v-model="form.recordedAt" type="datetime-local" required />
            </label>
          </div>
          <label>
            备注
            <textarea
              v-model="form.notes"
              rows="2"
              placeholder="可选"
            ></textarea>
          </label>
          <div class="form-actions">
            <span v-if="formError" class="error">{{ formError }}</span>
            <button class="primary-btn" type="submit" :disabled="submitting">
              {{ submitting ? "提交中..." : "新增费用" }}
            </button>
          </div>
        </form>
      </section>

      <section class="panel">
        <div class="panel__header">
          <h2>记账明细</h2>
          <button
            class="ghost-btn"
            type="button"
            @click="refresh"
            :disabled="isRefreshing"
          >
            刷新
          </button>
        </div>
        <table class="record-table" v-if="expensesList.length">
          <thead>
            <tr>
              <th>时间</th>
              <th>类别</th>
              <th>金额</th>
              <th>来源</th>
              <th>备注</th>
              <th class="col-actions">操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="expense in expensesList" :key="expense.id">
              <td>{{ formatDateTime(expense.recordedAt) }}</td>
              <td>{{ categoryLabel(expense.category) }}</td>
              <td>{{ formatCurrency(expense.amount, expense.currency) }}</td>
              <td>{{ methodLabel(expense.method) }}</td>
              <td>{{ expense.notes || "-" }}</td>
              <td class="col-actions">
                <button
                  class="ghost-btn"
                  type="button"
                  :disabled="isDeleting"
                  @click="removeExpense(expense.id)"
                >
                  删除
                </button>
              </td>
            </tr>
          </tbody>
        </table>
        <p v-else class="empty">当前行程暂无记账记录。</p>
      </section>
    </template>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref, watch } from "vue";
import { storeToRefs } from "pinia";
import { usePlanStore } from "../stores/plans";
import { useExpenseStore } from "../stores/expenses";
import type { ExpenseSummary } from "../stores/expenses";

const planStore = usePlanStore();
const expenseStore = useExpenseStore();

const {
  planList,
  loading: planLoading,
  lastError: planStoreError,
} = storeToRefs(planStore);
const {
  expenses,
  summary,
  loading: expenseLoading,
  lastError: expenseStoreError,
  isSaving,
  isDeleting,
} = storeToRefs(expenseStore);

const selectedPlanId = ref<string>("");
const isRefreshing = ref(false);
const formError = ref("");

const planOptions = computed(() => planList.value);
const expensesList = computed(() => expenses.value);
const summaryData = computed<ExpenseSummary>(
  () =>
    summary.value ?? {
      totalAmount: 0,
      currency: "CNY",
      categories: [],
    },
);
const isLoading = computed(() => planLoading.value || expenseLoading.value);
const expenseError = computed(() => expenseStoreError.value);
const planError = computed(() => planStoreError.value);
const submitting = computed(() => isSaving.value);

const categoryOptions = [
  { value: "TRANSPORT", label: "交通" },
  { value: "ACCOMMODATION", label: "住宿" },
  { value: "DINING", label: "餐饮" },
  { value: "ENTERTAINMENT", label: "娱乐" },
  { value: "SHOPPING", label: "购物" },
  { value: "OTHER", label: "其他" },
];

const methodOptions = [
  { value: "MANUAL", label: "手动" },
  { value: "VOICE", label: "语音" },
];

const form = reactive({
  amount: "",
  currency: "CNY",
  category: "DINING",
  method: "MANUAL",
  recordedAt: initLocalDateTime(),
  notes: "",
});

onMounted(async () => {
  if (!planOptions.value.length) {
    try {
      await planStore.fetchPlans();
    } catch (error) {
      console.error("Failed to fetch plans for budget view", error);
      return;
    }
  }
  if (!selectedPlanId.value) {
    const options = planOptions.value;
    if (options.length > 0 && options[0]?.id) {
      selectedPlanId.value = options[0].id;
    }
  }
});

watch(
  () => selectedPlanId.value,
  async (planId) => {
    if (!planId) {
      expenseStore.clear();
      return;
    }
    await loadExpenses(planId);
  },
  { immediate: false },
);

async function loadExpenses(planId: string) {
  try {
    isRefreshing.value = true;
    await Promise.all([
      expenseStore.fetchExpenses(planId),
      expenseStore.fetchSummary(planId),
    ]);
  } catch (error) {
    // 错误已在 store 中处理
  } finally {
    isRefreshing.value = false;
  }
}

function refresh() {
  if (selectedPlanId.value) {
    loadExpenses(selectedPlanId.value);
  }
}

async function handleSubmit() {
  formError.value = "";
  if (!selectedPlanId.value) {
    formError.value = "请先选择行程";
    return;
  }

  const amountNumeric = Number.parseFloat(form.amount);
  if (!Number.isFinite(amountNumeric) || amountNumeric <= 0) {
    formError.value = "请输入大于 0 的金额";
    return;
  }

  try {
    await expenseStore.createExpense(selectedPlanId.value, {
      amount: Number.parseFloat(amountNumeric.toFixed(2)),
      currency: form.currency.trim().toUpperCase() || "CNY",
      category: form.category,
      method: form.method,
      recordedAt: toUtcISOString(form.recordedAt),
      notes: form.notes?.trim() || undefined,
    });
    resetForm();
  } catch (error) {
    formError.value = "新增费用失败，请稍后再试";
  }
}

async function removeExpense(expenseId: string) {
  if (!selectedPlanId.value) {
    return;
  }
  try {
    await expenseStore.deleteExpense(selectedPlanId.value, expenseId);
  } catch (error) {
    // store 已记录错误，这里无需额外处理
  }
}

function resetForm() {
  form.amount = "";
  form.currency = summaryData.value.currency || "CNY";
  form.category = "DINING";
  form.method = "MANUAL";
  form.recordedAt = initLocalDateTime();
  form.notes = "";
}

function formatCurrency(value: number, currency: string) {
  if (!Number.isFinite(value)) {
    return "-";
  }
  return new Intl.NumberFormat("zh-CN", {
    style: "currency",
    currency,
    maximumFractionDigits: 2,
  }).format(value);
}

function formatDate(value: string) {
  return new Date(value).toLocaleDateString();
}

function formatDateTime(value: string) {
  return new Date(value).toLocaleString();
}

function categoryLabel(category: string) {
  const match = categoryOptions.find((item) => item.value === category);
  return match ? match.label : category;
}

function methodLabel(method: string) {
  const match = methodOptions.find((item) => item.value === method);
  return match ? match.label : method;
}

function initLocalDateTime() {
  const date = new Date();
  date.setMilliseconds(0);
  return date.toISOString().slice(0, 16);
}

function toUtcISOString(localValue: string) {
  const date = new Date(localValue);
  if (Number.isNaN(date.getTime())) {
    return new Date().toISOString();
  }
  return date.toISOString();
}
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
  cursor: not-allowed;
  opacity: 0.6;
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

.plan-selector {
  display: flex;
  gap: 16px;
}

.plan-selector label {
  display: flex;
  flex-direction: column;
  gap: 8px;
  font-size: 14px;
  color: #4b5563;
}

.plan-selector select {
  padding: 8px 12px;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  font-size: 14px;
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

.loading-panel {
  text-align: center;
  color: #4b5563;
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
  grid-template-columns: repeat(auto-fit, minmax(160px, 1fr));
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

.expense-form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.form-row {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(160px, 1fr));
  gap: 16px;
}

label {
  display: flex;
  flex-direction: column;
  gap: 8px;
  font-size: 14px;
  color: #4b5563;
}

input,
select,
textarea {
  padding: 8px 12px;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  font-size: 14px;
}

textarea {
  resize: vertical;
  min-height: 60px;
}

.form-actions {
  display: flex;
  align-items: center;
  gap: 16px;
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

.col-actions {
  width: 80px;
}

.error {
  padding: 12px 16px;
  border-radius: 8px;
  background: rgba(220, 38, 38, 0.08);
  color: #b91c1c;
}

.empty {
  color: #6b7280;
  text-align: center;
}

@media (max-width: 720px) {
  .budget__header {
    flex-direction: column;
    align-items: flex-start;
  }

  .header-actions {
    width: 100%;
    flex-direction: column;
  }

  .plan-selector {
    flex-direction: column;
  }
}
</style>
