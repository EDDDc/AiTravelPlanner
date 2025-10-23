# AI Travel Planner 项目开发流程

## 阶段 0：项目启动与环境准备

- [x] 确认团队角色与分工（前端 / 后端 / 测试 / 运维）
- [x] 建立项目管理看板（临时以内置文档记录，正式使用 GitHub Projects，见 `docs/project_management.md`）
- [x] 搭建开发环境：Node.js、Java/Spring Boot SDK、包管理工具、Docker（见 `docs/environment.md`）
- [x] 配置代码规范与工具：Lint、Prettier、EditorConfig、Husky+lint-staged（根目录已完成初始配置）
- [x] 创建 `.env.example`，约定 API Key、数据库等环境变量命名
- [x] 制定 API Key 管理规范（`docs/api-key-management.md`）

## 阶段 1：产品设计与原型确认

- [x] 输出低保真原型（行程概览、地图、预算、设置页面）——`docs/wireframes.md`
- [x] 完善信息架构与导航流程，确认 MVP 边界——`docs/information_architecture.md`
- [x] 细化用户故事与验收标准（Given-When-Then）——`docs/user_stories.md`
- [x] 评审并锁定 PRD、原型、业务字段字典——`AI_Travel_Planner_PRD_v0.3.md`、`docs/domain_dictionary.md`
- [x] 建立接口文档草稿——`docs/api/README.md`

## 阶段 2：后端基础设施

- [x] 初始化 Spring Boot + Maven 工程（多 Profile 支持）——`backend/`
- [x] 集成数据库连接与配置模板（PostgreSQL / Supabase 占位）——`backend/src/main/resources/application-*.yml`
- [x] 完成核心实体与仓储接口、编写 Flyway 初始迁移——`backend/src/main/java`、`backend/src/main/resources/db/migration`
- [x] 搭建认证模块（注册、登录、JWT）并提供测试用例——`backend/src/main/java/com/aitravelplanner/backend/auth`、`backend/src/test/java/com/aitravelplanner/backend/auth`
- [x] 搭建 API Key 管理骨架（保存 / 删除 / 测试）并补充测试——`backend/src/main/java/com/aitravelplanner/backend/settings`
- [x] 通过基础测试（Spring Boot + MockMvc）验证模块稳定性——`./backend/mvnw test`

## 阶段 3：前端框架与通用能力

- [x] 使用 Vite 创建 Vue 3 + TypeScript 项目并配置路径别名
- [x] 集成 Pinia、Vue Router、Axios 等基础依赖
- [x] 建立全局样式体系（自定义变量 + 响应式布局）
- [x] 实现登录/注册页面与全局布局（顶部导航 + 侧边栏）
- [x] 完成 API Key 设置页表单并与后端交互占位逻辑
- [x] 引入高德地图 SDK 基础封装，提供示例地图视图

## 阶段 4：核心业务功能迭代

### 4.1 行程规划

- [x] 设计行程数据模型的前端状态结构
- [ ] 实现行程需求输入表单（语音 / 文本）——已完成文本表单，语音录入待接入讯飞 API
- [ ] 接入科大讯飞语音转写（录音、签名、上传流程）
- [ ] 后端对接阿里云百炼模型，输出结构化计划
- [ ] 完成行程卡片视图与时间轴展示，支持拖拽调整
- [ ] 在地图中绘制每日路径并展示 POI 信息

### 4.2 预算与记账

- [ ] 设计预算模块 API（预算目标、费用记录、分类）
- [ ] 前端实现预算概览、分类统计、明细列表
- [ ] 集成语音记账流程（录音 → 转写 → 解析 → 入库）
- [ ] 提供 CSV 导出功能（后端或前端实现）

## 阶段 5：增强与辅助功能

- [ ] 实现行程提醒（站内通知 / 邮件）
- [ ] 拓展 POI 功能：基于高德服务获取附近推荐、快速插入行程
- [ ] 行程多版本管理：草稿 / 已发布 / 归档
- [ ] 增加旅行问答界面，复用百炼模型
- [ ] 完善错误处理与重试机制（语音 / 模型 / 地图）

## 阶段 6：测试、优化与发布

- [ ] 编写端到端测试（Cypress / Playwright）覆盖核心流程
- [ ] 性能与安全检查：首屏加载、接口响应、日志脱敏
- [ ] 完善 README（运行步骤、环境变量、Docker 使用、Key 配置）
- [ ] 编写部署脚本与 Dockerfile（多阶段构建）
- [ ] 配置 GitHub Actions：单元测试、构建、镜像推送
- [ ] 准备提交 PDF：包含仓库链接、运行说明、Key 获取方式（不含真实 Key）

## 阶段 7：运维与迭代计划

- [ ] 接入监控与日志（APM、阿里云日志等）
- [ ] 规划后续功能迭代（多用户协作、移动端适配、离线包等）
- [ ] 收集用户反馈渠道，建立需求评审与优先级流程
- [ ] 定期回顾开发流程与代码质量，整理技术债务清单
