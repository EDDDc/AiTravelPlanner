# API Key 管理规范

为满足课程要求并保护账户安全，本项目对所有敏感凭证做如下规定：

## 1. 管理原则

- **禁止**在代码仓库（包含提交历史、Issue、Wiki）中出现任何真实 Key。
- Key 仅存储于：
  - 本地 `.env` / `application-local.yml`（未纳入版本控制）
  - CI/CD 平台的 Secret（例如 GitHub Actions Secrets、阿里云凭证中心）
- 所有 Key 的使用范围应最小化，按需授予权限。

## 2. 关键变量命名

项目根目录的 `.env.example` 提供变量模板，主要包括：

| 变量名                                                                                | 说明                                       |
| ------------------------------------------------------------------------------------- | ------------------------------------------ |
| `BAILIAN_API_KEY`                                                                     | 阿里云百炼平台 Key，用于调用大语言模型。   |
| `BAILIAN_MODEL_ID`                                                                    | 默认使用的百炼模型 ID，便于切换模型。      |
| `XFYUN_APP_ID` / `XFYUN_API_SECRET` / `XFYUN_API_KEY`                                 | 科大讯飞语音识别凭证。                     |
| `AMAP_WEB_KEY`                                                                        | 高德地图 Web 服务 Key。                    |
| `SUPABASE_URL` / `SUPABASE_ANON_KEY`                                                  | Supabase 项目连接信息（若采用 Supabase）。 |
| `SPRING_DATASOURCE_URL` / `SPRING_DATASOURCE_USERNAME` / `SPRING_DATASOURCE_PASSWORD` | 后端数据库连接。                           |

实际工程中可根据需要拆分为前后端独立配置文件。

## 3. 输入与存储流程

- 用户侧：在应用的“设置”页面手动输入各类 Key，前端仅在运行时使用，不做持久化。
- 服务端：通过 API 接收并加密存储 Key（推荐 KMS 或数据库加密字段），调用时解密。
- 配置变更时需记录审计日志，方便追踪。

## 4. 安全措施

- HTTPS 全链路加密，前后端通过 `Secure`/`HttpOnly` Cookie 或 Token 传递。
- 后端对 Key 进行加密持久化（如使用 AES + KMS）；若仅使用环境变量，则托管于安全的 Secret 管理器。
- 日志与错误栈禁止打印 Key 及其前后 4 位。
- 定期轮换 Key（至少每 90 天），轮换流程需配合配置项回滚机制。

## 5. 测试与验收

- 测试环境使用低权限 Key；生产 Key 需独立申请，做到权限隔离。
- 验收前执行静态代码扫描，确认仓库未包含敏感信息。
- 提交作业时，在 README 中说明 Key 获取方式与有效期（不少于 3 个月），但不提供真实值。

如发现 Key 泄露或违规使用，需立即撤销并更新文档与配置。
