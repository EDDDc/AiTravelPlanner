# AI Travel Planner PRD v0.3

## 1. 产品概述

- 产品定位：面向自由行旅客的 AI 驱动旅行规划与出行助手。
- 产品愿景：通过语音与大模型结合，让用户在规划与出行过程中快速获得行程方案、预算管理和实时辅助。
- MVP 范围：实现语音录入、行程自动生成、预算概览、用户登录与云同步、地图展示。

## 2. 目标与关键指标

- 主要目标
  1. 3 分钟内生成覆盖交通、住宿、景点、餐饮的多日行程。
  2. 支持语音需求输入与语音记账，2 分钟内同步预算更新。
  3. 用户可在任意登录设备查看同一份行程。
- 核心指标
  - 行程生成成功率 ≥ 95%
  - 语音识别准确率 ≥ 90%
  - 首屏行程加载时间 ≤ 3s（良好网络）
  - 日活行程编辑次数 ≥ 1 次/用户

## 3. 用户画像

- 核心用户：25-40 岁城市白领，自由行经验丰富，乐于尝试新技术。
- 次要用户：亲子出游主理人、轻奢旅行用户。
- 痛点：行程规划耗时、预算分配困难、出行中缺乏实时提醒。

## 4. 核心场景

1. 出行前：语音描述目的地、日期、预算、偏好，生成可执行行程草案。
2. 准备阶段：对每日行程进行增删改、添加备注、确认地图路线。
3. 出行中：查看地图导航、获取附近推荐、语音记录开销。
4. 行程后：导出行程与费用报表，复用历史计划。

## 5. 功能范围（MVP）

- 语音与文本输入
  - Web 端语音录制，调用科大讯飞语音识别 API 转写。
  - 表单补充旅行参数，支持文本编辑与语音文本互转。
- 行程规划
  - 调用阿里云百炼平台大语言模型生成多日行程结构。
  - 行程卡片与时间轴视图，支持拖拽调整、替换活动。
  - 高德地图嵌入路线展示，POI 点击查看详情与导航链接。
- 预算与记账
  - 预算概览：按日与类别统计。
  - 语音记账：转换金额、分类、备注，可手动修正。
  - 费用明细列表、CSV 导出。
- 用户与数据
  - 注册/登录（邮箱密码 + 第三方 OAuth 可选）。
  - 多计划管理：创建、复制、归档。
  - 云端同步：行程、偏好、费用在多端实时一致。
- 实时辅助
  - 行程提醒（站内/邮件）。
  - 基于高德 POI 的临时推荐。
  - 旅行问答：基于百炼模型提供追加咨询支持。
- API Key 管理
  - 前端设置页面提供 Key 输入与更新入口。
  - Key 持久化放置于后端安全配置或密文存储，支持按需注入。

## 6. 信息架构与流程

- 主流程：登录 → 仪表盘 → 新建计划 → 语音或文本录入 → 行程生成 → 行程确认或编辑 → 地图查看 → 预算记账。
- 导航结构：顶部计划切换；侧边栏（概览、行程、地图、预算、设置）；主内容区支持视图切换。

## 7. 技术方案

- 前端
  - Vue 3 + Vite + TypeScript，UI 框架建议 Naive UI 或 Element Plus。
  - Pinia 管理全局状态，Vue Router 负责路由。
  - 高德地图 JS SDK 集成地图与路线渲染。
  - Web Audio API 采集语音，调用科大讯飞 Web API。
- 后端
  - Spring Boot 3（Java 或 Kotlin），提供 RESTful API，整合语音、地图、阿里云百炼模型服务。
  - Spring Security + JWT 实现鉴权，并预留 OAuth2 扩展。
  - 提供 WebSocket 或 SSE 以支持实时同步。
- 数据与认证
  - 推荐使用 Supabase（PostgreSQL + Auth + Storage）或自建 PostgreSQL + Keycloak，根据资源情况决策。
  - 对象存储保存语音文件与行程附件。
- 部署与交付
  - 前后端分别构建 Docker 镜像，多阶段构建减小体积。
  - GitHub Actions 在推送时自动构建与推送镜像，Secrets 管理 API Key 与凭证。

## 8. 数据模型（初稿）

- user：id, email, display_name, auth_provider, created_at
- travel_plan：id, user_id, title, destination, start_date, end_date, budget_total, preferences_json, status, created_at, updated_at
- day_plan：id, plan_id, day_index, summary, notes
- activity：id, day_plan_id, type, name, start_time, end_time, cost_estimate, location_lat, location_lng, metadata_json
- expense：id, plan_id, category, amount, method(voice/manual), recorded_at, transcript
- notification：id, plan_id, type, payload_json, created_at
- user_settings：id, user_id, provider, key_alias, encrypted_key

## 9. 非功能需求

- 性能：语音识别响应 ≤ 5s；行程生成 ≤ 20s；支持 100 并发用户。
- 安全：敏感数据加密存储；API Key 仅通过配置或设置页面输入获取，严禁写入仓库或日志；权限与访问日志可追溯。
- 可用性：兼容桌面与移动端；语音交互对常见口音友好，提供文本 fallback。
- 可靠性：核心服务可用性 ≥ 99%；提供失败重试与熔断降级策略。
- 合规：遵守阿里云百炼、科大讯飞、高德地图使用条款与数据合规要求。

## 10. 迭代排期示例

- Sprint 1：需求复盘、低保真原型、语音输入 MVP、Key 配置入口设计。
- Sprint 2：行程生成接口、行程编辑、地图展示与 POI 详情。
- Sprint 3：预算模块、语音记账、用户体系与云同步。
- Sprint 4：Docker 化、CI/CD、文档与验收，准备 PDF 与提交流程。

## 11. 风险与对策

- 语音识别误差：保留原始音频与转写文本，支持手动修订。
- 百炼模型偏差：模板化提示词，提供用户偏好调节参数，预留模型切换能力。
- 第三方服务限额：缓存常用结果，准备备用 Key，监控配额预警。
- Key 安全风险：所有 Key 通过设置页面或服务器环境变量配置，不写入代码仓库，前端存储使用浏览器安全存储并加密传输。
- 网络波动：提供离线提示与重试逻辑，关键操作添加保存草稿。

## 12. 交付要求

- 提交包含 GitHub 仓库地址与 README 的 PDF。
- GitHub 仓库提供源码、运行说明、Docker 镜像使用指南；Actions 自动构建并推送镜像。
- 使用阿里云百炼平台 Key，需在 README 单独说明获取方式与有效期（不少于 3 个月），但不得在代码或公开提交中展示 Key。
