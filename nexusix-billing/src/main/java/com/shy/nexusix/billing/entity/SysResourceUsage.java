package com.shy.nexusix.billing.entity;

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
 * 资源使用计量表 - 记录租户资源消耗流水
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_resource_usage")
@Schema(name="SysResourceUsage对象", description="资源使用计量表 - 记录租户资源消耗流水")
public class SysResourceUsage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键 ID (雪花算法)", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "租户 ID", example = "1987654321098765432")
    @TableField(value = "tenant_id")
    private Long tenantId;

    @Schema(description = "资源类型代码", example = "TOKENS")
    @TableField(value = "resource_code")
    private String resourceCode;

    @Schema(description = "消耗数值", example = "100")
    @TableField(value = "usage_value")
    private Long usageValue;

    @Schema(description = "业务类型", example = "CHAT_COMPLETION")
    @TableField(value = "business_type")
    private String businessType;

    @Schema(description = "关联业务 ID", example = "1987654321098765432")
    @TableField(value = "business_id")
    private Long businessId;

    @Schema(description = "消耗时间", format = "date-time", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "usage_time")
    private LocalDateTime usageTime;

    @Schema(description = "备注", example = "正常调用消耗")
    @TableField(value = "remark")
    private String remark;


}
