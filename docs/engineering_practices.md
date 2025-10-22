# 工程实践与规范

## 1. 代码风格

- 使用 `.editorconfig` 统一缩进、换行和编码。
- 前端：采用 ESLint（基于 `eslint-config-airbnb-typescript` 或 `@vue/eslint-config-typescript`）+ Prettier。
- 后端：采用 Spotless + Google Java Format（后续可根据团队偏好调整），并启用 Checkstyle。
- Markdown 使用 `prettier-plugin-organize-imports`/`markdownlint` 作为可选检查。

## 2. 提交流程

1. 新建分支：`<type>/<scope>`，例如 `feat/frontend-auth`。
2. 按照 Conventional Commits 撰写提交信息：
   - `feat(frontend): support voice itinerary input`
   - `fix(backend): handle bailian timeout`
3. 所有提交必须通过本地 lint、测试与格式检查。
4. 合并前需发起 Pull Request，由验收方进行 Review 与确认。

## 3. Git 钩子

- Husky + lint-staged 将在阶段 1 末启用，pre-commit 默认执行：
  - `npm run lint` / `pnpm lint`
  - `npm run format:check`
  - 运行受影响模块的单元测试（视场景决定）
- 可在 `.husky/pre-commit` 中新增自定义脚本，例如自动生成 `changelog`。

## 4. 分层目录建议

- `frontend/`：Vue 应用源码，使用 Vite 构建。
- `backend/`：Spring Boot 应用源码，使用 Maven 或 Gradle 管理。
- `docs/`：所有文档与设计资料。
- `infra/`：Docker、k8s、CI/CD 脚本与配置。

## 5. 质量保障

- 单元测试覆盖率目标：前端 ≥ 70%，后端 ≥ 80%。
- 行为观测（E2E）测试覆盖核心用户路径（语音输入 → 行程生成 → 地图展示 → 记账）。
- 引入 Sonar 或类似工具进行静态分析（阶段 6 可评估）。

## 6. 分支与发布策略

- `main`：稳定分支，用于交付与部署。
- 功能分支：对应单项需求或 Bug 修复，完成后合并至 `main`。
- 发版节奏：每阶段结束可打 Tag（例如 `v0.1.0-alpha`）并发布 Docker 镜像。

## 7. 文档要求

- 所有功能需在 README 或对应模块 README 中补充运行说明。
- 更新 PRD/流程/规范需随代码一并提交。
- 对第三方依赖或 API 限额有新约束时，需在 `docs/api-key-management.md` 同步说明。

遵守上述规范将有助于保持仓库整洁、变更可回溯，并提升交付质量。
