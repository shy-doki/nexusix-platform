package com.shy.nexusix.iam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.iam.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shy.nexusix.iam.rto.SysRoleAddRTO;
import com.shy.nexusix.iam.rto.SysRoleQueryRTO;
import com.shy.nexusix.iam.rto.SysRoleUpdateRTO;
import com.shy.nexusix.iam.vo.SysRoleCommonVO;
import com.shy.nexusix.iam.vo.SysRoleDetailVO;

import java.util.List;

/**
 * <p>
 * 角色表 - 定义系统/租户/用户级角色 服务类
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
public interface ISysRoleService extends IService<SysRole> {

    /**
     * <p>
     * 查询角色列表
     * </p>
     * <p>
     * 返回所有角色的平铺列表。
     * 需要登录并具备角色查看权限才能访问。
     * </p>
     *
     * @return 角色列表
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    List<SysRoleCommonVO> queryRoleList();

    /**
     * <p>
     * 分页查询角色列表
     * </p>
     * <p>
     * 返回分页后的角色列表。
     * 需要登录并具备角色查看权限才能访问。
     * </p>
     *
     * @param page 分页参数
     * @return 分页后的角色列表
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限
     * @author shy
     * @since 2026-05-06
     */
    IPage<SysRoleCommonVO> queryRolePage(PageCommonRTO page);

    /**
     * <p>
     * 条件查询\筛选角色列表
     * </p>
     * <p>
     * 返回满足条件的角色列表。
     * 需要登录并具备角色条件查询权限才能访问。
     * </p>
     *
     * @param queryParam 查询条件
     * @return 满足条件的角色列表
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    IPage<SysRoleCommonVO> queryRole(SysRoleQueryRTO queryParam);

    /**
     * <p>
     * 查询角色详情
     * </p>
     * <p>
     * 返回指定角色的详情信息。
     * 需要登录并具备角色详情查询权限才能访问。
     * </p>
     *
     * @param roleCode 角色编码
     * @return 角色详情信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    SysRoleDetailVO queryRoleDetail(String roleCode);

    /**
     * <p>
     * 新增角色
     * </p>
     * <p>
     * 新增角色信息，需要登录并具备角色新增权限才能访问。
     * </p>
     *
     * @param addParam 新增角色信息
     * @return 新增影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或新增失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    Integer addRole(SysRoleAddRTO addParam);

    /**
     * <p>
     * 修改角色
     * </p>
     * <p>
     * 修改角色信息，需要登录并具备角色修改权限才能访问。
     * 仅允许修改指定角色的有效配置信息，不允许修改角色唯一编码。
     * </p>
     *
     * @param updateParam 修改角色信息
     * @return 修改影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、角色不存在或修改失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    Integer updateRole(SysRoleUpdateRTO updateParam);

    /**
     * <p>
     * 更新角色状态
     * </p>
     * <p>
     * 更新指定角色的状态（启用/禁用），禁用后该角色将不可被分配。
     * 需要登录并具备角色修改权限才能访问。
     * </p>
     *
     * @param id 角色ID
     * @param status 角色状态（启用/禁用）
     * @return 更新结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、角色不存在或更新失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    Integer updateRoleStatus(String id, String status);

    /**
     * <p>
     * 删除角色
     * </p>
     * <p>
     * 删除指定角色信息，需要登录并具备角色删除权限才能访问。
     * 删除操作不可逆，删除后角色相关数据将同步清理。
     * </p>
     *
     * @param id 角色ID
     * @return 删除影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、角色不存在或删除失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    Integer deleteRole(String id);

    /**
     * <p>
     * 批量新增角色
     * </p>
     * <p>
     * 批量新增多个角色信息，需要登录并具备角色新增权限才能访问。
     * 批量操作支持事务回滚，任一角色新增失败则全部失败。
     * </p>
     *
     * @param addParamList 批量新增角色信息集合
     * @return 新增影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、参数校验失败或新增失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    Integer batchAddRole(List<SysRoleAddRTO> addParamList);

    /**
     * <p>
     * 批量修改角色
     * </p>
     * <p>
     * 批量修改多个角色信息，需要登录并具备角色修改权限才能访问。
     * </p>
     *
     * @param updateParamList 批量修改角色信息集合
     * @return 修改影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、角色不存在或修改失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    Integer batchUpdateRole(List<SysRoleUpdateRTO> updateParamList);

    /**
     * <p>
     * 批量更新角色状态
     * </p>
     * <p>
     * 批量更新多个指定角色的状态（启用/禁用），禁用后该角色将不可被分配。
     * 批量操作支持事务回滚，任一角色更新失败则全部失败。
     * 需要登录并具备角色修改权限才能访问。
     * </p>
     *
     * @param ids 角色ID集合
     * @param status 角色状态（启用/禁用）
     * @return 更新结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、角色不存在或更新失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    Integer batchUpdateRoleStatus(List<String> ids, String status);

    /**
     * <p>
     * 批量删除角色
     * </p>
     * <p>
     * 批量删除多个指定角色信息，需要登录并具备角色删除权限才能访问。
     * </p>
     *
     * @param ids 角色ID集合
     * @return 删除影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、角色不存在或删除失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    Integer batchDeleteRole(List<String> ids);

}
