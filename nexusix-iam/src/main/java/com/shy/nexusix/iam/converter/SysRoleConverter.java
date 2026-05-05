package com.shy.nexusix.iam.converter;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.iam.entity.SysRole;
import com.shy.nexusix.iam.rto.SysRoleAddRTO;
import com.shy.nexusix.iam.rto.SysRoleUpdateRTO;
import com.shy.nexusix.iam.vo.SysRoleCommonVO;
import com.shy.nexusix.iam.vo.SysRoleDetailVO;
import org.mapstruct.*;

import java.util.List;

/**
 * <p>
 * 角色对象转换器
 * </p>
 * <p>
 * 基于MapStruct实现Entity/RTO/VO之间的对象映射，
 * 包含角色层级、数据范围、状态、删除标记等枚举与描述的互转逻辑。
 * </p>
 *
 * @author shy
 * @since 2026-05-05
 */
@Mapper(componentModel = "spring")
public interface SysRoleConverter {

    /**
     * Entity转CommonVO（列表展示）
     * 角色层级、数据范围和状态自动转为中文描述
     */
    @Named("toCommonVO")
    @Mapping(target = "roleLevel", source = "roleLevel", qualifiedByName = "intRoleLevelToDesc")
    @Mapping(target = "dataScope", source = "dataScope", qualifiedByName = "intDataScopeToDesc")
    @Mapping(target = "status", source = "status", qualifiedByName = "intStatusToDesc")
    SysRoleCommonVO toCommonVO(SysRole entity);

    /**
     * Entity转DetailVO（详情展示）
     * 包含逻辑删除描述
     */
    @Mapping(target = "roleLevel", source = "roleLevel", qualifiedByName = "intRoleLevelToDesc")
    @Mapping(target = "dataScope", source = "dataScope", qualifiedByName = "intDataScopeToDesc")
    @Mapping(target = "status", source = "status", qualifiedByName = "intStatusToDesc")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysRoleDetailVO toDetailVO(SysRole entity);

    /**
     * AddRTO转Entity（新增映射）
     * 枚举自动转为数字编码
     */
    @Named("toEntityAdd")
    @Mapping(target = "roleLevel", qualifiedByName = "roleLevelToCode")
    @Mapping(target = "dataScope", qualifiedByName = "dataScopeToCode")
    @Mapping(target = "status", qualifiedByName = "statusToCode")
    SysRole toEntityAdd(SysRoleAddRTO rto);

    /**
     * UpdateRTO转Entity（更新映射）
     * 枚举自动转为数字编码
     */
    @Named("toEntityUpdate")
    @Mapping(target = "roleLevel", qualifiedByName = "roleLevelToCode")
    @Mapping(target = "dataScope", qualifiedByName = "dataScopeToCode")
    @Mapping(target = "status", qualifiedByName = "statusToCode")
    SysRole toEntityUpdate(SysRoleUpdateRTO rto);

    /**
     * Entity列表转CommonVO列表
     */
    @IterableMapping(qualifiedByName = "toCommonVO")
    List<SysRoleCommonVO> toVoList(List<SysRole> list);

    /**
     * AddRTO列表转Entity列表
     */
    @IterableMapping(qualifiedByName = "toEntityAdd")
    List<SysRole> toEntityListAdd(List<SysRoleAddRTO> list);

    /**
     * Entity分页转VO分页
     * 保留分页参数，仅转换记录列表
     */
    default IPage<SysRoleCommonVO> toVOPage(IPage<SysRole> entityPage) {
        if (entityPage == null) {
            return null;
        }
        IPage<SysRoleCommonVO> voPage = new Page<>();
        voPage.setCurrent(entityPage.getCurrent());
        voPage.setSize(entityPage.getSize());
        voPage.setTotal(entityPage.getTotal());
        voPage.setRecords(toVoList(entityPage.getRecords()));
        return voPage;
    }

    /**
     * 角色层级编码转中文描述
     * 1-系统 2-租户 3-用户
     */
    @Named("intRoleLevelToDesc")
    default String intRoleLevelToDesc(Integer code) {
        if (code == null) return null;
        GlobalEnum.RoleLevel level = GlobalEnum.RoleLevel.getByCode(code);
        return level != null ? level.getDesc() : null;
    }

    /**
     * 角色层级枚举转编码
     */
    @Named("roleLevelToCode")
    default Integer roleLevelToCode(GlobalEnum.RoleLevel level) {
        return level != null ? level.getCode() : null;
    }

    /**
     * 数据范围编码转中文描述
     * 1-全部 2-本部门 3-本人 4-自定义
     */
    @Named("intDataScopeToDesc")
    default String intDataScopeToDesc(Integer code) {
        if (code == null) return null;
        GlobalEnum.DataScope scope = GlobalEnum.DataScope.getByCode(code);
        return scope != null ? scope.getDesc() : null;
    }

    /**
     * 数据范围枚举转编码
     */
    @Named("dataScopeToCode")
    default Integer dataScopeToCode(GlobalEnum.DataScope scope) {
        return scope != null ? scope.getCode() : null;
    }

    /**
     * 状态编码转中文描述
     * 1-启用 0-禁用
     */
    @Named("intStatusToDesc")
    default String intStatusToDesc(Integer code) {
        if (code == null) return null;
        GlobalEnum.Status status = GlobalEnum.Status.getByCode(code);
        return status != null ? status.getDesc() : null;
    }

    /**
     * 状态枚举转编码
     */
    @Named("statusToCode")
    default Integer statusToCode(GlobalEnum.Status status) {
        return status != null ? status.getCode() : null;
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
