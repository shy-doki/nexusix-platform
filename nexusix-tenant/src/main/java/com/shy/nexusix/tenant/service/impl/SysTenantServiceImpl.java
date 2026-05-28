package com.shy.nexusix.tenant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.core.context.UserContext;
import com.shy.nexusix.core.entity.dto.UserContextDTO;
import com.shy.nexusix.tenant.converter.SysTenantConverter;
import com.shy.nexusix.tenant.entity.SysTenant;
import com.shy.nexusix.tenant.mapper.SysTenantMapper;
import com.shy.nexusix.tenant.rto.SysTenantAddRTO;
import com.shy.nexusix.tenant.rto.SysTenantAssignRTO;
import com.shy.nexusix.tenant.rto.SysTenantQueryRTO;
import com.shy.nexusix.tenant.rto.SysTenantUpdateRTO;
import com.shy.nexusix.tenant.service.ISysTenantService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shy.nexusix.tenant.vo.SysTenantCommonVO;
import com.shy.nexusix.tenant.vo.SysTenantDetailVO;
import com.shy.nexusix.tenant.vo.SysTenantTreeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 租户信息表 - 存储租户基础信息，支持无限层级 服务实现类
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Service
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenant> implements ISysTenantService {

    @Autowired
    private SysTenantConverter sysTenantConverter;

    @Override
    public List<SysTenantCommonVO> queryTenantList() {

        UserContextDTO userContext = UserContext.getUserContext();

        // 获取当前用户对租户表的访问权限
        UserContextDTO.FieldPerm fieldPerm = userContext.getPermInfo().getFieldPerm();
        UserContextDTO.EntityFieldPerm tenantQueryPerm = fieldPerm.getQuery().get("sys_tenant");

        List<String> visibleFields = tenantQueryPerm.getVisibleFields();

        LambdaQueryWrapper<SysTenant> wrapper = new LambdaQueryWrapper<SysTenant>()
                .select(SysTenant.class, entity -> visibleFields.contains(entity.getColumn()))
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        List<SysTenant> tenantList = this.list(wrapper);
        return sysTenantConverter.entityListToCommonVoList(tenantList);

    }

    @Override
    public IPage<SysTenantCommonVO> queryTenantPage(PageCommonRTO page) {
        return null;
    }

    @Override
    public List<SysTenantTreeVO> queryTenantTreeList() {
        return List.of();
    }

    @Override
    public IPage<SysTenantTreeVO> queryTenantTreePage(PageCommonRTO page) {
        return null;
    }

    @Override
    public SysTenantTreeVO queryTenantTree(String id) {
        return null;
    }

    @Override
    public IPage<SysTenantCommonVO> queryTenant(SysTenantQueryRTO queryParam) {
        return null;
    }

    @Override
    public SysTenantDetailVO queryTenantDetail(String tenantCode) {
        return null;
    }

    @Override
    public Integer addTenant(SysTenantAddRTO addParam) {
        return 0;
    }

    @Override
    public Integer updateTenant(SysTenantUpdateRTO updateParam) {
        return 0;
    }

    @Override
    public Integer updateTenantStatus(String id, String status) {
        return 0;
    }

    @Override
    public Integer deleteTenant(String id) {
        return 0;
    }

    @Override
    public Integer batchAddTenant(List<SysTenantAddRTO> addParamList) {
        return 0;
    }

    @Override
    public Integer batchUpdateTenant(List<SysTenantUpdateRTO> updateParamList) {
        return 0;
    }

    @Override
    public Integer batchUpdateTenantStatus(List<String> ids, String status) {
        return 0;
    }

    @Override
    public Integer batchDeleteTenant(List<String> ids) {
        return 0;
    }

    @Override
    public Integer assignSubTenant(SysTenantAssignRTO assignParam) {
        return 0;
    }

    @Override
    public Integer assignParentTenant(SysTenantAssignRTO assignParam) {
        return 0;
    }

}
