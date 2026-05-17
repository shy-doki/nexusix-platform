package com.shy.nexusix.iam.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shy.nexusix.iam.entity.SysUser;
import com.shy.nexusix.iam.mapper.SysUserMapper;
import com.shy.nexusix.iam.service.IAuthService;
import org.springframework.stereotype.Service;

@Service
public class IAuthServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements IAuthService {
}
