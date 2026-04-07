package com.shy.nexusix.system.entity;

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
 * 字典类型表 - 定义系统常量类型
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_dict")
@Schema(name="SysDict对象", description="字典类型表 - 定义系统常量类型")
public class SysDict implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键 ID (雪花算法)", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "字典名称", example = "性别")
    @TableField(value = "dict_name")
    private String dictName;

    @Schema(description = "字典类型", example = "sys_user_sex")
    @TableField(value = "dict_type")
    private String dictType;

    @Schema(description = "所属租户 (0 为系统字典)", example = "0")
    @TableField(value = "tenant_id")
    private Long tenantId;

    @Schema(description = "状态 (1-正常 0-停用)", example = "1")
    @TableField(value = "status")
    private Integer status;

    @Schema(description = "备注", example = "用户性别字典")
    @TableField(value = "remark")
    private String remark;

    @Schema(description = "创建时间", format = "date-time", example = "2026-04-07 15:45:30")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @Schema(description = "逻辑删除 (0-正常 1-删除)", example = "0")
    @TableField(value = "is_deleted")
    private Integer isDeleted;


}
