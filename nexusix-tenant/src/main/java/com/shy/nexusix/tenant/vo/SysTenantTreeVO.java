package com.shy.nexusix.tenant.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "租户树形视图对象")
public class SysTenantTreeVO extends SysTenantCommonVO {

    /**
     * 子租户
     */
    private List<SysTenantTreeVO> chileTenant;

}
