package com.shy.nexusix.tenant.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class SysTenantTreeVO extends SysTenantCommonVO {

    @Schema(description = "子租户列表")
    private List<SysTenantTreeVO> children;

}
