# NexusIX-Platform 多行业通用SaaS租户系统 — 产品需求文档 (PRD)

> **文档版本**: v1.0.0
> **创建日期**: 2026-06-08
> **文档状态**: Draft
> **产品负责人**: [待定]

---

## 目录

1. [产品概述](#1-产品概述)
2. [创业压力测试诊断](#2-创业压力测试诊断)
3. [系统架构设计](#3-系统架构设计)
4. [租户管理模块](#4-租户管理模块)
5. [权限控制体系](#5-权限控制体系)
6. [多行业适配方案](#6-多行业适配方案)
7. [用户角色管理](#7-用户角色管理)
8. [租户个性化配置](#8-租户个性化配置)
9. [数据隔离机制](#9-数据隔离机制)
10. [业务流程](#10-业务流程)
11. [界面原型说明](#11-界面原型说明)
12. [非功能需求](#12-非功能需求)
13. [安全要求](#13-安全要求)
14. [验收标准](#14-验收标准)
15. [附录](#15-附录)

---

## 1. 产品概述

### 1.1 产品定位

NexusIX-Platform 是一个**高度可定制化、权限颗粒化的多行业通用SaaS租户管理平台**，面向中大型企业及集团型组织，提供从租户生命周期管理、四级级联权限控制、字段级数据权限到多行业业务适配的全栈解决方案。

### 1.2 核心价值主张

| 维度 | 价值 |
|------|------|
| **权限颗粒度** | 差异化的四级级联权限模型（系统→租户→角色→用户），支持字段级数据权限控制，按级别记录禁用原因 |
| **租户灵活性** | 无限深度租户树（物化路径模式），租户与组织架构严格分离，租户为计费/隔离边界 |
| **行业适配** | 低代码行业适配：配置层(权限+表单) + 规则层(JSON DSL) + 扩展层(插件接口) |
| **数据安全** | 共享数据库逻辑隔离 + 行级安全策略(RLS)，租户隔离与数据权限分层过滤 |

### 1.3 目标用户画像

| 角色 | 描述 | 核心诉求 |
|------|------|----------|
| **平台运营方** | SaaS平台管理员 | 统一管控所有租户，系统级权限禁用，计费管理 |
| **集团管理员** | 一级企业租户管理员 | 管理下属分公司/部门，租户级权限配置，订阅管理 |
| **分公司管理员** | 二级租户管理员 | 管理本分公司用户和业务，继承上级权限约束 |
| **部门主管** | 三级及以下租户管理员 | 管理本部门人员，有限权限范围 |
| **普通用户** | 业务操作人员 | 多租户切换，按权限操作业务 |

### 1.4 现有代码资产评估

基于对现有项目代码的深度分析，当前代码资产状态如下：

| 模块 | 完成度 | 关键资产 |
|------|--------|----------|
| `nexusix-tenant` | **90%** | 完整的租户CRUD、树操作、批量操作、字段级权限执行 |
| `nexusix-iam` | **70%** | 登录流程完整、权限上下文构建完整、密码加密待实现 |
| `nexusix-core` | **40%** | `UserContextDTO`完整，`UserContext`/`TenantContext`大量TODO |
| `nexusix-org` | **10%** | 仅有Mapper XML（角色、部门、岗位、用户组） |
| `nexusix-billing` | **0%** | 仅POM文件 |
| `nexusix-dynamic` | **0%** | 仅POM文件 |
| `nexusix-notify` | **10%** | 仅有Mapper XML（收件箱、消息调度、模板） |
| `nexusix-audit` | **0%** | 仅POM文件 |
| `nexusix-system` | **0%** | 仅POM文件 |

---

## 2. 创业压力测试诊断

> 基于 codex-startup-pressure-test 技能方法论，对NexusIX-Platform进行创业压力测试诊断。

### 2.1 核心假设

**核心假设**: 中大型企业愿意为"字段级权限控制+无限层级租户树"的差异化能力支付溢价，而非选择现有RBAC方案。

**验证方式**: 在3个不同行业（互联网、制造、教育）各签约1家付费客户，验证字段级权限是否为购买决策关键因素。

### 2.2 致命缺陷识别

| 编号 | 致命缺陷 | 严重度 | 缓解措施 |
|------|----------|--------|----------|
| F1 | 密码明文比对（`AuthServiceImpl`中TODO） | **Critical** | 立即实现BCrypt加密 |
| F2 | Sa-Token全局过滤器已注释，无路由级鉴权 | **Critical** | 激活过滤器，配置白名单 |
| F3 | `UserContext`/`TenantContext`大量TODO方法 | **High** | 优先实现上下文获取方法 |
| F4 | 无注册端点（`RegisterRTO`存在但无实现） | **High** | 实现租户自助注册流程 |
| F5 | 无数据权限行级过滤（仅有字段级） | **Medium** | 实现基于租户路径的行级过滤 |
| F6 | 无操作审计日志 | **Medium** | 实现`nexusix-audit`模块 |

### 2.3 问题真实性验证

| 问题 | 真实性 | 证据 |
|------|--------|------|
| 企业需要字段级权限控制 | **真实** | 医疗/金融行业合规要求字段脱敏，ERP系统常见字段权限需求 |
| 无限层级租户树是刚需 | **部分真实** | 大部分企业3-5层足够，但集团型企业确实需要5+层 |
| 多行业适配是购买决策因素 | **待验证** | 多数企业更关注本行业深度适配而非通用性 |

### 2.4 竞争格局

| 竞品 | 定位 | 优势 | 劣势 |
|------|------|------|------|
| **Sa-Plus** | Java SaaS脚手架 | 社区活跃，上手快 | 权限颗粒度粗，无字段级控制 |
| **Bladex** | 商业SaaS框架 | 功能全面，商业支持 | 商业授权费高，定制性受限 |
| **MaxKey** | 统一身份认证 | SSO能力强 | 仅专注认证，无业务权限 |
| **Casbin** | 权限策略引擎 | 灵活的策略模型 | 需自行集成，无SaaS租户能力 |

**差异化优势**: NexusIX的四级级联权限+字段级控制是当前市场空白。

### 2.5 首批10个客户获取计划

| 序号 | 目标客户 | 行业 | 获取方式 | 验证目标 |
|------|----------|------|----------|----------|
| 1 | 中型电商公司 | 互联网 | 技术社区推广 | 字段级权限需求验证 |
| 2 | 连锁餐饮集团 | 餐饮 | 行业展会 | 多层级租户需求验证 |
| 3 | 制造型企业 | 制造 | 伙伴推荐 | 行业适配验证 |
| 4 | 教育培训机构 | 教育 | 免费试用 | 行业模板验证 |
| 5 | 医疗信息公司 | 医疗 | 合规驱动 | 字段脱敏需求验证 |
| 6 | 物流公司 | 物流 | 冷启动 | 多组织架构验证 |
| 7 | 连锁零售 | 零售 | 竞品迁移 | 订阅计费验证 |
| 8 | 金融科技公司 | 金融 | 合规驱动 | 审计日志需求验证 |
| 9 | SaaS ISV | 平台 | API合作 | 多租户隔离验证 |
| 10 | 政府信息化项目 | 政府 | 招标 | 安全合规验证 |

### 2.6 2周MVP方向

**目标**: 验证"字段级权限+无限层级租户"的核心价值假设。

| 天数 | 任务 | 交付物 |
|------|------|--------|
| D1-D2 | 修复致命缺陷F1-F3 | BCrypt加密、Sa-Token过滤器激活、Context方法实现 |
| D3-D4 | 实现注册端点 | 租户自助注册+管理员审核流程 |
| D5-D7 | 实现角色管理模块 | 角色CRUD、角色-权限关联、角色-用户分配 |
| D8-D9 | 实现数据权限行级过滤 | 基于租户路径的MyBatis拦截器 |
| D10-D11 | 实现审计日志 | 操作日志AOP切面+查询接口 |
| D12-D14 | 集成测试+Demo环境 | 可演示的完整权限流程 |

### 2.7 诊断结论

**判定: STRONG — 条件性推进**

核心权限模型设计优秀，四级级联+字段级控制在Java SaaS生态中有明确差异化。但必须先修复安全致命缺陷（明文密码、无鉴权过滤器），否则无法进入任何合规客户的评估流程。

---

## 3. 系统架构设计

### 3.1 整体架构

```
┌─────────────────────────────────────────────────────────────────────┐
│                         客户端层 (Client)                           │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────────────┐   │
│  │ Web SPA  │  │ Mobile   │  │ MiniApp  │  │  Open API Client │   │
│  └────┬─────┘  └────┬─────┘  └────┬─────┘  └───────┬──────────┘   │
└───────┼──────────────┼──────────────┼────────────────┼──────────────┘
        │              │              │                │
┌───────▼──────────────▼──────────────▼────────────────▼──────────────┐
│                      API 网关层 (Gateway)                           │
│  ┌─────────────┐  ┌──────────────┐  ┌──────────────────────────┐  │
│  │ 路由转发     │  │ 限流熔断     │  │ 统一认证 (Sa-Token JWT)  │  │
│  └─────────────┘  └──────────────┘  └──────────────────────────┘  │
└────────────────────────────┬────────────────────────────────────────┘
                             │
┌────────────────────────────▼────────────────────────────────────────┐
│                    业务服务层 (Services)                             │
│                                                                     │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐               │
│  │ nexusix-iam  │ │nexusix-tenant│ │ nexusix-org  │               │
│  │ 认证/权限    │ │ 租户管理     │ │ 组织/角色    │               │
│  └──────┬───────┘ └──────┬───────┘ └──────┬───────┘               │
│         │                │                │                         │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐               │
│  │nexusix-billing│ │nexusix-notify│ │nexusix-audit │               │
│  │ 计费/订阅    │ │ 通知/消息    │ │ 审计日志     │               │
│  └──────┬───────┘ └──────┬───────┘ └──────┬───────┘               │
│         │                │                │                         │
│  ┌──────────────┐ ┌──────────────┐                                 │
│  │nexusix-dynamic│ │nexusix-system│                                 │
│  │ 动态表单/字段│ │ 系统配置     │                                 │
│  └──────┬───────┘ └──────┬───────┘                                 │
│         │                │                                         │
└─────────┼────────────────┼─────────────────────────────────────────┘
          │                │
┌─────────▼────────────────▼─────────────────────────────────────────┐
│                     基础设施层 (Infrastructure)                      │
│                                                                     │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐               │
│  │  PostgreSQL  │ │    Redis     │ │   RabbitMQ   │               │
│  │  主数据库    │ │ 缓存/会话    │ │  消息队列    │               │
│  └──────────────┘ └──────────────┘ └──────────────┘               │
│                                                                     │
│  ┌──────────────┐ ┌──────────────┐                                 │
│  │  MinIO/OSS   │ │   Jasypt     │                                 │
│  │  对象存储    │ │  配置加密    │                                 │
│  └──────────────┘ └──────────────┘                                 │
└─────────────────────────────────────────────────────────────────────┘
```

### 3.2 模块依赖关系

```
nexusix-boot (启动入口)
  ├── nexusix-iam (认证与权限)
  │     ├── nexusix-core (领域上下文)
  │     │     └── nexusix-common (公共工具)
  │     └── nexusix-common
  ├── nexusix-tenant (租户管理)
  │     ├── nexusix-core
  │     └── nexusix-common
  ├── nexusix-org (组织架构)
  │     ├── nexusix-core
  │     └── nexusix-common
  ├── nexusix-billing (计费)
  │     └── nexusix-common
  ├── nexusix-dynamic (动态表单)
  │     └── nexusix-common
  ├── nexusix-notify (通知)
  │     └── nexusix-common
  ├── nexusix-audit (审计)
  │     └── nexusix-common
  └── nexusix-system (系统配置)
        └── nexusix-common
```

### 3.3 技术选型

| 层级 | 技术 | 版本 | 选型理由 |
|------|------|------|----------|
| **语言** | Java | 17 | LTS版本，虚拟线程支持 |
| **框架** | Spring Boot | 3.3.4 | 生态成熟，虚拟线程就绪 |
| **ORM** | MyBatis-Plus | 3.5.12 | 灵活SQL，支持动态字段选择 |
| **数据库** | PostgreSQL | 17.5 | JSONB原生支持，RLS行级安全 |
| **缓存** | Redis | 7.x | 会话存储+业务缓存双库隔离 |
| **认证** | Sa-Token + JWT | 1.42.0 | 轻量级，多端适配 |
| **消息** | RabbitMQ | 3.x | 可靠投递，审计日志异步化 |
| **JSON** | Fastjson2 | 2.0.54 | 高性能JSON处理 |
| **API文档** | Knife4j | 4.5.0 | OpenAPI3规范 |
| **对象映射** | MapStruct | 1.5.2 | 编译期类型安全 |
| **ID生成** | Snowflake | - | 有序ID，分布式友好 |

### 3.4 数据库设计原则

1. **共享数据库逻辑隔离**: 所有租户共享同一数据库，通过 `tenant_id` 字段实现逻辑隔离
2. **物化路径模式**: 租户层级使用 `path` 字段（如 `/1/2/3/`）实现无限深度树
3. **JSONB扩展**: 租户扩展属性(`ext_attributes`)、字段权限配置(`field_operates`)使用JSONB存储
4. **逻辑删除**: 所有表使用 `is_deleted` + `deleted_at` 实现软删除
5. **雪花ID**: 主键使用雪花算法生成有序int8

---

## 4. 租户管理模块

### 4.1 功能清单

| 功能编号 | 功能名称 | 优先级 | 状态 | 描述 |
|----------|----------|--------|------|------|
| T-001 | 租户CRUD | P0 | ✅已实现 | 租户新增、修改、删除、详情查询 |
| T-002 | 租户列表查询 | P0 | ✅已实现 | 分页列表、条件筛选 |
| T-003 | 租户树查询 | P0 | ✅已实现 | 树形列表、树形分页 |
| T-004 | 租户批量操作 | P1 | ✅已实现 | 批量新增、修改、状态变更、删除 |
| T-005 | 租户状态管理 | P0 | ✅已实现 | 启用/禁用/过期状态切换 |
| T-006 | 子租户分配 | P0 | ✅已实现 | 创建子租户、调整父租户 |
| T-007 | 租户订阅管理 | P1 | ✅已实现 | 套餐订阅、续费、升降级 |
| T-008 | 租户自助注册 | P0 | ❌未实现 | 企业自助注册+平台审核 |
| T-009 | 租户配额管理 | P1 | ❌未实现 | 用户数/存储/功能模块配额 |
| T-010 | 租户数据迁移 | P2 | ❌未实现 | 租户数据导入导出 |

### 4.2 租户层级模型

> **设计决策**: 租户(Tenant)与部门(Dept)严格分离。租户是计费和数据隔离的边界，部门是租户内的组织架构节点。租户树仅包含集团/分公司等计费单元，部门在租户内部独立管理。

```
平台运营方 (PLAT) ─── 顶级租户，管理全局
  ├── 集团A (GRP_A) ─── 一级企业租户
  │     ├── 华东分公司 (GRP_A_EAST) ─── 二级分公司租户
  │     ├── 华南分公司 (GRP_A_SOUTH) ─── 二级分公司租户
  │     └── 华北分公司 (GRP_A_NORTH) ─── 二级分公司租户
  ├── 集团B (GRP_B) ─── 已停用租户
  ├── 集团C (GRP_C) ─── 已过期租户
  └── 集团D (GRP_D) ─── 教育/制造行业租户
        └── 教育事业部 (GRP_D_EDU) ─── 二级分公司租户

注意: 技术部、销售部、人事部、财务部、研发部等属于部门(Dept)，
在租户内部通过 sys_dept 表管理，不再作为子租户。
```

**租户 vs 部门的职责划分**:

| 维度 | 租户 (sys_tenant) | 部门 (sys_dept) |
|------|-------------------|-----------------|
| 核心职责 | 计费边界、数据隔离边界 | 组织架构、人员管理 |
| 层级深度 | 无限（物化路径） | 无限（物化路径） |
| 独立订阅 | 是 | 否（跟随所属租户） |
| 独立停用 | 是 | 否（跟随所属租户） |
| 数据隔离 | tenant_id 过滤 | dept_id 过滤（数据权限范围内） |
| 管理员 | 租户管理员 | 部门负责人 |

**物化路径设计**:
- 平台: `/1/`
- 集团A: `/1/2/`
- 华东分公司: `/1/2/3/`

**路径查询优势**:
- 查询某租户所有后代: `WHERE path LIKE '/1/2/%'` — 单索引扫描
- 查询某租户所有祖先: 拆分路径为ID列表 — O(深度)
- 环路检测: 更新parent_id时校验新路径不包含自身ID

### 4.3 租户状态机

> **设计决策**: 租户状态变更采用物理级联策略——停用租户时级联修改所有子租户状态，恢复时不自动级联。增加 `disable_reason` 字段区分禁用原因（自身禁用 vs 因父级级联禁用）。

```
                    ┌──────────┐
         注册审核通过 │          │ 审核拒绝
        ┌───────────│  PENDING  │──────────┐
        │           │  待审核   │          │
        │           └──────────┘          │
        ▼                                  ▼
  ┌──────────┐                      ┌──────────┐
  │ ENABLED  │◄──── 续费/恢复 ─────│ DISABLED │
  │  已启用  │──── 停用 ────────►│  已停用  │
  └────┬─────┘                      └──────────┘
       │ 过期                             ▲
       ▼                                  │
  ┌──────────┐                     级联停用
  │ EXPIRED  │──── 续费 ────► ENABLED    (父级停用时
  │  已过期  │                          子租户自动停用)
  └──────────┘
```

**级联规则**:
- 停用租户 → 所有子租户**物理级联停用**（修改子租户状态为DISABLED，`disable_reason` 记录为 `PARENT_CASCADE:{父租户ID}`）
- 恢复租户 → 子租户**不自动恢复**，需手动逐个恢复（因子租户可能有自身停用原因）
- 过期检测 → 定时任务（每小时）+ 登录时实时校验双保险

**禁用原因字段**:
```sql
ALTER TABLE sys_tenant ADD COLUMN "disable_reason" varchar(200);
-- 值示例:
-- "ADMIN_DISABLE"          - 管理员主动停用
-- "PARENT_CASCADE:2"       - 因父租户(ID=2)停用而级联停用
-- "SUBSCRIPTION_EXPIRED"   - 订阅过期
-- "VIOLATION"              - 违规停用
```

### 4.4 租户订阅模型

| 字段 | 说明 | 枚举值 |
|------|------|--------|
| `subscription_type` | 订阅类型 | `NEW`(新购) / `RENEWAL`(续费) / `UPGRADE`(升级) / `DOWNGRADE`(降级) |
| `status` | 订阅状态 | `ACTIVE`(生效中) / `PENDING`(待生效) / `EXPIRED`(已过期) / `CANCELLED`(已取消) |
| `source_type` | 订阅来源 | `DIRECT`(直购) / `PARENT_GRANT`(上级分配) / `ADMIN_ASSIGN`(管理员指派) |
| `is_auto_renew` | 自动续费 | `true` / `false` |

### 4.5 租户扩展属性设计

租户表 `ext_attributes` 字段（JSONB）用于存储行业定制化属性：

```json
{
  "industry": "电商",
  "scale": "500+",
  "region": "华东",
  "customFields": {
    "businessLicense": "91310000XXXX",
    "taxId": "310XXXXXXX",
    "contactAddress": "上海市浦东新区XXX"
  }
}
```

---

## 5. 权限控制体系

### 5.1 权限模型总览

NexusIX 实现了**四维权限控制模型**：

```
┌─────────────────────────────────────────────────────────┐
│                    权限控制体系                           │
├─────────────┬──────────────┬──────────────┬────────────┤
│  功能权限    │  数据权限     │  操作权限     │ 字段权限   │
│  (菜单/按钮) │  (行级过滤)   │  (API访问)    │ (列级控制) │
├─────────────┼──────────────┼──────────────┼────────────┤
│ MENU菜单    │ 租户数据隔离  │ API接口权限   │ QUERY查询  │
│ BUTTON按钮  │ 部门数据可见  │ 操作审批流    │ CREATE创建 │
│ API接口     │ 个人数据私有  │ 操作频率限制  │ UPDATE更新 │
└─────────────┴──────────────┴──────────────┴────────────┘
                          │
                    四级级联控制
          ┌──────────────┼──────────────┐
          │              │              │
     SYSTEM级        TENANT级       ROLE级  ──► USER级
     (最高优先)      (租户级)       (角色级)    (用户级)
     不可覆盖        可覆盖角色/用户  可覆盖用户   最低优先
```

### 5.2 四级级联权限模型（已实现）

#### 5.2.1 级联规则

**优先级**: SYSTEM > TENANT > ROLE > USER

| 级别 | 策略状态 | 说明 | 可否被下级覆盖 |
|------|----------|------|----------------|
| SYSTEM | `DISABLED_SYSTEM_LEVEL` | 系统管理员全局禁用 | **不可** |
| TENANT | `DISABLED_TENANT_LEVEL` | 租户管理员在租户层面禁用 | 不可被角色/用户覆盖 |
| ROLE | `DISABLED_ROLE_LEVEL` | 角色配置层面禁用 | 不可被用户覆盖 |
| USER | `DISABLED_USER_LEVEL` | 对特定用户单独禁用 | — (最低级) |
| — | `ACTIVE` | 权限正常生效 | — |

> **设计决策**: `sys_perm_policy.target_type` 增加 `SYSTEM` 类型，四级对应四种 target_type。系统级策略不需要 target_id（或固定为0），语义清晰，查询时按 target_type 分区。

#### 5.2.2 权限上下文构建流程（已实现）

```
用户登录
  │
  ▼
查询用户-租户关联 (sys_user_tenant_rel)
  │
  ▼
验证租户状态 (非DISABLED/EXPIRED)
  │
  ▼
查询用户所有权限策略 (sys_perm_policy + sys_user_perm_rel)
  │
  ▼
构建 UserContextDTO:
  ├── TenantInfo (tenantCode, tenantName)
  └── PermInfo
        ├── perms (全部权限编码)
        ├── validPerms (有效权限)
        ├── invalidPerms (无效权限)
        ├── CascadeDisabled (四级禁用详情)
        │     ├── systemDisabled
        │     ├── tenantDisabled
        │     ├── roleDisabled
        │     └── userDisabled
        └── FieldPerm (字段级权限)
              ├── query (Map<tableName, EntityFieldPerm>)
              ├── create
              └── update
```

#### 5.2.3 权限资源树（已实现）

`sys_perm` 表使用物化路径存储权限资源树：

| perm_type | 说明 | 示例 |
|-----------|------|------|
| `MENU` | 菜单权限 | 系统管理 > 用户管理 > 用户列表 |
| `BUTTON` | 按钮权限 | 新增用户、编辑用户、删除用户 |
| `API` | 接口权限 | 用户接口、角色接口 |

**权限编码规范**: `模块:资源:操作`，如 `system:user:add`

### 5.3 字段级权限控制（已实现）

#### 5.3.1 数据模型

`sys_perm_policy` 表核心字段：

| 字段 | 类型 | 说明 |
|------|------|------|
| `target_id` | int8 | 授权目标ID（租户ID/角色ID/用户ID） |
| `target_type` | varchar | 授权目标类型：`TENANT`/`ROLE`/`USER` |
| `perm_id` | int8 | 关联的权限资源ID |
| `tenant_id` | int8 | 所属租户ID |
| `table_name` | varchar | 控制的数据表名 |
| `access_type` | varchar | 访问类型：`QUERY`/`CREATE`/`UPDATE` |
| `field_operates` | jsonb | 允许操作的字段列表 |

#### 5.3.2 字段权限执行流程（已在SysTenantServiceImpl中实现）

```
1. 从 UserContextDTO.FieldPerm 获取当前操作类型对应的字段权限
2. 获取指定表名的 EntityFieldPerm
3. 合并 visibleFields（ACTIVE策略）和 disabledBy（按级别禁用的字段映射）
4. 查询时: SELECT 仅可见字段
5. 创建时: SET 仅允许字段
6. 更新时: SET 仅允许字段

> **设计决策**: `EntityFieldPerm` 将 `invisibleFields` 替换为 `disabledBy`（Map<级别, List<字段名>>），按级别记录字段禁用原因。前端可据此展示"该字段被租户级禁用"等提示信息，便于权限审计与排查。
```

#### 5.3.3 字段权限示例

| 策略 | 目标 | 表 | 操作 | 可见字段 | 状态 |
|------|------|-----|------|----------|------|
| POL-SYS-QUERY-USER | 全租户 | sys_user | QUERY | id, user_name, nick_name | ACTIVE |
| POL-SYS-ADD-USER-DIS | 全租户 | sys_user | CREATE | password, phone | DISABLED_SYSTEM_LEVEL |
| POL-TENANT-ADD-USER | 集团A | sys_user | CREATE | user_name, nick_name, email | ACTIVE |
| POL-BRANCH-QUERY-USER | 华东分公司 | sys_user | QUERY | id, user_name | ACTIVE |

**效果**: 华东分公司用户查询用户列表时，仅能看到 `id` 和 `user_name` 两个字段。

### 5.4 数据权限（行级过滤）— 待实现

> **设计决策**: 租户隔离与数据权限是**分层关系**，不是替代关系。租户隔离拦截器先限定"你属于哪个租户"，数据权限拦截器再限定"在租户内你能看什么范围"。两者叠加过滤，不冲突。

#### 5.4.1 数据范围定义

| 数据范围 | 编码 | 说明 |
|----------|------|------|
| 全部数据 | `ALL` | 可查看租户内所有数据 |
| 本级及下级 | `LEVEL_AND_CHILDREN` | 本租户及所有子租户数据 |
| 本级数据 | `LEVEL_ONLY` | 仅本租户数据 |
| 本部门数据 | `DEPT_ONLY` | 仅本部门数据 |
| 本部门及下级 | `DEPT_AND_CHILDREN` | 本部门及下属部门数据 |
| 个人数据 | `SELF_ONLY` | 仅本人创建的数据 |

#### 5.4.2 实现方案: MyBatis拦截器

```java
// 基于租户路径的行级过滤拦截器
@Intercepts({@Signature(type = Executor.class, method = "query", ...)})
public class DataScopeInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) {
        // 1. 从ThreadLocal获取当前用户数据范围
        // 2. 解析SQL，注入租户路径过滤条件
        // 3. WHERE tenant_id IN (子查询: 按path前缀匹配)
    }
}
```

#### 5.4.3 PostgreSQL RLS增强方案

```sql
-- 启用行级安全策略
ALTER TABLE biz_order ENABLE ROW LEVEL SECURITY;

-- 创建策略: 用户只能查看所属租户及子租户的数据
CREATE POLICY tenant_data_isolation ON biz_order
  USING (tenant_id::text IN (
    SELECT id::text FROM sys_tenant
    WHERE path LIKE (
      SELECT path || '%' FROM sys_tenant
      WHERE id = current_setting('app.current_tenant_id')::int8
    )
  ));
```

### 5.5 操作权限 — 待实现

#### 5.5.1 API权限校验

```java
// 基于注解的API权限校验
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresPerm {
    String value(); // 权限编码，如 "system:user:add"
    Logical logical() default Logical.AND; // 多权限逻辑关系
}

// 使用示例
@RequiresPerm("system:tenant:add")
@PostMapping("/add")
public Result<Void> add(@RequestBody TenantAddRTO rto) { ... }
```

#### 5.5.2 操作审批流

对于高风险操作（如删除租户、权限变更），需要审批流程：

| 操作 | 风险等级 | 审批要求 |
|------|----------|----------|
| 删除租户 | Critical | 二次确认 + 短信/邮件验证 |
| 权限策略变更 | High | 上级管理员审批 |
| 角色分配 | Medium | 租户管理员审批 |
| 普通操作 | Low | 无需审批 |

### 5.6 权限缓存策略

> **设计决策**: 权限变更采用分级一致性策略——权限收紧（禁用）同步刷新Session，权限放宽（启用）异步刷新。API层 `@RequiresPerm` 实时校验作为兜底防线。

```
┌─────────────────────────────────────────────┐
│              权限缓存架构                     │
│                                              │
│  登录时 ──► 构建UserContextDTO               │
│              │                               │
│              ▼                               │
│     Sa-Token Session (Redis DB1)             │
│     ├── 用户基础信息                          │
│     ├── 租户信息                             │
│     └── 完整权限上下文                        │
│                                              │
│  权限变更时:                                  │
│  ├── 权限收紧（禁用）──► 同步刷新Session      │
│  │   安全优先，宁可牺牲性能                   │
│  └── 权限放宽（启用）──► RabbitMQ异步刷新     │
│      延迟几秒可接受                          │
│                                              │
│  兜底防线:                                    │
│  API层 @RequiresPerm 实时校验数据库权限       │
│  即使Session未刷新，API调用仍被正确拦截       │
└─────────────────────────────────────────────┘
```

---

## 6. 多行业适配方案

> **设计决策**: 行业适配采用**低代码**方案（非零代码），分三层实现。MVP阶段只实现L1配置层，L2/L3作为后续迭代。MVP阶段只做2-3个行业模板（互联网+制造+教育），其余行业后续按需扩展。

### 6.1 行业适配架构

**三层低代码体系**：

| 层级 | 能力 | 实现方式 | MVP阶段 | 示例 |
|------|------|----------|---------|------|
| **L1: 配置层** | 权限集、表单字段、扩展属性 | 行业模板 + 动态表单 | ✅实现 | 医疗行业预置"患者管理"权限和表单 |
| **L2: 规则层** | 数据校验、字段联动、状态流转 | 规则引擎（JSON DSL） | ❌后续 | "工单状态=已完成时，不可修改数量" |
| **L3: 扩展层** | 自定义业务逻辑 | 插件接口 + 脚本引擎 | ❌后续 | 制造业的BOM计算逻辑 |

### 6.2 行业模板定义

#### 6.2.1 行业模板数据结构

```sql
CREATE TABLE "public"."sys_industry_template" (
    "id" int8 NOT NULL,
    "industry_code" varchar(50) NOT NULL,      -- 行业编码
    "industry_name" varchar(100) NOT NULL,     -- 行业名称
    "industry_desc" varchar(500) NOT NULL,     -- 行业描述
    "perm_template_id" int8 NOT NULL,          -- 默认权限模板ID
    "form_template_id" int8,                   -- 默认表单模板ID
    "default_ext_attributes" jsonb,            -- 默认扩展属性
    "default_tenant_type" varchar(50),         -- 默认租户类型
    "icon_url" varchar(500),                   -- 行业图标
    "sort_order" int4 DEFAULT 0,               -- 排序
    "status" varchar(20) NOT NULL,             -- 状态
    "create_by" int8 NOT NULL,
    "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "update_by" int8 NOT NULL,
    "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "is_deleted" varchar(20) NOT NULL DEFAULT 'NOT_DELETED',
    "deleted_at" timestamp(6)
);
```

#### 6.2.2 预置行业模板

| 行业编码 | 行业名称 | 特色权限 | 特色扩展属性 |
|----------|----------|----------|-------------|
| `INTERNET` | 互联网/电商 | 商品管理、订单管理、营销活动 | scale, platform_type |
| `MANUFACTURING` | 制造业 | 生产管理、质检管理、仓储管理 | factory_count, cert_type |
| `EDUCATION` | 教育 | 课程管理、学员管理、考试管理 | school_type, edu_level |
| `HEALTHCARE` | 医疗 | 患者管理、病历管理、药品管理 | hospital_level, license_no |
| `FINANCE` | 金融 | 交易管理、风控管理、合规审计 | license_type, risk_level |
| `RETAIL` | 零售 | 门店管理、库存管理、会员管理 | store_count, pos_type |
| `LOGISTICS` | 物流 | 运单管理、车辆管理、仓储管理 | fleet_size, route_type |
| `GOVERNMENT` | 政府 | 公文管理、审批流程、信息公开 | org_level, gov_dept |

### 6.3 动态表单引擎

#### 6.3.1 表单模板定义

```sql
CREATE TABLE "public"."sys_form_template" (
    "id" int8 NOT NULL,
    "form_code" varchar(100) NOT NULL,         -- 表单编码
    "form_name" varchar(100) NOT NULL,         -- 表单名称
    "form_type" varchar(20) NOT NULL,          -- 表单类型: TENANT/USER/BIZ
    "industry_code" varchar(50),               -- 关联行业
    "form_schema" jsonb NOT NULL,              -- 表单Schema (JSON)
    "table_name" varchar(64),                  -- 关联数据表
    "version" int4 DEFAULT 1,                  -- 版本号
    "status" varchar(20) NOT NULL,
    "create_by" int8 NOT NULL,
    "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "update_by" int8 NOT NULL,
    "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "is_deleted" varchar(20) NOT NULL DEFAULT 'NOT_DELETED',
    "deleted_at" timestamp(6)
);
```

#### 6.3.2 表单Schema示例

```json
{
  "fields": [
    {
      "key": "businessLicense",
      "label": "营业执照号",
      "type": "text",
      "required": true,
      "pattern": "^[0-9A-Z]{18}$",
      "placeholder": "请输入18位统一社会信用代码"
    },
    {
      "key": "companyScale",
      "label": "企业规模",
      "type": "select",
      "required": true,
      "options": [
        {"value": "1-50", "label": "1-50人"},
        {"value": "51-200", "label": "51-200人"},
        {"value": "201-500", "label": "201-500人"},
        {"value": "500+", "label": "500人以上"}
      ]
    },
    {
      "key": "certExpiryDate",
      "label": "资质到期日",
      "type": "date",
      "required": false
    }
  ],
  "layout": "vertical",
  "submitText": "提交注册"
}
```

### 6.4 行业适配流程

```
1. 租户注册时选择行业
       │
       ▼
2. 加载行业模板
   ├── 默认权限集 → 自动创建权限策略
   ├── 默认表单 → 生成行业定制表单
   ├── 默认扩展属性 → 填充ext_attributes
   └── 默认租户类型 → 设置tenant_type
       │
       ▼
3. 租户管理员自定义
   ├── 调整权限策略
   ├── 修改表单字段
   └── 添加自定义扩展属性
       │
       ▼
4. 运行时动态渲染
   ├── 前端根据form_schema渲染表单
   ├── 后端根据field_perm控制字段
   └── 查询根据data_scope过滤数据
```

---

## 7. 用户角色管理

### 7.1 角色模型

#### 7.1.1 角色表设计

```sql
CREATE TABLE "public"."sys_role" (
    "id" int8 NOT NULL,
    "role_code" varchar(100) NOT NULL,         -- 角色编码
    "role_name" varchar(100) NOT NULL,         -- 角色名称
    "role_desc" varchar(500) NOT NULL,         -- 角色描述
    "tenant_id" int8 NOT NULL,                 -- 所属租户ID
    "data_scope" varchar(30) NOT NULL,         -- 数据范围
    "sort_order" int4 DEFAULT 0,               -- 排序
    "status" varchar(20) NOT NULL,             -- 状态
    "create_by" int8 NOT NULL,
    "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "update_by" int8 NOT NULL,
    "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "is_deleted" varchar(20) NOT NULL DEFAULT 'NOT_DELETED',
    "deleted_at" timestamp(6)
);
```

#### 7.1.2 角色类型

> **设计决策**: 角色分两层——系统预置角色（`tenant_id = 0`，只读不可修改）+ 租户自定义角色（`tenant_id = 具体租户`，可自由定制）。行业模板中的角色作为"角色模板"存储，租户注册时从模板复制为租户自定义角色。每个用户在不同租户中可有不同的角色。

| 角色类型 | 编码 | 说明 | 创建者 | tenant_id |
|----------|------|------|--------|-----------|
| 系统预置角色 | `SYS_*` | 平台级角色，只读不可删除/修改 | 平台管理员 | 0 |
| 租户自定义角色 | `CUSTOM_*` | 租户自定义角色，可自由定制 | 租户管理员 | 具体租户ID |

#### 7.1.3 预置角色

| 角色 | 编码 | 数据范围 | 说明 |
|------|------|----------|------|
| 超级管理员 | `SYS_SUPER_ADMIN` | ALL | 平台最高权限 |
| 平台运营 | `SYS_OPERATOR` | ALL | 平台日常运营 |
| 租户管理员 | `TENANT_ADMIN` | LEVEL_AND_CHILDREN | 租户内最高权限 |
| 部门管理员 | `DEPT_ADMIN` | DEPT_AND_CHILDREN | 部门管理权限 |
| 普通用户 | `NORMAL_USER` | SELF_ONLY | 基础操作权限 |
| 审计员 | `AUDITOR` | LEVEL_ONLY | 只读+审计权限 |

### 7.2 角色-权限关联

```sql
CREATE TABLE "public"."sys_role_perm_rel" (
    "id" int8 NOT NULL,
    "role_id" int8 NOT NULL,                   -- 角色ID
    "perm_id" int8 NOT NULL,                   -- 权限资源ID
    "tenant_id" int8 NOT NULL,                 -- 所属租户
    "create_by" int8 NOT NULL,
    "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "update_by" int8 NOT NULL,
    "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "is_deleted" varchar(20) NOT NULL DEFAULT 'NOT_DELETED',
    "deleted_at" timestamp(6)
);
```

### 7.3 用户-角色关联

```sql
CREATE TABLE "public"."sys_user_role_rel" (
    "id" int8 NOT NULL,
    "user_id" int8 NOT NULL,                   -- 用户ID
    "role_id" int8 NOT NULL,                   -- 角色ID
    "tenant_id" int8 NOT NULL,                 -- 所属租户（用户可在多租户有不同角色）
    "create_by" int8 NOT NULL,
    "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "update_by" int8 NOT NULL,
    "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "is_deleted" varchar(20) NOT NULL DEFAULT 'NOT_DELETED',
    "deleted_at" timestamp(6)
);
```

### 7.4 组织架构

#### 7.4.1 部门表

```sql
CREATE TABLE "public"."sys_dept" (
    "id" int8 NOT NULL,
    "dept_code" varchar(100) NOT NULL,
    "dept_name" varchar(100) NOT NULL,
    "tenant_id" int8 NOT NULL,
    "parent_id" int8 NOT NULL,
    "path" varchar(1000) NOT NULL,             -- 物化路径
    "leader_id" int8,                          -- 部门负责人
    "sort_order" int4 DEFAULT 0,
    "status" varchar(20) NOT NULL,
    "create_by" int8 NOT NULL,
    "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "update_by" int8 NOT NULL,
    "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "is_deleted" varchar(20) NOT NULL DEFAULT 'NOT_DELETED',
    "deleted_at" timestamp(6)
);
```

#### 7.4.2 岗位表

```sql
CREATE TABLE "public"."sys_post" (
    "id" int8 NOT NULL,
    "post_code" varchar(100) NOT NULL,
    "post_name" varchar(100) NOT NULL,
    "post_level" int4 NOT NULL,                -- 岗位级别
    "tenant_id" int8 NOT NULL,
    "sort_order" int4 DEFAULT 0,
    "status" varchar(20) NOT NULL,
    "create_by" int8 NOT NULL,
    "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "update_by" int8 NOT NULL,
    "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "is_deleted" varchar(20) NOT NULL DEFAULT 'NOT_DELETED',
    "deleted_at" timestamp(6)
);
```

#### 7.4.3 用户组

```sql
CREATE TABLE "public"."sys_user_group" (
    "id" int8 NOT NULL,
    "group_code" varchar(100) NOT NULL,
    "group_name" varchar(100) NOT NULL,
    "tenant_id" int8 NOT NULL,
    "group_type" varchar(20) NOT NULL,         -- STATIC/DYNAMIC
    "status" varchar(20) NOT NULL,
    "create_by" int8 NOT NULL,
    "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "update_by" int8 NOT NULL,
    "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "is_deleted" varchar(20) NOT NULL DEFAULT 'NOT_DELETED',
    "deleted_at" timestamp(6)
);
```

### 7.5 权限计算流程

```
用户请求操作
  │
  ▼
1. 获取用户上下文 (UserContextDTO)
  │
  ▼
2. 合并权限来源:
   ├── 用户直接关联的策略 (sys_user_perm_rel)
   ├── 角色关联的策略 (sys_role_perm_rel → sys_perm_policy)
   └── 租户级策略 (target_type=TENANT)
  │
  ▼
3. 级联禁用计算:
   ├── 系统级禁用 → 标记为不可恢复
   ├── 租户级禁用 → 标记为租户级不可用
   ├── 角色级禁用 → 标记为角色级不可用
   └── 用户级禁用 → 标记为用户级不可用
  │
  ▼
4. 生成最终权限集:
   ├── validPerms: 可用权限编码集合
   ├── invalidPerms: 不可用权限编码集合
   └── fieldPerm: 字段级权限映射
  │
  ▼
5. 鉴权判断:
   ├── 功能权限: permCode ∈ validPerms ?
   ├── 数据权限: dataScope 过滤
   ├── 操作权限: @RequiresPerm 校验
   └── 字段权限: 动态字段选择
```

---

## 8. 租户个性化配置

### 8.1 配置体系

```
┌─────────────────────────────────────────────────────┐
│              租户个性化配置体系                        │
│                                                      │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐ │
│  │  UI配置     │  │  功能配置    │  │  业务配置    │ │
│  │  主题/Logo  │  │  模块开关    │  │  流程/规则   │ │
│  └─────────────┘  └─────────────┘  └─────────────┘ │
│                                                      │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐ │
│  │  通知配置    │  │  安全配置    │  │  集成配置    │ │
│  │  模板/渠道  │  │  策略/规则   │  │  第三方API   │ │
│  └─────────────┘  └─────────────┘  └─────────────┘ │
└─────────────────────────────────────────────────────┘
```

### 8.2 租户配置表

```sql
CREATE TABLE "public"."sys_tenant_config" (
    "id" int8 NOT NULL,
    "tenant_id" int8 NOT NULL,                 -- 租户ID
    "config_key" varchar(200) NOT NULL,        -- 配置键
    "config_value" jsonb NOT NULL,             -- 配置值 (JSON)
    "config_type" varchar(30) NOT NULL,        -- 配置类型
    "config_desc" varchar(500),                -- 配置描述
    "is_inheritable" bool DEFAULT false,       -- 子租户是否可继承
    "status" varchar(20) NOT NULL,
    "create_by" int8 NOT NULL,
    "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "update_by" int8 NOT NULL,
    "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "is_deleted" varchar(20) NOT NULL DEFAULT 'NOT_DELETED',
    "deleted_at" timestamp(6)
);
```

### 8.3 配置项清单

#### 8.3.1 UI配置

| 配置键 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| `ui.theme` | string | `default` | 主题名称 |
| `ui.primary_color` | string | `#1890ff` | 主色调 |
| `ui.logo_url` | string | - | 租户Logo |
| `ui.login_background` | string | - | 登录页背景 |
| `ui.favicon` | string | - | 网站图标 |
| `ui.copyright` | string | - | 底部版权信息 |

#### 8.3.2 功能配置

| 配置键 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| `feature.modules` | array | `[]` | 已启用的功能模块列表 |
| `feature.max_users` | int | `100` | 最大用户数 |
| `feature.max_storage_mb` | int | `1024` | 最大存储空间(MB) |
| `feature.sso_enabled` | bool | `false` | 是否启用SSO |
| `feature.mfa_enabled` | bool | `false` | 是否启用MFA |

#### 8.3.3 安全配置

| 配置键 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| `security.password_policy` | object | `{minLength:8}` | 密码策略 |
| `security.login_attempts` | int | `5` | 登录失败锁定次数 |
| `security.lock_duration_min` | int | `30` | 锁定时长(分钟) |
| `security.session_timeout_min` | int | `120` | 会话超时(分钟) |
| `security.ip_whitelist` | array | `[]` | IP白名单 |

#### 8.3.4 通知配置

| 配置键 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| `notify.channels` | array | `["system"]` | 启用的通知渠道 |
| `notify.email_template_id` | int | - | 邮件通知模板 |
| `notify.sms_sign_name` | string | - | 短信签名 |
| `notify.webhook_url` | string | - | Webhook回调地址 |

### 8.4 配置继承机制

> **设计决策**: 配置继承查询采用Redis预计算缓存策略。配置变更时递归计算所有子租户的最终配置值并缓存到Redis，查询时直接读Redis（O(1)），避免沿租户路径逐级查询数据库。

```
平台默认配置
  │
  ▼ 继承 + 覆盖
一级租户配置
  │
  ▼ 继承 + 覆盖
二级租户配置
  │
  ▼ 继承 + 覆盖
三级租户配置
```

**继承规则**:
- `is_inheritable = true` 的配置项可被子租户继承
- 子租户可覆盖父租户的配置
- 查询配置时直接从Redis读取预计算的最终配置值

**Redis缓存策略**:
- 缓存Key: `tenant:config:{tenant_id}:{config_key}` → 最终配置值
- 写入时: 递归计算当前租户及所有子租户的最终配置值，批量更新Redis
- 查询时: 直接读Redis，未命中则从数据库计算并回填
- 失效: 配置变更时主动刷新受影响租户的缓存

---

## 9. 数据隔离机制

### 9.1 隔离策略选择

| 策略 | 隔离级别 | 性能 | 成本 | 安全性 | 适用场景 |
|------|----------|------|------|--------|----------|
| **独立数据库** | 最高 | 最好 | 最高 | 最高 | 金融/医疗合规 |
| **共享数据库独立Schema** | 高 | 好 | 中 | 高 | 中大型企业 |
| **共享数据库逻辑隔离** ✅ | 中 | 中 | 最低 | 中 | 通用SaaS |

**NexusIX选择**: 共享数据库逻辑隔离（默认），支持按需升级为Schema隔离。

### 9.2 逻辑隔离实现

#### 9.2.1 租户ID字段

所有业务表必须包含 `tenant_id` 字段：

```sql
-- 业务表标准字段
CREATE TABLE "public"."biz_order" (
    "id" int8 NOT NULL,
    "tenant_id" int8 NOT NULL,                 -- 租户ID，逻辑隔离核心字段
    -- ... 业务字段 ...
);
```

#### 9.2.2 MyBatis-Plus租户拦截器

```java
@Component
public class TenantLineInnerInterceptor extends BaseMultiTableInnerInterceptor {
    @Override
    public void beforeQuery(Executor executor, ...) {
        // 1. 从TenantContext获取当前租户ID
        // 2. 自动为SQL添加 WHERE tenant_id = ?
        // 3. 支持忽略特定表（sys_user, sys_perm等全局表）
    }

    @Override
    public void beforeInsert(Executor executor, ...) {
        // 自动填充 tenant_id 字段
    }
}
```

#### 9.2.3 忽略租户隔离的表

| 表名 | 原因 |
|------|------|
| `sys_user` | 用户跨租户共享 |
| `sys_perm` | 权限资源全局共享 |
| `sys_user_tenant_rel` | 用户-租户关联表本身 |
| `sys_tenant` | 租户表本身 |

### 9.3 PostgreSQL RLS增强

对于高安全要求场景，启用PostgreSQL行级安全策略：

```sql
-- 1. 启用RLS
ALTER TABLE biz_order ENABLE ROW LEVEL SECURITY;

-- 2. 创建租户隔离策略
CREATE POLICY tenant_isolation ON biz_order
  USING (tenant_id = current_setting('app.current_tenant_id')::int8);

-- 3. 管理员绕过策略
CREATE POLICY admin_bypass ON biz_order
  USING (current_setting('app.is_admin')::boolean = true);
```

### 9.4 数据隔离验证

| 验证项 | 方法 | 预期结果 |
|--------|------|----------|
| 查询隔离 | 租户A用户查询数据 | 仅返回租户A的数据 |
| 写入隔离 | 租户A用户创建数据 | tenant_id自动填充为租户A |
| 跨租户访问 | 租户A用户访问租户B数据 | 返回空或403 |
| 超级管理员 | 平台管理员查询 | 可查看所有租户数据 |
| 子租户继承 | 父租户查询子租户数据 | 根据data_scope决定 |

---

## 10. 业务流程

### 10.1 租户注册流程

```
┌─────────┐    ┌─────────┐    ┌─────────┐    ┌─────────┐    ┌─────────┐
│  企业    │    │  平台    │    │  系统    │    │  租户    │    │  系统    │
│  用户    │    │  管理员  │    │  自动    │    │  管理员  │    │  自动    │
└────┬────┘    └────┬────┘    └────┬────┘    └────┬────┘    └────┬────┘
     │              │              │              │              │
     │ 1.填写注册信息│              │              │              │
     │ (行业/规模等) │              │              │              │
     ├─────────────►│              │              │              │
     │              │ 2.审核注册    │              │              │
     │              │ 信息         │              │              │
     │              ├───┐          │              │              │
     │              │   │审核通过   │              │              │
     │              │◄──┘          │              │              │
     │              │              │              │              │
     │              │ 3.创建租户    │              │              │
     │              ├─────────────►│              │              │
     │              │              │ 4.加载行业模板│              │
     │              │              ├───┐          │              │
     │              │              │   │创建权限   │              │
     │              │              │   │策略/表单  │              │
     │              │              │◄──┘          │              │
     │              │              │              │              │
     │              │              │ 5.创建租户    │              │
     │              │              │ 管理员账号    │              │
     │              │              ├─────────────►│              │
     │              │              │              │ 6.初始化配置  │
     │              │              │              ├─────────────►│
     │              │              │              │              │
     │ 7.发送注册    │              │              │              │
     │ 成功通知     │              │              │              │
     │◄─────────────┤              │              │              │
     │              │              │              │              │
     │ 8.首次登录    │              │              │              │
     │ 修改密码     │              │              │              │
     ├─────────────►│              │              │              │
```

### 10.2 用户登录与权限加载流程

```
用户提交登录请求
  │
  ▼
检查是否已登录 (Sa-Token)
  │── 是 → 直接返回用户上下文
  │── 否 ↓
  │
  ▼
查询用户信息 + 默认租户 (sys_user JOIN sys_user_tenant_rel JOIN sys_tenant)
  │
  ▼
验证租户状态 (非DISABLED/EXPIRED)
  │── 失败 → 强制弹出租户选择弹窗，用户必须选择一个可用租户
  │── 通过 ↓
  │
  ▼
验证密码 (BCrypt比对)
  │── 失败 → 累计失败次数，超限锁定
  │── 通过 ↓
  │
  ▼
Sa-Token登录 (StpUtil.login())
  │
  ▼
查询用户所有权限策略
  ├── 直接关联策略 (sys_user_perm_rel)
  ├── 角色关联策略 (sys_user_role_rel → sys_role_perm_rel → sys_perm_policy)
  └── 租户级策略 (target_type=TENANT)
  │
  ▼
构建UserContextDTO
  ├── TenantInfo
  ├── PermInfo
  │     ├── perms / validPerms / invalidPerms
  │     ├── CascadeDisabled (四级)
  │     └── FieldPerm (字段级)
  │
  ▼
存入Sa-Token Session (Redis DB1)
  │
  ▼
返回登录成功 + Token + 用户上下文
```

> **设计决策**: 权限上下文完整存储在Sa-Token Session（Redis DB1）中，Token仅含用户ID。切换租户时调用后端接口重建Session。默认租户不可用时强制弹出租户选择弹窗。
```

### 10.3 权限变更流程

```
管理员修改权限策略
  │
  ▼
校验操作权限 (@RequiresPerm)
  │
  ▼
持久化变更 (数据库)
  │
  ▼
发布权限变更消息 (RabbitMQ)
  │
  ▼
消费者接收消息
  │
  ▼
刷新受影响用户的Session
  ├── 直接关联用户: 刷新Session
  ├── 角色关联用户: 刷新所有该角色用户的Session
  └── 租户关联用户: 刷新所有该租户用户的Session
  │
  ▼
用户下次请求时使用新权限
```

### 10.4 租户切换流程

```
用户请求切换租户
  │
  ▼
验证用户是否属于目标租户 (sys_user_tenant_rel)
  │── 不属于 → 返回403
  │── 属于 ↓
  │
  ▼
验证目标租户状态 (非DISABLED/EXPIRED)
  │
  ▼
重新构建目标租户的权限上下文
  │
  ▼
更新Sa-Token Session
  │
  ▼
返回切换成功 + 新用户上下文
```

---

## 11. 界面原型说明

### 11.1 全局布局

```
┌─────────────────────────────────────────────────────────────┐
│  [Logo]  NexusIX Platform    [租户选择器▼]  [通知🔔] [用户▼] │
├──────────┬──────────────────────────────────────────────────┤
│          │                                                   │
│  导航菜单 │              主内容区域                            │
│          │                                                   │
│  ▸ 系统管理│                                                   │
│    用户管理│  ┌──────────────────────────────────────────┐   │
│    角色管理│  │  面包屑: 系统管理 > 用户管理               │   │
│    租户管理│  ├──────────────────────────────────────────┤   │
│    权限管理│  │  [新增] [批量删除] [导出]    搜索: [____]  │   │
│  ▸ 业务模块│  ├──────────────────────────────────────────┤   │
│    订单管理│  │  ☑ │ 用户名 │ 昵称  │ 邮箱  │ 状态 │ 操作 │   │
│    商品管理│  │  ☐ │ admin │ 超管  │ a@n.c │ 启用 │ ... │   │
│  ▸ 个性化 │  │  ☐ │ zhang │ 张总  │ z@g.c │ 启用 │ ... │   │
│    主题设置│  │  ...                                     │   │
│    表单配置│  ├──────────────────────────────────────────┤   │
│          │  │  共20条  < 1 2 3 4 5 >  每页[10▼]         │   │
│          │  └──────────────────────────────────────────┘   │
│          │                                                   │
├──────────┴──────────────────────────────────────────────────┤
│  © 2026 NexusIX Platform  │  当前租户: 集团A  │  v1.0.0     │
└─────────────────────────────────────────────────────────────┘
```

### 11.2 核心页面说明

#### 11.2.1 租户管理页面

**路由**: `/system/tenant`

| 区域 | 元素 | 说明 |
|------|------|------|
| 工具栏 | [新增租户] [批量操作▼] [切换视图] | 视图切换: 列表/树形 |
| 筛选区 | 租户名称/编码/状态/行业 | 支持组合筛选 |
| 列表区 | 租户树形表格 | 展示层级关系，支持展开/折叠 |
| 操作列 | [详情] [编辑] [子租户] [权限] [更多▼] | 更多: 停用/启用/删除 |

**树形视图**:
```
▼ 平台运营方 (PLAT) - 旗舰版 - 启用
  ▼ 集团A (GRP_A) - 企业版 - 启用
    ▼ 华东分公司 (GRP_A_EAST) - 商业版 - 启用
      ● 技术部 (GRP_A_EAST_TECH) - 基础版 - 启用
      ● 销售部 (GRP_A_EAST_SALE) - 基础版 - 启用
      ● 人事部 (GRP_A_EAST_HR) - 基础版 - 启用
    ▼ 华南分公司 (GRP_A_SOUTH) - 商业版 - 启用
      ● 财务部 (GRP_A_SOUTH_FIN) - 基础版 - 启用
  ● 集团B (GRP_B) - 基础版 - 已停用
  ● 集团C (GRP_C) - 基础版 - 已过期
```

#### 11.2.2 权限策略配置页面

**路由**: `/system/perm/policy`

| 区域 | 元素 | 说明 |
|------|------|------|
| 左侧 | 权限资源树 | MENU/BUTTON/API三级树 |
| 右侧 | 策略列表 | 选中权限节点后展示关联策略 |
| 策略表单 | 策略编辑 | 目标类型/目标/表名/操作类型/字段配置 |

**字段权限配置器**:
```
┌─────────────────────────────────────────────────────┐
│  字段权限配置 - sys_user 表 - QUERY 操作              │
│                                                      │
│  ☑ id          ☑ user_name     ☐ password            │
│  ☑ nick_name   ☐ email         ☑ phone               │
│  ☐ avatar      ☐ status        ☐ login_ip            │
│  ☐ login_date  ☐ create_by     ☐ create_at           │
│                                                      │
│  [全选] [全不选] [反选]              [保存] [取消]    │
└─────────────────────────────────────────────────────┘
```

#### 11.2.3 角色管理页面

**路由**: `/system/role`

| 区域 | 元素 | 说明 |
|------|------|------|
| 角色列表 | 角色卡片/列表 | 角色编码/名称/数据范围/用户数 |
| 权限分配 | 权限树勾选 | 勾选MENU/BUTTON/API权限 |
| 数据范围 | 下拉选择 | ALL/LEVEL_AND_CHILDREN/... |
| 成员管理 | 用户列表 | 角色-用户关联管理 |

#### 11.2.4 租户切换组件

```
┌──────────────────────────────────┐
│  切换租户                         │
│                                   │
│  ● 集团A (默认)                   │
│  ○ 集团A-华东分公司               │
│  ○ 集团A-华南分公司               │
│                                   │
│  搜索: [输入租户名称...]           │
│                                   │
│           [确认切换] [取消]        │
└──────────────────────────────────┘
```

---

## 12. 非功能需求

### 12.1 性能要求

| 指标 | 目标值 | 测量方法 |
|------|--------|----------|
| 登录响应时间 | < 500ms (P95) | 从请求到Token返回 |
| 权限校验延迟 | < 10ms (P99) | 单次权限检查耗时 |
| 列表查询响应 | < 200ms (P95) | 100条数据分页查询 |
| 树形查询响应 | < 300ms (P95) | 1000节点租户树 |
| 并发登录 | 500 QPS | 同时登录请求 |
| 并发查询 | 2000 QPS | 常规业务查询 |
| 权限上下文大小 | < 50KB | UserContextDTO序列化后 |
| 数据库连接池 | 20-50 | HikariCP配置 |

### 12.2 可用性要求

| 指标 | 目标值 |
|------|--------|
| 系统可用性 | 99.9% (年停机 < 8.76小时) |
| 故障恢复时间 (RTO) | < 30分钟 |
| 数据恢复点 (RPO) | < 5分钟 |
| 计划维护窗口 | 每月1次，凌晨2:00-4:00 |

### 12.3 可扩展性要求

| 维度 | 目标 |
|------|------|
| 租户数量 | 支持10,000+租户 |
| 单租户用户数 | 支持10,000+用户 |
| 租户层级深度 | 支持20+层 |
| 权限策略数 | 单租户支持1,000+策略 |
| 数据量 | 单表支持1亿+行 |

### 12.4 兼容性要求

| 维度 | 要求 |
|------|------|
| 浏览器 | Chrome 90+, Firefox 88+, Safari 14+, Edge 90+ |
| 移动端 | iOS 14+, Android 10+ |
| API协议 | RESTful + OpenAPI 3.0 |
| 字符编码 | UTF-8 |
| 时区 | 支持多时区，默认Asia/Shanghai |

---

## 13. 安全要求

### 13.1 认证安全

| 安全项 | 要求 | 实现方案 |
|--------|------|----------|
| 密码存储 | BCrypt加密，cost factor ≥ 10 | 替换当前明文比对 |
| 密码策略 | 最少8位，含大小写+数字+特殊字符 | 正则校验 |
| 登录防暴破 | 5次失败锁定30分钟 | Redis计数器 |
| 会话管理 | JWT + Redis双校验 | Sa-Token配置 |
| 多端互踢 | 同一账号仅允许一个活跃会话 | Sa-Token配置 |
| MFA | 可选TOTP二次验证 | Google Authenticator兼容 |

### 13.2 传输安全

| 安全项 | 要求 |
|--------|------|
| HTTPS | 全站强制HTTPS |
| CORS | 白名单域名，禁止 * |
| CSP | Content-Security-Policy 头 |
| XSS | 输入过滤 + 输出编码 |
| CSRF | SameSite Cookie + Token |

### 13.3 数据安全

| 安全项 | 要求 | 实现方案 |
|--------|------|----------|
| 敏感字段加密 | 手机号/身份证/银行卡加密存储 | AES-256-GCM |
| 数据脱敏 | 查询返回时按字段权限脱敏 | FieldPerm控制 |
| SQL注入 | 参数化查询 | MyBatis-Plus默认 |
| 数据备份 | 每日全量 + 实时WAL | PostgreSQL pg_basebackup |
| 数据导出 | 导出操作审计 + 水印 | 审计日志 + 动态水印 |

### 13.4 操作安全

| 安全项 | 要求 |
|--------|------|
| 操作审计 | 所有写操作记录审计日志 |
| 敏感操作二次验证 | 删除/权限变更需短信/邮件验证 |
| 权限变更审批 | 高风险权限变更需上级审批 |
| 登录日志 | 记录IP/设备/时间/结果 |
| 异常检测 | 异地登录/异常时间登录告警 |

### 13.5 合规要求

| 标准 | 适用行业 | 要求 |
|------|----------|------|
| 等保2.0 | 全行业 | 三级及以上 |
| GDPR | 国际业务 | 数据主体权利、数据跨境 |
| HIPAA | 医疗 | 患者数据保护 |
| PCI-DSS | 金融 | 支付卡数据保护 |
| SOC2 | SaaS | 安全/可用/保密 |

---

## 14. 验收标准

### 14.1 功能验收

#### 14.1.1 租户管理验收

| 编号 | 验收项 | 前置条件 | 操作步骤 | 预期结果 |
|------|--------|----------|----------|----------|
| T-AT-001 | 创建租户 | 管理员已登录 | 填写租户信息并提交 | 租户创建成功，自动生成物化路径 |
| T-AT-002 | 租户树展示 | 存在多层级租户 | 访问租户管理页面 | 正确展示树形结构，层级关系正确 |
| T-AT-003 | 租户状态变更 | 租户已启用 | 停用租户 | 租户状态变为DISABLED，其下用户无法登录 |
| T-AT-004 | 租户过期 | 租户到期时间已过 | 系统自动检查 | 过期租户状态变为EXPIRED，用户无法操作 |
| T-AT-005 | 子租户创建 | 父租户已存在 | 在父租户下创建子租户 | 子租户path正确，has_children更新 |
| T-AT-006 | 租户层级调整 | 子租户已存在 | 修改子租户父级 | 路径更新，环路检测生效 |

#### 14.1.2 权限控制验收

| 编号 | 验收项 | 前置条件 | 操作步骤 | 预期结果 |
|------|--------|----------|----------|----------|
| P-AT-001 | 功能权限校验 | 用户无"新增用户"权限 | 点击新增用户按钮 | 按钮不可见或点击返回403 |
| P-AT-002 | 系统级禁用 | 权限被系统级禁用 | 租户管理员尝试启用 | 无法启用，提示系统级禁用 |
| P-AT-003 | 租户级禁用 | 权限被租户级禁用 | 角色管理员尝试启用 | 无法启用，提示租户级禁用 |
| P-AT-004 | 字段权限-查询 | 用户仅可见id/user_name | 查询用户列表 | 返回数据仅含id和user_name字段 |
| P-AT-005 | 字段权限-创建 | 用户仅可填user_name/nick_name | 创建用户 | 仅指定字段被写入，其他字段忽略 |
| P-AT-006 | 字段权限-更新 | 用户不可修改email | 更新用户email | email字段更新被忽略 |
| P-AT-007 | 数据权限-行级 | 用户数据范围为DEPT_ONLY | 查询数据列表 | 仅返回本部门数据 |

#### 14.1.3 多租户隔离验收

| 编号 | 验收项 | 前置条件 | 操作步骤 | 预期结果 |
|------|--------|----------|----------|----------|
| I-AT-001 | 数据查询隔离 | 租户A/B各有数据 | 租户A用户查询 | 仅返回租户A数据 |
| I-AT-002 | 数据写入隔离 | 用户属于租户A | 创建业务数据 | tenant_id自动填充为租户A |
| I-AT-003 | 跨租户禁止 | 用户仅属于租户A | 尝试访问租户B数据 | 返回403或空数据 |
| I-AT-004 | 租户切换 | 用户属于多个租户 | 切换到租户B | 权限上下文重新加载为租户B的权限 |

### 14.2 性能验收

| 编号 | 验收项 | 测试方法 | 通过标准 |
|------|--------|----------|----------|
| PER-001 | 登录性能 | JMeter 500并发登录 | P95 < 500ms |
| PER-002 | 权限校验性能 | 单次权限检查压测 | P99 < 10ms |
| PER-003 | 列表查询性能 | 100条数据分页查询 | P95 < 200ms |
| PER-004 | 租户树性能 | 1000节点树查询 | P95 < 300ms |
| PER-005 | 权限上下文大小 | 序列化UserContextDTO | < 50KB |

### 14.3 安全验收

| 编号 | 验收项 | 测试方法 | 通过标准 |
|------|--------|----------|----------|
| SEC-001 | 密码加密 | 检查数据库密码字段 | BCrypt哈希，非明文 |
| SEC-002 | SQL注入 | 输入恶意SQL参数 | 参数化查询，无注入 |
| SEC-003 | 越权访问 | 伪造请求访问其他租户 | 返回403 |
| SEC-004 | 会话劫持 | 尝试使用过期Token | Token验证失败 |
| SEC-005 | 暴力破解 | 连续5次错误密码 | 账号锁定30分钟 |

---

## 15. 附录

### 15.1 现有数据库表结构

| 表名 | 说明 | 状态 |
|------|------|------|
| `sys_tenant` | 租户信息表 | ✅已实现 |
| `sys_tenant_subscription` | 租户订阅记录表 | ✅已实现 |
| `sys_user` | 用户基础信息表 | ✅已实现 |
| `sys_perm` | 权限资源表 | ✅已实现 |
| `sys_perm_policy` | 权限策略表 | ✅已实现 |
| `sys_user_perm_rel` | 用户-策略关联表 | ✅已实现 |
| `sys_user_tenant_rel` | 用户-租户关联表 | ✅已实现 |

### 15.2 待实现数据库表

| 表名 | 说明 | 优先级 |
|------|------|--------|
| `sys_role` | 角色表 | P0 |
| `sys_role_perm_rel` | 角色-权限关联表 | P0 |
| `sys_user_role_rel` | 用户-角色关联表 | P0 |
| `sys_dept` | 部门表 | P0 |
| `sys_post` | 岗位表 | P1 |
| `sys_user_group` | 用户组表 | P1 |
| `sys_user_group_rel` | 用户组-用户关联表 | P1 |
| `sys_tenant_config` | 租户配置表 | P1 |
| `sys_industry_template` | 行业模板表 | P1 |
| `sys_form_template` | 表单模板表 | P1 |
| `sys_audit_log` | 审计日志表（PG按月分区，30天以上归档） | P0 |
| `sys_login_log` | 登录日志表 | P0 |
| `sys_package` | 套餐定义表 | P1 |
| `sys_package_feature` | 套餐功能项表 | P1 |

### 15.3 现有API端点

| 模块 | 端点 | 方法 | 说明 | 状态 |
|------|------|------|------|------|
| IAM | `/auth/login` | POST | 用户登录 | ✅ |
| Tenant | `/tenant/list` | GET | 租户列表 | ✅ |
| Tenant | `/tenant/page` | GET | 租户分页 | ✅ |
| Tenant | `/tenant/tree/list` | GET | 租户树列表 | ✅ |
| Tenant | `/tenant/tree/page` | GET | 租户树分页 | ✅ |
| Tenant | `/tenant/detail/{tenantCode}` | GET | 租户详情 | ✅ |
| Tenant | `/tenant/add` | POST | 新增租户 | ✅ |
| Tenant | `/tenant/update` | PUT | 更新租户 | ✅ |
| Tenant | `/tenant/status` | PUT | 变更状态 | ✅ |
| Tenant | `/tenant/delete` | DELETE | 删除租户 | ✅ |
| Tenant | `/tenant/assign/sub` | POST | 分配子租户 | ✅ |
| Tenant | `/tenant/assign/parent` | PUT | 调整父租户 | ✅ |

### 15.4 待实现API端点

| 模块 | 端点 | 方法 | 说明 | 优先级 |
|------|------|------|------|--------|
| IAM | `/auth/register` | POST | 租户注册 | P0 |
| IAM | `/auth/logout` | POST | 退出登录 | P0 |
| IAM | `/auth/switch-tenant` | POST | 切换租户 | P0 |
| IAM | `/auth/refresh` | POST | 刷新Token | P0 |
| Org | `/role/*` | CRUD | 角色管理 | P0 |
| Org | `/dept/*` | CRUD | 部门管理 | P0 |
| Org | `/post/*` | CRUD | 岗位管理 | P1 |
| Org | `/user-group/*` | CRUD | 用户组管理 | P1 |
| System | `/tenant-config/*` | CRUD | 租户配置 | P1 |
| System | `/industry-template/*` | CRUD | 行业模板 | P1 |
| System | `/form-template/*` | CRUD | 表单模板 | P1 |
| Audit | `/audit-log/*` | QUERY | 审计日志查询 | P0 |
| Audit | `/login-log/*` | QUERY | 登录日志查询 | P0 |

### 15.5 枚举值汇总

| 枚举类 | 值 | 说明 |
|--------|-----|------|
| TenantStatus | `ENABLED` | 已启用 |
| TenantStatus | `DISABLED` | 已停用 |
| TenantStatus | `EXPIRED` | 已过期 |
| TenantStatus | `PENDING` | 待审核 |
| UserStatus | `ENABLED` | 已启用 |
| UserStatus | `DISABLED` | 已禁用 |
| PermType | `MENU` | 菜单权限 |
| PermType | `BUTTON` | 按钮权限 |
| PermType | `API` | 接口权限 |
| PermStatus | `ENABLED` | 已启用 |
| PermStatus | `DISABLED` | 已禁用 |
| PolicyStatus | `ACTIVE` | 策略生效 |
| PolicyStatus | `DISABLED_SYSTEM_LEVEL` | 系统级禁用 |
| PolicyStatus | `DISABLED_TENANT_LEVEL` | 租户级禁用 |
| PolicyStatus | `DISABLED_ROLE_LEVEL` | 角色级禁用 |
| PolicyStatus | `DISABLED_USER_LEVEL` | 用户级禁用 |
| AccessType | `QUERY` | 查询操作 |
| AccessType | `CREATE` | 创建操作 |
| AccessType | `UPDATE` | 更新操作 |
| TargetType | `SYSTEM` | 系统级策略 |
| TargetType | `TENANT` | 租户级策略 |
| TargetType | `ROLE` | 角色级策略 |
| TargetType | `USER` | 用户级策略 |
| SubscriptionType | `NEW` | 新购 |
| SubscriptionType | `RENEWAL` | 续费 |
| SubscriptionType | `UPGRADE` | 升级 |
| SubscriptionType | `DOWNGRADE` | 降级 |
| DataScope | `ALL` | 全部数据 |
| DataScope | `LEVEL_AND_CHILDREN` | 本级及下级 |
| DataScope | `LEVEL_ONLY` | 本级数据 |
| DataScope | `DEPT_ONLY` | 本部门 |
| DataScope | `DEPT_AND_CHILDREN` | 本部门及下级 |
| DataScope | `SELF_ONLY` | 仅本人 |
| DeleteStatus | `NOT_DELETED` | 未删除 |
| DeleteStatus | `DELETED` | 已删除 |

### 15.6 术语表

| 术语 | 英文 | 定义 |
|------|------|------|
| 租户 | Tenant | SaaS系统中的独立组织单元，拥有独立的数据空间和权限体系 |
| 物化路径 | Materialized Path | 树形结构存储方式，使用路径字符串(如/1/2/3/)表示层级关系 |
| 级联禁用 | Cascade Disabled | 高级别权限禁用自动向下传播，低级别无法覆盖 |
| 字段权限 | Field Permission | 控制用户对特定数据表字段的访问(可见/不可见) |
| 数据范围 | Data Scope | 控制用户可访问的数据行范围 |
| 权限策略 | Permission Policy | 定义特定目标(租户/角色/用户)对特定资源的访问规则 |
| 行级安全 | Row Level Security (RLS) | 数据库层面的行级访问控制机制 |
| 动态表单 | Dynamic Form | 通过JSON Schema定义的运行时渲染表单 |

---

> **文档结束** — 本文档为NexusIX-Platform多行业通用SaaS租户系统的完整产品需求文档，后续将根据开发进展持续更新。
