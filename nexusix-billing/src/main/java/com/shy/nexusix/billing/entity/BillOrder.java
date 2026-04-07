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
 * 订单表 - 记录租户购买订单
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bill_order")
@Schema(name="BillOrder对象", description="订单表 - 记录租户购买订单")
public class BillOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键 ID (雪花算法)", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "租户 ID", example = "1987654321098765432")
    @TableField(value = "tenant_id")
    private Long tenantId;

    @Schema(description = "订单号", example = "ORD202604070001")
    @TableField(value = "order_no")
    private String orderNo;

    @Schema(description = "产品类型 (1-套餐 2-配额包)", example = "1")
    @TableField(value = "product_type")
    private Integer productType;

    @Schema(description = "产品 ID", example = "1001")
    @TableField(value = "product_id")
    private Long productId;

    @Schema(description = "订单总金额", example = "1000.00")
    @TableField(value = "total_amount")
    private BigDecimal totalAmount;

    @Schema(description = "实际支付金额", example = "1000.00")
    @TableField(value = "pay_amount")
    private BigDecimal payAmount;

    @Schema(description = "订单状态 (0-未付 1-已付 2-取消)", example = "1")
    @TableField(value = "status")
    private Integer status;

    @Schema(description = "支付时间", format = "date-time", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "pay_time")
    private LocalDateTime payTime;

    @Schema(description = "支付渠道", example = "ALIPAY")
    @TableField(value = "pay_channel")
    private String payChannel;

    @Schema(description = "订单明细 (JSONB，商品/服务列表)", example = "[{\"name\": \"套餐A\", \"price\": 100}]")
    @TableField(value = "order_items")
    private String orderItems;

    @Schema(description = "行业扩展属性 (JSONB，存储行业特有订单字段)", example = "{\"industry\": \"tech\"}")
    @TableField(value = "ext_attributes")
    private String extAttributes;

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
