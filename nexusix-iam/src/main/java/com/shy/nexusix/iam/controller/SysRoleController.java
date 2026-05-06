package com.shy.nexusix.iam.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.iam.rto.SysRoleAddRTO;
import com.shy.nexusix.iam.rto.SysRoleQueryRTO;
import com.shy.nexusix.iam.rto.SysRoleUpdateRTO;
import com.shy.nexusix.iam.service.ISysRoleService;
import com.shy.nexusix.iam.vo.SysRoleCommonVO;
import com.shy.nexusix.iam.vo.SysRoleDetailVO;
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
 * 角色表 - 定义系统/租户/用户级角色 前端控制器
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@RestController
@RequestMapping("/role")
@Tag(name = "角色管理", description = "角色管理相关接口")
@Validated
public class SysRoleController {

    @Autowired
    private ISysRoleService iSysRoleService;

    /**
     * <p>
     * 查询角色列表
     * </p>
     * <p>
     * 返回所有角色的平铺列表。
     * 需要登录并具备角色查看权限才能访问。
     * </p>
     *
     * @return 角色列表
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @GetMapping("/list")
    @Operation(summary = "查询角色列表", description = "返回所有角色列表")
    public ApiResponse queryRoleList() {
        List<SysRoleCommonVO> roleList = iSysRoleService.queryRoleList();
        return ApiResponse.success(roleList);
    }

    /**
     * <p>
     * 分页查询角色列表
     * </p>
     * <p>
     * 返回分页后的角色列表。
     * 需要登录并具备角色查看权限才能访问。
     * </p>
     *
     * @param page 分页参数
     * @return 分页后的角色列表
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限
     * @author shy
     * @since 2026-05-06
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询角色列表", description = "返回分页后的角色列表")
    public ApiResponse queryRolePage(@Valid PageCommonRTO page) {
        IPage<SysRoleCommonVO> rolePage = iSysRoleService.queryRolePage(page);
        return ApiResponse.success(rolePage);
    }

    /**
     * <p>
     * 条件查询\筛选角色列表
     * </p>
     * <p>
     * 返回满足条件的角色列表。
     * 需要登录并具备角色条件查询权限才能访问。
     * </p>
     *
     * @param queryParam 查询条件
     * @return 满足条件的角色列表
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @PostMapping("/query")
    @Operation(summary = "条件查询角色列表", description = "返回满足条件的角色列表")
    public ApiResponse queryRole(@Valid @RequestBody SysRoleQueryRTO queryParam) {
        IPage<SysRoleCommonVO> rolePage = iSysRoleService.queryRole(queryParam);
        return ApiResponse.success(rolePage);
    }

    /**
     * <p>
     * 查询角色详情
     * </p>
     * <p>
     * 返回指定角色的详情信息。
     * 需要登录并具备角色详情查询权限才能访问。
     * </p>
     *
     * @param roleCode 角色编码
     * @return 角色详情信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @GetMapping("/detail/{roleCode}")
    @Operation(summary = "查询角色详情", description = "返回指定角色的详情信息")
    public ApiResponse queryRoleDetail(@NotBlank(message = "角色编码不能为空") @PathVariable String roleCode) {
        SysRoleDetailVO roleDetail = iSysRoleService.queryRoleDetail(roleCode);
        return ApiResponse.success(roleDetail);
    }

    /**
     * <p>
     * 新增角色
     * </p>
     * <p>
     * 新增角色信息，需要登录并具备角色新增权限才能访问。
     * </p>
     *
     * @param addParam 新增角色信息
     * @return 新增影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或新增失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @PostMapping("/add")
    @Operation(summary = "新增角色", description = "新增角色信息")
    public ApiResponse addRole(@Valid @RequestBody SysRoleAddRTO addParam) {
        Integer affectedRows = iSysRoleService.addRole(addParam);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>
     * 修改角色
     * </p>
     * <p>
     * 修改角色信息，需要登录并具备角色修改权限才能访问。
     * </p>
     *
     * @param updateParam 修改角色信息
     * @return 修改影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、角色不存在或修改失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @PutMapping("/update")
    @Operation(summary = "修改角色", description = "修改角色信息")
    public ApiResponse updateRole(@Valid @RequestBody SysRoleUpdateRTO updateParam) {
        Integer affectedRows = iSysRoleService.updateRole(updateParam);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>
     * 更新角色状态
     * </p>
     * <p>
     * 更新指定角色的状态（启用/禁用），禁用后该角色将不可被分配。
     * 需要登录并具备角色修改权限才能访问。
     * </p>
     *
     * @param id 角色ID
     * @param status 角色状态（启用/禁用）
     * @return 更新结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、角色不存在或更新失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @PutMapping("/status")
    @Operation(summary = "更新角色状态", description = "更新指定角色的状态（启用/禁用）")
    public ApiResponse updateRoleStatus(@NotBlank(message = "Id不能为空") @RequestParam String id,
                                         @NotBlank(message = "状态不能为空") @RequestParam String status) {
        Integer affectedRows = iSysRoleService.updateRoleStatus(id, status);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>
     * 删除角色
     * </p>
     * <p>
     * 删除指定角色信息，需要登录并具备角色删除权限才能访问。
     * </p>
     *
     * @param id 角色ID
     * @return 删除影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、角色不存在或删除失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @DeleteMapping("/delete")
    @Operation(summary = "删除角色", description = "删除角色信息")
    public ApiResponse deleteRole(@NotBlank(message = "Id不能为空") @RequestParam String id) {
        Integer affectedRows = iSysRoleService.deleteRole(id);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>
     * 批量新增角色
     * </p>
     * <p>
     * 批量新增多个角色信息，需要登录并具备角色新增权限才能访问。
     * 批量操作支持事务回滚，任一角色新增失败则全部失败。
     * </p>
     *
     * @param addParamList 批量新增角色信息集合
     * @return 新增影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、参数校验失败或新增失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @PostMapping("/batch")
    @Operation(summary = "批量新增角色", description = "批量新增角色信息")
    public ApiResponse batchAddRole(@Valid @NotEmpty @RequestBody List<SysRoleAddRTO> addParamList) {
        Integer affectedRows = iSysRoleService.batchAddRole(addParamList);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>
     * 批量修改角色
     * </p>
     * <p>
     * 批量修改多个角色信息，需要登录并具备角色修改权限才能访问。
     * </p>
     *
     * @param updateParamList 批量修改角色信息集合
     * @return 修改影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、角色不存在或修改失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @PutMapping("/batch")
    @Operation(summary = "批量修改角色", description = "批量修改角色信息")
    public ApiResponse batchUpdateRole(@Valid @RequestBody List<SysRoleUpdateRTO> updateParamList) {
        Integer affectedRows = iSysRoleService.batchUpdateRole(updateParamList);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>
     * 批量更新角色状态
     * </p>
     * <p>
     * 批量更新多个指定角色的状态（启用/禁用），禁用后该角色将不可被分配。
     * 批量操作支持事务回滚，任一角色更新失败则全部失败。
     * 需要登录并具备角色修改权限才能访问。
     * </p>
     *
     * @param ids 角色ID集合
     * @param status 角色状态（启用/禁用）
     * @return 更新结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、角色不存在或更新失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @PutMapping("/status/batch")
    @Operation(summary = "批量更新角色状态", description = "批量更新多个指定角色的状态（启用/禁用）")
    public ApiResponse batchUpdateRoleStatus(@NotEmpty(message = "角色ID集合不能为空") @RequestBody List<String> ids,
                                              @NotBlank(message = "状态不能为空") @RequestParam String status) {
        Integer affectedRows = iSysRoleService.batchUpdateRoleStatus(ids, status);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>
     * 批量删除角色
     * </p>
     * <p>
     * 批量删除多个指定角色信息，需要登录并具备角色删除权限才能访问。
     * </p>
     *
     * @param ids 角色ID集合
     * @return 删除影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、角色不存在或删除失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除角色", description = "批量删除角色信息")
    public ApiResponse batchDeleteRole(@NotEmpty(message = "角色ID集合不能为空") @RequestBody List<String> ids) {
        Integer affectedRows = iSysRoleService.batchDeleteRole(ids);
        return ApiResponse.success(affectedRows);
    }

}
