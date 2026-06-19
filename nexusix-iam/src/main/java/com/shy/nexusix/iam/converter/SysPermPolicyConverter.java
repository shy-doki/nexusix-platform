package com.shy.nexusix.iam.converter;

import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.iam.entity.SysPermPolicy;
import com.shy.nexusix.iam.rto.SysPermPolicyAddRTO;
import com.shy.nexusix.iam.rto.SysPermPolicyUpdateRTO;
import com.shy.nexusix.iam.vo.SysPermPolicyCommonVO;
import com.shy.nexusix.iam.vo.SysPermPolicyDetailVO;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

/**
 * <p>权限策略对象转换器，基于MapStruct实现实体与VO互转</p>
 *
 * @author shy
 */
@Mapper(componentModel = "spring")
public interface SysPermPolicyConverter {

    /**
     * <p>权限策略实体转公共VO</p>
     *
     * @param entity 权限策略实体
     * @return 权限策略通用VO
     */
    @Named("toCommonVO")
    @Mapping(source = "createBy", target = "createByName")
    @Mapping(source = "createAt", target = "createTime")
    @Mapping(source = "updateBy", target = "updateByName")
    @Mapping(source = "updateAt", target = "updateTime")
    @Mapping(source = "deletedAt", target = "deleteTime")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "isDeleted", target = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysPermPolicyCommonVO toCommonVO(SysPermPolicy entity);

    /**
     * <p>批量转换权限策略实体列表为VO列表</p>
     *
     * @param entityList 权限策略实体集合
     * @return 权限策略VO集合
     */
    @IterableMapping(qualifiedByName = "toCommonVO")
    List<SysPermPolicyCommonVO> entityListToCommonVoList(List<SysPermPolicy> entityList);

    /**
     * <p>权限策略实体转详情VO</p>
     *
     * @param entity 权限策略实体
     * @return 权限策略详情VO
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
    SysPermPolicyDetailVO toDetailVO(SysPermPolicy entity);

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
     * @param addRTO 权限策略新增请求对象
     * @return 权限策略实体
     */
    @Named("toEntityFromAdd")
    @Mapping(source = "createByCode", target = "createBy", qualifiedByName = "stringToLong")
    @Mapping(source = "createTime", target = "createAt")
    @Mapping(source = "updateByCode", target = "updateBy", qualifiedByName = "stringToLong")
    @Mapping(source = "updateTime", target = "updateAt")
    @Mapping(source = "isDeleted", target = "isDeleted", qualifiedByName = "isDeletedToCode")
    @Mapping(source = "deleteTime", target = "deletedAt")
    @Mapping(target = "id", ignore = true)
    SysPermPolicy toEntityFromAdd(SysPermPolicyAddRTO addRTO);

    /**
     * <p>更新RTO转实体</p>
     *
     * @param updateRTO 权限策略更新请求对象
     * @return 权限策略实体
     */
    @Named("toEntityFromUpdate")
    @Mapping(source = "createByCode", target = "createBy", qualifiedByName = "stringToLong")
    @Mapping(source = "createTime", target = "createAt")
    @Mapping(source = "updateByCode", target = "updateBy", qualifiedByName = "stringToLong")
    @Mapping(source = "updateTime", target = "updateAt")
    @Mapping(source = "isDeleted", target = "isDeleted", qualifiedByName = "isDeletedToCode")
    @Mapping(source = "deleteTime", target = "deletedAt")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "permId", ignore = true)
    @Mapping(target = "targetType", ignore = true)
    @Mapping(target = "targetId", ignore = true)
    SysPermPolicy toEntityFromUpdate(SysPermPolicyUpdateRTO updateRTO);

    /**
     * <p>批量新增RTO转实体列表</p>
     *
     * @param addRTOList 权限策略新增请求对象集合
     * @return 权限策略实体集合
     */
    @IterableMapping(qualifiedByName = "toEntityFromAdd")
    List<SysPermPolicy> addRTOListToEntityList(List<SysPermPolicyAddRTO> addRTOList);

    /**
     * <p>批量更新RTO转实体列表</p>
     *
     * @param updateRTOList 权限策略更新请求对象集合
     * @return 权限策略实体集合
     */
    @IterableMapping(qualifiedByName = "toEntityFromUpdate")
    List<SysPermPolicy> updateRTOListToEntityList(List<SysPermPolicyUpdateRTO> updateRTOList);

}
