# NexusIX Platform 权限系统与性能优化 - 总体规格文档

## 文档目录

本文档集包含以下规格文档：

1. [字段级权限实现规格](./01-field-permission-spec.md)
2. [数据可见范围实现规格](./02-data-scope-spec.md)
3. [租户树服务优化规格](./03-tenant-tree-optimization-spec.md)
4. 本文档（总体架构与实施指南）

---

## 1. 系统架构总览

### 1.1 多租户权限系统架构

```
┌─────────────────────────────────────────────────────────┐
│                        前端层                            │
│            React / Vue / Angular                        │
└─────────────────┬───────────────────────────────────────┘
                  │ HTTP/JSON
                  ↓
┌─────────────────────────────────────────────────────────┐
│                    Controller层                          │
│  - 接收请求                                              │
│  - 字段权限校验（新增/更新）                              │
│  - 调用Service                                          │
└─────────────────┬───────────────────────────────────────┘
                  │
                  ↓
┌─────────────────────────────────────────────────────────┐
│                    Service层                             │
│  - 获取UserContext（Session）                           │
│  - 应用数据范围（ALL/DEPT_AND_SUB/DEPT/SELF）            │
│  - 应用字段权限（动态SELECT / MapStruct过滤）             │
│  - 业务逻辑处理                                          │
│  - 树形结构构建（内存构建，避免N+1）                      │
└─────────────────┬───────────────────────────────────────┘
                  │
                  ↓
┌─────────────────────────────────────────────────────────┐
│                    Mapper层                              │
│  - MyBatis-Plus                                         │
│  - 动态SQL生成                                           │
└─────────────────┬───────────────────────────────────────┘
                  │
                  ↓
┌─────────────────────────────────────────────────────────┐
│                    数据库层                              │
│  - PostgreSQL                                           │
│  - 索引优化（path、level、dept_path）                    │
│  - 租户隔离                                              │
└─────────────────────────────────────────────────────────┘
```

---

## 2. 核心设计理念

### 2.1 两层ID体系

**系统级ID（全局唯一）**
- 系统用户、系统租户、系统部门、系统角色、系统权限
- 跨租户复用的模板定义

**租户级ID（租户内唯一）**
- 租户用户、租户部门、租户角色、租户权限
- 通过 `sys_tenant_policy` 和 `sys_user_policy` 映射
- 实现租户隔离

**转换关系：**
```
系统用户 2（张三）
  ↓ sys_user_policy
  ├─→ 租户用户 4001（张三在万象集团）
  └─→ 租户用户 4010（张三在鼎新集团）

系统权限 511（用户管理）
  ↓ sys_perm_policy (target_type='TENANT')
  ├─→ 租户权限 5001（万象集团的权限池）
  └─→ 租户权限 5008（鼎新集团的权限池）
```

---

### 2.2 权限授予三层模型

```
第一层：系统权限 → 租户（建立权限池）
  作用：定义租户的权限边界
  记录：sys_perm_policy (target_type='TENANT')
  
第二层：租户权限 → 角色/部门（实际分配）
  作用：从权限池中取出权限，分配给角色/部门
  记录：sys_perm_policy (target_type='ROLE'/'DEPT')
  
第三层：租户权限 → 用户（特殊授权）
  作用：直接授予用户个人的特殊权限
  记录：sys_perm_policy (target_type='USER')
```

**用户最终权限公式：**
```
用户权限 = 
  授予给【用户所拥有的所有角色】的【租户权限】∪
  授予给【用户所属的所有部门】的【租户权限】∪
  授予给【用户个人】的【租户权限】

约束：租户权限 ⊆ 租户权限池
```

---

### 2.3 权限控制维度

| 维度 | 控制内容 | 实现方式 |
|-----|---------|---------|
| **权限编码** | 用户有哪些权限 | Session中的 permissions.valid 列表 |
| **数据范围** | 可以看到哪些数据（行级） | WHERE条件（ALL/DEPT_AND_SUB/DEPT/SELF）|
| **字段权限** | 可以操作哪些字段（列级）| SELECT动态字段 / MapStruct过滤 |
| **操作类型** | 可以进行哪些操作 | query/create/update 分组 |

---

## 3. 登录流程

### 3.1 完整流程

```
1. 验证用户身份（sys_user）
   ↓
2. 查询用户的租户（sys_user_policy → sys_tenant）
   - 得到租户列表 + 租户用户ID
   - 选择当前登录租户（主租户优先）
   ↓
3. 查询用户的部门（四表关联）
   - sys_user_policy（租户用户ID）
   - → sys_user_policy（部门绑定）
   - → sys_tenant_policy（租户部门ID → 系统部门ID）
   - → sys_dept（部门详情）
   ↓
4. 查询用户的角色（四表关联）
   - sys_user_policy（租户用户ID）
   - → sys_user_policy（角色绑定）
   - → sys_tenant_policy（租户角色ID → 系统角色ID）
   - → sys_role（角色详情）
   ↓
5. 查询用户的权限（三个UNION）
   - 用户个人权限（target_type='USER'）
   - + 角色权限（target_type='ROLE'）
   - + 部门权限（target_type='DEPT'）
   - ❌ 不查询租户权限（target_type='TENANT'是权限池）
   ↓
6. 权限处理
   - 权限编码去重
   - 状态分类（ACTIVE → valid, DISABLED → invalid）
   - 字段权限解析（JSON → operable/inoperable）
   - 按操作类型分组（query/create/update）
   - 按表名分组（sys_user/biz_order/...）
   ↓
7. 数据分组
   - 租户：current/all/valid/invalid
   - 部门：current/all/valid/invalid（带tenantCode）
   - 角色：current/all/valid/invalid（带tenantCode）
   - 权限：all/valid/invalid + fieldPermission
   ↓
8. 组装 UserContextDTO → 存入 Session
   ↓
登录完成
```

### 3.2 UserContextDTO 结构

```json
{
  "userInfo": { "userId": 2, "userName": "zhangsan" },
  "currentTenant": { "tenantCode": "WX001" },
  "tenants": { "all": [...], "valid": [...], "invalid": [] },
  "depts": { "current": [...], "all": [...], "valid": [...], "invalid": [] },
  "roles": { "current": [...], "all": [...], "valid": [...], "invalid": [] },
  "permissions": {
    "all": ["PERM_USER_VIEW", "PERM_USER_CREATE", ...],
    "valid": [...],
    "invalid": [...],
    "fieldPermission": {
      "query": {
        "sys_user": {
          "operable": ["user_name", "email"],
          "inoperable": ["password"]
        }
      },
      "create": { ... },
      "update": { ... }
    }
  }
}
```

---

## 4. 查询接口完整实现

### 4.1 标准查询接口模板

```java
@Override
public List<SysUserVO> queryUserList(SysUserQueryRTO queryRTO) {
    // ============ 第1步：构建基础查询条件 ============
    LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(SysUser::getIsDeleted, "NOT_DELETED");
    
    // ============ 第2步：租户隔离（强制） ============
    UserContextDTO userContext = UserContext.getUserContext();
    wrapper.eq(SysUser::getTenantId, 
        userContext.getCurrentTenant().getTenantCode());
    
    // ============ 第3步：应用数据范围 ============
    String dataScope = getDataScopeForPermission("PERM_USER_VIEW");
    applyDataScope(wrapper, dataScope);
    
    // ============ 第4步：添加业务查询条件 ============
    if (StringUtils.isNotBlank(queryRTO.getUserName())) {
        wrapper.like(SysUser::getUserName, queryRTO.getUserName());
    }
    if (StringUtils.isNotBlank(queryRTO.getStatus())) {
        wrapper.eq(SysUser::getStatus, queryRTO.getStatus());
    }
    
    // ============ 第5步：应用字段权限（动态SELECT） ============
    List<String> operableFields = getQueryOperableFields("sys_user");
    if (operableFields != null && !operableFields.isEmpty()) {
        wrapper.select(SysUser.class, 
            field -> operableFields.contains(field.getColumn())
        );
    }
    
    // ============ 第6步：执行查询 ============
    List<SysUser> userList = sysUserMapper.selectList(wrapper);
    
    // ============ 第7步：转换VO（二次字段过滤） ============
    return sysUserConverter.toVOList(userList, operableFields);
}

// 应用数据范围
private void applyDataScope(LambdaQueryWrapper<SysUser> wrapper, String dataScope) {
    UserContextDTO userContext = UserContext.getUserContext();
    
    if ("ALL".equals(dataScope)) {
        // 不添加额外条件
    } else if ("DEPT_AND_SUB".equals(dataScope)) {
        String deptPath = userContext.getDepts().getCurrent().get(0).getPath();
        wrapper.likeRight(SysUser::getDeptPath, deptPath);
    } else if ("DEPT".equals(dataScope)) {
        String deptId = userContext.getDepts().getCurrent().get(0).getDeptCode();
        wrapper.eq(SysUser::getDeptId, deptId);
    } else if ("SELF".equals(dataScope)) {
        Long userId = userContext.getUserInfo().getUserId();
        wrapper.eq(SysUser::getId, userId);
    }
}

// 获取字段权限
private List<String> getQueryOperableFields(String tableName) {
    UserContextDTO userContext = UserContext.getUserContext();
    UserContextDTO.TableFieldPermission tableFieldPerm = 
        userContext.getPermissions()
        .getFieldPermission()
        .getQuery()
        .get(tableName);
    
    return tableFieldPerm != null ? tableFieldPerm.getOperable() : null;
}

// 获取数据范围
private String getDataScopeForPermission(String permCode) {
    UserContextDTO userContext = UserContext.getUserContext();
    
    String maxDataScope = "SELF";
    for (UserContextDTO.RoleItem role : userContext.getRoles().getValid()) {
        if (isWiderDataScope(role.getDataScope(), maxDataScope)) {
            maxDataScope = role.getDataScope();
        }
    }
    
    return maxDataScope;
}
```

---

## 5. 性能优化总结

### 5.1 优化成果

| 优化项 | 优化前 | 优化后 | 性能提升 |
|-------|--------|--------|---------|
| 租户树查询（1000节点）| 1001次SQL | 1次SQL | **1000倍** |
| 租户子树查询（100节点）| 101次SQL | 2次SQL | **50倍** |
| 租户树分页（10根×100子）| 1011次SQL | 2次SQL | **500倍** |
| 字段权限过滤 | 反射遍历 | MapStruct编译期 | **10倍** |
| 删除租户检查 | 2次SQL | 1次SQL（hasChildren）| 2倍 |

### 5.2 核心优化技术

1. **避免N+1查询**
   - 一次查询所有节点 + 内存构建树
   - 时间复杂度：O(n)

2. **充分利用索引**
   - path字段：前缀匹配查询子树
   - level字段：排序和深度限制
   - hasChildren字段：快速判断

3. **避免反射**
   - MapStruct 编译期代码生成
   - MyBatis-Plus 动态 SELECT

4. **数据库索引**
   ```sql
   CREATE INDEX idx_sys_tenant_path ON sys_tenant(path);
   CREATE INDEX idx_sys_tenant_deleted_level_path ON sys_tenant(is_deleted, level, path);
   CREATE INDEX idx_sys_user_dept_path ON sys_user(dept_path);
   ```

---

## 6. 关键数据库表

### 6.1 核心表结构

| 表名 | 作用 | 关键字段 |
|-----|------|---------|
| sys_user | 系统用户（全局）| id, user_name, user_code |
| sys_tenant | 系统租户 | id, tenant_code, parent_id, path, level, has_children |
| sys_dept | 系统部门（模板）| id, dept_code, dept_name |
| sys_role | 系统角色（模板）| id, role_code, role_name |
| sys_perm | 系统权限（模板）| id, perm_code, perm_name |
| sys_user_policy | 用户策略（绑定关系）| id（租户用户ID）, user_id, target_type, target_id |
| sys_tenant_policy | 租户策略（系统实体→租户）| id（租户部门ID/租户角色ID）, source_type, source_id, tenant_id |
| sys_perm_policy | 权限策略（权限授予）| id, perm_id, target_type, target_id, data_scope, field_operation |

### 6.2 表关系图

```
sys_user（系统用户）
    ↓ user_id
sys_user_policy（target_type='TENANT'）
    ├─ id = 租户用户ID
    └─ target_id → sys_tenant（租户）
    
租户用户ID
    ↓ user_id
sys_user_policy（target_type='ROLE'）
    └─ target_id = 租户角色ID
        ↓ id
    sys_tenant_policy（source_type='ROLE'）
        └─ source_id → sys_role（系统角色）
```

---

## 7. 实施路线图

### 7.1 Phase 1：基础设施（Week 1-2）

**目标：** 完善数据结构和工具类

**任务：**
- [ ] 检查并补充数据库字段（path, level, hasChildren, dept_path）
- [ ] 创建必需的数据库索引
- [ ] 实现 FieldPermissionHelper 工具类
- [ ] 实现 DataScopeHelper 工具类
- [ ] 完善 UserContext 工具类

**交付物：**
- 数据库迁移脚本
- 通用工具类代码
- 单元测试

---

### 7.2 Phase 2：登录与权限加载（Week 3）

**目标：** 完善 AuthServiceImpl.login() 方法

**任务：**
- [ ] 修复 buildPermissionInfo() 方法（字段权限解析）
- [ ] 实现字段权限按操作类型分组
- [ ] 实现字段权限按状态分类（operable/inoperable）
- [ ] 测试 UserContextDTO 数据结构

**交付物：**
- 完善的 AuthServiceImpl
- 登录接口单元测试
- UserContextDTO 示例数据

---

### 7.3 Phase 3：查询接口改造（Week 4-5）

**目标：** 应用字段权限和数据范围

**任务：**
- [ ] 为每个实体创建带字段过滤的 Converter
- [ ] 改造查询接口，应用数据范围
- [ ] 改造查询接口，应用字段权限
- [ ] 集成测试

**交付物：**
- 改造后的 Service 层代码
- Converter 代码
- 集成测试用例

---

### 7.4 Phase 4：新增/更新接口改造（Week 6）

**目标：** 应用字段权限校验

**任务：**
- [ ] 在 Controller 添加字段权限校验
- [ ] 实现新增接口字段校验
- [ ] 实现更新接口差异检测+校验
- [ ] 集成测试

**交付物：**
- 改造后的 Controller 层代码
- 字段校验工具类
- 集成测试用例

---

### 7.5 Phase 5：租户树优化（Week 7）

**目标：** 优化租户树查询性能

**任务：**
- [ ] 实现 buildTreeInMemory() 通用方法
- [ ] 重构 queryTenantTree()
- [ ] 重构 querySubTree()
- [ ] 重构 queryTenantTreePage()
- [ ] 性能测试

**交付物：**
- 优化后的 SysTenantServiceImpl
- 性能测试报告

---

### 7.6 Phase 6：测试与上线（Week 8）

**目标：** 全面测试和灰度发布

**任务：**
- [ ] 全链路集成测试
- [ ] 性能压测
- [ ] 代码审查
- [ ] 灰度发布
- [ ] 监控告警

**交付物：**
- 完整测试报告
- 上线文档
- 回滚预案

---

## 8. 测试策略

### 8.1 单元测试

**覆盖范围：**
- [ ] FieldPermissionHelper 工具类
- [ ] DataScopeHelper 工具类
- [ ] buildTreeInMemory() 方法
- [ ] Converter 字段过滤逻辑

**测试框架：** JUnit 5 + Mockito

---

### 8.2 集成测试

**测试场景：**
- [ ] 用户登录 → 权限加载
- [ ] 查询接口 → 字段权限过滤
- [ ] 查询接口 → 数据范围过滤
- [ ] 新增接口 → 字段权限校验
- [ ] 更新接口 → 字段权限校验
- [ ] 租户树查询 → 性能验证

**测试工具：** Spring Boot Test + H2

---

### 8.3 性能测试

**测试指标：**
- [ ] 查询接口响应时间（P50/P95/P99）
- [ ] 租户树查询SQL次数
- [ ] 数据库查询时间
- [ ] 内存占用

**测试工具：** JMeter + JProfiler

---

## 9. 监控与告警

### 9.1 性能监控

**关键指标：**
- 接口响应时间
- SQL执行次数
- 慢查询日志
- 数据库连接池使用率

**工具：** Prometheus + Grafana

---

### 9.2 业务监控

**关键指标：**
- 登录成功率
- 权限校验失败次数
- 数据范围限制命中率
- 字段权限过滤命中率

**工具：** ELK Stack

---

## 10. 风险与应对

### 10.1 性能风险

**风险：** 大量用户同时登录，权限加载耗时

**应对：**
- 优化 SQL 查询（减少 JOIN）
- 添加 Redis 缓存（UserContextDTO）
- 异步加载非关键数据

---

### 10.2 数据一致性风险

**风险：** path、hasChildren 字段不一致

**应对：**
- 添加数据库触发器
- 定期数据校验脚本
- 事务保证原子性

---

### 10.3 兼容性风险

**风险：** 老接口无权限控制，直接暴露数据

**应对：**
- 灰度发布，逐步迁移
- 默认策略：无配置 = 管理员权限
- 向后兼容模式开关

---

## 11. 文档清单

| 文档 | 路径 | 状态 |
|-----|------|------|
| 字段级权限实现规格 | ./01-field-permission-spec.md | ✅ 完成 |
| 数据可见范围实现规格 | ./02-data-scope-spec.md | ✅ 完成 |
| 租户树服务优化规格 | ./03-tenant-tree-optimization-spec.md | ✅ 完成 |
| 总体架构与实施指南 | ./00-overview-spec.md | ✅ 完成 |

---

## 12. 参考资料

### 12.1 相关代码文件

- `AuthServiceImpl.java` - 登录与权限加载
- `SysTenantServiceImpl.java` - 租户树服务
- `UserContext.java` - 用户上下文工具
- `UserContextDTO.java` - 用户上下文数据结构

### 12.2 数据库设计

- `docs/new_permission_design.sql` - 权限系统数据库设计
- `docs/superpowers/specs/user-permission-analysis.md` - 用户权限分析示例

---

## 13. 联系方式

如有问题，请联系：
- 架构师：[负责人姓名]
- 开发负责人：[负责人姓名]
- 项目经理：[负责人姓名]

---

**文档版本：** v1.0  
**创建日期：** 2026-06-12  
**最后更新：** 2026-06-12  
**文档状态：** 待评审
