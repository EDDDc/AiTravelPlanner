<template>
  <div class="app-shell">
    <aside class="sidebar">
      <div class="sidebar__brand">
        <span class="brand__logo">🧭</span>
        <span class="brand__name">AI Travel Planner</span>
      </div>
      <nav class="sidebar__nav">
        <RouterLink
          to="/"
          class="nav__item"
          active-class="nav__item--active"
          exact
        >
          行程概览
        </RouterLink>
        <RouterLink
          to="/plans"
          class="nav__item"
          active-class="nav__item--active"
        >
          我的行程
        </RouterLink>
        <RouterLink
          to="/budget"
          class="nav__item"
          active-class="nav__item--active"
        >
          预算中心
        </RouterLink>
        <RouterLink
          to="/settings/api-keys"
          class="nav__item"
          active-class="nav__item--active"
        >
          API Key 设置
        </RouterLink>
      </nav>
    </aside>

    <div class="content">
      <header class="content__header">
        <div class="header__title">{{ pageTitle }}</div>
        <div class="header__actions">
          <button type="button" class="action-btn ghost">语音输入</button>
          <button type="button" class="action-btn primary">新建行程</button>
          <div class="auth-entry" v-if="isAuthenticated">
            <div class="auth-entry__name">{{ userDisplayName }}</div>
            <button type="button" class="text-btn" @click="handleLogout">
              退出登录
            </button>
          </div>
          <div class="auth-entry" v-else>
            <button type="button" class="text-btn" @click="goLogin">
              登录
            </button>
            <span class="divider">|</span>
            <button type="button" class="text-btn" @click="goRegister">
              注册
            </button>
          </div>
        </div>
      </header>

      <main class="content__body">
        <RouterView />
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from "vue";
import { RouterLink, RouterView, useRoute, useRouter } from "vue-router";
import { useAuthStore } from "../stores/auth";

const route = useRoute();
const router = useRouter();
const authStore = useAuthStore();

const pageTitle = computed(() => route.meta?.title ?? "AI Travel Planner");
const isAuthenticated = computed(() => authStore.isAuthenticated);
const userDisplayName = computed(
  () => authStore.user?.displayName ?? authStore.user?.email ?? "旅行者",
);

function goLogin() {
  router.push({ name: "login", query: { redirect: route.fullPath } });
}

function goRegister() {
  router.push({ name: "register", query: { redirect: route.fullPath } });
}

function handleLogout() {
  authStore.logout();
  router.push({ name: "login" });
}
</script>

<style scoped>
.app-shell {
  display: grid;
  grid-template-columns: 240px 1fr;
  min-height: 100vh;
  background-color: var(--page-bg);
  color: var(--text-primary);
}

.sidebar {
  background: var(--sidebar-bg);
  border-right: 1px solid var(--border-color);
  padding: 24px 18px;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.sidebar__brand {
  display: flex;
  align-items: center;
  gap: 12px;
  font-weight: 600;
  font-size: 18px;
  color: var(--text-primary);
}

.brand__logo {
  font-size: 22px;
}

.sidebar__nav {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.nav__item {
  padding: 10px 12px;
  border-radius: 8px;
  color: var(--text-secondary);
  transition: background-color 0.2s ease;
}

.nav__item:hover {
  background-color: var(--sidebar-hover);
  color: var(--text-primary);
}

.nav__item--active {
  background-color: var(--primary-quiet);
  color: var(--primary-color);
  font-weight: 600;
}

.content {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.content__header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24px 32px 16px;
  border-bottom: 1px solid var(--border-color);
  background-color: var(--page-bg);
  position: sticky;
  top: 0;
  z-index: 10;
}

.header__title {
  font-size: 20px;
  font-weight: 600;
}

.header__actions {
  display: flex;
  gap: 12px;
  align-items: center;
}

.action-btn {
  padding: 8px 16px;
  border-radius: 8px;
  border: 1px solid transparent;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.action-btn.primary {
  background: var(--primary-color);
  color: white;
}

.action-btn.primary:hover {
  opacity: 0.9;
}

.action-btn.ghost {
  background: transparent;
  color: var(--text-secondary);
  border-color: var(--border-color);
}

.action-btn.ghost:hover {
  color: var(--primary-color);
  border-color: var(--primary-color);
}

.auth-entry {
  display: flex;
  align-items: center;
  gap: 8px;
  color: var(--text-secondary);
  font-size: 14px;
}

.auth-entry__name {
  font-weight: 600;
  color: var(--text-primary);
}

.text-btn {
  background: transparent;
  border: none;
  color: #5271ff;
  cursor: pointer;
  padding: 0;
}

.divider {
  color: var(--border-color);
}

.content__body {
  padding: 24px 32px 48px;
  flex: 1;
  background-color: var(--content-bg);
}

@media (max-width: 960px) {
  .app-shell {
    grid-template-columns: 1fr;
  }

  .sidebar {
    position: fixed;
    inset: 0 auto 0 0;
    width: 220px;
    transform: translateX(-100%);
    transition: transform 0.2s ease;
    z-index: 999;
  }
}
</style>
