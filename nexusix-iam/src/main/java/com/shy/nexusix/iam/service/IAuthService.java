package com.shy.nexusix.iam.service;

import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.iam.rto.LoginRTO;

/**
 * <p>
 * 认证服务接口，定义用户登录认证的核心契约。
 * </p>
 *
 * <p><b>设计意图：</b></p>
 * <p>本接口抽象了认证流程的核心行为，将认证逻辑与控制器层解耦，
 * 便于不同认证方式（如密码登录、SSO、OAuth2 等）的扩展实现。</p>
 *
 * <p><b>核心职责：</b></p>
 * <ul>
 *   <li>用户身份验证</li>
 *   <li>会话建立与权限上下文初始化</li>
 * </ul>
 *
 * @author shy
 * @since 2026-04-07
 * @see com.shy.nexusix.iam.service.Impl.AuthServiceImpl
 */
public interface IAuthService {

    /**
     * <p>
     * 用户登录，执行身份验证并初始化权限上下文。
     * </p>
     *
     * <p>登录成功后，系统会自动完成以下操作：</p>
     * <ul>
     *   <li>通过 Sa-Token 建立用户会话</li>
     *   <li>构建并缓存用户权限上下文（UserContextDTO）至 Session</li>
     * </ul>
     *
     * @param param 登录请求参数，包含 username（用户名）和 password（密码）
     * @return ApiResponse 登录成功时返回成功响应
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户名或密码不正确时抛出
     * @throws com.shy.nexusix.common.exception.BusinessException 当所属租户已停用或已过期时抛出
     */
    ApiResponse login(LoginRTO param);

}
