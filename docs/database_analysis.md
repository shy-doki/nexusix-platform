# NexusIX 平台数据库表结构分析与优化建议

## 一、整体架构概览

### 1.1 权限体系设计
当前系统采用**四级权限策略**：系统 > 租户 > 角色 > 用户  
当前系统采用**三级角色层级**：系统级 > 租户级 > 用户级

### 1.2 核心对比
| 维度 | 权限管理 | 角色管理 |
|------|---------|---------|
| 策略表 | `sys_permission_policy` ✓ | **缺失** ✗ |
| 层级数 | 4级 | 3级 |
| 级联禁用 | 通过策略表 `action` + `inheritance_enabled` 实现 | **无实现** |
| 缓存策略 | 按层级分别缓存 | 部分实现 |

---

## 二、sys_role 表深入分析

### 2.1 现有字段评估

```sql
-- 当前 sys_role 表结构 (L363-L397)
id                  BIGINT          NOT NULL,
role_name           VARCHAR(50)     NOT NULL,
role_code           VARCHAR(50)     NOT NULL,
role_level          SMALLINT        DEFAULT 2,
tenant_id           BIGINT          DEFAULT 0,
tenant_name         VARCHAR(100)    NOT NULL,  -- 冗余字段
data_scope          SMALLINT        DEFAULT 1,
status              SMALLINT        DEFAULT 1,
-- 审计字段...
```

**设计优点**:
- ✓ 角色层级清晰 (1-系统 2-租户 3-用户)
- ✓ 包含数据范围控制字段 (data_scope)
- ✓ 完整的审计追踪字段 (create_by, update_by等)

**存在问题**:

| 序号 | 问题 | 影响 | 优先级 |
|------|------|------|--------|
| 1 | **缺少 `sort_order` 字段** | 无法控制角色在UI中的展示顺序 | 高 |
| 2 | **缺少 `description` / `remark` 字段** | 无法记录角色用途说明，不利于运维 | 高 |
| 3 | **缺少级联禁用控制字段** | 无法实现"禁用系统角色影响所有租户" | **严重** |
| 4 | **缺少角色策略关联** | 与权限管理体系不一致 | **严重** |
| 5 | `tenant_name` 冗余 | 需要额外同步逻辑，存在数据一致性风险 | 中 |

### 2.2 缺失字段详细分析

#### ① `sort_order` 字段
```sql
sort_order INT DEFAULT 0,
```
**必要性**:
- 其他表 (`sys_menu`, `sys_dept`, `sys_post`) 均有此字段
- 前端角色列表需要可配置展示顺序
- 保持系统一致性

#### ② `description` 字段
```sql
description VARCHAR(500) DEFAULT '',
```
**必要性**:
- 便于管理员理解每个角色的职责范围
- 审计时能快速识别角色用途
- 系统级角色尤其需要描述说明

#### ③ 级联禁用控制 (核心问题)
**当前困境**:
- 权限策略表通过 `action`(1-允许 2-拒绝) + `inheritance_enabled`(是否向下继承) 实现级联禁用
- 角色表只有 `status` 字段，无法区分"角色本身禁用"和"被上层级联禁用"
- 上层角色禁用时，无法控制是否影响下层角色

---

## 三、核心问题：缺少角色策略表

### 3.1 权限策略表设计模式
```sql
sys_permission_policy (
    target_type    SMALLINT,  -- 1-系统 2-租户 3-角色 4-用户
    target_id      BIGINT,
    permission_id  BIGINT,
    action         SMALLINT,  -- 1-允许 2-拒绝
    inheritance_enabled BOOLEAN DEFAULT TRUE,  -- 关键：是否向下继承
    priority       INT DEFAULT 0,
    ...
)
```

**设计理念**: 将"谁对什么资源有什么操作"从实体表中剥离，实现灵活的策略控制

### 3.2 角色管理现状
```
问题场景:
- 系统管理员禁用"系统级角色A"
- 租户下所有用户如果关联了角色A，应该怎样处理？
- 当前只有 status 字段，无法表达：
  - 是角色本身被禁用？
  - 还是被上级角色禁用？
  - 是否级联影响到下级？
```

### 3.3 建议：新增 `sys_role_policy` 表

```sql
CREATE TABLE sys_role_policy (
    id              BIGINT          NOT NULL,
    target_type     SMALLINT        NOT NULL,     -- 目标类型 (1-系统 2-租户 3-用户)
    target_id       BIGINT          NOT NULL,     -- 目标 ID
    target_name     VARCHAR(100)    NOT NULL,     -- 目标名称
    role_id         BIGINT          NOT NULL,     -- 关联角色 ID
    role_name       VARCHAR(50)     NOT NULL,     -- 关联角色名称
    action          SMALLINT        NOT NULL,     -- 动作 (1-允许 2-拒绝/禁用)
    priority        INT             DEFAULT 0,    -- 优先级
    inheritance_enabled BOOLEAN     DEFAULT TRUE, -- 是否向下继承
    create_by       BIGINT          DEFAULT 0,
    create_by_name  VARCHAR(100)    DEFAULT NULL,
    create_time     TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    update_by       BIGINT          DEFAULT 0,
    update_by_name  VARCHAR(100)    DEFAULT NULL,
    update_time     TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    is_deleted      SMALLINT        DEFAULT 0,
    PRIMARY KEY (id)
);
COMMENT ON TABLE sys_role_policy IS '角色策略控制表 - 实现角色级联禁用及策略继承';
COMMENT ON COLUMN sys_role_policy.target_type IS '目标类型 (1-系统 2-租户 3-用户)';
COMMENT ON COLUMN sys_role_policy.target_id IS '目标 ID (对应租户/用户 ID)';
COMMENT ON COLUMN sys_role_policy.role_id IS '关联角色 ID';
COMMENT ON COLUMN sys_role_policy.action IS '动作 (1-允许 2-拒绝)';
COMMENT ON COLUMN sys_role_policy.priority IS '优先级 (数字越大优先级越高)';
COMMENT ON COLUMN sys_role_policy.inheritance_enabled IS '是否向下继承';
```

**字段对齐权限策略表**:
- `target_type`: 3个层级(系统/租户/用户)，角色没有"角色级"策略
- `action`: 1-允许角色分配 2-拒绝角色分配
- `inheritance_enabled`: 禁用时是否级联影响下层

---

## 四、级联禁用功能实现方案

### 4.1 方案对比

| 方案 | 优点 | 缺点 | 推荐度 |
|------|------|------|--------|
| 方案A: 角色策略表 | 与权限设计一致，灵活度高 | 新增表，复杂度增加 | ★★★★★ |
| 方案B: 增加字段 | 简单直接 | 扩展性差 | ★★★ |
| 方案C: 仅修改状态字段 | 改动最小 | 无法实现复杂场景 | ★★ |

### 4.2 推荐方案A：角色策略表

**核心实现逻辑**:

```
角色获取流程 (StpInterfaceImpl.getRoleList):

1. 系统级角色查询
   ↓
2. 检查 sys_role_policy 表：
   - 系统级是否有拒绝策略？
   - 租户级是否有拒绝策略？
   - 用户级是否有拒绝策略？
   ↓
3. 合并结果，排除被拒绝的角色
```

**代码实现示例**:

```java
// 在 StpInterfaceImpl.getRoleList() 中增加策略检查
public List<String> getRoleList(Object loginId, String loginType) {
    Long userId = Long.parseLong(loginId.toString());
    Long tenantId = TenantContext.getCurrentTenantId();
    
    Set<String> roleCodeSet = new HashSet<>();
    
    // 1. 查询系统级角色
    List<SysRole> systemRoles = querySystemRoles();
    // 2. 查询租户级角色
    List<SysRole> tenantRoles = queryTenantRoles(tenantId, userId);
    // 3. 查询用户级角色
    List<SysRole> userRoles = queryUserRoles(userId);
    
    // 4. 检查角色策略 - 关键新增逻辑
    Set<String> deniedRoleIds = getDeniedRoleIds(userId, tenantId);
    
    // 5. 过滤被拒绝的角色
    mergeAndFilterRoles(roleCodeSet, systemRoles, tenantRoles, userRoles, deniedRoleIds);
    
    return new ArrayList<>(roleCodeSet);
}

/**
 * 获取被拒绝的角色ID集合 (按优先级: 系统 > 租户 > 用户)
 */
private Set<String> getDeniedRoleIds(Long userId, Long tenantId) {
    // 查询系统级拒绝策略
    // 查询租户级拒绝策略
    // 查询用户级拒绝策略
    // 合并并返回
}
```

### 4.3 缓存策略对齐

参考权限策略缓存模式:

```java
// 角色策略缓存键设计
GlobalConstant.RedisKey.ROLE_POLICY_SYSTEM_DENY = "NexusIX:role:policy:system:deny"
GlobalConstant.RedisKey.ROLE_POLICY_TENANT_DENY_PREFIX = "NexusIX:role:policy:tenant:{tenantId}:deny"
GlobalConstant.RedisKey.ROLE_POLICY_USER_DENY_PREFIX = "NexusIX:role:policy:user:{userId}:deny"
```

---

## 五、其他表结构问题

### 5.1 `sys_user_role_rel` 表
**现状**: 缺少有效期控制字段

**建议**: 
```sql
-- 新增字段
effective_time TIMESTAMP DEFAULT NULL,  -- 生效时间
expire_time    TIMESTAMP DEFAULT NULL,  -- 失效时间
```

**原因**: 支持临时角色、试用期角色等场景

### 5.2 `sys_dept` 表
**现状**: 缺少 `leader_name` 冗余字段

**建议**:
```sql
leader_name VARCHAR(100) DEFAULT NULL,
```

**原因**: 与 `sys_tenant`, `sys_user` 等表保持一致的冗余字段规范

### 5.3 `sys_menu` 表
**现状**: 
- 缺少 `tenant_id` 字段 (是否支持租户级菜单？)
- 缺少审计字段 (`create_by`, `update_by`)

**建议**: 如果菜单是系统级统一管理，保持现状即可；如需租户级定制，则需增加字段

### 5.4 `sys_user_tenant_rel` 表
**现状**: 缺少 `is_primary` 或 `main_tenant` 标识

**当前设计**: 使用 `is_default` 字段
**问题**: `is_default` 含义不够明确

**建议**: 保持现状但增加注释明确说明

---

## 六、StpInterfaceImpl 实现问题

### 6.1 代码不完整
**当前文件**: `StpInterfaceImpl.java` 第106-123行存在未完成的代码:

```java
LambdaQueryWrapper<SysPermissionPolicy> permPolicyWrapper = new LambdaQueryWrapper<SysPermissionPolicy>()
        .eq(SysPermissionPolicy::getTargetId, tenantId)
        .eq(SysPermissionPolicy::getTargetType, GlobalEnum.PermType)

// 如果存在权限策略，则查询对应的权限详情
if () {  // ← 条件缺失
    
} else {
    // 无权限策略时，缓存空列表避免缓存穿透
    
}
```

**需要**: 
- 补充条件判断逻辑
- 完善缓存读取和写入逻辑
- 参考下方 `getRoleList()` 方法的实现模式

### 6.2 缓存键不一致
**问题**: 
```java
// 当前使用
StpUtil.getSession().get(GlobalConstant.RedisKey.PERM_SYSTEM_VALID);

// 但 RedisKey 定义是
PERM_SYSTEM_VALID = "perm:system:valid"  // 无前缀
ROLE_SYSTEM_VALID = "NexusIX:role:system:valid"  // 有前缀
```

**建议**: 统一缓存键命名规范，所有键都应包含 `NexusIX` 前缀

---

## 七、优化方案汇总

### 7.1 数据库变更清单

| 序号 | 变更类型 | 表名 | 变更内容 | 优先级 |
|------|---------|------|---------|--------|
| 1 | **新增表** | `sys_role_policy` | 角色策略控制表 | **P0** |
| 2 | 新增字段 | `sys_role` | `sort_order`, `description` | P1 |
| 3 | 新增字段 | `sys_user_role_rel` | `effective_time`, `expire_time` | P2 |
| 4 | 修改注释 | `GlobalConstant.RedisKey` | 统一缓存键前缀 | P1 |

### 7.2 代码变更清单

| 序号 | 变更文件 | 变更内容 |
|------|---------|---------|
| 1 | `StpInterfaceImpl.java` | 完善 `getPermissionList()` 方法 |
| 2 | `StpInterfaceImpl.java` | 增加角色策略检查逻辑 |
| 3 | 新增 `ISysRolePolicyService` | 角色策略服务接口 |
| 4 | 新增 `SysRolePolicyServiceImpl` | 角色策略服务实现 |
| 5 | `GlobalConstant.java` | 统一缓存键定义 |
| 6 | 新增 `GlobalEnum.TargetType` 扩展 | 支持角色策略目标类型 |

### 7.3 实施步骤建议

**第一阶段 (核心功能)**:
1. 创建 `sys_role_policy` 表
2. 实现基础的角色策略CRUD接口
3. 修改 `StpInterfaceImpl.getRoleList()` 增加策略过滤

**第二阶段 (完善功能)**:
4. 补充 `sys_role` 缺失字段
5. 完善缓存策略
6. 修复 `StpInterfaceImpl.getPermissionList()`

**第三阶段 (扩展功能)**:
7. 增加用户角色关联有效期控制
8. 实现角色策略批量操作
9. 完善级联禁用的前端交互

---

## 八、架构设计建议

### 8.1 权限与角色设计一致性原则

当前系统存在**设计不对称**问题:

```
权限管理: sys_permission → sys_permission_policy (策略表) ✓
角色管理: sys_role → ??? (缺失策略表) ✗
```

**建议原则**:
- 权限和角色应使用相同的策略管理模式
- 级联禁用逻辑应在两个体系中保持一致
- 缓存策略应统一设计

### 8.2 未来扩展考虑

如果后续需要支持:
- **角色组**: 多个角色组合成一个角色组
- **角色互斥**: 某些角色不能同时分配给同一用户
- **角色依赖**: 分配角色A前必须先分配角色B

这些都需要在策略表中扩展，而非在主表中增加字段。

---

## 九、总结

### 9.1 核心结论

1. **角色表缺少策略机制**是最大问题，导致无法实现级联禁用
2. **建议新增 `sys_role_policy` 表**，与权限策略表保持设计一致性
3. **补充缺失字段** (`sort_order`, `description`) 提升可用性
4. **修复代码实现** (`StpInterfaceImpl` 未完成逻辑)

### 9.2 风险评估

| 风险项 | 影响 | 缓解措施 |
|--------|------|---------|
| 现有数据迁移 | 低 | 策略表新增不影响现有数据 |
| 接口兼容性 | 中 | 新增接口，不修改现有接口 |
| 性能影响 | 低 | 通过缓存策略优化 |
| 开发工作量 | 中 | 预计2-3天可完成核心功能 |

### 9.3 下一步行动

1. **确认方案**: 评审本分析报告，确认是否采用方案A
2. **编写SQL**: 创建 `sys_role_policy` 建表脚本
3. **生成代码**: 使用代码生成器创建 Entity/Service/Mapper
4. **实现逻辑**: 修改 `StpInterfaceImpl` 增加策略过滤
5. **测试验证**: 编写单元测试和集成测试
