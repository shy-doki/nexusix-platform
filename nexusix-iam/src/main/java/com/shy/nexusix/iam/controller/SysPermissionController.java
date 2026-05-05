package com.shy.nexusix.iam.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.iam.rto.SysPermissionAddRTO;
import com.shy.nexusix.iam.rto.SysPermissionQueryRTO;
import com.shy.nexusix.iam.rto.SysPermissionUpdateRTO;
import com.shy.nexusix.iam.service.ISysPermissionService;
import com.shy.nexusix.iam.vo.SysPermissionCommonVO;
import com.shy.nexusix.iam.vo.SysPermissionDetailVO;
import com.shy.nexusix.iam.vo.SysPermissionTreeVO;
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
 * 权限资源表 - 控制器
 * </p>
 * <p>
 * 提供权限资源的CRUD、树形结构、批量操作、状态切换等管理接口。
 * </p>
 *
 * @author shy
 * @since 2026-05-05
 */
@RestController
@RequestMapping("/sys-permission")
@Tag(name = "权限管理", description = "权限资源定义与管理接口")
@Validated
public class SysPermissionController {

    @Autowired
    private ISysPermissionService iSysPermissionService;

    /**
     * <p>
     * 查询权限列表
     * </p>
     * <p>
     * 返回所有权限的平铺列表，按权限类型和ID升序排列。
     * </p>
     *
     * @return 权限公共视图对象列表
     * @author shy
     * @since 2026-05-05
     */
    @GetMapping("/list")
    @Operation(summary = "查询权限列表", description = "返回所有权限列表")
    public ApiResponse queryPermissionList() {
        List<SysPermissionCommonVO> permissionList = iSysPermissionService.queryPermissionList();
        return ApiResponse.success(permissionList);
    }

    /**
     * <p>
     * 分页查询权限列表
     * </p>
     *
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @return 分页后的权限公共视图对象
     * @author shy
     * @since 2026-05-05
     */
    @PostMapping("/page")
    @Operation(summary = "分页查询权限列表", description = "返回分页后的权限列表")
    public ApiResponse queryPermissionPage(@Valid @RequestBody PageCommonRTO page) {
        IPage<SysPermissionCommonVO> permissionPage = iSysPermissionService.queryPermissionPage(page);
        return ApiResponse.success(permissionPage);
    }

    /**
     * <p>
     * 查询权限树形结构列表
     * </p>
     * <p>
     * 返回按parentId层级关系构建的树形结构
     * </p>
     *
     * @return 权限树形视图对象列表
     * @author shy
     * @since 2026-05-05
     */
    @GetMapping("/tree/list")
    @Operation(summary = "查询权限树形结构列表", description = "返回所有权限的层级树形结构")
    public ApiResponse queryPermissionTreeList() {
        List<SysPermissionTreeVO> treeList = iSysPermissionService.queryPermissionTreeList();
        return ApiResponse.success(treeList);
    }

    /**
     * <p>
     * 条件查询权限列表
     * </p>
     * <p>
     * 支持按权限名称、标识模糊匹配，按类型和状态精确筛选
     * </p>
     *
     * @param queryParam 查询条件
     * @return 分页后的权限公共视图对象
     * @author shy
     * @since 2026-05-05
     */
    @PostMapping("/query")
    @Operation(summary = "条件查询权限列表", description = "返回满足条件的权限列表")
    public ApiResponse queryPermission(@Valid @RequestBody SysPermissionQueryRTO queryParam) {
        IPage<SysPermissionCommonVO> permissionPage = iSysPermissionService.queryPermission(queryParam);
        return ApiResponse.success(permissionPage);
    }

    /**
     * <p>
     * 查询权限详情
     * </p>
     *
     * @param id 权限ID
     * @return 权限详情视图对象
     * @throws com.shy.nexusix.common.exception.BusinessException 当权限不存在时抛出
     * @author shy
     * @since 2026-05-05
     */
    @GetMapping("/detail/{id}")
    @Operation(summary = "查询权限详情", description = "返回指定权限的详情信息")
    public ApiResponse queryPermissionDetail(@NotBlank(message = "权限Id不能为空") @PathVariable String id) {
        SysPermissionDetailVO detail = iSysPermissionService.queryPermissionDetail(id);
        return ApiResponse.success(detail);
    }

    /**
     * <p>
     * 新增权限
     * </p>
     * <p>
     * 校验权限标识唯一性，parentId为空时默认设为0（顶级权限）
     * </p>
     *
     * @param addParam 新增参数
     * @return 影响行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当权限标识已存在时抛出
     * @author shy
     * @since 2026-05-05
     */
    @PostMapping("/add")
    @Operation(summary = "新增权限", description = "新增权限信息")
    public ApiResponse addPermission(@Valid @RequestBody SysPermissionAddRTO addParam) {
        Integer affectedRows = iSysPermissionService.addPermission(addParam);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>
     * 修改权限
     * </p>
     * <p>
     * 校验权限存在性和标识唯一性（排除自身）
     * </p>
     *
     * @param updateParam 修改参数
     * @return 影响行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当权限不存在或标识已被占用时抛出
     * @author shy
     * @since 2026-05-05
     */
    @PutMapping("/update")
    @Operation(summary = "修改权限", description = "修改权限信息")
    public ApiResponse updatePermission(@Valid @RequestBody SysPermissionUpdateRTO updateParam) {
        Integer affectedRows = iSysPermissionService.updatePermission(updateParam);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>
     * 更新权限状态
     * </p>
     * <p>
     * 更新指定权限的状态（启用/禁用），禁用后该权限对所有人失效
     * </p>
     *
     * @param id     权限ID
     * @param status 状态值（1-启用 0-禁用）
     * @return 影响行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当权限不存在或状态未变更时抛出
     * @author shy
     * @since 2026-05-05
     */
    @PutMapping("/status")
    @Operation(summary = "更新权限状态", description = "更新指定权限的状态（启用/禁用）")
    public ApiResponse updatePermissionStatus(@NotBlank(message = "权限Id不能为空") @RequestParam String id, @NotBlank(message = "状态不能为空") @RequestParam String status) {
        Integer affectedRows = iSysPermissionService.updatePermissionStatus(id, status);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>
     * 删除权限
     * </p>
     * <p>
     * 逻辑删除，删除前会校验是否存在子权限
     * </p>
     *
     * @param id 权限ID
     * @return 影响行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当权限不存在或存在子权限时抛出
     * @author shy
     * @since 2026-05-05
     */
    @DeleteMapping("/delete")
    @Operation(summary = "删除权限", description = "删除权限信息")
    public ApiResponse deletePermission(@NotBlank(message = "权限Id不能为空") @RequestParam String id) {
        Integer affectedRows = iSysPermissionService.deletePermission(id);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>
     * 批量新增权限
     * </p>
     * <p>
     * 单次上限100条，校验批量内部去重和数据库唯一性
     * </p>
     *
     * @param addParamList 新增参数列表
     * @return 影响行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当存在重复标识或超过上限时抛出
     * @author shy
     * @since 2026-05-05
     */
    @PostMapping("/batch")
    @Operation(summary = "批量新增权限", description = "批量新增权限信息")
    public ApiResponse batchAddPermission(@Valid @RequestBody List<SysPermissionAddRTO> addParamList) {
        Integer affectedRows = iSysPermissionService.batchAddPermission(addParamList);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>
     * 批量修改权限
     * </p>
     * <p>
     * 单次上限100条，校验ID存在性和标识唯一性
     * </p>
     *
     * @param updateParamList 修改参数列表
     * @return 影响行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当部分权限不存在或标识冲突时抛出
     * @author shy
     * @since 2026-05-05
     */
    @PutMapping("/batch")
    @Operation(summary = "批量修改权限", description = "批量修改权限信息")
    public ApiResponse batchUpdatePermission(@Valid @RequestBody List<SysPermissionUpdateRTO> updateParamList) {
        Integer affectedRows = iSysPermissionService.batchUpdatePermission(updateParamList);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>
     * 批量删除权限
     * </p>
     * <p>
     * 单次上限100条，逻辑删除，删除前校验是否存在子权限
     * </p>
     *
     * @param ids 权限ID列表
     * @return 影响行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当存在子权限或超过上限时抛出
     * @author shy
     * @since 2026-05-05
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除权限", description = "批量删除权限信息")
    public ApiResponse batchDeletePermission(@NotEmpty(message = "权限Id列表不能为空") @RequestBody List<String> ids) {
        Integer affectedRows = iSysPermissionService.batchDeletePermission(ids);
        return ApiResponse.success(affectedRows);
    }

}
