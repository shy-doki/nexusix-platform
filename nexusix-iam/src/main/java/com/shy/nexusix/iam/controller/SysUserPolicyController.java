package com.shy.nexusix.iam.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.iam.rto.SysUserPolicyAddRTO;
import com.shy.nexusix.iam.rto.SysUserPolicyBatchBindRTO;
import com.shy.nexusix.iam.rto.SysUserPolicyBatchUnbindRTO;
import com.shy.nexusix.iam.rto.SysUserPolicyQueryRTO;
import com.shy.nexusix.iam.rto.SysUserPolicyUpdateRTO;
import com.shy.nexusix.iam.service.ISysUserPolicyService;
import com.shy.nexusix.iam.vo.SysUserPolicyCommonVO;
import com.shy.nexusix.iam.vo.SysUserPolicyDetailVO;
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
 * <p>用户策略管理控制器</p>
 *
 * @author shy
 */
@RestController
@RequestMapping("/user-policy")
@Tag(name = "用户策略管理", description = "用户策略管理相关接口")
@Validated
public class SysUserPolicyController {

    @Autowired
    private ISysUserPolicyService iSysUserPolicyService;

    /**
     * <p>查询用户策略列表</p>
     *
     * @return 用户策略列表
     * @throws BusinessException 无权限或查询失败时抛出
     */
    @GetMapping("/list")
    @Operation(summary = "查询用户策略列表", description = "返回所有用户策略列表")
    public ApiResponse queryPolicyList() {
        List<SysUserPolicyCommonVO> policyList = iSysUserPolicyService.queryPolicyList();
        return ApiResponse.success(policyList);
    }

    /**
     * <p>分页查询用户策略列表</p>
     *
     * @param page 分页参数
     * @return 分页后的用户策略列表
     * @throws BusinessException 无权限时抛出
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询用户策略列表", description = "返回分页后的用户策略列表")
    public ApiResponse queryPolicyPage(@Valid PageCommonRTO page) {
        IPage<SysUserPolicyCommonVO> policyPage = iSysUserPolicyService.queryPolicyPage(page);
        return ApiResponse.success(policyPage);
    }

    /**
     * <p>条件查询用户策略列表</p>
     *
     * @param queryParam 查询条件
     * @return 满足条件的用户策略列表
     * @throws BusinessException 无权限或查询失败时抛出
     */
    @PostMapping("/query")
    @Operation(summary = "条件查询用户策略列表", description = "返回满足条件的用户策略列表")
    public ApiResponse queryPolicy(@Valid @RequestBody SysUserPolicyQueryRTO queryParam) {
        IPage<SysUserPolicyCommonVO> policyPage = iSysUserPolicyService.queryPolicy(queryParam);
        return ApiResponse.success(policyPage);
    }

    /**
     * <p>查询用户策略详情</p>
     *
     * @param policyCode 策略编码
     * @return 用户策略详情信息
     * @throws BusinessException 无权限或查询失败时抛出
     */
    @GetMapping("/detail/{policyCode}")
    @Operation(summary = "查询用户策略详情", description = "返回指定用户策略的详情信息")
    public ApiResponse queryPolicyDetail(@NotBlank(message = "策略编码不能为空") @PathVariable String policyCode) {
        SysUserPolicyDetailVO policyDetail = iSysUserPolicyService.queryPolicyDetail(policyCode);
        return ApiResponse.success(policyDetail);
    }

    /**
     * <p>新增用户策略</p>
     *
     * @param addParam 新增用户策略信息
     * @return 新增结果行数
     * @throws BusinessException 无权限或新增失败时抛出
     */
    @PostMapping("/add")
    @Operation(summary = "新增用户策略", description = "新增用户策略信息")
    public ApiResponse addPolicy(@Valid @RequestBody SysUserPolicyAddRTO addParam) {
        Integer affectedRows = iSysUserPolicyService.addPolicy(addParam);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>修改用户策略</p>
     *
     * @param updateParam 修改用户策略信息
     * @return 修改结果行数
     * @throws BusinessException 无权限、策略不存在或修改失败时抛出
     */
    @PutMapping("/update")
    @Operation(summary = "修改用户策略", description = "修改用户策略信息")
    public ApiResponse updatePolicy(@Valid @RequestBody SysUserPolicyUpdateRTO updateParam) {
        Integer affectedRows = iSysUserPolicyService.updatePolicy(updateParam);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>更新用户策略状态</p>
     *
     * @param policyCode 策略编码
     * @param status 策略状态
     * @return 更新结果行数
     * @throws BusinessException 无权限、策略不存在或更新失败时抛出
     */
    @PutMapping("/status")
    @Operation(summary = "更新用户策略状态", description = "更新指定用户策略的状态（激活/禁用）")
    public ApiResponse updatePolicyStatus(@NotBlank(message = "策略编码不能为空") @RequestParam String policyCode,
                                          @NotBlank(message = "状态不能为空") @RequestParam String status) {
        Integer affectedRows = iSysUserPolicyService.updatePolicyStatus(policyCode, status);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>删除用户策略</p>
     *
     * @param policyCode 策略编码
     * @return 删除结果行数
     * @throws BusinessException 无权限、策略不存在或删除失败时抛出
     */
    @DeleteMapping("/delete")
    @Operation(summary = "删除用户策略", description = "删除用户策略信息")
    public ApiResponse deletePolicy(@NotBlank(message = "策略编码不能为空") @RequestParam String policyCode) {
        Integer affectedRows = iSysUserPolicyService.deletePolicy(policyCode);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>批量新增用户策略</p>
     *
     * @param addParamList 批量新增用户策略信息集合
     * @return 新增结果行数
     * @throws BusinessException 无权限或新增失败时抛出
     */
    @PostMapping("/batch")
    @Operation(summary = "批量新增用户策略", description = "批量新增用户策略信息")
    public ApiResponse batchAddPolicy(@Valid @NotEmpty @RequestBody List<SysUserPolicyAddRTO> addParamList) {
        Integer affectedRows = iSysUserPolicyService.batchAddPolicy(addParamList);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>批量修改用户策略</p>
     *
     * @param updateParamList 批量修改用户策略信息集合
     * @return 修改结果行数
     * @throws BusinessException 无权限或修改失败时抛出
     */
    @PutMapping("/batch")
    @Operation(summary = "批量修改用户策略", description = "批量修改用户策略信息")
    public ApiResponse batchUpdatePolicy(@Valid @RequestBody List<SysUserPolicyUpdateRTO> updateParamList) {
        Integer affectedRows = iSysUserPolicyService.batchUpdatePolicy(updateParamList);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>批量更新用户策略状态</p>
     *
     * @param policyCodeList 策略编码集合
     * @param status 策略状态
     * @return 更新结果行数
     * @throws BusinessException 无权限或更新失败时抛出
     */
    @PutMapping("/status/batch")
    @Operation(summary = "批量更新用户策略状态", description = "批量更新多个指定用户策略的状态（激活/禁用）")
    public ApiResponse batchUpdatePolicyStatus(@NotEmpty(message = "策略编码集合不能为空") @RequestBody List<String> policyCodeList,
                                               @NotBlank(message = "状态不能为空") @RequestParam String status) {
        Integer affectedRows = iSysUserPolicyService.batchUpdatePolicyStatus(policyCodeList, status);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>批量删除用户策略</p>
     *
     * @param policyCodeList 策略编码集合
     * @return 删除结果行数
     * @throws BusinessException 无权限或删除失败时抛出
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除用户策略", description = "批量删除用户策略信息")
    public ApiResponse batchDeletePolicy(@NotEmpty(message = "策略编码集合不能为空") @RequestBody List<String> policyCodeList) {
        Integer affectedRows = iSysUserPolicyService.batchDeletePolicy(policyCodeList);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>批量绑定用户到目标</p>
     *
     * @param bindParam 批量绑定参数
     * @return 成功绑定的策略数量
     * @throws BusinessException 无权限或绑定失败时抛出
     */
    @PostMapping("/bind")
    @Operation(summary = "批量绑定到目标", description = "将多个用户批量绑定到指定目标")
    public ApiResponse batchBindToTarget(@Valid @RequestBody SysUserPolicyBatchBindRTO bindParam) {
        Integer affectedRows = iSysUserPolicyService.batchBindToTarget(bindParam);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>批量解绑用户与目标</p>
     *
     * @param unbindParam 批量解绑参数
     * @return 成功解绑的策略数量
     * @throws BusinessException 无权限或解绑失败时抛出
     */
    @PostMapping("/unbind")
    @Operation(summary = "批量解绑", description = "批量解除用户与目标的绑定关系")
    public ApiResponse batchUnbindFromTarget(@Valid @RequestBody SysUserPolicyBatchUnbindRTO unbindParam) {
        Integer affectedRows = iSysUserPolicyService.batchUnbindFromTarget(unbindParam);
        return ApiResponse.success(affectedRows);
    }

}
