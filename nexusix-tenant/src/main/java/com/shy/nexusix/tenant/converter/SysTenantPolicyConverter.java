package com.shy.nexusix.tenant.converter;

import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.tenant.entity.SysTenantPolicy;
import com.shy.nexusix.tenant.rto.SysTenantPolicyAddRTO;
import com.shy.nexusix.tenant.rto.SysTenantPolicyUpdateRTO;
import com.shy.nexusix.tenant.vo.SysTenantPolicyCommonVO;
import com.shy.nexusix.tenant.vo.SysTenantPolicyDetailVO;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

/**
 * <p>
 * 租户策略对象转换器
 * </p>
 * <p>
 * 基于MapStruct实现，Spring容器管理，负责租户策略实体与前端VO对象的转换，
 * 包含字段映射、状态码/删除标记枚举转文本描述
 * </p>
 *
 * @author shy
 * @since 2026-06-13
 */
@Mapper(componentModel = "spring")
public interface SysTenantPolicyConverter {

    /**
     * <p>租户策略实体转换为公共VO对象</p>
     * <p>字段重命名映射，状态码/删除标记转换为前端可读描述</p>
     *
     * @param entity 租户策略数据库实体
     * @return 前端通用租户策略VO
     */
    @Named("toCommonVO")
    @Mapping(source = "createBy", target = "createByName")
    @Mapping(source = "createAt", target = "createTime")
    @Mapping(source = "updateBy", target = "updateByName")
    @Mapping(source = "updateAt", target = "updateTime")
    @Mapping(source = "deletedAt", target = "deleteTime")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "isDeleted", target = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysTenantPolicyCommonVO toCommonVO(SysTenantPolicy entity);

    /**
     * <p>租户策略实体列表批量转换为VO列表</p>
     * <p>复用单对象转换规则，实现批量映射</p>
     *
     * @param entityList 租户策略实体集合
     * @return 租户策略VO集合
     */
    @IterableMapping(qualifiedByName = "toCommonVO")
    List<SysTenantPolicyCommonVO> entityListToCommonVoList(List<SysTenantPolicy> entityList);

    /**
     * <p>租户策略实体转换为详情VO对象</p>
     * <p>在公共VO映射基础上，额外映射详情字段和创建/更新人编码</p>
     *
     * @param entity 租户策略数据库实体
     * @return 租户策略详情VO
     */
    @Named("toDetailVO")
    @Mapping(source = "createBy", target = "createByCode")
    @Mapping(source = "createBy", target = "createByName")
    @Mapping(source = "createAt", target = "createTime")
    @Mapping(source = "updateBy", target = "updateByCode")
    @Mapping(source = "updateBy", target = "updateByName")
    @Mapping(source = "updateAt", target = "updateTime")
    @Mapping(source = "deletedAt", target = "deleteTime")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "isDeleted", target = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysTenantPolicyDetailVO toDetailVO(SysTenantPolicy entity);

    /**
     * <p>删除标记编码转换为描述文本</p>
     *
     * @param code 删除标记编码
     * @return 删除状态描述
     */
    @Named("intDeletedToDesc")
    default String intDeletedToDesc(String code) {
        if (code == null) return null;
        GlobalEnum.Deleted deleted = GlobalEnum.Deleted.getByCode(code);
        return deleted != null ? deleted.getDesc() : null;
    }

    /**
     * <p>删除标记枚举转换为编码</p>
     *
     * @param del 删除标记枚举
     * @return 删除标记编码
     */
    @Named("isDeletedToCode")
    default String isDeletedToCode(GlobalEnum.Deleted del) {
        return del != null ? del.getCode() : null;
    }

    /**
     * <p>字符串转换为Long类型</p>
     * <p>用于RTO中String类型的编码字段转换为Entity中Long类型的ID字段</p>
     *
     * @param value 字符串值
     * @return Long类型值，输入为空时返回null
     */
    @Named("stringToLong")
    default Long stringToLong(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        return Long.parseLong(value.trim());
    }

    /**
     * <p>租户策略新增RTO转换为实体对象</p>
     * <p>字段重命名映射，枚举类型转换为编码字符串</p>
     *
     * @param addRTO 租户策略新增请求对象
     * @return 租户策略数据库实体
     */
    @Named("toEntityFromAdd")
    @Mapping(source = "createByCode", target = "createBy", qualifiedByName = "stringToLong")
    @Mapping(source = "createTime", target = "createAt")
    @Mapping(source = "updateByCode", target = "updateBy", qualifiedByName = "stringToLong")
    @Mapping(source = "updateTime", target = "updateAt")
    @Mapping(source = "isDeleted", target = "isDeleted", qualifiedByName = "isDeletedToCode")
    @Mapping(source = "deleteTime", target = "deletedAt")
    @Mapping(target = "id", ignore = true)
    SysTenantPolicy toEntityFromAdd(SysTenantPolicyAddRTO addRTO);

    /**
     * <p>租户策略更新RTO转换为实体对象</p>
     * <p>字段重命名映射，枚举类型转换为编码字符串</p>
     *
     * @param updateRTO 租户策略更新请求对象
     * @return 租户策略数据库实体
     */
    @Named("toEntityFromUpdate")
    @Mapping(source = "createByCode", target = "createBy", qualifiedByName = "stringToLong")
    @Mapping(source = "createTime", target = "createAt")
    @Mapping(source = "updateByCode", target = "updateBy", qualifiedByName = "stringToLong")
    @Mapping(source = "updateTime", target = "updateAt")
    @Mapping(source = "isDeleted", target = "isDeleted", qualifiedByName = "isDeletedToCode")
    @Mapping(source = "deleteTime", target = "deletedAt")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sourceType", ignore = true)
    @Mapping(target = "sourceId", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    SysTenantPolicy toEntityFromUpdate(SysTenantPolicyUpdateRTO updateRTO);

    /**
     * <p>租户策略新增RTO列表批量转换为实体列表</p>
     * <p>复用单对象转换规则，实现批量映射</p>
     *
     * @param addRTOList 租户策略新增请求对象集合
     * @return 租户策略实体集合
     */
    @IterableMapping(qualifiedByName = "toEntityFromAdd")
    List<SysTenantPolicy> addRTOListToEntityList(List<SysTenantPolicyAddRTO> addRTOList);

    /**
     * <p>租户策略更新RTO列表批量转换为实体列表</p>
     * <p>复用单对象转换规则，实现批量映射</p>
     *
     * @param updateRTOList 租户策略更新请求对象集合
     * @return 租户策略实体集合
     */
    @IterableMapping(qualifiedByName = "toEntityFromUpdate")
    List<SysTenantPolicy> updateRTOListToEntityList(List<SysTenantPolicyUpdateRTO> updateRTOList);

}
