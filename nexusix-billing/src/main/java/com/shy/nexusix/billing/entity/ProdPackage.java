package com.shy.nexusix.billing.entity;

import java.math.BigDecimal;
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
 * 产品套餐定义表 - 定义可售卖的套餐模板
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("prod_package")
@Schema(name="ProdPackage对象", description="产品套餐定义表 - 定义可售卖的套餐模板")
public class ProdPackage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键 ID (雪花算法)", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "套餐名称", example = "企业版套餐")
    @TableField(value = "package_name")
    private String packageName;

    @Schema(description = "套餐编码", example = "PKG_ENT_001")
    @TableField(value = "package_code")
    private String packageCode;

    @Schema(description = "套餐描述", example = "适用于中大型企业的全功能套餐")
    @TableField(value = "description")
    private String description;

    @Schema(description = "价格", example = "999.00")
    @TableField(value = "price")
    private BigDecimal price;

    @Schema(description = "周期类型 (1-天 2-月 3-年)", example = "2")
    @TableField(value = "cycle_type")
    private Integer cycleType;

    @Schema(description = "周期数值", example = "1")
    @TableField(value = "cycle_value")
    private Integer cycleValue;

    @Schema(description = "状态 (1-上架 0-下架)", example = "1")
    @TableField(value = "status")
    private Integer status;

    @Schema(description = "排序", example = "1")
    @TableField(value = "sort_order")
    private Integer sortOrder;

    @Schema(description = "扩展配置 (JSONB，存储套餐功能开关)", example = "{\"featureA\": true}")
    @TableField(value = "ext_config")
    private String extConfig;

    @Schema(description = "创建人 ID", example = "100")
    @TableField(value = "create_by")
    private Long createBy;

    @Schema(description = "更新人 ID", example = "100")
    @TableField(value = "update_by")
    private Long updateBy;

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
