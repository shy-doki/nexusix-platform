package com.shy.nexusix.iam.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.iam.rto.SysRoleAddRTO;
import com.shy.nexusix.iam.rto.SysRoleGrantPermRTO;
import com.shy.nexusix.iam.rto.SysRoleQueryRTO;
import com.shy.nexusix.iam.rto.SysRoleUpdateRTO;
import com.shy.nexusix.iam.service.ISysRoleService;
import com.shy.nexusix.iam.vo.SysPermCommonVO;
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
 * <p>角色管理控制器</p>
 *
 * @author shy
 */
@RestController
@RequestMapping("/role")
@Tag(name = "角色管理", description = "角色管理相关接口")
@Validated
public class SysRoleController {

    @Autowired
    private ISysRoleService iSysRoleService;

    /**
     * <p>查询角色列表</p>
     *
     * @return 角色列表
     * @throws BusinessException 无权限或查询失败时抛出
     */
    @GetMapping("/list")
    @Operation(summary = "查询角色列表", description = "返回所有角色列表")
    public ApiResponse queryRoleList() {
        List<SysRoleCommonVO> roleList = iSysRoleService.queryRoleList();
        return ApiResponse.success(roleList);
    }

    /**
     * <p>分页查询角色列表</p>
     *
     * @param page 分页参数
     * @return 分页后的角色列表
     * @throws BusinessException 无权限时抛出
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询角色列表", description = "返回分页后的角色列表")
    public ApiResponse queryRolePage(@Valid PageCommonRTO page) {
        IPage<SysRoleCommonVO> rolePage = iSysRoleService.queryRolePage(page);
        return ApiResponse.success(rolePage);
    }

    /**
     * <p>条件查询角色列表</p>
     *
     * @param queryParam 查询条件
     * @return 满足条件的角色分页列表
     * @throws BusinessException 无权限或查询失败时抛出
     */
    @PostMapping("/query")
    @Operation(summary = "条件查询角色列表", description = "返回满足条件的角色列表")
    public ApiResponse queryRole(@Valid @RequestBody SysRoleQueryRTO queryParam) {
        IPage<SysRoleCommonVO> rolePage = iSysRoleService.queryRole(queryParam);
        return ApiResponse.success(rolePage);
    }

    /**
     * <p>查询角色详情</p>
     *
     * @param roleCode 角色编码
     * @return 角色详情信息
     * @throws BusinessException 无权限或查询失败时抛出
     */
    @GetMapping("/detail/{roleCode}")
    @Operation(summary = "查询角色详情", description = "返回指定角色的详情信息")
    public ApiResponse queryRoleDetail(@NotBlank(message = "角色编码不能为空") @PathVariable String roleCode) {
        SysRoleDetailVO roleDetail = iSysRoleService.queryRoleDetail(roleCode);
        return ApiResponse.success(roleDetail);
    }

    /**
     * <p>新增角色</p>
     *
     * @param addParam 新增角色信息
     * @return 新增结果行数
     * @throws BusinessException 无权限或新增失败时抛出
     */
    @PostMapping("/add")
    @Operation(summary = "新增角色", description = "新增角色信息")
    public ApiResponse addRole(@Valid @RequestBody SysRoleAddRTO addParam) {
        Integer affectedRows = iSysRoleService.addRole(addParam);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>修改角色</p>
     *
     * @param updateParam 修改角色信息
     * @return 修改结果行数
     * @throws BusinessException 无权限、角色不存在或修改失败时抛出
     */
    @PutMapping("/update")
    @Operation(summary = "修改角色", description = "修改角色信息")
    public ApiResponse updateRole(@Valid @RequestBody SysRoleUpdateRTO updateParam) {
        Integer affectedRows = iSysRoleService.updateRole(updateParam);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>更新角色状态</p>
     *
     * @param id 角色ID
     * @param status 角色状态
     * @return 更新结果行数
     * @throws BusinessException 无权限、角色不存在或更新失败时抛出
     */
    @PutMapping("/status")
    @Operation(summary = "更新角色状态", description = "更新指定角色的状态")
    public ApiResponse updateRoleStatus(@NotBlank(message = "Id不能为空") @RequestParam String id,
                                        @NotBlank(message = "状态不能为空") @RequestParam String status) {
        Integer affectedRows = iSysRoleService.updateRoleStatus(id, status);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>删除角色</p>
     *
     * @param id 角色ID
     * @return 删除结果行数
     * @throws BusinessException 无权限、角色不存在或删除失败时抛出
     */
    @DeleteMapping("/delete")
    @Operation(summary = "删除角色", description = "删除角色信息")
    public ApiResponse deleteRole(@NotBlank(message = "Id不能为空") @RequestParam String id) {
        Integer affectedRows = iSysRoleService.deleteRole(id);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>批量新增角色</p>
     *
     * @param addParamList 批量新增角色信息集合
     * @return 新增结果行数
     * @throws BusinessException 无权限、参数校验失败或新增失败时抛出
     */
    @PostMapping("/batch")
    @Operation(summary = "批量新增角色", description = "批量新增角色信息")
    public ApiResponse batchAddRole(@Valid @NotEmpty @RequestBody List<SysRoleAddRTO> addParamList) {
        Integer affectedRows = iSysRoleService.batchAddRole(addParamList);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>批量修改角色</p>
     *
     * @param updateParamList 批量修改角色信息集合
     * @return 修改结果行数
     * @throws BusinessException 无权限、角色不存在或修改失败时抛出
     */
    @PutMapping("/batch")
    @Operation(summary = "批量修改角色", description = "批量修改角色信息")
    public ApiResponse batchUpdateRole(@Valid @RequestBody List<SysRoleUpdateRTO> updateParamList) {
        Integer affectedRows = iSysRoleService.batchUpdateRole(updateParamList);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>批量更新角色状态</p>
     *
     * @param ids 角色ID集合
     * @param status 角色状态
     * @return 更新结果行数
     * @throws BusinessException 无权限、角色不存在或更新失败时抛出
     */
    @PutMapping("/status/batch")
    @Operation(summary = "批量更新角色状态", description = "批量更新多个指定角色的状态")
    public ApiResponse batchUpdateRoleStatus(@NotEmpty(message = "角色ID集合不能为空") @RequestBody List<String> ids,
                                             @NotBlank(message = "状态不能为空") @RequestParam String status) {
        Integer affectedRows = iSysRoleService.batchUpdateRoleStatus(ids, status);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>批量删除角色</p>
     *
     * @param ids 角色ID集合
     * @return 删除结果行数
     * @throws BusinessException 无权限、角色不存在或删除失败时抛出
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除角色", description = "批量删除角色信息")
    public ApiResponse batchDeleteRole(@NotEmpty List<String> ids) {
        Integer affectedRows = iSysRoleService.batchDeleteRole(ids);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>查询角色权限</p>
     *
     * @param roleCode 角色编码
     * @return 角色权限列表
     * @throws BusinessException 角色不存在时抛出
     */
    @GetMapping("/{roleCode}/permissions")
    @Operation(summary = "查询角色权限", description = "返回指定角色拥有的权限列表")
    public ApiResponse queryRolePerms(@NotBlank(message = "角色编码不能为空") @PathVariable String roleCode) {
        List<SysPermCommonVO> permList = iSysRoleService.queryRolePerms(roleCode);
        return ApiResponse.success(permList);
    }

    /**
     * <p>为角色授予权限</p>
     *
     * @param roleCode 角色编码
     * @param param 授权请求参数，包含权限编码列表
     * @return 新创建的权限策略数量
     * @throws BusinessException 角色不存在或权限编码不存在时抛出
     */
    @PostMapping("/{roleCode}/permissions")
    @Operation(summary = "为角色授予权限", description = "为指定角色授予多个权限")
    public ApiResponse grantPermissions(@NotBlank(message = "角色编码不能为空") @PathVariable String roleCode,
                                         @Valid @RequestBody SysRoleGrantPermRTO param) {
        Integer affectedRows = iSysRoleService.grantPermissions(roleCode, param);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>撤销角色权限</p>
     *
     * @param roleCode 角色编码
     * @param permCodeList 权限编码列表
     * @return 删除的权限策略数量
     * @throws BusinessException 角色不存在时抛出
     */
    @DeleteMapping("/{roleCode}/permissions")
    @Operation(summary = "撤销角色权限", description = "撤销指定角色的多个权限")
    public ApiResponse revokePermissions(@NotBlank(message = "角色编码不能为空") @PathVariable String roleCode,
                                          @NotEmpty(message = "权限编码列表不能为空") @RequestBody List<String> permCodeList) {
        Integer affectedRows = iSysRoleService.revokePermissions(roleCode, permCodeList);
        return ApiResponse.success(affectedRows);
    }

}
