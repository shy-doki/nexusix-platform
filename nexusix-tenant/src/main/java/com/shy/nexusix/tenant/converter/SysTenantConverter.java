package com.shy.nexusix.tenant.converter;

import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.tenant.entity.SysTenant;
import com.shy.nexusix.tenant.vo.SysTenantCommonVO;
import com.shy.nexusix.tenant.vo.SysTenantDetailVO;
import com.shy.nexusix.tenant.vo.SysTenantTreeVO;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

/**
 * 租户对象转换器
 * <p>基于MapStruct实现，Spring容器管理，负责租户实体与前端VO对象的转换，
 * 包含字段映射、状态码/删除标记枚举转文本描述</p>
 *
 * @author shy
 * @since 2026-04-27
 */
@Mapper(componentModel = "spring")
public interface SysTenantConverter {

    /**
     * 租户实体转换为公共VO对象
     * <p>字段重命名映射，状态码/删除标记转换为前端可读描述</p>
     *
     * @param entity 租户数据库实体
     * @return 前端通用租户VO
     */
    @Named("toCommonVO")
    @Mapping(source = "createBy", target = "createByName")
    @Mapping(source = "createAt", target = "createTime")
    @Mapping(source = "updateBy", target = "updateByName")
    @Mapping(source = "updateAt", target = "updateTime")
    @Mapping(source = "deletedAt", target = "deleteTime")
    @Mapping(source = "status", target = "status", qualifiedByName = "intStatusToDesc")
    @Mapping(source = "isDeleted", target = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysTenantCommonVO toCommonVO(SysTenant entity);

    /**
     * 租户实体列表批量转换为VO列表
     * <p>复用单对象转换规则，实现批量映射</p>
     *
     * @param entityList 租户实体集合
     * @return 租户VO集合
     */
    @IterableMapping(qualifiedByName  = "toCommonVO")
    List<SysTenantCommonVO> entityListToCommonVoList(List<SysTenant> entityList);

    /**
     * 租户实体转换为详情VO对象
     * <p>在公共VO映射基础上，额外映射详情字段和创建/更新人编码</p>
     *
     * @param entity 租户数据库实体
     * @return 租户详情VO
     */
    @Named("toDetailVO")
    @Mapping(source = "createBy", target = "createByName")
    @Mapping(source = "createAt", target = "createTime")
    @Mapping(source = "updateBy", target = "updateByName")
    @Mapping(source = "updateAt", target = "updateTime")
    @Mapping(source = "deletedAt", target = "deleteTime")
    @Mapping(source = "status", target = "status", qualifiedByName = "intStatusToDesc")
    @Mapping(source = "isDeleted", target = "isDeleted", qualifiedByName = "intDeletedToDesc")
    @Mapping(source = "createBy", target = "createByCode")
    @Mapping(source = "updateBy", target = "updateByCode")
    SysTenantDetailVO toDetailVO(SysTenant entity);

    /**
     * 租户实体转换为树形VO对象
     * <p>在公共VO映射基础上，额外映射父租户编码字段</p>
     *
     * @param entity 租户数据库实体
     * @return 租户树形VO
     */
    @Named("toTreeVO")
    @Mapping(source = "createBy", target = "createByName")
    @Mapping(source = "createAt", target = "createTime")
    @Mapping(source = "updateBy", target = "updateByName")
    @Mapping(source = "updateAt", target = "updateTime")
    @Mapping(source = "deletedAt", target = "deleteTime")
    @Mapping(source = "status", target = "status", qualifiedByName = "intStatusToDesc")
    @Mapping(source = "isDeleted", target = "isDeleted", qualifiedByName = "intDeletedToDesc")
    @Mapping(source = "parentId", target = "parentCode")
    SysTenantTreeVO toTreeVO(SysTenant entity);

    /**
     * 租户实体列表批量转换为树形VO列表
     * <p>复用单对象转换规则，实现批量映射</p>
     *
     * @param entityList 租户实体集合
     * @return 租户树形VO集合
     */
    @IterableMapping(qualifiedByName = "toTreeVO")
    List<SysTenantTreeVO> entityListToTreeVoList(List<SysTenant> entityList);

    /**
     * 租户状态枚举转换为描述文本
     *
     * @param status 租户状态枚举
     * @return 状态描述
     */
    @Named("statusToDesc")
    default String statusToDesc(GlobalEnum.TenantStatus status) {
        return status != null ? status.getDesc() : null;
    }

    /**
     * 租户状态枚举转换为状态码
     *
     * @param status 租户状态枚举
     * @return 状态码
     */
    @Named("statusToCode")
    default String statusToCode(GlobalEnum.TenantStatus status) {
        return status != null ? status.getCode() : null;
    }

    /**
     * 租户状态码转换为描述文本
     *
     * @param code 状态码
     * @return 状态描述
     */
    @Named("intStatusToDesc")
    default String intStatusToDesc(String code) {
        if (code == null) return null;
        GlobalEnum.TenantStatus status = GlobalEnum.TenantStatus.getByCode(code);
        return status != null ? status.getDesc() : null;
    }

    /**
     * 删除标记枚举转换为描述文本
     *
     * @param del 删除标记枚举
     * @return 删除状态描述
     */
    @Named("isDeletedToDesc")
    default String isDeletedToDesc(GlobalEnum.Deleted del) {
        return del != null ? del.getDesc() : null;
    }

    /**
     * 删除标记枚举转换为编码
     *
     * @param del 删除标记枚举
     * @return 删除标记编码
     */
    @Named("isDeletedToCode")
    default String isDeletedToCode(GlobalEnum.Deleted del) {
        return del != null ? del.getCode() : null;
    }

    /**
     * 删除标记编码转换为描述文本
     *
     * @param code 删除标记编码
     * @return 删除状态描述
     */
    @Named("intDeletedToDesc")
    default String intDeletedToDesc(String code) {
        if (code == null) return null;
        GlobalEnum.Deleted deleted = GlobalEnum.Deleted.getByCode(code);
        return deleted != null ? deleted.getDesc() : null;
    }


}
