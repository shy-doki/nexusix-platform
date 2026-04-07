package com.shy.nexusix.iam.service.impl;

import com.shy.nexusix.iam.entity.SysUserToken;
import com.shy.nexusix.iam.mapper.SysUserTokenMapper;
import com.shy.nexusix.iam.service.ISysUserTokenService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户 Token 记录表 - 用于多端登录管理和强制下线 服务实现类
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Service
public class SysUserTokenServiceImpl extends ServiceImpl<SysUserTokenMapper, SysUserToken> implements ISysUserTokenService {

}
