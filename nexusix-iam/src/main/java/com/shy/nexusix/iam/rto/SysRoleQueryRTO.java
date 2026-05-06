package com.shy.nexusix.iam.rto;

import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.common.rto.TimeRangeCommonRTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 角色条件查询请求对象
 * </p>
 *
 * @author shy
 * @since 2026-05-06
 */
@Data
@Schema(description = "角色条件查询请求对象")
public class SysRoleQueryRTO extends PageCommonRTO {

    /**
     * 角色名称
     */
    @Schema(description = "角色名称", example = "系统管理员")
    private String roleName;

    /**
     * 角色编码
     */
    @Schema(description = "角色编码", example = "ROLE_ADMIN")
    private String roleCode;

    /**
     * 角色层级 (1-系统 2-租户 3-用户)
     */
    @Schema(description = "角色层级 (1-系统 2-租户 3-用户)", example = "2")
    private Integer roleLevel;

    /**
     * 所属租户ID
     */
    @Schema(description = "所属租户ID", example = "1987654321098765432")
    private String tenantId;

    /**
     * 所属租户名称
     */
    @Schema(description = "所属租户名称", example = "某科技公司")
    private String tenantName;

    /**
     * 数据范围 (1-全部 2-本部门 3-本人 4-自定义)
     */
    @Schema(description = "数据范围 (1-全部 2-本部门 3-本人 4-自定义)", example = "1")
    private Integer dataScope;

    /**
     * 状态 (1-启用 0-禁用)
     */
    @Schema(description = "状态 (1-启用 0-禁用)", example = "1")
    private String status;

    /**
     * 创建人姓名
     */
    @Schema(description = "创建人姓名", example = "张三")
    private String createByName;

    /**
     * 创建时间范围
     */
    @Schema(description = "创建时间范围(一个/多个)[第一个参数为开始时间;第二个参数为结束时间]", example = "{\"startTime\":\"2026-01-01 00:00:00\",\"endTime\":\"2026-12-31 23:59:59\"}")
    private TimeRangeCommonRTO createTime;

}
