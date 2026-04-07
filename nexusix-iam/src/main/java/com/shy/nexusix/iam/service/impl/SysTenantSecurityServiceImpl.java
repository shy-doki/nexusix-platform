package com.shy.nexusix.iam.service.impl;

import com.shy.nexusix.iam.entity.SysTenantSecurity;
import com.shy.nexusix.iam.mapper.SysTenantSecurityMapper;
import com.shy.nexusix.iam.service.ISysTenantSecurityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 租户安全策略配置表 - 定义租户密码策略和登录限制 服务实现类
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Service
public class SysTenantSecurityServiceImpl extends ServiceImpl<SysTenantSecurityMapper, SysTenantSecurity> implements ISysTenantSecurityService {

}
