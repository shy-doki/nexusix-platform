package com.shy.nexusix.iam.converter;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shy.nexusix.common.enums.GlobalEnum.Deleted;
import com.shy.nexusix.common.enums.GlobalEnum.PermStatus;
import com.shy.nexusix.common.enums.GlobalEnum.PermType;
import com.shy.nexusix.iam.entity.SysPermission;
import com.shy.nexusix.iam.rto.SysPermissionAddRTO;
import com.shy.nexusix.iam.rto.SysPermissionUpdateRTO;
import com.shy.nexusix.iam.vo.SysPermissionCommonVO;
import com.shy.nexusix.iam.vo.SysPermissionDetailVO;
import org.mapstruct.*;

import java.util.List;

/**
 * <p>
 * 权限信息转换器 - 负责权限实体、请求对象与视图对象之间的转换
 * </p>
 * <p>
 * 状态字段使用专用枚举 PermStatus 进行转换，与通用 Status 枚举解耦
 * </p>
 *
 * @author shy
 * @since 2026-05-06
 */
@Mapper(componentModel = "spring")
public interface SysPermissionConverter {

    @Named("toCommonVO")
    @Mapping(target = "permType", source = "permType", qualifiedByName = "intPermTypeToDesc")
    @Mapping(target = "status", source = "status", qualifiedByName = "intPermStatusToDesc")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysPermissionCommonVO toCommonVO(SysPermission permission);

    @Mapping(target = "permType", source = "permType", qualifiedByName = "intPermTypeToDesc")
    @Mapping(target = "status", source = "status", qualifiedByName = "intPermStatusToDesc")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysPermissionDetailVO toDetailVO(SysPermission permission);

    @Named("toEntityAdd")
    @Mapping(target = "permType", qualifiedByName = "permTypeToCode")
    @Mapping(target = "status", qualifiedByName = "permStatusToCode")
    @Mapping(target = "isDeleted", qualifiedByName = "isDeletedToCode", source = "isDeleted")
    @Mapping(target = "parentId", source = "parentId", qualifiedByName = "stringToLong")
    SysPermission toEntityAdd(SysPermissionAddRTO rto);

    @Named("toEntityUpdate")
    @Mapping(target = "permType", qualifiedByName = "permTypeToCode")
    @Mapping(target = "status", qualifiedByName = "permStatusToCode")
    @Mapping(target = "isDeleted", qualifiedByName = "isDeletedToCode", source = "isDeleted")
    @Mapping(target = "parentId", source = "parentId", qualifiedByName = "stringToLong")
    SysPermission toEntityUpdate(SysPermissionUpdateRTO rto);

    @IterableMapping(qualifiedByName = "toEntityAdd")
    List<SysPermission> toEntityListAdd(List<SysPermissionAddRTO> list);

    @IterableMapping(qualifiedByName = "toEntityUpdate")
    List<SysPermission> toEntityListUpdate(List<SysPermissionUpdateRTO> list);

    @IterableMapping(qualifiedByName = "toCommonVO")
    List<SysPermissionCommonVO> toVoList(List<SysPermission> list);

    default IPage<SysPermissionCommonVO> toVOPage(IPage<SysPermission> entityPage) {
        if (entityPage == null) {
            return null;
        }
        IPage<SysPermissionCommonVO> voPage = new Page<>();
        voPage.setCurrent(entityPage.getCurrent());
        voPage.setSize(entityPage.getSize());
        voPage.setTotal(entityPage.getTotal());

        List<SysPermissionCommonVO> voList = toVoList(entityPage.getRecords());
        voPage.setRecords(voList);

        return voPage;
    }

    @Named("permTypeToCode")
    default Integer permTypeToCode(PermType permType) {
        return permType != null ? permType.getCode() : null;
    }

    @Named("intPermTypeToDesc")
    default String intPermTypeToDesc(Integer code) {
        if (code == null) return null;
        PermType permType = PermType.getByCode(code);
        return permType != null ? permType.getDesc() : null;
    }

    /**
     * 将权限状态枚举转换为状态码
     * 权限状态使用专用枚举 PermStatus，与通用 Status 解耦
     */
    @Named("permStatusToCode")
    default Integer permStatusToCode(PermStatus status) {
        return status != null ? status.getCode() : null;
    }

    /**
     * 将整数状态码转换为权限状态描述字符串
     * 权限状态使用专用枚举 PermStatus，与通用 Status 解耦
     */
    @Named("intPermStatusToDesc")
    default String intPermStatusToDesc(Integer code) {
        if (code == null) return null;
        PermStatus status = PermStatus.getByCode(code);
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
