# disabledDetailByTenant 结构完整性修复

## 🎯 需求

确保 `disabledDetailByTenant` 返回所有租户的完整结构，而不是只返回有禁用权限的租户。

---

## 📊 修改前后对比

### 修改前（问题）

只返回有禁用权限的租户：

```json
"disabledDetailByTenant": {
  "TENANT_A": {
    "dept": ["DATA_USER"],
    "role": [],
    "system": [],
    "tenant": [],
    "user": []
  }
  // ❌ TENANT_B 和 TENANT_C 不存在
}
```

### 修改后（正确）

返回所有租户的完整结构：

```json
"disabledDetailByTenant": {
  "TENANT_A": {
    "dept": ["DATA_USER"],
    "role": [],
    "system": [],
    "tenant": [],
    "user": []
  },
  "TENANT_B": {
    "dept": [],
    "role": [],
    "system": [],
    "tenant": [],
    "user": []
  },
  "TENANT_C": {
    "dept": [],
    "role": [],
    "system": [],
    "tenant": [],
    "user": []
  }
}
```

---

## 🔧 修改方案

### 修改位置

**文件：** `nexusix-iam/src/main/java/com/shy/nexusix/iam/service/impl/AuthServiceImpl.java`

**行号：** 第389-390行

### 修改逻辑

在设置 `disabledDetailByTenant` 之前，遍历所有租户（`permsByTenant`），确保每个租户都有完整的 `DisabledDetail` 结构。

### 修改前代码

```java
// 设置禁用详情（已在遍历中收集完成）
permissionInfo.setDisabledDetailByTenant(disabledDetailByTenant);
```

**问题：**
- `disabledDetailByTenant` 只在遇到禁用权限时才创建对应租户的 `DisabledDetail`
- 没有禁用权限的租户不会出现在结果中

### 修改后代码

```java
// 设置禁用详情（确保所有租户都有完整结构）
for (String tenantCode : permsByTenant.keySet()) {
    if (!disabledDetailByTenant.containsKey(tenantCode)) {
        UserContextDTO.DisabledDetail detail = new UserContextDTO.DisabledDetail();
        detail.setSystem(new ArrayList<>());
        detail.setTenant(new ArrayList<>());
        detail.setDept(new ArrayList<>());
        detail.setRole(new ArrayList<>());
        detail.setUser(new ArrayList<>());
        disabledDetailByTenant.put(tenantCode, detail);
    }
}
permissionInfo.setDisabledDetailByTenant(disabledDetailByTenant);
```

**改进：**
- 遍历所有租户（`permsByTenant.keySet()`）
- 检查该租户是否已在 `disabledDetailByTenant` 中
- 如果不存在，创建一个空的 `DisabledDetail` 结构
- 确保所有租户都有完整的5个级别列表（system/tenant/dept/role/user）

---

## 💡 设计思路

### 数据流程

1. **权限遍历阶段**（第152-225行）
   - 遍历所有权限记录
   - 将权限按租户分组存入 `permsByTenant`
   - 遇到禁用权限时，创建或更新该租户的 `DisabledDetail`

2. **结构补全阶段**（第389-399行）
   - 遍历 `permsByTenant` 的所有租户
   - 为没有禁用权限的租户创建空的 `DisabledDetail`
   - 确保返回结构完整

### 为什么需要补全

1. **前端展示一致性**
   - 前端可能需要遍历所有租户显示禁用详情
   - 如果某些租户不存在，需要额外的null检查

2. **API契约明确**
   - 明确告诉调用方：所有租户都会返回
   - 空列表表示该租户该级别无禁用权限

3. **与其他字段对齐**
   - `fieldPermissionByTenant` 也返回所有租户
   - 保持数据结构的一致性

---

## 📋 测试验证

### 测试场景1：部分租户有禁用权限

**前置条件：**
- 租户A：禁用部门级DATA_USER权限
- 租户B：无禁用权限
- 租户C：无禁用权限

**预期结果：**
```json
"disabledDetailByTenant": {
  "TENANT_A": {
    "dept": ["DATA_USER"],
    "role": [],
    "system": [],
    "tenant": [],
    "user": []
  },
  "TENANT_B": {
    "dept": [],
    "role": [],
    "system": [],
    "tenant": [],
    "user": []
  },
  "TENANT_C": {
    "dept": [],
    "role": [],
    "system": [],
    "tenant": [],
    "user": []
  }
}
```

### 测试场景2：所有租户都无禁用权限

**前置条件：**
- 所有权限策略都是ACTIVE

**预期结果：**
```json
"disabledDetailByTenant": {
  "TENANT_A": {
    "dept": [],
    "role": [],
    "system": [],
    "tenant": [],
    "user": []
  },
  "TENANT_B": {
    "dept": [],
    "role": [],
    "system": [],
    "tenant": [],
    "user": []
  },
  "TENANT_C": {
    "dept": [],
    "role": [],
    "system": [],
    "tenant": [],
    "user": []
  }
}
```

### 测试场景3：多个租户多级禁用

**前置条件：**
- 租户A：部门级禁用DATA_USER
- 租户B：租户级禁用DATA_ORDER
- 租户C：角色级禁用MENU_SYSTEM

**预期结果：**
```json
"disabledDetailByTenant": {
  "TENANT_A": {
    "dept": ["DATA_USER"],
    "role": [],
    "system": [],
    "tenant": [],
    "user": []
  },
  "TENANT_B": {
    "dept": [],
    "role": [],
    "system": [],
    "tenant": ["DATA_ORDER"],
    "user": []
  },
  "TENANT_C": {
    "dept": [],
    "role": ["MENU_SYSTEM"],
    "system": [],
    "tenant": [],
    "user": []
  }
}
```

---

## ✅ 编译状态

**编译成功**（BUILD SUCCESS）

```
[INFO] nexusix-iam ........................................ SUCCESS
[INFO] Total time:  4.788 s
```

---

## 📝 影响范围

### 受影响的功能
- ✅ `disabledDetailByTenant` 返回结构
- ✅ API响应数据完整性

### 不受影响的功能
- ✅ 禁用权限收集逻辑
- ✅ 权限查询和显示
- ✅ 其他字段的返回

### 兼容性
- ✅ 向后兼容
- ✅ 只是补全了缺失的租户结构
- ✅ 原有的禁用权限数据不变

---

## 🎯 与其他字段的一致性

### fieldPermissionByTenant

同样返回所有租户：

```java
for (String tenantCode : permsByTenant.keySet()) {
    UserContextDTO.FieldPermission fp = new UserContextDTO.FieldPermission();
    fp.setQuery(queryFieldMapByTenant.getOrDefault(tenantCode, new HashMap<>()));
    fp.setCreate(createFieldMapByTenant.getOrDefault(tenantCode, new HashMap<>()));
    fp.setUpdate(updateFieldMapByTenant.getOrDefault(tenantCode, new HashMap<>()));
    fieldPermByTenant.put(tenantCode, fp);
}
```

### disabledDetailByTenant

现在也返回所有租户（修改后）：

```java
for (String tenantCode : permsByTenant.keySet()) {
    if (!disabledDetailByTenant.containsKey(tenantCode)) {
        // 创建空结构
    }
}
```

**一致性：** 两者都基于 `permsByTenant.keySet()` 遍历，确保返回相同的租户集合。

---

## 🎉 总结

**修改目标：** 确保所有租户都有完整的 `disabledDetailByTenant` 结构

**修改方法：** 在设置前遍历所有租户，补全缺失的空结构

**修改位置：** `AuthServiceImpl.java` 第389-399行

**编译状态：** ✅ 成功

**测试状态：** ⬜ 待验证

---

**修改完成！请重启应用并验证所有租户都返回完整结构！** 🚀
