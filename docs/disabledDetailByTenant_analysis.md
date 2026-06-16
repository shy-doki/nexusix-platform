# disabledDetailByTenant 实现逻辑分析

## 📋 数据结构定义

```java
public static class DisabledDetail {
    /** 系统级禁用的权限编码 */
    private List<String> system;
    /** 租户级禁用的权限编码 */
    private List<String> tenant;
    /** 角色级禁用的权限编码 */
    private List<String> role;
    /** 用户级禁用的权限编码 */
    private List<String> user;
}
```

**字段说明：**
- `system`: 系统级别禁用的权限（权限本身被禁用）
- `tenant`: 租户级别禁用的权限（租户策略禁用）
- `role`: 角色级别禁用的权限（角色策略禁用）
- `user`: 用户级别禁用的权限（用户策略禁用）

---

## 📊 当前实现逻辑

### 1. 初始化代码（349-359行）

```java
// 初始化禁用详情（按租户分组）
Map<String, UserContextDTO.DisabledDetail> disabledDetailByTenant = new HashMap<>();
for (String tenantCode : permsByTenant.keySet()) {
    UserContextDTO.DisabledDetail detail = new UserContextDTO.DisabledDetail();
    detail.setSystem(new ArrayList<>());
    detail.setTenant(new ArrayList<>());
    detail.setRole(new ArrayList<>());
    detail.setUser(new ArrayList<>());
    disabledDetailByTenant.put(tenantCode, detail);
}
permissionInfo.setDisabledDetailByTenant(disabledDetailByTenant);
```

### 2. 当前行为

**只做了初始化，没有填充数据！**

- ✅ 为每个租户创建了空的 `DisabledDetail` 对象
- ✅ 初始化了4个空列表（system、tenant、role、user）
- ❌ **没有任何逻辑去填充这些列表**

### 3. API返回结果

```json
"disabledDetailByTenant": {
  "TENANT_A": {
    "system": [],
    "tenant": [],
    "role": [],
    "user": []
  },
  "TENANT_B": {
    "system": [],
    "tenant": [],
    "role": [],
    "user": []
  },
  "TENANT_C": {
    "system": [],
    "tenant": [],
    "role": []
    "user": []
  }
}
```

**所有列表都为空！**

---

## 🔍 问题分析

### 问题1：功能未实现

`disabledDetailByTenant` 的初衷是记录**哪些权限在哪个级别被禁用**，但当前代码：
- ✅ 创建了数据结构
- ❌ 没有收集禁用权限信息
- ❌ 没有按禁用级别分类

### 问题2：与 `current.disabled` 的区别

当前代码中有另一个禁用权限列表：

```java
// 当前租户的启用/禁用分组
if (currentTenantCode.equals(tenantCode)) {
    if ("ACTIVE".equals(perm.getPermPolicyStatus())) {
        currentEnabledPerms.add(item);
    } else {
        currentDisabledPerms.add(item);  // 这里收集了禁用权限
    }
}
```

**区别：**
- `current.disabled`: 只包含**当前租户**的禁用权限列表（不区分禁用级别）
- `disabledDetailByTenant`: 应该包含**所有租户**的禁用权限，并**按禁用级别分类**

---

## 🎯 应该如何实现

### 设计思路

禁用权限可能来自4个级别：

1. **SYSTEM级别**：权限主表 `sys_perm.status = 'DISABLED'`
2. **TENANT级别**：租户策略 `sys_perm_policy.target_type='TENANT' AND status='INACTIVE'`
3. **ROLE级别**：角色策略 `sys_perm_policy.target_type='ROLE' AND status='INACTIVE'`
4. **USER级别**：用户策略 `sys_perm_policy.target_type='USER' AND status='INACTIVE'`

### 实现逻辑伪代码

```java
for (UserPermDTO perm : permList) {
    String tenantCode = perm.getTenantCode();
    String permCode = perm.getPermCode();
    
    // 获取该租户的DisabledDetail
    UserContextDTO.DisabledDetail detail = disabledDetailByTenant.get(tenantCode);
    
    // 如果权限策略状态不是ACTIVE
    if (!"ACTIVE".equals(perm.getPermPolicyStatus())) {
        // 根据策略类型分类
        switch (perm.getTargetType()) {
            case "TENANT":
                if (!detail.getTenant().contains(permCode)) {
                    detail.getTenant().add(permCode);
                }
                break;
            case "ROLE":
                if (!detail.getRole().contains(permCode)) {
                    detail.getRole().add(permCode);
                }
                break;
            case "USER":
                if (!detail.getUser().contains(permCode)) {
                    detail.getUser().add(permCode);
                }
                break;
        }
    }
    
    // 如果权限本身被禁用（需要额外字段）
    if ("DISABLED".equals(perm.getPermStatus())) {
        if (!detail.getSystem().contains(permCode)) {
            detail.getSystem().add(permCode);
        }
    }
}
```

### 数据库需要的字段

当前 `UserPermDTO` 可能缺少字段，需要确认：
- ✅ `permCode` - 权限编码
- ✅ `tenantCode` - 租户编码
- ✅ `permPolicyStatus` - 策略状态（ACTIVE/INACTIVE）
- ✅ `targetType` - 策略类型（TENANT/ROLE/USER）
- ❓ `permStatus` - 权限主表状态（需要确认是否有）

---

## 📈 预期返回示例

假设：
- 租户A：角色策略禁用了 `DATA_USER`
- 租户B：用户策略禁用了 `MENU_MONITOR`
- 租户C：租户策略禁用了 `MENU_SYSTEM`

```json
"disabledDetailByTenant": {
  "TENANT_A": {
    "system": [],
    "tenant": [],
    "role": ["DATA_USER"],
    "user": []
  },
  "TENANT_B": {
    "system": [],
    "tenant": [],
    "role": [],
    "user": ["MENU_MONITOR"]
  },
  "TENANT_C": {
    "system": [],
    "tenant": ["MENU_SYSTEM"],
    "role": [],
    "user": []
  }
}
```

---

## 🔧 是否需要实现

### 场景分析

**有用的场景：**
1. **权限审计**：查看哪些权限在哪个层级被禁用
2. **问题排查**：用户看不到某个功能，可以查看是哪个层级禁用的
3. **权限管理UI**：前端可以显示禁用的层级信息

**可能不需要的原因：**
1. **当前返回的数据始终为空**，说明可能不是核心功能
2. **已有 `current.disabled` 列表**，可能足够使用
3. **增加复杂度**，需要额外的数据收集和去重逻辑

---

## 💡 建议

### 方案1：完整实现（推荐如果需要详细审计）

实现完整的禁用权限收集逻辑，按级别分类。

**优点：**
- 信息完整，便于问题排查
- 符合字段设计初衷

**缺点：**
- 需要额外开发和测试
- 增加响应数据量

### 方案2：保持现状（推荐如果不需要）

如果业务上不需要区分禁用级别，可以：
- 保持当前空实现
- 或者直接移除这个字段

**优点：**
- 不增加开发工作量
- 减少响应数据量

**缺点：**
- 返回无用的空数据

### 方案3：简化实现

只实现一个总的禁用列表，不区分级别：

```json
"disabledDetailByTenant": {
  "TENANT_A": ["DATA_USER"],
  "TENANT_B": ["MENU_MONITOR"],
  "TENANT_C": ["MENU_SYSTEM"]
}
```

**优点：**
- 实现简单
- 数据量小

**缺点：**
- 失去了级别信息

---

## ❓ 需要确认的问题

1. **业务需求**：是否需要区分权限在哪个级别被禁用？
2. **使用场景**：前端是否会使用这个字段？
3. **数据完整性**：SQL查询是否包含了权限主表的状态？

---

**当前状态：** `disabledDetailByTenant` 只是占位实现，所有列表都为空，未实际填充禁用权限信息。

**建议：** 根据业务需求决定是否需要完整实现，如果不需要可以考虑移除或简化。
