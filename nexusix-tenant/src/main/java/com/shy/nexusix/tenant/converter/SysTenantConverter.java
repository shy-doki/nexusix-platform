package com.shy.nexusix.tenant.converter;

import com.shy.nexusix.common.constant.GlobalConstant;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.tenant.entity.SysTenant;
import com.shy.nexusix.tenant.rto.SysTenantAddRTO;
import com.shy.nexusix.tenant.rto.SysTenantUpdateRTO;
import com.shy.nexusix.tenant.vo.SysTenantCommonVO;
import com.shy.nexusix.tenant.vo.SysTenantDetailVO;
import com.shy.nexusix.tenant.vo.SysTenantTreeVO;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 租户对象转换器
 * <p>基于MapStruct实现，Spring容器管理，负责租户实体与前端VO对象的转换，
 * 包含字段映射、状态码/删除标记枚举转文本描述</p>
 *
 * @author shy
 * @since 2026-04-27
 */
@Mapper(componentModel = "spring")
public interface SysTenantConverter {

    /**
     * 租户实体转换为公共VO对象
     * <p>字段重命名映射，状态码/删除标记转换为前端可读描述</p>
     *
     * @param entity 租户数据库实体
     * @return 前端通用租户VO
     */
    @Named("toCommonVO")
    @Mapping(source = "createBy", target = "createByName")
    @Mapping(source = "createAt", target = "createTime")
    @Mapping(source = "updateBy", target = "updateByName")
    @Mapping(source = "updateAt", target = "updateTime")
    @Mapping(source = "deletedAt", target = "deleteTime")
    @Mapping(source = "status", target = "status", qualifiedByName = "intStatusToDesc")
    @Mapping(source = "isDeleted", target = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysTenantCommonVO toCommonVO(SysTenant entity);

    /**
     * 租户实体列表批量转换为VO列表
     * <p>复用单对象转换规则，实现批量映射</p>
     *
     * @param entityList 租户实体集合
     * @return 租户VO集合
     */
    @IterableMapping(qualifiedByName  = "toCommonVO")
    List<SysTenantCommonVO> entityListToCommonVoList(List<SysTenant> entityList);

    /**
     * 租户实体转换为详情VO对象
     * <p>在公共VO映射基础上，额外映射详情字段和创建/更新人编码</p>
     *
     * @param entity 租户数据库实体
     * @return 租户详情VO
     */
    @Named("toDetailVO")
    @Mapping(source = "createBy", target = "createByName")
    @Mapping(source = "createAt", target = "createTime")
    @Mapping(source = "updateBy", target = "updateByName")
    @Mapping(source = "updateAt", target = "updateTime")
    @Mapping(source = "deletedAt", target = "deleteTime")
    @Mapping(source = "status", target = "status", qualifiedByName = "intStatusToDesc")
    @Mapping(source = "isDeleted", target = "isDeleted", qualifiedByName = "intDeletedToDesc")
    @Mapping(source = "createBy", target = "createByCode")
    @Mapping(source = "updateBy", target = "updateByCode")
    SysTenantDetailVO toDetailVO(SysTenant entity);

    /**
     * 租户实体转换为树形VO对象
     * <p>在公共VO映射基础上，额外映射父租户编码字段</p>
     *
     * @param entity 租户数据库实体
     * @return 租户树形VO
     */
    @Named("toTreeVO")
    @Mapping(source = "createBy", target = "createByName")
    @Mapping(source = "createAt", target = "createTime")
    @Mapping(source = "updateBy", target = "updateByName")
    @Mapping(source = "updateAt", target = "updateTime")
    @Mapping(source = "deletedAt", target = "deleteTime")
    @Mapping(source = "status", target = "status", qualifiedByName = "intStatusToDesc")
    @Mapping(source = "isDeleted", target = "isDeleted", qualifiedByName = "intDeletedToDesc")
    @Mapping(source = "parentId", target = "parentCode")
    SysTenantTreeVO toTreeVO(SysTenant entity);

    /**
     * 租户实体列表批量转换为树形VO列表
     * <p>复用单对象转换规则，实现批量映射</p>
     *
     * @param entityList 租户实体集合
     * @return 租户树形VO集合
     */
    @IterableMapping(qualifiedByName = "toTreeVO")
    List<SysTenantTreeVO> entityListToTreeVoList(List<SysTenant> entityList);

    /**
     * 租户状态枚举转换为描述文本
     *
     * @param status 租户状态枚举
     * @return 状态描述
     */
    @Named("statusToDesc")
    default String statusToDesc(GlobalEnum.TenantStatus status) {
        return status != null ? status.getDesc() : null;
    }

    /**
     * 租户状态枚举转换为状态码
     *
     * @param status 租户状态枚举
     * @return 状态码
     */
    @Named("statusToCode")
    default String statusToCode(GlobalEnum.TenantStatus status) {
        return status != null ? status.getCode() : null;
    }

    /**
     * 租户状态码转换为描述文本
     *
     * @param code 状态码
     * @return 状态描述
     */
    @Named("intStatusToDesc")
    default String intStatusToDesc(String code) {
        if (code == null) return null;
        GlobalEnum.TenantStatus status = GlobalEnum.TenantStatus.getByCode(code);
        return status != null ? status.getDesc() : null;
    }

    /**
     * 删除标记枚举转换为描述文本
     *
     * @param del 删除标记枚举
     * @return 删除状态描述
     */
    @Named("isDeletedToDesc")
    default String isDeletedToDesc(GlobalEnum.Deleted del) {
        return del != null ? del.getDesc() : null;
    }

    /**
     * 删除标记枚举转换为编码
     *
     * @param del 删除标记枚举
     * @return 删除标记编码
     */
    @Named("isDeletedToCode")
    default String isDeletedToCode(GlobalEnum.Deleted del) {
        return del != null ? del.getCode() : null;
    }

    /**
     * 删除标记编码转换为描述文本
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
     * 字符串转换为Long类型
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
     * 租户新增RTO转换为实体对象
     * <p>字段重命名映射，枚举类型转换为编码字符串</p>
     *
     * @param addRTO 租户新增请求对象
     * @return 租户数据库实体
     */
    @Named("toEntityFromAdd")
    @Mapping(source = "parentCode", target = "parentId")
    @Mapping(source = "packageCode", target = "packageId", qualifiedByName = "stringToLong")
    @Mapping(source = "createByCode", target = "createBy")
    @Mapping(source = "createTime", target = "createAt")
    @Mapping(source = "updateByCode", target = "updateBy")
    @Mapping(source = "updateTime", target = "updateAt")
    @Mapping(source = "status", target = "status", qualifiedByName = "statusToCode")
    @Mapping(source = "isDeleted", target = "isDeleted", qualifiedByName = "isDeletedToCode")
    @Mapping(source = "deleteTime", target = "deletedAt")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hasChildren", ignore = true)
    SysTenant toEntityFromAdd(SysTenantAddRTO addRTO);

    /**
     * 租户更新RTO转换为实体对象
     * <p>字段重命名映射，枚举类型转换为编码字符串</p>
     *
     * @param updateRTO 租户更新请求对象
     * @return 租户数据库实体
     */
    @Named("toEntityFromUpdate")
    @Mapping(source = "parentCode", target = "parentId")
    @Mapping(source = "packageCode", target = "packageId", qualifiedByName = "stringToLong")
    @Mapping(source = "createByCode", target = "createBy")
    @Mapping(source = "createTime", target = "createAt")
    @Mapping(source = "updateByCode", target = "updateBy")
    @Mapping(source = "updateTime", target = "updateAt")
    @Mapping(source = "status", target = "status", qualifiedByName = "statusToCode")
    @Mapping(source = "isDeleted", target = "isDeleted", qualifiedByName = "isDeletedToCode")
    @Mapping(source = "deleteTime", target = "deletedAt")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hasChildren", ignore = true)
    SysTenant toEntityFromUpdate(SysTenantUpdateRTO updateRTO);

    /**
     * 租户新增RTO列表批量转换为实体列表
     * <p>复用单对象转换规则，实现批量映射</p>
     *
     * @param addRTOList 租户新增请求对象集合
     * @return 租户实体集合
     */
    @IterableMapping(qualifiedByName = "toEntityFromAdd")
    List<SysTenant> addRTOListToEntityList(List<SysTenantAddRTO> addRTOList);

    /**
     * 租户更新RTO列表批量转换为实体列表
     * <p>复用单对象转换规则，实现批量映射</p>
     *
     * @param updateRTOList 租户更新请求对象集合
     * @return 租户实体集合
     */
    @IterableMapping(qualifiedByName = "toEntityFromUpdate")
    List<SysTenant> updateRTOListToEntityList(List<SysTenantUpdateRTO> updateRTOList);

    /**
     * 根据用户可操作字段列表过滤树形VO对象
     * <p>将不在visibleFields对应的VO字段设为null，确保前端仅接收有权限的数据</p>
     * <p>使用直接setter替代反射，性能更优且类型安全</p>
     *
     * @param treeVO        树形视图对象
     * @param visibleFields 用户可操作字段列表（DB列名）
     */
    default void filterTreeVoByVisibleFields(SysTenantTreeVO treeVO, List<String> visibleFields) {
        if (treeVO == null || visibleFields == null) return;

        // 构建允许的VO字段名集合
        Set<String> allowedVoFields = buildAllowedVoFields(visibleFields);

        // 使用直接setter过滤SysTenantCommonVO中的字段
        filterCommonVoFields(treeVO, allowedVoFields);

        // 过滤SysTenantTreeVO自身字段
        if (!allowedVoFields.contains("parentCode")) {
            treeVO.setParentCode(null);
        }
        // childTenant是结构性字段，不属于数据字段，不参与过滤

        // 递归过滤子节点
        if (treeVO.getChildTenant() != null) {
            for (SysTenantTreeVO child : treeVO.getChildTenant()) {
                filterTreeVoByVisibleFields(child, visibleFields);
            }
        }
    }

    /**
     * 批量过滤树形VO列表
     *
     * @param treeVOList    树形视图对象列表
     * @param visibleFields 用户可操作字段列表（DB列名）
     */
    default void filterTreeVoListByVisibleFields(List<SysTenantTreeVO> treeVOList, List<String> visibleFields) {
        if (treeVOList == null) return;
        for (SysTenantTreeVO treeVO : treeVOList) {
            filterTreeVoByVisibleFields(treeVO, visibleFields);
        }
    }

    /**
     * 构建允许的VO字段名集合：将DB列名转换为VO字段名
     *
     * @param visibleFields 用户可操作字段列表（DB列名）
     * @return 允许的VO字段名集合
     */
    private Set<String> buildAllowedVoFields(List<String> visibleFields) {
        Map<String, List<String>> dbColumnToVoFields = GlobalConstant.FieldPerm.DB_COLUMN_TO_VO_FIELDS;
        Set<String> allowedVoFields = new HashSet<>();
        for (String dbColumn : visibleFields) {
            List<String> voFields = dbColumnToVoFields.get(dbColumn);
            if (voFields != null) {
                allowedVoFields.addAll(voFields);
            }
        }
        return allowedVoFields;
    }

    /**
     * 使用直接setter过滤SysTenantCommonVO中的字段
     * <p>替代反射方式，性能更优且类型安全</p>
     *
     * @param vo              视图对象
     * @param allowedVoFields 允许的VO字段名集合
     */
    private void filterCommonVoFields(SysTenantCommonVO vo, Set<String> allowedVoFields) {
        if (vo == null) return;
        if (!allowedVoFields.contains("tenantCode")) vo.setTenantCode(null);
        if (!allowedVoFields.contains("tenantName")) vo.setTenantName(null);
        if (!allowedVoFields.contains("tenantType")) vo.setTenantType(null);
        if (!allowedVoFields.contains("parentName")) vo.setParentName(null);
        if (!allowedVoFields.contains("contactName")) vo.setContactName(null);
        if (!allowedVoFields.contains("contactPhone")) vo.setContactPhone(null);
        if (!allowedVoFields.contains("status")) vo.setStatus(null);
        if (!allowedVoFields.contains("expireTime")) vo.setExpireTime(null);
        if (!allowedVoFields.contains("hasChildren")) vo.setHasChildren(null);
        if (!allowedVoFields.contains("packageName")) vo.setPackageName(null);
        if (!allowedVoFields.contains("createByName")) vo.setCreateByName(null);
        if (!allowedVoFields.contains("createTime")) vo.setCreateTime(null);
        if (!allowedVoFields.contains("updateByName")) vo.setUpdateByName(null);
        if (!allowedVoFields.contains("updateTime")) vo.setUpdateTime(null);
        if (!allowedVoFields.contains("isDeleted")) vo.setIsDeleted(null);
        if (!allowedVoFields.contains("deleteTime")) vo.setDeleteTime(null);
    }


}
