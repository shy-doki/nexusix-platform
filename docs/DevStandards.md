# NexusIX-Platform 开发规范

## 1 文档信息

| 项目 | 内容 |
|------|------|
| 版本号 | v1.0.0 |
| 日期 | 2026-05-23 |
| 作者 | shy |
| 项目 | NexusIX-Platform — 多租户 SaaS 平台底座 |

### 变更记录

| 版本 | 日期 | 作者 | 变更说明 |
|------|------|------|----------|
| v1.0.0 | 2026-05-23 | shy | 初始版本 |

---

## 2 代码规范

### 2.1 代码分层规范

请求调用链路严格遵循以下分层，禁止跨层调用：

```
Controller（接收请求 + 参数校验）
    ↓
Service（业务逻辑 + 事务控制）
    ↓
Mapper（数据访问）
    ↓
Entity（表映射）
```

**核心约束：**

- **禁止 Controller 直接注入 Mapper**，所有数据访问必须通过 Service 层
- Controller 只负责接收请求、参数校验（`@Valid` / `@Validated`）、调用 Service、返回 `ApiResponse`
- Service 层承载全部业务逻辑，事务注解 `@Transactional` 仅标注在 Service 方法上
- Mapper 层仅编写数据访问逻辑，复杂 SQL 放在对应 XML 文件中

### 2.2 Entity 对外隔离

- Entity 对外（Controller / API 响应）一律使用 DTO / VO，禁止直接返回 Entity
- 请求入参使用 RTO（Request Transfer Object），响应出参使用 VO（View Object）
- Entity ↔ DTO / VO 之间的转换通过 Converter 类完成，禁止在 Controller / Service 中内联转换逻辑

```
RTO（入参）→ Service → Entity → Mapper
                              ↓
VO（出参）← Service ← Entity ← Mapper
```

### 2.3 JSONB 字段处理

PostgreSQL 的 JSONB 字段在 Entity 中统一使用 `String` 类型 + `JsonTypeHandler`：

```java
@TableField(value = "ext_attributes", typeHandler = JsonTypeHandler.class)
private String extAttributes;
```

如需在业务层操作 JSONB 内容，在 Service 中使用 Fastjson2 / Jackson 解析为具体对象。

### 2.4 代码格式

| 规则 | 说明 |
|------|------|
| 缩进 | 4 空格，禁止 Tab |
| 行宽 | 120 字符，超出换行 |
| 大括号 | K&R 风格：左大括号不换行，右大括号独占一行 |

```java
public void example() {
    if (condition) {
        doSomething();
    } else {
        doOther();
    }
}
```

### 2.5 Import 顺序

按以下分组排列，各组之间空一行，组内按字母序排列：

1. `java.*`
2. `jakarta.*`
3. 第三方库（`com.baomidou.*`, `io.swagger.*`, `lombok.*` 等）
4. 项目内部（`com.shy.nexusix.*`）

IDE 配置推荐：IntelliJ IDEA → Settings → Editor → Code Style → Java → Imports，按上述顺序配置。

### 2.6 空行规范

| 场景 | 空行数 |
|------|--------|
| 成员变量之间 | 1 空行 |
| 方法之间 | 1 空行 |
| 方法内逻辑块之间 | 1 空行 |
| 类声明与首个成员之间 | 1 空行 |
| 最后一个成员与右大括号之间 | 1 空行 |

---

## 3 命名规范

### 3.1 包命名

```
com.shy.nexusix.{module}.controller    — 控制器
com.shy.nexusix.{module}.service       — 服务接口及实现
com.shy.nexusix.{module}.mapper        — 数据访问
com.shy.nexusix.{module}.entity        — 实体类
com.shy.nexusix.{module}.dto           — 数据传输对象
com.shy.nexusix.{module}.vo            — 视图对象
com.shy.nexusix.{module}.converter     — 对象转换器
com.shy.nexusix.{module}.rto           — 请求传输对象
```

其中 `{module}` 为业务模块名，如 `tenant`、`iam`、`org`、`notify` 等。

### 3.2 类命名

| 类型 | 命名规则 | 示例 |
|------|----------|------|
| Entity | `Sys` 前缀 + 业务名 | `SysTenant`, `SysUser` |
| Mapper | 业务名 + `Mapper` | `SysTenantMapper` |
| Service 接口 | `I` 前缀 + 业务名 + `Service` | `ISysTenantService` |
| Service 实现 | 业务名 + `ServiceImpl` | `SysTenantServiceImpl` |
| Controller | 业务名 + `Controller` | `SysTenantController` |
| DTO | 业务名 + `DTO` | `UserContextDTO` |
| RTO | 业务名 + 动作 + `RTO` | `SysTenantAddRTO`, `SysTenantQueryRTO` |
| VO | 业务名 + `VO` | `SysTenantDetailVO`, `SysTenantCommonVO` |
| Converter | 业务名 + `Converter` | `SysTenantConverter` |
| 常量类 | 业务名 + `Constant` | `GlobalConstant` |
| 枚举类 | 业务名 + `Enum` | `StatusEnum` |
| 工具类 | 业务名 + `Utils` | `DateUtils`, `IpUtil` |
| 异常类 | 业务名 + `Exception` | `BusinessException` |

### 3.3 方法命名

| 前缀 | 含义 | 示例 |
|------|------|------|
| `get` | 查询单个对象 | `getTenantById` |
| `list` | 查询集合（无分页） | `listTenants` |
| `page` | 分页查询 | `pageTenants` |
| `create` | 新增 | `createTenant` |
| `update` | 修改 | `updateTenant` |
| `delete` | 删除 | `deleteTenant` |
| `exists` | 判断是否存在 | `existsByCode` |
| `count` | 统计数量 | `countByStatus` |

Controller 层方法可使用 `query` 前缀表示查询操作（如 `queryTenantList`、`queryTenantPage`），与 Service 层 `get` / `list` / `page` 对应。

### 3.4 变量命名

| 类型 | 风格 | 示例 |
|------|------|------|
| 成员变量 / 局部变量 | lowerCamelCase | `tenantCode`, `pageSize` |
| 常量 | UPPER_SNAKE_CASE | `MAX_RETRY_COUNT`, `DEFAULT_PAGE_SIZE` |
| 布尔变量 | `is` / `has` / `can` 前缀 | `isActive`, `hasChildren`, `canDelete` |

### 3.5 数据库命名

| 对象 | 规则 | 示例 |
|------|------|------|
| 表名 | `sys_{module}` | `sys_tenant`, `sys_user` |
| 字段名 | snake_case | `tenant_code`, `create_at` |
| 主键 | `id` | `id` |
| 外键 | `{ref}_id` | `parent_id`, `package_id` |
| 普通索引 | `idx_{table}_{columns}` | `idx_sys_tenant_status` |
| 唯一索引 | `uk_{table}_{columns}` | `uk_sys_tenant_tenant_code` |

### 3.6 API 命名

RESTful 风格，统一前缀 `/api/v1/{resource}`：

| 操作 | HTTP 方法 | 路径 | 示例 |
|------|-----------|------|------|
| 查询单个 | GET | `/api/v1/tenants/{id}` | 查询租户详情 |
| 查询列表 | GET | `/api/v1/tenants/list` | 查询租户列表 |
| 分页查询 | GET | `/api/v1/tenants/page` | 分页查询租户 |
| 条件查询 | POST | `/api/v1/tenants/query` | 条件筛选租户 |
| 创建 | POST | `/api/v1/tenants` | 新增租户 |
| 更新 | PUT | `/api/v1/tenants` | 修改租户 |
| 删除 | DELETE | `/api/v1/tenants/{id}` | 删除租户 |

---

## 4 版本控制规范

### 4.1 分支管理

```
main                — 生产分支，保护分支，仅通过 Release PR 合入
  ↑
release/{version}   — 发布分支，从 develop 拉出，测试通过后合入 main 和 develop
  ↑
develop             — 开发分支，日常开发基线
  ↑
feature/{module}-{desc}  — 功能分支，从 develop 拉出，完成后 PR 回 develop
hotfix/{desc}            — 修复分支，从 main 拉出，修复后 PR 回 main 和 develop
```

**分支命名示例：**

- `feature/tenant-subscription` — 租户订阅功能
- `feature/iam-rbac` — IAM RBAC 权限功能
- `hotfix/fix-login-token-expire` — 登录 Token 过期修复
- `release/1.0.0` — 1.0.0 版本发布

### 4.2 提交规范

提交信息格式：`<type>(<scope>): <subject>`

| type | 说明 |
|------|------|
| `feat` | 新功能 |
| `fix` | 修复 Bug |
| `docs` | 文档变更 |
| `style` | 代码格式调整（不影响逻辑） |
| `refactor` | 重构（不新增功能、不修复 Bug） |
| `test` | 测试相关 |
| `chore` | 构建 / 工具变更 |
| `perf` | 性能优化 |

**示例：**

```
feat(tenant): 新增租户订阅管理接口
fix(iam): 修复登录 Token 过期未刷新问题
docs: 更新开发规范文档
refactor(common): 重构 ApiResponse 链式调用
```

### 4.3 代码审查

- 所有 PR 至少 1 人 Review 后方可合入
- Review 关注点：
  - 业务逻辑正确性
  - 安全性（SQL 注入、XSS、敏感数据泄露）
  - 性能（N+1 查询、大事务、缓存使用）
  - 规范符合度（命名、分层、异常处理）
  - 测试覆盖

### 4.4 .gitignore 规范

必须忽略的文件 / 目录：

```
# 构建产物
target/
build/
*.class
*.jar
*.war

# IDE
.idea/
*.iml
.vscode/
.settings/
.project
.classpath

# 系统文件
.DS_Store
Thumbs.db

# 日志
*.log
logs/

# 配置（含敏感信息）
application-local.yml
application-local.properties

# 依赖
node_modules/
```

---

## 5 文档规范

### 5.1 文档目录

所有项目文档统一在 `docs/` 目录下管理：

```
docs/
├── PRD.md              — 产品需求文档
├── DBDesign.md         — 数据库设计文档
├── APIDesign.md        — API 设计文档
├── HLD.md              — 高层设计文档
├── LLD.md              — 低层设计文档
├── DevStandards.md     — 开发规范
└── create_table.sql    — 建表脚本
```

### 5.2 文档命名

- 大驼峰命名或业界通用简称
- 禁止使用中文文件名、空格、特殊字符

### 5.3 文档格式

- 使用 Markdown 格式
- 架构图 / 流程图使用 Mermaid 语法内嵌
- 表格使用 Markdown 表格

### 5.4 文档版本

每篇文档头部必须包含版本信息：

```markdown
| 项目 | 内容 |
|------|------|
| 版本号 | v1.0.0 |
| 日期 | 2026-05-23 |
| 作者 | shy |

### 变更记录

| 版本 | 日期 | 作者 | 变更说明 |
|------|------|------|----------|
| v1.0.0 | 2026-05-23 | shy | 初始版本 |
```

### 5.5 API 文档

使用 Knife4j / Swagger 注解自动生成 API 文档：

- Controller 类使用 `@Tag` 标注模块名

```java
@Tag(name = "租户管理", description = "租户基础信息管理相关接口")
```

- 接口方法使用 `@Operation` 标注操作说明

```java
@Operation(summary = "查询租户列表", description = "返回所有租户列表")
```

- Entity / DTO / VO / RTO 字段使用 `@Schema` 标注描述和示例

```java
@Schema(description = "租户唯一编码", example = "TENANT_001")
```

---

## 6 注释规范

### 6.1 类注释

每个类必须有 Javadoc，包含功能描述、`@author`、`@since`：

```java
/**
 * <p>
 * 租户信息表 - 存储租户基础信息，支持无限层级
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
public class SysTenant {
```

### 6.2 方法注释

公开方法必须有 Javadoc，包含功能描述、`@param`、`@return`、`@throws`：

```java
/**
 * <p>
 * 分页查询租户列表
 * </p>
 * <p>
 * 返回分页后的租户列表，租户编码会自动进行脱敏处理。
 * 需要登录并具备租户查看权限才能访问。
 * </p>
 *
 * @param page 分页参数
 * @return 分页后的租户列表
 * @throws BusinessException 当用户无权限或查询失败时抛出
 */
```

### 6.3 字段注释

Entity / DTO / VO / RTO 字段使用 `@Schema` 注解描述，关键约束需在注解中说明：

```java
@Schema(description = "租户唯一编码", example = "TENANT_001")
private String tenantCode;

@Schema(description = "联系人电话", example = "13800138000", pattern = "^1[3-9]\\d{9}$")
private String contactPhone;
```

### 6.4 行内注释

- 注释应说明**意图**而非**做法**
- 仅在逻辑不直观时添加，禁止逐行翻译代码

```java
// 正确：说明意图
// 冻结租户后，其下所有用户 Token 需要失效
invalidateTokensByTenant(tenantId);

// 错误：说明做法
// 调用 invalidateTokensByTenant 方法
invalidateTokensByTenant(tenantId);
```

### 6.5 禁止无意义注释

- 禁止注释掉的代码（用版本控制代替）
- 禁止 `// TODO` 长期遗留，必须在当前迭代内处理
- 禁止与代码矛盾的注释

---

## 7 异常处理规范

### 7.1 业务异常

业务校验失败统一抛出 `BusinessException`：

```java
throw new BusinessException(ResultCode.XXX);
```

当前 `BusinessException` 支持以下构造方式：

```java
// 指定错误码和消息
throw new BusinessException(4001, "租户编码已存在");

// 仅指定消息（默认错误码 500）
throw new BusinessException("租户不存在");
```

### 7.2 禁止吞异常

```java
// 禁止
try {
    doSomething();
} catch (Exception e) {
    // 什么都不做
}

// 正确：至少记录日志
try {
    doSomething();
} catch (Exception e) {
    log.error("操作失败: param={}", param, e);
    throw new BusinessException("操作失败");
}
```

### 7.3 日志记录

| 异常类型 | 日志级别 | 说明 |
|----------|----------|------|
| 业务异常 | WARN | 记录业务异常信息，不含堆栈 |
| 系统异常 | ERROR | 记录完整堆栈信息 |

```java
// 业务异常
log.warn("租户不存在: tenantCode={}", tenantCode);

// 系统异常
log.error("数据库操作异常: tenantId={}", tenantId, e);
```

### 7.4 敏感数据脱敏

异常信息和日志中涉及敏感数据必须脱敏后记录：

- 手机号：保留前 3 后 4，如 `138****8000`
- 身份证：保留前 3 后 4，如 `110***********1234`
- 密码：禁止出现在任何日志中

---

## 8 日志规范

### 8.1 日志级别

| 级别 | 使用场景 | 示例 |
|------|----------|------|
| ERROR | 系统异常，需要立即处理 | 数据库连接失败、第三方服务不可用 |
| WARN | 业务警告，不影响主流程 | 参数非法、权限不足、重试 |
| INFO | 关键业务操作 | 用户登录、租户创建、权限变更 |
| DEBUG | 调试信息，生产环境关闭 | SQL 参数、方法入参出参 |

### 8.2 日志格式

使用占位符 `{}`，禁止字符串拼接：

```java
// 正确
log.info("创建租户: tenantCode={}, tenantName={}", tenantCode, tenantName);

// 禁止
log.info("创建租户: tenantCode=" + tenantCode + ", tenantName=" + tenantName);
```

### 8.3 操作日志

关键业务操作使用 `@OperationLog` 注解 + AOP 切面异步写入：

```java
@OperationLog(module = "租户管理", action = "新增租户")
@PostMapping("/add")
public ApiResponse addTenant(@Valid @RequestBody SysTenantAddRTO addParam) {
    // ...
}
```

操作日志记录内容：

- 操作人（用户 ID / 用户名）
- 操作模块和动作
- 操作参数（脱敏后）
- 操作结果（成功 / 失败）
- 操作时间
- IP 地址

---

## 9 安全规范

### 9.1 密码安全

- 密码使用 BCrypt 或 Argon2 加密存储，**禁止明文存储**
- 密码强度要求：至少 8 位，包含大小写字母 + 数字 + 特殊字符
- 密码字段使用 `@JsonIgnore` 禁止序列化输出

```java
@JsonIgnore
private String password;
```

### 9.2 敏感数据保护

| 数据类型 | 保护措施 |
|----------|----------|
| 密码 | `@JsonIgnore` + BCrypt/Argon2 加密 |
| 手机号 | 日志脱敏 `138****8000`，VO 按需脱敏 |
| 身份证 | 日志脱敏，VO 按需脱敏 |
| Token | 仅 HTTPS 传输，禁止 URL 参数传递 |

### 9.3 SQL 注入防护

- 使用 MyBatis-Plus 参数化查询，**禁止字符串拼接 SQL**
- Mapper XML 中使用 `#{param}` 而非 `${param}`
- 特殊场景必须使用 `${param}` 时，需在 Service 层做白名单校验

```xml
<!-- 正确 -->
WHERE tenant_code = #{tenantCode}

<!-- 禁止 -->
WHERE tenant_code = '${tenantCode}'
```

### 9.4 XSS 防护

用户输入存储前使用 `HtmlUtils.htmlEscape()` 转义：

```java
String safeInput = HtmlUtils.htmlEscape(userInput);
```

或在 Web 层通过全局过滤器统一处理。

### 9.5 Token 安全

- Token 仅通过 HTTPS 传输
- Token 存储：Redis + DB 双写，支持服务重启后恢复会话
- Token 失效：支持主动失效和踢下线
- Token 刷新：短期 Access Token + 长期 Refresh Token 机制

---

## 10 测试规范

### 10.1 单元测试

- 框架：JUnit 5 + Mockito
- 覆盖率要求：≥ 80%
- Service 层为重点测试对象

```java
@ExtendWith(MockitoExtension.class)
class SysTenantServiceImplTest {

    @Mock
    private SysTenantMapper sysTenantMapper;

    @InjectMocks
    private SysTenantServiceImpl sysTenantService;

    @Test
    void testCreateTenant_CodeExists_ThrowBusinessException() {
        // given
        when(sysTenantMapper.selectOne(any())).thenReturn(new SysTenant());

        // when & then
        assertThrows(BusinessException.class,
            () -> sysTenantService.createTenant(addRTO));
    }
}
```

### 10.2 集成测试

- 框架：TestContainers + PostgreSQL
- 使用真实数据库容器验证 SQL 和事务行为

```java
@Testcontainers
@SpringBootTest
class SysTenantServiceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17");
}
```

### 10.3 测试命名

格式：`test{Method}_{Scenario}_{ExpectedResult}`

```java
testCreateTenant_CodeNotExists_Success()
testCreateTenant_CodeExists_ThrowBusinessException()
testDeleteTenant_HasChildren_ThrowBusinessException()
testQueryTenantPage_ValidParam_ReturnPagedResult()
```

### 10.4 测试目录

测试代码目录结构与源码一一对应：

```
src/test/java/com/shy/nexusix/
├── tenant/
│   ├── controller/
│   │   └── SysTenantControllerTest.java
│   ├── service/
│   │   └── SysTenantServiceImplTest.java
│   └── mapper/
│       └── SysTenantMapperTest.java
├── iam/
│   └── ...
└── common/
    └── ...
```
