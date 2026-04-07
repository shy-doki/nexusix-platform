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
 * 定时消息任务表 - 预约发送或周期性发送的消息
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_message_schedule")
@Schema(name="SysMessageSchedule对象", description="定时消息任务表 - 预约发送或周期性发送的消息")
public class SysMessageSchedule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键 ID (雪花算法)", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "任务名称", example = "每日账单提醒")
    @TableField(value = "task_name")
    private String taskName;

    @Schema(description = "租户 ID", example = "1987654321098765432")
    @TableField(value = "tenant_id")
    private Long tenantId;

    @Schema(description = "消息模板 ID", example = "1001")
    @TableField(value = "template_id")
    private Long templateId;

    @Schema(description = "目标类型 (1-指定用户 2-指定租户 3-符合条件的所有用户)", example = "1")
    @TableField(value = "target_type")
    private Integer targetType;

    @Schema(description = "目标 ID 集合 (逗号分隔)", example = "100,200,300")
    @TableField(value = "target_ids")
    private String targetIds;

    @Schema(description = "触发类型 (1-单次定时 2-周期循环 3-事件触发)", example = "2")
    @TableField(value = "trigger_type")
    private Integer triggerType;

    @Schema(description = "触发条件 (JSONB，定义时间/事件规则)", example = "{\"time\": \"09:00\"}")
    @TableField(value = "trigger_condition")
    private String triggerCondition;

    @Schema(description = "计划执行时间", format = "date-time", example = "2026-04-08 09:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "execute_time")
    private LocalDateTime executeTime;

    @Schema(description = "重复规则 (JSONB，如{"type":"daily","interval":1})", example = "{\"type\": \"daily\", \"interval\": 1}")
    @TableField(value = "repeat_rule")
    private String repeatRule;

    @Schema(description = "状态 (0-待执行 1-执行中 2-已完成 3-已取消)", example = "0")
    @TableField(value = "status")
    private Integer status;

    @Schema(description = "已执行次数", example = "5")
    @TableField(value = "executed_count")
    private Integer executedCount;

    @Schema(description = "最后执行时间", format = "date-time", example = "2026-04-07 09:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "last_execute_time")
    private LocalDateTime lastExecuteTime;

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
