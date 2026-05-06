package com.shy.nexusix.iam.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.iam.rto.SysPermissionPolicyAddRTO;
import com.shy.nexusix.iam.rto.SysPermissionPolicyQueryRTO;
import com.shy.nexusix.iam.rto.SysPermissionPolicyUpdateRTO;
import com.shy.nexusix.iam.service.ISysPermissionPolicyService;
import com.shy.nexusix.iam.vo.SysPermissionPolicyCommonVO;
import com.shy.nexusix.iam.vo.SysPermissionPolicyDetailVO;
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
 * <p>
 * 权限策略控制表 - 实现四层权限及禁用继承逻辑 前端控制器
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@RestController
@RequestMapping("/permission-policy")
@Tag(name = "权限策略管理", description = "权限策略控制相关接口")
@Validated
public class SysPermissionPolicyController {

    @Autowired
    private ISysPermissionPolicyService iSysPermissionPolicyService;

    /**
     * <p>
     * 查询权限策略列表
     * </p>
     * <p>
     * 返回所有权限策略的平铺列表。
     * 需要登录并具备权限策略查看权限才能访问。
     * </p>
     *
     * @return 权限策略列表
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @GetMapping("/list")
    @Operation(summary = "查询权限策略列表", description = "返回所有权限策略列表")
    public ApiResponse queryPolicyList() {
        List<SysPermissionPolicyCommonVO> policyList = iSysPermissionPolicyService.queryPolicyList();
        return ApiResponse.success(policyList);
    }

    /**
     * <p>
     * 分页查询权限策略列表
     * </p>
     * <p>
     * 返回分页后的权限策略列表。
     * 需要登录并具备权限策略查看权限才能访问。
     * </p>
     *
     * @param page 分页参数
     * @return 分页后的权限策略列表
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限
     * @author shy
     * @since 2026-05-06
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询权限策略列表", description = "返回分页后的权限策略列表")
    public ApiResponse queryPolicyPage(@Valid PageCommonRTO page) {
        IPage<SysPermissionPolicyCommonVO> policyPage = iSysPermissionPolicyService.queryPolicyPage(page);
        return ApiResponse.success(policyPage);
    }

    /**
     * <p>
     * 条件查询\筛选权限策略列表
     * </p>
     * <p>
     * 返回满足条件的权限策略列表。
     * 需要登录并具备权限策略条件查询权限才能访问。
     * </p>
     *
     * @param queryParam 查询条件
     * @return 满足条件的权限策略列表
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @PostMapping("/query")
    @Operation(summary = "条件查询权限策略列表", description = "返回满足条件的权限策略列表")
    public ApiResponse queryPolicy(@Valid @RequestBody SysPermissionPolicyQueryRTO queryParam) {
        IPage<SysPermissionPolicyCommonVO> policyPage = iSysPermissionPolicyService.queryPolicy(queryParam);
        return ApiResponse.success(policyPage);
    }

    /**
     * <p>
     * 查询权限策略详情
     * </p>
     * <p>
     * 返回指定权限策略的详情信息。
     * 需要登录并具备权限策略详情查询权限才能访问。
     * </p>
     *
     * @param id 策略ID
     * @return 权限策略详情信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @GetMapping("/detail/{id}")
    @Operation(summary = "查询权限策略详情", description = "返回指定权限策略的详情信息")
    public ApiResponse queryPolicyDetail(@NotBlank(message = "策略ID不能为空") @PathVariable String id) {
        SysPermissionPolicyDetailVO policyDetail = iSysPermissionPolicyService.queryPolicyDetail(id);
        return ApiResponse.success(policyDetail);
    }

    /**
     * <p>
     * 新增权限策略
     * </p>
     * <p>
     * 新增权限策略信息，需要登录并具备权限策略新增权限才能访问。
     * </p>
     *
     * @param addParam 新增权限策略信息
     * @return 新增影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或新增失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @PostMapping("/add")
    @Operation(summary = "新增权限策略", description = "新增权限策略信息")
    public ApiResponse addPolicy(@Valid @RequestBody SysPermissionPolicyAddRTO addParam) {
        Integer affectedRows = iSysPermissionPolicyService.addPolicy(addParam);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>
     * 修改权限策略
     * </p>
     * <p>
     * 修改权限策略信息，需要登录并具备权限策略修改权限才能访问。
     * </p>
     *
     * @param updateParam 修改权限策略信息
     * @return 修改影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、策略不存在或修改失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @PutMapping("/update")
    @Operation(summary = "修改权限策略", description = "修改权限策略信息")
    public ApiResponse updatePolicy(@Valid @RequestBody SysPermissionPolicyUpdateRTO updateParam) {
        Integer affectedRows = iSysPermissionPolicyService.updatePolicy(updateParam);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>
     * 更新权限策略动作
     * </p>
     * <p>
     * 更新指定权限策略的动作（允许/拒绝）。
     * 需要登录并具备权限策略修改权限才能访问。
     * </p>
     *
     * @param id 策略ID
     * @param action 动作（允许/拒绝）
     * @return 更新结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、策略不存在或更新失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @PutMapping("/action")
    @Operation(summary = "更新权限策略动作", description = "更新指定权限策略的动作（允许/拒绝）")
    public ApiResponse updatePolicyAction(@NotBlank(message = "Id不能为空") @RequestParam String id,
                                           @NotBlank(message = "动作不能为空") @RequestParam String action) {
        Integer affectedRows = iSysPermissionPolicyService.updatePolicyAction(id, action);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>
     * 删除权限策略
     * </p>
     * <p>
     * 删除指定权限策略信息，需要登录并具备权限策略删除权限才能访问。
     * </p>
     *
     * @param id 策略ID
     * @return 删除影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、策略不存在或删除失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @DeleteMapping("/delete")
    @Operation(summary = "删除权限策略", description = "删除权限策略信息")
    public ApiResponse deletePolicy(@NotBlank(message = "Id不能为空") @RequestParam String id) {
        Integer affectedRows = iSysPermissionPolicyService.deletePolicy(id);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>
     * 批量新增权限策略
     * </p>
     * <p>
     * 批量新增多个权限策略信息，需要登录并具备权限策略新增权限才能访问。
     * 批量操作支持事务回滚，任一策略新增失败则全部失败。
     * </p>
     *
     * @param addParamList 批量新增权限策略信息集合
     * @return 新增影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、参数校验失败或新增失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @PostMapping("/batch")
    @Operation(summary = "批量新增权限策略", description = "批量新增权限策略信息")
    public ApiResponse batchAddPolicy(@Valid @NotEmpty @RequestBody List<SysPermissionPolicyAddRTO> addParamList) {
        Integer affectedRows = iSysPermissionPolicyService.batchAddPolicy(addParamList);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>
     * 批量修改权限策略
     * </p>
     * <p>
     * 批量修改多个权限策略信息，需要登录并具备权限策略修改权限才能访问。
     * </p>
     *
     * @param updateParamList 批量修改权限策略信息集合
     * @return 修改影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、策略不存在或修改失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @PutMapping("/batch")
    @Operation(summary = "批量修改权限策略", description = "批量修改权限策略信息")
    public ApiResponse batchUpdatePolicy(@Valid @RequestBody List<SysPermissionPolicyUpdateRTO> updateParamList) {
        Integer affectedRows = iSysPermissionPolicyService.batchUpdatePolicy(updateParamList);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>
     * 批量更新权限策略动作
     * </p>
     * <p>
     * 批量更新多个指定权限策略的动作（允许/拒绝）。
     * 批量操作支持事务回滚，任一策略更新失败则全部失败。
     * 需要登录并具备权限策略修改权限才能访问。
     * </p>
     *
     * @param ids 策略ID集合
     * @param action 动作（允许/拒绝）
     * @return 更新结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、策略不存在或更新失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @PutMapping("/action/batch")
    @Operation(summary = "批量更新权限策略动作", description = "批量更新多个指定权限策略的动作（允许/拒绝）")
    public ApiResponse batchUpdatePolicyAction(@NotEmpty(message = "策略ID集合不能为空") @RequestBody List<String> ids,
                                                @NotBlank(message = "动作不能为空") @RequestParam String action) {
        Integer affectedRows = iSysPermissionPolicyService.batchUpdatePolicyAction(ids, action);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>
     * 批量删除权限策略
     * </p>
     * <p>
     * 批量删除多个指定权限策略信息，需要登录并具备权限策略删除权限才能访问。
     * </p>
     *
     * @param ids 策略ID集合
     * @return 删除影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、策略不存在或删除失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除权限策略", description = "批量删除权限策略信息")
    public ApiResponse batchDeletePolicy(@NotEmpty(message = "策略ID集合不能为空") @RequestBody List<String> ids) {
        Integer affectedRows = iSysPermissionPolicyService.batchDeletePolicy(ids);
        return ApiResponse.success(affectedRows);
    }

}
