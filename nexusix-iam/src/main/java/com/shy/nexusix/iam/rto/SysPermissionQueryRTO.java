package com.shy.nexusix.iam.rto;

import com.shy.nexusix.common.rto.PageCommonRTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "权限条件查询请求对象")
public class SysPermissionQueryRTO extends PageCommonRTO {

    @Schema(description = "权限名称", example = "用户管理")
    private String permName;

    @Schema(description = "权限标识", example = "system:user")
    private String permCode;

    @Schema(description = "权限类型 (1-菜单 2-按钮 3-接口 4-数据字段)", example = "1")
    private Integer permType;

    @Schema(description = "状态 (1-正常 0-禁用)", example = "1")
    private Integer status;

}
