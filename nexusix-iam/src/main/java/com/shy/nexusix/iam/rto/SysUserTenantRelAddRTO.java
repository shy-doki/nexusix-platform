package com.shy.nexusix.iam.rto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shy.nexusix.common.annotation.EnumField;
import com.shy.nexusix.common.enums.GlobalEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

import static com.shy.nexusix.common.constant.RegexConstant.Code.SNOWFLAKE_ID;

/**
 * <p>
 * 用户租户关联新增请求对象
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
@Schema(description = "用户租户关联新增请求对象")
public class SysUserTenantRelAddRTO {

    /**
     * 用户ID
     */
    @NotBlank(message = "用户ID不能为空")
    @Pattern(regexp = SNOWFLAKE_ID, message = "用户ID格式不正确")
    @Schema(description = "用户ID", example = "1987654321098765432")
    private String userId;

    /**
     * 租户ID
     */
    @NotBlank(message = "租户ID不能为空")
    @Pattern(regexp = SNOWFLAKE_ID, message = "租户ID格式不正确")
    @Schema(description = "租户ID", example = "1987654321098765432")
    private String tenantId;

    /**
     * 主部门ID
     */
    @Schema(description = "主部门ID (必须属于当前租户)", example = "1001")
    private Long deptId;

    /**
     * 是否租户管理员
     */
    @Schema(description = "是否租户管理员", example = "false")
    private Boolean isAdmin;

    /**
     * 加入时间
     */
    @Schema(description = "加入时间", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime joinTime;

    /**
     * 创建人ID
     */
    @Schema(description = "创建人ID", example = "100")
    private Long createBy;

    /**
     * 创建人姓名
     */
    @Schema(description = "创建人姓名", example = "张三")
    private String createByName;

    /**
     * 更新人ID
     */
    @Schema(description = "更新人ID", example = "100")
    private Long updateBy;

    /**
     * 更新人姓名
     */
    @Schema(description = "更新人姓名", example = "张三")
    private String updateByName;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     */
    @Schema(description = "逻辑删除 (0-正常 1-删除)", example = "0")
    @EnumField
    private GlobalEnum.Deleted isDeleted;

}
