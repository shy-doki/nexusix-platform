package com.shy.nexusix.audit.service.impl;

import com.shy.nexusix.audit.entity.SysDataAuditLog;
import com.shy.nexusix.audit.mapper.SysDataAuditLogMapper;
import com.shy.nexusix.audit.service.ISysDataAuditLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 数据变更审计表 - 记录数据字段变更详情 服务实现类
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Service
public class SysDataAuditLogServiceImpl extends ServiceImpl<SysDataAuditLogMapper, SysDataAuditLog> implements ISysDataAuditLogService {

}
