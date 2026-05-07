package com.shy.nexusix.iam.converter;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.iam.entity.SysUserTenantRel;
import com.shy.nexusix.iam.rto.SysUserTenantRelAddRTO;
import com.shy.nexusix.iam.rto.SysUserTenantRelUpdateRTO;
import com.shy.nexusix.iam.vo.SysUserTenantRelCommonVO;
import com.shy.nexusix.iam.vo.SysUserTenantRelDetailVO;
import com.shy.nexusix.iam.vo.SysUserTenantRelExportVO;
import org.mapstruct.*;

import java.util.List;

/**
 * <p>
 * 用户租户关联对象转换器
 * </p>
 * <p>
 * 负责Entity、RTO、VO之间的对象转换，包含枚举值与描述的转换逻辑
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Mapper(componentModel = "spring")
public interface SysUserTenantRelConverter {

    @Named("toEntityAdd")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "deletedToCode")
    SysUserTenantRel toEntityAdd(SysUserTenantRelAddRTO addParam);

    @Named("toEntityUpdate")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "deletedToCode")
    SysUserTenantRel toEntityUpdate(SysUserTenantRelUpdateRTO updateParam);

    @IterableMapping(qualifiedByName = "toEntityAdd")
    List<SysUserTenantRel> toEntityListAdd(List<SysUserTenantRelAddRTO> addParamList);

    @IterableMapping(qualifiedByName = "toEntityUpdate")
    List<SysUserTenantRel> toEntityListUpdate(List<SysUserTenantRelUpdateRTO> updateParamList);

    @Named("toCommonVO")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysUserTenantRelCommonVO toCommonVO(SysUserTenantRel rel);

    @IterableMapping(qualifiedByName = "toCommonVO")
    List<SysUserTenantRelCommonVO> toVoList(List<SysUserTenantRel> relList);

    default IPage<SysUserTenantRelCommonVO> toVOPage(IPage<SysUserTenantRel> relPage) {
        if (relPage == null) {
            return null;
        }
        IPage<SysUserTenantRelCommonVO> voPage = new Page<>();
        voPage.setCurrent(relPage.getCurrent());
        voPage.setSize(relPage.getSize());
        voPage.setTotal(relPage.getTotal());
        voPage.setRecords(toVoList(relPage.getRecords()));
        return voPage;
    }

    @Named("toDetailVO")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysUserTenantRelDetailVO toDetailVO(SysUserTenantRel rel);

    @Named("toExportVO")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysUserTenantRelExportVO toExportVO(SysUserTenantRel rel);

    @IterableMapping(qualifiedByName = "toExportVO")
    List<SysUserTenantRelExportVO> toExportVoList(List<SysUserTenantRel> relList);

    @Named("deletedToCode")
    default Integer deletedToCode(GlobalEnum.Deleted deleted) {
        return deleted != null ? deleted.getCode() : null;
    }

    @Named("intDeletedToDesc")
    default String intDeletedToDesc(Integer code) {
        if (code == null) return null;
        GlobalEnum.Deleted deleted = GlobalEnum.Deleted.getByCode(code);
        return deleted != null ? deleted.getDesc() : null;
    }

}
