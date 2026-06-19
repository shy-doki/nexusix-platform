package com.shy.nexusix.iam.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>权限树形视图对象</p>
 *
 * @author shy
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "权限树形视图对象")
public class SysPermTreeVO extends SysPermCommonVO {

    /**
     * 父权限编码
     */
    @Schema(description = "父权限编码", example = "0")
    private String parentCode;

    /**
     * 子权限
     */
    @Schema(description = "子权限列表")
    private List<SysPermTreeVO> childPerm;

}
