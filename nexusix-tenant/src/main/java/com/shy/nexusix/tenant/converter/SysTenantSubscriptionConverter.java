package com.shy.nexusix.tenant.converter;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.enums.GlobalEnum.SubscriptionStatus;
import com.shy.nexusix.common.enums.GlobalEnum.SubscriptionType;
import com.shy.nexusix.tenant.entity.SysTenantSubscription;
import com.shy.nexusix.tenant.rto.SysTenantSubscriptionAddRTO;
import com.shy.nexusix.tenant.rto.SysTenantSubscriptionUpdateRTO;
import com.shy.nexusix.tenant.vo.SysTenantSubscriptionCommonVO;
import com.shy.nexusix.tenant.vo.SysTenantSubscriptionDetailVO;
import com.shy.nexusix.tenant.vo.SysTenantSubscriptionExportVO;
import org.mapstruct.*;

import java.util.List;

/**
 * <p>
 * 租户套餐订阅对象转换器
 * </p>
 * <p>
 * 负责Entity、RTO、VO之间的对象转换，包含枚举值与描述的转换逻辑
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Mapper(componentModel = "spring")
public interface SysTenantSubscriptionConverter {

    /**
     * 新增RTO转Entity
     *
     * @param addParam 新增请求对象
     * @return 实体对象
     */
    @Mapping(target = "subscriptionType", source = "subscriptionType", qualifiedByName = "subscriptionTypeToCode")
    @Mapping(target = "status", source = "status", qualifiedByName = "subscriptionStatusToCode")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "deletedToCode")
    SysTenantSubscription toEntityAdd(SysTenantSubscriptionAddRTO addParam);

    /**
     * 更新RTO转Entity
     *
     * @param updateParam 更新请求对象
     * @return 实体对象
     */
    @Mapping(target = "subscriptionType", source = "subscriptionType", qualifiedByName = "subscriptionTypeToCode")
    @Mapping(target = "status", source = "status", qualifiedByName = "subscriptionStatusToCode")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "deletedToCode")
    SysTenantSubscription toEntityUpdate(SysTenantSubscriptionUpdateRTO updateParam);

    /**
     * 新增RTO列表转Entity列表
     *
     * @param addParamList 新增请求对象列表
     * @return 实体对象列表
     */
    List<SysTenantSubscription> toEntityListAdd(List<SysTenantSubscriptionAddRTO> addParamList);

    /**
     * 更新RTO列表转Entity列表
     *
     * @param updateParamList 更新请求对象列表
     * @return 实体对象列表
     */
    @Mapping(target = "subscriptionType", source = "subscriptionType", qualifiedByName = "subscriptionTypeToCode")
    @Mapping(target = "status", source = "status", qualifiedByName = "subscriptionStatusToCode")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "deletedToCode")
    List<SysTenantSubscription> toEntityListUpdate(List<SysTenantSubscriptionUpdateRTO> updateParamList);

    /**
     * Entity转通用VO
     *
     * @param subscription 实体对象
     * @return 通用视图对象
     */
    @Named("toCommonVO")
    @Mapping(target = "subscriptionType", source = "subscriptionType", qualifiedByName = "intSubscriptionTypeToDesc")
    @Mapping(target = "status", source = "status", qualifiedByName = "intSubscriptionStatusToDesc")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysTenantSubscriptionCommonVO toCommonVO(SysTenantSubscription subscription);

    /**
     * Entity列表转通用VO列表
     *
     * @param subscriptionList 实体对象列表
     * @return 通用视图对象列表
     */
    @IterableMapping(qualifiedByName = "toCommonVO")
    List<SysTenantSubscriptionCommonVO> toVoList(List<SysTenantSubscription> subscriptionList);

    /**
     * Entity分页转通用VO分页
     *
     * @param subscriptionPage 实体对象分页
     * @return 通用视图对象分页
     */
    default IPage<SysTenantSubscriptionCommonVO> toVOPage(IPage<SysTenantSubscription> subscriptionPage) {
        IPage<SysTenantSubscriptionCommonVO> voPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(
                subscriptionPage.getCurrent(), subscriptionPage.getSize(), subscriptionPage.getTotal());
        voPage.setRecords(toVoList(subscriptionPage.getRecords()));
        return voPage;
    }

    /**
     * Entity转详情VO
     *
     * @param subscription 实体对象
     * @return 详情视图对象
     */
    @Named("toDetailVO")
    @Mapping(target = "subscriptionType", source = "subscriptionType", qualifiedByName = "intSubscriptionTypeToDesc")
    @Mapping(target = "status", source = "status", qualifiedByName = "intSubscriptionStatusToDesc")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysTenantSubscriptionDetailVO toDetailVO(SysTenantSubscription subscription);

    /**
     * Entity转导出VO
     *
     * @param subscription 实体对象
     * @return 导出视图对象
     */
    @Named("toExportVO")
    @Mapping(target = "subscriptionType", source = "subscriptionType", qualifiedByName = "intSubscriptionTypeToDesc")
    @Mapping(target = "status", source = "status", qualifiedByName = "intSubscriptionStatusToDesc")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysTenantSubscriptionExportVO toExportVO(SysTenantSubscription subscription);

    /**
     * Entity列表转导出VO列表
     *
     * @param subscriptionList 实体对象列表
     * @return 导出视图对象列表
     */
    @IterableMapping(qualifiedByName = "toExportVO")
    List<SysTenantSubscriptionExportVO> toExportVoList(List<SysTenantSubscription> subscriptionList);

    /**
     * 订阅类型枚举转编码
     */
    @Named("subscriptionTypeToCode")
    default Integer subscriptionTypeToCode(SubscriptionType subscriptionType) {
        if (subscriptionType == null) return null;
        return subscriptionType.getCode();
    }

    /**
     * 订阅状态枚举转编码
     */
    @Named("subscriptionStatusToCode")
    default Integer subscriptionStatusToCode(SubscriptionStatus subscriptionStatus) {
        if (subscriptionStatus == null) return null;
        return subscriptionStatus.getCode();
    }

    /**
     * 删除标记枚举转编码
     */
    @Named("deletedToCode")
    default Integer deletedToCode(GlobalEnum.Deleted deleted) {
        if (deleted == null) return null;
        return deleted.getCode();
    }

    /**
     * 订阅类型编码转中文描述
     */
    @Named("intSubscriptionTypeToDesc")
    default String intSubscriptionTypeToDesc(Integer code) {
        if (code == null) return null;
        SubscriptionType subscriptionType = SubscriptionType.getByCode(code);
        return subscriptionType != null ? subscriptionType.getDesc() : null;
    }

    /**
     * 订阅状态编码转中文描述
     */
    @Named("intSubscriptionStatusToDesc")
    default String intSubscriptionStatusToDesc(Integer code) {
        if (code == null) return null;
        SubscriptionStatus subscriptionStatus = SubscriptionStatus.getByCode(code);
        return subscriptionStatus != null ? subscriptionStatus.getDesc() : null;
    }

    /**
     * 删除标记编码转中文描述
     */
    @Named("intDeletedToDesc")
    default String intDeletedToDesc(Integer code) {
        if (code == null) return null;
        GlobalEnum.Deleted deleted = GlobalEnum.Deleted.getByCode(code);
        return deleted != null ? deleted.getDesc() : null;
    }

}
