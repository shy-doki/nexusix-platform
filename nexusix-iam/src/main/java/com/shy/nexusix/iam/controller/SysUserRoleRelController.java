package com.shy.nexusix.iam.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.iam.rto.SysUserRoleRelAddRTO;
import com.shy.nexusix.iam.rto.SysUserRoleRelQueryRTO;
import com.shy.nexusix.iam.rto.SysUserRoleRelUpdateRTO;
import com.shy.nexusix.iam.service.ISysUserRoleRelService;
import com.shy.nexusix.iam.vo.SysUserRoleRelCommonVO;
import com.shy.nexusix.iam.vo.SysUserRoleRelDetailVO;
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
 * 用户角色关联表 - 用户与角色的绑定关系 前端控制器
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@RestController
@RequestMapping("/sys-user-role-rel")
@Tag(name = "用户角色关联管理", description = "用户角色关联信息管理相关接口")
@Validated
public class SysUserRoleRelController {

    @Autowired
    private ISysUserRoleRelService iSysUserRoleRelService;

    @GetMapping("/list")
    @Operation(summary = "查询关联列表", description = "返回所有用户角色关联列表")
    public ApiResponse queryUserRoleRelList() {
        List<SysUserRoleRelCommonVO> relList = iSysUserRoleRelService.queryUserRoleRelList();
        return ApiResponse.success(relList);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询关联列表", description = "返回分页后的用户角色关联列表")
    public ApiResponse queryUserRoleRelPage(@Valid PageCommonRTO page) {
        IPage<SysUserRoleRelCommonVO> relPage = iSysUserRoleRelService.queryUserRoleRelPage(page);
        return ApiResponse.success(relPage);
    }

    @PostMapping("/query")
    @Operation(summary = "条件查询关联列表", description = "返回满足条件的用户角色关联列表")
    public ApiResponse queryUserRoleRel(@Valid @RequestBody SysUserRoleRelQueryRTO queryParam) {
        IPage<SysUserRoleRelCommonVO> relPage = iSysUserRoleRelService.queryUserRoleRel(queryParam);
        return ApiResponse.success(relPage);
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "查询关联详情", description = "返回指定用户角色关联的详情信息")
    public ApiResponse queryUserRoleRelDetail(@NotBlank(message = "关联记录ID不能为空") @PathVariable String id) {
        SysUserRoleRelDetailVO relDetail = iSysUserRoleRelService.queryUserRoleRelDetail(id);
        return ApiResponse.success(relDetail);
    }

    @PostMapping("/add")
    @Operation(summary = "新增关联", description = "新增用户角色关联记录")
    public ApiResponse addUserRoleRel(@Valid @RequestBody SysUserRoleRelAddRTO addParam) {
        Integer affectedRows = iSysUserRoleRelService.addUserRoleRel(addParam);
        return ApiResponse.success(affectedRows);
    }

    @PutMapping("/update")
    @Operation(summary = "修改关联", description = "修改用户角色关联记录信息")
    public ApiResponse updateUserRoleRel(@Valid @RequestBody SysUserRoleRelUpdateRTO updateParam) {
        Integer affectedRows = iSysUserRoleRelService.updateUserRoleRel(updateParam);
        return ApiResponse.success(affectedRows);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除关联", description = "删除用户角色关联记录")
    public ApiResponse deleteUserRoleRel(@NotBlank(message = "关联记录ID不能为空") @RequestParam String id) {
        Integer affectedRows = iSysUserRoleRelService.deleteUserRoleRel(id);
        return ApiResponse.success(affectedRows);
    }

    @PostMapping("/batch")
    @Operation(summary = "批量新增关联", description = "批量新增用户角色关联记录")
    public ApiResponse batchAddUserRoleRel(@Valid @NotEmpty @RequestBody List<SysUserRoleRelAddRTO> addParamList) {
        Integer affectedRows = iSysUserRoleRelService.batchAddUserRoleRel(addParamList);
        return ApiResponse.success(affectedRows);
    }

    @PutMapping("/batch")
    @Operation(summary = "批量修改关联", description = "批量修改用户角色关联记录")
    public ApiResponse batchUpdateUserRoleRel(@Valid @RequestBody List<SysUserRoleRelUpdateRTO> updateParamList) {
        Integer affectedRows = iSysUserRoleRelService.batchUpdateUserRoleRel(updateParamList);
        return ApiResponse.success(affectedRows);
    }

    @DeleteMapping("/batch")
    @Operation(summary = "批量删除关联", description = "批量删除用户角色关联记录")
    public ApiResponse batchDeleteUserRoleRel(@NotEmpty(message = "关联记录ID集合不能为空") @RequestBody List<String> ids) {
        Integer affectedRows = iSysUserRoleRelService.batchDeleteUserRoleRel(ids);
        return ApiResponse.success(affectedRows);
    }

}
