package com.shy.nexusix.notify.service.impl;

import com.shy.nexusix.notify.entity.SysInboxMessage;
import com.shy.nexusix.notify.mapper.SysInboxMessageMapper;
import com.shy.nexusix.notify.service.ISysInboxMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 站内信收件箱表 - 用户个人消息 inbox 服务实现类
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Service
public class SysInboxMessageServiceImpl extends ServiceImpl<SysInboxMessageMapper, SysInboxMessage> implements ISysInboxMessageService {

}
