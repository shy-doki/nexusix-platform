package com.shy.nexusix.tenant.converter;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.enums.GlobalEnum.SubscriptionStatus;
import com.shy.nexusix.common.enums.GlobalEnum.SubscriptionType;
import com.shy.nexusix.tenant.entity.SysTenantSubscription;
import com.shy.nexusix.tenant.rto.SysTenantSubscriptionAddRTO;
import com.shy.nexusix.tenant.rto.SysTenantSubscriptionUpdateRTO;
import com.shy.nexusix.tenant.vo.SysTenantSubscriptionCommonVO;
import com.shy.nexusix.tenant.vo.SysTenantSubscriptionDetailVO;
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
     * <p>
     * 将新增订阅请求对象转换为订阅实体
     * </p>
     * <p>
     * 说明：
     * 1. 枚举字段（subscriptionType、status、isDeleted）转换为对应的code值存储到数据库
     * 2. parentCode映射为parentId（仅字段名映射，code与id数值不同但字段对应）
     * 3. createByCode映射为createBy，updateByCode映射为updateBy
     * 4. createTime映射为createAt，updateTime映射为updateAt，deleteTime映射为deleteAt
     * 5. tenantCode、packageCode 直接映射为 tenantCode、packageId
     * 6. id、subscriptionCode、tenantId 等由系统自动生成或服务层处理，忽略映射
     * 7. packageName、parentName 等名称字段在订阅实体中不存在，RTO中的这些字段不会被映射，
     *    需要在Service层通过连表查询补充
     * </p>
     *
     * @param addParam 新增请求对象
     * @return 实体对象
     * @author shy
     * @since 2026-04-07
     */
    @Named("toEntityAdd")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subscriptionCode", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "subscriptionType", source = "subscriptionType", qualifiedByName = "subscriptionTypeToCode")
    @Mapping(target = "tenantCode", source = "tenantCode")
    @Mapping(target = "tenantName", source = "tenantName")
    @Mapping(target = "packageId", source = "packageCode")
    @Mapping(target = "parentId", source = "parentCode")
    @Mapping(target = "createBy", source = "createByCode")
    @Mapping(target = "createAt", source = "createAt")
    @Mapping(target = "updateBy", source = "updateByCode")
    @Mapping(target = "updateAt", source = "updateAt")
    @Mapping(target = "deleteAt", source = "deleteAt")
    @Mapping(target = "status", source = "status", qualifiedByName = "subscriptionStatusToCode")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "deletedToCode")
    SysTenantSubscription toEntityAdd(SysTenantSubscriptionAddRTO addParam);

    /**
     * <p>
     * 将更新订阅请求对象转换为订阅实体
     * </p>
     * <p>
     * 说明：
     * 1. 枚举字段（subscriptionType、status、isDeleted）转换为对应的code值存储到数据库
     * 2. parentCode映射为parentId（仅字段名映射，code与id数值不同但字段对应）
     * 3. createByCode映射为createBy，updateByCode映射为updateBy
     * 4. createTime映射为createAt，updateTime映射为updateAt，deleteTime映射为deleteAt
     * 5. tenantCode、packageCode 直接映射为 tenantCode、packageId
     * 6. id 等由系统维护，忽略映射
     * 7. packageName、parentName 等名称字段在订阅实体中不存在，RTO中的这些字段不会被映射，
     *    需要在Service层通过连表查询补充
     * </p>
     *
     * @param updateParam 更新请求对象
     * @return 实体对象
     * @author shy
     * @since 2026-04-07
     */
    @Named("toEntityUpdate")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subscriptionCode", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "subscriptionType", source = "subscriptionType", qualifiedByName = "subscriptionTypeToCode")
    @Mapping(target = "tenantCode", source = "tenantCode")
    @Mapping(target = "tenantName", source = "tenantName")
    @Mapping(target = "packageId", source = "packageCode")
    @Mapping(target = "parentId", source = "parentCode")
    @Mapping(target = "createBy", source = "createByCode")
    @Mapping(target = "createAt", source = "createTime")
    @Mapping(target = "updateBy", source = "updateByCode")
    @Mapping(target = "updateAt", source = "updateTime")
    @Mapping(target = "deleteAt", source = "deleteTime")
    @Mapping(target = "status", source = "status", qualifiedByName = "subscriptionStatusToCode")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "deletedToCode")
    SysTenantSubscription toEntityUpdate(SysTenantSubscriptionUpdateRTO updateParam);

    /**
     * <p>
     * 将新增订阅请求对象列表转换为订阅实体列表
     * </p>
     *
     * @param addParamList 新增请求对象列表
     * @return 实体对象列表
     * @author shy
     * @since 2026-04-07
     */
    @IterableMapping(qualifiedByName = "toEntityAdd")
    List<SysTenantSubscription> toEntityListAdd(List<SysTenantSubscriptionAddRTO> addParamList);

    /**
     * <p>
     * 将更新订阅请求对象列表转换为订阅实体列表
     * </p>
     *
     * @param updateParamList 更新请求对象列表
     * @return 实体对象列表
     * @author shy
     * @since 2026-04-07
     */
    @IterableMapping(qualifiedByName = "toEntityUpdate")
    List<SysTenantSubscription> toEntityListUpdate(List<SysTenantSubscriptionUpdateRTO> updateParamList);

    /**
     * <p>
     * 将订阅实体转换为通用视图对象
     * </p>
     * <p>
     * 说明：
     * 1. 订阅类型、订阅状态、删除标记的code值转换为中文描述
     * 2. 时间字段映射（createAt→createTime，updateAt→updateTime，deleteAt→deleteTime）
     * 3. sourceType字段为String类型，直接映射
     * 4. tenantName 等名称字段实体中存在，可直接映射；
     *    packageName、parentName 等名称字段实体中不存在，需要在Service层通过连表查询补充到VO
     * </p>
     *
     * @param subscription 实体对象
     * @return 通用视图对象
     * @author shy
     * @since 2026-04-07
     */
    @Named("toCommonVO")
    @Mapping(target = "subscriptionType", source = "subscriptionType", qualifiedByName = "intSubscriptionTypeToDesc")
    @Mapping(target = "createTime", source = "createAt")
    @Mapping(target = "updateTime", source = "updateAt")
    @Mapping(target = "deleteTime", source = "deleteAt")
    @Mapping(target = "status", source = "status", qualifiedByName = "intSubscriptionStatusToDesc")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysTenantSubscriptionCommonVO toCommonVO(SysTenantSubscription subscription);

    /**
     * <p>
     * 将订阅实体列表转换为通用视图对象列表
     * </p>
     *
     * @param subscriptionList 实体对象列表
     * @return 通用视图对象列表
     * @author shy
     * @since 2026-04-07
     */
    @IterableMapping(qualifiedByName = "toCommonVO")
    List<SysTenantSubscriptionCommonVO> toVoList(List<SysTenantSubscription> subscriptionList);

    /**
     * <p>
     * 将订阅实体分页对象转换为通用视图对象分页对象
     * </p>
     *
     * @param subscriptionPage 实体对象分页
     * @return 通用视图对象分页
     * @author shy
     * @since 2026-04-07
     */
    default IPage<SysTenantSubscriptionCommonVO> toVOPage(IPage<SysTenantSubscription> subscriptionPage) {
        if (subscriptionPage == null) {
            return null;
        }
        IPage<SysTenantSubscriptionCommonVO> voPage = new Page<>();
        voPage.setCurrent(subscriptionPage.getCurrent());
        voPage.setSize(subscriptionPage.getSize());
        voPage.setTotal(subscriptionPage.getTotal());

        List<SysTenantSubscriptionCommonVO> voList = toVoList(subscriptionPage.getRecords());
        voPage.setRecords(voList);

        return voPage;
    }

    /**
     * <p>
     * 将订阅实体转换为详情视图对象
     * </p>
     * <p>
     * 说明：详情视图包含租户编码、套餐编码、父租户编码、创建人编码、更新人编码等核心标识信息，
     * 注意：code不等于id，此处仅返回编码信息
     * </p>
     *
     * @param subscription 实体对象
     * @return 详情视图对象
     * @author shy
     * @since 2026-04-07
     */
    @Named("toDetailVO")
    @Mapping(target = "tenantCode", source = "tenantCode")
    @Mapping(target = "packageCode", source = "packageId")
    @Mapping(target = "parentCode", source = "parentId")
    @Mapping(target = "createByCode", source = "createBy")
    @Mapping(target = "updateByCode", source = "updateBy")
    SysTenantSubscriptionDetailVO toDetailVO(SysTenantSubscription subscription);

    /**
     * <p>
     * 将订阅类型枚举转换为编码
     * </p>
     *
     * @param subscriptionType 订阅类型枚举
     * @return 类型编码
     * @author shy
     * @since 2026-04-07
     */
    @Named("subscriptionTypeToCode")
    default String subscriptionTypeToCode(SubscriptionType subscriptionType) {
        if (subscriptionType == null) return null;
        return subscriptionType.getCode();
    }

    /**
     * <p>
     * 将订阅状态枚举转换为编码
     * </p>
     *
     * @param subscriptionStatus 订阅状态枚举
     * @return 状态编码
     * @author shy
     * @since 2026-04-07
     */
    @Named("subscriptionStatusToCode")
    default String subscriptionStatusToCode(SubscriptionStatus subscriptionStatus) {
        if (subscriptionStatus == null) return null;
        return subscriptionStatus.getCode();
    }

    /**
     * <p>
     * 将删除标记枚举转换为编码
     * </p>
     *
     * @param deleted 删除标记枚举
     * @return 删除标记编码
     * @author shy
     * @since 2026-04-07
     */
    @Named("deletedToCode")
    default String deletedToCode(GlobalEnum.Deleted deleted) {
        if (deleted == null) return null;
        return deleted.getCode();
    }

    /**
     * <p>
     * 将订阅类型编码转换为中文描述
     * </p>
     *
     * @param code 订阅类型编码
     * @return 类型中文描述
     * @author shy
     * @since 2026-04-07
     */
    @Named("intSubscriptionTypeToDesc")
    default String intSubscriptionTypeToDesc(String code) {
        if (code == null) return null;
        SubscriptionType subscriptionType = SubscriptionType.getByCode(code);
        return subscriptionType != null ? subscriptionType.getDesc() : null;
    }

    /**
     * <p>
     * 将订阅状态编码转换为中文描述
     * </p>
     *
     * @param code 订阅状态编码
     * @return 状态中文描述
     * @author shy
     * @since 2026-04-07
     */
    @Named("intSubscriptionStatusToDesc")
    default String intSubscriptionStatusToDesc(String code) {
        if (code == null) return null;
        SubscriptionStatus subscriptionStatus = SubscriptionStatus.getByCode(code);
        return subscriptionStatus != null ? subscriptionStatus.getDesc() : null;
    }

    /**
     * <p>
     * 将删除标记编码转换为中文描述
     * </p>
     *
     * @param code 删除标记编码
     * @return 删除标记中文描述
     * @author shy
     * @since 2026-04-07
     */
    @Named("intDeletedToDesc")
    default String intDeletedToDesc(String code) {
        if (code == null) return null;
        GlobalEnum.Deleted deleted = GlobalEnum.Deleted.getByCode(code);
        return deleted != null ? deleted.getDesc() : null;
    }

}
