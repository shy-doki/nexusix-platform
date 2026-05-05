package com.shy.nexusix.iam.controller;

import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.iam.rto.SysPermissionPolicyAddRTO;
import com.shy.nexusix.iam.rto.SysPermissionPolicyQueryRTO;
import com.shy.nexusix.iam.rto.SysPermissionPolicyUpdateRTO;
import com.shy.nexusix.iam.rto.SysRolePermissionAssignRTO;
import com.shy.nexusix.iam.service.ISysPermissionPolicyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sys-permission-policy")
@Tag(name = "权限策略管理", description = "权限策略控制与分配接口")
public class SysPermissionPolicyController {

    @Autowired
    private ISysPermissionPolicyService iSysPermissionPolicyService;

    @Operation(summary = "查询权限策略列表")
    @GetMapping("/list")
    public ApiResponse queryPolicyList() {
        return ApiResponse.success(iSysPermissionPolicyService.queryPolicyList());
    }

    @Operation(summary = "分页查询权限策略")
    @GetMapping("/page")
    public ApiResponse queryPolicyPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageCommonRTO page = new PageCommonRTO();
        page.setPageNum(pageNum);
        page.setPageSize(pageSize);
        return ApiResponse.success(iSysPermissionPolicyService.queryPolicyPage(page));
    }

    @Operation(summary = "条件查询权限策略")
    @PostMapping("/query")
    public ApiResponse queryPolicy(@Valid @RequestBody SysPermissionPolicyQueryRTO queryParam) {
        return ApiResponse.success(iSysPermissionPolicyService.queryPolicy(queryParam));
    }

    @Operation(summary = "查询权限策略详情")
    @GetMapping("/detail/{id}")
    public ApiResponse queryPolicyDetail(@PathVariable String id) {
        return ApiResponse.success(iSysPermissionPolicyService.queryPolicyDetail(id));
    }

    @Operation(summary = "新增权限策略")
    @PostMapping("/add")
    public ApiResponse addPolicy(@Valid @RequestBody SysPermissionPolicyAddRTO addParam) {
        return ApiResponse.success(iSysPermissionPolicyService.addPolicy(addParam));
    }

    @Operation(summary = "修改权限策略")
    @PutMapping("/update")
    public ApiResponse updatePolicy(@Valid @RequestBody SysPermissionPolicyUpdateRTO updateParam) {
        return ApiResponse.success(iSysPermissionPolicyService.updatePolicy(updateParam));
    }

    @Operation(summary = "删除权限策略")
    @DeleteMapping("/delete")
    public ApiResponse deletePolicy(@RequestParam String id) {
        return ApiResponse.success(iSysPermissionPolicyService.deletePolicy(id));
    }

    @Operation(summary = "批量新增权限策略")
    @PostMapping("/batch")
    public ApiResponse batchAddPolicy(@Valid @RequestBody List<SysPermissionPolicyAddRTO> addParamList) {
        return ApiResponse.success(iSysPermissionPolicyService.batchAddPolicy(addParamList));
    }

    @Operation(summary = "批量删除权限策略")
    @DeleteMapping("/batch")
    public ApiResponse batchDeletePolicy(@RequestBody List<String> ids) {
        return ApiResponse.success(iSysPermissionPolicyService.batchDeletePolicy(ids));
    }

    @Operation(summary = "分配角色权限")
    @PostMapping("/assign/role")
    public ApiResponse assignRolePermission(@Valid @RequestBody SysRolePermissionAssignRTO assignParam) {
        return ApiResponse.success(iSysPermissionPolicyService.assignRolePermission(assignParam));
    }

}
