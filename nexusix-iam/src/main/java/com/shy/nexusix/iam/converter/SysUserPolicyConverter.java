package com.shy.nexusix.iam.converter;

import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.iam.entity.SysUserPolicy;
import com.shy.nexusix.iam.rto.SysUserPolicyAddRTO;
import com.shy.nexusix.iam.rto.SysUserPolicyUpdateRTO;
import com.shy.nexusix.iam.vo.SysUserPolicyCommonVO;
import com.shy.nexusix.iam.vo.SysUserPolicyDetailVO;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

/**
 * <p>用户策略对象转换器，基于MapStruct实现实体与VO互转</p>
 *
 * @author shy
 */
@Mapper(componentModel = "spring")
public interface SysUserPolicyConverter {

    /**
     * <p>用户策略实体转公共VO</p>
     *
     * @param entity 用户策略实体
     * @return 用户策略通用VO
     */
    @Named("toCommonVO")
    @Mapping(source = "createBy", target = "createByName")
    @Mapping(source = "createAt", target = "createTime")
    @Mapping(source = "updateBy", target = "updateByName")
    @Mapping(source = "updateAt", target = "updateTime")
    @Mapping(source = "deletedAt", target = "deleteTime")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "isDeleted", target = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysUserPolicyCommonVO toCommonVO(SysUserPolicy entity);

    /**
     * <p>批量转换用户策略实体列表为VO列表</p>
     *
     * @param entityList 用户策略实体集合
     * @return 用户策略VO集合
     */
    @IterableMapping(qualifiedByName = "toCommonVO")
    List<SysUserPolicyCommonVO> entityListToCommonVoList(List<SysUserPolicy> entityList);

    /**
     * <p>用户策略实体转详情VO</p>
     *
     * @param entity 用户策略实体
     * @return 用户策略详情VO
     */
    @Named("toDetailVO")
    @Mapping(source = "createBy", target = "createByCode")
    @Mapping(source = "createBy", target = "createByName")
    @Mapping(source = "createAt", target = "createTime")
    @Mapping(source = "updateBy", target = "updateByCode")
    @Mapping(source = "updateBy", target = "updateByName")
    @Mapping(source = "updateAt", target = "updateTime")
    @Mapping(source = "deletedAt", target = "deleteTime")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "isDeleted", target = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysUserPolicyDetailVO toDetailVO(SysUserPolicy entity);

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
     * @param addRTO 用户策略新增请求对象
     * @return 用户策略实体
     */
    @Named("toEntityFromAdd")
    @Mapping(source = "createByCode", target = "createBy", qualifiedByName = "stringToLong")
    @Mapping(source = "createTime", target = "createAt")
    @Mapping(source = "updateByCode", target = "updateBy", qualifiedByName = "stringToLong")
    @Mapping(source = "updateTime", target = "updateAt")
    @Mapping(source = "isDeleted", target = "isDeleted", qualifiedByName = "isDeletedToCode")
    @Mapping(source = "deleteTime", target = "deletedAt")
    @Mapping(target = "id", ignore = true)
    SysUserPolicy toEntityFromAdd(SysUserPolicyAddRTO addRTO);

    /**
     * <p>更新RTO转实体</p>
     *
     * @param updateRTO 用户策略更新请求对象
     * @return 用户策略实体
     */
    @Named("toEntityFromUpdate")
    @Mapping(source = "createByCode", target = "createBy", qualifiedByName = "stringToLong")
    @Mapping(source = "createTime", target = "createAt")
    @Mapping(source = "updateByCode", target = "updateBy", qualifiedByName = "stringToLong")
    @Mapping(source = "updateTime", target = "updateAt")
    @Mapping(source = "isDeleted", target = "isDeleted", qualifiedByName = "isDeletedToCode")
    @Mapping(source = "deleteTime", target = "deletedAt")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "targetType", ignore = true)
    @Mapping(target = "targetId", ignore = true)
    SysUserPolicy toEntityFromUpdate(SysUserPolicyUpdateRTO updateRTO);

    /**
     * <p>批量新增RTO转实体列表</p>
     *
     * @param addRTOList 用户策略新增请求对象集合
     * @return 用户策略实体集合
     */
    @IterableMapping(qualifiedByName = "toEntityFromAdd")
    List<SysUserPolicy> addRTOListToEntityList(List<SysUserPolicyAddRTO> addRTOList);

    /**
     * <p>批量更新RTO转实体列表</p>
     *
     * @param updateRTOList 用户策略更新请求对象集合
     * @return 用户策略实体集合
     */
    @IterableMapping(qualifiedByName = "toEntityFromUpdate")
    List<SysUserPolicy> updateRTOListToEntityList(List<SysUserPolicyUpdateRTO> updateRTOList);

}
