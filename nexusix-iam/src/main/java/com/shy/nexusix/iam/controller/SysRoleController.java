package com.shy.nexusix.iam.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.iam.rto.SysRoleAddRTO;
import com.shy.nexusix.iam.rto.SysRolePermissionAssignRTO;
import com.shy.nexusix.iam.rto.SysRoleQueryRTO;
import com.shy.nexusix.iam.rto.SysRoleUpdateRTO;
import com.shy.nexusix.iam.rto.SysRoleUserAssignRTO;
import com.shy.nexusix.iam.service.ISysPermissionPolicyService;
import com.shy.nexusix.iam.service.ISysRoleService;
import com.shy.nexusix.iam.vo.SysPermissionTreeVO;
import com.shy.nexusix.iam.vo.SysRoleCommonVO;
import com.shy.nexusix.iam.vo.SysRoleDetailVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 角色管理 - 控制器
 * </p>
 * <p>
 * 提供角色的CRUD、批量操作、状态切换、权限分配、用户分配等管理接口。
 * </p>
 *
 * @author shy
 * @since 2026-05-05
 */
@RestController
@RequestMapping("/sys-role")
@Tag(name = "角色管理", description = "角色基础信息与权限用户关联管理接口")
@Validated
public class SysRoleController {

    @Autowired
    private ISysRoleService iSysRoleService;

    @Autowired
    private ISysPermissionPolicyService iSysPermissionPolicyService;

    /**
     * <p>
     * 查询角色列表
     * </p>
     * <p>
     * 返回所有角色的平铺列表，按排序和ID升序排列
     * </p>
     *
     * @return 角色公共视图对象列表
     * @author shy
     * @since 2026-05-05
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
     *
     * @param page 分页参数
     * @return 分页后的角色公共视图对象
     * @author shy
     * @since 2026-05-05
     */
    @PostMapping("/page")
    @Operation(summary = "分页查询角色列表", description = "返回分页后的角色列表")
    public ApiResponse queryRolePage(@Valid @RequestBody PageCommonRTO page) {
        IPage<SysRoleCommonVO> rolePage = iSysRoleService.queryRolePage(page);
        return ApiResponse.success(rolePage);
    }

    /**
     * <p>
     * 条件查询角色列表
     * </p>
     * <p>
     * 支持按角色名称、编码模糊匹配，按层级、租户、状态、创建人、创建时间筛选
     * </p>
     *
     * @param queryParam 查询条件
     * @return 分页后的角色公共视图对象
     * @author shy
     * @since 2026-05-05
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
     *
     * @param id 角色ID
     * @return 角色详情视图对象
     * @throws com.shy.nexusix.common.exception.BusinessException 当角色不存在时抛出
     * @author shy
     * @since 2026-05-05
     */
    @GetMapping("/detail/{id}")
    @Operation(summary = "查询角色详情", description = "返回指定角色的详情信息")
    public ApiResponse queryRoleDetail(@PathVariable String id) {
        SysRoleDetailVO detail = iSysRoleService.queryRoleDetail(id);
        return ApiResponse.success(detail);
    }

    /**
     * <p>
     * 新增角色
     * </p>
     * <p>
     * 校验角色编码唯一性，tenantId为空时默认设为0（系统级），
     * 自动填充tenantName、createBy、createByName等冗余字段
     * </p>
     *
     * @param addParam 新增参数
     * @return 影响行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当角色编码已存在时抛出
     * @author shy
     * @since 2026-05-05
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
     * 校验角色存在性和编码唯一性（排除自身），
     * 自动更新tenantName、updateBy、updateByName等冗余字段
     * </p>
     *
     * @param updateParam 修改参数
     * @return 影响行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当角色不存在或编码已被占用时抛出
     * @author shy
     * @since 2026-05-05
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
     * 更新指定角色的状态（启用/禁用），禁用后该角色对所有人失效
     * </p>
     *
     * @param id     角色ID
     * @param status 状态值（1-启用 0-禁用）
     * @return 影响行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当角色不存在或状态未变更时抛出
     * @author shy
     * @since 2026-05-05
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
     * 逻辑删除，删除前会校验是否存在用户关联
     * </p>
     *
     * @param id 角色ID
     * @return 影响行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当角色不存在或存在用户关联时抛出
     * @author shy
     * @since 2026-05-05
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
     * 单次上限100条，校验批量内部去重和数据库唯一性
     * </p>
     *
     * @param addParamList 新增参数列表
     * @return 影响行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当存在重复编码或超过上限时抛出
     * @author shy
     * @since 2026-05-05
     */
    @PostMapping("/batch")
    @Operation(summary = "批量新增角色", description = "批量新增角色信息")
    public ApiResponse batchAddRole(@Valid @RequestBody List<SysRoleAddRTO> addParamList) {
        Integer affectedRows = iSysRoleService.batchAddRole(addParamList);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>
     * 批量修改角色
     * </p>
     * <p>
     * 单次上限100条，校验ID存在性和编码唯一性
     * </p>
     *
     * @param updateParamList 修改参数列表
     * @return 影响行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当部分角色不存在或编码冲突时抛出
     * @author shy
     * @since 2026-05-05
     */
    @PutMapping("/batch")
    @Operation(summary = "批量修改角色", description = "批量修改角色信息")
    public ApiResponse batchUpdateRole(@Valid @RequestBody List<SysRoleUpdateRTO> updateParamList) {
        Integer affectedRows = iSysRoleService.batchUpdateRole(updateParamList);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>
     * 批量删除角色
     * </p>
     * <p>
     * 单次上限100条，逻辑删除，删除前校验是否存在用户关联
     * </p>
     *
     * @param ids 角色ID列表
     * @return 影响行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当存在用户关联或超过上限时抛出
     * @author shy
     * @since 2026-05-05
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除角色", description = "批量删除角色信息")
    public ApiResponse batchDeleteRole(@RequestBody List<String> ids) {
        Integer affectedRows = iSysRoleService.batchDeleteRole(ids);
        return ApiResponse.success(affectedRows);
    }

}
