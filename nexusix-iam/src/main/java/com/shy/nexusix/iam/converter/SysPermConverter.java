package com.shy.nexusix.iam.converter;

import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.iam.entity.SysPerm;
import com.shy.nexusix.iam.rto.SysPermAddRTO;
import com.shy.nexusix.iam.rto.SysPermUpdateRTO;
import com.shy.nexusix.iam.vo.SysPermCommonVO;
import com.shy.nexusix.iam.vo.SysPermDetailVO;
import com.shy.nexusix.iam.vo.SysPermTreeVO;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>权限对象转换器，基于MapStruct实现实体与VO互转</p>
 *
 * @author shy
 */
@Mapper(componentModel = "spring")
public interface SysPermConverter {

    /**
     * <p>权限实体转换为公共VO</p>
     *
     * @param entity 权限实体
     * @return 权限通用VO
     */
    @Named("toCommonVO")
    @Mapping(source = "createBy", target = "createByName")
    @Mapping(source = "createAt", target = "createTime")
    @Mapping(source = "updateBy", target = "updateByName")
    @Mapping(source = "updateAt", target = "updateTime")
    @Mapping(source = "deletedAt", target = "deleteTime")
    @Mapping(source = "status", target = "status", qualifiedByName = "intStatusToDesc")
    @Mapping(source = "isDeleted", target = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysPermCommonVO toCommonVO(SysPerm entity);

    /**
     * <p>批量转换权限实体列表为VO列表</p>
     *
     * @param entityList 权限实体集合
     * @return 权限VO集合
     */
    @IterableMapping(qualifiedByName = "toCommonVO")
    List<SysPermCommonVO> entityListToCommonVoList(List<SysPerm> entityList);

    /**
     * <p>权限实体转换为详情VO</p>
     *
     * @param entity 权限实体
     * @return 权限详情VO
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
    SysPermDetailVO toDetailVO(SysPerm entity);

    /**
     * <p>权限实体转换为树形VO</p>
     *
     * @param entity 权限实体
     * @return 权限树形VO
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
    SysPermTreeVO toTreeVO(SysPerm entity);

    /**
     * <p>批量转换权限实体列表为树形VO列表</p>
     *
     * @param entityList 权限实体集合
     * @return 权限树形VO集合
     */
    @IterableMapping(qualifiedByName = "toTreeVO")
    List<SysPermTreeVO> entityListToTreeVoList(List<SysPerm> entityList);

    /**
     * <p>状态码转描述</p>
     *
     * @param code 状态码
     * @return 状态描述
     */
    @Named("intStatusToDesc")
    default String intStatusToDesc(String code) {
        if (code == null) return null;
        GlobalEnum.PermStatus status = GlobalEnum.PermStatus.getByCode(code);
        return status != null ? status.getDesc() : null;
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
     * <p>权限状态枚举转编码</p>
     *
     * @param status 权限状态枚举
     * @return 状态码
     */
    @Named("statusToCode")
    default String statusToCode(GlobalEnum.PermStatus status) {
        return status != null ? status.getCode() : null;
    }

    /**
     * <p>新增RTO转实体</p>
     *
     * @param addRTO 权限新增请求对象
     * @return 权限实体
     */
    @Named("toEntityFromAdd")
    @Mapping(source = "parentCode", target = "parentId", qualifiedByName = "stringToLong")
    @Mapping(source = "createByCode", target = "createBy")
    @Mapping(source = "createTime", target = "createAt")
    @Mapping(source = "updateByCode", target = "updateBy")
    @Mapping(source = "updateTime", target = "updateAt")
    @Mapping(source = "status", target = "status", qualifiedByName = "statusToCode")
    @Mapping(source = "isDeleted", target = "isDeleted", qualifiedByName = "isDeletedToCode")
    @Mapping(source = "deleteTime", target = "deletedAt")
    @Mapping(target = "id", ignore = true)
    SysPerm toEntityFromAdd(SysPermAddRTO addRTO);

    /**
     * <p>更新RTO转实体</p>
     *
     * @param updateRTO 权限更新请求对象
     * @return 权限实体
     */
    @Named("toEntityFromUpdate")
    @Mapping(source = "parentCode", target = "parentId", qualifiedByName = "stringToLong")
    @Mapping(source = "createByCode", target = "createBy")
    @Mapping(source = "createTime", target = "createAt")
    @Mapping(source = "updateByCode", target = "updateBy")
    @Mapping(source = "updateTime", target = "updateAt")
    @Mapping(source = "status", target = "status", qualifiedByName = "statusToCode")
    @Mapping(source = "isDeleted", target = "isDeleted", qualifiedByName = "isDeletedToCode")
    @Mapping(source = "deleteTime", target = "deletedAt")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "permCode", ignore = true)
    SysPerm toEntityFromUpdate(SysPermUpdateRTO updateRTO);

    /**
     * <p>批量新增RTO转实体列表</p>
     *
     * @param addRTOList 权限新增请求对象集合
     * @return 权限实体集合
     */
    @IterableMapping(qualifiedByName = "toEntityFromAdd")
    List<SysPerm> addRTOListToEntityList(List<SysPermAddRTO> addRTOList);

    /**
     * <p>批量更新RTO转实体列表</p>
     *
     * @param updateRTOList 权限更新请求对象集合
     * @return 权限实体集合
     */
    @IterableMapping(qualifiedByName = "toEntityFromUpdate")
    List<SysPerm> updateRTOListToEntityList(List<SysPermUpdateRTO> updateRTOList);

    /**
     * <p>根据可见字段过滤树形VO列表</p>
     *
     * @param treeVoList 树形VO列表
     * @param visibleFields 可见字段列表
     */
    default void filterTreeVoListByVisibleFields(List<SysPermTreeVO> treeVoList, List<String> visibleFields) {
        if (treeVoList == null || treeVoList.isEmpty() || visibleFields == null) {
            return;
        }

        for (SysPermTreeVO vo : treeVoList) {
            filterTreeVoByVisibleFields(vo, visibleFields);

            // 递归处理子节点
            if (vo.getChildPerm() != null && !vo.getChildPerm().isEmpty()) {
                filterTreeVoListByVisibleFields(vo.getChildPerm(), visibleFields);
            }
        }
    }

    /**
     * <p>过滤单个树形VO的字段</p>
     */
    default void filterTreeVoByVisibleFields(SysPermTreeVO vo, List<String> visibleFields) {
        if (vo == null || visibleFields == null) {
            return;
        }

        Set<String> visibleFieldSet = new HashSet<>(visibleFields);

        // 根据可见字段控制，不可见的字段置为null
        if (!visibleFieldSet.contains("permCode")) vo.setPermCode(null);
        if (!visibleFieldSet.contains("permName")) vo.setPermName(null);
        if (!visibleFieldSet.contains("permType")) vo.setPermType(null);
        if (!visibleFieldSet.contains("parentName")) vo.setParentName(null);
        if (!visibleFieldSet.contains("resourceType")) vo.setResourceType(null);
        if (!visibleFieldSet.contains("resourcePath")) vo.setResourcePath(null);
        if (!visibleFieldSet.contains("resourceMethod")) vo.setResourceMethod(null);
        if (!visibleFieldSet.contains("icon")) vo.setIcon(null);
        if (!visibleFieldSet.contains("sortOrder")) vo.setSortOrder(null);
        if (!visibleFieldSet.contains("isVisible")) vo.setIsVisible(null);
        if (!visibleFieldSet.contains("status")) vo.setStatus(null);
        if (!visibleFieldSet.contains("disableReason")) vo.setDisableReason(null);
        if (!visibleFieldSet.contains("createByName")) vo.setCreateByName(null);
        if (!visibleFieldSet.contains("createTime")) vo.setCreateTime(null);
        if (!visibleFieldSet.contains("updateByName")) vo.setUpdateByName(null);
        if (!visibleFieldSet.contains("updateTime")) vo.setUpdateTime(null);
        if (!visibleFieldSet.contains("isDeleted")) vo.setIsDeleted(null);
        if (!visibleFieldSet.contains("deleteTime")) vo.setDeleteTime(null);
        if (!visibleFieldSet.contains("parentCode")) vo.setParentCode(null);
    }

}
