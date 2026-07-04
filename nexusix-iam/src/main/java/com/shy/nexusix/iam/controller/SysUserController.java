package com.shy.nexusix.iam.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.iam.rto.SysUserAddRTO;
import com.shy.nexusix.iam.rto.SysUserChangePasswordRTO;
import com.shy.nexusix.iam.rto.SysUserQueryRTO;
import com.shy.nexusix.iam.rto.SysUserResetPasswordRTO;
import com.shy.nexusix.iam.rto.SysUserUpdateRTO;
import com.shy.nexusix.iam.service.ISysUserService;
import com.shy.nexusix.iam.vo.SysRoleCommonVO;
import com.shy.nexusix.iam.vo.SysUserCommonVO;
import com.shy.nexusix.iam.vo.SysUserDetailVO;
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
 * <p>用户管理控制器</p>
 *
 * @author shy
 */
@RestController
@RequestMapping("/user")
@Tag(name = "用户管理", description = "用户管理相关接口")
@Validated
public class SysUserController {

    @Autowired
    private ISysUserService iSysUserService;

    /**
     * <p>查询用户列表</p>
     *
     * @return 用户列表
     * @throws BusinessException 无权限或查询失败时抛出
     */
    @GetMapping("/list")
    @Operation(summary = "查询用户列表", description = "返回所有用户列表")
    public ApiResponse queryUserList() {
        List<SysUserCommonVO> userList = iSysUserService.queryUserList();
        return ApiResponse.success(userList);
    }

    /**
     * <p>分页查询用户列表</p>
     *
     * @param page 分页参数
     * @return 分页后的用户列表
     * @throws BusinessException 无权限时抛出
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询用户列表", description = "返回分页后的用户列表")
    public ApiResponse queryUserPage(@Valid PageCommonRTO page) {
        IPage<SysUserCommonVO> userPage = iSysUserService.queryUserPage(page);
        return ApiResponse.success(userPage);
    }

    /**
     * <p>条件查询用户列表</p>
     *
     * @param queryParam 查询条件
     * @return 满足条件的用户分页列表
     * @throws BusinessException 无权限或查询失败时抛出
     */
    @PostMapping("/query")
    @Operation(summary = "条件查询用户列表", description = "返回满足条件的用户列表")
    public ApiResponse queryUser(@Valid @RequestBody SysUserQueryRTO queryParam) {
        IPage<SysUserCommonVO> userPage = iSysUserService.queryUser(queryParam);
        return ApiResponse.success(userPage);
    }

    /**
     * <p>查询用户详情</p>
     *
     * @param userCode 用户编码
     * @return 用户详情信息
     * @throws BusinessException 无权限或查询失败时抛出
     */
    @GetMapping("/detail/{userCode}")
    @Operation(summary = "查询用户详情", description = "返回指定用户的详情信息")
    public ApiResponse queryUserDetail(@NotBlank(message = "用户编码不能为空") @PathVariable String userCode) {
        SysUserDetailVO userDetail = iSysUserService.queryUserDetail(userCode);
        return ApiResponse.success(userDetail);
    }

    /**
     * <p>新增用户</p>
     *
     * @param addParam 新增用户信息
     * @return 新增结果行数
     * @throws BusinessException 无权限或新增失败时抛出
     */
    @PostMapping("/add")
    @Operation(summary = "新增用户", description = "新增用户信息")
    public ApiResponse addUser(@Valid @RequestBody SysUserAddRTO addParam) {
        Integer affectedRows = iSysUserService.addUser(addParam);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>修改用户</p>
     *
     * @param updateParam 修改用户信息
     * @return 修改结果行数
     * @throws BusinessException 无权限、用户不存在或修改失败时抛出
     */
    @PutMapping("/update")
    @Operation(summary = "修改用户", description = "修改用户信息")
    public ApiResponse updateUser(@Valid @RequestBody SysUserUpdateRTO updateParam) {
        Integer affectedRows = iSysUserService.updateUser(updateParam);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>更新用户状态</p>
     *
     * @param id 用户ID
     * @param status 用户状态
     * @return 更新结果行数
     * @throws BusinessException 无权限、用户不存在或更新失败时抛出
     */
    @PutMapping("/status")
    @Operation(summary = "更新用户状态", description = "更新指定用户的状态")
    public ApiResponse updateUserStatus(@NotBlank(message = "Id不能为空") @RequestParam String id,
                                        @NotBlank(message = "状态不能为空") @RequestParam String status) {
        Integer affectedRows = iSysUserService.updateUserStatus(id, status);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>删除用户</p>
     *
     * @param id 用户ID
     * @return 删除结果行数
     * @throws BusinessException 无权限、用户不存在或删除失败时抛出
     */
    @DeleteMapping("/delete")
    @Operation(summary = "删除用户", description = "删除用户信息")
    public ApiResponse deleteUser(@NotBlank(message = "Id不能为空") @RequestParam String id) {
        Integer affectedRows = iSysUserService.deleteUser(id);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>批量新增用户</p>
     *
     * @param addParamList 批量新增用户信息集合
     * @return 新增结果行数
     * @throws BusinessException 无权限、参数校验失败或新增失败时抛出
     */
    @PostMapping("/batch")
    @Operation(summary = "批量新增用户", description = "批量新增用户信息")
    public ApiResponse batchAddUser(@Valid @NotEmpty @RequestBody List<SysUserAddRTO> addParamList) {
        Integer affectedRows = iSysUserService.batchAddUser(addParamList);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>批量修改用户</p>
     *
     * @param updateParamList 批量修改用户信息集合
     * @return 修改结果行数
     * @throws BusinessException 无权限、用户不存在或修改失败时抛出
     */
    @PutMapping("/batch")
    @Operation(summary = "批量修改用户", description = "批量修改用户信息")
    public ApiResponse batchUpdateUser(@Valid @RequestBody List<SysUserUpdateRTO> updateParamList) {
        Integer affectedRows = iSysUserService.batchUpdateUser(updateParamList);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>批量更新用户状态</p>
     *
     * @param ids 用户ID集合
     * @param status 用户状态
     * @return 更新结果行数
     * @throws BusinessException 无权限、用户不存在或更新失败时抛出
     */
    @PutMapping("/status/batch")
    @Operation(summary = "批量更新用户状态", description = "批量更新多个指定用户的状态")
    public ApiResponse batchUpdateUserStatus(@NotEmpty(message = "用户ID集合不能为空") @RequestBody List<String> ids,
                                             @NotBlank(message = "状态不能为空") @RequestParam String status) {
        Integer affectedRows = iSysUserService.batchUpdateUserStatus(ids, status);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>批量删除用户</p>
     *
     * @param ids 用户ID集合
     * @return 删除结果行数
     * @throws BusinessException 无权限、用户不存在或删除失败时抛出
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除用户", description = "批量删除用户信息")
    public ApiResponse batchDeleteUser(@NotEmpty List<String> ids) {
        Integer affectedRows = iSysUserService.batchDeleteUser(ids);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>管理员重置用户密码</p>
     *
     * @param param 重置密码参数
     * @return 更新结果行数
     * @throws BusinessException 用户不存在时抛出
     */
    @PostMapping("/reset-password")
    @Operation(summary = "重置用户密码", description = "管理员重置指定用户的密码")
    public ApiResponse resetPassword(@Valid @RequestBody SysUserResetPasswordRTO param) {
        Integer affectedRows = iSysUserService.resetPassword(param);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>用户修改密码</p>
     *
     * @param param 修改密码参数
     * @return 更新结果行数
     * @throws BusinessException 两次密码不一致、用户不存在或旧密码不正确时抛出
     */
    @PostMapping("/change-password")
    @Operation(summary = "用户修改密码", description = "用户自行修改登录密码")
    public ApiResponse changePassword(@Valid @RequestBody SysUserChangePasswordRTO param) {
        Integer affectedRows = iSysUserService.changePassword(param);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>查询用户角色</p>
     *
     * @param userCode 用户编码
     * @return 用户角色列表
     * @throws BusinessException 用户不存在时抛出
     */
    @GetMapping("/{userCode}/roles")
    @Operation(summary = "查询用户角色", description = "返回指定用户的角色列表")
    public ApiResponse queryUserRoles(@NotBlank(message = "用户编码不能为空") @PathVariable String userCode) {
        List<SysRoleCommonVO> roleList = iSysUserService.queryUserRoles(userCode);
        return ApiResponse.success(roleList);
    }

    /**
     * <p>为用户分配角色</p>
     *
     * @param userCode 用户编码
     * @param roleCodeList 角色编码集合
     * @return 新创建的角色策略数量
     * @throws BusinessException 用户不存在或无法获取租户信息时抛出
     */
    @PostMapping("/{userCode}/roles")
    @Operation(summary = "为用户分配角色", description = "为指定用户分配角色，覆盖原有角色")
    public ApiResponse assignRoles(@NotBlank(message = "用户编码不能为空") @PathVariable String userCode,
                                    @NotEmpty(message = "角色编码集合不能为空") @RequestBody List<String> roleCodeList) {
        Integer affectedRows = iSysUserService.assignRoles(userCode, roleCodeList);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>查询用户权限</p>
     *
     * @param userCode 用户编码
     * @return 用户权限编码列表
     * @throws BusinessException 用户不存在时抛出
     */
    @GetMapping("/{userCode}/permissions")
    @Operation(summary = "查询用户权限", description = "返回指定用户的权限编码列表")
    public ApiResponse queryUserPerms(@NotBlank(message = "用户编码不能为空") @PathVariable String userCode) {
        List<String> permList = iSysUserService.queryUserPerms(userCode);
        return ApiResponse.success(permList);
    }

}
