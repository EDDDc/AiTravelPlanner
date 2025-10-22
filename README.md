# AI Travel Planner

本仓库用于实现《AI Travel Planner》课程作业。项目采用前后端分离架构（前端 Vue 3、后端 Spring Boot），并集成阿里云百炼、科大讯飞语音、高德地图等服务。

## 团队角色

- 产品/验收：由用户负责，确认 PRD、迭代计划与交付质量。
- 开发执行：由 Codex Agent 负责落地实现、测试与交付文档。

## 环境要求

- Node.js：推荐使用 LTS v20（本机检测到的版本：`v22.20.0`）。建议通过 nvm 切换到 v20 以兼容前端依赖。
- 包管理：建议使用 pnpm 8+（亦可使用 npm/yarn，根据后续前端模板决定）。
- Java：建议使用 Temurin/OpenJDK 21（本机检测到的版本：`23.0.1`，需切换至 21 以匹配 Spring Boot 3）。
- 构建工具：Maven 3.9+ 或 Gradle 8+（待后端初始化时确认）。
- Docker：24+，用于本地构建与测试容器镜像。

详细环境配置请参考 `docs/environment.md`。

## 工程规范

- 统一使用 `.editorconfig` 中定义的格式设置；提交前应确保通过 lint 与格式化检查。
- 严禁将任何 API Key 提交至仓库；通过运行时环境变量或设置页面注入。
- 提交信息遵循 Conventional Commits，例如 `feat(frontend): add itinerary board`。

更多工程实践请参见 `docs/engineering_practices.md` 与 `docs/api-key-management.md`。

## 当前进展

- ✅ 发布 PRD v0.3（`AI_Travel_Planner_PRD_v0.3.md`）
- ✅ 输出开发阶段计划（`development_flow.md`）
- 🚧 阶段 0：环境准备与规范配置（当前任务）

后续工作会按照 `development_flow.md` 的阶段划分逐步推进。
