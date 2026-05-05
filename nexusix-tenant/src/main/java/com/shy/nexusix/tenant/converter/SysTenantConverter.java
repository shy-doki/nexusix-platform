package com.shy.nexusix.tenant.converter;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shy.nexusix.common.enums.GlobalEnum.Deleted;
import com.shy.nexusix.common.enums.GlobalEnum.TenantStatus;
import com.shy.nexusix.tenant.entity.SysTenant;
import com.shy.nexusix.tenant.rto.SysTenantAddRTO;
import com.shy.nexusix.tenant.rto.SysTenantUpdateRTO;
import com.shy.nexusix.tenant.vo.SysTenantCommonVO;
import com.shy.nexusix.tenant.vo.SysTenantDetailVO;
import org.mapstruct.*;

import java.util.List;

/**
 * <p>
 * 租户信息转换器 - 负责租户实体、请求对象与视图对象之间的转换
 * </p>
 *
 * @author shy
 * @since 2026-04-27
 */
@Mapper(componentModel = "spring")
public interface SysTenantConverter {

    /**
     * <p>
     * 将租户实体转换为通用视图对象
     * </p>
     *
     * @param tenant 租户实体
     * @return 租户通用视图对象
     * @author shy
     * @since 2026-04-27
     */
    @Named("toCommonVO")
    @Mapping(target = "status", source = "status", qualifiedByName = "intStatusToDesc")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysTenantCommonVO toCommonVO(SysTenant tenant);

    /**
     * <p>
     * 将租户实体转换为详情视图对象
     * </p>
     *
     * @param tenant 租户实体
     * @return 租户详情视图对象
     * @author shy
     * @since 2026-04-27
     */
    @Mapping(target = "status", source = "status", qualifiedByName = "intStatusToDesc")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysTenantDetailVO toDetailVO(SysTenant tenant);

    /**
     * <p>
     * 将新增租户请求对象转换为租户实体
     * </p>
     *
     * @param rto 新增租户请求对象
     * @return 租户实体
     * @author shy
     * @since 2026-04-27
     */
    @Named("toEntityAdd")
    @Mapping(target = "status", qualifiedByName = "statusToCode")
    @Mapping(target = "isDeleted", qualifiedByName = "isDeletedToCode", source = "isDeleted")
    SysTenant toEntityAdd(SysTenantAddRTO rto);

    /**
     * <p>
     * 将更新租户请求对象转换为租户实体
     * </p>
     *
     * @param rto 更新租户请求对象
     * @return 租户实体
     * @author shy
     * @since 2026-04-27
     */
    @Named("toEntityUpdate")
    @Mapping(target = "status", qualifiedByName = "statusToCode")
    @Mapping(target = "isDeleted", qualifiedByName = "isDeletedToCode", source = "isDeleted")
    SysTenant toEntityUpdate(SysTenantUpdateRTO rto);

    /**
     * <p>
     * 将新增租户请求对象列表转换为租户实体列表
     * </p>
     *
     * @param list 新增租户请求对象列表
     * @return 租户实体列表，可用于批量持久化操作
     * @author shy
     * @since 2026-04-27
     */
    @IterableMapping(qualifiedByName = "toEntityAdd")
    List<SysTenant> toEntityListAdd(List<SysTenantAddRTO> list);

    /**
     * <p>
     * 将更新租户请求对象列表转换为租户实体列表
     * </p>
     *
     * @param list 更新租户请求对象列表
     * @return 租户实体列表，可用于批量更新操作
     * @author shy
     * @since 2026-04-27
     */
    @IterableMapping(qualifiedByName = "toEntityUpdate")
    List<SysTenant> toEntityListUpdate(List<SysTenantUpdateRTO> list);

    /**
     * <p>
     * 将租户实体列表转换为通用视图对象列表
     * </p>
     *
     * @param list 租户实体列表
     * @return 租户通用视图对象列表
     * @author shy
     * @since 2026-04-27
     */
    @IterableMapping(qualifiedByName = "toCommonVO")
    List<SysTenantCommonVO> toVoList(List<SysTenant> list);

    /**
     * <p>
     * 将租户实体分页对象转换为通用视图对象分页对象
     * </p>
     *
     * @param entityPage 租户实体分页对象
     * @return 租户通用视图对象分页对象
     * @author shy
     * @since 2026-04-27
     */
    default IPage<SysTenantCommonVO> toVOPage(IPage<SysTenant> entityPage) {
        if (entityPage == null) {
            return null;
        }
        IPage<SysTenantCommonVO> voPage = new Page<>();
        voPage.setCurrent(entityPage.getCurrent());
        voPage.setSize(entityPage.getSize());
        voPage.setTotal(entityPage.getTotal());

        List<SysTenantCommonVO> voList = toVoList(entityPage.getRecords());
        voPage.setRecords(voList);

        return voPage;
    }

    /**
     * <p>
     * 将租户状态枚举转换为描述字符串
     * </p>
     *
     * @param status 租户状态枚举
     * @return 状态描述字符串
     * @author shy
     * @since 2026-04-27
     */
    @Named("statusToDesc")
    default String statusToDesc(TenantStatus status) {
        return status != null ? status.getDesc() : null;
    }

    /**
     * <p>
     * 将租户状态枚举转换为状态码
     * </p>
     *
     * @param status 租户状态枚举
     * @return 状态码
     * @author shy
     * @since 2026-04-27
     */
    @Named("statusToCode")
    default Integer statusToCode(TenantStatus status) {
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
     * @since 2026-04-27
     */
    @Named("intStatusToDesc")
    default String intStatusToDesc(Integer code) {
        if (code == null) return null;
        TenantStatus status = TenantStatus.getByCode(code);
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
     * @since 2026-04-27
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
     * @since 2026-04-27
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
     * @since 2026-04-27
     */
    @Named("intDeletedToDesc")
    default String intDeletedToDesc(Integer code) {
        if (code == null) return null;
        Deleted deleted = Deleted.getByCode(code);
        return deleted != null ? deleted.getDesc() : null;
    }

} 