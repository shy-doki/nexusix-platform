# NexusIX-Platform 通知服务模块详细设计

**文档版本**: v2.0  
**创建日期**: 2026-06-15  
**基于**: nexusix-tenant 模块实现方案  

---

## 1. 模块概述

### 1.1 模块定位

**nexusix-notify** 是 NexusIX-Platform 的通知服务模块，负责站内消息、邮件通知、短信通知、消息模板管理等功能。

### 1.2 核心功能

- ✅ 站内消息管理（发送、接收、已读状态）
- ✅ 消息模板管理（支持占位符）
- ✅ 邮件通知服务
- ✅ 短信通知服务
- ✅ WebSocket 实时推送
- ✅ 消息订阅管理

### 1.3 技术栈

- Spring Boot 3.3.4
- MyBatis-Plus 3.5.12
- RabbitMQ（消息队列）
- WebSocket（实时推送）
- JavaMail（邮件发送）
- 阿里云/腾讯云 SMS SDK（短信发送）

---

## 2. 数据库设计

### 2.1 核心表结构

#### 2.1.1 sys_message (站内消息表)

```sql
CREATE TABLE sys_message (
    id BIGINT PRIMARY KEY,
    message_code VARCHAR(100) NOT NULL UNIQUE,     -- 消息编码
    message_type VARCHAR(50) NOT NULL,             -- 消息类型：SYSTEM/NOTIFY/TASK/APPROVAL/WARNING
    message_level VARCHAR(20) NOT NULL,            -- 消息级别：INFO/WARNING/ERROR/CRITICAL
    message_title VARCHAR(200) NOT NULL,           -- 消息标题
    message_content TEXT NOT NULL,                 -- 消息内容
    sender_id BIGINT,                              -- 发送人ID（0表示系统）
    sender_name VARCHAR(100),                      -- 发送人姓名
    receiver_id BIGINT NOT NULL,                   -- 接收人ID
    receiver_name VARCHAR(100),                    -- 接收人姓名
    tenant_id BIGINT NOT NULL,                     -- 租户ID
    is_read BOOLEAN DEFAULT FALSE,                 -- 是否已读
    read_time TIMESTAMP,                           -- 阅读时间
    related_id BIGINT,                             -- 关联业务ID
    related_type VARCHAR(50),                      -- 关联业务类型：USER/DEPT/ROLE/ORDER
    related_url VARCHAR(500),                      -- 关联跳转URL
    send_time TIMESTAMP NOT NULL,                  -- 发送时间
    expire_time TIMESTAMP,                         -- 过期时间
    priority INT DEFAULT 0,                        -- 优先级（0-100）
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
CREATE INDEX idx_message_receiver ON sys_message(receiver_id, is_read);
CREATE INDEX idx_message_type ON sys_message(message_type, send_time);
CREATE INDEX idx_message_tenant ON sys_message(tenant_id, is_deleted);
```

#### 2.1.2 sys_message_template (消息模板表)

```sql
CREATE TABLE sys_message_template (
    id BIGINT PRIMARY KEY,
    template_code VARCHAR(100) NOT NULL UNIQUE,    -- 模板编码
    template_name VARCHAR(100) NOT NULL,           -- 模板名称
    template_type VARCHAR(20) NOT NULL,            -- 模板类型：MESSAGE/EMAIL/SMS
    template_category VARCHAR(50),                 -- 模板分类：USER/ORDER/APPROVAL
    template_title VARCHAR(200),                   -- 模板标题（支持占位符）
    template_content TEXT NOT NULL,                -- 模板内容（支持占位符）
    template_params TEXT,                          -- 参数定义（JSON）
    template_example TEXT,                         -- 模板示例
    is_system BOOLEAN DEFAULT FALSE,               -- 是否系统模板
    is_enabled BOOLEAN DEFAULT TRUE,               -- 是否启用
    sort_order INT DEFAULT 0,
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
CREATE UNIQUE INDEX uk_template_code ON sys_message_template(template_code, is_deleted);
CREATE INDEX idx_template_type ON sys_message_template(template_type, is_enabled);
```

#### 2.1.3 sys_notification_config (通知配置表)

```sql
CREATE TABLE sys_notification_config (
    id BIGINT PRIMARY KEY,
    config_code VARCHAR(100) NOT NULL UNIQUE,      -- 配置编码
    config_name VARCHAR(100) NOT NULL,             -- 配置名称
    notify_type VARCHAR(20) NOT NULL,              -- 通知类型：MESSAGE/EMAIL/SMS
    event_type VARCHAR(50) NOT NULL,               -- 事件类型：USER_REGISTER/ORDER_CREATE
    template_code VARCHAR(100),                    -- 关联模板编码
    is_enabled BOOLEAN DEFAULT TRUE,               -- 是否启用
    receiver_rules TEXT,                           -- 接收人规则（JSON）
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

#### 2.1.4 sys_message_subscription (消息订阅表)

```sql
CREATE TABLE sys_message_subscription (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,                       -- 用户ID
    tenant_id BIGINT NOT NULL,                     -- 租户ID
    message_type VARCHAR(50) NOT NULL,             -- 订阅的消息类型
    notify_method VARCHAR(20) NOT NULL,            -- 通知方式：MESSAGE/EMAIL/SMS
    is_enabled BOOLEAN DEFAULT TRUE,               -- 是否启用
    -- 审计字段
    create_tenant BIGINT NOT NULL,
    create_dept BIGINT NOT NULL,
    create_role BIGINT NOT NULL,
    create_by BIGINT NOT NULL,
    create_at TIMESTAMP NOT NULL,
    update_by BIGINT,
    update_at TIMESTAMP,
    is_deleted VARCHAR(20) DEFAULT 'NOT_DELETED',
    deleted_at TIMESTAMP,
    UNIQUE(user_id, message_type, notify_method, is_deleted)
);
```

---

## 3. 目录结构

```
nexusix-notify/src/main/java/com/shy/nexusix/notify/
├── controller/
│   ├── SysMessageController.java               # 站内消息控制器
│   ├── SysMessageTemplateController.java       # 消息模板控制器
│   ├── SysNotificationConfigController.java    # 通知配置控制器
│   └── SysMessageSubscriptionController.java   # 消息订阅控制器
├── converter/
│   ├── SysMessageConverter.java
│   ├── SysMessageTemplateConverter.java
│   └── SysNotificationConfigConverter.java
├── dto/
│   └── (内部传输对象)
├── entity/
│   ├── SysMessage.java
│   ├── SysMessageTemplate.java
│   ├── SysNotificationConfig.java
│   └── SysMessageSubscription.java
├── mapper/
│   ├── SysMessageMapper.java
│   ├── SysMessageMapper.xml
│   ├── SysMessageTemplateMapper.java
│   ├── SysMessageTemplateMapper.xml
│   ├── SysNotificationConfigMapper.java
│   └── SysMessageSubscriptionMapper.java
├── rto/
│   ├── SysMessageSendRTO.java                  # 发送消息请求
│   ├── SysMessageBatchSendRTO.java             # 批量发送消息
│   ├── SysMessageQueryRTO.java                 # 查询消息请求
│   ├── SysMessageTemplateAddRTO.java           # 新增模板
│   ├── SysMessageTemplateUpdateRTO.java        # 更新模板
│   └── SysNotificationConfigRTO.java           # 通知配置
├── service/
│   ├── ISysMessageService.java
│   ├── ISysMessageTemplateService.java
│   ├── IEmailService.java
│   ├── ISmsService.java
│   ├── IWebSocketService.java
│   └── impl/
│       ├── SysMessageServiceImpl.java
│       ├── SysMessageTemplateServiceImpl.java
│       ├── EmailServiceImpl.java
│       ├── SmsServiceImpl.java
│       └── WebSocketServiceImpl.java
├── vo/
│   ├── SysMessageCommonVO.java
│   ├── SysMessageDetailVO.java
│   ├── SysMessageTemplateVO.java
│   └── SysNotificationConfigVO.java
├── event/
│   ├── MessageEvent.java                       # 消息事件
│   └── MessageEventListener.java               # 消息事件监听器
├── websocket/
│   ├── WebSocketHandler.java                   # WebSocket处理器
│   └── WebSocketConfig.java                    # WebSocket配置
└── util/
    ├── TemplateEngine.java                     # 模板引擎
    └── MessageUtil.java                        # 消息工具类
```

---

## 4. SysMessageController (站内消息控制器)

### 4.1 接口列表

| 接口方法 | 请求方式 | 路径 | 功能说明 | 权限编码 |
|---------|---------|------|---------|---------|
| `queryMyMessages()` | GET | `/message/my/page` | 查询我的消息（分页） | - |
| `queryMessageList()` | GET | `/message/list` | 查询消息列表 | MESSAGE:VIEW |
| `queryMessagePage()` | GET | `/message/page` | 分页查询消息 | MESSAGE:VIEW |
| `queryMessageDetail()` | GET | `/message/detail/{code}` | 查询消息详情 | - |
| `sendMessage()` | POST | `/message/send` | 发送单条消息 | MESSAGE:SEND |
| `batchSendMessage()` | POST | `/message/batch-send` | 批量发送消息 | MESSAGE:SEND |
| `broadcastMessage()` | POST | `/message/broadcast` | 群发消息（全员/角色/部门） | MESSAGE:BROADCAST |
| `markAsRead()` | PUT | `/message/read/{code}` | 标记为已读 | - |
| `batchMarkAsRead()` | PUT | `/message/read/batch` | 批量标记已读 | - |
| `markAllAsRead()` | PUT | `/message/read/all` | 全部标记已读 | - |
| `deleteMessage()` | DELETE | `/message/delete/{code}` | 删除消息 | - |
| `batchDeleteMessage()` | DELETE | `/message/delete/batch` | 批量删除消息 | - |
| `getUnreadCount()` | GET | `/message/unread/count` | 获取未读消息数 | - |
| `getUnreadList()` | GET | `/message/unread/list` | 获取未读消息列表 | - |
| `recallMessage()` | POST | `/message/recall/{code}` | 撤回消息 | MESSAGE:RECALL |

### 4.2 核心接口实现

#### 4.2.1 queryMyMessages - 查询我的消息

```java
@GetMapping("/my/page")
@Operation(summary = "查询我的消息", description = "分页查询当前用户的消息")
public ApiResponse queryMyMessages(@Valid PageCommonRTO page, 
                                    @Valid SysMessageQueryRTO param) {
    
    // 1. 获取当前用户ID
    Long userId = UserContext.getCurrentUserId();
    
    // 2. 构建查询条件
    Page<SysMessage> messagePage = new Page<>(page.getPageNum(), page.getPageSize());
    LambdaQueryWrapper<SysMessage> wrapper = new LambdaQueryWrapper<>();
    
    wrapper.eq(SysMessage::getReceiverId, userId)
           .eq(SysMessage::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED);
    
    // 消息类型过滤
    if (StringUtils.isNotBlank(param.getMessageType())) {
        wrapper.eq(SysMessage::getMessageType, param.getMessageType());
    }
    
    // 是否已读过滤
    if (param.getIsRead() != null) {
        wrapper.eq(SysMessage::getIsRead, param.getIsRead());
    }
    
    // 时间范围过滤
    if (param.getSendTimeStart() != null) {
        wrapper.ge(SysMessage::getSendTime, param.getSendTimeStart());
    }
    if (param.getSendTimeEnd() != null) {
        wrapper.le(SysMessage::getSendTime, param.getSendTimeEnd());
    }
    
    // 排序：未读优先，时间倒序
    wrapper.orderByAsc(SysMessage::getIsRead)
           .orderByDesc(SysMessage::getSendTime);
    
    // 3. 执行查询
    IPage<SysMessage> result = messageMapper.selectPage(messagePage, wrapper);
    
    // 4. 转换VO
    IPage<SysMessageCommonVO> voPage = result.convert(messageConverter::toCommonVO);
    
    // 5. 字段权限过滤
    voPage = applyFieldPermissionFilter(voPage);
    
    return ApiResponse.success(voPage);
}
```

#### 4.2.2 sendMessage - 发送消息

```java
@PostMapping("/send")
@Operation(summary = "发送消息")
@SaCheckPermission("MESSAGE:SEND")
@Transactional(rollbackFor = Exception.class)
@OperationLog(module = "消息管理", type = "SEND", desc = "发送消息")
public ApiResponse sendMessage(@Valid @RequestBody SysMessageSendRTO param) {
    
    // 1. 参数校验
    if (param.getReceiverId() == null && CollectionUtils.isEmpty(param.getReceiverIds())) {
        throw new BusinessException("接收人不能为空");
    }
    
    // 2. 获取接收人列表
    List<Long> receiverIds = new ArrayList<>();
    if (param.getReceiverId() != null) {
        receiverIds.add(param.getReceiverId());
    }
    if (!CollectionUtils.isEmpty(param.getReceiverIds())) {
        receiverIds.addAll(param.getReceiverIds());
    }
    
    // 3. 去重
    receiverIds = receiverIds.stream().distinct().collect(Collectors.toList());
    
    // 4. 查询接收人信息
    List<SysUser> receivers = userMapper.selectBatchIds(receiverIds);
    if (receivers.size() != receiverIds.size()) {
        throw new BusinessException("部分接收人不存在");
    }
    
    // 5. 循环发送
    List<String> messageCodes = new ArrayList<>();
    Long senderId = UserContext.getCurrentUserId();
    String senderName = UserContext.getCurrentUserName();
    Long tenantId = TenantContext.getCurrentTenantId();
    
    for (SysUser receiver : receivers) {
        // 生成消息编码
        String messageCode = generateMessageCode();
        
        // 构建消息对象
        SysMessage message = new SysMessage();
        message.setMessageCode(messageCode);
        message.setMessageType(param.getMessageType());
        message.setMessageLevel(param.getMessageLevel());
        message.setMessageTitle(param.getMessageTitle());
        message.setMessageContent(param.getMessageContent());
        message.setSenderId(senderId);
        message.setSenderName(senderName);
        message.setReceiverId(receiver.getId());
        message.setReceiverName(receiver.getNickName());
        message.setTenantId(tenantId);
        message.setIsRead(false);
        message.setSendTime(LocalDateTime.now());
        message.setPriority(param.getPriority() != null ? param.getPriority() : 0);
        
        // 设置关联信息
        if (param.getRelatedId() != null) {
            message.setRelatedId(param.getRelatedId());
            message.setRelatedType(param.getRelatedType());
            message.setRelatedUrl(param.getRelatedUrl());
        }
        
        // 设置过期时间
        if (param.getExpireDays() != null && param.getExpireDays() > 0) {
            message.setExpireTime(LocalDateTime.now().plusDays(param.getExpireDays()));
        }
        
        // 插入消息
        messageMapper.insert(message);
        messageCodes.add(messageCode);
        
        // 发送WebSocket推送
        pushToWebSocket(receiver.getId(), message);
    }
    
    // 6. 返回结果
    Map<String, Object> result = new HashMap<>();
    result.put("successCount", messageCodes.size());
    result.put("messageCodes", messageCodes);
    
    return ApiResponse.success("消息发送成功", result);
}

/**
 * 生成消息编码
 */
private String generateMessageCode() {
    return "MSG" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
            + RandomStringUtils.randomNumeric(6);
}

/**
 * 推送到WebSocket
 */
private void pushToWebSocket(Long userId, SysMessage message) {
    // 构建推送事件
    MessagePushEvent event = new MessagePushEvent();
    event.setUserId(userId);
    event.setMessageCode(message.getMessageCode());
    event.setMessageType(message.getMessageType());
    event.setMessageTitle(message.getMessageTitle());
    event.setSendTime(message.getSendTime());
    
    // 发送到RabbitMQ
    rabbitTemplate.convertAndSend("message.exchange", "message.push", event);
}
```

#### 4.2.3 broadcastMessage - 群发消息

```java
@PostMapping("/broadcast")
@Operation(summary = "群发消息")
@SaCheckPermission("MESSAGE:BROADCAST")
@Transactional(rollbackFor = Exception.class)
@OperationLog(module = "消息管理", type = "BROADCAST", desc = "群发消息")
public ApiResponse broadcastMessage(@Valid @RequestBody SysMessageBroadcastRTO param) {
    
    // 1. 根据广播类型查询接收人列表
    List<Long> receiverIds = new ArrayList<>();
    Long tenantId = TenantContext.getCurrentTenantId();
    
    switch (param.getBroadcastType()) {
        case "ALL":
            // 全员广播：查询租户下所有用户
            receiverIds = userMapper.selectList(
                new LambdaQueryWrapper<SysUser>()
                    .eq(SysUser::getCreateTenant, tenantId)
                    .eq(SysUser::getStatus, "ENABLED")
                    .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED)
            ).stream().map(SysUser::getId).collect(Collectors.toList());
            break;
            
        case "ROLE":
            // 角色广播：查询指定角色的用户
            if (CollectionUtils.isEmpty(param.getRoleCodes())) {
                throw new BusinessException("角色编码不能为空");
            }
            receiverIds = userPolicyMapper.queryUserIdsByRoleCodes(
                tenantId, param.getRoleCodes());
            break;
            
        case "DEPT":
            // 部门广播：查询指定部门的用户
            if (CollectionUtils.isEmpty(param.getDeptCodes())) {
                throw new BusinessException("部门编码不能为空");
            }
            receiverIds = userPolicyMapper.queryUserIdsByDeptCodes(
                tenantId, param.getDeptCodes());
            break;
            
        case "CUSTOM":
            // 自定义：指定用户列表
            if (CollectionUtils.isEmpty(param.getReceiverIds())) {
                throw new BusinessException("接收人列表不能为空");
            }
            receiverIds = param.getReceiverIds();
            break;
            
        default:
            throw new BusinessException("不支持的广播类型");
    }
    
    // 2. 去重
    receiverIds = receiverIds.stream().distinct().collect(Collectors.toList());
    
    if (receiverIds.isEmpty()) {
        throw new BusinessException("没有符合条件的接收人");
    }
    
    // 3. 构建发送请求
    SysMessageSendRTO sendParam = new SysMessageSendRTO();
    BeanUtils.copyProperties(param, sendParam);
    sendParam.setReceiverIds(receiverIds);
    
    // 4. 调用发送方法
    return sendMessage(sendParam);
}
```

#### 4.2.4 使用模板发送消息

```java
/**
 * 使用模板发送消息
 */
@Override
public void sendMessageByTemplate(String templateCode, Long receiverId, 
                                   Map<String, Object> params) {
    
    // 1. 查询模板
    SysMessageTemplate template = templateMapper.selectOne(
        new LambdaQueryWrapper<SysMessageTemplate>()
            .eq(SysMessageTemplate::getTemplateCode, templateCode)
            .eq(SysMessageTemplate::getIsEnabled, true)
            .eq(SysMessageTemplate::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED)
    );
    
    if (template == null) {
        throw new BusinessException("消息模板不存在或已禁用");
    }
    
    // 2. 替换模板占位符
    String title = replacePlaceholder(template.getTemplateTitle(), params);
    String content = replacePlaceholder(template.getTemplateContent(), params);
    
    // 3. 构建发送请求
    SysMessageSendRTO sendParam = new SysMessageSendRTO();
    sendParam.setMessageType("SYSTEM");
    sendParam.setMessageLevel("INFO");
    sendParam.setMessageTitle(title);
    sendParam.setMessageContent(content);
    sendParam.setReceiverId(receiverId);
    
    // 4. 发送消息
    sendMessage(sendParam);
}

/**
 * 替换模板占位符
 * 示例：尊敬的{{userName}}，您的订单{{orderNo}}已创建成功
 */
private String replacePlaceholder(String template, Map<String, Object> params) {
    if (StringUtils.isBlank(template)) {
        return template;
    }
    
    String result = template;
    for (Map.Entry<String, Object> entry : params.entrySet()) {
        String placeholder = "{{" + entry.getKey() + "}}";
        String value = entry.getValue() != null ? String.valueOf(entry.getValue()) : "";
        result = result.replace(placeholder, value);
    }
    
    return result;
}
```

### 4.3 RTO设计

#### SysMessageSendRTO.java

```java
package com.shy.nexusix.notify.rto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class SysMessageSendRTO {
    
    @NotBlank(message = "消息类型不能为空")
    @Pattern(regexp = "^(SYSTEM|NOTIFY|TASK|APPROVAL|WARNING)$", 
             message = "消息类型值错误")
    private String messageType;
    
    @NotBlank(message = "消息级别不能为空")
    @Pattern(regexp = "^(INFO|WARNING|ERROR|CRITICAL)$", 
             message = "消息级别值错误")
    private String messageLevel;
    
    @NotBlank(message = "消息标题不能为空")
    @Length(max = 200, message = "消息标题不能超过200字符")
    private String messageTitle;
    
    @NotBlank(message = "消息内容不能为空")
    private String messageContent;
    
    // 单个接收人（与receiverIds二选一）
    private Long receiverId;
    
    // 多个接收人（与receiverId二选一）
    private List<Long> receiverIds;
    
    // 关联信息
    private Long relatedId;
    private String relatedType;
    private String relatedUrl;
    
    // 优先级（0-100）
    @Min(value = 0, message = "优先级最小为0")
    @Max(value = 100, message = "优先级最大为100")
    private Integer priority;
    
    // 过期天数
    @Min(value = 1, message = "过期天数最小为1")
    @Max(value = 365, message = "过期天数最大为365")
    private Integer expireDays;
}
```

#### SysMessageBroadcastRTO.java

```java
package com.shy.nexusix.notify.rto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class SysMessageBroadcastRTO extends SysMessageSendRTO {
    
    @NotBlank(message = "广播类型不能为空")
    @Pattern(regexp = "^(ALL|ROLE|DEPT|CUSTOM)$", 
             message = "广播类型值错误")
    private String broadcastType;
    
    // 角色编码列表（当broadcastType=ROLE时必填）
    private List<String> roleCodes;
    
    // 部门编码列表（当broadcastType=DEPT时必填）
    private List<String> deptCodes;
}
```

#### SysMessageQueryRTO.java

```java
package com.shy.nexusix.notify.rto;

import lombok.Data;

@Data
public class SysMessageQueryRTO {
    
    private String messageTitle;      // 消息标题（模糊查询）
    private String messageType;       // 消息类型
    private Boolean isRead;           // 是否已读
    private String senderName;        // 发送人（模糊查询）
    private String sendTimeStart;     // 发送时间开始
    private String sendTimeEnd;       // 发送时间结束
}
```

### 4.4 VO设计

#### SysMessageCommonVO.java

```java
package com.shy.nexusix.notify.vo;

import lombok.Data;

@Data
public class SysMessageCommonVO {
    
    private String messageCode;
    private String messageType;
    private String messageTypeDesc;      // 消息类型描述
    private String messageLevel;
    private String messageLevelDesc;     // 消息级别描述
    private String messageTitle;
    private String messageContentPreview; // 内容预览（前100字符）
    private String senderName;
    private Boolean isRead;
    private String readTime;
    private String sendTime;
    private Integer priority;
    private String relatedType;
    private String relatedUrl;
}
```

#### SysMessageDetailVO.java

```java
package com.shy.nexusix.notify.vo;

import lombok.Data;

@Data
public class SysMessageDetailVO extends SysMessageCommonVO {
    
    private String messageContent;       // 完整内容
    private Long senderId;
    private String receiverName;
    private Long relatedId;
    private String expireTime;
    private String createTime;
}
```

---

## 5. SysMessageTemplateController (消息模板控制器)

### 5.1 接口列表

| 接口方法 | 请求方式 | 路径 | 功能说明 | 权限编码 |
|---------|---------|------|---------|---------|
| `queryTemplateList()` | GET | `/template/list` | 查询模板列表 | TEMPLATE:VIEW |
| `queryTemplatePage()` | GET | `/template/page` | 分页查询模板 | TEMPLATE:VIEW |
| `queryTemplateDetail()` | GET | `/template/detail/{code}` | 查询模板详情 | TEMPLATE:VIEW |
| `addTemplate()` | POST | `/template/add` | 新增模板 | TEMPLATE:ADD |
| `updateTemplate()` | PUT | `/template/update` | 更新模板 | TEMPLATE:EDIT |
| `deleteTemplate()` | DELETE | `/template/delete/{code}` | 删除模板 | TEMPLATE:DELETE |
| `testTemplate()` | POST | `/template/test` | 测试模板 | TEMPLATE:TEST |
| `copyTemplate()` | POST | `/template/copy` | 复制模板 | TEMPLATE:COPY |

---

**文档未完待续，billing和dynamic模块将在下一个文档中详细说明...**
