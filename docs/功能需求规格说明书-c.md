# NexusIX-Platform 功能需求规格说明书（FRS）

**文档版本**：1.0  
**依据**：`create_table.sql` 中生效 DDL 及字段 COMMENT  
**技术前提**：PostgreSQL 17+；主键为雪花 BIGINT；无物理外键；核心业务表统一 `is_deleted` 逻辑删除（`sys_resource_usage`、`sys_user_token`、`sys_login_log`、`sys_data_audit_log`、`sys_inbox_message`、`sys_notice_user_rel`、`sys_user_group_rel`、`sys_user_role_rel` 等部分表无该字段，按各表语义单独说明）。

---

## 1. 系统概述

### 1.1 产品定位

NexusIX-Platform 是一套基于 PostgreSQL 的**多租户 SaaS 底座**：在**租户与子租户树**、**套餐与订阅**、**配额与计量**之上，提供 **IAM（用户/角色/策略型权限）**、**组织架构（部门/岗位/用户组/数据范围）**、**动态配置（表单/数据源/打印）**、**订单与发票**、**公告与消息中心（模板、站内信、定时任务）** 及 **操作/登录/数据变更审计**。核心价值是：**一套平台支撑多行业扩展**，通过 JSONB 与配置表实现“配置即能力”，同时用租户上下文与策略表保证隔离与细粒度授权。

### 1.2 目标用户（由表结构推断）

| 角色 | 说明 |
|------|------|
| **平台超级管理员** | 维护系统级租户（`tenant_id=0`）、全局套餐、系统字典/菜单/权限、跨租户运营与审计。 |
| **租户管理员** | 在某租户内 `sys_user_tenant_rel.is_admin=true`，负责成员、部门、角色、安全策略、业务配置；不一定等同于某固定“管理员角色”。 |
| **租户普通用户** | 绑定租户与部门，持租户内角色与数据范围，使用业务功能。 |
| **财务人员/开票人员** | 处理 `bill_order`、`bill_invoice` 相关流程（权限由策略与角色控制）。 |
| **运维/安全审计人员** | 查看 `sys_oper_log`、`sys_login_log`、`sys_data_audit_log`。 |

### 1.3 核心业务流程图

```mermaid
flowchart LR
  A[平台开通租户] --> B[维护套餐 prod_package / 配额模板]
  B --> C[租户订阅 sys_tenant_subscription]
  C --> D[同步租户主套餐 package_id / expire_time]
  D --> E[配额基线 = 套餐配额 + 调整项 sys_tenant_quota_adjustment]
  E --> F[邀请用户 / 注册 sys_user]
  F --> G[用户-租户绑定 sys_user_tenant_rel]
  G --> H[分配角色 sys_user_role_rel]
  H --> I[策略授权 sys_permission_policy 可选]
  I --> J[登录选租户 / 发 Token sys_user_token]
  J --> K[请求鉴权: RBAC + 策略合并 + 数据范围 sys_role / sys_role_dept_rel]
```

---

## 2. 功能模块清单

| 模块编号 | 模块名称 | 核心功能点摘要 | 优先级 |
|---------|----------|----------------|--------|
| M01 | 租户中心 | 租户档案、父子树、状态/过期、扩展属性、主套餐引用 | P0 |
| M02 | 计费与套餐 | 套餐定义、周期/价格、配额模板、上下架 | P0 |
| M03 | 订阅与配额 | 订阅生命周期、自购/父租分配、自动续费、配额调整、用量流水 | P0 |
| M04 | 订单与发票 | 订单创建支付、明细 JSON、发票申请与状态 | P1 |
| M05 | IAM 账户 | 全局用户、密码与状态、登录痕迹字段 | P0 |
| M06 | 租户成员与管理员标志 | 用户-租户-主部门、租户管理员位（与角色解耦） | P0 |
| M07 | 角色与权限资源 | 多层级角色、数据范围、权限树、菜单与 perm 映射 | P0 |
| M08 | 权限策略引擎 | 系统/租户/角色/用户目标 + 允许/拒绝 + 优先级 + 继承开关 | P0 |
| M09 | 租户安全 | 密码长度/复杂度/过期、登录失败锁定 | P0 |
| M10 | 会话与 Token | 多租户上下文 Token、设备、过期、失效控制 | P0 |
| M11 | 组织架构 | 部门树、岗位、用户组（静/动）、角色-部门数据权限 | P0 |
| M12 | 动态配置中心 | 动态表单、动态数据源、打印模板（含纸张/版本/默认） | P1 |
| M13 | 系统支撑 | 菜单、系统/租户字典、文件元数据 | P1 |
| M14 | 公告中心 | 公告发布范围、用户已读状态 | P1 |
| M15 | 消息中心 | 消息模板、站内信、定时/周期/事件类调度任务 | P1 |
| M16 | 审计与日志 | 操作日志、登录日志、字段级数据审计 | P1 |

---

## 3. 详细功能需求

### 3.1 租户中心（M01）

**功能名称**：租户主数据与层级管理  

**功能描述**：维护租户名称、编码、联系人、状态、服务过期时间、当前主套餐 ID，并支持**无限层级**子租户（`parent_id`、`ancestors` 物化路径）。`ext_attributes` 承载行业扩展。

**前置条件**：平台级操作权限；创建子租户时需校验父租户存在且未删除。

**输入要素**：`tenant_name`、`tenant_code` 必填；`parent_id` 默认 0（根）；`ancestors` 由应用层在保存时重算；`status` 1/0；`expire_time` 可空；`package_id` 默认 0；`ext_attributes` 合法 JSON。

**处理逻辑**：  
- 新建/移动节点时更新 `ancestors`，避免环。  
- `tenant_id=0` 表示系统级数据载体（与字典等表语义一致），与“业务租户”区分。  
- 删除为逻辑删除：`is_deleted=1`，查询默认过滤。  
- `expire_time` 可与订阅结束时间联动（应用层一致性问题）。

**输出结果**：租户记录创建/更新；失败时返回校验或唯一编码冲突信息。

**关联数据表**：`sys_tenant`、`sys_tenant_subscription`（间接）、`sys_user_tenant_rel`、`sys_dept` 等。

**优先级与预估工时**：P0；约 8–12 人日（含树维护、与订阅联动）。

---

### 3.2 产品套餐与配额模板（M02）

**功能名称**：可售卖套餐及内置配额定义  

**功能描述**：`prod_package` 定义名称、编码、描述、价格、`cycle_type`（天/月/年）与 `cycle_value`；`ext_config` 存功能开关等；`prod_package_quota` 按 `resource_code` 定义配额值、单位、是否允许超额。

**前置条件**：平台侧维护权限；下架套餐需考虑已有订阅策略。

**输入要素**：套餐编码唯一；价格 `DECIMAL(10,2)`；`status` 上下架；配额 `quota_value` 必填；`is_allow_overage` 布尔。

**处理逻辑**：套餐与配额行软删除；租户侧“有效配额”需合并订阅、调整项与用量（见 3.3）。

**输出结果**：套餐及配额模板 CRUD；列表支持排序 `sort_order`。

**关联数据表**：`prod_package`、`prod_package_quota`、`sys_tenant`、`sys_tenant_subscription`。

**优先级与预估工时**：P0；约 6–10 人日。

---

### 3.3 订阅、配额调整与用量（M03）

**功能名称**：订阅生命周期与配额闭环  

**功能描述**：`sys_tenant_subscription` 记录租户对套餐的订阅区间、状态、**自动续费**、订阅类型（自购/父租户分配）、`parent_grant_id` 关联分配来源。`sys_tenant_quota_adjustment` 记录套餐外增减：加油包、补偿、惩罚，含生效/过期时间。`sys_resource_usage` 记录按 `resource_code` 的消耗流水（可关联业务类型与业务 ID）。

**前置条件**：租户已存在；套餐有效；扣减用量前需校验当前上下文租户。

**输入要素**：订阅 `start_time`/`end_time` 必填；`status` 生效/过期；`is_auto_renew`；调整项 `adjust_value` 可正可负；`adjust_type` 1/2/3；`expire_time` 空表示永久有效。

**处理逻辑**：  
- **有效订阅**驱动租户服务能力；过期后策略：只读/禁止登录等由产品定义，库表通过 `status`、`end_time` 支撑。  
- **有效配额**建议公式（应用层）：各资源 = 当前生效订阅对应套餐配额之和（多订阅场景若有）+ 在有效期内的 `adjust_value` 汇总 − 已消耗（由 `sys_resource_usage` 聚合或异步汇总表，DDL 未要求汇总表则实时聚合）。  
- `is_allow_overage` 为 false 时，超额操作应拒绝并记日志。  
- 父租户分配：`subscription_type=2` 与 `parent_grant_id` 可追溯来源。

**输出结果**：订阅与调整记录；用量插入成功/失败（配额不足）。

**关联数据表**：`sys_tenant_subscription`、`prod_package`、`prod_package_quota`、`sys_tenant_quota_adjustment`、`sys_resource_usage`、`sys_tenant`。

**优先级与预估工时**：P0；约 12–18 人日（含并发扣减与对账）。

---

### 3.4 订单与发票（M04）

**功能名称**：租户订单与开票  

**功能描述**：`bill_order` 记录订单号、产品类型（套餐/配额包）、金额、支付状态、渠道与时间；`order_items`、`ext_attributes` 为 JSONB。`bill_invoice` 关联订单，维护发票类型、抬头、税号、金额、状态及文件 URL。

**前置条件**：租户上下文；创建发票需关联有效订单。

**输入要素**：`order_no` 唯一；`total_amount` 必填；订单状态 0/1/2；发票 `invoice_title`、`tax_id`、`amount` 必填。

**处理逻辑**：支付完成后可触发订阅生效或配额调整（应用层编排）；发票状态机：待开具→已开具→邮寄/作废等。

**输出结果**：订单与发票记录及状态变更。

**关联数据表**：`bill_order`、`bill_invoice`、`sys_tenant`。

**优先级与预估工时**：P1；约 10–15 人日（支付对接另计）。

---

### 3.5 全局用户与租户成员（M05–M06）

**功能名称**：全局账号与多租户任职  

**功能描述**：`sys_user` 存全局用户名、加密密码、联系方式、**全局**状态与最近登录信息。`sys_user_tenant_rel` 实现同一用户在不同租户的任职：`dept_id` 为主部门且须属于该租户。

**前置条件**：邀请/创建用户需权限；绑定租户时租户有效。

**输入要素**：用户名、密码必填；邮箱/手机格式校验；`is_admin` 标识**租户管理员**，与角色无关——解决同一人 A 租户管理员、B 租户普通成员。

**处理逻辑**：  
- 用户软删除后应禁止登录并清理或失效 Token（策略由应用定）。  
- 租户切换：**每次会话仅一个当前租户**，所有部门/角色/策略查询必须带 `tenant_id` 过滤（与 `sys_role_dept_rel` 注释一致）。

**输出结果**：用户与关系表记录；失败返回重复用户名等。

**关联数据表**：`sys_user`、`sys_user_tenant_rel`、`sys_dept`。

**优先级与预估工时**：P0；约 8–12 人日。

---

### 3.6 角色、权限资源、用户角色（M07）

**功能名称**：RBAC 基础模型与数据范围  

**功能描述**：`sys_role` 含 `role_level`（系统/租户/用户）、`tenant_id`（系统级为 0）、`data_scope`（全部/本部门/本人/自定义）。`sys_permission` 为菜单/按钮/接口/数据字段等资源树。`sys_user_role_rel` 绑定用户与角色且**带 tenant_id**，保证角色归属租户一致。`sys_role_dept_rel` 在数据范围为自定义时绑定可见部门。`sys_menu` 提供前端导航与 `perm_code` 映射。

**前置条件**：分配角色时角色 `tenant_id` 必须等于当前租户（系统级角色仅平台上下文可用）。

**输入要素**：角色编码在租户内唯一（应用层约束）；`data_scope` 与 `sys_role_dept_rel` 联动——自定义时必须配置部门列表。

**处理逻辑**：数据权限 SQL 需拼接部门树与 `ancestors`（与 `sys_dept` 配合）。

**输出结果**：角色、权限、菜单树及用户授权结果。

**关联数据表**：`sys_role`、`sys_permission`、`sys_user_role_rel`、`sys_role_dept_rel`、`sys_menu`、`sys_dept`。

**优先级与预估工时**：P0；约 14–20 人日。

---

### 3.7 权限策略（RBAC + ABAC 混合）（M08）

**功能名称**：四层目标策略与允许/拒绝合并  

**功能描述**：`sys_permission_policy` 通过 `target_type` 指定策略作用在**系统、租户、角色、用户**哪一层，`target_id` 为对应实体 ID，`permission_id` 指向细粒度权限，`action` 为**允许或拒绝**，`priority` 越大越优先，`inheritance_enabled` 控制是否向下继承。

**前置条件**：平台或租户策略管理权限；目标实体与权限已存在。

**输入要素**：`target_type` 1–4；`action` 1–2；`priority` 整型；继承布尔。

**处理逻辑（推导）**：  
- **RBAC 基底**：用户最终权限 = 角色权限集合 ∪ 直接绑定（若有）。  
- **策略叠加**：对同一 `permission_id` 合并系统→租户→角色→用户多层策略；**拒绝可覆盖允许**（典型 ABAC 需产品明确：建议“显式拒绝优先于允许”，同优先级时拒绝优先；再以 `priority` 解决冲突）。  
- **继承**：`inheritance_enabled=false` 时，子层不再继承该条策略（用于租户特例、用户特例）。  
- `target_type=系统` 时 `target_id` 语义需统一（如固定 0 或平台实体 ID），由实现约定。

**输出结果**：鉴权结果允许/拒绝；审计可记录匹配到的策略 ID（若需可扩展，当前表无审计字段）。

**关联数据表**：`sys_permission_policy`、`sys_permission`、`sys_role`、`sys_user`、`sys_tenant`。

**优先级与预估工时**：P0；约 12–18 人日（含缓存与单测）。

---

### 3.8 租户安全与 Token（M09–M10）

**功能名称**：租户密码与登录锁定；多设备会话  

**功能描述**：`sys_tenant_security` 按租户配置最小密码长度、复杂度、过期天数、失败锁定次数与锁定时长。`sys_user_token` 记录某用户在某租户下的 Token、设备、过期与有效状态，用于多端与强制下线。

**前置条件**：用户属于租户；登录成功写 Token。

**输入要素**：安全策略为非负整数；Token 串唯一性由应用保证。

**处理逻辑**：注册/改密时按租户策略校验；登录失败计数达 `login_fail_limit` 锁定 `lock_duration` 分钟；登出或风控将 `status=0`。

**输出结果**：策略生效；Token 创建/失效。

**关联数据表**：`sys_tenant_security`、`sys_user_token`、`sys_user`、`sys_tenant`。

**优先级与预估工时**：P0；约 6–10 人日。

---

### 3.9 组织扩展：岗位与用户组（M11）

**功能名称**：部门下岗位与跨部门用户组  

**功能描述**：`sys_post` 属于部门与租户。`sys_user_group` 支持静态/动态组，用于跨部门授权（与角色配合由产品定义）。`sys_user_group_rel` 为组成员（无 `is_deleted`，删除可采用物理删或应用层规范）。

**前置条件**：部门租户一致；组内用户须已绑定该租户。

**输入要素**：岗位/组名称、编码；`group_type` 1/2。

**处理逻辑**：动态组规则若仅存业务层，需与 `group_type=2` 对齐（DDL 未存规则 JSON，可后续扩展）。

**输出结果**：岗位、组成员维护结果。

**关联数据表**：`sys_post`、`sys_user_group`、`sys_user_group_rel`、`sys_dept`。

**优先级与预估工时**：P0；约 6–8 人日。

---

### 3.10 动态配置中心：表单、数据源、打印（M12）——配置即功能

**功能名称**：低代码/零代码扩展三件套  

**功能描述**：  
- **动态表单** `sys_form_config`：按 `biz_type` 与租户隔离，`form_schema`（JSONB）定义字段、校验、选项；`version` 支持版本化与回滚。  
- **动态数据源** `sys_datasource_config`：`datasource_type` 支持业务表/API/字典/SQL；`datasource_config` 描述取数逻辑；支持缓存及过期秒数。  
- **打印模板** `sys_print_template`：`template_type` HTML/Markdown/JSON 等，`template_content` + `template_config` 变量与条件；纸张大小、方向、默认模板、版本。

**前置条件**：租户管理员或配置权限；`biz_type`/`template_code`/`datasource_code` 在租户内唯一（应用层）。

**输入要素**：JSON Schema 合法性校验；停用状态不可被运行时选用。

**处理逻辑**：运行时渲染引擎根据 `biz_type` 拉取启用且合适版本的表单/模板/数据源；变更版本不影响历史单据时可复制版本号策略。

**输出结果**：配置 CRUD；运行时动态 UI、下拉数据、打印/PDF。

**关联数据表**：`sys_form_config`、`sys_datasource_config`、`sys_print_template`。

**优先级与预估工时**：P1；约 20–30 人日（含渲染器与安全沙箱）。

---

### 3.11 字典、菜单、文件（M13）

**功能名称**：系统/租户字典；菜单导航；文件元数据  

**功能描述**：`sys_dict` 的 `tenant_id=0` 为系统字典，否则租户字典；`sys_dict_item` 为键值对。`sys_menu` 树与权限码。`sys_file` 存租户文件元数据及 URL（存储实现不在 DDL）。

**前置条件**：字典与菜单维护权限分平台/租户。

**输入要素**：`dict_type` 唯一性分 scope；文件路径与 URL 必填。

**处理逻辑**：列表接口高频，建议缓存字典全量；文件删除软删除。

**关联数据表**：`sys_dict`、`sys_dict_item`、`sys_menu`、`sys_file`。

**优先级与预估工时**：P1；约 8–12 人日。

---

### 3.12 公告与已读（M14）

**功能名称**：公告发布与阅读状态  

**功能描述**：`sys_notice` 支持类型、发布状态、`target_type`（全员/指定租户/指定用户）、`target_ids` 列表、`tenant_id=0` 为系统公告。`sys_notice_user_rel` 记录用户在租户下的已读状态与时间。

**前置条件**：发布权限；用户已登录租户。

**输入要素**：标题必填；目标类型与 ID 列表一致。

**处理逻辑**：打开公告写已读；列表过滤目标受众。

**关联数据表**：`sys_notice`、`sys_notice_user_rel`。

**优先级与预估工时**：P1；约 5–8 人日。

---

### 3.13 消息模板、站内信、定时发送（M15）

**功能名称**：可模板化消息与调度  

**功能描述**：`sys_message_template` 支持系统模板（`tenant_id=0`）与租户模板，含变量定义与示例 JSON、多语言。`sys_inbox_message` 为用户收件箱，支持优先级、已读/归档、过期、操作按钮。`sys_message_schedule` 绑定模板，定义目标、触发类型（单次/周期/事件）、`execute_time`、`repeat_rule`、`trigger_condition`，并跟踪执行次数与状态。

**前置条件**：模板启用；调度任务创建权限。

**输入要素**：`execute_time` 必填；周期任务 JSON 格式校验。

**处理逻辑**：调度器扫描 `status=待执行` 且到达时间，渲染模板变量后写入 `sys_inbox_message` 或对接外部通道（DDL 未表驱动通道，可扩展）；事件触发由业务投递。

**输出结果**：站内信记录；任务状态流转。

**关联数据表**：`sys_message_template`、`sys_inbox_message`、`sys_message_schedule`。

**优先级与预估工时**：P1；约 12–18 人日（含调度可靠性）。

---

### 3.14 审计与日志（M16）

**功能名称**：操作、登录、数据变更审计  

**功能描述**：`sys_oper_log` 记模块、业务类型、请求方法、操作人、IP、参数与结果摘要、成功失败。`sys_login_log` 记登录成败与终端信息。`sys_data_audit_log` 按表名与记录 ID 存 `old_value`/`new_value` JSONB，操作类型更新/删除。

**前置条件**：切面或拦截器自动写入；数据变更审计需白名单表。

**输入要素**：敏感字段脱敏后写入 `oper_param`/`json_result`。

**处理逻辑**：日志表无软删除，需归档与分区策略（产品级 NFR）。

**关联数据表**：上述三张表。

**优先级与预估工时**：P1；约 6–10 人日。

---

## 4. 非功能需求（NFR）

### 4.1 性能

- **权限与策略**：对 `sys_permission`、`sys_permission_policy`、`sys_user_role_rel` 的合并结果建议按 `(user_id, tenant_id)` 缓存，失效事件在角色/策略变更时触发。  
- **字典与菜单**：全量缓存 + 版本号或广播失效；租户字典须带 `tenant_id` 维度。  
- **租户树与部门树**：`ancestors` 前缀查询需合适索引（应用层建索引建议：`tenant_id` + `ancestors`）。  
- **用量与配额**：高频写入 `sys_resource_usage` 时可采用批量插入或异步聚合，避免每次请求全表扫描。  
- **消息调度**：`sys_message_schedule` 按 `execute_time`、`status` 建索引，避免全表扫。

### 4.2 安全性

- **密码**：存储为加密后密文；强度与过期遵循 `sys_tenant_security`。  
- **隔离**：所有租户数据查询强制 `tenant_id` 条件；系统级 `tenant_id=0` 数据仅平台角色可访问。  
- **Token**：HTTPS 传输；支持失效与踢下线。  
- **审计**：关键操作写 `sys_oper_log`；敏感字段变更写 `sys_data_audit_log`；保留周期与脱敏合规。  
- **无物理外键**：删除/级联依赖应用层事务与一致性校验，防止孤儿 ID。

### 4.3 可扩展性（JSONB 等）

- **`ext_attributes` / `ext_config` / `order_items` / `form_schema` / `datasource_config` / `template_config` 等**：  
  - 定义**版本化 Schema**（可由 JSON Schema 或内部规范文档约束），禁止随意改字段含义。  
  - 大对象控制单文档大小，避免单行过大影响 Vacuum。  
  - 行业扩展优先走扩展 JSON，核心计费与权限仍走强类型列。  
- **打印与表单**：内容与 Schema 分版本发布，支持灰度与回滚。

---

## 附录：有效表清单（未注释 CREATE TABLE）

`sys_tenant`，`sys_tenant_subscription`，`prod_package`，`prod_package_quota`，`sys_tenant_quota_adjustment`，`sys_resource_usage`，`bill_order`，`bill_invoice`，`sys_user`，`sys_user_tenant_rel`，`sys_role`，`sys_permission`，`sys_permission_policy`，`sys_user_role_rel`，`sys_tenant_security`，`sys_user_token`，`sys_dept`，`sys_post`，`sys_user_group`，`sys_user_group_rel`，`sys_role_dept_rel`，`sys_form_config`，`sys_datasource_config`，`sys_print_template`，`sys_menu`，`sys_dict`，`sys_dict_item`，`sys_file`，`sys_notice`，`sys_notice_user_rel`，`sys_oper_log`，`sys_login_log`，`sys_data_audit_log`，`sys_message_template`，`sys_inbox_message`，`sys_message_schedule`。
