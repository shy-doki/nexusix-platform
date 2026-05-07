package com.shy.nexusix.iam.rto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shy.nexusix.common.annotation.EnumField;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.enums.GlobalEnum.PermStatus;
import com.shy.nexusix.common.enums.GlobalEnum.PermType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

import static com.shy.nexusix.common.constant.RegexConstant.Code.SNOWFLAKE_ID;

/**
 * <p>
 * 权限更新请求对象
 * </p>
 *
 * @author shy
 * @since 2026-05-06
 */
@Data
@Schema(description = "权限更新请求对象")
public class SysPermissionUpdateRTO {

    /**
     * <p>主键Id</p>
     * <p>TODO 后续考虑加入加解密注解 这里就不加入正则判断 因为id默认加密传输</p>
     */
    @NotNull(message = "Id不能为空")
    @Schema(description = "主键 ID (雪花算法)", example = "1987654321098765432")
    private Long id;

    /**
     * 权限名称
     */
    @NotBlank(message = "权限名称不能为空")
    @Size(min = 2, max = 100, message = "权限名称必须在2-100字符之间")
    @Schema(description = "权限名称", example = "用户管理")
    private String permName;

    /**
     * 权限标识 (如 system:user:add)
     */
    @NotBlank(message = "权限标识不能为空")
    @Schema(description = "权限标识", example = "system:user:add")
    private String permCode;

    /**
     * 类型(通过枚举转换)
     */
    @NotNull(message = "权限类型不能为空")
    @EnumField
    @Schema(description = "类型(通过枚举转换)", example = "菜单")
    private PermType permType;

    /**
     * 父权限ID
     */
    @NotNull(message = "父权限ID不能为空")
    @Pattern(regexp = SNOWFLAKE_ID, message = "父权限ID格式不正确")
    @Schema(description = "父权限ID", example = "1987654321098765432")
    private String parentId;

    /**
     * 父权限名称
     */
    @NotBlank(message = "父权限名称不能为空")
    @Size(min = 2, max = 100, message = "父权限名称必须在2-100字符之间")
    @Schema(description = "父权限名称", example = "系统管理")
    private String parentName;

    /**
     * 资源路径
     */
    @Schema(description = "资源路径", example = "/system/user")
    private String path;

    /**
     * 状态(通过枚举转换)
     */
    @NotNull(message = "状态不能为空")
    @EnumField
    @Schema(description = "状态(通过枚举转换)", example = "启用")
    private PermStatus status;

    /**
     * 创建人ID
     * 默认为当前登录用户
     * 超级管理员可以指定创建人ID
     */
    @Schema(description = "创建人ID", example = "100")
    private Long createBy;

    /**
     * 创建人姓名
     * 默认为创建人姓名
     * 超级管理员可以指定创建人姓名
     */
    @Schema(description = "创建人姓名", example = "张三")
    private String createByName;

    /**
     * 更新人ID
     * 默认为当前登录用户
     * 超级管理员可以指定更新人ID
     */
    @Schema(description = "更新人ID", example = "100")
    private Long updateBy;

    /**
     * 更新人姓名
     * 默认为创建人姓名
     * 超级管理员可以指定更新人姓名
     */
    @Schema(description = "更新人姓名", example = "张三")
    private String updateByName;

    /**
     * 创建时间
     * 默认为当前时间
     * 超级管理员可以指定创建时间
     */
    @Schema(description = "创建时间", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 更新时间
     * 默认为当前时间
     * 超级管理员可以指定更新时间
     */
    @Schema(description = "更新时间", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    /**
     * 逻辑删除 (0-正常 1-删除)
     * 默认为0
     * 超级管理员可以指定逻辑删除状态
     */
    @Schema(description = "逻辑删除 (0-正常 1-删除)", example = "未删除")
    @EnumField
    private GlobalEnum.Deleted isDeleted;

}
