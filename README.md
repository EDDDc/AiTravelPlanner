# AI Travel Planner

本仓库用于实现《AI Travel Planner》课程作业。项目采用前端 Vue 3、后端 Spring Boot 的分层架构，并整合阿里云百炼、科大讯飞语音识别、高德地图等第三方服务。

## 团队分工

- 产品 / 验收：由用户负责，确认 PRD、迭代计划与交付质量。
- 开发执行：由 Codex Agent 负责代码实现、测试、文档与交付物。

## 环境要求

- Node.js：推荐使用 LTS v20（当前开发机为 `v22.20.0`，需切换到 v20）。
- 包管理：建议 pnpm 8+（可根据项目需要调整）。
- Java：Temurin/OpenJDK 21（当前开发机为 `23.0.1`，需切换到 21）。
- Maven：3.9+（后端使用 Maven Wrapper 管理）。
- Docker：24+，用于容器化构建与运行。

详细的工具配置请参阅 `docs/environment.md`。

## 项目进度

- ✅ 发布 PRD v0.3（`AI_Travel_Planner_PRD_v0.3.md`）
- ✅ 输出开发阶段计划（`development_flow.md`）
- ✅ 阶段 0：环境准备与规范配置
- ✅ 阶段 1：产品设计与原型确认
- ✅ 阶段 2：后端基础设施
- ✅ 阶段 3：前端框架与通用能力

## 文档索引

- 产品需求：`AI_Travel_Planner_PRD_v0.3.md`
- 低保真原型：`docs/wireframes.md`
- 信息架构：`docs/information_architecture.md`
- 业务字段字典：`docs/domain_dictionary.md`
- 用户故事与验收标准：`docs/user_stories.md`
- 接口草稿：`docs/api/README.md`
- 工程规范：`docs/engineering_practices.md`
- 环境配置：`docs/environment.md`
- 项目管理约定：`docs/project_management.md`
- API Key 管理：`docs/api-key-management.md`
- 后端说明：`backend/README.md`

## 快速开始（后端）

1. 进入 `backend` 目录：`cd backend`。
2. 复制根目录 `.env.example` 为 `.env`，填写数据库连接信息（Supabase/PostgreSQL）。
3. 执行 `./mvnw spring-boot:run`（默认启用 `dev` profile，Flyway 自动迁移表结构）。
4. 运行测试：`./mvnw test`。

## 快速开始（前端）

1. 进入 `frontend` 目录：`cd frontend`。
2. 复制 `.env.example` 为 `.env`，设置 `VITE_APP_API_BASE_URL` 以及 `VITE_APP_AMAP_WEB_KEY`。
3. 安装依赖：`npm install`。
4. 本地开发：`npm run dev`；生产构建：`npm run build`。

## 工程检查

- 根目录：`npm run format`（Prettier 检查）。
- 后端：`./backend/mvnw test`。
- 前端：`npm run lint && npm run build`（在 `frontend` 目录执行）。

后续迭代将按照 `development_flow.md` 中的阶段继续推进。
