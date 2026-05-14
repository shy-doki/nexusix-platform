package com.shy.nexusix.iam.service.impl;

import com.shy.nexusix.iam.entity.SysUserToken;
import com.shy.nexusix.iam.mapper.SysUserTokenMapper;
import com.shy.nexusix.iam.service.ISysUserTokenService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户Token记录表-用于多端登录管理和强制下线 服务实现类
 * </p>
 *
 * @author author
 * @since 2026-05-13
 */
@Service
public class SysUserTokenServiceImpl extends ServiceImpl<SysUserTokenMapper, SysUserToken> implements ISysUserTokenService {

}
