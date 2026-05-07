package com.shy.nexusix.iam.converter;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.iam.entity.SysUserRoleRel;
import com.shy.nexusix.iam.rto.SysUserRoleRelAddRTO;
import com.shy.nexusix.iam.rto.SysUserRoleRelUpdateRTO;
import com.shy.nexusix.iam.vo.SysUserRoleRelCommonVO;
import com.shy.nexusix.iam.vo.SysUserRoleRelDetailVO;
import org.mapstruct.*;

import java.util.List;

/**
 * <p>
 * 用户角色关联对象转换器
 * </p>
 * <p>
 * 负责Entity、RTO、VO之间的对象转换，包含枚举值与描述的转换逻辑
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Mapper(componentModel = "spring")
public interface SysUserRoleRelConverter {

    /**
     * 新增RTO转Entity
     */
    @Named("toEntityAdd")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "deletedToCode")
    SysUserRoleRel toEntityAdd(SysUserRoleRelAddRTO addParam);

    /**
     * 更新RTO转Entity
     */
    @Named("toEntityUpdate")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "deletedToCode")
    SysUserRoleRel toEntityUpdate(SysUserRoleRelUpdateRTO updateParam);

    /**
     * 新增RTO列表转Entity列表
     */
    @IterableMapping(qualifiedByName = "toEntityAdd")
    List<SysUserRoleRel> toEntityListAdd(List<SysUserRoleRelAddRTO> addParamList);

    /**
     * 更新RTO列表转Entity列表
     */
    @IterableMapping(qualifiedByName = "toEntityUpdate")
    List<SysUserRoleRel> toEntityListUpdate(List<SysUserRoleRelUpdateRTO> updateParamList);

    /**
     * Entity转通用VO
     */
    @Named("toCommonVO")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysUserRoleRelCommonVO toCommonVO(SysUserRoleRel rel);

    /**
     * Entity列表转通用VO列表
     */
    @IterableMapping(qualifiedByName = "toCommonVO")
    List<SysUserRoleRelCommonVO> toVoList(List<SysUserRoleRel> relList);

    /**
     * Entity分页转通用VO分页
     */
    default IPage<SysUserRoleRelCommonVO> toVOPage(IPage<SysUserRoleRel> relPage) {
        if (relPage == null) {
            return null;
        }
        IPage<SysUserRoleRelCommonVO> voPage = new Page<>();
        voPage.setCurrent(relPage.getCurrent());
        voPage.setSize(relPage.getSize());
        voPage.setTotal(relPage.getTotal());
        voPage.setRecords(toVoList(relPage.getRecords()));
        return voPage;
    }

    /**
     * Entity转详情VO
     */
    @Named("toDetailVO")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysUserRoleRelDetailVO toDetailVO(SysUserRoleRel rel);

    /**
     * 删除标记枚举转编码
     */
    @Named("deletedToCode")
    default Integer deletedToCode(GlobalEnum.Deleted deleted) {
        return deleted != null ? deleted.getCode() : null;
    }

    /**
     * 删除标记编码转中文描述
     */
    @Named("intDeletedToDesc")
    default String intDeletedToDesc(Integer code) {
        if (code == null) return null;
        GlobalEnum.Deleted deleted = GlobalEnum.Deleted.getByCode(code);
        return deleted != null ? deleted.getDesc() : null;
    }

}
