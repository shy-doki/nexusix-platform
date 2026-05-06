package com.shy.nexusix.iam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.iam.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shy.nexusix.iam.rto.SysUserAddRTO;
import com.shy.nexusix.iam.rto.SysUserQueryRTO;
import com.shy.nexusix.iam.rto.SysUserUpdateRTO;
import com.shy.nexusix.iam.vo.SysUserCommonVO;
import com.shy.nexusix.iam.vo.SysUserDetailVO;

import java.util.List;

/**
 * <p>
 * 用户基础表 - 存储全局用户信息 (不区分租户) 服务类
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * <p>
     * 查询用户列表
     * </p>
     * <p>
     * 返回所有用户的平铺列表。
     * 需要登录并具备用户查看权限才能访问。
     * </p>
     *
     * @return 用户列表
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    List<SysUserCommonVO> queryUserList();

    /**
     * <p>
     * 分页查询用户列表
     * </p>
     * <p>
     * 返回分页后的用户列表。
     * 需要登录并具备用户查看权限才能访问。
     * </p>
     *
     * @param page 分页参数
     * @return 分页后的用户列表
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限
     * @author shy
     * @since 2026-05-06
     */
    IPage<SysUserCommonVO> queryUserPage(PageCommonRTO page);

    /**
     * <p>
     * 条件查询\筛选用户列表
     * </p>
     * <p>
     * 返回满足条件的用户列表。
     * 需要登录并具备用户条件查询权限才能访问。
     * </p>
     *
     * @param queryParam 查询条件
     * @return 满足条件的用户列表
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    IPage<SysUserCommonVO> queryUser(SysUserQueryRTO queryParam);

    /**
     * <p>
     * 查询用户详情
     * </p>
     * <p>
     * 返回指定用户的详情信息。
     * 需要登录并具备用户详情查询权限才能访问。
     * </p>
     *
     * @param username 用户名
     * @return 用户详情信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    SysUserDetailVO queryUserDetail(String username);

    /**
     * <p>
     * 新增用户
     * </p>
     * <p>
     * 新增用户信息，需要登录并具备用户新增权限才能访问。
     * 密码将进行加密处理后存储。
     * </p>
     *
     * @param addParam 新增用户信息
     * @return 新增影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或新增失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    Integer addUser(SysUserAddRTO addParam);

    /**
     * <p>
     * 修改用户
     * </p>
     * <p>
     * 修改用户信息，需要登录并具备用户修改权限才能访问。
     * 仅允许修改指定用户的有效配置信息，不允许修改用户唯一标识。
     * 密码为空则不修改，不为空则加密后更新。
     * </p>
     *
     * @param updateParam 修改用户信息
     * @return 修改影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、用户不存在或修改失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    Integer updateUser(SysUserUpdateRTO updateParam);

    /**
     * <p>
     * 更新用户状态
     * </p>
     * <p>
     * 更新指定用户的状态（启用/禁用），禁用后该用户将无法登录。
     * 需要登录并具备用户修改权限才能访问。
     * </p>
     *
     * @param id 用户ID
     * @param status 用户状态（启用/禁用）
     * @return 更新结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、用户不存在或更新失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    Integer updateUserStatus(String id, String status);

    /**
     * <p>
     * 删除用户
     * </p>
     * <p>
     * 删除指定用户信息，需要登录并具备用户删除权限才能访问。
     * 删除操作不可逆，删除后用户相关数据将同步清理。
     * </p>
     *
     * @param id 用户ID
     * @return 删除影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、用户不存在或删除失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    Integer deleteUser(String id);

    /**
     * <p>
     * 批量新增用户
     * </p>
     * <p>
     * 批量新增多个用户信息，需要登录并具备用户新增权限才能访问。
     * 批量操作支持事务回滚，任一用户新增失败则全部失败。
     * </p>
     *
     * @param addParamList 批量新增用户信息集合
     * @return 新增影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、参数校验失败或新增失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    Integer batchAddUser(List<SysUserAddRTO> addParamList);

    /**
     * <p>
     * 批量修改用户
     * </p>
     * <p>
     * 批量修改多个用户信息，需要登录并具备用户修改权限才能访问。
     * </p>
     *
     * @param updateParamList 批量修改用户信息集合
     * @return 修改影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、用户不存在或修改失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    Integer batchUpdateUser(List<SysUserUpdateRTO> updateParamList);

    /**
     * <p>
     * 批量更新用户状态
     * </p>
     * <p>
     * 批量更新多个指定用户的状态（启用/禁用），禁用后该用户将无法登录。
     * 批量操作支持事务回滚，任一用户更新失败则全部失败。
     * 需要登录并具备用户修改权限才能访问。
     * </p>
     *
     * @param ids 用户ID集合
     * @param status 用户状态（启用/禁用）
     * @return 更新结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、用户不存在或更新失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    Integer batchUpdateUserStatus(List<String> ids, String status);

    /**
     * <p>
     * 批量删除用户
     * </p>
     * <p>
     * 批量删除多个指定用户信息，需要登录并具备用户删除权限才能访问。
     * </p>
     *
     * @param ids 用户ID集合
     * @return 删除影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、用户不存在或删除失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    Integer batchDeleteUser(List<String> ids);

}
