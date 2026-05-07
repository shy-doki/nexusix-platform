package com.shy.nexusix.iam.converter;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shy.nexusix.common.enums.GlobalEnum.Deleted;
import com.shy.nexusix.common.enums.GlobalEnum.UserStatus;
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
 * 状态字段使用专用枚举 UserStatus 进行转换，与通用 Status 枚举解耦
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

    @Named("toCommonVO")
    @Mapping(target = "status", source = "status", qualifiedByName = "intUserStatusToDesc")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysUserCommonVO toCommonVO(SysUser user);

    @Mapping(target = "status", source = "status", qualifiedByName = "intUserStatusToDesc")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysUserDetailVO toDetailVO(SysUser user);

    @Named("toEntityAdd")
    @Mapping(target = "status", qualifiedByName = "userStatusToCode")
    @Mapping(target = "isDeleted", qualifiedByName = "isDeletedToCode", source = "isDeleted")
    SysUser toEntityAdd(SysUserAddRTO rto);

    @Named("toEntityUpdate")
    @Mapping(target = "status", qualifiedByName = "userStatusToCode")
    @Mapping(target = "isDeleted", qualifiedByName = "isDeletedToCode", source = "isDeleted")
    @Mapping(target = "password", ignore = true)
    SysUser toEntityUpdate(SysUserUpdateRTO rto);

    @IterableMapping(qualifiedByName = "toEntityAdd")
    List<SysUser> toEntityListAdd(List<SysUserAddRTO> list);

    @IterableMapping(qualifiedByName = "toEntityUpdate")
    List<SysUser> toEntityListUpdate(List<SysUserUpdateRTO> list);

    @IterableMapping(qualifiedByName = "toCommonVO")
    List<SysUserCommonVO> toVoList(List<SysUser> list);

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
     * 将用户状态枚举转换为状态码
     * 用户状态使用专用枚举 UserStatus，与通用 Status 解耦
     */
    @Named("userStatusToCode")
    default Integer userStatusToCode(UserStatus status) {
        return status != null ? status.getCode() : null;
    }

    /**
     * 将整数状态码转换为用户状态描述字符串
     * 用户状态使用专用枚举 UserStatus，与通用 Status 解耦
     */
    @Named("intUserStatusToDesc")
    default String intUserStatusToDesc(Integer code) {
        if (code == null) return null;
        UserStatus status = UserStatus.getByCode(code);
        return status != null ? status.getDesc() : null;
    }

    @Named("isDeletedToCode")
    default Integer isDeletedToCode(Deleted del) {
        return del != null ? del.getCode() : null;
    }

    @Named("intDeletedToDesc")
    default String intDeletedToDesc(Integer code) {
        if (code == null) return null;
        Deleted deleted = Deleted.getByCode(code);
        return deleted != null ? deleted.getDesc() : null;
    }

}
