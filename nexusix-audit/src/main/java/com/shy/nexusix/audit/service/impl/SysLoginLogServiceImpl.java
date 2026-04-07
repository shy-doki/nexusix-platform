package com.shy.nexusix.audit.service.impl;

import com.shy.nexusix.audit.entity.SysLoginLog;
import com.shy.nexusix.audit.mapper.SysLoginLogMapper;
import com.shy.nexusix.audit.service.ISysLoginLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 登录日志表 - 记录用户登录信息 服务实现类
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Service
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLog> implements ISysLoginLogService {

}
