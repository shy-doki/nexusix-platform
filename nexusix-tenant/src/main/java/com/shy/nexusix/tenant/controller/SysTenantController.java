package com.shy.nexusix.tenant.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.tenant.rto.SysTenantAddRTO;
import com.shy.nexusix.tenant.rto.SysTenantQueryRTO;
import com.shy.nexusix.tenant.service.ISysTenantService;
import com.shy.nexusix.tenant.vo.SysTenantCommonVO;
import com.shy.nexusix.tenant.vo.SysTenantDetailVO;
import com.shy.nexusix.tenant.vo.SysTenantTreeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 租户信息表 - 存储租户基础信息
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@RestController
@RequestMapping("/tenant")
@Tag(name = "租户管理", description = "租户基础信息管理相关接口")
public class SysTenantController {

    @Autowired
    private ISysTenantService iSysTenantService;

    /**
     * <p>
     * 查询租户列表
     * </p>
     * <p>
     * 返回所有租户的平铺列表，租户编码会自动进行脱敏处理（保留前3位和后3位）。
     * 需要登录并具备租户查看权限才能访问。
     * </p>
     *
     * @return 租户列表，包含租户名称、脱敏后的租户编码、联系人、状态等信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-04-19
     */
    @GetMapping("/list")
    @Operation(summary = "查询租户列表", description = "返回所有租户列表")
    public ApiResponse queryTenantList() {
        List<SysTenantCommonVO> tenantList = iSysTenantService.queryTenantList();
        return ApiResponse.success(tenantList);
    }

    /**
     * <p>
     * 分页查询租户列表
     * </p>
     * <p>
     * 返回分页后的租户列表，租户编码会自动进行脱敏处理（保留前3位和后3位）。
     * 需要登录并具备租户查看权限才能访问。
     * </p>
     *
     * @param page 分页参数
     * @return 分页后的租户列表，包含租户名称、脱敏后的租户编码、联系人、状态等信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限
     * @author shy
     * @since 2026-04-19
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询租户列表", description = "返回分页后的租户列表")
    public ApiResponse queryTenantPage(@RequestParam PageCommonRTO page) {
        IPage<SysTenantCommonVO> tenantPage = iSysTenantService.queryTenantPage(page);
        return ApiResponse.success(tenantPage);
    }

    /**
     * <p>
     * 查询指定租户的树形结构
     * </p>
     * <p>
     * 返回所有租户的层级树形结构，租户编码会自动进行脱敏处理（保留前3位和后3位）。
     * 需要登录并具备租户查看权限才能访问。
     * </p>
     *
     * @param tenantCode 租户编码，用于定位要查询的租户节点
     * @return 租户树形结构列表，包含租户名称、脱敏后的租户编码、父租户ID等信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-04-19
     */
    @GetMapping("/tree/{tenantCode}")
    @Operation(summary = "查询租户树形结构", description = "返回所有租户的层级树形结构，租户编码会自动脱敏显示")
    public ApiResponse queryTenantTree(@RequestParam String tenantCode) {
       List<SysTenantTreeVO> tenantTree = iSysTenantService.queryTenantTree(tenantCode);
       return ApiResponse.success(tenantTree);
    }

    /**
     * <p>
     * 条件查询\筛选租户列表
     * </p>
     * <p>
     * 返回满足条件的租户列表，租户编码会自动进行脱敏处理（保留前3位和后3位）。
     * 需要登录并具备租户条件查询权限才能访问。
     * </p>
     *
     * @param queryParam 查询条件
     * @return 满足条件的租户列表，包含租户名称、脱敏后的租户编码、联系人、状态等信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-04-19
     */
    @PostMapping("/query")
    @Operation(summary = "条件查询租户列表", description = "返回满足条件的租户列表")
    public ApiResponse queryTenant(@RequestBody SysTenantQueryRTO queryParam) {
        IPage<SysTenantCommonVO> tenantPage = iSysTenantService.queryTenant(queryParam);
        return ApiResponse.success(tenantPage);
    }

    /**
     * <p>
     * 查询租户详情
     * </p>
     * <p>
     * 返回指定租户的详情信息，租户敏感会自动进行脱敏处理（保留前3位和后3位）。
     * 需要登录并具备租户详情查询权限才能访问。
     * </p>
     *
     * @param tenantCode 租户编码
     * @return 租户详情信息，包含租户名称、租户编码、联系人、状态等信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-04-19
     */
    @GetMapping("/detail/{tenantCode}")
    @Operation(summary = "查询租户详情", description = "返回指定租户的详情信息")
    public ApiResponse queryTenantDetail(@PathVariable String tenantCode) {
        SysTenantDetailVO tenantDetail = iSysTenantService.queryTenantDetail(tenantCode);
        return ApiResponse.success(tenantDetail);
    }

    /**
     * <p>
     * 新增租户
     * </p>
     * <p>
     * 新增租户信息，需要登录并具备租户新增权限才能访问。
     * </p>
     *
     * @param addParam 新增租户信息
     * @return 新增租户的ID
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或新增失败时抛出
     * @author shy
     * @since 2026-04-19
     */
    @PostMapping("/add")
    @Operation(summary = "新增租户", description = "新增租户信息")
    public ApiResponse addTenant(@RequestBody SysTenantAddRTO addParam) {
        Long tenantId = iSysTenantService.addTenant(addParam);
        return ApiResponse.success(tenantId);
    }

}
