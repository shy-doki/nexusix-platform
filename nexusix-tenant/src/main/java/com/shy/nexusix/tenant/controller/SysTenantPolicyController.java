package com.shy.nexusix.tenant.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.tenant.rto.SysTenantPolicyAddRTO;
import com.shy.nexusix.tenant.rto.SysTenantPolicyBatchBindRTO;
import com.shy.nexusix.tenant.rto.SysTenantPolicyBatchUnbindRTO;
import com.shy.nexusix.tenant.rto.SysTenantPolicyQueryRTO;
import com.shy.nexusix.tenant.rto.SysTenantPolicyUpdateRTO;
import com.shy.nexusix.tenant.service.ISysTenantPolicyService;
import com.shy.nexusix.tenant.vo.SysTenantPolicyCommonVO;
import com.shy.nexusix.tenant.vo.SysTenantPolicyDetailVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 租户策略表 - 管理部门/角色与租户的绑定关系
 * </p>
 *
 * @author shy
 * @since 2026-06-13
 */
@RestController
@RequestMapping("/tenant-policy")
@Tag(name = "租户策略管理", description = "租户策略相关接口")
@Validated
public class SysTenantPolicyController {

    @Autowired
    private ISysTenantPolicyService iSysTenantPolicyService;

    /**
     * <p>查询租户策略列表</p>
     * <p>返回所有租户策略的平铺列表</p>
     *
     * @return 租户策略列表
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-06-13
     */
    @GetMapping("/list")
    @Operation(summary = "查询租户策略列表", description = "返回所有租户策略列表")
    public ApiResponse queryPolicyList() {
        List<SysTenantPolicyCommonVO> policyList = iSysTenantPolicyService.queryPolicyList();
        return ApiResponse.success(policyList);
    }

    /**
     * <p>分页查询租户策略列表</p>
     * <p>返回分页后的租户策略列表</p>
     *
     * @param page 分页参数
     * @return 分页后的租户策略列表
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限时抛出
     * @author shy
     * @since 2026-06-13
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询租户策略列表", description = "返回分页后的租户策略列表")
    public ApiResponse queryPolicyPage(@Valid PageCommonRTO page) {
        IPage<SysTenantPolicyCommonVO> policyPage = iSysTenantPolicyService.queryPolicyPage(page);
        return ApiResponse.success(policyPage);
    }

    /**
     * <p>条件查询租户策略列表</p>
     * <p>返回满足条件的租户策略列表</p>
     *
     * @param queryParam 查询条件
     * @return 满足条件的租户策略列表
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-06-13
     */
    @PostMapping("/query")
    @Operation(summary = "条件查询租户策略列表", description = "返回满足条件的租户策略列表")
    public ApiResponse queryPolicy(@Valid @RequestBody SysTenantPolicyQueryRTO queryParam) {
        IPage<SysTenantPolicyCommonVO> policyPage = iSysTenantPolicyService.queryPolicy(queryParam);
        return ApiResponse.success(policyPage);
    }

    /**
     * <p>查询租户策略详情</p>
     * <p>返回指定租户策略的详情信息</p>
     *
     * @param policyCode 策略编码
     * @return 租户策略详情信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-06-13
     */
    @GetMapping("/detail/{policyCode}")
    @Operation(summary = "查询租户策略详情", description = "返回指定租户策略的详情信息")
    public ApiResponse queryPolicyDetail(@NotBlank(message = "策略编码不能为空") @PathVariable String policyCode) {
        SysTenantPolicyDetailVO policyDetail = iSysTenantPolicyService.queryPolicyDetail(policyCode);
        return ApiResponse.success(policyDetail);
    }

    /**
     * <p>新增租户策略</p>
     * <p>创建部门/角色与租户的绑定关系</p>
     *
     * @param addParam 新增租户策略信息
     * @return 新增结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或新增失败时抛出
     * @author shy
     * @since 2026-06-13
     */
    @PostMapping("/add")
    @Operation(summary = "新增租户策略", description = "新增租户策略信息")
    public ApiResponse addPolicy(@Valid @RequestBody SysTenantPolicyAddRTO addParam) {
        Integer affectedRows = iSysTenantPolicyService.addPolicy(addParam);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>修改租户策略</p>
     * <p>修改租户策略信息，仅允许修改策略名称、状态等非关键字段</p>
     *
     * @param updateParam 修改租户策略信息
     * @return 修改结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、策略不存在或修改失败时抛出
     * @author shy
     * @since 2026-06-13
     */
    @PutMapping("/update")
    @Operation(summary = "修改租户策略", description = "修改租户策略信息")
    public ApiResponse updatePolicy(@Valid @RequestBody SysTenantPolicyUpdateRTO updateParam) {
        Integer affectedRows = iSysTenantPolicyService.updatePolicy(updateParam);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>更新租户策略状态</p>
     * <p>更新指定租户策略的状态（激活/禁用）</p>
     *
     * @param policyCode 策略编码
     * @param status 策略状态
     * @return 更新结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、策略不存在或更新失败时抛出
     * @author shy
     * @since 2026-06-13
     */
    @PutMapping("/status")
    @Operation(summary = "更新租户策略状态", description = "更新指定租户策略的状态（激活/禁用）")
    public ApiResponse updatePolicyStatus(@NotBlank(message = "策略编码不能为空") @RequestParam String policyCode,
                                          @NotBlank(message = "状态不能为空") @RequestParam String status) {
        Integer affectedRows = iSysTenantPolicyService.updatePolicyStatus(policyCode, status);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>删除租户策略</p>
     * <p>逻辑删除指定租户策略</p>
     *
     * @param policyCode 策略编码
     * @return 删除结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、策略不存在或删除失败时抛出
     * @author shy
     * @since 2026-06-13
     */
    @DeleteMapping("/delete")
    @Operation(summary = "删除租户策略", description = "删除租户策略信息")
    public ApiResponse deletePolicy(@NotBlank(message = "策略编码不能为空") @RequestParam String policyCode) {
        Integer affectedRows = iSysTenantPolicyService.deletePolicy(policyCode);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>批量新增租户策略</p>
     * <p>批量创建部门/角色与租户的绑定关系</p>
     *
     * @param addParamList 批量新增租户策略信息集合
     * @return 成功新增的策略数量
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或新增失败时抛出
     * @author shy
     * @since 2026-06-13
     */
    @PostMapping("/batch")
    @Operation(summary = "批量新增租户策略", description = "批量新增租户策略信息")
    public ApiResponse batchAddPolicy(@Valid @NotEmpty @RequestBody List<SysTenantPolicyAddRTO> addParamList) {
        Integer affectedRows = iSysTenantPolicyService.batchAddPolicy(addParamList);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>批量修改租户策略</p>
     * <p>批量修改多个租户策略信息</p>
     *
     * @param updateParamList 批量修改租户策略信息集合
     * @return 成功修改的策略数量
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或修改失败时抛出
     * @author shy
     * @since 2026-06-13
     */
    @PutMapping("/batch")
    @Operation(summary = "批量修改租户策略", description = "批量修改租户策略信息")
    public ApiResponse batchUpdatePolicy(@Valid @RequestBody List<SysTenantPolicyUpdateRTO> updateParamList) {
        Integer affectedRows = iSysTenantPolicyService.batchUpdatePolicy(updateParamList);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>批量更新租户策略状态</p>
     * <p>批量更新多个指定租户策略的状态（激活/禁用）</p>
     *
     * @param policyCodeList 策略编码集合
     * @param status 策略状态
     * @return 更新结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或更新失败时抛出
     * @author shy
     * @since 2026-06-13
     */
    @PutMapping("/status/batch")
    @Operation(summary = "批量更新租户策略状态", description = "批量更新多个指定租户策略的状态（激活/禁用）")
    public ApiResponse batchUpdatePolicyStatus(@NotEmpty(message = "策略编码集合不能为空") @RequestBody List<String> policyCodeList,
                                               @NotBlank(message = "状态不能为空") @RequestParam String status) {
        Integer affectedRows = iSysTenantPolicyService.batchUpdatePolicyStatus(policyCodeList, status);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>批量删除租户策略</p>
     * <p>批量逻辑删除多个指定租户策略</p>
     *
     * @param policyCodeList 策略编码集合
     * @return 删除结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或删除失败时抛出
     * @author shy
     * @since 2026-06-13
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除租户策略", description = "批量删除租户策略信息")
    public ApiResponse batchDeletePolicy(@NotEmpty(message = "策略编码集合不能为空") @RequestBody List<String> policyCodeList) {
        Integer affectedRows = iSysTenantPolicyService.batchDeletePolicy(policyCodeList);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>批量绑定部门/角色到租户</p>
     * <p>将多个部门或角色批量绑定到指定租户</p>
     *
     * @param bindParam 批量绑定参数
     * @return 成功绑定的策略数量
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或绑定失败时抛出
     * @author shy
     * @since 2026-06-13
     */
    @PostMapping("/bind")
    @Operation(summary = "批量绑定到租户", description = "将多个部门或角色批量绑定到指定租户")
    public ApiResponse batchBindToTenant(@Valid @RequestBody SysTenantPolicyBatchBindRTO bindParam) {
        Integer affectedRows = iSysTenantPolicyService.batchBindToTenant(bindParam);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>批量解绑部门/角色与租户</p>
     * <p>批量解除部门或角色与租户的绑定关系</p>
     *
     * @param unbindParam 批量解绑参数
     * @return 成功解绑的策略数量
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或解绑失败时抛出
     * @author shy
     * @since 2026-06-13
     */
    @PostMapping("/unbind")
    @Operation(summary = "批量解绑", description = "批量解除部门或角色与租户的绑定关系")
    public ApiResponse batchUnbindFromTenant(@Valid @RequestBody SysTenantPolicyBatchUnbindRTO unbindParam) {
        Integer affectedRows = iSysTenantPolicyService.batchUnbindFromTenant(unbindParam);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>查询租户的所有部门绑定</p>
     * <p>查询指定租户下所有已绑定的部门列表</p>
     *
     * @param tenantId 租户ID
     * @return 租户策略列表
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-06-13
     */
    @GetMapping("/tenant/{tenantId}/depts")
    @Operation(summary = "查询租户的部门绑定", description = "查询指定租户下所有已绑定的部门列表")
    public ApiResponse queryTenantDepts(@NotNull(message = "租户ID不能为空") @PathVariable Long tenantId) {
        List<SysTenantPolicyCommonVO> policyList = iSysTenantPolicyService.queryTenantDepts(tenantId);
        return ApiResponse.success(policyList);
    }

    /**
     * <p>查询租户的所有角色绑定</p>
     * <p>查询指定租户下所有已绑定的角色列表</p>
     *
     * @param tenantId 租户ID
     * @return 租户策略列表
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-06-13
     */
    @GetMapping("/tenant/{tenantId}/roles")
    @Operation(summary = "查询租户的角色绑定", description = "查询指定租户下所有已绑定的角色列表")
    public ApiResponse queryTenantRoles(@NotNull(message = "租户ID不能为空") @PathVariable Long tenantId) {
        List<SysTenantPolicyCommonVO> policyList = iSysTenantPolicyService.queryTenantRoles(tenantId);
        return ApiResponse.success(policyList);
    }

}
