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
 * 套餐配额模板表 - 定义套餐包含的资源配额
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("prod_package_quota")
@Schema(name="ProdPackageQuota对象", description="套餐配额模板表 - 定义套餐包含的资源配额")
public class ProdPackageQuota implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键 ID (雪花算法)", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "关联套餐 ID", example = "1001")
    @TableField(value = "package_id")
    private Long packageId;

    @Schema(description = "资源类型代码 (如 TOKENS, STORAGE)", example = "TOKENS")
    @TableField(value = "resource_code")
    private String resourceCode;

    @Schema(description = "资源名称", example = "AI Token 额度")
    @TableField(value = "resource_name")
    private String resourceName;

    @Schema(description = "配额数值", example = "100000")
    @TableField(value = "quota_value")
    private Long quotaValue;

    @Schema(description = "单位", example = "次")
    @TableField(value = "unit")
    private String unit;

    @Schema(description = "是否允许超额使用", example = "false")
    @TableField(value = "is_allow_overage")
    private Boolean isAllowOverage;

    @Schema(description = "创建时间", format = "date-time", example = "2026-04-07 15:45:30")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @Schema(description = "逻辑删除 (0-正常 1-删除)", example = "0")
    @TableField(value = "is_deleted")
    private Integer isDeleted;


}
