package com.shy.nexusix.iam.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.iam.rto.SysPermissionAddRTO;
import com.shy.nexusix.iam.rto.SysPermissionQueryRTO;
import com.shy.nexusix.iam.rto.SysPermissionUpdateRTO;
import com.shy.nexusix.iam.service.ISysPermissionService;
import com.shy.nexusix.iam.vo.SysPermissionCommonVO;
import com.shy.nexusix.iam.vo.SysPermissionDetailVO;
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
 * 权限/资源表 - 定义系统所有可授权资源 前端控制器
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@RestController
@RequestMapping("/permission")
@Tag(name = "权限管理", description = "权限资源管理相关接口")
@Validated
public class SysPermissionController {

    @Autowired
    private ISysPermissionService iSysPermissionService;

    /**
     * <p>
     * 查询权限列表
     * </p>
     * <p>
     * 返回所有权限的平铺列表。
     * 需要登录并具备权限查看权限才能访问。
     * </p>
     *
     * @return 权限列表，包含权限名称、权限标识、类型、状态等信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-05-06
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
     * <p>
     * 返回分页后的权限列表。
     * 需要登录并具备权限查看权限才能访问。
     * </p>
     *
     * @param page 分页参数
     * @return 分页后的权限列表，包含权限名称、权限标识、类型、状态等信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限
     * @author shy
     * @since 2026-05-06
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询权限列表", description = "返回分页后的权限列表")
    public ApiResponse queryPermissionPage(@Valid PageCommonRTO page) {
        IPage<SysPermissionCommonVO> permissionPage = iSysPermissionService.queryPermissionPage(page);
        return ApiResponse.success(permissionPage);
    }

    /**
     * <p>
     * 条件查询\筛选权限列表
     * </p>
     * <p>
     * 返回满足条件的权限列表。
     * 需要登录并具备权限条件查询权限才能访问。
     * </p>
     *
     * @param queryParam 查询条件
     * @return 满足条件的权限列表，包含权限名称、权限标识、类型、状态等信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-05-06
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
     * <p>
     * 返回指定权限的详情信息。
     * 需要登录并具备权限详情查询权限才能访问。
     * </p>
     *
     * @param permCode 权限标识
     * @return 权限详情信息，包含权限名称、权限标识、类型、状态等信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @GetMapping("/detail/{permCode}")
    @Operation(summary = "查询权限详情", description = "返回指定权限的详情信息")
    public ApiResponse queryPermissionDetail(@NotBlank(message = "权限标识不能为空") @PathVariable String permCode) {
        SysPermissionDetailVO permissionDetail = iSysPermissionService.queryPermissionDetail(permCode);
        return ApiResponse.success(permissionDetail);
    }

    /**
     * <p>
     * 新增权限
     * </p>
     * <p>
     * 新增权限信息，需要登录并具备权限新增权限才能访问。
     * </p>
     *
     * @param addParam 新增权限信息
     * @return 新增影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或新增失败时抛出
     * @author shy
     * @since 2026-05-06
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
     * 修改权限信息，需要登录并具备权限修改权限才能访问。
     * 仅允许修改指定权限的有效配置信息，不允许修改权限唯一标识。
     * </p>
     *
     * @param updateParam 修改权限信息
     * @return 修改影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、权限不存在或修改失败时抛出
     * @author shy
     * @since 2026-05-06
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
     * 更新指定权限的状态（启用/禁用），禁用后该权限将不可被分配。
     * 需要登录并具备权限修改权限才能访问。
     * </p>
     *
     * @param id 权限ID
     * @param status 权限状态（启用/禁用）
     * @return 更新结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、权限不存在或更新失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @PutMapping("/status")
    @Operation(summary = "更新权限状态", description = "更新指定权限的状态（启用/禁用）")
    public ApiResponse updatePermissionStatus(@NotBlank(message = "Id不能为空") @RequestParam String id,
                                               @NotBlank(message = "状态不能为空") @RequestParam String status) {
        Integer affectedRows = iSysPermissionService.updatePermissionStatus(id, status);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>
     * 删除权限
     * </p>
     * <p>
     * 删除指定权限信息，需要登录并具备权限删除权限才能访问。
     * 删除操作不可逆，删除后权限相关数据将同步清理。
     * </p>
     *
     * @param id 权限ID
     * @return 删除影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、权限不存在或删除失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @DeleteMapping("/delete")
    @Operation(summary = "删除权限", description = "删除权限信息")
    public ApiResponse deletePermission(@NotBlank(message = "Id不能为空") @RequestParam String id) {
        Integer affectedRows = iSysPermissionService.deletePermission(id);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>
     * 批量新增权限
     * </p>
     * <p>
     * 批量新增多个权限信息，需要登录并具备权限新增权限才能访问。
     * 批量操作支持事务回滚，任一权限新增失败则全部失败。
     * </p>
     *
     * @param addParamList 批量新增权限信息集合
     * @return 新增影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、参数校验失败或新增失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @PostMapping("/batch")
    @Operation(summary = "批量新增权限", description = "批量新增权限信息")
    public ApiResponse batchAddPermission(@Valid @NotEmpty @RequestBody List<SysPermissionAddRTO> addParamList) {
        Integer affectedRows = iSysPermissionService.batchAddPermission(addParamList);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>
     * 批量修改权限
     * </p>
     * <p>
     * 批量修改多个权限信息，需要登录并具备权限修改权限才能访问。
     * 仅允许修改指定权限的有效配置信息，不允许修改权限唯一标识。
     * </p>
     *
     * @param updateParamList 批量修改权限信息集合
     * @return 修改影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、权限不存在或修改失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @PutMapping("/batch")
    @Operation(summary = "批量修改权限", description = "批量修改权限信息")
    public ApiResponse batchUpdatePermission(@Valid @RequestBody List<SysPermissionUpdateRTO> updateParamList) {
        Integer affectedRows = iSysPermissionService.batchUpdatePermission(updateParamList);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>
     * 批量更新权限状态
     * </p>
     * <p>
     * 批量更新多个指定权限的状态（启用/禁用），禁用后该权限将不可被分配。
     * 批量操作支持事务回滚，任一权限更新失败则全部失败。
     * 需要登录并具备权限修改权限才能访问。
     * </p>
     *
     * @param ids 权限ID集合
     * @param status 权限状态（启用/禁用）
     * @return 更新结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、权限不存在或更新失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @PutMapping("/status/batch")
    @Operation(summary = "批量更新权限状态", description = "批量更新多个指定权限的状态（启用/禁用）")
    public ApiResponse batchUpdatePermissionStatus(@NotEmpty(message = "权限ID集合不能为空") @RequestBody List<String> ids,
                                                    @NotBlank(message = "状态不能为空") @RequestParam String status) {
        Integer affectedRows = iSysPermissionService.batchUpdatePermissionStatus(ids, status);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>
     * 批量删除权限
     * </p>
     * <p>
     * 批量删除多个指定权限信息，需要登录并具备权限删除权限才能访问。
     * 删除操作不可逆，删除后权限相关数据将同步清理。
     * </p>
     *
     * @param ids 权限ID集合
     * @return 删除影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、权限不存在或删除失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除权限", description = "批量删除权限信息")
    public ApiResponse batchDeletePermission(@NotEmpty(message = "权限ID集合不能为空") @RequestBody List<String> ids) {
        Integer affectedRows = iSysPermissionService.batchDeletePermission(ids);
        return ApiResponse.success(affectedRows);
    }

}
