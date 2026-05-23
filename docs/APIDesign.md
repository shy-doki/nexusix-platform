# NexusIX-Platform 接口设计文档

---

## 1. 文档信息

| 项目 | 内容 |
|------|------|
| 项目名称 | NexusIX-Platform 多租户 SaaS 平台底座 |
| 文档版本 | v1.0.0 |
| 创建日期 | 2026-05-23 |
| 作者 | shy |
| 技术栈 | Spring Boot 3 / Sa-Token JWT / MyBatis-Plus / Redis / PostgreSQL |
| API风格 | RESTful |
| 接口文档 | Knife4j OpenAPI 3 (访问 `/doc.html`) |

---

## 2. 接口规范概述

### 2.1 基础路径

```
/api/v1
```

所有接口均以此为前缀，例如：`/api/v1/auth/login`

### 2.2 认证方式

| 项目 | 说明 |
|------|------|
| 认证框架 | Sa-Token JWT (Simple模式) |
| Token格式 | JWT |
| 传输方式 | HTTP Header |
| Header名称 | `Authorization` |
| Token前缀 | `NexusIX`（可选配置） |
| 会话存储 | Redis (独立 database:1，与业务缓存隔离) |
| Token写入响应头 | 是 (`is-write-header: true`) |

**请求示例：**

```
GET /api/v1/users/page HTTP/1.1
Authorization: NexusIX eyJhbGciOiJIUzI1NiJ9...
Content-Type: application/json
```

### 2.3 统一响应格式

所有接口统一返回 `ApiResponse` 结构：

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {}
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| code | Integer | 业务状态码，200 表示成功 |
| msg | String | 响应消息 |
| data | T | 响应数据，泛型 |

**快捷响应映射：**

| 场景 | code | msg |
|------|------|-----|
| 成功 | 200 | 操作成功 |
| 未授权 | 401 | 未授权 |
| 禁止访问 | 403 | 禁止访问 |
| 资源不存在 | 404 | 资源不存在 |
| 服务器错误 | 500 | 服务器内部错误 |
| 参数验证失败 | 400 | 参数验证失败: {字段详情} |

### 2.4 分页响应格式

分页查询接口的 `data` 字段返回如下结构：

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "records": [],
    "total": 100,
    "pageNum": 1,
    "pageSize": 10,
    "pages": 10
  }
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| records | Array | 当前页数据列表 |
| total | Long | 总记录数 |
| pageNum | Integer | 当前页码（从1开始） |
| pageSize | Integer | 每页数量 |
| pages | Integer | 总页数 |

**分页请求参数（PageCommonRTO）：**

| 字段 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| pageNum | Integer | 否 | 1 | 当前页码，最小值1 |
| pageSize | Integer | 否 | 10 | 每页数量，范围1-100 |

### 2.5 HTTP 状态码

| HTTP状态码 | 含义 | 使用场景 |
|------------|------|----------|
| 200 | 成功 | 请求处理成功 |
| 401 | 未授权 | Token无效/过期/未登录 |
| 403 | 禁止访问 | 权限不足/角色缺失/服务封禁 |
| 404 | 不存在 | 请求资源不存在 |
| 500 | 服务器错误 | 系统内部异常 |

> 注意：本平台所有业务错误均通过 HTTP 200 + `ApiResponse.code` 返回，HTTP状态码仅用于框架级异常。

---

## 3. 错误码定义体系

### 3.1 错误码总览

| 范围 | 模块 | 说明 |
|------|------|------|
| 200 | 通用 | 成功 |
| 1000-1999 | 租户 | 租户相关业务错误 |
| 2000-2999 | 用户 | 用户相关业务错误 |
| 3000-3999 | 权限 | 权限/角色相关业务错误 |
| 4000-4999 | 组织架构 | 组织架构相关业务错误（预留） |
| 5000-5999 | 计费 | 计费相关业务错误（预留） |
| 10001-10099 | Sa-Token | 认证框架相关错误 |

### 3.2 租户相关错误码（1000-1999）

| 错误码 | 常量名 | 说明 | 触发场景 |
|--------|--------|------|----------|
| 1001 | TENANT_CODE_EXISTS | 租户编码已存在 | 创建租户时编码重复 |
| 1002 | TENANT_PARENT_NOT_FOUND | 父租户不存在 | 指定的父租户ID/编码无效 |
| 1003 | TENANT_NOT_FOUND | 租户不存在 | 查询的租户ID/编码不存在 |
| 1004 | TENANT_DISABLED | 租户已停用 | 登录时所属租户状态为停用 |
| 1005 | TENANT_EXPIRED | 租户已过期 | 登录时所属租户状态为过期 |

### 3.3 用户相关错误码（2000-2999）

| 错误码 | 常量名 | 说明 | 触发场景 |
|--------|--------|------|----------|
| 2001 | USER_NOT_FOUND | 用户不存在 | 用户名查询无结果 |
| 2002 | USER_PASSWORD_ERROR | 密码错误 | 密码校验不通过 |
| 2003 | USER_DISABLED | 用户已禁用 | 用户状态为禁用 |
| 2004 | USER_NAME_EXISTS | 用户名已存在 | 注册/创建时用户名重复 |
| 2005 | USER_LOGIN_LOCKED | 登录失败锁定 | 连续登录失败次数超限 |

### 3.4 权限相关错误码（3000-3999）

| 错误码 | 常量名 | 说明 | 触发场景 |
|--------|--------|------|----------|
| 3001 | PERM_DENIED | 权限不足 | 缺少必要权限标识 |
| 3002 | ROLE_NOT_FOUND | 角色不存在 | 指定的角色ID不存在 |
| 3003 | ROLE_CODE_EXISTS | 角色编码已存在 | 创建角色时编码重复 |

### 3.5 组织架构相关错误码（4000-4999）

预留，待后续模块实现时定义。

### 3.6 计费相关错误码（5000-5999）

预留，待后续模块实现时定义。

### 3.7 Sa-Token 相关错误码（10001-10099）

| 错误码 | 常量名 | 说明 | 触发场景 |
|--------|--------|------|----------|
| 10001 | TOKEN_INVALID | Token无效 | Token格式错误或被篡改 |
| 10002 | TOKEN_EXPIRED | Token过期 | Token已超过有效期 |
| 10003 | TOKEN_KICKOUT | 被顶替 | 同一账号在新设备登录，旧Token失效 |
| 10004 | TOKEN_REPLACED | 被踢出 | 管理员强制下线 |
| 10005 | NOT_LOGIN | 未登录 | 请求未携带Token或Session无效 |
| 10006 | SA_PERM_DENIED | Sa-Token权限不足 | Sa-Token框架级权限校验失败 |

---

## 4. 各模块接口详细定义

### 4.1 认证模块 `/api/v1/auth`

#### 4.1.1 POST /auth/login - 登录

**描述：** 用户登录，执行五层级认证流程（详见第5章），返回JWT Token。

**权限要求：** 无（公开接口）

**请求参数（LoginRTO）：**

| 字段 | 类型 | 必填 | 说明 | 示例 |
|------|------|------|------|------|
| username | String | 是 | 用户名，不能为空 | admin |
| password | String | 是 | 密码，不能为空 | admin123 |

**响应格式：**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "username": "admin",
    "nickname": "管理员",
    "tenantName": "默认租户"
  }
}
```

**可能错误码：** 2001、2002、2003、2005、1003、1004、1005

---

#### 4.1.2 POST /auth/logout - 登出

**描述：** 退出登录，清除当前会话和Token。

**权限要求：** 需登录

**请求参数：** 无

**响应格式：**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": null
}
```

**可能错误码：** 10005

---

#### 4.1.3 POST /auth/register - 注册

**描述：** 用户注册，创建新用户账号。

**权限要求：** 无（公开接口）

**请求参数（RegisterRTO）：**

| 字段 | 类型 | 必填 | 说明 | 示例 |
|------|------|------|------|------|
| username | String | 是 | 用户名，3-20字符 | newuser |
| password | String | 是 | 密码，须含字母和数字，6-20位 | Admin@123 |
| nickname | String | 否 | 昵称 | 新用户 |
| email | String | 否 | 邮箱（须符合邮箱格式） | user@example.com |
| phone | String | 否 | 手机号（须符合中国手机号格式） | 13800138000 |

**响应格式：**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": null
}
```

**可能错误码：** 2004、400（参数验证失败）

---

#### 4.1.4 POST /auth/switch-tenant - 切换租户

**描述：** 切换当前用户的活跃租户上下文，重新加载权限策略。

**权限要求：** 需登录

**请求参数：**

| 字段 | 类型 | 必填 | 说明 | 示例 |
|------|------|------|------|------|
| tenantCode | String | 是 | 目标租户编码 | TEN0000001 |

**响应格式：**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "tenantCode": "TEN0000001",
    "tenantName": "某某科技有限公司"
  }
}
```

**可能错误码：** 1003、1004、1005、10005

---

### 4.2 用户模块 `/api/v1/users`

#### 4.2.1 POST /users - 创建用户

**描述：** 创建新用户，需指定所属租户。

**权限要求：** `@RequirePermission("user:create")`

**请求参数：**

| 字段 | 类型 | 必填 | 说明 | 示例 |
|------|------|------|------|------|
| userName | String | 是 | 登录用户名 | zhangsan |
| password | String | 是 | 初始密码 | Admin@123 |
| nickName | String | 否 | 昵称 | 张三 |
| email | String | 否 | 邮箱 | zhangsan@example.com |
| phone | String | 否 | 手机号 | 13800138000 |
| avatar | String | 否 | 头像地址 | /upload/avatar/xxx.png |
| status | String | 否 | 用户状态，默认ENABLED | ENABLED |
| tenantCode | String | 否 | 所属租户编码 | TEN0000001 |

**响应格式：**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "id": 1987654321098765432,
    "userCode": "USER_001",
    "userName": "zhangsan"
  }
}
```

**可能错误码：** 2004、1003、3001

---

#### 4.2.2 PUT /users/{id} - 更新用户

**描述：** 更新指定用户信息。

**权限要求：** `@RequirePermission("user:update")`

**路径参数：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 用户ID |

**请求参数：**

| 字段 | 类型 | 必填 | 说明 | 示例 |
|------|------|------|------|------|
| nickName | String | 否 | 昵称 | 张三 |
| email | String | 否 | 邮箱 | zhangsan@example.com |
| phone | String | 否 | 手机号 | 13800138000 |
| avatar | String | 否 | 头像地址 | /upload/avatar/xxx.png |
| status | String | 否 | 用户状态 | ENABLED |

**响应格式：**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": null
}
```

**可能错误码：** 2001、3001

---

#### 4.2.3 DELETE /users/{id} - 删除用户

**描述：** 逻辑删除指定用户。

**权限要求：** `@RequirePermission("user:delete")`

**路径参数：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 用户ID |

**响应格式：**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": null
}
```

**可能错误码：** 2001、3001

---

#### 4.2.4 GET /users/{id} - 查询用户详情

**描述：** 根据ID查询用户详细信息。

**权限要求：** `@RequirePermission("user:view")`

**路径参数：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 用户ID |

**响应格式：**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "id": 1987654321098765432,
    "userCode": "USER_001",
    "userName": "zhangsan",
    "nickName": "张三",
    "email": "zhangsan@example.com",
    "phone": "138****8000",
    "avatar": "/upload/avatar/xxx.png",
    "status": "ENABLED",
    "loginIp": "192.168.1.1",
    "loginDate": "2026-05-19 15:45:30",
    "createAt": "2026-05-19 10:00:00",
    "updateAt": "2026-05-19 15:45:30"
  }
}
```

**可能错误码：** 2001、3001

---

#### 4.2.5 GET /users/page - 分页查询用户

**描述：** 分页查询用户列表，支持条件筛选。

**权限要求：** `@RequirePermission("user:list")`

**请求参数（Query）：**

| 字段 | 类型 | 必填 | 默认值 | 说明 | 示例 |
|------|------|------|--------|------|------|
| pageNum | Integer | 否 | 1 | 当前页码 | 1 |
| pageSize | Integer | 否 | 10 | 每页数量（1-100） | 10 |
| userName | String | 否 | - | 用户名（模糊查询） | zhang |
| nickName | String | 否 | - | 昵称（模糊查询） | 张 |
| phone | String | 否 | - | 手机号 | 13800138000 |
| status | String | 否 | - | 用户状态 | ENABLED |

**响应格式：**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "records": [
      {
        "id": 1987654321098765432,
        "userCode": "USER_001",
        "userName": "zhangsan",
        "nickName": "张三",
        "email": "zhangsan@example.com",
        "phone": "138****8000",
        "status": "ENABLED",
        "createAt": "2026-05-19 10:00:00"
      }
    ],
    "total": 50,
    "pageNum": 1,
    "pageSize": 10,
    "pages": 5
  }
}
```

**可能错误码：** 3001、400（参数验证失败）

---

#### 4.2.6 PUT /users/{id}/password - 修改密码

**描述：** 用户修改自己的密码。

**权限要求：** 需登录（仅允许修改自己的密码）

**路径参数：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 用户ID |

**请求参数：**

| 字段 | 类型 | 必填 | 说明 | 示例 |
|------|------|------|------|------|
| oldPassword | String | 是 | 原密码 | OldPass123 |
| newPassword | String | 是 | 新密码，须含字母和数字，6-20位 | NewPass456 |

**响应格式：**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": null
}
```

**可能错误码：** 2001、2002、10005

---

#### 4.2.7 POST /users/{id}/reset-password - 重置密码

**描述：** 管理员重置指定用户的密码。

**权限要求：** `@RequirePermission("user:reset-password")`

**路径参数：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 用户ID |

**请求参数：**

| 字段 | 类型 | 必填 | 说明 | 示例 |
|------|------|------|------|------|
| newPassword | String | 否 | 新密码，不传则使用默认密码 | ResetPass123 |

**响应格式：**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": null
}
```

**可能错误码：** 2001、3001

---

### 4.3 租户模块 `/api/v1/tenants`

#### 4.3.1 POST /tenants - 创建租户

**描述：** 创建新租户，支持指定父租户实现层级关系。

**权限要求：** `@RequirePermission("tenant:create")`

**请求参数（SysTenantAddRTO）：**

| 字段 | 类型 | 必填 | 说明 | 示例 |
|------|------|------|------|------|
| tenantCode | String | 是 | 租户编码，不能为空 | TEN0000001 |
| tenantName | String | 是 | 租户名称，2-100字符 | 某某科技有限公司 |
| tenantType | String | 是 | 租户类型，2-50字符 | 互联网 |
| tenantDesc | String | 否 | 租户描述，最多500字符 | 这是...类型公司 |
| tenantLogoUrl | String | 否 | 租户logo路径 | /upload/tenant/logo/xxx.png |
| parentCode | String | 否 | 父租户编码 | PAREN_TENANT_001 |
| contactName | String | 否 | 联系人姓名 | 张三 |
| contactPhone | String | 否 | 联系人电话（须符合手机号格式） | 13800138000 |
| expireTime | LocalDateTime | 否 | 服务过期时间 | 2027-12-31 23:59:59 |

**响应格式：**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": 1
}
```

**可能错误码：** 1001、1002、3001

---

#### 4.3.2 PUT /tenants/{id} - 更新租户

**描述：** 更新指定租户信息。

**权限要求：** `@RequirePermission("tenant:update")`

**路径参数：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 租户ID |

**请求参数（SysTenantUpdateRTO）：**

| 字段 | 类型 | 必填 | 说明 | 示例 |
|------|------|------|------|------|
| tenantCode | String | 是 | 租户编码 | TEN0000001 |
| tenantName | String | 是 | 租户名称，2-100字符 | 某某科技有限公司 |
| tenantType | String | 是 | 租户类型，2-50字符 | 互联网 |
| tenantDesc | String | 否 | 租户描述，最多500字符 | 这是...类型公司 |
| tenantLogoUrl | String | 否 | 租户logo路径 | /upload/tenant/logo/xxx.png |
| contactName | String | 否 | 联系人姓名 | 张三 |
| contactPhone | String | 否 | 联系人电话 | 13800138000 |

**响应格式：**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": null
}
```

**可能错误码：** 1003、3001

---

#### 4.3.3 DELETE /tenants/{id} - 删除租户

**描述：** 逻辑删除指定租户。

**权限要求：** `@RequirePermission("tenant:delete")`

**路径参数：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 租户ID |

**响应格式：**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": null
}
```

**可能错误码：** 1003、3001

---

#### 4.3.4 GET /tenants/{id} - 查询租户详情

**描述：** 根据ID查询租户详细信息。

**权限要求：** `@RequirePermission("tenant:view")`

**路径参数：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 租户ID |

**响应格式：**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "id": 1987654321098765432,
    "tenantCode": "TEN***001",
    "tenantName": "某某科技有限公司",
    "tenantType": "互联网",
    "tenantDesc": "这是...类型公司",
    "tenantLogoUrl": "/upload/tenant/logo/xxx.png",
    "parentCode": "PAREN_TENANT_001",
    "parentName": "父租户名称",
    "path": "0,100,200",
    "contactName": "张三",
    "contactPhone": "138****8000",
    "status": "ENABLED",
    "expireTime": "2027-12-31 23:59:59",
    "hasChildren": true,
    "createAt": "2026-04-07 10:00:00",
    "updateAt": "2026-05-01 14:30:00"
  }
}
```

> 租户编码自动脱敏处理（保留前3位和后3位）

**可能错误码：** 1003、3001

---

#### 4.3.5 GET /tenants/tree - 查询租户树

**描述：** 查询所有租户的层级树形结构。

**权限要求：** `@RequirePermission("tenant:view")`

**请求参数：** 无

**响应格式：**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "id": 1,
      "tenantCode": "TEN***001",
      "tenantName": "根租户",
      "children": [
        {
          "id": 2,
          "tenantCode": "TEN***002",
          "tenantName": "子租户A",
          "children": []
        }
      ]
    }
  ]
}
```

**可能错误码：** 3001

---

#### 4.3.6 GET /tenants/page - 分页查询租户

**描述：** 分页查询租户列表，支持条件筛选。

**权限要求：** `@RequirePermission("tenant:list")`

**请求参数（Query）：**

| 字段 | 类型 | 必填 | 默认值 | 说明 | 示例 |
|------|------|------|--------|------|------|
| pageNum | Integer | 否 | 1 | 当前页码 | 1 |
| pageSize | Integer | 否 | 10 | 每页数量（1-100） | 10 |
| tenantCode | String | 否 | - | 租户编码 | TEN000001 |
| tenantName | String | 否 | - | 租户名称（模糊查询） | 科技 |
| tenantType | String | 否 | - | 租户类型 | 互联网 |
| contactName | String | 否 | - | 联系人姓名 | 张三 |
| contactPhone | String | 否 | - | 联系人电话 | 13800138000 |
| status | String | 否 | - | 状态 | ENABLED |

**响应格式：**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "records": [
      {
        "id": 1987654321098765432,
        "tenantCode": "TEN***001",
        "tenantName": "某某科技有限公司",
        "tenantType": "互联网",
        "contactName": "张三",
        "contactPhone": "138****8000",
        "status": "ENABLED",
        "expireTime": "2027-12-31 23:59:59",
        "createAt": "2026-04-07 10:00:00"
      }
    ],
    "total": 25,
    "pageNum": 1,
    "pageSize": 10,
    "pages": 3
  }
}
```

**可能错误码：** 3001、400（参数验证失败）

---

#### 4.3.7 PUT /tenants/{id}/status - 更新租户状态

**描述：** 更新租户状态（启用/停用/过期）。

**权限要求：** `@RequirePermission("tenant:update")`

**路径参数：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 租户ID |

**请求参数：**

| 字段 | 类型 | 必填 | 说明 | 示例 |
|------|------|------|------|------|
| status | String | 是 | 目标状态（ENABLED/DISABLED/EXPIRED） | DISABLED |

**响应格式：**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": null
}
```

**可能错误码：** 1003、3001

---

### 4.4 角色模块 `/api/v1/roles`

#### 4.4.1 POST /roles - 创建角色

**描述：** 创建新角色。

**权限要求：** `@RequirePermission("role:create")`

**请求参数：**

| 字段 | 类型 | 必填 | 说明 | 示例 |
|------|------|------|------|------|
| roleName | String | 是 | 角色名称 | 租户管理员 |
| roleCode | String | 是 | 角色编码 | TENANT_ADMIN |
| roleDesc | String | 否 | 角色描述 | 租户级别管理员 |
| dataScope | String | 否 | 数据范围 | TENANT |

**响应格式：**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "id": 1987654321098765432,
    "roleName": "租户管理员",
    "roleCode": "TENANT_ADMIN"
  }
}
```

**可能错误码：** 3003、3001

---

#### 4.4.2 PUT /roles/{id} - 更新角色

**描述：** 更新指定角色信息。

**权限要求：** `@RequirePermission("role:update")`

**路径参数：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 角色ID |

**请求参数：**

| 字段 | 类型 | 必填 | 说明 | 示例 |
|------|------|------|------|------|
| roleName | String | 否 | 角色名称 | 租户管理员 |
| roleDesc | String | 否 | 角色描述 | 租户级别管理员 |
| dataScope | String | 否 | 数据范围 | TENANT |
| status | String | 否 | 角色状态 | ENABLED |

**响应格式：**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": null
}
```

**可能错误码：** 3002、3001

---

#### 4.4.3 DELETE /roles/{id} - 删除角色

**描述：** 逻辑删除指定角色。

**权限要求：** `@RequirePermission("role:delete")`

**路径参数：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 角色ID |

**响应格式：**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": null
}
```

**可能错误码：** 3002、3001

---

#### 4.4.4 GET /roles - 查询角色列表

**描述：** 查询当前租户下的角色列表。

**权限要求：** `@RequirePermission("role:list")`

**请求参数（Query）：**

| 字段 | 类型 | 必填 | 说明 | 示例 |
|------|------|------|------|------|
| roleName | String | 否 | 角色名称（模糊查询） | 管理员 |
| roleCode | String | 否 | 角色编码 | TENANT_ADMIN |
| status | String | 否 | 角色状态 | ENABLED |

**响应格式：**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "id": 1,
      "roleName": "租户管理员",
      "roleCode": "TENANT_ADMIN",
      "roleDesc": "租户级别管理员",
      "dataScope": "TENANT",
      "status": "ENABLED"
    }
  ]
}
```

**可能错误码：** 3001

---

#### 4.4.5 POST /users/{id}/roles - 分配用户角色

**描述：** 为指定用户分配角色，支持批量分配。

**权限要求：** `@RequirePermission("role:assign")`

**路径参数：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 用户ID |

**请求参数：**

| 字段 | 类型 | 必填 | 说明 | 示例 |
|------|------|------|------|------|
| roleIds | Array\<Long\> | 是 | 角色ID列表 | [1, 2, 3] |

**响应格式：**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": null
}
```

**可能错误码：** 2001、3002、3001

---

### 4.5 权限模块 `/api/v1/perms`

#### 4.5.1 POST /perms - 创建权限

**描述：** 创建新的权限资源。

**权限要求：** `@RequirePermission("perm:create")`

**请求参数：**

| 字段 | 类型 | 必填 | 说明 | 示例 |
|------|------|------|------|------|
| permName | String | 是 | 权限名称 | 用户管理 |
| permDesc | String | 否 | 权限描述 | 管理系统用户信息 |
| permCode | String | 是 | 权限编码 | SYS_USER |
| permKey | String | 是 | 权限标识 | user:list |
| permType | String | 是 | 权限类型（MENU/BUTTON/API） | MENU |
| parentId | Long | 否 | 父权限ID，0为顶级 | 1 |
| parentName | String | 否 | 父权限名称 | 系统管理 |
| path | String | 否 | 权限路径 | /system/user |
| status | String | 否 | 权限状态，默认ENABLED | ENABLED |

**响应格式：**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "id": 1987654321098765432,
    "permName": "用户管理",
    "permCode": "SYS_USER"
  }
}
```

**可能错误码：** 3001

---

#### 4.5.2 PUT /perms/{id} - 更新权限

**描述：** 更新指定权限资源信息。

**权限要求：** `@RequirePermission("perm:update")`

**路径参数：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 权限ID |

**请求参数：**

| 字段 | 类型 | 必填 | 说明 | 示例 |
|------|------|------|------|------|
| permName | String | 否 | 权限名称 | 用户管理 |
| permDesc | String | 否 | 权限描述 | 管理系统用户信息 |
| permKey | String | 否 | 权限标识 | user:list |
| permType | String | 否 | 权限类型 | MENU |
| path | String | 否 | 权限路径 | /system/user |
| status | String | 否 | 权限状态 | ENABLED |

**响应格式：**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": null
}
```

**可能错误码：** 3001

---

#### 4.5.3 DELETE /perms/{id} - 删除权限

**描述：** 逻辑删除指定权限资源。

**权限要求：** `@RequirePermission("perm:delete")`

**路径参数：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 权限ID |

**响应格式：**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": null
}
```

**可能错误码：** 3001

---

#### 4.5.4 GET /perms/tree - 查询权限树

**描述：** 查询权限资源的树形结构。

**权限要求：** `@RequirePermission("perm:list")`

**请求参数：** 无

**响应格式：**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "id": 1,
      "permName": "系统管理",
      "permCode": "SYSTEM",
      "permKey": "system",
      "permType": "MENU",
      "path": "/system",
      "children": [
        {
          "id": 2,
          "permName": "用户管理",
          "permCode": "SYS_USER",
          "permKey": "user:list",
          "permType": "MENU",
          "path": "/system/user",
          "children": []
        }
      ]
    }
  ]
}
```

**可能错误码：** 3001

---

### 4.6 权限策略模块 `/api/v1/perm-policies`

#### 4.6.1 POST /perm-policies - 创建策略

**描述：** 创建权限策略，定义权限在指定数据表上的字段级控制。

**权限要求：** `@RequirePermission("policy:create")`

**请求参数：**

| 字段 | 类型 | 必填 | 说明 | 示例 |
|------|------|------|------|------|
| policyCode | String | 是 | 策略编码 | POLICY_001 |
| policyName | String | 是 | 策略名称 | 用户数据读写权限 |
| targetId | Long | 是 | 授权目标ID（租户/角色/用户ID） | 100 |
| targetType | String | 是 | 授权目标类型（TENANT/ROLE/USER） | USER |
| permId | Long | 是 | 关联权限ID | 1 |
| tenantId | Long | 是 | 所属租户ID | 1 |
| tableName | String | 是 | 控制的数据表名 | sys_user |
| tableDesc | String | 否 | 数据表描述 | 系统用户表 |
| accessType | String | 是 | 访问类型（READ_ONLY/READ_WRITE） | READ_WRITE |
| fieldOperates | String | 否 | 允许操作的字段，JSON格式 | ["id","name","email"] |
| status | String | 否 | 策略状态，默认ACTIVE | ACTIVE |

**响应格式：**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "id": 1987654321098765432,
    "policyCode": "POLICY_001",
    "policyName": "用户数据读写权限"
  }
}
```

**可能错误码：** 3001、1003

---

#### 4.6.2 PUT /perm-policies/{id} - 更新策略

**描述：** 更新指定权限策略信息。

**权限要求：** `@RequirePermission("policy:update")`

**路径参数：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 策略ID |

**请求参数：**

| 字段 | 类型 | 必填 | 说明 | 示例 |
|------|------|------|------|------|
| policyName | String | 否 | 策略名称 | 用户数据读写权限 |
| accessType | String | 否 | 访问类型 | READ_WRITE |
| fieldOperates | String | 否 | 允许操作的字段 | ["id","name","email"] |
| status | String | 否 | 策略状态 | ACTIVE |

**响应格式：**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": null
}
```

**可能错误码：** 3001

---

#### 4.6.3 DELETE /perm-policies/{id} - 删除策略

**描述：** 逻辑删除指定权限策略。

**权限要求：** `@RequirePermission("policy:delete")`

**路径参数：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 策略ID |

**响应格式：**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": null
}
```

**可能错误码：** 3001

---

#### 4.6.4 GET /perm-policies/page - 分页查询策略

**描述：** 分页查询权限策略列表。

**权限要求：** `@RequirePermission("policy:list")`

**请求参数（Query）：**

| 字段 | 类型 | 必填 | 默认值 | 说明 | 示例 |
|------|------|------|--------|------|------|
| pageNum | Integer | 否 | 1 | 当前页码 | 1 |
| pageSize | Integer | 否 | 10 | 每页数量（1-100） | 10 |
| policyCode | String | 否 | - | 策略编码 | POLICY_001 |
| policyName | String | 否 | - | 策略名称（模糊查询） | 读写 |
| targetType | String | 否 | - | 授权目标类型 | USER |
| tableName | String | 否 | - | 数据表名 | sys_user |
| status | String | 否 | - | 策略状态 | ACTIVE |

**响应格式：**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "records": [
      {
        "id": 1987654321098765432,
        "policyCode": "POLICY_001",
        "policyName": "用户数据读写权限",
        "targetType": "USER",
        "tableName": "sys_user",
        "accessType": "READ_WRITE",
        "status": "ACTIVE",
        "createAt": "2026-05-19 10:00:00"
      }
    ],
    "total": 15,
    "pageNum": 1,
    "pageSize": 10,
    "pages": 2
  }
}
```

**可能错误码：** 3001

---

### 4.7 Token管理模块 `/api/v1/tokens`

#### 4.7.1 GET /tokens/devices - 查询登录设备

**描述：** 查询当前用户的所有登录设备（Token记录）。

**权限要求：** 需登录

**请求参数：** 无

**响应格式：**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "id": 1,
      "userId": 1987654321098765432,
      "tenantId": 1,
      "token": "eyJhbG...（脱敏）",
      "deviceInfo": "Chrome/Windows",
      "loginIp": "192.168.1.1",
      "loginAt": "2026-05-23 09:00:00",
      "expireTime": "2026-05-23 21:00:00",
      "status": "ONLINE"
    }
  ]
}
```

**可能错误码：** 10005

---

#### 4.7.2 DELETE /tokens/{id} - 强制下线

**描述：** 强制指定Token下线（踢出用户）。

**权限要求：** `@RequirePermission("token:kickout")`

**路径参数：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | Token记录ID |

**响应格式：**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": null
}
```

**可能错误码：** 10005、3001

---

## 5. 登录接口五层级认证流程

登录接口采用五层级认证机制，逐层校验确保安全。以下为完整流程：

### 5.1 流程总览

```
客户端请求 → [第一层] → [第二层] → [第三层] → [第四层] → [第五层] → 登录成功
              用户校验    密码校验    租户关联    租户状态    Sa-Token登录
```

### 5.2 第一层：用户基本信息校验

| 项目 | 说明 |
|------|------|
| 校验内容 | 根据用户名查询 `sys_user` 表，验证用户是否存在 |
| 数据表 | `sys_user` |
| 查询条件 | `user_name = #{username} AND is_deleted = 'NOT_DELETED'` |
| 失败处理 | 抛出 BusinessException("用户名或密码不正确")，错误码 2001 |
| 安全策略 | 不区分"用户不存在"和"密码错误"，统一返回相同提示，防止用户名枚举攻击 |

### 5.3 第二层：密码校验

| 项目 | 说明 |
|------|------|
| 校验内容 | 比对用户输入密码与数据库存储的加密密码 |
| 数据表 | `sys_user` |
| 比对方式 | 明文比对（当前实现），后续升级为 BCrypt 哈希比对 |
| 失败处理 | 抛出 BusinessException("用户名或密码不正确")，错误码 2002 |
| 安全策略 | 连续失败次数达阈值后锁定账号（错误码 2005） |

### 5.4 第三层：用户-租户关联校验

| 项目 | 说明 |
|------|------|
| 校验内容 | 查询用户默认租户关联关系，确认用户已加入租户 |
| 数据表 | `sys_user_tenant_rel` |
| 查询条件 | `user_id = #{userId} AND is_default = true AND is_deleted = 'NOT_DELETED'` |
| 失败处理 | 抛出 BusinessException("用户未加入任何租户") |
| 关联数据 | 返回 `tenantId`，用于第四层租户状态校验 |

### 5.5 第四层：租户状态校验

| 项目 | 说明 |
|------|------|
| 校验内容 | 查询租户信息，校验租户状态是否正常 |
| 数据表 | `sys_tenant` |
| 查询条件 | `id = #{tenantId} AND is_deleted = 'NOT_DELETED'` |
| 状态检查 | DISABLED → 抛出 BusinessException("所属租户已停用")，错误码 1004 |
| | EXPIRED → 抛出 BusinessException("所属租户已过期")，错误码 1005 |
| 失败处理 | 返回对应错误码和提示信息，阻止登录 |

### 5.6 第五层：Sa-Token 登录与权限上下文构建

| 项目 | 说明 |
|------|------|
| 校验内容 | 执行 Sa-Token 登录，构建并缓存用户权限上下文 |
| 核心操作 | `StpUtil.login(userId)` 生成 JWT Token |
| 缓存策略 | 优先从 Sa-Token Session 读取已缓存的 `userContext`，命中则跳过后续查询 |
| 数据表 | `sys_user_perm_rel`、`sys_perm_policy`、`sys_perm` |

**权限上下文构建步骤（缓存未命中时）：**

1. 查询用户权限策略关联（`sys_user_perm_rel`）
2. 批量查询权限策略详情（`sys_perm_policy`）
3. 查询权限资源详情（`sys_perm`）
4. 分类汇总权限编码（有效/无效）
5. 构建字段级权限映射（query/create/update 操作 × 表名 → 可见/不可见字段）
6. 组装 `UserContextDTO` 并缓存到 Sa-Token Session（Redis 持久化）

**UserContextDTO 数据结构：**

```
UserContextDTO
├── tenantInfo
│   ├── tenantCode        // 租户编码
│   └── tenantName        // 租户名称
└── permInfo
    ├── perms             // 全部权限编码列表 [有效+无效]
    ├── validPerms        // 有效权限编码列表
    ├── invalidPerm       // 无效权限编码列表
    ├── query             // 查询字段权限 {表名: {visibleFields, invisibleFields}}
    ├── create            // 创建字段权限 {表名: {visibleFields, invisibleFields}}
    └── update            // 更新字段权限 {表名: {visibleFields, invisibleFields}}
```

---

## 6. 权限控制说明

### 6.1 @RequirePermission 注解

自定义权限注解，用于接口级别的权限控制：

```java
@RequirePermission("user:create")
@PostMapping("/users")
public ApiResponse createUser(@RequestBody UserCreateRTO param) {
    // ...
}
```

**工作原理：**

1. 通过 AOP 切面拦截标注了 `@RequirePermission` 的方法
2. 从 Sa-Token Session 中获取当前用户的权限上下文（`UserContextDTO.permInfo.validPerms`）
3. 校验用户是否持有注解中指定的权限标识
4. 校验不通过则抛出异常，返回错误码 3001

### 6.2 Sa-Token 框架级校验

Sa-Token 提供的框架级权限校验，通过 `SaInterceptor` 拦截器实现：

**登录校验：**

```java
registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
    .addPathPatterns("/tenant/**", "/sys-user/**", "/sys-role/**", ...)
    .excludePathPatterns("/auth/login", "/auth/register", "/doc.html#/**");
```

**Sa-Token 异常处理（GlobalExceptionHandler）：**

| 异常类型 | HTTP状态码 | 说明 |
|----------|------------|------|
| NotLoginException | 401 | 未登录 |
| NotRoleException | 403 | 缺少角色 |
| NotPermissionException | 403 | 缺少权限 |
| NotSafeException | 403 | 二级认证校验失败 |
| DisableServiceException | 403 | 服务封禁 |

### 6.3 数据范围拦截

数据范围拦截基于权限策略（`sys_perm_policy`）实现字段级权限控制：

**拦截层级：**

| 层级 | 策略目标类型 | 说明 |
|------|-------------|------|
| 租户级 | TENANT | 租户维度的权限策略，影响租户下所有用户 |
| 角色级 | ROLE | 角色维度的权限策略，影响拥有该角色的用户 |
| 用户级 | USER | 用户维度的权限策略，仅影响特定用户 |

**字段权限控制：**

权限策略通过 `field_operates`（JSON格式）定义允许操作的字段，通过 `field_un_operates` 定义禁止操作的字段。系统在构建用户权限上下文时，会合并所有层级的策略，生成最终的：

- `query`：查询操作可见/不可见字段映射
- `create`：创建操作可见/不可见字段映射
- `update`：更新操作可见/不可见字段映射

**数据流：**

```
请求 → SaInterceptor(登录校验) → @RequirePermission(权限校验) → 数据范围拦截(字段过滤) → 业务逻辑
```

---

## 7. 请求/响应示例

### 7.1 登录请求

**请求：**

```http
POST /api/v1/auth/login HTTP/1.1
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

**成功响应：**

```http
HTTP/1.1 200 OK
Content-Type: application/json
Authorization: NexusIX eyJhbGciOiJIUzI1NiJ9.eyJsb2dpblR5cGUiOiJsb2dpbiIsImxvZ2luSWQiOjEwMDAwMX0.xxx

{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJsb2dpblR5cGUiOiJsb2dpbiIsImxvZ2luSWQiOjEwMDAwMX0.xxx",
    "username": "admin",
    "nickname": "管理员",
    "tenantName": "默认租户"
  }
}
```

**失败响应（用户名或密码错误）：**

```json
{
  "code": 2001,
  "msg": "用户名或密码不正确",
  "data": null
}
```

**失败响应（租户已停用）：**

```json
{
  "code": 1004,
  "msg": "所属租户已停用",
  "data": null
}
```

### 7.2 创建租户请求

**请求：**

```http
POST /api/v1/tenants HTTP/1.1
Content-Type: application/json
Authorization: NexusIX eyJhbGciOiJIUzI1NiJ9...

{
  "tenantCode": "TEN0000001",
  "tenantName": "某某科技有限公司",
  "tenantType": "互联网",
  "tenantDesc": "一家互联网科技公司",
  "parentCode": "PAREN_TENANT_001",
  "contactName": "张三",
  "contactPhone": "13800138000",
  "expireTime": "2027-12-31 23:59:59"
}
```

**成功响应：**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": 1
}
```

**失败响应（租户编码已存在）：**

```json
{
  "code": 1001,
  "msg": "租户编码已存在",
  "data": null
}
```

### 7.3 分页查询用户请求

**请求：**

```http
GET /api/v1/users/page?pageNum=1&pageSize=10&userName=zhang&status=ENABLED HTTP/1.1
Authorization: NexusIX eyJhbGciOiJIUzI1NiJ9...
```

**成功响应：**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "records": [
      {
        "id": 1987654321098765432,
        "userCode": "USER_001",
        "userName": "zhangsan",
        "nickName": "张三",
        "email": "zhangsan@example.com",
        "phone": "138****8000",
        "status": "ENABLED",
        "createAt": "2026-05-19 10:00:00"
      },
      {
        "id": 1987654321098765433,
        "userCode": "USER_002",
        "userName": "zhangwei",
        "nickName": "张伟",
        "email": "zhangwei@example.com",
        "phone": "139****9000",
        "status": "ENABLED",
        "createAt": "2026-05-20 14:00:00"
      }
    ],
    "total": 2,
    "pageNum": 1,
    "pageSize": 10,
    "pages": 1
  }
}
```

### 7.4 未授权请求

**请求（无Token）：**

```http
GET /api/v1/users/page HTTP/1.1
```

**响应：**

```json
{
  "code": 401,
  "msg": "未登录：无效的Token",
  "data": null
}
```

### 7.5 权限不足请求

**请求（缺少权限）：**

```http
DELETE /api/v1/users/1987654321098765432 HTTP/1.1
Authorization: NexusIX eyJhbGciOiJIUzI1NiJ9...
```

**响应：**

```json
{
  "code": 403,
  "msg": "缺少权限：user:delete",
  "data": null
}
```

### 7.6 参数验证失败请求

**请求（缺少必填字段）：**

```http
POST /api/v1/auth/register HTTP/1.1
Content-Type: application/json

{
  "username": "ab",
  "password": "123"
}
```

**响应：**

```json
{
  "code": 400,
  "msg": "参数验证失败: username: 用户名长度必须在3-20个字符之间; password: 密码必须包含字母和数字，长度6-20位",
  "data": null
}
```

### 7.7 查询权限树请求

**请求：**

```http
GET /api/v1/perms/tree HTTP/1.1
Authorization: NexusIX eyJhbGciOiJIUzI1NiJ9...
```

**成功响应：**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "id": 1,
      "permName": "系统管理",
      "permCode": "SYSTEM",
      "permKey": "system",
      "permType": "MENU",
      "path": "/system",
      "status": "ENABLED",
      "children": [
        {
          "id": 2,
          "permName": "用户管理",
          "permCode": "SYS_USER",
          "permKey": "user:list",
          "permType": "MENU",
          "path": "/system/user",
          "status": "ENABLED",
          "children": [
            {
              "id": 3,
              "permName": "创建用户",
              "permCode": "SYS_USER_CREATE",
              "permKey": "user:create",
              "permType": "BUTTON",
              "path": "/system/user/create",
              "status": "ENABLED",
              "children": []
            }
          ]
        }
      ]
    }
  ]
}
```
