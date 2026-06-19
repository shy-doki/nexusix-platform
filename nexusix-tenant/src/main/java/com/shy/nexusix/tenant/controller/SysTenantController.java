package com.shy.nexusix.tenant.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.tenant.rto.SysTenantAddRTO;
import com.shy.nexusix.tenant.rto.SysTenantAssignRTO;
import com.shy.nexusix.tenant.rto.SysTenantQueryRTO;
import com.shy.nexusix.tenant.rto.SysTenantRegisterRTO;
import com.shy.nexusix.tenant.rto.SysTenantReviewRTO;
import com.shy.nexusix.tenant.rto.SysTenantSwitchRTO;
import com.shy.nexusix.tenant.rto.SysTenantUpdateRTO;
import com.shy.nexusix.tenant.service.ISysTenantService;
import com.shy.nexusix.tenant.vo.SysTenantCommonVO;
import com.shy.nexusix.tenant.vo.SysTenantDetailVO;
import com.shy.nexusix.tenant.vo.SysTenantTreeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.shy.nexusix.common.exception.BusinessException;

import java.util.List;

/**
 * <p>租户管理控制器</p>
 *
 * @author shy
 */
@RestController
@RequestMapping("/tenant")
@Tag(name = "租户管理", description = "租户基础信息管理相关接口")
@Validated
public class SysTenantController {

    @Autowired
    private ISysTenantService iSysTenantService;

    /**
     * <p>查询租户列表</p>
     *
     * @return 租户列表
     * @throws BusinessException 无权限或查询失败时抛出
     */
    @GetMapping("/list")
    @Operation(summary = "查询租户列表", description = "返回所有租户列表")
    public ApiResponse queryTenantList() {
        List<SysTenantCommonVO> tenantList = iSysTenantService.queryTenantList();
        return ApiResponse.success(tenantList);
    }

    /**
     * <p>分页查询租户列表</p>
     *
     * @param page 分页参数
     * @return 分页后的租户列表
     * @throws BusinessException 无权限时抛出
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询租户列表", description = "返回分页后的租户列表")
    public ApiResponse queryTenantPage(@Valid PageCommonRTO page) {
        IPage<SysTenantCommonVO> tenantPage = iSysTenantService.queryTenantPage(page);
        return ApiResponse.success(tenantPage);
    }

    /**
     * <p>查询租户树形结构</p>
     *
     * @return 租户树形列表
     * @throws BusinessException 无权限时抛出
     */
    @GetMapping("/tree/list")
    @Operation(summary = "查询租户树形结构列表", description = "返回所有租户的层级树形结构")
    public ApiResponse queryTenantTreeList() {
        List<SysTenantTreeVO> tenantTreeList = iSysTenantService.queryTenantTreeList();
        return ApiResponse.success(tenantTreeList);
    }

    /**
     * <p>分页查询租户树形结构</p>
     *
     * @param page 分页参数
     * @return 分页后的租户树形列表
     * @throws BusinessException 无权限时抛出
     */
    @GetMapping("/tree/page")
    @Operation(summary = "分页查询租户树形结构", description = "返回所有租户的层级树形结构")
    public ApiResponse queryTenantTreePage(@Valid PageCommonRTO page) {
        IPage<SysTenantTreeVO> tenantTreePage = iSysTenantService.queryTenantTreePage(page);
        return ApiResponse.success(tenantTreePage);
    }

    /**
     * <p>查询指定租户的树形结构</p>
     *
     * @param id 租户ID
     * @return 租户树形结构
     * @throws BusinessException 无权限或查询失败时抛出
     */
    @GetMapping("/tree/{id}")
    @Operation(summary = "查询指定租户树形结构", description = "返回所有租户的层级树形结构")
    public ApiResponse queryTenantTree(@NotBlank(message = "Id不能为空") @PathVariable String id) {
        SysTenantTreeVO tenantTree = iSysTenantService.queryTenantTree(id);
        return ApiResponse.success(tenantTree);
    }

    /**
     * <p>条件查询租户列表</p>
     *
     * @param queryParam 查询条件
     * @return 满足条件的租户分页列表
     * @throws BusinessException 无权限或查询失败时抛出
     */
    @PostMapping("/query")
    @Operation(summary = "条件查询租户列表", description = "返回满足条件的租户列表")
    public ApiResponse queryTenant(@Valid @RequestBody SysTenantQueryRTO queryParam) {
        IPage<SysTenantCommonVO> tenantPage = iSysTenantService.queryTenant(queryParam);
        return ApiResponse.success(tenantPage);
    }

    /**
     * <p>查询租户详情</p>
     *
     * @param tenantCode 租户编码
     * @return 租户详情信息
     * @throws BusinessException 无权限或查询失败时抛出
     */
    @GetMapping("/detail/{tenantCode}")
    @Operation(summary = "查询租户详情", description = "返回指定租户的详情信息")
    public ApiResponse queryTenantDetail(@NotBlank(message = "租户编码不能为空") @PathVariable String tenantCode) {
        SysTenantDetailVO tenantDetail = iSysTenantService.queryTenantDetail(tenantCode);
        return ApiResponse.success(tenantDetail);
    }

    /**
     * <p>新增租户</p>
     *
     * @param addParam 新增租户信息
     * @return 新增结果行数
     * @throws BusinessException 无权限或新增失败时抛出
     */
    @PostMapping("/add")
    @Operation(summary = "新增租户", description = "新增租户信息")
    public ApiResponse addTenant(@Valid @RequestBody SysTenantAddRTO addParam) {
        Integer affectedRows = iSysTenantService.addTenant(addParam);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>修改租户</p>
     *
     * @param updateParam 修改租户信息
     * @return 修改结果行数
     * @throws BusinessException 无权限、租户不存在或修改失败时抛出
     */
    @PutMapping("/update")
    @Operation(summary = "修改租户", description = "修改租户信息")
    public ApiResponse updateTenant(@Valid @RequestBody SysTenantUpdateRTO updateParam) {
        Integer affectedRows = iSysTenantService.updateTenant(updateParam);
        return  ApiResponse.success(affectedRows);
    }

    /**
     * <p>更新租户状态</p>
     *
     * @param id 租户ID
     * @param status 租户状态
     * @return 更新结果行数
     * @throws BusinessException 无权限、租户不存在或更新失败时抛出
     */
    @PutMapping("/status")
    @Operation(summary = "更新租户状态", description = "更新指定租户的状态（正常/冻结）")
    public ApiResponse updateTenantStatus(@NotBlank(message = "Id不能为空") @RequestParam String id,
                                          @NotBlank(message = "状态不能为空") @RequestParam String status) {
        Integer affectedRows = iSysTenantService.updateTenantStatus(id, status);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>删除租户</p>
     *
     * @param id 租户ID
     * @return 删除结果行数
     * @throws BusinessException 无权限、租户不存在或删除失败时抛出
     */
    @DeleteMapping("/delete")
    @Operation(summary = "删除租户", description = "删除租户信息")
    public ApiResponse deleteTenant(@NotBlank(message = "Id不能为空") @RequestParam String id) {
        Integer affectedRows = iSysTenantService.deleteTenant(id);
        return  ApiResponse.success(affectedRows);
    }

    /**
     * <p>批量新增租户</p>
     *
     * @param addParamList 批量新增租户信息集合
     * @return 新增结果行数
     * @throws BusinessException 无权限、参数校验失败或新增失败时抛出
     */
    @PostMapping("/batch")
    @Operation(summary = "批量新增租户", description = "批量新增租户信息")
    public ApiResponse batchAddTenant(@Valid @NotEmpty @RequestBody List<SysTenantAddRTO> addParamList) {
        Integer affectedRows = iSysTenantService.batchAddTenant(addParamList);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>批量修改租户</p>
     *
     * @param updateParamList 批量修改租户信息集合
     * @return 修改结果行数
     * @throws BusinessException 无权限、租户不存在或修改失败时抛出
     */
    @PutMapping("/batch")
    @Operation(summary = "批量修改租户", description = "批量修改租户信息")
    public ApiResponse batchUpdateTenant(@Valid @RequestBody List<SysTenantUpdateRTO> updateParamList) {
        Integer affectedRows = iSysTenantService.batchUpdateTenant(updateParamList);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>批量更新租户状态</p>
     *
     * @param ids 租户ID集合
     * @param status 租户状态
     * @return 更新结果行数
     * @throws BusinessException 无权限、租户不存在或更新失败时抛出
     */
    @PutMapping("/status/batch")
    @Operation(summary = "批量更新租户状态", description = "批量更新多个指定租户的状态（正常/冻结）")
    public ApiResponse batchUpdateTenantStatus(@NotEmpty(message = "租户ID集合不能为空") @RequestBody List<String> ids,
                                               @NotBlank(message = "状态不能为空") @RequestParam String status) {
        Integer affectedRows = iSysTenantService.batchUpdateTenantStatus(ids, status);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>批量删除租户</p>
     *
     * @param ids 租户ID集合
     * @return 删除结果行数
     * @throws BusinessException 无权限、租户不存在或删除失败时抛出
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除租户", description = "批量删除租户信息")
    public ApiResponse batchDeleteTenant(@NotEmpty List<String> ids) {
        Integer affectedRows =  iSysTenantService.batchDeleteTenant(ids);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>分配子租户</p>
     *
     * @param assignParam 子租户分配参数
     * @return 更新结果行数
     * @throws BusinessException 无权限、父租户不存在或分配失败时抛出
     */
    @PostMapping("/assign/sub")
    @Operation(summary = "分配子租户", description = "为指定父租户分配子租户，自动处理层级关系")
    public ApiResponse assignSubTenant(@Valid @RequestBody SysTenantAssignRTO assignParam) {
        Integer affectedRows = iSysTenantService.assignSubTenant(assignParam);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>分配父租户</p>
     *
     * @param assignParam 父租户分配参数
     * @return 更新结果行数
     * @throws BusinessException 无权限、参数非法或分配失败时抛出
     */
    @PutMapping("/assign/parent")
    @Operation(summary = "分配父租户", description = "为指定租户分配父租户，处理层级调整及数据关联更新")
    public ApiResponse assignParentTenant(@Valid @RequestBody SysTenantAssignRTO assignParam) {
        Integer affectedRows = iSysTenantService.assignParentTenant(assignParam);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>租户自助注册</p>
     *
     * @param registerParam 注册信息
     * @return 新增结果行数
     * @throws BusinessException 注册信息不合法或租户编码已存在时抛出
     */
    @PostMapping("/register")
    @Operation(summary = "租户自助注册", description = "企业用户自助注册租户，注册后需平台审核")
    public ApiResponse registerTenant(@Valid @RequestBody SysTenantRegisterRTO registerParam) {
        Integer affectedRows = iSysTenantService.registerTenant(registerParam);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>审核租户注册</p>
     *
     * @param reviewParam 审核信息
     * @return 审核结果行数
     * @throws BusinessException 租户不存在或状态非PENDING时抛出
     */
    @PutMapping("/review")
    @Operation(summary = "审核租户注册", description = "平台管理员审核租户注册申请")
    public ApiResponse reviewTenant(@Valid @RequestBody SysTenantReviewRTO reviewParam) {
        Integer affectedRows = iSysTenantService.reviewTenant(reviewParam);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>切换租户</p>
     *
     * @param switchParam 切换参数
     * @return 切换后的租户信息
     * @throws BusinessException 用户不属于目标租户或租户状态异常时抛出
     */
    @PostMapping("/switch")
    @Operation(summary = "切换租户", description = "切换当前用户的工作租户上下文")
    public ApiResponse switchTenant(@Valid @RequestBody SysTenantSwitchRTO switchParam) {
        SysTenantCommonVO tenantInfo = iSysTenantService.switchTenant(switchParam);
        return ApiResponse.success(tenantInfo);
    }

}
