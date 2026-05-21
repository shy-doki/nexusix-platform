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
import com.shy.nexusix.tenant.vo.SysTenantTreeVO;
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
     * <p>
     * 说明：将数据库实体中的状态码和删除标记码转换为中文描述，供列表展示使用
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
     * <p>
     * 说明：详情视图继承自通用视图，额外包含租户描述、logo路径、祖级路径、扩展属性等字段
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
     * <p>
     * 说明：
     * 1. 枚举字段（status、isDeleted）转换为对应的code值存储到数据库
     * 2. parentCode映射为parentId，packageCode映射为packageId（仅字段名映射，非数值转换）
     * 3. createByCode映射为createBy，updateCode映射为updateBy
     * 4. createTime映射为createAt，updateTime映射为updateAt，deleteTime映射为deleteAt
     * 5. id、hasChildren等字段由系统自动生成，忽略映射
     * </p>
     *
     * @param rto 新增租户请求对象
     * @return 租户实体
     * @author shy
     * @since 2026-04-27
     */
    @Named("toEntityAdd")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hasChildren", ignore = true)
    @Mapping(target = "parentId", source = "parentCode")
    @Mapping(target = "packageId", source = "packageCode")
    @Mapping(target = "createBy", source = "createByCode")
    @Mapping(target = "createAt", source = "createTime")
    @Mapping(target = "updateBy", source = "updateCode")
    @Mapping(target = "updateAt", source = "updateTime")
    @Mapping(target = "deleteAt", source = "deleteTime")
    @Mapping(target = "status", source = "status", qualifiedByName = "statusToCode")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "isDeletedToCode")
    SysTenant toEntityAdd(SysTenantAddRTO rto);

    /**
     * <p>
     * 将更新租户请求对象转换为租户实体
     * </p>
     * <p>
     * 说明：
     * 1. 枚举字段（status、isDeleted）转换为对应的code值存储到数据库
     * 2. parentCode映射为parentId，packageCode映射为packageId（仅字段名映射，非数值转换）
     * 3. createByCode映射为createBy，updateByCode映射为updateBy
     * 4. createTime映射为createAt，updateTime映射为updateAt，deleteTime映射为deleteAt
     * 5. id、parentId、hasChildren等字段由系统维护，忽略映射
     * </p>
     *
     * @param rto 更新租户请求对象
     * @return 租户实体
     * @author shy
     * @since 2026-04-27
     */
    @Named("toEntityUpdate")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hasChildren", ignore = true)
    @Mapping(target = "parentId", source = "parentCode")
    @Mapping(target = "packageId", source = "packageCode")
    @Mapping(target = "createBy", source = "createByCode")
    @Mapping(target = "createAt", source = "createTime")
    @Mapping(target = "updateBy", source = "updateByCode")
    @Mapping(target = "updateAt", source = "updateTime")
    @Mapping(target = "deleteAt", source = "deleteTime")
    @Mapping(target = "status", source = "status", qualifiedByName = "statusToCode")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "isDeletedToCode")
    SysTenant toEntityUpdate(SysTenantUpdateRTO rto);

    /**
     * <p>
     * 将租户实体转换为树形视图对象
     * </p>
     * <p>
     * 说明：树形视图继承自通用视图，额外包含parentCode和childTenant字段，用于树形结构展示
     * </p>
     *
     * @param tenant 租户实体
     * @return 租户树形视图对象
     * @author shy
     * @since 2026-05-04
     */
    @Named("toTreeVO")
    @Mapping(target = "status", source = "status", qualifiedByName = "intStatusToDesc")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysTenantTreeVO toTreeVO(SysTenant tenant);

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
     * 将租户实体列表转换为树形视图对象列表
     * </p>
     *
     * @param list 租户实体列表
     * @return 租户树形视图对象列表
     * @author shy
     * @since 2026-05-04
     */
    @IterableMapping(qualifiedByName = "toTreeVO")
    List<SysTenantTreeVO> toTreeVOList(List<SysTenant> list);

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
     * 将租户实体分页对象转换为树形视图对象分页对象
     * </p>
     *
     * @param entityPage 租户实体分页对象
     * @return 租户树形视图对象分页对象
     * @author shy
     * @since 2026-05-04
     */
    default IPage<SysTenantTreeVO> toTreeVOPage(IPage<SysTenant> entityPage) {
        if (entityPage == null) {
            return null;
        }
        IPage<SysTenantTreeVO> voPage = new Page<>();
        voPage.setCurrent(entityPage.getCurrent());
        voPage.setSize(entityPage.getSize());
        voPage.setTotal(entityPage.getTotal());

        List<SysTenantTreeVO> voList = toTreeVOList(entityPage.getRecords());
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
    default String statusToCode(TenantStatus status) {
        return status != null ? status.getCode() : null;
    }

    /**
     * <p>
     * 将状态码转换为租户状态描述字符串
     * </p>
     *
     * @param code 状态码
     * @return 状态描述字符串
     * @author shy
     * @since 2026-04-27
     */
    @Named("intStatusToDesc")
    default String intStatusToDesc(String code) {
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
    default String isDeletedToCode(Deleted del) {
        return del != null ? del.getCode() : null;
    }

    /**
     * <p>
     * 将删除标记码转换为删除标记描述字符串
     * </p>
     *
     * @param code 删除标记码
     * @return 删除标记描述字符串
     * @author shy
     * @since 2026-04-27
     */
    @Named("intDeletedToDesc")
    default String intDeletedToDesc(String code) {
        if (code == null) return null;
        Deleted deleted = Deleted.getByCode(code);
        return deleted != null ? deleted.getDesc() : null;
    }

}
