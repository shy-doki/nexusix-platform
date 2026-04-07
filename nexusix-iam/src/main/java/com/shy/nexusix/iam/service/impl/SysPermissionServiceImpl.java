package com.shy.nexusix.iam.service.impl;

import com.shy.nexusix.iam.entity.SysPermission;
import com.shy.nexusix.iam.mapper.SysPermissionMapper;
import com.shy.nexusix.iam.service.ISysPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 权限/资源表 - 定义系统所有可授权资源 服务实现类
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {

}
