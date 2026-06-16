# NexusIX-Platform 模块功能设计总览

**文档版本**: v2.0  
**创建日期**: 2026-06-15  
**基于**: nexusix-tenant 模块实现方案  

---

## 📋 文档导航

本系列PRD文档共包含5个子文档：

1. [组织架构模块PRD](./模块功能设计PRD-组织架构模块.md)
2. [IAM模块补充PRD](./模块功能设计PRD-IAM模块补充.md)
3. [系统配置模块PRD](./模块功能设计PRD-系统配置模块.md)
4. [审计通知计费模块PRD](./模块功能设计PRD-审计通知计费模块.md)
5. [本文档 - 总览]

---

## 一、项目模块全景图

### 1.1 模块分层架构

```
┌─────────────────────────────────────────────────────────────┐
│                    nexusix-boot (启动模块)                     │
│                     聚合所有业务模块                            │
└────────────────────┬────────────────────────────────────────┘
                     │
    ┌────────────────┴──────────────────┐
    │                                   │
┌───▼─────────────┐          ┌─────────▼──────────┐
│ nexusix-common  │          │   nexusix-core     │
│  (公共基础层)     │◄─────────┤   (核心服务层)      │
└─────────────────┘          └────────────────────┘
         ▲                            ▲
         │                            │
         └────────────┬───────────────┘
                      │
         ┌────────────┴──────────────┐
         │                           │
    ┌────▼──────┐              ┌────▼──────┐
    │ 业务模块层  │              │ 功能模块层  │
    └───────────┘              └───────────┘
         │                           │
    ┌────┴────────────┐         ┌───┴──────────┐
    │                 │         │              │
┌───▼────┐  ┌────▼────┐  ┌────▼────┐  ┌─────▼─────┐
│nexusix-│  │nexusix- │  │nexusix- │  │nexusix-   │
│iam     │  │tenant   │  │org      │  │system     │
└────────┘  └─────────┘  └─────────┘  └───────────┘
┌────────┐  ┌─────────┐  ┌─────────┐  ┌───────────┐
│nexusix-│  │nexusix- │  │nexusix- │  │nexusix-   │
│audit   │  │notify   │  │billing  │  │dynamic    │
└────────┘  └─────────┘  └─────────┘  └───────────┘
```

### 1.2 模块依赖关系

| 模块 | 依赖关系 | 说明 |
|------|---------|------|
| **nexusix-common** | 无 | 底层公共模块，被所有模块依赖 |
| **nexusix-core** | common | 核心服务，提供上下文和基础配置 |
| **业务模块** | core + common | 业务功能实现 |
| **nexusix-boot** | 所有模块 | 启动入口，聚合所有模块 |

---

## 二、模块功能矩阵

### 2.1 已实现模块（基于tenant模块）

| 模块 | 功能概述 | 核心实体 | 接口数量 | 实现状态 |
|------|---------|---------|---------|---------|
| **nexusix-tenant** | 租户管理、租户策略 | SysTenant, SysTenantPolicy | 12+ | ✅ 已实现 |
| **nexusix-iam** | 认证授权（基础） | SysUser, SysRole, SysPerm | 8+ | ✅ 已实现 |
| **nexusix-common** | 公共组件、配置 | - | - | ✅ 已实现 |
| **nexusix-core** | 用户上下文、租户上下文 | UserContextDTO | - | ✅ 已实现 |

### 2.2 本次设计新增模块

| 模块 | 功能概述 | 核心实体 | 接口数量 | 文档状态 |
|------|---------|---------|---------|---------|
| **nexusix-org** | 组织架构、部门管理 | SysDept, SysPost | 12+ | ✅ 已设计 |
| **nexusix-iam** (扩展) | 用户管理、角色权限完整功能 | SysUser, SysRole, SysPerm | 40+ | ✅ 已设计 |
| **nexusix-system** | 系统配置、字典、日志、监控 | SysConfig, SysDict, SysLog | 30+ | ✅ 已设计 |
| **nexusix-audit** | 审计日志、数据变更追踪 | SysAuditLog, SysDataChangeLog | 7+ | ✅ 已设计 |
| **nexusix-notify** | 消息通知、模板管理 | SysMessage, SysMessageTemplate | 8+ | ✅ 已设计 |
| **nexusix-billing** | 套餐订单、计费管理 | SysPackage, SysOrder | 11+ | ✅ 已设计 |
| **nexusix-dynamic** | 动态表单、工作流 | SysFormTemplate, SysWorkflow | - | ⚠️ 预留 |

---

## 三、接口统计汇总

### 3.1 按模块统计

| 模块 | 控制器数量 | 接口总数 | 核心接口 | 管理接口 |
|------|-----------|---------|---------|---------|
| nexusix-tenant | 2 | 12 | 租户CRUD、树形查询 | 租户策略管理 |
| nexusix-iam | 5 | 46 | 登录认证、用户管理 | 角色权限管理 |
| nexusix-org | 2 | 12 | 部门CRUD、树形查询 | 部门成员管理 |
| nexusix-system | 5 | 30 | 配置字典管理 | 日志监控 |
| nexusix-audit | 2 | 7 | 审计日志查询 | 数据变更追踪 |
| nexusix-notify | 2 | 8 | 消息发送查询 | 模板管理 |
| nexusix-billing | 3 | 11 | 套餐查询、订单管理 | 订阅管理 |
| **合计** | **21** | **126+** | - | - |

### 3.2 按功能类型统计

| 功能类型 | 接口数量 | 占比 |
|---------|---------|------|
| 查询类（GET） | 65+ | 51.6% |
| 新增类（POST） | 25+ | 19.8% |
| 更新类（PUT） | 20+ | 15.9% |
| 删除类（DELETE） | 16+ | 12.7% |

---

## 四、数据库设计汇总

### 4.1 核心表清单

| 序号 | 表名 | 所属模块 | 主要字段 | 索引数量 |
|------|------|---------|---------|---------|
| 1 | sys_tenant | tenant | id, tenant_code, parent_id, path | 4 |
| 2 | sys_tenant_policy | tenant | id, source_type, source_id, tenant_id | 3 |
| 3 | sys_user | iam | id, user_code, user_name, status | 5 |
| 4 | sys_user_policy | iam | id, user_id, tenant_id, dept_id, role_id | 4 |
| 5 | sys_role | iam | id, role_code, role_name, data_scope | 3 |
| 6 | sys_perm | iam | id, perm_code, parent_id, path | 4 |
| 7 | sys_perm_policy | iam | id, role_id, perm_id, field_permissions | 3 |
| 8 | sys_dept | org | id, dept_code, parent_id, path | 4 |
| 9 | sys_post | org | id, post_code, post_name | 2 |
| 10 | sys_config | system | id, config_key, config_value | 2 |
| 11 | sys_dict_type | system | id, dict_type_code, dict_type_name | 2 |
| 12 | sys_dict_data | system | id, dict_type_code, dict_data_code | 3 |
| 13 | sys_login_log | system | id, user_id, login_time | 3 |
| 14 | sys_operation_log | system | id, user_id, operation_time | 4 |
| 15 | sys_audit_log | audit | id, audit_type, operation_time | 4 |
| 16 | sys_data_change_log | audit | id, table_name, record_id | 3 |
| 17 | sys_message | notify | id, receiver_id, is_read | 3 |
| 18 | sys_message_template | notify | id, template_code | 2 |
| 19 | sys_package | billing | id, package_code, package_type | 2 |
| 20 | sys_order | billing | id, order_no, tenant_id | 4 |
| 21 | sys_subscription | billing | id, tenant_id, package_id | 3 |
| 22 | sys_resource_usage | billing | id, tenant_id, resource_type | 2 |

**表总数**: 22张核心表  
**索引总数**: 70+  

### 4.2 数据库设计原则

1. **统一审计字段**: 所有业务表包含以下字段
   - create_tenant, create_dept, create_role, create_by, create_at
   - update_by, update_at
   - is_deleted, deleted_at

2. **物化路径**: 树形结构表（tenant, dept, perm）使用path字段存储完整路径

3. **软删除**: 所有表使用逻辑删除，通过is_deleted字段标记

4. **编码唯一**: 所有业务对象使用全局唯一编码（xxx_code字段）

5. **状态枚举**: 状态字段使用VARCHAR存储枚举值（如ENABLED/DISABLED）

---

## 五、技术实现规范

### 5.1 分层架构规范

```
Controller层   → 接收HTTP请求，参数校验，调用Service
  ├─ RTO        → 请求传输对象（Request Transfer Object）
  └─ VO         → 视图对象（View Object）

Service层     → 业务逻辑实现，事务控制
  └─ DTO        → 数据传输对象（Data Transfer Object）

Mapper层      → 数据访问，MyBatis-Plus + XML
  └─ Entity     → 实体对象（对应数据库表）

Converter层   → 对象转换（使用MapStruct）
```

### 5.2 命名规范

#### 5.2.1 接口命名

| 操作类型 | 命名规则 | 示例 |
|---------|---------|------|
| 查询列表 | query + 实体 + List | queryUserList() |
| 分页查询 | query + 实体 + Page | queryUserPage() |
| 查询详情 | query + 实体 + Detail | queryUserDetail() |
| 新增 | add + 实体 | addUser() |
| 更新 | update + 实体 | updateUser() |
| 删除 | delete + 实体 | deleteUser() |
| 批量操作 | batch + 操作 | batchAddMembers() |

#### 5.2.2 RTO命名

| 场景 | 命名规则 | 示例 |
|------|---------|------|
| 新增请求 | 实体 + AddRTO | SysUserAddRTO |
| 更新请求 | 实体 + UpdateRTO | SysUserUpdateRTO |
| 查询请求 | 实体 + QueryRTO | SysUserQueryRTO |
| 特殊操作 | 实体 + 操作 + RTO | SysUserResetPasswordRTO |

#### 5.2.3 VO命名

| 场景 | 命名规则 | 示例 |
|------|---------|------|
| 通用列表 | 实体 + CommonVO | SysUserCommonVO |
| 详情信息 | 实体 + DetailVO | SysUserDetailVO |
| 树形结构 | 实体 + TreeVO | SysDeptTreeVO |
| 导出数据 | 实体 + ExportVO | SysUserExportVO |

### 5.3 参数校验规范

使用Jakarta Validation注解：

```java
@NotBlank(message = "用户名不能为空")
@Length(min = 3, max = 50, message = "用户名长度3-50字符")
private String userName;

@Email(message = "邮箱格式错误")
private String email;

@Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式错误")
private String phone;

@NotEmpty(message = "角色列表不能为空")
@Size(min = 1, max = 10, message = "角色数量1-10个")
private List<String> roleCodes;
```

### 5.4 权限控制规范

使用Sa-Token注解：

```java
@SaCheckPermission("USER:VIEW")    // 单个权限
@SaCheckPermission(value = {"USER:VIEW", "USER:EDIT"}, mode = SaMode.OR)  // OR模式
@SaCheckRole("ADMIN")              // 角色检查
```

### 5.5 日志记录规范

#### 操作日志

```java
@PostMapping("/add")
@OperationLog(module = "用户管理", type = "ADD", desc = "新增用户")
public ApiResponse addUser(@Valid @RequestBody SysUserAddRTO param) {
    // ...
}
```

#### 数据变更追踪

```java
@PutMapping("/update")
@DataChange(tableName = "sys_user", changeType = "UPDATE")
public ApiResponse updateUser(@Valid @RequestBody SysUserUpdateRTO param) {
    // ...
}
```

---

## 六、核心特性实现

### 6.1 多租户隔离

**方式1: 手动过滤（当前方案）**
```java
wrapper.eq(SysUser::getCreateTenant, TenantContext.getCurrentTenantId());
```

**方式2: MyBatis-Plus拦截器（可选）**
```java
@Bean
public MybatisPlusInterceptor mybatisPlusInterceptor() {
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    TenantLineInnerInterceptor tenantInterceptor = new TenantLineInnerInterceptor();
    // 配置租户拦截器...
    return interceptor;
}
```

### 6.2 物化路径树形结构

**查询子树（单次SQL）**:
```sql
SELECT * FROM sys_dept 
WHERE path LIKE '/101/%' 
AND is_deleted = 'NOT_DELETED'
ORDER BY path;
```

**移动节点（批量更新）**:
```java
// 1. 更新所有子孙节点的path
for (SysDept subDept : subDepts) {
    String updatedPath = subDept.getPath().replace(oldPath, newPath);
    subDept.setPath(updatedPath);
    subDept.setLevel(subDept.getLevel() + levelDiff);
}
```

### 6.3 权限黑名单机制

**四级禁用体系**:
```
DISABLED_SYSTEM_LEVEL     (最高优先级)
DISABLED_TENANT_LEVEL
DISABLED_ROLE_LEVEL
DISABLED_USER_LEVEL       (最低优先级)
```

**判断逻辑**: 任意一级禁用，则权限最终不可用

### 6.4 字段级权限

**存储格式（JSONB）**:
```json
{
    "user_name": ["READ", "CREATE", "UPDATE"],
    "email": ["READ", "UPDATE"],
    "salary": []
}
```

**应用方式**:
```java
// 查询时动态选择字段
wrapper.select(SysUser.class, 
    entity -> visibleFields.contains(entity.getColumn()));
```

### 6.5 Redis缓存策略

| 缓存类型 | Key格式 | TTL | 用途 |
|---------|--------|-----|------|
| Sa-Token Session | satoken:login:session:{userId} | 30天 | 用户上下文 |
| 系统配置 | system:config:{configKey} | 24小时 | 配置信息 |
| 数据字典 | system:dict:{dictTypeCode} | 永久 | 字典数据 |

---

## 七、开发检查清单

### 7.1 新增模块检查

- [ ] 模块pom.xml配置正确
- [ ] 继承nexusix-common和nexusix-core
- [ ] 实体类包含完整审计字段
- [ ] Mapper继承BaseMapper
- [ ] Service继承IService
- [ ] Converter使用MapStruct
- [ ] Controller统一使用ApiResponse
- [ ] 接口添加Swagger注解
- [ ] 关键接口添加权限注解
- [ ] 敏感操作添加日志注解

### 7.2 代码质量检查

- [ ] 参数校验完整（使用Jakarta Validation）
- [ ] 异常处理规范（抛出BusinessException）
- [ ] 事务注解正确（@Transactional）
- [ ] SQL注入防护（使用参数绑定）
- [ ] 敏感信息脱敏（日志、返回值）
- [ ] 分页查询使用Page
- [ ] 批量操作考虑性能
- [ ] 长事务拆分优化

### 7.3 测试覆盖检查

- [ ] 单元测试覆盖率 > 80%
- [ ] 集成测试覆盖核心流程
- [ ] 边界条件测试
- [ ] 并发安全测试
- [ ] 性能压测通过

---

## 八、待实现功能清单

### 8.1 P0 优先级（核心功能）

| 功能 | 所属模块 | 预计工时 | 状态 |
|------|---------|---------|------|
| UserContext工具方法 | nexusix-core | 2天 | ⏳ 待实现 |
| TenantContext工具方法 | nexusix-core | 0.5天 | ⏳ 待实现 |
| 密码BCrypt加密 | nexusix-iam | 0.5天 | ⏳ 待实现 |
| 登录IP获取 | nexusix-iam | 0.5天 | ⏳ 待实现 |

### 8.2 P1 优先级（重要功能）

| 功能 | 所属模块 | 预计工时 | 状态 |
|------|---------|---------|------|
| 组织架构完整实现 | nexusix-org | 5天 | 📝 已设计 |
| 用户管理扩展 | nexusix-iam | 3天 | 📝 已设计 |
| 角色权限管理 | nexusix-iam | 3天 | 📝 已设计 |
| 系统配置模块 | nexusix-system | 5天 | 📝 已设计 |
| 数据字典管理 | nexusix-system | 2天 | 📝 已设计 |
| 日志管理 | nexusix-system | 3天 | 📝 已设计 |

### 8.3 P2 优先级（增强功能）

| 功能 | 所属模块 | 预计工时 | 状态 |
|------|---------|---------|------|
| 审计日志 | nexusix-audit | 3天 | 📝 已设计 |
| 消息通知 | nexusix-notify | 4天 | 📝 已设计 |
| 计费管理 | nexusix-billing | 5天 | 📝 已设计 |
| 动态表单 | nexusix-dynamic | 10天 | ⚠️ 预留 |

---

## 九、扩展方向

### 9.1 技术演进

- 🔄 微服务拆分（Spring Cloud Alibaba）
- 🔄 读写分离（ShardingSphere）
- 🔄 分库分表（按租户分片）
- 🔄 Elasticsearch日志检索
- 🔄 Prometheus + Grafana监控
- 🔄 Docker + Kubernetes容器化

### 9.2 功能扩展

- 🔄 移动端适配（小程序/APP）
- 🔄 数据大屏
- 🔄 BI报表
- 🔄 AI智能助手
- 🔄 第三方集成（钉钉/企微/飞书）

---

## 十、文档使用说明

### 10.1 开发人员

1. 阅读本总览文档，了解整体架构
2. 根据分配模块，阅读对应子文档
3. 按照设计实现代码
4. 参考tenant模块的实现风格
5. 完成后进行代码审查

### 10.2 架构师/技术负责人

1. 审查模块设计合理性
2. 评估技术方案可行性
3. 把控代码质量
4. 协调模块间依赖
5. 制定开发排期

### 10.3 产品经理

1. 了解功能覆盖范围
2. 验证业务需求完整性
3. 确认接口定义
4. 参与用户验收测试

---

## 附录：快速参考

### A. 关键类路径

```
用户上下文：com.shy.nexusix.core.context.UserContext
租户上下文：com.shy.nexusix.core.context.TenantContext
统一响应：com.shy.nexusix.common.result.ApiResponse
业务异常：com.shy.nexusix.common.exception.BusinessException
全局枚举：com.shy.nexusix.common.enums.GlobalEnum
全局常量：com.shy.nexusix.common.constant.GlobalConstant
```

### B. 核心配置文件

```
application.yml              - 主配置
application-dev.yml          - 开发环境
application-prod.yml         - 生产环境
logback-spring.xml           - 日志配置
```

### C. 常用工具类

```
BCryptUtil                   - 密码加密
DateTimeUtil                 - 日期时间
StringUtil                   - 字符串处理
JsonUtil                     - JSON处理（Fastjson2）
```

---

**文档完成日期**: 2026-06-15  
**文档维护**: 开发团队  
**更新频率**: 随项目迭代更新  

如有疑问，请联系架构组。
