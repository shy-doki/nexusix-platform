package com.shy.nexusix.iam.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.iam.entity.SysUser;
import com.shy.nexusix.iam.mapper.SysUserMapper;
import com.shy.nexusix.iam.service.ISafeAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SafeAuthServiceImpl implements ISafeAuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private SysUserMapper userMapper;

    /**
     * 二级认证验证密码 - 完整业务逻辑实现
     */
    @Override
    public String verifySafePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new BusinessException(400, "密码不能为空");
        }

        Long userId = StpUtil.getLoginIdAsLong();
        SysUser user = userMapper.selectById(userId);

        if (user == null) {
            logger.warn("二级认证失败 - 用户不存在: userId={}", userId);
            throw new BusinessException(401, "用户不存在");
        }

        if (!BCrypt.checkpw(password, user.getPassword())) {
            logger.warn("二级认证失败 - 密码错误: userId={}", userId);
            throw new BusinessException(401, "密码验证失败");
        }

        // 开启二级认证，有效期120秒 → 自动写入Redis
        StpUtil.openSafe(120);
        logger.info("二级认证成功 - userId={}", userId);

        return "二级认证成功，有效期120秒";
    }

}
