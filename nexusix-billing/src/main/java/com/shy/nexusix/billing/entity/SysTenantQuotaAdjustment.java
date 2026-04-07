package com.shy.nexusix.billing.entity;

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
 * 租户配额调整表 - 记录套餐外的配额增减 (加油包/补偿)
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_tenant_quota_adjustment")
@Schema(name="SysTenantQuotaAdjustment对象", description="租户配额调整表 - 记录套餐外的配额增减 (加油包/补偿)")
public class SysTenantQuotaAdjustment implements Serializable {

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

    @Schema(description = "调整数值 (正增负减)", example = "1000")
    @TableField(value = "adjust_value")
    private Long adjustValue;

    @Schema(description = "调整类型 (1-购买加油包 2-补偿 3-惩罚)", example = "1")
    @TableField(value = "adjust_type")
    private Integer adjustType;

    @Schema(description = "调整原因", example = "临时扩容")
    @TableField(value = "reason")
    private String reason;

    @Schema(description = "生效时间", format = "date-time", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "effective_time")
    private LocalDateTime effectiveTime;

    @Schema(description = "过期时间 (NULL 为永久)", format = "date-time", example = "2026-12-31T23:59:59")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "expire_time")
    private LocalDateTime expireTime;

    @Schema(description = "操作人 ID", example = "100")
    @TableField(value = "operator_id")
    private Long operatorId;

    @Schema(description = "创建时间", format = "date-time", example = "2026-04-07 15:45:30")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @Schema(description = "逻辑删除 (0-正常 1-删除)", example = "0")
    @TableField(value = "is_deleted")
    private Integer isDeleted;


}
