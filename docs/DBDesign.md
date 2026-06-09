# NexusIX-Platform 数据库设计文档

## 1 文档信息

| 项目 | 内容 |
|------|------|
| 项目名称 | NexusIX-Platform 多租户 SaaS 平台底座 |
| 文档版本 | v2.0.0 |
| 数据库 | PostgreSQL 17+ |
| 创建日期 | 2026-05-23 |
| 最后更新 | 2026-06-09 |
| 作者 | shy |

---

## 2 数据库概述

### 2.1 技术选型

| 项目 | 说明 |
|------|------|
| 数据库 | PostgreSQL 17+ |
| 主键策略 | 雪花算法（Snowflake）生成 BIGINT 分布式唯一 ID |
| 外键约束 | 无物理外键，所有关联关系通过应用层保证数据一致性 |
| 软删除 | `is_deleted` 字段类型为 `VARCHAR(20)`，枚举值 `NOT_DELETED` / `DELETED` |
| 状态字段 | `status` 字段类型为 `VARCHAR(20)`，使用字符串枚举值而非数字编码 |
| 扩展字段 | 使用 PostgreSQL 原生 JSONB 类型存储灵活结构化数据 |

### 2.2 设计原则

1. **雪花 ID 主键**：所有表主键均为 `BIGINT`，由应用层雪花算法生成，保证分布式环境下的 ID 唯一性和趋势递增
2. **无物理外键**：不创建数据库层面的 FOREIGN KEY 约束，关联关系通过应用层 Service 逻辑保证，降低锁争用和级联风险
3. **字符串枚举**：`is_deleted`、`status` 等状态字段采用 `VARCHAR(20)` 存储可读性强的字符串枚举值，配合 MyBatis-Plus `@EnumValue` 注解实现自动映射
4. **JSONB 扩展**：利用 PostgreSQL 原生 JSONB 类型存储动态属性，支持 GIN 索引高效查询
5. **冗余字段**：关联表中适当冗余名称字段（如 `tenant_code`、`tenant_name`），减少高频查询的 JOIN 操作
6. **审计字段**：所有表均包含 `create_by`、`create_at`、`update_by`、`update_at`、`is_deleted`、`deleted_at` 等审计字段
7. **字段命名统一**：审计字段统一为 `create_by`/`create_at`/`update_by`/`update_at`/`deleted_at`

### 2.3 表清单

| 序号 | 表名 | 说明 | 所属模块 |
|------|------|------|----------|
| 1 | sys_tenant | 租户信息表 | nexusix-tenant |
| 2 | sys_tenant_subscription | 租户套餐订阅表 | nexusix-tenant |
| 3 | sys_user | 用户基础表 | nexusix-iam |
| 4 | sys_user_tenant_rel | 用户-租户关联表 | nexusix-iam |
| 5 | sys_perm | 权限/资源表 | nexusix-iam |
| 6 | sys_perm_policy | 权限策略控制表 | nexusix-iam |
| 7 | sys_user_perm_rel | 用户权限表 | nexusix-iam |
| 8 | sys_role | 角色表 | nexusix-org |
| 9 | sys_role_policy | 角色策略控制表 | nexusix-org |
| 10 | sys_user_role_rel | 用户角色关联表 | nexusix-org |
| 11 | sys_user_token | 用户 Token 记录表 | nexusix-iam |
| 12 | prod_package | 产品套餐定义表 | nexusix-billing |
| 13 | prod_package_quota | 套餐配额模板表 | nexusix-billing |
| 14 | sys_tenant_quota_adjustment | 租户配额调整表 | nexusix-billing |
| 15 | sys_resource_usage | 资源使用计量表 | nexusix-billing |
| 16 | bill_order | 订单表 | nexusix-billing |
| 17 | bill_invoice | 发票管理表 | nexusix-billing |
| 18 | sys_dept | 部门表 | nexusix-org |
| 19 | sys_post | 岗位表 | nexusix-org |
| 20 | sys_user_group | 用户组表 | nexusix-org |
| 21 | sys_user_group_rel | 用户组成员表 | nexusix-org |
| 22 | sys_role_dept_rel | 角色数据权限关联表 | nexusix-org |
| 23 | sys_menu | 菜单表 | nexusix-system |
| 24 | sys_dict | 字典类型表 | nexusix-system |
| 25 | sys_dict_item | 字典数据表 | nexusix-system |
| 26 | sys_file | 文件资源表 | nexusix-system |
| 27 | sys_notice | 通知公告表 | nexusix-system |
| 28 | sys_notice_user_rel | 用户公告阅读状态表 | nexusix-system |
| 29 | sys_form_config | 动态表单配置表 | nexusix-config |
| 30 | sys_datasource_config | 动态数据源配置表 | nexusix-config |
| 31 | sys_print_template | 打印模板配置表 | nexusix-config |
| 32 | sys_oper_log | 操作日志表 | nexusix-audit |
| 33 | sys_login_log | 登录日志表 | nexusix-audit |
| 34 | sys_data_audit_log | 数据变更审计表 | nexusix-audit |
| 35 | sys_message_template | 消息模板表 | nexusix-notification |
| 36 | sys_inbox_message | 站内信收件箱表 | nexusix-notification |
| 37 | sys_message_schedule | 定时消息任务表 | nexusix-notification |
| 38 | sys_tenant_security | 租户安全策略配置表 | nexusix-security |
| 39 | sys_industry_template | 行业模板表 | nexusix-industry |
| 40 | sys_form_template | 表单模板表 | nexusix-industry |
| 41 | sys_tenant_config | 租户配置表 | nexusix-industry |

---

## 3 ER 关系图

```mermaid
erDiagram
    sys_tenant {
        BIGINT id PK
        VARCHAR50 tenant_code UK
        VARCHAR100 tenant_name
        VARCHAR50 tenant_type
        VARCHAR500 tenant_desc
        VARCHAR500 tenant_logo_url
        BIGINT parent_id
        VARCHAR50 parent_code
        VARCHAR100 parent_name
        VARCHAR1000 path
        VARCHAR50 contact_name
        VARCHAR20 contact_phone
        VARCHAR20 status
        VARCHAR500 disable_reason
        TIMESTAMP expire_time
        VARCHAR50 package_id
        VARCHAR100 package_name
        JSONB ext_attributes
        BOOLEAN has_children
        BIGINT create_by
        TIMESTAMP create_at
        BIGINT update_by
        TIMESTAMP update_at
        VARCHAR20 is_deleted
        TIMESTAMP deleted_at
    }

    sys_tenant_subscription {
        BIGINT id PK
        VARCHAR50 subscription_code UK
        BIGINT tenant_id FK
        VARCHAR50 tenant_code
        VARCHAR100 tenant_name
        BIGINT package_id
        VARCHAR20 subscription_type
        TIMESTAMP start_time
        TIMESTAMP end_time
        VARCHAR20 status
        BOOLEAN is_auto_renew
        VARCHAR20 source_type
        BIGINT parent_id
        BIGINT create_by
        TIMESTAMP create_at
        BIGINT update_by
        TIMESTAMP update_at
        VARCHAR20 is_deleted
        TIMESTAMP deleted_at
    }

    sys_user {
        BIGINT id PK
        VARCHAR50 user_code UK
        VARCHAR50 user_name
        VARCHAR100 password
        VARCHAR50 nick_name
        VARCHAR100 email
        VARCHAR20 phone
        VARCHAR500 avatar
        VARCHAR20 status
        VARCHAR45 login_ip
        TIMESTAMP login_date
        BIGINT create_by
        TIMESTAMP create_at
        BIGINT update_by
        TIMESTAMP update_at
        VARCHAR20 is_deleted
        TIMESTAMP deleted_at
    }

    sys_user_tenant_rel {
        BIGINT id PK
        BIGINT user_id FK
        BIGINT tenant_id FK
        BIGINT dept_id
        BOOLEAN is_admin
        TIMESTAMP join_time
        BOOLEAN is_default
        BIGINT create_by
        TIMESTAMP create_at
        BIGINT update_by
        TIMESTAMP update_at
        VARCHAR20 is_deleted
        TIMESTAMP deleted_at
    }

    sys_perm {
        BIGINT id PK
        VARCHAR100 perm_name
        VARCHAR200 perm_desc
        VARCHAR100 perm_code UK
        VARCHAR100 perm_key
        VARCHAR20 perm_type
        BIGINT parent_id
        VARCHAR100 parent_name
        VARCHAR200 path
        VARCHAR20 status
        BIGINT create_by
        TIMESTAMP create_at
        BIGINT update_by
        TIMESTAMP update_at
        VARCHAR20 is_deleted
        TIMESTAMP deleted_at
    }

    sys_perm_policy {
        BIGINT id PK
        VARCHAR100 policy_code UK
        VARCHAR100 policy_name
        BIGINT target_id
        VARCHAR20 target_type
        BIGINT perm_id FK
        BIGINT tenant_id FK
        VARCHAR64 table_name
        VARCHAR100 table_desc
        VARCHAR20 access_type
        JSONB field_operates
        VARCHAR50 status
        BIGINT create_by
        TIMESTAMP create_at
        BIGINT update_by
        TIMESTAMP update_at
        VARCHAR20 is_deleted
        TIMESTAMP deleted_at
    }

    sys_user_perm_rel {
        BIGINT id PK
        BIGINT user_id FK
        BIGINT policy_id FK
        BIGINT create_by
        TIMESTAMP create_at
        BIGINT update_by
        TIMESTAMP update_at
        VARCHAR20 is_deleted
        TIMESTAMP deleted_at
    }

    sys_role {
        BIGINT id PK
        VARCHAR100 role_name
        VARCHAR200 role_desc
        VARCHAR100 role_code UK
        VARCHAR20 role_level
        BIGINT tenant_id FK
        VARCHAR50 tenant_code
        VARCHAR100 tenant_name
        VARCHAR20 data_scope
        INT4 sort_order
        VARCHAR20 status
        BIGINT create_by
        TIMESTAMP create_at
        BIGINT update_by
        TIMESTAMP update_at
        VARCHAR20 is_deleted
        TIMESTAMP deleted_at
    }

    sys_role_policy {
        BIGINT id PK
        VARCHAR100 policy_code UK
        VARCHAR100 policy_name
        BIGINT target_id
        VARCHAR20 target_type
        BIGINT role_id FK
        BIGINT tenant_id FK
        VARCHAR20 status
        BIGINT create_by
        TIMESTAMP create_at
        BIGINT update_by
        TIMESTAMP update_at
        VARCHAR20 is_deleted
        TIMESTAMP deleted_at
    }

    sys_user_role_rel {
        BIGINT id PK
        BIGINT user_id FK
        BIGINT role_id FK
        BIGINT policy_id FK
        BIGINT create_by
        TIMESTAMP create_at
        BIGINT update_by
        TIMESTAMP update_at
        VARCHAR20 is_deleted
        TIMESTAMP deleted_at
    }

    sys_user_token {
        BIGINT id PK
        BIGINT user_id FK
        BIGINT tenant_id FK
        VARCHAR500 token
        VARCHAR200 device_info
        VARCHAR45 login_ip
        TIMESTAMP login_at
        TIMESTAMP expire_time
        VARCHAR20 status
        BIGINT create_by
        TIMESTAMP create_at
        BIGINT update_by
        TIMESTAMP update_at
        VARCHAR20 is_deleted
        TIMESTAMP deleted_at
    }

    prod_package {
        BIGINT id PK
        VARCHAR100 package_name
        VARCHAR50 package_code UK
        VARCHAR500 description
        NUMERIC10_2 price
        VARCHAR20 cycle_type
        INT4 cycle_value
        VARCHAR20 status
        INT4 sort_order
        JSONB ext_attributes
        BIGINT create_by
        TIMESTAMP create_at
        BIGINT update_by
        TIMESTAMP update_at
        VARCHAR20 is_deleted
        TIMESTAMP deleted_at
    }

    prod_package_quota {
        BIGINT id PK
        BIGINT package_id FK
        VARCHAR50 resource_code
        VARCHAR100 resource_name
        BIGINT quota_value
        VARCHAR20 unit
        BOOLEAN is_allow_overage
        BIGINT create_by
        TIMESTAMP create_at
        BIGINT update_by
        TIMESTAMP update_at
        VARCHAR20 is_deleted
        TIMESTAMP deleted_at
    }

    sys_tenant_quota_adjustment {
        BIGINT id PK
        BIGINT tenant_id FK
        VARCHAR50 tenant_code
        VARCHAR100 tenant_name
        VARCHAR50 resource_code
        BIGINT adjust_value
        VARCHAR20 adjust_type
        VARCHAR500 reason
        TIMESTAMP effective_time
        TIMESTAMP expire_time
        BIGINT operator_id
        BIGINT create_by
        TIMESTAMP create_at
        BIGINT update_by
        TIMESTAMP update_at
        VARCHAR20 is_deleted
        TIMESTAMP deleted_at
    }

    sys_resource_usage {
        BIGINT id PK
        BIGINT tenant_id FK
        VARCHAR50 tenant_code
        VARCHAR50 resource_code
        BIGINT usage_value
        VARCHAR50 business_type
        BIGINT business_id
        TIMESTAMP usage_time
        VARCHAR500 remark
        BIGINT create_by
        TIMESTAMP create_at
        BIGINT update_by
        TIMESTAMP update_at
        VARCHAR20 is_deleted
        TIMESTAMP deleted_at
    }

    bill_order {
        BIGINT id PK
        BIGINT tenant_id FK
        VARCHAR50 tenant_code
        VARCHAR100 tenant_name
        VARCHAR64 order_no UK
        VARCHAR20 product_type
        BIGINT product_id
        NUMERIC10_2 total_amount
        NUMERIC10_2 pay_amount
        VARCHAR20 status
        TIMESTAMP pay_time
        VARCHAR50 pay_channel
        JSONB order_items
        JSONB ext_attributes
        BIGINT create_by
        TIMESTAMP create_at
        BIGINT update_by
        TIMESTAMP update_at
        VARCHAR20 is_deleted
        TIMESTAMP deleted_at
    }

    bill_invoice {
        BIGINT id PK
        BIGINT tenant_id FK
        VARCHAR50 tenant_code
        VARCHAR100 tenant_name
        BIGINT order_id FK
        VARCHAR64 invoice_no UK
        VARCHAR20 invoice_type
        VARCHAR200 invoice_title
        VARCHAR50 tax_id
        NUMERIC10_2 amount
        VARCHAR20 status
        VARCHAR500 invoice_url
        BIGINT create_by
        TIMESTAMP create_at
        BIGINT update_by
        TIMESTAMP update_at
        VARCHAR20 is_deleted
        TIMESTAMP deleted_at
    }

    sys_dept {
        BIGINT id PK
        VARCHAR50 dept_name
        BIGINT parent_id
        VARCHAR1000 path
        BIGINT tenant_id FK
        VARCHAR50 tenant_code
        BIGINT leader_id
        VARCHAR20 phone
        INT4 sort_order
        VARCHAR20 status
        VARCHAR500 dept_desc
        BIGINT create_by
        TIMESTAMP create_at
        BIGINT update_by
        TIMESTAMP update_at
        VARCHAR20 is_deleted
        TIMESTAMP deleted_at
    }

    sys_post {
        BIGINT id PK
        VARCHAR50 post_name
        VARCHAR50 post_code UK
        BIGINT dept_id FK
        BIGINT tenant_id FK
        VARCHAR50 tenant_code
        INT4 sort_order
        VARCHAR20 status
        VARCHAR500 post_desc
        BIGINT create_by
        TIMESTAMP create_at
        BIGINT update_by
        TIMESTAMP update_at
        VARCHAR20 is_deleted
        TIMESTAMP deleted_at
    }

    sys_user_group {
        BIGINT id PK
        VARCHAR50 group_name
        VARCHAR20 group_type
        BIGINT tenant_id FK
        VARCHAR50 tenant_code
        VARCHAR200 description
        BIGINT create_by
        TIMESTAMP create_at
        BIGINT update_by
        TIMESTAMP update_at
        VARCHAR20 is_deleted
        TIMESTAMP deleted_at
    }

    sys_user_group_rel {
        BIGINT id PK
        BIGINT group_id FK
        BIGINT user_id FK
        BIGINT create_by
        TIMESTAMP create_at
        BIGINT update_by
        TIMESTAMP update_at
        VARCHAR20 is_deleted
        TIMESTAMP deleted_at
    }

    sys_role_dept_rel {
        BIGINT id PK
        BIGINT role_id FK
        BIGINT dept_id FK
        BIGINT tenant_id FK
        VARCHAR50 tenant_code
        BIGINT create_by
        TIMESTAMP create_at
        BIGINT update_by
        TIMESTAMP update_at
        VARCHAR20 is_deleted
        TIMESTAMP deleted_at
    }

    sys_menu {
        BIGINT id PK
        VARCHAR50 menu_name
        VARCHAR20 menu_type
        BIGINT parent_id
        VARCHAR100 parent_name
        VARCHAR200 menu_desc
        VARCHAR100 icon
        VARCHAR200 path
        VARCHAR200 component
        VARCHAR100 perm_code
        BOOLEAN visible
        VARCHAR20 status
        INT4 sort_order
        BIGINT create_by
        TIMESTAMP create_at
        BIGINT update_by
        TIMESTAMP update_at
        VARCHAR20 is_deleted
        TIMESTAMP deleted_at
    }

    sys_dict {
        BIGINT id PK
        VARCHAR100 dict_name
        VARCHAR100 dict_type UK
        BIGINT tenant_id FK
        VARCHAR50 tenant_code
        VARCHAR20 status
        VARCHAR500 remark
        BIGINT create_by
        TIMESTAMP create_at
        BIGINT update_by
        TIMESTAMP update_at
        VARCHAR20 is_deleted
        TIMESTAMP deleted_at
    }

    sys_dict_item {
        BIGINT id PK
        BIGINT dict_id FK
        VARCHAR100 dict_label
        VARCHAR100 dict_value
        INT4 sort_order
        VARCHAR20 status
        BIGINT create_by
        TIMESTAMP create_at
        BIGINT update_by
        TIMESTAMP update_at
        VARCHAR20 is_deleted
        TIMESTAMP deleted_at
    }

    sys_file {
        BIGINT id PK
        BIGINT tenant_id FK
        VARCHAR50 tenant_code
        VARCHAR255 file_name
        VARCHAR500 file_path
        VARCHAR500 file_url
        BIGINT file_size
        VARCHAR50 file_type
        BIGINT upload_by
        TIMESTAMP upload_time
        BIGINT create_by
        TIMESTAMP create_at
        BIGINT update_by
        TIMESTAMP update_at
        VARCHAR20 is_deleted
        TIMESTAMP deleted_at
    }

    sys_notice {
        BIGINT id PK
        VARCHAR100 notice_title
        VARCHAR20 notice_type
        TEXT notice_content
        VARCHAR20 status
        VARCHAR20 target_type
        VARCHAR1000 target_ids
        BIGINT tenant_id FK
        VARCHAR50 tenant_code
        BIGINT create_by
        TIMESTAMP create_at
        BIGINT update_by
        TIMESTAMP update_at
        VARCHAR20 is_deleted
        TIMESTAMP deleted_at
    }

    sys_notice_user_rel {
        BIGINT id PK
        BIGINT notice_id FK
        BIGINT user_id FK
        BIGINT tenant_id FK
        VARCHAR20 read_status
        TIMESTAMP read_time
        BIGINT create_by
        TIMESTAMP create_at
        BIGINT update_by
        TIMESTAMP update_at
        VARCHAR20 is_deleted
        TIMESTAMP deleted_at
    }

    sys_form_config {
        BIGINT id PK
        BIGINT tenant_id FK
        VARCHAR50 tenant_code
        VARCHAR50 biz_type
        VARCHAR100 form_name
        JSONB form_schema
        VARCHAR20 status
        INT4 version
        BIGINT create_by
        TIMESTAMP create_at
        BIGINT update_by
        TIMESTAMP update_at
        VARCHAR20 is_deleted
        TIMESTAMP deleted_at
    }

    sys_datasource_config {
        BIGINT id PK
        BIGINT tenant_id FK
        VARCHAR50 tenant_code
        VARCHAR50 datasource_code
        VARCHAR100 datasource_name
        VARCHAR20 datasource_type
        JSONB datasource_config
        BOOLEAN cache_enabled
        INT4 cache_expire
        VARCHAR20 status
        BIGINT create_by
        TIMESTAMP create_at
        BIGINT update_by
        TIMESTAMP update_at
        VARCHAR20 is_deleted
        TIMESTAMP deleted_at
    }

    sys_print_template {
        BIGINT id PK
        BIGINT tenant_id FK
        VARCHAR50 tenant_code
        VARCHAR50 template_code
        VARCHAR100 template_name
        VARCHAR50 biz_type
        VARCHAR20 template_type
        TEXT template_content
        JSONB template_config
        VARCHAR20 paper_size
        VARCHAR10 orientation
        VARCHAR20 status
        BOOLEAN is_default
        INT4 version
        BIGINT create_by
        TIMESTAMP create_at
        BIGINT update_by
        TIMESTAMP update_at
        VARCHAR20 is_deleted
        TIMESTAMP deleted_at
    }

    sys_oper_log {
        BIGINT id PK
        BIGINT tenant_id FK
        VARCHAR50 tenant_code
        VARCHAR50 module
        VARCHAR20 business_type
        VARCHAR100 method
        VARCHAR10 request_method
        VARCHAR50 operator_name
        BIGINT operator_id
        VARCHAR50 dept_name
        VARCHAR255 oper_url
        VARCHAR128 oper_ip
        VARCHAR255 oper_location
        VARCHAR2000 oper_param
        VARCHAR2000 json_result
        VARCHAR20 status
        VARCHAR2000 error_msg
        TIMESTAMP oper_time
        VARCHAR20 is_deleted
        TIMESTAMP deleted_at
    }

    sys_login_log {
        BIGINT id PK
        BIGINT user_id
        VARCHAR50 username
        BIGINT tenant_id FK
        VARCHAR50 tenant_code
        VARCHAR128 ip_address
        VARCHAR255 login_location
        VARCHAR50 browser
        VARCHAR50 os
        VARCHAR20 status
        VARCHAR255 msg
        TIMESTAMP login_time
        VARCHAR20 is_deleted
        TIMESTAMP deleted_at
    }

    sys_data_audit_log {
        BIGINT id PK
        BIGINT tenant_id FK
        VARCHAR50 tenant_code
        VARCHAR50 table_name
        BIGINT record_id
        BIGINT operator_id
        VARCHAR20 operate_type
        JSONB old_value
        JSONB new_value
        TIMESTAMP operate_time
        VARCHAR20 is_deleted
        TIMESTAMP deleted_at
    }

    sys_message_template {
        BIGINT id PK
        VARCHAR100 template_code
        VARCHAR100 template_name
        BIGINT tenant_id FK
        VARCHAR50 tenant_code
        VARCHAR50 biz_type
        VARCHAR20 message_type
        VARCHAR200 template_title
        TEXT template_content
        JSONB template_example
        JSONB variables
        VARCHAR20 status
        INT4 version
        VARCHAR20 language
        BIGINT create_by
        TIMESTAMP create_at
        BIGINT update_by
        TIMESTAMP update_at
        VARCHAR20 is_deleted
        TIMESTAMP deleted_at
    }

    sys_inbox_message {
        BIGINT id PK
        BIGINT user_id FK
        BIGINT tenant_id FK
        VARCHAR50 tenant_code
        VARCHAR20 message_type
        VARCHAR200 title
        TEXT content
        VARCHAR20 priority
        BOOLEAN is_read
        TIMESTAMP read_time
        BOOLEAN is_archived
        TIMESTAMP archived_time
        TIMESTAMP expire_time
        VARCHAR500 action_url
        VARCHAR50 action_text
        BIGINT create_by
        TIMESTAMP create_at
        BIGINT update_by
        TIMESTAMP update_at
        VARCHAR20 is_deleted
        TIMESTAMP deleted_at
    }

    sys_message_schedule {
        BIGINT id PK
        VARCHAR100 task_name
        BIGINT tenant_id FK
        VARCHAR50 tenant_code
        BIGINT template_id FK
        VARCHAR20 target_type
        VARCHAR1000 target_ids
        VARCHAR20 trigger_type
        JSONB trigger_condition
        TIMESTAMP execute_time
        JSONB repeat_rule
        VARCHAR20 status
        INT4 executed_count
        TIMESTAMP last_execute_time
        BIGINT create_by
        TIMESTAMP create_at
        BIGINT update_by
        TIMESTAMP update_at
        VARCHAR20 is_deleted
        TIMESTAMP deleted_at
    }

    sys_tenant_security {
        BIGINT id PK
        BIGINT tenant_id FK
        VARCHAR50 tenant_code
        INT4 pwd_min_length
        VARCHAR20 pwd_complexity
        INT4 pwd_expire_days
        INT4 login_fail_limit
        INT4 lock_duration
        BIGINT create_by
        TIMESTAMP create_at
        BIGINT update_by
        TIMESTAMP update_at
        VARCHAR20 is_deleted
        TIMESTAMP deleted_at
    }

    sys_industry_template {
        BIGINT id PK
        VARCHAR50 template_code
        VARCHAR100 template_name
        VARCHAR20 industry_type
        VARCHAR500 template_desc
        JSONB template_config
        VARCHAR20 status
        BIGINT create_by
        TIMESTAMP create_at
        BIGINT update_by
        TIMESTAMP update_at
        VARCHAR20 is_deleted
        TIMESTAMP deleted_at
    }

    sys_form_template {
        BIGINT id PK
        BIGINT template_id FK
        VARCHAR50 form_code
        VARCHAR100 form_name
        VARCHAR50 biz_type
        JSONB form_schema
        INT4 form_version
        VARCHAR20 status
        BIGINT create_by
        TIMESTAMP create_at
        BIGINT update_by
        TIMESTAMP update_at
        VARCHAR20 is_deleted
        TIMESTAMP deleted_at
    }

    sys_tenant_config {
        BIGINT id PK
        BIGINT tenant_id FK
        VARCHAR50 tenant_code
        VARCHAR100 config_key
        TEXT config_value
        VARCHAR20 config_type
        VARCHAR50 config_group
        VARCHAR200 config_desc
        BOOLEAN inheritable
        VARCHAR20 status
        BIGINT create_by
        TIMESTAMP create_at
        BIGINT update_by
        TIMESTAMP update_at
        VARCHAR20 is_deleted
        TIMESTAMP deleted_at
    }

    sys_tenant ||--o{ sys_tenant : "parent_id 自引用层级"
    sys_tenant ||--o{ sys_tenant_subscription : "tenant_id"
    sys_user ||--o{ sys_user_tenant_rel : "user_id"
    sys_tenant ||--o{ sys_user_tenant_rel : "tenant_id"
    sys_perm ||--o{ sys_perm : "parent_id 自引用树"
    sys_perm ||--o{ sys_perm_policy : "perm_id"
    sys_tenant ||--o{ sys_perm_policy : "tenant_id"
    sys_user ||--o{ sys_user_perm_rel : "user_id"
    sys_perm_policy ||--o{ sys_user_perm_rel : "policy_id"
    sys_tenant ||--o{ sys_role : "tenant_id"
    sys_role ||--o{ sys_role_policy : "role_id"
    sys_tenant ||--o{ sys_role_policy : "tenant_id"
    sys_user ||--o{ sys_user_role_rel : "user_id"
    sys_role ||--o{ sys_user_role_rel : "role_id"
    sys_perm_policy ||--o{ sys_user_role_rel : "policy_id"
    sys_user ||--o{ sys_user_token : "user_id"
    sys_tenant ||--o{ sys_user_token : "tenant_id"
    prod_package ||--o{ prod_package_quota : "package_id"
    prod_package ||--o{ sys_tenant_subscription : "package_id"
    sys_tenant ||--o{ sys_tenant_quota_adjustment : "tenant_id"
    sys_tenant ||--o{ sys_resource_usage : "tenant_id"
    sys_tenant ||--o{ bill_order : "tenant_id"
    bill_order ||--o{ bill_invoice : "order_id"
    sys_tenant ||--o{ sys_dept : "tenant_id"
    sys_dept ||--o{ sys_dept : "parent_id 自引用树"
    sys_dept ||--o{ sys_post : "dept_id"
    sys_tenant ||--o{ sys_post : "tenant_id"
    sys_tenant ||--o{ sys_user_group : "tenant_id"
    sys_user_group ||--o{ sys_user_group_rel : "group_id"
    sys_user ||--o{ sys_user_group_rel : "user_id"
    sys_role ||--o{ sys_role_dept_rel : "role_id"
    sys_dept ||--o{ sys_role_dept_rel : "dept_id"
    sys_tenant ||--o{ sys_role_dept_rel : "tenant_id"
    sys_menu ||--o{ sys_menu : "parent_id 自引用树"
    sys_tenant ||--o{ sys_dict : "tenant_id"
    sys_dict ||--o{ sys_dict_item : "dict_id"
    sys_tenant ||--o{ sys_file : "tenant_id"
    sys_tenant ||--o{ sys_notice : "tenant_id"
    sys_notice ||--o{ sys_notice_user_rel : "notice_id"
    sys_user ||--o{ sys_notice_user_rel : "user_id"
    sys_tenant ||--o{ sys_form_config : "tenant_id"
    sys_tenant ||--o{ sys_datasource_config : "tenant_id"
    sys_tenant ||--o{ sys_print_template : "tenant_id"
    sys_tenant ||--o{ sys_oper_log : "tenant_id"
    sys_tenant ||--o{ sys_login_log : "tenant_id"
    sys_tenant ||--o{ sys_data_audit_log : "tenant_id"
    sys_tenant ||--o{ sys_message_template : "tenant_id"
    sys_tenant ||--o{ sys_inbox_message : "tenant_id"
    sys_user ||--o{ sys_inbox_message : "user_id"
    sys_tenant ||--o{ sys_message_schedule : "tenant_id"
    sys_message_template ||--o{ sys_message_schedule : "template_id"
    sys_tenant ||--o{ sys_tenant_security : "tenant_id"
    sys_industry_template ||--o{ sys_form_template : "template_id"
    sys_tenant ||--o{ sys_tenant_config : "tenant_id"
```

---

## 4 表详细设计

### 4.1 sys_tenant — 租户信息表

存储租户基础信息，支持无限层级树形结构。

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | BIGINT | NOT NULL, PK | — | 雪花算法主键 |
| tenant_code | VARCHAR(50) | NOT NULL | — | 租户唯一编码 |
| tenant_name | VARCHAR(100) | NOT NULL | — | 租户名称 |
| tenant_type | VARCHAR(50) | NOT NULL | — | 租户类型（行业分类） |
| tenant_desc | VARCHAR(500) | NOT NULL | — | 租户描述 |
| tenant_logo_url | VARCHAR(500) | NOT NULL | — | 租户 Logo 路径 |
| parent_id | BIGINT | NOT NULL | — | 父租户 ID，顶级为 0 |
| parent_code | VARCHAR(50) | NOT NULL | — | 父租户编码（冗余） |
| parent_name | VARCHAR(100) | NOT NULL | — | 父租户名称（冗余） |
| path | VARCHAR(1000) | NOT NULL | — | 祖级路径，如 `/1/2/3/`（斜杠分隔 ID 格式） |
| contact_name | VARCHAR(50) | NOT NULL | — | 联系人姓名 |
| contact_phone | VARCHAR(20) | NOT NULL | — | 联系人电话 |
| status | VARCHAR(20) | NOT NULL | — | 状态：ENABLED / DISABLED / EXPIRED |
| disable_reason | VARCHAR(500) | — | NULL | 禁用原因 |
| expire_time | TIMESTAMP | NOT NULL | — | 服务过期时间 |
| package_id | VARCHAR(50) | — | — | 当前主套餐 ID |
| package_name | VARCHAR(100) | NOT NULL | — | 当前主套餐名称（冗余） |
| ext_attributes | JSONB | — | `'{}'::jsonb` | 扩展属性（行业特定配置） |
| has_children | BOOLEAN | NOT NULL | — | 是否有子租户 |
| create_by | BIGINT | NOT NULL | — | 创建人 ID |
| create_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 创建时间 |
| update_by | BIGINT | NOT NULL | — | 更新人 ID |
| update_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 更新时间 |
| is_deleted | VARCHAR(20) | NOT NULL | — | 逻辑删除：NOT_DELETED / DELETED |
| deleted_at | TIMESTAMP | — | NULL | 删除时间 |

### 4.2 sys_tenant_subscription — 租户套餐订阅表

记录租户购买的套餐及订阅状态，支持续费、升级、降级等操作。

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | BIGINT | NOT NULL, PK | — | 雪花算法主键 |
| subscription_code | VARCHAR(50) | NOT NULL | — | 订阅编码 |
| tenant_id | BIGINT | NOT NULL | — | 租户 ID |
| tenant_code | VARCHAR(50) | NOT NULL | — | 租户编码（冗余） |
| tenant_name | VARCHAR(100) | NOT NULL | — | 租户名称（冗余） |
| package_id | BIGINT | NOT NULL | — | 套餐产品 ID |
| subscription_type | VARCHAR(20) | NOT NULL | — | 订阅类型：NEW / RENEWAL / UPGRADE / DOWNGRADE |
| start_time | TIMESTAMP | NOT NULL | — | 订阅开始时间 |
| end_time | TIMESTAMP | NOT NULL | — | 订阅结束时间 |
| status | VARCHAR(20) | NOT NULL | — | 状态：ACTIVE / EXPIRED / CANCELLED / PENDING |
| is_auto_renew | BOOLEAN | NOT NULL | — | 是否自动续费 |
| source_type | VARCHAR(20) | NOT NULL | — | 来源类型：DIRECT / ADMIN_ASSIGN / PARENT_GRANT |
| parent_id | BIGINT | NOT NULL | — | 父订阅 ID（续费/升级时关联原订阅） |
| create_by | BIGINT | NOT NULL | — | 创建人 ID |
| create_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 创建时间 |
| update_by | BIGINT | NOT NULL | — | 更新人 ID |
| update_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 更新时间 |
| is_deleted | VARCHAR(20) | NOT NULL | — | 逻辑删除：NOT_DELETED / DELETED |
| deleted_at | TIMESTAMP | — | NULL | 删除时间 |

### 4.3 sys_user — 用户基础表

存储系统用户的基础认证信息，用户跨租户共享。

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | BIGINT | NOT NULL, PK | — | 雪花算法主键 |
| user_code | VARCHAR(50) | NOT NULL | — | 用户编码 |
| user_name | VARCHAR(50) | NOT NULL | — | 登录用户名 |
| password | VARCHAR(100) | NOT NULL | — | 加密密码（BCrypt） |
| nick_name | VARCHAR(50) | NOT NULL | — | 用户昵称 |
| email | VARCHAR(100) | NOT NULL | — | 邮箱 |
| phone | VARCHAR(20) | NOT NULL | — | 手机号码 |
| avatar | VARCHAR(500) | NOT NULL | — | 头像地址 |
| status | VARCHAR(20) | NOT NULL | — | 状态：ENABLED / DISABLED |
| login_ip | VARCHAR(45) | NOT NULL | — | 最后登录 IP |
| login_date | TIMESTAMP | NOT NULL | — | 最后登录时间 |
| create_by | BIGINT | NOT NULL | — | 创建人 ID |
| create_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 创建时间 |
| update_by | BIGINT | NOT NULL | — | 更新人 ID |
| update_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 更新时间 |
| is_deleted | VARCHAR(20) | NOT NULL | — | 逻辑删除：NOT_DELETED / DELETED |
| deleted_at | TIMESTAMP | — | NULL | 删除时间 |

### 4.4 sys_user_tenant_rel — 用户-租户关联表

用户与租户的多对多关联，记录用户在各租户下的身份信息。

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | BIGINT | NOT NULL, PK | — | 雪花算法主键 |
| user_id | BIGINT | NOT NULL | — | 用户 ID |
| tenant_id | BIGINT | NOT NULL | — | 租户 ID |
| dept_id | BIGINT | NOT NULL | — | 部门 ID |
| is_admin | BOOLEAN | NOT NULL | — | 是否为该租户管理员 |
| join_time | TIMESTAMP | — | CURRENT_TIMESTAMP | 加入租户时间 |
| is_default | BOOLEAN | NOT NULL | — | 是否默认租户 |
| create_by | BIGINT | NOT NULL | — | 创建人 ID |
| create_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 创建时间 |
| update_by | BIGINT | NOT NULL | — | 更新人 ID |
| update_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 更新时间 |
| is_deleted | VARCHAR(20) | NOT NULL | — | 逻辑删除：NOT_DELETED / DELETED |
| deleted_at | TIMESTAMP | — | NULL | 删除时间 |

### 4.5 sys_perm — 权限/资源表

权限资源树形结构，支持菜单、按钮、API 等多种权限类型。

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | BIGINT | NOT NULL, PK | — | 雪花算法主键 |
| perm_name | VARCHAR(100) | NOT NULL | — | 权限名称 |
| perm_desc | VARCHAR(200) | NOT NULL | — | 权限描述 |
| perm_code | VARCHAR(100) | NOT NULL | — | 权限编码（如 `SYS_USER`） |
| perm_key | VARCHAR(100) | NOT NULL | — | 权限标识（如 `user:list`） |
| perm_type | VARCHAR(20) | NOT NULL | — | 权限类型：MENU / BUTTON / API |
| parent_id | BIGINT | NOT NULL | — | 父权限 ID，顶级为 0 |
| parent_name | VARCHAR(100) | NOT NULL | — | 父权限名称（冗余） |
| path | VARCHAR(200) | NOT NULL | — | 权限路径 |
| status | VARCHAR(20) | NOT NULL | — | 状态：ENABLED / DISABLED |
| create_by | BIGINT | NOT NULL | — | 创建人 ID |
| create_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 创建时间 |
| update_by | BIGINT | NOT NULL | — | 更新人 ID |
| update_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 更新时间 |
| is_deleted | VARCHAR(20) | NOT NULL | — | 逻辑删除：NOT_DELETED / DELETED |
| deleted_at | TIMESTAMP | — | NULL | 删除时间 |

### 4.6 sys_perm_policy — 权限策略控制表

层级划分与多租户多实例权限配置，实现字段级权限控制。

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | BIGINT | NOT NULL, PK | — | 雪花算法主键 |
| policy_code | VARCHAR(100) | NOT NULL | — | 策略编码 |
| policy_name | VARCHAR(100) | NOT NULL | — | 策略名称 |
| target_id | BIGINT | NOT NULL | — | 授权目标 ID |
| target_type | VARCHAR(20) | NOT NULL | — | 授权目标类型：TENANT / ROLE / USER |
| perm_id | BIGINT | NOT NULL | — | 关联权限 ID |
| tenant_id | BIGINT | NOT NULL | — | 租户 ID |
| table_name | VARCHAR(64) | NOT NULL | — | 控制的数据表名 |
| table_desc | VARCHAR(100) | NOT NULL | — | 数据表描述 |
| access_type | VARCHAR(20) | NOT NULL | — | 访问类型：QUERY / CREATE / UPDATE |
| field_operates | JSONB | — | `'[]'::jsonb` | 允许操作的字段名列表，如 `["id", "user_name", "nick_name"]` |
| status | VARCHAR(50) | NOT NULL | — | 策略状态：ACTIVE / DISABLED_SYSTEM_LEVEL / DISABLED_TENANT_LEVEL / DISABLED_ROLE_LEVEL / DISABLED_USER_LEVEL |
| create_by | BIGINT | NOT NULL | — | 创建人 ID |
| create_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 创建时间 |
| update_by | BIGINT | NOT NULL | — | 更新人 ID |
| update_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 更新时间 |
| is_deleted | VARCHAR(20) | NOT NULL | — | 逻辑删除：NOT_DELETED / DELETED |
| deleted_at | TIMESTAMP | — | NULL | 删除时间 |

### 4.7 sys_user_perm_rel — 用户权限表

用户与权限策略的直接关联，支持绕过角色直接授权。

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | BIGINT | NOT NULL, PK | — | 雪花算法主键 |
| user_id | BIGINT | NOT NULL | — | 用户 ID |
| policy_id | BIGINT | NOT NULL | — | 权限策略 ID |
| create_by | BIGINT | NOT NULL | — | 创建人 ID |
| create_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 创建时间 |
| update_by | BIGINT | NOT NULL | — | 更新人 ID |
| update_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 更新时间 |
| is_deleted | VARCHAR(20) | NOT NULL | — | 逻辑删除：NOT_DELETED / DELETED |
| deleted_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 删除时间 |

### 4.8 sys_role — 角色表

定义系统角色，角色在租户范围内生效。

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | BIGINT | NOT NULL, PK | — | 雪花算法主键 |
| role_name | VARCHAR(100) | NOT NULL | — | 角色名称 |
| role_desc | VARCHAR(200) | NOT NULL | — | 角色描述 |
| role_code | VARCHAR(100) | NOT NULL | — | 角色编码 |
| role_level | VARCHAR(20) | NOT NULL | — | 角色层级：SYSTEM / TENANT / USER |
| tenant_id | BIGINT | NOT NULL | — | 所属租户 ID（系统级为 0） |
| tenant_code | VARCHAR(50) | NOT NULL | — | 租户编码（冗余） |
| tenant_name | VARCHAR(100) | NOT NULL | — | 租户名称（冗余） |
| data_scope | VARCHAR(20) | NOT NULL | — | 数据权限范围：ALL / DEPT / DEPT_AND_SUB / SELF / CUSTOM |
| sort_order | INT4 | NOT NULL | — | 排序 |
| status | VARCHAR(20) | NOT NULL | — | 状态：ENABLED / DISABLED |
| create_by | BIGINT | NOT NULL | — | 创建人 ID |
| create_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 创建时间 |
| update_by | BIGINT | NOT NULL | — | 更新人 ID |
| update_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 更新时间 |
| is_deleted | VARCHAR(20) | NOT NULL | — | 逻辑删除：NOT_DELETED / DELETED |
| deleted_at | TIMESTAMP | — | NULL | 删除时间 |

### 4.9 sys_role_policy — 角色策略控制表

角色与权限策略的关联，定义角色在各租户下的策略配置。

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | BIGINT | NOT NULL, PK | — | 雪花算法主键 |
| policy_code | VARCHAR(100) | NOT NULL | — | 策略编码 |
| policy_name | VARCHAR(100) | NOT NULL | — | 策略名称 |
| target_id | BIGINT | NOT NULL | — | 授权目标 ID |
| target_type | VARCHAR(20) | NOT NULL | — | 授权目标类型：TENANT / ROLE / USER |
| role_id | BIGINT | NOT NULL | — | 角色 ID |
| tenant_id | BIGINT | NOT NULL | — | 租户 ID |
| status | VARCHAR(20) | NOT NULL | — | 策略状态：ACTIVE / DISABLED_SYSTEM_LEVEL / DISABLED_TENANT_LEVEL / DISABLED_ROLE_LEVEL / DISABLED_USER_LEVEL |
| create_by | BIGINT | NOT NULL | — | 创建人 ID |
| create_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 创建时间 |
| update_by | BIGINT | NOT NULL | — | 更新人 ID |
| update_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 更新时间 |
| is_deleted | VARCHAR(20) | NOT NULL | — | 逻辑删除：NOT_DELETED / DELETED |
| deleted_at | TIMESTAMP | — | NULL | 删除时间 |

### 4.10 sys_user_role_rel — 用户角色关联表

用户与角色的多对多关联，同时绑定策略上下文。

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | BIGINT | NOT NULL, PK | — | 雪花算法主键 |
| user_id | BIGINT | NOT NULL | — | 用户 ID |
| role_id | BIGINT | NOT NULL | — | 角色 ID |
| policy_id | BIGINT | NOT NULL | — | 权限策略 ID |
| create_by | BIGINT | NOT NULL | — | 创建人 ID |
| create_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 创建时间 |
| update_by | BIGINT | NOT NULL | — | 更新人 ID |
| update_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 更新时间 |
| is_deleted | VARCHAR(20) | NOT NULL | — | 逻辑删除：NOT_DELETED / DELETED |
| deleted_at | TIMESTAMP | — | NULL | 删除时间 |

### 4.11 sys_user_token — 用户 Token 记录表

记录用户登录 Token 及会话信息，支持多设备登录管理。

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | BIGINT | NOT NULL, PK | — | 雪花算法主键 |
| user_id | BIGINT | NOT NULL | — | 用户 ID |
| tenant_id | BIGINT | NOT NULL | — | 租户 ID |
| token | VARCHAR(500) | NOT NULL | — | Token 值 |
| device_info | VARCHAR(200) | NOT NULL | — | 设备信息 |
| login_ip | VARCHAR(45) | NOT NULL | — | 登录 IP |
| login_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 登录时间 |
| expire_time | TIMESTAMP | NOT NULL | — | 过期时间 |
| status | VARCHAR(20) | NOT NULL | — | 状态：ACTIVE / EXPIRED / REVOKED |
| create_by | BIGINT | NOT NULL | — | 创建人 ID |
| create_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 创建时间 |
| update_by | BIGINT | NOT NULL | — | 更新人 ID |
| update_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 更新时间 |
| is_deleted | VARCHAR(20) | NOT NULL | — | 逻辑删除：NOT_DELETED / DELETED |
| deleted_at | TIMESTAMP | — | NULL | 删除时间 |

### 4.12 prod_package — 产品套餐定义表

定义平台提供的产品套餐，包含价格、周期等信息。

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | BIGINT | NOT NULL, PK | — | 雪花算法主键 |
| package_name | VARCHAR(100) | NOT NULL | — | 套餐名称 |
| package_code | VARCHAR(50) | NOT NULL | — | 套餐编码 |
| description | VARCHAR(500) | NOT NULL | — | 套餐描述 |
| price | NUMERIC(10,2) | — | 0.00 | 价格 |
| cycle_type | VARCHAR(20) | NOT NULL | — | 周期类型：MONTH / QUARTER / YEAR |
| cycle_value | INT4 | — | 1 | 周期数值 |
| status | VARCHAR(20) | NOT NULL | — | 套餐状态：ENABLED / DISABLED |
| sort_order | INT4 | — | 0 | 排序 |
| ext_attributes | JSONB | — | `'{}'::jsonb` | 扩展属性 |
| create_by | BIGINT | NOT NULL | — | 创建人 ID |
| create_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 创建时间 |
| update_by | BIGINT | NOT NULL | — | 更新人 ID |
| update_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 更新时间 |
| is_deleted | VARCHAR(20) | NOT NULL | — | 逻辑删除：NOT_DELETED / DELETED |
| deleted_at | TIMESTAMP | — | NULL | 删除时间 |

### 4.13 prod_package_quota — 套餐配额模板表

定义套餐中各资源的配额限制。

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | BIGINT | NOT NULL, PK | — | 雪花算法主键 |
| package_id | BIGINT | NOT NULL | — | 关联套餐 ID |
| resource_code | VARCHAR(50) | NOT NULL | — | 资源类型代码 |
| resource_name | VARCHAR(100) | NOT NULL | — | 资源名称 |
| quota_value | BIGINT | NOT NULL | — | 配额数值 |
| unit | VARCHAR(20) | NOT NULL | — | 单位 |
| is_allow_overage | BOOLEAN | NOT NULL | — | 是否允许超额使用 |
| create_by | BIGINT | NOT NULL | — | 创建人 ID |
| create_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 创建时间 |
| update_by | BIGINT | NOT NULL | — | 更新人 ID |
| update_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 更新时间 |
| is_deleted | VARCHAR(20) | NOT NULL | — | 逻辑删除：NOT_DELETED / DELETED |
| deleted_at | TIMESTAMP | — | NULL | 删除时间 |

### 4.14 sys_tenant_quota_adjustment — 租户配额调整表

记录租户配额的手工调整记录，支持临时增减配额。

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | BIGINT | NOT NULL, PK | — | 雪花算法主键 |
| tenant_id | BIGINT | NOT NULL | — | 租户 ID |
| tenant_code | VARCHAR(50) | NOT NULL | — | 租户编码（冗余） |
| tenant_name | VARCHAR(100) | NOT NULL | — | 租户名称（冗余） |
| resource_code | VARCHAR(50) | NOT NULL | — | 资源类型代码 |
| adjust_value | BIGINT | NOT NULL | — | 调整数值（正数增加，负数减少） |
| adjust_type | VARCHAR(20) | NOT NULL | — | 调整类型：INCREASE / DECREASE |
| reason | VARCHAR(500) | NOT NULL | — | 调整原因 |
| effective_time | TIMESTAMP | — | CURRENT_TIMESTAMP | 生效时间 |
| expire_time | TIMESTAMP | — | NULL | 过期时间（NULL 表示永久生效） |
| operator_id | BIGINT | NOT NULL | — | 操作人 ID |
| create_by | BIGINT | NOT NULL | — | 创建人 ID |
| create_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 创建时间 |
| update_by | BIGINT | NOT NULL | — | 更新人 ID |
| update_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 更新时间 |
| is_deleted | VARCHAR(20) | NOT NULL | — | 逻辑删除：NOT_DELETED / DELETED |
| deleted_at | TIMESTAMP | — | NULL | 删除时间 |

### 4.15 sys_resource_usage — 资源使用计量表

记录租户资源消耗明细，用于配额管控和计费依据。

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | BIGINT | NOT NULL, PK | — | 雪花算法主键 |
| tenant_id | BIGINT | NOT NULL | — | 租户 ID |
| tenant_code | VARCHAR(50) | NOT NULL | — | 租户编码 |
| resource_code | VARCHAR(50) | NOT NULL | — | 资源类型代码 |
| usage_value | BIGINT | NOT NULL | — | 消耗数值 |
| business_type | VARCHAR(50) | NOT NULL | — | 业务类型 |
| business_id | BIGINT | NOT NULL | — | 关联业务 ID |
| usage_time | TIMESTAMP | — | CURRENT_TIMESTAMP | 消耗时间 |
| remark | VARCHAR(500) | NOT NULL | — | 备注 |
| create_by | BIGINT | NOT NULL | — | 创建人 ID |
| create_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 创建时间 |
| update_by | BIGINT | NOT NULL | — | 更新人 ID |
| update_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 更新时间 |
| is_deleted | VARCHAR(20) | NOT NULL | — | 逻辑删除：NOT_DELETED / DELETED |
| deleted_at | TIMESTAMP | — | NULL | 删除时间 |

### 4.16 bill_order — 订单表

记录租户购买套餐、续费等产生的订单信息。

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | BIGINT | NOT NULL, PK | — | 雪花算法主键 |
| tenant_id | BIGINT | NOT NULL | — | 租户 ID |
| tenant_code | VARCHAR(50) | NOT NULL | — | 租户编码（冗余） |
| tenant_name | VARCHAR(100) | NOT NULL | — | 租户名称（冗余） |
| order_no | VARCHAR(64) | NOT NULL | — | 订单号 |
| product_type | VARCHAR(20) | NOT NULL | — | 产品类型：PACKAGE / QUOTA_ADJUSTMENT |
| product_id | BIGINT | NOT NULL | — | 产品 ID |
| total_amount | NUMERIC(10,2) | NOT NULL | — | 订单总金额 |
| pay_amount | NUMERIC(10,2) | — | 0.00 | 实际支付金额 |
| status | VARCHAR(20) | NOT NULL | — | 订单状态：PENDING / PAID / CANCELLED / REFUNDED |
| pay_time | TIMESTAMP | — | NULL | 支付时间 |
| pay_channel | VARCHAR(50) | NOT NULL | — | 支付渠道 |
| order_items | JSONB | — | `'[]'::jsonb` | 订单明细 |
| ext_attributes | JSONB | — | `'{}'::jsonb` | 扩展属性 |
| create_by | BIGINT | NOT NULL | — | 创建人 ID |
| create_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 创建时间 |
| update_by | BIGINT | NOT NULL | — | 更新人 ID |
| update_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 更新时间 |
| is_deleted | VARCHAR(20) | NOT NULL | — | 逻辑删除：NOT_DELETED / DELETED |
| deleted_at | TIMESTAMP | — | NULL | 删除时间 |

### 4.17 bill_invoice — 发票管理表

记录订单关联的发票信息。

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | BIGINT | NOT NULL, PK | — | 雪花算法主键 |
| tenant_id | BIGINT | NOT NULL | — | 租户 ID |
| tenant_code | VARCHAR(50) | NOT NULL | — | 租户编码（冗余） |
| tenant_name | VARCHAR(100) | NOT NULL | — | 租户名称（冗余） |
| order_id | BIGINT | NOT NULL | — | 关联订单 ID |
| invoice_no | VARCHAR(64) | NOT NULL | — | 发票号码 |
| invoice_type | VARCHAR(20) | NOT NULL | — | 发票类型：ORDINARY / SPECIAL / ELECTRONIC |
| invoice_title | VARCHAR(200) | NOT NULL | — | 发票抬头 |
| tax_id | VARCHAR(50) | NOT NULL | — | 税号 |
| amount | NUMERIC(10,2) | NOT NULL | — | 发票金额 |
| status | VARCHAR(20) | NOT NULL | — | 发票状态：PENDING / ISSUED / CANCELLED |
| invoice_url | VARCHAR(500) | NOT NULL | — | 发票文件 URL |
| create_by | BIGINT | NOT NULL | — | 创建人 ID |
| create_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 创建时间 |
| update_by | BIGINT | NOT NULL | — | 更新人 ID |
| update_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 更新时间 |
| is_deleted | VARCHAR(20) | NOT NULL | — | 逻辑删除：NOT_DELETED / DELETED |
| deleted_at | TIMESTAMP | — | NULL | 删除时间 |

### 4.18 sys_dept — 部门表

存储部门树形结构，支持无限层级。

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | BIGINT | NOT NULL, PK | — | 雪花算法主键 |
| dept_name | VARCHAR(50) | NOT NULL | — | 部门名称 |
| parent_id | BIGINT | NOT NULL | — | 父部门 ID，顶级为 0 |
| path | VARCHAR(1000) | NOT NULL | — | 部门层级路径 |
| tenant_id | BIGINT | NOT NULL | — | 所属租户 ID |
| tenant_code | VARCHAR(50) | NOT NULL | — | 租户编码 |
| leader_id | BIGINT | NOT NULL | — | 部门负责人 ID |
| phone | VARCHAR(20) | NOT NULL | — | 部门电话 |
| sort_order | INT4 | NOT NULL | — | 排序 |
| status | VARCHAR(20) | NOT NULL | — | 部门状态：ENABLED / DISABLED |
| dept_desc | VARCHAR(500) | NOT NULL | — | 部门描述 |
| create_by | BIGINT | NOT NULL | — | 创建人 ID |
| create_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 创建时间 |
| update_by | BIGINT | NOT NULL | — | 更新人 ID |
| update_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 更新时间 |
| is_deleted | VARCHAR(20) | NOT NULL | — | 逻辑删除：NOT_DELETED / DELETED |
| deleted_at | TIMESTAMP | — | NULL | 删除时间 |

### 4.19 sys_post — 岗位表

定义岗位信息，关联部门。

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | BIGINT | NOT NULL, PK | — | 雪花算法主键 |
| post_name | VARCHAR(50) | NOT NULL | — | 岗位名称 |
| post_code | VARCHAR(50) | NOT NULL | — | 岗位编码 |
| dept_id | BIGINT | NOT NULL | — | 所属部门 ID |
| tenant_id | BIGINT | NOT NULL | — | 所属租户 ID |
| tenant_code | VARCHAR(50) | NOT NULL | — | 租户编码 |
| sort_order | INT4 | NOT NULL | — | 排序 |
| status | VARCHAR(20) | NOT NULL | — | 岗位状态：ENABLED / DISABLED |
| post_desc | VARCHAR(500) | NOT NULL | — | 岗位描述 |
| create_by | BIGINT | NOT NULL | — | 创建人 ID |
| create_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 创建时间 |
| update_by | BIGINT | NOT NULL | — | 更新人 ID |
| update_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 更新时间 |
| is_deleted | VARCHAR(20) | NOT NULL | — | 逻辑删除：NOT_DELETED / DELETED |
| deleted_at | TIMESTAMP | — | NULL | 删除时间 |

### 4.20 sys_user_group — 用户组表

定义用户组，支持按类型分组管理用户。

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | BIGINT | NOT NULL, PK | — | 雪花算法主键 |
| group_name | VARCHAR(50) | NOT NULL | — | 用户组名称 |
| group_type | VARCHAR(20) | NOT NULL | — | 用户组类型 |
| tenant_id | BIGINT | NOT NULL | — | 所属租户 ID |
| tenant_code | VARCHAR(50) | NOT NULL | — | 租户编码 |
| description | VARCHAR(200) | NOT NULL | — | 描述 |
| create_by | BIGINT | NOT NULL | — | 创建人 ID |
| create_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 创建时间 |
| update_by | BIGINT | NOT NULL | — | 更新人 ID |
| update_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 更新时间 |
| is_deleted | VARCHAR(20) | NOT NULL | — | 逻辑删除：NOT_DELETED / DELETED |
| deleted_at | TIMESTAMP | — | NULL | 删除时间 |

### 4.21 sys_user_group_rel — 用户组成员表

用户组与用户的多对多关联。

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | BIGINT | NOT NULL, PK | — | 雪花算法主键 |
| group_id | BIGINT | NOT NULL | — | 用户组 ID |
| user_id | BIGINT | NOT NULL | — | 用户 ID |
| create_by | BIGINT | NOT NULL | — | 创建人 ID |
| create_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 创建时间 |
| update_by | BIGINT | NOT NULL | — | 更新人 ID |
| update_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 更新时间 |
| is_deleted | VARCHAR(20) | NOT NULL | — | 逻辑删除：NOT_DELETED / DELETED |
| deleted_at | TIMESTAMP | — | NULL | 删除时间 |

### 4.22 sys_role_dept_rel — 角色数据权限关联表

角色与部门的数据权限关联，用于 CUSTOM 数据权限范围。

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | BIGINT | NOT NULL, PK | — | 雪花算法主键 |
| role_id | BIGINT | NOT NULL | — | 角色 ID |
| dept_id | BIGINT | NOT NULL | — | 部门 ID |
| tenant_id | BIGINT | NOT NULL | — | 租户 ID |
| tenant_code | VARCHAR(50) | NOT NULL | — | 租户编码 |
| create_by | BIGINT | NOT NULL | — | 创建人 ID |
| create_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 创建时间 |
| update_by | BIGINT | NOT NULL | — | 更新人 ID |
| update_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 更新时间 |
| is_deleted | VARCHAR(20) | NOT NULL | — | 逻辑删除：NOT_DELETED / DELETED |
| deleted_at | TIMESTAMP | — | NULL | 删除时间 |

### 4.23 sys_menu — 菜单表

前端导航与按钮权限映射，支持目录、菜单、按钮三级结构。

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | BIGINT | NOT NULL, PK | — | 雪花算法主键 |
| menu_name | VARCHAR(50) | NOT NULL | — | 菜单名称 |
| menu_type | VARCHAR(20) | NOT NULL | — | 菜单类型：DIRECTORY / MENU / BUTTON |
| parent_id | BIGINT | NOT NULL | — | 父菜单 ID（0 为根菜单） |
| parent_name | VARCHAR(100) | NOT NULL | — | 父菜单名称（冗余） |
| menu_desc | VARCHAR(200) | NOT NULL | — | 菜单描述 |
| icon | VARCHAR(100) | NOT NULL | — | 菜单图标 |
| path | VARCHAR(200) | NOT NULL | — | 路由地址 |
| component | VARCHAR(200) | NOT NULL | — | 组件路径 |
| perm_code | VARCHAR(100) | NOT NULL | — | 权限标识 |
| visible | BOOLEAN | NOT NULL | TRUE | 是否可见 |
| status | VARCHAR(20) | NOT NULL | — | 菜单状态：ENABLED / DISABLED |
| sort_order | INT4 | — | 0 | 排序 |
| create_by | BIGINT | NOT NULL | — | 创建人 ID |
| create_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 创建时间 |
| update_by | BIGINT | NOT NULL | — | 更新人 ID |
| update_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 更新时间 |
| is_deleted | VARCHAR(20) | NOT NULL | — | 逻辑删除：NOT_DELETED / DELETED |
| deleted_at | TIMESTAMP | — | NULL | 删除时间 |

### 4.24 sys_dict — 字典类型表

定义系统常量类型，支持租户级自定义字典。

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | BIGINT | NOT NULL, PK | — | 雪花算法主键 |
| dict_name | VARCHAR(100) | NOT NULL | — | 字典名称 |
| dict_type | VARCHAR(100) | NOT NULL | — | 字典类型编码 |
| tenant_id | BIGINT | NOT NULL | — | 所属租户 ID（0 为系统字典） |
| tenant_code | VARCHAR(50) | NOT NULL | — | 租户编码（冗余） |
| status | VARCHAR(20) | NOT NULL | — | 字典状态：ENABLED / DISABLED |
| remark | VARCHAR(500) | NOT NULL | — | 备注 |
| create_by | BIGINT | NOT NULL | — | 创建人 ID |
| create_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 创建时间 |
| update_by | BIGINT | NOT NULL | — | 更新人 ID |
| update_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 更新时间 |
| is_deleted | VARCHAR(20) | NOT NULL | — | 逻辑删除：NOT_DELETED / DELETED |
| deleted_at | TIMESTAMP | — | NULL | 删除时间 |

### 4.25 sys_dict_item — 字典数据表

字典具体键值对，关联字典类型。

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | BIGINT | NOT NULL, PK | — | 雪花算法主键 |
| dict_id | BIGINT | NOT NULL | — | 关联字典类型 ID |
| dict_label | VARCHAR(100) | NOT NULL | — | 字典标签 |
| dict_value | VARCHAR(100) | NOT NULL | — | 字典值 |
| sort_order | INT4 | — | 0 | 排序 |
| status | VARCHAR(20) | NOT NULL | — | 字典项状态：ENABLED / DISABLED |
| create_by | BIGINT | NOT NULL | — | 创建人 ID |
| create_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 创建时间 |
| update_by | BIGINT | NOT NULL | — | 更新人 ID |
| update_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 更新时间 |
| is_deleted | VARCHAR(20) | NOT NULL | — | 逻辑删除：NOT_DELETED / DELETED |
| deleted_at | TIMESTAMP | — | NULL | 删除时间 |

### 4.26 sys_file — 文件资源表

存储上传的文件信息。

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | BIGINT | NOT NULL, PK | — | 雪花算法主键 |
| tenant_id | BIGINT | NOT NULL | — | 所属租户 ID |
| tenant_code | VARCHAR(50) | NOT NULL | — | 租户编码（冗余） |
| file_name | VARCHAR(255) | NOT NULL | — | 文件名称 |
| file_path | VARCHAR(500) | NOT NULL | — | 文件存储路径 |
| file_url | VARCHAR(500) | NOT NULL | — | 文件访问 URL |
| file_size | BIGINT | — | 0 | 文件大小（字节） |
| file_type | VARCHAR(50) | NOT NULL | — | 文件类型 |
| upload_by | BIGINT | NOT NULL | — | 上传人 ID |
| upload_time | TIMESTAMP | — | CURRENT_TIMESTAMP | 上传时间 |
| create_by | BIGINT | NOT NULL | — | 创建人 ID |
| create_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 创建时间 |
| update_by | BIGINT | NOT NULL | — | 更新人 ID |
| update_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 更新时间 |
| is_deleted | VARCHAR(20) | NOT NULL | — | 逻辑删除：NOT_DELETED / DELETED |
| deleted_at | TIMESTAMP | — | NULL | 删除时间 |

### 4.27 sys_notice — 通知公告表

系统公告与站内信，支持多种目标类型。

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | BIGINT | NOT NULL, PK | — | 雪花算法主键 |
| notice_title | VARCHAR(100) | NOT NULL | — | 公告标题 |
| notice_type | VARCHAR(20) | NOT NULL | — | 公告类型：ANNOUNCEMENT / NOTIFICATION |
| notice_content | TEXT | — | — | 公告内容 |
| status | VARCHAR(20) | NOT NULL | — | 公告状态：PUBLISHED / UNPUBLISHED |
| target_type | VARCHAR(20) | NOT NULL | — | 目标类型：ALL / SPECIFIED_TENANT / SPECIFIED_USER |
| target_ids | VARCHAR(1000) | NOT NULL | — | 目标 ID 集合（逗号分隔） |
| tenant_id | BIGINT | NOT NULL | — | 所属租户 ID（0 为系统公告） |
| tenant_code | VARCHAR(50) | NOT NULL | — | 租户编码（冗余） |
| create_by | BIGINT | NOT NULL | — | 创建人 ID |
| create_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 创建时间 |
| update_by | BIGINT | NOT NULL | — | 更新人 ID |
| update_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 更新时间 |
| is_deleted | VARCHAR(20) | NOT NULL | — | 逻辑删除：NOT_DELETED / DELETED |
| deleted_at | TIMESTAMP | — | NULL | 删除时间 |

### 4.28 sys_notice_user_rel — 用户公告阅读状态表

记录用户公告已读/未读状态。

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | BIGINT | NOT NULL, PK | — | 雪花算法主键 |
| notice_id | BIGINT | NOT NULL | — | 公告 ID |
| user_id | BIGINT | NOT NULL | — | 用户 ID |
| tenant_id | BIGINT | NOT NULL | — | 租户 ID |
| read_status | VARCHAR(20) | NOT NULL | — | 阅读状态：UNREAD / READ |
| read_time | TIMESTAMP | — | NULL | 阅读时间 |
| create_by | BIGINT | NOT NULL | — | 创建人 ID |
| create_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 创建时间 |
| update_by | BIGINT | NOT NULL | — | 更新人 ID |
| update_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 更新时间 |
| is_deleted | VARCHAR(20) | NOT NULL | — | 逻辑删除：NOT_DELETED / DELETED |
| deleted_at | TIMESTAMP | — | NULL | 删除时间 |

### 4.29 sys_form_config — 动态表单配置表

实现不同行业表单字段动态配置。

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | BIGINT | NOT NULL, PK | — | 雪花算法主键 |
| tenant_id | BIGINT | NOT NULL | — | 所属租户 ID |
| tenant_code | VARCHAR(50) | NOT NULL | — | 租户编码（冗余） |
| biz_type | VARCHAR(50) | NOT NULL | — | 业务类型（如 education_order, restaurant_order） |
| form_name | VARCHAR(100) | NOT NULL | — | 表单名称 |
| form_schema | JSONB | NOT NULL | — | 表单结构配置（定义字段类型/校验/选项） |
| status | VARCHAR(20) | NOT NULL | — | 表单状态：ENABLED / DISABLED |
| version | INT4 | — | 1 | 版本号（支持表单版本管理） |
| create_by | BIGINT | NOT NULL | — | 创建人 ID |
| create_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 创建时间 |
| update_by | BIGINT | NOT NULL | — | 更新人 ID |
| update_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 更新时间 |
| is_deleted | VARCHAR(20) | NOT NULL | — | 逻辑删除：NOT_DELETED / DELETED |
| deleted_at | TIMESTAMP | — | NULL | 删除时间 |

### 4.30 sys_datasource_config — 动态数据源配置表

实现下拉框数据来源动态配置。

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | BIGINT | NOT NULL, PK | — | 雪花算法主键 |
| tenant_id | BIGINT | NOT NULL | — | 所属租户 ID |
| tenant_code | VARCHAR(50) | NOT NULL | — | 租户编码（冗余） |
| datasource_code | VARCHAR(50) | NOT NULL | — | 数据源编码（如 dish_list, part_list） |
| datasource_name | VARCHAR(100) | NOT NULL | — | 数据源名称 |
| datasource_type | VARCHAR(20) | NOT NULL | — | 数据源类型：BIZ_TABLE / API / DICT / SQL |
| datasource_config | JSONB | NOT NULL | — | 数据源配置（定义表名/字段/条件） |
| cache_enabled | BOOLEAN | NOT NULL | TRUE | 是否启用缓存 |
| cache_expire | INT4 | — | 300 | 缓存过期时间（秒） |
| status | VARCHAR(20) | NOT NULL | — | 数据源状态：ENABLED / DISABLED |
| create_by | BIGINT | NOT NULL | — | 创建人 ID |
| create_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 创建时间 |
| update_by | BIGINT | NOT NULL | — | 更新人 ID |
| update_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 更新时间 |
| is_deleted | VARCHAR(20) | NOT NULL | — | 逻辑删除：NOT_DELETED / DELETED |
| deleted_at | TIMESTAMP | — | NULL | 删除时间 |

### 4.31 sys_print_template — 打印模板配置表

实现不同行业打印/PDF模板动态配置。

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | BIGINT | NOT NULL, PK | — | 雪花算法主键 |
| tenant_id | BIGINT | NOT NULL | — | 所属租户 ID |
| tenant_code | VARCHAR(50) | NOT NULL | — | 租户编码（冗余） |
| template_code | VARCHAR(50) | NOT NULL | — | 模板编码（如 education_contract, restaurant_receipt） |
| template_name | VARCHAR(100) | NOT NULL | — | 模板名称 |
| biz_type | VARCHAR(50) | NOT NULL | — | 业务类型 |
| template_type | VARCHAR(20) | NOT NULL | — | 模板类型：HTML / MARKDOWN / JSON_CONFIG |
| template_content | TEXT | NOT NULL | — | 模板内容（HTML/模板引擎语法） |
| template_config | JSONB | — | `'{}'::jsonb` | 模板配置（定义变量/条件显示） |
| paper_size | VARCHAR(20) | NOT NULL | 'A4' | 纸张大小（A4/A5/80mm热敏纸等） |
| orientation | VARCHAR(10) | NOT NULL | 'portrait' | 纸张方向（portrait/landscape） |
| status | VARCHAR(20) | NOT NULL | — | 模板状态：ENABLED / DISABLED |
| is_default | BOOLEAN | NOT NULL | FALSE | 是否默认模板 |
| version | INT4 | — | 1 | 版本号 |
| create_by | BIGINT | NOT NULL | — | 创建人 ID |
| create_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 创建时间 |
| update_by | BIGINT | NOT NULL | — | 更新人 ID |
| update_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 更新时间 |
| is_deleted | VARCHAR(20) | NOT NULL | — | 逻辑删除：NOT_DELETED / DELETED |
| deleted_at | TIMESTAMP | — | NULL | 删除时间 |

### 4.32 sys_oper_log — 操作日志表

审计用户操作行为。

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | BIGINT | NOT NULL, PK | — | 雪花算法主键 |
| tenant_id | BIGINT | NOT NULL | — | 租户 ID |
| tenant_code | VARCHAR(50) | NOT NULL | — | 租户编码（冗余） |
| module | VARCHAR(50) | NOT NULL | — | 操作模块 |
| business_type | VARCHAR(20) | NOT NULL | — | 业务类型：INSERT / UPDATE / DELETE / EXPORT / IMPORT / OTHER |
| method | VARCHAR(100) | NOT NULL | — | 请求方法 |
| request_method | VARCHAR(10) | NOT NULL | — | 请求方式：GET / POST |
| operator_name | VARCHAR(50) | NOT NULL | — | 操作人员姓名 |
| operator_id | BIGINT | NOT NULL | — | 操作人员 ID |
| dept_name | VARCHAR(50) | NOT NULL | — | 部门名称 |
| oper_url | VARCHAR(255) | NOT NULL | — | 请求 URL |
| oper_ip | VARCHAR(128) | NOT NULL | — | 操作 IP |
| oper_location | VARCHAR(255) | NOT NULL | — | 操作地点 |
| oper_param | VARCHAR(2000) | NOT NULL | — | 请求参数 |
| json_result | VARCHAR(2000) | NOT NULL | — | 返回结果 |
| status | VARCHAR(20) | NOT NULL | — | 操作状态：SUCCESS / FAILED |
| error_msg | VARCHAR(2000) | NOT NULL | — | 错误消息 |
| oper_time | TIMESTAMP | — | CURRENT_TIMESTAMP | 操作时间 |
| is_deleted | VARCHAR(20) | NOT NULL | — | 逻辑删除：NOT_DELETED / DELETED |
| deleted_at | TIMESTAMP | — | NULL | 删除时间 |

### 4.33 sys_login_log — 登录日志表

记录用户登录信息。

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | BIGINT | NOT NULL, PK | — | 雪花算法主键 |
| user_id | BIGINT | NOT NULL | — | 用户 ID |
| username | VARCHAR(50) | NOT NULL | — | 用户名 |
| tenant_id | BIGINT | NOT NULL | — | 租户 ID |
| tenant_code | VARCHAR(50) | NOT NULL | — | 租户编码（冗余） |
| ip_address | VARCHAR(128) | NOT NULL | — | 登录 IP |
| login_location | VARCHAR(255) | NOT NULL | — | 登录地点 |
| browser | VARCHAR(50) | NOT NULL | — | 浏览器 |
| os | VARCHAR(50) | NOT NULL | — | 操作系统 |
| status | VARCHAR(20) | NOT NULL | — | 登录状态：SUCCESS / FAILED |
| msg | VARCHAR(255) | NOT NULL | — | 提示消息 |
| login_time | TIMESTAMP | — | CURRENT_TIMESTAMP | 登录时间 |
| is_deleted | VARCHAR(20) | NOT NULL | — | 逻辑删除：NOT_DELETED / DELETED |
| deleted_at | TIMESTAMP | — | NULL | 删除时间 |

### 4.34 sys_data_audit_log — 数据变更审计表

记录数据字段变更详情。

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | BIGINT | NOT NULL, PK | — | 雪花算法主键 |
| tenant_id | BIGINT | NOT NULL | — | 租户 ID |
| tenant_code | VARCHAR(50) | NOT NULL | — | 租户编码（冗余） |
| table_name | VARCHAR(50) | NOT NULL | — | 表名 |
| record_id | BIGINT | NOT NULL | — | 记录 ID |
| operator_id | BIGINT | NOT NULL | — | 操作人 ID |
| operate_type | VARCHAR(20) | NOT NULL | — | 操作类型：UPDATE / DELETE |
| old_value | JSONB | — | `'{}'::jsonb` | 修改前数据 |
| new_value | JSONB | — | `'{}'::jsonb` | 修改后数据 |
| operate_time | TIMESTAMP | — | CURRENT_TIMESTAMP | 操作时间 |
| is_deleted | VARCHAR(20) | NOT NULL | — | 逻辑删除：NOT_DELETED / DELETED |
| deleted_at | TIMESTAMP | — | NULL | 删除时间 |

### 4.35 sys_message_template — 消息模板表

定义各类消息的内容模板。

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | BIGINT | NOT NULL, PK | — | 雪花算法主键 |
| template_code | VARCHAR(100) | NOT NULL | — | 模板编码（如 ORDER_PAID, PACKAGE_EXPIRE_WARNING） |
| template_name | VARCHAR(100) | NOT NULL | — | 模板名称 |
| tenant_id | BIGINT | NOT NULL | — | 所属租户 ID（0 为系统模板） |
| tenant_code | VARCHAR(50) | NOT NULL | — | 租户编码（冗余） |
| biz_type | VARCHAR(50) | NOT NULL | — | 业务类型（如 order, subscription, system） |
| message_type | VARCHAR(20) | NOT NULL | — | 消息类型：NOTIFICATION / MARKETING / VERIFICATION / REMINDER |
| template_title | VARCHAR(200) | NOT NULL | — | 模板标题 |
| template_content | TEXT | NOT NULL | — | 模板内容（支持变量占位符如 `${userName}`） |
| template_example | JSONB | — | `'{}'::jsonb` | 示例数据（用于测试预览） |
| variables | JSONB | — | `'[]'::jsonb` | 变量定义（定义变量名/类型/必填） |
| status | VARCHAR(20) | NOT NULL | — | 模板状态：ENABLED / DISABLED |
| version | INT4 | — | 1 | 版本号 |
| language | VARCHAR(20) | NOT NULL | 'zh-CN' | 语言（zh-CN/en-US 等） |
| create_by | BIGINT | NOT NULL | — | 创建人 ID |
| create_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 创建时间 |
| update_by | BIGINT | NOT NULL | — | 更新人 ID |
| update_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 更新时间 |
| is_deleted | VARCHAR(20) | NOT NULL | — | 逻辑删除：NOT_DELETED / DELETED |
| deleted_at | TIMESTAMP | — | NULL | 删除时间 |

### 4.36 sys_inbox_message — 站内信收件箱表

用户个人消息 inbox。

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | BIGINT | NOT NULL, PK | — | 雪花算法主键 |
| user_id | BIGINT | NOT NULL | — | 用户 ID |
| tenant_id | BIGINT | NOT NULL | — | 租户 ID |
| tenant_code | VARCHAR(50) | NOT NULL | — | 租户编码（冗余） |
| message_type | VARCHAR(20) | NOT NULL | — | 消息类型：SYSTEM / APPROVAL / BILLING / MARKETING |
| title | VARCHAR(200) | NOT NULL | — | 消息标题 |
| content | TEXT | NOT NULL | — | 消息内容 |
| priority | VARCHAR(20) | NOT NULL | — | 优先级：NORMAL / IMPORTANT / URGENT |
| is_read | BOOLEAN | NOT NULL | FALSE | 是否已读 |
| read_time | TIMESTAMP | — | NULL | 阅读时间 |
| is_archived | BOOLEAN | NOT NULL | FALSE | 是否已归档 |
| archived_time | TIMESTAMP | — | NULL | 归档时间 |
| expire_time | TIMESTAMP | — | NULL | 过期时间（NULL 为永不过期） |
| action_url | VARCHAR(500) | NOT NULL | — | 操作按钮 URL |
| action_text | VARCHAR(50) | NOT NULL | — | 操作按钮文案 |
| create_by | BIGINT | NOT NULL | — | 创建人 ID |
| create_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 创建时间 |
| update_by | BIGINT | NOT NULL | — | 更新人 ID |
| update_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 更新时间 |
| is_deleted | VARCHAR(20) | NOT NULL | — | 逻辑删除：NOT_DELETED / DELETED |
| deleted_at | TIMESTAMP | — | NULL | 删除时间 |

### 4.37 sys_message_schedule — 定时消息任务表

预约发送或周期性发送的消息。

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | BIGINT | NOT NULL, PK | — | 雪花算法主键 |
| task_name | VARCHAR(100) | NOT NULL | — | 任务名称 |
| tenant_id | BIGINT | NOT NULL | — | 租户 ID |
| tenant_code | VARCHAR(50) | NOT NULL | — | 租户编码（冗余） |
| template_id | BIGINT | NOT NULL | — | 消息模板 ID |
| target_type | VARCHAR(20) | NOT NULL | — | 目标类型：SPECIFIED_USER / SPECIFIED_TENANT / CONDITION_MATCH |
| target_ids | VARCHAR(1000) | NOT NULL | — | 目标 ID 集合（逗号分隔） |
| trigger_type | VARCHAR(20) | NOT NULL | — | 触发类型：ONCE / PERIODIC / EVENT |
| trigger_condition | JSONB | — | `'{}'::jsonb` | 触发条件（定义时间/事件规则） |
| execute_time | TIMESTAMP | NOT NULL | — | 计划执行时间 |
| repeat_rule | JSONB | — | `'{}'::jsonb` | 重复规则（如 `{"type":"daily","interval":1}`） |
| status | VARCHAR(20) | NOT NULL | — | 任务状态：PENDING / EXECUTING / COMPLETED / CANCELLED |
| executed_count | INT4 | — | 0 | 已执行次数 |
| last_execute_time | TIMESTAMP | — | NULL | 最后执行时间 |
| create_by | BIGINT | NOT NULL | — | 创建人 ID |
| create_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 创建时间 |
| update_by | BIGINT | NOT NULL | — | 更新人 ID |
| update_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 更新时间 |
| is_deleted | VARCHAR(20) | NOT NULL | — | 逻辑删除：NOT_DELETED / DELETED |
| deleted_at | TIMESTAMP | — | NULL | 删除时间 |

### 4.38 sys_tenant_security — 租户安全策略配置表

存储租户级别的安全策略配置。

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | BIGINT | NOT NULL, PK | — | 雪花算法主键 |
| tenant_id | BIGINT | NOT NULL | — | 租户 ID |
| tenant_code | VARCHAR(50) | NOT NULL | — | 租户编码（冗余） |
| pwd_min_length | INT4 | NOT NULL | — | 密码最小长度 |
| pwd_complexity | VARCHAR(20) | NOT NULL | — | 密码复杂度：NONE / LETTER_NUMBER / LETTER_NUMBER_SPECIAL |
| pwd_expire_days | INT4 | NOT NULL | — | 密码过期天数（0 表示永不过期） |
| login_fail_limit | INT4 | NOT NULL | — | 登录失败锁定次数 |
| lock_duration | INT4 | NOT NULL | — | 锁定时长（分钟） |
| create_by | BIGINT | NOT NULL | — | 创建人 ID |
| create_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 创建时间 |
| update_by | BIGINT | NOT NULL | — | 更新人 ID |
| update_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 更新时间 |
| is_deleted | VARCHAR(20) | NOT NULL | — | 逻辑删除：NOT_DELETED / DELETED |
| deleted_at | TIMESTAMP | — | NULL | 删除时间 |

### 4.39 sys_industry_template — 行业模板表

定义不同行业的初始化配置模板。

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | BIGINT | NOT NULL, PK | — | 雪花算法主键 |
| template_code | VARCHAR(50) | NOT NULL | — | 行业模板编码 |
| template_name | VARCHAR(100) | NOT NULL | — | 行业模板名称 |
| industry_type | VARCHAR(20) | NOT NULL | — | 行业类型：INTERNET / MANUFACTURING / EDUCATION / GOVERNMENT / FINANCE / HEALTHCARE / RESTAURANT |
| template_desc | VARCHAR(500) | NOT NULL | — | 模板描述 |
| template_config | JSONB | — | `'{}'::jsonb` | 模板配置（包含默认权限/表单/数据源等） |
| status | VARCHAR(20) | NOT NULL | — | 模板状态：ENABLED / DISABLED |
| create_by | BIGINT | NOT NULL | — | 创建人 ID |
| create_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 创建时间 |
| update_by | BIGINT | NOT NULL | — | 更新人 ID |
| update_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 更新时间 |
| is_deleted | VARCHAR(20) | NOT NULL | — | 逻辑删除：NOT_DELETED / DELETED |
| deleted_at | TIMESTAMP | — | NULL | 删除时间 |

### 4.40 sys_form_template — 表单模板表

定义行业模板下的动态表单配置。

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | BIGINT | NOT NULL, PK | — | 雪花算法主键 |
| template_id | BIGINT | NOT NULL | — | 关联行业模板 ID |
| form_code | VARCHAR(50) | NOT NULL | — | 表单编码 |
| form_name | VARCHAR(100) | NOT NULL | — | 表单名称 |
| biz_type | VARCHAR(50) | NOT NULL | — | 业务类型 |
| form_schema | JSONB | NOT NULL | — | 表单结构配置 |
| form_version | INT4 | — | 1 | 版本号 |
| status | VARCHAR(20) | NOT NULL | — | 表单状态：ENABLED / DISABLED |
| create_by | BIGINT | NOT NULL | — | 创建人 ID |
| create_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 创建时间 |
| update_by | BIGINT | NOT NULL | — | 更新人 ID |
| update_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 更新时间 |
| is_deleted | VARCHAR(20) | NOT NULL | — | 逻辑删除：NOT_DELETED / DELETED |
| deleted_at | TIMESTAMP | — | NULL | 删除时间 |

### 4.41 sys_tenant_config — 租户配置表

存储租户级别的键值对配置。

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | BIGINT | NOT NULL, PK | — | 雪花算法主键 |
| tenant_id | BIGINT | NOT NULL | — | 租户 ID |
| tenant_code | VARCHAR(50) | NOT NULL | — | 租户编码（冗余） |
| config_key | VARCHAR(100) | NOT NULL | — | 配置键 |
| config_value | TEXT | NOT NULL | — | 配置值 |
| config_type | VARCHAR(20) | NOT NULL | — | 配置类型：STRING / NUMBER / BOOLEAN / JSON |
| config_group | VARCHAR(50) | NOT NULL | — | 配置分组 |
| config_desc | VARCHAR(200) | NOT NULL | — | 配置描述 |
| inheritable | BOOLEAN | NOT NULL | TRUE | 是否可继承给子租户 |
| status | VARCHAR(20) | NOT NULL | — | 配置状态：ENABLED / DISABLED |
| create_by | BIGINT | NOT NULL | — | 创建人 ID |
| create_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 创建时间 |
| update_by | BIGINT | NOT NULL | — | 更新人 ID |
| update_at | TIMESTAMP | — | CURRENT_TIMESTAMP | 更新时间 |
| is_deleted | VARCHAR(20) | NOT NULL | — | 逻辑删除：NOT_DELETED / DELETED |
| deleted_at | TIMESTAMP | — | NULL | 删除时间 |

---

## 5 索引设计

### 5.1 唯一索引

| 表名 | 索引名 | 索引列 | 说明 |
|------|--------|--------|------|
| sys_tenant | uk_tenant_code | tenant_code | 租户编码全局唯一 |
| sys_tenant_subscription | uk_subscription_code | subscription_code | 订阅编码全局唯一 |
| sys_user | uk_user_code | user_code | 用户编码全局唯一 |
| sys_user | uk_user_name | user_name | 登录用户名全局唯一 |
| sys_perm | uk_perm_code | perm_code | 权限编码全局唯一 |
| sys_perm_policy | uk_policy_code | policy_code | 策略编码全局唯一 |
| sys_role | uk_role_code | role_code | 角色编码租户内唯一 |
| sys_role_policy | uk_role_policy_code | policy_code | 角色策略编码全局唯一 |
| prod_package | uk_package_code | package_code | 套餐编码全局唯一 |
| bill_order | uk_order_no | order_no | 订单号全局唯一 |
| bill_invoice | uk_invoice_no | invoice_no | 发票号全局唯一 |
| sys_post | uk_post_code | post_code | 岗位编码租户内唯一 |
| sys_dict | uk_dict_type | dict_type | 字典类型租户内唯一 |
| sys_datasource_config | uk_datasource_code | tenant_id, datasource_code | 数据源编码租户内唯一 |
| sys_print_template | uk_template_code | tenant_id, template_code | 打印模板编码租户内唯一 |
| sys_message_template | uk_template_code | tenant_id, template_code | 消息模板编码租户内唯一 |
| sys_tenant_config | uk_config_key | tenant_id, config_key | 配置键租户内唯一 |
| sys_industry_template | uk_industry_template_code | template_code | 行业模板编码全局唯一 |
| sys_form_template | uk_form_code | template_id, form_code | 表单编码模板内唯一 |
| sys_form_config | uk_form_config | tenant_id, biz_type | 表单配置租户+业务类型唯一 |

### 5.2 普通索引

| 表名 | 索引名 | 索引列 | 说明 |
|------|--------|--------|------|
| sys_tenant | idx_tenant_parent_id | parent_id | 加速树形层级查询 |
| sys_tenant | idx_tenant_status | status | 按状态筛选租户 |
| sys_tenant_subscription | idx_sub_tenant_id | tenant_id | 按租户查订阅 |
| sys_tenant_subscription | idx_sub_status | status | 按状态筛选订阅 |
| sys_tenant_subscription | idx_sub_end_time | end_time | 过期扫描 |
| sys_user | idx_user_status | status | 按状态筛选用户 |
| sys_user | idx_user_email | email | 按邮箱查询用户 |
| sys_user | idx_user_phone | phone | 按手机号查询用户 |
| sys_user_tenant_rel | idx_utr_user_id | user_id | 按用户查租户列表 |
| sys_user_tenant_rel | idx_utr_tenant_id | tenant_id | 按租户查用户列表 |
| sys_user_tenant_rel | idx_utr_user_tenant | user_id, tenant_id | 用户-租户联合查询 |
| sys_perm | idx_perm_parent_id | parent_id | 加速权限树查询 |
| sys_perm | idx_perm_type | perm_type | 按权限类型筛选 |
| sys_perm_policy | idx_pp_perm_id | perm_id | 按权限查策略 |
| sys_perm_policy | idx_pp_tenant_id | tenant_id | 按租户查策略 |
| sys_perm_policy | idx_pp_target | target_type, target_id | 按目标查策略 |
| sys_perm_policy | idx_pp_access_type | access_type | 按访问类型筛选 |
| sys_user_perm_rel | idx_upr_user_id | user_id | 按用户查权限策略 |
| sys_user_perm_rel | idx_upr_policy_id | policy_id | 按策略查用户 |
| sys_role | idx_role_tenant_id | tenant_id | 按租户查角色 |
| sys_role | idx_role_status | status | 按状态筛选角色 |
| sys_role_policy | idx_rp_role_id | role_id | 按角色查策略 |
| sys_role_policy | idx_rp_tenant_id | tenant_id | 按租户查角色策略 |
| sys_user_role_rel | idx_urr_user_id | user_id | 按用户查角色 |
| sys_user_role_rel | idx_urr_role_id | role_id | 按角色查用户 |
| sys_user_role_rel | idx_urr_policy_id | policy_id | 按策略查关联 |
| sys_user_token | idx_token_user_id | user_id | 按用户查 Token |
| sys_user_token | idx_token_tenant_id | tenant_id | 按租户查 Token |
| sys_user_token | idx_token_expire_time | expire_time | 过期清理扫描 |
| prod_package | idx_pkg_status | status | 按状态筛选套餐 |
| prod_package_quota | idx_pq_package_id | package_id | 按套餐查配额 |
| prod_package_quota | idx_pq_resource_code | resource_code | 按资源代码查询 |
| sys_tenant_quota_adjustment | idx_tqa_tenant_id | tenant_id | 按租户查调整记录 |
| sys_tenant_quota_adjustment | idx_tqa_resource_code | resource_code | 按资源代码查询 |
| sys_resource_usage | idx_ru_tenant_id | tenant_id | 按租户查使用记录 |
| sys_resource_usage | idx_ru_resource_code | resource_code | 按资源代码查询 |
| sys_resource_usage | idx_ru_business | business_type, business_id | 按业务查询 |
| bill_order | idx_order_tenant_id | tenant_id | 按租户查订单 |
| bill_order | idx_order_status | status | 按状态筛选订单 |
| bill_invoice | idx_invoice_tenant_id | tenant_id | 按租户查发票 |
| bill_invoice | idx_invoice_order_id | order_id | 按订单查发票 |
| sys_dept | idx_dept_tenant_id | tenant_id | 按租户查部门 |
| sys_dept | idx_dept_parent_id | parent_id | 加速部门树查询 |
| sys_post | idx_post_tenant_id | tenant_id | 按租户查岗位 |
| sys_post | idx_post_dept_id | dept_id | 按部门查岗位 |
| sys_user_group | idx_ug_tenant_id | tenant_id | 按租户查用户组 |
| sys_user_group_rel | idx_ugr_group_id | group_id | 按用户组查成员 |
| sys_user_group_rel | idx_ugr_user_id | user_id | 按用户查用户组 |
| sys_role_dept_rel | idx_rdr_role_id | role_id | 按角色查部门 |
| sys_role_dept_rel | idx_rdr_dept_id | dept_id | 按部门查角色 |
| sys_role_dept_rel | idx_rdr_tenant_id | tenant_id | 按租户查关联 |
| sys_menu | idx_menu_parent_id | parent_id | 加速菜单树查询 |
| sys_menu | idx_menu_status | status | 按状态筛选菜单 |
| sys_dict | idx_dict_tenant_id | tenant_id | 按租户查字典 |
| sys_dict_item | idx_di_dict_id | dict_id | 按字典类型查数据 |
| sys_file | idx_file_tenant_id | tenant_id | 按租户查文件 |
| sys_file | idx_file_upload_by | upload_by | 按上传人查文件 |
| sys_notice | idx_notice_tenant_id | tenant_id | 按租户查公告 |
| sys_notice_user_rel | idx_nur_notice_id | notice_id | 按公告查阅读状态 |
| sys_notice_user_rel | idx_nur_user_id | user_id | 按用户查公告 |
| sys_form_config | idx_fc_tenant_id | tenant_id | 按租户查表单配置 |
| sys_datasource_config | idx_dc_tenant_id | tenant_id | 按租户查数据源 |
| sys_print_template | idx_pt_tenant_id | tenant_id | 按租户查打印模板 |
| sys_oper_log | idx_ol_tenant_id | tenant_id | 按租户查操作日志 |
| sys_oper_log | idx_ol_operator_id | operator_id | 按操作人查日志 |
| sys_oper_log | idx_ol_oper_time | oper_time | 按时间范围查询 |
| sys_login_log | idx_ll_tenant_id | tenant_id | 按租户查登录日志 |
| sys_login_log | idx_ll_user_id | user_id | 按用户查登录日志 |
| sys_login_log | idx_ll_login_time | login_time | 按时间范围查询 |
| sys_data_audit_log | idx_dal_tenant_id | tenant_id | 按租户查审计日志 |
| sys_data_audit_log | idx_dal_table_record | table_name, record_id | 按表+记录查变更 |
| sys_data_audit_log | idx_dal_operator_id | operator_id | 按操作人查审计 |
| sys_message_template | idx_mt_tenant_id | tenant_id | 按租户查消息模板 |
| sys_inbox_message | idx_im_user_id | user_id | 按用户查站内信 |
| sys_inbox_message | idx_im_tenant_id | tenant_id | 按租户查站内信 |
| sys_inbox_message | idx_im_is_read | user_id, is_read | 按用户查未读消息 |
| sys_message_schedule | idx_ms_tenant_id | tenant_id | 按租户查定时消息 |
| sys_message_schedule | idx_ms_status | status | 按状态筛选任务 |
| sys_message_schedule | idx_ms_execute_time | execute_time | 定时扫描执行 |
| sys_tenant_security | idx_ts_tenant_id | tenant_id | 按租户查安全策略 |
| sys_industry_template | idx_it_industry_type | industry_type | 按行业类型筛选 |
| sys_form_template | idx_ft_template_id | template_id | 按行业模板查表单 |
| sys_tenant_config | idx_tc_tenant_id | tenant_id | 按租户查配置 |
| sys_tenant_config | idx_tc_config_group | tenant_id, config_group | 按租户+分组查配置 |

### 5.3 GIN 索引（JSONB 字段）

| 表名 | 索引名 | 索引列 | 说明 |
|------|--------|--------|------|
| sys_tenant | idx_tenant_ext_attributes_gin | ext_attributes | 加速扩展属性 JSONB 查询 |
| sys_perm_policy | idx_pp_field_operates_gin | field_operates | 加速允许字段 JSONB 查询 |
| prod_package | idx_pkg_ext_attributes_gin | ext_attributes | 加速套餐扩展属性 JSONB 查询 |
| bill_order | idx_order_order_items_gin | order_items | 加速订单明细 JSONB 查询 |
| bill_order | idx_order_ext_attributes_gin | ext_attributes | 加速订单扩展属性 JSONB 查询 |
| sys_data_audit_log | idx_dal_old_value_gin | old_value | 加速变更前数据 JSONB 查询 |
| sys_data_audit_log | idx_dal_new_value_gin | new_value | 加速变更后数据 JSONB 查询 |
| sys_form_config | idx_fc_form_schema_gin | form_schema | 加速表单结构 JSONB 查询 |
| sys_datasource_config | idx_dc_datasource_config_gin | datasource_config | 加速数据源配置 JSONB 查询 |
| sys_print_template | idx_pt_template_config_gin | template_config | 加速打印模板配置 JSONB 查询 |
| sys_message_template | idx_mt_template_example_gin | template_example | 加速消息模板示例 JSONB 查询 |
| sys_message_template | idx_mt_variables_gin | variables | 加速消息变量 JSONB 查询 |
| sys_message_schedule | idx_ms_trigger_condition_gin | trigger_condition | 加速触发条件 JSONB 查询 |
| sys_message_schedule | idx_ms_repeat_rule_gin | repeat_rule | 加速重复规则 JSONB 查询 |
| sys_industry_template | idx_it_template_config_gin | template_config | 加速行业模板配置 JSONB 查询 |
| sys_form_template | idx_ft_form_schema_gin | form_schema | 加速表单模板结构 JSONB 查询 |

### 5.4 部分索引（WHERE is_deleted）

利用 PostgreSQL 部分索引特性，仅对未删除数据建立索引，减小索引体积并提升查询性能。

| 表名 | 索引名 | 索引列 | WHERE 条件 | 说明 |
|------|--------|--------|------------|------|
| sys_tenant | idx_tenant_code_active | tenant_code | is_deleted = 'NOT_DELETED' | 未删除租户编码唯一 |
| sys_tenant | idx_tenant_status_active | status | is_deleted = 'NOT_DELETED' | 未删除租户状态筛选 |
| sys_user | idx_user_name_active | user_name | is_deleted = 'NOT_DELETED' | 未删除用户名唯一 |
| sys_user | idx_user_status_active | status | is_deleted = 'NOT_DELETED' | 未删除用户状态筛选 |
| sys_user_tenant_rel | idx_utr_user_tenant_active | user_id, tenant_id | is_deleted = 'NOT_DELETED' | 未删除关联查询 |
| sys_perm | idx_perm_code_active | perm_code | is_deleted = 'NOT_DELETED' | 未删除权限编码唯一 |
| sys_perm_policy | idx_pp_policy_code_active | policy_code | is_deleted = 'NOT_DELETED' | 未删除策略编码唯一 |
| sys_role | idx_role_code_active | role_code | is_deleted = 'NOT_DELETED' | 未删除角色编码唯一 |
| sys_user_token | idx_token_user_tenant_active | user_id, tenant_id | is_deleted = 'NOT_DELETED' | 未删除 Token 查询 |
| sys_dept | idx_dept_tenant_active | tenant_id | is_deleted = 'NOT_DELETED' | 未删除部门租户查询 |
| sys_post | idx_post_code_active | post_code | is_deleted = 'NOT_DELETED' | 未删除岗位编码唯一 |
| sys_dict | idx_dict_type_active | dict_type | is_deleted = 'NOT_DELETED' | 未删除字典类型唯一 |
| sys_menu | idx_menu_perm_code_active | perm_code | is_deleted = 'NOT_DELETED' | 未删除菜单权限标识 |
| prod_package | idx_pkg_code_active | package_code | is_deleted = 'NOT_DELETED' | 未删除套餐编码唯一 |
| sys_tenant_config | idx_tc_key_active | tenant_id, config_key | is_deleted = 'NOT_DELETED' | 未删除配置键唯一 |

---

## 6 表关系说明

### 6.1 关系总览

| 关系 | 类型 | 关联表 | 关联字段 | 说明 |
|------|------|--------|----------|------|
| 租户 → 子租户 | 一对多（自引用） | sys_tenant | parent_id → id | 支持无限层级树形结构 |
| 租户 → 订阅 | 一对多 | sys_tenant → sys_tenant_subscription | tenant_id → id | 一个租户可拥有多条订阅记录 |
| 租户 → 套餐 | 多对一 | sys_tenant_subscription → prod_package | package_id → id | 订阅关联套餐产品 |
| 套餐 → 配额 | 一对多 | prod_package → prod_package_quota | package_id → id | 一个套餐包含多个资源配额 |
| 用户 ↔ 租户 | 多对多 | sys_user_tenant_rel | user_id, tenant_id | 一个用户可属于多个租户 |
| 权限 → 子权限 | 一对多（自引用） | sys_perm | parent_id → id | 权限树形结构 |
| 权限 → 策略 | 一对多 | sys_perm → sys_perm_policy | perm_id → id | 一个权限可有多条策略 |
| 用户 ↔ 策略 | 多对多 | sys_user_perm_rel | user_id, policy_id | 用户直接绑定策略 |
| 租户 → 角色 | 一对多 | sys_tenant → sys_role | tenant_id → id | 租户拥有多个角色 |
| 角色 ↔ 策略 | 多对多 | sys_role_policy | role_id, tenant_id | 角色绑定策略（含租户上下文） |
| 用户 ↔ 角色 | 多对多 | sys_user_role_rel | user_id, role_id, policy_id | 用户绑定角色（含策略上下文） |
| 用户 → Token | 一对多 | sys_user_token | user_id, tenant_id | 用户在租户下的会话记录 |
| 租户 → 配额调整 | 一对多 | sys_tenant → sys_tenant_quota_adjustment | tenant_id → id | 租户的配额调整记录 |
| 租户 → 资源使用 | 一对多 | sys_tenant → sys_resource_usage | tenant_id → id | 租户的资源消耗记录 |
| 租户 → 订单 | 一对多 | sys_tenant → bill_order | tenant_id → id | 租户的订单记录 |
| 订单 → 发票 | 一对多 | bill_order → bill_invoice | order_id → id | 一个订单可有多张发票 |
| 租户 → 部门 | 一对多 | sys_tenant → sys_dept | tenant_id → id | 租户下的部门 |
| 部门 → 子部门 | 一对多（自引用） | sys_dept | parent_id → id | 部门树形结构 |
| 部门 → 岗位 | 一对多 | sys_dept → sys_post | dept_id → id | 部门下的岗位 |
| 租户 → 用户组 | 一对多 | sys_tenant → sys_user_group | tenant_id → id | 租户下的用户组 |
| 用户组 ↔ 用户 | 多对多 | sys_user_group_rel | group_id, user_id | 用户组成员关联 |
| 角色 ↔ 部门 | 多对多 | sys_role_dept_rel | role_id, dept_id | 角色数据权限关联 |
| 菜单 → 子菜单 | 一对多（自引用） | sys_menu | parent_id → id | 菜单树形结构 |
| 租户 → 字典 | 一对多 | sys_tenant → sys_dict | tenant_id → id | 租户下的字典 |
| 字典 → 字典项 | 一对多 | sys_dict → sys_dict_item | dict_id → id | 字典下的键值对 |
| 租户 → 文件 | 一对多 | sys_tenant → sys_file | tenant_id → id | 租户下的文件 |
| 租户 → 公告 | 一对多 | sys_tenant → sys_notice | tenant_id → id | 租户下的公告 |
| 公告 ↔ 用户 | 多对多 | sys_notice_user_rel | notice_id, user_id | 公告阅读状态 |
| 租户 → 表单配置 | 一对多 | sys_tenant → sys_form_config | tenant_id → id | 租户下的表单配置 |
| 租户 → 数据源 | 一对多 | sys_tenant → sys_datasource_config | tenant_id → id | 租户下的数据源 |
| 租户 → 打印模板 | 一对多 | sys_tenant → sys_print_template | tenant_id → id | 租户下的打印模板 |
| 租户 → 操作日志 | 一对多 | sys_tenant → sys_oper_log | tenant_id → id | 租户下的操作日志 |
| 租户 → 登录日志 | 一对多 | sys_tenant → sys_login_log | tenant_id → id | 租户下的登录日志 |
| 租户 → 审计日志 | 一对多 | sys_tenant → sys_data_audit_log | tenant_id → id | 租户下的数据审计 |
| 租户 → 消息模板 | 一对多 | sys_tenant → sys_message_template | tenant_id → id | 租户下的消息模板 |
| 租户 → 站内信 | 一对多 | sys_tenant → sys_inbox_message | tenant_id → id | 租户下的站内信 |
| 用户 → 站内信 | 一对多 | sys_user → sys_inbox_message | user_id → id | 用户的站内信 |
| 租户 → 定时消息 | 一对多 | sys_tenant → sys_message_schedule | tenant_id → id | 租户下的定时消息 |
| 消息模板 → 定时消息 | 一对多 | sys_message_template → sys_message_schedule | template_id → id | 模板关联定时任务 |
| 租户 → 安全策略 | 一对一 | sys_tenant → sys_tenant_security | tenant_id → id | 租户安全策略配置 |
| 行业模板 → 表单模板 | 一对多 | sys_industry_template → sys_form_template | template_id → id | 行业模板下的表单 |
| 租户 → 租户配置 | 一对多 | sys_tenant → sys_tenant_config | tenant_id → id | 租户下的键值配置 |

### 6.2 用户-租户 多对多

```
sys_user  1 ──── *  sys_user_tenant_rel  * ──── 1  sys_tenant
```

- 通过 `sys_user_tenant_rel` 中间表关联
- 中间表携带 `is_admin`（是否管理员）、`is_default`（是否默认租户）、`dept_id`（所属部门）等上下文信息
- 用户登录后需选择租户上下文，`is_default = true` 的租户为默认进入租户

### 6.3 用户-角色 多对多

```
sys_user  1 ──── *  sys_user_role_rel  * ──── 1  sys_role
```

- 通过 `sys_user_role_rel` 中间表关联
- 中间表携带 `policy_id`，表示该用户-角色绑定关联的策略上下文
- 角色在租户范围内生效，通过 `sys_role.tenant_id` 实现租户隔离

### 6.4 用户-策略 多对多

```
sys_user  1 ──── *  sys_user_perm_rel  * ──── 1  sys_perm_policy
```

- 通过 `sys_user_perm_rel` 中间表关联
- 支持绕过角色直接为用户授权策略，适用于特殊权限场景
- 策略最终生效需同时满足 `sys_perm_policy.status = 'ACTIVE'`

### 6.5 角色-策略 多对多

```
sys_role  1 ──── *  sys_role_policy  * ──── 1  sys_perm_policy
```

- 通过 `sys_role_policy` 中间表关联
- 每条关联记录携带 `tenant_id`，实现同一角色在不同租户下拥有不同策略
- `target_type` + `target_id` 定义策略的授权目标层级

### 6.6 权限解析链路

```
用户登录 → 查询 sys_user_role_rel（获取角色列表）
        → 查询 sys_role_policy（获取角色关联策略）
        → 查询 sys_user_perm_rel（获取用户直接策略）
        → 合并策略 → 查询 sys_perm_policy（获取策略详情）
        → 查询 sys_perm（获取权限资源信息）
        → 构建用户权限集
```

### 6.7 策略禁用层级

策略状态采用五值枚举，支持从系统级到用户级的逐层禁用：

```
ACTIVE（生效）
  ↓ 系统级禁用
DISABLED_SYSTEM_LEVEL
  ↓ 租户级禁用
DISABLED_TENANT_LEVEL
  ↓ 角色级禁用
DISABLED_ROLE_LEVEL
  ↓ 用户级禁用
DISABLED_USER_LEVEL
```

- 禁用优先级：用户级 > 角色级 > 租户级 > 系统级
- 即低层级禁用可覆盖高层级启用，高层级禁用则所有低层级均不可用

---

## 7 数据字典

### 7.1 通用枚举

#### is_deleted — 逻辑删除标记

| 枚举值 | 说明 | 适用表 |
|--------|------|--------|
| NOT_DELETED | 未删除 | 所有表 |
| DELETED | 已删除 | 所有表 |

> 对应 Java 枚举：`GlobalEnum.Deleted`

#### status — 通用状态（用户/权限/角色/部门/岗位等）

| 枚举值 | 说明 | 适用表 |
|--------|------|--------|
| ENABLED | 启用 | sys_user, sys_perm, sys_role, sys_dept, sys_post, sys_menu, sys_dict, sys_dict_item, sys_file, prod_package, sys_form_config, sys_datasource_config, sys_print_template, sys_message_template, sys_industry_template, sys_form_template, sys_tenant_config |
| DISABLED | 禁用 | 同上 |

> 对应 Java 枚举：`GlobalEnum.PermStatus`

### 7.2 租户相关枚举

#### TenantStatus — 租户状态

| 枚举值 | 说明 |
|--------|------|
| ENABLED | 启用 |
| DISABLED | 停用 |
| EXPIRED | 过期 |

> 对应 Java 枚举：`GlobalEnum.TenantStatus`

#### SubscriptionStatus — 订阅状态

| 枚举值 | 说明 |
|--------|------|
| ACTIVE | 生效中 |
| EXPIRED | 已过期 |
| CANCELLED | 已取消 |
| PENDING | 待生效 |

> 对应 Java 枚举：`GlobalEnum.SubscriptionStatus`

#### SubscriptionType — 订阅类型

| 枚举值 | 说明 |
|--------|------|
| NEW | 新订阅 |
| RENEWAL | 续费 |
| UPGRADE | 升级 |
| DOWNGRADE | 降级 |

> 对应 Java 枚举：`GlobalEnum.SubscriptionType`

#### SourceType — 订阅来源类型

| 枚举值 | 说明 |
|--------|------|
| DIRECT | 直接购买 |
| ADMIN_ASSIGN | 管理员分配 |
| PARENT_GRANT | 上级租户授予 |

### 7.3 权限策略相关枚举

#### PermPolicyStatus — 策略状态

| 枚举值 | 说明 |
|--------|------|
| ACTIVE | 生效 |
| DISABLED_SYSTEM_LEVEL | 系统级禁用 |
| DISABLED_TENANT_LEVEL | 租户级禁用 |
| DISABLED_ROLE_LEVEL | 角色级禁用 |
| DISABLED_USER_LEVEL | 用户级禁用 |

> 对应 Java 枚举：`GlobalEnum.PermPolicyStatus`

#### PermPolicyTargetType — 策略目标类型

| 枚举值 | 说明 |
|--------|------|
| TENANT | 租户级策略 |
| ROLE | 角色级策略 |
| USER | 用户级策略 |

> 对应 Java 枚举：`GlobalEnum.PermPolicyTargetType`

#### PermPolicyAccessType — 访问类型

| 枚举值 | 说明 |
|--------|------|
| QUERY | 查询 |
| CREATE | 新增 |
| UPDATE | 更新 |

> 对应 Java 枚举：`GlobalEnum.PermPolicyAccessType`

### 7.4 角色相关枚举

#### RoleLevel — 角色层级

| 枚举值 | 说明 |
|--------|------|
| SYSTEM | 系统级角色 |
| TENANT | 租户级角色 |
| USER | 用户级角色 |

#### DataScope — 数据权限范围

| 枚举值 | 说明 |
|--------|------|
| ALL | 全部数据 |
| DEPT | 本部门数据 |
| DEPT_AND_SUB | 本部门及下级数据 |
| SELF | 仅本人数据 |
| CUSTOM | 自定义数据 |

### 7.5 权限类型枚举

#### perm_type — 权限类型

| 枚举值 | 说明 |
|--------|------|
| MENU | 菜单权限 |
| BUTTON | 按钮权限 |
| API | 接口权限 |

### 7.6 Token 相关枚举

#### TokenStatus — Token 状态

| 枚举值 | 说明 |
|--------|------|
| ACTIVE | 有效 |
| EXPIRED | 已过期 |
| REVOKED | 已撤销 |

### 7.7 计费相关枚举

#### PackageCycleType — 套餐周期类型

| 枚举值 | 说明 |
|--------|------|
| MONTH | 月 |
| QUARTER | 季度 |
| YEAR | 年 |

#### QuotaAdjustType — 配额调整类型

| 枚举值 | 说明 |
|--------|------|
| INCREASE | 增加 |
| DECREASE | 减少 |

#### OrderStatus — 订单状态

| 枚举值 | 说明 |
|--------|------|
| PENDING | 待支付 |
| PAID | 已支付 |
| CANCELLED | 已取消 |
| REFUNDED | 已退款 |

#### InvoiceType — 发票类型

| 枚举值 | 说明 |
|--------|------|
| ORDINARY | 普通发票 |
| SPECIAL | 专用发票 |
| ELECTRONIC | 电子发票 |

#### InvoiceStatus — 发票状态

| 枚举值 | 说明 |
|--------|------|
| PENDING | 待开具 |
| ISSUED | 已开具 |
| CANCELLED | 已作废 |

### 7.8 组织架构相关枚举

#### MenuType — 菜单类型

| 枚举值 | 说明 |
|--------|------|
| DIRECTORY | 目录 |
| MENU | 菜单 |
| BUTTON | 按钮 |

#### NoticeType — 公告类型

| 枚举值 | 说明 |
|--------|------|
| ANNOUNCEMENT | 公告 |
| NOTIFICATION | 通知 |

#### NoticeStatus — 公告状态

| 枚举值 | 说明 |
|--------|------|
| PUBLISHED | 已发布 |
| UNPUBLISHED | 未发布 |

#### NoticeTargetType — 公告目标类型

| 枚举值 | 说明 |
|--------|------|
| ALL | 全部 |
| SPECIFIED_TENANT | 指定租户 |
| SPECIFIED_USER | 指定用户 |

#### ReadStatus — 阅读状态

| 枚举值 | 说明 |
|--------|------|
| UNREAD | 未读 |
| READ | 已读 |

### 7.9 动态配置相关枚举

#### DatasourceType — 数据源类型

| 枚举值 | 说明 |
|--------|------|
| BIZ_TABLE | 业务表 |
| API | 接口 |
| DICT | 字典 |
| SQL | SQL 查询 |

#### TemplateType — 打印模板类型

| 枚举值 | 说明 |
|--------|------|
| HTML | HTML 模板 |
| MARKDOWN | Markdown 模板 |
| JSON_CONFIG | JSON 配置 |

#### ConfigType — 配置类型

| 枚举值 | 说明 |
|--------|------|
| STRING | 字符串 |
| NUMBER | 数字 |
| BOOLEAN | 布尔 |
| JSON | JSON 对象 |

### 7.10 审计相关枚举

#### OperBusinessType — 操作业务类型

| 枚举值 | 说明 |
|--------|------|
| INSERT | 新增 |
| UPDATE | 修改 |
| DELETE | 删除 |
| EXPORT | 导出 |
| IMPORT | 导入 |
| OTHER | 其他 |

#### OperStatus — 操作状态

| 枚举值 | 说明 |
|--------|------|
| SUCCESS | 成功 |
| FAILED | 失败 |

#### LoginStatus — 登录状态

| 枚举值 | 说明 |
|--------|------|
| SUCCESS | 成功 |
| FAILED | 失败 |

#### DataAuditOperateType — 数据审计操作类型

| 枚举值 | 说明 |
|--------|------|
| UPDATE | 修改 |
| DELETE | 删除 |

### 7.11 消息通知相关枚举

#### MessageType — 消息类型（模板）

| 枚举值 | 说明 |
|--------|------|
| NOTIFICATION | 通知 |
| MARKETING | 营销 |
| VERIFICATION | 验证码 |
| REMINDER | 提醒 |

#### InboxMessageType — 站内信消息类型

| 枚举值 | 说明 |
|--------|------|
| SYSTEM | 系统消息 |
| APPROVAL | 审批消息 |
| BILLING | 计费消息 |
| MARKETING | 营销消息 |

#### MessagePriority — 消息优先级

| 枚举值 | 说明 |
|--------|------|
| NORMAL | 普通 |
| IMPORTANT | 重要 |
| URGENT | 紧急 |

#### ScheduleTriggerType — 定时消息触发类型

| 枚举值 | 说明 |
|--------|------|
| ONCE | 一次性 |
| PERIODIC | 周期性 |
| EVENT | 事件触发 |

#### ScheduleStatus — 定时消息状态

| 枚举值 | 说明 |
|--------|------|
| PENDING | 待执行 |
| EXECUTING | 执行中 |
| COMPLETED | 已完成 |
| CANCELLED | 已取消 |

#### ScheduleTargetType — 定时消息目标类型

| 枚举值 | 说明 |
|--------|------|
| SPECIFIED_USER | 指定用户 |
| SPECIFIED_TENANT | 指定租户 |
| CONDITION_MATCH | 条件匹配 |

### 7.12 安全相关枚举

#### PwdComplexity — 密码复杂度

| 枚举值 | 说明 |
|--------|------|
| NONE | 无要求 |
| LETTER_NUMBER | 字母+数字 |
| LETTER_NUMBER_SPECIAL | 字母+数字+特殊字符 |

### 7.13 行业适配相关枚举

#### IndustryType — 行业类型

| 枚举值 | 说明 |
|--------|------|
| INTERNET | 互联网 |
| MANUFACTURING | 制造业 |
| EDUCATION | 教育 |
| GOVERNMENT | 政务 |
| FINANCE | 金融 |
| HEALTHCARE | 医疗 |
| RESTAURANT | 餐饮 |

---

## 8 JSONB 字段使用规范

### 8.1 sys_tenant.ext_attributes

租户扩展属性，存储行业特定配置和自定义元数据。

**Schema 定义：**

```json
{
  "type": "object",
  "properties": {
    "industry": {
      "type": "string",
      "description": "行业分类标识"
    },
    "quota": {
      "type": "integer",
      "description": "配额限制"
    },
    "custom_fields": {
      "type": "object",
      "description": "自定义扩展字段",
      "additionalProperties": true
    },
    "features": {
      "type": "array",
      "description": "启用的特性列表",
      "items": {
        "type": "string"
      }
    }
  },
  "additionalProperties": true
}
```

**示例数据：**

```json
{
  "industry": "tech",
  "quota": 100,
  "custom_fields": {
    "region": "east",
    "level": "premium"
  },
  "features": ["sso", "audit_log", "data_export"]
}
```

**查询示例：**

```sql
SELECT * FROM sys_tenant WHERE ext_attributes @> '{"industry": "tech"}' AND is_deleted = 'NOT_DELETED';
SELECT * FROM sys_tenant WHERE ext_attributes->>'quota' > '50' AND is_deleted = 'NOT_DELETED';
```

### 8.2 sys_perm_policy.field_operates

权限策略允许操作的字段名列表。由于 `access_type` 已是独立字段，`field_operates` 仅需存储该 `access_type` 下允许的字段名列表。

**Schema 定义：**

```json
{
  "type": "array",
  "description": "允许操作的字段名列表",
  "items": {
    "type": "string"
  }
}
```

**示例数据：**

```json
["id", "user_name", "nick_name"]
```

**查询示例：**

```sql
-- 查询包含 "email" 字段的策略
SELECT * FROM sys_perm_policy
WHERE field_operates ? 'email'
  AND is_deleted = 'NOT_DELETED';

-- 查询空字段列表（无字段权限）
SELECT * FROM sys_perm_policy
WHERE field_operates = '[]'::jsonb
  AND is_deleted = 'NOT_DELETED';
```

### 8.3 bill_order.order_items

订单明细，存储订单包含的产品项列表。

**Schema 定义：**

```json
{
  "type": "array",
  "items": {
    "type": "object",
    "properties": {
      "product_id": { "type": "integer", "description": "产品ID" },
      "product_name": { "type": "string", "description": "产品名称" },
      "quantity": { "type": "integer", "description": "数量" },
      "unit_price": { "type": "number", "description": "单价" },
      "amount": { "type": "number", "description": "金额" }
    },
    "required": ["product_id", "product_name", "quantity", "unit_price", "amount"]
  }
}
```

**示例数据：**

```json
[
  {"product_id": 101, "product_name": "企业版套餐", "quantity": 1, "unit_price": 999.00, "amount": 999.00},
  {"product_id": 201, "product_name": "额外用户配额", "quantity": 50, "unit_price": 10.00, "amount": 500.00}
]
```

### 8.4 sys_data_audit_log.old_value / new_value

数据变更审计的修改前后数据快照。

**Schema 定义：**

```json
{
  "type": "object",
  "description": "字段变更快照，键为字段名，值为字段值",
  "additionalProperties": true
}
```

**示例数据：**

```json
{
  "nick_name": "张三",
  "email": "old@example.com",
  "status": "ENABLED"
}
```

### 8.5 sys_form_config.form_schema

动态表单结构配置，定义字段类型、校验规则、选项等。

**Schema 定义：**

```json
{
  "type": "object",
  "properties": {
    "fields": {
      "type": "array",
      "items": {
        "type": "object",
        "properties": {
          "field_key": { "type": "string", "description": "字段标识" },
          "field_label": { "type": "string", "description": "字段标签" },
          "field_type": { "type": "string", "description": "字段类型(text/select/date/number...)" },
          "required": { "type": "boolean", "description": "是否必填" },
          "options": { "type": "array", "description": "选项列表(select类型)" },
          "validation": { "type": "object", "description": "校验规则" }
        }
      }
    },
    "layout": { "type": "string", "description": "布局方式" }
  }
}
```

**示例数据：**

```json
{
  "fields": [
    {"field_key": "order_no", "field_label": "订单号", "field_type": "text", "required": true},
    {"field_key": "amount", "field_label": "金额", "field_type": "number", "required": true, "validation": {"min": 0}},
    {"field_key": "status", "field_label": "状态", "field_type": "select", "options": ["PENDING", "PAID", "CANCELLED"]}
  ],
  "layout": "vertical"
}
```

### 8.6 sys_datasource_config.datasource_config

动态数据源配置，定义下拉框数据来源。

**Schema 定义：**

```json
{
  "type": "object",
  "properties": {
    "table_name": { "type": "string", "description": "表名(BIZ_TABLE类型)" },
    "label_field": { "type": "string", "description": "显示字段" },
    "value_field": { "type": "string", "description": "值字段" },
    "conditions": { "type": "array", "description": "过滤条件" },
    "api_url": { "type": "string", "description": "接口地址(API类型)" },
    "dict_type": { "type": "string", "description": "字典类型(DICT类型)" },
    "sql_template": { "type": "string", "description": "SQL模板(SQL类型)" }
  }
}
```

**示例数据：**

```json
{
  "table_name": "sys_dict_item",
  "label_field": "dict_label",
  "value_field": "dict_value",
  "conditions": [{"field": "dict_id", "operator": "=", "value": "${dictId}"}]
}
```

### 8.7 sys_print_template.template_config

打印模板配置，定义变量和条件显示规则。

**Schema 定义：**

```json
{
  "type": "object",
  "properties": {
    "variables": {
      "type": "array",
      "items": {
        "type": "object",
        "properties": {
          "name": { "type": "string" },
          "label": { "type": "string" },
          "default_value": {}
        }
      }
    },
    "conditional_sections": {
      "type": "array",
      "items": {
        "type": "object",
        "properties": {
          "section_id": { "type": "string" },
          "condition": { "type": "string" }
        }
      }
    }
  },
  "additionalProperties": true
}
```

**示例数据：**

```json
{
  "variables": [
    {"name": "companyName", "label": "公司名称", "default_value": ""},
    {"name": "totalAmount", "label": "总金额", "default_value": 0}
  ],
  "conditional_sections": [
    {"section_id": "tax-section", "condition": "${hasTax} === true"}
  ]
}
```

### 8.8 sys_message_template.variables

消息模板变量定义。

**Schema 定义：**

```json
{
  "type": "array",
  "items": {
    "type": "object",
    "properties": {
      "name": { "type": "string", "description": "变量名" },
      "type": { "type": "string", "description": "变量类型(string/number/date)" },
      "required": { "type": "boolean", "description": "是否必填" },
      "default_value": { "description": "默认值" }
    }
  }
}
```

**示例数据：**

```json
[
  {"name": "userName", "type": "string", "required": true},
  {"name": "expireDate", "type": "date", "required": true},
  {"name": "packageName", "type": "string", "required": false, "default_value": "基础版"}
]
```

### 8.9 sys_message_schedule.trigger_condition / repeat_rule

定时消息的触发条件和重复规则。

**trigger_condition Schema：**

```json
{
  "type": "object",
  "properties": {
    "event_type": { "type": "string", "description": "事件类型(EVENT触发)" },
    "event_source": { "type": "string", "description": "事件来源" },
    "time_range": {
      "type": "object",
      "properties": {
        "start": { "type": "string", "description": "开始时间" },
        "end": { "type": "string", "description": "结束时间" }
      }
    }
  },
  "additionalProperties": true
}
```

**repeat_rule Schema：**

```json
{
  "type": "object",
  "properties": {
    "type": { "type": "string", "description": "重复类型(daily/weekly/monthly)" },
    "interval": { "type": "integer", "description": "间隔" },
    "end_condition": { "type": "string", "description": "结束条件(date/count/never)" },
    "end_value": { "description": "结束值" }
  }
}
```

**示例数据：**

```json
{"type": "daily", "interval": 1, "end_condition": "date", "end_value": "2026-12-31"}
```

### 8.10 sys_industry_template.template_config

行业模板配置，包含默认权限、表单、数据源等初始化配置。

**Schema 定义：**

```json
{
  "type": "object",
  "properties": {
    "default_perms": { "type": "array", "description": "默认权限编码列表" },
    "default_forms": { "type": "array", "description": "默认表单编码列表" },
    "default_datasources": { "type": "array", "description": "默认数据源编码列表" },
    "default_configs": { "type": "object", "description": "默认租户配置" }
  },
  "additionalProperties": true
}
```

**示例数据：**

```json
{
  "default_perms": ["ORDER_MGMT", "ORDER_LIST", "ORDER_CREATE"],
  "default_forms": ["education_order_form"],
  "default_datasources": ["course_list", "teacher_list"],
  "default_configs": {"max_users": 100, "storage_limit": "10GB"}
}
```

### 8.11 JSONB 使用规范

| 规范项 | 说明 |
|--------|------|
| 默认值 | 对象类型 JSONB 默认值为 `'{}'::jsonb`，数组类型默认值为 `'[]'::jsonb`，不允许 NULL |
| 空值处理 | 无数据时写入 `{}` 或 `[]`，不使用 NULL |
| 键名规范 | 使用小写蛇形命名（如 `field_key`、`default_value`），与数据库字段命名风格一致 |
| 查询方式 | 使用 PostgreSQL JSONB 操作符 `@>`（包含）、`->>`（文本提取）、`?`（键/值存在） |
| 索引策略 | 所有 JSONB 字段均创建 GIN 索引，支持 `@>` 和 `?` 操作符的高效查询 |
| 数据校验 | 应用层负责 JSONB 数据的 Schema 校验，数据库层不做 CHECK 约束 |
| 大小限制 | 单个 JSONB 字段数据建议不超过 10KB，超大数据应拆分为独立表 |

---

## 9 多租户数据隔离策略

### 9.1 隔离模型

NexusIX-Platform 采用 **共享数据库 + 共享 Schema** 的多租户隔离模型，通过 `tenant_id` 列实现行级数据隔离。

### 9.2 隔离机制

#### 租户隔离表

以下表包含 `tenant_id` 列，查询时必须携带租户条件：

| 表名 | tenant_id 列 | 隔离方式 | 说明 |
|------|-------------|----------|------|
| sys_tenant | — | 自身即租户 | 通过 `id` 字段标识租户，`parent_id` 构建层级 |
| sys_tenant_subscription | tenant_id | 行级隔离 | 按租户隔离订阅数据 |
| sys_user_tenant_rel | tenant_id | 行级隔离 | 按租户隔离用户-租户关联 |
| sys_perm_policy | tenant_id | 行级隔离 | 按租户隔离权限策略 |
| sys_role | tenant_id | 行级隔离 | 按租户隔离角色 |
| sys_role_policy | tenant_id | 行级隔离 | 按租户隔离角色策略 |
| sys_user_token | tenant_id | 行级隔离 | 按租户隔离会话记录 |
| sys_tenant_quota_adjustment | tenant_id | 行级隔离 | 按租户隔离配额调整 |
| sys_resource_usage | tenant_id | 行级隔离 | 按租户隔离资源使用 |
| bill_order | tenant_id | 行级隔离 | 按租户隔离订单 |
| bill_invoice | tenant_id | 行级隔离 | 按租户隔离发票 |
| sys_dept | tenant_id | 行级隔离 | 按租户隔离部门 |
| sys_post | tenant_id | 行级隔离 | 按租户隔离岗位 |
| sys_user_group | tenant_id | 行级隔离 | 按租户隔离用户组 |
| sys_role_dept_rel | tenant_id | 行级隔离 | 按租户隔离角色-部门关联 |
| sys_dict | tenant_id | 行级隔离 | 按租户隔离字典（0 为系统字典） |
| sys_file | tenant_id | 行级隔离 | 按租户隔离文件 |
| sys_notice | tenant_id | 行级隔离 | 按租户隔离公告（0 为系统公告） |
| sys_notice_user_rel | tenant_id | 行级隔离 | 按租户隔离阅读状态 |
| sys_form_config | tenant_id | 行级隔离 | 按租户隔离表单配置 |
| sys_datasource_config | tenant_id | 行级隔离 | 按租户隔离数据源 |
| sys_print_template | tenant_id | 行级隔离 | 按租户隔离打印模板 |
| sys_oper_log | tenant_id | 行级隔离 | 按租户隔离操作日志 |
| sys_login_log | tenant_id | 行级隔离 | 按租户隔离登录日志 |
| sys_data_audit_log | tenant_id | 行级隔离 | 按租户隔离数据审计 |
| sys_message_template | tenant_id | 行级隔离 | 按租户隔离消息模板（0 为系统模板） |
| sys_inbox_message | tenant_id | 行级隔离 | 按租户隔离站内信 |
| sys_message_schedule | tenant_id | 行级隔离 | 按租户隔离定时消息 |
| sys_tenant_security | tenant_id | 行级隔离 | 按租户隔离安全策略 |
| sys_tenant_config | tenant_id | 行级隔离 | 按租户隔离配置 |

#### 忽略表清单（无需租户隔离）

以下表不含 `tenant_id` 列，为全局共享数据：

| 表名 | 原因 |
|------|------|
| sys_user | 用户跨租户共享，同一用户可属于多个租户 |
| sys_perm | 权限资源为系统级定义，所有租户共享同一套权限树 |
| sys_user_perm_rel | 用户直接策略关联，租户隔离通过 `sys_perm_policy.tenant_id` 间接实现 |
| sys_user_role_rel | 用户角色关联，租户隔离通过 `sys_role.tenant_id` 间接实现 |
| sys_menu | 菜单为系统级定义，所有租户共享同一套菜单树 |
| sys_dict_item | 字典项通过 `sys_dict.tenant_id` 间接实现租户隔离 |
| sys_user_group_rel | 用户组成员关联，租户隔离通过 `sys_user_group.tenant_id` 间接实现 |
| sys_tenant | 租户表本身，为全局核心数据，无需租户隔离 |
| prod_package | 套餐为全局产品定义，所有租户共享同一套套餐体系 |
| prod_package_quota | 配额模板通过 `prod_package` 间接关联，为全局产品定义 |
| sys_industry_template | 行业模板为系统级定义，所有租户共享 |
| sys_form_template | 表单模板通过 `sys_industry_template` 间接关联，为系统级定义 |

### 9.4 隔离实现要点

#### 应用层拦截

- 所有包含 `tenant_id` 的表，在执行 SQL 时必须携带 `WHERE tenant_id = ?` 条件
- 通过 MyBatis-Plus 或类似 ORM 框架的租户插件自动注入租户条件
- 写入操作自动填充当前租户 ID，查询操作自动追加租户过滤

#### 超级管理员

- 超级管理员（系统级）可跨租户查看数据，此时需显式绕过租户拦截
- 跨租户操作必须记录审计日志

#### 间接隔离表查询

对于不含 `tenant_id` 但通过关联表间接实现隔离的表，查询时需通过 JOIN 关联表实现：

```sql
-- 示例：查询某租户下的用户角色关联
SELECT urr.*
FROM sys_user_role_rel urr
INNER JOIN sys_role r ON urr.role_id = r.id
WHERE r.tenant_id = ? AND urr.is_deleted = 'NOT_DELETED';

-- 示例：查询某租户下的字典项
SELECT di.*
FROM sys_dict_item di
INNER JOIN sys_dict d ON di.dict_id = d.id
WHERE d.tenant_id = ? AND di.is_deleted = 'NOT_DELETED';
```

#### 系统级数据共享

- `sys_perm`、`sys_menu` 等系统级表为全局共享，所有租户可见
- `sys_dict`、`sys_notice`、`sys_message_template` 等表中 `tenant_id = 0` 表示系统级数据，所有租户可读
- 租户自定义数据 `tenant_id > 0`，仅对所属租户可见

---

> **文档结束** — NexusIX-Platform 数据库设计文档 v2.0.0 | 最后更新：2026-06-09