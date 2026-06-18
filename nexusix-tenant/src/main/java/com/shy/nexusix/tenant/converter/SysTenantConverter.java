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

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>租户对象转换器，基于MapStruct实现实体与VO互转</p>
 *
 * @author shy
 */
@Mapper(componentModel = "spring")
public interface SysTenantConverter {

    /**
     * <p>租户实体转换为公共VO</p>
     *
     * @param entity 租户实体
     * @return 租户通用VO
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
     * <p>批量转换租户实体列表为VO列表</p>
     *
     * @param entityList 租户实体集合
     * @return 租户VO集合
     */
    @IterableMapping(qualifiedByName  = "toCommonVO")
    List<SysTenantCommonVO> entityListToCommonVoList(List<SysTenant> entityList);

    /**
     * <p>租户实体转换为详情VO</p>
     *
     * @param entity 租户实体
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
     * <p>租户实体转换为树形VO</p>
     *
     * @param entity 租户实体
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
     * <p>批量转换租户实体列表为树形VO列表</p>
     *
     * @param entityList 租户实体集合
     * @return 租户树形VO集合
     */
    @IterableMapping(qualifiedByName = "toTreeVO")
    List<SysTenantTreeVO> entityListToTreeVoList(List<SysTenant> entityList);

    /**
     * <p>租户状态枚举转描述</p>
     *
     * @param status 租户状态枚举
     * @return 状态描述
     */
    @Named("statusToDesc")
    default String statusToDesc(GlobalEnum.TenantStatus status) {
        return status != null ? status.getDesc() : null;
    }

    /**
     * <p>租户状态枚举转编码</p>
     *
     * @param status 租户状态枚举
     * @return 状态码
     */
    @Named("statusToCode")
    default String statusToCode(GlobalEnum.TenantStatus status) {
        return status != null ? status.getCode() : null;
    }

    /**
     * <p>状态码转描述</p>
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
     * <p>删除标记枚举转描述</p>
     *
     * @param del 删除标记枚举
     * @return 删除状态描述
     */
    @Named("isDeletedToDesc")
    default String isDeletedToDesc(GlobalEnum.Deleted del) {
        return del != null ? del.getDesc() : null;
    }

    /**
     * <p>删除标记枚举转编码</p>
     *
     * @param del 删除标记枚举
     * @return 删除标记编码
     */
    @Named("isDeletedToCode")
    default String isDeletedToCode(GlobalEnum.Deleted del) {
        return del != null ? del.getCode() : null;
    }

    /**
     * <p>删除标记编码转描述</p>
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
     * <p>字符串转Long</p>
     *
     * @param value 字符串值
     * @return Long值，输入为空时返回null
     */
    @Named("stringToLong")
    default Long stringToLong(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        return Long.parseLong(value.trim());
    }

    /**
     * <p>新增RTO转实体</p>
     *
     * @param addRTO 租户新增请求对象
     * @return 租户实体
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
     * <p>更新RTO转实体</p>
     *
     * @param updateRTO 租户更新请求对象
     * @return 租户实体
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
     * <p>批量新增RTO转实体列表</p>
     *
     * @param addRTOList 租户新增请求对象集合
     * @return 租户实体集合
     */
    @IterableMapping(qualifiedByName = "toEntityFromAdd")
    List<SysTenant> addRTOListToEntityList(List<SysTenantAddRTO> addRTOList);

    /**
     * <p>批量更新RTO转实体列表</p>
     *
     * @param updateRTOList 租户更新请求对象集合
     * @return 租户实体集合
     */
    @IterableMapping(qualifiedByName = "toEntityFromUpdate")
    List<SysTenant> updateRTOListToEntityList(List<SysTenantUpdateRTO> updateRTOList);

    /**
     * <p>根据可见字段过滤树形VO列表</p>
     *
     * @param treeVoList 树形VO列表
     * @param visibleFields 可见字段列表
     */
    default void filterTreeVoListByVisibleFields(List<SysTenantTreeVO> treeVoList, List<String> visibleFields) {
        if (treeVoList == null || treeVoList.isEmpty() || visibleFields == null) {
            return;
        }

        for (SysTenantTreeVO vo : treeVoList) {
            filterTreeVoByVisibleFields(vo, visibleFields);

            // 递归处理子节点
            if (vo.getChildTenant() != null && !vo.getChildTenant().isEmpty()) {
                filterTreeVoListByVisibleFields(vo.getChildTenant(), visibleFields);
            }
        }
    }

    /**
     * <p>过滤单个树形VO的字段</p>
     */
    default void filterTreeVoByVisibleFields(SysTenantTreeVO vo, List<String> visibleFields) {
        if (vo == null || visibleFields == null) {
            return;
        }

        Set<String> visibleFieldSet = new HashSet<>(visibleFields);

        // 根据可见字段控制，不可见的字段置为null
        if (!visibleFieldSet.contains("tenantCode")) vo.setTenantCode(null);
        if (!visibleFieldSet.contains("tenantName")) vo.setTenantName(null);
        if (!visibleFieldSet.contains("tenantType")) vo.setTenantType(null);
        if (!visibleFieldSet.contains("parentName")) vo.setParentName(null);
        if (!visibleFieldSet.contains("contactName")) vo.setContactName(null);
        if (!visibleFieldSet.contains("contactPhone")) vo.setContactPhone(null);
        if (!visibleFieldSet.contains("status")) vo.setStatus(null);
        if (!visibleFieldSet.contains("disableReason")) vo.setDisableReason(null);
        if (!visibleFieldSet.contains("expireTime")) vo.setExpireTime(null);
        if (!visibleFieldSet.contains("hasChildren")) vo.setHasChildren(null);
        if (!visibleFieldSet.contains("packageName")) vo.setPackageName(null);
        if (!visibleFieldSet.contains("createByName")) vo.setCreateByName(null);
        if (!visibleFieldSet.contains("createTime")) vo.setCreateTime(null);
        if (!visibleFieldSet.contains("updateByName")) vo.setUpdateByName(null);
        if (!visibleFieldSet.contains("updateTime")) vo.setUpdateTime(null);
        if (!visibleFieldSet.contains("isDeleted")) vo.setIsDeleted(null);
        if (!visibleFieldSet.contains("deleteTime")) vo.setDeleteTime(null);
        if (!visibleFieldSet.contains("parentCode")) vo.setParentCode(null);
    }

}
