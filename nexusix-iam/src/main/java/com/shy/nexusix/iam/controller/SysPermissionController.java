package com.shy.nexusix.iam.controller;

import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.iam.rto.SysPermissionAddRTO;
import com.shy.nexusix.iam.rto.SysPermissionQueryRTO;
import com.shy.nexusix.iam.rto.SysPermissionUpdateRTO;
import com.shy.nexusix.iam.service.ISysPermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sys-permission")
@Tag(name = "权限管理", description = "权限资源定义与管理接口")
public class SysPermissionController {

    @Autowired
    private ISysPermissionService iSysPermissionService;

    @Operation(summary = "查询权限列表")
    @GetMapping("/list")
    public ApiResponse queryPermissionList() {
        return ApiResponse.success(iSysPermissionService.queryPermissionList());
    }

    @Operation(summary = "分页查询权限")
    @GetMapping("/page")
    public ApiResponse queryPermissionPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageCommonRTO page = new PageCommonRTO();
        page.setPageNum(pageNum);
        page.setPageSize(pageSize);
        return ApiResponse.success(iSysPermissionService.queryPermissionPage(page));
    }

    @Operation(summary = "查询权限树形列表")
    @GetMapping("/tree/list")
    public ApiResponse queryPermissionTreeList() {
        return ApiResponse.success(iSysPermissionService.queryPermissionTreeList());
    }

    @Operation(summary = "条件查询权限")
    @PostMapping("/query")
    public ApiResponse queryPermission(@Valid @RequestBody SysPermissionQueryRTO queryParam) {
        return ApiResponse.success(iSysPermissionService.queryPermission(queryParam));
    }

    @Operation(summary = "查询权限详情")
    @GetMapping("/detail/{id}")
    public ApiResponse queryPermissionDetail(@PathVariable String id) {
        return ApiResponse.success(iSysPermissionService.queryPermissionDetail(id));
    }

    @Operation(summary = "新增权限")
    @PostMapping("/add")
    public ApiResponse addPermission(@Valid @RequestBody SysPermissionAddRTO addParam) {
        return ApiResponse.success(iSysPermissionService.addPermission(addParam));
    }

    @Operation(summary = "修改权限")
    @PutMapping("/update")
    public ApiResponse updatePermission(@Valid @RequestBody SysPermissionUpdateRTO updateParam) {
        return ApiResponse.success(iSysPermissionService.updatePermission(updateParam));
    }

    @Operation(summary = "更新权限状态")
    @PutMapping("/status")
    public ApiResponse updatePermissionStatus(
            @RequestParam String id,
            @RequestParam String status) {
        return ApiResponse.success(iSysPermissionService.updatePermissionStatus(id, status));
    }

    @Operation(summary = "删除权限")
    @DeleteMapping("/delete")
    public ApiResponse deletePermission(@RequestParam String id) {
        return ApiResponse.success(iSysPermissionService.deletePermission(id));
    }

    @Operation(summary = "批量新增权限")
    @PostMapping("/batch")
    public ApiResponse batchAddPermission(@Valid @RequestBody List<SysPermissionAddRTO> addParamList) {
        return ApiResponse.success(iSysPermissionService.batchAddPermission(addParamList));
    }

    @Operation(summary = "批量修改权限")
    @PutMapping("/batch")
    public ApiResponse batchUpdatePermission(@Valid @RequestBody List<SysPermissionUpdateRTO> updateParamList) {
        return ApiResponse.success(iSysPermissionService.batchUpdatePermission(updateParamList));
    }

    @Operation(summary = "批量删除权限")
    @DeleteMapping("/batch")
    public ApiResponse batchDeletePermission(@RequestBody List<String> ids) {
        return ApiResponse.success(iSysPermissionService.batchDeletePermission(ids));
    }

}
