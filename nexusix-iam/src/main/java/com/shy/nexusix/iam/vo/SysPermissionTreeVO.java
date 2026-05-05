package com.shy.nexusix.iam.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "权限树形视图对象")
public class SysPermissionTreeVO extends SysPermissionCommonVO {

    /**
     * 子权限列表
     * 用于构建树形结构
     */
    @Schema(description = "子权限列表")
    private List<SysPermissionTreeVO> childPermission;

}
