# 权限辅助类重构 - 从独立工具类改为内联实现

## 📋 修改概述

**修改时间：** 2026-06-12  
**修改原因：** 将独立的工具类改为直接在调用类的方法体内实现，避免过度抽象

---

## ✅ 已完成的修改

### 1. 删除独立工具类

**已删除文件：**
- ❌ `nexusix-core/src/main/java/com/shy/nexusix/core/helper/FieldPermissionHelper.java`
- ❌ `nexusix-core/src/main/java/com/shy/nexusix/core/helper/DataScopeHelper.java`

**删除原因：**
- 这些功能目前只在一个地方使用，不需要独立的工具类
- 内联实现更直观，减少不必要的抽象层
- 避免过早优化，保持代码简洁

---

### 2. SysTenantServiceImpl 改为内联实现

**修改文件：** `nexusix-tenant/src/main/java/com/shy/nexusix/tenant/service/impl/SysTenantServiceImpl.java`

**修改内容：**

#### **修改前（使用工具类）：**
```java
private List<String> getQueryOperableFields() {
    return FieldPermissionHelper.getQueryOperableFields(GlobalConstant.Table.TENANT);
}
```

#### **修改后（内联实现）：**
```java
private List<String> getQueryOperableFields() {
    // 直接从UserContext获取字段权限，不使用独立工具类
    UserContextDTO userContext = UserContext.getUserContext();
    if (userContext == null) {
        return null;
    }

    UserContextDTO.FieldPermission fieldPerm =
        userContext.getPermissions().getFieldPermission();

    if (fieldPerm == null || fieldPerm.getQuery() == null) {
        return null;
    }

    UserContextDTO.TableFieldPermission tableFieldPerm =
        fieldPerm.getQuery().get(GlobalConstant.Table.TENANT);

    if (tableFieldPerm == null || tableFieldPerm.getOperable() == null
        || tableFieldPerm.getOperable().isEmpty()) {
        return null;  // null表示无限制
    }

    return tableFieldPerm.getOperable();
}
```

---

## 💡 内联实现的优势

### 1. **更直观**
- 代码逻辑一目了然，不需要跳转到其他文件
- 便于理解数据流向

### 2. **更灵活**
- 可以根据具体业务场景调整逻辑
- 不需要考虑通用性，代码更简洁

### 3. **减少依赖**
- 不依赖外部工具类
- 减少模块间耦合

### 4. **避免过早优化**
- 只在真正需要复用时才抽象为工具类
- 遵循 YAGNI 原则（You Aren't Gonna Need It）

---

## 📖 如何在其他Service中实现字段权限

当你需要在其他 Service（如 `SysUserServiceImpl`）中实现字段权限时，参考以下模板：

### **模板代码：**

```java
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> 
    implements ISysUserService {
    
    /**
     * 获取查询操作的可操作字段
     */
    private List<String> getQueryOperableFields() {
        UserContextDTO userContext = UserContext.getUserContext();
        if (userContext == null) {
            return null;
        }

        UserContextDTO.FieldPermission fieldPerm =
            userContext.getPermissions().getFieldPermission();

        if (fieldPerm == null || fieldPerm.getQuery() == null) {
            return null;
        }

        UserContextDTO.TableFieldPermission tableFieldPerm =
            fieldPerm.getQuery().get(GlobalConstant.Table.USER); // 改为对应的表名

        if (tableFieldPerm == null || tableFieldPerm.getOperable() == null
            || tableFieldPerm.getOperable().isEmpty()) {
            return null;
        }

        return tableFieldPerm.getOperable();
    }
    
    /**
     * 获取新增操作的可操作字段
     */
    private List<String> getCreateOperableFields() {
        UserContextDTO userContext = UserContext.getUserContext();
        if (userContext == null) {
            return null;
        }

        UserContextDTO.FieldPermission fieldPerm =
            userContext.getPermissions().getFieldPermission();

        if (fieldPerm == null || fieldPerm.getCreate() == null) {
            return null;
        }

        UserContextDTO.TableFieldPermission tableFieldPerm =
            fieldPerm.getCreate().get(GlobalConstant.Table.USER);

        if (tableFieldPerm == null || tableFieldPerm.getOperable() == null
            || tableFieldPerm.getOperable().isEmpty()) {
            return null;
        }

        return tableFieldPerm.getOperable();
    }
    
    /**
     * 获取更新操作的可操作字段
     */
    private List<String> getUpdateOperableFields() {
        UserContextDTO userContext = UserContext.getUserContext();
        if (userContext == null) {
            return null;
        }

        UserContextDTO.FieldPermission fieldPerm =
            userContext.getPermissions().getFieldPermission();

        if (fieldPerm == null || fieldPerm.getUpdate() == null) {
            return null;
        }

        UserContextDTO.TableFieldPermission tableFieldPerm =
            fieldPerm.getUpdate().get(GlobalConstant.Table.USER);

        if (tableFieldPerm == null || tableFieldPerm.getOperable() == null
            || tableFieldPerm.getOperable().isEmpty()) {
            return null;
        }

        return tableFieldPerm.getOperable();
    }
    
    @Override
    public List<SysUserVO> queryUserList(SysUserQueryRTO queryRTO) {
        // 使用字段权限
        List<String> visibleFields = getQueryOperableFields();
        
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        if (visibleFields != null && !visibleFields.isEmpty()) {
            wrapper.select(SysUser.class, 
                field -> visibleFields.contains(field.getColumn())
            );
        }
        
        // ... 其他查询逻辑
    }
}
```

---

## 📖 如何在其他Service中实现数据范围

### **模板代码：**

```java
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> 
    implements ISysUserService {
    
    /**
     * 获取权限的数据范围（取最宽松的）
     */
    private String getDataScopeForPermission(String permCode) {
        UserContextDTO userContext = UserContext.getUserContext();
        if (userContext == null) {
            return "SELF";
        }

        // 优先级：ALL > DEPT_AND_SUB > DEPT > SELF
        String maxDataScope = "SELF";

        List<UserContextDTO.RoleItem> validRoles = userContext.getRoles().getValid();
        if (validRoles != null) {
            for (UserContextDTO.RoleItem role : validRoles) {
                String roleDataScope = role.getDataScope();
                if (roleDataScope != null && isWiderDataScope(roleDataScope, maxDataScope)) {
                    maxDataScope = roleDataScope;
                }
            }
        }

        return maxDataScope;
    }
    
    /**
     * 比较数据范围宽松度
     */
    private boolean isWiderDataScope(String scope1, String scope2) {
        Map<String, Integer> scopeLevel = new HashMap<>();
        scopeLevel.put("ALL", 4);
        scopeLevel.put("DEPT_AND_SUB", 3);
        scopeLevel.put("DEPT", 2);
        scopeLevel.put("SELF", 1);

        return scopeLevel.getOrDefault(scope1, 0) > scopeLevel.getOrDefault(scope2, 0);
    }
    
    /**
     * 获取当前用户的部门路径
     */
    private String getCurrentUserDeptPath() {
        UserContextDTO userContext = UserContext.getUserContext();
        if (userContext == null) {
            throw new BusinessException("当前用户未登录");
        }

        List<UserContextDTO.DeptItem> currentDepts = userContext.getDepts().getCurrent();
        if (currentDepts == null || currentDepts.isEmpty()) {
            throw new BusinessException("当前用户未分配部门");
        }

        return currentDepts.get(0).getPath();
    }
    
    /**
     * 应用数据范围到查询条件
     */
    private void applyDataScope(LambdaQueryWrapper<SysUser> wrapper, String dataScope) {
        if ("ALL".equals(dataScope)) {
            return; // 不添加额外条件
        }
        
        if ("DEPT_AND_SUB".equals(dataScope)) {
            String deptPath = getCurrentUserDeptPath();
            wrapper.likeRight(SysUser::getDeptPath, deptPath);
            return;
        }
        
        if ("DEPT".equals(dataScope)) {
            String deptId = getCurrentUserDeptId();
            wrapper.eq(SysUser::getDeptId, deptId);
            return;
        }
        
        if ("SELF".equals(dataScope)) {
            Long userId = getCurrentUserId();
            wrapper.eq(SysUser::getCreateBy, userId);
            return;
        }
    }
    
    @Override
    public List<SysUserVO> queryUserList(SysUserQueryRTO queryRTO) {
        // 1. 获取字段权限
        List<String> visibleFields = getQueryOperableFields();
        
        // 2. 构建查询条件
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getIsDeleted, "NOT_DELETED");
        
        // 3. 租户隔离
        UserContextDTO userContext = UserContext.getUserContext();
        wrapper.eq(SysUser::getTenantId, 
            userContext.getCurrentTenant().getTenantCode());
        
        // 4. 应用数据范围
        String dataScope = getDataScopeForPermission("PERM_USER_VIEW");
        applyDataScope(wrapper, dataScope);
        
        // 5. 应用字段权限
        if (visibleFields != null && !visibleFields.isEmpty()) {
            wrapper.select(SysUser.class, 
                field -> visibleFields.contains(field.getColumn())
            );
        }
        
        // 6. 执行查询
        List<SysUser> userList = this.list(wrapper);
        
        // 7. 转换VO
        return sysUserConverter.toVOList(userList, visibleFields);
    }
}
```

---

## 🎯 何时应该抽象为工具类

只有在满足以下条件时，才考虑抽象为独立工具类：

1. **真正的复用需求**
   - 至少在 3 个以上的 Service 中使用相同的逻辑
   - 逻辑完全一致，不需要定制

2. **通用性强**
   - 不依赖特定的业务场景
   - 可以适用于所有表/所有实体

3. **逻辑复杂**
   - 实现逻辑超过 50 行代码
   - 包含复杂的算法或计算

4. **便于测试**
   - 需要独立的单元测试
   - 逻辑需要频繁变更

**目前不满足这些条件，所以保持内联实现即可。**

---

## 📚 相关文档

- [实施指南](../specifications/04-implementation-guide.md)
- [字段权限规格](../specifications/01-field-permission-spec.md)
- [数据范围规格](../specifications/02-data-scope-spec.md)

---

**修改完成时间：** 2026-06-12  
**修改类型：** 代码重构（删除工具类，改为内联实现）  
**影响范围：** 仅影响 `SysTenantServiceImpl`，其他代码未使用这些工具类
