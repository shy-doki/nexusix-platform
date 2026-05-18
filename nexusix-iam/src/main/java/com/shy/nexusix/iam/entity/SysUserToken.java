package com.shy.nexusix.iam.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2026-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user_token")
@Schema(name="SysUserToken对象", description="用户Token记录表-用于多端登录管理和强制下线")
public class SysUserToken implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @Schema(description = "用户编码")
    private String userCode;

    @Schema(description = "用户名称")
    private String userName;

    @Schema(description = "租户编码")
    private String tenantCode;

    @Schema(description = "租户名称")
    private String tenantName;

    @Schema(description = "登录令牌")
    private String token;

    @Schema(description = "设备信息")
    private String deviceInfo;

    @Schema(description = "最后登录IP")
    private String loginIp;

    @Schema(description = "登录时间")
    private LocalDateTime loginTime;

    @Schema(description = "过期时间")
    private LocalDateTime expireTime;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "逻辑删除")
    private String isDeleted;


}
