package com.shy.nexusix.iam.converter;

import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.iam.entity.SysRole;
import com.shy.nexusix.iam.rto.SysRoleAddRTO;
import com.shy.nexusix.iam.rto.SysRoleUpdateRTO;
import com.shy.nexusix.iam.vo.SysRoleCommonVO;
import com.shy.nexusix.iam.vo.SysRoleDetailVO;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

/**
 * <p>角色对象转换器，基于MapStruct实现实体与VO互转</p>
 *
 * @author shy
 */
@Mapper(componentModel = "spring")
public interface SysRoleConverter {

    /**
     * <p>角色实体转换为公共VO</p>
     *
     * @param entity 角色实体
     * @return 角色通用VO
     */
    @Named("toCommonVO")
    @Mapping(source = "createBy", target = "createByName")
    @Mapping(source = "createAt", target = "createTime")
    @Mapping(source = "updateBy", target = "updateByName")
    @Mapping(source = "updateAt", target = "updateTime")
    @Mapping(source = "deletedAt", target = "deleteTime")
    @Mapping(source = "status", target = "status", qualifiedByName = "intStatusToDesc")
    @Mapping(source = "isDeleted", target = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysRoleCommonVO toCommonVO(SysRole entity);

    /**
     * <p>批量转换角色实体列表为VO列表</p>
     *
     * @param entityList 角色实体集合
     * @return 角色VO集合
     */
    @IterableMapping(qualifiedByName = "toCommonVO")
    List<SysRoleCommonVO> entityListToCommonVoList(List<SysRole> entityList);

    /**
     * <p>角色实体转换为详情VO</p>
     *
     * @param entity 角色实体
     * @return 角色详情VO
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
    SysRoleDetailVO toDetailVO(SysRole entity);

    /**
     * <p>状态码转描述</p>
     *
     * @param code 状态码
     * @return 状态描述
     */
    @Named("intStatusToDesc")
    default String intStatusToDesc(String code) {
        if (code == null) return null;
        GlobalEnum.RoleStatus status = GlobalEnum.RoleStatus.getByCode(code);
        return status != null ? status.getDesc() : null;
    }

    /**
     * <p>角色状态枚举转编码</p>
     *
     * @param status 角色状态枚举
     * @return 状态码
     */
    @Named("statusToCode")
    default String statusToCode(GlobalEnum.RoleStatus status) {
        return status != null ? status.getCode() : null;
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
     * <p>新增RTO转实体</p>
     *
     * @param addRTO 角色新增请求对象
     * @return 角色实体
     */
    @Named("toEntityFromAdd")
    @Mapping(source = "createByCode", target = "createBy")
    @Mapping(source = "createTime", target = "createAt")
    @Mapping(source = "updateByCode", target = "updateBy")
    @Mapping(source = "updateTime", target = "updateAt")
    @Mapping(source = "status", target = "status", qualifiedByName = "statusToCode")
    @Mapping(source = "isDeleted", target = "isDeleted", qualifiedByName = "isDeletedToCode")
    @Mapping(source = "deleteTime", target = "deletedAt")
    @Mapping(target = "id", ignore = true)
    SysRole toEntityFromAdd(SysRoleAddRTO addRTO);

    /**
     * <p>更新RTO转实体</p>
     *
     * @param updateRTO 角色更新请求对象
     * @return 角色实体
     */
    @Named("toEntityFromUpdate")
    @Mapping(source = "createByCode", target = "createBy")
    @Mapping(source = "createTime", target = "createAt")
    @Mapping(source = "updateByCode", target = "updateBy")
    @Mapping(source = "updateTime", target = "updateAt")
    @Mapping(source = "status", target = "status", qualifiedByName = "statusToCode")
    @Mapping(source = "isDeleted", target = "isDeleted", qualifiedByName = "isDeletedToCode")
    @Mapping(source = "deleteTime", target = "deletedAt")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roleCode", ignore = true)
    SysRole toEntityFromUpdate(SysRoleUpdateRTO updateRTO);

    /**
     * <p>批量新增RTO转实体列表</p>
     *
     * @param addRTOList 角色新增请求对象集合
     * @return 角色实体集合
     */
    @IterableMapping(qualifiedByName = "toEntityFromAdd")
    List<SysRole> addRTOListToEntityList(List<SysRoleAddRTO> addRTOList);

    /**
     * <p>批量更新RTO转实体列表</p>
     *
     * @param updateRTOList 角色更新请求对象集合
     * @return 角色实体集合
     */
    @IterableMapping(qualifiedByName = "toEntityFromUpdate")
    List<SysRole> updateRTOListToEntityList(List<SysRoleUpdateRTO> updateRTOList);

}
