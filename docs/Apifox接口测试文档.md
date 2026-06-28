# NexusIX-Platform 接口测试文档

> 基础地址：`http://localhost:8081/NexusIxService`
> Token请求头：`NexusIX: {token值}`
> 所有响应格式：`{ "code": 200, "msg": "success", "data": ... }`

---

## 0. 前置：登录获取Token

### 0.1 POST /auth/login

**请求体：**
```json
{
  "username": "test_all",
  "password": "$2a$10$dummyhash"
}
```

**验证点：**
- 返回200，data中包含token
- 将返回的token值填入后续所有请求的 `NexusIX` 请求头

---

## 1. Auth模块（1个接口）

### 1.1 POST /auth/login

同上方0.1，已在前面描述。

---

## 2. Tenant模块 — SysTenantController（20个接口）

### 2.1 GET /tenant/list

**请求头：** `NexusIX: {token}`

**验证点：** 返回所有租户列表，应包含13条记录，其中TENANT_A(7001)、TENANT_B(7002)、TENANT_C(7003)

---

### 2.2 GET /tenant/page

**请求头：** `NexusIX: {token}`

**Query参数：**
| 参数 | 值 |
|------|----|
| pageNum | 1 |
| pageSize | 5 |

**验证点：** 返回分页数据，total≈13，当前页5条

---

### 2.3 GET /tenant/tree/list

**请求头：** `NexusIX: {token}`

**验证点：** 返回租户树形结构，顶层节点含TENANT_A等

---

### 2.4 GET /tenant/tree/page

**请求头：** `NexusIX: {token}`

**Query参数：**
| 参数 | 值 |
|------|----|
| pageNum | 1 |
| pageSize | 10 |

**验证点：** 返回树形分页数据

---

### 2.5 GET /tenant/tree/{id}

**请求头：** `NexusIX: {token}`

**路径参数：** `id` = `7001`

**验证点：** 返回租户A(id=7001)的树形结构及其子节点

---

### 2.6 POST /tenant/query

**请求头：** `NexusIX: {token}`

**请求体：**
```json
{
  "tenantCode": "TENANT_A",
  "pageNum": 1,
  "pageSize": 10
}
```

**验证点：** 精确查询TENANT_A，返回1条记录，tenantName="租户A"

---

### 2.7 GET /tenant/detail/{tenantCode}

**请求头：** `NexusIX: {token}`

**路径参数：** `tenantCode` = `TENANT_A`

**验证点：** 返回租户A详情，id=7001，tenantType=ENTERPRISE

---

### 2.8 POST /tenant/add

**请求头：** `NexusIX: {token}`

**请求体：**
```json
{
  "tenantCode": "TENANT_TEST",
  "tenantName": "测试租户",
  "tenantType": "TRIAL",
  "contactName": "张三",
  "contactPhone": "13800138000",
  "status": "ENABLED",
  "isDeleted": "NOT_DELETED"
}
```

**验证点：** 返回成功，数据库新增一条租户

---

### 2.9 PUT /tenant/update

**请求头：** `NexusIX: {token}`

**请求体：**
```json
{
  "tenantCode": "TENANT_TEST",
  "tenantName": "测试租户-已修改",
  "tenantType": "TRIAL",
  "contactName": "张三",
  "contactPhone": "13800138001",
  "status": "ENABLED",
  "isDeleted": "NOT_DELETED"
}
```

**验证点：** 返回成功，tenantName已变为"测试租户-已修改"

---

### 2.10 PUT /tenant/status

**请求头：** `NexusIX: {token}`

**Query参数：**
| 参数 | 值 |
|------|----|
| id | 7001 |
| status | DISABLED |

**验证点：** 返回成功。**注意：这会停用TENANT_A，测试完建议改回ENABLED**

---

### 2.11 DELETE /tenant/delete

**请求头：** `NexusIX: {token}`

**Query参数：**
| 参数 | 值 |
|------|----|
| id | 上一步新增的TENANT_TEST的id |

**验证点：** 返回成功，逻辑删除该租户（is_deleted=DELETED）

---

### 2.12 POST /tenant/batch

**请求头：** `NexusIX: {token}`

**请求体：**
```json
[
  {
    "tenantCode": "TENANT_BATCH_1",
    "tenantName": "批量租户1",
    "tenantType": "TRIAL",
    "status": "ENABLED",
    "isDeleted": "NOT_DELETED"
  },
  {
    "tenantCode": "TENANT_BATCH_2",
    "tenantName": "批量租户2",
    "tenantType": "TRIAL",
    "status": "ENABLED",
    "isDeleted": "NOT_DELETED"
  }
]
```

**验证点：** 返回成功，批量创建2个租户

---

### 2.13 PUT /tenant/batch

**请求头：** `NexusIX: {token}`

**请求体：**
```json
[
  {
    "tenantCode": "TENANT_BATCH_1",
    "tenantName": "批量租户1-修改",
    "tenantType": "TRIAL",
    "status": "ENABLED",
    "isDeleted": "NOT_DELETED"
  }
]
```

**验证点：** 返回成功，批量修改租户名称

---

### 2.14 PUT /tenant/status/batch

**请求头：** `NexusIX: {token}`

**Query参数：**
| 参数 | 值 |
|------|----|
| status | ENABLED |

**请求体：**
```json
["TENANT_BATCH_1", "TENANT_BATCH_2"]
```

**验证点：** 返回成功，批量修改状态

---

### 2.15 DELETE /tenant/batch

**请求头：** `NexusIX: {token}`

**请求体：**
```json
["TENANT_BATCH_1", "TENANT_BATCH_2"]
```

**验证点：** 返回成功，批量逻辑删除

---

### 2.16 POST /tenant/assign/sub

**请求头：** `NexusIX: {token}`

**请求体：**
```json
{
  "parentCode": "TENANT_A",
  "subCode": ["TENANT_B"]
}
```

**验证点：** 返回成功，TENANT_B成为TENANT_A的子租户

---

### 2.17 PUT /tenant/assign/parent

**请求头：** `NexusIX: {token}`

**请求体：**
```json
{
  "parentCode": "TENANT_A",
  "subCode": ["TENANT_C"]
}
```

**验证点：** 返回成功，TENANT_C的父租户改为TENANT_A

---

### 2.18 POST /tenant/register

**请求头：** 无需Token（或视实现而定）

**请求体：**
```json
{
  "tenantName": "新注册租户",
  "tenantType": "TRIAL",
  "contactName": "李四",
  "contactPhone": "13900139000"
}
```

**验证点：** 返回成功，创建一个待审核(PENDING)状态的租户

---

### 2.19 PUT /tenant/review

**请求头：** `NexusIX: {token}`

**请求体：**
```json
{
  "tenantId": "上一步注册的租户id",
  "approved": true,
  "reviewRemark": "审核通过"
}
```

**验证点：** 返回成功，租户状态变为ENABLED

---

### 2.20 POST /tenant/switch

**请求头：** `NexusIX: {token}`

**请求体：**
```json
{
  "targetTenantCode": "TENANT_B"
}
```

**验证点：** 返回成功，当前租户从TENANT_A切换到TENANT_B

---

## 3. Tenant模块 — SysTenantPolicyController（16个接口）

### 3.1 GET /tenant-policy/list

**请求头：** `NexusIX: {token}`

**验证点：** 返回所有租户策略列表，约49条

---

### 3.2 GET /tenant-policy/page

**请求头：** `NexusIX: {token}`

**Query参数：**
| 参数 | 值 |
|------|----|
| pageNum | 1 |
| pageSize | 10 |

**验证点：** 返回分页数据

---

### 3.3 POST /tenant-policy/query

**请求头：** `NexusIX: {token}`

**请求体：**
```json
{
  "sourceType": "DEPT",
  "tenantId": 7001,
  "pageNum": 1,
  "pageSize": 10
}
```

**验证点：** 查询租户A下的部门策略，应返回id=75001/75002/75007等记录

---

### 3.4 GET /tenant-policy/detail/{policyCode}

**请求头：** `NexusIX: {token}`

**路径参数：** `policyCode` = `DEPT_RD_7001`（需确认数据库中实际的策略编码）

**验证点：** 返回对应的策略详情

---

### 3.5 POST /tenant-policy/add

**请求头：** `NexusIX: {token}`

**请求体：**
```json
{
  "policyCode": "TP_TEST_ADD",
  "policyName": "测试新增策略",
  "sourceType": "DEPT",
  "sourceId": 75001,
  "tenantId": 7001,
  "status": "ENABLED",
  "isDeleted": "NOT_DELETED"
}
```

**验证点：** 返回成功

---

### 3.6 PUT /tenant-policy/update

**请求头：** `NexusIX: {token}`

**请求体：**
```json
{
  "policyCode": "TP_TEST_ADD",
  "policyName": "测试新增策略-修改",
  "status": "ENABLED",
  "isDeleted": "NOT_DELETED"
}
```

**验证点：** 返回成功，policyName已修改

---

### 3.7 PUT /tenant-policy/status

**请求头：** `NexusIX: {token}`

**Query参数：**
| 参数 | 值 |
|------|----|
| policyCode | TP_TEST_ADD |
| status | DISABLED |

**验证点：** 返回成功

---

### 3.8 DELETE /tenant-policy/delete

**请求头：** `NexusIX: {token}`

**Query参数：**
| 参数 | 值 |
|------|----|
| policyCode | TP_TEST_ADD |

**验证点：** 返回成功，逻辑删除

---

### 3.9 POST /tenant-policy/batch

**请求头：** `NexusIX: {token}`

**请求体：**
```json
[
  {
    "policyCode": "TP_BATCH_1",
    "policyName": "批量策略1",
    "sourceType": "DEPT",
    "sourceId": 75001,
    "tenantId": 7001,
    "status": "ENABLED",
    "isDeleted": "NOT_DELETED"
  },
  {
    "policyCode": "TP_BATCH_2",
    "policyName": "批量策略2",
    "sourceType": "ROLE",
    "sourceId": 76001,
    "tenantId": 7001,
    "status": "ENABLED",
    "isDeleted": "NOT_DELETED"
  }
]
```

**验证点：** 返回成功，批量创建2条

---

### 3.10 PUT /tenant-policy/batch

**请求头：** `NexusIX: {token}`

**请求体：**
```json
[
  {
    "policyCode": "TP_BATCH_1",
    "policyName": "批量策略1-修改",
    "status": "ENABLED",
    "isDeleted": "NOT_DELETED"
  }
]
```

**验证点：** 返回成功

---

### 3.11 PUT /tenant-policy/status/batch

**请求头：** `NexusIX: {token}`

**Query参数：**
| 参数 | 值 |
|------|----|
| status | DISABLED |

**请求体：**
```json
["TP_BATCH_1", "TP_BATCH_2"]
```

**验证点：** 返回成功

---

### 3.12 DELETE /tenant-policy/batch

**请求头：** `NexusIX: {token}`

**请求体：**
```json
["TP_BATCH_1", "TP_BATCH_2"]
```

**验证点：** 返回成功

---

### 3.13 POST /tenant-policy/bind

**请求头：** `NexusIX: {token}`

**请求体：**
```json
{
  "sourceType": "DEPT",
  "sourceIdList": [75001, 75002],
  "tenantId": 7001
}
```

**验证点：** 返回成功，批量绑定部门到租户A

---

### 3.14 POST /tenant-policy/unbind

**请求头：** `NexusIX: {token}`

**请求体：**
```json
{
  "policyCodeList": ["上一步绑定生成的策略编码"]
}
```

**验证点：** 返回成功，解绑

---

### 3.15 GET /tenant-policy/tenant/{tenantId}/depts

**请求头：** `NexusIX: {token}`

**路径参数：** `tenantId` = `7001`

**验证点：** 返回租户A下绑定的部门列表，应含研发部(75001)、市场部(75002)、销售部(75007)

---

### 3.16 GET /tenant-policy/tenant/{tenantId}/roles

**请求头：** `NexusIX: {token}`

**路径参数：** `tenantId` = `7001`

**验证点：** 返回租户A下绑定的角色列表，应含管理员(76001)、普通员工(76002)、审计员(76007)

---

## 4. IAM模块 — SysUserController（12个接口）

### 4.1 GET /user/list

**请求头：** `NexusIX: {token}`

**验证点：** 返回所有用户列表，约31条

---

### 4.2 GET /user/page

**请求头：** `NexusIX: {token}`

**Query参数：**
| 参数 | 值 |
|------|----|
| pageNum | 1 |
| pageSize | 10 |

**验证点：** 返回分页数据

---

### 4.3 POST /user/query

**请求头：** `NexusIX: {token}`

**请求体：**
```json
{
  "userCode": "TEST_ALL",
  "pageNum": 1,
  "pageSize": 10
}
```

**验证点：** 精确查询TEST_ALL，返回1条记录

---

### 4.4 GET /user/detail/{userCode}

**请求头：** `NexusIX: {token}`

**路径参数：** `userCode` = `TEST_ALL`

**验证点：** 返回用户详情，id=7301，userName=test_all，status=ENABLED

---

### 4.5 POST /user/add

**请求头：** `NexusIX: {token}`

**请求体：**
```json
{
  "userCode": "USER_TEST_001",
  "userName": "user_test_001",
  "nickName": "测试用户",
  "email": "test001@test.com",
  "phone": "13800000001",
  "password": "123456",
  "status": "ENABLED",
  "isDeleted": "NOT_DELETED"
}
```

**验证点：** 返回成功，密码应被BCrypt加密存储

---

### 4.6 PUT /user/update

**请求头：** `NexusIX: {token}`

**请求体：**
```json
{
  "userCode": "USER_TEST_001",
  "userName": "user_test_001",
  "nickName": "测试用户-修改",
  "email": "test001@test.com",
  "phone": "13800000002",
  "status": "ENABLED",
  "isDeleted": "NOT_DELETED"
}
```

**验证点：** 返回成功，nickName已修改

---

### 4.7 PUT /user/status

**请求头：** `NexusIX: {token}`

**Query参数：**
| 参数 | 值 |
|------|----|
| id | 上一步新增用户的id |
| status | LOCKED |

**验证点：** 返回成功，用户状态变为LOCKED

---

### 4.8 DELETE /user/delete

**请求头：** `NexusIX: {token}`

**Query参数：**
| 参数 | 值 |
|------|----|
| id | 测试用户的id |

**验证点：** 返回成功，逻辑删除

---

### 4.9 POST /user/batch

**请求头：** `NexusIX: {token}`

**请求体：**
```json
[
  {
    "userCode": "USER_BATCH_1",
    "userName": "user_batch_1",
    "nickName": "批量用户1",
    "password": "123456",
    "status": "ENABLED",
    "isDeleted": "NOT_DELETED"
  },
  {
    "userCode": "USER_BATCH_2",
    "userName": "user_batch_2",
    "nickName": "批量用户2",
    "password": "123456",
    "status": "ENABLED",
    "isDeleted": "NOT_DELETED"
  }
]
```

**验证点：** 返回成功，批量创建2个用户

---

### 4.10 PUT /user/batch

**请求头：** `NexusIX: {token}`

**请求体：**
```json
[
  {
    "userCode": "USER_BATCH_1",
    "userName": "user_batch_1",
    "nickName": "批量用户1-修改",
    "status": "ENABLED",
    "isDeleted": "NOT_DELETED"
  }
]
```

**验证点：** 返回成功

---

### 4.11 PUT /user/status/batch

**请求头：** `NexusIX: {token}`

**Query参数：**
| 参数 | 值 |
|------|----|
| status | DISABLED |

**请求体：**
```json
["USER_BATCH_1的id", "USER_BATCH_2的id"]
```

**验证点：** 返回成功

---

### 4.12 DELETE /user/batch

**请求头：** `NexusIX: {token}`

**请求体：**
```json
["USER_BATCH_1的id", "USER_BATCH_2的id"]
```

**验证点：** 返回成功

---

## 5. IAM模块 — SysRoleController（12个接口）

### 5.1 GET /role/list

**请求头：** `NexusIX: {token}`

**验证点：** 返回所有角色列表，约32条

---

### 5.2 GET /role/page

**请求头：** `NexusIX: {token}`

**Query参数：**
| 参数 | 值 |
|------|----|
| pageNum | 1 |
| pageSize | 10 |

**验证点：** 返回分页数据

---

### 5.3 POST /role/query

**请求头：** `NexusIX: {token}`

**请求体：**
```json
{
  "roleCode": "ROLE_ADMIN",
  "pageNum": 1,
  "pageSize": 10
}
```

**验证点：** 查询ROLE_ADMIN，返回对应记录

---

### 5.4 GET /role/detail/{roleCode}

**请求头：** `NexusIX: {token}`

**路径参数：** `roleCode` = `ROLE_ADMIN`

**验证点：** 返回角色详情，roleName="管理员"

---

### 5.5 POST /role/add

**请求头：** `NexusIX: {token}`

**请求体：**
```json
{
  "roleCode": "ROLE_TEST",
  "roleName": "测试角色",
  "roleDesc": "用于接口测试",
  "status": "ENABLED",
  "isDeleted": "NOT_DELETED"
}
```

**验证点：** 返回成功

---

### 5.6 PUT /role/update

**请求头：** `NexusIX: {token}`

**请求体：**
```json
{
  "roleCode": "ROLE_TEST",
  "roleName": "测试角色-修改",
  "roleDesc": "用于接口测试-修改",
  "status": "ENABLED",
  "isDeleted": "NOT_DELETED"
}
```

**验证点：** 返回成功

---

### 5.7 PUT /role/status

**请求头：** `NexusIX: {token}`

**Query参数：**
| 参数 | 值 |
|------|----|
| id | ROLE_TEST的id |
| status | DISABLED |

**验证点：** 返回成功

---

### 5.8 DELETE /role/delete

**请求头：** `NexusIX: {token}`

**Query参数：**
| 参数 | 值 |
|------|----|
| id | ROLE_TEST的id |

**验证点：** 返回成功

---

### 5.9 POST /role/batch

**请求头：** `NexusIX: {token}`

**请求体：**
```json
[
  {
    "roleCode": "ROLE_BATCH_1",
    "roleName": "批量角色1",
    "status": "ENABLED",
    "isDeleted": "NOT_DELETED"
  },
  {
    "roleCode": "ROLE_BATCH_2",
    "roleName": "批量角色2",
    "status": "ENABLED",
    "isDeleted": "NOT_DELETED"
  }
]
```

**验证点：** 返回成功

---

### 5.10 PUT /role/batch

**请求头：** `NexusIX: {token}`

**请求体：**
```json
[
  {
    "roleCode": "ROLE_BATCH_1",
    "roleName": "批量角色1-修改",
    "status": "ENABLED",
    "isDeleted": "NOT_DELETED"
  }
]
```

**验证点：** 返回成功

---

### 5.11 PUT /role/status/batch

**请求头：** `NexusIX: {token}`

**Query参数：**
| 参数 | 值 |
|------|----|
| status | DISABLED |

**请求体：**
```json
["ROLE_BATCH_1的id", "ROLE_BATCH_2的id"]
```

**验证点：** 返回成功

---

### 5.12 DELETE /role/batch

**请求头：** `NexusIX: {token}`

**请求体：**
```json
["ROLE_BATCH_1的id", "ROLE_BATCH_2的id"]
```

**验证点：** 返回成功

---

## 6. IAM模块 — SysPermController（15个接口）

### 6.1 GET /perm/list

**请求头：** `NexusIX: {token}`

**验证点：** 返回所有权限列表，约67条

---

### 6.2 GET /perm/page

**请求头：** `NexusIX: {token}`

**Query参数：**
| 参数 | 值 |
|------|----|
| pageNum | 1 |
| pageSize | 10 |

**验证点：** 返回分页数据

---

### 6.3 GET /perm/tree/list

**请求头：** `NexusIX: {token}`

**验证点：** 返回权限树形结构，顶层节点含MENU_DASHBOARD、MENU_SYSTEM、MENU_BUSINESS等

---

### 6.4 GET /perm/tree/page

**请求头：** `NexusIX: {token}`

**Query参数：**
| 参数 | 值 |
|------|----|
| pageNum | 1 |
| pageSize | 5 |

**验证点：** 返回树形分页数据

---

### 6.5 GET /perm/tree/{id}

**请求头：** `NexusIX: {token}`

**路径参数：** `id` = `7401`

**验证点：** 返回MENU_DASHBOARD的树形结构及其子节点

---

### 6.6 POST /perm/query

**请求头：** `NexusIX: {token}`

**请求体：**
```json
{
  "permCode": "MENU_DASHBOARD",
  "pageNum": 1,
  "pageSize": 10
}
```

**验证点：** 精确查询，返回1条

---

### 6.7 GET /perm/detail/{permCode}

**请求头：** `NexusIX: {token}`

**路径参数：** `permCode` = `MENU_DASHBOARD`

**验证点：** 返回权限详情，id=7401，permName="仪表盘"，permType="MENU"

---

### 6.8 POST /perm/add

**请求头：** `NexusIX: {token}`

**请求体：**
```json
{
  "permCode": "PERM_TEST",
  "permName": "测试权限",
  "permType": "BUTTON",
  "parentCode": "MENU_DASHBOARD",
  "resourceType": "API",
  "resourcePath": "/api/test",
  "resourceMethod": "GET",
  "sortOrder": 99,
  "isVisible": true,
  "status": "ENABLED",
  "isDeleted": "NOT_DELETED"
}
```

**验证点：** 返回成功，path应根据parentCode自动生成物化路径

---

### 6.9 PUT /perm/update

**请求头：** `NexusIX: {token}`

**请求体：**
```json
{
  "permCode": "PERM_TEST",
  "permName": "测试权限-修改",
  "permType": "BUTTON",
  "parentCode": "MENU_DASHBOARD",
  "sortOrder": 100,
  "isVisible": true,
  "status": "ENABLED",
  "isDeleted": "NOT_DELETED"
}
```

**验证点：** 返回成功

---

### 6.10 PUT /perm/status

**请求头：** `NexusIX: {token}`

**Query参数：**
| 参数 | 值 |
|------|----|
| id | PERM_TEST的id |
| status | DISABLED |

**验证点：** 返回成功。**注意：停用父权限可能级联停用子权限**

---

### 6.11 DELETE /perm/delete

**请求头：** `NexusIX: {token}`

**Query参数：**
| 参数 | 值 |
|------|----|
| id | PERM_TEST的id |

**验证点：** 返回成功。**注意：删除父权限可能级联删除子权限**

---

### 6.12 POST /perm/batch

**请求头：** `NexusIX: {token}`

**请求体：**
```json
[
  {
    "permCode": "PERM_BATCH_1",
    "permName": "批量权限1",
    "permType": "BUTTON",
    "parentCode": "MENU_DASHBOARD",
    "status": "ENABLED",
    "isDeleted": "NOT_DELETED"
  },
  {
    "permCode": "PERM_BATCH_2",
    "permName": "批量权限2",
    "permType": "DATA",
    "parentCode": "MENU_DASHBOARD",
    "status": "ENABLED",
    "isDeleted": "NOT_DELETED"
  }
]
```

**验证点：** 返回成功

---

### 6.13 PUT /perm/batch

**请求头：** `NexusIX: {token}`

**请求体：**
```json
[
  {
    "permCode": "PERM_BATCH_1",
    "permName": "批量权限1-修改",
    "permType": "BUTTON",
    "status": "ENABLED",
    "isDeleted": "NOT_DELETED"
  }
]
```

**验证点：** 返回成功

---

### 6.14 PUT /perm/status/batch

**请求头：** `NexusIX: {token}`

**Query参数：**
| 参数 | 值 |
|------|----|
| status | DISABLED |

**请求体：**
```json
["PERM_BATCH_1的id", "PERM_BATCH_2的id"]
```

**验证点：** 返回成功

---

### 6.15 DELETE /perm/batch

**请求头：** `NexusIX: {token}`

**请求体：**
```json
["PERM_BATCH_1的id", "PERM_BATCH_2的id"]
```

**验证点：** 返回成功

---

## 7. IAM模块 — SysPermPolicyController（14个接口）

### 7.1 GET /perm-policy/list

**请求头：** `NexusIX: {token}`

**验证点：** 返回所有权限策略列表，约66条

---

### 7.2 GET /perm-policy/page

**请求头：** `NexusIX: {token}`

**Query参数：**
| 参数 | 值 |
|------|----|
| pageNum | 1 |
| pageSize | 10 |

**验证点：** 返回分页数据

---

### 7.3 POST /perm-policy/query

**请求头：** `NexusIX: {token}`

**请求体：**
```json
{
  "targetType": "TENANT",
  "targetId": 7001,
  "pageNum": 1,
  "pageSize": 10
}
```

**验证点：** 查询租户A的权限策略，应返回id=80001~80004共4条

---

### 7.4 GET /perm-policy/detail/{policyCode}

**请求头：** `NexusIX: {token}`

**路径参数：** `policyCode` = `PP_TENANT_A_DASH`

**验证点：** 返回策略详情，permId=7401(MENU_DASHBOARD)，targetType=TENANT，targetId=7001

---

### 7.5 POST /perm-policy/add

**请求头：** `NexusIX: {token}`

**请求体：**
```json
{
  "policyCode": "PP_TEST_ADD",
  "policyName": "测试策略",
  "permId": 7401,
  "targetType": "TENANT",
  "targetId": 7001,
  "dataScope": "ALL",
  "status": "ENABLED",
  "isDeleted": "NOT_DELETED"
}
```

**验证点：** 返回成功

---

### 7.6 PUT /perm-policy/update

**请求头：** `NexusIX: {token}`

**请求体：**
```json
{
  "policyCode": "PP_TEST_ADD",
  "policyName": "测试策略-修改",
  "dataScope": "PARTIAL",
  "status": "ENABLED",
  "isDeleted": "NOT_DELETED"
}
```

**验证点：** 返回成功

---

### 7.7 PUT /perm-policy/status

**请求头：** `NexusIX: {token}`

**Query参数：**
| 参数 | 值 |
|------|----|
| policyCode | PP_TEST_ADD |
| status | DISABLED |

**验证点：** 返回成功

---

### 7.8 DELETE /perm-policy/delete

**请求头：** `NexusIX: {token}`

**Query参数：**
| 参数 | 值 |
|------|----|
| policyCode | PP_TEST_ADD |

**验证点：** 返回成功

---

### 7.9 POST /perm-policy/batch

**请求头：** `NexusIX: {token}`

**请求体：**
```json
[
  {
    "policyCode": "PP_BATCH_1",
    "policyName": "批量策略1",
    "permId": 7401,
    "targetType": "TENANT",
    "targetId": 7001,
    "status": "ENABLED",
    "isDeleted": "NOT_DELETED"
  },
  {
    "policyCode": "PP_BATCH_2",
    "policyName": "批量策略2",
    "permId": 7402,
    "targetType": "TENANT",
    "targetId": 7001,
    "status": "ENABLED",
    "isDeleted": "NOT_DELETED"
  }
]
```

**验证点：** 返回成功

---

### 7.10 PUT /perm-policy/batch

**请求头：** `NexusIX: {token}`

**请求体：**
```json
[
  {
    "policyCode": "PP_BATCH_1",
    "policyName": "批量策略1-修改",
    "status": "ENABLED",
    "isDeleted": "NOT_DELETED"
  }
]
```

**验证点：** 返回成功

---

### 7.11 PUT /perm-policy/status/batch

**请求头：** `NexusIX: {token}`

**Query参数：**
| 参数 | 值 |
|------|----|
| status | DISABLED |

**请求体：**
```json
["PP_BATCH_1", "PP_BATCH_2"]
```

**验证点：** 返回成功

---

### 7.12 DELETE /perm-policy/batch

**请求头：** `NexusIX: {token}`

**请求体：**
```json
["PP_BATCH_1", "PP_BATCH_2"]
```

**验证点：** 返回成功

---

### 7.13 POST /perm-policy/bind

**请求头：** `NexusIX: {token}`

**请求体：**
```json
{
  "permId": 7401,
  "targetType": "DEPT",
  "targetIdList": [75001, 75002]
}
```

**验证点：** 返回成功，将MENU_DASHBOARD绑定到研发部(75001)和市场部(75002)

---

### 7.14 POST /perm-policy/unbind

**请求头：** `NexusIX: {token}`

**请求体：**
```json
{
  "policyCodeList": ["上一步绑定生成的策略编码"]
}
```

**验证点：** 返回成功

---

## 8. IAM模块 — SysUserPolicyController（14个接口）

### 8.1 GET /user-policy/list

**请求头：** `NexusIX: {token}`

**验证点：** 返回所有用户策略列表，约51条

---

### 8.2 GET /user-policy/page

**请求头：** `NexusIX: {token}`

**Query参数：**
| 参数 | 值 |
|------|----|
| pageNum | 1 |
| pageSize | 10 |

**验证点：** 返回分页数据

---

### 8.3 POST /user-policy/query

**请求头：** `NexusIX: {token}`

**请求体：**
```json
{
  "userId": 7301,
  "targetType": "TENANT",
  "pageNum": 1,
  "pageSize": 10
}
```

**验证点：** 查询TEST_ALL的租户绑定，返回id=77001/77002/77003共3条

---

### 8.4 GET /user-policy/detail/{policyCode}

**请求头：** `NexusIX: {token}`

**路径参数：** `policyCode` = `UP_TENANT_A`

**验证点：** 返回策略详情，userId=7301，targetType=TENANT，targetId=7001，isPrimary=true

---

### 8.5 POST /user-policy/add

**请求头：** `NexusIX: {token}`

**请求体：**
```json
{
  "policyCode": "UP_TEST_ADD",
  "policyName": "测试策略",
  "userId": 7301,
  "targetType": "DEPT",
  "targetId": 75001,
  "isPrimary": false,
  "status": "ENABLED",
  "isDeleted": "NOT_DELETED"
}
```

**验证点：** 返回成功

---

### 8.6 PUT /user-policy/update

**请求头：** `NexusIX: {token}`

**请求体：**
```json
{
  "policyCode": "UP_TEST_ADD",
  "policyName": "测试策略-修改",
  "isPrimary": true,
  "status": "ENABLED",
  "isDeleted": "NOT_DELETED"
}
```

**验证点：** 返回成功

---

### 8.7 PUT /user-policy/status

**请求头：** `NexusIX: {token}`

**Query参数：**
| 参数 | 值 |
|------|----|
| policyCode | UP_TEST_ADD |
| status | DISABLED |

**验证点：** 返回成功

---

### 8.8 DELETE /user-policy/delete

**请求头：** `NexusIX: {token}`

**Query参数：**
| 参数 | 值 |
|------|----|
| policyCode | UP_TEST_ADD |

**验证点：** 返回成功

---

### 8.9 POST /user-policy/batch

**请求头：** `NexusIX: {token}`

**请求体：**
```json
[
  {
    "policyCode": "UP_BATCH_1",
    "policyName": "批量策略1",
    "userId": 7301,
    "targetType": "DEPT",
    "targetId": 75003,
    "isPrimary": false,
    "status": "ENABLED",
    "isDeleted": "NOT_DELETED"
  },
  {
    "policyCode": "UP_BATCH_2",
    "policyName": "批量策略2",
    "userId": 7301,
    "targetType": "ROLE",
    "targetId": 76001,
    "isPrimary": false,
    "status": "ENABLED",
    "isDeleted": "NOT_DELETED"
  }
]
```

**验证点：** 返回成功

---

### 8.10 PUT /user-policy/batch

**请求头：** `NexusIX: {token}`

**请求体：**
```json
[
  {
    "policyCode": "UP_BATCH_1",
    "policyName": "批量策略1-修改",
    "isPrimary": false,
    "status": "ENABLED",
    "isDeleted": "NOT_DELETED"
  }
]
```

**验证点：** 返回成功

---

### 8.11 PUT /user-policy/status/batch

**请求头：** `NexusIX: {token}`

**Query参数：**
| 参数 | 值 |
|------|----|
| status | DISABLED |

**请求体：**
```json
["UP_BATCH_1", "UP_BATCH_2"]
```

**验证点：** 返回成功

---

### 8.12 DELETE /user-policy/batch

**请求头：** `NexusIX: {token}`

**请求体：**
```json
["UP_BATCH_1", "UP_BATCH_2"]
```

**验证点：** 返回成功

---

### 8.13 POST /user-policy/bind

**请求头：** `NexusIX: {token}`

**请求体：**
```json
{
  "userId": 7301,
  "targetType": "DEPT",
  "targetIdList": [75004, 75005]
}
```

**验证点：** 返回成功，批量绑定部门到TEST_ALL

---

### 8.14 POST /user-policy/unbind

**请求头：** `NexusIX: {token}`

**请求体：**
```json
{
  "policyCodeList": ["上一步绑定生成的策略编码"]
}
```

**验证点：** 返回成功

---

## 附录A：数据库关键ID速查

| 表 | ID | 编码/名称 | 说明 |
|----|-----|----------|------|
| sys_user | 7301 | TEST_ALL / test_all | 测试用户 |
| sys_tenant | 7001 | TENANT_A / 租户A | 主租户 |
| sys_tenant | 7002 | TENANT_B / 租户B | 辅租户 |
| sys_tenant | 7003 | TENANT_C / 租户C | 辅租户 |
| sys_dept | 75001 | 研发部(租户A) | |
| sys_dept | 75002 | 市场部(租户A) | |
| sys_dept | 75003 | 市场部(租户B) | |
| sys_dept | 75004 | 销售部(租户B) | |
| sys_dept | 75005 | 研发部(租户C) | |
| sys_dept | 75006 | 销售部(租户C) | |
| sys_dept | 75007 | 销售部(租户A) | |
| sys_dept | 75008 | 研发部(租户B) | |
| sys_dept | 75010 | 市场部(租户C) | |
| sys_role | 7201 | ROLE_ADMIN / 管理员 | |
| sys_role | 7202 | ROLE_EMPLOYEE / 普通员工 | |
| sys_role | 7203 | ROLE_AUDITOR / 审计员 | |
| sys_role | 76001 | 管理员(租户A) | |
| sys_role | 76002 | 普通员工(租户A) | |
| sys_role | 76003 | 普通员工(租户B) | |
| sys_role | 76004 | 审计员(租户B) | |
| sys_role | 76005 | 管理员(租户C) | |
| sys_role | 76006 | 审计员(租户C) | |
| sys_role | 76007 | 审计员(租户A) | |
| sys_role | 76008 | 管理员(租户B) | |
| sys_role | 76009 | 普通员工(租户C) | |
| sys_perm | 7401 | MENU_DASHBOARD / 仪表盘 | |
| sys_perm | 7402 | MENU_SYSTEM / 系统管理 | |
| sys_perm | 7403 | MENU_BUSINESS / 业务中心 | |
| sys_perm | 7441 | MENU_REPORT / 报表中心 | |
| sys_perm | 7442 | MENU_MONITOR / 系统监控 | |
| sys_perm | 7431 | DATA_USER / 用户数据 | |
| sys_perm | 7432 | DATA_ORDER / 订单数据 | |
| sys_perm | 7451 | DATA_PRODUCT / 产品数据 | |
| sys_perm | 7452 | DATA_LOG / 日志数据 | |
| sys_user_policy | 77001 | UP_TENANT_A | test_all→租户A(主) |
| sys_user_policy | 77002 | UP_TENANT_B | test_all→租户B(辅) |
| sys_user_policy | 77003 | UP_TENANT_C | test_all→租户C(辅) |
| sys_perm_policy | 80001 | PP_TENANT_A_DASH | 仪表盘→租户A |
| sys_perm_policy | 80002 | PP_TENANT_A_DATA | 用户数据→租户A |

---

## 附录B：枚举值速查

| 枚举类 | 枚举值 | 说明 |
|--------|--------|------|
| UserStatus | ENABLED | 启用 |
| UserStatus | DISABLED | 停用 |
| UserStatus | LOCKED | 锁定 |
| RoleStatus | ENABLED | 启用 |
| RoleStatus | DISABLED | 停用 |
| PermStatus | ENABLED | 启用 |
| PermStatus | DISABLED | 停用 |
| TenantStatus | ENABLED | 启用 |
| TenantStatus | DISABLED | 停用 |
| TenantStatus | PENDING | 待审核 |
| Deleted | NOT_DELETED | 未删除 |
| Deleted | DELETED | 已删除 |

---

## 附录C：测试建议顺序

1. **登录** → 获取Token（必须最先）
2. **查询类** → list/page/detail/query（只读，安全）
3. **单条增删改** → add/update/delete/status（测试CRUD）
4. **批量增删改** → batch/batchUpdate/batchStatus/batchDelete
5. **绑定解绑** → bind/unbind（策略绑定）
6. **特殊业务** → register/review/switch/assign（租户业务）
