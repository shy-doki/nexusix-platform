package com.shy.nexusix.iam.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
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
 * 系统用户基础信息表
 * </p>
 *
 * @author shy
 * @since 2026-05-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user")
@Schema(name="SysUser对象", description="系统用户基础信息表")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "用户编码", example = "USER_001")
    @TableField(value = "user_code")
    private String userCode;

    @Schema(description = "登录用户名", example = "zhangsan")
    @TableField(value = "user_name")
    private String userName;

    @Schema(description = "加密密码", example = "$2a$10$abc...")
    @TableField(value = "password")
    private String password;

    @Schema(description = "用户昵称", example = "张三")
    @TableField(value = "nick_name")
    private String nickName;

    @Schema(description = "邮箱", example = "zhangsan@example.com")
    @TableField(value = "email")
    private String email;

    @Schema(description = "手机号码", example = "13800138000", pattern = "^1[3-9]\\d{9}$")
    @TableField(value = "phone")
    private String phone;

    @Schema(description = "头像地址", example = "/upload/avatar/2026/05/13/xxx.png")
    @TableField(value = "avatar")
    private String avatar;

    @Schema(description = "用户状态", example = "ENABLED")
    @TableField(value = "status")
    private String status;

    @Schema(description = "最后登录IP", example = "192.168.1.1")
    @TableField(value = "login_ip")
    private String loginIp;

    @Schema(description = "最后登录时间", format = "date-time", example = "2026-05-19 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "login_date")
    private LocalDateTime loginDate;

    @Schema(description = "创建人ID", example = "100")
    @TableField(value = "create_by")
    private String createBy;

    @Schema(description = "创建时间", format = "date-time", example = "2026-05-19 15:45:30")
    @TableField(value = "create_at")
    private LocalDateTime createAt;

    @Schema(description = "更新人ID", example = "100")
    @TableField(value = "update_by")
    private String updateBy;

    @Schema(description = "更新时间", format = "date-time", example = "2026-05-19 15:45:30")
    @TableField(value = "update_at")
    private LocalDateTime updateAt;

    @Schema(description = "逻辑删除", example = "NOT_DELETED")
    @TableField(value = "is_deleted")
    private String isDeleted;

    @Schema(description = "删除时间", example = "2026-05-19 15:45:30")
    @TableField(value = "deleted_at")
    private LocalDateTime deletedAt;


}
