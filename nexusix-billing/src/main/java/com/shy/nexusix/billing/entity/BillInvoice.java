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
 * 发票管理表 - 记录租户发票申请与开具
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bill_invoice")
@Schema(name="BillInvoice对象", description="发票管理表 - 记录租户发票申请与开具")
public class BillInvoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键 ID (雪花算法)", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "租户 ID", example = "1987654321098765432")
    @TableField(value = "tenant_id")
    private Long tenantId;

    @Schema(description = "关联订单 ID", example = "1987654321098765432")
    @TableField(value = "order_id")
    private Long orderId;

    @Schema(description = "发票号码", example = "INV202604070001")
    @TableField(value = "invoice_no")
    private String invoiceNo;

    @Schema(description = "发票类型 (1-普票 2-专票 3-电子发票)", example = "1")
    @TableField(value = "invoice_type")
    private Integer invoiceType;

    @Schema(description = "发票抬头", example = "某某科技有限公司")
    @TableField(value = "invoice_title")
    private String invoiceTitle;

    @Schema(description = "税号", example = "91110108MA00000000")
    @TableField(value = "tax_id")
    private String taxId;

    @Schema(description = "发票金额", example = "1000.00")
    @TableField(value = "amount")
    private BigDecimal amount;

    @Schema(description = "状态 (0-待开具 1-已开具 2-已邮寄 3-已作废)", example = "1")
    @TableField(value = "status")
    private Integer status;

    @Schema(description = "发票文件 URL", example = "https://oss.example.com/invoice.pdf")
    @TableField(value = "invoice_url")
    private String invoiceUrl;

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
