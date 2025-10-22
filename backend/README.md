# AI Travel Planner Backend

基于 Spring Boot 3.3 + Maven，提供行程规划、预算管理、API Key 管理等后端服务。

## 运行环境

- Java 21（推荐 Temurin/OpenJDK）
- Maven 3.9+
- PostgreSQL 14+（开发环境）

## 常用命令

```bash
./mvnw clean verify          # 编译并运行测试
./mvnw spring-boot:run       # 启动开发环境（默认使用 dev profile）
./mvnw flyway:migrate -Dflyway.configFiles=src/main/resources/flyway-dev.conf
```

> Windows 用户可使用 `mvnw.cmd`。

## 配置说明

- 默认激活 `dev` Profile，可在启动时通过 `--spring.profiles.active=prod` 切换。
- 关键环境变量可参考仓库根目录的 `.env.example`。
- `application-dev.yml` 中的数据库配置使用 `SPRING_DATASOURCE_*` 变量。
- `application-test.yml` 使用 Testcontainers 的 JDBC URL（`jdbc:tc:postgresql:16:///...`），运行测试无需本地数据库。

## 目录结构

```
backend/
├── src/main/java        # 源码（待填充业务模块）
├── src/main/resources   # 配置、迁移脚本
├── src/test/java        # 测试代码
├── pom.xml
└── README.md
```

后续模块会以分层架构划分：

- `com.aitravelplanner.backend.config`：配置类
- `com.aitravelplanner.backend.domain`：实体、仓储接口
- `com.aitravelplanner.backend.application`：服务逻辑
- `com.aitravelplanner.backend.interfaces`：控制器、DTO

如需运行 Flyway 迁移，可在 `src/main/resources/db/migration` 中新增 SQL 脚本。
