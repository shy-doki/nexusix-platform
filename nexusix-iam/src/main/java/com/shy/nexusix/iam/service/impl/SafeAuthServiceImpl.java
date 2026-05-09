package com.shy.nexusix.iam.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.iam.entity.SysUser;
import com.shy.nexusix.iam.mapper.SysUserMapper;
import com.shy.nexusix.iam.service.ISafeAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 二级认证服务实现类 - 高风险操作前的密码二次验证
 * </p>
 * <p>
 * 本类实现二级认证相关的业务逻辑，与SafeAuthController层解耦。
 * 验证流程：获取当前用户 → 查询密码 → BCrypt匹配 → 开启Sa-Token二级认证
 * </p>
 *
 * @author shy
 * @since 2026-05-08
 */
@Slf4j
@Service
public class SafeAuthServiceImpl implements ISafeAuthService {

    @Autowired
    private SysUserMapper userMapper;

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
     */
    @Override
    public String verifySafePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new BusinessException(400, "密码不能为空");
        }

        Long userId = StpUtil.getLoginIdAsLong();
        SysUser user = userMapper.selectById(userId);

        if (user == null) {
            log.warn("二级认证失败 - 用户不存在: userId={}", userId);
            throw new BusinessException(401, "用户不存在");
        }

        if (!BCrypt.checkpw(password, user.getPassword())) {
            log.warn("二级认证失败 - 密码错误: userId={}", userId);
            throw new BusinessException(401, "密码验证失败");
        }

        // 开启二级认证，有效期120秒 → 自动写入Redis
        StpUtil.openSafe(120);
        log.info("二级认证成功 - userId={}", userId);

        return "二级认证成功，有效期120秒";
    }

}
