package com.shy.nexusix.audit.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 数据变更审计表 - 记录数据字段变更详情
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_data_audit_log")
@Schema(name="SysDataAuditLog对象", description="数据变更审计表 - 记录数据字段变更详情")
public class SysDataAuditLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键 ID (雪花算法)", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "租户 ID", example = "1987654321098765432")
    @TableField(value = "tenant_id")
    private Long tenantId;

    @Schema(description = "表名", example = "sys_user")
    @TableField(value = "table_name")
    private String tableName;

    @Schema(description = "记录 ID", example = "1987654321098765432")
    @TableField(value = "record_id")
    private Long recordId;

    @Schema(description = "操作人 ID", example = "100")
    @TableField(value = "operator_id")
    private Long operatorId;

    @Schema(description = "操作类型 (1-更新 2-删除)", example = "1")
    @TableField(value = "operate_type")
    private Integer operateType;

    @Schema(description = "修改前数据 (JSONB)", example = "{\"name\": \"张三\"}")
    @TableField(value = "old_value")
    private String oldValue;

    @Schema(description = "修改后数据 (JSONB)", example = "{\"name\": \"李四\"}")
    @TableField(value = "new_value")
    private String newValue;

    @Schema(description = "操作时间", format = "date-time", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "operate_time")
    private LocalDateTime operateTime;


}
