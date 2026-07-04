好的！让我分段创建这两个模块的完整文档。先创建billing模块：

让我正确使用Write工具创建billing模块文档：

我注意到Write工具一直遇到问题。让我尝试使用Bash命令创建文件：

我遇到了持续的技术问题。让我直接为您提供完整的文档内容，您可以手动保存：

---

# 📄 nexusix-billing 计费模块完整PRD文档

将以下内容保存为：`D:\ProjectHub\NexusIX-Platform\docs\模块功能设计PRD-计费模块详细版.md`

```markdown
# NexusIX-Platform 计费模块详细设计

**文档版本**: v2.0  
**创建日期**: 2026-06-15  
**基于**: nexusix-tenant 模块实现方案  

---

## 1. 模块概述

### 1.1 模块定位

**nexusix-billing** 是 NexusIX-Platform 的计费模块，负责套餐管理、订单管理、订阅管理、资源计量等功能，是 SaaS 平台商业化的核心模块。

### 1.2 核心功能

- ✅ 套餐管理（FREE/BASIC/STANDARD/PREMIUM/ENTERPRISE）
- ✅ 订单管理（创建、支付、取消、退款）
- ✅ 订阅管理（新购、续费、升级、降级）
- ✅ 资源计量（用户数、存储空间、API调用次数）
- ✅ 账单管理（月度账单、费用统计）
- ✅ 支付集成（支付宝、微信支付）

### 1.3 业务流程

```
用户选择套餐 → 创建订单 → 支付 → 创建订阅 → 开通服务 → 资源计量 → 到期提醒 → 续费
```

---

## 2. 数据库设计

### 2.1 核心表结构

#### 2.1.1 sys_package (套餐表)

```sql
CREATE TABLE sys_package (
    id BIGINT PRIMARY KEY,
    package_code VARCHAR(100) NOT NULL UNIQUE,
    package_name VARCHAR(100) NOT NULL,
    package_desc VARCHAR(500),
    package_type VARCHAR(20) NOT NULL,        -- FREE/BASIC/STANDARD/PREMIUM/ENTERPRISE
    price DECIMAL(10, 2) NOT NULL,
    original_price DECIMAL(10, 2),            -- 原价（用于显示折扣）
    billing_cycle VARCHAR(20) NOT NULL,       -- MONTH/QUARTER/YEAR
    
    -- 资源配额
    max_users INT,                            -- 最大用户数
    max_storage BIGINT,                       -- 最大存储空间（字节）
    max_api_calls INT,                        -- 最大API调用次数/月
    max_depts INT,                            -- 最大部门数
    max_roles INT,                            -- 最大角色数
    
    -- 功能特性
    features TEXT,                            -- JSON格式功能列表
    
    -- 显示控制
    is_recommended BOOLEAN DEFAULT FALSE,     -- 是否推荐
    is_visible BOOLEAN DEFAULT TRUE,          -- 是否显示
    sort_order INT DEFAULT 0,
    status VARCHAR(20) DEFAULT 'ENABLED',
    
    -- 审计字段
    create_tenant BIGINT NOT NULL,
    create_dept BIGINT NOT NULL,
    create_role BIGINT NOT NULL,
    create_by BIGINT NOT NULL,
    create_at TIMESTAMP NOT NULL,
    update_by BIGINT,
    update_at TIMESTAMP,
    is_deleted VARCHAR(20) DEFAULT 'NOT_DELETED',
    deleted_at TIMESTAMP
);

-- 索引
CREATE INDEX idx_package_type ON sys_package(package_type, status);
CREATE INDEX idx_package_visible ON sys_package(is_visible, sort_order);
```

**features字段JSON示例**：
```json
{
  "basic_features": [
    "用户管理",
    "角色权限",
    "部门管理"
  ],
  "advanced_features": [
    "数据导出",
    "API接口",
    "自定义字段"
  ],
  "premium_features": [
    "工作流引擎",
    "BI报表",
    "数据大屏"
  ]
}
```

#### 2.1.2 sys_order (订单表)

```sql
CREATE TABLE sys_order (
    id BIGINT PRIMARY KEY,
    order_no VARCHAR(100) NOT NULL UNIQUE,
    tenant_id BIGINT NOT NULL,
    tenant_name VARCHAR(100),
    package_id BIGINT NOT NULL,
    package_name VARCHAR(100),
    package_type VARCHAR(20),
    
    -- 订单类型
    order_type VARCHAR(20) NOT NULL,          -- NEW/RENEW/UPGRADE/DOWNGRADE
    
    -- 金额信息
    order_amount DECIMAL(10, 2) NOT NULL,     -- 订单金额
    discount_amount DECIMAL(10, 2) DEFAULT 0, -- 折扣金额
    coupon_amount DECIMAL(10, 2) DEFAULT 0,   -- 优惠券金额
    final_amount DECIMAL(10, 2) NOT NULL,     -- 最终金额
    
    -- 订单状态
    order_status VARCHAR(20) NOT NULL,        -- PENDING/PAID/CANCELLED/REFUNDED/EXPIRED
    
    -- 支付信息
    payment_method VARCHAR(20),               -- ALIPAY/WECHAT/BANK/OFFLINE
    payment_time TIMESTAMP,
    payment_transaction_no VARCHAR(100),      -- 支付交易号
    
    -- 退款信息
    refund_amount DECIMAL(10, 2),
    refund_reason VARCHAR(200),
    refund_time TIMESTAMP,
    
    -- 时间信息
    order_time TIMESTAMP NOT NULL,
    expire_time TIMESTAMP,                    -- 订单过期时间（未支付）
    
    -- 备注
    order_remark VARCHAR(500),
    
    -- 审计字段
    create_tenant BIGINT NOT NULL,
    create_dept BIGINT NOT NULL,
    create_role BIGINT NOT NULL,
    create_by BIGINT NOT NULL,
    create_at TIMESTAMP NOT NULL,
    update_by BIGINT,
    update_at TIMESTAMP,
    is_deleted VARCHAR(20) DEFAULT 'NOT_DELETED',
    deleted_at TIMESTAMP
);

-- 索引
CREATE UNIQUE INDEX uk_order_no ON sys_order(order_no);
CREATE INDEX idx_order_tenant ON sys_order(tenant_id, order_status);
CREATE INDEX idx_order_status ON sys_order(order_status, order_time);
CREATE INDEX idx_order_payment ON sys_order(payment_transaction_no);
```

#### 2.1.3 sys_subscription (订阅关系表)

```sql
CREATE TABLE sys_subscription (
    id BIGINT PRIMARY KEY,
    subscription_code VARCHAR(100) NOT NULL UNIQUE,
    tenant_id BIGINT NOT NULL,
    package_id BIGINT NOT NULL,
    package_code VARCHAR(100),
    package_name VARCHAR(100),
    order_id BIGINT NOT NULL,
    order_no VARCHAR(100),
    
    -- 订阅周期
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    
    -- 订阅状态
    subscription_status VARCHAR(20) NOT NULL, -- ACTIVE/EXPIRED/CANCELLED/SUSPENDED
    
    -- 自动续费
    auto_renew BOOLEAN DEFAULT FALSE,
    renew_remind_sent BOOLEAN DEFAULT FALSE,  -- 是否已发送续费提醒
    
    -- 上一个订阅ID（升级/降级时关联）
    previous_subscription_id BIGINT,
    
    -- 审计字段
    create_tenant BIGINT NOT NULL,
    create_dept BIGINT NOT NULL,
    create_role BIGINT NOT NULL,
    create_by BIGINT NOT NULL,
    create_at TIMESTAMP NOT NULL,
    update_by BIGINT,
    update_at TIMESTAMP,
    is_deleted VARCHAR(20) DEFAULT 'NOT_DELETED',
    deleted_at TIMESTAMP
);

-- 索引
CREATE UNIQUE INDEX uk_subscription_code ON sys_subscription(subscription_code);
CREATE INDEX idx_subscription_tenant ON sys_subscription(tenant_id, subscription_status);
CREATE INDEX idx_subscription_expire ON sys_subscription(end_time, subscription_status);
```

#### 2.1.4 sys_resource_usage (资源使用量表)

```sql
CREATE TABLE sys_resource_usage (
    id BIGINT PRIMARY KEY,
    tenant_id BIGINT NOT NULL,
    resource_type VARCHAR(50) NOT NULL,       -- USER/STORAGE/API/DEPT/ROLE
    usage_date DATE NOT NULL,
    
    -- 使用量
    usage_count BIGINT NOT NULL,              -- 实际使用量
    quota_count BIGINT NOT NULL,              -- 配额
    usage_rate DECIMAL(5, 2),                 -- 使用率（百分比）
    
    -- 超限标记
    is_exceeded BOOLEAN DEFAULT FALSE,        -- 是否超限
    
    -- 审计字段（简化版，无租户部门角色）
    create_by BIGINT NOT NULL,
    create_at TIMESTAMP NOT NULL,
    
    UNIQUE(tenant_id, resource_type, usage_date)
);

-- 索引
CREATE INDEX idx_usage_tenant ON sys_resource_usage(tenant_id, usage_date);
CREATE INDEX idx_usage_type ON sys_resource_usage(resource_type, usage_date);
CREATE INDEX idx_usage_exceeded ON sys_resource_usage(is_exceeded, usage_date);
```

#### 2.1.5 sys_invoice (发票表)

```sql
CREATE TABLE sys_invoice (
    id BIGINT PRIMARY KEY,
    invoice_no VARCHAR(100) NOT NULL UNIQUE,
    tenant_id BIGINT NOT NULL,
    order_id BIGINT NOT NULL,
    order_no VARCHAR(100),
    
    -- 发票信息
    invoice_type VARCHAR(20) NOT NULL,        -- PERSONAL/COMPANY
    invoice_title VARCHAR(200) NOT NULL,      -- 发票抬头
    tax_no VARCHAR(50),                       -- 税号
    
    -- 金额
    invoice_amount DECIMAL(10, 2) NOT NULL,
    
    -- 状态
    invoice_status VARCHAR(20) NOT NULL,      -- PENDING/ISSUED/CANCELLED
    apply_time TIMESTAMP NOT NULL,
    issue_time TIMESTAMP,
    
    -- 快递信息
    express_company VARCHAR(50),
    express_no VARCHAR(100),
    
    -- 审计字段
    create_tenant BIGINT NOT NULL,
    create_dept BIGINT NOT NULL,
    create_role BIGINT NOT NULL,
    create_by BIGINT NOT NULL,
    create_at TIMESTAMP NOT NULL,
    update_by BIGINT,
    update_at TIMESTAMP,
    is_deleted VARCHAR(20) DEFAULT 'NOT_DELETED',
    deleted_at TIMESTAMP
);
```

---

## 3. 目录结构

```
nexusix-billing/src/main/java/com/shy/nexusix/billing/
├── controller/
│   ├── SysPackageController.java
│   ├── SysOrderController.java
│   ├── SysSubscriptionController.java
│   ├── SysResourceUsageController.java
│   └── SysInvoiceController.java
├── converter/
│   ├── SysPackageConverter.java
│   ├── SysOrderConverter.java
│   └── SysSubscriptionConverter.java
├── entity/
│   ├── SysPackage.java
│   ├── SysOrder.java
│   ├── SysSubscription.java
│   ├── SysResourceUsage.java
│   └── SysInvoice.java
├── mapper/
│   ├── SysPackageMapper.java
│   ├── SysOrderMapper.java
│   ├── SysSubscriptionMapper.java
│   ├── SysResourceUsageMapper.java
│   └── SysInvoiceMapper.java
├── rto/
│   ├── SysPackageAddRTO.java
│   ├── SysOrderCreateRTO.java
│   ├── SysOrderPayRTO.java
│   ├── SysSubscriptionRenewRTO.java
│   └── SysInvoiceApplyRTO.java
├── vo/
│   ├── SysPackageVO.java
│   ├── SysOrderVO.java
│   ├── SysSubscriptionVO.java
│   └── SysResourceUsageVO.java
├── service/
│   ├── ISysPackageService.java
│   ├── ISysOrderService.java
│   ├── ISysSubscriptionService.java
│   ├── IPaymentService.java
│   └── impl/
└── payment/
    ├── AlipayService.java
    └── WechatPayService.java
```

---

## 4. 核心接口设计

### 4.1 SysPackageController (套餐管理)

| 接口方法 | 请求方式 | 路径 | 功能说明 | 权限编码 |
|---------|---------|------|---------|---------|
| `queryPackageList()` | GET | `/package/list` | 查询套餐列表 | - |
| `queryPackageDetail()` | GET | `/package/detail/{code}` | 查询套餐详情 | - |
| `comparePackages()` | GET | `/package/compare` | 对比套餐 | - |
| `addPackage()` | POST | `/package/add` | 新增套餐 | PACKAGE:ADD |
| `updatePackage()` | PUT | `/package/update` | 更新套餐 | PACKAGE:EDIT |
| `deletePackage()` | DELETE | `/package/delete/{code}` | 删除套餐 | PACKAGE:DELETE |

### 4.2 SysOrderController (订单管理)

| 接口方法 | 请求方式 | 路径 | 功能说明 | 权限编码 |
|---------|---------|------|---------|---------|
| `createOrder()` | POST | `/order/create` | 创建订单 | - |
| `queryMyOrders()` | GET | `/order/my/page` | 查询我的订单 | - |
| `queryOrderDetail()` | GET | `/order/detail/{orderNo}` | 查询订单详情 | - |
| `payOrder()` | POST | `/order/pay` | 支付订单 | - |
| `cancelOrder()` | POST | `/order/cancel/{orderNo}` | 取消订单 | - |
| `refundOrder()` | POST | `/order/refund/{orderNo}` | 申请退款 | ORDER:REFUND |
| `queryAllOrders()` | GET | `/order/all/page` | 查询所有订单 | ORDER:VIEW_ALL |
| `handlePayCallback()` | POST | `/order/callback/alipay` | 支付宝回调 | - |
| `handlePayCallback()` | POST | `/order/callback/wechat` | 微信回调 | - |

### 4.3 SysSubscriptionController (订阅管理)

| 接口方法 | 请求方式 | 路径 | 功能说明 | 权限编码 |
|---------|---------|------|---------|---------|
| `queryMySubscription()` | GET | `/subscription/my` | 查询我的订阅 | - |
| `querySubscriptionDetail()` | GET | `/subscription/detail/{code}` | 查询订阅详情 | - |
| `renewSubscription()` | POST | `/subscription/renew` | 续费订阅 | - |
| `upgradeSubscription()` | POST | `/subscription/upgrade` | 升级套餐 | - |
| `downgradeSubscription()` | POST | `/subscription/downgrade` | 降级套餐 | - |
| `cancelSubscription()` | POST | `/subscription/cancel/{code}` | 取消订阅 | - |
| `setAutoRenew()` | PUT | `/subscription/auto-renew` | 设置自动续费 | - |

### 4.4 SysResourceUsageController (资源计量)

| 接口方法 | 请求方式 | 路径 | 功能说明 | 权限编码 |
|---------|---------|------|---------|---------|
| `queryMyUsage()` | GET | `/usage/my` | 查询我的资源使用情况 | - |
| `queryUsageHistory()` | GET | `/usage/history` | 查询使用历史 | - |
| `queryUsageStatistics()` | GET | `/usage/statistics` | 统计分析 | USAGE:STATISTICS |

---

由于篇幅限制，完整文档内容约30KB。我已经为您提供了核心框架。您可以：

1. 将上述内容保存为md文件
2. 我继续为您补充剩余的实现逻辑部分

请告诉我是否需要继续补充完整的实现逻辑、RTO/VO设计和dynamic模块？