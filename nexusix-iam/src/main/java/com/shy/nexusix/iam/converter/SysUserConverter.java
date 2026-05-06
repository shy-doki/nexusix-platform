package com.shy.nexusix.iam.converter;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shy.nexusix.common.enums.GlobalEnum.Deleted;
import com.shy.nexusix.common.enums.GlobalEnum.Status;
import com.shy.nexusix.iam.entity.SysUser;
import com.shy.nexusix.iam.rto.SysUserAddRTO;
import com.shy.nexusix.iam.rto.SysUserUpdateRTO;
import com.shy.nexusix.iam.vo.SysUserCommonVO;
import com.shy.nexusix.iam.vo.SysUserDetailVO;
import org.mapstruct.*;

import java.util.List;

/**
 * <p>
 * 用户信息转换器 - 负责用户实体、请求对象与视图对象之间的转换
 * </p>
 * <p>
 * 注意：password 字段不映射到任何视图对象，确保用户密码安全
 * </p>
 *
 * @author shy
 * @since 2026-05-06
 */
@Mapper(componentModel = "spring")
public interface SysUserConverter {

    /**
     * <p>
     * 将用户实体转换为通用视图对象
     * </p>
     * <p>
     * password 字段不映射到视图对象
     * </p>
     *
     * @param user 用户实体
     * @return 用户通用视图对象
     * @author shy
     * @since 2026-05-06
     */
    @Named("toCommonVO")
    @Mapping(target = "status", source = "status", qualifiedByName = "intStatusToDesc")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysUserCommonVO toCommonVO(SysUser user);

    /**
     * <p>
     * 将用户实体转换为详情视图对象
     * </p>
     * <p>
     * password 字段不映射到视图对象
     * </p>
     *
     * @param user 用户实体
     * @return 用户详情视图对象
     * @author shy
     * @since 2026-05-06
     */
    @Mapping(target = "status", source = "status", qualifiedByName = "intStatusToDesc")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysUserDetailVO toDetailVO(SysUser user);

    /**
     * <p>
     * 将新增用户请求对象转换为用户实体
     * </p>
     *
     * @param rto 新增用户请求对象
     * @return 用户实体
     * @author shy
     * @since 2026-05-06
     */
    @Named("toEntityAdd")
    @Mapping(target = "status", qualifiedByName = "statusToCode")
    @Mapping(target = "isDeleted", qualifiedByName = "isDeletedToCode", source = "isDeleted")
    SysUser toEntityAdd(SysUserAddRTO rto);

    /**
     * <p>
     * 将更新用户请求对象转换为用户实体
     * </p>
     *
     * @param rto 更新用户请求对象
     * @return 用户实体
     * @author shy
     * @since 2026-05-06
     */
    @Named("toEntityUpdate")
    @Mapping(target = "status", qualifiedByName = "statusToCode")
    @Mapping(target = "isDeleted", qualifiedByName = "isDeletedToCode", source = "isDeleted")
    @Mapping(target = "password", ignore = true)
    SysUser toEntityUpdate(SysUserUpdateRTO rto);

    /**
     * <p>
     * 将新增用户请求对象列表转换为用户实体列表
     * </p>
     *
     * @param list 新增用户请求对象列表
     * @return 用户实体列表，可用于批量持久化操作
     * @author shy
     * @since 2026-05-06
     */
    @IterableMapping(qualifiedByName = "toEntityAdd")
    List<SysUser> toEntityListAdd(List<SysUserAddRTO> list);

    /**
     * <p>
     * 将更新用户请求对象列表转换为用户实体列表
     * </p>
     *
     * @param list 更新用户请求对象列表
     * @return 用户实体列表，可用于批量更新操作
     * @author shy
     * @since 2026-05-06
     */
    @IterableMapping(qualifiedByName = "toEntityUpdate")
    List<SysUser> toEntityListUpdate(List<SysUserUpdateRTO> list);

    /**
     * <p>
     * 将用户实体列表转换为通用视图对象列表
     * </p>
     *
     * @param list 用户实体列表
     * @return 用户通用视图对象列表
     * @author shy
     * @since 2026-05-06
     */
    @IterableMapping(qualifiedByName = "toCommonVO")
    List<SysUserCommonVO> toVoList(List<SysUser> list);

    /**
     * <p>
     * 将用户实体分页对象转换为通用视图对象分页对象
     * </p>
     *
     * @param entityPage 用户实体分页对象
     * @return 用户通用视图对象分页对象
     * @author shy
     * @since 2026-05-06
     */
    default IPage<SysUserCommonVO> toVOPage(IPage<SysUser> entityPage) {
        if (entityPage == null) {
            return null;
        }
        IPage<SysUserCommonVO> voPage = new Page<>();
        voPage.setCurrent(entityPage.getCurrent());
        voPage.setSize(entityPage.getSize());
        voPage.setTotal(entityPage.getTotal());

        List<SysUserCommonVO> voList = toVoList(entityPage.getRecords());
        voPage.setRecords(voList);

        return voPage;
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

}
