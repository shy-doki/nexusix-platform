package com.shy.nexusix.iam.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.iam.rto.SysUserTenantRelAddRTO;
import com.shy.nexusix.iam.rto.SysUserTenantRelQueryRTO;
import com.shy.nexusix.iam.rto.SysUserTenantRelUpdateRTO;
import com.shy.nexusix.iam.service.ISysUserTenantRelService;
import com.shy.nexusix.iam.vo.SysUserTenantRelCommonVO;
import com.shy.nexusix.iam.vo.SysUserTenantRelDetailVO;
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
 * 用户 - 租户关联表 - 实现用户与多租户绑定 前端控制器
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@RestController
@RequestMapping("/sys-user-tenant-rel")
@Tag(name = "用户租户关联管理", description = "用户租户关联信息管理相关接口")
@Validated
public class SysUserTenantRelController {

    @Autowired
    private ISysUserTenantRelService iSysUserTenantRelService;

    @GetMapping("/list")
    @Operation(summary = "查询关联列表", description = "返回所有用户租户关联列表")
    public ApiResponse queryUserTenantRelList() {
        List<SysUserTenantRelCommonVO> relList = iSysUserTenantRelService.queryUserTenantRelList();
        return ApiResponse.success(relList);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询关联列表", description = "返回分页后的用户租户关联列表")
    public ApiResponse queryUserTenantRelPage(@Valid PageCommonRTO page) {
        IPage<SysUserTenantRelCommonVO> relPage = iSysUserTenantRelService.queryUserTenantRelPage(page);
        return ApiResponse.success(relPage);
    }

    @PostMapping("/query")
    @Operation(summary = "条件查询关联列表", description = "返回满足条件的用户租户关联列表")
    public ApiResponse queryUserTenantRel(@Valid @RequestBody SysUserTenantRelQueryRTO queryParam) {
        IPage<SysUserTenantRelCommonVO> relPage = iSysUserTenantRelService.queryUserTenantRel(queryParam);
        return ApiResponse.success(relPage);
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "查询关联详情", description = "返回指定用户租户关联的详情信息")
    public ApiResponse queryUserTenantRelDetail(@NotBlank(message = "关联记录ID不能为空") @PathVariable String id) {
        SysUserTenantRelDetailVO relDetail = iSysUserTenantRelService.queryUserTenantRelDetail(id);
        return ApiResponse.success(relDetail);
    }

    @PostMapping("/add")
    @Operation(summary = "新增关联", description = "新增用户租户关联记录")
    public ApiResponse addUserTenantRel(@Valid @RequestBody SysUserTenantRelAddRTO addParam) {
        Integer affectedRows = iSysUserTenantRelService.addUserTenantRel(addParam);
        return ApiResponse.success(affectedRows);
    }

    @PutMapping("/update")
    @Operation(summary = "修改关联", description = "修改用户租户关联记录信息")
    public ApiResponse updateUserTenantRel(@Valid @RequestBody SysUserTenantRelUpdateRTO updateParam) {
        Integer affectedRows = iSysUserTenantRelService.updateUserTenantRel(updateParam);
        return ApiResponse.success(affectedRows);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除关联", description = "删除用户租户关联记录")
    public ApiResponse deleteUserTenantRel(@NotBlank(message = "关联记录ID不能为空") @RequestParam String id) {
        Integer affectedRows = iSysUserTenantRelService.deleteUserTenantRel(id);
        return ApiResponse.success(affectedRows);
    }

    @PostMapping("/batch")
    @Operation(summary = "批量新增关联", description = "批量新增用户租户关联记录")
    public ApiResponse batchAddUserTenantRel(@Valid @NotEmpty @RequestBody List<SysUserTenantRelAddRTO> addParamList) {
        Integer affectedRows = iSysUserTenantRelService.batchAddUserTenantRel(addParamList);
        return ApiResponse.success(affectedRows);
    }

    @PutMapping("/batch")
    @Operation(summary = "批量修改关联", description = "批量修改用户租户关联记录")
    public ApiResponse batchUpdateUserTenantRel(@Valid @RequestBody List<SysUserTenantRelUpdateRTO> updateParamList) {
        Integer affectedRows = iSysUserTenantRelService.batchUpdateUserTenantRel(updateParamList);
        return ApiResponse.success(affectedRows);
    }

    @DeleteMapping("/batch")
    @Operation(summary = "批量删除关联", description = "批量删除用户租户关联记录")
    public ApiResponse batchDeleteUserTenantRel(@NotEmpty(message = "关联记录ID集合不能为空") @RequestBody List<String> ids) {
        Integer affectedRows = iSysUserTenantRelService.batchDeleteUserTenantRel(ids);
        return ApiResponse.success(affectedRows);
    }

}
