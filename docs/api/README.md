# 后端接口草稿（v0.2）

> 本文档同步当前已实现/规划中的主要接口。实际参数与响应请以后续的 Swagger/OpenAPI 描述为准，开发时需保持更新。

## 1. 认证模块 `/api/auth`

| 方法 | 路径        | 描述       | 请求体                             | 响应              |
| ---- | ----------- | ---------- | ---------------------------------- | ----------------- |
| POST | `/login`    | 用户登录   | `{ email, password }`              | `{ token, user }` |
| POST | `/register` | 注册新用户 | `{ email, password, displayName }` | `{ token, user }` |
| POST | `/refresh`  | 刷新 Token | `{ refreshToken }`                 | `{ token }`       |

## 2. 行程模块 `/api/plans`

| 方法 | 路径        | 描述             | 请求体             | 响应                  |
| ---- | ----------- | ---------------- | ------------------ | --------------------- |
| GET  | `/`         | 获取当前用户行程 | -                  | `TravelPlanSummary[]` |
| GET  | `/{planId}` | 获取行程详情     | -                  | `TravelPlanDetail`    |
| POST | `/`         | 创建行程（占位） | `TravelPlanCreate` | `TravelPlanDetail`    |

`TravelPlanCreate` 示例：

```json
{
  "title": "东京亲子行",
  "destinations": ["东京"],
  "startDate": "2025-02-01",
  "endDate": "2025-02-03",
  "budgetTotal": 12000,
  "preferences": {
    "travelers": ["family"],
    "interests": ["food", "anime"]
  }
}
```

- `endDate` 必须不早于 `startDate`，`destinations` 至少包含一个目的地。
- 行程详情中的 `days/activities` 当前由后端占位生成，后续将由 AI 规划结果替换。

## 3. 预算模块 `/api/plans/{planId}/expenses`

| 方法   | 路径           | 描述               | 请求体           | 响应                |
| ------ | -------------- | ------------------ | ---------------- | ------------------- |
| GET    | `/`            | 列出指定行程的费用 | `start/end` 可选 | `ExpenseResponse[]` |
| POST   | `/`            | 新增费用记录       | `ExpenseRequest` | `ExpenseResponse`   |
| PUT    | `/{expenseId}` | 更新费用记录       | `ExpenseRequest` | `ExpenseResponse`   |
| DELETE | `/{expenseId}` | 删除费用记录       | -                | `204 No Content`    |
| GET    | `/summary`     | 汇总总额与分类分布 | `start/end` 可选 | `ExpenseSummary`    |

- 查询参数 `start`、`end` 采用 ISO 8601（`OffsetDateTime`），不传则返回全部记录。
- `ExpenseRequest` 示例：
  ```json
  {
    "amount": 120.5,
    "currency": "CNY",
    "category": "DINING",
    "method": "MANUAL",
    "recordedAt": "2025-05-01T12:00:00+08:00",
    "notes": "午餐",
    "transcript": "语音识别结果"
  }
  ```
- `ExpenseSummary` 示例：
  ```json
  {
    "totalAmount": 350.75,
    "currency": "CNY",
    "categories": [
      { "category": "DINING", "amount": 120.5 },
      { "category": "TRANSPORT", "amount": 230.25 }
    ]
  }
  ```

## 4. 语音与大模型服务

| 方法 | 路径                        | 描述                    |
| ---- | --------------------------- | ----------------------- |
| POST | `/api/voice/transcriptions` | 上传音频 → 返回转写文本 |
| POST | `/api/llm/itinerary`        | 使用百炼模型生成行程    |
| POST | `/api/llm/assistant`        | 行程问答                |

所有外部调用均在后端完成，避免前端暴露 Key。`/api/voice/transcriptions` 目前返回占位文本，后续接入科大讯飞后输出真实转写结果。

## 5. 设置 / Key 管理 `/api/settings`

| 方法 | 路径             | 描述                                  |
| ---- | ---------------- | ------------------------------------- |
| GET  | `/apikeys`       | 获取当前用户 Key 列表                 |
| POST | `/apikeys`       | 新增/更新 Key (`{ provider, value }`) |
| POST | `/apikeys/test`  | 测试 Key 是否有效                     |
| GET  | `/notifications` | 获取通知配置                          |
| PUT  | `/notifications` | 更新通知渠道、阈值、偏好              |

## 6. 地图 / POI `/api/maps`

- `GET /poi/search?planId=&type=`：根据行程位置推荐附近 POI。
- `GET /routes?origin=&destination=&mode=`：封装高德路线规划。

## 7. 数据模型示例

```json
{
  "id": "a5f9c2d9-4c1b-4a2b-9278-5d3f8d90d8ae",
  "title": "东京亲子行",
  "destinations": ["东京"],
  "startDate": "2025-02-01",
  "endDate": "2025-02-03",
  "budgetTotal": 12000,
  "status": "DRAFT",
  "preferences": {
    "travelers": ["family"],
    "interests": ["anime"]
  },
  "days": [
    {
      "dayIndex": 1,
      "date": "2025-02-01",
      "summary": "占位行程概述",
      "notes": "详细日程稍后生成",
      "activities": [
        {
          "type": "OTHER",
          "name": "待生成活动",
          "description": "AI 生成内容将在此处展示"
        }
      ]
    }
  ]
}
```

---

更新流程：如新增接口或字段，需同步修改本文档、Swagger 文档，以及 `development_flow.md` 中的任务状态。
