import {
  createRouter,
  createWebHistory,
  type RouteRecordRaw,
} from "vue-router";

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

router.beforeEach((to, _from, next) => {
  if (to.meta?.title) {
    document.title = `AI Travel Planner · ${to.meta.title as string}`;
  }
  // TODO: 集成真实的鉴权逻辑
  next();
});

export default router;
