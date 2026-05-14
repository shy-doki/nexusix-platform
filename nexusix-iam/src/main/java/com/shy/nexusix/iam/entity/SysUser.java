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
 * 用户基础表-存储全局用户信息 (不区分租户)
 * </p>
 *
 * @author shy
 * @since 2026-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user")
@Schema(name="SysUser对象", description="用户基础表-存储全局用户信息 (不区分租户)")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @Schema(description = "用户名", example = "admin")
    @TableField(value = "username")
    private String username;

    @Schema(description = "加密密码", example = "$2a$10$xxxxx")
    @TableField(value = "password")
    private String password;

    @Schema(description = "昵称", example = "管理员")
    @TableField(value = "nickname")
    private String nickname;

    @Schema(description = "邮箱", example = "admin@example.com")
    @TableField(value = "email")
    private String email;

    @Schema(description = "手机号", example = "13800138000", pattern = "^1[3-9]\\d{9}$")
    @TableField(value = "phone")
    private String phone;

    @Schema(description = "头像地址", example = "/upload/avatar/2026/05/13/xxx.png")
    @TableField(value = "avatar")
    private String avatar;

    @Schema(description = "全局状态", example = "ENABLED")
    @TableField(value = "status")
    private String status;

    @Schema(description = "最后登录IP", example = "192.168.1.100")
    @TableField(value = "login_ip")
    private String loginIp;

    @Schema(description = "最后登录时间", example = "2026-05-13 15:45:30")
    @TableField(value = "login_date")
    private LocalDateTime loginDate;

    @Schema(description = "创建人ID", example = "100")
    @TableField(value = "create_by")
    private String createBy;

    @Schema(description = "创建人姓名", example = "张三")
    @TableField(value = "create_by_name")
    private String createByName;

    @Schema(description = "更新人ID", example = "100")
    @TableField(value = "update_by")
    private String updateBy;

    @Schema(description = "更新人姓名", example = "张三")
    @TableField(value = "update_by_name")
    private String updateByName;

    @Schema(description = "更新时间", format = "date-time", example = "2026-05-13 15:45:30")
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    @Schema(description = "创建时间", format = "date-time", example = "2026-05-13 15:45:30")
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @Schema(description = "逻辑删除", example = "ACTIVE")
    @TableField(value = "is_deleted")
    private String isDeleted;

}
