# AI Travel Planner 开发流程

## 阶段 0：项目启动与环境准备

- [x] 确认团队角色分工（前端 / 后端 / 测试 / 运维）
- [x] 建立项目管理机制（暂以文档记录，计划迁移到 GitHub Projects，见 `docs/project_management.md`）
- [x] 搭建开发环境：Node.js、Java/Spring Boot、包管理工具、Docker（见 `docs/environment.md`）
- [x] 配置代码规范与工具链：Lint、Prettier、EditorConfig、Husky + lint-staged
- [x] 编写 `.env.example`，约定 API Key、数据库等环境变量
- [x] 制定 API Key 管理规范（见 `docs/api-key-management.md`）

## 阶段 1：产品设计与原型确认

- [x] 低保真原型（行程概览 / 地图 / 预算 / 设置），参见 `docs/wireframes.md`
- [x] 信息架构与导航流程确认，参见 `docs/information_architecture.md`
- [x] 用户故事与验收标准（Given-When-Then），参见 `docs/user_stories.md`
- [x] 确认 PRD、领域词典等文档，参见 `AI_Travel_Planner_PRD_v0.3.md`、`docs/domain_dictionary.md`
- [x] 建立接口文档草稿，参见 `docs/api/README.md`

## 阶段 2：后端基础设施

- [x] 初始化 Spring Boot + Maven 工程（多 Profile 支持），目录 `backend/`
- [x] 配置数据库连接模板（PostgreSQL / Supabase 占位），见 `backend/src/main/resources/application-*.yml`
- [x] 完成核心实体、存储接口与 Flyway 初始迁移
- [x] 搭建认证模块（注册 / 登录 / JWT），并补充测试
- [x] 搭建 API Key 管理模块（增删改查 + 测试）
- [x] 通过基础自测：`./backend/mvnw test`

## 阶段 3：前端框架与通用能力

- [x] 使用 Vite 创建 Vue 3 + TypeScript 项目，配置路径别名
- [x] 集成 Pinia、Vue Router、Axios 等基础依赖
- [x] 建立全局样式体系（自定义变量 + 响应式布局）
- [x] 实现登录 / 注册页面与全局布局（含导航）
- [x] 完成 API Key 设置页占位逻辑
- [x] 引入高德地图 SDK 封装，提供地图示例视图

## 阶段 4：核心业务功能迭代

### 4.1 行程规划

- [x] 设计前端行程状态结构
- [x] 完成行程需求输入表单：文本入口可用；语音录入可调用占位转写（待接入讯飞）
- [ ] 整合讯飞语音转写流程（录音 → 转写 → 标签 → 上传）
- [ ] 后端接入阿里云百炼模型，输出结构化行程方案
- [ ] 实现行程卡片视图、时间轴展示、拖拽调整
- [ ] 地图模式下展示每日路线与 POI 信息

### 4.2 预算与记账

- [x] 设计预算模块 API：提供费用 CRUD、日期过滤、分类汇总（见 `ExpenseController`）
- [ ] 前端实现预算概览、分类统计、明细列表
- [ ] 语音记账流程（录音 → 转写 → 分类 → 入库）
- [ ] CSV 导出能力（后端或前端实现）

## 阶段 5：增强与辅助功能

- [ ] 行程提醒（站内通知 / 邮件）
- [ ] 扩展 POI 能力：基于高德获取附近推荐，快速插入行程
- [ ] 行程多版本管理（草稿 / 已发布 / 归档）
- [ ] 旅行问答模块，复用百炼模型
- [ ] 错误处理与重试机制（语音 / 模型 / 地图）

## 阶段 6：测试、优化与发布

- [ ] 编写端到端测试（Cypress / Playwright）覆盖核心流程
- [ ] 性能与安全检查：首屏加载、接口响应、日志脱敏
- [ ] 完善 README（运行步骤、环境变量、Docker 使用、Key 配置）
- [ ] 编写部署脚本与 Dockerfile
- [ ] 配置 GitHub Actions（单元测试 / 构建 / 镜像推送）
- [ ] 准备提交材料：仓库链接、运行说明、Key 获取方式（不含真实 Key）

## 阶段 7：运维与迭代规划

- [ ] 接入监控与日志（APM、阿里云日志等）
- [ ] 规划后续功能迭代（多人协作、移动端适配、离线包等）
- [ ] 收集用户反馈渠道，建立需求评审与优先级流程
- [ ] 定期回顾开发流程与代码质量，整理技术债务清单
