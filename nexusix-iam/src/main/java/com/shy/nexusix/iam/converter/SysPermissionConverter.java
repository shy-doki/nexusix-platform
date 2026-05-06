package com.shy.nexusix.iam.converter;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shy.nexusix.common.enums.GlobalEnum.Deleted;
import com.shy.nexusix.common.enums.GlobalEnum.Status;
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
 *
 * @author shy
 * @since 2026-05-06
 */
@Mapper(componentModel = "spring")
public interface SysPermissionConverter {

    /**
     * <p>
     * 将权限实体转换为通用视图对象
     * </p>
     *
     * @param permission 权限实体
     * @return 权限通用视图对象
     * @author shy
     * @since 2026-05-06
     */
    @Named("toCommonVO")
    @Mapping(target = "status", source = "status", qualifiedByName = "intStatusToDesc")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysPermissionCommonVO toCommonVO(SysPermission permission);

    /**
     * <p>
     * 将权限实体转换为详情视图对象
     * </p>
     *
     * @param permission 权限实体
     * @return 权限详情视图对象
     * @author shy
     * @since 2026-05-06
     */
    @Mapping(target = "status", source = "status", qualifiedByName = "intStatusToDesc")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysPermissionDetailVO toDetailVO(SysPermission permission);

    /**
     * <p>
     * 将新增权限请求对象转换为权限实体
     * </p>
     *
     * @param rto 新增权限请求对象
     * @return 权限实体
     * @author shy
     * @since 2026-05-06
     */
    @Named("toEntityAdd")
    @Mapping(target = "status", qualifiedByName = "statusToCode")
    @Mapping(target = "isDeleted", qualifiedByName = "isDeletedToCode", source = "isDeleted")
    @Mapping(target = "parentId", source = "parentId", qualifiedByName = "stringToLong")
    SysPermission toEntityAdd(SysPermissionAddRTO rto);

    /**
     * <p>
     * 将更新权限请求对象转换为权限实体
     * </p>
     *
     * @param rto 更新权限请求对象
     * @return 权限实体
     * @author shy
     * @since 2026-05-06
     */
    @Named("toEntityUpdate")
    @Mapping(target = "status", qualifiedByName = "statusToCode")
    @Mapping(target = "isDeleted", qualifiedByName = "isDeletedToCode", source = "isDeleted")
    @Mapping(target = "parentId", source = "parentId", qualifiedByName = "stringToLong")
    SysPermission toEntityUpdate(SysPermissionUpdateRTO rto);

    /**
     * <p>
     * 将新增权限请求对象列表转换为权限实体列表
     * </p>
     *
     * @param list 新增权限请求对象列表
     * @return 权限实体列表，可用于批量持久化操作
     * @author shy
     * @since 2026-05-06
     */
    @IterableMapping(qualifiedByName = "toEntityAdd")
    List<SysPermission> toEntityListAdd(List<SysPermissionAddRTO> list);

    /**
     * <p>
     * 将更新权限请求对象列表转换为权限实体列表
     * </p>
     *
     * @param list 更新权限请求对象列表
     * @return 权限实体列表，可用于批量更新操作
     * @author shy
     * @since 2026-05-06
     */
    @IterableMapping(qualifiedByName = "toEntityUpdate")
    List<SysPermission> toEntityListUpdate(List<SysPermissionUpdateRTO> list);

    /**
     * <p>
     * 将权限实体列表转换为通用视图对象列表
     * </p>
     *
     * @param list 权限实体列表
     * @return 权限通用视图对象列表
     * @author shy
     * @since 2026-05-06
     */
    @IterableMapping(qualifiedByName = "toCommonVO")
    List<SysPermissionCommonVO> toVoList(List<SysPermission> list);

    /**
     * <p>
     * 将权限实体分页对象转换为通用视图对象分页对象
     * </p>
     *
     * @param entityPage 权限实体分页对象
     * @return 权限通用视图对象分页对象
     * @author shy
     * @since 2026-05-06
     */
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

    /**
     * <p>
     * 将权限状态枚举转换为描述字符串
     * </p>
     *
     * @param status 权限状态枚举
     * @return 状态描述字符串
     * @author shy
     * @since 2026-05-06
     */
    @Named("statusToDesc")
    default String statusToDesc(Status status) {
        return status != null ? status.getDesc() : null;
    }

    /**
     * <p>
     * 将权限状态枚举转换为状态码
     * </p>
     *
     * @param status 权限状态枚举
     * @return 状态码
     * @author shy
     * @since 2026-05-06
     */
    @Named("statusToCode")
    default Integer statusToCode(Status status) {
        return status != null ? status.getCode() : null;
    }

    /**
     * <p>
     * 将整数状态码转换为状态描述字符串
     * </p>
     *
     * @param code 状态码
     * @return 状态描述字符串
     * @author shy
     * @since 2026-05-06
     */
    @Named("intStatusToDesc")
    default String intStatusToDesc(Integer code) {
        if (code == null) return null;
        Status status = Status.getByCode(code);
        return status != null ? status.getDesc() : null;
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
