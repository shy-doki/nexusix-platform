package com.shy.nexusix.tenant.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.tenant.rto.SysTenantAddRTO;
import com.shy.nexusix.tenant.rto.SysTenantAssignRTO;
import com.shy.nexusix.tenant.rto.SysTenantQueryRTO;
import com.shy.nexusix.tenant.rto.SysTenantUpdateRTO;
import com.shy.nexusix.tenant.service.ISysTenantService;
import com.shy.nexusix.tenant.vo.SysTenantCommonVO;
import com.shy.nexusix.tenant.vo.SysTenantDetailVO;
import com.shy.nexusix.tenant.vo.SysTenantTreeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    public ApiResponse queryTenantPage(PageCommonRTO page) {
        IPage<SysTenantCommonVO> tenantPage = iSysTenantService.queryTenantPage(page);
        return ApiResponse.success(tenantPage);
    }

    /**
     * <p>
     * 查询租户树形结构
     * </p>
     * <p>
     * 返回所有租户的层级树形结构
     * 需要登录并具备租户查看权限才能访问。
     * </p>
     *
     * @return 分页后的租户列表，包含租户名称、脱敏后的租户编码、联系人、状态等信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限
     * @author shy
     * @since 2026-04-19
     */
    @GetMapping("/tree/list")
    @Operation(summary = "查询租户树形结构列表", description = "返回所有租户的层级树形结构")
    public ApiResponse queryTenantTreeList() {
        List<SysTenantTreeVO> tenantTreeList = iSysTenantService.queryTenantTreeList();
        return ApiResponse.success(tenantTreeList);
    }

    /**
     * <p>
     * 分页查询租户树形结构
     * </p>
     * <p>
     * 返回所有租户的层级树形结构
     * 需要登录并具备租户查看权限才能访问。
     * </p>
     *
     * @param page 分页参数
     * @return 分页后的租户列表，包含租户名称、脱敏后的租户编码、联系人、状态等信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限
     * @author shy
     * @since 2026-04-19
     */
    @GetMapping("/tree/page")
    @Operation(summary = "分页查询租户树形结构", description = "返回所有租户的层级树形结构")
    public ApiResponse queryTenantTreePage(PageCommonRTO page) {
        IPage<SysTenantTreeVO> tenantTreePage = iSysTenantService.queryTenantTreePage(page);
        return ApiResponse.success(tenantTreePage);
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
     * @param id 租户Id，用于定位要查询的租户节点
     * @return 租户树形结构列表，包含租户名称、脱敏后的租户编码、父租户ID等信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-04-19
     */
    @GetMapping("/tree/{id}")
    @Operation(summary = "查询指定租户树形结构", description = "返回所有租户的层级树形结构")
    public ApiResponse queryTenantTree(@RequestParam String id) {
        SysTenantTreeVO tenantTree = iSysTenantService.queryTenantTree(id);
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
        Integer affectedRows = iSysTenantService.addTenant(addParam);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>
     * 修改租户
     * </p>
     * <p>
     * 修改租户信息，需要登录并具备租户修改权限才能访问。
     * 仅允许修改指定租户的有效配置信息，不允许修改租户唯一标识。
     * </p>
     *
     * @param updateParam 修改租户信息
     * @return 修改结果：true-成功，false-失败
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、租户不存在或修改失败时抛出
     * @author shy
     * @since 2026-04-20
     */
    @PutMapping("/update")
    @Operation(summary = "修改租户", description = "修改租户信息")
    public ApiResponse updateTenant(@RequestBody SysTenantUpdateRTO updateParam) {
        Integer affectedRows = iSysTenantService.updateTenant(updateParam);
        return  ApiResponse.success(affectedRows);
    }

    /**
     * <p>
     * 删除租户
     * </p>
     * <p>
     * 删除指定租户信息，需要登录并具备租户删除权限才能访问。
     * 删除操作不可逆，删除后租户相关数据将同步清理。
     * </p>
     *
     * @param id 租户ID
     * @return 删除结果：true-成功，false-失败
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、租户不存在或删除失败时抛出
     * @author shy
     * @since 2026-04-20
     */
    @DeleteMapping("/delete")
    @Operation(summary = "删除租户", description = "删除租户信息")
    public ApiResponse deleteTenant(@RequestParam @Valid String id) {
        Integer affectedRows = iSysTenantService.deleteTenant(id);
        return  ApiResponse.success(affectedRows);
    }

    /**
     * <p>
     * 批量新增租户
     * </p>
     * <p>
     * 批量新增多个租户信息，需要登录并具备租户新增权限才能访问。
     * 批量操作支持事务回滚，任一租户新增失败则全部失败。
     * </p>
     *
     * @param addParamList 批量新增租户信息集合
     * @return 成功新增的租户ID集合
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、参数校验失败或新增失败时抛出
     * @author shy
     * @since 2026-04-20
     */
    @PostMapping("/batch")
    @Operation(summary = "批量新增租户", description = "批量新增租户信息")
    public ApiResponse batchAddTenant(@RequestBody List<SysTenantAddRTO> addParamList) {
        Integer affectedRows = iSysTenantService.batchAddTenant(addParamList);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>
     * 批量修改租户
     * </p>
     * <p>
     * 批量修改多个租户信息，需要登录并具备租户修改权限才能访问。
     * 仅允许修改指定租户的有效配置信息，不允许修改租户唯一标识。
     * </p>
     *
     * @param updateParamList 批量修改租户信息集合
     * @return 修改结果：true-全部成功，false-部分/全部失败
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、租户不存在或修改失败时抛出
     * @author shy
     * @since 2026-04-20
     */
    @PutMapping("/batch")
    @Operation(summary = "批量修改租户", description = "批量修改租户信息")
    public ApiResponse batchUpdateTenant(@RequestBody List<SysTenantUpdateRTO> updateParamList) {
        Integer affectedRows = iSysTenantService.batchUpdateTenant(updateParamList);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>
     * 批量删除租户
     * </p>
     * <p>
     * 批量删除多个指定租户信息，需要登录并具备租户删除权限才能访问。
     * 删除操作不可逆，删除后租户相关数据将同步清理。
     * </p>
     *
     * @param ids 租户ID集合
     * @return 删除结果：true-全部成功，false-部分/全部失败
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、租户不存在或删除失败时抛出
     * @author shy
     * @since 2026-04-20
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除租户", description = "批量删除租户信息")
    public ApiResponse batchDeleteTenant(List<String> ids) {
        Integer affectedRows =  iSysTenantService.batchDeleteTenant(ids);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>
     * 分配子租户
     * </p>
     * <p>
     * 为指定父租户分配一个新的子租户，自动处理层级关系和ancestors字段更新。
     * 需要登录并具备租户分配权限才能访问。
     * </p>
     *
     * @param assignParam 子租户分配参数
     * @return 更新子租户行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、父租户不存在或分配失败时抛出
     * @author shy
     * @since 2026-05-04
     */
    @PostMapping("/assign/sub")
    @Operation(summary = "分配子租户", description = "为指定父租户分配子租户，自动处理层级关系")
    public ApiResponse assignSubTenant(@RequestBody @Valid SysTenantAssignRTO assignParam) {
        Integer affectedRows = iSysTenantService.assignSubTenant(assignParam);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>
     * 分配父租户
     * </p>
     * <p>
     * 为指定租户分配一个新的父租户，处理层级关系调整及数据关联更新。
     * 会进行循环层级验证，避免形成环状结构。
     * 需要登录并具备租户分配权限才能访问。
     * </p>
     *
     * @param assignParam 父租户分配参数
     * @return 更新子租户行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、参数非法或分配失败时抛出
     * @author shy
     * @since 2026-05-04
     */
    @PutMapping("/assign/parent")
    @Operation(summary = "分配父租户", description = "为指定租户分配父租户，处理层级调整及数据关联更新")
    public ApiResponse assignParentTenant(@RequestBody @Valid SysTenantAssignRTO assignParam) {
        Integer affectedRows = iSysTenantService.assignParentTenant(assignParam);
        return ApiResponse.success(affectedRows);
    }

}
