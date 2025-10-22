<template>
  <div class="auth-card">
    <h1>注册新账户</h1>
    <p class="subtitle">快速创建账号，开始构建你的智能旅行计划。</p>

    <form class="form" @submit.prevent="handleSubmit">
      <label class="form__field">
        <span>昵称</span>
        <input
          v-model="form.displayName"
          type="text"
          placeholder="我爱自由行"
          required
        />
      </label>

      <label class="form__field">
        <span>邮箱</span>
        <input
          v-model="form.email"
          type="email"
          placeholder="example@email.com"
          required
        />
      </label>

      <label class="form__field">
        <span>密码</span>
        <input
          v-model="form.password"
          type="password"
          placeholder="至少 8 位字符"
          minlength="8"
          required
        />
      </label>

      <button class="submit-btn" type="submit" :disabled="loading">
        {{ loading ? "注册中..." : "注册并登录" }}
      </button>
    </form>

    <p v-if="errorMessage" class="feedback error">{{ errorMessage }}</p>

    <p class="helper">
      已有账号？
      <RouterLink :to="{ name: 'login', query: { redirect: redirectTarget } }">
        立即登录
      </RouterLink>
    </p>
  </div>
</template>

<script setup lang="ts">
import { computed, reactive, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useAuthStore } from "../stores/auth";

const authStore = useAuthStore();
const router = useRouter();
const route = useRoute();

const loading = ref(false);
const errorMessage = ref("");
const form = reactive({
  displayName: "",
  email: "",
  password: "",
});

const redirectTarget = computed(() => {
  const redirect = route.query.redirect;
  if (typeof redirect === "string" && redirect.startsWith("/")) {
    return redirect;
  }
  return "/";
});

async function handleSubmit() {
  loading.value = true;
  errorMessage.value = "";
  try {
    await authStore.register(form);
    router.push(redirectTarget.value);
  } catch (error) {
    console.error(error);
    errorMessage.value = "注册失败，请稍后重试。";
  } finally {
    loading.value = false;
  }
}
</script>

<style scoped>
.auth-card {
  max-width: 440px;
  margin: 48px auto;
  padding: 40px 32px;
  border-radius: 16px;
  background: white;
  box-shadow: 0 12px 36px -18px rgba(20, 36, 60, 0.25);
}

h1 {
  margin: 0 0 12px;
  font-size: 26px;
  font-weight: 600;
  color: #16213e;
}

.subtitle {
  margin: 0 0 24px;
  color: #6b7280;
}

.form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.form__field {
  display: flex;
  flex-direction: column;
  gap: 8px;
  color: #374151;
  font-size: 14px;
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

.submit-btn {
  margin-top: 8px;
  padding: 12px 16px;
  border-radius: 10px;
  border: none;
  background-color: #5271ff;
  color: white;
  font-weight: 600;
  cursor: pointer;
  transition: opacity 0.2s ease;
}

.submit-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.feedback {
  margin-top: 16px;
  text-align: center;
  font-size: 14px;
}

.feedback.error {
  color: #dc2626;
}

.helper {
  margin-top: 20px;
  font-size: 14px;
  color: #6b7280;
  text-align: center;
}

.helper a {
  color: #5271ff;
}
</style>
