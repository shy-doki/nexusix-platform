package com.shy.nexusix.iam.converter;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.iam.entity.SysPermission;
import com.shy.nexusix.iam.rto.SysPermissionAddRTO;
import com.shy.nexusix.iam.rto.SysPermissionUpdateRTO;
import com.shy.nexusix.iam.vo.SysPermissionCommonVO;
import com.shy.nexusix.iam.vo.SysPermissionDetailVO;
import com.shy.nexusix.iam.vo.SysPermissionTreeVO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SysPermissionConverter {

    @Named("toCommonVO")
    @Mapping(target = "permType", source = "permType", qualifiedByName = "intPermTypeToDesc")
    @Mapping(target = "status", source = "status", qualifiedByName = "intStatusToDesc")
    SysPermissionCommonVO toCommonVO(SysPermission entity);

    @Mapping(target = "permType", source = "permType", qualifiedByName = "intPermTypeToDesc")
    @Mapping(target = "status", source = "status", qualifiedByName = "intStatusToDesc")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysPermissionDetailVO toDetailVO(SysPermission entity);

    @Named("toEntityAdd")
    @Mapping(target = "status", qualifiedByName = "statusToCode")
    SysPermission toEntityAdd(SysPermissionAddRTO rto);

    @Named("toEntityUpdate")
    @Mapping(target = "status", qualifiedByName = "statusToCode")
    SysPermission toEntityUpdate(SysPermissionUpdateRTO rto);

    @Named("toTreeVO")
    @Mapping(target = "permType", source = "permType", qualifiedByName = "intPermTypeToDesc")
    @Mapping(target = "status", source = "status", qualifiedByName = "intStatusToDesc")
    SysPermissionTreeVO toTreeVO(SysPermission entity);

    @IterableMapping(qualifiedByName = "toCommonVO")
    List<SysPermissionCommonVO> toVoList(List<SysPermission> list);

    @IterableMapping(qualifiedByName = "toTreeVO")
    List<SysPermissionTreeVO> toTreeVOList(List<SysPermission> list);

    @IterableMapping(qualifiedByName = "toEntityAdd")
    List<SysPermission> toEntityListAdd(List<SysPermissionAddRTO> list);

    default IPage<SysPermissionCommonVO> toVOPage(IPage<SysPermission> entityPage) {
        if (entityPage == null) {
            return null;
        }
        IPage<SysPermissionCommonVO> voPage = new Page<>();
        voPage.setCurrent(entityPage.getCurrent());
        voPage.setSize(entityPage.getSize());
        voPage.setTotal(entityPage.getTotal());
        voPage.setRecords(toVoList(entityPage.getRecords()));
        return voPage;
    }

    @Named("intPermTypeToDesc")
    default String intPermTypeToDesc(Integer code) {
        if (code == null) return null;
        switch (code) {
            case 1: return "菜单";
            case 2: return "按钮";
            case 3: return "接口";
            case 4: return "数据字段";
            default: return null;
        }
    }

    @Named("intStatusToDesc")
    default String intStatusToDesc(Integer code) {
        if (code == null) return null;
        GlobalEnum.Status status = GlobalEnum.Status.getByCode(code);
        return status != null ? status.getDesc() : null;
    }

    @Named("statusToCode")
    default Integer statusToCode(GlobalEnum.Status status) {
        return status != null ? status.getCode() : null;
    }

    @Named("intDeletedToDesc")
    default String intDeletedToDesc(Integer code) {
        if (code == null) return null;
        GlobalEnum.Deleted deleted = GlobalEnum.Deleted.getByCode(code);
        return deleted != null ? deleted.getDesc() : null;
    }

}
