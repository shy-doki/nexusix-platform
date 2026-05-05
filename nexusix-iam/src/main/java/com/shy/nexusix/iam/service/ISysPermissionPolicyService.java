package com.shy.nexusix.iam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shy.nexusix.iam.entity.SysPermissionPolicy;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shy.nexusix.iam.rto.SysPermissionPolicyAddRTO;
import com.shy.nexusix.iam.rto.SysPermissionPolicyQueryRTO;
import com.shy.nexusix.iam.rto.SysPermissionPolicyUpdateRTO;
import com.shy.nexusix.iam.rto.SysRolePermissionAssignRTO;
import com.shy.nexusix.iam.vo.SysPermissionPolicyCommonVO;
import com.shy.nexusix.iam.vo.SysPermissionPolicyDetailVO;
import com.shy.nexusix.iam.vo.SysPermissionTreeVO;

import java.util.List;

/**
 * <p>
 * 权限策略控制表 - 服务接口
 * </p>
 * <p>
 * 提供四层权限模型（系统→租户→角色→用户）的策略管理、
 * 角色权限分配、用户权限聚合查询与权限校验等核心能力。
 * </p>
 *
 * @author shy
 * @since 2026-05-05
 */
public interface ISysPermissionPolicyService extends IService<SysPermissionPolicy> {

    /**
     * 查询权限策略列表
     *
     * @return 权限策略公共视图对象列表（含关联权限名称和标识）
     */
    List<SysPermissionPolicyCommonVO> queryPolicyList();

    /**
     * 分页查询权限策略
     *
     * @param page 分页参数
     * @return 分页后的权限策略公共视图对象
     */
    IPage<SysPermissionPolicyCommonVO> queryPolicyPage(com.shy.nexusix.common.rto.PageCommonRTO page);

    /**
     * 条件查询权限策略
     *
     * @param queryParam 查询条件（支持目标类型、目标ID、权限ID、动作筛选）
     * @return 分页后的权限策略公共视图对象
     */
    IPage<SysPermissionPolicyCommonVO> queryPolicy(SysPermissionPolicyQueryRTO queryParam);

    /**
     * 查询权限策略详情
     *
     * @param id 策略ID
     * @return 权限策略详情视图对象
     * @throws com.shy.nexusix.common.exception.BusinessException 当策略不存在时抛出
     */
    SysPermissionPolicyDetailVO queryPolicyDetail(String id);

    /**
     * 新增权限策略
     * <p>
     * 会校验目标角色是否存在、关联权限是否存在
     * </p>
     *
     * @param addParam 新增参数
     * @return 影响行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当目标角色或关联权限不存在时抛出
     */
    Integer addPolicy(SysPermissionPolicyAddRTO addParam);

    /**
     * 修改权限策略
     *
     * @param updateParam 修改参数
     * @return 影响行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当策略不存在或关联数据无效时抛出
     */
    Integer updatePolicy(SysPermissionPolicyUpdateRTO updateParam);

    /**
     * 删除权限策略
     * <p>
     * 逻辑删除
     * </p>
     *
     * @param id 策略ID
     * @return 影响行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当策略不存在时抛出
     */
    Integer deletePolicy(String id);

    /**
     * 批量新增权限策略
     * <p>
     * 单次上限100条，会校验目标有效性
     * </p>
     *
     * @param addParamList 新增参数列表
     * @return 影响行数
     */
    Integer batchAddPolicy(List<SysPermissionPolicyAddRTO> addParamList);

    /**
     * 批量删除权限策略
     * <p>
     * 单次上限100条，逻辑删除
     * </p>
     *
     * @param ids 策略ID列表
     * @return 影响行数
     */
    Integer batchDeletePolicy(List<String> ids);

    /**
     * 分配角色权限
     * <p>
     * 采用先清后写模式：先逻辑删除该角色的所有已有策略，再批量新增新策略。
     * </p>
     *
     * @param assignParam 角色权限分配参数
     * @return 新增策略数量
     * @throws com.shy.nexusix.common.exception.BusinessException 当角色不存在时抛出
     */
    Integer assignRolePermission(SysRolePermissionAssignRTO assignParam);

    /**
     * 查询角色已分配的权限树形列表
     *
     * @param roleId 角色ID
     * @return 权限树形视图对象列表
     */
    List<SysPermissionTreeVO> queryPermissionsByRoleId(Long roleId);

    /**
     * 查询用户权限标识列表
     * <p>
     * 聚合四层权限策略（系统→租户→角色→用户），DENY优先级高于ALLOW
     * </p>
     *
     * @param userId   用户ID
     * @param tenantId 租户ID（可为null）
     * @return 权限标识编码列表
     */
    List<String> queryPermissionCodesByUserId(Long userId, Long tenantId);

    /**
     * 校验用户是否拥有指定权限
     *
     * @param userId   用户ID
     * @param tenantId 租户ID
     * @param permCode 权限标识编码
     * @return true-拥有权限，false-无权限
     */
    boolean checkPermission(Long userId, Long tenantId, String permCode);

}
