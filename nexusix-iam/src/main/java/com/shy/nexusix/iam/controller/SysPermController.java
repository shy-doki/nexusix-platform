package com.shy.nexusix.iam.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.iam.rto.SysPermAddRTO;
import com.shy.nexusix.iam.rto.SysPermQueryRTO;
import com.shy.nexusix.iam.rto.SysPermUpdateRTO;
import com.shy.nexusix.iam.service.ISysPermService;
import com.shy.nexusix.iam.vo.SysPermCommonVO;
import com.shy.nexusix.iam.vo.SysPermDetailVO;
import com.shy.nexusix.iam.vo.SysPermTreeVO;
import com.shy.nexusix.common.exception.BusinessException;
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
 * <p>权限管理控制器</p>
 *
 * @author shy
 */
@RestController
@RequestMapping("/perm")
@Tag(name = "权限管理", description = "权限管理相关接口")
@Validated
public class SysPermController {

    @Autowired
    private ISysPermService iSysPermService;

    /**
     * <p>查询权限列表</p>
     *
     * @return 权限列表
     * @throws BusinessException 无权限或查询失败时抛出
     */
    @GetMapping("/list")
    @Operation(summary = "查询权限列表", description = "返回所有权限列表")
    public ApiResponse queryPermList() {
        List<SysPermCommonVO> permList = iSysPermService.queryPermList();
        return ApiResponse.success(permList);
    }

    /**
     * <p>分页查询权限列表</p>
     *
     * @param page 分页参数
     * @return 分页后的权限列表
     * @throws BusinessException 无权限时抛出
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询权限列表", description = "返回分页后的权限列表")
    public ApiResponse queryPermPage(@Valid PageCommonRTO page) {
        IPage<SysPermCommonVO> permPage = iSysPermService.queryPermPage(page);
        return ApiResponse.success(permPage);
    }

    /**
     * <p>查询权限树形结构列表</p>
     *
     * @return 权限树形列表
     * @throws BusinessException 无权限时抛出
     */
    @GetMapping("/tree/list")
    @Operation(summary = "查询权限树形结构列表", description = "返回所有权限的层级树形结构")
    public ApiResponse queryPermTreeList() {
        List<SysPermTreeVO> permTreeList = iSysPermService.queryPermTreeList();
        return ApiResponse.success(permTreeList);
    }

    /**
     * <p>分页查询权限树形结构</p>
     *
     * @param page 分页参数
     * @return 分页后的权限树形列表
     * @throws BusinessException 无权限时抛出
     */
    @GetMapping("/tree/page")
    @Operation(summary = "分页查询权限树形结构", description = "返回分页后的权限树形结构")
    public ApiResponse queryPermTreePage(@Valid PageCommonRTO page) {
        IPage<SysPermTreeVO> permTreePage = iSysPermService.queryPermTreePage(page);
        return ApiResponse.success(permTreePage);
    }

    /**
     * <p>查询指定权限的树形结构</p>
     *
     * @param id 权限ID
     * @return 权限树形结构
     * @throws BusinessException 无权限或查询失败时抛出
     */
    @GetMapping("/tree/{id}")
    @Operation(summary = "查询指定权限树形结构", description = "返回指定权限的层级树形结构")
    public ApiResponse queryPermTree(@NotBlank(message = "Id不能为空") @PathVariable String id) {
        SysPermTreeVO permTree = iSysPermService.queryPermTree(id);
        return ApiResponse.success(permTree);
    }

    /**
     * <p>条件查询权限列表</p>
     *
     * @param queryParam 查询条件
     * @return 满足条件的权限分页列表
     * @throws BusinessException 无权限或查询失败时抛出
     */
    @PostMapping("/query")
    @Operation(summary = "条件查询权限列表", description = "返回满足条件的权限列表")
    public ApiResponse queryPerm(@Valid @RequestBody SysPermQueryRTO queryParam) {
        IPage<SysPermCommonVO> permPage = iSysPermService.queryPerm(queryParam);
        return ApiResponse.success(permPage);
    }

    /**
     * <p>查询权限详情</p>
     *
     * @param permCode 权限编码
     * @return 权限详情信息
     * @throws BusinessException 无权限或查询失败时抛出
     */
    @GetMapping("/detail/{permCode}")
    @Operation(summary = "查询权限详情", description = "返回指定权限的详情信息")
    public ApiResponse queryPermDetail(@NotBlank(message = "权限编码不能为空") @PathVariable String permCode) {
        SysPermDetailVO permDetail = iSysPermService.queryPermDetail(permCode);
        return ApiResponse.success(permDetail);
    }

    /**
     * <p>新增权限</p>
     *
     * @param addParam 新增权限信息
     * @return 新增结果行数
     * @throws BusinessException 无权限或新增失败时抛出
     */
    @PostMapping("/add")
    @Operation(summary = "新增权限", description = "新增权限信息")
    public ApiResponse addPerm(@Valid @RequestBody SysPermAddRTO addParam) {
        Integer affectedRows = iSysPermService.addPerm(addParam);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>修改权限</p>
     *
     * @param updateParam 修改权限信息
     * @return 修改结果行数
     * @throws BusinessException 无权限、权限不存在或修改失败时抛出
     */
    @PutMapping("/update")
    @Operation(summary = "修改权限", description = "修改权限信息")
    public ApiResponse updatePerm(@Valid @RequestBody SysPermUpdateRTO updateParam) {
        Integer affectedRows = iSysPermService.updatePerm(updateParam);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>更新权限状态</p>
     *
     * @param id 权限ID
     * @param status 权限状态
     * @return 更新结果行数
     * @throws BusinessException 无权限、权限不存在或更新失败时抛出
     */
    @PutMapping("/status")
    @Operation(summary = "更新权限状态", description = "更新指定权限的状态（正常/冻结）")
    public ApiResponse updatePermStatus(@NotBlank(message = "Id不能为空") @RequestParam String id,
                                        @NotBlank(message = "状态不能为空") @RequestParam String status) {
        Integer affectedRows = iSysPermService.updatePermStatus(id, status);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>删除权限</p>
     *
     * @param id 权限ID
     * @return 删除结果行数
     * @throws BusinessException 无权限、权限不存在或删除失败时抛出
     */
    @DeleteMapping("/delete")
    @Operation(summary = "删除权限", description = "删除权限信息")
    public ApiResponse deletePerm(@NotBlank(message = "Id不能为空") @RequestParam String id) {
        Integer affectedRows = iSysPermService.deletePerm(id);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>批量新增权限</p>
     *
     * @param addParamList 批量新增权限信息集合
     * @return 新增结果行数
     * @throws BusinessException 无权限、参数校验失败或新增失败时抛出
     */
    @PostMapping("/batch")
    @Operation(summary = "批量新增权限", description = "批量新增权限信息")
    public ApiResponse batchAddPerm(@Valid @NotEmpty @RequestBody List<SysPermAddRTO> addParamList) {
        Integer affectedRows = iSysPermService.batchAddPerm(addParamList);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>批量修改权限</p>
     *
     * @param updateParamList 批量修改权限信息集合
     * @return 修改结果行数
     * @throws BusinessException 无权限、权限不存在或修改失败时抛出
     */
    @PutMapping("/batch")
    @Operation(summary = "批量修改权限", description = "批量修改权限信息")
    public ApiResponse batchUpdatePerm(@Valid @RequestBody List<SysPermUpdateRTO> updateParamList) {
        Integer affectedRows = iSysPermService.batchUpdatePerm(updateParamList);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>批量更新权限状态</p>
     *
     * @param ids 权限ID集合
     * @param status 权限状态
     * @return 更新结果行数
     * @throws BusinessException 无权限、权限不存在或更新失败时抛出
     */
    @PutMapping("/status/batch")
    @Operation(summary = "批量更新权限状态", description = "批量更新多个指定权限的状态（正常/冻结）")
    public ApiResponse batchUpdatePermStatus(@NotEmpty(message = "权限ID集合不能为空") @RequestBody List<String> ids,
                                             @NotBlank(message = "状态不能为空") @RequestParam String status) {
        Integer affectedRows = iSysPermService.batchUpdatePermStatus(ids, status);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>批量删除权限</p>
     *
     * @param ids 权限ID集合
     * @return 删除结果行数
     * @throws BusinessException 无权限、权限不存在或删除失败时抛出
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除权限", description = "批量删除权限信息")
    public ApiResponse batchDeletePerm(@NotEmpty(message = "权限ID集合不能为空") @RequestBody List<String> ids) {
        Integer affectedRows = iSysPermService.batchDeletePerm(ids);
        return ApiResponse.success(affectedRows);
    }

}
