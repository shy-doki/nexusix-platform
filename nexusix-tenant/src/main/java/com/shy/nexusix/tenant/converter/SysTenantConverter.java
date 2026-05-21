package com.shy.nexusix.tenant.converter;

import com.shy.nexusix.tenant.entity.SysTenant;
import com.shy.nexusix.tenant.vo.SysTenantCommonVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SysTenantConverter {

    @Mapping(source = "createBy", target = "createByName")
    @Mapping(source = "createAt", target = "createTime")
    @Mapping(source = "updateBy", target = "updateByName")
    @Mapping(source = "updateAt", target = "updateTime")
    @Mapping(source = "deletedAt", target = "deleteTime")
    SysTenantCommonVO toCommonVO(SysTenant entity);

}
