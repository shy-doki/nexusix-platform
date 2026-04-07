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
 * 消息模板表 - 定义各类消息的内容模板
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_message_template")
@Schema(name="SysMessageTemplate对象", description="消息模板表 - 定义各类消息的内容模板")
public class SysMessageTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键 ID (雪花算法)", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "模板编码 (如：ORDER_PAID, PACKAGE_EXPIRE_WARNING)", example = "ORDER_PAID")
    @TableField(value = "template_code")
    private String templateCode;

    @Schema(description = "模板名称", example = "订单支付成功通知")
    @TableField(value = "template_name")
    private String templateName;

    @Schema(description = "所属租户 ID (0 为系统模板)", example = "0")
    @TableField(value = "tenant_id")
    private Long tenantId;

    @Schema(description = "业务类型 (如：order, subscription, system)", example = "order")
    @TableField(value = "biz_type")
    private String bizType;

    @Schema(description = "消息类型 (1-通知 2-营销 3-验证 4-提醒)", example = "1")
    @TableField(value = "message_type")
    private Integer messageType;

    @Schema(description = "模板标题", example = "订单支付成功")
    @TableField(value = "template_title")
    private String templateTitle;

    @Schema(description = "模板内容 (支持变量占位符，如 ${userName})", example = "尊敬的${userName}，您的订单已支付成功...")
    @TableField(value = "template_content")
    private String templateContent;

    @Schema(description = "示例数据 (JSONB，用于测试预览)", example = "{\"userName\": \"张三\"}")
    @TableField(value = "template_example")
    private String templateExample;

    @Schema(description = "变量定义 (JSONB，定义变量名/类型/必填)", example = "[{\"name\": \"userName\", \"type\": \"string\", \"required\": true}]")
    @TableField(value = "variables")
    private String variables;

    @Schema(description = "状态 (1-启用 0-停用)", example = "1")
    @TableField(value = "status")
    private Integer status;

    @Schema(description = "版本号", example = "1")
    @TableField(value = "version")
    private Integer version;

    @Schema(description = "语言 (zh-CN/en-US 等)", example = "zh-CN")
    @TableField(value = "language")
    private String language;

    @Schema(description = "创建人 ID", example = "100")
    @TableField(value = "create_by")
    private Long createBy;

    @Schema(description = "创建时间", format = "date-time", example = "2026-04-07 15:45:30")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", format = "date-time", example = "2026-04-07 15:45:30")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @Schema(description = "逻辑删除 (0-正常 1-删除)", example = "0")
    @TableField(value = "is_deleted")
    private Integer isDeleted;


}
