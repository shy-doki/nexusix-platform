package com.shy.nexusix.system.converter;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.enums.GlobalEnum.Deleted;
import com.shy.nexusix.system.entity.SysFile;
import com.shy.nexusix.system.rto.SysFileUpdateRTO;
import com.shy.nexusix.system.vo.SysFileCommonVO;
import com.shy.nexusix.system.vo.SysFileDetailVO;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SysFileConverter {

    @Named("toCommVO")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeleteToDesc")
    SysFileCommonVO toCommonVO(SysFile file);

    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeleteToDesc")
    SysFileDetailVO toDetailVO(SysFile file);

    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeleteToDesc")
    SysFile toEntityUpdate(SysFileUpdateRTO rto);

    @IterableMapping(qualifiedByName = "toCommVO")
    List<SysFileCommonVO> toVoList(List<SysFile> list);

    default IPage<SysFileCommonVO> toVOPage(IPage<SysFile> entityPage) {
        if (entityPage == null) {
            return null;
        }
        IPage<SysFileCommonVO> voPage = new Page<>();
        voPage.setCurrent(entityPage.getCurrent());
        voPage.setSize(entityPage.getSize());
        voPage.setTotal(entityPage.getTotal());

        List<SysFileCommonVO> voList = toVoList(entityPage.getRecords());
        voPage.setRecords(voList);

        return voPage;
    }

    @Named("intDeleteToDesc")
    default String intDeleteToDesc(Integer code) {
        if (code == null) return null;
        Deleted deleted = GlobalEnum.Deleted.getByCode(code);
        return deleted != null ? deleted.getDesc() : null;
    }

}
