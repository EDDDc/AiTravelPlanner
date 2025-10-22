# 业务字段字典（初稿）

| 实体         | 字段          | 类型                                                                   | 说明                                          |
| ------------ | ------------- | ---------------------------------------------------------------------- | --------------------------------------------- |
| User         | id            | UUID                                                                   | 唯一标识                                      |
|              | email         | string                                                                 | 登录邮箱                                      |
|              | password_hash | string                                                                 | 采用 Bcrypt/Scrypt 存储（不在客户端传输明文） |
|              | display_name  | string                                                                 | 展示昵称                                      |
|              | auth_provider | enum(local, oauth)                                                     | 登录方式                                      |
|              | avatar_url    | string                                                                 | 头像（可选）                                  |
|              | created_at    | timestamp                                                              | 注册时间                                      |
|              | updated_at    | timestamp                                                              | 更新时间                                      |
| UserSetting  | id            | UUID                                                                   |                                               |
|              | user_id       | UUID                                                                   | 关联用户                                      |
|              | provider      | enum(bailian, xfyun, amap, supabase)                                   | Key 类型                                      |
|              | key_alias     | string                                                                 | Key 别名，避免直接展示真实 key                |
|              | encrypted_key | string                                                                 | 加密存储后的值                                |
|              | updated_at    | timestamp                                                              | 最近更新                                      |
| TravelPlan   | id            | UUID                                                                   | 行程 ID                                       |
|              | user_id       | UUID                                                                   | 所属用户                                      |
|              | title         | string                                                                 | 行程标题                                      |
|              | destination   | string                                                                 | 目的地（可多值）                              |
|              | start_date    | date                                                                   | 出发日期                                      |
|              | end_date      | date                                                                   | 结束日期                                      |
|              | budget_total  | number                                                                 | 预算总额                                      |
|              | preferences   | jsonb                                                                  | 用户偏好（标签、同行人类型等）                |
|              | status        | enum(draft, active, archived)                                          | 行程状态                                      |
|              | created_at    | timestamp                                                              | 创建时间                                      |
|              | updated_at    | timestamp                                                              | 更新                                          |
| DayPlan      | id            | UUID                                                                   | 按天计划                                      |
|              | plan_id       | UUID                                                                   | 对应 TravelPlan                               |
|              | day_index     | integer                                                                | 第几天（从 1 开始）                           |
|              | date          | date                                                                   | 实际日期（便于处理跨日调整）                  |
|              | summary       | string                                                                 | 当日概述                                      |
|              | notes         | text                                                                   | 用户备注                                      |
| Activity     | id            | UUID                                                                   | 活动条目                                      |
|              | day_plan_id   | UUID                                                                   | 关联 DayPlan                                  |
|              | type          | enum(transport, attraction, dining, shopping, hotel, custom)           | 活动类型                                      |
|              | name          | string                                                                 | 活动名称                                      |
|              | description   | text                                                                   | 详细说明                                      |
|              | start_time    | time                                                                   | 开始时间                                      |
|              | end_time      | time                                                                   | 结束时间                                      |
|              | location      | jsonb                                                                  | { lat, lng, address }                         |
|              | cost_estimate | number                                                                 | 预估费用                                      |
|              | metadata      | jsonb                                                                  | LLM 附加信息（推荐理由、预订链接等）          |
| Expense      | id            | UUID                                                                   | 费用记录                                      |
|              | plan_id       | UUID                                                                   | 对应行程                                      |
|              | category      | enum(transport, accommodation, dining, entertainment, shopping, other) | 费用分类                                      |
|              | amount        | number                                                                 | 金额                                          |
|              | currency      | string                                                                 | 货币（默认 CNY）                              |
|              | method        | enum(voice, manual)                                                    | 录入方式                                      |
|              | transcript    | text                                                                   | 语音识别原文（method=voice 时）               |
|              | recorded_at   | timestamp                                                              | 记录时间                                      |
|              | notes         | text                                                                   | 备注                                          |
| Notification | id            | UUID                                                                   |                                               |
|              | plan_id       | UUID                                                                   | 对应行程                                      |
|              | type          | enum(reminder, task, warning)                                          | 通知类型                                      |
|              | channel       | enum(email, in_app)                                                    | 推送渠道                                      |
|              | trigger_time  | timestamp                                                              | 触发时间                                      |
|              | payload       | jsonb                                                                  | 自定义数据                                    |
|              | status        | enum(pending, sent, failed)                                            | 状态                                          |

评分/标签等扩展字段可在后续迭代时追加。
