package com.shy.nexusix.iam.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.iam.rto.SysPermissionPolicyAddRTO;
import com.shy.nexusix.iam.rto.SysPermissionPolicyQueryRTO;
import com.shy.nexusix.iam.rto.SysPermissionPolicyUpdateRTO;
import com.shy.nexusix.iam.rto.SysRolePermissionAssignRTO;
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
 * 权限策略控制表 - 控制器
 * </p>
 * <p>
 * 提供四层权限模型的策略管理、角色权限分配、用户权限查询与校验等接口。
 * </p>
 *
 * @author shy
 * @since 2026-05-05
 */
@RestController
@RequestMapping("/sys-permission-policy")
@Tag(name = "权限策略管理", description = "权限策略控制与分配接口")
@Validated
public class SysPermissionPolicyController {

    @Autowired
    private ISysPermissionPolicyService iSysPermissionPolicyService;

    /**
     * <p>
     * 查询权限策略列表
     * </p>
     *
     * @return 权限策略公共视图对象列表
     * @author shy
     * @since 2026-05-05
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
     *
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @return 分页后的权限策略公共视图对象
     * @author shy
     * @since 2026-05-05
     */
    @PostMapping("/page")
    @Operation(summary = "分页查询权限策略列表", description = "返回分页后的权限策略列表")
    public ApiResponse queryPolicyPage(@Valid @RequestBody PageCommonRTO page) {
        IPage<SysPermissionPolicyCommonVO> policyPage = iSysPermissionPolicyService.queryPolicyPage(page);
        return ApiResponse.success(policyPage);
    }

    /**
     * <p>
     * 条件查询权限策略列表
     * </p>
     * <p>
     * 支持按目标类型、目标ID、权限ID、动作筛选
     * </p>
     *
     * @param queryParam 查询条件
     * @return 分页后的权限策略公共视图对象
     * @author shy
     * @since 2026-05-05
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
     *
     * @param id 策略ID
     * @return 权限策略详情视图对象
     * @throws com.shy.nexusix.common.exception.BusinessException 当策略不存在时抛出
     * @author shy
     * @since 2026-05-05
     */
    @GetMapping("/detail/{id}")
    @Operation(summary = "查询权限策略详情", description = "返回指定权限策略的详情信息")
    public ApiResponse queryPolicyDetail(@NotBlank(message = "权限策略Id不能为空") @PathVariable String id) {
        SysPermissionPolicyDetailVO detail = iSysPermissionPolicyService.queryPolicyDetail(id);
        return ApiResponse.success(detail);
    }

    /**
     * <p>
     * 新增权限策略
     * </p>
     * <p>
     * 校验目标角色和关联权限的有效性
     * </p>
     *
     * @param addParam 新增参数
     * @return 影响行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当目标角色或关联权限不存在时抛出
     * @author shy
     * @since 2026-05-05
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
     *
     * @param updateParam 修改参数
     * @return 影响行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当策略不存在或关联数据无效时抛出
     * @author shy
     * @since 2026-05-05
     */
    @PutMapping("/update")
    @Operation(summary = "修改权限策略", description = "修改权限策略信息")
    public ApiResponse updatePolicy(@Valid @RequestBody SysPermissionPolicyUpdateRTO updateParam) {
        Integer affectedRows = iSysPermissionPolicyService.updatePolicy(updateParam);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>
     * 删除权限策略
     * </p>
     * <p>
     * 逻辑删除
     * </p>
     *
     * @param id 策略ID
     * @return 影响行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当策略不存在时抛出
     * @author shy
     * @since 2026-05-05
     */
    @DeleteMapping("/delete")
    @Operation(summary = "删除权限策略", description = "删除权限策略信息")
    public ApiResponse deletePolicy(@NotBlank(message = "权限策略Id不能为空") @RequestParam String id) {
        Integer affectedRows = iSysPermissionPolicyService.deletePolicy(id);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>
     * 批量新增权限策略
     * </p>
     * <p>
     * 单次上限100条
     * </p>
     *
     * @param addParamList 新增参数列表
     * @return 影响行数
     * @author shy
     * @since 2026-05-05
     */
    @PostMapping("/batch")
    @Operation(summary = "批量新增权限策略", description = "批量新增权限策略信息")
    public ApiResponse batchAddPolicy(@Valid @RequestBody List<SysPermissionPolicyAddRTO> addParamList) {
        Integer affectedRows = iSysPermissionPolicyService.batchAddPolicy(addParamList);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>
     * 批量删除权限策略
     * </p>
     * <p>
     * 单次上限100条，逻辑删除
     * </p>
     *
     * @param ids 策略ID列表
     * @return 影响行数
     * @author shy
     * @since 2026-05-05
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除权限策略", description = "批量删除权限策略信息")
    public ApiResponse batchDeletePolicy(@NotEmpty(message = "权限策略Id列表不能为空") @RequestBody List<String> ids) {
        Integer affectedRows = iSysPermissionPolicyService.batchDeletePolicy(ids);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>
     * 分配角色权限
     * </p>
     * <p>
     * 采用先清后写模式：先删除该角色的所有已有策略，再批量新增新策略。
     * 所有策略动作默认为允许(ALLOW)，优先级从100递增，默认开启继承。
     * </p>
     *
     * @param assignParam 角色权限分配参数
     * @return 新增策略数量
     * @throws com.shy.nexusix.common.exception.BusinessException 当角色不存在时抛出
     * @author shy
     * @since 2026-05-05
     */
    @PostMapping("/assign/role")
    @Operation(summary = "分配角色权限", description = "为指定角色分配权限，先清后写")
    public ApiResponse assignRolePermission(@Valid @RequestBody SysRolePermissionAssignRTO assignParam) {
        Integer affectedRows = iSysPermissionPolicyService.assignRolePermission(assignParam);
        return ApiResponse.success(affectedRows);
    }

}
