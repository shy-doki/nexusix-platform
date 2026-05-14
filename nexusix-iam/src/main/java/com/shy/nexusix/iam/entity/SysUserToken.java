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
 * 用户Token记录表-用于多端登录管理和强制下线
 * </p>
 *
 * @author shy
 * @since 2026-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user_token")
@Schema(name="SysUserToken对象", description="用户Token记录表-用于多端登录管理和强制下线")
public class SysUserToken implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @Schema(description = "用户ID", example = "1001")
    @TableField(value = "user_id")
    private String userId;

    @Schema(description = "用户名称", example = "admin")
    @TableField(value = "user_name")
    private String userName;

    @Schema(description = "租户ID", example = "TENANT_001")
    @TableField(value = "tenant_id")
    private String tenantId;

    @Schema(description = "租户名称", example = "某某科技有限公司")
    @TableField(value = "tenant_name")
    private String tenantName;

    @Schema(description = "登录令牌", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    @TableField(value = "token")
    private String token;

    @Schema(description = "设备信息", example = "Chrome 120.0 on Windows 10")
    @TableField(value = "device_info")
    private String deviceInfo;

    @Schema(description = "最后登录IP", example = "192.168.1.100")
    @TableField(value = "login_ip")
    private String loginIp;

    @Schema(description = "登录时间", example = "2026-05-13 15:45:30")
    @TableField(value = "login_time")
    private LocalDateTime loginTime;

    @Schema(description = "过期时间", example = "2026-05-14 15:45:30")
    @TableField(value = "expire_time")
    private LocalDateTime expireTime;

    @Schema(description = "状态", example = "ACTIVE")
    @TableField(value = "status")
    private String status;

    @Schema(description = "逻辑删除", example = "ACTIVE")
    @TableField(value = "is_deleted")
    private String isDeleted;

}
