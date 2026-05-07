package com.shy.nexusix.iam.converter;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.enums.GlobalEnum.TokenStatus;
import com.shy.nexusix.iam.entity.SysUserToken;
import com.shy.nexusix.iam.rto.SysUserTokenAddRTO;
import com.shy.nexusix.iam.rto.SysUserTokenUpdateRTO;
import com.shy.nexusix.iam.vo.SysUserTokenCommonVO;
import com.shy.nexusix.iam.vo.SysUserTokenDetailVO;
import com.shy.nexusix.iam.vo.SysUserTokenExportVO;
import org.mapstruct.*;

import java.util.List;

/**
 * <p>
 * 用户Token对象转换器
 * </p>
 * <p>
 * 负责Entity、RTO、VO之间的对象转换，包含枚举值与描述的转换逻辑
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Mapper(componentModel = "spring")
public interface SysUserTokenConverter {

    /**
     * 新增RTO转Entity
     */
    @Named("toEntityAdd")
    @Mapping(target = "status", source = "status", qualifiedByName = "tokenStatusToCode")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "deletedToCode")
    SysUserToken toEntityAdd(SysUserTokenAddRTO addParam);

    /**
     * 更新RTO转Entity
     */
    @Named("toEntityUpdate")
    @Mapping(target = "status", source = "status", qualifiedByName = "tokenStatusToCode")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "deletedToCode")
    SysUserToken toEntityUpdate(SysUserTokenUpdateRTO updateParam);

    /**
     * 新增RTO列表转Entity列表
     */
    @IterableMapping(qualifiedByName = "toEntityAdd")
    List<SysUserToken> toEntityListAdd(List<SysUserTokenAddRTO> addParamList);

    /**
     * 更新RTO列表转Entity列表
     */
    @IterableMapping(qualifiedByName = "toEntityUpdate")
    List<SysUserToken> toEntityListUpdate(List<SysUserTokenUpdateRTO> updateParamList);

    /**
     * Entity转通用VO
     */
    @Named("toCommonVO")
    @Mapping(target = "status", source = "status", qualifiedByName = "intTokenStatusToDesc")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysUserTokenCommonVO toCommonVO(SysUserToken token);

    /**
     * Entity列表转通用VO列表
     */
    @IterableMapping(qualifiedByName = "toCommonVO")
    List<SysUserTokenCommonVO> toVoList(List<SysUserToken> tokenList);

    /**
     * Entity分页转通用VO分页
     */
    default IPage<SysUserTokenCommonVO> toVOPage(IPage<SysUserToken> tokenPage) {
        if (tokenPage == null) {
            return null;
        }
        IPage<SysUserTokenCommonVO> voPage = new Page<>();
        voPage.setCurrent(tokenPage.getCurrent());
        voPage.setSize(tokenPage.getSize());
        voPage.setTotal(tokenPage.getTotal());
        voPage.setRecords(toVoList(tokenPage.getRecords()));
        return voPage;
    }

    /**
     * Entity转详情VO
     */
    @Named("toDetailVO")
    @Mapping(target = "status", source = "status", qualifiedByName = "intTokenStatusToDesc")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysUserTokenDetailVO toDetailVO(SysUserToken token);

    /**
     * Entity转导出VO
     */
    @Named("toExportVO")
    @Mapping(target = "status", source = "status", qualifiedByName = "intTokenStatusToDesc")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysUserTokenExportVO toExportVO(SysUserToken token);

    /**
     * Entity列表转导出VO列表
     */
    @IterableMapping(qualifiedByName = "toExportVO")
    List<SysUserTokenExportVO> toExportVoList(List<SysUserToken> tokenList);

    /**
     * Token状态枚举转编码
     */
    @Named("tokenStatusToCode")
    default Integer tokenStatusToCode(TokenStatus status) {
        return status != null ? status.getCode() : null;
    }

    /**
     * 删除标记枚举转编码
     */
    @Named("deletedToCode")
    default Integer deletedToCode(GlobalEnum.Deleted deleted) {
        return deleted != null ? deleted.getCode() : null;
    }

    /**
     * Token状态编码转中文描述
     */
    @Named("intTokenStatusToDesc")
    default String intTokenStatusToDesc(Integer code) {
        if (code == null) return null;
        TokenStatus status = TokenStatus.getByCode(code);
        return status != null ? status.getDesc() : null;
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
