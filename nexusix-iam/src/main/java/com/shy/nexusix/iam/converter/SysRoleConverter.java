package com.shy.nexusix.iam.converter;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shy.nexusix.common.enums.GlobalEnum.DataScope;
import com.shy.nexusix.common.enums.GlobalEnum.Deleted;
import com.shy.nexusix.common.enums.GlobalEnum.RoleLevel;
import com.shy.nexusix.common.enums.GlobalEnum.RoleStatus;
import com.shy.nexusix.iam.entity.SysRole;
import com.shy.nexusix.iam.rto.SysRoleAddRTO;
import com.shy.nexusix.iam.rto.SysRoleUpdateRTO;
import com.shy.nexusix.iam.vo.SysRoleCommonVO;
import com.shy.nexusix.iam.vo.SysRoleDetailVO;
import org.mapstruct.*;

import java.util.List;

/**
 * <p>
 * 角色信息转换器 - 负责角色实体、请求对象与视图对象之间的转换
 * </p>
 * <p>
 * 状态字段使用专用枚举 RoleStatus 进行转换，与通用 Status 枚举解耦
 * </p>
 *
 * @author shy
 * @since 2026-05-06
 */
@Mapper(componentModel = "spring")
public interface SysRoleConverter {

    @Named("toCommonVO")
    @Mapping(target = "roleLevel", source = "roleLevel", qualifiedByName = "intRoleLevelToDesc")
    @Mapping(target = "dataScope", source = "dataScope", qualifiedByName = "intDataScopeToDesc")
    @Mapping(target = "status", source = "status", qualifiedByName = "intRoleStatusToDesc")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysRoleCommonVO toCommonVO(SysRole role);

    @Mapping(target = "roleLevel", source = "roleLevel", qualifiedByName = "intRoleLevelToDesc")
    @Mapping(target = "dataScope", source = "dataScope", qualifiedByName = "intDataScopeToDesc")
    @Mapping(target = "status", source = "status", qualifiedByName = "intRoleStatusToDesc")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysRoleDetailVO toDetailVO(SysRole role);

    @Named("toEntityAdd")
    @Mapping(target = "roleLevel", qualifiedByName = "roleLevelToCode")
    @Mapping(target = "dataScope", qualifiedByName = "dataScopeToCode")
    @Mapping(target = "status", qualifiedByName = "roleStatusToCode")
    @Mapping(target = "isDeleted", qualifiedByName = "isDeletedToCode", source = "isDeleted")
    @Mapping(target = "tenantId", source = "tenantId", qualifiedByName = "stringToLong")
    SysRole toEntityAdd(SysRoleAddRTO rto);

    @Named("toEntityUpdate")
    @Mapping(target = "roleLevel", qualifiedByName = "roleLevelToCode")
    @Mapping(target = "dataScope", qualifiedByName = "dataScopeToCode")
    @Mapping(target = "status", qualifiedByName = "roleStatusToCode")
    @Mapping(target = "isDeleted", qualifiedByName = "isDeletedToCode", source = "isDeleted")
    @Mapping(target = "tenantId", source = "tenantId", qualifiedByName = "stringToLong")
    SysRole toEntityUpdate(SysRoleUpdateRTO rto);

    @IterableMapping(qualifiedByName = "toEntityAdd")
    List<SysRole> toEntityListAdd(List<SysRoleAddRTO> list);

    @IterableMapping(qualifiedByName = "toEntityUpdate")
    List<SysRole> toEntityListUpdate(List<SysRoleUpdateRTO> list);

    @IterableMapping(qualifiedByName = "toCommonVO")
    List<SysRoleCommonVO> toVoList(List<SysRole> list);

    default IPage<SysRoleCommonVO> toVOPage(IPage<SysRole> entityPage) {
        if (entityPage == null) {
            return null;
        }
        IPage<SysRoleCommonVO> voPage = new Page<>();
        voPage.setCurrent(entityPage.getCurrent());
        voPage.setSize(entityPage.getSize());
        voPage.setTotal(entityPage.getTotal());

        List<SysRoleCommonVO> voList = toVoList(entityPage.getRecords());
        voPage.setRecords(voList);

        return voPage;
    }

    @Named("roleLevelToCode")
    default Integer roleLevelToCode(RoleLevel roleLevel) {
        return roleLevel != null ? roleLevel.getCode() : null;
    }

    @Named("intRoleLevelToDesc")
    default String intRoleLevelToDesc(Integer code) {
        if (code == null) return null;
        RoleLevel roleLevel = RoleLevel.getByCode(code);
        return roleLevel != null ? roleLevel.getDesc() : null;
    }

    @Named("dataScopeToCode")
    default Integer dataScopeToCode(DataScope dataScope) {
        return dataScope != null ? dataScope.getCode() : null;
    }

    @Named("intDataScopeToDesc")
    default String intDataScopeToDesc(Integer code) {
        if (code == null) return null;
        DataScope dataScope = DataScope.getByCode(code);
        return dataScope != null ? dataScope.getDesc() : null;
    }

    /**
     * 将角色状态枚举转换为状态码
     * 角色状态使用专用枚举 RoleStatus，与通用 Status 解耦
     */
    @Named("roleStatusToCode")
    default Integer roleStatusToCode(RoleStatus status) {
        return status != null ? status.getCode() : null;
    }

    /**
     * 将整数状态码转换为角色状态描述字符串
     * 角色状态使用专用枚举 RoleStatus，与通用 Status 解耦
     */
    @Named("intRoleStatusToDesc")
    default String intRoleStatusToDesc(Integer code) {
        if (code == null) return null;
        RoleStatus status = RoleStatus.getByCode(code);
        return status != null ? status.getDesc() : null;
    }

    @Named("isDeletedToCode")
    default Integer isDeletedToCode(Deleted del) {
        return del != null ? del.getCode() : null;
    }

    @Named("intDeletedToDesc")
    default String intDeletedToDesc(Integer code) {
        if (code == null) return null;
        Deleted deleted = Deleted.getByCode(code);
        return deleted != null ? deleted.getDesc() : null;
    }

    @Named("stringToLong")
    default Long stringToLong(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return Long.parseLong(value);
    }

}
