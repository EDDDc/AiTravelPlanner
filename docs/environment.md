# 环境配置说明

项目采用“前端 Vue 3 + 后端 Spring Boot”的分层架构。为保证团队成员处于一致的开发环境，请参考以下指南。

## 1. 基础依赖

| 组件     | 推荐版本                | 备注                                                                                   |
| -------- | ----------------------- | -------------------------------------------------------------------------------------- |
| Node.js  | 20.x LTS                | 建议使用 `nvm` 或 `fnm` 管理版本；当前机器：`v22.20.0`，请切换至 20.x 以兼容前端依赖。 |
| 包管理器 | pnpm 8.x（或 npm 10.x） | 优先选择 pnpm，以提升多包管理效率。                                                    |
| Java     | Temurin/OpenJDK 21      | 当前机器：`23.0.1`，需安装并切换到 21。                                                |
| Maven    | 3.9+                    | 后端已采用 Maven，建议启用 Wrapper（仓库已提供 `mvnw`）。                              |
| Docker   | 24+                     | 用于本地构建和运行容器镜像。                                                           |

> 建议在 Windows 上使用 [scoop](https://scoop.sh/) 或 [winget](https://learn.microsoft.com/windows/package-manager/) 管理开发工具；跨平台可考虑 [asdf](https://asdf-vm.com/)。

## 2. 常用检查命令

```bash
node -v          # 期望输出 v20.x.x
npm -v           # 或 pnpm -v / yarn -v
java -version    # 期望输出 21.x.x
mvn -v
docker version
```

若输出版本不符合要求，请先升级或安装，以免后续构建失败。

## 3. IDE 与插件

- 推荐 VS Code 或 JetBrains 系列（IntelliJ IDEA、WebStorm）。
- 必装插件：EditorConfig、ESLint、Prettier、Docker、GitLens。
- Java 开发建议启用 Lombok/MapStruct 插件（后续可能使用）。
- 统一配置：
  - 换行符：LF
  - 编码：UTF-8
  - 保存时自动格式化与去除行尾空格

## 4. Git 钩子与质量保障

- 根目录已配置 Husky + lint-staged，占位执行 Prettier 校验。
- 后续将依据前端/后端脚手架补充 ESLint、Spotless、测试脚本等。
- 提交前可执行：
  ```bash
  npm run format:check
  ./backend/mvnw test
  ```

## 5. 环境变量约定

- 根目录提供 `.env.example`，作为环境变量模板。
- 推荐在前端使用 `frontend/.env.local`，后端使用 `backend/.env` 或 profile 专用的 `application-local.yml`。
- 所有敏感信息不得提交至仓库，需通过运行时注入或 CI/CD Secret 管理。

常用变量（摘录）：

- `SPRING_DATASOURCE_URL / USERNAME / PASSWORD / SCHEMA`
- `APP_SECURITY_JWT_SECRET / ISSUER / ACCESS_TTL / REFRESH_TTL`
- `BAILIAN_API_KEY`、`XFYUN_APP_ID`、`AMAP_WEB_KEY`

## 6. Docker 与镜像

- 统一采用多阶段构建压缩镜像体积（阶段 6 将补充 Dockerfile）。
- 本地调试可使用 `docker compose`（后续提供配置）。
- 生产环境镜像推送目标：阿里云镜像仓库，结合 GitHub Actions 完成自动化。

---

若需新增工具或调整版本，请在修改前更新本文档并通知团队成员，确保环境一致性。
