<template>
  <section class="settings">
    <header class="settings__header">
      <div>
        <h1>API Key 设置</h1>
        <p>请在此安全地配置阿里云百炼、科大讯飞、高德地图等凭证。</p>
      </div>
      <button class="ghost-btn" type="button" @click="testConnection">
        测试连通性
      </button>
    </header>

    <form class="settings__form" @submit.prevent="handleSubmit">
      <fieldset v-for="provider in providers" :key="provider.id">
        <legend>{{ provider.label }}</legend>
        <p class="field-hint">{{ provider.hint }}</p>
        <label class="form-field">
          <span>Key 别名</span>
          <input
            v-model="provider.alias"
            type="text"
            placeholder="示例：bailian-default"
          />
        </label>
        <label class="form-field">
          <span>密钥值</span>
          <input
            v-model="provider.value"
            type="password"
            placeholder="请填写完整的 API Key"
            required
          />
        </label>
      </fieldset>

      <div class="form-actions">
        <button class="ghost-btn" type="button" @click="resetForm">重置</button>
        <button class="primary-btn" type="submit">保存设置</button>
      </div>
    </form>
  </section>
</template>

<script setup lang="ts">
import { reactive } from "vue";
import { useApiKeyStore } from "../stores/apiKeys";

const apiKeyStore = useApiKeyStore();

const providers = reactive([
  {
    id: "BAILIAN",
    label: "阿里云百炼",
    hint: "用于调用通义千问大模型生成行程和问答服务。",
    alias: "",
    value: "",
  },
  {
    id: "XFYUN",
    label: "科大讯飞",
    hint: "用于语音转写与语音记账功能。",
    alias: "",
    value: "",
  },
  {
    id: "AMAP",
    label: "高德地图",
    hint: "用于地图展示、路线规划与附近推荐。",
    alias: "",
    value: "",
  },
]);

function resetForm() {
  providers.forEach((provider) => {
    provider.alias = "";
    provider.value = "";
  });
}

async function handleSubmit() {
  await apiKeyStore.saveKeys(
    providers.map(({ id, alias, value }) => ({
      provider: id,
      alias,
      value,
    })),
  );
}

async function testConnection() {
  await apiKeyStore.testKey();
}
</script>

<style scoped>
.settings {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.settings__header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 24px;
}

.settings__header h1 {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
  color: #16213e;
}

.settings__header p {
  margin: 8px 0 0;
  color: #6b7280;
}

.settings__form {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

fieldset {
  border: none;
  padding: 24px;
  border-radius: 16px;
  background: white;
  box-shadow: 0 12px 36px -24px rgba(20, 36, 60, 0.25);
  display: flex;
  flex-direction: column;
  gap: 16px;
}

legend {
  font-size: 18px;
  font-weight: 600;
  color: #16213e;
}

.field-hint {
  margin: 0;
  color: #6b7280;
  font-size: 14px;
}

.form-field {
  display: flex;
  flex-direction: column;
  gap: 8px;
  font-size: 14px;
  color: #374151;
}

input {
  padding: 12px 14px;
  border-radius: 10px;
  border: 1px solid #d0d5dd;
  font-size: 14px;
  transition: border-color 0.2s ease;
}

input:focus {
  border-color: #5271ff;
  outline: none;
  box-shadow: 0 0 0 3px rgba(82, 113, 255, 0.15);
}

.form-actions {
  display: flex;
  justify-content: flex-end;
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
</style>
