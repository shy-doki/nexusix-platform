package com.shy.nexusix.notify.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 站内信收件箱表 - 用户个人消息 inbox
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_inbox_message")
@Schema(name="SysInboxMessage对象", description="站内信收件箱表 - 用户个人消息 inbox")
public class SysInboxMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键 ID (雪花算法)", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "用户 ID", example = "1987654321098765432")
    @TableField(value = "user_id")
    private Long userId;

    @Schema(description = "租户 ID", example = "1987654321098765432")
    @TableField(value = "tenant_id")
    private Long tenantId;

    @Schema(description = "消息类型 (1-系统通知 2-审批通知 3-账单提醒 4-营销活动)", example = "1")
    @TableField(value = "message_type")
    private Integer messageType;

    @Schema(description = "消息标题", example = "订单支付成功通知")
    @TableField(value = "title")
    private String title;

    @Schema(description = "消息内容", example = "您的订单已支付成功...")
    @TableField(value = "content")
    private String content;

    @Schema(description = "优先级 (1-普通 2-重要 3-紧急)", example = "1")
    @TableField(value = "priority")
    private Integer priority;

    @Schema(description = "是否已读", example = "false")
    @TableField(value = "is_read")
    private Boolean isRead;

    @Schema(description = "阅读时间", format = "date-time", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "read_time")
    private LocalDateTime readTime;

    @Schema(description = "是否已归档", example = "false")
    @TableField(value = "is_archived")
    private Boolean isArchived;

    @Schema(description = "归档时间", format = "date-time", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "archived_time")
    private LocalDateTime archivedTime;

    @Schema(description = "过期时间 (NULL 为永不过期)", format = "date-time", example = "2026-12-31T23:59:59")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "expire_time")
    private LocalDateTime expireTime;

    @Schema(description = "操作按钮 URL", example = "/order/detail/123")
    @TableField(value = "action_url")
    private String actionUrl;

    @Schema(description = "操作按钮文案", example = "查看详情")
    @TableField(value = "action_text")
    private String actionText;

    @Schema(description = "创建时间", format = "date-time", example = "2026-04-07 15:45:30")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;


}
