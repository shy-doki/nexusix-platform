package com.shy.nexusix.tenant.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.tenant.entity.SysTenant;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shy.nexusix.tenant.rto.SysTenantAddRTO;
import com.shy.nexusix.tenant.rto.SysTenantQueryRTO;
import com.shy.nexusix.tenant.vo.SysTenantCommonVO;
import com.shy.nexusix.tenant.vo.SysTenantDetailVO;
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
     * 查询租户列表
     * </p>
     * <p>
     * 返回所有租户的平铺列表，租户编码会自动进行脱敏处理（保留前3位和后3位）。
     * 需要登录并具备租户查看权限才能访问。
     * </p>
     *
     * @return 租户列表，包含租户名称、脱敏后的租户编码、联系人、状态等信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-04-19
     */
    List<SysTenantCommonVO> queryTenantList();

    /**
     * <p>
     * 分页查询租户列表
     * </p>
     * <p>
     * 返回分页后的租户列表，租户编码会自动进行脱敏处理（保留前3位和后3位）。
     * 需要登录并具备租户查看权限才能访问。
     * </p>
     *
     * @param page 分页参数
     * @return 分页后的租户列表，包含租户名称、脱敏后的租户编码、联系人、状态等信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限
     * @author shy
     * @since 2026-04-19
     */
    IPage<SysTenantCommonVO> queryTenantPage(PageCommonRTO page);

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

    /**
     * <p>
     * 条件查询\筛选租户列表
     * </p>
     * <p>
     * 返回满足条件的租户列表，租户编码会自动进行脱敏处理（保留前3位和后3位）。
     * 需要登录并具备租户条件查询权限才能访问。
     * </p>
     *
     * @param queryParam 查询条件
     * @return 满足条件的租户列表，包含租户名称、脱敏后的租户编码、联系人、状态等信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-04-19
     */
    IPage<SysTenantCommonVO> queryTenant(SysTenantQueryRTO queryParam);

    /**
     * <p>
     * 查询租户详情
     * </p>
     * <p>
     * 返回指定租户的详情信息，租户敏感会自动进行脱敏处理（保留前3位和后3位）。
     * 需要登录并具备租户详情查询权限才能访问。
     * </p>
     *
     * @param tenantCode 租户编码
     * @return 租户详情信息，包含租户名称、租户编码、联系人、状态等信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-04-19
     */
    SysTenantDetailVO queryTenantDetail(String tenantCode);

    /**
     * <p>
     * 新增租户
     * </p>
     * <p>
     * 新增租户信息，需要登录并具备租户新增权限才能访问。
     * </p>
     *
     * @param addParam 新增租户信息
     * @return 新增租户的ID
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或新增失败时抛出
     * @author shy
     * @since 2026-04-19
     */
    Long addTenant(SysTenantAddRTO addParam);

}
