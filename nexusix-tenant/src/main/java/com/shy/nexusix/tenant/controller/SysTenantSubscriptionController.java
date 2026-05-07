package com.shy.nexusix.tenant.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.tenant.rto.SysTenantSubscriptionAddRTO;
import com.shy.nexusix.tenant.rto.SysTenantSubscriptionQueryRTO;
import com.shy.nexusix.tenant.rto.SysTenantSubscriptionUpdateRTO;
import com.shy.nexusix.tenant.service.ISysTenantSubscriptionService;
import com.shy.nexusix.tenant.vo.SysTenantSubscriptionCommonVO;
import com.shy.nexusix.tenant.vo.SysTenantSubscriptionDetailVO;
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
 * 租户套餐订阅表 - 记录租户购买的套餐及订阅状态 前端控制器
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@RestController
@RequestMapping("/sys-tenant-subscription")
@Tag(name = "租户套餐订阅管理", description = "租户套餐订阅信息管理相关接口")
@Validated
public class SysTenantSubscriptionController {

    @Autowired
    private ISysTenantSubscriptionService iSysTenantSubscriptionService;

    @GetMapping("/list")
    @Operation(summary = "查询订阅列表", description = "返回所有租户套餐订阅列表")
    public ApiResponse querySubscriptionList() {
        List<SysTenantSubscriptionCommonVO> subscriptionList = iSysTenantSubscriptionService.querySubscriptionList();
        return ApiResponse.success(subscriptionList);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询订阅列表", description = "返回分页后的租户套餐订阅列表")
    public ApiResponse querySubscriptionPage(@Valid PageCommonRTO page) {
        IPage<SysTenantSubscriptionCommonVO> subscriptionPage = iSysTenantSubscriptionService.querySubscriptionPage(page);
        return ApiResponse.success(subscriptionPage);
    }

    @PostMapping("/query")
    @Operation(summary = "条件查询订阅列表", description = "返回满足条件的租户套餐订阅列表")
    public ApiResponse querySubscription(@Valid @RequestBody SysTenantSubscriptionQueryRTO queryParam) {
        IPage<SysTenantSubscriptionCommonVO> subscriptionPage = iSysTenantSubscriptionService.querySubscription(queryParam);
        return ApiResponse.success(subscriptionPage);
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "查询订阅详情", description = "返回指定租户套餐订阅的详情信息")
    public ApiResponse querySubscriptionDetail(@NotBlank(message = "订阅ID不能为空") @PathVariable String id) {
        SysTenantSubscriptionDetailVO subscriptionDetail = iSysTenantSubscriptionService.querySubscriptionDetail(id);
        return ApiResponse.success(subscriptionDetail);
    }

    @PostMapping("/add")
    @Operation(summary = "新增订阅", description = "新增租户套餐订阅信息")
    public ApiResponse addSubscription(@Valid @RequestBody SysTenantSubscriptionAddRTO addParam) {
        Integer affectedRows = iSysTenantSubscriptionService.addSubscription(addParam);
        return ApiResponse.success(affectedRows);
    }

    @PutMapping("/update")
    @Operation(summary = "修改订阅", description = "修改租户套餐订阅信息")
    public ApiResponse updateSubscription(@Valid @RequestBody SysTenantSubscriptionUpdateRTO updateParam) {
        Integer affectedRows = iSysTenantSubscriptionService.updateSubscription(updateParam);
        return ApiResponse.success(affectedRows);
    }

    @PutMapping("/status")
    @Operation(summary = "更新订阅状态", description = "更新指定租户套餐订阅的状态（生效/过期）")
    public ApiResponse updateSubscriptionStatus(@NotBlank(message = "订阅ID不能为空") @RequestParam String id,
                                                 @NotBlank(message = "状态不能为空") @RequestParam String status) {
        Integer affectedRows = iSysTenantSubscriptionService.updateSubscriptionStatus(id, status);
        return ApiResponse.success(affectedRows);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除订阅", description = "删除租户套餐订阅信息")
    public ApiResponse deleteSubscription(@NotBlank(message = "订阅ID不能为空") @RequestParam String id) {
        Integer affectedRows = iSysTenantSubscriptionService.deleteSubscription(id);
        return ApiResponse.success(affectedRows);
    }

    @PostMapping("/batch")
    @Operation(summary = "批量新增订阅", description = "批量新增租户套餐订阅信息")
    public ApiResponse batchAddSubscription(@Valid @NotEmpty @RequestBody List<SysTenantSubscriptionAddRTO> addParamList) {
        Integer affectedRows = iSysTenantSubscriptionService.batchAddSubscription(addParamList);
        return ApiResponse.success(affectedRows);
    }

    @PutMapping("/batch")
    @Operation(summary = "批量修改订阅", description = "批量修改租户套餐订阅信息")
    public ApiResponse batchUpdateSubscription(@Valid @RequestBody List<SysTenantSubscriptionUpdateRTO> updateParamList) {
        Integer affectedRows = iSysTenantSubscriptionService.batchUpdateSubscription(updateParamList);
        return ApiResponse.success(affectedRows);
    }

    @PutMapping("/status/batch")
    @Operation(summary = "批量更新订阅状态", description = "批量更新多个指定租户套餐订阅的状态（生效/过期）")
    public ApiResponse batchUpdateSubscriptionStatus(@NotEmpty(message = "订阅ID集合不能为空") @RequestBody List<String> ids,
                                                      @NotBlank(message = "状态不能为空") @RequestParam String status) {
        Integer affectedRows = iSysTenantSubscriptionService.batchUpdateSubscriptionStatus(ids, status);
        return ApiResponse.success(affectedRows);
    }

    @DeleteMapping("/batch")
    @Operation(summary = "批量删除订阅", description = "批量删除租户套餐订阅信息")
    public ApiResponse batchDeleteSubscription(@NotEmpty(message = "订阅ID集合不能为空") @RequestBody List<String> ids) {
        Integer affectedRows = iSysTenantSubscriptionService.batchDeleteSubscription(ids);
        return ApiResponse.success(affectedRows);
    }

}
