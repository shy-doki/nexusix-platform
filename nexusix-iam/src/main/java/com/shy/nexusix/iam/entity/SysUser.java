package com.shy.nexusix.iam.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户表：系统级用户实体（全局）
 * </p>
 *
 * @author shy
 * @since 2026-06-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user")
@Schema(name = "SysUser对象", description = "用户表：系统级用户实体（全局）")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID（系统用户ID）", example = "1")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "用户编码", example = "U001")
    @TableField(value = "user_code")
    private String userCode;

    @Schema(description = "用户名（登录名）", example = "admin")
    @TableField(value = "user_name")
    private String userName;

    @Schema(description = "昵称", example = "管理员")
    @TableField(value = "nick_name")
    private String nickName;

    @Schema(description = "真实姓名", example = "张三")
    @TableField(value = "real_name")
    private String realName;

    @Schema(description = "电子邮箱", example = "admin@example.com")
    @TableField(value = "email")
    private String email;

    @Schema(description = "手机号码", example = "13800000001")
    @TableField(value = "phone")
    private String phone;

    @Schema(description = "密码哈希值")
    @TableField(value = "password")
    private String password;

    @Schema(description = "头像URL")
    @TableField(value = "avatar_url")
    private String avatarUrl;

    @Schema(description = "性别：MALE, FEMALE, UNKNOWN", example = "MALE")
    @TableField(value = "gender")
    private String gender;

    @Schema(description = "出生日期", example = "1990-01-01")
    @TableField(value = "birthday")
    private LocalDate birthday;

    @Schema(description = "状态：ENABLED, DISABLED, LOCKED", example = "ENABLED")
    @TableField(value = "status")
    private String status;

    @Schema(description = "禁用原因")
    @TableField(value = "disable_reason")
    private String disableReason;

    @Schema(description = "最后登录时间", format = "date-time", example = "2026-06-12 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Schema(description = "最后登录IP")
    @TableField(value = "last_login_ip")
    private String lastLoginIp;

    @Schema(description = "创建时所属租户ID", example = "0")
    @TableField(value = "create_tenant")
    private Long createTenant;

    @Schema(description = "创建时所属部门ID", example = "0")
    @TableField(value = "create_dept")
    private Long createDept;

    @Schema(description = "创建时使用角色ID", example = "0")
    @TableField(value = "create_role")
    private Long createRole;

    @Schema(description = "创建人用户ID", example = "1")
    @TableField(value = "create_by")
    private Long createBy;

    @Schema(description = "创建时间", format = "date-time", example = "2026-06-12 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "create_at")
    private LocalDateTime createAt;

    @Schema(description = "最后更新人用户ID", example = "1")
    @TableField(value = "update_by")
    private Long updateBy;

    @Schema(description = "最后更新时间", format = "date-time", example = "2026-06-12 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "update_at")
    private LocalDateTime updateAt;

    @Schema(description = "逻辑删除标记", example = "NOT_DELETED")
    @TableField(value = "is_deleted")
    private String isDeleted;

    @Schema(description = "删除时间", format = "date-time", example = "2026-06-12 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "deleted_at")
    private LocalDateTime deletedAt;

}
