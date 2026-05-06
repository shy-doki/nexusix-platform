package com.shy.nexusix.iam.rto;

import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.common.rto.TimeRangeCommonRTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 权限条件查询请求对象
 * </p>
 *
 * @author shy
 * @since 2026-05-06
 */
@Data
@Schema(description = "权限条件查询请求对象")
public class SysPermissionQueryRTO extends PageCommonRTO {

    /**
     * 权限名称
     */
    @Schema(description = "权限名称", example = "用户管理")
    private String permName;

    /**
     * 权限标识
     */
    @Schema(description = "权限标识", example = "system:user:add")
    private String permCode;

    /**
     * 类型 (1-菜单 2-按钮 3-接口 4-数据字段)
     * 支持数字编码、中文描述或枚举名称查询
     */
    @Schema(description = "类型 (1-菜单 2-按钮 3-接口 4-数据字段)，支持数字编码、中文描述或枚举名称", example = "菜单")
    private String permType;

    /**
     * 父权限ID
     */
    @Schema(description = "父权限ID", example = "1987654321098765432")
    private String parentId;

    /**
     * 父权限名称
     */
    @Schema(description = "父权限名称", example = "用户管理")
    private String parentName;

    /**
     * 状态 (1-启用 0-禁用)
     * 支持数字编码、中文描述或枚举名称查询
     */
    @Schema(description = "状态 (1-启用 0-禁用)，支持数字编码、中文描述或枚举名称", example = "启用")
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
