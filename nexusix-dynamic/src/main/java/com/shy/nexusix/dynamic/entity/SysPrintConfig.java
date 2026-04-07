package com.shy.nexusix.dynamic.entity;

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
 * 打印模板配置表 - 实现不同行业打印/PDF 模板动态配置
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_print_config")
@Schema(name="SysPrintConfig对象", description="打印模板配置表 - 实现不同行业打印/PDF 模板动态配置")
public class SysPrintConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键 ID (雪花算法)", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "所属租户 ID", example = "1987654321098765432")
    @TableField(value = "tenant_id")
    private Long tenantId;

    @Schema(description = "模板编码 (如：education_contract, restaurant_receipt)", example = "education_contract")
    @TableField(value = "template_code")
    private String templateCode;

    @Schema(description = "模板名称", example = "教育培训合同模板")
    @TableField(value = "template_name")
    private String templateName;

    @Schema(description = "业务类型", example = "education_order")
    @TableField(value = "biz_type")
    private String bizType;

    @Schema(description = "模板类型 (1-HTML 2-Markdown 3-JSON 配置)", example = "1")
    @TableField(value = "template_type")
    private Integer templateType;

    @Schema(description = "模板内容 (HTML/模板引擎语法)", example = "<h1>{{title}}</h1>")
    @TableField(value = "template_content")
    private String templateContent;

    @Schema(description = "模板配置 (JSONB，定义变量/条件显示)", example = "{\"variables\": [\"title\", \"content\"]}")
    @TableField(value = "template_config")
    private String templateConfig;

    @Schema(description = "纸张大小 (A4/A5/80mm 热敏纸等)", example = "A4")
    @TableField(value = "paper_size")
    private String paperSize;

    @Schema(description = "纸张方向 (portrait/landscape)", example = "portrait")
    @TableField(value = "orientation")
    private String orientation;

    @Schema(description = "状态 (1-启用 0-停用)", example = "1")
    @TableField(value = "status")
    private Integer status;

    @Schema(description = "是否默认模板", example = "false")
    @TableField(value = "is_default")
    private Boolean isDefault;

    @Schema(description = "版本号", example = "1")
    @TableField(value = "version")
    private Integer version;

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
