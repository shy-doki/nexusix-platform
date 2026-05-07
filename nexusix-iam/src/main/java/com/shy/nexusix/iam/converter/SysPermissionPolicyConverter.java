package com.shy.nexusix.iam.converter;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shy.nexusix.common.enums.GlobalEnum.Deleted;
import com.shy.nexusix.common.enums.GlobalEnum.PolicyAction;
import com.shy.nexusix.common.enums.GlobalEnum.TargetType;
import com.shy.nexusix.iam.entity.SysPermissionPolicy;
import com.shy.nexusix.iam.rto.SysPermissionPolicyAddRTO;
import com.shy.nexusix.iam.rto.SysPermissionPolicyUpdateRTO;
import com.shy.nexusix.iam.vo.SysPermissionPolicyCommonVO;
import com.shy.nexusix.iam.vo.SysPermissionPolicyDetailVO;
import org.mapstruct.*;

import java.util.List;

/**
 * <p>
 * 权限策略信息转换器 - 负责权限策略实体、请求对象与视图对象之间的转换
 * </p>
 * <p>
 * 动作字段使用专用枚举 PolicyAction 进行转换，与通用 Action 枚举解耦
 * </p>
 *
 * @author shy
 * @since 2026-05-06
 */
@Mapper(componentModel = "spring")
public interface SysPermissionPolicyConverter {

    @Named("toCommonVO")
    @Mapping(target = "targetType", source = "targetType", qualifiedByName = "intTargetTypeToDesc")
    @Mapping(target = "action", source = "action", qualifiedByName = "intPolicyActionToDesc")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysPermissionPolicyCommonVO toCommonVO(SysPermissionPolicy policy);

    @Mapping(target = "targetType", source = "targetType", qualifiedByName = "intTargetTypeToDesc")
    @Mapping(target = "action", source = "action", qualifiedByName = "intPolicyActionToDesc")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysPermissionPolicyDetailVO toDetailVO(SysPermissionPolicy policy);

    @Named("toEntityAdd")
    @Mapping(target = "targetType", qualifiedByName = "targetTypeToCode")
    @Mapping(target = "action", qualifiedByName = "policyActionToCode")
    @Mapping(target = "isDeleted", qualifiedByName = "isDeletedToCode", source = "isDeleted")
    @Mapping(target = "targetId", source = "targetId", qualifiedByName = "stringToLong")
    @Mapping(target = "permissionId", source = "permissionId", qualifiedByName = "stringToLong")
    SysPermissionPolicy toEntityAdd(SysPermissionPolicyAddRTO rto);

    @Named("toEntityUpdate")
    @Mapping(target = "targetType", qualifiedByName = "targetTypeToCode")
    @Mapping(target = "action", qualifiedByName = "policyActionToCode")
    @Mapping(target = "isDeleted", qualifiedByName = "isDeletedToCode", source = "isDeleted")
    @Mapping(target = "targetId", source = "targetId", qualifiedByName = "stringToLong")
    @Mapping(target = "permissionId", source = "permissionId", qualifiedByName = "stringToLong")
    SysPermissionPolicy toEntityUpdate(SysPermissionPolicyUpdateRTO rto);

    @IterableMapping(qualifiedByName = "toEntityAdd")
    List<SysPermissionPolicy> toEntityListAdd(List<SysPermissionPolicyAddRTO> list);

    @IterableMapping(qualifiedByName = "toEntityUpdate")
    List<SysPermissionPolicy> toEntityListUpdate(List<SysPermissionPolicyUpdateRTO> list);

    @IterableMapping(qualifiedByName = "toCommonVO")
    List<SysPermissionPolicyCommonVO> toVoList(List<SysPermissionPolicy> list);

    default IPage<SysPermissionPolicyCommonVO> toVOPage(IPage<SysPermissionPolicy> entityPage) {
        if (entityPage == null) {
            return null;
        }
        IPage<SysPermissionPolicyCommonVO> voPage = new Page<>();
        voPage.setCurrent(entityPage.getCurrent());
        voPage.setSize(entityPage.getSize());
        voPage.setTotal(entityPage.getTotal());

        List<SysPermissionPolicyCommonVO> voList = toVoList(entityPage.getRecords());
        voPage.setRecords(voList);

        return voPage;
    }

    @Named("targetTypeToCode")
    default Integer targetTypeToCode(TargetType targetType) {
        return targetType != null ? targetType.getCode() : null;
    }

    @Named("intTargetTypeToDesc")
    default String intTargetTypeToDesc(Integer code) {
        if (code == null) return null;
        TargetType targetType = TargetType.getByCode(code);
        return targetType != null ? targetType.getDesc() : null;
    }

    /**
     * 将权限策略动作枚举转换为编码
     * 权限策略动作使用专用枚举 PolicyAction，与通用 Action 解耦
     */
    @Named("policyActionToCode")
    default Integer policyActionToCode(PolicyAction action) {
        return action != null ? action.getCode() : null;
    }

    /**
     * 将整数动作编码转换为权限策略动作描述字符串
     * 权限策略动作使用专用枚举 PolicyAction，与通用 Action 解耦
     */
    @Named("intPolicyActionToDesc")
    default String intPolicyActionToDesc(Integer code) {
        if (code == null) return null;
        PolicyAction action = PolicyAction.getByCode(code);
        return action != null ? action.getDesc() : null;
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
