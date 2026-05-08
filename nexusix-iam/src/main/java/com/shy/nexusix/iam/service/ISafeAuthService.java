package com.shy.nexusix.iam.service;

public interface ISafeAuthService {

    /**
     * <p>
     * 二级认证验证密码
     * </p>
     * <p>
     * 验证当前登录用户的密码，用于高风险操作前的二次验证：
     * 1. 获取当前登录用户ID
     * 2. 查询用户密码
     * 3. BCrypt密码匹配校验
     * 4. 开启Sa-Token二级认证（有效期120秒）
     * </p>
     *
     * @param password 用户密码
     * @return 验证结果消息
     * @throws com.shy.nexusix.common.exception.BusinessException 验证失败时抛出（密码错误、用户不存在等）
     * @author shy
     * @since 2026-05-08
     */
    String verifySafePassword(String password);

}
