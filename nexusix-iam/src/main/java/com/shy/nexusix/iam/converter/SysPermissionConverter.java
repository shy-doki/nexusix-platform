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

/**
 * <p>
 * 权限对象转换器
 * </p>
 * <p>
 * 基于MapStruct实现Entity/RTO/VO之间的对象映射，
 * 包含权限类型、状态、删除标记等枚举与描述的互转逻辑。
 * </p>
 *
 * @author shy
 * @since 2026-05-05
 */
@Mapper(componentModel = "spring")
public interface SysPermissionConverter {

    /**
     * Entity转CommonVO（列表展示）
     * 权限类型和状态自动转为中文描述
     */
    @Named("toCommonVO")
    @Mapping(target = "permType", source = "permType", qualifiedByName = "intPermTypeToDesc")
    @Mapping(target = "status", source = "status", qualifiedByName = "intStatusToDesc")
    SysPermissionCommonVO toCommonVO(SysPermission entity);

    /**
     * Entity转DetailVO（详情展示）
     * 包含逻辑删除描述
     */
    @Mapping(target = "permType", source = "permType", qualifiedByName = "intPermTypeToDesc")
    @Mapping(target = "status", source = "status", qualifiedByName = "intStatusToDesc")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysPermissionDetailVO toDetailVO(SysPermission entity);

    /**
     * AddRTO转Entity（新增映射）
     * 状态枚举自动转为数字编码
     */
    @Named("toEntityAdd")
    @Mapping(target = "status", qualifiedByName = "statusToCode")
    SysPermission toEntityAdd(SysPermissionAddRTO rto);

    /**
     * UpdateRTO转Entity（更新映射）
     * 状态枚举自动转为数字编码
     */
    @Named("toEntityUpdate")
    @Mapping(target = "status", qualifiedByName = "statusToCode")
    SysPermission toEntityUpdate(SysPermissionUpdateRTO rto);

    /**
     * Entity转TreeVO（树形展示）
     * 权限类型和状态自动转为中文描述
     */
    @Named("toTreeVO")
    @Mapping(target = "permType", source = "permType", qualifiedByName = "intPermTypeToDesc")
    @Mapping(target = "status", source = "status", qualifiedByName = "intStatusToDesc")
    SysPermissionTreeVO toTreeVO(SysPermission entity);

    /**
     * Entity列表转CommonVO列表
     */
    @IterableMapping(qualifiedByName = "toCommonVO")
    List<SysPermissionCommonVO> toVoList(List<SysPermission> list);

    /**
     * Entity列表转TreeVO列表
     */
    @IterableMapping(qualifiedByName = "toTreeVO")
    List<SysPermissionTreeVO> toTreeVOList(List<SysPermission> list);

    /**
     * AddRTO列表转Entity列表
     */
    @IterableMapping(qualifiedByName = "toEntityAdd")
    List<SysPermission> toEntityListAdd(List<SysPermissionAddRTO> list);

    /**
     * Entity分页转VO分页
     * 保留分页参数，仅转换记录列表
     */
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

    /**
     * 权限类型编码转中文描述
     * 1-菜单 2-按钮 3-接口 4-数据字段
     */
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
