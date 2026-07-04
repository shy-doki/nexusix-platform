# NexusIX-Platform IAM模块接口测试文档

> **测试工具**: Apifox
> **版本**: v1.0
> **创建日期**: 2026-07-04
> **适用模块**: nexusix-iam（认证与权限管理服务）
> **接口总数**: 72个（6个Controller）

---

## 修订记录

| 版本 | 日期 | 修订人 | 修订内容 |
|------|------|--------|----------|
| v1.0 | 2026-07-04 | 测试团队 | 初始版本，覆盖IAM模块6个Controller共72个接口 |

---

## 目录

- [1. 测试环境说明](#1-测试环境说明)
- [2. 测试数据准备](#2-测试数据准备)
- [3. AuthController 认证管理（1个接口）](#3-authcontroller-认证管理1个接口)
  - [3.1 POST /auth/login 用户登录认证](#31-post-authlogin-用户登录认证)
- [4. SysUserController 用户管理（12个接口）](#4-sysusercontroller-用户管理12个接口)
  - [4.1 GET /user/list 查询用户列表](#41-get-userlist-查询用户列表)
  - [4.2 GET /user/page 分页查询用户列表](#42-get-userpage-分页查询用户列表)
  - [4.3 POST /user/query 条件查询用户列表](#43-post-userquery-条件查询用户列表)
  - [4.4 GET /user/detail/{userCode} 查询用户详情](#44-get-userdetailusercode-查询用户详情)
  - [4.5 POST /user/add 新增用户](#45-post-useradd-新增用户)
  - [4.6 PUT /user/update 修改用户](#46-put-userupdate-修改用户)
  - [4.7 PUT /user/status 更新用户状态](#47-put-userstatus-更新用户状态)
  - [4.8 DELETE /user/delete 删除用户](#48-delete-userdelete-删除用户)
  - [4.9 POST /user/batch 批量新增用户](#49-post-userbatch-批量新增用户)
  - [4.10 PUT /user/batch 批量修改用户](#410-put-userbatch-批量修改用户)
  - [4.11 PUT /user/status/batch 批量更新用户状态](#411-put-userstatusbatch-批量更新用户状态)
  - [4.12 DELETE /user/batch 批量删除用户](#412-delete-userbatch-批量删除用户)
- [5. SysRoleController 角色管理（12个接口）](#5-sysrolecontroller-角色管理12个接口)
  - [5.1 GET /role/list 查询角色列表](#51-get-rolelist-查询角色列表)
  - [5.2 GET /role/page 分页查询角色列表](#52-get-rolepage-分页查询角色列表)
  - [5.3 POST /role/query 条件查询角色列表](#53-post-rolequery-条件查询角色列表)
  - [5.4 GET /role/detail/{roleCode} 查询角色详情](#54-get-roledetailrolecode-查询角色详情)
  - [5.5 POST /role/add 新增角色](#55-post-roleadd-新增角色)
  - [5.6 PUT /role/update 修改角色](#56-put-roleupdate-修改角色)
  - [5.7 PUT /role/status 更新角色状态](#57-put-rolestatus-更新角色状态)
  - [5.8 DELETE /role/delete 删除角色](#58-delete-roledelete-删除角色)
  - [5.9 POST /role/batch 批量新增角色](#59-post-rolebatch-批量新增角色)
  - [5.10 PUT /role/batch 批量修改角色](#510-put-rolebatch-批量修改角色)
  - [5.11 PUT /role/status/batch 批量更新角色状态](#511-put-rolestatusbatch-批量更新角色状态)
  - [5.12 DELETE /role/batch 批量删除角色](#512-delete-rolebatch-批量删除角色)
- [6. SysPermController 权限管理（15个接口）](#6-syspermcontroller-权限管理15个接口)
  - [6.1 GET /perm/list 查询权限列表](#61-get-permlist-查询权限列表)
  - [6.2 GET /perm/page 分页查询权限列表](#62-get-permpage-分页查询权限列表)
  - [6.3 GET /perm/tree/list 查询权限树形结构列表](#63-get-permtreelist-查询权限树形结构列表)
  - [6.4 GET /perm/tree/page 分页查询权限树形结构](#64-get-permtreepage-分页查询权限树形结构)
  - [6.5 GET /perm/tree/{id} 查询指定权限树形结构](#65-get-permtreeid-查询指定权限树形结构)
  - [6.6 POST /perm/query 条件查询权限列表](#66-post-permquery-条件查询权限列表)
  - [6.7 GET /perm/detail/{permCode} 查询权限详情](#67-get-permdetailpermcode-查询权限详情)
  - [6.8 POST /perm/add 新增权限](#68-post-permadd-新增权限)
  - [6.9 PUT /perm/update 修改权限](#69-put-permupdate-修改权限)
  - [6.10 PUT /perm/status 更新权限状态](#610-put-permstatus-更新权限状态)
  - [6.11 DELETE /perm/delete 删除权限](#611-delete-permdelete-删除权限)
  - [6.12 POST /perm/batch 批量新增权限](#612-post-permbatch-批量新增权限)
  - [6.13 PUT /perm/batch 批量修改权限](#613-put-permbatch-批量修改权限)
  - [6.14 PUT /perm/status/batch 批量更新权限状态](#614-put-permstatusbatch-批量更新权限状态)
  - [6.15 DELETE /perm/batch 批量删除权限](#615-delete-permbatch-批量删除权限)
- [7. SysPermPolicyController 权限策略管理（16个接口）](#7-syspermpolicycontroller-权限策略管理16个接口)
  - [7.1 GET /perm-policy/list 查询权限策略列表](#71-get-perm-policylist-查询权限策略列表)
  - [7.2 GET /perm-policy/page 分页查询权限策略列表](#72-get-perm-policypage-分页查询权限策略列表)
  - [7.3 POST /perm-policy/query 条件查询权限策略列表](#73-post-perm-policyquery-条件查询权限策略列表)
  - [7.4 GET /perm-policy/detail/{policyCode} 查询权限策略详情](#74-get-perm-policydetailpolicycode-查询权限策略详情)
  - [7.5 POST /perm-policy/add 新增权限策略](#75-post-perm-policyadd-新增权限策略)
  - [7.6 PUT /perm-policy/update 修改权限策略](#76-put-perm-policyupdate-修改权限策略)
  - [7.7 PUT /perm-policy/status 更新权限策略状态](#77-put-perm-policystatus-更新权限策略状态)
  - [7.8 DELETE /perm-policy/delete 删除权限策略](#78-delete-perm-policydelete-删除权限策略)
  - [7.9 POST /perm-policy/batch 批量新增权限策略](#79-post-perm-policybatch-批量新增权限策略)
  - [7.10 PUT /perm-policy/batch 批量修改权限策略](#710-put-perm-policybatch-批量修改权限策略)
  - [7.11 PUT /perm-policy/status/batch 批量更新权限策略状态](#711-put-perm-policystatusbatch-批量更新权限策略状态)
  - [7.12 DELETE /perm-policy/batch 批量删除权限策略](#712-delete-perm-policybatch-批量删除权限策略)
  - [7.13 POST /perm-policy/bind 批量绑定权限到目标](#713-post-perm-policybind-批量绑定权限到目标)
  - [7.14 POST /perm-policy/unbind 批量解绑权限与目标](#714-post-perm-policyunbind-批量解绑权限与目标)
- [8. SysUserPolicyController 用户策略管理（16个接口）](#8-sysuserpolicycontroller-用户策略管理16个接口)
  - [8.1 GET /user-policy/list 查询用户策略列表](#81-get-user-policylist-查询用户策略列表)
  - [8.2 GET /user-policy/page 分页查询用户策略列表](#82-get-user-policypage-分页查询用户策略列表)
  - [8.3 POST /user-policy/query 条件查询用户策略列表](#83-post-user-policyquery-条件查询用户策略列表)
  - [8.4 GET /user-policy/detail/{policyCode} 查询用户策略详情](#84-get-user-policydetailpolicycode-查询用户策略详情)
  - [8.5 POST /user-policy/add 新增用户策略](#85-post-user-policyadd-新增用户策略)
  - [8.6 PUT /user-policy/update 修改用户策略](#86-put-user-policyupdate-修改用户策略)
  - [8.7 PUT /user-policy/status 更新用户策略状态](#87-put-user-policystatus-更新用户策略状态)
  - [8.8 DELETE /user-policy/delete 删除用户策略](#88-delete-user-policydelete-删除用户策略)
  - [8.9 POST /user-policy/batch 批量新增用户策略](#89-post-user-policybatch-批量新增用户策略)
  - [8.10 PUT /user-policy/batch 批量修改用户策略](#810-put-user-policybatch-批量修改用户策略)
  - [8.11 PUT /user-policy/status/batch 批量更新用户策略状态](#811-put-user-policystatusbatch-批量更新用户策略状态)
  - [8.12 DELETE /user-policy/batch 批量删除用户策略](#812-delete-user-policybatch-批量删除用户策略)
  - [8.13 POST /user-policy/bind 批量绑定用户到目标](#813-post-user-policybind-批量绑定用户到目标)
  - [8.14 POST /user-policy/unbind 批量解绑用户与目标](#814-post-user-policyunbind-批量解绑用户与目标)
- [9. 附录](#9-附录)

---

## 1. 测试环境说明

### 1.1 基础配置

| 配置项 | 值 |
|--------|-----|
| 基础URL | `http://localhost:8081/NexusIxService` |
| 认证方式 | Header `NexusIX: {token}` |
| 内容类型 | `application/json` |
| 统一成功响应 | `{ "code": 200, "msg": "success", "data": ... }` |
| 统一错误响应 | `{ "code": 500, "msg": "错误描述", "data": null }` |

### 1.2 技术栈

| 技术栈 | 版本 |
|--------|------|
| Spring Boot | 3.3.4 |
| MyBatis-Plus | 3.5.12 |
| Sa-Token | 最新版本 |
| PostgreSQL | 主数据库，JSONB存储field_operation |
| Redis | Session缓存 |

### 1.3 枚举值速查

| 枚举类 | 枚举值 | 说明 |
|--------|--------|------|
| UserStatus | ENABLED | 启用 |
| UserStatus | DISABLED | 停用 |
| UserStatus | LOCKED | 锁定 |
| RoleStatus | ENABLED | 启用 |
| RoleStatus | DISABLED | 停用 |
| PermStatus | ENABLED | 启用 |
| PermStatus | DISABLED | 冻结 |
| PolicyStatus | ACTIVE | 激活 |
| PolicyStatus | DISABLED | 禁用 |
| Deleted | NOT_DELETED | 未删除 |
| Deleted | DELETED | 已删除 |
| TargetType(权限策略) | TENANT / DEPT / ROLE / USER | 目标类型 |
| TargetType(用户策略) | TENANT / DEPT / ROLE | 目标类型 |
| PermType | MENU / BUTTON / API / DATA | 权限类型 |
| DataScope | ALL / DEPT_AND_SUB / DEPT / SELF | 数据范围 |

### 1.4 分页参数规范（PageCommonRTO）

| 参数名 | 类型 | 是否必填 | 约束条件 | 默认值 | 说明 |
|--------|------|---------|---------|--------|------|
| pageNum | Integer | 否 | 最小为1 | 1 | 当前页码 |
| pageSize | Integer | 否 | 最小为1，最大为100 | 10 | 每页数量 |

---

## 2. 测试数据准备

### 2.1 测试用户

| 用户名 | userCode | id | 说明 | 密码 |
|--------|----------|----|----|------|
| test_all | TEST_ALL | 7301 | 权限测试专用用户，覆盖4级权限体系 | $2a$10$dummyhash |
| 张三 | - | 2 | 普通用户 | - |
| 李四 | - | 3 | 普通用户 | - |
| 王五 | - | 4 | 普通用户 | - |
| 赵六 | - | 5 | 普通用户 | - |

### 2.2 租户/部门/角色数据

| 类型 | 编码 | id | 名称 | 说明 |
|------|------|----|------|------|
| 租户 | TENANT_A | 7001 | 租户A | test_all主租户(is_primary=true) |
| 租户 | TENANT_B | 7002 | 租户B | test_all辅租户 |
| 租户 | TENANT_C | 7003 | 租户C | test_all辅租户 |
| 部门 | DEPT_RD_A | 75001 | 研发部(租户A) | test_all主部门 |
| 部门 | DEPT_MKT_A | 75002 | 市场部(租户A) | - |
| 部门 | DEPT_SALES_A | 75007 | 销售部(租户A) | - |
| 部门 | DEPT_MKT_B | 75003 | 市场部(租户B) | test_all主部门 |
| 部门 | DEPT_SALES_B | 75004 | 销售部(租户B) | - |
| 部门 | DEPT_RD_B | 75008 | 研发部(租户B) | - |
| 部门 | DEPT_SALES_C | 75006 | 销售部(租户C) | test_all主部门 |
| 部门 | DEPT_RD_C | 75005 | 研发部(租户C) | - |
| 部门 | DEPT_MKT_C | 75010 | 市场部(租户C) | - |
| 角色 | ROLE_ADMIN | 7201 | 管理员 | 系统级角色 |
| 角色 | ROLE_EMPLOYEE | 7202 | 普通员工 | 系统级角色 |
| 角色 | ROLE_AUDITOR | 7203 | 审计员 | 系统级角色 |
| 角色 | - | 76001 | 管理员(租户A) | - |
| 角色 | - | 76002 | 普通员工(租户A) | - |
| 角色 | - | 76003 | 普通员工(租户B) | - |
| 角色 | - | 76004 | 审计员(租户B) | - |
| 角色 | - | 76005 | 管理员(租户C) | - |
| 角色 | - | 76006 | 审计员(租户C) | - |
| 角色 | - | 76007 | 审计员(租户A) | - |
| 角色 | - | 76008 | 管理员(租户B) | - |
| 角色 | - | 76009 | 普通员工(租户C) | - |

### 2.3 权限数据

| 权限编码 | id | 权限名称 | 权限类型 |
|----------|----|---------|---------|
| MENU_DASHBOARD | 7401 | 仪表盘 | MENU |
| MENU_SYSTEM | 7402 | 系统管理 | MENU |
| MENU_BUSINESS | 7403 | 业务中心 | MENU |
| MENU_REPORT | 7441 | 报表中心 | MENU |
| MENU_MONITOR | 7442 | 系统监控 | MENU |
| DATA_USER | 7431 | 用户数据 | DATA |
| DATA_ORDER | 7432 | 订单数据 | DATA |
| DATA_PRODUCT | 7451 | 产品数据 | DATA |
| DATA_LOG | 7452 | 日志数据 | DATA |

### 2.4 权限策略数据（test_all相关）

| 策略编码 | id | 目标类型 | 目标ID | 权限ID | 字段配置 | 状态 |
|----------|----|---------|--------|--------|---------|------|
| PP_TENANT_A_DASH | 80001 | TENANT | 7001 | 7401 | {} | ACTIVE |
| PP_TENANT_A_DATA | 80002 | TENANT | 7001 | 7431 | sys_user:{email:[R],user_name:[R,U]} | ACTIVE |
| PP_TENANT_A_REPORT | 80003 | TENANT | 7001 | 7441 | {} | ACTIVE |
| PP_TENANT_A_PRODUCT | 80004 | TENANT | 7001 | 7451 | product:{name:[R],price:[R,U]} | ACTIVE |
| PP_DEPT_RD_A_BTN | 81001 | DEPT | 75001 | 80002 | {} | ACTIVE |
| PP_DEPT_RD_A_DATA | 81002 | DEPT | 75001 | 80002 | sys_user:{phone:[R]} | ACTIVE |
| PP_ROLE_ADMIN_A_BTN | 82001 | ROLE | 76001 | 80002 | {} | ACTIVE |
| PP_USER_A_BTN | 83001 | USER | 77001 | 80002 | {} | ACTIVE |
| PP_USER_A_DATA | 83002 | USER | 77001 | 80002 | sys_user:{email:[R]} | ACTIVE |

### 2.5 用户策略数据（test_all相关）

| 策略编码 | id | 目标类型 | 目标ID | 是否主 | 状态 |
|----------|----|---------|--------|--------|------|
| UP_TENANT_A | 77001 | TENANT | 7001 | true | ACTIVE |
| UP_TENANT_B | 77002 | TENANT | 7002 | false | ACTIVE |
| UP_TENANT_C | 77003 | TENANT | 7003 | false | ACTIVE |
| UP_DEPT_RD_A | 77101 | DEPT | 75001 | true | ACTIVE |
| UP_DEPT_MKT_A | 77102 | DEPT | 75002 | false | ACTIVE |
| UP_ROLE_ADMIN_A | 77201 | ROLE | 76001 | - | ACTIVE |

### 2.6 获取测试Token

执行以下登录请求获取Token，后续所有接口请求头需携带 `NexusIX: {token}`：

```http
POST http://localhost:8081/NexusIxService/auth/login
Content-Type: application/json

{
  "username": "test_all",
  "password": "$2a$10$dummyhash"
}
```

---

## 3. AuthController 认证管理（1个接口）

### 3.1 POST /auth/login 用户登录认证

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /auth/login |
| HTTP方法 | POST |
| 接口描述 | 用户登录认证，返回token及用户上下文信息 |
| 是否需要认证 | 否 |
| 权限要求 | 无 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| Content-Type | String | 是 | 内容类型 | application/json |

##### 请求体 (Body)

| 参数名 | 类型 | 是否必填 | 约束条件 | 说明 | 示例值 |
|--------|------|---------|---------|------|--------|
| username | String | 是 | @NotBlank 不为空 | 用户名 | test_all |
| password | String | 是 | @NotBlank 不为空 | 密码 | $2a$10$dummyhash |

##### 请求示例

```json
{
  "username": "test_all",
  "password": "$2a$10$dummyhash"
}
```

```bash
curl -X POST 'http://localhost:8081/NexusIxService/auth/login' \
  -H 'Content-Type: application/json' \
  -d '{
    "username": "test_all",
    "password": "$2a$10$dummyhash"
  }'
```

#### 业务场景测试用例

##### 测试用例 TC-3.1.1: 正常登录（test_all用户）

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证test_all用户正常登录，返回完整UserContextDTO结构 |
| 前置条件 | test_all用户存在(id=7301)且状态为ENABLED，所有策略为ACTIVE基线状态 |
| 操作步骤 | 1. 发送登录请求 2. 验证响应结构 3. 验证token可用性 |
| 请求参数 | `{"username":"test_all","password":"$2a$10$dummyhash"}` |
| 预期结果 | code=200，data包含token字符串；data.permInfo.current.enabled包含4个权限(MENU_DASHBOARD/DATA_USER/MENU_REPORT/DATA_PRODUCT)；data.permInfo.current.disabled为空数组；data.permInfo.disabledDetailByTenant包含TENANT_A/B/C三个租户且每个租户有完整5个列表(system/tenant/dept/role/user)；data.permInfo.fieldPermissionByTenant包含字段级权限配置；data.userInfo包含用户基本信息；data.tenantInfo/deptInfo/roleInfo/permInfo结构完整 |
| 验证SQL | `SELECT id, user_name, status FROM sys_user WHERE id = 7301;` 预期status=ENABLED |

##### 测试用例 TC-3.1.2: 用户名不存在

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证使用不存在的用户名登录时返回错误 |
| 前置条件 | 数据库中不存在用户名"nonexistent_user" |
| 操作步骤 | 1. 发送登录请求使用不存在的用户名 |
| 请求参数 | `{"username":"nonexistent_user","password":"anypassword"}` |
| 预期结果 | code=500，msg包含用户不存在的错误描述，data=null |
| 验证SQL | `SELECT COUNT(*) FROM sys_user WHERE user_name = 'nonexistent_user' AND is_deleted = 'NOT_DELETED';` 预期返回0 |

##### 测试用例 TC-3.1.3: 密码错误

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证密码错误时登录失败 |
| 前置条件 | test_all用户存在且状态为ENABLED |
| 操作步骤 | 1. 发送登录请求使用正确用户名和错误密码 |
| 请求参数 | `{"username":"test_all","password":"wrong_password"}` |
| 预期结果 | code=500，msg包含密码错误的描述，data=null |
| 验证SQL | 无 |

##### 测试用例 TC-3.1.4: 用户状态为DISABLED

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证状态为DISABLED的用户无法登录 |
| 前置条件 | 将test_all用户状态改为DISABLED：`UPDATE sys_user SET status='DISABLED' WHERE id=7301;` |
| 操作步骤 | 1. 修改用户状态为DISABLED 2. 发送登录请求 3. 恢复用户状态 |
| 请求参数 | `{"username":"test_all","password":"$2a$10$dummyhash"}` |
| 预期结果 | code=500，msg包含用户已禁用的描述，data=null |
| 验证SQL | `SELECT status FROM sys_user WHERE id = 7301;` 预期status=DISABLED；测试后恢复：`UPDATE sys_user SET status='ENABLED' WHERE id=7301;` |

##### 测试用例 TC-3.1.5: 用户状态为LOCKED

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证状态为LOCKED的用户无法登录 |
| 前置条件 | 将test_all用户状态改为LOCKED：`UPDATE sys_user SET status='LOCKED' WHERE id=7301;` |
| 操作步骤 | 1. 修改用户状态为LOCKED 2. 发送登录请求 3. 恢复用户状态 |
| 请求参数 | `{"username":"test_all","password":"$2a$10$dummyhash"}` |
| 预期结果 | code=500，msg包含用户已锁定的描述，data=null |
| 验证SQL | `SELECT status FROM sys_user WHERE id = 7301;` 预期status=LOCKED；测试后恢复：`UPDATE sys_user SET status='ENABLED' WHERE id=7301;` |

##### 测试用例 TC-3.1.6: 验证登录响应UserContextDTO完整结构

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证登录响应包含完整的UserContextDTO结构，包括userInfo/tenantInfo/deptInfo/roleInfo/permInfo |
| 前置条件 | test_all用户存在，所有策略为ACTIVE基线状态 |
| 操作步骤 | 1. 发送登录请求 2. 逐项验证响应结构 |
| 请求参数 | `{"username":"test_all","password":"$2a$10$dummyhash"}` |
| 预期结果 | code=200。验证以下结构：1)data.token为非空字符串; 2)data.userInfo包含id/userCode/userName等用户基本信息; 3)data.tenantInfo包含当前租户信息(TENANT_A); 4)data.deptInfo包含部门信息; 5)data.roleInfo包含角色信息; 6)data.permInfo.current.enabled为启用权限数组; 7)data.permInfo.current.disabled为禁用权限数组; 8)data.permInfo.disabledDetailByTenant包含TENANT_A/B/C三个租户，每个租户有system/tenant/dept/role/user五个数组; 9)data.permInfo.fieldPermissionByTenant包含字段级权限配置 |
| 验证SQL | 无 |

##### 测试用例 TC-3.1.7: 验证permInfo.current.enabled/disabled正确分类

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证禁用策略后，权限从enabled移动到disabled列表 |
| 前置条件 | 禁用策略80001(PP_TENANT_A_DASH)：`UPDATE sys_perm_policy SET status='DISABLED' WHERE id=80001;` |
| 操作步骤 | 1. 禁用策略80001 2. 发送登录请求 3. 验证enabled不包含MENU_DASHBOARD，disabled包含MENU_DASHBOARD 4. 恢复策略 |
| 请求参数 | `{"username":"test_all","password":"$2a$10$dummyhash"}` |
| 预期结果 | data.permInfo.current.enabled包含3个权限(DATA_USER/MENU_REPORT/DATA_PRODUCT)，不含MENU_DASHBOARD；data.permInfo.current.disabled包含1个元素(permCode=MENU_DASHBOARD, permPolicyStatus=DISABLED) |
| 验证SQL | `SELECT id, status FROM sys_perm_policy WHERE id = 80001;` 预期status=DISABLED；恢复：`UPDATE sys_perm_policy SET status='ACTIVE' WHERE id=80001;` |

##### 测试用例 TC-3.1.8: 验证disabledDetailByTenant结构完整（5个列表）

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证disabledDetailByTenant包含所有3个租户，每个租户有system/tenant/dept/role/user五个完整列表 |
| 前置条件 | 所有策略为ACTIVE基线状态 |
| 操作步骤 | 1. 发送登录请求 2. 验证disabledDetailByTenant结构 |
| 请求参数 | `{"username":"test_all","password":"$2a$10$dummyhash"}` |
| 预期结果 | data.permInfo.disabledDetailByTenant包含3个key(TENANT_A/TENANT_B/TENANT_C)；每个租户对象包含5个数组字段：system(始终为空数组,SYSTEM级预留)、tenant、dept、role、user；基线状态下所有数组均为空 |
| 验证SQL | 无 |

##### 测试用例 TC-3.1.9: 验证fieldPermissionByTenant字段级权限

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证fieldPermissionByTenant正确返回字段级权限配置（operable/inoperable） |
| 前置条件 | 所有策略为ACTIVE基线状态，策略80002配置了sys_user表的email/user_name字段 |
| 操作步骤 | 1. 发送登录请求 2. 验证fieldPermissionByTenant结构 |
| 请求参数 | `{"username":"test_all","password":"$2a$10$dummyhash"}` |
| 预期结果 | data.permInfo.fieldPermissionByTenant.TENANT_A包含query/create/update三个操作类型；query.sys_user.operable包含email和user_name；update.sys_user.operable包含user_name |
| 验证SQL | `SELECT field_operation FROM sys_perm_policy WHERE id = 80002;` 预期返回JSON含email和user_name配置 |

##### 测试用例 TC-3.1.10: 验证token可用于后续请求

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证登录返回的token可用于后续接口请求的NexusIX请求头 |
| 前置条件 | test_all用户存在且状态为ENABLED |
| 操作步骤 | 1. 发送登录请求获取token 2. 使用token调用GET /user/list接口 |
| 请求参数 | 登录：`{"username":"test_all","password":"$2a$10$dummyhash"}`；后续请求Header: `NexusIX: {token}` |
| 预期结果 | 登录返回token非空；使用该token调用/user/list返回code=200且data为用户列表数组 |
| 验证SQL | 无 |

##### 测试用例 TC-3.1.11: 用户名为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证username为空时返回参数校验错误 |
| 前置条件 | 无 |
| 操作步骤 | 1. 发送登录请求，username为空字符串 |
| 请求参数 | `{"username":"","password":"$2a$10$dummyhash"}` |
| 预期结果 | code=500，msg包含"用户名不能为空"的描述，data=null |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 50 / 100 / 200 |
| 持续时间 | 60s / 120s / 60s |
| 加压策略 | 阶梯加压：每30秒增加50个并发用户 |
| 思考时间 | 100ms-500ms随机 |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-3.1.1 | 正常负载-多用户登录 | 50 | 60s | ≥100 | ≤300ms | ≤0.1% |
| PT-3.1.2 | 高负载-多用户登录 | 100 | 120s | ≥150 | ≤500ms | ≤1% |
| PT-3.1.3 | 峰值负载-多用户登录 | 200 | 60s | ≥200 | ≤1000ms | ≤5% |

##### 压测监控指标

- TPS（每秒事务数）
- 平均响应时间 / P95 / P99响应时间
- 错误率
- 服务器CPU / 内存 / 网络IO
- 数据库连接数 / QPS
- Redis命中率（Session写入）

##### Apifox压测设置说明

1. 在Apifox中创建测试场景"IAM-Login-压测"
2. 添加POST /auth/login接口请求，参数化username和password（使用CSV数据源）
3. 设置断言：响应code=200，响应时间<1000ms
4. 配置并发设置：阶梯加压模式
5. 启动压测，监控实时TPS和响应时间曲线

---

## 4. SysUserController 用户管理（12个接口）

### 4.1 GET /user/list 查询用户列表

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /user/list |
| HTTP方法 | GET |
| 接口描述 | 返回所有用户列表 |
| 是否需要认证 | 是 |
| 权限要求 | 用户查看权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |

##### 请求示例

```bash
curl -X GET 'http://localhost:8081/NexusIxService/user/list' \
  -H 'NexusIX: abc123def456'
```

#### 业务场景测试用例

##### 测试用例 TC-4.1.1: 正常查询用户列表

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证已认证用户可成功获取用户列表 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 携带token发送GET /user/list请求 |
| 请求参数 | 无（仅Header） |
| 预期结果 | code=200，data为用户数组，数组长度约31条，包含test_all(id=7301)等用户记录，每条记录包含userCode/userName/nickName/status等字段 |
| 验证SQL | `SELECT COUNT(*) FROM sys_user WHERE is_deleted = 'NOT_DELETED';` |

##### 测试用例 TC-4.1.2: 未认证请求

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证未携带token时请求被拒绝 |
| 前置条件 | 不携带NexusIX请求头 |
| 操作步骤 | 1. 不带token发送GET /user/list请求 |
| 请求参数 | 无Header |
| 预期结果 | code=500或401，msg包含未认证/无权限的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-4.1.3: 无效token请求

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证携带无效token时请求被拒绝 |
| 前置条件 | 携带过期或无效的token |
| 操作步骤 | 1. 携带无效token发送GET /user/list请求 |
| 请求参数 | Header: `NexusIX: invalid_token_xxx` |
| 预期结果 | code=500或401，msg包含token无效的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 50 / 100 / 200 |
| 持续时间 | 60s / 120s / 60s |
| 思考时间 | 100ms-500ms随机 |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-4.1.1 | 正常负载 | 50 | 60s | ≥150 | ≤200ms | ≤0.1% |
| PT-4.1.2 | 高负载 | 100 | 120s | ≥200 | ≤500ms | ≤1% |
| PT-4.1.3 | 峰值负载 | 200 | 60s | ≥250 | ≤1000ms | ≤5% |

##### Apifox压测设置说明

1. 创建测试场景，添加GET /user/list请求
2. 设置NexusIX请求头参数化（使用环境变量token）
3. 设置断言：code=200，响应时间<1000ms
4. 并发模式：固定并发数

---

### 4.2 GET /user/page 分页查询用户列表

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /user/page |
| HTTP方法 | GET |
| 接口描述 | 返回分页后的用户列表 |
| 是否需要认证 | 是 |
| 权限要求 | 用户查看权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |

##### 查询参数 (Query)

| 参数名 | 类型 | 是否必填 | 约束条件 | 说明 | 示例值 |
|--------|------|---------|---------|------|--------|
| pageNum | Integer | 否 | @Min(1)，默认1 | 当前页码 | 1 |
| pageSize | Integer | 否 | @Min(1)@Max(100)，默认10 | 每页数量 | 10 |

##### 请求示例

```bash
curl -X GET 'http://localhost:8081/NexusIxService/user/page?pageNum=1&pageSize=10' \
  -H 'NexusIX: abc123def456'
```

#### 业务场景测试用例

##### 测试用例 TC-4.2.1: 正常分页查询

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证默认分页参数查询用户列表 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送GET /user/page?pageNum=1&pageSize=10 |
| 请求参数 | pageNum=1, pageSize=10 |
| 预期结果 | code=200，data为IPage对象，包含records(当前页数据)、total(总记录数)、size(每页大小)、current(当前页)、pages(总页数)；records长度≤10 |
| 验证SQL | `SELECT COUNT(*) FROM sys_user WHERE is_deleted = 'NOT_DELETED';` 与total字段一致 |

##### 测试用例 TC-4.2.2: 第二页查询

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证翻页查询第二页数据 |
| 前置条件 | 用户总数>10 |
| 操作步骤 | 1. 发送GET /user/page?pageNum=2&pageSize=10 |
| 请求参数 | pageNum=2, pageSize=10 |
| 预期结果 | code=200，data.current=2，data.records为第二页数据，与第一页数据不重复 |
| 验证SQL | 无 |

##### 测试用例 TC-4.2.3: 边界值-pageSize为1

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证pageSize最小值边界 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送GET /user/page?pageNum=1&pageSize=1 |
| 请求参数 | pageNum=1, pageSize=1 |
| 预期结果 | code=200，data.records长度=1，data.size=1 |
| 验证SQL | 无 |

##### 测试用例 TC-4.2.4: 边界值-pageSize为100

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证pageSize最大值边界 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送GET /user/page?pageNum=1&pageSize=100 |
| 请求参数 | pageNum=1, pageSize=100 |
| 预期结果 | code=200，data.records长度≤100，data.size=100 |
| 验证SQL | 无 |

##### 测试用例 TC-4.2.5: 边界值-pageSize超过100

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证pageSize超过最大值100时的校验 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送GET /user/page?pageNum=1&pageSize=101 |
| 请求参数 | pageNum=1, pageSize=101 |
| 预期结果 | code=500，msg包含"每页数量最大为100"的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 50 / 100 / 200 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-4.2.1 | 正常负载 | 50 | 60s | ≥150 | ≤200ms | ≤0.1% |
| PT-4.2.2 | 高负载 | 100 | 120s | ≥200 | ≤500ms | ≤1% |
| PT-4.2.3 | 峰值负载 | 200 | 60s | ≥250 | ≤1000ms | ≤5% |

##### Apifox压测设置说明

1. 创建测试场景，添加GET /user/page请求
2. 参数化pageNum和pageSize（随机1-10页，pageSize固定10）
3. 设置断言：code=200，data.records非空

---

### 4.3 POST /user/query 条件查询用户列表

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /user/query |
| HTTP方法 | POST |
| 接口描述 | 返回满足条件的用户分页列表 |
| 是否需要认证 | 是 |
| 权限要求 | 用户查看权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |
| Content-Type | String | 是 | 内容类型 | application/json |

##### 请求体 (Body)

| 参数名 | 类型 | 是否必填 | 约束条件 | 说明 | 示例值 |
|--------|------|---------|---------|------|--------|
| pageNum | Integer | 否 | @Min(1)，默认1 | 当前页码 | 1 |
| pageSize | Integer | 否 | @Min(1)@Max(100)，默认10 | 每页数量 | 10 |
| userCode | String | 否 | - | 用户编码 | TEST_ALL |
| userName | String | 否 | - | 用户名 | test_all |
| nickName | String | 否 | - | 昵称 | - |
| realName | String | 否 | - | 真实姓名 | - |
| email | String | 否 | - | 电子邮箱 | - |
| phone | String | 否 | - | 手机号码 | - |
| gender | String | 否 | - | 性别 | MALE |
| status | String | 否 | - | 状态 | ENABLED |
| createByCode | String | 否 | - | 创建人编码 | - |
| createByName | String | 否 | - | 创建人姓名 | - |
| createTimeRange | Object | 否 | - | 创建时间范围 | {"startTime":"...","endTime":"..."} |
| updateByCode | String | 否 | - | 更新人编码 | - |
| updateByName | String | 否 | - | 更新人姓名 | - |
| updateTimeRange | Object | 否 | - | 更新时间范围 | - |
| isDeleted | String | 否 | - | 逻辑删除 | NOT_DELETED |

##### 请求示例

```json
{
  "userCode": "TEST_ALL",
  "pageNum": 1,
  "pageSize": 10
}
```

```bash
curl -X POST 'http://localhost:8081/NexusIxService/user/query' \
  -H 'NexusIX: abc123def456' \
  -H 'Content-Type: application/json' \
  -d '{
    "userCode": "TEST_ALL",
    "pageNum": 1,
    "pageSize": 10
  }'
```

#### 业务场景测试用例

##### 测试用例 TC-4.3.1: 精确查询用户编码

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证通过userCode精确查询用户 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /user/query请求，userCode=TEST_ALL |
| 请求参数 | `{"userCode":"TEST_ALL","pageNum":1,"pageSize":10}` |
| 预期结果 | code=200，data.total=1，data.records包含1条记录，userCode=TEST_ALL，userName=test_all |
| 验证SQL | `SELECT id, user_code, user_name FROM sys_user WHERE user_code = 'TEST_ALL' AND is_deleted = 'NOT_DELETED';` |

##### 测试用例 TC-4.3.2: 按状态查询

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证通过status条件查询启用状态的用户 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /user/query请求，status=ENABLED |
| 请求参数 | `{"status":"ENABLED","pageNum":1,"pageSize":10}` |
| 预期结果 | code=200，data.records中所有用户status均为ENABLED |
| 验证SQL | `SELECT COUNT(*) FROM sys_user WHERE status = 'ENABLED' AND is_deleted = 'NOT_DELETED';` |

##### 测试用例 TC-4.3.3: 多条件组合查询

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证多个条件组合查询 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /user/query请求，同时指定status和gender |
| 请求参数 | `{"status":"ENABLED","gender":"MALE","pageNum":1,"pageSize":10}` |
| 预期结果 | code=200，data.records中所有用户同时满足status=ENABLED和gender=MALE |
| 验证SQL | `SELECT COUNT(*) FROM sys_user WHERE status = 'ENABLED' AND gender = 'MALE' AND is_deleted = 'NOT_DELETED';` |

##### 测试用例 TC-4.3.4: 时间范围查询

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证通过创建时间范围查询用户 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /user/query请求，指定createTimeRange |
| 请求参数 | `{"createTimeRange":{"startTime":"2026-01-01 00:00:00","endTime":"2026-12-31 23:59:59"},"pageNum":1,"pageSize":10}` |
| 预期结果 | code=200，data.records中所有用户创建时间在2026年内 |
| 验证SQL | `SELECT COUNT(*) FROM sys_user WHERE create_at BETWEEN '2026-01-01' AND '2026-12-31' AND is_deleted = 'NOT_DELETED';` |

##### 测试用例 TC-4.3.5: 空条件查询（全量分页）

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证不传任何条件时返回全量分页数据 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /user/query请求，仅传分页参数 |
| 请求参数 | `{"pageNum":1,"pageSize":10}` |
| 预期结果 | code=200，data.total约31，data.records长度≤10 |
| 验证SQL | `SELECT COUNT(*) FROM sys_user WHERE is_deleted = 'NOT_DELETED';` |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 50 / 100 / 200 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-4.3.1 | 正常负载-条件查询 | 50 | 60s | ≥120 | ≤300ms | ≤0.1% |
| PT-4.3.2 | 高负载-条件查询 | 100 | 120s | ≥150 | ≤500ms | ≤1% |
| PT-4.3.3 | 峰值负载-条件查询 | 200 | 60s | ≥180 | ≤1000ms | ≤5% |

##### Apifox压测设置说明

1. 创建测试场景，添加POST /user/query请求
2. 参数化userCode（使用CSV数据源随机选择用户编码）
3. 设置断言：code=200，data.records非空

---

### 4.4 GET /user/detail/{userCode} 查询用户详情

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /user/detail/{userCode} |
| HTTP方法 | GET |
| 接口描述 | 返回指定用户的详情信息 |
| 是否需要认证 | 是 |
| 权限要求 | 用户查看权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |

##### 路径参数 (Path)

| 参数名 | 类型 | 是否必填 | 约束条件 | 说明 | 示例值 |
|--------|------|---------|---------|------|--------|
| userCode | String | 是 | @NotBlank 不为空 | 用户编码 | TEST_ALL |

##### 请求示例

```bash
curl -X GET 'http://localhost:8081/NexusIxService/user/detail/TEST_ALL' \
  -H 'NexusIX: abc123def456'
```

#### 业务场景测试用例

##### 测试用例 TC-4.4.1: 正常查询用户详情

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证查询存在的用户详情 |
| 前置条件 | 已登录获取有效token，TEST_ALL用户存在 |
| 操作步骤 | 1. 发送GET /user/detail/TEST_ALL请求 |
| 请求参数 | 路径参数userCode=TEST_ALL |
| 预期结果 | code=200，data包含完整用户详情，id=7301，userCode=TEST_ALL，userName=test_all，status=ENABLED，包含基本信息字段（email/phone/gender等） |
| 验证SQL | `SELECT * FROM sys_user WHERE user_code = 'TEST_ALL' AND is_deleted = 'NOT_DELETED';` |

##### 测试用例 TC-4.4.2: 查询不存在的用户

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证查询不存在的userCode时的处理 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送GET /user/detail/NON_EXISTENT请求 |
| 请求参数 | 路径参数userCode=NON_EXISTENT |
| 预期结果 | code=500，msg包含用户不存在的描述，data=null |
| 验证SQL | `SELECT COUNT(*) FROM sys_user WHERE user_code = 'NON_EXISTENT' AND is_deleted = 'NOT_DELETED';` 预期返回0 |

##### 测试用例 TC-4.4.3: userCode为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证userCode为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送GET /user/detail/请求（userCode为空） |
| 请求参数 | 路径参数userCode为空 |
| 预期结果 | code=500，msg包含"用户编码不能为空"的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 50 / 100 / 200 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-4.4.1 | 正常负载 | 50 | 60s | ≥200 | ≤100ms | ≤0.1% |
| PT-4.4.2 | 高负载 | 100 | 120s | ≥250 | ≤300ms | ≤1% |
| PT-4.4.3 | 峰值负载 | 200 | 60s | ≥300 | ≤800ms | ≤5% |

##### Apifox压测设置说明

1. 创建测试场景，添加GET /user/detail/{userCode}请求
2. 参数化userCode（CSV数据源，随机选择10个用户编码）
3. 设置断言：code=200

---

### 4.5 POST /user/add 新增用户

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /user/add |
| HTTP方法 | POST |
| 接口描述 | 新增用户信息 |
| 是否需要认证 | 是 |
| 权限要求 | 用户管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |
| Content-Type | String | 是 | 内容类型 | application/json |

##### 请求体 (Body)

| 参数名 | 类型 | 是否必填 | 约束条件 | 说明 | 示例值 |
|--------|------|---------|---------|------|--------|
| userCode | String | 是 | @NotBlank 不为空 | 用户编码 | USER_TEST_001 |
| userName | String | 是 | @NotBlank @Size(3-20) | 用户名 | user_test_001 |
| nickName | String | 否 | @Size(max=50) | 昵称 | 测试用户 |
| realName | String | 否 | @Size(max=50) | 真实姓名 | 张三 |
| email | String | 否 | @Pattern(EMAIL格式) | 电子邮箱 | test001@test.com |
| phone | String | 否 | @Pattern(中国手机号) | 手机号码 | 13800000001 |
| password | String | 是 | @NotBlank @Pattern(字母+数字,6-20位) | 密码 | abc123456 |
| avatarUrl | String | 否 | - | 头像URL | - |
| gender | String | 否 | MALE/FEMALE/UNKNOWN | 性别 | MALE |
| birthday | Date | 否 | yyyy-MM-dd | 出生日期 | 1990-01-01 |
| status | Enum | 是 | @NotNull @EnumField(UserStatus) | 状态 | ENABLED |
| disableReason | String | 否 | @Size(max=200) | 禁用原因 | - |

##### 请求示例

```json
{
  "userCode": "USER_TEST_001",
  "userName": "user_test_001",
  "nickName": "测试用户",
  "email": "test001@test.com",
  "phone": "13800000001",
  "password": "abc123456",
  "gender": "MALE",
  "birthday": "1990-01-01",
  "status": "ENABLED"
}
```

```bash
curl -X POST 'http://localhost:8081/NexusIxService/user/add' \
  -H 'NexusIX: abc123def456' \
  -H 'Content-Type: application/json' \
  -d '{
    "userCode": "USER_TEST_001",
    "userName": "user_test_001",
    "nickName": "测试用户",
    "email": "test001@test.com",
    "phone": "13800000001",
    "password": "abc123456",
    "gender": "MALE",
    "status": "ENABLED"
  }'
```

#### 业务场景测试用例

##### 测试用例 TC-4.5.1: 正常新增用户

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证使用完整合法参数新增用户成功 |
| 前置条件 | 已登录获取有效token，USER_TEST_001不存在 |
| 操作步骤 | 1. 发送POST /user/add请求 2. 验证数据库新增记录 3. 清理测试数据 |
| 请求参数 | `{"userCode":"USER_TEST_001","userName":"user_test_001","nickName":"测试用户","email":"test001@test.com","phone":"13800000001","password":"abc123456","gender":"MALE","status":"ENABLED"}` |
| 预期结果 | code=200，data为受影响行数(1)；数据库新增一条用户记录，密码被BCrypt加密存储 |
| 验证SQL | `SELECT id, user_code, user_name, password FROM sys_user WHERE user_code = 'USER_TEST_001' AND is_deleted = 'NOT_DELETED';` 预期password不以明文存储；清理：`UPDATE sys_user SET is_deleted='DELETED' WHERE user_code='USER_TEST_001';` |

##### 测试用例 TC-4.5.2: 用户编码重复

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证新增已存在的userCode时返回错误 |
| 前置条件 | 已存在userCode=TEST_ALL的用户 |
| 操作步骤 | 1. 发送POST /user/add请求，userCode=TEST_ALL |
| 请求参数 | `{"userCode":"TEST_ALL","userName":"test_dup","password":"abc123456","status":"ENABLED"}` |
| 预期结果 | code=500，msg包含用户编码已存在的描述 |
| 验证SQL | `SELECT COUNT(*) FROM sys_user WHERE user_code = 'TEST_ALL' AND is_deleted = 'NOT_DELETED';` 预期返回1 |

##### 测试用例 TC-4.5.3: 必填参数为空-userCode

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证userCode为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /user/add请求，userCode为空 |
| 请求参数 | `{"userCode":"","userName":"test_user","password":"abc123456","status":"ENABLED"}` |
| 预期结果 | code=500，msg包含"用户编码不能为空"的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-4.5.4: 密码格式校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证密码不满足格式要求（必须包含字母和数字）时返回错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /user/add请求，password为纯数字 |
| 请求参数 | `{"userCode":"USER_PWD_TEST","userName":"user_pwd_test","password":"12345678","status":"ENABLED"}` |
| 预期结果 | code=500，msg包含"密码必须包含字母和数字"的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-4.5.5: 用户名长度校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证userName长度不足3位时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /user/add请求，userName为"ab"（2个字符） |
| 请求参数 | `{"userCode":"USER_LEN_TEST","userName":"ab","password":"abc123456","status":"ENABLED"}` |
| 预期结果 | code=500，msg包含"用户名必须在3-20字符之间"的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 50 / 100 / 200 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-4.5.1 | 正常负载-新增用户 | 50 | 60s | ≥80 | ≤300ms | ≤0.1% |
| PT-4.5.2 | 高负载-新增用户 | 100 | 120s | ≥100 | ≤500ms | ≤1% |
| PT-4.5.3 | 峰值负载-新增用户 | 200 | 60s | ≥120 | ≤1000ms | ≤5% |

##### Apifox压测设置说明

1. 创建测试场景，添加POST /user/add请求
2. 参数化userCode和userName（使用随机数生成器确保唯一）
3. 设置后置脚本：提取新增用户ID用于清理
4. 设置断言：code=200

---

### 4.6 PUT /user/update 修改用户

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /user/update |
| HTTP方法 | PUT |
| 接口描述 | 修改用户信息 |
| 是否需要认证 | 是 |
| 权限要求 | 用户管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |
| Content-Type | String | 是 | 内容类型 | application/json |

##### 请求体 (Body)

| 参数名 | 类型 | 是否必填 | 约束条件 | 说明 | 示例值 |
|--------|------|---------|---------|------|--------|
| userCode | String | 是 | @NotBlank | 用户编码（定位用户） | USER_TEST_001 |
| userName | String | 是 | @NotBlank @Size(3-20) | 用户名 | user_test_001 |
| nickName | String | 否 | @Size(max=50) | 昵称 | 测试用户-修改 |
| realName | String | 否 | @Size(max=50) | 真实姓名 | 张三 |
| email | String | 否 | @Pattern(EMAIL格式) | 电子邮箱 | - |
| phone | String | 否 | @Pattern(中国手机号) | 手机号码 | - |
| avatarUrl | String | 否 | - | 头像URL | - |
| gender | String | 否 | MALE/FEMALE/UNKNOWN | 性别 | - |
| birthday | Date | 否 | yyyy-MM-dd | 出生日期 | - |
| status | Enum | 是 | @NotNull @EnumField(UserStatus) | 状态 | ENABLED |
| disableReason | String | 否 | @Size(max=200) | 禁用原因 | - |

##### 请求示例

```json
{
  "userCode": "USER_TEST_001",
  "userName": "user_test_001",
  "nickName": "测试用户-修改",
  "email": "test001@test.com",
  "phone": "13800000002",
  "status": "ENABLED"
}
```

```bash
curl -X PUT 'http://localhost:8081/NexusIxService/user/update' \
  -H 'NexusIX: abc123def456' \
  -H 'Content-Type: application/json' \
  -d '{
    "userCode": "USER_TEST_001",
    "userName": "user_test_001",
    "nickName": "测试用户-修改",
    "status": "ENABLED"
  }'
```

#### 业务场景测试用例

##### 测试用例 TC-4.6.1: 正常修改用户信息

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证修改用户nickName成功 |
| 前置条件 | 已新增USER_TEST_001用户 |
| 操作步骤 | 1. 先新增用户USER_TEST_001 2. 发送PUT /user/update修改nickName 3. 验证数据库 4. 清理 |
| 请求参数 | `{"userCode":"USER_TEST_001","userName":"user_test_001","nickName":"测试用户-修改","status":"ENABLED"}` |
| 预期结果 | code=200，data为受影响行数(1)；数据库中nickName已更新为"测试用户-修改" |
| 验证SQL | `SELECT nick_name FROM sys_user WHERE user_code = 'USER_TEST_001' AND is_deleted = 'NOT_DELETED';` 预期nick_name='测试用户-修改' |

##### 测试用例 TC-4.6.2: 修改不存在的用户

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证修改不存在的userCode时返回错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送PUT /user/update请求，userCode=NON_EXISTENT |
| 请求参数 | `{"userCode":"NON_EXISTENT","userName":"non_existent","status":"ENABLED"}` |
| 预期结果 | code=500，msg包含用户不存在的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-4.6.3: 修改用户状态为DISABLED

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证通过update接口修改用户状态为DISABLED |
| 前置条件 | 已新增USER_TEST_001用户 |
| 操作步骤 | 1. 发送PUT /user/update请求，status=DISABLED 2. 验证数据库 3. 清理 |
| 请求参数 | `{"userCode":"USER_TEST_001","userName":"user_test_001","status":"DISABLED","disableReason":"测试禁用"}` |
| 预期结果 | code=200，数据库status=DISABLED，disable_reason='测试禁用' |
| 验证SQL | `SELECT status, disable_reason FROM sys_user WHERE user_code = 'USER_TEST_001';` |

##### 测试用例 TC-4.6.4: 必填参数userCode为空

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证userCode为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送PUT /user/update请求，userCode为空 |
| 请求参数 | `{"userCode":"","userName":"test","status":"ENABLED"}` |
| 预期结果 | code=500，msg包含"用户编码不能为空"的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 50 / 100 / 200 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-4.6.1 | 正常负载-修改用户 | 50 | 60s | ≥100 | ≤200ms | ≤0.1% |
| PT-4.6.2 | 高负载-修改用户 | 100 | 120s | ≥120 | ≤500ms | ≤1% |
| PT-4.6.3 | 峰值负载-修改用户 | 200 | 60s | ≥150 | ≤1000ms | ≤5% |

##### Apifox压测设置说明

1. 创建测试场景，先调用POST /user/add准备测试数据
2. 添加PUT /user/update请求，参数化userCode
3. 设置断言：code=200

---

### 4.7 PUT /user/status 更新用户状态

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /user/status |
| HTTP方法 | PUT |
| 接口描述 | 更新指定用户的状态 |
| 是否需要认证 | 是 |
| 权限要求 | 用户管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |

##### 查询参数 (Query)

| 参数名 | 类型 | 是否必填 | 约束条件 | 说明 | 示例值 |
|--------|------|---------|---------|------|--------|
| id | String | 是 | @NotBlank | 用户ID | 7301 |
| status | String | 是 | @NotBlank | 用户状态(ENABLED/DISABLED/LOCKED) | DISABLED |

##### 请求示例

```bash
curl -X PUT 'http://localhost:8081/NexusIxService/user/status?id=7301&status=DISABLED' \
  -H 'NexusIX: abc123def456'
```

#### 业务场景测试用例

##### 测试用例 TC-4.7.1: 正常更新用户状态为DISABLED

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证将用户状态更新为DISABLED |
| 前置条件 | 已新增测试用户并获取其id |
| 操作步骤 | 1. 发送PUT /user/status?id={id}&status=DISABLED 2. 验证数据库 3. 恢复状态 |
| 请求参数 | id={测试用户id}, status=DISABLED |
| 预期结果 | code=200，data为受影响行数(1)；数据库status=DISABLED |
| 验证SQL | `SELECT status FROM sys_user WHERE id = {id};` 预期status=DISABLED |

##### 测试用例 TC-4.7.2: 正常更新用户状态为LOCKED

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证将用户状态更新为LOCKED |
| 前置条件 | 已新增测试用户 |
| 操作步骤 | 1. 发送PUT /user/status?id={id}&status=LOCKED 2. 验证数据库 3. 恢复 |
| 请求参数 | id={测试用户id}, status=LOCKED |
| 预期结果 | code=200，数据库status=LOCKED |
| 验证SQL | `SELECT status FROM sys_user WHERE id = {id};` |

##### 测试用例 TC-4.7.3: 更新不存在的用户状态

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证更新不存在用户的id时返回错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送PUT /user/status?id=999999&status=DISABLED |
| 请求参数 | id=999999, status=DISABLED |
| 预期结果 | code=500，msg包含用户不存在的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-4.7.4: id为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证id为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送PUT /user/status?status=DISABLED（不传id） |
| 请求参数 | 不传id参数 |
| 预期结果 | code=500，msg包含"Id不能为空"的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 50 / 100 / 200 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-4.7.1 | 正常负载 | 50 | 60s | ≥200 | ≤100ms | ≤0.1% |
| PT-4.7.2 | 高负载 | 100 | 120s | ≥250 | ≤300ms | ≤1% |
| PT-4.7.3 | 峰值负载 | 200 | 60s | ≥300 | ≤800ms | ≤5% |

##### Apifox压测设置说明

1. 创建测试场景，添加PUT /user/status请求
2. 参数化id（使用CSV数据源）
3. 设置断言：code=200

---

### 4.8 DELETE /user/delete 删除用户

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /user/delete |
| HTTP方法 | DELETE |
| 接口描述 | 删除用户信息（逻辑删除） |
| 是否需要认证 | 是 |
| 权限要求 | 用户管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |

##### 查询参数 (Query)

| 参数名 | 类型 | 是否必填 | 约束条件 | 说明 | 示例值 |
|--------|------|---------|---------|------|--------|
| id | String | 是 | @NotBlank | 用户ID | 7301 |

##### 请求示例

```bash
curl -X DELETE 'http://localhost:8081/NexusIxService/user/delete?id=7301' \
  -H 'NexusIX: abc123def456'
```

#### 业务场景测试用例

##### 测试用例 TC-4.8.1: 正常删除用户

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证删除已存在的用户（逻辑删除） |
| 前置条件 | 已新增测试用户USER_TEST_001并获取其id |
| 操作步骤 | 1. 发送DELETE /user/delete?id={id} 2. 验证数据库逻辑删除标记 |
| 请求参数 | id={测试用户id} |
| 预期结果 | code=200，data为受影响行数(1)；数据库is_deleted=DELETED |
| 验证SQL | `SELECT is_deleted FROM sys_user WHERE id = {id};` 预期is_deleted=DELETED |

##### 测试用例 TC-4.8.2: 删除不存在的用户

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证删除不存在的用户id时返回错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送DELETE /user/delete?id=999999 |
| 请求参数 | id=999999 |
| 预期结果 | code=500，msg包含用户不存在的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-4.8.3: id为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证id为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送DELETE /user/delete（不传id） |
| 请求参数 | 不传id参数 |
| 预期结果 | code=500，msg包含"Id不能为空"的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 50 / 100 / 200 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-4.8.1 | 正常负载 | 50 | 60s | ≥200 | ≤100ms | ≤0.1% |
| PT-4.8.2 | 高负载 | 100 | 120s | ≥250 | ≤300ms | ≤1% |
| PT-4.8.3 | 峰值负载 | 200 | 60s | ≥300 | ≤800ms | ≤5% |

##### Apifox压测设置说明

1. 创建测试场景，先批量新增测试用户
2. 添加DELETE /user/delete请求，参数化id
3. 设置断言：code=200

---

### 4.9 POST /user/batch 批量新增用户

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /user/batch |
| HTTP方法 | POST |
| 接口描述 | 批量新增用户信息 |
| 是否需要认证 | 是 |
| 权限要求 | 用户管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |
| Content-Type | String | 是 | 内容类型 | application/json |

##### 请求体 (Body)

请求体为 `List<SysUserAddRTO>` 数组，每个元素字段同[4.5 POST /user/add](#45-post-useradd-新增用户)请求体。集合不能为空（@NotEmpty）。

##### 请求示例

```json
[
  {
    "userCode": "USER_BATCH_1",
    "userName": "user_batch_1",
    "nickName": "批量用户1",
    "password": "abc123456",
    "status": "ENABLED"
  },
  {
    "userCode": "USER_BATCH_2",
    "userName": "user_batch_2",
    "nickName": "批量用户2",
    "password": "abc123456",
    "status": "ENABLED"
  }
]
```

```bash
curl -X POST 'http://localhost:8081/NexusIxService/user/batch' \
  -H 'NexusIX: abc123def456' \
  -H 'Content-Type: application/json' \
  -d '[
    {"userCode":"USER_BATCH_1","userName":"user_batch_1","password":"abc123456","status":"ENABLED"},
    {"userCode":"USER_BATCH_2","userName":"user_batch_2","password":"abc123456","status":"ENABLED"}
  ]'
```

#### 业务场景测试用例

##### 测试用例 TC-4.9.1: 正常批量新增用户

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证批量新增2个用户成功 |
| 前置条件 | 已登录获取有效token，USER_BATCH_1和USER_BATCH_2不存在 |
| 操作步骤 | 1. 发送POST /user/batch请求 2. 验证数据库 3. 清理 |
| 请求参数 | 包含2个用户的数组（见请求示例） |
| 预期结果 | code=200，data为受影响行数(2)；数据库新增2条用户记录 |
| 验证SQL | `SELECT COUNT(*) FROM sys_user WHERE user_code IN ('USER_BATCH_1','USER_BATCH_2') AND is_deleted='NOT_DELETED';` 预期返回2；清理：`UPDATE sys_user SET is_deleted='DELETED' WHERE user_code IN ('USER_BATCH_1','USER_BATCH_2');` |

##### 测试用例 TC-4.9.2: 空集合参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证传入空数组时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /user/batch请求，body为空数组[] |
| 请求参数 | `[]` |
| 预期结果 | code=500，msg包含集合不能为空的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-4.9.3: 批量新增含重复userCode

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证批量新增中包含已存在的userCode时处理 |
| 前置条件 | TEST_ALL用户已存在 |
| 操作步骤 | 1. 发送POST /user/batch请求，其中一个userCode=TEST_ALL |
| 请求参数 | `[{"userCode":"TEST_ALL","userName":"test","password":"abc123456","status":"ENABLED"},{"userCode":"USER_BATCH_NEW","userName":"user_new","password":"abc123456","status":"ENABLED"}]` |
| 预期结果 | code=500，msg包含用户编码已存在的描述；事务回滚，USER_BATCH_NEW也未创建 |
| 验证SQL | `SELECT COUNT(*) FROM sys_user WHERE user_code = 'USER_BATCH_NEW' AND is_deleted='NOT_DELETED';` 预期返回0 |

##### 测试用例 TC-4.9.4: 单条数据参数校验失败

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证批量数据中某条userCode为空时整体校验失败 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /user/batch请求，第一条userCode为空 |
| 请求参数 | `[{"userCode":"","userName":"test1","password":"abc123456","status":"ENABLED"},{"userCode":"USER_BATCH_OK","userName":"test2","password":"abc123456","status":"ENABLED"}]` |
| 预期结果 | code=500，msg包含"用户编码不能为空"的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 50 / 100 / 200 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-4.9.1 | 正常负载-批量新增5个 | 50 | 60s | ≥50 | ≤500ms | ≤0.1% |
| PT-4.9.2 | 高负载-批量新增5个 | 100 | 120s | ≥80 | ≤800ms | ≤1% |
| PT-4.9.3 | 峰值负载-批量新增5个 | 200 | 60s | ≥100 | ≤1500ms | ≤5% |

##### Apifox压测设置说明

1. 创建测试场景，添加POST /user/batch请求
2. 使用Apifox脚本生成5个随机用户数据
3. 设置后置脚本：提取新增用户ID用于清理
4. 设置断言：code=200

---

### 4.10 PUT /user/batch 批量修改用户

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /user/batch |
| HTTP方法 | PUT |
| 接口描述 | 批量修改用户信息 |
| 是否需要认证 | 是 |
| 权限要求 | 用户管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |
| Content-Type | String | 是 | 内容类型 | application/json |

##### 请求体 (Body)

请求体为 `List<SysUserUpdateRTO>` 数组，每个元素字段同[4.6 PUT /user/update](#46-put-userupdate-修改用户)请求体。

##### 请求示例

```json
[
  {
    "userCode": "USER_BATCH_1",
    "userName": "user_batch_1",
    "nickName": "批量用户1-修改",
    "status": "ENABLED"
  }
]
```

```bash
curl -X PUT 'http://localhost:8081/NexusIxService/user/batch' \
  -H 'NexusIX: abc123def456' \
  -H 'Content-Type: application/json' \
  -d '[{"userCode":"USER_BATCH_1","userName":"user_batch_1","nickName":"批量用户1-修改","status":"ENABLED"}]'
```

#### 业务场景测试用例

##### 测试用例 TC-4.10.1: 正常批量修改用户

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证批量修改已存在的用户信息成功 |
| 前置条件 | 已批量新增USER_BATCH_1和USER_BATCH_2用户 |
| 操作步骤 | 1. 发送PUT /user/batch请求修改nickName 2. 验证数据库 3. 清理 |
| 请求参数 | `[{"userCode":"USER_BATCH_1","userName":"user_batch_1","nickName":"批量用户1-修改","status":"ENABLED"}]` |
| 预期结果 | code=200，data为受影响行数；数据库USER_BATCH_1的nickName已更新 |
| 验证SQL | `SELECT nick_name FROM sys_user WHERE user_code='USER_BATCH_1' AND is_deleted='NOT_DELETED';` |

##### 测试用例 TC-4.10.2: 批量修改含不存在的用户

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证批量修改中包含不存在的userCode时返回错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送PUT /user/batch请求，userCode包含NON_EXISTENT |
| 请求参数 | `[{"userCode":"NON_EXISTENT","userName":"non","status":"ENABLED"}]` |
| 预期结果 | code=500，msg包含用户不存在的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-4.10.3: 批量修改参数校验失败

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证批量修改中某条userName为空时返回校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送PUT /user/batch请求，第一条userName为空 |
| 请求参数 | `[{"userCode":"USER_BATCH_1","userName":"","status":"ENABLED"}]` |
| 预期结果 | code=500，msg包含"用户名不能为空"的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 50 / 100 / 200 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-4.10.1 | 正常负载 | 50 | 60s | ≥80 | ≤300ms | ≤0.1% |
| PT-4.10.2 | 高负载 | 100 | 120s | ≥100 | ≤500ms | ≤1% |
| PT-4.10.3 | 峰值负载 | 200 | 60s | ≥120 | ≤1000ms | ≤5% |

##### Apifox压测设置说明

1. 创建测试场景，先批量新增测试数据
2. 添加PUT /user/batch请求，参数化userCode
3. 设置断言：code=200

---

### 4.11 PUT /user/status/batch 批量更新用户状态

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /user/status/batch |
| HTTP方法 | PUT |
| 接口描述 | 批量更新多个指定用户的状态 |
| 是否需要认证 | 是 |
| 权限要求 | 用户管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |
| Content-Type | String | 是 | 内容类型 | application/json |

##### 查询参数 (Query)

| 参数名 | 类型 | 是否必填 | 约束条件 | 说明 | 示例值 |
|--------|------|---------|---------|------|--------|
| status | String | 是 | @NotBlank | 用户状态 | DISABLED |

##### 请求体 (Body)

请求体为 `List<String>` 用户ID集合，不能为空（@NotEmpty）。

##### 请求示例

```bash
curl -X PUT 'http://localhost:8081/NexusIxService/user/status/batch?status=DISABLED' \
  -H 'NexusIX: abc123def456' \
  -H 'Content-Type: application/json' \
  -d '["7301", "7302"]'
```

#### 业务场景测试用例

##### 测试用例 TC-4.11.1: 正常批量更新用户状态

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证批量更新多个用户状态为DISABLED |
| 前置条件 | 已批量新增USER_BATCH_1和USER_BATCH_2并获取其id |
| 操作步骤 | 1. 发送PUT /user/status/batch?status=DISABLED 2. 验证数据库 3. 清理 |
| 请求参数 | status=DISABLED, body=["USER_BATCH_1的id", "USER_BATCH_2的id"] |
| 预期结果 | code=200，data为受影响行数；两个用户status均为DISABLED |
| 验证SQL | `SELECT status FROM sys_user WHERE id IN (id1, id2);` 预期均为DISABLED |

##### 测试用例 TC-4.11.2: 空集合参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证ids集合为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送PUT /user/status/batch?status=DISABLED，body为[] |
| 请求参数 | status=DISABLED, body=[] |
| 预期结果 | code=500，msg包含"用户ID集合不能为空"的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-4.11.3: status为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证status为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送PUT /user/status/batch（不传status） |
| 请求参数 | 不传status, body=["7301"] |
| 预期结果 | code=500，msg包含"状态不能为空"的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 50 / 100 / 200 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-4.11.1 | 正常负载 | 50 | 60s | ≥150 | ≤200ms | ≤0.1% |
| PT-4.11.2 | 高负载 | 100 | 120s | ≥180 | ≤500ms | ≤1% |
| PT-4.11.3 | 峰值负载 | 200 | 60s | ≥200 | ≤1000ms | ≤5% |

##### Apifox压测设置说明

1. 创建测试场景，添加PUT /user/status/batch请求
2. 参数化ids（使用CSV数据源批量ID）
3. 设置断言：code=200

---

### 4.12 DELETE /user/batch 批量删除用户

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /user/batch |
| HTTP方法 | DELETE |
| 接口描述 | 批量删除用户信息（逻辑删除） |
| 是否需要认证 | 是 |
| 权限要求 | 用户管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |
| Content-Type | String | 是 | 内容类型 | application/json |

##### 请求体 (Body)

请求体为 `List<String>` 用户ID集合，不能为空（@NotEmpty）。

##### 请求示例

```bash
curl -X DELETE 'http://localhost:8081/NexusIxService/user/batch' \
  -H 'NexusIX: abc123def456' \
  -H 'Content-Type: application/json' \
  -d '["7301", "7302"]'
```

#### 业务场景测试用例

##### 测试用例 TC-4.12.1: 正常批量删除用户

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证批量删除多个用户成功 |
| 前置条件 | 已批量新增USER_BATCH_1和USER_BATCH_2并获取其id |
| 操作步骤 | 1. 发送DELETE /user/batch请求 2. 验证数据库逻辑删除 |
| 请求参数 | body=["USER_BATCH_1的id", "USER_BATCH_2的id"] |
| 预期结果 | code=200，data为受影响行数；两个用户is_deleted=DELETED |
| 验证SQL | `SELECT is_deleted FROM sys_user WHERE id IN (id1, id2);` 预期均为DELETED |

##### 测试用例 TC-4.12.2: 空集合参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证ids集合为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送DELETE /user/batch，body为[] |
| 请求参数 | body=[] |
| 预期结果 | code=500，msg包含集合不能为空的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-4.12.3: 批量删除含不存在的id

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证批量删除中包含不存在的id时处理 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送DELETE /user/batch，body包含不存在的id |
| 请求参数 | body=["999999"] |
| 预期结果 | code=500或code=200但data=0，msg包含用户不存在的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 50 / 100 / 200 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-4.12.1 | 正常负载 | 50 | 60s | ≥150 | ≤200ms | ≤0.1% |
| PT-4.12.2 | 高负载 | 100 | 120s | ≥180 | ≤500ms | ≤1% |
| PT-4.12.3 | 峰值负载 | 200 | 60s | ≥200 | ≤1000ms | ≤5% |

##### Apifox压测设置说明

1. 创建测试场景，先批量新增测试数据
2. 添加DELETE /user/batch请求，参数化ids
3. 设置断言：code=200

---

## 5. SysRoleController 角色管理（12个接口）

### 5.1 GET /role/list 查询角色列表

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /role/list |
| HTTP方法 | GET |
| 接口描述 | 返回所有角色列表 |
| 是否需要认证 | 是 |
| 权限要求 | 角色查看权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |

##### 请求示例

```bash
curl -X GET 'http://localhost:8081/NexusIxService/role/list' \
  -H 'NexusIX: abc123def456'
```

#### 业务场景测试用例

##### 测试用例 TC-5.1.1: 正常查询角色列表

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证已认证用户可成功获取角色列表 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 携带token发送GET /role/list请求 |
| 请求参数 | 无（仅Header） |
| 预期结果 | code=200，data为角色数组，数组长度约32条，包含ROLE_ADMIN(7201)、ROLE_EMPLOYEE(7202)等角色记录，每条记录包含roleCode/roleName/status等字段 |
| 验证SQL | `SELECT COUNT(*) FROM sys_role WHERE is_deleted = 'NOT_DELETED';` |

##### 测试用例 TC-5.1.2: 未认证请求

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证未携带token时请求被拒绝 |
| 前置条件 | 不携带NexusIX请求头 |
| 操作步骤 | 1. 不带token发送GET /role/list请求 |
| 请求参数 | 无Header |
| 预期结果 | code=500或401，msg包含未认证/无权限的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-5.1.3: 数据可见范围测试

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证不同租户用户查询角色列表的数据隔离 |
| 前置条件 | 使用test_all用户登录（属于TENANT_A/B/C） |
| 操作步骤 | 1. 携带token发送GET /role/list请求 2. 验证返回的角色数据范围 |
| 请求参数 | 无（仅Header） |
| 预期结果 | code=200，data中的角色应为当前用户可见范围内的角色（含系统级角色和当前租户角色） |
| 验证SQL | `SELECT role_code, role_name FROM sys_role WHERE is_deleted = 'NOT_DELETED' ORDER BY id;` |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 50 / 100 / 200 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-5.1.1 | 正常负载 | 50 | 60s | ≥150 | ≤200ms | ≤0.1% |
| PT-5.1.2 | 高负载 | 100 | 120s | ≥200 | ≤500ms | ≤1% |
| PT-5.1.3 | 峰值负载 | 200 | 60s | ≥250 | ≤1000ms | ≤5% |

##### Apifox压测设置说明

1. 创建测试场景，添加GET /role/list请求
2. 设置NexusIX请求头参数化
3. 设置断言：code=200，data非空

---

### 5.2 GET /role/page 分页查询角色列表

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /role/page |
| HTTP方法 | GET |
| 接口描述 | 返回分页后的角色列表 |
| 是否需要认证 | 是 |
| 权限要求 | 角色查看权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |

##### 查询参数 (Query)

| 参数名 | 类型 | 是否必填 | 约束条件 | 说明 | 示例值 |
|--------|------|---------|---------|------|--------|
| pageNum | Integer | 否 | @Min(1)，默认1 | 当前页码 | 1 |
| pageSize | Integer | 否 | @Min(1)@Max(100)，默认10 | 每页数量 | 10 |

##### 请求示例

```bash
curl -X GET 'http://localhost:8081/NexusIxService/role/page?pageNum=1&pageSize=10' \
  -H 'NexusIX: abc123def456'
```

#### 业务场景测试用例

##### 测试用例 TC-5.2.1: 正常分页查询

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证默认分页参数查询角色列表 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送GET /role/page?pageNum=1&pageSize=10 |
| 请求参数 | pageNum=1, pageSize=10 |
| 预期结果 | code=200，data为IPage对象，records长度≤10，total约32 |
| 验证SQL | `SELECT COUNT(*) FROM sys_role WHERE is_deleted = 'NOT_DELETED';` |

##### 测试用例 TC-5.2.2: 边界值-pageSize为100

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证pageSize最大值边界 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送GET /role/page?pageNum=1&pageSize=100 |
| 请求参数 | pageNum=1, pageSize=100 |
| 预期结果 | code=200，data.records长度≤100 |
| 验证SQL | 无 |

##### 测试用例 TC-5.2.3: 边界值-pageSize超过100

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证pageSize超过最大值100时的校验 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送GET /role/page?pageNum=1&pageSize=101 |
| 请求参数 | pageNum=1, pageSize=101 |
| 预期结果 | code=500，msg包含"每页数量最大为100"的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 50 / 100 / 200 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-5.2.1 | 正常负载 | 50 | 60s | ≥150 | ≤200ms | ≤0.1% |
| PT-5.2.2 | 高负载 | 100 | 120s | ≥200 | ≤500ms | ≤1% |
| PT-5.2.3 | 峰值负载 | 200 | 60s | ≥250 | ≤1000ms | ≤5% |

##### Apifox压测设置说明

1. 创建测试场景，添加GET /role/page请求
2. 参数化pageNum和pageSize
3. 设置断言：code=200

---

### 5.3 POST /role/query 条件查询角色列表

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /role/query |
| HTTP方法 | POST |
| 接口描述 | 返回满足条件的角色分页列表 |
| 是否需要认证 | 是 |
| 权限要求 | 角色查看权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |
| Content-Type | String | 是 | 内容类型 | application/json |

##### 请求体 (Body)

| 参数名 | 类型 | 是否必填 | 约束条件 | 说明 | 示例值 |
|--------|------|---------|---------|------|--------|
| pageNum | Integer | 否 | @Min(1)，默认1 | 当前页码 | 1 |
| pageSize | Integer | 否 | @Min(1)@Max(100)，默认10 | 每页数量 | 10 |
| roleCode | String | 否 | - | 角色编码 | ROLE_ADMIN |
| roleName | String | 否 | - | 角色名称 | - |
| status | String | 否 | - | 状态 | ENABLED |
| createByCode | String | 否 | - | 创建人编码 | - |
| createByName | String | 否 | - | 创建人姓名 | - |
| createTimeRange | Object | 否 | - | 创建时间范围 | - |
| updateByCode | String | 否 | - | 更新人编码 | - |
| updateByName | String | 否 | - | 更新人姓名 | - |
| updateTimeRange | Object | 否 | - | 更新时间范围 | - |
| isDeleted | String | 否 | - | 逻辑删除 | NOT_DELETED |

##### 请求示例

```json
{
  "roleCode": "ROLE_ADMIN",
  "pageNum": 1,
  "pageSize": 10
}
```

```bash
curl -X POST 'http://localhost:8081/NexusIxService/role/query' \
  -H 'NexusIX: abc123def456' \
  -H 'Content-Type: application/json' \
  -d '{"roleCode":"ROLE_ADMIN","pageNum":1,"pageSize":10}'
```

#### 业务场景测试用例

##### 测试用例 TC-5.3.1: 精确查询角色编码

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证通过roleCode精确查询角色 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /role/query请求，roleCode=ROLE_ADMIN |
| 请求参数 | `{"roleCode":"ROLE_ADMIN","pageNum":1,"pageSize":10}` |
| 预期结果 | code=200，data.total=1，data.records包含1条记录，roleCode=ROLE_ADMIN，roleName=管理员 |
| 验证SQL | `SELECT id, role_code, role_name FROM sys_role WHERE role_code = 'ROLE_ADMIN' AND is_deleted='NOT_DELETED';` |

##### 测试用例 TC-5.3.2: 按状态查询

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证通过status条件查询启用状态的角色 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /role/query请求，status=ENABLED |
| 请求参数 | `{"status":"ENABLED","pageNum":1,"pageSize":10}` |
| 预期结果 | code=200，data.records中所有角色status均为ENABLED |
| 验证SQL | `SELECT COUNT(*) FROM sys_role WHERE status='ENABLED' AND is_deleted='NOT_DELETED';` |

##### 测试用例 TC-5.3.3: 空条件查询

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证不传任何条件时返回全量分页数据 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /role/query请求，仅传分页参数 |
| 请求参数 | `{"pageNum":1,"pageSize":10}` |
| 预期结果 | code=200，data.total约32 |
| 验证SQL | `SELECT COUNT(*) FROM sys_role WHERE is_deleted='NOT_DELETED';` |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 50 / 100 / 200 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-5.3.1 | 正常负载 | 50 | 60s | ≥120 | ≤300ms | ≤0.1% |
| PT-5.3.2 | 高负载 | 100 | 120s | ≥150 | ≤500ms | ≤1% |
| PT-5.3.3 | 峰值负载 | 200 | 60s | ≥180 | ≤1000ms | ≤5% |

##### Apifox压测设置说明

1. 创建测试场景，添加POST /role/query请求
2. 参数化roleCode（CSV数据源）
3. 设置断言：code=200

---

### 5.4 GET /role/detail/{roleCode} 查询角色详情

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /role/detail/{roleCode} |
| HTTP方法 | GET |
| 接口描述 | 返回指定角色的详情信息 |
| 是否需要认证 | 是 |
| 权限要求 | 角色查看权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |

##### 路径参数 (Path)

| 参数名 | 类型 | 是否必填 | 约束条件 | 说明 | 示例值 |
|--------|------|---------|---------|------|--------|
| roleCode | String | 是 | @NotBlank | 角色编码 | ROLE_ADMIN |

##### 请求示例

```bash
curl -X GET 'http://localhost:8081/NexusIxService/role/detail/ROLE_ADMIN' \
  -H 'NexusIX: abc123def456'
```

#### 业务场景测试用例

##### 测试用例 TC-5.4.1: 正常查询角色详情

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证查询存在的角色详情 |
| 前置条件 | 已登录获取有效token，ROLE_ADMIN角色存在 |
| 操作步骤 | 1. 发送GET /role/detail/ROLE_ADMIN请求 |
| 请求参数 | 路径参数roleCode=ROLE_ADMIN |
| 预期结果 | code=200，data包含完整角色详情，id=7201，roleCode=ROLE_ADMIN，roleName=管理员，status=ENABLED |
| 验证SQL | `SELECT * FROM sys_role WHERE role_code='ROLE_ADMIN' AND is_deleted='NOT_DELETED';` |

##### 测试用例 TC-5.4.2: 查询不存在的角色

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证查询不存在的roleCode时的处理 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送GET /role/detail/NON_EXISTENT请求 |
| 请求参数 | 路径参数roleCode=NON_EXISTENT |
| 预期结果 | code=500，msg包含角色不存在的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-5.4.3: roleCode为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证roleCode为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送GET /role/detail/请求（roleCode为空） |
| 请求参数 | 路径参数roleCode为空 |
| 预期结果 | code=500，msg包含"角色编码不能为空"的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 50 / 100 / 200 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-5.4.1 | 正常负载 | 50 | 60s | ≥200 | ≤100ms | ≤0.1% |
| PT-5.4.2 | 高负载 | 100 | 120s | ≥250 | ≤300ms | ≤1% |
| PT-5.4.3 | 峰值负载 | 200 | 60s | ≥300 | ≤800ms | ≤5% |

##### Apifox压测设置说明

1. 创建测试场景，添加GET /role/detail/{roleCode}请求
2. 参数化roleCode（CSV数据源）
3. 设置断言：code=200

---

### 5.5 POST /role/add 新增角色

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /role/add |
| HTTP方法 | POST |
| 接口描述 | 新增角色信息 |
| 是否需要认证 | 是 |
| 权限要求 | 角色管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |
| Content-Type | String | 是 | 内容类型 | application/json |

##### 请求体 (Body)

| 参数名 | 类型 | 是否必填 | 约束条件 | 说明 | 示例值 |
|--------|------|---------|---------|------|--------|
| roleCode | String | 是 | @NotBlank | 角色编码 | ROLE_TEST |
| roleName | String | 是 | @NotBlank @Size(2-100) | 角色名称 | 测试角色 |
| roleDesc | String | 否 | @Size(max=500) | 角色描述 | 用于接口测试 |
| status | Enum | 是 | @NotNull @EnumField(RoleStatus) | 状态 | ENABLED |
| disableReason | String | 否 | @Size(max=200) | 禁用原因 | - |

##### 请求示例

```json
{
  "roleCode": "ROLE_TEST",
  "roleName": "测试角色",
  "roleDesc": "用于接口测试",
  "status": "ENABLED"
}
```

```bash
curl -X POST 'http://localhost:8081/NexusIxService/role/add' \
  -H 'NexusIX: abc123def456' \
  -H 'Content-Type: application/json' \
  -d '{"roleCode":"ROLE_TEST","roleName":"测试角色","roleDesc":"用于接口测试","status":"ENABLED"}'
```

#### 业务场景测试用例

##### 测试用例 TC-5.5.1: 正常新增角色

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证使用完整合法参数新增角色成功 |
| 前置条件 | 已登录获取有效token，ROLE_TEST不存在 |
| 操作步骤 | 1. 发送POST /role/add请求 2. 验证数据库 3. 清理 |
| 请求参数 | `{"roleCode":"ROLE_TEST","roleName":"测试角色","roleDesc":"用于接口测试","status":"ENABLED"}` |
| 预期结果 | code=200，data为受影响行数(1)；数据库新增一条角色记录 |
| 验证SQL | `SELECT id, role_code, role_name FROM sys_role WHERE role_code='ROLE_TEST' AND is_deleted='NOT_DELETED';` 清理：`UPDATE sys_role SET is_deleted='DELETED' WHERE role_code='ROLE_TEST';` |

##### 测试用例 TC-5.5.2: 角色编码重复

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证新增已存在的roleCode时返回错误 |
| 前置条件 | ROLE_ADMIN角色已存在 |
| 操作步骤 | 1. 发送POST /role/add请求，roleCode=ROLE_ADMIN |
| 请求参数 | `{"roleCode":"ROLE_ADMIN","roleName":"重复角色","status":"ENABLED"}` |
| 预期结果 | code=500，msg包含角色编码已存在的描述 |
| 验证SQL | `SELECT COUNT(*) FROM sys_role WHERE role_code='ROLE_ADMIN' AND is_deleted='NOT_DELETED';` |

##### 测试用例 TC-5.5.3: 必填参数roleCode为空

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证roleCode为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /role/add请求，roleCode为空 |
| 请求参数 | `{"roleCode":"","roleName":"测试角色","status":"ENABLED"}` |
| 预期结果 | code=500，msg包含"角色编码不能为空"的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-5.5.4: 角色名称长度校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证roleName长度不足2位时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /role/add请求，roleName为"A"（1个字符） |
| 请求参数 | `{"roleCode":"ROLE_LEN_TEST","roleName":"A","status":"ENABLED"}` |
| 预期结果 | code=500，msg包含"角色名称必须在2-100字符之间"的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 50 / 100 / 200 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-5.5.1 | 正常负载 | 50 | 60s | ≥100 | ≤200ms | ≤0.1% |
| PT-5.5.2 | 高负载 | 100 | 120s | ≥120 | ≤500ms | ≤1% |
| PT-5.5.3 | 峰值负载 | 200 | 60s | ≥150 | ≤1000ms | ≤5% |

##### Apifox压测设置说明

1. 创建测试场景，添加POST /role/add请求
2. 参数化roleCode（随机生成器确保唯一）
3. 设置断言：code=200

---

### 5.6 PUT /role/update 修改角色

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /role/update |
| HTTP方法 | PUT |
| 接口描述 | 修改角色信息 |
| 是否需要认证 | 是 |
| 权限要求 | 角色管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |
| Content-Type | String | 是 | 内容类型 | application/json |

##### 请求体 (Body)

| 参数名 | 类型 | 是否必填 | 约束条件 | 说明 | 示例值 |
|--------|------|---------|---------|------|--------|
| roleCode | String | 是 | @NotBlank | 角色编码（定位角色） | ROLE_TEST |
| roleName | String | 是 | @NotBlank @Size(2-100) | 角色名称 | 测试角色-修改 |
| roleDesc | String | 否 | @Size(max=500) | 角色描述 | - |
| status | Enum | 是 | @NotNull @EnumField(RoleStatus) | 状态 | ENABLED |
| disableReason | String | 否 | @Size(max=200) | 禁用原因 | - |

##### 请求示例

```json
{
  "roleCode": "ROLE_TEST",
  "roleName": "测试角色-修改",
  "roleDesc": "用于接口测试-修改",
  "status": "ENABLED"
}
```

```bash
curl -X PUT 'http://localhost:8081/NexusIxService/role/update' \
  -H 'NexusIX: abc123def456' \
  -H 'Content-Type: application/json' \
  -d '{"roleCode":"ROLE_TEST","roleName":"测试角色-修改","status":"ENABLED"}'
```

#### 业务场景测试用例

##### 测试用例 TC-5.6.1: 正常修改角色信息

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证修改角色roleName成功 |
| 前置条件 | 已新增ROLE_TEST角色 |
| 操作步骤 | 1. 发送PUT /role/update修改roleName 2. 验证数据库 3. 清理 |
| 请求参数 | `{"roleCode":"ROLE_TEST","roleName":"测试角色-修改","status":"ENABLED"}` |
| 预期结果 | code=200，数据库roleName已更新为"测试角色-修改" |
| 验证SQL | `SELECT role_name FROM sys_role WHERE role_code='ROLE_TEST' AND is_deleted='NOT_DELETED';` |

##### 测试用例 TC-5.6.2: 修改不存在的角色

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证修改不存在的roleCode时返回错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送PUT /role/update请求，roleCode=NON_EXISTENT |
| 请求参数 | `{"roleCode":"NON_EXISTENT","roleName":"不存在","status":"ENABLED"}` |
| 预期结果 | code=500，msg包含角色不存在的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-5.6.3: 实体禁用状态测试-修改角色状态为DISABLED

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证通过update接口修改角色状态为DISABLED，并验证禁用后影响 |
| 前置条件 | 已新增ROLE_TEST角色，test_all用户通过ROLE_ADMIN_A(76001)角色关联 |
| 操作步骤 | 1. 修改ROLE_ADMIN_A角色状态为DISABLED 2. 登录验证该角色相关权限在disabled列表 3. 恢复 |
| 请求参数 | 修改：`UPDATE sys_role SET status='DISABLED' WHERE id=76001;`；验证：调用登录接口 |
| 预期结果 | 修改后登录，disabledDetailByTenant中对应租户的role列表包含该角色关联的权限；恢复后正常 |
| 验证SQL | `SELECT status FROM sys_role WHERE id=76001;`；恢复：`UPDATE sys_role SET status='ENABLED' WHERE id=76001;` |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 50 / 100 / 200 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-5.6.1 | 正常负载 | 50 | 60s | ≥100 | ≤200ms | ≤0.1% |
| PT-5.6.2 | 高负载 | 100 | 120s | ≥120 | ≤500ms | ≤1% |
| PT-5.6.3 | 峰值负载 | 200 | 60s | ≥150 | ≤1000ms | ≤5% |

##### Apifox压测设置说明

1. 创建测试场景，先调用POST /role/add准备测试数据
2. 添加PUT /role/update请求，参数化roleCode
3. 设置断言：code=200

---

### 5.7 PUT /role/status 更新角色状态

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /role/status |
| HTTP方法 | PUT |
| 接口描述 | 更新指定角色的状态 |
| 是否需要认证 | 是 |
| 权限要求 | 角色管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |

##### 查询参数 (Query)

| 参数名 | 类型 | 是否必填 | 约束条件 | 说明 | 示例值 |
|--------|------|---------|---------|------|--------|
| id | String | 是 | @NotBlank | 角色ID | 7201 |
| status | String | 是 | @NotBlank | 角色状态(ENABLED/DISABLED) | DISABLED |

##### 请求示例

```bash
curl -X PUT 'http://localhost:8081/NexusIxService/role/status?id=7201&status=DISABLED' \
  -H 'NexusIX: abc123def456'
```

#### 业务场景测试用例

##### 测试用例 TC-5.7.1: 正常更新角色状态为DISABLED

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证将角色状态更新为DISABLED |
| 前置条件 | 已新增测试角色并获取其id |
| 操作步骤 | 1. 发送PUT /role/status?id={id}&status=DISABLED 2. 验证数据库 3. 恢复 |
| 请求参数 | id={测试角色id}, status=DISABLED |
| 预期结果 | code=200，数据库status=DISABLED |
| 验证SQL | `SELECT status FROM sys_role WHERE id={id};` |

##### 测试用例 TC-5.7.2: 更新不存在的角色状态

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证更新不存在角色的id时返回错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送PUT /role/status?id=999999&status=DISABLED |
| 请求参数 | id=999999, status=DISABLED |
| 预期结果 | code=500，msg包含角色不存在的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-5.7.3: 权限层级禁用测试-禁用角色后验证登录

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证禁用角色(ROLE)后，登录响应中disabledDetailByTenant对应租户的role列表包含相关权限 |
| 前置条件 | test_all用户通过UP_ROLE_ADMIN_A绑定76001角色，策略82001关联该角色 |
| 操作步骤 | 1. 禁用角色76001：`UPDATE sys_role SET status='DISABLED' WHERE id=76001;` 2. 调用登录接口 3. 验证响应 4. 恢复 |
| 请求参数 | 直接SQL修改角色状态后登录验证 |
| 预期结果 | 登录响应中，TENANT_A下角色76001关联的权限策略查询被过滤（因r.status='ENABLED'条件）；disabledDetailByTenant.TENANT_A.role列表应有变化 |
| 验证SQL | `SELECT status FROM sys_role WHERE id=76001;`；恢复：`UPDATE sys_role SET status='ENABLED' WHERE id=76001;` |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 50 / 100 / 200 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-5.7.1 | 正常负载 | 50 | 60s | ≥200 | ≤100ms | ≤0.1% |
| PT-5.7.2 | 高负载 | 100 | 120s | ≥250 | ≤300ms | ≤1% |
| PT-5.7.3 | 峰值负载 | 200 | 60s | ≥300 | ≤800ms | ≤5% |

##### Apifox压测设置说明

1. 创建测试场景，添加PUT /role/status请求
2. 参数化id（CSV数据源）
3. 设置断言：code=200

---

### 5.8 DELETE /role/delete 删除角色

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /role/delete |
| HTTP方法 | DELETE |
| 接口描述 | 删除角色信息（逻辑删除） |
| 是否需要认证 | 是 |
| 权限要求 | 角色管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |

##### 查询参数 (Query)

| 参数名 | 类型 | 是否必填 | 约束条件 | 说明 | 示例值 |
|--------|------|---------|---------|------|--------|
| id | String | 是 | @NotBlank | 角色ID | 7201 |

##### 请求示例

```bash
curl -X DELETE 'http://localhost:8081/NexusIxService/role/delete?id=7201' \
  -H 'NexusIX: abc123def456'
```

#### 业务场景测试用例

##### 测试用例 TC-5.8.1: 正常删除角色

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证删除已存在的角色（逻辑删除） |
| 前置条件 | 已新增测试角色ROLE_TEST并获取其id |
| 操作步骤 | 1. 发送DELETE /role/delete?id={id} 2. 验证数据库逻辑删除标记 |
| 请求参数 | id={测试角色id} |
| 预期结果 | code=200，数据库is_deleted=DELETED |
| 验证SQL | `SELECT is_deleted FROM sys_role WHERE id={id};` 预期is_deleted=DELETED |

##### 测试用例 TC-5.8.2: 删除不存在的角色

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证删除不存在的角色id时返回错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送DELETE /role/delete?id=999999 |
| 请求参数 | id=999999 |
| 预期结果 | code=500，msg包含角色不存在的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-5.8.3: id为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证id为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送DELETE /role/delete（不传id） |
| 请求参数 | 不传id参数 |
| 预期结果 | code=500，msg包含"Id不能为空"的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 50 / 100 / 200 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-5.8.1 | 正常负载 | 50 | 60s | ≥200 | ≤100ms | ≤0.1% |
| PT-5.8.2 | 高负载 | 100 | 120s | ≥250 | ≤300ms | ≤1% |
| PT-5.8.3 | 峰值负载 | 200 | 60s | ≥300 | ≤800ms | ≤5% |

##### Apifox压测设置说明

1. 创建测试场景，先批量新增测试角色
2. 添加DELETE /role/delete请求，参数化id
3. 设置断言：code=200

---

### 5.9 POST /role/batch 批量新增角色

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /role/batch |
| HTTP方法 | POST |
| 接口描述 | 批量新增角色信息 |
| 是否需要认证 | 是 |
| 权限要求 | 角色管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |
| Content-Type | String | 是 | 内容类型 | application/json |

##### 请求体 (Body)

请求体为 `List<SysRoleAddRTO>` 数组，每个元素字段同[5.5 POST /role/add](#55-post-roleadd-新增角色)请求体。集合不能为空（@NotEmpty）。

##### 请求示例

```json
[
  {
    "roleCode": "ROLE_BATCH_1",
    "roleName": "批量角色1",
    "status": "ENABLED"
  },
  {
    "roleCode": "ROLE_BATCH_2",
    "roleName": "批量角色2",
    "status": "ENABLED"
  }
]
```

```bash
curl -X POST 'http://localhost:8081/NexusIxService/role/batch' \
  -H 'NexusIX: abc123def456' \
  -H 'Content-Type: application/json' \
  -d '[{"roleCode":"ROLE_BATCH_1","roleName":"批量角色1","status":"ENABLED"},{"roleCode":"ROLE_BATCH_2","roleName":"批量角色2","status":"ENABLED"}]'
```

#### 业务场景测试用例

##### 测试用例 TC-5.9.1: 正常批量新增角色

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证批量新增2个角色成功 |
| 前置条件 | 已登录获取有效token，ROLE_BATCH_1和ROLE_BATCH_2不存在 |
| 操作步骤 | 1. 发送POST /role/batch，body为2个角色对象数组 2. 验证响应与数据库 |
| 请求参数 | body=2个角色对象数组 |
| 预期结果 | code=200，data返回新增角色列表，每条记录均生成id |
| 验证SQL | `SELECT * FROM sys_role WHERE role_code IN ('ROLE_BATCH_1','ROLE_BATCH_2') AND is_deleted='NOT_DELETED';` 预期返回2条 |

##### 测试用例 TC-5.9.2: 集合为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证请求体为空数组时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /role/batch，body=[] |
| 请求参数 | body=[] |
| 预期结果 | code=500，msg包含集合不能为空的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-5.9.3: 集合内单条记录校验失败

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证集合中存在不合法记录时整体失败（或部分失败） |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /role/batch，body包含1个合法+1个roleName长度=1的非法记录 |
| 请求参数 | body=[{合法对象},{roleName:"A"}] |
| 预期结果 | code=500，msg包含参数校验错误信息（角色名长度2-100） |
| 验证SQL | `SELECT count(*) FROM sys_role WHERE role_code IN (合法roleCode);` 依业务实现核对 |

##### 测试用例 TC-5.9.4: 角色编码重复

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证批量新增中存在已存在roleCode时返回业务错误 |
| 前置条件 | 已登录获取有效token，ROLE_EXISTING已存在 |
| 操作步骤 | 1. 发送POST /role/batch，body包含roleCode=ROLE_EXISTING |
| 请求参数 | body=[{roleCode:"ROLE_EXISTING",roleName:"重复角色",status:"ENABLED"}] |
| 预期结果 | code=500，msg包含角色编码已存在的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 30 / 80 / 150 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-5.9.1 | 正常负载 | 30 | 60s | ≥150 | ≤200ms | ≤0.5% |
| PT-5.9.2 | 高负载 | 80 | 120s | ≥180 | ≤500ms | ≤1% |
| PT-5.9.3 | 峰值负载 | 150 | 60s | ≥200 | ≤1000ms | ≤5% |

##### Apifox压测设置说明

1. 创建测试场景，准备N组（N=并发数）不同的roleCode数据集
2. 添加POST /role/batch请求，body参数化引用数据集
3. 设置断言：code=200
4. 压测后清理：DELETE /role/delete批量删除新增角色

---

### 5.10 PUT /role/batch 批量修改角色

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /role/batch |
| HTTP方法 | PUT |
| 接口描述 | 批量修改角色信息 |
| 是否需要认证 | 是 |
| 权限要求 | 角色管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |
| Content-Type | String | 是 | 内容类型 | application/json |

##### 请求体 (Body)

请求体为 `List<SysRoleUpdateRTO>` 数组，每个元素字段同[5.6 PUT /role/update](#56-put-roleupdate-修改角色)请求体。注意：批量修改接口未声明@NotEmpty，但建议至少传1条。

##### 请求示例

```json
[
  {
    "roleCode": "ROLE_BATCH_1",
    "roleName": "批量角色1-更新",
    "status": "ENABLED"
  },
  {
    "roleCode": "ROLE_BATCH_2",
    "roleName": "批量角色2-更新",
    "status": "DISABLED"
  }
]
```

```bash
curl -X PUT 'http://localhost:8081/NexusIxService/role/batch' \
  -H 'NexusIX: abc123def456' \
  -H 'Content-Type: application/json' \
  -d '[{"roleCode":"ROLE_BATCH_1","roleName":"批量角色1-更新","status":"ENABLED"},{"roleCode":"ROLE_BATCH_2","roleName":"批量角色2-更新","status":"DISABLED"}]'
```

#### 业务场景测试用例

##### 测试用例 TC-5.10.1: 正常批量修改角色

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证批量修改2个角色成功 |
| 前置条件 | 已新增ROLE_BATCH_1和ROLE_BATCH_2 |
| 操作步骤 | 1. 发送PUT /role/batch 2. 验证响应与数据库 |
| 请求参数 | body=2个修改对象数组 |
| 预期结果 | code=200，data返回影响行数 |
| 验证SQL | `SELECT role_name,status FROM sys_role WHERE role_code IN ('ROLE_BATCH_1','ROLE_BATCH_2');` 预期名称已更新 |

##### 测试用例 TC-5.10.2: 批量修改中包含不存在roleCode

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证批量修改中包含不存在的roleCode时返回业务错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送PUT /role/batch，body包含roleCode=ROLE_NOT_EXIST |
| 请求参数 | body=[{roleCode:"ROLE_NOT_EXIST",roleName:"不存在",status:"ENABLED"}] |
| 预期结果 | code=500，msg包含角色不存在的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-5.10.3: 集合内单条记录校验失败

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证集合中存在不合法记录时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送PUT /role/batch，body包含roleName长度=1的非法记录 |
| 请求参数 | body=[{roleCode:"ROLE_BATCH_1",roleName:"A"}] |
| 预期结果 | code=500，msg包含角色名长度2-100的校验错误 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 30 / 80 / 150 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-5.10.1 | 正常负载 | 30 | 60s | ≥150 | ≤200ms | ≤0.5% |
| PT-5.10.2 | 高负载 | 80 | 120s | ≥180 | ≤500ms | ≤1% |
| PT-5.10.3 | 峰值负载 | 150 | 60s | ≥200 | ≤1000ms | ≤5% |

##### Apifox压测设置说明

1. 准备N条测试角色作为压测数据
2. 添加PUT /role/batch请求，body参数化引用数据集
3. 设置断言：code=200

---

### 5.11 PUT /role/status/batch 批量更新角色状态

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /role/status/batch |
| HTTP方法 | PUT |
| 接口描述 | 批量更新多个指定角色的状态 |
| 是否需要认证 | 是 |
| 权限要求 | 角色管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |
| Content-Type | String | 是 | 内容类型 | application/json |

##### 查询参数 (Query)

| 参数名 | 类型 | 是否必填 | 约束条件 | 说明 | 示例值 |
|--------|------|---------|---------|------|--------|
| status | String | 是 | @NotBlank | 角色状态（ENABLED/DISABLED） | DISABLED |

##### 请求体 (Body)

请求体为 `List<String>` 类型，元素为角色ID。集合不能为空（@NotEmpty）。

##### 请求示例

```json
["7201", "7202", "7203"]
```

```bash
curl -X PUT 'http://localhost:8081/NexusIxService/role/status/batch?status=DISABLED' \
  -H 'NexusIX: abc123def456' \
  -H 'Content-Type: application/json' \
  -d '["7201","7202","7203"]'
```

#### 业务场景测试用例

##### 测试用例 TC-5.11.1: 正常批量禁用角色

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证批量禁用3个角色成功 |
| 前置条件 | 已新增3个测试角色并获取其id |
| 操作步骤 | 1. 发送PUT /role/status/batch?status=DISABLED，body=[id1,id2,id3] 2. 验证数据库 |
| 请求参数 | status=DISABLED, body=[id1,id2,id3] |
| 预期结果 | code=200，data返回影响行数3 |
| 验证SQL | `SELECT status FROM sys_role WHERE id IN (id1,id2,id3);` 预期status=DISABLED |

##### 测试用例 TC-5.11.2: 集合为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证ids集合为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送PUT /role/status/batch?status=DISABLED，body=[] |
| 请求参数 | status=DISABLED, body=[] |
| 预期结果 | code=500，msg包含"角色ID集合不能为空"的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-5.11.3: status为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证status为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送PUT /role/status/batch（不传status），body=["7201"] |
| 请求参数 | 不传status参数 |
| 预期结果 | code=500，msg包含"状态不能为空"的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-5.11.4: 集合包含不存在的id

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证集合中包含不存在的角色id时返回业务错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送PUT /role/status/batch?status=DISABLED，body=["999999"] |
| 请求参数 | status=DISABLED, body=["999999"] |
| 预期结果 | code=500，msg包含角色不存在的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 50 / 100 / 200 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-5.11.1 | 正常负载 | 50 | 60s | ≥200 | ≤100ms | ≤0.1% |
| PT-5.11.2 | 高负载 | 100 | 120s | ≥250 | ≤300ms | ≤1% |
| PT-5.11.3 | 峰值负载 | 200 | 60s | ≥300 | ≤800ms | ≤5% |

##### Apifox压测设置说明

1. 准备N个测试角色id集合
2. 添加PUT /role/status/batch请求，body参数化id集合
3. 设置断言：code=200
4. 压测后清理：恢复角色状态为ENABLED

---

### 5.12 DELETE /role/batch 批量删除角色

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /role/batch |
| HTTP方法 | DELETE |
| 接口描述 | 批量删除角色信息（逻辑删除） |
| 是否需要认证 | 是 |
| 权限要求 | 角色管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |

##### 请求参数 (Body)

请求参数为 `List<String>` 类型，元素为角色ID。注意：当前实现未显式声明@NotEmpty，但建议至少传1条。参数通过请求体传递（非query参数）。

##### 请求示例

```json
["7201", "7202", "7203"]
```

```bash
curl -X DELETE 'http://localhost:8081/NexusIxService/role/batch' \
  -H 'NexusIX: abc123def456' \
  -H 'Content-Type: application/json' \
  -d '["7201","7202","7203"]'
```

#### 业务场景测试用例

##### 测试用例 TC-5.12.1: 正常批量删除角色

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证批量删除3个角色成功（逻辑删除） |
| 前置条件 | 已新增3个测试角色并获取其id |
| 操作步骤 | 1. 发送DELETE /role/batch，body=[id1,id2,id3] 2. 验证数据库逻辑删除标记 |
| 请求参数 | body=[id1,id2,id3] |
| 预期结果 | code=200，data返回影响行数3 |
| 验证SQL | `SELECT is_deleted FROM sys_role WHERE id IN (id1,id2,id3);` 预期is_deleted=DELETED |

##### 测试用例 TC-5.12.2: 集合包含不存在的id

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证集合中包含不存在的角色id时返回业务错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送DELETE /role/batch，body=["999999"] |
| 请求参数 | body=["999999"] |
| 预期结果 | code=500，msg包含角色不存在的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-5.12.3: 已删除角色重复删除

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证对已逻辑删除的角色再次删除时返回业务错误 |
| 前置条件 | 已删除测试角色（is_deleted=DELETED） |
| 操作步骤 | 1. 发送DELETE /role/batch，body=[已删除角色id] |
| 请求参数 | body=[已删除角色id] |
| 预期结果 | code=500，msg包含角色不存在的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 50 / 100 / 200 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-5.12.1 | 正常负载 | 50 | 60s | ≥200 | ≤100ms | ≤0.1% |
| PT-5.12.2 | 高负载 | 100 | 120s | ≥250 | ≤300ms | ≤1% |
| PT-5.12.3 | 峰值负载 | 200 | 60s | ≥300 | ≤800ms | ≤5% |

##### Apifox压测设置说明

1. 准备N个测试角色作为压测数据
2. 添加DELETE /role/batch请求，body参数化id集合
3. 设置断言：code=200
4. 压测后无需清理（已逻辑删除）

---

## 6. SysPermController 权限管理接口测试

> Controller路径：`nexusix-iam\src\main\java\com\shy\nexusix\iam\controller\SysPermController.java`
> 接口前缀：`/perm`
> 共15个接口

### 6.1 GET /perm/list 查询权限列表

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /perm/list |
| HTTP方法 | GET |
| 接口描述 | 返回所有权限列表 |
| 是否需要认证 | 是 |
| 权限要求 | 权限管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |

##### 请求参数

无

##### 请求示例

```bash
curl -X GET 'http://localhost:8081/NexusIxService/perm/list' \
  -H 'NexusIX: abc123def456'
```

#### 业务场景测试用例

##### 测试用例 TC-6.1.1: 正常查询权限列表

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证已登录用户成功获取权限列表 |
| 前置条件 | 已登录获取有效token，数据库存在权限数据 |
| 操作步骤 | 1. 发送GET /perm/list 2. 验证响应数据结构 |
| 请求参数 | 无 |
| 预期结果 | code=200，data为权限列表数组，每条记录包含id/permCode/permName/permType/parentCode等字段 |
| 验证SQL | `SELECT count(*) FROM sys_perm WHERE is_deleted='NOT_DELETED';` 与响应列表数量一致 |

##### 测试用例 TC-6.1.2: 未携带Token

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证未携带NexusIX Token时返回未登录错误 |
| 前置条件 | 不携带Token |
| 操作步骤 | 1. 发送GET /perm/list，不设置NexusIX Header |
| 请求参数 | 无 |
| 预期结果 | code=401，msg包含未登录的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-6.1.3: 无权限访问

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证无权限管理权限的用户访问时返回权限不足错误 |
| 前置条件 | 已登录但无权限管理权限的token |
| 操作步骤 | 1. 发送GET /perm/list |
| 请求参数 | 无 |
| 预期结果 | code=403或业务错误码，msg包含无权限的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 100 / 200 / 500 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-6.1.1 | 正常负载 | 100 | 60s | ≥300 | ≤50ms | ≤0.1% |
| PT-6.1.2 | 高负载 | 200 | 120s | ≥400 | ≤150ms | ≤1% |
| PT-6.1.3 | 峰值负载 | 500 | 60s | ≥500 | ≤500ms | ≤5% |

##### Apifox压测设置说明

1. 创建测试场景，添加GET /perm/list请求
2. 设置全局变量Token
3. 设置断言：code=200 且 data不为空

---

### 6.2 GET /perm/page 分页查询权限列表

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /perm/page |
| HTTP方法 | GET |
| 接口描述 | 返回分页后的权限列表 |
| 是否需要认证 | 是 |
| 权限要求 | 权限管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |

##### 查询参数 (Query)

| 参数名 | 类型 | 是否必填 | 约束条件 | 说明 | 示例值 |
|--------|------|---------|---------|------|--------|
| pageNum | Integer | 否 | @Min(1)，默认1 | 页码 | 1 |
| pageSize | Integer | 否 | @Min(1)@Max(100)，默认10 | 每页条数 | 10 |

##### 请求示例

```bash
curl -X GET 'http://localhost:8081/NexusIxService/perm/page?pageNum=1&pageSize=10' \
  -H 'NexusIX: abc123def456'
```

#### 业务场景测试用例

##### 测试用例 TC-6.2.1: 正常分页查询

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证默认分页查询成功 |
| 前置条件 | 已登录获取有效token，数据库存在权限数据 |
| 操作步骤 | 1. 发送GET /perm/page?pageNum=1&pageSize=10 |
| 请求参数 | pageNum=1, pageSize=10 |
| 预期结果 | code=200，data包含records/total/current/size字段，records长度≤10 |
| 验证SQL | `SELECT count(*) FROM sys_perm WHERE is_deleted='NOT_DELETED';` 与total一致 |

##### 测试用例 TC-6.2.2: 默认参数分页查询

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证不传分页参数时使用默认值（pageNum=1, pageSize=10） |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送GET /perm/page（不传任何参数） |
| 请求参数 | 无 |
| 预期结果 | code=200，data.current=1, data.size=10 |
| 验证SQL | 无 |

##### 测试用例 TC-6.2.3: pageSize超出上限

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证pageSize=200时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送GET /perm/page?pageNum=1&pageSize=200 |
| 请求参数 | pageNum=1, pageSize=200 |
| 预期结果 | code=500，msg包含pageSize最大100的校验错误 |
| 验证SQL | 无 |

##### 测试用例 TC-6.2.4: pageNum为0

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证pageNum=0时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送GET /perm/page?pageNum=0&pageSize=10 |
| 请求参数 | pageNum=0, pageSize=10 |
| 预期结果 | code=500，msg包含pageNum最小1的校验错误 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 100 / 200 / 500 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-6.2.1 | 正常负载 | 100 | 60s | ≥300 | ≤50ms | ≤0.1% |
| PT-6.2.2 | 高负载 | 200 | 120s | ≥400 | ≤150ms | ≤1% |
| PT-6.2.3 | 峰值负载 | 500 | 60s | ≥500 | ≤500ms | ≤5% |

##### Apifox压测设置说明

1. 创建测试场景，添加GET /perm/page请求
2. 参数化pageNum和pageSize，模拟翻页
3. 设置断言：code=200

---

### 6.3 GET /perm/tree/list 查询权限树形结构列表

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /perm/tree/list |
| HTTP方法 | GET |
| 接口描述 | 返回所有权限的层级树形结构 |
| 是否需要认证 | 是 |
| 权限要求 | 权限管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |

##### 请求参数

无

##### 请求示例

```bash
curl -X GET 'http://localhost:8081/NexusIxService/perm/tree/list' \
  -H 'NexusIX: abc123def456'
```

#### 业务场景测试用例

##### 测试用例 TC-6.3.1: 正常查询权限树形结构

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证查询权限树形结构成功 |
| 前置条件 | 已登录获取有效token，数据库存在多层级权限数据 |
| 操作步骤 | 1. 发送GET /perm/tree/list 2. 验证响应数据为树形结构 |
| 请求参数 | 无 |
| 预期结果 | code=200，data为权限树数组，每个节点包含id/permCode/permName/children（子节点列表）字段 |
| 验证SQL | `SELECT count(*) FROM sys_perm WHERE is_deleted='NOT_DELETED' AND parent_code='0';` 与根节点数量一致 |

##### 测试用例 TC-6.3.2: 未携带Token

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证未携带Token时返回未登录错误 |
| 前置条件 | 不携带Token |
| 操作步骤 | 1. 发送GET /perm/tree/list，不设置NexusIX Header |
| 请求参数 | 无 |
| 预期结果 | code=401，msg包含未登录的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-6.3.3: 树形结构层级验证

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证权限树形结构正确反映父子关系 |
| 前置条件 | 已登录获取有效token，数据库存在多层级权限 |
| 操作步骤 | 1. 发送GET /perm/tree/list 2. 检查每个根节点的children字段 3. 验证子节点的parentCode与父节点permCode一致 |
| 请求参数 | 无 |
| 预期结果 | code=200，children字段中的子节点parentCode与父节点permCode匹配 |
| 验证SQL | `SELECT perm_code, parent_code FROM sys_perm WHERE is_deleted='NOT_DELETED' ORDER BY parent_code;` |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 100 / 200 / 500 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-6.3.1 | 正常负载 | 100 | 60s | ≥250 | ≤100ms | ≤0.1% |
| PT-6.3.2 | 高负载 | 200 | 120s | ≥300 | ≤200ms | ≤1% |
| PT-6.3.3 | 峰值负载 | 500 | 60s | ≥400 | ≤800ms | ≤5% |

##### Apifox压测设置说明

1. 树形结构查询较列表查询耗时（需递归构建），并发数适当降低
2. 设置断言：code=200 且 data长度>0

---

### 6.4 GET /perm/tree/page 分页查询权限树形结构

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /perm/tree/page |
| HTTP方法 | GET |
| 接口描述 | 返回分页后的权限树形结构 |
| 是否需要认证 | 是 |
| 权限要求 | 权限管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |

##### 查询参数 (Query)

| 参数名 | 类型 | 是否必填 | 约束条件 | 说明 | 示例值 |
|--------|------|---------|---------|------|--------|
| pageNum | Integer | 否 | @Min(1)，默认1 | 页码 | 1 |
| pageSize | Integer | 否 | @Min(1)@Max(100)，默认10 | 每页条数 | 10 |

##### 请求示例

```bash
curl -X GET 'http://localhost:8081/NexusIxService/perm/tree/page?pageNum=1&pageSize=10' \
  -H 'NexusIX: abc123def456'
```

#### 业务场景测试用例

##### 测试用例 TC-6.4.1: 正常分页查询树形结构

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证分页查询权限树形结构成功 |
| 前置条件 | 已登录获取有效token，数据库存在权限数据 |
| 操作步骤 | 1. 发送GET /perm/tree/page?pageNum=1&pageSize=10 |
| 请求参数 | pageNum=1, pageSize=10 |
| 预期结果 | code=200，data包含records/total，records为分页后的树形结构 |
| 验证SQL | `SELECT count(*) FROM sys_perm WHERE is_deleted='NOT_DELETED' AND parent_code='0';` 与total一致 |

##### 测试用例 TC-6.4.2: pageSize超出上限

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证pageSize=150时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送GET /perm/tree/page?pageNum=1&pageSize=150 |
| 请求参数 | pageNum=1, pageSize=150 |
| 预期结果 | code=500，msg包含pageSize最大100的校验错误 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 100 / 200 / 500 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-6.4.1 | 正常负载 | 100 | 60s | ≥200 | ≤150ms | ≤0.1% |
| PT-6.4.2 | 高负载 | 200 | 120s | ≥250 | ≤300ms | ≤1% |
| PT-6.4.3 | 峰值负载 | 500 | 60s | ≥300 | ≤1000ms | ≤5% |

##### Apifox压测设置说明

1. 树形结构分页查询需构建子树，性能较列表查询略低
2. 设置断言：code=200

---

### 6.5 GET /perm/tree/{id} 查询指定权限树形结构

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /perm/tree/{id} |
| HTTP方法 | GET |
| 接口描述 | 返回指定权限的层级树形结构 |
| 是否需要认证 | 是 |
| 权限要求 | 权限管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |

##### 路径参数 (Path)

| 参数名 | 类型 | 是否必填 | 约束条件 | 说明 | 示例值 |
|--------|------|---------|---------|------|--------|
| id | String | 是 | @NotBlank | 权限ID | 5001 |

##### 请求示例

```bash
curl -X GET 'http://localhost:8081/NexusIxService/perm/tree/5001' \
  -H 'NexusIX: abc123def456'
```

#### 业务场景测试用例

##### 测试用例 TC-6.5.1: 正常查询指定权限树

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证查询指定权限的树形结构成功 |
| 前置条件 | 已登录获取有效token，权限id=5001存在且有子权限 |
| 操作步骤 | 1. 发送GET /perm/tree/5001 2. 验证返回的树形结构包含子节点 |
| 请求参数 | id=5001 |
| 预期结果 | code=200，data为权限对象，包含children子节点列表 |
| 验证SQL | `SELECT * FROM sys_perm WHERE id=5001 AND is_deleted='NOT_DELETED';` |

##### 测试用例 TC-6.5.2: id为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证id为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送GET /perm/tree/（路径参数为空） |
| 请求参数 | id为空 |
| 预期结果 | code=500，msg包含"Id不能为空"的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-6.5.3: 查询不存在的权限id

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证查询不存在的权限id时返回业务错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送GET /perm/tree/999999 |
| 请求参数 | id=999999 |
| 预期结果 | code=500，msg包含权限不存在的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 100 / 200 / 500 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-6.5.1 | 正常负载 | 100 | 60s | ≥250 | ≤100ms | ≤0.1% |
| PT-6.5.2 | 高负载 | 200 | 120s | ≥300 | ≤200ms | ≤1% |
| PT-6.5.3 | 峰值负载 | 500 | 60s | ≥400 | ≤800ms | ≤5% |

##### Apifox压测设置说明

1. 参数化id，覆盖根节点、叶子节点
2. 设置断言：code=200

---

### 6.6 POST /perm/query 条件查询权限列表

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /perm/query |
| HTTP方法 | POST |
| 接口描述 | 返回满足条件的权限列表（分页） |
| 是否需要认证 | 是 |
| 权限要求 | 权限管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |
| Content-Type | String | 是 | 内容类型 | application/json |

##### 请求体 (Body)

SysPermQueryRTO extends PageCommonRTO，包含pageNum/pageSize（继承）+ permCode/permName/permType/status/createTimeRange等条件字段。

##### 请求示例

```json
{
  "pageNum": 1,
  "pageSize": 10,
  "permName": "用户",
  "permType": "MENU",
  "status": "ACTIVE"
}
```

```bash
curl -X POST 'http://localhost:8081/NexusIxService/perm/query' \
  -H 'NexusIX: abc123def456' \
  -H 'Content-Type: application/json' \
  -d '{"pageNum":1,"pageSize":10,"permName":"用户","permType":"MENU","status":"ACTIVE"}'
```

#### 业务场景测试用例

##### 测试用例 TC-6.6.1: 按权限名称模糊查询

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证按权限名称模糊查询成功 |
| 前置条件 | 已登录获取有效token，数据库存在名称包含"用户"的权限 |
| 操作步骤 | 1. 发送POST /perm/query，body包含permName="用户" |
| 请求参数 | body={"pageNum":1,"pageSize":10,"permName":"用户"} |
| 预期结果 | code=200，data.records中所有权限名称包含"用户" |
| 验证SQL | `SELECT * FROM sys_perm WHERE perm_name LIKE '%用户%' AND is_deleted='NOT_DELETED' LIMIT 10;` |

##### 测试用例 TC-6.6.2: 按权限类型查询

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证按权限类型查询MENU类型权限 |
| 前置条件 | 已登录获取有效token，数据库存在MENU类型权限 |
| 操作步骤 | 1. 发送POST /perm/query，body包含permType="MENU" |
| 请求参数 | body={"pageNum":1,"pageSize":10,"permType":"MENU"} |
| 预期结果 | code=200，data.records中所有权限类型为MENU |
| 验证SQL | `SELECT count(*) FROM sys_perm WHERE perm_type='MENU' AND is_deleted='NOT_DELETED';` |

##### 测试用例 TC-6.6.3: 多条件组合查询

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证多条件组合查询（permName+permType+status）成功 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /perm/query，body包含permName/permType/status |
| 请求参数 | body={"pageNum":1,"pageSize":10,"permName":"用户","permType":"MENU","status":"ACTIVE"} |
| 预期结果 | code=200，data.records同时满足3个条件 |
| 验证SQL | `SELECT * FROM sys_perm WHERE perm_name LIKE '%用户%' AND perm_type='MENU' AND status='ACTIVE' AND is_deleted='NOT_DELETED';` |

##### 测试用例 TC-6.6.4: 无匹配数据

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证查询条件无匹配数据时返回空列表 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /perm/query，body包含permName="不存在的权限名XYZ" |
| 请求参数 | body={"pageNum":1,"pageSize":10,"permName":"不存在的权限名XYZ"} |
| 预期结果 | code=200，data.records=[]，data.total=0 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 100 / 200 / 500 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-6.6.1 | 正常负载 | 100 | 60s | ≥250 | ≤100ms | ≤0.1% |
| PT-6.6.2 | 高负载 | 200 | 120s | ≥300 | ≤200ms | ≤1% |
| PT-6.6.3 | 峰值负载 | 500 | 60s | ≥400 | ≤800ms | ≤5% |

##### Apifox压测设置说明

1. 参数化查询条件，覆盖名称/类型/状态组合
2. 设置断言：code=200

---

### 6.7 GET /perm/detail/{permCode} 查询权限详情

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /perm/detail/{permCode} |
| HTTP方法 | GET |
| 接口描述 | 返回指定权限的详情信息 |
| 是否需要认证 | 是 |
| 权限要求 | 权限管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |

##### 路径参数 (Path)

| 参数名 | 类型 | 是否必填 | 约束条件 | 说明 | 示例值 |
|--------|------|---------|---------|------|--------|
| permCode | String | 是 | @NotBlank | 权限编码 | MENU_DASHBOARD |

##### 请求示例

```bash
curl -X GET 'http://localhost:8081/NexusIxService/perm/detail/MENU_DASHBOARD' \
  -H 'NexusIX: abc123def456'
```

#### 业务场景测试用例

##### 测试用例 TC-6.7.1: 正常查询权限详情

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证查询已存在权限详情成功 |
| 前置条件 | 已登录获取有效token，权限MENU_DASHBOARD存在 |
| 操作步骤 | 1. 发送GET /perm/detail/MENU_DASHBOARD |
| 请求参数 | permCode=MENU_DASHBOARD |
| 预期结果 | code=200，data包含id/permCode/permName/permType/parentCode/parentName/path等完整字段 |
| 验证SQL | `SELECT * FROM sys_perm WHERE perm_code='MENU_DASHBOARD' AND is_deleted='NOT_DELETED';` |

##### 测试用例 TC-6.7.2: permCode为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证permCode为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送GET /perm/detail/（路径参数为空） |
| 请求参数 | permCode为空 |
| 预期结果 | code=500，msg包含"权限编码不能为空"的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-6.7.3: 查询不存在的权限编码

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证查询不存在的权限编码时返回业务错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送GET /perm/detail/PERM_NOT_EXIST |
| 请求参数 | permCode=PERM_NOT_EXIST |
| 预期结果 | code=500，msg包含权限不存在的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 100 / 200 / 500 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-6.7.1 | 正常负载 | 100 | 60s | ≥300 | ≤50ms | ≤0.1% |
| PT-6.7.2 | 高负载 | 200 | 120s | ≥400 | ≤150ms | ≤1% |
| PT-6.7.3 | 峰值负载 | 500 | 60s | ≥500 | ≤500ms | ≤5% |

##### Apifox压测设置说明

1. 参数化permCode，覆盖菜单/按钮/API/数据类型权限
2. 设置断言：code=200

---

### 6.8 POST /perm/add 新增权限

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /perm/add |
| HTTP方法 | POST |
| 接口描述 | 新增权限信息 |
| 是否需要认证 | 是 |
| 权限要求 | 权限管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |
| Content-Type | String | 是 | 内容类型 | application/json |

##### 请求体 (Body)

SysPermAddRTO 字段校验规则：

| 字段名 | 类型 | 是否必填 | 约束条件 | 说明 | 示例值 |
|--------|------|---------|---------|------|--------|
| permCode | String | 是 | @NotBlank | 权限编码 | PERM_TEST_001 |
| permName | String | 是 | @NotBlank @Size(2-100) | 权限名称 | 测试权限 |
| permDesc | String | 否 | @Size(max=500) | 权限描述 | 测试用 |
| permType | String | 是 | @NotBlank | 权限类型（MENU/BUTTON/API/DATA） | MENU |
| parentCode | String | 是 | @NotNull | 父权限编码（根节点为"0"） | 0 |
| parentName | String | 否 | 无 | 父权限名称 | 系统管理 |
| path | String | 否 | 无 | 权限层级路径 | /501 |
| resourceType | String | 否 | 无 | 资源类型（URL/METHOD/TABLE） | URL |
| resourcePath | String | 否 | 无 | 资源路径 | /api/user/list |
| resourceMethod | String | 否 | 无 | 资源方法 | GET |
| icon | String | 否 | 无 | 图标 | icon-user |
| sortOrder | Integer | 否 | 无 | 排序序号 | 0 |
| isVisible | Boolean | 否 | 无 | 是否可见 | true |
| status | Enum | 是 | @NotNull @EnumField | 状态（PermStatus枚举） | ACTIVE |
| disableReason | String | 否 | @Size(max=200) | 禁用原因 | 权限调整中 |

##### 请求示例

```json
{
  "permCode": "PERM_TEST_001",
  "permName": "测试权限",
  "permDesc": "测试用权限",
  "permType": "MENU",
  "parentCode": "0",
  "parentName": "根节点",
  "path": "/501",
  "resourceType": "URL",
  "resourcePath": "/api/test/list",
  "resourceMethod": "GET",
  "icon": "icon-test",
  "sortOrder": 0,
  "isVisible": true,
  "status": "ACTIVE"
}
```

```bash
curl -X POST 'http://localhost:8081/NexusIxService/perm/add' \
  -H 'NexusIX: abc123def456' \
  -H 'Content-Type: application/json' \
  -d '{"permCode":"PERM_TEST_001","permName":"测试权限","permType":"MENU","parentCode":"0","status":"ACTIVE"}'
```

#### 业务场景测试用例

##### 测试用例 TC-6.8.1: 正常新增权限

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证新增权限成功 |
| 前置条件 | 已登录获取有效token，PERM_TEST_001不存在 |
| 操作步骤 | 1. 发送POST /perm/add 2. 验证响应与数据库 |
| 请求参数 | body=权限对象 |
| 预期结果 | code=200，data返回影响行数1 |
| 验证SQL | `SELECT * FROM sys_perm WHERE perm_code='PERM_TEST_001' AND is_deleted='NOT_DELETED';` |

##### 测试用例 TC-6.8.2: permCode为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证permCode为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /perm/add，body不包含permCode |
| 请求参数 | body={"permName":"测试","permType":"MENU","parentCode":"0","status":"ACTIVE"} |
| 预期结果 | code=500，msg包含"权限编码不能为空"的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-6.8.3: permName长度不足

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证permName长度=1时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /perm/add，body包含permName="A" |
| 请求参数 | body={"permCode":"PERM_TEST","permName":"A","permType":"MENU","parentCode":"0","status":"ACTIVE"} |
| 预期结果 | code=500，msg包含权限名称长度2-100的校验错误 |
| 验证SQL | 无 |

##### 测试用例 TC-6.8.4: permCode重复

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证permCode已存在时返回业务错误 |
| 前置条件 | 已登录获取有效token，MENU_DASHBOARD已存在 |
| 操作步骤 | 1. 发送POST /perm/add，body包含permCode=MENU_DASHBOARD |
| 请求参数 | body={"permCode":"MENU_DASHBOARD","permName":"重复权限","permType":"MENU","parentCode":"0","status":"ACTIVE"} |
| 预期结果 | code=500，msg包含权限编码已存在的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-6.8.5: parentCode为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证parentCode为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /perm/add，body不包含parentCode |
| 请求参数 | body={"permCode":"PERM_TEST","permName":"测试","permType":"MENU","status":"ACTIVE"} |
| 预期结果 | code=500，msg包含"父权限编码不能为空"的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 50 / 100 / 200 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-6.8.1 | 正常负载 | 50 | 60s | ≥200 | ≤100ms | ≤0.1% |
| PT-6.8.2 | 高负载 | 100 | 120s | ≥250 | ≤300ms | ≤1% |
| PT-6.8.3 | 峰值负载 | 200 | 60s | ≥300 | ≤800ms | ≤5% |

##### Apifox压测设置说明

1. 准备N组不同的permCode数据集
2. 添加POST /perm/add请求，body参数化引用数据集
3. 设置断言：code=200
4. 压测后清理：DELETE /perm/delete批量删除新增权限

---

### 6.9 PUT /perm/update 修改权限

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /perm/update |
| HTTP方法 | PUT |
| 接口描述 | 修改权限信息 |
| 是否需要认证 | 是 |
| 权限要求 | 权限管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |
| Content-Type | String | 是 | 内容类型 | application/json |

##### 请求体 (Body)

SysPermUpdateRTO 字段校验规则同SysPermAddRTO，但所有必填字段（permCode/permName/permType/parentCode/status）均不可缺省。

##### 请求示例

```json
{
  "permCode": "PERM_TEST_001",
  "permName": "测试权限-更新",
  "permDesc": "更新后的描述",
  "permType": "MENU",
  "parentCode": "0",
  "status": "ACTIVE"
}
```

```bash
curl -X PUT 'http://localhost:8081/NexusIxService/perm/update' \
  -H 'NexusIX: abc123def456' \
  -H 'Content-Type: application/json' \
  -d '{"permCode":"PERM_TEST_001","permName":"测试权限-更新","permType":"MENU","parentCode":"0","status":"ACTIVE"}'
```

#### 业务场景测试用例

##### 测试用例 TC-6.9.1: 正常修改权限

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证修改已存在权限成功 |
| 前置条件 | 已新增PERM_TEST_001权限 |
| 操作步骤 | 1. 发送PUT /perm/update 2. 验证响应与数据库 |
| 请求参数 | body=权限修改对象 |
| 预期结果 | code=200，data返回影响行数1 |
| 验证SQL | `SELECT perm_name FROM sys_perm WHERE perm_code='PERM_TEST_001';` 预期名称已更新 |

##### 测试用例 TC-6.9.2: 修改不存在的权限

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证修改不存在的权限时返回业务错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送PUT /perm/update，body包含permCode=PERM_NOT_EXIST |
| 请求参数 | body={"permCode":"PERM_NOT_EXIST","permName":"测试","permType":"MENU","parentCode":"0","status":"ACTIVE"} |
| 预期结果 | code=500，msg包含权限不存在的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-6.9.3: permName长度超出上限

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证permName长度=101时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送PUT /perm/update，body包含permName长度=101 |
| 请求参数 | body={"permCode":"PERM_TEST_001","permName":"A"重复101次,"permType":"MENU","parentCode":"0","status":"ACTIVE"} |
| 预期结果 | code=500，msg包含权限名称长度2-100的校验错误 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 50 / 100 / 200 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-6.9.1 | 正常负载 | 50 | 60s | ≥200 | ≤100ms | ≤0.1% |
| PT-6.9.2 | 高负载 | 100 | 120s | ≥250 | ≤300ms | ≤1% |
| PT-6.9.3 | 峰值负载 | 200 | 60s | ≥300 | ≤800ms | ≤5% |

##### Apifox压测设置说明

1. 准备N条测试权限作为压测数据
2. 添加PUT /perm/update请求，body参数化引用数据集
3. 设置断言：code=200

---

### 6.10 PUT /perm/status 更新权限状态

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /perm/status |
| HTTP方法 | PUT |
| 接口描述 | 更新指定权限的状态（ACTIVE/FROZEN） |
| 是否需要认证 | 是 |
| 权限要求 | 权限管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |

##### 查询参数 (Query)

| 参数名 | 类型 | 是否必填 | 约束条件 | 说明 | 示例值 |
|--------|------|---------|---------|------|--------|
| id | String | 是 | @NotBlank | 权限ID | 5001 |
| status | String | 是 | @NotBlank | 权限状态（ACTIVE/FROZEN） | FROZEN |

##### 请求示例

```bash
curl -X PUT 'http://localhost:8081/NexusIxService/perm/status?id=5001&status=FROZEN' \
  -H 'NexusIX: abc123def456'
```

#### 业务场景测试用例

##### 测试用例 TC-6.10.1: 正常冻结权限

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证冻结权限成功 |
| 前置条件 | 已登录获取有效token，权限id=5001状态为ACTIVE |
| 操作步骤 | 1. 发送PUT /perm/status?id=5001&status=FROZEN 2. 验证数据库 |
| 请求参数 | id=5001, status=FROZEN |
| 预期结果 | code=200，data返回影响行数1 |
| 验证SQL | `SELECT status FROM sys_perm WHERE id=5001;` 预期status=FROZEN |

##### 测试用例 TC-6.10.2: 权限状态恢复

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证将冻结的权限恢复为ACTIVE成功 |
| 前置条件 | 权限id=5001状态为FROZEN |
| 操作步骤 | 1. 发送PUT /perm/status?id=5001&status=ACTIVE 2. 验证数据库 |
| 请求参数 | id=5001, status=ACTIVE |
| 预期结果 | code=200，data返回影响行数1 |
| 验证SQL | `SELECT status FROM sys_perm WHERE id=5001;` 预期status=ACTIVE |

##### 测试用例 TC-6.10.3: id为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证id为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送PUT /perm/status?status=FROZEN（不传id） |
| 请求参数 | status=FROZEN |
| 预期结果 | code=500，msg包含"Id不能为空"的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-6.10.4: 冻结权限影响下游用户

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证冻结父权限后，绑定该权限的用户登录时该权限出现在禁用列表 |
| 前置条件 | 权限id=5001存在关联的sys_perm_policy记录，test_all用户绑定该权限 |
| 操作步骤 | 1. PUT /perm/status?id=5001&status=FROZEN 2. 调用POST /auth/login登录test_all 3. 验证permInfo.disabledDetailByTenant列表 |
| 请求参数 | id=5001, status=FROZEN |
| 预期结果 | login响应中该权限出现在禁用列表 |
| 验证SQL | `SELECT status FROM sys_perm WHERE id=5001;` |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 50 / 100 / 200 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-6.10.1 | 正常负载 | 50 | 60s | ≥200 | ≤100ms | ≤0.1% |
| PT-6.10.2 | 高负载 | 100 | 120s | ≥250 | ≤300ms | ≤1% |
| PT-6.10.3 | 峰值负载 | 200 | 60s | ≥300 | ≤800ms | ≤5% |

##### Apifox压测设置说明

1. 准备N个测试权限作为压测数据
2. 添加PUT /perm/status请求，参数化id和status
3. 设置断言：code=200
4. 压测后清理：恢复权限状态为ACTIVE

---

### 6.11 DELETE /perm/delete 删除权限

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /perm/delete |
| HTTP方法 | DELETE |
| 接口描述 | 删除权限信息（逻辑删除） |
| 是否需要认证 | 是 |
| 权限要求 | 权限管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |

##### 查询参数 (Query)

| 参数名 | 类型 | 是否必填 | 约束条件 | 说明 | 示例值 |
|--------|------|---------|---------|------|--------|
| id | String | 是 | @NotBlank | 权限ID | 5001 |

##### 请求示例

```bash
curl -X DELETE 'http://localhost:8081/NexusIxService/perm/delete?id=5001' \
  -H 'NexusIX: abc123def456'
```

#### 业务场景测试用例

##### 测试用例 TC-6.11.1: 正常删除权限

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证删除已存在的权限成功（逻辑删除） |
| 前置条件 | 已新增测试权限PERM_TEST_001并获取其id |
| 操作步骤 | 1. 发送DELETE /perm/delete?id={id} 2. 验证数据库逻辑删除标记 |
| 请求参数 | id={测试权限id} |
| 预期结果 | code=200，data返回影响行数1 |
| 验证SQL | `SELECT is_deleted FROM sys_perm WHERE id={id};` 预期is_deleted=DELETED |

##### 测试用例 TC-6.11.2: 删除不存在的权限

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证删除不存在的权限id时返回业务错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送DELETE /perm/delete?id=999999 |
| 请求参数 | id=999999 |
| 预期结果 | code=500，msg包含权限不存在的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-6.11.3: id为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证id为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送DELETE /perm/delete（不传id） |
| 请求参数 | 不传id参数 |
| 预期结果 | code=500，msg包含"Id不能为空"的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 50 / 100 / 200 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-6.11.1 | 正常负载 | 50 | 60s | ≥200 | ≤100ms | ≤0.1% |
| PT-6.11.2 | 高负载 | 100 | 120s | ≥250 | ≤300ms | ≤1% |
| PT-6.11.3 | 峰值负载 | 200 | 60s | ≥300 | ≤800ms | ≤5% |

##### Apifox压测设置说明

1. 创建测试场景，先批量新增测试权限
2. 添加DELETE /perm/delete请求，参数化id
3. 设置断言：code=200

---

### 6.12 POST /perm/batch 批量新增权限

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /perm/batch |
| HTTP方法 | POST |
| 接口描述 | 批量新增权限信息 |
| 是否需要认证 | 是 |
| 权限要求 | 权限管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |
| Content-Type | String | 是 | 内容类型 | application/json |

##### 请求体 (Body)

请求体为 `List<SysPermAddRTO>` 数组，每个元素字段同[6.8 POST /perm/add](#68-post-permadd-新增权限)请求体。集合不能为空（@NotEmpty）。

##### 请求示例

```json
[
  {
    "permCode": "PERM_BATCH_1",
    "permName": "批量权限1",
    "permType": "MENU",
    "parentCode": "0",
    "status": "ACTIVE"
  },
  {
    "permCode": "PERM_BATCH_2",
    "permName": "批量权限2",
    "permType": "BUTTON",
    "parentCode": "0",
    "status": "ACTIVE"
  }
]
```

```bash
curl -X POST 'http://localhost:8081/NexusIxService/perm/batch' \
  -H 'NexusIX: abc123def456' \
  -H 'Content-Type: application/json' \
  -d '[{"permCode":"PERM_BATCH_1","permName":"批量权限1","permType":"MENU","parentCode":"0","status":"ACTIVE"},{"permCode":"PERM_BATCH_2","permName":"批量权限2","permType":"BUTTON","parentCode":"0","status":"ACTIVE"}]'
```

#### 业务场景测试用例

##### 测试用例 TC-6.12.1: 正常批量新增权限

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证批量新增2个权限成功 |
| 前置条件 | 已登录获取有效token，PERM_BATCH_1和PERM_BATCH_2不存在 |
| 操作步骤 | 1. 发送POST /perm/batch，body为2个权限对象数组 2. 验证响应与数据库 |
| 请求参数 | body=2个权限对象数组 |
| 预期结果 | code=200，data返回影响行数 |
| 验证SQL | `SELECT * FROM sys_perm WHERE perm_code IN ('PERM_BATCH_1','PERM_BATCH_2') AND is_deleted='NOT_DELETED';` 预期返回2条 |

##### 测试用例 TC-6.12.2: 集合为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证请求体为空数组时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /perm/batch，body=[] |
| 请求参数 | body=[] |
| 预期结果 | code=500，msg包含集合不能为空的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-6.12.3: 集合内单条记录校验失败

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证集合中存在不合法记录时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /perm/batch，body包含permName长度=1的非法记录 |
| 请求参数 | body=[{合法对象},{permName:"A"}] |
| 预期结果 | code=500，msg包含权限名称长度2-100的校验错误 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 30 / 80 / 150 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-6.12.1 | 正常负载 | 30 | 60s | ≥150 | ≤200ms | ≤0.5% |
| PT-6.12.2 | 高负载 | 80 | 120s | ≥180 | ≤500ms | ≤1% |
| PT-6.12.3 | 峰值负载 | 150 | 60s | ≥200 | ≤1000ms | ≤5% |

##### Apifox压测设置说明

1. 创建测试场景，准备N组不同的permCode数据集
2. 添加POST /perm/batch请求，body参数化引用数据集
3. 设置断言：code=200
4. 压测后清理：DELETE /perm/delete批量删除新增权限

---

### 6.13 PUT /perm/batch 批量修改权限

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /perm/batch |
| HTTP方法 | PUT |
| 接口描述 | 批量修改权限信息 |
| 是否需要认证 | 是 |
| 权限要求 | 权限管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |
| Content-Type | String | 是 | 内容类型 | application/json |

##### 请求体 (Body)

请求体为 `List<SysPermUpdateRTO>` 数组，每个元素字段同[6.9 PUT /perm/update](#69-put-permupdate-修改权限)请求体。注意：批量修改接口未声明@NotEmpty。

##### 请求示例

```json
[
  {
    "permCode": "PERM_BATCH_1",
    "permName": "批量权限1-更新",
    "permType": "MENU",
    "parentCode": "0",
    "status": "ACTIVE"
  },
  {
    "permCode": "PERM_BATCH_2",
    "permName": "批量权限2-更新",
    "permType": "BUTTON",
    "parentCode": "0",
    "status": "FROZEN"
  }
]
```

```bash
curl -X PUT 'http://localhost:8081/NexusIxService/perm/batch' \
  -H 'NexusIX: abc123def456' \
  -H 'Content-Type: application/json' \
  -d '[{"permCode":"PERM_BATCH_1","permName":"批量权限1-更新","permType":"MENU","parentCode":"0","status":"ACTIVE"},{"permCode":"PERM_BATCH_2","permName":"批量权限2-更新","permType":"BUTTON","parentCode":"0","status":"FROZEN"}]'
```

#### 业务场景测试用例

##### 测试用例 TC-6.13.1: 正常批量修改权限

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证批量修改2个权限成功 |
| 前置条件 | 已新增PERM_BATCH_1和PERM_BATCH_2 |
| 操作步骤 | 1. 发送PUT /perm/batch 2. 验证响应与数据库 |
| 请求参数 | body=2个修改对象数组 |
| 预期结果 | code=200，data返回影响行数 |
| 验证SQL | `SELECT perm_name,status FROM sys_perm WHERE perm_code IN ('PERM_BATCH_1','PERM_BATCH_2');` 预期名称已更新 |

##### 测试用例 TC-6.13.2: 批量修改中包含不存在permCode

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证批量修改中包含不存在的permCode时返回业务错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送PUT /perm/batch，body包含permCode=PERM_NOT_EXIST |
| 请求参数 | body=[{permCode:"PERM_NOT_EXIST",permName:"不存在",permType:"MENU","parentCode":"0","status":"ACTIVE"}] |
| 预期结果 | code=500，msg包含权限不存在的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 30 / 80 / 150 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-6.13.1 | 正常负载 | 30 | 60s | ≥150 | ≤200ms | ≤0.5% |
| PT-6.13.2 | 高负载 | 80 | 120s | ≥180 | ≤500ms | ≤1% |
| PT-6.13.3 | 峰值负载 | 150 | 60s | ≥200 | ≤1000ms | ≤5% |

##### Apifox压测设置说明

1. 准备N条测试权限作为压测数据
2. 添加PUT /perm/batch请求，body参数化引用数据集
3. 设置断言：code=200

---

### 6.14 PUT /perm/status/batch 批量更新权限状态

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /perm/status/batch |
| HTTP方法 | PUT |
| 接口描述 | 批量更新多个指定权限的状态（ACTIVE/FROZEN） |
| 是否需要认证 | 是 |
| 权限要求 | 权限管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |
| Content-Type | String | 是 | 内容类型 | application/json |

##### 查询参数 (Query)

| 参数名 | 类型 | 是否必填 | 约束条件 | 说明 | 示例值 |
|--------|------|---------|---------|------|--------|
| status | String | 是 | @NotBlank | 权限状态（ACTIVE/FROZEN） | FROZEN |

##### 请求体 (Body)

请求体为 `List<String>` 类型，元素为权限ID。集合不能为空（@NotEmpty）。

##### 请求示例

```json
["5001", "5002", "5003"]
```

```bash
curl -X PUT 'http://localhost:8081/NexusIxService/perm/status/batch?status=FROZEN' \
  -H 'NexusIX: abc123def456' \
  -H 'Content-Type: application/json' \
  -d '["5001","5002","5003"]'
```

#### 业务场景测试用例

##### 测试用例 TC-6.14.1: 正常批量冻结权限

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证批量冻结3个权限成功 |
| 前置条件 | 已新增3个测试权限并获取其id |
| 操作步骤 | 1. 发送PUT /perm/status/batch?status=FROZEN，body=[id1,id2,id3] 2. 验证数据库 |
| 请求参数 | status=FROZEN, body=[id1,id2,id3] |
| 预期结果 | code=200，data返回影响行数3 |
| 验证SQL | `SELECT status FROM sys_perm WHERE id IN (id1,id2,id3);` 预期status=FROZEN |

##### 测试用例 TC-6.14.2: 集合为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证ids集合为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送PUT /perm/status/batch?status=FROZEN，body=[] |
| 请求参数 | status=FROZEN, body=[] |
| 预期结果 | code=500，msg包含"权限ID集合不能为空"的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-6.14.3: status为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证status为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送PUT /perm/status/batch（不传status），body=["5001"] |
| 请求参数 | 不传status参数 |
| 预期结果 | code=500，msg包含"状态不能为空"的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 50 / 100 / 200 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-6.14.1 | 正常负载 | 50 | 60s | ≥200 | ≤100ms | ≤0.1% |
| PT-6.14.2 | 高负载 | 100 | 120s | ≥250 | ≤300ms | ≤1% |
| PT-6.14.3 | 峰值负载 | 200 | 60s | ≥300 | ≤800ms | ≤5% |

##### Apifox压测设置说明

1. 准备N个测试权限id集合
2. 添加PUT /perm/status/batch请求，body参数化id集合
3. 设置断言：code=200
4. 压测后清理：恢复权限状态为ACTIVE

---

### 6.15 DELETE /perm/batch 批量删除权限

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /perm/batch |
| HTTP方法 | DELETE |
| 接口描述 | 批量删除权限信息（逻辑删除） |
| 是否需要认证 | 是 |
| 权限要求 | 权限管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |
| Content-Type | String | 是 | 内容类型 | application/json |

##### 请求体 (Body)

请求体为 `List<String>` 类型，元素为权限ID。集合不能为空（@NotEmpty）。

##### 请求示例

```json
["5001", "5002", "5003"]
```

```bash
curl -X DELETE 'http://localhost:8081/NexusIxService/perm/batch' \
  -H 'NexusIX: abc123def456' \
  -H 'Content-Type: application/json' \
  -d '["5001","5002","5003"]'
```

#### 业务场景测试用例

##### 测试用例 TC-6.15.1: 正常批量删除权限

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证批量删除3个权限成功（逻辑删除） |
| 前置条件 | 已新增3个测试权限并获取其id |
| 操作步骤 | 1. 发送DELETE /perm/batch，body=[id1,id2,id3] 2. 验证数据库逻辑删除标记 |
| 请求参数 | body=[id1,id2,id3] |
| 预期结果 | code=200，data返回影响行数3 |
| 验证SQL | `SELECT is_deleted FROM sys_perm WHERE id IN (id1,id2,id3);` 预期is_deleted=DELETED |

##### 测试用例 TC-6.15.2: 集合为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证ids集合为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送DELETE /perm/batch，body=[] |
| 请求参数 | body=[] |
| 预期结果 | code=500，msg包含"权限ID集合不能为空"的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-6.15.3: 已删除权限重复删除

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证对已逻辑删除的权限再次删除时返回业务错误 |
| 前置条件 | 已删除测试权限（is_deleted=DELETED） |
| 操作步骤 | 1. 发送DELETE /perm/batch，body=[已删除权限id] |
| 请求参数 | body=[已删除权限id] |
| 预期结果 | code=500，msg包含权限不存在的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 50 / 100 / 200 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-6.15.1 | 正常负载 | 50 | 60s | ≥200 | ≤100ms | ≤0.1% |
| PT-6.15.2 | 高负载 | 100 | 120s | ≥250 | ≤300ms | ≤1% |
| PT-6.15.3 | 峰值负载 | 200 | 60s | ≥300 | ≤800ms | ≤5% |

##### Apifox压测设置说明

1. 准备N个测试权限作为压测数据
2. 添加DELETE /perm/batch请求，body参数化id集合
3. 设置断言：code=200
4. 压测后无需清理（已逻辑删除）

---

## 7. SysPermPolicyController 权限策略管理接口测试

> Controller路径：`nexusix-iam\src\main\java\com\shy\nexusix\iam\controller\SysPermPolicyController.java`
> 接口前缀：`/perm-policy`
> 共14个接口（实际实现，原任务描述为16个）
> 特别说明：本Controller涉及`field_operation` JSONB字段级权限配置，需重点测试

### 7.1 GET /perm-policy/list 查询权限策略列表

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /perm-policy/list |
| HTTP方法 | GET |
| 接口描述 | 返回所有权限策略列表 |
| 是否需要认证 | 是 |
| 权限要求 | 权限策略管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |

##### 请求参数

无

##### 请求示例

```bash
curl -X GET 'http://localhost:8081/NexusIxService/perm-policy/list' \
  -H 'NexusIX: abc123def456'
```

#### 业务场景测试用例

##### 测试用例 TC-7.1.1: 正常查询权限策略列表

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证已登录用户成功获取权限策略列表 |
| 前置条件 | 已登录获取有效token，数据库存在权限策略数据 |
| 操作步骤 | 1. 发送GET /perm-policy/list 2. 验证响应数据结构 |
| 请求参数 | 无 |
| 预期结果 | code=200，data为权限策略列表数组，每条记录包含id/policyCode/policyName/permId/targetType/targetId/dataScope/tableName/fieldOperation/status等字段 |
| 验证SQL | `SELECT count(*) FROM sys_perm_policy WHERE is_deleted='NOT_DELETED';` 与响应列表数量一致 |

##### 测试用例 TC-7.1.2: 未携带Token

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证未携带Token时返回未登录错误 |
| 前置条件 | 不携带Token |
| 操作步骤 | 1. 发送GET /perm-policy/list，不设置NexusIX Header |
| 请求参数 | 无 |
| 预期结果 | code=401，msg包含未登录的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-7.1.3: field_operation字段结构验证

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证返回的策略记录中field_operation字段为合法JSON对象 |
| 前置条件 | 已登录获取有效token，数据库存在field_operation非空的策略（如PP_001） |
| 操作步骤 | 1. 发送GET /perm-policy/list 2. 检查PP_001的fieldOperation字段 |
| 请求参数 | 无 |
| 预期结果 | code=200，fieldOperation为JSON对象，键为表名字段名，值为操作数组（如["READ","CREATE","UPDATE"]） |
| 验证SQL | `SELECT field_operation FROM sys_perm_policy WHERE policy_code='PP_001';` |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 100 / 200 / 500 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-7.1.1 | 正常负载 | 100 | 60s | ≥250 | ≤100ms | ≤0.1% |
| PT-7.1.2 | 高负载 | 200 | 120s | ≥300 | ≤200ms | ≤1% |
| PT-7.1.3 | 峰值负载 | 500 | 60s | ≥400 | ≤800ms | ≤5% |

##### Apifox压测设置说明

1. 创建测试场景，添加GET /perm-policy/list请求
2. 设置全局变量Token
3. 设置断言：code=200 且 data不为空

---

### 7.2 GET /perm-policy/page 分页查询权限策略列表

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /perm-policy/page |
| HTTP方法 | GET |
| 接口描述 | 返回分页后的权限策略列表 |
| 是否需要认证 | 是 |
| 权限要求 | 权限策略管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |

##### 查询参数 (Query)

| 参数名 | 类型 | 是否必填 | 约束条件 | 说明 | 示例值 |
|--------|------|---------|---------|------|--------|
| pageNum | Integer | 否 | @Min(1)，默认1 | 页码 | 1 |
| pageSize | Integer | 否 | @Min(1)@Max(100)，默认10 | 每页条数 | 10 |

##### 请求示例

```bash
curl -X GET 'http://localhost:8081/NexusIxService/perm-policy/page?pageNum=1&pageSize=10' \
  -H 'NexusIX: abc123def456'
```

#### 业务场景测试用例

##### 测试用例 TC-7.2.1: 正常分页查询

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证默认分页查询成功 |
| 前置条件 | 已登录获取有效token，数据库存在权限策略数据 |
| 操作步骤 | 1. 发送GET /perm-policy/page?pageNum=1&pageSize=10 |
| 请求参数 | pageNum=1, pageSize=10 |
| 预期结果 | code=200，data包含records/total/current/size字段，records长度≤10 |
| 验证SQL | `SELECT count(*) FROM sys_perm_policy WHERE is_deleted='NOT_DELETED';` 与total一致 |

##### 测试用例 TC-7.2.2: pageSize超出上限

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证pageSize=200时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送GET /perm-policy/page?pageNum=1&pageSize=200 |
| 请求参数 | pageNum=1, pageSize=200 |
| 预期结果 | code=500，msg包含pageSize最大100的校验错误 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 100 / 200 / 500 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-7.2.1 | 正常负载 | 100 | 60s | ≥300 | ≤50ms | ≤0.1% |
| PT-7.2.2 | 高负载 | 200 | 120s | ≥400 | ≤150ms | ≤1% |
| PT-7.2.3 | 峰值负载 | 500 | 60s | ≥500 | ≤500ms | ≤5% |

##### Apifox压测设置说明

1. 参数化pageNum和pageSize，模拟翻页
2. 设置断言：code=200

---

### 7.3 POST /perm-policy/query 条件查询权限策略列表

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /perm-policy/query |
| HTTP方法 | POST |
| 接口描述 | 返回满足条件的权限策略列表（分页） |
| 是否需要认证 | 是 |
| 权限要求 | 权限策略管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |
| Content-Type | String | 是 | 内容类型 | application/json |

##### 请求体 (Body)

SysPermPolicyQueryRTO 字段（含分页参数）：

| 字段名 | 类型 | 是否必填 | 约束条件 | 说明 | 示例值 |
|--------|------|---------|---------|------|--------|
| pageNum | Integer | 是 | @NotNull @Min(1) | 页码 | 1 |
| pageSize | Integer | 是 | @NotNull @Min(1) | 每页条数 | 10 |
| policyCode | String | 否 | 无 | 策略编码 | PP_001 |
| policyName | String | 否 | 无 | 策略名称 | 用户管理 |
| permId | Long | 否 | 无 | 权限ID | 511 |
| targetType | String | 否 | 无 | 目标类型（TENANT/DEPT/ROLE/USER） | TENANT |
| targetId | Long | 否 | 无 | 目标ID | 101 |
| status | String | 否 | 无 | 状态 | ACTIVE |

##### 请求示例

```json
{
  "pageNum": 1,
  "pageSize": 10,
  "targetType": "TENANT",
  "status": "ACTIVE"
}
```

```bash
curl -X POST 'http://localhost:8081/NexusIxService/perm-policy/query' \
  -H 'NexusIX: abc123def456' \
  -H 'Content-Type: application/json' \
  -d '{"pageNum":1,"pageSize":10,"targetType":"TENANT","status":"ACTIVE"}'
```

#### 业务场景测试用例

##### 测试用例 TC-7.3.1: 按目标类型查询

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证按targetType=TENANT查询租户级策略 |
| 前置条件 | 已登录获取有效token，数据库存在TENANT级策略 |
| 操作步骤 | 1. 发送POST /perm-policy/query，body包含targetType=TENANT |
| 请求参数 | body={"pageNum":1,"pageSize":10,"targetType":"TENANT"} |
| 预期结果 | code=200，data.records中所有策略target_type=TENANT |
| 验证SQL | `SELECT count(*) FROM sys_perm_policy WHERE target_type='TENANT' AND is_deleted='NOT_DELETED';` |

##### 测试用例 TC-7.3.2: 按权限ID查询

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证按permId查询权限策略 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /perm-policy/query，body包含permId=511 |
| 请求参数 | body={"pageNum":1,"pageSize":10,"permId":511} |
| 预期结果 | code=200，data.records中所有策略perm_id=511或继承自511的策略 |
| 验证SQL | `SELECT * FROM sys_perm_policy WHERE perm_id=511 AND is_deleted='NOT_DELETED';` |

##### 测试用例 TC-7.3.3: pageNum为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证pageNum为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /perm-policy/query，body不包含pageNum |
| 请求参数 | body={"pageSize":10} |
| 预期结果 | code=500，msg包含pageNum不能为空的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 100 / 200 / 500 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-7.3.1 | 正常负载 | 100 | 60s | ≥250 | ≤100ms | ≤0.1% |
| PT-7.3.2 | 高负载 | 200 | 120s | ≥300 | ≤200ms | ≤1% |
| PT-7.3.3 | 峰值负载 | 500 | 60s | ≥400 | ≤800ms | ≤5% |

##### Apifox压测设置说明

1. 参数化查询条件，覆盖目标类型/权限ID/状态组合
2. 设置断言：code=200

---

### 7.4 GET /perm-policy/detail/{policyCode} 查询权限策略详情

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /perm-policy/detail/{policyCode} |
| HTTP方法 | GET |
| 接口描述 | 返回指定权限策略的详情信息 |
| 是否需要认证 | 是 |
| 权限要求 | 权限策略管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |

##### 路径参数 (Path)

| 参数名 | 类型 | 是否必填 | 约束条件 | 说明 | 示例值 |
|--------|------|---------|---------|------|--------|
| policyCode | String | 是 | @NotBlank | 策略编码 | PP_001 |

##### 请求示例

```bash
curl -X GET 'http://localhost:8081/NexusIxService/perm-policy/detail/PP_001' \
  -H 'NexusIX: abc123def456'
```

#### 业务场景测试用例

##### 测试用例 TC-7.4.1: 正常查询策略详情

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证查询已存在策略详情成功 |
| 前置条件 | 已登录获取有效token，策略PP_001存在 |
| 操作步骤 | 1. 发送GET /perm-policy/detail/PP_001 |
| 请求参数 | policyCode=PP_001 |
| 预期结果 | code=200，data包含id/policyCode/policyName/permId/targetType/targetId/dataScope/tableName/fieldOperation/status/disableReason/createTenant等完整字段 |
| 验证SQL | `SELECT * FROM sys_perm_policy WHERE policy_code='PP_001' AND is_deleted='NOT_DELETED';` |

##### 测试用例 TC-7.4.2: policyCode为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证policyCode为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送GET /perm-policy/detail/（路径参数为空） |
| 请求参数 | policyCode为空 |
| 预期结果 | code=500，msg包含"策略编码不能为空"的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-7.4.3: 查询不存在的策略编码

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证查询不存在的策略编码时返回业务错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送GET /perm-policy/detail/PP_NOT_EXIST |
| 请求参数 | policyCode=PP_NOT_EXIST |
| 预期结果 | code=500，msg包含策略不存在的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 100 / 200 / 500 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-7.4.1 | 正常负载 | 100 | 60s | ≥300 | ≤50ms | ≤0.1% |
| PT-7.4.2 | 高负载 | 200 | 120s | ≥400 | ≤150ms | ≤1% |
| PT-7.4.3 | 峰值负载 | 500 | 60s | ≥500 | ≤500ms | ≤5% |

##### Apifox压测设置说明

1. 参数化policyCode，覆盖TENANT/DEPT/ROLE/USER级策略
2. 设置断言：code=200

---

### 7.5 POST /perm-policy/add 新增权限策略

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /perm-policy/add |
| HTTP方法 | POST |
| 接口描述 | 新增权限策略信息 |
| 是否需要认证 | 是 |
| 权限要求 | 权限策略管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |
| Content-Type | String | 是 | 内容类型 | application/json |

##### 请求体 (Body)

SysPermPolicyAddRTO 字段校验规则：

| 字段名 | 类型 | 是否必填 | 约束条件 | 说明 | 示例值 |
|--------|------|---------|---------|------|--------|
| policyCode | String | 是 | @NotBlank @Size(max=100) | 策略编码 | PP_TEST_001 |
| policyName | String | 是 | @Size(2-100) | 策略名称 | 测试策略 |
| permId | Long | 是 | @NotNull | 权限ID（可指向sys_perm.id或父策略id） | 511 |
| targetType | String | 是 | @NotBlank | 目标类型（TENANT/DEPT/ROLE/USER） | TENANT |
| targetId | Long | 是 | @NotNull | 目标ID | 101 |
| dataScope | String | 否 | 无 | 行级权限（ALL/DEPT/DEPT_AND_SUB/SELF） | ALL |
| tableName | String | 否 | 无 | 关联的数据表名 | sys_user |
| fieldPermissions | String | 否 | 无 | 字段级权限配置（JSON字符串） | {"email":["READ","UPDATE"]} |
| status | String | 否 | 无 | 状态 | ACTIVE |

##### 请求示例

```json
{
  "policyCode": "PP_TEST_001",
  "policyName": "测试策略",
  "permId": 511,
  "targetType": "TENANT",
  "targetId": 101,
  "dataScope": "ALL",
  "tableName": "sys_user",
  "fieldPermissions": "{\"email\":[\"READ\",\"UPDATE\"],\"phone\":[\"READ\"]}",
  "status": "ACTIVE"
}
```

```bash
curl -X POST 'http://localhost:8081/NexusIxService/perm-policy/add' \
  -H 'NexusIX: abc123def456' \
  -H 'Content-Type: application/json' \
  -d '{"policyCode":"PP_TEST_001","policyName":"测试策略","permId":511,"targetType":"TENANT","targetId":101,"dataScope":"ALL","tableName":"sys_user","fieldPermissions":"{\"email\":[\"READ\",\"UPDATE\"]}","status":"ACTIVE"}'
```

#### 业务场景测试用例

##### 测试用例 TC-7.5.1: 正常新增权限策略（含field_operation）

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证新增包含field_operation字段级权限的策略成功 |
| 前置条件 | 已登录获取有效token，PP_TEST_001不存在，permId=511存在 |
| 操作步骤 | 1. 发送POST /perm-policy/add 2. 验证响应与数据库 |
| 请求参数 | body=策略对象（含fieldPermissions） |
| 预期结果 | code=200，data返回影响行数1，field_operation字段正确写入 |
| 验证SQL | `SELECT field_operation FROM sys_perm_policy WHERE policy_code='PP_TEST_001';` 预期为JSON对象 |

##### 测试用例 TC-7.5.2: policyCode为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证policyCode为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /perm-policy/add，body不包含policyCode |
| 请求参数 | body={"policyName":"测试","permId":511,"targetType":"TENANT","targetId":101} |
| 预期结果 | code=500，msg包含"策略编码不能为空"的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-7.5.3: permId为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证permId为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /perm-policy/add，body不包含permId |
| 请求参数 | body={"policyCode":"PP_TEST","policyName":"测试","targetType":"TENANT","targetId":101} |
| 预期结果 | code=500，msg包含"权限ID不能为空"的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-7.5.4: policyCode重复

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证policyCode已存在时返回业务错误 |
| 前置条件 | 已登录获取有效token，PP_001已存在 |
| 操作步骤 | 1. 发送POST /perm-policy/add，body包含policyCode=PP_001 |
| 请求参数 | body={"policyCode":"PP_001","policyName":"重复","permId":511,"targetType":"TENANT","targetId":101} |
| 预期结果 | code=500，msg包含策略编码已存在的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-7.5.5: field_operation非法JSON

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证fieldPermissions为非法JSON字符串时返回业务错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /perm-policy/add，body包含fieldPermissions="{invalid json" |
| 请求参数 | body={...,"fieldPermissions":"{invalid json"} |
| 预期结果 | code=500，msg包含JSON解析错误的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 50 / 100 / 200 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-7.5.1 | 正常负载 | 50 | 60s | ≥150 | ≤150ms | ≤0.5% |
| PT-7.5.2 | 高负载 | 100 | 120s | ≥200 | ≤300ms | ≤1% |
| PT-7.5.3 | 峰值负载 | 200 | 60s | ≥250 | ≤800ms | ≤5% |

##### Apifox压测设置说明

1. 准备N组不同的policyCode数据集
2. 添加POST /perm-policy/add请求，body参数化引用数据集
3. 设置断言：code=200
4. 压测后清理：DELETE /perm-policy/delete批量删除新增策略

---

### 7.6 PUT /perm-policy/update 修改权限策略

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /perm-policy/update |
| HTTP方法 | PUT |
| 接口描述 | 修改权限策略信息 |
| 是否需要认证 | 是 |
| 权限要求 | 权限策略管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |
| Content-Type | String | 是 | 内容类型 | application/json |

##### 请求体 (Body)

SysPermPolicyUpdateRTO 字段：policyCode @NotBlank（定位用），其他字段可选（policyName/permId/targetType/targetId/dataScope/tableName/fieldPermissions/status）。

##### 请求示例

```json
{
  "policyCode": "PP_TEST_001",
  "policyName": "测试策略-更新",
  "dataScope": "DEPT",
  "fieldPermissions": "{\"email\":[\"READ\"]}",
  "status": "ACTIVE"
}
```

```bash
curl -X PUT 'http://localhost:8081/NexusIxService/perm-policy/update' \
  -H 'NexusIX: abc123def456' \
  -H 'Content-Type: application/json' \
  -d '{"policyCode":"PP_TEST_001","policyName":"测试策略-更新","dataScope":"DEPT","fieldPermissions":"{\"email\":[\"READ\"]}","status":"ACTIVE"}'
```

#### 业务场景测试用例

##### 测试用例 TC-7.6.1: 正常修改策略

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证修改已存在策略成功 |
| 前置条件 | 已新增PP_TEST_001策略 |
| 操作步骤 | 1. 发送PUT /perm-policy/update 2. 验证响应与数据库 |
| 请求参数 | body=策略修改对象 |
| 预期结果 | code=200，data返回影响行数1 |
| 验证SQL | `SELECT policy_name,data_scope FROM sys_perm_policy WHERE policy_code='PP_TEST_001';` 预期已更新 |

##### 测试用例 TC-7.6.2: 修改不存在的策略

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证修改不存在的策略时返回业务错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送PUT /perm-policy/update，body包含policyCode=PP_NOT_EXIST |
| 请求参数 | body={"policyCode":"PP_NOT_EXIST","policyName":"不存在"} |
| 预期结果 | code=500，msg包含策略不存在的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-7.6.3: policyCode为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证policyCode为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送PUT /perm-policy/update，body不包含policyCode |
| 请求参数 | body={"policyName":"测试"} |
| 预期结果 | code=500，msg包含"策略编码不能为空"的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 50 / 100 / 200 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-7.6.1 | 正常负载 | 50 | 60s | ≥150 | ≤150ms | ≤0.5% |
| PT-7.6.2 | 高负载 | 100 | 120s | ≥200 | ≤300ms | ≤1% |
| PT-7.6.3 | 峰值负载 | 200 | 60s | ≥250 | ≤800ms | ≤5% |

##### Apifox压测设置说明

1. 准备N条测试策略作为压测数据
2. 添加PUT /perm-policy/update请求，body参数化引用数据集
3. 设置断言：code=200

---

### 7.7 PUT /perm-policy/status 更新权限策略状态

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /perm-policy/status |
| HTTP方法 | PUT |
| 接口描述 | 更新指定权限策略的状态（ACTIVE/DISABLED） |
| 是否需要认证 | 是 |
| 权限要求 | 权限策略管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |

##### 查询参数 (Query)

| 参数名 | 类型 | 是否必填 | 约束条件 | 说明 | 示例值 |
|--------|------|---------|---------|------|--------|
| policyCode | String | 是 | @NotBlank | 策略编码 | PP_001 |
| status | String | 是 | @NotBlank | 策略状态（ACTIVE/DISABLED） | DISABLED |

##### 请求示例

```bash
curl -X PUT 'http://localhost:8081/NexusIxService/perm-policy/status?policyCode=PP_001&status=DISABLED' \
  -H 'NexusIX: abc123def456'
```

#### 业务场景测试用例

##### 测试用例 TC-7.7.1: 正常禁用策略

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证禁用权限策略成功 |
| 前置条件 | 已登录获取有效token，策略PP_001状态为ACTIVE |
| 操作步骤 | 1. 发送PUT /perm-policy/status?policyCode=PP_001&status=DISABLED 2. 验证数据库 |
| 请求参数 | policyCode=PP_001, status=DISABLED |
| 预期结果 | code=200，data返回影响行数1 |
| 验证SQL | `SELECT status FROM sys_perm_policy WHERE policy_code='PP_001';` 预期status=DISABLED |

##### 测试用例 TC-7.7.2: 禁用策略影响用户登录权限

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证禁用TENANT级策略后，关联用户登录时该权限出现在禁用列表 |
| 前置条件 | PP_001为TENANT级策略（target_id=101），test_all用户绑定TENANT_A |
| 操作步骤 | 1. PUT /perm-policy/status?policyCode=PP_001&status=DISABLED 2. POST /auth/login登录test_all 3. 验证permInfo.disabledDetailByTenant.TENANT中包含PP_001对应权限 |
| 请求参数 | policyCode=PP_001, status=DISABLED |
| 预期结果 | code=200，login响应中该权限出现在禁用列表 |
| 验证SQL | `SELECT status FROM sys_perm_policy WHERE policy_code='PP_001';` |

##### 测试用例 TC-7.7.3: policyCode为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证policyCode为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送PUT /perm-policy/status?status=DISABLED（不传policyCode） |
| 请求参数 | status=DISABLED |
| 预期结果 | code=500，msg包含"策略编码不能为空"的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 50 / 100 / 200 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-7.7.1 | 正常负载 | 50 | 60s | ≥200 | ≤100ms | ≤0.1% |
| PT-7.7.2 | 高负载 | 100 | 120s | ≥250 | ≤300ms | ≤1% |
| PT-7.7.3 | 峰值负载 | 200 | 60s | ≥300 | ≤800ms | ≤5% |

##### Apifox压测设置说明

1. 准备N个测试策略作为压测数据
2. 添加PUT /perm-policy/status请求，参数化policyCode和status
3. 设置断言：code=200
4. 压测后清理：恢复策略状态为ACTIVE

---

### 7.8 DELETE /perm-policy/delete 删除权限策略

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /perm-policy/delete |
| HTTP方法 | DELETE |
| 接口描述 | 删除权限策略信息（逻辑删除） |
| 是否需要认证 | 是 |
| 权限要求 | 权限策略管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |

##### 查询参数 (Query)

| 参数名 | 类型 | 是否必填 | 约束条件 | 说明 | 示例值 |
|--------|------|---------|---------|------|--------|
| policyCode | String | 是 | @NotBlank | 策略编码 | PP_TEST_001 |

##### 请求示例

```bash
curl -X DELETE 'http://localhost:8081/NexusIxService/perm-policy/delete?policyCode=PP_TEST_001' \
  -H 'NexusIX: abc123def456'
```

#### 业务场景测试用例

##### 测试用例 TC-7.8.1: 正常删除策略

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证删除已存在的策略成功（逻辑删除） |
| 前置条件 | 已新增测试策略PP_TEST_001 |
| 操作步骤 | 1. 发送DELETE /perm-policy/delete?policyCode=PP_TEST_001 2. 验证数据库逻辑删除标记 |
| 请求参数 | policyCode=PP_TEST_001 |
| 预期结果 | code=200，data返回影响行数1 |
| 验证SQL | `SELECT is_deleted FROM sys_perm_policy WHERE policy_code='PP_TEST_001';` 预期is_deleted=DELETED |

##### 测试用例 TC-7.8.2: 删除不存在的策略

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证删除不存在的策略编码时返回业务错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送DELETE /perm-policy/delete?policyCode=PP_NOT_EXIST |
| 请求参数 | policyCode=PP_NOT_EXIST |
| 预期结果 | code=500，msg包含策略不存在的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-7.8.3: policyCode为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证policyCode为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送DELETE /perm-policy/delete（不传policyCode） |
| 请求参数 | 不传policyCode参数 |
| 预期结果 | code=500，msg包含"策略编码不能为空"的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 50 / 100 / 200 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-7.8.1 | 正常负载 | 50 | 60s | ≥200 | ≤100ms | ≤0.1% |
| PT-7.8.2 | 高负载 | 100 | 120s | ≥250 | ≤300ms | ≤1% |
| PT-7.8.3 | 峰值负载 | 200 | 60s | ≥300 | ≤800ms | ≤5% |

##### Apifox压测设置说明

1. 创建测试场景，先批量新增测试策略
2. 添加DELETE /perm-policy/delete请求，参数化policyCode
3. 设置断言：code=200

---

### 7.9 POST /perm-policy/batch 批量新增权限策略

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /perm-policy/batch |
| HTTP方法 | POST |
| 接口描述 | 批量新增权限策略信息 |
| 是否需要认证 | 是 |
| 权限要求 | 权限策略管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |
| Content-Type | String | 是 | 内容类型 | application/json |

##### 请求体 (Body)

请求体为 `List<SysPermPolicyAddRTO>` 数组，每个元素字段同[7.5 POST /perm-policy/add](#75-post-perm-policyadd-新增权限策略)请求体。集合不能为空（@NotEmpty）。

##### 请求示例

```json
[
  {
    "policyCode": "PP_BATCH_1",
    "policyName": "批量策略1",
    "permId": 511,
    "targetType": "TENANT",
    "targetId": 101,
    "status": "ACTIVE"
  },
  {
    "policyCode": "PP_BATCH_2",
    "policyName": "批量策略2",
    "permId": 521,
    "targetType": "ROLE",
    "targetId": 3001,
    "status": "ACTIVE"
  }
]
```

```bash
curl -X POST 'http://localhost:8081/NexusIxService/perm-policy/batch' \
  -H 'NexusIX: abc123def456' \
  -H 'Content-Type: application/json' \
  -d '[{"policyCode":"PP_BATCH_1","policyName":"批量策略1","permId":511,"targetType":"TENANT","targetId":101,"status":"ACTIVE"},{"policyCode":"PP_BATCH_2","policyName":"批量策略2","permId":521,"targetType":"ROLE","targetId":3001,"status":"ACTIVE"}]'
```

#### 业务场景测试用例

##### 测试用例 TC-7.9.1: 正常批量新增策略

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证批量新增2个策略成功 |
| 前置条件 | 已登录获取有效token，PP_BATCH_1和PP_BATCH_2不存在 |
| 操作步骤 | 1. 发送POST /perm-policy/batch 2. 验证响应与数据库 |
| 请求参数 | body=2个策略对象数组 |
| 预期结果 | code=200，data返回影响行数 |
| 验证SQL | `SELECT * FROM sys_perm_policy WHERE policy_code IN ('PP_BATCH_1','PP_BATCH_2') AND is_deleted='NOT_DELETED';` 预期返回2条 |

##### 测试用例 TC-7.9.2: 集合为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证请求体为空数组时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /perm-policy/batch，body=[] |
| 请求参数 | body=[] |
| 预期结果 | code=500，msg包含集合不能为空的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-7.9.3: 集合内单条记录校验失败

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证集合中存在policyName长度=1的非法记录时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /perm-policy/batch，body包含policyName="A"的非法记录 |
| 请求参数 | body=[{合法对象},{policyName:"A"}] |
| 预期结果 | code=500，msg包含策略名称长度2-100的校验错误 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 30 / 80 / 150 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-7.9.1 | 正常负载 | 30 | 60s | ≥100 | ≤200ms | ≤0.5% |
| PT-7.9.2 | 高负载 | 80 | 120s | ≥150 | ≤500ms | ≤1% |
| PT-7.9.3 | 峰值负载 | 150 | 60s | ≥200 | ≤1000ms | ≤5% |

##### Apifox压测设置说明

1. 创建测试场景，准备N组不同的policyCode数据集
2. 添加POST /perm-policy/batch请求，body参数化引用数据集
3. 设置断言：code=200
4. 压测后清理：DELETE /perm-policy/delete批量删除新增策略

---

### 7.10 PUT /perm-policy/batch 批量修改权限策略

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /perm-policy/batch |
| HTTP方法 | PUT |
| 接口描述 | 批量修改权限策略信息 |
| 是否需要认证 | 是 |
| 权限要求 | 权限策略管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |
| Content-Type | String | 是 | 内容类型 | application/json |

##### 请求体 (Body)

请求体为 `List<SysPermPolicyUpdateRTO>` 数组，每个元素字段同[7.6 PUT /perm-policy/update](#76-put-perm-policyupdate-修改权限策略)请求体。注意：批量修改接口未声明@NotEmpty。

##### 请求示例

```json
[
  {
    "policyCode": "PP_BATCH_1",
    "policyName": "批量策略1-更新",
    "status": "ACTIVE"
  },
  {
    "policyCode": "PP_BATCH_2",
    "policyName": "批量策略2-更新",
    "status": "DISABLED"
  }
]
```

```bash
curl -X PUT 'http://localhost:8081/NexusIxService/perm-policy/batch' \
  -H 'NexusIX: abc123def456' \
  -H 'Content-Type: application/json' \
  -d '[{"policyCode":"PP_BATCH_1","policyName":"批量策略1-更新","status":"ACTIVE"},{"policyCode":"PP_BATCH_2","policyName":"批量策略2-更新","status":"DISABLED"}]'
```

#### 业务场景测试用例

##### 测试用例 TC-7.10.1: 正常批量修改策略

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证批量修改2个策略成功 |
| 前置条件 | 已新增PP_BATCH_1和PP_BATCH_2 |
| 操作步骤 | 1. 发送PUT /perm-policy/batch 2. 验证响应与数据库 |
| 请求参数 | body=2个修改对象数组 |
| 预期结果 | code=200，data返回影响行数 |
| 验证SQL | `SELECT policy_name,status FROM sys_perm_policy WHERE policy_code IN ('PP_BATCH_1','PP_BATCH_2');` 预期名称已更新 |

##### 测试用例 TC-7.10.2: 批量修改中包含不存在policyCode

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证批量修改中包含不存在的policyCode时返回业务错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送PUT /perm-policy/batch，body包含policyCode=PP_NOT_EXIST |
| 请求参数 | body=[{policyCode:"PP_NOT_EXIST",policyName:"不存在"}] |
| 预期结果 | code=500，msg包含策略不存在的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 30 / 80 / 150 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-7.10.1 | 正常负载 | 30 | 60s | ≥100 | ≤200ms | ≤0.5% |
| PT-7.10.2 | 高负载 | 80 | 120s | ≥150 | ≤500ms | ≤1% |
| PT-7.10.3 | 峰值负载 | 150 | 60s | ≥200 | ≤1000ms | ≤5% |

##### Apifox压测设置说明

1. 准备N条测试策略作为压测数据
2. 添加PUT /perm-policy/batch请求，body参数化引用数据集
3. 设置断言：code=200

---

### 7.11 PUT /perm-policy/status/batch 批量更新权限策略状态

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /perm-policy/status/batch |
| HTTP方法 | PUT |
| 接口描述 | 批量更新多个指定权限策略的状态（ACTIVE/DISABLED） |
| 是否需要认证 | 是 |
| 权限要求 | 权限策略管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |
| Content-Type | String | 是 | 内容类型 | application/json |

##### 查询参数 (Query)

| 参数名 | 类型 | 是否必填 | 约束条件 | 说明 | 示例值 |
|--------|------|---------|---------|------|--------|
| status | String | 是 | @NotBlank | 策略状态（ACTIVE/DISABLED） | DISABLED |

##### 请求体 (Body)

请求体为 `List<String>` 类型，元素为策略编码（policyCode）。集合不能为空（@NotEmpty）。

##### 请求示例

```json
["PP_001", "PP_002", "PP_003"]
```

```bash
curl -X PUT 'http://localhost:8081/NexusIxService/perm-policy/status/batch?status=DISABLED' \
  -H 'NexusIX: abc123def456' \
  -H 'Content-Type: application/json' \
  -d '["PP_001","PP_002","PP_003"]'
```

#### 业务场景测试用例

##### 测试用例 TC-7.11.1: 正常批量禁用策略

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证批量禁用3个策略成功 |
| 前置条件 | 已新增3个测试策略 |
| 操作步骤 | 1. 发送PUT /perm-policy/status/batch?status=DISABLED，body=[code1,code2,code3] 2. 验证数据库 |
| 请求参数 | status=DISABLED, body=[code1,code2,code3] |
| 预期结果 | code=200，data返回影响行数3 |
| 验证SQL | `SELECT status FROM sys_perm_policy WHERE policy_code IN ('code1','code2','code3');` 预期status=DISABLED |

##### 测试用例 TC-7.11.2: 集合为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证policyCodeList集合为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送PUT /perm-policy/status/batch?status=DISABLED，body=[] |
| 请求参数 | status=DISABLED, body=[] |
| 预期结果 | code=500，msg包含"策略编码集合不能为空"的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-7.11.3: status为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证status为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送PUT /perm-policy/status/batch（不传status），body=["PP_001"] |
| 请求参数 | 不传status参数 |
| 预期结果 | code=500，msg包含"状态不能为空"的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 50 / 100 / 200 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-7.11.1 | 正常负载 | 50 | 60s | ≥150 | ≤150ms | ≤0.5% |
| PT-7.11.2 | 高负载 | 100 | 120s | ≥200 | ≤300ms | ≤1% |
| PT-7.11.3 | 峰值负载 | 200 | 60s | ≥250 | ≤800ms | ≤5% |

##### Apifox压测设置说明

1. 准备N个测试策略编码集合
2. 添加PUT /perm-policy/status/batch请求，body参数化编码集合
3. 设置断言：code=200
4. 压测后清理：恢复策略状态为ACTIVE

---

### 7.12 DELETE /perm-policy/batch 批量删除权限策略

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /perm-policy/batch |
| HTTP方法 | DELETE |
| 接口描述 | 批量删除权限策略信息（逻辑删除） |
| 是否需要认证 | 是 |
| 权限要求 | 权限策略管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |
| Content-Type | String | 是 | 内容类型 | application/json |

##### 请求体 (Body)

请求体为 `List<String>` 类型，元素为策略编码（policyCode）。集合不能为空（@NotEmpty）。

##### 请求示例

```json
["PP_BATCH_1", "PP_BATCH_2", "PP_BATCH_3"]
```

```bash
curl -X DELETE 'http://localhost:8081/NexusIxService/perm-policy/batch' \
  -H 'NexusIX: abc123def456' \
  -H 'Content-Type: application/json' \
  -d '["PP_BATCH_1","PP_BATCH_2","PP_BATCH_3"]'
```

#### 业务场景测试用例

##### 测试用例 TC-7.12.1: 正常批量删除策略

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证批量删除3个策略成功（逻辑删除） |
| 前置条件 | 已新增3个测试策略 |
| 操作步骤 | 1. 发送DELETE /perm-policy/batch，body=[code1,code2,code3] 2. 验证数据库逻辑删除标记 |
| 请求参数 | body=[code1,code2,code3] |
| 预期结果 | code=200，data返回影响行数3 |
| 验证SQL | `SELECT is_deleted FROM sys_perm_policy WHERE policy_code IN ('code1','code2','code3');` 预期is_deleted=DELETED |

##### 测试用例 TC-7.12.2: 集合为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证policyCodeList集合为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送DELETE /perm-policy/batch，body=[] |
| 请求参数 | body=[] |
| 预期结果 | code=500，msg包含"策略编码集合不能为空"的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-7.12.3: 已删除策略重复删除

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证对已逻辑删除的策略再次删除时返回业务错误 |
| 前置条件 | 已删除测试策略（is_deleted=DELETED） |
| 操作步骤 | 1. 发送DELETE /perm-policy/batch，body=[已删除策略code] |
| 请求参数 | body=[已删除策略code] |
| 预期结果 | code=500，msg包含策略不存在的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 50 / 100 / 200 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-7.12.1 | 正常负载 | 50 | 60s | ≥150 | ≤150ms | ≤0.5% |
| PT-7.12.2 | 高负载 | 100 | 120s | ≥200 | ≤300ms | ≤1% |
| PT-7.12.3 | 峰值负载 | 200 | 60s | ≥250 | ≤800ms | ≤5% |

##### Apifox压测设置说明

1. 准备N个测试策略作为压测数据
2. 添加DELETE /perm-policy/batch请求，body参数化编码集合
3. 设置断言：code=200
4. 压测后无需清理（已逻辑删除）

---

### 7.13 POST /perm-policy/bind 批量绑定权限到目标

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /perm-policy/bind |
| HTTP方法 | POST |
| 接口描述 | 将多个权限批量绑定到指定目标（TENANT/DEPT/ROLE/USER） |
| 是否需要认证 | 是 |
| 权限要求 | 权限策略管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |
| Content-Type | String | 是 | 内容类型 | application/json |

##### 请求体 (Body)

SysPermPolicyBatchBindRTO 字段校验规则：

| 字段名 | 类型 | 是否必填 | 约束条件 | 说明 | 示例值 |
|--------|------|---------|---------|------|--------|
| permId | Long | 是 | @NotNull | 权限ID | 511 |
| targetType | String | 是 | @NotBlank | 目标类型（TENANT/DEPT/ROLE/USER） | TENANT |
| targetIdList | List<Long> | 是 | @NotEmpty | 目标ID集合 | [101,102] |

##### 请求示例

```json
{
  "permId": 511,
  "targetType": "TENANT",
  "targetIdList": [101, 102]
}
```

```bash
curl -X POST 'http://localhost:8081/NexusIxService/perm-policy/bind' \
  -H 'NexusIX: abc123def456' \
  -H 'Content-Type: application/json' \
  -d '{"permId":511,"targetType":"TENANT","targetIdList":[101,102]}'
```

#### 业务场景测试用例

##### 测试用例 TC-7.13.1: 正常批量绑定权限到租户

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证将权限511批量绑定到2个租户成功 |
| 前置条件 | 已登录获取有效token，租户101和102存在，权限511存在 |
| 操作步骤 | 1. 发送POST /perm-policy/bind 2. 验证响应与数据库 |
| 请求参数 | body={"permId":511,"targetType":"TENANT","targetIdList":[101,102]} |
| 预期结果 | code=200，data返回成功绑定的策略数量 |
| 验证SQL | `SELECT * FROM sys_perm_policy WHERE perm_id=511 AND target_type='TENANT' AND target_id IN (101,102) AND is_deleted='NOT_DELETED';` 预期返回2条 |

##### 测试用例 TC-7.13.2: targetIdList为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证targetIdList为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /perm-policy/bind，body包含targetIdList=[] |
| 请求参数 | body={"permId":511,"targetType":"TENANT","targetIdList":[]} |
| 预期结果 | code=500，msg包含"目标ID集合不能为空"的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-7.13.3: permId为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证permId为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /perm-policy/bind，body不包含permId |
| 请求参数 | body={"targetType":"TENANT","targetIdList":[101]} |
| 预期结果 | code=500，msg包含"权限ID不能为空"的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 30 / 80 / 150 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-7.13.1 | 正常负载 | 30 | 60s | ≥100 | ≤200ms | ≤0.5% |
| PT-7.13.2 | 高负载 | 80 | 120s | ≥150 | ≤500ms | ≤1% |
| PT-7.13.3 | 峰值负载 | 150 | 60s | ≥200 | ≤1000ms | ≤5% |

##### Apifox压测设置说明

1. 准备N组不同的permId和targetIdList数据集
2. 添加POST /perm-policy/bind请求，body参数化引用数据集
3. 设置断言：code=200
4. 压测后清理：DELETE /perm-policy/delete批量删除新增策略

---

### 7.14 POST /perm-policy/unbind 批量解绑权限与目标

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /perm-policy/unbind |
| HTTP方法 | POST |
| 接口描述 | 批量解除权限与目标的绑定关系 |
| 是否需要认证 | 是 |
| 权限要求 | 权限策略管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |
| Content-Type | String | 是 | 内容类型 | application/json |

##### 请求体 (Body)

SysPermPolicyBatchUnbindRTO 字段校验规则：

| 字段名 | 类型 | 是否必填 | 约束条件 | 说明 | 示例值 |
|--------|------|---------|---------|------|--------|
| policyCodeList | List<String> | 是 | @NotEmpty | 策略编码集合 | ["PP_BATCH_1","PP_BATCH_2"] |

##### 请求示例

```json
{
  "policyCodeList": ["PP_BATCH_1", "PP_BATCH_2"]
}
```

```bash
curl -X POST 'http://localhost:8081/NexusIxService/perm-policy/unbind' \
  -H 'NexusIX: abc123def456' \
  -H 'Content-Type: application/json' \
  -d '{"policyCodeList":["PP_BATCH_1","PP_BATCH_2"]}'
```

#### 业务场景测试用例

##### 测试用例 TC-7.14.1: 正常批量解绑

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证批量解绑2个策略成功 |
| 前置条件 | 已绑定PP_BATCH_1和PP_BATCH_2策略 |
| 操作步骤 | 1. 发送POST /perm-policy/unbind 2. 验证响应与数据库 |
| 请求参数 | body={"policyCodeList":["PP_BATCH_1","PP_BATCH_2"]} |
| 预期结果 | code=200，data返回成功解绑的策略数量 |
| 验证SQL | `SELECT is_deleted FROM sys_perm_policy WHERE policy_code IN ('PP_BATCH_1','PP_BATCH_2');` 预期is_deleted=DELETED |

##### 测试用例 TC-7.14.2: policyCodeList为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证policyCodeList为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /perm-policy/unbind，body包含policyCodeList=[] |
| 请求参数 | body={"policyCodeList":[]} |
| 预期结果 | code=500，msg包含"策略编码集合不能为空"的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-7.14.3: 解绑不存在的策略编码

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证解绑不存在的策略编码时返回业务错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /perm-policy/unbind，body包含policyCodeList=["PP_NOT_EXIST"] |
| 请求参数 | body={"policyCodeList":["PP_NOT_EXIST"]} |
| 预期结果 | code=500，msg包含策略不存在的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 30 / 80 / 150 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-7.14.1 | 正常负载 | 30 | 60s | ≥100 | ≤200ms | ≤0.5% |
| PT-7.14.2 | 高负载 | 80 | 120s | ≥150 | ≤500ms | ≤1% |
| PT-7.14.3 | 峰值负载 | 150 | 60s | ≥200 | ≤1000ms | ≤5% |

##### Apifox压测设置说明

1. 准备N个测试策略编码集合
2. 添加POST /perm-policy/unbind请求，body参数化编码集合
3. 设置断言：code=200

---

## 8. SysUserPolicyController 用户策略管理接口测试

> Controller路径：`nexusix-iam\src\main\java\com\shy\nexusix\iam\controller\SysUserPolicyController.java`
> 接口前缀：`/user-policy`
> 共14个接口（实际实现，原任务描述为16个）
> 特别说明：本Controller涉及`is_primary`主租户/主部门标记，需重点测试

### 8.1 GET /user-policy/list 查询用户策略列表

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /user-policy/list |
| HTTP方法 | GET |
| 接口描述 | 返回所有用户策略列表 |
| 是否需要认证 | 是 |
| 权限要求 | 用户策略管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |

##### 请求参数

无

##### 请求示例

```bash
curl -X GET 'http://localhost:8081/NexusIxService/user-policy/list' \
  -H 'NexusIX: abc123def456'
```

#### 业务场景测试用例

##### 测试用例 TC-8.1.1: 正常查询用户策略列表

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证已登录用户成功获取用户策略列表 |
| 前置条件 | 已登录获取有效token，数据库存在用户策略数据 |
| 操作步骤 | 1. 发送GET /user-policy/list 2. 验证响应数据结构 |
| 请求参数 | 无 |
| 预期结果 | code=200，data为用户策略列表数组，每条记录包含id/policyCode/policyName/userId/targetType/targetId/isPrimary/status等字段 |
| 验证SQL | `SELECT count(*) FROM sys_user_policy WHERE is_deleted='NOT_DELETED';` 与响应列表数量一致 |

##### 测试用例 TC-8.1.2: 未携带Token

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证未携带Token时返回未登录错误 |
| 前置条件 | 不携带Token |
| 操作步骤 | 1. 发送GET /user-policy/list，不设置NexusIX Header |
| 请求参数 | 无 |
| 预期结果 | code=401，msg包含未登录的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-8.1.3: is_primary字段验证

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证返回的策略记录中is_primary字段正确反映主租户/主部门标记 |
| 前置条件 | 已登录获取有效token，数据库存在is_primary=true的策略（如UP_TENANT_A） |
| 操作步骤 | 1. 发送GET /user-policy/list 2. 检查UP_TENANT_A的isPrimary字段 |
| 请求参数 | 无 |
| 预期结果 | code=200，UP_TENANT_A的isPrimary=true，其他TENANT策略的isPrimary=false |
| 验证SQL | `SELECT policy_code, is_primary FROM sys_user_policy WHERE is_deleted='NOT_DELETED';` |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 100 / 200 / 500 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-8.1.1 | 正常负载 | 100 | 60s | ≥250 | ≤100ms | ≤0.1% |
| PT-8.1.2 | 高负载 | 200 | 120s | ≥300 | ≤200ms | ≤1% |
| PT-8.1.3 | 峰值负载 | 500 | 60s | ≥400 | ≤800ms | ≤5% |

##### Apifox压测设置说明

1. 创建测试场景，添加GET /user-policy/list请求
2. 设置全局变量Token
3. 设置断言：code=200 且 data不为空

---

### 8.2 GET /user-policy/page 分页查询用户策略列表

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /user-policy/page |
| HTTP方法 | GET |
| 接口描述 | 返回分页后的用户策略列表 |
| 是否需要认证 | 是 |
| 权限要求 | 用户策略管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |

##### 查询参数 (Query)

| 参数名 | 类型 | 是否必填 | 约束条件 | 说明 | 示例值 |
|--------|------|---------|---------|------|--------|
| pageNum | Integer | 否 | @Min(1)，默认1 | 页码 | 1 |
| pageSize | Integer | 否 | @Min(1)@Max(100)，默认10 | 每页条数 | 10 |

##### 请求示例

```bash
curl -X GET 'http://localhost:8081/NexusIxService/user-policy/page?pageNum=1&pageSize=10' \
  -H 'NexusIX: abc123def456'
```

#### 业务场景测试用例

##### 测试用例 TC-8.2.1: 正常分页查询

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证默认分页查询成功 |
| 前置条件 | 已登录获取有效token，数据库存在用户策略数据 |
| 操作步骤 | 1. 发送GET /user-policy/page?pageNum=1&pageSize=10 |
| 请求参数 | pageNum=1, pageSize=10 |
| 预期结果 | code=200，data包含records/total/current/size字段，records长度≤10 |
| 验证SQL | `SELECT count(*) FROM sys_user_policy WHERE is_deleted='NOT_DELETED';` 与total一致 |

##### 测试用例 TC-8.2.2: pageSize超出上限

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证pageSize=200时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送GET /user-policy/page?pageNum=1&pageSize=200 |
| 请求参数 | pageNum=1, pageSize=200 |
| 预期结果 | code=500，msg包含pageSize最大100的校验错误 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 100 / 200 / 500 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-8.2.1 | 正常负载 | 100 | 60s | ≥300 | ≤50ms | ≤0.1% |
| PT-8.2.2 | 高负载 | 200 | 120s | ≥400 | ≤150ms | ≤1% |
| PT-8.2.3 | 峰值负载 | 500 | 60s | ≥500 | ≤500ms | ≤5% |

##### Apifox压测设置说明

1. 参数化pageNum和pageSize，模拟翻页
2. 设置断言：code=200

---

### 8.3 POST /user-policy/query 条件查询用户策略列表

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /user-policy/query |
| HTTP方法 | POST |
| 接口描述 | 返回满足条件的用户策略列表（分页） |
| 是否需要认证 | 是 |
| 权限要求 | 用户策略管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |
| Content-Type | String | 是 | 内容类型 | application/json |

##### 请求体 (Body)

SysUserPolicyQueryRTO 字段（含分页参数）：

| 字段名 | 类型 | 是否必填 | 约束条件 | 说明 | 示例值 |
|--------|------|---------|---------|------|--------|
| pageNum | Integer | 是 | @NotNull @Min(1) | 页码 | 1 |
| pageSize | Integer | 是 | @NotNull @Min(1) | 每页条数 | 10 |
| policyCode | String | 否 | 无 | 策略编码 | UP_TENANT_A |
| policyName | String | 否 | 无 | 策略名称 | 万象集团 |
| userId | Long | 否 | 无 | 用户ID | 7301 |
| targetType | String | 否 | 无 | 目标类型（TENANT/DEPT/ROLE） | TENANT |
| targetId | Long | 否 | 无 | 目标ID | 7001 |
| isPrimary | Boolean | 否 | 无 | 是否主租户/主部门 | true |
| status | String | 否 | 无 | 状态 | ACTIVE |

##### 请求示例

```json
{
  "pageNum": 1,
  "pageSize": 10,
  "userId": 7301,
  "targetType": "TENANT",
  "isPrimary": true
}
```

```bash
curl -X POST 'http://localhost:8081/NexusIxService/user-policy/query' \
  -H 'NexusIX: abc123def456' \
  -H 'Content-Type: application/json' \
  -d '{"pageNum":1,"pageSize":10,"userId":7301,"targetType":"TENANT","isPrimary":true}'
```

#### 业务场景测试用例

##### 测试用例 TC-8.3.1: 按用户ID查询

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证按userId查询用户策略 |
| 前置条件 | 已登录获取有效token，用户7301存在 |
| 操作步骤 | 1. 发送POST /user-policy/query，body包含userId=7301 |
| 请求参数 | body={"pageNum":1,"pageSize":10,"userId":7301} |
| 预期结果 | code=200，data.records中所有策略user_id=7301 |
| 验证SQL | `SELECT count(*) FROM sys_user_policy WHERE user_id=7301 AND is_deleted='NOT_DELETED';` |

##### 测试用例 TC-8.3.2: 按isPrimary查询主租户策略

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证按isPrimary=true查询主租户/主部门策略 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /user-policy/query，body包含isPrimary=true |
| 请求参数 | body={"pageNum":1,"pageSize":10,"isPrimary":true} |
| 预期结果 | code=200，data.records中所有策略is_primary=true |
| 验证SQL | `SELECT count(*) FROM sys_user_policy WHERE is_primary=true AND is_deleted='NOT_DELETED';` |

##### 测试用例 TC-8.3.3: 多条件组合查询

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证多条件组合查询（userId+targetType+isPrimary）成功 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /user-policy/query，body包含userId/targetType/isPrimary |
| 请求参数 | body={"pageNum":1,"pageSize":10,"userId":7301,"targetType":"TENANT","isPrimary":true} |
| 预期结果 | code=200，data.records同时满足3个条件 |
| 验证SQL | `SELECT * FROM sys_user_policy WHERE user_id=7301 AND target_type='TENANT' AND is_primary=true AND is_deleted='NOT_DELETED';` |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 100 / 200 / 500 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-8.3.1 | 正常负载 | 100 | 60s | ≥250 | ≤100ms | ≤0.1% |
| PT-8.3.2 | 高负载 | 200 | 120s | ≥300 | ≤200ms | ≤1% |
| PT-8.3.3 | 峰值负载 | 500 | 60s | ≥400 | ≤800ms | ≤5% |

##### Apifox压测设置说明

1. 参数化查询条件，覆盖用户ID/目标类型/isPrimary组合
2. 设置断言：code=200

---

### 8.4 GET /user-policy/detail/{policyCode} 查询用户策略详情

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /user-policy/detail/{policyCode} |
| HTTP方法 | GET |
| 接口描述 | 返回指定用户策略的详情信息 |
| 是否需要认证 | 是 |
| 权限要求 | 用户策略管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |

##### 路径参数 (Path)

| 参数名 | 类型 | 是否必填 | 约束条件 | 说明 | 示例值 |
|--------|------|---------|---------|------|--------|
| policyCode | String | 是 | @NotBlank | 策略编码 | UP_TENANT_A |

##### 请求示例

```bash
curl -X GET 'http://localhost:8081/NexusIxService/user-policy/detail/UP_TENANT_A' \
  -H 'NexusIX: abc123def456'
```

#### 业务场景测试用例

##### 测试用例 TC-8.4.1: 正常查询策略详情

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证查询已存在策略详情成功 |
| 前置条件 | 已登录获取有效token，策略UP_TENANT_A存在 |
| 操作步骤 | 1. 发送GET /user-policy/detail/UP_TENANT_A |
| 请求参数 | policyCode=UP_TENANT_A |
| 预期结果 | code=200，data包含id/policyCode/policyName/userId/targetType/targetId/isPrimary/status/disableReason/createTenant等完整字段 |
| 验证SQL | `SELECT * FROM sys_user_policy WHERE policy_code='UP_TENANT_A' AND is_deleted='NOT_DELETED';` |

##### 测试用例 TC-8.4.2: policyCode为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证policyCode为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送GET /user-policy/detail/（路径参数为空） |
| 请求参数 | policyCode为空 |
| 预期结果 | code=500，msg包含"策略编码不能为空"的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-8.4.3: 查询不存在的策略编码

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证查询不存在的策略编码时返回业务错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送GET /user-policy/detail/UP_NOT_EXIST |
| 请求参数 | policyCode=UP_NOT_EXIST |
| 预期结果 | code=500，msg包含策略不存在的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 100 / 200 / 500 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-8.4.1 | 正常负载 | 100 | 60s | ≥300 | ≤50ms | ≤0.1% |
| PT-8.4.2 | 高负载 | 200 | 120s | ≥400 | ≤150ms | ≤1% |
| PT-8.4.3 | 峰值负载 | 500 | 60s | ≥500 | ≤500ms | ≤5% |

##### Apifox压测设置说明

1. 参数化policyCode，覆盖TENANT/DEPT/ROLE级策略
2. 设置断言：code=200

---

### 8.5 POST /user-policy/add 新增用户策略

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /user-policy/add |
| HTTP方法 | POST |
| 接口描述 | 新增用户策略信息 |
| 是否需要认证 | 是 |
| 权限要求 | 用户策略管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |
| Content-Type | String | 是 | 内容类型 | application/json |

##### 请求体 (Body)

SysUserPolicyAddRTO 字段校验规则：

| 字段名 | 类型 | 是否必填 | 约束条件 | 说明 | 示例值 |
|--------|------|---------|---------|------|--------|
| policyCode | String | 是 | @NotBlank @Size(max=100) | 策略编码 | UP_TEST_001 |
| policyName | String | 是 | @NotBlank @Size(2-100) | 策略名称 | 测试用户策略 |
| userId | Long | 是 | @NotNull | 用户ID | 7301 |
| targetType | String | 是 | @NotBlank | 目标类型（TENANT/DEPT/ROLE） | TENANT |
| targetId | Long | 是 | @NotNull | 目标ID | 7001 |
| isPrimary | Boolean | 否 | 无 | 是否为主租户/主部门 | false |
| status | String | 否 | 无 | 状态（ACTIVE/DISABLED） | ACTIVE |
| disableReason | String | 否 | @Size(max=200) | 禁用原因 | ADMIN_DISABLE |

##### 请求示例

```json
{
  "policyCode": "UP_TEST_001",
  "policyName": "测试用户策略",
  "userId": 7301,
  "targetType": "TENANT",
  "targetId": 7001,
  "isPrimary": false,
  "status": "ACTIVE"
}
```

```bash
curl -X POST 'http://localhost:8081/NexusIxService/user-policy/add' \
  -H 'NexusIX: abc123def456' \
  -H 'Content-Type: application/json' \
  -d '{"policyCode":"UP_TEST_001","policyName":"测试用户策略","userId":7301,"targetType":"TENANT","targetId":7001,"isPrimary":false,"status":"ACTIVE"}'
```

#### 业务场景测试用例

##### 测试用例 TC-8.5.1: 正常新增用户策略

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证新增用户策略成功 |
| 前置条件 | 已登录获取有效token，UP_TEST_001不存在，userId=7301和targetId=7001存在 |
| 操作步骤 | 1. 发送POST /user-policy/add 2. 验证响应与数据库 |
| 请求参数 | body=策略对象 |
| 预期结果 | code=200，data返回影响行数1 |
| 验证SQL | `SELECT * FROM sys_user_policy WHERE policy_code='UP_TEST_001' AND is_deleted='NOT_DELETED';` |

##### 测试用例 TC-8.5.2: 新增主租户策略（is_primary=true）

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证新增is_primary=true的主租户策略成功 |
| 前置条件 | 已登录获取有效token，用户7301在租户7001下尚无主租户策略 |
| 操作步骤 | 1. 发送POST /user-policy/add，body包含isPrimary=true 2. 验证数据库 |
| 请求参数 | body={...,"isPrimary":true} |
| 预期结果 | code=200，数据库is_primary=true |
| 验证SQL | `SELECT is_primary FROM sys_user_policy WHERE policy_code='UP_TEST_001';` 预期is_primary=true |

##### 测试用例 TC-8.5.3: policyCode为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证policyCode为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /user-policy/add，body不包含policyCode |
| 请求参数 | body={"policyName":"测试","userId":7301,"targetType":"TENANT","targetId":7001} |
| 预期结果 | code=500，msg包含"策略编码不能为空"的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-8.5.4: userId为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证userId为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /user-policy/add，body不包含userId |
| 请求参数 | body={"policyCode":"UP_TEST","policyName":"测试","targetType":"TENANT","targetId":7001} |
| 预期结果 | code=500，msg包含"用户ID不能为空"的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-8.5.5: policyCode重复

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证policyCode已存在时返回业务错误 |
| 前置条件 | 已登录获取有效token，UP_TENANT_A已存在 |
| 操作步骤 | 1. 发送POST /user-policy/add，body包含policyCode=UP_TENANT_A |
| 请求参数 | body={"policyCode":"UP_TENANT_A","policyName":"重复","userId":7301,"targetType":"TENANT","targetId":7001} |
| 预期结果 | code=500，msg包含策略编码已存在的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 50 / 100 / 200 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-8.5.1 | 正常负载 | 50 | 60s | ≥150 | ≤150ms | ≤0.5% |
| PT-8.5.2 | 高负载 | 100 | 120s | ≥200 | ≤300ms | ≤1% |
| PT-8.5.3 | 峰值负载 | 200 | 60s | ≥250 | ≤800ms | ≤5% |

##### Apifox压测设置说明

1. 准备N组不同的policyCode数据集
2. 添加POST /user-policy/add请求，body参数化引用数据集
3. 设置断言：code=200
4. 压测后清理：DELETE /user-policy/delete批量删除新增策略

---

### 8.6 PUT /user-policy/update 修改用户策略

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /user-policy/update |
| HTTP方法 | PUT |
| 接口描述 | 修改用户策略信息 |
| 是否需要认证 | 是 |
| 权限要求 | 用户策略管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |
| Content-Type | String | 是 | 内容类型 | application/json |

##### 请求体 (Body)

SysUserPolicyUpdateRTO 字段：policyCode @NotBlank（定位用），其他字段可选（policyName/userId/targetType/targetId/isPrimary/status）。

##### 请求示例

```json
{
  "policyCode": "UP_TEST_001",
  "policyName": "测试用户策略-更新",
  "isPrimary": true,
  "status": "ACTIVE"
}
```

```bash
curl -X PUT 'http://localhost:8081/NexusIxService/user-policy/update' \
  -H 'NexusIX: abc123def456' \
  -H 'Content-Type: application/json' \
  -d '{"policyCode":"UP_TEST_001","policyName":"测试用户策略-更新","isPrimary":true,"status":"ACTIVE"}'
```

#### 业务场景测试用例

##### 测试用例 TC-8.6.1: 正常修改策略

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证修改已存在策略成功 |
| 前置条件 | 已新增UP_TEST_001策略 |
| 操作步骤 | 1. 发送PUT /user-policy/update 2. 验证响应与数据库 |
| 请求参数 | body=策略修改对象 |
| 预期结果 | code=200，data返回影响行数1 |
| 验证SQL | `SELECT policy_name,is_primary FROM sys_user_policy WHERE policy_code='UP_TEST_001';` 预期已更新 |

##### 测试用例 TC-8.6.2: 修改不存在的策略

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证修改不存在的策略时返回业务错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送PUT /user-policy/update，body包含policyCode=UP_NOT_EXIST |
| 请求参数 | body={"policyCode":"UP_NOT_EXIST","policyName":"不存在"} |
| 预期结果 | code=500，msg包含策略不存在的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-8.6.3: 修改is_primary标记

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证修改is_primary标记成功（如将非主租户改为主租户） |
| 前置条件 | 已新增UP_TEST_001策略，is_primary=false |
| 操作步骤 | 1. 发送PUT /user-policy/update，body包含isPrimary=true 2. 验证数据库 |
| 请求参数 | body={"policyCode":"UP_TEST_001","isPrimary":true} |
| 预期结果 | code=200，数据库is_primary=true |
| 验证SQL | `SELECT is_primary FROM sys_user_policy WHERE policy_code='UP_TEST_001';` 预期is_primary=true |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 50 / 100 / 200 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-8.6.1 | 正常负载 | 50 | 60s | ≥150 | ≤150ms | ≤0.5% |
| PT-8.6.2 | 高负载 | 100 | 120s | ≥200 | ≤300ms | ≤1% |
| PT-8.6.3 | 峰值负载 | 200 | 60s | ≥250 | ≤800ms | ≤5% |

##### Apifox压测设置说明

1. 准备N条测试策略作为压测数据
2. 添加PUT /user-policy/update请求，body参数化引用数据集
3. 设置断言：code=200

---

### 8.7 PUT /user-policy/status 更新用户策略状态

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /user-policy/status |
| HTTP方法 | PUT |
| 接口描述 | 更新指定用户策略的状态（ACTIVE/DISABLED） |
| 是否需要认证 | 是 |
| 权限要求 | 用户策略管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |

##### 查询参数 (Query)

| 参数名 | 类型 | 是否必填 | 约束条件 | 说明 | 示例值 |
|--------|------|---------|---------|------|--------|
| policyCode | String | 是 | @NotBlank | 策略编码 | UP_TENANT_A |
| status | String | 是 | @NotBlank | 策略状态（ACTIVE/DISABLED） | DISABLED |

##### 请求示例

```bash
curl -X PUT 'http://localhost:8081/NexusIxService/user-policy/status?policyCode=UP_TENANT_A&status=DISABLED' \
  -H 'NexusIX: abc123def456'
```

#### 业务场景测试用例

##### 测试用例 TC-8.7.1: 正常禁用策略

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证禁用用户策略成功 |
| 前置条件 | 已登录获取有效token，策略UP_TENANT_A状态为ACTIVE |
| 操作步骤 | 1. 发送PUT /user-policy/status?policyCode=UP_TENANT_A&status=DISABLED 2. 验证数据库 |
| 请求参数 | policyCode=UP_TENANT_A, status=DISABLED |
| 预期结果 | code=200，data返回影响行数1 |
| 验证SQL | `SELECT status FROM sys_user_policy WHERE policy_code='UP_TENANT_A';` 预期status=DISABLED |

##### 测试用例 TC-8.7.2: 禁用主租户策略影响用户登录

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证禁用TENANT级主租户策略后，关联用户登录时主租户信息缺失或权限受影响 |
| 前置条件 | UP_TENANT_A为主租户策略（is_primary=true），test_all用户绑定该策略 |
| 操作步骤 | 1. PUT /user-policy/status?policyCode=UP_TENANT_A&status=DISABLED 2. POST /auth/login登录test_all 3. 验证响应中tenantInfo是否包含TENANT_A |
| 请求参数 | policyCode=UP_TENANT_A, status=DISABLED |
| 预期结果 | code=200，login响应中TENANT_A不在启用租户列表中（业务规则依实现） |
| 验证SQL | `SELECT status FROM sys_user_policy WHERE policy_code='UP_TENANT_A';` |

##### 测试用例 TC-8.7.3: policyCode为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证policyCode为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送PUT /user-policy/status?status=DISABLED（不传policyCode） |
| 请求参数 | status=DISABLED |
| 预期结果 | code=500，msg包含"策略编码不能为空"的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 50 / 100 / 200 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-8.7.1 | 正常负载 | 50 | 60s | ≥200 | ≤100ms | ≤0.1% |
| PT-8.7.2 | 高负载 | 100 | 120s | ≥250 | ≤300ms | ≤1% |
| PT-8.7.3 | 峰值负载 | 200 | 60s | ≥300 | ≤800ms | ≤5% |

##### Apifox压测设置说明

1. 准备N个测试策略作为压测数据
2. 添加PUT /user-policy/status请求，参数化policyCode和status
3. 设置断言：code=200
4. 压测后清理：恢复策略状态为ACTIVE

---

### 8.8 DELETE /user-policy/delete 删除用户策略

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /user-policy/delete |
| HTTP方法 | DELETE |
| 接口描述 | 删除用户策略信息（逻辑删除） |
| 是否需要认证 | 是 |
| 权限要求 | 用户策略管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |

##### 查询参数 (Query)

| 参数名 | 类型 | 是否必填 | 约束条件 | 说明 | 示例值 |
|--------|------|---------|---------|------|--------|
| policyCode | String | 是 | @NotBlank | 策略编码 | UP_TEST_001 |

##### 请求示例

```bash
curl -X DELETE 'http://localhost:8081/NexusIxService/user-policy/delete?policyCode=UP_TEST_001' \
  -H 'NexusIX: abc123def456'
```

#### 业务场景测试用例

##### 测试用例 TC-8.8.1: 正常删除策略

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证删除已存在的策略成功（逻辑删除） |
| 前置条件 | 已新增测试策略UP_TEST_001 |
| 操作步骤 | 1. 发送DELETE /user-policy/delete?policyCode=UP_TEST_001 2. 验证数据库逻辑删除标记 |
| 请求参数 | policyCode=UP_TEST_001 |
| 预期结果 | code=200，data返回影响行数1 |
| 验证SQL | `SELECT is_deleted FROM sys_user_policy WHERE policy_code='UP_TEST_001';` 预期is_deleted=DELETED |

##### 测试用例 TC-8.8.2: 删除不存在的策略

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证删除不存在的策略编码时返回业务错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送DELETE /user-policy/delete?policyCode=UP_NOT_EXIST |
| 请求参数 | policyCode=UP_NOT_EXIST |
| 预期结果 | code=500，msg包含策略不存在的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-8.8.3: policyCode为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证policyCode为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送DELETE /user-policy/delete（不传policyCode） |
| 请求参数 | 不传policyCode参数 |
| 预期结果 | code=500，msg包含"策略编码不能为空"的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 50 / 100 / 200 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-8.8.1 | 正常负载 | 50 | 60s | ≥200 | ≤100ms | ≤0.1% |
| PT-8.8.2 | 高负载 | 100 | 120s | ≥250 | ≤300ms | ≤1% |
| PT-8.8.3 | 峰值负载 | 200 | 60s | ≥300 | ≤800ms | ≤5% |

##### Apifox压测设置说明

1. 创建测试场景，先批量新增测试策略
2. 添加DELETE /user-policy/delete请求，参数化policyCode
3. 设置断言：code=200

---

### 8.9 POST /user-policy/batch 批量新增用户策略

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /user-policy/batch |
| HTTP方法 | POST |
| 接口描述 | 批量新增用户策略信息 |
| 是否需要认证 | 是 |
| 权限要求 | 用户策略管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |
| Content-Type | String | 是 | 内容类型 | application/json |

##### 请求体 (Body)

请求体为 `List<SysUserPolicyAddRTO>` 数组，每个元素字段同[8.5 POST /user-policy/add](#85-post-user-policyadd-新增用户策略)请求体。集合不能为空（@NotEmpty）。

##### 请求示例

```json
[
  {
    "policyCode": "UP_BATCH_1",
    "policyName": "批量策略1",
    "userId": 7301,
    "targetType": "TENANT",
    "targetId": 7001,
    "isPrimary": false,
    "status": "ACTIVE"
  },
  {
    "policyCode": "UP_BATCH_2",
    "policyName": "批量策略2",
    "userId": 7301,
    "targetType": "DEPT",
    "targetId": 75001,
    "isPrimary": true,
    "status": "ACTIVE"
  }
]
```

```bash
curl -X POST 'http://localhost:8081/NexusIxService/user-policy/batch' \
  -H 'NexusIX: abc123def456' \
  -H 'Content-Type: application/json' \
  -d '[{"policyCode":"UP_BATCH_1","policyName":"批量策略1","userId":7301,"targetType":"TENANT","targetId":7001,"isPrimary":false,"status":"ACTIVE"},{"policyCode":"UP_BATCH_2","policyName":"批量策略2","userId":7301,"targetType":"DEPT","targetId":75001,"isPrimary":true,"status":"ACTIVE"}]'
```

#### 业务场景测试用例

##### 测试用例 TC-8.9.1: 正常批量新增策略

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证批量新增2个用户策略成功 |
| 前置条件 | 已登录获取有效token，UP_BATCH_1和UP_BATCH_2不存在 |
| 操作步骤 | 1. 发送POST /user-policy/batch 2. 验证响应与数据库 |
| 请求参数 | body=2个策略对象数组 |
| 预期结果 | code=200，data返回影响行数 |
| 验证SQL | `SELECT * FROM sys_user_policy WHERE policy_code IN ('UP_BATCH_1','UP_BATCH_2') AND is_deleted='NOT_DELETED';` 预期返回2条 |

##### 测试用例 TC-8.9.2: 集合为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证请求体为空数组时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /user-policy/batch，body=[] |
| 请求参数 | body=[] |
| 预期结果 | code=500，msg包含集合不能为空的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-8.9.3: 集合内单条记录校验失败

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证集合中存在policyName长度=1的非法记录时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /user-policy/batch，body包含policyName="A"的非法记录 |
| 请求参数 | body=[{合法对象},{policyName:"A"}] |
| 预期结果 | code=500，msg包含策略名称长度2-100的校验错误 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 30 / 80 / 150 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-8.9.1 | 正常负载 | 30 | 60s | ≥100 | ≤200ms | ≤0.5% |
| PT-8.9.2 | 高负载 | 80 | 120s | ≥150 | ≤500ms | ≤1% |
| PT-8.9.3 | 峰值负载 | 150 | 60s | ≥200 | ≤1000ms | ≤5% |

##### Apifox压测设置说明

1. 创建测试场景，准备N组不同的policyCode数据集
2. 添加POST /user-policy/batch请求，body参数化引用数据集
3. 设置断言：code=200
4. 压测后清理：DELETE /user-policy/delete批量删除新增策略

---

### 8.10 PUT /user-policy/batch 批量修改用户策略

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /user-policy/batch |
| HTTP方法 | PUT |
| 接口描述 | 批量修改用户策略信息 |
| 是否需要认证 | 是 |
| 权限要求 | 用户策略管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |
| Content-Type | String | 是 | 内容类型 | application/json |

##### 请求体 (Body)

请求体为 `List<SysUserPolicyUpdateRTO>` 数组，每个元素字段同[8.6 PUT /user-policy/update](#86-put-user-policyupdate-修改用户策略)请求体。注意：批量修改接口未声明@NotEmpty。

##### 请求示例

```json
[
  {
    "policyCode": "UP_BATCH_1",
    "policyName": "批量策略1-更新",
    "status": "ACTIVE"
  },
  {
    "policyCode": "UP_BATCH_2",
    "policyName": "批量策略2-更新",
    "isPrimary": false,
    "status": "DISABLED"
  }
]
```

```bash
curl -X PUT 'http://localhost:8081/NexusIxService/user-policy/batch' \
  -H 'NexusIX: abc123def456' \
  -H 'Content-Type: application/json' \
  -d '[{"policyCode":"UP_BATCH_1","policyName":"批量策略1-更新","status":"ACTIVE"},{"policyCode":"UP_BATCH_2","policyName":"批量策略2-更新","isPrimary":false,"status":"DISABLED"}]'
```

#### 业务场景测试用例

##### 测试用例 TC-8.10.1: 正常批量修改策略

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证批量修改2个策略成功 |
| 前置条件 | 已新增UP_BATCH_1和UP_BATCH_2 |
| 操作步骤 | 1. 发送PUT /user-policy/batch 2. 验证响应与数据库 |
| 请求参数 | body=2个修改对象数组 |
| 预期结果 | code=200，data返回影响行数 |
| 验证SQL | `SELECT policy_name,status FROM sys_user_policy WHERE policy_code IN ('UP_BATCH_1','UP_BATCH_2');` 预期名称已更新 |

##### 测试用例 TC-8.10.2: 批量修改中包含不存在policyCode

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证批量修改中包含不存在的policyCode时返回业务错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送PUT /user-policy/batch，body包含policyCode=UP_NOT_EXIST |
| 请求参数 | body=[{policyCode:"UP_NOT_EXIST",policyName:"不存在"}] |
| 预期结果 | code=500，msg包含策略不存在的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 30 / 80 / 150 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-8.10.1 | 正常负载 | 30 | 60s | ≥100 | ≤200ms | ≤0.5% |
| PT-8.10.2 | 高负载 | 80 | 120s | ≥150 | ≤500ms | ≤1% |
| PT-8.10.3 | 峰值负载 | 150 | 60s | ≥200 | ≤1000ms | ≤5% |

##### Apifox压测设置说明

1. 准备N条测试策略作为压测数据
2. 添加PUT /user-policy/batch请求，body参数化引用数据集
3. 设置断言：code=200

---

### 8.11 PUT /user-policy/status/batch 批量更新用户策略状态

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /user-policy/status/batch |
| HTTP方法 | PUT |
| 接口描述 | 批量更新多个指定用户策略的状态（ACTIVE/DISABLED） |
| 是否需要认证 | 是 |
| 权限要求 | 用户策略管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |
| Content-Type | String | 是 | 内容类型 | application/json |

##### 查询参数 (Query)

| 参数名 | 类型 | 是否必填 | 约束条件 | 说明 | 示例值 |
|--------|------|---------|---------|------|--------|
| status | String | 是 | @NotBlank | 策略状态（ACTIVE/DISABLED） | DISABLED |

##### 请求体 (Body)

请求体为 `List<String>` 类型，元素为策略编码（policyCode）。集合不能为空（@NotEmpty）。

##### 请求示例

```json
["UP_TENANT_A", "UP_TENANT_B", "UP_TENANT_C"]
```

```bash
curl -X PUT 'http://localhost:8081/NexusIxService/user-policy/status/batch?status=DISABLED' \
  -H 'NexusIX: abc123def456' \
  -H 'Content-Type: application/json' \
  -d '["UP_TENANT_A","UP_TENANT_B","UP_TENANT_C"]'
```

#### 业务场景测试用例

##### 测试用例 TC-8.11.1: 正常批量禁用策略

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证批量禁用3个策略成功 |
| 前置条件 | 已新增3个测试策略 |
| 操作步骤 | 1. 发送PUT /user-policy/status/batch?status=DISABLED，body=[code1,code2,code3] 2. 验证数据库 |
| 请求参数 | status=DISABLED, body=[code1,code2,code3] |
| 预期结果 | code=200，data返回影响行数3 |
| 验证SQL | `SELECT status FROM sys_user_policy WHERE policy_code IN ('code1','code2','code3');` 预期status=DISABLED |

##### 测试用例 TC-8.11.2: 集合为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证policyCodeList集合为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送PUT /user-policy/status/batch?status=DISABLED，body=[] |
| 请求参数 | status=DISABLED, body=[] |
| 预期结果 | code=500，msg包含"策略编码集合不能为空"的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-8.11.3: status为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证status为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送PUT /user-policy/status/batch（不传status），body=["UP_TENANT_A"] |
| 请求参数 | 不传status参数 |
| 预期结果 | code=500，msg包含"状态不能为空"的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 50 / 100 / 200 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-8.11.1 | 正常负载 | 50 | 60s | ≥150 | ≤150ms | ≤0.5% |
| PT-8.11.2 | 高负载 | 100 | 120s | ≥200 | ≤300ms | ≤1% |
| PT-8.11.3 | 峰值负载 | 200 | 60s | ≥250 | ≤800ms | ≤5% |

##### Apifox压测设置说明

1. 准备N个测试策略编码集合
2. 添加PUT /user-policy/status/batch请求，body参数化编码集合
3. 设置断言：code=200
4. 压测后清理：恢复策略状态为ACTIVE

---

### 8.12 DELETE /user-policy/batch 批量删除用户策略

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /user-policy/batch |
| HTTP方法 | DELETE |
| 接口描述 | 批量删除用户策略信息（逻辑删除） |
| 是否需要认证 | 是 |
| 权限要求 | 用户策略管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |
| Content-Type | String | 是 | 内容类型 | application/json |

##### 请求体 (Body)

请求体为 `List<String>` 类型，元素为策略编码（policyCode）。集合不能为空（@NotEmpty）。

##### 请求示例

```json
["UP_BATCH_1", "UP_BATCH_2", "UP_BATCH_3"]
```

```bash
curl -X DELETE 'http://localhost:8081/NexusIxService/user-policy/batch' \
  -H 'NexusIX: abc123def456' \
  -H 'Content-Type: application/json' \
  -d '["UP_BATCH_1","UP_BATCH_2","UP_BATCH_3"]'
```

#### 业务场景测试用例

##### 测试用例 TC-8.12.1: 正常批量删除策略

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证批量删除3个策略成功（逻辑删除） |
| 前置条件 | 已新增3个测试策略 |
| 操作步骤 | 1. 发送DELETE /user-policy/batch，body=[code1,code2,code3] 2. 验证数据库逻辑删除标记 |
| 请求参数 | body=[code1,code2,code3] |
| 预期结果 | code=200，data返回影响行数3 |
| 验证SQL | `SELECT is_deleted FROM sys_user_policy WHERE policy_code IN ('code1','code2','code3');` 预期is_deleted=DELETED |

##### 测试用例 TC-8.12.2: 集合为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证policyCodeList集合为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送DELETE /user-policy/batch，body=[] |
| 请求参数 | body=[] |
| 预期结果 | code=500，msg包含"策略编码集合不能为空"的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-8.12.3: 已删除策略重复删除

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证对已逻辑删除的策略再次删除时返回业务错误 |
| 前置条件 | 已删除测试策略（is_deleted=DELETED） |
| 操作步骤 | 1. 发送DELETE /user-policy/batch，body=[已删除策略code] |
| 请求参数 | body=[已删除策略code] |
| 预期结果 | code=500，msg包含策略不存在的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 50 / 100 / 200 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-8.12.1 | 正常负载 | 50 | 60s | ≥150 | ≤150ms | ≤0.5% |
| PT-8.12.2 | 高负载 | 100 | 120s | ≥200 | ≤300ms | ≤1% |
| PT-8.12.3 | 峰值负载 | 200 | 60s | ≥250 | ≤800ms | ≤5% |

##### Apifox压测设置说明

1. 准备N个测试策略作为压测数据
2. 添加DELETE /user-policy/batch请求，body参数化编码集合
3. 设置断言：code=200
4. 压测后无需清理（已逻辑删除）

---

### 8.13 POST /user-policy/bind 批量绑定用户到目标

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /user-policy/bind |
| HTTP方法 | POST |
| 接口描述 | 将多个用户批量绑定到指定目标（TENANT/DEPT/ROLE） |
| 是否需要认证 | 是 |
| 权限要求 | 用户策略管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |
| Content-Type | String | 是 | 内容类型 | application/json |

##### 请求体 (Body)

SysUserPolicyBatchBindRTO 字段校验规则：

| 字段名 | 类型 | 是否必填 | 约束条件 | 说明 | 示例值 |
|--------|------|---------|---------|------|--------|
| userId | Long | 是 | @NotNull | 用户ID | 7301 |
| targetType | String | 是 | @NotBlank | 目标类型（TENANT/DEPT/ROLE） | TENANT |
| targetIdList | List<Long> | 是 | @NotEmpty | 目标ID集合 | [7001,7002] |

##### 请求示例

```json
{
  "userId": 7301,
  "targetType": "TENANT",
  "targetIdList": [7001, 7002]
}
```

```bash
curl -X POST 'http://localhost:8081/NexusIxService/user-policy/bind' \
  -H 'NexusIX: abc123def456' \
  -H 'Content-Type: application/json' \
  -d '{"userId":7301,"targetType":"TENANT","targetIdList":[7001,7002]}'
```

#### 业务场景测试用例

##### 测试用例 TC-8.13.1: 正常批量绑定用户到租户

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证将用户7301批量绑定到2个租户成功 |
| 前置条件 | 已登录获取有效token，租户7001和7002存在，用户7301存在 |
| 操作步骤 | 1. 发送POST /user-policy/bind 2. 验证响应与数据库 |
| 请求参数 | body={"userId":7301,"targetType":"TENANT","targetIdList":[7001,7002]} |
| 预期结果 | code=200，data返回成功绑定的策略数量 |
| 验证SQL | `SELECT * FROM sys_user_policy WHERE user_id=7301 AND target_type='TENANT' AND target_id IN (7001,7002) AND is_deleted='NOT_DELETED';` 预期返回2条 |

##### 测试用例 TC-8.13.2: targetIdList为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证targetIdList为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /user-policy/bind，body包含targetIdList=[] |
| 请求参数 | body={"userId":7301,"targetType":"TENANT","targetIdList":[]} |
| 预期结果 | code=500，msg包含"目标ID集合不能为空"的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-8.13.3: userId为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证userId为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /user-policy/bind，body不包含userId |
| 请求参数 | body={"targetType":"TENANT","targetIdList":[7001]} |
| 预期结果 | code=500，msg包含"用户ID不能为空"的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 30 / 80 / 150 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-8.13.1 | 正常负载 | 30 | 60s | ≥100 | ≤200ms | ≤0.5% |
| PT-8.13.2 | 高负载 | 80 | 120s | ≥150 | ≤500ms | ≤1% |
| PT-8.13.3 | 峰值负载 | 150 | 60s | ≥200 | ≤1000ms | ≤5% |

##### Apifox压测设置说明

1. 准备N组不同的userId和targetIdList数据集
2. 添加POST /user-policy/bind请求，body参数化引用数据集
3. 设置断言：code=200
4. 压测后清理：DELETE /user-policy/delete批量删除新增策略

---

### 8.14 POST /user-policy/unbind 批量解绑用户与目标

#### 接口基本信息

| 项目 | 值 |
|------|-----|
| 接口路径 | /user-policy/unbind |
| HTTP方法 | POST |
| 接口描述 | 批量解除用户与目标的绑定关系 |
| 是否需要认证 | 是 |
| 权限要求 | 用户策略管理权限 |

#### 请求参数规范

##### 请求头 (Headers)

| 参数名 | 类型 | 是否必填 | 说明 | 示例值 |
|--------|------|---------|------|--------|
| NexusIX | String | 是 | 认证Token | abc123def456 |
| Content-Type | String | 是 | 内容类型 | application/json |

##### 请求体 (Body)

SysUserPolicyBatchUnbindRTO 字段校验规则：

| 字段名 | 类型 | 是否必填 | 约束条件 | 说明 | 示例值 |
|--------|------|---------|---------|------|--------|
| policyCodeList | List<String> | 是 | @NotEmpty | 策略编码集合 | ["UP_BATCH_1","UP_BATCH_2"] |

##### 请求示例

```json
{
  "policyCodeList": ["UP_BATCH_1", "UP_BATCH_2"]
}
```

```bash
curl -X POST 'http://localhost:8081/NexusIxService/user-policy/unbind' \
  -H 'NexusIX: abc123def456' \
  -H 'Content-Type: application/json' \
  -d '{"policyCodeList":["UP_BATCH_1","UP_BATCH_2"]}'
```

#### 业务场景测试用例

##### 测试用例 TC-8.14.1: 正常批量解绑

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证批量解绑2个策略成功 |
| 前置条件 | 已绑定UP_BATCH_1和UP_BATCH_2策略 |
| 操作步骤 | 1. 发送POST /user-policy/unbind 2. 验证响应与数据库 |
| 请求参数 | body={"policyCodeList":["UP_BATCH_1","UP_BATCH_2"]} |
| 预期结果 | code=200，data返回成功解绑的策略数量 |
| 验证SQL | `SELECT is_deleted FROM sys_user_policy WHERE policy_code IN ('UP_BATCH_1','UP_BATCH_2');` 预期is_deleted=DELETED |

##### 测试用例 TC-8.14.2: policyCodeList为空参数校验

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证policyCodeList为空时返回参数校验错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /user-policy/unbind，body包含policyCodeList=[] |
| 请求参数 | body={"policyCodeList":[]} |
| 预期结果 | code=500，msg包含"策略编码集合不能为空"的描述 |
| 验证SQL | 无 |

##### 测试用例 TC-8.14.3: 解绑不存在的策略编码

| 项目 | 内容 |
|------|------|
| 测试场景 | 验证解绑不存在的策略编码时返回业务错误 |
| 前置条件 | 已登录获取有效token |
| 操作步骤 | 1. 发送POST /user-policy/unbind，body包含policyCodeList=["UP_NOT_EXIST"] |
| 请求参数 | body={"policyCodeList":["UP_NOT_EXIST"]} |
| 预期结果 | code=500，msg包含策略不存在的描述 |
| 验证SQL | 无 |

#### 压测方案 (Apifox)

##### 压测配置

| 配置项 | 值 |
|--------|-----|
| 压测类型 | 并发请求 |
| 并发用户数 | 30 / 80 / 150 |
| 持续时间 | 60s / 120s / 60s |

##### 压测场景

| 场景编号 | 场景描述 | 并发数 | 持续时间 | 期望TPS | 期望响应时间 | 期望错误率 |
|---------|---------|--------|---------|---------|------------|-----------|
| PT-8.14.1 | 正常负载 | 30 | 60s | ≥100 | ≤200ms | ≤0.5% |
| PT-8.14.2 | 高负载 | 80 | 120s | ≥150 | ≤500ms | ≤1% |
| PT-8.14.3 | 峰值负载 | 150 | 60s | ≥200 | ≤1000ms | ≤5% |

##### Apifox压测设置说明

1. 准备N个测试策略编码集合
2. 添加POST /user-policy/unbind请求，body参数化编码集合
3. 设置断言：code=200

---

## 9. 附录

### 9.1 测试用例统计

| 章节 | Controller | 接口数 | 测试用例数 |
|------|-----------|--------|-----------|
| 3 | AuthController | 1 | 11 |
| 4 | SysUserController | 12 | 36 |
| 5 | SysRoleController | 12 | 36 |
| 6 | SysPermController | 15 | 45 |
| 7 | SysPermPolicyController | 14 | 42 |
| 8 | SysUserPolicyController | 14 | 42 |
| **合计** | **6个Controller** | **68** | **212** |

> 注：原任务描述中SysPermPolicyController和SysUserPolicyController各为16个接口，实际源码实现各为14个接口，本文档以实际实现为准。

### 9.2 业务场景覆盖说明

| 场景类型 | 覆盖章节 | 说明 |
|---------|---------|------|
| 正常场景 | 全部 | 验证接口正常调用流程 |
| 参数校验 | 全部 | @NotBlank/@NotNull/@Size/@Min/@Max/@NotEmpty/@EnumField校验 |
| 权限层级禁用 | 6.10、7.7、8.7 | 验证权限/策略禁用后影响下游用户登录 |
| 实体禁用状态 | 4.7、5.7、6.10、7.7、8.7 | 验证ENABLED/DISABLED、ACTIVE/FROZEN状态切换 |
| 数据查询可见范围 | 4.6、5.3、6.6、7.3、8.3 | 条件查询返回数据符合筛选条件 |
| 字段级权限控制 | 7.1、7.5 | field_operation JSONB字段级权限配置与读取 |
| 主租户/主部门标记 | 8.1、8.3、8.5、8.6、8.7 | is_primary标记的设置与影响 |
| 边界值 | 4.2、5.2、6.2、7.2、8.2 | 分页参数pageNum=0、pageSize=200等 |
| 异常场景 | 全部 | 未携带Token、无权限、不存在记录、重复编码、已删除重复操作 |

### 9.3 Apifox配置快速参考

#### 9.3.1 全局变量配置

| 变量名 | 类型 | 初始值 | 说明 |
|--------|------|--------|------|
| baseUrl | String | http://localhost:8081/NexusIxService | 基础URL |
| token | String | （登录后设置） | 认证Token |

#### 9.3.2 全局Header配置

| Header名 | 值 |
|----------|-----|
| NexusIX | {{token}} |
| Content-Type | application/json |

#### 9.3.3 全局断言配置

```json
{
  "assertions": [
    {
      "type": "json",
      "expression": "$.code",
      "operator": "equals",
      "value": 200
    }
  ]
}
```

### 9.4 关联文档索引

| 文档名称 | 路径 | 关联章节 |
|---------|------|---------|
| 接口测试文档 | docs/03-测试用例文档/接口测试文档.md | 1-9章测试数据参考 |
| 权限禁用测试用例文档 | docs/03-测试用例文档/权限禁用测试用例文档.md | 3.1、6.10、7.7禁用场景参考 |
| 用户表SQL脚本 | docs/05-数据库脚本/sys_user.sql | 4章测试数据 |
| 权限策略表SQL脚本 | docs/05-数据库脚本/sys_perm_policy.sql | 7章field_operation参考 |
| AuthController源码 | nexusix-iam/src/main/java/com/shy/nexusix/iam/controller/AuthController.java | 3章接口定义 |
| SysUserController源码 | nexusix-iam/src/main/java/com/shy/nexusix/iam/controller/SysUserController.java | 4章接口定义 |
| SysRoleController源码 | nexusix-iam/src/main/java/com/shy/nexusix/iam/controller/SysRoleController.java | 5章接口定义 |
| SysPermController源码 | nexusix-iam/src/main/java/com/shy/nexusix/iam/controller/SysPermController.java | 6章接口定义 |
| SysPermPolicyController源码 | nexusix-iam/src/main/java/com/shy/nexusix/iam/controller/SysPermPolicyController.java | 7章接口定义 |
| SysUserPolicyController源码 | nexusix-iam/src/main/java/com/shy/nexusix/iam/controller/SysUserPolicyController.java | 8章接口定义 |

### 9.5 修订记录

| 版本 | 日期 | 修订内容 | 修订人 |
|------|------|---------|--------|
| v1.0 | 2026-07-04 | 初始版本，覆盖6个Controller共68个接口 | AI助手 |

---

**文档结束**