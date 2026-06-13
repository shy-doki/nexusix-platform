package com.shy.nexusix.tenant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shy.nexusix.tenant.entity.SysTenantPolicy;
import com.shy.nexusix.tenant.vo.SysTenantPolicyCommonVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 租户策略表 Mapper 接口
 * </p>
 *
 * @author shy
 * @since 2026-06-13
 */
public interface SysTenantPolicyMapper extends BaseMapper<SysTenantPolicy> {

    /**
     * <p>查询租户策略列表，关联查询源实体和目标租户的详细信息</p>
     *
     * @param sourceType 源实体类型
     * @param sourceId 源实体ID
     * @param tenantId 目标租户ID
     * @param status 策略状态
     * @return 租户策略VO列表
     */
    List<SysTenantPolicyCommonVO> queryPolicyListWithDetails(@Param("sourceType") String sourceType,
                                                               @Param("sourceId") Long sourceId,
                                                               @Param("tenantId") Long tenantId,
                                                               @Param("status") String status);

    /**
     * <p>根据策略编码查询策略详情，关联查询源实体和目标租户的详细信息</p>
     *
     * @param policyCode 策略编码
     * @return 租户策略VO
     */
    SysTenantPolicyCommonVO queryPolicyDetailByCode(@Param("policyCode") String policyCode);

}
