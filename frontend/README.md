# AI Travel Planner · Frontend

基于 Vue 3 + TypeScript + Vite 的前端工程，用于实现 AI 旅行规划器的 Web 端界面。

## 目录结构

```
frontend/
├── src/
│   ├── layouts/         # 全局布局组件
│   ├── router/          # Vue Router 配置
│   ├── services/        # Axios 封装、AMap SDK 加载等通用服务
│   ├── stores/          # Pinia 状态管理
│   ├── styles/          # 全局样式与主题变量
│   └── views/           # 页面级组件
├── .env.example         # 前端环境变量模板
├── package.json
└── vite.config.ts
```

## 快速开始

```bash
cd frontend
npm install
npm run dev      # 启动本地开发
npm run lint     # 类型检查
npm run build    # 生产构建
```

## 环境变量

- `VITE_APP_API_BASE_URL`：后端服务地址，默认 `http://localhost:8080`。
- `VITE_APP_AMAP_WEB_KEY`：高德地图 Web API Key，需在高德开放平台申请。

## 当前功能

- 登录 / 注册占位流程，支持登录后返回原路由。
- 全局布局（顶部导航 + 侧边栏）和仪表盘、行程、预算、API Key 设置等页面骨架。
- 高德地图视图（`/map`），通过 SDK 动态加载示例行程标记。
- Axios 拦截器统一附加 Token；Pinia 存储用户状态与 API Key 表单数据。

## 待完善

- 与后端认证接口打通（替换当前 Mock 数据）。
- 行程、预算与地图视图的数据联动。
- 语音相关功能与真实 POI 推荐。
