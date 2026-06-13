# 用户权限详细分析 - 张三（U002）

## 一、用户基础信息

**用户信息：**
- **用户ID（系统级）**: 2
- **用户编码**: U002
- **用户名**: zhangsan
- **真实姓名**: 张三
- **状态**: ENABLED

---

## 二、租户归属关系

### 2.1 张三加入的租户

根据 `sys_user_policy` 表中 `target_type='TENANT'` 的记录：

| 策略ID | 用户ID | 目标租户ID | 租户名称 | 是否主租户 | 租户用户ID | 状态 |
|--------|--------|-----------|---------|-----------|-----------|------|
| 4001 | 2 (系统用户) | 101 | 万象集团 | TRUE | 4001 | ACTIVE |

**结论：**
- 张三只加入了**万象集团（101）**
- 这是他的**主租户**
- 租户用户ID = **4001**（这个ID在后续绑定部门和角色时会用到）

---

## 三、部门归属关系

### 3.1 张三在万象集团的部门

根据 `sys_user_policy` 表中 `user_id=4001`（租户用户ID）且 `target_type='DEPT'` 的记录：

| 策略ID | 租户用户ID | 目标部门ID | 部门类型 | 是否主部门 | 状态 |
|--------|-----------|-----------|---------|-----------|------|
| 4002 | 4001 | 2001 | 租户部门ID | TRUE | ACTIVE |

**解析流程：**
1. `sys_user_policy.target_id = 2001` → 这是**租户部门ID**（在 `sys_tenant_policy` 表中的ID）
2. 查询 `sys_tenant_policy` 表，`id=2001`：
   - `source_type = 'DEPT'`
   - `source_id = 301`（系统部门ID）
   - `tenant_id = 101`（万象集团）
3. 查询 `sys_dept` 表，`id=301`：
   - `dept_code = 'DEPT_FINANCE'`
   - `dept_name = '财务部'`

**结论：**
- 张三在万象集团的**财务部（系统部门ID=301，租户部门ID=2001）**
- 这是他的**主部门**

---

## 四、角色归属关系

### 4.1 张三在万象集团的角色

根据 `sys_user_policy` 表中 `user_id=4001`（租户用户ID）且 `target_type='ROLE'` 的记录：

| 策略ID | 租户用户ID | 目标角色ID | 角色类型 | 状态 |
|--------|-----------|-----------|---------|------|
| 4003 | 4001 | 3003 | 租户角色ID | ACTIVE |

**解析流程：**
1. `sys_user_policy.target_id = 3003` → 这是**租户角色ID**（在 `sys_tenant_policy` 表中的ID）
2. 查询 `sys_tenant_policy` 表，`id=3003`：
   - `source_type = 'ROLE'`
   - `source_id = 421`（系统角色ID）
   - `tenant_id = 101`（万象集团）
3. 查询 `sys_role` 表，`id=421`：
   - `role_code = 'DEPT_MANAGER'`
   - `role_name = '部门经理'`

**结论：**
- 张三在万象集团拥有**部门经理角色（系统角色ID=421，租户角色ID=3003）**

---

## 五、权限授予详细分析

张三的权限来源于**四个层级**的授予：

### 5.1 租户级权限（TENANT → 万象集团）

所有授予万象集团（tenant_id=101）的权限，张三都拥有：

| 权限策略ID | 权限名称 | 权限编码 | 数据范围 | 字段权限 | 授予对象 |
|-----------|---------|---------|---------|---------|---------|
| 5001 | 用户管理权限→万象集团 | PERM_USER_MANAGE (511) | ALL | user_name, email, phone, password, status | TENANT:101 |
| 5002 | 用户查看权限→万象集团 | PERM_USER_VIEW (521) | ALL | user_name, email, phone, nick_name, status | TENANT:101 |
| 5003 | 用户新增权限→万象集团 | PERM_USER_CREATE (522) | ALL | user_name, email, phone, password, nick_name | TENANT:101 |
| 5004 | 用户编辑权限→万象集团 | PERM_USER_UPDATE (523) | ALL | user_name, email, phone, nick_name, status | TENANT:101 |
| 5005 | 用户删除权限→万象集团 | PERM_USER_DELETE (524) | ALL | 无 | TENANT:101 |
| 5006 | 订单管理权限→万象集团 | PERM_ORDER_MANAGE (541) | ALL | order_no, customer_name, amount, status | TENANT:101 |
| 5007 | 销售报表权限→万象集团 | PERM_REPORT_SALES (551) | ALL | 无 | TENANT:101 |

**特点：**
- 数据范围：**ALL**（全租户数据）
- 字段权限：完整的字段级操作权限

---

### 5.2 角色级权限（ROLE → 部门经理）

因为张三拥有**部门经理角色（租户角色ID=3003）**，他继承该角色的权限：

| 权限策略ID | 权限名称 | 基础权限 | 数据范围 | 字段权限 | 授予对象 |
|-----------|---------|---------|---------|---------|---------|
| 5012 | 用户新增→部门经理 | 5003（租户权限ID） | DEPT_AND_SUB | user_name, email, phone, password (CREATE) | ROLE:3003 |
| 5013 | 用户编辑→部门经理 | 5004（租户权限ID） | DEPT_AND_SUB | email, phone, status (UPDATE) | ROLE:3003 |
| 5015 | 销售报表→部门经理 | 5007（租户权限ID） | DEPT_AND_SUB | 无 | ROLE:3003 |

**特点：**
- 数据范围：**DEPT_AND_SUB**（本部门及下级部门数据）
- 这是对租户权限的**二次授予和范围限制**

---

### 5.3 部门级权限（DEPT → 财务部）

因为张三属于**财务部（租户部门ID=2001）**，他继承该部门的权限：

| 权限策略ID | 权限名称 | 基础权限 | 数据范围 | 字段权限 | 授予对象 |
|-----------|---------|---------|---------|---------|---------|
| 5016 | 用户删除→财务部 | 5005（租户权限ID） | DEPT | 无 | DEPT:2001 |

**特点：**
- 数据范围：**DEPT**（仅本部门数据）
- 这是对租户权限的**部门级限制**

---

### 5.4 用户级权限（USER → 张三特殊授权）

直接授予张三个人的特殊权限：

| 权限策略ID | 权限名称 | 基础权限 | 数据范围 | 字段权限 | 授予对象 |
|-----------|---------|---------|---------|---------|---------|
| 5019 | 用户管理→张三(特殊授权) | 5001（租户权限ID） | DEPT | user_name, email, status (READ/UPDATE) | USER:4001 |

**特点：**
- 数据范围：**DEPT**（仅本部门数据）
- 字段权限：**部分字段受限**（相比租户权限）
- 这是针对张三个人的**特殊权限覆盖**

---

## 六、权限合并与优先级规则

### 6.1 权限合并逻辑

张三最终的权限是**四层权限的并集**：

```
最终权限 = 租户权限 ∪ 角色权限 ∪ 部门权限 ∪ 用户权限
```

### 6.2 数据范围优先级（从宽到严）

当同一个权限在多个层级授予时，**取最宽松的数据范围**：

1. **ALL** > DEPT_AND_SUB > DEPT > SELF
2. 例如：
   - 租户级授予 `PERM_USER_CREATE (522)` with `ALL`
   - 角色级授予 `PERM_USER_CREATE (522)` with `DEPT_AND_SUB`
   - **最终生效：ALL**（租户级更宽松）

### 6.3 字段权限合并

字段权限采用**并集合并**：

例如 `PERM_USER_MANAGE (511)`：
- 租户级：`{user_name: [READ, CREATE, UPDATE], email: [READ, CREATE, UPDATE]}`
- 用户级：`{user_name: [READ, UPDATE], email: [READ, UPDATE], status: [READ]}`
- **合并后**：`{user_name: [READ, CREATE, UPDATE], email: [READ, CREATE, UPDATE], status: [READ]}`

### 6.4 禁用状态的影响

如果某层级的策略状态为 `DISABLED`，该层级的权限**不生效**：
- 例如：`5026 (PP_026)` 状态为 `DISABLED`
- 则该条权限授予无效，不参与合并

---

## 七、张三的最终权限清单

### 7.1 权限编码列表（去重后）

```
权限编码清单：
1. PERM_USER_MANAGE (511) - 用户管理权限
2. PERM_USER_VIEW (521) - 用户查看权限
3. PERM_USER_CREATE (522) - 用户新增权限
4. PERM_USER_UPDATE (523) - 用户编辑权限
5. PERM_USER_DELETE (524) - 用户删除权限
6. PERM_ORDER_MANAGE (541) - 订单管理权限
7. PERM_REPORT_SALES (551) - 销售报表权限
```

### 7.2 详细权限矩阵

| 权限编码 | 数据范围 | 表名 | 字段权限 | 来源层级 |
|---------|---------|------|---------|---------|
| 511 | ALL → DEPT | sys_user | user_name[R,C,U], email[R,C,U], password[C,U], status[R,U] | TENANT + USER覆盖 |
| 521 | ALL | sys_user | user_name[R], email[R], phone[R], nick_name[R], status[R] | TENANT |
| 522 | ALL → DEPT_AND_SUB | sys_user | user_name[C], email[C], phone[C], password[C], nick_name[C] | TENANT + ROLE限制 |
| 523 | ALL → DEPT_AND_SUB | sys_user | user_name[U], email[U], phone[U], nick_name[U], status[U] | TENANT + ROLE限制 |
| 524 | ALL → DEPT | - | 无字段限制 | TENANT + DEPT限制 |
| 541 | ALL | biz_order | order_no[R,C,U], customer_name[R], amount[R,C,U], status[R,U] | TENANT |
| 551 | ALL → DEPT_AND_SUB | - | 无字段限制 | TENANT + ROLE限制 |

**说明：**
- **数据范围**：`A → B` 表示租户授予A，但角色/部门/用户限制为B，**最终取并集中最宽松的**
- **字段权限**：`[R,C,U,D]` 分别表示 READ, CREATE, UPDATE, DELETE

---

## 八、权限查询SQL示例

### 8.1 查询张三所有权限的SQL

```sql
-- 1. 用户直接授予的权限（USER级别）
SELECT 
    p.perm_code AS permCode,
    p.perm_name AS permName,
    pp.target_type AS targetType,
    pp.data_scope AS dataScope,
    pp.table_name AS tableName,
    pp.field_operation AS fieldPermissions,
    t.tenant_code AS tenantCode,
    '用户级' AS permissionLevel
FROM sys_user_policy up_tenant
INNER JOIN sys_tenant t ON t.id = up_tenant.target_id
    AND up_tenant.target_type = 'TENANT'
LEFT JOIN sys_perm_policy pp ON pp.target_type = 'USER'
    AND pp.target_id = up_tenant.id
    AND pp.is_deleted = 'NOT_DELETED'
LEFT JOIN sys_perm p ON p.id = pp.perm_id
    AND p.is_deleted = 'NOT_DELETED'
WHERE up_tenant.user_id = 2  -- 张三的系统用户ID
  AND up_tenant.is_deleted = 'NOT_DELETED'
  AND p.perm_code IS NOT NULL

UNION ALL

-- 2. 用户角色授予的权限（ROLE级别）
SELECT 
    p.perm_code,
    p.perm_name,
    pp.target_type,
    pp.data_scope,
    pp.table_name,
    pp.field_operation,
    t.tenant_code,
    '角色级' AS permissionLevel
FROM sys_user_policy up_tenant
INNER JOIN sys_tenant t ON t.id = up_tenant.target_id
    AND up_tenant.target_type = 'TENANT'
LEFT JOIN sys_user_policy up_role ON up_role.user_id = up_tenant.id
    AND up_role.target_type = 'ROLE'
    AND up_role.is_deleted = 'NOT_DELETED'
LEFT JOIN sys_perm_policy pp ON pp.target_type = 'ROLE'
    AND pp.target_id = up_role.target_id
    AND pp.is_deleted = 'NOT_DELETED'
LEFT JOIN sys_perm p ON p.id = pp.perm_id
    AND p.is_deleted = 'NOT_DELETED'
WHERE up_tenant.user_id = 2
  AND up_tenant.is_deleted = 'NOT_DELETED'
  AND p.perm_code IS NOT NULL

UNION ALL

-- 3. 用户部门授予的权限（DEPT级别）
SELECT 
    p.perm_code,
    p.perm_name,
    pp.target_type,
    pp.data_scope,
    pp.table_name,
    pp.field_operation,
    t.tenant_code,
    '部门级' AS permissionLevel
FROM sys_user_policy up_tenant
INNER JOIN sys_tenant t ON t.id = up_tenant.target_id
    AND up_tenant.target_type = 'TENANT'
LEFT JOIN sys_user_policy up_dept ON up_dept.user_id = up_tenant.id
    AND up_dept.target_type = 'DEPT'
    AND up_dept.is_deleted = 'NOT_DELETED'
LEFT JOIN sys_perm_policy pp ON pp.target_type = 'DEPT'
    AND pp.target_id = up_dept.target_id
    AND pp.is_deleted = 'NOT_DELETED'
LEFT JOIN sys_perm p ON p.id = pp.perm_id
    AND p.is_deleted = 'NOT_DELETED'
WHERE up_tenant.user_id = 2
  AND up_tenant.is_deleted = 'NOT_DELETED'
  AND p.perm_code IS NOT NULL

UNION ALL

-- 4. 租户级别授予的权限（TENANT级别）
SELECT 
    p.perm_code,
    p.perm_name,
    pp.target_type,
    pp.data_scope,
    pp.table_name,
    pp.field_operation,
    t.tenant_code,
    '租户级' AS permissionLevel
FROM sys_user_policy up_tenant
INNER JOIN sys_tenant t ON t.id = up_tenant.target_id
    AND up_tenant.target_type = 'TENANT'
LEFT JOIN sys_perm_policy pp ON pp.target_type = 'TENANT'
    AND pp.target_id = t.id
    AND pp.is_deleted = 'NOT_DELETED'
LEFT JOIN sys_perm p ON p.id = pp.perm_id
    AND p.is_deleted = 'NOT_DELETED'
WHERE up_tenant.user_id = 2
  AND up_tenant.is_deleted = 'NOT_DELETED'
  AND p.perm_code IS NOT NULL

ORDER BY tenantCode, permCode, permissionLevel;
```

---

## 九、实际应用场景示例

### 9.1 场景1：张三查询用户列表

**权限检查：**
- 需要权限：`PERM_USER_VIEW (521)`
- 张三拥有：✅ 是（租户级授予）
- 数据范围：**ALL**（可以查看万象集团所有用户）
- 字段范围：`user_name, email, phone, nick_name, status`

**结果：** 允许查询，返回所有字段

---

### 9.2 场景2：张三创建新用户

**权限检查：**
- 需要权限：`PERM_USER_CREATE (522)`
- 张三拥有：✅ 是
  - 租户级：ALL（全租户）
  - 角色级：DEPT_AND_SUB（本部门及下级）
- **最终数据范围：ALL**（取最宽松）
- 字段范围：`user_name, email, phone, password, nick_name`

**结果：** 允许创建，可填写指定字段

---

### 9.3 场景3：张三删除用户

**权限检查：**
- 需要权限：`PERM_USER_DELETE (524)`
- 张三拥有：✅ 是
  - 租户级：ALL（全租户）
  - 部门级：DEPT（仅财务部）
- **最终数据范围：ALL**（租户级更宽松）

**结果：** 允许删除万象集团任意用户

---

### 9.4 场景4：张三修改用户email

**权限检查：**
- 需要权限：`PERM_USER_UPDATE (523)` 且字段 `email`
- 张三拥有：✅ 是
  - 租户级：ALL + email[UPDATE]
  - 角色级：DEPT_AND_SUB + email[UPDATE]
  - 用户级：DEPT + email[UPDATE]
- **最终数据范围：ALL**
- **字段权限：email[UPDATE]** ✅

**结果：** 允许修改

---

## 十、总结

### 10.1 张三的权限特点

1. **租户归属**：单租户（万象集团）
2. **部门归属**：财务部（主部门）
3. **角色归属**：部门经理
4. **权限层级**：4层权限授予（租户+角色+部门+用户）
5. **数据范围**：大部分权限为 ALL，部分被限制为 DEPT_AND_SUB 或 DEPT
6. **特殊授权**：拥有用户级特殊权限覆盖

### 10.2 权限模型优势

1. ✅ **灵活性**：支持4层权限授予（租户/部门/角色/用户）
2. ✅ **细粒度**：支持字段级权限控制
3. ✅ **数据隔离**：支持多种数据范围（ALL/DEPT_AND_SUB/DEPT/SELF）
4. ✅ **权限继承**：用户自动继承所属租户/部门/角色的权限
5. ✅ **特殊授权**：支持用户级特殊权限覆盖
6. ✅ **权限合并**：多层级权限自动合并，取并集

---

**文档完成时间：** 2026-06-12  
**分析用户：** 张三（U002，user_id=2）  
**所属租户：** 万象集团（tenant_id=101）
