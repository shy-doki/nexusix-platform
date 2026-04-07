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
 * 动态表单配置表 - 实现不同行业表单字段动态配置
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_form_config")
@Schema(name="SysFormConfig对象", description="动态表单配置表 - 实现不同行业表单字段动态配置")
public class SysFormConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键 ID (雪花算法)", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "所属租户 ID", example = "1987654321098765432")
    @TableField(value = "tenant_id")
    private Long tenantId;

    @Schema(description = "业务类型 (如：education_order, restaurant_order)", example = "education_order")
    @TableField(value = "biz_type")
    private String bizType;

    @Schema(description = "表单名称", example = "教育培训订单表单")
    @TableField(value = "form_name")
    private String formName;

    @Schema(description = "表单结构配置 (JSONB，定义字段类型/校验/选项)", example = "{\"fields\": [{\"name\": \"studentName\", \"type\": \"text\"}]}")
    @TableField(value = "form_schema")
    private String formSchema;

    @Schema(description = "状态 (1-启用 0-停用)", example = "1")
    @TableField(value = "status")
    private Integer status;

    @Schema(description = "版本号 (支持表单版本管理)", example = "1")
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
