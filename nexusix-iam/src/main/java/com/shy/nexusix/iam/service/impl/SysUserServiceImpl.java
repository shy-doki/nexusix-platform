package com.shy.nexusix.iam.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shy.nexusix.iam.entity.SysUser;
import com.shy.nexusix.iam.mapper.SysUserMapper;
import com.shy.nexusix.iam.service.ISysUserService;
import org.springframework.stereotype.Service;

/**
 * 系统用户服务实现类
 *
 * @author NexusIX
 * @since 2026-06-12
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

}
