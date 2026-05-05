package com.shy.nexusix.iam.rto;

import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.common.rto.TimeRangeCommonRTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "权限策略条件查询请求对象")
public class SysPermissionPolicyQueryRTO extends PageCommonRTO {

    /**
     * 目标类型
     * 1-系统 2-租户 3-角色 4-用户
     */
    @Schema(description = "目标类型 (1-系统 2-租户 3-角色 4-用户)", example = "租户")
    private String targetType;

    /**
     * 目标名称
     */
    @Schema(description = "目标名称", example = "某科技公司")
    private Long targetName;

    /**
     * 关联权限名称
     */
    @Schema(description = "关联权限名称", example = "用户管理")
    private String permName;

    /**
     * 动作
     * 1-允许 2-拒绝
     */
    @Schema(description = "动作 (1-允许 2-拒绝)", example = "允许")
    private String action;

    /**
     * 创建人姓名
     */
    @Schema(description = "创建人姓名", example = "李四")
    private String createByName;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间范围(一个/多个)[第一个参数为开始时间;第二个参数为结束时间]", example = "{\"startTime\":\"2026-01-01 00:00:00\",\"endTime\":\"2026-12-31 23:59:59\"}")
    private TimeRangeCommonRTO createTime;

}
