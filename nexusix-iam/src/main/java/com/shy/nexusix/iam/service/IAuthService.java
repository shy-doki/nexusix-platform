package com.shy.nexusix.iam.service;

import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.iam.rto.LoginRTO;
import com.shy.nexusix.common.exception.BusinessException;

/**
 * <p>认证服务接口，定义用户登录认证的核心契约</p>
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

}
