package com.shy.nexusix.iam.service;

import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.iam.rto.LoginRTO;
import com.shy.nexusix.iam.rto.TenantRegisterRTO;
import com.shy.nexusix.iam.rto.UserRegisterRTO;

/**
 * <p>认证服务接口，定义用户登录、登出、注册的核心契约</p>
 *
 * @author shy
 */
public interface IAuthService {

    /**
     * <p>用户登录，执行身份验证并初始化权限上下文</p>
     *
     * @param param 登录请求参数（用户名、密码）
     * @return 登录成功时返回包含用户上下文信息的响应
     * @throws BusinessException 当用户不存在、已禁用、未关联租户或租户已禁用时抛出
     */
    ApiResponse login(LoginRTO param);

    /**
     * <p>用户登出，清除当前登录会话</p>
     *
     * @return 登出结果
     */
    ApiResponse logout();

    /**
     * <p>租户注册（含管理员账户）</p>
     * <p>创建租户、管理员用户、用户策略（关联租户），并初始化租户邀请码</p>
     *
     * @param param 租户注册请求参数
     * @return 注册结果，包含登录账号和租户编码
     * @throws BusinessException 当邮箱/手机号已存在或租户创建失败时抛出
     */
    ApiResponse registerTenant(TenantRegisterRTO param);

    /**
     * <p>用户注册（通过邀请码加入租户）</p>
     * <p>创建用户、校验邀请码、绑定到对应租户</p>
     *
     * @param param 用户注册请求参数
     * @return 注册结果，包含登录账号和租户编码
     * @throws BusinessException 当邀请码无效、邮箱/手机号已存在或注册失败时抛出
     */
    ApiResponse registerUser(UserRegisterRTO param);

}
