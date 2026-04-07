package com.shy.nexusix.notify.service.impl;

import com.shy.nexusix.notify.entity.SysMessageSchedule;
import com.shy.nexusix.notify.mapper.SysMessageScheduleMapper;
import com.shy.nexusix.notify.service.ISysMessageScheduleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 定时消息任务表 - 预约发送或周期性发送的消息 服务实现类
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Service
public class SysMessageScheduleServiceImpl extends ServiceImpl<SysMessageScheduleMapper, SysMessageSchedule> implements ISysMessageScheduleService {

}
