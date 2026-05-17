package com.shy.nexusix.iam.service.impl;

import com.shy.nexusix.iam.entity.SysUser;
import com.shy.nexusix.iam.mapper.SysUserMapper;
import com.shy.nexusix.iam.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户基础表-存储全局用户信息 (不区分租户) 服务实现类
 * </p>
 *
 * @author shy
 * @since 2026-05-17
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

}
