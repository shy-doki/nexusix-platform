package com.shy.nexusix.iam.converter;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shy.nexusix.common.enums.GlobalEnum.DataScope;
import com.shy.nexusix.common.enums.GlobalEnum.Deleted;
import com.shy.nexusix.common.enums.GlobalEnum.RoleLevel;
import com.shy.nexusix.common.enums.GlobalEnum.Status;
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
 *
 * @author shy
 * @since 2026-05-06
 */
@Mapper(componentModel = "spring")
public interface SysRoleConverter {

    /**
     * <p>
     * 将角色实体转换为通用视图对象
     * </p>
     *
     * @param role 角色实体
     * @return 角色通用视图对象
     * @author shy
     * @since 2026-05-06
     */
    @Named("toCommonVO")
    @Mapping(target = "roleLevel", source = "roleLevel", qualifiedByName = "intRoleLevelToDesc")
    @Mapping(target = "dataScope", source = "dataScope", qualifiedByName = "intDataScopeToDesc")
    @Mapping(target = "status", source = "status", qualifiedByName = "intStatusToDesc")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysRoleCommonVO toCommonVO(SysRole role);

    /**
     * <p>
     * 将角色实体转换为详情视图对象
     * </p>
     *
     * @param role 角色实体
     * @return 角色详情视图对象
     * @author shy
     * @since 2026-05-06
     */
    @Mapping(target = "roleLevel", source = "roleLevel", qualifiedByName = "intRoleLevelToDesc")
    @Mapping(target = "dataScope", source = "dataScope", qualifiedByName = "intDataScopeToDesc")
    @Mapping(target = "status", source = "status", qualifiedByName = "intStatusToDesc")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysRoleDetailVO toDetailVO(SysRole role);

    /**
     * <p>
     * 将新增角色请求对象转换为角色实体
     * </p>
     *
     * @param rto 新增角色请求对象
     * @return 角色实体
     * @author shy
     * @since 2026-05-06
     */
    @Named("toEntityAdd")
    @Mapping(target = "roleLevel", qualifiedByName = "roleLevelToCode")
    @Mapping(target = "dataScope", qualifiedByName = "dataScopeToCode")
    @Mapping(target = "status", qualifiedByName = "statusToCode")
    @Mapping(target = "isDeleted", qualifiedByName = "isDeletedToCode", source = "isDeleted")
    @Mapping(target = "tenantId", source = "tenantId", qualifiedByName = "stringToLong")
    SysRole toEntityAdd(SysRoleAddRTO rto);

    /**
     * <p>
     * 将更新角色请求对象转换为角色实体
     * </p>
     *
     * @param rto 更新角色请求对象
     * @return 角色实体
     * @author shy
     * @since 2026-05-06
     */
    @Named("toEntityUpdate")
    @Mapping(target = "roleLevel", qualifiedByName = "roleLevelToCode")
    @Mapping(target = "dataScope", qualifiedByName = "dataScopeToCode")
    @Mapping(target = "status", qualifiedByName = "statusToCode")
    @Mapping(target = "isDeleted", qualifiedByName = "isDeletedToCode", source = "isDeleted")
    @Mapping(target = "tenantId", source = "tenantId", qualifiedByName = "stringToLong")
    SysRole toEntityUpdate(SysRoleUpdateRTO rto);

    /**
     * <p>
     * 将新增角色请求对象列表转换为角色实体列表
     * </p>
     *
     * @param list 新增角色请求对象列表
     * @return 角色实体列表，可用于批量持久化操作
     * @author shy
     * @since 2026-05-06
     */
    @IterableMapping(qualifiedByName = "toEntityAdd")
    List<SysRole> toEntityListAdd(List<SysRoleAddRTO> list);

    /**
     * <p>
     * 将更新角色请求对象列表转换为角色实体列表
     * </p>
     *
     * @param list 更新角色请求对象列表
     * @return 角色实体列表，可用于批量更新操作
     * @author shy
     * @since 2026-05-06
     */
    @IterableMapping(qualifiedByName = "toEntityUpdate")
    List<SysRole> toEntityListUpdate(List<SysRoleUpdateRTO> list);

    /**
     * <p>
     * 将角色实体列表转换为通用视图对象列表
     * </p>
     *
     * @param list 角色实体列表
     * @return 角色通用视图对象列表
     * @author shy
     * @since 2026-05-06
     */
    @IterableMapping(qualifiedByName = "toCommonVO")
    List<SysRoleCommonVO> toVoList(List<SysRole> list);

    /**
     * <p>
     * 将角色实体分页对象转换为通用视图对象分页对象
     * </p>
     *
     * @param entityPage 角色实体分页对象
     * @return 角色通用视图对象分页对象
     * @author shy
     * @since 2026-05-06
     */
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

    /**
     * <p>
     * 将角色层级枚举转换为描述字符串
     * </p>
     *
     * @param roleLevel 角色层级枚举
     * @return 角色层级描述字符串
     * @author shy
     * @since 2026-05-06
     */
    @Named("roleLevelToDesc")
    default String roleLevelToDesc(RoleLevel roleLevel) {
        return roleLevel != null ? roleLevel.getDesc() : null;
    }

    /**
     * <p>
     * 将角色层级枚举转换为编码
     * </p>
     *
     * @param roleLevel 角色层级枚举
     * @return 角色层级编码
     * @author shy
     * @since 2026-05-06
     */
    @Named("roleLevelToCode")
    default Integer roleLevelToCode(RoleLevel roleLevel) {
        return roleLevel != null ? roleLevel.getCode() : null;
    }

    /**
     * <p>
     * 将整数角色层级编码转换为描述字符串
     * </p>
     *
     * @param code 角色层级编码
     * @return 角色层级描述字符串
     * @author shy
     * @since 2026-05-06
     */
    @Named("intRoleLevelToDesc")
    default String intRoleLevelToDesc(Integer code) {
        if (code == null) return null;
        RoleLevel roleLevel = RoleLevel.getByCode(code);
        return roleLevel != null ? roleLevel.getDesc() : null;
    }

    /**
     * <p>
     * 将数据范围枚举转换为描述字符串
     * </p>
     *
     * @param dataScope 数据范围枚举
     * @return 数据范围描述字符串
     * @author shy
     * @since 2026-05-06
     */
    @Named("dataScopeToDesc")
    default String dataScopeToDesc(DataScope dataScope) {
        return dataScope != null ? dataScope.getDesc() : null;
    }

    /**
     * <p>
     * 将数据范围枚举转换为编码
     * </p>
     *
     * @param dataScope 数据范围枚举
     * @return 数据范围编码
     * @author shy
     * @since 2026-05-06
     */
    @Named("dataScopeToCode")
    default Integer dataScopeToCode(DataScope dataScope) {
        return dataScope != null ? dataScope.getCode() : null;
    }

    /**
     * <p>
     * 将整数数据范围编码转换为描述字符串
     * </p>
     *
     * @param code 数据范围编码
     * @return 数据范围描述字符串
     * @author shy
     * @since 2026-05-06
     */
    @Named("intDataScopeToDesc")
    default String intDataScopeToDesc(Integer code) {
        if (code == null) return null;
        DataScope dataScope = DataScope.getByCode(code);
        return dataScope != null ? dataScope.getDesc() : null;
    }

    /**
     * <p>
     * 将状态枚举转换为编码
     * </p>
     *
     * @param status 状态枚举
     * @return 状态编码
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
     * 将删除标记枚举转换为编码
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
     * 将整数删除标记码转换为描述字符串
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
