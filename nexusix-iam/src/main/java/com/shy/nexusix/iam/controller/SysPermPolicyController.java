package com.shy.nexusix.iam.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.iam.rto.SysPermPolicyAddRTO;
import com.shy.nexusix.iam.rto.SysPermPolicyBatchBindRTO;
import com.shy.nexusix.iam.rto.SysPermPolicyBatchUnbindRTO;
import com.shy.nexusix.iam.rto.SysPermPolicyQueryRTO;
import com.shy.nexusix.iam.rto.SysPermPolicyUpdateRTO;
import com.shy.nexusix.iam.service.ISysPermPolicyService;
import com.shy.nexusix.iam.vo.SysPermPolicyCommonVO;
import com.shy.nexusix.iam.vo.SysPermPolicyDetailVO;
import com.shy.nexusix.common.exception.BusinessException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>权限策略管理控制器</p>
 *
 * @author shy
 */
@RestController
@RequestMapping("/perm-policy")
@Tag(name = "权限策略管理", description = "权限策略管理相关接口")
@Validated
public class SysPermPolicyController {

    @Autowired
    private ISysPermPolicyService iSysPermPolicyService;

    /**
     * <p>查询权限策略列表</p>
     *
     * @return 权限策略列表
     * @throws BusinessException 无权限或查询失败时抛出
     */
    @GetMapping("/list")
    @Operation(summary = "查询权限策略列表", description = "返回所有权限策略列表")
    public ApiResponse queryPolicyList() {
        List<SysPermPolicyCommonVO> policyList = iSysPermPolicyService.queryPolicyList();
        return ApiResponse.success(policyList);
    }

    /**
     * <p>分页查询权限策略列表</p>
     *
     * @param page 分页参数
     * @return 分页后的权限策略列表
     * @throws BusinessException 无权限时抛出
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询权限策略列表", description = "返回分页后的权限策略列表")
    public ApiResponse queryPolicyPage(@Valid PageCommonRTO page) {
        IPage<SysPermPolicyCommonVO> policyPage = iSysPermPolicyService.queryPolicyPage(page);
        return ApiResponse.success(policyPage);
    }

    /**
     * <p>条件查询权限策略列表</p>
     *
     * @param queryParam 查询条件
     * @return 满足条件的权限策略列表
     * @throws BusinessException 无权限或查询失败时抛出
     */
    @PostMapping("/query")
    @Operation(summary = "条件查询权限策略列表", description = "返回满足条件的权限策略列表")
    public ApiResponse queryPolicy(@Valid @RequestBody SysPermPolicyQueryRTO queryParam) {
        IPage<SysPermPolicyCommonVO> policyPage = iSysPermPolicyService.queryPolicy(queryParam);
        return ApiResponse.success(policyPage);
    }

    /**
     * <p>查询权限策略详情</p>
     *
     * @param policyCode 策略编码
     * @return 权限策略详情信息
     * @throws BusinessException 无权限或查询失败时抛出
     */
    @GetMapping("/detail/{policyCode}")
    @Operation(summary = "查询权限策略详情", description = "返回指定权限策略的详情信息")
    public ApiResponse queryPolicyDetail(@NotBlank(message = "策略编码不能为空") @PathVariable String policyCode) {
        SysPermPolicyDetailVO policyDetail = iSysPermPolicyService.queryPolicyDetail(policyCode);
        return ApiResponse.success(policyDetail);
    }

    /**
     * <p>新增权限策略</p>
     *
     * @param addParam 新增权限策略信息
     * @return 新增结果行数
     * @throws BusinessException 无权限或新增失败时抛出
     */
    @PostMapping("/add")
    @Operation(summary = "新增权限策略", description = "新增权限策略信息")
    public ApiResponse addPolicy(@Valid @RequestBody SysPermPolicyAddRTO addParam) {
        Integer affectedRows = iSysPermPolicyService.addPolicy(addParam);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>修改权限策略</p>
     *
     * @param updateParam 修改权限策略信息
     * @return 修改结果行数
     * @throws BusinessException 无权限、策略不存在或修改失败时抛出
     */
    @PutMapping("/update")
    @Operation(summary = "修改权限策略", description = "修改权限策略信息")
    public ApiResponse updatePolicy(@Valid @RequestBody SysPermPolicyUpdateRTO updateParam) {
        Integer affectedRows = iSysPermPolicyService.updatePolicy(updateParam);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>更新权限策略状态</p>
     *
     * @param policyCode 策略编码
     * @param status 策略状态
     * @return 更新结果行数
     * @throws BusinessException 无权限、策略不存在或更新失败时抛出
     */
    @PutMapping("/status")
    @Operation(summary = "更新权限策略状态", description = "更新指定权限策略的状态（激活/禁用）")
    public ApiResponse updatePolicyStatus(@NotBlank(message = "策略编码不能为空") @RequestParam String policyCode,
                                          @NotBlank(message = "状态不能为空") @RequestParam String status) {
        Integer affectedRows = iSysPermPolicyService.updatePolicyStatus(policyCode, status);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>删除权限策略</p>
     *
     * @param policyCode 策略编码
     * @return 删除结果行数
     * @throws BusinessException 无权限、策略不存在或删除失败时抛出
     */
    @DeleteMapping("/delete")
    @Operation(summary = "删除权限策略", description = "删除权限策略信息")
    public ApiResponse deletePolicy(@NotBlank(message = "策略编码不能为空") @RequestParam String policyCode) {
        Integer affectedRows = iSysPermPolicyService.deletePolicy(policyCode);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>批量新增权限策略</p>
     *
     * @param addParamList 批量新增权限策略信息集合
     * @return 新增结果行数
     * @throws BusinessException 无权限或新增失败时抛出
     */
    @PostMapping("/batch")
    @Operation(summary = "批量新增权限策略", description = "批量新增权限策略信息")
    public ApiResponse batchAddPolicy(@Valid @NotEmpty @RequestBody List<SysPermPolicyAddRTO> addParamList) {
        Integer affectedRows = iSysPermPolicyService.batchAddPolicy(addParamList);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>批量修改权限策略</p>
     *
     * @param updateParamList 批量修改权限策略信息集合
     * @return 修改结果行数
     * @throws BusinessException 无权限或修改失败时抛出
     */
    @PutMapping("/batch")
    @Operation(summary = "批量修改权限策略", description = "批量修改权限策略信息")
    public ApiResponse batchUpdatePolicy(@Valid @RequestBody List<SysPermPolicyUpdateRTO> updateParamList) {
        Integer affectedRows = iSysPermPolicyService.batchUpdatePolicy(updateParamList);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>批量更新权限策略状态</p>
     *
     * @param policyCodeList 策略编码集合
     * @param status 策略状态
     * @return 更新结果行数
     * @throws BusinessException 无权限或更新失败时抛出
     */
    @PutMapping("/status/batch")
    @Operation(summary = "批量更新权限策略状态", description = "批量更新多个指定权限策略的状态（激活/禁用）")
    public ApiResponse batchUpdatePolicyStatus(@NotEmpty(message = "策略编码集合不能为空") @RequestBody List<String> policyCodeList,
                                               @NotBlank(message = "状态不能为空") @RequestParam String status) {
        Integer affectedRows = iSysPermPolicyService.batchUpdatePolicyStatus(policyCodeList, status);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>批量删除权限策略</p>
     *
     * @param policyCodeList 策略编码集合
     * @return 删除结果行数
     * @throws BusinessException 无权限或删除失败时抛出
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除权限策略", description = "批量删除权限策略信息")
    public ApiResponse batchDeletePolicy(@NotEmpty(message = "策略编码集合不能为空") @RequestBody List<String> policyCodeList) {
        Integer affectedRows = iSysPermPolicyService.batchDeletePolicy(policyCodeList);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>批量绑定权限到目标</p>
     *
     * @param bindParam 批量绑定参数
     * @return 成功绑定的策略数量
     * @throws BusinessException 无权限或绑定失败时抛出
     */
    @PostMapping("/bind")
    @Operation(summary = "批量绑定到目标", description = "将多个权限批量绑定到指定目标")
    public ApiResponse batchBindToTarget(@Valid @RequestBody SysPermPolicyBatchBindRTO bindParam) {
        Integer affectedRows = iSysPermPolicyService.batchBindToTarget(bindParam);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>批量解绑权限与目标</p>
     *
     * @param unbindParam 批量解绑参数
     * @return 成功解绑的策略数量
     * @throws BusinessException 无权限或解绑失败时抛出
     */
    @PostMapping("/unbind")
    @Operation(summary = "批量解绑", description = "批量解除权限与目标的绑定关系")
    public ApiResponse batchUnbindFromTarget(@Valid @RequestBody SysPermPolicyBatchUnbindRTO unbindParam) {
        Integer affectedRows = iSysPermPolicyService.batchUnbindFromTarget(unbindParam);
        return ApiResponse.success(affectedRows);
    }

}
