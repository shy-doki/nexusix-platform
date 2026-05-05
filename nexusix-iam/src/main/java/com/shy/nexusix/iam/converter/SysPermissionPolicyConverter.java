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

/**
 * <p>
 * 权限策略对象转换器
 * </p>
 * <p>
 * 基于MapStruct实现Entity/RTO/VO之间的对象映射，
 * 包含目标类型、动作、删除标记等枚举与描述的互转逻辑。
     * permName和permCode需在Service层通过关联查询手动填充。
 * </p>
 *
 * @author shy
 * @since 2026-05-05
 */
@Mapper(componentModel = "spring")
public interface SysPermissionPolicyConverter {

    /**
     * Entity转CommonVO（列表展示）
     * 目标类型和动作自动转为中文描述，权限名称和标识需外部填充
     */
    @Named("toCommonVO")
    @Mapping(target = "targetType", source = "targetType", qualifiedByName = "intTargetTypeToDesc")
    @Mapping(target = "action", source = "action", qualifiedByName = "intActionToDesc")
    @Mapping(target = "permName", ignore = true)
    @Mapping(target = "permCode", ignore = true)
    SysPermissionPolicyCommonVO toCommonVO(SysPermissionPolicy entity);

    /**
     * Entity转DetailVO（详情展示）
     * 包含逻辑删除描述，权限名称和标识需外部填充
     */
    @Mapping(target = "targetType", source = "targetType", qualifiedByName = "intTargetTypeToDesc")
    @Mapping(target = "action", source = "action", qualifiedByName = "intActionToDesc")
    @Mapping(target = "permName", ignore = true)
    @Mapping(target = "permCode", ignore = true)
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysPermissionPolicyDetailVO toDetailVO(SysPermissionPolicy entity);

    /**
     * AddRTO转Entity（新增映射）
     * 目标类型和动作枚举自动转为数字编码
     */
    @Named("toEntityAdd")
    @Mapping(target = "targetType", qualifiedByName = "targetTypeToCode")
    @Mapping(target = "action", qualifiedByName = "actionToCode")
    SysPermissionPolicy toEntityAdd(SysPermissionPolicyAddRTO rto);

    /**
     * UpdateRTO转Entity（更新映射）
     * 目标类型和动作枚举自动转为数字编码
     */
    @Named("toEntityUpdate")
    @Mapping(target = "targetType", qualifiedByName = "targetTypeToCode")
    @Mapping(target = "action", qualifiedByName = "actionToCode")
    SysPermissionPolicy toEntityUpdate(SysPermissionPolicyUpdateRTO rto);

    /**
     * Entity列表转CommonVO列表
     */
    @IterableMapping(qualifiedByName = "toCommonVO")
    List<SysPermissionPolicyCommonVO> toVoList(List<SysPermissionPolicy> list);

    /**
     * AddRTO列表转Entity列表
     */
    @IterableMapping(qualifiedByName = "toEntityAdd")
    List<SysPermissionPolicy> toEntityListAdd(List<SysPermissionPolicyAddRTO> list);

    /**
     * Entity分页转VO分页
     * 保留分页参数，仅转换记录列表
     */
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

    /**
     * 目标类型编码转中文描述
     * 1-系统 2-租户 3-角色 4-用户
     */
    @Named("intTargetTypeToDesc")
    default String intTargetTypeToDesc(Integer code) {
        if (code == null) return null;
        GlobalEnum.TargetType type = GlobalEnum.TargetType.getByCode(code);
        return type != null ? type.getDesc() : null;
    }

    /**
     * 目标类型枚举转编码
     */
    @Named("targetTypeToCode")
    default Integer targetTypeToCode(GlobalEnum.TargetType type) {
        return type != null ? type.getCode() : null;
    }

    /**
     * 动作编码转中文描述
     * 1-允许 2-拒绝
     */
    @Named("intActionToDesc")
    default String intActionToDesc(Integer code) {
        if (code == null) return null;
        GlobalEnum.Action action = GlobalEnum.Action.getByCode(code);
        return action != null ? action.getDesc() : null;
    }

    /**
     * 动作枚举转编码
     */
    @Named("actionToCode")
    default Integer actionToCode(GlobalEnum.Action action) {
        return action != null ? action.getCode() : null;
    }

    /**
     * 删除标记编码转中文描述
     * 0-未删除 1-已删除
     */
    @Named("intDeletedToDesc")
    default String intDeletedToDesc(Integer code) {
        if (code == null) return null;
        GlobalEnum.Deleted deleted = GlobalEnum.Deleted.getByCode(code);
        return deleted != null ? deleted.getDesc() : null;
    }

}
