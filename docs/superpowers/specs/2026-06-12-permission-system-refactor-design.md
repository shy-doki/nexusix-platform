# 多租户权限系统重构技术规格文档

**文档版本**: 1.0  
**创建日期**: 2026-06-12  
**项目**: NexusIX Platform  
**涉及模块**: nexusix-iam, nexusix-tenant, nexusix-org

---

## 一、重构目标

### 1.1 核心目标
1. **数据库模型升级**: 将现有权限模型迁移到新的多租户、多层级权限架构
2. **实体类完整性**: 创建缺失的实体类，修正现有实体类字段
3. **功能增强**: 在 `AuthServiceImpl` 中新增部门信息缓存功能
4. **代码适配**: 修复 `SysTenantServiceImpl` 以适配新表结构

### 1.2 约束条件
- ✅ 保持现有模块依赖关系不变
- ✅ 使用 MyBatis XML 进行跨模块查询
- ✅ 保持原有业务逻辑不变
- ✅ 遵循现有代码风格（参考 `SysTenant.java`）

---

## 二、数据库架构概览

### 2.1 新架构核心表（8张）

| 表名 | 说明 | 所属模块 |
|------|------|---------|
| `sys_tenant` | 租户表 | nexusix-tenant |
| `sys_tenant_policy` | 租户策略表（部门/角色→租户绑定） | nexusix-tenant |
| `sys_user` | 用户表 | nexusix-iam |
| `sys_role` | 角色表 | nexusix-iam |
| `sys_perm` | 权限表 | nexusix-iam |
| `sys_user_policy` | 用户策略表 | nexusix-iam |
| `sys_perm_policy` | 权限策略表 | nexusix-iam |
| `sys_dept` | 部门表 | nexusix-org |

### 2.2 核心设计原理

#### **两层ID体系**
- **系统级ID**: 全局唯一（如系统部门ID、系统角色ID）
- **租户级ID**: 策略表记录ID（如租户部门ID = `sys_tenant_policy.id`）

#### **绑定逻辑**
```
1. 系统部门/角色 → 租户 (sys_tenant_policy, 生成租户级ID)
2. 系统用户 → 系统租户 (sys_user_policy, 生成租户用户ID)
3. 租户用户ID → 租户部门ID/租户角色ID (sys_user_policy)
```

---

## 三、实体类创建清单

### 3.1 需要创建的新实体类（6个）

#### **nexusix-iam 模块（5个）**
1. `SysUser.java` - 用户表
2. `SysRole.java` - 角色表
3. `SysPerm.java` - 权限表
4. `SysUserPolicy.java` - 用户策略表
5. `SysPermPolicy.java` - 权限策略表

**实体类风格要求**：
- 参考 `SysTenant.java` 的编写风格
- 使用 MyBatis-Plus 注解
- 使用 Lombok `@Data`, `@EqualsAndHashCode`, `@Accessors(chain = true)`
- 使用 Swagger `@Schema` 注解
- 日期字段使用 `@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")`

#### **nexusix-tenant 模块（1个）**
6. `SysTenantPolicy.java` - 租户策略表

### 3.2 需要修正的现有实体类

#### **SysTenant.java - 新增字段**
```java
// 联系信息
private String contactName;
private String contactPhone;
private String contactEmail;

// 服务配置
private LocalDateTime expireTime;
private Long packageId;
private Integer maxUsers;

// 扩展属性
private String extAttributes;

// 审计字段补充
private Long createTenant;
private Long createDept;
private Long createRole;
```

#### **SysDept.java - 移除字段**
- ❌ 删除 `deptType` (dept_type)
- ❌ 删除 `leaderId` (leader_id)

---

## 四、UserContextDTO 缓存结构设计

### 4.1 新增部门缓存结构

```java
/** 部门分组（当前租户/全部/有效/无效） */
private DeptGroup depts;

@Data
public static class DeptItem {
    private String deptCode;
    private String deptName;
    private String path;
    private Integer level;
    private String tenantCode;
    private String tenantName;
    private Boolean isPrimary;
    private String userPolicyStatus;
}

@Data
public static class DeptGroup {
    private List<DeptItem> current = new ArrayList<>();  // 当前租户部门
    private List<DeptItem> all = new ArrayList<>();      // 全部部门
    private List<DeptItem> valid = new ArrayList<>();    // 有效部门
    private List<DeptItem> invalid = new ArrayList<>();  // 无效部门
}
```

---

## 五、AuthServiceImpl 重构设计

### 5.1 新增 DTO

**UserDeptDTO.java**
```java
@Data
public class UserDeptDTO {
    private String deptCode;
    private String deptName;
    private String path;
    private Integer level;
    private String tenantCode;
    private String tenantName;
    private Boolean isPrimary;
    private String userPolicyStatus;
    private Long tenantUserId;
}
```

### 5.2 Mapper 新增方法

**SysUserPolicyMapper.java**
```java
/**
 * 查询用户在所有租户下的部门信息
 */
List<UserDeptDTO> queryUserAllDeptInfo(@Param("userId") Long userId);
```

### 5.3 核心 XML 查询

**SysUserPolicyMapper.xml**
```xml
<select id="queryUserAllDeptInfo" resultType="com.shy.nexusix.iam.dto.UserDeptDTO">
    SELECT
        d.dept_code AS deptCode,
        d.dept_name AS deptName,
        d.path AS path,
        d.level AS level,
        t.tenant_code AS tenantCode,
        t.tenant_name AS tenantName,
        up_dept.is_primary AS isPrimary,
        up_dept.status AS userPolicyStatus,
        up_tenant.id AS tenantUserId
    FROM sys_user_policy up_tenant
    INNER JOIN sys_tenant t ON t.id = up_tenant.target_id
        AND up_tenant.target_type = 'TENANT'
        AND t.is_deleted = 'NOT_DELETED'
    LEFT JOIN sys_user_policy up_dept ON up_dept.user_id = up_tenant.id
        AND up_dept.target_type = 'DEPT'
        AND up_dept.is_deleted = 'NOT_DELETED'
    LEFT JOIN sys_tenant_policy tp ON tp.id = up_dept.target_id
        AND tp.source_type = 'DEPT'
        AND tp.is_deleted = 'NOT_DELETED'
    LEFT JOIN sys_dept d ON d.id = tp.source_id
        AND d.is_deleted = 'NOT_DELETED'
    WHERE up_tenant.user_id = #{userId}
      AND up_tenant.is_deleted = 'NOT_DELETED'
    ORDER BY t.tenant_code, d.dept_code
</select>
```

### 5.4 登录逻辑（伪代码）

```java
@Override
public ApiResponse login(LoginRTO param) {
    // 1. 前置校验（用户名密码、租户有效性）
    
    // 2. 执行登录
    StpUtil.login(user.getId());
    
    // 3. 查询权限信息
    List<UserPermJoinDTO> permList = ...;
    
    // 4. 查询角色信息
    List<UserRoleDTO> roleList = ...;
    
    // 5. 查询部门信息（新增）
    List<UserDeptDTO> deptList = sysUserPolicyMapper.queryUserAllDeptInfo(user.getId());
    
    // 6. 构建 UserContextDTO
    UserContextDTO userContext = new UserContextDTO();
    userContext.setCurrentTenant(...);
    userContext.setTenants(buildTenantGroup(...));
    userContext.setPermissions(buildPermissionInfo(...));
    userContext.setRoles(buildRoleGroup(...));
    userContext.setDepts(buildDeptGroup(deptList, currentTenantCode)); // 新增
    
    // 7. 存入 Session
    StpUtil.getSession().set(GlobalConstant.Session.USER_CONTEXT, userContext);
    
    return ApiResponse.success("登录成功", userContext);
}

private UserContextDTO.DeptGroup buildDeptGroup(List<UserDeptDTO> deptList, String currentTenantCode) {
    UserContextDTO.DeptGroup deptGroup = new UserContextDTO.DeptGroup();
    
    // 过滤有效部门
    List<UserDeptDTO> validDepts = deptList.stream()
        .filter(d -> d.getDeptCode() != null)
        .collect(Collectors.toList());
    
    // 当前租户部门
    deptGroup.setCurrent(validDepts.stream()
        .filter(d -> currentTenantCode.equals(d.getTenantCode()))
        .map(this::convertToDeptItem)
        .collect(Collectors.toList()));
    
    // 全部部门
    deptGroup.setAll(validDepts.stream().map(this::convertToDeptItem).collect(Collectors.toList()));
    
    // 有效部门
    deptGroup.setValid(validDepts.stream()
        .filter(d -> "ACTIVE".equals(d.getUserPolicyStatus()))
        .map(this::convertToDeptItem)
        .collect(Collectors.toList()));
    
    // 无效部门
    deptGroup.setInvalid(validDepts.stream()
        .filter(d -> !"ACTIVE".equals(d.getUserPolicyStatus()))
        .map(this::convertToDeptItem)
        .collect(Collectors.toList()));
    
    return deptGroup;
}
```

---

## 六、SysTenantServiceImpl 适配修复

### 6.1 修复原则

**仅适配字段变更，不改变业务逻辑**

### 6.2 需要修改的地方

1. **查询方法**: 确保新增字段被正确查询和返回
2. **更新方法**: 处理新增字段的更新
3. **创建方法**: 设置新增字段的初始值
4. **Converter**: 更新字段映射

---

## 七、Mapper 接口创建清单

### 7.1 nexusix-iam 模块
- `SysUserMapper.java`
- `SysRoleMapper.java`
- `SysPermMapper.java`
- `SysUserPolicyMapper.java`
- `SysPermPolicyMapper.java`

### 7.2 nexusix-tenant 模块
- `SysTenantPolicyMapper.java`

所有 Mapper 继承 `BaseMapper<T>` 并添加 `@Mapper` 注解。

---

## 八、VO/RTO 适配

### 8.1 需要更新的类

- `SysTenantDetailVO` - 新增字段
- `SysTenantAddRTO` - 新增字段
- `SysTenantUpdateRTO` - 新增字段
- `SysTenantConverter` - 更新字段映射

---

## 九、实施步骤

### 阶段1：实体类创建与修正（2-3小时）
1. 创建 6 个新实体类
2. 修正 `SysTenant` 和 `SysDept`
3. 创建对应 Mapper 接口

### 阶段2：DTO 和缓存结构（1-2小时）
1. 创建 `UserDeptDTO`
2. 修改 `UserContextDTO`
3. 更新相关 VO/RTO

### 阶段3：AuthServiceImpl 重构（3-4小时）
1. 创建 XML 查询
2. 实现部门缓存逻辑
3. 集成测试

### 阶段4：SysTenantServiceImpl 适配（2-3小时）
1. 适配新增字段
2. 测试原有功能

### 阶段5：测试验证（2-3小时）
1. 单元测试
2. 集成测试
3. 数据一致性验证

---

## 十、验证检查清单

- [ ] 所有新实体类字段与 SQL 表结构完全一致
- [ ] `SysTenant` 新增字段在所有 CRUD 方法中正确处理
- [ ] `SysDept` 不再引用已删除字段
- [ ] `UserContextDTO` 正确缓存部门信息
- [ ] 登录时能查询到用户所有租户的部门
- [ ] 部门按当前租户/全部/有效/无效正确分组
- [ ] `SysTenantServiceImpl` 原有功能不受影响
- [ ] 跨模块查询通过 XML 实现，未修改模块依赖

---

## 十一、风险与注意事项

### 11.1 数据迁移
- ⚠️ 需要执行 `new_permission_design.sql` 进行结构升级
- ⚠️ 旧数据需要迁移到新表结构

### 11.2 向后兼容
- ⚠️ `SysDeptPolicy` 被删除，依赖该表的代码需要重构

### 11.3 性能考虑
- 💡 跨表查询建议添加适当索引
- 💡 注意 Session 存储容量

---

**文档编写完成！**

下一步：调用 writing-plans 技能创建实施计划。
