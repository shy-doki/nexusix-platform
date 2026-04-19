package com.shy.nexusix.tenant.service;

import com.shy.nexusix.tenant.entity.SysTenant;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shy.nexusix.tenant.vo.SysTenantTreeVO;

import java.util.List;

/**
 * <p>
 * 租户信息表 - 存储租户基础信息，支持无限层级 服务类
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
public interface ISysTenantService extends IService<SysTenant> {

    /**
     * <p>
     * 查询指定租户的树形结构
     * </p>
     * <p>
     * 查询系统中所有租户的层级关系，并构建成树形结构返回。
     * 返回的租户编码会自动进行脱敏处理（保留前3位和后3位，中间用星号替换）。
     * </p>
     *
     * @param tenantCode 租户编码，用于定位要查询的租户节点
     * @return 租户树形结构列表，每个节点包含租户名称、脱敏后的租户编码、父租户ID、联系人、状态等信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当数据库查询失败或数据异常时抛出
     * @author shy
     * @since 2026-04-19
     */
    List<SysTenantTreeVO> queryTenantTree(String tenantCode);
}
