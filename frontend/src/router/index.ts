import {
  createRouter,
  createWebHistory,
  type RouteRecordRaw,
} from "vue-router";
import { useAuthStore } from "../stores/auth";

const routes: RouteRecordRaw[] = [
  {
    path: "/login",
    name: "login",
    component: () => import("../views/LoginView.vue"),
    meta: { public: true, title: "登录" },
  },
  {
    path: "/register",
    name: "register",
    component: () => import("../views/RegisterView.vue"),
    meta: { public: true, title: "注册" },
  },
  {
    path: "/",
    component: () => import("../layouts/AppShell.vue"),
    children: [
      {
        path: "",
        name: "dashboard",
        component: () => import("../views/DashboardView.vue"),
        meta: { title: "行程概览" },
      },
      {
        path: "plans",
        name: "plans",
        component: () => import("../views/PlansView.vue"),
        meta: { title: "我的行程" },
      },
      {
        path: "budget",
        name: "budget",
        component: () => import("../views/BudgetView.vue"),
        meta: { title: "预算中心" },
      },
      {
        path: "map",
        name: "map",
        component: () => import("../views/MapView.vue"),
        meta: { title: "地图视图" },
      },
      {
        path: "settings/api-keys",
        name: "settings-api-keys",
        component: () => import("../views/ApiKeySettingsView.vue"),
        meta: { title: "API Key 设置" },
      },
    ],
  },
  {
    path: "/:pathMatch(.*)*",
    name: "not-found",
    component: () => import("../views/NotFoundView.vue"),
    meta: { public: true, title: "未找到页面" },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to) => {
  if (to.meta?.title) {
    document.title = `AI Travel Planner · ${to.meta.title as string}`;
  }

  const authStore = useAuthStore();
  if (!to.meta?.public && !authStore.isAuthenticated) {
    return {
      name: "login",
      query: { redirect: to.fullPath },
    };
  }
});

export default router;
