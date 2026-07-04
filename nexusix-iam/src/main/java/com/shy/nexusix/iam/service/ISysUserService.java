package com.shy.nexusix.iam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.iam.entity.SysUser;
import com.shy.nexusix.iam.rto.SysUserAddRTO;
import com.shy.nexusix.iam.rto.SysUserChangePasswordRTO;
import com.shy.nexusix.iam.rto.SysUserQueryRTO;
import com.shy.nexusix.iam.rto.SysUserResetPasswordRTO;
import com.shy.nexusix.iam.rto.SysUserUpdateRTO;
import com.shy.nexusix.iam.vo.SysRoleCommonVO;
import com.shy.nexusix.iam.vo.SysUserCommonVO;
import com.shy.nexusix.iam.vo.SysUserDetailVO;

import java.util.List;

/**
 * <p>系统用户服务接口</p>
 *
 * @author shy
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * <p>查询用户列表</p>
     *
     * @return 用户通用VO列表
     * @throws BusinessException 无权限或查询失败时抛出
     */
    List<SysUserCommonVO> queryUserList();

    /**
     * <p>分页查询用户列表</p>
     *
     * @param page 分页参数
     * @return 分页后的用户通用VO列表
     * @throws BusinessException 无权限时抛出
     */
    IPage<SysUserCommonVO> queryUserPage(PageCommonRTO page);

    /**
     * <p>条件查询用户列表</p>
     *
     * @param queryParam 查询条件
     * @return 满足条件的用户分页列表
     * @throws BusinessException 无权限或查询失败时抛出
     */
    IPage<SysUserCommonVO> queryUser(SysUserQueryRTO queryParam);

    /**
     * <p>查询用户详情</p>
     *
     * @param userCode 用户编码
     * @return 用户详情VO
     * @throws BusinessException 无权限或查询失败时抛出
     */
    SysUserDetailVO queryUserDetail(String userCode);

    /**
     * <p>新增用户</p>
     *
     * @param addParam 新增用户信息
     * @return 新增结果行数
     * @throws BusinessException 无权限或新增失败时抛出
     */
    Integer addUser(SysUserAddRTO addParam);

    /**
     * <p>修改用户</p>
     *
     * @param updateParam 修改用户信息
     * @return 修改结果行数
     * @throws BusinessException 无权限、用户不存在或修改失败时抛出
     */
    Integer updateUser(SysUserUpdateRTO updateParam);

    /**
     * <p>更新用户状态</p>
     *
     * @param id 用户ID
     * @param status 用户状态
     * @return 更新结果行数
     * @throws BusinessException 无权限、用户不存在或更新失败时抛出
     */
    Integer updateUserStatus(String id, String status);

    /**
     * <p>删除用户</p>
     *
     * @param id 用户ID
     * @return 删除结果行数
     * @throws BusinessException 无权限、用户不存在或删除失败时抛出
     */
    Integer deleteUser(String id);

    /**
     * <p>批量新增用户</p>
     *
     * @param addParamList 批量新增用户信息集合
     * @return 新增结果行数
     * @throws BusinessException 无权限、参数校验失败或新增失败时抛出
     */
    Integer batchAddUser(List<SysUserAddRTO> addParamList);

    /**
     * <p>批量修改用户</p>
     *
     * @param updateParamList 批量修改用户信息集合
     * @return 修改结果行数
     * @throws BusinessException 无权限、用户不存在或修改失败时抛出
     */
    Integer batchUpdateUser(List<SysUserUpdateRTO> updateParamList);

    /**
     * <p>批量更新用户状态</p>
     *
     * @param ids 用户ID集合
     * @param status 用户状态
     * @return 更新结果行数
     * @throws BusinessException 无权限、用户不存在或更新失败时抛出
     */
    Integer batchUpdateUserStatus(List<String> ids, String status);

    /**
     * <p>批量删除用户</p>
     *
     * @param ids 用户ID集合
     * @return 删除结果行数
     * @throws BusinessException 无权限、用户不存在或删除失败时抛出
     */
    Integer batchDeleteUser(List<String> ids);

    /**
     * <p>管理员重置用户密码</p>
     *
     * @param param 重置密码参数，包含用户编码和新密码
     * @return 更新结果行数
     * @throws BusinessException 用户不存在时抛出
     */
    Integer resetPassword(SysUserResetPasswordRTO param);

    /**
     * <p>用户自行修改密码</p>
     *
     * @param param 修改密码参数，包含旧密码、新密码和确认密码
     * @return 更新结果行数
     * @throws BusinessException 两次密码不一致、用户不存在或旧密码不正确时抛出
     */
    Integer changePassword(SysUserChangePasswordRTO param);

    /**
     * <p>查询用户角色</p>
     *
     * @param userCode 用户编码
     * @return 用户角色通用VO列表
     * @throws BusinessException 用户不存在时抛出
     */
    List<SysRoleCommonVO> queryUserRoles(String userCode);

    /**
     * <p>为用户分配角色</p>
     *
     * @param userCode 用户编码
     * @param roleCodeList 角色编码集合
     * @return 新创建的角色策略数量
     * @throws BusinessException 用户不存在或无法获取当前租户信息时抛出
     */
    Integer assignRoles(String userCode, List<String> roleCodeList);

    /**
     * <p>查询用户权限</p>
     *
     * @param userCode 用户编码
     * @return 用户权限编码列表
     * @throws BusinessException 用户不存在时抛出
     */
    List<String> queryUserPerms(String userCode);

}
