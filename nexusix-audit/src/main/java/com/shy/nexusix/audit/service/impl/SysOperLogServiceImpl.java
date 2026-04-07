package com.shy.nexusix.audit.service.impl;

import com.shy.nexusix.audit.entity.SysOperLog;
import com.shy.nexusix.audit.mapper.SysOperLogMapper;
import com.shy.nexusix.audit.service.ISysOperLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 操作日志表 - 审计用户操作行为 服务实现类
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Service
public class SysOperLogServiceImpl extends ServiceImpl<SysOperLogMapper, SysOperLog> implements ISysOperLogService {

}
