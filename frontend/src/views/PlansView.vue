<template>
  <section class="plans">
    <header class="plans__header">
      <div>
        <h1>我的行程</h1>
        <p>结合 AI 快速生成多日行程，随时查看与调整。</p>
      </div>
      <button class="primary-btn" type="button" @click="toggleForm">
        {{ showForm ? "取消创建" : "新建行程" }}
      </button>
    </header>

    <transition name="fade">
      <form v-if="showForm" class="plan-form" @submit.prevent="handleSubmit">
        <h2 class="plan-form__title">填写基础信息</h2>

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
            目的地（使用逗号分隔）
            <input
              v-model.trim="form.destinations"
              type="text"
              placeholder="东京, 横滨"
              required
            />
          </label>
        </div>

        <div class="form-row form-row--dates">
          <label>
            开始日期
            <input v-model="form.startDate" type="date" required />
          </label>
          <label>
            结束日期
            <input v-model="form.endDate" type="date" required />
          </label>
          <label>
            总预算（¥）
            <input
              v-model.trim="form.budget"
              type="number"
              min="0"
              step="100"
              placeholder="可选，例如：15000"
            />
          </label>
        </div>

        <div class="form-row form-row--checkbox">
          <fieldset>
            <legend>同行旅客</legend>
            <label v-for="option in travelerOptions" :key="option">
              <input v-model="form.travelers" type="checkbox" :value="option" />
              {{ option }}
            </label>
          </fieldset>

          <fieldset>
            <legend>偏好主题</legend>
            <label v-for="option in interestOptions" :key="option">
              <input v-model="form.interests" type="checkbox" :value="option" />
              {{ option }}
            </label>
          </fieldset>
        </div>

        <label class="textarea-label">
          额外备注
          <textarea
            v-model.trim="form.notes"
            rows="3"
            placeholder="例如：希望靠近地铁，安排一天自由活动"
          />
        </label>

        <section class="voice-box">
          <header class="voice-box__header">
            <h3>语音速记</h3>
            <span class="voice-box__hint"
              >使用麦克风描述行程需求，系统会自动转写为文字并填入备注。</span
            >
          </header>
          <div class="voice-box__controls">
            <button
              type="button"
              class="ghost-btn"
              @click="recording ? stopRecording() : startRecording()"
              :disabled="transcriptionLoading"
            >
              {{ recording ? "停止录音" : "开始录音" }}
            </button>
            <button
              v-if="transcript"
              type="button"
              class="ghost-btn"
              @click="resetVoiceState"
            >
              清除结果
            </button>
          </div>
          <p v-if="recording" class="voice-status">正在录音，请开始讲话…</p>
          <p v-else-if="transcriptionLoading" class="voice-status">
            正在转写音频…
          </p>
          <p v-else-if="transcript" class="voice-status success">
            已写入备注，可继续编辑。
          </p>
          <p v-if="voiceError" class="error">{{ voiceError }}</p>
          <textarea
            v-if="transcript"
            class="voice-preview"
            :value="transcript"
            readonly
          />
        </section>

        <div class="form-actions">
          <span v-if="creating" class="hint">正在创建占位行程...</span>
          <span v-else-if="formError" class="error">{{ formError }}</span>
          <span v-else-if="lastError" class="error">{{ lastError }}</span>
          <button class="primary-btn" type="submit" :disabled="creating">
            创建并查看详情
          </button>
        </div>
      </form>
    </transition>

    <div v-if="loading" class="loading">正在加载行程列表...</div>

    <p v-else-if="!hasPlans" class="empty">
      暂无行程，点击上方“新建行程”按钮开始规划。
    </p>

    <div v-else class="plans__grid">
      <article class="plan-card" v-for="plan in plans" :key="plan.id">
        <div class="plan-card__head">
          <div>
            <h2>{{ plan.title }}</h2>
            <p class="plan-card__meta">
              {{ plan.destinations.join(" · ") }}
            </p>
          </div>
          <span :class="['status', `status--${plan.status.toLowerCase()}`]">
            {{ statusLabel(plan.status) }}
          </span>
        </div>
        <p class="plan-card__meta">
          {{ formatDate(plan.startDate) }} – {{ formatDate(plan.endDate) }} （{{
            durationText(plan.startDate, plan.endDate)
          }}）
        </p>
        <p v-if="plan.budgetTotal" class="plan-card__meta">
          预算：¥{{ formatCurrency(plan.budgetTotal) }}
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
import { onMounted, reactive, ref, computed } from "vue";
import { storeToRefs } from "pinia";
import { useRouter } from "vue-router";
import http from "../services/http";
import { usePlanStore } from "../stores/plans";

interface VoiceTranscriptionResponse {
  transcript: string;
  provider: string;
  status: string;
}

const planStore = usePlanStore();
const router = useRouter();

const showForm = ref(false);
const formError = ref("");

const travelerOptions = ["独自旅行", "情侣 / 夫妻", "亲子家庭", "朋友结伴"];
const interestOptions = [
  "美食探索",
  "自然风光",
  "文化历史",
  "购物娱乐",
  "休闲度假",
];

const form = reactive({
  title: "",
  destinations: "",
  startDate: "",
  endDate: "",
  budget: "",
  travelers: [] as string[],
  interests: [] as string[],
  notes: "",
});

const { planList, loading, creating, lastError, hasPlans } =
  storeToRefs(planStore);
const plans = computed(() => planList.value);

const recording = ref(false);
const transcriptionLoading = ref(false);
const transcript = ref("");
const voiceError = ref("");

let mediaRecorder: MediaRecorder | null = null;
let mediaStream: MediaStream | null = null;
let recordedChunks: BlobPart[] = [];
let discardNextRecording = false;

onMounted(() => {
  planStore.fetchPlans();
});

function toggleForm() {
  showForm.value = !showForm.value;
  planStore.clearError();
  formError.value = "";
  if (!showForm.value) {
    resetForm();
  }
  resetVoiceState();
}

function resetForm() {
  form.title = "";
  form.destinations = "";
  form.startDate = "";
  form.endDate = "";
  form.budget = "";
  form.travelers = [];
  form.interests = [];
  form.notes = "";
}

function resetVoiceState() {
  if (recording.value) {
    discardNextRecording = true;
    stopRecording();
  } else {
    cleanupMediaStream();
  }
  transcript.value = "";
  voiceError.value = "";
  transcriptionLoading.value = false;
  recordedChunks = [];
}

function normalizeDestinations(raw: string) {
  return raw
    .split(/[,，]/)
    .map((item) => item.trim())
    .filter(Boolean);
}

function buildPreferences() {
  const preferences: Record<string, unknown> = {};
  if (form.travelers.length) {
    preferences.travelers = [...form.travelers];
  }
  if (form.interests.length) {
    preferences.interests = [...form.interests];
  }
  if (form.notes) {
    preferences.notes = form.notes;
  }
  return Object.keys(preferences).length ? preferences : undefined;
}

async function handleSubmit() {
  planStore.clearError();
  formError.value = "";

  const destinations = normalizeDestinations(form.destinations);
  if (!destinations.length) {
    formError.value = "请至少输入一个目的地。";
    return;
  }

  if (!form.startDate || !form.endDate) {
    formError.value = "请完整选择行程起止日期。";
    return;
  }

  const start = new Date(form.startDate);
  const end = new Date(form.endDate);
  if (start > end) {
    formError.value = "结束日期必须晚于或等于开始日期。";
    return;
  }

  let budgetTotal: number | null = null;
  if (form.budget !== "") {
    budgetTotal = Number.parseFloat(form.budget);
    if (!Number.isFinite(budgetTotal) || budgetTotal < 0) {
      formError.value = "总预算需要填写为不小于 0 的数字。";
      return;
    }
  }

  try {
    const created = await planStore.createPlan({
      title: form.title.trim(),
      destinations,
      startDate: form.startDate,
      endDate: form.endDate,
      budgetTotal,
      preferences: buildPreferences(),
    });
    resetForm();
    showForm.value = false;
    resetVoiceState();
    router.push({ name: "plan-detail", params: { planId: created.id } });
  } catch (error) {
    console.error("Failed to create plan", error);
    if (!lastError.value) {
      formError.value = "无法创建行程，请稍后再试。";
    }
  }
}

function goDetail(planId: string) {
  planStore.setCurrentPlan(planId);
  router.push({ name: "plan-detail", params: { planId } });
}

async function startRecording() {
  if (!navigator.mediaDevices?.getUserMedia) {
    voiceError.value = "当前浏览器不支持语音录入，请改用文字输入。";
    return;
  }
  if (recording.value) {
    return;
  }
  voiceError.value = "";
  transcript.value = "";
  discardNextRecording = false;
  try {
    mediaStream = await navigator.mediaDevices.getUserMedia({ audio: true });
    recordedChunks = [];
    mediaRecorder = new MediaRecorder(mediaStream);
    mediaRecorder.ondataavailable = (event: BlobEvent) => {
      if (event.data.size > 0) {
        recordedChunks.push(event.data);
      }
    };
    mediaRecorder.onstop = async () => {
      recording.value = false;
      const blob = new Blob(recordedChunks, { type: "audio/webm" });
      recordedChunks = [];
      await uploadRecordedAudio(blob);
      cleanupMediaStream();
    };
    mediaRecorder.start();
    recording.value = true;
  } catch (error) {
    console.error("Failed to start recording", error);
    voiceError.value = "无法访问麦克风，请检查浏览器权限。";
    cleanupMediaStream();
  }
}

function stopRecording() {
  if (mediaRecorder && mediaRecorder.state !== "inactive") {
    mediaRecorder.stop();
  } else {
    cleanupMediaStream();
    recording.value = false;
  }
}

async function uploadRecordedAudio(blob: Blob) {
  if (discardNextRecording) {
    discardNextRecording = false;
    return;
  }
  if (blob.size === 0) {
    voiceError.value = "录音数据为空，请重试。";
    return;
  }
  transcriptionLoading.value = true;
  voiceError.value = "";
  try {
    const formData = new FormData();
    formData.append("file", blob, `record-${Date.now()}.webm`);
    const response = await http.post<VoiceTranscriptionResponse>(
      "/api/voice/transcriptions",
      formData,
    );
    transcript.value = response.data.transcript;
    form.notes = form.notes
      ? `${form.notes}\n${response.data.transcript}`
      : response.data.transcript;
  } catch (error) {
    console.error("Failed to upload audio", error);
    voiceError.value = "语音转写失败，请稍后再试，或改用文字输入。";
  } finally {
    transcriptionLoading.value = false;
  }
}

function cleanupMediaStream() {
  if (mediaStream) {
    mediaStream.getTracks().forEach((track) => track.stop());
    mediaStream = null;
  }
  mediaRecorder = null;
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

function formatCurrency(value: number | string | null | undefined) {
  if (value == null) {
    return "";
  }
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
.plans {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.plans__header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 16px;
}

.plans__header h1 {
  margin: 0;
  font-size: 26px;
  font-weight: 600;
  color: #16213e;
}

.plans__header p {
  margin: 6px 0 0;
  color: #6b7280;
}

.plan-form {
  background: white;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 12px 36px -24px rgba(20, 36, 60, 0.25);
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.plan-form__title {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #16213e;
}

.form-row {
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
}

.form-row label,
.textarea-label {
  flex: 1 1 240px;
  display: flex;
  flex-direction: column;
  gap: 8px;
  color: #374151;
  font-size: 14px;
}

.form-row input,
.textarea-label textarea {
  padding: 12px 14px;
  border-radius: 10px;
  border: 1px solid #d0d5dd;
  font-size: 14px;
}

.textarea-label textarea {
  resize: vertical;
  min-height: 90px;
}

.form-row--dates label {
  flex: 1 1 200px;
}

.form-row--checkbox {
  align-items: flex-start;
}

.form-row--checkbox fieldset {
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  padding: 16px;
  flex: 1 1 260px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.form-row--checkbox legend {
  font-size: 14px;
  font-weight: 600;
  color: #16213e;
  padding: 0 6px;
}

.form-row--checkbox label {
  flex: none;
  flex-direction: row;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  color: #4b5563;
}

.form-row--checkbox input[type="checkbox"] {
  width: 16px;
  height: 16px;
}

.voice-box {
  border: 1px dashed #cbd5f5;
  border-radius: 12px;
  padding: 16px;
  background: rgba(82, 113, 255, 0.04);
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.voice-box__header {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.voice-box__header h3 {
  margin: 0;
  font-size: 15px;
  font-weight: 600;
  color: #16213e;
}

.voice-box__hint {
  font-size: 13px;
  color: #6b7280;
}

.voice-box__controls {
  display: flex;
  gap: 12px;
}

.voice-status {
  margin: 0;
  font-size: 13px;
  color: #4b5563;
}

.voice-status.success {
  color: #15803d;
}

.voice-preview {
  width: 100%;
  min-height: 80px;
  border-radius: 10px;
  border: 1px solid #cbd5f5;
  background: white;
  padding: 10px;
  color: #334155;
  font-size: 13px;
  resize: vertical;
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
  align-items: flex-start;
  gap: 12px;
}

.plan-card__head h2 {
  margin: 0;
  font-size: 18px;
  color: #16213e;
}

.plan-card__meta {
  margin: 0;
  color: #6b7280;
  font-size: 14px;
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

@media (max-width: 768px) {
  .plan-form {
    padding: 20px;
  }

  .form-row {
    flex-direction: column;
  }

  .form-row--checkbox fieldset {
    width: 100%;
  }
}
</style>
