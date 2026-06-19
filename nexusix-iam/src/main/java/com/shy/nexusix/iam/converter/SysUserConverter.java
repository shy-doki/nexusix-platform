package com.shy.nexusix.iam.converter;

import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.iam.entity.SysUser;
import com.shy.nexusix.iam.rto.SysUserAddRTO;
import com.shy.nexusix.iam.rto.SysUserUpdateRTO;
import com.shy.nexusix.iam.vo.SysUserCommonVO;
import com.shy.nexusix.iam.vo.SysUserDetailVO;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

/**
 * <p>用户对象转换器，基于MapStruct实现实体与VO互转</p>
 *
 * @author shy
 */
@Mapper(componentModel = "spring")
public interface SysUserConverter {

    /**
     * <p>用户实体转换为公共VO</p>
     *
     * @param entity 用户实体
     * @return 用户公共VO
     */
    @Named("toCommonVO")
    @Mapping(source = "createBy", target = "createByName")
    @Mapping(source = "createAt", target = "createTime")
    @Mapping(source = "updateBy", target = "updateByName")
    @Mapping(source = "updateAt", target = "updateTime")
    @Mapping(source = "deletedAt", target = "deleteTime")
    @Mapping(source = "status", target = "status", qualifiedByName = "intStatusToDesc")
    @Mapping(source = "isDeleted", target = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysUserCommonVO toCommonVO(SysUser entity);

    /**
     * <p>批量转换用户实体列表为VO列表</p>
     *
     * @param entityList 用户实体集合
     * @return 用户VO集合
     */
    @IterableMapping(qualifiedByName = "toCommonVO")
    List<SysUserCommonVO> entityListToCommonVoList(List<SysUser> entityList);

    /**
     * <p>用户实体转换为详情VO</p>
     *
     * @param entity 用户实体
     * @return 用户详情VO
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
    SysUserDetailVO toDetailVO(SysUser entity);

    /**
     * <p>用户状态枚举转编码</p>
     *
     * @param status 用户状态枚举
     * @return 状态码
     */
    @Named("statusToCode")
    default String statusToCode(GlobalEnum.UserStatus status) {
        return status != null ? status.getCode() : null;
    }

    /**
     * <p>用户状态码转描述</p>
     *
     * @param code 状态码
     * @return 状态描述
     */
    @Named("intStatusToDesc")
    default String intStatusToDesc(String code) {
        if (code == null) return null;
        GlobalEnum.UserStatus status = GlobalEnum.UserStatus.getByCode(code);
        return status != null ? status.getDesc() : null;
    }

    /**
     * <p>删除标记编码转描述</p>
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

    /**
     * <p>删除标记枚举转编码</p>
     *
     * @param del 删除标记枚举
     * @return 删除标记编码
     */
    @Named("isDeletedToCode")
    default String isDeletedToCode(GlobalEnum.Deleted del) {
        return del != null ? del.getCode() : null;
    }

    /**
     * <p>字符串转Long</p>
     *
     * @param value 字符串值
     * @return Long值，输入为空时返回null
     */
    @Named("stringToLong")
    default Long stringToLong(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        return Long.parseLong(value.trim());
    }

    /**
     * <p>新增RTO转实体</p>
     *
     * @param addRTO 用户新增请求对象
     * @return 用户实体
     */
    @Named("toEntityFromAdd")
    @Mapping(source = "createByCode", target = "createBy", qualifiedByName = "stringToLong")
    @Mapping(source = "createTime", target = "createAt")
    @Mapping(source = "updateByCode", target = "updateBy", qualifiedByName = "stringToLong")
    @Mapping(source = "updateTime", target = "updateAt")
    @Mapping(source = "status", target = "status", qualifiedByName = "statusToCode")
    @Mapping(source = "isDeleted", target = "isDeleted", qualifiedByName = "isDeletedToCode")
    @Mapping(source = "deleteTime", target = "deletedAt")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    SysUser toEntityFromAdd(SysUserAddRTO addRTO);

    /**
     * <p>更新RTO转实体</p>
     *
     * @param updateRTO 用户更新请求对象
     * @return 用户实体
     */
    @Named("toEntityFromUpdate")
    @Mapping(source = "createByCode", target = "createBy", qualifiedByName = "stringToLong")
    @Mapping(source = "createTime", target = "createAt")
    @Mapping(source = "updateByCode", target = "updateBy", qualifiedByName = "stringToLong")
    @Mapping(source = "updateTime", target = "updateAt")
    @Mapping(source = "status", target = "status", qualifiedByName = "statusToCode")
    @Mapping(source = "isDeleted", target = "isDeleted", qualifiedByName = "isDeletedToCode")
    @Mapping(source = "deleteTime", target = "deletedAt")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userCode", ignore = true)
    @Mapping(target = "password", ignore = true)
    SysUser toEntityFromUpdate(SysUserUpdateRTO updateRTO);

    /**
     * <p>批量新增RTO转实体列表</p>
     *
     * @param addRTOList 用户新增请求对象集合
     * @return 用户实体集合
     */
    @IterableMapping(qualifiedByName = "toEntityFromAdd")
    List<SysUser> addRTOListToEntityList(List<SysUserAddRTO> addRTOList);

    /**
     * <p>批量更新RTO转实体列表</p>
     *
     * @param updateRTOList 用户更新请求对象集合
     * @return 用户实体集合
     */
    @IterableMapping(qualifiedByName = "toEntityFromUpdate")
    List<SysUser> updateRTOListToEntityList(List<SysUserUpdateRTO> updateRTOList);

}
