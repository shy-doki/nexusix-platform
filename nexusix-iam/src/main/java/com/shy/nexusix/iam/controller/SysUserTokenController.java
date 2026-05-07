package com.shy.nexusix.iam.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.iam.rto.SysUserTokenAddRTO;
import com.shy.nexusix.iam.rto.SysUserTokenQueryRTO;
import com.shy.nexusix.iam.rto.SysUserTokenUpdateRTO;
import com.shy.nexusix.iam.service.ISysUserTokenService;
import com.shy.nexusix.iam.vo.SysUserTokenCommonVO;
import com.shy.nexusix.iam.vo.SysUserTokenDetailVO;
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
 * 用户 Token 记录表 - 用于多端登录管理和强制下线 前端控制器
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@RestController
@RequestMapping("/sys-user-token")
@Tag(name = "用户Token管理", description = "用户Token记录管理相关接口")
@Validated
public class SysUserTokenController {

    @Autowired
    private ISysUserTokenService iSysUserTokenService;

    @GetMapping("/list")
    @Operation(summary = "查询Token列表", description = "返回所有用户Token列表")
    public ApiResponse queryTokenList() {
        List<SysUserTokenCommonVO> tokenList = iSysUserTokenService.queryTokenList();
        return ApiResponse.success(tokenList);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询Token列表", description = "返回分页后的用户Token列表")
    public ApiResponse queryTokenPage(@Valid PageCommonRTO page) {
        IPage<SysUserTokenCommonVO> tokenPage = iSysUserTokenService.queryTokenPage(page);
        return ApiResponse.success(tokenPage);
    }

    @PostMapping("/query")
    @Operation(summary = "条件查询Token列表", description = "返回满足条件的用户Token列表")
    public ApiResponse queryToken(@Valid @RequestBody SysUserTokenQueryRTO queryParam) {
        IPage<SysUserTokenCommonVO> tokenPage = iSysUserTokenService.queryToken(queryParam);
        return ApiResponse.success(tokenPage);
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "查询Token详情", description = "返回指定用户Token的详情信息")
    public ApiResponse queryTokenDetail(@NotBlank(message = "Token记录ID不能为空") @PathVariable String id) {
        SysUserTokenDetailVO tokenDetail = iSysUserTokenService.queryTokenDetail(id);
        return ApiResponse.success(tokenDetail);
    }

    @PostMapping("/add")
    @Operation(summary = "新增Token", description = "新增用户Token记录")
    public ApiResponse addToken(@Valid @RequestBody SysUserTokenAddRTO addParam) {
        Integer affectedRows = iSysUserTokenService.addToken(addParam);
        return ApiResponse.success(affectedRows);
    }

    @PutMapping("/update")
    @Operation(summary = "修改Token", description = "修改用户Token记录信息")
    public ApiResponse updateToken(@Valid @RequestBody SysUserTokenUpdateRTO updateParam) {
        Integer affectedRows = iSysUserTokenService.updateToken(updateParam);
        return ApiResponse.success(affectedRows);
    }

    @PutMapping("/status")
    @Operation(summary = "更新Token状态", description = "更新指定用户Token的状态（有效/无效）")
    public ApiResponse updateTokenStatus(@NotBlank(message = "Token记录ID不能为空") @RequestParam String id,
                                          @NotBlank(message = "状态不能为空") @RequestParam String status) {
        Integer affectedRows = iSysUserTokenService.updateTokenStatus(id, status);
        return ApiResponse.success(affectedRows);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除Token", description = "删除用户Token记录")
    public ApiResponse deleteToken(@NotBlank(message = "Token记录ID不能为空") @RequestParam String id) {
        Integer affectedRows = iSysUserTokenService.deleteToken(id);
        return ApiResponse.success(affectedRows);
    }

    @PostMapping("/batch")
    @Operation(summary = "批量新增Token", description = "批量新增用户Token记录")
    public ApiResponse batchAddToken(@Valid @NotEmpty @RequestBody List<SysUserTokenAddRTO> addParamList) {
        Integer affectedRows = iSysUserTokenService.batchAddToken(addParamList);
        return ApiResponse.success(affectedRows);
    }

    @PutMapping("/batch")
    @Operation(summary = "批量修改Token", description = "批量修改用户Token记录")
    public ApiResponse batchUpdateToken(@Valid @RequestBody List<SysUserTokenUpdateRTO> updateParamList) {
        Integer affectedRows = iSysUserTokenService.batchUpdateToken(updateParamList);
        return ApiResponse.success(affectedRows);
    }

    @PutMapping("/status/batch")
    @Operation(summary = "批量更新Token状态", description = "批量更新多个指定用户Token的状态（有效/无效）")
    public ApiResponse batchUpdateTokenStatus(@NotEmpty(message = "Token记录ID集合不能为空") @RequestBody List<String> ids,
                                               @NotBlank(message = "状态不能为空") @RequestParam String status) {
        Integer affectedRows = iSysUserTokenService.batchUpdateTokenStatus(ids, status);
        return ApiResponse.success(affectedRows);
    }

    @DeleteMapping("/batch")
    @Operation(summary = "批量删除Token", description = "批量删除用户Token记录")
    public ApiResponse batchDeleteToken(@NotEmpty(message = "Token记录ID集合不能为空") @RequestBody List<String> ids) {
        Integer affectedRows = iSysUserTokenService.batchDeleteToken(ids);
        return ApiResponse.success(affectedRows);
    }

}
