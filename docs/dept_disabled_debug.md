# 部门级禁用权限问题排查方案

## 🔴 问题描述

执行 TC-03（部门级禁用测试）后，禁用的权限没有出现在 `disabledDetailByTenant.dept` 中，列表仍然为空。

---

## 🔍 问题分析

### 可能的原因

1. **SQL查询未返回DEPT级的禁用权限**
   - SQL可能过滤掉了INACTIVE状态的策略
   - 或者JOIN条件有问题

2. **targetType字段值不匹配**
   - SQL返回的targetType可能不是 "DEPT"
   - 可能是其他值或null

3. **permPolicyStatus判断问题**
   - 状态值可能不是 "ACTIVE"/"INACTIVE"
   - 可能是其他值

4. **去重逻辑问题**
   - 权限已经在其他级别被添加
   - 去重key冲突

---

## 🧪 排查步骤

### 步骤1：验证数据库状态

执行以下SQL，确认策略已被正确禁用：

```sql
-- 查看策略81001的状态
SELECT 
    pp.id,
    pp.policy_code,
    p.perm_code,
    p.perm_name,
    pp.target_type,
    pp.target_id,
    pp.status,
    d.dept_code,
    t.tenant_code
FROM sys_perm_policy pp
INNER JOIN sys_perm p ON p.id = pp.perm_id
LEFT JOIN sys_tenant_policy tp ON tp.id = pp.target_id AND pp.target_type = 'DEPT'
LEFT JOIN sys_dept d ON d.id = tp.source_id
LEFT JOIN sys_tenant t ON t.id = tp.tenant_id
WHERE pp.id = 81001;
```

**预期结果：**
- `status` = 'INACTIVE'
- `target_type` = 'DEPT'
- `perm_code` = 'DATA_USER'
- `dept_code` = 'DEPT_RD'
- `tenant_code` = 'TENANT_A'

---

### 步骤2：验证SQL查询结果

执行登录时使用的SQL，查看是否返回了DEPT级的禁用权限：

```sql
-- 模拟登录SQL：查询test_all用户的所有权限（包括禁用的）
-- 3. 用户部门授予的权限（DEPT级别）
SELECT
    p.perm_code AS permCode,
    p.perm_name AS permName,
    p.perm_type AS permType,
    t.tenant_code AS tenantCode,
    t.tenant_name AS tenantName,
    pp.status AS permPolicyStatus,
    pp.target_type AS targetType,
    pp.data_scope AS dataScope,
    pp.table_name AS tableName,
    pp.field_operation AS fieldPermissions,
    up_tenant.id AS tenantUserId
FROM sys_user_policy up_tenant
INNER JOIN sys_tenant t ON t.id = up_tenant.target_id
    AND up_tenant.target_type = 'TENANT'
    AND t.status = 'ENABLED'
    AND t.is_deleted = 'NOT_DELETED'
INNER JOIN sys_user_policy up_dept ON up_dept.user_id = up_tenant.id
    AND up_dept.target_type = 'DEPT'
    AND up_dept.status = 'ACTIVE'
    AND up_dept.is_deleted = 'NOT_DELETED'
INNER JOIN sys_tenant_policy tp_dept ON tp_dept.id = up_dept.target_id
    AND tp_dept.source_type = 'DEPT'
    AND tp_dept.tenant_id = t.id
    AND tp_dept.status = 'ACTIVE'
    AND tp_dept.is_deleted = 'NOT_DELETED'
INNER JOIN sys_dept d ON d.id = tp_dept.source_id
    AND d.status = 'ENABLED'
    AND d.is_deleted = 'NOT_DELETED'
INNER JOIN sys_perm_policy pp ON pp.target_type = 'DEPT'
    AND pp.target_id = tp_dept.id
    -- ⚠️ 注意这里：是否过滤了INACTIVE状态？
    -- AND pp.status = 'ACTIVE'  -- 如果有这行，禁用的策略不会返回
    AND pp.is_deleted = 'NOT_DELETED'
LEFT JOIN sys_perm_policy pp_parent ON pp_parent.id = pp.perm_id
INNER JOIN sys_perm p ON p.id = COALESCE(pp_parent.perm_id, pp.perm_id)
    AND p.status = 'ENABLED'
    AND p.is_deleted = 'NOT_DELETED'
WHERE up_tenant.user_id = 7301
  AND up_tenant.status = 'ACTIVE'
  AND up_tenant.is_deleted = 'NOT_DELETED'
  AND t.tenant_code = 'TENANT_A'  -- 只看租户A
ORDER BY permCode;
```

**关键检查点：**
1. 是否返回了 `perm_code = 'DATA_USER'` 的记录？
2. 如果返回了，`permPolicyStatus` 是什么值？
3. `targetType` 是什么值？

**预期结果：**
应该返回一条记录：
- `permCode` = 'DATA_USER'
- `permPolicyStatus` = 'INACTIVE'
- `targetType` = 'DEPT'

**如果没有返回记录，说明SQL查询有过滤条件！**

---

### 步骤3：检查SQL过滤条件

查看 `SysPermPolicyMapper.xml` 中DEPT级查询是否过滤了INACTIVE：

```bash
# 在命令行执行
grep -A 30 "用户部门授予的权限" "D:\PojectHub\NexusIX-Platform\nexusix-iam\src\main\resources\mapper\SysPermPolicyMapper.xml"
```

**查找关键行：**
```xml
<select id="queryUserAllPermInfo">
    ...
    -- 3. 用户部门授予的权限（DEPT级别）
    ...
    INNER JOIN sys_perm_policy pp ON pp.target_type = 'DEPT'
        AND pp.target_id = tp_dept.id
        AND pp.status = 'ACTIVE'  <!-- ⚠️ 如果有这行，问题就在这里！ -->
        AND pp.is_deleted = 'NOT_DELETED'
```

**如果发现有 `pp.status = 'ACTIVE'` 过滤条件：**
- 这就是问题所在！
- SQL只返回ACTIVE的策略，INACTIVE的策略被过滤掉了
- 需要移除这个过滤条件

---

### 步骤4：添加调试日志

在 `AuthServiceImpl.java` 中添加日志来验证数据流：

```java
// 在第190行之前添加
System.out.println("【DEBUG】处理权限: permCode=" + perm.getPermCode() 
    + ", tenantCode=" + perm.getTenantCode()
    + ", targetType=" + perm.getTargetType() 
    + ", status=" + perm.getPermPolicyStatus());

// 🔴 收集禁用权限的级别信息
if (!"ACTIVE".equals(perm.getPermPolicyStatus())) {
    System.out.println("【DEBUG】发现禁用权限: " + perm.getPermCode() 
        + ", targetType=" + perm.getTargetType());
    
    // ... 原有代码 ...
}
```

然后：
1. 重新编译
2. 重启应用
3. 登录
4. 查看控制台输出

**预期看到：**
```
【DEBUG】处理权限: permCode=DATA_USER, tenantCode=TENANT_A, targetType=DEPT, status=INACTIVE
【DEBUG】发现禁用权限: DATA_USER, targetType=DEPT
```

**如果没有看到DEPT的禁用权限，说明SQL没有返回！**

---

## 🔧 解决方案

### 方案1：修改SQL查询（最可能的原因）

如果SQL中有 `pp.status = 'ACTIVE'` 过滤条件，需要移除：

**修改位置：** `nexusix-iam/src/main/resources/mapper/SysPermPolicyMapper.xml`

**修改前（第116-119行左右）：**
```xml
INNER JOIN sys_perm_policy pp ON pp.target_type = 'DEPT'
    AND pp.target_id = tp_dept.id
    AND pp.status = 'ACTIVE'  <!-- ❌ 移除这行 -->
    AND pp.is_deleted = 'NOT_DELETED'
```

**修改后：**
```xml
INNER JOIN sys_perm_policy pp ON pp.target_type = 'DEPT'
    AND pp.target_id = tp_dept.id
    AND pp.is_deleted = 'NOT_DELETED'
```

**同样需要修改其他级别的查询：**
- TENANT级（第23-26行左右）
- ROLE级（第76-79行左右）
- USER级（第42-45行左右）

所有 `pp.status = 'ACTIVE'` 都需要移除，改为只判断 `pp.is_deleted = 'NOT_DELETED'`。

---

### 方案2：检查用户部门策略状态

如果SQL正确但仍然不返回，检查用户部门绑定策略是否也被禁用了：

```sql
-- 查看test_all在租户A的研发部绑定状态
SELECT 
    up.id,
    up.policy_code,
    up.status,
    d.dept_code,
    t.tenant_code
FROM sys_user_policy up
INNER JOIN sys_tenant t ON t.id = (
    SELECT target_id FROM sys_user_policy 
    WHERE id = up.user_id AND target_type = 'TENANT'
)
INNER JOIN sys_tenant_policy tp ON tp.id = up.target_id
INNER JOIN sys_dept d ON d.id = tp.source_id
WHERE up.user_id = 77001  -- test_all在租户A的用户ID
  AND up.target_type = 'DEPT'
  AND d.dept_code = 'DEPT_RD';
```

**检查：**
- 如果 `up.status = 'INACTIVE'`，说明用户部门绑定被禁用了
- SQL查询中有 `up_dept.status = 'ACTIVE'` 过滤，会导致该部门的所有权限都不返回

**解决：** 确保测试时只禁用权限策略，不禁用用户策略。

---

### 方案3：修改去重逻辑位置

可能权限在 `permDeduplicationSet` 去重时已经被跳过了。让我检查去重逻辑：

```java
// 第162-185行
String deduplicationKey = perm.getPermCode() + "_" + tenantCode;
if (!permDeduplicationSet.contains(deduplicationKey)) {
    permDeduplicationSet.add(deduplicationKey);
    // ... 只在这里面处理权限
}

// 🔴 禁用权限收集在外面（第190行）
if (!"ACTIVE".equals(perm.getPermPolicyStatus())) {
    // ...
}
```

**问题：** 如果同一个权限（如DATA_USER）在多个级别都有策略（TENANT、DEPT、ROLE），去重后只会处理第一个，后面的都被跳过了。

**解决：** 将禁用权限收集移到去重判断之外，改为基于 `permCode + tenantCode + targetType` 去重。

---

## 🎯 最可能的原因

根据经验，**99%是SQL查询过滤了INACTIVE状态的策略**。

请执行步骤2的SQL验证，如果确实没有返回DEPT级的禁用权限，就修改 `SysPermPolicyMapper.xml`，移除所有 `pp.status = 'ACTIVE'` 过滤条件。

---

## 📝 验证修复

修改后重新测试：

1. 重新编译
2. 重启应用
3. 执行 TC-03 测试
4. 检查 `disabledDetailByTenant.TENANT_A.dept` 是否包含 `["DATA_USER"]`

---

**请先执行步骤2的SQL验证，把结果告诉我，我来帮你定位具体问题！** 🔍
