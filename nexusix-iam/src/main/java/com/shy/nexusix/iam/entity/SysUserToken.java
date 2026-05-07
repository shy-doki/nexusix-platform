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
 * 用户 Token 记录表 - 用于多端登录管理和强制下线
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user_token")
@Schema(name="SysUserToken对象", description="用户 Token 记录表 - 用于多端登录管理和强制下线")
public class SysUserToken implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键 ID (雪花算法)", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "用户 ID", example = "1987654321098765432")
    @TableField(value = "user_id")
    private Long userId;

    @Schema(description = "用户名称", example = "张三")
    @TableField(value = "user_name")
    private String userName;

    @Schema(description = "租户 ID", example = "1987654321098765432")
    @TableField(value = "tenant_id")
    private Long tenantId;

    @Schema(description = "租户名称", example = "某科技公司")
    @TableField(value = "tenant_name")
    private String tenantName;

    @Schema(description = "登录令牌", example = "eyJhbGciOiJIUzI1NiJ9...")
    @TableField(value = "token")
    private String token;

    @Schema(description = "设备信息", example = "{\"device\": \"Chrome\", \"os\": \"Windows\"}")
    @TableField(value = "device_info")
    private String deviceInfo;

    @Schema(description = "最后登录 IP", example = "192.168.1.100")
    @TableField(value = "login_ip")
    private String loginIp;

    @Schema(description = "登录时间", format = "date-time", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "login_time")
    private LocalDateTime loginTime;

    @Schema(description = "过期时间", format = "date-time", example = "2026-04-08 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "expire_time")
    private LocalDateTime expireTime;

    @Schema(description = "状态 (1-有效 0-失效)", example = "1")
    @TableField(value = "status")
    private Integer status;

    @Schema(description = "逻辑删除 (0-正常 1-删除)", example = "0")
    @TableField(value = "is_deleted")
    private Integer isDeleted;


}
