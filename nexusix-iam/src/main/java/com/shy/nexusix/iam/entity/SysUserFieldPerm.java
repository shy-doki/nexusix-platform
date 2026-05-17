package com.shy.nexusix.iam.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户字段权限表-存储用户在各表上的字段操作权限（JSONB压缩存储，由权限体系计算后的扁平化结果）
 * </p>
 *
 * @author shy
 * @since 2026-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user_field_perm")
@Schema(name="SysUserFieldPerm对象", description="用户字段权限表-存储用户在各表上的字段操作权限（JSONB压缩存储，由权限体系计算后的扁平化结果）")
public class SysUserFieldPerm implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @Schema(description = "用户ID")
    private String userId;

    @Schema(description = "用户名称（冗余，便于展示）")
    private String userName;

    @Schema(description = "租户ID（同一用户在不同租户下字段权限可能不同）")
    private String tenantId;

    @Schema(description = "租户名称（冗余，便于展示）")
    private String tenantName;

    @Schema(description = "数据库表名")
    private String tableName;

    @Schema(description = "表描述")
    private String tableDesc;

    @Schema(description = "访问类型")
    private String accessType;

    @Schema(description = "字段操作权限矩阵（JSONB，key=字段名，value=操作类型数组，如{\"phone\":{\"ops\": [\"QUERY\"],\"policy_id\": \"12356\",\"policy_type\": \"系统初始化\",\"policy_name\": \"系统初始化\"},\"email\":[\"QUERY\"]}）")
    private String fieldOperates;

    @Schema(description = "创建人ID")
    private String createBy;

    @Schema(description = "创建人名称")
    private String createByName;

    @Schema(description = "更新人ID")
    private String updateBy;

    @Schema(description = "更新人名称")
    private String updateByName;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Schema(description = "逻辑删除")
    private String isDeleted;


}
