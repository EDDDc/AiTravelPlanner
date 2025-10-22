# 环境配置说明

本项目采用“前端 Vue 3 + 后端 Spring Boot”的分层架构。为确保所有成员在相同环境下开发，请参照下述配置：

## 1. 基础依赖

| 组件           | 推荐版本                | 备注                                                                                 |
| -------------- | ----------------------- | ------------------------------------------------------------------------------------ |
| Node.js        | 20.x LTS                | 建议使用 `nvm` 或 `fnm` 管理版本；当前机器：`v22.20.0`，请切换至 20.x 以保证兼容性。 |
| 包管理器       | pnpm 8.x（或 npm 10.x） | 优先选择 pnpm，以提升多包管理效率。                                                  |
| Java           | Temurin/OpenJDK 21      | 当前机器：`23.0.1`，需安装 21 并在开发时指定该版本。                                 |
| Maven / Gradle | Maven 3.9+ 或 Gradle 8+ | 根据后端脚手架确定其一；若使用 Gradle，建议启用 Wrapper。                            |
| Docker         | 24+                     | 用于本地构建和运行容器。                                                             |

> 建议安装 [`asdf`](https://asdf-vm.com/) 或在 Windows 下使用 [`scoop`](https://scoop.sh/) 来统一管理上述工具版本。

## 2. 必备命令检查

```bash
node -v          # 期望输出 v20.x.x
pnpm -v          # 期望输出 8.x.x（如使用 npm，可改为 npm -v）
java -version    # 期望输出 21.x.x
mvn -v           # 或 gradle -v
docker version
```

若输出版本不符合要求，请先完成升级或安装，避免后续构建失败。

## 3. IDE 与扩展

- 推荐使用 VS Code 或 JetBrains 全家桶：
  - 必装插件：EditorConfig、ESLint、Prettier、Docker、GitLens。
  - Java 开发建议安装 IntelliJ IDEA，启用 Lombok/MapStruct 插件（后续若使用）。
- 建议统一设置：
  - 默认换行符：LF
  - 文件编码：UTF-8
  - 保存时自动格式化、自动去除行尾空格

## 4. Git 钩子与质量保障

- 阶段 1 结束前会引入 Husky + lint-staged，执行以下检查：
  - 前端：ESLint、Prettier、Stylelint（若使用 Tailwind 需额外规则）
  - 后端：Spotless/Checkstyle、JUnit 测试
- 在脚手架搭建完成之前，可使用 `npm run lint`/`npm run format` 的占位脚本进行验证。

## 5. 环境变量约定

项目根目录提供 `.env.example`，作为各模块环境变量命名的参考。实际运行时请复制并命名为：

- 前端：`frontend/.env.local`
- 后端：`backend/.env` 或通过 Spring Profile 的 `application-local.yml`

**禁止**在任何配置文件中填写真实 Key。请通过系统设置页面或 CI/CD 的 Secret 功能注入。

## 6. Docker 与镜像仓库

- 统一使用多阶段构建减小镜像体积。
- 本地调试可使用 `docker compose`（文件将在阶段 6 提供）。
- 生产镜像推送目标：阿里云镜像仓库（配合 GitHub Actions 完成自动化）。

---

如需新增工具或版本，请在合并前更新此文档并通知所有成员，确保环境一致性。
