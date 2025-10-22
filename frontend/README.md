# AI Travel Planner · Frontend

基于 Vue 3 + TypeScript + Vite 的前端工程，用于实现 AI 旅行规划器的 Web 端界面。

## 目录结构

```
frontend/
├── src/
│   ├── layouts/         # 全局布局组件
│   ├── router/          # Vue Router 配置
│   ├── services/        # Axios 封装等通用服务
│   ├── stores/          # Pinia 状态管理
│   ├── styles/          # 全局样式与变量
│   └── views/           # 页面级组件
├── .env.example         # 前端环境变量模板
├── package.json
└── vite.config.ts
```

## 快速开始

```bash
# 进入目录
cd frontend

# 安装依赖
npm install

# 启动本地开发服务器
npm run dev

# 类型检查
npm run lint

# 生产构建
npm run build
```

## 环境变量

- `VITE_APP_API_BASE_URL`：后端服务地址，默认指向 `http://localhost:8080`。

## 约定

- 状态管理基于 Pinia，`stores/auth.ts`、`stores/apiKeys.ts` 提供示例。
- Axios 实例位于 `services/http.ts`，会自动附加本地保存的 Access Token。
- 登录 / 注册 / API Key 设置等页面以占位逻辑实现，后续与后端接口联调。
- 待完成事项：高德地图 SDK 封装、行程与预算模块的真实数据对接。
