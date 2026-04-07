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
 * 动态数据源配置表 - 实现下拉框数据来源动态配置
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_datasource_config")
@Schema(name="SysDatasourceConfig对象", description="动态数据源配置表 - 实现下拉框数据来源动态配置")
public class SysDatasourceConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键 ID (雪花算法)", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "所属租户 ID", example = "1987654321098765432")
    @TableField(value = "tenant_id")
    private Long tenantId;

    @Schema(description = "数据源编码 (如：dish_list, part_list)", example = "dish_list")
    @TableField(value = "datasource_code")
    private String datasourceCode;

    @Schema(description = "数据源名称", example = "菜品列表")
    @TableField(value = "datasource_name")
    private String datasourceName;

    @Schema(description = "数据源类型 (1-业务表 2-API 接口 3-字典 4-SQL 查询)", example = "1")
    @TableField(value = "datasource_type")
    private Integer datasourceType;

    @Schema(description = "数据源配置 (JSONB，定义表名/字段/条件)", example = "{\"table\": \"sys_dept\", \"label\": \"dept_name\", \"value\": \"id\"}")
    @TableField(value = "datasource_config")
    private String datasourceConfig;

    @Schema(description = "是否启用缓存", example = "true")
    @TableField(value = "cache_enabled")
    private Boolean cacheEnabled;

    @Schema(description = "缓存过期时间 (秒)", example = "3600")
    @TableField(value = "cache_expire")
    private Integer cacheExpire;

    @Schema(description = "状态 (1-启用 0-停用)", example = "1")
    @TableField(value = "status")
    private Integer status;

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
