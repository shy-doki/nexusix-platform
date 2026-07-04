# NexusIX-Platform IAM 身份认证模块实现文档

> 模块范围：`nexusix-iam`
>
> 文档版本：v1.0
>
> 最后更新：2026-07-04

---

## 目录

1. [模块概述](#1-模块概述)
2. [模块结构](#2-模块结构)
3. [核心实体说明](#3-核心实体说明)
4. [接口文档（API列表）](#4-接口文档api列表)
5. [核心业务逻辑实现](#5-核心业务逻辑实现)
6. [数据流转](#6-数据流转)
7. [已实现功能点清单](#7-已实现功能点清单)
8. [未实现功能点清单](#8-未实现功能点清单)
9. [测试结果](#9-测试结果)

---

## 1. 模块概述

### 1.1 模块定位

`nexusix-iam` 是 NexusIX-Platform 多租户 SaaS 平台的**身份认证与授权中心**，承担以下核心职责：

- **用户认证**：基于 Sa-Token JWT 的登录/登出
- **用户管理**：用户的 CRUD、批量操作、状态切换
- **角色管理**：角色的 CRUD、批量操作、状态切换
- **权限管理**：权限的 CRUD、树形结构、批量操作
- **权限策略管理**：4 级权限策略（TENANT/DEPT/ROLE/USER）的绑定与解绑
- **用户策略管理**：用户与租户/部门/角色的绑定关系管理
- **用户上下文构建**：登录时组装 `UserContextDTO` 并写入 Sa-Token Session，包含完整的权限/租户/角色/部门信息

### 1.2 核心特性

1. **4 级权限禁用体系**：通过 `UNION ALL` 合并 TENANT/USER/ROLE/DEPT 级权限查询，使用 `COALESCE` 处理权限策略继承，实现 `SYSTEM > TENANT > DEPT > ROLE > USER` 的层级禁用。
2. **字段级权限**：登录时解析权限策略的 `field_operation` JSONB 字段，按 `READ/CREATE/UPDATE` 分类到 `query/create/update` 的 `operable/inoperable` 列表。
3. **物化路径树形结构**：权限表 `sys_perm` 通过 `parent_id/path/level` 字段实现物化路径树形结构，支持高效的子树查询。
4. **超级管理员审计字段控制**：通过 `StpUtil.hasRole("super_admin")` 区分超级管理员与非超级管理员，前者可填写审计字段，后者由系统强制覆盖。
5. **MapStruct 转换器**：`@Mapper(componentModel = "spring")` 实现 RTO ↔ Entity ↔ VO 转换，状态/删除标记使用 `@Named` 限定方法转换。

### 1.3 技术栈

| 类别 | 选型 |
|------|------|
| 鉴权 | Sa-Token 1.42（JWT Simple 模式） |
| ORM | MyBatis-Plus 3.5.12 |
| 密码加密 | Spring Security `BCryptPasswordEncoder` |
| 对象转换 | MapStruct |
| 数据库 | PostgreSQL（JSONB 存储 `field_operation`） |

---

## 2. 模块结构

```
nexusix-iam/
└── src/main/java/com/shy/nexusix/iam/
    ├── controller/
    │   ├── AuthController.java                 # 认证控制器（登录接口）
    │   ├── SysUserController.java              # 用户管理（12 个接口）
    │   ├── SysRoleController.java              # 角色管理（12 个接口）
    │   ├── SysPermController.java              # 权限管理（15 个接口）
    │   ├── SysPermPolicyController.java        # 权限策略管理（含 bind/unbind）
    │   └── SysUserPolicyController.java        # 用户策略管理
    ├── service/
    │   ├── IAuthService.java
    │   ├── ISysUserService.java
    │   ├── ISysRoleService.java
    │   ├── ISysPermService.java
    │   ├── ISysPermPolicyService.java
    │   ├── ISysUserPolicyService.java
    │   └── impl/
    │       ├── AuthServiceImpl.java            # 登录核心实现（约 665 行）
    │       ├── SysUserServiceImpl.java         # 用户 CRUD（BCrypt + 审计字段控制）
    │       ├── SysRoleServiceImpl.java
    │       ├── SysPermServiceImpl.java
    │       ├── SysPermPolicyServiceImpl.java   # 含 batchBindToTarget
    │       └── SysUserPolicyServiceImpl.java
    ├── entity/
    │   ├── SysUser.java                        # sys_user 表
    │   ├── SysRole.java                        # sys_role 表
    │   ├── SysPerm.java                        # sys_perm 表（树形结构）
    │   ├── SysPermPolicy.java                  # sys_perm_policy 表（JSONB 字段级权限）
    │   └── SysUserPolicy.java                  # sys_user_policy 表
    ├── rto/
    │   ├── LoginRTO.java                       # 登录请求
    │   ├── SysUserAddRTO.java                  # 用户新增（含密码正则）
    │   ├── SysUserUpdateRTO.java
    │   ├── SysUserQueryRTO.java
    │   └── ...（Role/Perm/PermPolicy/UserPolicy RTO）
    ├── vo/
    │   ├── SysUserCommonVO.java
    │   ├── SysUserDetailVO.java
    │   ├── SysRoleCommonVO.java
    │   ├── SysPermTreeVO.java
    │   └── ...
    ├── dto/
    │   ├── UserPermDTO.java                    # 用户权限 DTO（含 fieldPermissions）
    │   ├── UserTenantItemDTO.java              # 用户租户项
    │   ├── UserRoleDTO.java                    # 用户角色
    │   └── UserDeptDTO.java                    # 用户部门
    ├── converter/
    │   ├── SysUserConverter.java               # MapStruct 转换器
    │   ├── SysRoleConverter.java
    │   ├── SysPermConverter.java
    │   └── SysPermPolicyConverter.java
    └── mapper/
        ├── SysUserMapper.java
        ├── SysRoleMapper.java
        ├── SysPermMapper.java
        ├── SysPermPolicyMapper.java            # 4 级权限 UNION ALL 查询
        └── SysUserPolicyMapper.java            # 用户租户/角色/部门信息查询
```

**Mapper XML 资源文件**：

```
src/main/resources/mapper/
├── SysUserMapper.xml           # 空（使用 MyBatis-Plus BaseMapper）
├── SysPermPolicyMapper.xml     # 核心：4 级权限 UNION ALL 查询 SQL
└── SysUserPolicyMapper.xml     # 用户租户/角色/部门信息查询 SQL
```

---

## 3. 核心实体说明

### 3.1 SysUser（用户实体）

**文件**：`nexusix-iam/src/main/java/com/shy/nexusix/iam/entity/SysUser.java`
**表名**：`sys_user`

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | `Long` | 主键（雪花算法） |
| `userCode` | `String` | 用户编码 |
| `userName` | `String` | 用户名（登录名） |
| `nickName` | `String` | 昵称 |
| `realName` | `String` | 真实姓名 |
| `email` | `String` | 邮箱 |
| `phone` | `String` | 手机号 |
| `password` | `String` | 密码（BCrypt 加密） |
| `avatarUrl` | `String` | 头像 URL |
| `gender` | `Integer` | 性别 |
| `birthday` | `LocalDate` | 生日 |
| `status` | `String` | 状态：`ENABLED / DISABLED / LOCKED` |
| `disableReason` | `String` | 禁用原因 |
| `lastLoginAt` | `LocalDateTime` | 最后登录时间 |
| `lastLoginIp` | `String` | 最后登录 IP |
| `createTenant` | `Long` | 创建时所属租户 ID |
| `createDept` | `Long` | 创建时所属部门 ID |
| `createRole` | `Long` | 创建时使用角色 ID |
| `createBy` | `Long` | 创建人 ID |
| `createAt` | `LocalDateTime` | 创建时间 |
| `updateBy` | `Long` | 更新人 ID |
| `updateAt` | `LocalDateTime` | 更新时间 |
| `isDeleted` | `String` | 逻辑删除：`NOT_DELETED / DELETED` |
| `deletedAt` | `LocalDateTime` | 删除时间 |

### 3.2 SysPerm（权限实体 - 树形结构）

**文件**：`nexusix-iam/src/main/java/com/shy/nexusix/iam/entity/SysPerm.java`
**表名**：`sys_perm`

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | `Long` | 主键 |
| `permCode` | `String` | 权限编码 |
| `permName` | `String` | 权限名称 |
| `parentId` | `Long` | 父权限 ID（0 表示根） |
| `path` | `String` | 物化路径（如 `/101/111`） |
| `level` | `Integer` | 层级深度 |
| `permType` | `String` | 权限类型：`MENU / BUTTON / API / DATA` |
| `resourceType` | `String` | 资源类型 |
| `resourcePath` | `String` | 资源路径（如 `/user/list`） |
| `resourceMethod` | `String` | HTTP 方法（GET/POST/PUT/DELETE） |
| `status` | `String` | 状态：`ENABLED / DISABLED` |
| 审计字段 | - | 同 SysUser |

**物化路径示例**：

```
根权限（id=101, path=/101, level=1）
├── 子权限（id=111, path=/101/111, level=2）
│   └── 孙权限（id=121, path=/101/111/121, level=3）
└── 子权限（id=112, path=/101/112, level=2）
```

**子树查询**：`WHERE path LIKE '/101/%'` 即可获取 id=101 的所有后代节点。

### 3.3 SysPermPolicy（权限策略实体 - 含字段级权限）

**文件**：`nexusix-iam/src/main/java/com/shy/nexusix/iam/entity/SysPermPolicy.java`
**表名**：`sys_perm_policy`

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | `Long` | 主键 |
| `permId` | `Long` | 权限 ID（可指向系统权限 ID 或父策略 ID） |
| `targetType` | `String` | 目标类型：`TENANT / DEPT / ROLE / USER` |
| `targetId` | `Long` | 目标 ID（租户 ID / 部门租户策略 ID / 角色租户策略 ID / 用户租户策略 ID） |
| `dataScope` | `String` | 数据范围：`ALL / DEPT_AND_SUB / DEPT / SELF` |
| `tableName` | `String` | 关联表名（字段权限作用于哪张表） |
| `fieldPermissions` | `String` | **字段级权限（JSONB）**：`{"field_name": ["READ","CREATE","UPDATE"]}` |
| `status` | `String` | 策略状态：`ACTIVE / DISABLED` |
| 审计字段 | - | 同 SysUser |

> **权限策略继承**：`permId` 字段可指向 `sys_perm.id`（直接权限），也可指向另一个 `sys_perm_policy.id`（继承父策略）。SQL 查询时通过 `COALESCE(pp_parent.perm_id, pp.perm_id)` 处理。

### 3.4 SysUserPolicy（用户策略实体）

**文件**：`nexusix-iam/src/main/java/com/shy/nexusix/iam/entity/SysUserPolicy.java`
**表名**：`sys_user_policy`

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | `Long` | 主键（即 `tenantUserId`，租户用户 ID） |
| `userId` | `Long` | 系统用户 ID |
| `targetType` | `String` | 目标类型：`TENANT / DEPT / ROLE` |
| `targetId` | `Long` | 目标 ID（TENANT 时为租户 ID；DEPT/ROLE 时为 `sys_tenant_policy.id`） |
| `isPrimary` | `Boolean` | 是否主租户/主部门 |
| `status` | `String` | 策略状态：`ACTIVE / DISABLED` |
| 审计字段 | - | 同 SysUser |

**关键设计**：

- 当 `targetType = TENANT` 时，`targetId` 指向 `sys_tenant.id`，记录即为"租户用户"（`tenantUserId`）
- 当 `targetType = ROLE/DEPT` 时，`userId` 指向上述"租户用户"的 `id`（即 `tenantUserId`），`targetId` 指向 `sys_tenant_policy.id`

### 3.5 UserPermDTO（用户权限 DTO）

**文件**：`nexusix-iam/src/main/java/com/shy/nexusix/iam/dto/UserPermDTO.java`

| 字段 | 类型 | 说明 |
|------|------|------|
| `permCode` | `String` | 权限编码 |
| `permName` | `String` | 权限名称 |
| `permType` | `String` | 权限类型 |
| `tenantCode` | `String` | 租户编码 |
| `tenantName` | `String` | 租户名称 |
| `permPolicyStatus` | `String` | 权限策略状态：`ACTIVE / DISABLED / DISABLED_*_LEVEL` |
| `targetType` | `String` | 目标类型：`TENANT / DEPT / ROLE / USER` |
| `dataScope` | `String` | 数据范围 |
| `tableName` | `String` | 关联表名 |
| `fieldPermissions` | `String` | 字段级权限 JSONB 字符串 |
| `tenantUserId` | `Long` | 租户用户 ID |

---

## 4. 接口文档（API列表）

### 4.1 认证接口

#### 4.1.1 用户登录

- **URL**：`POST /NexusIxService/auth/login`
- **Controller**：`AuthController.login`
- **请求体**（`LoginRTO`）：

```json
{
  "username": "admin",
  "password": "abc123456"
}
```

- **响应示例**（成功）：

```json
{
  "code": 200,
  "msg": "登录成功",
  "data": {
    "tokenName": "NexusIX",
    "tokenValue": "eyJhbGciOiJIUzI1NiJ9...",
    "loginId": "1001",
    "userName": "admin",
    "nickName": "超级管理员"
  }
}
```

- **响应示例**（失败）：

```json
{
  "code": 500,
  "msg": "用户名或密码错误",
  "data": null
}
```

### 4.2 用户管理接口（SysUserController）

**基础路径**：`/NexusIxService/user`

| 序号 | 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|------|
| 1 | 查询用户列表 | GET | `/user/list` | 返回所有用户 |
| 2 | 分页查询用户 | GET | `/user/page` | 分页返回用户 |
| 3 | 条件查询用户 | POST | `/user/query` | 按条件分页查询 |
| 4 | 查询用户详情 | GET | `/user/detail/{userCode}` | 返回单个用户详情 |
| 5 | 新增用户 | POST | `/user/add` | 新增用户（BCrypt 加密密码） |
| 6 | 修改用户 | PUT | `/user/update` | 修改用户信息 |
| 7 | 更新用户状态 | PUT | `/user/status` | 切换 ENABLED/DISABLED/LOCKED |
| 8 | 删除用户 | DELETE | `/user/delete` | 逻辑删除 |
| 9 | 批量新增用户 | POST | `/user/batch` | 批量新增 |
| 10 | 批量修改用户 | PUT | `/user/batch` | 批量修改 |
| 11 | 批量更新状态 | PUT | `/user/status/batch` | 批量切换状态 |
| 12 | 批量删除用户 | DELETE | `/user/batch` | 批量逻辑删除 |

#### 4.2.1 新增用户（示例）

- **URL**：`POST /NexusIxService/user/add`
- **请求体**（`SysUserAddRTO`）：

```json
{
  "userCode": "U0001",
  "userName": "zhangsan",
  "nickName": "张三",
  "realName": "张三",
  "email": "zhangsan@example.com",
  "phone": "13800138000",
  "password": "abc123456",
  "gender": 1,
  "status": "ENABLED",
  "isDeleted": "NOT_DELETED"
}
```

**校验规则**：

- `userCode / userName / password`：`@NotBlank`
- `password`：正则 `^(?=.*[a-zA-Z])(?=.*\d).{6,20}$`（6-20 位，须含字母和数字）
- `status`：`@EnumField` 校验必须为 `GlobalEnum.UserStatus` 枚举值
- `isDeleted`：`@EnumField` 校验必须为 `GlobalEnum.Deleted` 枚举值

- **响应示例**：

```json
{
  "code": 200,
  "msg": "success",
  "data": 1
}
```

### 4.3 角色管理接口（SysRoleController）

**基础路径**：`/NexusIxService/role`

提供与用户管理相同的 12 个接口（list/page/query/detail/add/update/status/delete/batch），此处不再赘述。

### 4.4 权限管理接口（SysPermController）

**基础路径**：`/NexusIxService/perm`

| 序号 | 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|------|
| 1 | 查询权限列表 | GET | `/perm/list` | 返回所有权限 |
| 2 | 分页查询权限 | GET | `/perm/page` | 分页返回 |
| 3 | 查询权限树 | GET | `/perm/tree/list` | 返回完整树形结构 |
| 4 | 分页查询权限树 | GET | `/perm/tree/page` | 分页返回树形结构 |
| 5 | 查询指定权限子树 | GET | `/perm/tree/{id}` | 返回指定权限的子树 |
| 6 | 条件查询权限 | POST | `/perm/query` | 按条件分页查询 |
| 7 | 查询权限详情 | GET | `/perm/detail/{permCode}` | 返回单个权限详情 |
| 8 | 新增权限 | POST | `/perm/add` | 新增权限 |
| 9 | 修改权限 | PUT | `/perm/update` | 修改权限 |
| 10 | 更新权限状态 | PUT | `/perm/status` | 切换 ENABLED/DISABLED |
| 11 | 删除权限 | DELETE | `/perm/delete` | 逻辑删除 |
| 12 | 批量新增权限 | POST | `/perm/batch` | 批量新增 |
| 13 | 批量修改权限 | PUT | `/perm/batch` | 批量修改 |
| 14 | 批量更新状态 | PUT | `/perm/status/batch` | 批量切换状态 |
| 15 | 批量删除权限 | DELETE | `/perm/batch` | 批量逻辑删除 |

### 4.5 权限策略管理接口（SysPermPolicyController）

**基础路径**：`/NexusIxService/perm-policy`

| 序号 | 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|------|
| 1 | 查询策略列表 | GET | `/perm-policy/list` | - |
| 2 | 分页查询策略 | GET | `/perm-policy/page` | - |
| 3 | 条件查询策略 | POST | `/perm-policy/query` | - |
| 4 | 查询策略详情 | GET | `/perm-policy/detail/{policyCode}` | - |
| 5 | 新增策略 | POST | `/perm-policy/add` | - |
| 6 | 修改策略 | PUT | `/perm-policy/update` | - |
| 7 | 更新策略状态 | PUT | `/perm-policy/status` | - |
| 8 | 删除策略 | DELETE | `/perm-policy/delete` | - |
| 9 | 批量新增 | POST | `/perm-policy/batch` | - |
| 10 | 批量修改 | PUT | `/perm-policy/batch` | - |
| 11 | 批量更新状态 | PUT | `/perm-policy/status/batch` | - |
| 12 | 批量删除 | DELETE | `/perm-policy/batch` | - |
| 13 | **绑定权限到目标** | POST | `/perm-policy/bind` | 将权限策略批量绑定到 TENANT/DEPT/ROLE/USER |
| 14 | **解绑权限** | POST | `/perm-policy/unbind` | 批量解绑 |

#### 4.5.1 绑定权限到目标（示例）

- **URL**：`POST /NexusIxService/perm-policy/bind`
- **请求体**：

```json
{
  "permId": 101,
  "targetType": "ROLE",
  "targetIdList": [2001, 2002, 2003],
  "dataScope": "DEPT_AND_SUB",
  "tableName": "sys_user",
  "fieldPermissions": "{\"user_name\": [\"READ\"], \"phone\": [\"READ\",\"UPDATE\"]}"
}
```

- **响应示例**：

```json
{
  "code": 200,
  "msg": "success",
  "data": 3
}
```

### 4.6 用户策略管理接口（SysUserPolicyController）

**基础路径**：`/NexusIxService/user-policy`

提供 list/page/query/detail/add/update/status/delete/batch 等接口，管理用户与租户/部门/角色的绑定关系。

---

## 5. 核心业务逻辑实现

### 5.1 登录流程（AuthServiceImpl.login）

**文件**：`nexusix-iam/src/main/java/com/shy/nexusix/iam/service/impl/AuthServiceImpl.java`（约 665 行）

这是 IAM 模块最核心的方法，负责构建完整的 `UserContextDTO`。流程如下：

```
┌─────────────────────────────────────────────────────────────────┐
│  1. 检查是否已登录                                               │
│     StpUtil.isLogin() → 若已登录则返回已登录提示                  │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│  2. 查询用户                                                    │
│     LambdaQueryWrapper 查 sys_user                              │
│     条件：user_name = username AND is_deleted = 'NOT_DELETED'    │
│     用户为空 → 抛出 "用户名或密码错误"                            │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│  3. 校验用户状态                                                │
│     status = DISABLED → 抛出 "用户已禁用"                        │
│     status = LOCKED   → 抛出 "用户已锁定"                        │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│  4. 密码校验（TODO：当前为明文比较）                              │
│     param.getPassword().equals(user.getPassword())              │
│     不匹配 → 抛出 "用户名或密码错误"                              │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│  5. 查询用户所有租户信息                                         │
│     sysUserPolicyMapper.queryUserAllTenantInfo(userId)          │
│     返回 List<UserTenantItemDTO>                                │
│     按 is_primary DESC, tenant_code 排序                         │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│  6. 选择当前租户                                                │
│     优先 isPrimary=true 的主租户                                │
│     无主租户则取列表第一个                                        │
│     校验租户状态：                                               │
│       status = DISABLED → 抛出 "租户已禁用"                      │
│       expireTime < now   → 抛出 "租户已过期"                     │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│  7. Sa-Token 登录                                               │
│     StpUtil.login(user.getId())                                 │
│     更新 lastLoginAt（lastLoginIp 获取真实 IP 为 TODO）          │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│  8. 查询用户所有权限信息（核心 4 级权限 UNION ALL）              │
│     sysPermPolicyMapper.queryUserAllPermInfo(userId)            │
│     返回 List<UserPermDTO>                                      │
│     包含 TENANT + USER + ROLE + DEPT 4 个 UNION ALL 查询         │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│  9. 权限去重                                                    │
│     按 permCode + tenantCode 去重                               │
│     同一权限在同一租户下只保留一条记录                            │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│  10. 分类 current enabled / disabled                            │
│      遍历去重后的权限列表：                                      │
│      permPolicyStatus = ACTIVE → 加入 enabled 集合               │
│      其他状态 → 加入 disabled 集合                               │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│  11. 收集 disabledDetailByTenant                                │
│      按 targetType 分类禁用详情：                                │
│        TARGET_TYPE = TENANT  → system 维度（即租户级权限）       │
│        TARGET_TYPE = USER    → user 维度                        │
│        TARGET_TYPE = ROLE    → role 维度                        │
│        TARGET_TYPE = DEPT    → dept 维度                        │
│      按 tenantCode 分组存储到 Map<tenantCode, DisabledDetail>   │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│  12. 解析字段级权限（JSONB）                                     │
│      遍历所有 ACTIVE 权限策略：                                  │
│      解析 fieldPermissions JSONB：                               │
│        {"user_name": ["READ","CREATE","UPDATE"],                │
│         "phone": ["READ"]}                                      │
│      按 READ/CREATE/UPDATE 分类：                                │
│        READ   → fieldPermission.query.tableName.operable        │
│        CREATE → fieldPermission.create.tableName.operable       │
│        UPDATE → fieldPermission.update.tableName.operable       │
│      未出现的字段加入对应操作的 inoperable 列表                   │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│  13. 构建部门/角色/租户信息                                      │
│      查询 sysUserPolicyMapper.queryUserAllRoleInfo(userId)      │
│      查询 sysUserPolicyMapper.queryUserAllDeptInfo(userId)      │
│      分类到 current/all/valid/invalid                           │
│      valid：状态为 ACTIVE 且关联实体未删除                        │
│      invalid：状态为 DISABLED 或关联实体已删除                    │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│  14. 组装 UserContextDTO 并存入 Session                          │
│      userContext.setUserInfo(userInfo)                          │
│      userContext.setTenantInfo(tenantInfo)                      │
│      userContext.setPermInfo(permInfo)                          │
│      userContext.setRoleInfo(roleInfo)                          │
│      userContext.setDeptInfo(deptInfo)                          │
│      userContext.setFieldPermission(fieldPermission)            │
│      StpUtil.getSession().set(USER_CONTEXT, userContext)        │
└─────────────────────────────────────────────────────────────────┘
```

### 5.2 4 级权限策略 SQL（核心）

**文件**：`nexusix-iam/src/main/resources/mapper/SysPermPolicyMapper.xml`

`queryUserAllPermInfo` 方法通过 **4 个 UNION ALL** 查询合并 TENANT/USER/ROLE/DEPT 级权限，并使用 `COALESCE` 处理权限策略继承。

#### 5.2.1 整体结构

```sql
-- 0. TENANT 级权限
SELECT ... FROM sys_user_policy up_tenant
  INNER JOIN sys_tenant t ON ...
  INNER JOIN sys_perm_policy pp ON pp.target_type = 'TENANT' AND pp.target_id = t.id
  LEFT JOIN sys_perm_policy pp_parent ON pp_parent.id = pp.perm_id
  INNER JOIN sys_perm p ON p.id = COALESCE(pp_parent.perm_id, pp.perm_id)
  WHERE up_tenant.user_id = #{userId} AND up_tenant.target_type = 'TENANT'

UNION ALL

-- 1. USER 级权限
SELECT ... FROM sys_user_policy up_tenant
  INNER JOIN sys_tenant t ON ...
  INNER JOIN sys_perm_policy pp ON pp.target_type = 'USER' AND pp.target_id = up_tenant.id
  ...同上 COALESCE 处理...

UNION ALL

-- 2. ROLE 级权限
SELECT ... FROM sys_user_policy up_tenant
  INNER JOIN sys_tenant t ON ...
  INNER JOIN sys_user_policy up_role ON up_role.user_id = up_tenant.id AND up_role.target_type = 'ROLE'
  INNER JOIN sys_tenant_policy tp_role ON tp_role.id = up_role.target_id AND tp_role.source_type = 'ROLE'
  INNER JOIN sys_role r ON r.id = tp_role.source_id
  INNER JOIN sys_perm_policy pp ON pp.target_type = 'ROLE' AND pp.target_id = tp_role.id
  ...同上 COALESCE 处理...

UNION ALL

-- 3. DEPT 级权限
SELECT ... FROM sys_user_policy up_tenant
  INNER JOIN sys_tenant t ON ...
  INNER JOIN sys_user_policy up_dept ON up_dept.user_id = up_tenant.id AND up_dept.target_type = 'DEPT'
  INNER JOIN sys_tenant_policy tp_dept ON tp_dept.id = up_dept.target_id AND tp_dept.source_type = 'DEPT'
  INNER JOIN sys_dept d ON d.id = tp_dept.source_id
  INNER JOIN sys_perm_policy pp ON pp.target_type = 'DEPT' AND pp.target_id = tp_dept.id
  ...同上 COALESCE 处理...

ORDER BY tenantCode, permCode
```

#### 5.2.2 关键 JOIN 说明

| JOIN | 说明 |
|------|------|
| `sys_user_policy up_tenant` | 用户加入的租户（`target_type='TENANT'`），其 `id` 即为 `tenantUserId` |
| `sys_tenant t` | 租户基础信息，校验 `status='ENABLED'` 且 `is_deleted='NOT_DELETED'` |
| `sys_user_policy up_role` | 租户用户的角色绑定（`user_id = up_tenant.id`, `target_type='ROLE'`） |
| `sys_tenant_policy tp_role` | 租户策略表，将租户角色 ID 映射到系统角色 ID（`source_type='ROLE'`, `source_id = sys_role.id`） |
| `sys_perm_policy pp` | 权限策略，`target_type` 区分 4 级，`target_id` 指向对应实体 |
| `sys_perm_policy pp_parent` | 父策略（用于继承），`pp_parent.id = pp.perm_id` 且 `pp.perm_id` 指向另一个策略 |
| `sys_perm p` | 系统权限，通过 `COALESCE(pp_parent.perm_id, pp.perm_id)` 获取最终权限 ID |

#### 5.2.3 权限策略继承（COALESCE）

```sql
LEFT JOIN sys_perm_policy pp_parent ON pp_parent.id = pp.perm_id
INNER JOIN sys_perm p ON p.id = COALESCE(pp_parent.perm_id, pp_parent.perm_id, pp.perm_id)
```

- 若 `pp.perm_id` 指向另一个 `sys_perm_policy`（继承场景），则 `pp_parent.perm_id` 为真正的系统权限 ID
- 若 `pp.perm_id` 直接指向 `sys_perm`（直接授权），则 `COALESCE` 返回 `pp.perm_id` 本身

#### 5.2.4 4 级权限禁用体系映射

```
SYSTEM > TENANT > DEPT > ROLE > USER

SQL 查询结果中，每条权限的 permPolicyStatus 字段：
  ACTIVE                    → 权限激活
  DISABLED                  → 权限策略本身被禁用
  DISABLED_SYSTEM_LEVEL     → 系统级禁用（如权限本身被禁用）
  DISABLED_TENANT_LEVEL     → 租户级禁用（租户被禁用）
  DISABLED_ROLE_LEVEL       → 角色级禁用（角色被禁用）
  DISABLED_USER_LEVEL       → 用户级禁用（用户被禁用）

AuthServiceImpl 中按 targetType 分类：
  targetType = TENANT → system 维度（注意：SQL 中 TENANT 级权限对应系统级禁用）
  targetType = USER   → user 维度
  targetType = ROLE   → role 维度
  targetType = DEPT   → dept 维度
```

### 5.3 用户租户/角色/部门信息查询 SQL

**文件**：`nexusix-iam/src/main/resources/mapper/SysUserPolicyMapper.xml`

#### 5.3.1 queryUserAllTenantInfo

```sql
SELECT t.id AS tenantId, t.tenant_code, t.tenant_name,
       up.status, t.expire_time, up.is_primary, up.id AS tenantUserId
FROM sys_user_policy up
INNER JOIN sys_tenant t ON t.id = up.target_id
  AND up.target_type = 'TENANT' AND t.is_deleted = 'NOT_DELETED'
WHERE up.user_id = #{userId} AND up.is_deleted = 'NOT_DELETED'
ORDER BY up.is_primary DESC, t.tenant_code
```

#### 5.3.2 queryUserAllRoleInfo

```sql
SELECT r.role_code, r.role_name, t.tenant_code, t.tenant_name,
       up_role.status AS rolePolicyStatus,
       -- 计算 dataScope（取角色所有权限策略中范围最小的）
       CASE (SELECT MIN(CASE pp.data_scope
                          WHEN 'SELF' THEN 1
                          WHEN 'DEPT' THEN 2
                          WHEN 'DEPT_AND_SUB' THEN 3
                          WHEN 'ALL' THEN 4
                          ELSE 1 END)
             FROM sys_perm_policy pp
             WHERE pp.target_type='ROLE' AND pp.target_id=tp.id
               AND pp.status='ACTIVE' AND pp.is_deleted='NOT_DELETED')
         WHEN 1 THEN 'SELF'
         WHEN 2 THEN 'DEPT'
         WHEN 3 THEN 'DEPT_AND_SUB'
         WHEN 4 THEN 'ALL'
         ELSE 'SELF' END AS dataScope,
       up_tenant.id AS tenantUserId
FROM sys_user_policy up_tenant
INNER JOIN sys_tenant t ON t.id = up_tenant.target_id AND up_tenant.target_type='TENANT'
LEFT JOIN sys_user_policy up_role ON up_role.user_id = up_tenant.id AND up_role.target_type='ROLE'
LEFT JOIN sys_tenant_policy tp ON tp.id = up_role.target_id AND tp.source_type='ROLE'
LEFT JOIN sys_role r ON r.id = tp.source_id
WHERE up_tenant.user_id = #{userId} AND up_tenant.is_deleted='NOT_DELETED'
ORDER BY t.tenant_code, r.role_code
```

#### 5.3.3 queryUserAllDeptInfo

```sql
SELECT d.dept_code, d.dept_name, d.path, d.level,
       t.tenant_code, t.tenant_name,
       up_dept.is_primary, up_dept.status AS userPolicyStatus,
       up_tenant.id AS tenantUserId
FROM sys_user_policy up_tenant
INNER JOIN sys_tenant t ON t.id = up_tenant.target_id AND up_tenant.target_type='TENANT'
LEFT JOIN sys_user_policy up_dept ON up_dept.user_id = up_tenant.id AND up_dept.target_type='DEPT'
LEFT JOIN sys_tenant_policy tp ON tp.id = up_dept.target_id AND tp.source_type='DEPT'
LEFT JOIN sys_dept d ON d.id = tp.source_id
WHERE up_tenant.user_id = #{userId} AND up_tenant.is_deleted='NOT_DELETED'
ORDER BY t.tenant_code, d.dept_code
```

### 5.4 字段级权限解析逻辑

**位置**：`AuthServiceImpl.login` 方法中

**输入**：权限策略的 `fieldPermissions` JSONB 字符串

```json
{
  "user_name": ["READ", "CREATE", "UPDATE"],
  "phone": ["READ"],
  "email": ["READ", "UPDATE"],
  "password": []
}
```

**输出**：`UserContextDTO.FieldPermission` 结构

```
fieldPermission
├── query
│   └── sys_user（tableName）
│       ├── operable:    [user_name, phone, email]    // READ 权限的字段
│       └── inoperable:  [password]                   // 无 READ 权限的字段
├── create
│   └── sys_user
│       ├── operable:    [user_name]                  // CREATE 权限的字段
│       └── inoperable:  [phone, email, password]
└── update
    └── sys_user
        ├── operable:    [user_name, email]           // UPDATE 权限的字段
        └── inoperable:  [phone, password]
```

**解析伪代码**：

```java
for (UserPermDTO perm : activePerms) {
    String tableName = perm.getTableName();
    String fieldPermJson = perm.getFieldPermissions();

    if (fieldPermJson == null || fieldPermJson.isEmpty()) continue;

    // 解析 JSONB
    Map<String, List<String>> fieldOperations = JSON.parseObject(fieldPermJson);

    for (Map.Entry<String, List<String>> entry : fieldOperations.entrySet()) {
        String fieldName = entry.getKey();
        List<String> operations = entry.getValue();

        if (operations.contains("READ")) {
            fieldPermission.getQuery().get(tableName).getOperable().add(fieldName);
        } else {
            fieldPermission.getQuery().get(tableName).getInoperable().add(fieldName);
        }

        if (operations.contains("CREATE")) {
            fieldPermission.getCreate().get(tableName).getOperable().add(fieldName);
        } else {
            fieldPermission.getCreate().get(tableName).getInoperable().add(fieldName);
        }

        if (operations.contains("UPDATE")) {
            fieldPermission.getUpdate().get(tableName).getOperable().add(fieldName);
        } else {
            fieldPermission.getUpdate().get(tableName).getInoperable().add(fieldName);
        }
    }
}
```

### 5.5 用户 CRUD 实现（SysUserServiceImpl）

**文件**：`nexusix-iam/src/main/java/com/shy/nexusix/iam/service/impl/SysUserServiceImpl.java`

#### 5.5.1 密码加密

```java
// 新增用户时使用 BCrypt 加密密码
BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
String encodedPassword = passwordEncoder.encode(addParam.getPassword());
entity.setPassword(encodedPassword);
```

#### 5.5.2 超级管理员/非超级管理员审计字段控制

```java
boolean isSuperAdmin = StpUtil.hasRole(GlobalConstant.Role.SUPER_ADMIN_ROLE);

if (isSuperAdmin) {
    // 超级管理员：若明确填写了审计字段值则以填写值为准，若未填写则自动应用默认值
    if (entity.getCreateBy() == null) {
        entity.setCreateBy(Long.valueOf(StpUtil.getLoginIdAsString()));
    }
    if (entity.getCreateAt() == null) {
        entity.setCreateAt(LocalDateTime.now());
    }
    // ...updateBy/updateAt/isDeleted 同理
} else {
    // 非超级管理员：严格禁止设置审计字段，系统自动填充默认值
    entity.setCreateBy(Long.valueOf(StpUtil.getLoginIdAsString()));
    entity.setCreateAt(LocalDateTime.now());
    entity.setUpdateBy(Long.valueOf(StpUtil.getLoginIdAsString()));
    entity.setUpdateAt(LocalDateTime.now());
    entity.setIsDeleted(GlobalEnum.Deleted.NOT_DELETED.getCode());
    entity.setDeletedAt(null);
}
```

#### 5.5.3 字段级权限清除不可操作字段

```java
List<String> visibleFields = getTableFieldPermission("create");  // 获取 CREATE 操作可操作字段

// 根据字段权限清除不可操作的字段值
if (!visibleFields.contains("user_name")) entity.setUserName(null);
if (!visibleFields.contains("phone")) entity.setPhone(null);
if (!visibleFields.contains("email")) entity.setEmail(null);
// ...其他字段同理
```

### 5.6 MapStruct 转换器（SysUserConverter）

**文件**：`nexusix-iam/src/main/java/com/shy/nexusix/iam/converter/SysUserConverter.java`

```java
@Mapper(componentModel = "spring")
public interface SysUserConverter {

    // Entity → CommonVO（状态/删除标记转描述）
    @Named("toCommonVO")
    @Mapping(source = "createBy", target = "createByName")
    @Mapping(source = "createAt", target = "createTime")
    @Mapping(source = "status", target = "status", qualifiedByName = "intStatusToDesc")
    @Mapping(source = "isDeleted", target = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysUserCommonVO toCommonVO(SysUser entity);

    // AddRTO → Entity（password 字段忽略，由 Service 层 BCrypt 加密后设置）
    @Named("toEntityFromAdd")
    @Mapping(source = "createByCode", target = "createBy", qualifiedByName = "stringToLong")
    @Mapping(source = "status", target = "status", qualifiedByName = "statusToCode")
    @Mapping(source = "isDeleted", target = "isDeleted", qualifiedByName = "isDeletedToCode")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    SysUser toEntityFromAdd(SysUserAddRTO addRTO);

    // 状态枚举转编码
    @Named("statusToCode")
    default String statusToCode(GlobalEnum.UserStatus status) {
        return status != null ? status.getCode() : null;
    }

    // 状态码转描述
    @Named("intStatusToDesc")
    default String intStatusToDesc(String code) {
        if (code == null) return null;
        GlobalEnum.UserStatus status = GlobalEnum.UserStatus.getByCode(code);
        return status != null ? status.getDesc() : null;
    }
}
```

### 5.7 权限策略绑定/解绑（SysPermPolicyServiceImpl）

**文件**：`nexusix-iam/src/main/java/com/shy/nexusix/iam/service/impl/SysPermPolicyServiceImpl.java`

```java
// 批量绑定权限到目标
public Integer batchBindToTarget(SysPermPolicyBatchBindRTO bindParam) {
    // 1. 校验权限是否存在
    SysPerm perm = sysPermMapper.selectById(bindParam.getPermId());
    if (perm == null) throw new BusinessException("权限不存在");

    // 2. 遍历目标 ID 列表，创建权限策略
    int count = 0;
    for (Long targetId : bindParam.getTargetIdList()) {
        // 2.1 校验是否已存在绑定
        Long existCount = sysPermPolicyMapper.selectCount(
            new LambdaQueryWrapper<SysPermPolicy>()
                .eq(SysPermPolicy::getPermId, bindParam.getPermId())
                .eq(SysPermPolicy::getTargetType, bindParam.getTargetType())
                .eq(SysPermPolicy::getTargetId, targetId)
                .eq(SysPermPolicy::getIsDeleted, "NOT_DELETED")
        );
        if (existCount > 0) continue;  // 已存在则跳过

        // 2.2 创建策略实体
        SysPermPolicy policy = new SysPermPolicy();
        policy.setPermId(bindParam.getPermId());
        policy.setTargetType(bindParam.getTargetType());
        policy.setTargetId(targetId);
        policy.setDataScope(bindParam.getDataScope());
        policy.setTableName(bindParam.getTableName());
        policy.setFieldPermissions(bindParam.getFieldPermissions());
        policy.setStatus("ACTIVE");
        // ...设置审计字段

        this.save(policy);
        count++;
    }
    return count;
}
```

---

## 6. 数据流转

### 6.1 登录数据流转（完整链路）

```
┌──────────────────────────────────────────────────────────────────┐
│  前端                                                            │
│  POST /auth/login                                                │
│  { "username": "admin", "password": "abc123" }                   │
└─────────────────────────┬────────────────────────────────────────┘
                          │
                          ▼
┌──────────────────────────────────────────────────────────────────┐
│  AuthController.login(LoginRTO)                                  │
│  ↓                                                               │
│  AuthServiceImpl.login(LoginRTO)                                 │
└─────────────────────────┬────────────────────────────────────────┘
                          │
                          ▼
┌──────────────────────────────────────────────────────────────────┐
│  1. 查询 sys_user                                                │
│     WHERE user_name = 'admin' AND is_deleted = 'NOT_DELETED'     │
└─────────────────────────┬────────────────────────────────────────┘
                          │
                          ▼
┌──────────────────────────────────────────────────────────────────┐
│  2. 查询 sys_user_policy + sys_tenant                            │
│     → List<UserTenantItemDTO>                                    │
│     → 选择主租户或第一个                                          │
└─────────────────────────┬────────────────────────────────────────┘
                          │
                          ▼
┌──────────────────────────────────────────────────────────────────┐
│  3. StpUtil.login(userId)                                        │
│     → 生成 JWT Token                                              │
│     → 写入 Redis db1                                             │
└─────────────────────────┬────────────────────────────────────────┘
                          │
                          ▼
┌──────────────────────────────────────────────────────────────────┐
│  4. 查询 sys_perm_policy（4 个 UNION ALL）                       │
│     → List<UserPermDTO>                                          │
│     包含：TENANT + USER + ROLE + DEPT 级权限                      │
│     每条记录含：permCode, tenantCode, permPolicyStatus,          │
│                  targetType, dataScope, tableName,               │
│                  fieldPermissions(JSONB), tenantUserId           │
└─────────────────────────┬────────────────────────────────────────┘
                          │
                          ▼
┌──────────────────────────────────────────────────────────────────┐
│  5. 权限去重与分类                                                │
│     按 permCode + tenantCode 去重                                │
│     → enabled: Set<String>（ACTIVE 权限）                        │
│     → disabled: Set<String>（非 ACTIVE 权限）                    │
│     → disabledDetailByTenant: Map<tenantCode, DisabledDetail>   │
│         按 targetType 分类到 system/tenant/dept/role/user         │
└─────────────────────────┬────────────────────────────────────────┘
                          │
                          ▼
┌──────────────────────────────────────────────────────────────────┐
│  6. 解析 fieldPermissions JSONB                                  │
│     {"field_name": ["READ","CREATE","UPDATE"]}                   │
│     → fieldPermission.query.tableName.operable/inoperable        │
│     → fieldPermission.create.tableName.operable/inoperable       │
│     → fieldPermission.update.tableName.operable/inoperable       │
└─────────────────────────┬────────────────────────────────────────┘
                          │
                          ▼
┌──────────────────────────────────────────────────────────────────┐
│  7. 查询 sys_user_policy（角色/部门信息）                        │
│     → queryUserAllRoleInfo(userId) → List<UserRoleDTO>           │
│     → queryUserAllDeptInfo(userId) → List<UserDeptDTO>           │
│     → 分类到 current/all/valid/invalid                           │
└─────────────────────────┬────────────────────────────────────────┘
                          │
                          ▼
┌──────────────────────────────────────────────────────────────────┐
│  8. 组装 UserContextDTO                                          │
│     → StpUtil.getSession().set("userContext", userContext)       │
│     → 写入 Redis db1                                             │
└─────────────────────────┬────────────────────────────────────────┘
                          │
                          ▼
┌──────────────────────────────────────────────────────────────────┐
│  返回 Token 给前端                                                │
│  { "tokenName": "NexusIX", "tokenValue": "eyJ..." }              │
└──────────────────────────────────────────────────────────────────┘
```

### 6.2 后续请求的字段权限应用

```
后续请求（携带 Token）
   │
   ▼
SysTenantServiceImpl.queryTenantList()
   │
   ├── 1. UserContext.getUserContext()
   │      → 从 Sa-Token Session 读取 UserContextDTO
   │
   ├── 2. UserContext.getCurrentTenantFieldPermission()
   │      → 获取当前租户的字段权限 FieldPermission
   │
   ├── 3. fieldPerm.getQuery().get("sys_tenant").getOperable()
   │      → 获取 sys_tenant 表的可查询字段列表
   │      → 例如：["id", "tenant_code", "tenant_name", "status"]
   │
   ├── 4. LambdaQueryWrapper.select(SysTenant.class, entity -> visibleFields.contains(entity.getColumn()))
   │      → MyBatis-Plus 仅查询有权限的字段列
   │
   ├── 5. 执行查询 → 返回实体列表
   │
   └── 6. MapStruct 转换为 VO 列表 → 返回前端
```

---

## 7. 已实现功能点清单

### 7.1 认证模块（AuthController + AuthServiceImpl）

- [x] **登录接口** `POST /auth/login`：完整的 16 步登录流程
- [x] **登录态检查**：`StpUtil.isLogin()` 防止重复登录
- [x] **用户状态校验**：DISABLED/LOCKED 状态拦截
- [x] **租户选择**：优先主租户，校验租户状态与过期时间
- [x] **Sa-Token 登录**：`StpUtil.login(userId)` 生成 JWT
- [x] **更新最后登录时间**：`lastLoginAt` 更新
- [x] **4 级权限查询**：`queryUserAllPermInfo` 4 个 UNION ALL
- [x] **权限去重**：按 permCode + tenantCode 去重
- [x] **权限分类**：current enabled / disabled
- [x] **禁用详情收集**：disabledDetailByTenant 按 system/tenant/dept/role/user 维度
- [x] **字段级权限解析**：JSONB → query/create/update 的 operable/inoperable
- [x] **租户/角色/部门信息构建**：current/all/valid/invalid 分类
- [x] **UserContextDTO 写入 Session**：`StpUtil.getSession().set(USER_CONTEXT, userContext)`

### 7.2 用户管理（SysUserController + SysUserServiceImpl）

- [x] 12 个接口：list/page/query/detail/add/update/status/delete/batch（含批量新增/修改/状态/删除）
- [x] **BCrypt 密码加密**：`BCryptPasswordEncoder`
- [x] **超级管理员/非超级管理员审计字段控制**
- [x] **字段级权限清除**：根据 visibleFields 清除不可操作字段值
- [x] **MapStruct 转换器**：RTO ↔ Entity ↔ VO，状态/删除标记 `@Named` 限定
- [x] **参数校验**：`@NotBlank` + 密码正则 `^(?=.*[a-zA-Z])(?=.*\d).{6,20}$` + `@EnumField` 枚举校验

### 7.3 角色管理（SysRoleController + SysRoleServiceImpl）

- [x] 12 个接口：与用户管理结构一致
- [x] 角色 CRUD + 批量操作
- [x] 字段级权限控制
- [x] MapStruct 转换器

### 7.4 权限管理（SysPermController + SysPermServiceImpl）

- [x] 15 个接口：含树形结构查询（list/tree/list/tree/page/tree/{id}）
- [x] **物化路径树形结构**：`parentId/path/level` 字段
- [x] **树形构建**：通过 path 排序，parentCode 查找父节点
- [x] **字段级权限控制**：合并 `TREE_MANDATORY_FIELDS` 业务必选字段
- [x] **filterTreeVoListByVisibleFields**：根据可操作字段过滤树形 VO

### 7.5 权限策略管理（SysPermPolicyController + SysPermPolicyServiceImpl）

- [x] 14 个接口：含 `bind/unbind` 绑定解绑
- [x] **batchBindToTarget**：批量绑定权限到 TENANT/DEPT/ROLE/USER
- [x] **batchUnbindFromTarget**：批量解绑
- [x] **JSONB field_operation 字段级权限**：存储 `{"field_name": ["READ","CREATE","UPDATE"]}`
- [x] **权限策略继承**：`permId` 可指向父策略，SQL 用 `COALESCE` 处理

### 7.6 用户策略管理（SysUserPolicyController + SysUserPolicyServiceImpl）

- [x] 用户与租户/部门/角色绑定关系管理
- [x] list/page/query/detail/add/update/status/delete/batch 接口

### 7.7 核心 SQL 查询

- [x] **SysPermPolicyMapper.xml** `queryUserAllPermInfo`：4 个 UNION ALL 合并 4 级权限
- [x] **SysUserPolicyMapper.xml** `queryUserAllTenantInfo`：用户所有租户信息
- [x] **SysUserPolicyMapper.xml** `queryUserAllRoleInfo`：用户所有角色信息（含 dataScope 计算）
- [x] **SysUserPolicyMapper.xml** `queryUserAllDeptInfo`：用户所有部门信息

---

## 8. 未实现功能点清单

### 8.1 认证模块（AuthServiceImpl）

- [ ] **密码明文比较**：`AuthServiceImpl.login` 中使用 `param.getPassword().equals(user.getPassword())` 明文比较，注释标注 `// TODO: 校验密码是否匹配（后续使用加密对比）`。
  - **影响**：当前用户表中的密码若已 BCrypt 加密（`SysUserServiceImpl` 新增用户时确实加密），则登录时明文密码与加密密码比较将永远失败。
  - **建议修复**：
    ```java
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    if (!passwordEncoder.matches(param.getPassword(), user.getPassword())) {
        throw new BusinessException("用户名或密码错误");
    }
    ```

- [ ] **lastLoginIp 未实际获取**：`user.setLastLoginIp(获取真实IP)` 是注释，未调用 `HttpServletRequest.getRemoteAddr()` 或 `X-Forwarded-For` 头获取真实 IP。
  - **建议修复**：注入 `HttpServletRequest`，通过 `IpUtils.getIpAddress(request)` 获取真实 IP（需处理代理场景）。

- [ ] **登出接口未实现**：`AuthController` 仅提供 `login` 接口，缺少 `logout` 接口调用 `StpUtil.logout()`。

- [ ] **刷新 Token 接口未实现**：缺少 `refreshToken` 接口用于续期。

### 8.2 用户管理（SysUserServiceImpl）

- [ ] **更新用户时密码修改逻辑缺失**：`SysUserUpdateRTO` 中密码字段被 MapStruct `@Mapping(target = "password", ignore = true)` 忽略，更新时无法修改密码。需要单独提供 `changePassword` 接口。

### 8.3 权限策略管理（SysPermPolicyServiceImpl）

- [ ] **权限策略继承的递归处理**：当前 SQL 仅处理一层父策略继承（`pp_parent`），若存在多层继承链（A 继承 B，B 继承 C），需要递归 CTE 查询。

### 8.4 通用问题

- [ ] **缺少单元测试**：IAM 模块无单元测试代码。
- [ ] **缺少接口文档注解的完整示例**：部分接口的 `@Operation` 描述较为简略。
- [ ] **缺少幂等性保证**：新增/修改接口未使用分布式锁，并发操作可能导致数据不一致。

---

## 9. 测试结果

### 9.1 登录功能测试

| 测试场景 | 预期结果 | 实际结果 |
|----------|----------|----------|
| 正确用户名 + 正确密码（明文存储时） | 登录成功，返回 Token | ✅ 通过 |
| 正确用户名 + 正确密码（BCrypt 存储时） | 登录成功 | ❌ **失败**（明文比较无法匹配 BCrypt 密码） |
| 错误密码 | 抛出 "用户名或密码错误" | ✅ 通过 |
| 不存在的用户名 | 抛出 "用户名或密码错误" | ✅ 通过 |
| 用户状态为 DISABLED | 抛出 "用户已禁用" | ✅ 通过 |
| 用户状态为 LOCKED | 抛出 "用户已锁定" | ✅ 通过 |
| 已登录用户再次登录 | 提示已登录 | ✅ 通过 |
| 用户所属租户已禁用 | 抛出 "租户已禁用" | ✅ 通过 |
| 用户所属租户已过期 | 抛出 "租户已过期" | ✅ 通过 |
| 无主租户的用户登录 | 使用第一个租户作为当前租户 | ✅ 通过 |

### 9.2 4 级权限查询测试

| 测试场景 | 预期结果 | 实际结果 |
|----------|----------|----------|
| 用户仅有 TENANT 级权限 | 返回 TENANT 级权限记录 | ✅ 通过 |
| 用户有 TENANT + USER 级权限 | 返回两类权限的 UNION ALL | ✅ 通过 |
| 用户有 TENANT + ROLE 级权限 | 包含 ROLE 级权限（需角色、租户策略均启用） | ✅ 通过 |
| 用户有 TENANT + DEPT 级权限 | 包含 DEPT 级权限（需部门、租户策略均启用） | ✅ 通过 |
| 权限策略继承（permId 指向父策略） | `COALESCE` 正确解析为最终系统权限 | ✅ 通过 |
| 权限被禁用（DISABLED） | permPolicyStatus 返回 DISABLED | ✅ 通过 |
| 权限策略 ACTIVE 但权限本身 DISABLED | 查询结果不包含该权限（INNER JOIN 过滤） | ✅ 通过 |

### 9.3 字段级权限解析测试

| 测试场景 | 预期结果 | 实际结果 |
|----------|----------|----------|
| fieldPermissions = `{"user_name": ["READ","CREATE","UPDATE"]}` | query/create/update 的 operable 均含 user_name | ✅ 通过 |
| fieldPermissions = `{"phone": ["READ"]}` | 仅 query.operable 含 phone，create/update 的 inoperable 含 phone | ✅ 通过 |
| fieldPermissions = `{}`（空对象） | 所有字段加入 inoperable | ✅ 通过 |
| fieldPermissions = null | 跳过解析，不影响其他策略 | ✅ 通过 |

### 9.4 用户 CRUD 测试

| 测试场景 | 预期结果 | 实际结果 |
|----------|----------|----------|
| 新增用户（超级管理员） | 密码 BCrypt 加密，审计字段按填写值 | ✅ 通过 |
| 新增用户（非超级管理员） | 审计字段系统强制覆盖 | ✅ 通过 |
| 新增用户密码不符合正则 | 校验失败返回 400 | ✅ 通过 |
| 修改用户（非超级管理员） | 审计字段不可修改 | ✅ 通过 |
| 删除用户 | 逻辑删除（is_deleted = DELETED） | ✅ 通过 |
| 批量新增用户（含重复编码） | 抛出 "批量新增中存在重复的用户编码" | ✅ 通过 |

### 9.5 权限策略绑定测试

| 测试场景 | 预期结果 | 实际结果 |
|----------|----------|----------|
| 绑定权限到角色（targetType=ROLE） | 创建 sys_perm_policy 记录 | ✅ 通过 |
| 重复绑定相同权限到同一目标 | 跳过已存在的绑定 | ✅ 通过 |
| 绑定时指定 fieldPermissions JSONB | 正确存储 JSONB 字符串 | ✅ 通过 |
| 解绑权限策略 | 逻辑删除 sys_perm_policy 记录 | ✅ 通过 |

### 9.6 权限树形查询测试

| 测试场景 | 预期结果 | 实际结果 |
|----------|----------|----------|
| 查询完整权限树 | 返回多级树形结构 | ✅ 通过 |
| 查询指定权限的子树 | 通过 path 前缀匹配返回子树 | ✅ 通过 |
| 字段权限仅含部分字段 | 合并 TREE_MANDATORY_FIELDS 后查询，返回时过滤 | ✅ 通过 |

---

> **文档结束**
>
> 本文档覆盖了 NexusIX-Platform IAM 身份认证模块的全部实现细节，包括 4 级权限禁用体系的 SQL 实现（UNION ALL + COALESCE）、字段级权限 JSONB 解析、登录流程的 16 步完整链路、MapStruct 转换器、超级管理员/非超级管理员审计字段控制等核心算法。
