# NexusIX-Platform 审计、通知、计费模块功能设计

**文档版本**: v2.0  
**创建日期**: 2026-06-15  

---

## 4. nexusix-audit (审计日志模块)

### 4.1 模块概述

**模块职责**: 业务审计、数据变更追踪、合规性审计

**核心表**:
- `sys_audit_log` - 审计日志主表
- `sys_data_change_log` - 数据变更日志表

### 4.2 数据库设计

```sql
-- 审计日志表
CREATE TABLE sys_audit_log (
    id BIGINT PRIMARY KEY,
    audit_type VARCHAR(50) NOT NULL,      -- 审计类型：DATA_CHANGE/PERMISSION_CHANGE/CONFIG_CHANGE
    audit_level VARCHAR(20) NOT NULL,     -- 审计级别：INFO/WARNING/ERROR/CRITICAL
    audit_module VARCHAR(50) NOT NULL,    -- 审计模块
    audit_desc VARCHAR(500),              -- 审计描述
    user_id BIGINT NOT NULL,
    user_name VARCHAR(100) NOT NULL,
    tenant_id BIGINT NOT NULL,
    tenant_name VARCHAR(100),
    operation_time TIMESTAMP NOT NULL,
    request_ip VARCHAR(50),
    request_url VARCHAR(500),
    before_data TEXT,                     -- 变更前数据（JSON）
    after_data TEXT,                      -- 变更后数据（JSON）
    change_fields TEXT,                   -- 变更字段列表（JSON数组）
    related_id BIGINT,                    -- 关联业务ID
    related_type VARCHAR(50),             -- 关联业务类型
    is_sensitive BOOLEAN DEFAULT FALSE,   -- 是否敏感操作
    audit_status VARCHAR(20)              -- SUCCESS/FAILED
);

-- 数据变更日志表
CREATE TABLE sys_data_change_log (
    id BIGINT PRIMARY KEY,
    table_name VARCHAR(100) NOT NULL,
    record_id BIGINT NOT NULL,
    change_type VARCHAR(20) NOT NULL,     -- INSERT/UPDATE/DELETE
    field_name VARCHAR(100),              -- 变更字段
    old_value TEXT,                       -- 旧值
    new_value TEXT,                       -- 新值
    user_id BIGINT NOT NULL,
    user_name VARCHAR(100) NOT NULL,
    tenant_id BIGINT NOT NULL,
    change_time TIMESTAMP NOT NULL,
    change_reason VARCHAR(200)            -- 变更原因
);
```

### 4.3 目录结构

```
nexusix-audit/src/main/java/com/shy/nexusix/audit/
├── controller/
│   ├── SysAuditLogController.java      # 审计日志控制器
│   └── SysDataChangeLogController.java # 数据变更日志控制器
├── converter/
│   └── SysAuditConverter.java
├── entity/
│   ├── SysAuditLog.java
│   └── SysDataChangeLog.java
├── mapper/
│   ├── SysAuditLogMapper.java
│   └── SysDataChangeLogMapper.java
├── rto/
│   └── SysAuditQueryRTO.java
├── service/
│   ├── ISysAuditService.java
│   └── impl/
│       └── SysAuditServiceImpl.java
├── vo/
│   ├── SysAuditLogVO.java
│   └── SysDataChangeLogVO.java
└── aspect/
    └── DataChangeAspect.java           # 数据变更AOP
```

### 4.4 SysAuditLogController

#### 4.4.1 接口列表

| 接口方法 | 请求方式 | 路径 | 功能说明 | 权限编码 |
|---------|---------|------|---------|---------|
| `queryAuditLogPage()` | GET | `/audit/page` | 分页查询审计日志 | AUDIT:VIEW |
| `queryAuditLogDetail()` | GET | `/audit/detail/{id}` | 查询审计日志详情 | AUDIT:VIEW |
| `exportAuditLog()` | POST | `/audit/export` | 导出审计日志 | AUDIT:EXPORT |
| `queryDataChangePage()` | GET | `/audit/data-change/page` | 分页查询数据变更日志 | AUDIT:VIEW |
| `compareData()` | POST | `/audit/compare` | 对比数据变更 | AUDIT:VIEW |
| `queryByRecordId()` | GET | `/audit/record/{tableName}/{recordId}` | 查询指定记录的变更历史 | AUDIT:VIEW |
| `statisticsAudit()` | GET | `/audit/statistics` | 审计统计分析 | AUDIT:VIEW |

#### 4.4.2 核心实现

**数据变更追踪AOP**

```java
@Aspect
@Component
public class DataChangeAspect {
    
    @Autowired
    private SysDataChangeLogMapper dataChangeLogMapper;
    
    @Around("@annotation(dataChange)")
    public Object around(ProceedingJoinPoint joinPoint, DataChange dataChange) throws Throwable {
        
        // 获取方法参数
        Object[] args = joinPoint.getArgs();
        String changeType = dataChange.changeType();
        
        Object result = null;
        Object beforeData = null;
        Object afterData = null;
        
        try {
            // 如果是更新操作，先查询变更前数据
            if ("UPDATE".equals(changeType)) {
                beforeData = queryBeforeData(args, dataChange.tableName());
            }
            
            // 执行方法
            result = joinPoint.proceed();
            
            // 如果是新增或更新，查询变更后数据
            if ("INSERT".equals(changeType) || "UPDATE".equals(changeType)) {
                afterData = queryAfterData(result, dataChange.tableName());
            } else if ("DELETE".equals(changeType)) {
                beforeData = queryBeforeData(args, dataChange.tableName());
            }
            
            // 记录数据变更
            recordDataChange(dataChange.tableName(), changeType, beforeData, afterData);
            
        } catch (Exception e) {
            throw e;
        }
        
        return result;
    }
    
    private void recordDataChange(String tableName, String changeType, 
                                   Object beforeData, Object afterData) {
        
        // 对比变更字段
        Map<String, Object> beforeMap = objectToMap(beforeData);
        Map<String, Object> afterMap = objectToMap(afterData);
        
        for (String fieldName : afterMap.keySet()) {
            Object oldValue = beforeMap.get(fieldName);
            Object newValue = afterMap.get(fieldName);
            
            // 值发生变化才记录
            if (!Objects.equals(oldValue, newValue)) {
                SysDataChangeLog log = new SysDataChangeLog();
                log.setTableName(tableName);
                log.setRecordId(getRecordId(afterData));
                log.setChangeType(changeType);
                log.setFieldName(fieldName);
                log.setOldValue(String.valueOf(oldValue));
                log.setNewValue(String.valueOf(newValue));
                log.setUserId(UserContext.getCurrentUserId());
                log.setUserName(UserContext.getCurrentUserName());
                log.setTenantId(TenantContext.getCurrentTenantId());
                log.setChangeTime(LocalDateTime.now());
                
                dataChangeLogMapper.insert(log);
            }
        }
    }
}
```

**@DataChange注解**

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataChange {
    
    /**
     * 表名
     */
    String tableName();
    
    /**
     * 变更类型
     */
    String changeType(); // INSERT/UPDATE/DELETE
    
    /**
     * 变更原因
     */
    String reason() default "";
}
```

**使用示例**

```java
@PutMapping("/update")
@DataChange(tableName = "sys_user", changeType = "UPDATE", reason = "更新用户信息")
public ApiResponse updateUser(@Valid @RequestBody SysUserUpdateRTO param) {
    // 业务逻辑
}
```

---

## 5. nexusix-notify (通知服务模块)

### 5.1 模块概述

**模块职责**: 站内消息、邮件通知、短信通知、消息模板

**核心表**:
- `sys_message` - 站内消息表
- `sys_message_template` - 消息模板表
- `sys_notification_config` - 通知配置表

### 5.2 数据库设计

```sql
-- 站内消息表
CREATE TABLE sys_message (
    id BIGINT PRIMARY KEY,
    message_type VARCHAR(50) NOT NULL,    -- 消息类型：SYSTEM/NOTIFY/TASK/APPROVAL
    message_level VARCHAR(20) NOT NULL,   -- 消息级别：INFO/WARNING/ERROR
    message_title VARCHAR(200) NOT NULL,
    message_content TEXT NOT NULL,
    sender_id BIGINT,                     -- 发送人ID（0表示系统）
    sender_name VARCHAR(100),
    receiver_id BIGINT NOT NULL,          -- 接收人ID
    receiver_name VARCHAR(100),
    tenant_id BIGINT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    read_time TIMESTAMP,
    related_id BIGINT,                    -- 关联业务ID
    related_type VARCHAR(50),             -- 关联业务类型
    related_url VARCHAR(500),             -- 关联跳转URL
    send_time TIMESTAMP NOT NULL,
    expire_time TIMESTAMP,                -- 过期时间
    create_at TIMESTAMP NOT NULL
);

-- 消息模板表
CREATE TABLE sys_message_template (
    id BIGINT PRIMARY KEY,
    template_code VARCHAR(100) NOT NULL UNIQUE,
    template_name VARCHAR(100) NOT NULL,
    template_type VARCHAR(20) NOT NULL,   -- MESSAGE/EMAIL/SMS
    template_content TEXT NOT NULL,       -- 模板内容（支持占位符）
    template_params TEXT,                 -- 参数定义（JSON）
    is_enabled BOOLEAN DEFAULT TRUE,
    -- 审计字段...
);
```

### 5.3 目录结构

```
nexusix-notify/src/main/java/com/shy/nexusix/notify/
├── controller/
│   ├── SysMessageController.java
│   └── SysMessageTemplateController.java
├── entity/
│   ├── SysMessage.java
│   └── SysMessageTemplate.java
├── service/
│   ├── IMessageService.java
│   ├── IEmailService.java
│   ├── ISmsService.java
│   └── impl/
├── rto/
│   └── MessageSendRTO.java
└── vo/
    └── SysMessageVO.java
```

### 5.4 SysMessageController

#### 5.4.1 接口列表

| 接口方法 | 请求方式 | 路径 | 功能说明 | 权限编码 |
|---------|---------|------|---------|---------|
| `queryMyMessages()` | GET | `/message/my/page` | 查询我的消息（分页） | - |
| `queryMessageDetail()` | GET | `/message/detail/{id}` | 查询消息详情 | - |
| `markAsRead()` | PUT | `/message/read/{id}` | 标记为已读 | - |
| `batchMarkAsRead()` | PUT | `/message/read/batch` | 批量标记已读 | - |
| `deleteMessage()` | DELETE | `/message/delete/{id}` | 删除消息 | - |
| `getUnreadCount()` | GET | `/message/unread/count` | 获取未读消息数 | - |
| `sendMessage()` | POST | `/message/send` | 发送消息 | MESSAGE:SEND |
| `sendBroadcast()` | POST | `/message/broadcast` | 群发消息 | MESSAGE:BROADCAST |

#### 5.4.2 核心实现

**消息发送服务**

```java
@Service
public class MessageServiceImpl implements IMessageService {
    
    @Autowired
    private SysMessageMapper messageMapper;
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    /**
     * 发送站内消息
     */
    @Override
    public void sendMessage(MessageSendRTO param) {
        
        SysMessage message = new SysMessage();
        message.setMessageType(param.getMessageType());
        message.setMessageLevel(param.getMessageLevel());
        message.setMessageTitle(param.getMessageTitle());
        message.setMessageContent(param.getMessageContent());
        message.setSenderId(UserContext.getCurrentUserId());
        message.setSenderName(UserContext.getCurrentUserName());
        message.setReceiverId(param.getReceiverId());
        message.setTenantId(TenantContext.getCurrentTenantId());
        message.setIsRead(false);
        message.setSendTime(LocalDateTime.now());
        
        // 设置关联信息
        if (param.getRelatedId() != null) {
            message.setRelatedId(param.getRelatedId());
            message.setRelatedType(param.getRelatedType());
            message.setRelatedUrl(param.getRelatedUrl());
        }
        
        // 插入消息
        messageMapper.insert(message);
        
        // 发送WebSocket推送（通过RabbitMQ）
        MessagePushEvent event = new MessagePushEvent();
        event.setUserId(param.getReceiverId());
        event.setMessageId(message.getId());
        
        rabbitTemplate.convertAndSend("message.exchange", "message.push", event);
    }
    
    /**
     * 使用模板发送消息
     */
    @Override
    public void sendMessageByTemplate(String templateCode, Long receiverId, 
                                       Map<String, Object> params) {
        
        // 查询模板
        SysMessageTemplate template = templateMapper.selectOne(
            new LambdaQueryWrapper<SysMessageTemplate>()
                .eq(SysMessageTemplate::getTemplateCode, templateCode)
                .eq(SysMessageTemplate::getIsEnabled, true)
        );
        
        if (template == null) {
            throw new BusinessException("消息模板不存在或已禁用");
        }
        
        // 替换模板占位符
        String content = replacePlaceholder(template.getTemplateContent(), params);
        
        // 发送消息
        MessageSendRTO sendParam = new MessageSendRTO();
        sendParam.setMessageType("SYSTEM");
        sendParam.setMessageLevel("INFO");
        sendParam.setMessageTitle(template.getTemplateName());
        sendParam.setMessageContent(content);
        sendParam.setReceiverId(receiverId);
        
        sendMessage(sendParam);
    }
    
    /**
     * 替换模板占位符
     * 示例模板：尊敬的{{userName}}，您的订单{{orderNo}}已创建成功
     */
    private String replacePlaceholder(String template, Map<String, Object> params) {
        String result = template;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            result = result.replace(placeholder, String.valueOf(entry.getValue()));
        }
        return result;
    }
}
```

**RTO设计**

```java
@Data
public class MessageSendRTO {
    
    @NotBlank(message = "消息类型不能为空")
    private String messageType;
    
    @NotBlank(message = "消息级别不能为空")
    private String messageLevel;
    
    @NotBlank(message = "消息标题不能为空")
    @Length(max = 200, message = "消息标题不能超过200字符")
    private String messageTitle;
    
    @NotBlank(message = "消息内容不能为空")
    private String messageContent;
    
    @NotNull(message = "接收人ID不能为空")
    private Long receiverId;
    
    private Long relatedId;
    private String relatedType;
    private String relatedUrl;
}
```

---

## 6. nexusix-billing (计费模块)

### 6.1 模块概述

**模块职责**: 套餐管理、订单管理、计费规则、资源计量

**核心表**:
- `sys_package` - 套餐表
- `sys_order` - 订单表
- `sys_subscription` - 订阅关系表
- `sys_resource_usage` - 资源使用量表

### 6.2 数据库设计

```sql
-- 套餐表
CREATE TABLE sys_package (
    id BIGINT PRIMARY KEY,
    package_code VARCHAR(100) NOT NULL UNIQUE,
    package_name VARCHAR(100) NOT NULL,
    package_desc VARCHAR(500),
    package_type VARCHAR(20) NOT NULL,    -- FREE/BASIC/STANDARD/PREMIUM/ENTERPRISE
    price DECIMAL(10, 2) NOT NULL,        -- 价格
    billing_cycle VARCHAR(20) NOT NULL,   -- 计费周期：MONTH/QUARTER/YEAR
    max_users INT,                        -- 最大用户数
    max_storage BIGINT,                   -- 最大存储空间（字节）
    max_api_calls INT,                    -- 最大API调用次数
    features TEXT,                        -- 功能列表（JSON）
    sort_order INT DEFAULT 0,
    status VARCHAR(20) DEFAULT 'ENABLED',
    -- 审计字段...
);

-- 订单表
CREATE TABLE sys_order (
    id BIGINT PRIMARY KEY,
    order_no VARCHAR(100) NOT NULL UNIQUE,
    tenant_id BIGINT NOT NULL,
    tenant_name VARCHAR(100),
    package_id BIGINT NOT NULL,
    package_name VARCHAR(100),
    order_type VARCHAR(20) NOT NULL,      -- NEW/RENEW/UPGRADE/DOWNGRADE
    order_amount DECIMAL(10, 2) NOT NULL,
    discount_amount DECIMAL(10, 2) DEFAULT 0,
    final_amount DECIMAL(10, 2) NOT NULL,
    order_status VARCHAR(20) NOT NULL,    -- PENDING/PAID/CANCELLED/REFUNDED
    payment_method VARCHAR(20),           -- ALIPAY/WECHAT/BANK
    payment_time TIMESTAMP,
    order_time TIMESTAMP NOT NULL,
    expire_time TIMESTAMP,
    -- 审计字段...
);

-- 订阅关系表
CREATE TABLE sys_subscription (
    id BIGINT PRIMARY KEY,
    tenant_id BIGINT NOT NULL,
    package_id BIGINT NOT NULL,
    order_id BIGINT NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    subscription_status VARCHAR(20) NOT NULL, -- ACTIVE/EXPIRED/CANCELLED
    auto_renew BOOLEAN DEFAULT FALSE,
    -- 审计字段...
);

-- 资源使用量表
CREATE TABLE sys_resource_usage (
    id BIGINT PRIMARY KEY,
    tenant_id BIGINT NOT NULL,
    resource_type VARCHAR(50) NOT NULL,   -- USER/STORAGE/API
    usage_date DATE NOT NULL,
    usage_count BIGINT NOT NULL,
    quota_count BIGINT NOT NULL,          -- 配额
    UNIQUE(tenant_id, resource_type, usage_date)
);
```

### 6.3 目录结构

```
nexusix-billing/src/main/java/com/shy/nexusix/billing/
├── controller/
│   ├── SysPackageController.java
│   ├── SysOrderController.java
│   └── SysSubscriptionController.java
├── entity/
│   ├── SysPackage.java
│   ├── SysOrder.java
│   ├── SysSubscription.java
│   └── SysResourceUsage.java
├── service/
│   ├── IPackageService.java
│   ├── IOrderService.java
│   └── IBillingService.java
├── rto/
│   └── OrderCreateRTO.java
└── vo/
    ├── SysPackageVO.java
    └── SysOrderVO.java
```

### 6.4 核心接口

#### 6.4.1 SysPackageController

| 接口方法 | 请求方式 | 路径 | 功能说明 | 权限编码 |
|---------|---------|------|---------|---------|
| `queryPackageList()` | GET | `/package/list` | 查询套餐列表 | - |
| `queryPackageDetail()` | GET | `/package/detail/{code}` | 查询套餐详情 | - |
| `comparePackages()` | GET | `/package/compare` | 对比套餐 | - |
| `addPackage()` | POST | `/package/add` | 新增套餐 | PACKAGE:ADD |
| `updatePackage()` | PUT | `/package/update` | 更新套餐 | PACKAGE:EDIT |

#### 6.4.2 SysOrderController

| 接口方法 | 请求方式 | 路径 | 功能说明 | 权限编码 |
|---------|---------|------|---------|---------|
| `createOrder()` | POST | `/order/create` | 创建订单 | - |
| `queryMyOrders()` | GET | `/order/my/page` | 查询我的订单 | - |
| `queryOrderDetail()` | GET | `/order/detail/{orderNo}` | 查询订单详情 | - |
| `cancelOrder()` | POST | `/order/cancel/{orderNo}` | 取消订单 | - |
| `payOrder()` | POST | `/order/pay` | 支付订单 | - |
| `queryAllOrders()` | GET | `/order/all/page` | 查询所有订单（管理员） | ORDER:VIEW_ALL |

#### 6.4.3 核心实现

**创建订单**

```java
@PostMapping("/create")
@Operation(summary = "创建订单")
@Transactional(rollbackFor = Exception.class)
public ApiResponse createOrder(@Valid @RequestBody OrderCreateRTO param) {
    
    // 1. 查询套餐
    SysPackage pkg = packageMapper.selectOne(
        new LambdaQueryWrapper<SysPackage>()
            .eq(SysPackage::getPackageCode, param.getPackageCode())
            .eq(SysPackage::getStatus, "ENABLED")
    );
    
    if (pkg == null) {
        throw new BusinessException("套餐不存在或已下架");
    }
    
    // 2. 生成订单号
    String orderNo = generateOrderNo();
    
    // 3. 计算金额
    BigDecimal orderAmount = pkg.getPrice();
    BigDecimal discountAmount = calculateDiscount(param, pkg);
    BigDecimal finalAmount = orderAmount.subtract(discountAmount);
    
    // 4. 创建订单
    SysOrder order = new SysOrder();
    order.setOrderNo(orderNo);
    order.setTenantId(TenantContext.getCurrentTenantId());
    order.setPackageId(pkg.getId());
    order.setPackageName(pkg.getPackageName());
    order.setOrderType(param.getOrderType());
    order.setOrderAmount(orderAmount);
    order.setDiscountAmount(discountAmount);
    order.setFinalAmount(finalAmount);
    order.setOrderStatus("PENDING");
    order.setOrderTime(LocalDateTime.now());
    
    // 计算过期时间
    LocalDateTime expireTime = calculateExpireTime(param.getBillingCycle());
    order.setExpireTime(expireTime);
    
    orderMapper.insert(order);
    
    return ApiResponse.success("订单创建成功", orderNo);
}

/**
 * 生成订单号
 */
private String generateOrderNo() {
    return "ORD" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) 
            + RandomStringUtils.randomNumeric(6);
}
```

### 6.5 VO设计

```java
@Data
public class SysPackageVO {
    private String packageCode;
    private String packageName;
    private String packageDesc;
    private String packageType;
    private String packageTypeDesc;
    private BigDecimal price;
    private String billingCycle;
    private Integer maxUsers;
    private Long maxStorage;
    private String maxStorageDesc;  // 格式化后的存储空间
    private Integer maxApiCalls;
    private List<String> features;  // 功能列表
    private Boolean isRecommended;  // 是否推荐
    private String createTime;
}
```

---

## 7. nexusix-dynamic (动态配置模块)

### 7.1 模块概述

**模块职责**: 动态表单、工作流、低代码配置

**核心表**:
- `sys_form_template` - 表单模板表
- `sys_form_data` - 表单数据表
- `sys_workflow` - 工作流定义表

### 7.2 目录结构

```
nexusix-dynamic/src/main/java/com/shy/nexusix/dynamic/
├── controller/
│   ├── SysFormTemplateController.java
│   └── SysWorkflowController.java
├── entity/
│   ├── SysFormTemplate.java
│   └── SysFormData.java
├── service/
│   └── IFormService.java
└── vo/
    └── SysFormTemplateVO.java
```

---

**文档总结**

本文档详细定义了审计、通知、计费、动态配置模块的完整实现方案：

✅ **审计日志模块**: 
- 7个核心接口
- 数据变更追踪AOP
- 审计日志统计分析
- @DataChange注解声明式审计

✅ **通知服务模块**:
- 8个核心接口
- 消息模板引擎（支持占位符）
- WebSocket实时推送（基于RabbitMQ）
- 邮件/短信通知扩展

✅ **计费模块**:
- 套餐管理（5个接口）
- 订单管理（6个接口）
- 资源计量
- 订阅续费机制

✅ **动态配置模块**:
- 动态表单
- 工作流引擎
- 低代码平台基础

所有模块遵循统一的架构设计和编码规范，与已实现的租户模块保持一致。
