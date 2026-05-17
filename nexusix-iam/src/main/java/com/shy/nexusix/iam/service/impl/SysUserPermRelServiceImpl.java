package com.shy.nexusix.iam.service.impl;

import com.shy.nexusix.iam.entity.SysUserPermRel;
import com.shy.nexusix.iam.mapper.SysUserPermRelMapper;
import com.shy.nexusix.iam.service.ISysUserPermRelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户生效权限表(策略计算结果快照) 服务实现类
 * </p>
 *
 * @author shy
 * @since 2026-05-17
 */
@Service
public class SysUserPermRelServiceImpl extends ServiceImpl<SysUserPermRelMapper, SysUserPermRel> implements ISysUserPermRelService {

}
