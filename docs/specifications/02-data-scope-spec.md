# 数据可见范围实现规格文档

## 1. 概述

### 1.1 功能定义
数据可见范围：根据用户的数据范围权限，动态添加WHERE条件，限制查询结果。

### 1.2 适用场景
- **仅用于查询接口**（不涉及新增、更新、删除）

---

## 2. 数据范围类型

### 2.1 四种数据范围

| 数据范围 | 含义 | SQL条件示例 | 适用场景 |
|---------|------|------------|---------|
| ALL | 全租户数据 | `tenant_id = '101'` | 租户管理员、高级管理者 |
| DEPT_AND_SUB | 本部门及下级部门 | `dept_path LIKE '/301%'` | 部门经理、主管 |
| DEPT | 仅本部门 | `dept_id = '301'` | 部门普通成员 |
| SELF | 仅本人 | `create_by = 2` 或 `user_id = 2` | 普通员工 |

### 2.2 优先级规则

**宽松度排序：** ALL > DEPT_AND_SUB > DEPT > SELF

**合并规则：** 当用户通过多个角色/部门获得同一个权限时，取**最宽松**的数据范围。

**示例：**
```
用户张三：
- 通过"部门经理"角色获得权限A，数据范围 = DEPT_AND_SUB
- 通过"财务部"部门获得权限A，数据范围 = DEPT
- 个人特殊授权权限A，数据范围 = SELF

最终生效：DEPT_AND_SUB（最宽松）
```

---

## 3. 实现方案

### 3.1 方案一：Service层手动拼接（推荐）

#### 3.1.1 核心代码

```java
@Service
public class SysUserServiceImpl implements ISysUserService {
    
    @Override
    public List<SysUserVO> queryUserList(SysUserQueryRTO queryRTO) {
        // 1. 获取数据范围
        String dataScope = getDataScopeForPermission("PERM_USER_VIEW");
        
        // 2. 构建查询条件
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        
        // 基础条件：租户隔离
        UserContextDTO userContext = UserContext.getUserContext();
        wrapper.eq(SysUser::getTenantId, userContext.getCurrentTenant().getTenantCode())
               .eq(SysUser::getIsDeleted, "NOT_DELETED");
        
        // 3. 根据数据范围添加条件
        applyDataScope(wrapper, dataScope);
        
        // 4. 添加业务查询条件
        if (StringUtils.isNotBlank(queryRTO.getUserName())) {
            wrapper.like(SysUser::getUserName, queryRTO.getUserName());
        }
        
        // 5. 执行查询
        List<SysUser> userList = sysUserMapper.selectList(wrapper);
        
        // 6. 字段权限过滤 + 转换VO
        List<String> operableFields = getQueryOperableFields("sys_user");
        return sysUserConverter.toVOList(userList, operableFields);
    }
    
    /**
     * 应用数据范围
     */
    private void applyDataScope(LambdaQueryWrapper<SysUser> wrapper, String dataScope) {
        UserContextDTO userContext = UserContext.getUserContext();
        
        if ("ALL".equals(dataScope)) {
            // 不添加额外条件，可以查看全租户数据
            
        } else if ("DEPT_AND_SUB".equals(dataScope)) {
            // 本部门及下级部门
            String currentDeptPath = userContext.getDepts().getCurrent().get(0).getPath();
            wrapper.likeRight(SysUser::getDeptPath, currentDeptPath);
            
        } else if ("DEPT".equals(dataScope)) {
            // 仅本部门
            String currentDeptId = userContext.getDepts().getCurrent().get(0).getDeptCode();
            wrapper.eq(SysUser::getDeptId, currentDeptId);
            
        } else if ("SELF".equals(dataScope)) {
            // 仅本人创建的或本人的数据
            Long currentUserId = userContext.getUserInfo().getUserId();
            wrapper.eq(SysUser::getId, currentUserId);
        }
    }
    
    /**
     * 获取权限的数据范围（取最宽松的）
     */
    private String getDataScopeForPermission(String permCode) {
        UserContextDTO userContext = UserContext.getUserContext();
        
        // 优先级：ALL > DEPT_AND_SUB > DEPT > SELF
        String maxDataScope = "SELF";  // 默认最严格
        
        // 检查用户的角色数据范围
        for (UserContextDTO.RoleItem role : userContext.getRoles().getValid()) {
            String roleDataScope = role.getDataScope();
            if (isWiderDataScope(roleDataScope, maxDataScope)) {
                maxDataScope = roleDataScope;
            }
        }
        
        // TODO: 还可以检查权限本身的数据范围
        
        return maxDataScope;
    }
    
    /**
     * 比较数据范围宽松度
     */
    private boolean isWiderDataScope(String scope1, String scope2) {
        Map<String, Integer> scopeLevel = Map.of(
            "ALL", 4,
            "DEPT_AND_SUB", 3,
            "DEPT", 2,
            "SELF", 1
        );
        
        return scopeLevel.getOrDefault(scope1, 0) > scopeLevel.getOrDefault(scope2, 0);
    }
}
```

#### 3.1.2 生成的SQL示例

```sql
-- dataScope = "ALL"
SELECT * FROM sys_user 
WHERE tenant_id = '101' 
  AND is_deleted = 'NOT_DELETED';

-- dataScope = "DEPT_AND_SUB"
SELECT * FROM sys_user 
WHERE tenant_id = '101' 
  AND is_deleted = 'NOT_DELETED'
  AND dept_path LIKE '/301%';

-- dataScope = "DEPT"
SELECT * FROM sys_user 
WHERE tenant_id = '101' 
  AND is_deleted = 'NOT_DELETED'
  AND dept_id = '301';

-- dataScope = "SELF"
SELECT * FROM sys_user 
WHERE tenant_id = '101' 
  AND is_deleted = 'NOT_DELETED'
  AND id = 2;
```

---

### 3.2 通用工具类实现

#### 3.2.1 DataScopeHelper

```java
public class DataScopeHelper {
    
    /**
     * 应用数据范围到查询条件
     */
    public static <T> void applyDataScope(
        LambdaQueryWrapper<T> wrapper,
        String permCode,
        Class<T> entityClass) {
        
        // 获取数据范围
        String dataScope = getDataScopeForPermission(permCode);
        
        // 应用条件
        UserContextDTO userContext = UserContext.getUserContext();
        
        if ("ALL".equals(dataScope)) {
            // 全租户，不添加额外条件
            
        } else if ("DEPT_AND_SUB".equals(dataScope)) {
            String deptPath = getCurrentUserDeptPath();
            addDeptAndSubCondition(wrapper, deptPath, entityClass);
            
        } else if ("DEPT".equals(dataScope)) {
            String deptId = getCurrentUserDeptId();
            addDeptCondition(wrapper, deptId, entityClass);
            
        } else if ("SELF".equals(dataScope)) {
            Long userId = getCurrentUserId();
            addSelfCondition(wrapper, userId, entityClass);
        }
    }
    
    /**
     * 获取权限的数据范围
     */
    private static String getDataScopeForPermission(String permCode) {
        UserContextDTO userContext = UserContext.getUserContext();
        
        String maxDataScope = "SELF";
        
        // 遍历角色，找到最宽松的数据范围
        for (UserContextDTO.RoleItem role : userContext.getRoles().getValid()) {
            String roleDataScope = role.getDataScope();
            if (isWiderDataScope(roleDataScope, maxDataScope)) {
                maxDataScope = roleDataScope;
            }
        }
        
        return maxDataScope;
    }
    
    /**
     * 添加"本部门及下级"条件
     */
    private static <T> void addDeptAndSubCondition(
        LambdaQueryWrapper<T> wrapper, 
        String deptPath, 
        Class<T> entityClass) {
        
        try {
            // 通过反射获取 deptPath 字段的 getter
            Method getDeptPath = entityClass.getMethod("getDeptPath");
            
            // 使用 MyBatis-Plus 的 likeRight
            wrapper.likeRight(getDeptPath::invoke, deptPath);
            
        } catch (Exception e) {
            throw new BusinessException("实体类缺少 deptPath 字段");
        }
    }
    
    /**
     * 添加"仅本部门"条件
     */
    private static <T> void addDeptCondition(
        LambdaQueryWrapper<T> wrapper, 
        String deptId, 
        Class<T> entityClass) {
        
        try {
            Method getDeptId = entityClass.getMethod("getDeptId");
            wrapper.eq(getDeptId::invoke, deptId);
        } catch (Exception e) {
            throw new BusinessException("实体类缺少 deptId 字段");
        }
    }
    
    /**
     * 添加"仅本人"条件
     */
    private static <T> void addSelfCondition(
        LambdaQueryWrapper<T> wrapper, 
        Long userId, 
        Class<T> entityClass) {
        
        try {
            // 尝试使用 createBy 字段
            Method getCreateBy = entityClass.getMethod("getCreateBy");
            wrapper.eq(getCreateBy::invoke, userId);
        } catch (NoSuchMethodException e) {
            // 如果没有 createBy，尝试使用 userId 或 id
            try {
                Method getId = entityClass.getMethod("getId");
                wrapper.eq(getId::invoke, userId);
            } catch (Exception ex) {
                throw new BusinessException("实体类缺少 createBy 或 id 字段");
            }
        } catch (Exception e) {
            throw new BusinessException("应用数据范围失败");
        }
    }
    
    private static String getCurrentUserDeptPath() {
        UserContextDTO userContext = UserContext.getUserContext();
        List<UserContextDTO.DeptItem> currentDepts = userContext.getDepts().getCurrent();
        if (currentDepts == null || currentDepts.isEmpty()) {
            throw new BusinessException("当前用户未分配部门");
        }
        return currentDepts.get(0).getPath();
    }
    
    private static String getCurrentUserDeptId() {
        UserContextDTO userContext = UserContext.getUserContext();
        List<UserContextDTO.DeptItem> currentDepts = userContext.getDepts().getCurrent();
        if (currentDepts == null || currentDepts.isEmpty()) {
            throw new BusinessException("当前用户未分配部门");
        }
        return currentDepts.get(0).getDeptCode();
    }
    
    private static Long getCurrentUserId() {
        UserContextDTO userContext = UserContext.getUserContext();
        return userContext.getUserInfo().getUserId();
    }
    
    private static boolean isWiderDataScope(String scope1, String scope2) {
        Map<String, Integer> scopeLevel = Map.of(
            "ALL", 4,
            "DEPT_AND_SUB", 3,
            "DEPT", 2,
            "SELF", 1
        );
        return scopeLevel.getOrDefault(scope1, 0) > scopeLevel.getOrDefault(scope2, 0);
    }
}
```

---

### 3.3 使用示例

```java
@Service
public class SysUserServiceImpl implements ISysUserService {
    
    @Override
    public List<SysUserVO> queryUserList(SysUserQueryRTO queryRTO) {
        // 构建查询条件
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getIsDeleted, "NOT_DELETED");
        
        // 添加租户隔离
        UserContextDTO userContext = UserContext.getUserContext();
        wrapper.eq(SysUser::getTenantId, userContext.getCurrentTenant().getTenantCode());
        
        // 应用数据范围（一行代码搞定！）
        DataScopeHelper.applyDataScope(wrapper, "PERM_USER_VIEW", SysUser.class);
        
        // 执行查询
        List<SysUser> userList = sysUserMapper.selectList(wrapper);
        
        return sysUserConverter.toVOList(userList);
    }
}
```

---

## 4. 完整示例

### 4.1 查询用户列表（包含字段权限+数据范围）

```java
@Override
public List<SysUserVO> queryUserList(SysUserQueryRTO queryRTO) {
    // 1. 构建基础查询条件
    LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(SysUser::getIsDeleted, "NOT_DELETED");
    
    // 2. 添加租户隔离
    UserContextDTO userContext = UserContext.getUserContext();
    wrapper.eq(SysUser::getTenantId, userContext.getCurrentTenant().getTenantCode());
    
    // 3. 应用数据范围
    String dataScope = getDataScopeForPermission("PERM_USER_VIEW");
    applyDataScope(wrapper, dataScope);
    
    // 4. 添加业务查询条件
    if (StringUtils.isNotBlank(queryRTO.getUserName())) {
        wrapper.like(SysUser::getUserName, queryRTO.getUserName());
    }
    if (StringUtils.isNotBlank(queryRTO.getStatus())) {
        wrapper.eq(SysUser::getStatus, queryRTO.getStatus());
    }
    
    // 5. 应用字段权限（动态SELECT）
    List<String> operableFields = getQueryOperableFields("sys_user");
    if (operableFields != null && !operableFields.isEmpty()) {
        wrapper.select(SysUser.class, 
            field -> operableFields.contains(field.getColumn())
        );
    }
    
    // 6. 执行查询
    List<SysUser> userList = sysUserMapper.selectList(wrapper);
    
    // 7. 转换VO（二次字段过滤）
    return sysUserConverter.toVOList(userList, operableFields);
}
```

---

## 5. 数据表设计要求

### 5.1 必需字段

为了支持数据范围控制，实体表必须包含以下字段：

| 字段 | 类型 | 说明 | 必需程度 |
|-----|------|------|---------|
| tenant_id | VARCHAR/BIGINT | 租户ID | 必需（租户隔离）|
| dept_id | VARCHAR/BIGINT | 部门ID | DEPT范围必需 |
| dept_path | VARCHAR | 部门路径 | DEPT_AND_SUB范围必需 |
| create_by | BIGINT | 创建人ID | SELF范围必需 |

### 5.2 索引建议

```sql
-- 租户隔离索引
CREATE INDEX idx_tenant_id ON sys_user(tenant_id);

-- 部门范围索引
CREATE INDEX idx_dept_id ON sys_user(dept_id);
CREATE INDEX idx_dept_path ON sys_user(dept_path);

-- 创建人索引
CREATE INDEX idx_create_by ON sys_user(create_by);

-- 复合索引（常用查询条件）
CREATE INDEX idx_tenant_dept ON sys_user(tenant_id, dept_id);
CREATE INDEX idx_tenant_deleted ON sys_user(tenant_id, is_deleted);
```

---

## 6. 测试用例

### 6.1 测试ALL范围

```java
@Test
public void testQueryWithAllScope() {
    // 模拟用户数据范围：ALL
    mockDataScope("PERM_USER_VIEW", "ALL");
    
    // 查询用户列表
    List<SysUserVO> result = sysUserService.queryUserList(new SysUserQueryRTO());
    
    // 验证：返回全租户数据
    assertTrue(result.size() >= 10);  // 假设租户有10+个用户
}
```

### 6.2 测试DEPT_AND_SUB范围

```java
@Test
public void testQueryWithDeptAndSubScope() {
    // 模拟用户：财务部部门经理
    mockUserDept("DEPT_FINANCE", "/301");
    mockDataScope("PERM_USER_VIEW", "DEPT_AND_SUB");
    
    // 查询用户列表
    List<SysUserVO> result = sysUserService.queryUserList(new SysUserQueryRTO());
    
    // 验证：只返回财务部及下级部门的用户
    for (SysUserVO user : result) {
        assertTrue(user.getDeptPath().startsWith("/301"));
    }
}
```

### 6.3 测试DEPT范围

```java
@Test
public void testQueryWithDeptScope() {
    // 模拟用户：财务部普通成员
    mockUserDept("DEPT_FINANCE", "/301");
    mockDataScope("PERM_USER_VIEW", "DEPT");
    
    // 查询用户列表
    List<SysUserVO> result = sysUserService.queryUserList(new SysUserQueryRTO());
    
    // 验证：只返回本部门用户
    for (SysUserVO user : result) {
        assertEquals("DEPT_FINANCE", user.getDeptId());
    }
}
```

### 6.4 测试SELF范围

```java
@Test
public void testQueryWithSelfScope() {
    // 模拟用户：普通员工
    mockCurrentUser(2L);
    mockDataScope("PERM_USER_VIEW", "SELF");
    
    // 查询用户列表
    List<SysUserVO> result = sysUserService.queryUserList(new SysUserQueryRTO());
    
    // 验证：只返回本人
    assertEquals(1, result.size());
    assertEquals(2L, result.get(0).getId());
}
```

---

## 7. 性能优化

### 7.1 缓存数据范围

```java
// Service中缓存数据范围
private String cachedDataScope;

private String getDataScopeForPermission(String permCode) {
    if (cachedDataScope == null) {
        cachedDataScope = calculateDataScope(permCode);
    }
    return cachedDataScope;
}
```

### 7.2 避免重复查询

```java
// 在同一个请求中，数据范围只需计算一次
@RequestScope  // Spring的请求作用域
public class DataScopeCache {
    private Map<String, String> cache = new HashMap<>();
    
    public String getOrCompute(String permCode, Supplier<String> supplier) {
        return cache.computeIfAbsent(permCode, k -> supplier.get());
    }
}
```

---

## 8. 注意事项

### 8.1 安全性

- ✅ 数据范围是**强制**应用的（用户无法绕过）
- ✅ 租户隔离是**第一优先级**（必须先过滤租户）
- ✅ 默认策略：无配置 = SELF（最严格）

### 8.2 边界情况

- **用户无部门**：DEPT/DEPT_AND_SUB范围会抛出异常
- **跨租户查询**：不允许，租户隔离是强制的
- **多部门用户**：取当前部门（current）

### 8.3 兼容性

- ✅ 只影响查询接口
- ✅ 新增/更新/删除不受影响
- ✅ 向后兼容：老接口无需修改

---

## 9. 实施计划

### 9.1 阶段一：基础设施

- [ ] 创建 DataScopeHelper 工具类
- [ ] 在 UserContextDTO.RoleItem 中添加 dataScope 字段
- [ ] 测试数据范围计算逻辑

### 9.2 阶段二：数据库准备

- [ ] 检查所有实体表是否有必需字段
- [ ] 创建相关索引
- [ ] 数据迁移（如果需要）

### 9.3 阶段三：接口改造

- [ ] 修改所有查询接口，应用数据范围
- [ ] 编写单元测试
- [ ] 编写集成测试

### 9.4 阶段四：性能优化

- [ ] 添加数据范围缓存
- [ ] 优化SQL查询
- [ ] 性能测试
