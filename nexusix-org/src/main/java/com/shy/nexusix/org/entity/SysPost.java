package com.shy.nexusix.org.entity;

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
 * 岗位表 - 部门下的具体职位
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_post")
@Schema(name="SysPost对象", description="岗位表 - 部门下的具体职位")
public class SysPost implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键 ID (雪花算法)", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "岗位名称", example = "Java 开发工程师")
    @TableField(value = "post_name")
    private String postName;

    @Schema(description = "岗位编码", example = "JAVA_DEV")
    @TableField(value = "post_code")
    private String postCode;

    @Schema(description = "所属部门 ID", example = "1001")
    @TableField(value = "dept_id")
    private Long deptId;

    @Schema(description = "所属租户 ID", example = "1987654321098765432")
    @TableField(value = "tenant_id")
    private Long tenantId;

    @Schema(description = "排序", example = "1")
    @TableField(value = "sort_order")
    private Integer sortOrder;

    @Schema(description = "状态 (1-正常 0-停用)", example = "1")
    @TableField(value = "status")
    private Integer status;

    @Schema(description = "创建时间", format = "date-time", example = "2026-04-07 15:45:30")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @Schema(description = "逻辑删除 (0-正常 1-删除)", example = "0")
    @TableField(value = "is_deleted")
    private Integer isDeleted;


}
