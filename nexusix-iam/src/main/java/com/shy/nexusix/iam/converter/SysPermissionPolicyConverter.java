package com.shy.nexusix.iam.converter;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shy.nexusix.common.enums.GlobalEnum.Action;
import com.shy.nexusix.common.enums.GlobalEnum.Deleted;
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
 *
 * @author shy
 * @since 2026-05-06
 */
@Mapper(componentModel = "spring")
public interface SysPermissionPolicyConverter {

    /**
     * <p>
     * 将权限策略实体转换为通用视图对象
     * </p>
     *
     * @param policy 权限策略实体
     * @return 权限策略通用视图对象
     * @author shy
     * @since 2026-05-06
     */
    @Named("toCommonVO")
    @Mapping(target = "targetType", source = "targetType", qualifiedByName = "intTargetTypeToDesc")
    @Mapping(target = "action", source = "action", qualifiedByName = "intActionToDesc")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysPermissionPolicyCommonVO toCommonVO(SysPermissionPolicy policy);

    /**
     * <p>
     * 将权限策略实体转换为详情视图对象
     * </p>
     *
     * @param policy 权限策略实体
     * @return 权限策略详情视图对象
     * @author shy
     * @since 2026-05-06
     */
    @Mapping(target = "targetType", source = "targetType", qualifiedByName = "intTargetTypeToDesc")
    @Mapping(target = "action", source = "action", qualifiedByName = "intActionToDesc")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysPermissionPolicyDetailVO toDetailVO(SysPermissionPolicy policy);

    /**
     * <p>
     * 将新增权限策略请求对象转换为权限策略实体
     * </p>
     *
     * @param rto 新增权限策略请求对象
     * @return 权限策略实体
     * @author shy
     * @since 2026-05-06
     */
    @Named("toEntityAdd")
    @Mapping(target = "targetType", qualifiedByName = "targetTypeToCode")
    @Mapping(target = "action", qualifiedByName = "actionToCode")
    @Mapping(target = "isDeleted", qualifiedByName = "isDeletedToCode", source = "isDeleted")
    @Mapping(target = "targetId", source = "targetId", qualifiedByName = "stringToLong")
    @Mapping(target = "permissionId", source = "permissionId", qualifiedByName = "stringToLong")
    SysPermissionPolicy toEntityAdd(SysPermissionPolicyAddRTO rto);

    /**
     * <p>
     * 将更新权限策略请求对象转换为权限策略实体
     * </p>
     *
     * @param rto 更新权限策略请求对象
     * @return 权限策略实体
     * @author shy
     * @since 2026-05-06
     */
    @Named("toEntityUpdate")
    @Mapping(target = "targetType", qualifiedByName = "targetTypeToCode")
    @Mapping(target = "action", qualifiedByName = "actionToCode")
    @Mapping(target = "isDeleted", qualifiedByName = "isDeletedToCode", source = "isDeleted")
    @Mapping(target = "targetId", source = "targetId", qualifiedByName = "stringToLong")
    @Mapping(target = "permissionId", source = "permissionId", qualifiedByName = "stringToLong")
    SysPermissionPolicy toEntityUpdate(SysPermissionPolicyUpdateRTO rto);

    /**
     * <p>
     * 将新增权限策略请求对象列表转换为权限策略实体列表
     * </p>
     *
     * @param list 新增权限策略请求对象列表
     * @return 权限策略实体列表，可用于批量持久化操作
     * @author shy
     * @since 2026-05-06
     */
    @IterableMapping(qualifiedByName = "toEntityAdd")
    List<SysPermissionPolicy> toEntityListAdd(List<SysPermissionPolicyAddRTO> list);

    /**
     * <p>
     * 将更新权限策略请求对象列表转换为权限策略实体列表
     * </p>
     *
     * @param list 更新权限策略请求对象列表
     * @return 权限策略实体列表，可用于批量更新操作
     * @author shy
     * @since 2026-05-06
     */
    @IterableMapping(qualifiedByName = "toEntityUpdate")
    List<SysPermissionPolicy> toEntityListUpdate(List<SysPermissionPolicyUpdateRTO> list);

    /**
     * <p>
     * 将权限策略实体列表转换为通用视图对象列表
     * </p>
     *
     * @param list 权限策略实体列表
     * @return 权限策略通用视图对象列表
     * @author shy
     * @since 2026-05-06
     */
    @IterableMapping(qualifiedByName = "toCommonVO")
    List<SysPermissionPolicyCommonVO> toVoList(List<SysPermissionPolicy> list);

    /**
     * <p>
     * 将权限策略实体分页对象转换为通用视图对象分页对象
     * </p>
     *
     * @param entityPage 权限策略实体分页对象
     * @return 权限策略通用视图对象分页对象
     * @author shy
     * @since 2026-05-06
     */
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

    /**
     * <p>
     * 将目标类型枚举转换为描述字符串
     * </p>
     *
     * @param targetType 目标类型枚举
     * @return 目标类型描述字符串
     * @author shy
     * @since 2026-05-06
     */
    @Named("targetTypeToDesc")
    default String targetTypeToDesc(TargetType targetType) {
        return targetType != null ? targetType.getDesc() : null;
    }

    /**
     * <p>
     * 将目标类型枚举转换为编码
     * </p>
     *
     * @param targetType 目标类型枚举
     * @return 目标类型编码
     * @author shy
     * @since 2026-05-06
     */
    @Named("targetTypeToCode")
    default Integer targetTypeToCode(TargetType targetType) {
        return targetType != null ? targetType.getCode() : null;
    }

    /**
     * <p>
     * 将整数目标类型编码转换为描述字符串
     * </p>
     *
     * @param code 目标类型编码
     * @return 目标类型描述字符串
     * @author shy
     * @since 2026-05-06
     */
    @Named("intTargetTypeToDesc")
    default String intTargetTypeToDesc(Integer code) {
        if (code == null) return null;
        TargetType targetType = TargetType.getByCode(code);
        return targetType != null ? targetType.getDesc() : null;
    }

    /**
     * <p>
     * 将动作枚举转换为描述字符串
     * </p>
     *
     * @param action 动作枚举
     * @return 动作描述字符串
     * @author shy
     * @since 2026-05-06
     */
    @Named("actionToDesc")
    default String actionToDesc(Action action) {
        return action != null ? action.getDesc() : null;
    }

    /**
     * <p>
     * 将动作枚举转换为编码
     * </p>
     *
     * @param action 动作枚举
     * @return 动作编码
     * @author shy
     * @since 2026-05-06
     */
    @Named("actionToCode")
    default Integer actionToCode(Action action) {
        return action != null ? action.getCode() : null;
    }

    /**
     * <p>
     * 将整数动作编码转换为描述字符串
     * </p>
     *
     * @param code 动作编码
     * @return 动作描述字符串
     * @author shy
     * @since 2026-05-06
     */
    @Named("intActionToDesc")
    default String intActionToDesc(Integer code) {
        if (code == null) return null;
        Action action = Action.getByCode(code);
        return action != null ? action.getDesc() : null;
    }

    /**
     * <p>
     * 将删除标记枚举转换为描述字符串
     * </p>
     *
     * @param del 删除标记枚举
     * @return 删除标记描述字符串
     * @author shy
     * @since 2026-05-06
     */
    @Named("isDeletedToDesc")
    default String isDeletedToDesc(Deleted del) {
        return del != null ? del.getDesc() : null;
    }

    /**
     * <p>
     * 将删除标记枚举转换为删除标记码
     * </p>
     *
     * @param del 删除标记枚举
     * @return 删除标记码
     * @author shy
     * @since 2026-05-06
     */
    @Named("isDeletedToCode")
    default Integer isDeletedToCode(Deleted del) {
        return del != null ? del.getCode() : null;
    }

    /**
     * <p>
     * 将整数删除标记码转换为删除标记描述字符串
     * </p>
     *
     * @param code 删除标记码
     * @return 删除标记描述字符串
     * @author shy
     * @since 2026-05-06
     */
    @Named("intDeletedToDesc")
    default String intDeletedToDesc(Integer code) {
        if (code == null) return null;
        Deleted deleted = Deleted.getByCode(code);
        return deleted != null ? deleted.getDesc() : null;
    }

    /**
     * <p>
     * 将 String 类型的 ID 转换为 Long 类型
     * </p>
     *
     * @param value String 类型的 ID
     * @return Long 类型的 ID
     * @author shy
     * @since 2026-05-06
     */
    @Named("stringToLong")
    default Long stringToLong(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return Long.parseLong(value);
    }

}
