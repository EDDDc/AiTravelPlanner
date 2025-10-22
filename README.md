# AI Travel Planner

本仓库用于实现《AI Travel Planner》课程作业。项目采用前端 Vue 3、后端 Spring Boot 的分层架构，并将集成阿里云百炼、科大讯飞语音识别、高德地图等第三方服务。

## 团队分工

- 产品 / 验收：由用户负责，确认 PRD、迭代计划与交付质量。
- 开发执行：由 Codex Agent 负责代码实现、测试、文档与交付物。

## 环境要求

- Node.js：推荐使用 LTS v20（本机当前为 `v22.20.0`，需切换至 v20）。
- 包管理：建议 pnpm 8+（后续可按模板调整）。
- Java：Temurin/OpenJDK 21（本机当前为 `23.0.1`，需安装并切换到 21）。
- Maven：3.9+（后端使用 Maven 构建）。
- Docker：24+，用于本地容器构建与运行。

详细环境配置与工具建议请参阅 `docs/environment.md`。

## 工程规范

- 统一按照 `.editorconfig` 规范进行格式化；提交前需通过 lint / format 检查。
- 严禁将任何真实 API Key 纳入仓库，必须通过环境变量或设置页面输入。
- Git 提交遵循 Conventional Commits，例如 `feat(frontend): add itinerary board`。
- 更多工程实践与 API Key 管理规范见 `docs/engineering_practices.md` 与 `docs/api-key-management.md`。

## 进度追踪

- ✅ PRD v0.3（`AI_Travel_Planner_PRD_v0.3.md`）
- ✅ 开发阶段计划（`development_flow.md`）
- ✅ 阶段 0：环境准备与规范配置
- ✅ 阶段 1：产品设计与原型确认
- 🚧 阶段 2：后端基础设施（当前阶段）

后续工作将按 `development_flow.md` 中的阶段逐步推进。

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
- API Key 管理规范：`docs/api-key-management.md`
- 后端说明：`backend/README.md`

## 快速开始（后端）

1. 切换到 `backend` 目录，运行 `./mvnw spring-boot:run`（默认使用 `dev` Profile）。
2. 测试命令：`./mvnw test`（测试配置采用 H2 内存数据库）。
3. 环境变量参考根目录 `.env.example`，按需拆分到前后端。

当前阶段完成后，将继续开展前端脚手架与核心业务功能开发。
