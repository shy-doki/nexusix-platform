# 禁用权限分级功能实现

## ✅ 实现完成

已成功实现禁用权限的分级收集逻辑，可以区分权限在哪个级别被禁用。

---

## 📊 数据结构

### DisabledDetail 类

```java
public static class DisabledDetail {
    /** 系统级禁用的权限编码 */
    private List<String> system;
    /** 租户级禁用的权限编码 */
    private List<String> tenant;
    /** 部门级禁用的权限编码 */
    private List<String> dept;      // ✅ 新增
    /** 角色级禁用的权限编码 */
    private List<String> role;
    /** 用户级禁用的权限编码 */
    private List<String> user;
}
```

---

## 🔧 实现逻辑

### 1. 初始化（遍历前）

```java
// 禁用权限详情（按租户分组）
Map<String, UserContextDTO.DisabledDetail> disabledDetailByTenant = new HashMap<>();
```

### 2. 遍历权限时收集禁用信息

```java
for (UserPermDTO perm : permList) {
    // ... 权限去重逻辑 ...
    
    // 🔴 收集禁用权限的级别信息
    if (!"ACTIVE".equals(perm.getPermPolicyStatus())) {
        // 获取或创建该租户的DisabledDetail
        UserContextDTO.DisabledDetail detail = disabledDetailByTenant.get(tenantCode);
        if (detail == null) {
            detail = new UserContextDTO.DisabledDetail();
            detail.setSystem(new ArrayList<>());
            detail.setTenant(new ArrayList<>());
            detail.setDept(new ArrayList<>());
            detail.setRole(new ArrayList<>());
            detail.setUser(new ArrayList<>());
            disabledDetailByTenant.put(tenantCode, detail);
        }

        String permCode = perm.getPermCode();
        String targetType = perm.getTargetType();

        // 根据策略类型分类禁用权限
        if ("TENANT".equals(targetType)) {
            if (!detail.getTenant().contains(permCode)) {
                detail.getTenant().add(permCode);
            }
        } else if ("DEPT".equals(targetType)) {
            if (!detail.getDept().contains(permCode)) {
                detail.getDept().add(permCode);
            }
        } else if ("ROLE".equals(targetType)) {
            if (!detail.getRole().contains(permCode)) {
                detail.getRole().add(permCode);
            }
        } else if ("USER".equals(targetType)) {
            if (!detail.getUser().contains(permCode)) {
                detail.getUser().add(permCode);
            }
        }
    }
}
```

### 3. 设置到返回结果

```java
// 设置禁用详情（已在遍历中收集完成）
permissionInfo.setDisabledDetailByTenant(disabledDetailByTenant);
```

---

## 🎯 分级规则

| 级别 | 判断条件 | 说明 |
|-----|---------|------|
| **TENANT级** | `targetType = 'TENANT' AND status != 'ACTIVE'` | 租户策略中禁用的权限 |
| **DEPT级** | `targetType = 'DEPT' AND status != 'ACTIVE'` | 部门策略中禁用的权限 |
| **ROLE级** | `targetType = 'ROLE' AND status != 'ACTIVE'` | 角色策略中禁用的权限 |
| **USER级** | `targetType = 'USER' AND status != 'ACTIVE'` | 用户策略中禁用的权限 |
| **SYSTEM级** | 权限主表 `sys_perm.status = 'DISABLED'` | 暂未实现（需要额外字段） |

---

## 📋 预期返回示例

假设测试数据：
- 租户A：没有禁用权限
- 租户B：部门级禁用了 `DATA_LOG`
- 租户C：角色级禁用了 `MENU_REPORT`

```json
"disabledDetailByTenant": {
  "TENANT_A": {
    "system": [],
    "tenant": [],
    "dept": [],
    "role": [],
    "user": []
  },
  "TENANT_B": {
    "system": [],
    "tenant": [],
    "dept": ["DATA_LOG"],
    "role": [],
    "user": []
  },
  "TENANT_C": {
    "system": [],
    "tenant": [],
    "dept": [],
    "role": ["MENU_REPORT"],
    "user": []
  }
}
```

---

## 🔍 关键特性

### 1. 按租户分组
每个租户有独立的 `DisabledDetail` 对象，互不影响。

### 2. 自动去重
使用 `List.contains()` 检查，避免同一权限被多次添加。

### 3. 懒加载创建
只有当遇到禁用权限时，才为该租户创建 `DisabledDetail` 对象。

### 4. 多级禁用支持
同一个权限可能在不同级别都被禁用，会分别记录在对应列表中。

---

## 📝 修改的文件

1. **nexusix-core/src/main/java/com/shy/nexusix/core/entity/dto/UserContextDTO.java**
   - ✅ 在 `DisabledDetail` 类中新增 `dept` 字段

2. **nexusix-iam/src/main/java/com/shy/nexusix/iam/service/impl/AuthServiceImpl.java**
   - ✅ 在遍历前初始化 `disabledDetailByTenant`
   - ✅ 在权限遍历中收集禁用权限信息
   - ✅ 根据 `targetType` 分类到不同级别
   - ✅ 移除后面的重复初始化逻辑

---

## ✅ 编译状态

- **编译成功**（BUILD SUCCESS）
- 所有模块编译通过
- 无错误和警告

---

## 🚀 下一步

1. **重启应用**
2. **登录测试**
3. **验证返回结果**

查看 `disabledDetailByTenant` 是否正确收集了禁用权限，并按级别分类。

如果当前数据库中所有权限策略都是 `ACTIVE` 状态，则所有列表都为空。可以手动修改某个策略的状态为 `INACTIVE` 来测试：

```sql
-- 测试：禁用租户A的部门级DATA_USER权限
UPDATE sys_perm_policy 
SET status = 'INACTIVE' 
WHERE id = 81001;  -- 部门级策略

-- 测试后恢复
UPDATE sys_perm_policy 
SET status = 'ACTIVE' 
WHERE id = 81001;
```

---

**实现完成！请重启应用并测试！** 🎉
