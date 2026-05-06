package com.shy.nexusix.iam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.iam.entity.SysPermissionPolicy;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shy.nexusix.iam.rto.SysPermissionPolicyAddRTO;
import com.shy.nexusix.iam.rto.SysPermissionPolicyQueryRTO;
import com.shy.nexusix.iam.rto.SysPermissionPolicyUpdateRTO;
import com.shy.nexusix.iam.vo.SysPermissionPolicyCommonVO;
import com.shy.nexusix.iam.vo.SysPermissionPolicyDetailVO;

import java.util.List;

/**
 * <p>
 * 权限策略控制表 - 实现四层权限及禁用继承逻辑 服务类
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
public interface ISysPermissionPolicyService extends IService<SysPermissionPolicy> {

    /**
     * <p>
     * 查询权限策略列表
     * </p>
     * <p>
     * 返回所有权限策略的平铺列表。
     * 需要登录并具备权限策略查看权限才能访问。
     * </p>
     *
     * @return 权限策略列表
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    List<SysPermissionPolicyCommonVO> queryPolicyList();

    /**
     * <p>
     * 分页查询权限策略列表
     * </p>
     * <p>
     * 返回分页后的权限策略列表。
     * 需要登录并具备权限策略查看权限才能访问。
     * </p>
     *
     * @param page 分页参数
     * @return 分页后的权限策略列表
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限
     * @author shy
     * @since 2026-05-06
     */
    IPage<SysPermissionPolicyCommonVO> queryPolicyPage(PageCommonRTO page);

    /**
     * <p>
     * 条件查询\筛选权限策略列表
     * </p>
     * <p>
     * 返回满足条件的权限策略列表。
     * 需要登录并具备权限策略条件查询权限才能访问。
     * </p>
     *
     * @param queryParam 查询条件
     * @return 满足条件的权限策略列表
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    IPage<SysPermissionPolicyCommonVO> queryPolicy(SysPermissionPolicyQueryRTO queryParam);

    /**
     * <p>
     * 查询权限策略详情
     * </p>
     * <p>
     * 返回指定权限策略的详情信息。
     * 需要登录并具备权限策略详情查询权限才能访问。
     * </p>
     *
     * @param id 策略ID
     * @return 权限策略详情信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    SysPermissionPolicyDetailVO queryPolicyDetail(String id);

    /**
     * <p>
     * 新增权限策略
     * </p>
     * <p>
     * 新增权限策略信息，需要登录并具备权限策略新增权限才能访问。
     * </p>
     *
     * @param addParam 新增权限策略信息
     * @return 新增影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或新增失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    Integer addPolicy(SysPermissionPolicyAddRTO addParam);

    /**
     * <p>
     * 修改权限策略
     * </p>
     * <p>
     * 修改权限策略信息，需要登录并具备权限策略修改权限才能访问。
     * </p>
     *
     * @param updateParam 修改权限策略信息
     * @return 修改影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、策略不存在或修改失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    Integer updatePolicy(SysPermissionPolicyUpdateRTO updateParam);

    /**
     * <p>
     * 更新权限策略动作
     * </p>
     * <p>
     * 更新指定权限策略的动作（允许/拒绝）。
     * 需要登录并具备权限策略修改权限才能访问。
     * </p>
     *
     * @param id 策略ID
     * @param action 动作（允许/拒绝）
     * @return 更新结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、策略不存在或更新失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    Integer updatePolicyAction(String id, String action);

    /**
     * <p>
     * 删除权限策略
     * </p>
     * <p>
     * 删除指定权限策略信息，需要登录并具备权限策略删除权限才能访问。
     * </p>
     *
     * @param id 策略ID
     * @return 删除影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、策略不存在或删除失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    Integer deletePolicy(String id);

    /**
     * <p>
     * 批量新增权限策略
     * </p>
     * <p>
     * 批量新增多个权限策略信息，需要登录并具备权限策略新增权限才能访问。
     * 批量操作支持事务回滚，任一策略新增失败则全部失败。
     * </p>
     *
     * @param addParamList 批量新增权限策略信息集合
     * @return 新增影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、参数校验失败或新增失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    Integer batchAddPolicy(List<SysPermissionPolicyAddRTO> addParamList);

    /**
     * <p>
     * 批量修改权限策略
     * </p>
     * <p>
     * 批量修改多个权限策略信息，需要登录并具备权限策略修改权限才能访问。
     * </p>
     *
     * @param updateParamList 批量修改权限策略信息集合
     * @return 修改影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、策略不存在或修改失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    Integer batchUpdatePolicy(List<SysPermissionPolicyUpdateRTO> updateParamList);

    /**
     * <p>
     * 批量更新权限策略动作
     * </p>
     * <p>
     * 批量更新多个指定权限策略的动作（允许/拒绝）。
     * 批量操作支持事务回滚，任一策略更新失败则全部失败。
     * 需要登录并具备权限策略修改权限才能访问。
     * </p>
     *
     * @param ids 策略ID集合
     * @param action 动作（允许/拒绝）
     * @return 更新结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、策略不存在或更新失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    Integer batchUpdatePolicyAction(List<String> ids, String action);

    /**
     * <p>
     * 批量删除权限策略
     * </p>
     * <p>
     * 批量删除多个指定权限策略信息，需要登录并具备权限策略删除权限才能访问。
     * </p>
     *
     * @param ids 策略ID集合
     * @return 删除影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、策略不存在或删除失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    Integer batchDeletePolicy(List<String> ids);

}
