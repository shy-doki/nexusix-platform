package com.shy.nexusix.iam.converter;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.iam.entity.SysPermissionPolicy;
import com.shy.nexusix.iam.rto.SysPermissionPolicyAddRTO;
import com.shy.nexusix.iam.rto.SysPermissionPolicyUpdateRTO;
import com.shy.nexusix.iam.vo.SysPermissionPolicyCommonVO;
import com.shy.nexusix.iam.vo.SysPermissionPolicyDetailVO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SysPermissionPolicyConverter {

    @Named("toCommonVO")
    @Mapping(target = "targetType", source = "targetType", qualifiedByName = "intTargetTypeToDesc")
    @Mapping(target = "action", source = "action", qualifiedByName = "intActionToDesc")
    @Mapping(target = "permName", ignore = true)
    @Mapping(target = "permCode", ignore = true)
    SysPermissionPolicyCommonVO toCommonVO(SysPermissionPolicy entity);

    @Mapping(target = "targetType", source = "targetType", qualifiedByName = "intTargetTypeToDesc")
    @Mapping(target = "action", source = "action", qualifiedByName = "intActionToDesc")
    @Mapping(target = "permName", ignore = true)
    @Mapping(target = "permCode", ignore = true)
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysPermissionPolicyDetailVO toDetailVO(SysPermissionPolicy entity);

    @Named("toEntityAdd")
    @Mapping(target = "targetType", qualifiedByName = "targetTypeToCode")
    @Mapping(target = "action", qualifiedByName = "actionToCode")
    SysPermissionPolicy toEntityAdd(SysPermissionPolicyAddRTO rto);

    @Named("toEntityUpdate")
    @Mapping(target = "targetType", qualifiedByName = "targetTypeToCode")
    @Mapping(target = "action", qualifiedByName = "actionToCode")
    SysPermissionPolicy toEntityUpdate(SysPermissionPolicyUpdateRTO rto);

    @IterableMapping(qualifiedByName = "toCommonVO")
    List<SysPermissionPolicyCommonVO> toVoList(List<SysPermissionPolicy> list);

    @IterableMapping(qualifiedByName = "toEntityAdd")
    List<SysPermissionPolicy> toEntityListAdd(List<SysPermissionPolicyAddRTO> list);

    default IPage<SysPermissionPolicyCommonVO> toVOPage(IPage<SysPermissionPolicy> entityPage) {
        if (entityPage == null) {
            return null;
        }
        IPage<SysPermissionPolicyCommonVO> voPage = new Page<>();
        voPage.setCurrent(entityPage.getCurrent());
        voPage.setSize(entityPage.getSize());
        voPage.setTotal(entityPage.getTotal());
        voPage.setRecords(toVoList(entityPage.getRecords()));
        return voPage;
    }

    @Named("intTargetTypeToDesc")
    default String intTargetTypeToDesc(Integer code) {
        if (code == null) return null;
        GlobalEnum.TargetType type = GlobalEnum.TargetType.getByCode(code);
        return type != null ? type.getDesc() : null;
    }

    @Named("targetTypeToCode")
    default Integer targetTypeToCode(GlobalEnum.TargetType type) {
        return type != null ? type.getCode() : null;
    }

    @Named("intActionToDesc")
    default String intActionToDesc(Integer code) {
        if (code == null) return null;
        GlobalEnum.Action action = GlobalEnum.Action.getByCode(code);
        return action != null ? action.getDesc() : null;
    }

    @Named("actionToCode")
    default Integer actionToCode(GlobalEnum.Action action) {
        return action != null ? action.getCode() : null;
    }

    @Named("intDeletedToDesc")
    default String intDeletedToDesc(Integer code) {
        if (code == null) return null;
        GlobalEnum.Deleted deleted = GlobalEnum.Deleted.getByCode(code);
        return deleted != null ? deleted.getDesc() : null;
    }

}
