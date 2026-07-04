package com.shy.nexusix.iam.rto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * <p>角色授予权限请求传输对象</p>
 *
 * @author shy
 */
@Data
@Schema(description = "角色授予权限请求参数")
public class SysRoleGrantPermRTO {

    /**
     * 权限编码列表
     */
    @Schema(description = "权限编码列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "权限编码列表不能为空")
    private List<String> permCodeList;

}
