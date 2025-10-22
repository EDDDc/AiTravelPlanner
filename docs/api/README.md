# 后端接口草稿（v0.1）

> 本文档提供阶段 1 的 API 结构草稿，用于后续在 Apifox/Swagger 中建模。接口路径可能随实现调整，请在开发时同步更新。

## 1. 认证模块 `/api/auth`

| 方法 | 路径        | 描述       | 请求体                             | 响应              |
| ---- | ----------- | ---------- | ---------------------------------- | ----------------- |
| POST | `/login`    | 用户登录   | `{ email, password }`              | `{ token, user }` |
| POST | `/register` | 注册新用户 | `{ email, password, displayName }` | `{ token, user }` |
| POST | `/refresh`  | 刷新 Token | `{ refreshToken }`                 | `{ token }`       |

## 2. 行程模块 `/api/plans`

| 方法   | 路径        | 描述                   | 请求体                          | 响应                    |
| ------ | ----------- | ---------------------- | ------------------------------- | ----------------------- |
| GET    | `/`         | 获取用户行程列表       | `query: page, pageSize, status` | `Paginated<TravelPlan>` |
| POST   | `/`         | 创建新行程（触发 LLM） | `CreatePlanRequest`             | `TravelPlan`            |
| GET    | `/{planId}` | 获取行程详情           | -                               | `PlanDetail`            |
| PUT    | `/{planId}` | 更新行程基础信息       | `UpdatePlanRequest`             | `TravelPlan`            |
| DELETE | `/{planId}` | 归档/删除行程          | -                               | `{ success: true }`     |

### 2.1 行程活动子资源

- `GET /{planId}/days`
- `POST /{planId}/days`
- `PUT /{planId}/days/{dayId}`
- `POST /{planId}/activities`
- `PUT /{planId}/activities/{activityId}`
- `DELETE /{planId}/activities/{activityId}`

活动请求体包括：

```json
{
  "dayId": "UUID",
  "type": "transport",
  "name": "成田机场 → 上野",
  "startTime": "08:00",
  "endTime": "09:30",
  "location": { "lat": 35.7738, "lng": 140.3929, "address": "..." },
  "costEstimate": 1200,
  "notes": "",
  "metadata": {}
}
```

## 3. 预算模块 `/api/plans/{planId}/budget`

| 方法   | 路径                    | 描述                                         |
| ------ | ----------------------- | -------------------------------------------- |
| GET    | `/summary`              | 获取预算概览（总预算、已用、剩余、分类统计） |
| POST   | `/expenses`             | 创建费用记录（语音/手动统一入口）            |
| PUT    | `/expenses/{expenseId}` | 更新费用记录                                 |
| DELETE | `/expenses/{expenseId}` | 删除费用记录                                 |
| GET    | `/export`               | 导出 CSV（返回文件或下载链接）               |

语音记账流程：前端上传音频至 `/api/voice/expenses`，后端与讯飞交互后落库。

## 4. 语音与模型服务

| 方法 | 路径                        | 描述                    |
| ---- | --------------------------- | ----------------------- |
| POST | `/api/voice/transcriptions` | 上传音频 → 返回转写文本 |
| POST | `/api/llm/itinerary`        | 使用百炼模型生成行程    |
| POST | `/api/llm/assistant`        | 行程问答                |

所有外部调用应在后端完成，前端仅获取结果，避免暴露 Key。

## 5. 设置与 Key 管理 `/api/settings`

| 方法 | 路径             | 描述                                          |
| ---- | ---------------- | --------------------------------------------- |
| GET  | `/apikeys`       | 获取当前用户 Key 列表（仅返回别名与更新时间） |
| POST | `/apikeys`       | 新增/更新 Key（`{ provider, value }`）        |
| POST | `/apikeys/test`  | 测试 Key 是否有效                             |
| GET  | `/notifications` | 获取通知配置                                  |
| PUT  | `/notifications` | 更新通知渠道、阈值、偏好                      |

## 6. 地图与 POI `/api/maps`

- `GET /poi/search?planId=&type=`：根据计划位置推荐附近 POI。
- `GET /routes?origin=&destination=&mode=`：封装高德路径规划。

## 7. 数据模型（接口视角）

```json
{
  "id": "UUID",
  "title": "东京亲子游",
  "destination": ["东京", "箱根"],
  "startDate": "2025-02-01",
  "endDate": "2025-02-05",
  "budgetTotal": 10000,
  "preferences": {
    "travelers": ["family"],
    "interests": ["food", "anime"]
  },
  "status": "active",
  "days": [
    {
      "dayIndex": 1,
      "date": "2025-02-01",
      "summary": "抵达 & 浅草寺",
      "activities": []
    }
  ]
}
```

完整字段说明见 `docs/domain_dictionary.md`。

---

后续会将以上接口导入 Swagger/OpenAPI 文档，并与后端实现保持同步。若有新增接口，请在此文档中记录，并更新开发流程状态。
