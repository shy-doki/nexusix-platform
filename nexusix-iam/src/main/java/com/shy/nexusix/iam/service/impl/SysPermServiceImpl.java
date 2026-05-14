package com.shy.nexusix.iam.service.impl;

import com.shy.nexusix.iam.entity.SysPerm;
import com.shy.nexusix.iam.mapper.SysPermMapper;
import com.shy.nexusix.iam.service.ISysPermService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 权限/资源表-定义系统所有可授权资源 服务实现类
 * </p>
 *
 * @author author
 * @since 2026-05-13
 */
@Service
public class SysPermServiceImpl extends ServiceImpl<SysPermMapper, SysPerm> implements ISysPermService {

}
