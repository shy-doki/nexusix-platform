package com.shy.nexusix.tenant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.common.rto.TimeRangeCommonRTO;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>租户信息服务实现类</p>
 *
 * @author shy
 * @since 2026-06-07
 */
@Service
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenant> implements ISysTenantService {

    @Autowired
    private SysTenantConverter sysTenantConverter;

    /**
     * <p>查询租户列表</p>
     *
     * @return 租户通用VO列表，封装用户有权查看的租户信息
     * @throws BusinessException 用户上下文为空、字段权限配置异常时抛出业务异常
     */
    @Override
    public List<SysTenantCommonVO> queryTenantList() {

        // 获取当前登录用户的上下文信息，包含权限配置
        UserContextDTO userContext = UserContext.getUserContext();

        // 从权限上下文中提取字段级权限配置
        UserContextDTO.FieldPerm fieldPerm = userContext.getPermInfo().getFieldPerm();
        // 获取用户对租户表的查询操作字段权限
        UserContextDTO.EntityFieldPerm tenantQueryPerm = fieldPerm.getQuery().get("sys_tenant");

        // 提取用户不可操作的字段列表 用于动态列选择
        List<String> invisibleFields = tenantQueryPerm.getInvisibleFields();

        // 构建查询条件 仅选择用户有权限查看的列，并排除已删除的租户记录
        LambdaQueryWrapper<SysTenant> wrapper = new LambdaQueryWrapper<SysTenant>()
                // 遍历 SysTenant 实体的所有字段 排除不可操作字段
                .select(SysTenant.class, entity -> !invisibleFields.contains(entity.getColumn()))
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        List<SysTenant> tenantList = this.list(wrapper);
        // 通过 MapStruct 转换器将实体列表转换为 VO 列表，同时完成状态码到描述的转换
        return sysTenantConverter.entityListToCommonVoList(tenantList);

    }

    /**
     * <p>分页查询租户列表</p>
     *
     * @param page 分页参数，包含页码和每页数量
     * @return 分页后的租户通用VO列表
     * @throws BusinessException 用户上下文为空、字段权限配置异常时抛出业务异常
     */
    @Override
    public IPage<SysTenantCommonVO> queryTenantPage(PageCommonRTO page) {

        // 获取当前登录用户的上下文信息，包含权限配置
        UserContextDTO userContext = UserContext.getUserContext();

        // 从权限上下文中提取字段级权限配置
        UserContextDTO.FieldPerm fieldPerm = userContext.getPermInfo().getFieldPerm();
        // 获取用户对租户表的查询操作字段权限
        UserContextDTO.EntityFieldPerm tenantQueryPerm = fieldPerm.getQuery().get("sys_tenant");

        // 提取用户不可操作的字段列表 用于动态列选择
        List<String> invisibleFields = tenantQueryPerm.getInvisibleFields();

        // 构建分页查询条件 仅选择用户有权限查看的列 并排除已删除的租户记录
        LambdaQueryWrapper<SysTenant> wrapper = new LambdaQueryWrapper<SysTenant>()
                .select(SysTenant.class, entity -> !invisibleFields.contains(entity.getColumn()))
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());

        // 执行分页查询
        Page<SysTenant> pageParam = new Page<>(page.getPageNum(), page.getPageSize());
        IPage<SysTenant> tenantPage = this.page(pageParam, wrapper);

        // 将分页实体结果转换为分页VO结果
        Page<SysTenantCommonVO> voPage = new Page<>(tenantPage.getCurrent(), tenantPage.getSize(), tenantPage.getTotal());
        voPage.setRecords(sysTenantConverter.entityListToCommonVoList(tenantPage.getRecords()));
        return voPage;

    }

    /**
     * <p>查询租户树形结构</p>
     *
     * @return 租户树形VO列表，包含完整的层级关系
     * @throws BusinessException 用户上下文为空、字段权限配置异常时抛出业务异常
     */
    @Override
    public List<SysTenantTreeVO> queryTenantTreeList() {

        // 获取当前登录用户的上下文信息，包含权限配置
        UserContextDTO userContext = UserContext.getUserContext();

        // 从权限上下文中提取字段级权限配置
        UserContextDTO.FieldPerm fieldPerm = userContext.getPermInfo().getFieldPerm();
        // 获取用户对租户表的查询操作字段权限
        UserContextDTO.EntityFieldPerm tenantQueryPerm = fieldPerm.getQuery().get("sys_tenant");

        // 提取用户不可操作的字段列表 用于动态列选择
        List<String> invisibleFields = tenantQueryPerm.getInvisibleFields();

        // 构建查询条件 仅选择用户有权限查看的列 并排除已删除的租户记录
        LambdaQueryWrapper<SysTenant> wrapper = new LambdaQueryWrapper<SysTenant>()
                .select(SysTenant.class, entity -> !invisibleFields.contains(entity.getColumn()))
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());

        // 查询所有租户列表
        List<SysTenant> tenantList = this.list(wrapper);

        // 构建树形结构并返回
        return buildTenantTree(tenantList);

    }

    /**
     * <p>分页查询租户树形结构</p>
     * <p>对根节点进行分页，每个根节点包含其完整的子树</p>
     *
     * @param page 分页参数，包含页码和每页数量
     * @return 分页后的租户树形VO列表
     * @throws BusinessException 用户上下文为空、字段权限配置异常时抛出业务异常
     */
    @Override
    public IPage<SysTenantTreeVO> queryTenantTreePage(PageCommonRTO page) {

        // 获取当前登录用户的上下文信息，包含权限配置
        UserContextDTO userContext = UserContext.getUserContext();

        // 从权限上下文中提取字段级权限配置
        UserContextDTO.FieldPerm fieldPerm = userContext.getPermInfo().getFieldPerm();
        // 获取用户对租户表的查询操作字段权限
        UserContextDTO.EntityFieldPerm tenantQueryPerm = fieldPerm.getQuery().get("sys_tenant");

        // 提取用户不可操作的字段列表 用于动态列选择
        List<String> invisibleFields = tenantQueryPerm.getInvisibleFields();

        // 构建查询条件 仅选择用户有权限查看的列 并排除已删除的租户记录
        LambdaQueryWrapper<SysTenant> wrapper = new LambdaQueryWrapper<SysTenant>()
                .select(SysTenant.class, entity -> !invisibleFields.contains(entity.getColumn()))
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());

        // 查询所有租户列表
        List<SysTenant> tenantList = this.list(wrapper);

        // 构建完整树形结构
        List<SysTenantTreeVO> fullTree = buildTenantTree(tenantList);

        // 对根节点进行内存分页
        int total = fullTree.size();
        int fromIndex = (page.getPageNum() - 1) * page.getPageSize();
        int toIndex = Math.min(fromIndex + page.getPageSize(), total);

        // 计算当前页的根节点数据
        List<SysTenantTreeVO> pageRecords;
        if (fromIndex >= total) {
            pageRecords = Collections.emptyList();
        } else {
            pageRecords = fullTree.subList(fromIndex, toIndex);
        }

        // 封装分页结果
        Page<SysTenantTreeVO> voPage = new Page<>(page.getPageNum(), page.getPageSize(), total);
        voPage.setRecords(pageRecords);
        return voPage;

    }

    /**
     * <p>查询指定租户的树形结构</p>
     * <p>以指定租户为根节点，构建其及所有后代的树形结构</p>
     *
     * @param id 租户ID，用于定位要查询的租户节点
     * @return 以指定租户为根的树形结构
     * @throws BusinessException 指定租户不存在时抛出业务异常
     */
    @Override
    public SysTenantTreeVO queryTenantTree(String id) {

        // 获取当前登录用户的上下文信息，包含权限配置
        UserContextDTO userContext = UserContext.getUserContext();

        // 从权限上下文中提取字段级权限配置
        UserContextDTO.FieldPerm fieldPerm = userContext.getPermInfo().getFieldPerm();
        // 获取用户对租户表的查询操作字段权限
        UserContextDTO.EntityFieldPerm tenantQueryPerm = fieldPerm.getQuery().get("sys_tenant");

        // 提取用户不可操作的字段列表 用于动态列选择
        List<String> invisibleFields = tenantQueryPerm.getInvisibleFields();

        // 根据ID查询指定租户
        SysTenant targetTenant = this.getById(id);
        // 校验租户是否存在或已被删除
        if (targetTenant == null || GlobalEnum.Deleted.DELETED.getCode().equals(targetTenant.getIsDeleted())) {
            throw new BusinessException("指定租户不存在");
        }

        // 查询所有未删除的租户 用于构建完整树形结构后定位目标节点
        LambdaQueryWrapper<SysTenant> wrapper = new LambdaQueryWrapper<SysTenant>()
                .select(SysTenant.class, entity -> !invisibleFields.contains(entity.getColumn()))
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        List<SysTenant> allTenants = this.list(wrapper);

        // 构建完整树形结构
        List<SysTenantTreeVO> fullTree = buildTenantTree(allTenants);

        // 在完整树中递归查找指定租户节点
        SysTenantTreeVO result = findTreeNode(fullTree, targetTenant.getTenantCode());
        // 未找到则抛出异常
        if (result == null) {
            throw new BusinessException("未找到指定租户的树形结构");
        }
        return result;

    }

    /**
     * <p>条件查询/筛选租户列表</p>
     * <p>根据查询条件动态构建查询，支持模糊匹配、精确匹配和时间范围筛选</p>
     *
     * @param queryParam 查询条件，包含各种筛选字段和分页参数
     * @return 满足条件的分页租户列表
     * @throws BusinessException 用户上下文为空、字段权限配置异常时抛出业务异常
     */
    @Override
    public IPage<SysTenantCommonVO> queryTenant(SysTenantQueryRTO queryParam) {

        // 获取当前登录用户的上下文信息，包含权限配置
        UserContextDTO userContext = UserContext.getUserContext();

        // 从权限上下文中提取字段级权限配置
        UserContextDTO.FieldPerm fieldPerm = userContext.getPermInfo().getFieldPerm();
        // 获取用户对租户表的查询操作字段权限
        UserContextDTO.EntityFieldPerm tenantQueryPerm = fieldPerm.getQuery().get("sys_tenant");

        // 提取用户不可操作的字段列表 用于动态列选择
        List<String> invisibleFields = tenantQueryPerm.getInvisibleFields();

        // 构建查询条件 仅选择用户有权限查看的列 并排除已删除的租户记录
        LambdaQueryWrapper<SysTenant> wrapper = new LambdaQueryWrapper<SysTenant>()
                .select(SysTenant.class, entity -> !invisibleFields.contains(entity.getColumn()))
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());

        // 租户编码 精确匹配
        if (queryParam.getTenantCode() != null && !queryParam.getTenantCode().isEmpty()) {
            wrapper.eq(SysTenant::getTenantCode, queryParam.getTenantCode());
        }
        // 租户名称 模糊匹配
        if (queryParam.getTenantName() != null && !queryParam.getTenantName().isEmpty()) {
            wrapper.like(SysTenant::getTenantName, queryParam.getTenantName());
        }
        // 租户类型 精确匹配
        if (queryParam.getTenantType() != null && !queryParam.getTenantType().isEmpty()) {
            wrapper.eq(SysTenant::getTenantType, queryParam.getTenantType());
        }
        // 联系人姓名 模糊匹配
        if (queryParam.getContactName() != null && !queryParam.getContactName().isEmpty()) {
            wrapper.like(SysTenant::getContactName, queryParam.getContactName());
        }
        // 联系人电话 精确匹配
        if (queryParam.getContactPhone() != null && !queryParam.getContactPhone().isEmpty()) {
            wrapper.eq(SysTenant::getContactPhone, queryParam.getContactPhone());
        }
        // 状态 精确匹配
        if (queryParam.getStatus() != null && !queryParam.getStatus().isEmpty()) {
            wrapper.eq(SysTenant::getStatus, queryParam.getStatus());
        }
        // 服务过期时间范围筛选
        applyTimeRange(wrapper, SysTenant::getExpireTime, queryParam.getExpireTimeRange());
        // 创建人编码 精确匹配
        if (queryParam.getCreateByCode() != null && !queryParam.getCreateByCode().isEmpty()) {
            wrapper.eq(SysTenant::getCreateBy, queryParam.getCreateByCode());
        }
        // 创建时间范围筛选
        applyTimeRange(wrapper, SysTenant::getCreateAt, queryParam.getCreateTimeRange());
        // 更新人编码 精确匹配
        if (queryParam.getUpdateByCode() != null && !queryParam.getUpdateByCode().isEmpty()) {
            wrapper.eq(SysTenant::getUpdateBy, queryParam.getUpdateByCode());
        }
        // 更新时间范围筛选
        applyTimeRange(wrapper, SysTenant::getUpdateAt, queryParam.getUpdateTimeRange());
        // 逻辑删除标记 精确匹配（超级管理员可查询已删除数据）
        if (queryParam.getIsDeleted() != null && !queryParam.getIsDeleted().isEmpty()) {
            wrapper.eq(SysTenant::getIsDeleted, queryParam.getIsDeleted());
        }

        // 执行分页查询
        Page<SysTenant> pageParam = new Page<>(queryParam.getPageNum(), queryParam.getPageSize());
        IPage<SysTenant> tenantPage = this.page(pageParam, wrapper);

        // 将分页实体结果转换为分页VO结果
        Page<SysTenantCommonVO> voPage = new Page<>(tenantPage.getCurrent(), tenantPage.getSize(), tenantPage.getTotal());
        voPage.setRecords(sysTenantConverter.entityListToCommonVoList(tenantPage.getRecords()));
        return voPage;

    }

    /**
     * <p>查询租户详情</p>
     * <p>根据租户编码查询租户的完整详细信息</p>
     *
     * @param tenantCode 租户编码
     * @return 租户详情VO，包含描述、logo、路径等扩展信息
     * @throws BusinessException 租户不存在时抛出业务异常
     */
    @Override
    public SysTenantDetailVO queryTenantDetail(String tenantCode) {

        // 获取当前登录用户的上下文信息，包含权限配置
        UserContextDTO userContext = UserContext.getUserContext();

        // 从权限上下文中提取字段级权限配置
        UserContextDTO.FieldPerm fieldPerm = userContext.getPermInfo().getFieldPerm();
        // 获取用户对租户表的查询操作字段权限
        UserContextDTO.EntityFieldPerm tenantQueryPerm = fieldPerm.getQuery().get("sys_tenant");

        // 提取用户不可操作的字段列表 用于动态列选择
        List<String> invisibleFields = tenantQueryPerm.getInvisibleFields();

        // 根据租户编码查询租户详情 排除已删除的记录
        LambdaQueryWrapper<SysTenant> wrapper = new LambdaQueryWrapper<SysTenant>()
                .select(SysTenant.class, entity -> !invisibleFields.contains(entity.getColumn()))
                .eq(SysTenant::getTenantCode, tenantCode)
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysTenant tenant = this.getOne(wrapper);

        // 校验租户是否存在
        if (tenant == null) {
            throw new BusinessException("租户不存在");
        }

        // 通过 MapStruct 转换器将实体转换为详情VO
        return sysTenantConverter.toDetailVO(tenant);

    }

    /**
     * <p>新增租户</p>
     * <p>校验租户编码唯一性，自动构建物化路径，设置审计字段后保存</p>
     *
     * @param addParam 新增租户参数
     * @return 新增结果行数
     * @throws BusinessException 租户编码已存在、父租户不存在时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer addTenant(SysTenantAddRTO addParam) {

        // 校验租户编码是否已存在
        LambdaQueryWrapper<SysTenant> existWrapper = new LambdaQueryWrapper<SysTenant>()
                .eq(SysTenant::getTenantCode, addParam.getTenantCode());
        if (this.count(existWrapper) > 0) {
            throw new BusinessException("租户编码已存在");
        }

        // 根据父租户编码查询父租户信息 用于构建层级关系
        LambdaQueryWrapper<SysTenant> parentWrapper = new LambdaQueryWrapper<SysTenant>()
                .eq(SysTenant::getTenantCode, addParam.getParentCode())
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysTenant parentTenant = this.getOne(parentWrapper);

        // 校验父租户是否存在
        if (parentTenant == null) {
            throw new BusinessException("父租户不存在");
        }

        // 实例化租户实体对象
        SysTenant entity = new SysTenant();
        // 设置租户编码
        entity.setTenantCode(addParam.getTenantCode());
        // 设置租户名称
        entity.setTenantName(addParam.getTenantName());
        // 设置租户类型
        entity.setTenantType(addParam.getTenantType());
        // 设置租户描述
        entity.setTenantDesc(addParam.getTenantDesc());
        // 设置租户logo路径
        entity.setTenantLogoUrl(addParam.getTenantLogoUrl());
        // 设置父租户ID
        entity.setParentId(String.valueOf(parentTenant.getId()));
        // 设置父租户名称
        entity.setParentName(addParam.getParentName());
        // 设置联系人姓名
        entity.setContactName(addParam.getContactName());
        // 设置联系人电话
        entity.setContactPhone(addParam.getContactPhone());
        // 设置状态 将枚举转换为编码存储
        entity.setStatus(addParam.getStatus().getCode());
        // 设置服务过期时间
        entity.setExpireTime(addParam.getExpireTime());
        // 设置套餐名称
        entity.setPackageName(addParam.getPackageName());
        // TODO 后续通过套餐服务根据packageCode查询packageId
        try {
            entity.setPackageId(Long.parseLong(addParam.getPackageCode()));
        } catch (NumberFormatException e) {
            // packageCode非数字格式 暂不设置packageId
        }
        // 设置扩展属性
        entity.setExtAttributes(addParam.getExtAttributes());
        // 新增租户默认无子租户
        entity.setHasChildren(false);

        // 构建物化路径 若请求中指定了路径则使用指定值 否则自动根据父租户路径生成
        if (addParam.getPath() != null && !addParam.getPath().isEmpty()) {
            entity.setPath(addParam.getPath());
        } else {
            // 根据父租户的物化路径自动构建当前租户的路径
            String parentPath = parentTenant.getPath();
            if (parentPath != null && !parentPath.isEmpty()) {
                entity.setPath(parentPath + "/" + addParam.getTenantCode());
            } else {
                // 父租户为根节点 路径为父租户编码/当前租户编码
                entity.setPath(parentTenant.getTenantCode() + "/" + addParam.getTenantCode());
            }
        }

        // 设置审计字段 创建人
        entity.setCreateBy(addParam.getCreateByCode() != null ? addParam.getCreateByCode() : "system");
        // 设置审计字段 创建时间
        entity.setCreateAt(addParam.getCreateTime() != null ? addParam.getCreateTime() : LocalDateTime.now());
        // 设置审计字段 更新人
        entity.setUpdateBy(addParam.getUpdateByCode() != null ? addParam.getUpdateByCode() : "system");
        // 设置审计字段 更新时间
        entity.setUpdateAt(addParam.getUpdateTime() != null ? addParam.getUpdateTime() : LocalDateTime.now());
        // 设置逻辑删除标记 默认未删除
        entity.setIsDeleted(addParam.getIsDeleted() != null ? addParam.getIsDeleted().getCode() : GlobalEnum.Deleted.NOT_DELETED.getCode());

        // 保存租户信息
        boolean saved = this.save(entity);
        if (!saved) {
            throw new BusinessException("新增租户失败");
        }

        // 更新父租户的hasChildren标记为true
        if (!Boolean.TRUE.equals(parentTenant.getHasChildren())) {
            parentTenant.setHasChildren(true);
            this.updateById(parentTenant);
        }

        return 1;

    }

    /**
     * <p>修改租户</p>
     * <p>根据租户编码定位租户记录，更新其可修改字段</p>
     *
     * @param updateParam 修改租户参数
     * @return 修改结果行数
     * @throws BusinessException 租户不存在、父租户不存在时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateTenant(SysTenantUpdateRTO updateParam) {

        // 根据租户编码查询待更新的租户
        LambdaQueryWrapper<SysTenant> wrapper = new LambdaQueryWrapper<SysTenant>()
                .eq(SysTenant::getTenantCode, updateParam.getTenantCode())
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysTenant entity = this.getOne(wrapper);

        // 校验租户是否存在
        if (entity == null) {
            throw new BusinessException("租户不存在");
        }

        // 判断父租户是否发生变更
        boolean parentChanged = !String.valueOf(entity.getParentId()).equals(updateParam.getParentCode());

        // 设置租户名称
        entity.setTenantName(updateParam.getTenantName());
        // 设置租户类型
        entity.setTenantType(updateParam.getTenantType());
        // 设置租户描述
        entity.setTenantDesc(updateParam.getTenantDesc());
        // 设置租户logo路径
        entity.setTenantLogoUrl(updateParam.getTenantLogoUrl());
        // 设置父租户编码
        entity.setParentId(updateParam.getParentCode());
        // 设置父租户名称
        entity.setParentName(updateParam.getParentName());
        // 设置联系人姓名
        entity.setContactName(updateParam.getContactName());
        // 设置联系人电话
        entity.setContactPhone(updateParam.getContactPhone());
        // 设置状态 将枚举转换为编码存储
        entity.setStatus(updateParam.getStatus().getCode());
        // 设置服务过期时间
        entity.setExpireTime(updateParam.getExpireTime());
        // 设置套餐名称
        entity.setPackageName(updateParam.getPackageName());
        // TODO 后续通过套餐服务根据packageCode查询packageId
        try {
            entity.setPackageId(Long.parseLong(updateParam.getPackageCode()));
        } catch (NumberFormatException e) {
            // packageCode非数字格式 暂不设置packageId
        }
        // 设置扩展属性
        entity.setExtAttributes(updateParam.getExtAttributes());

        // 若父租户发生变更 需要重新构建物化路径
        if (parentChanged) {
            // 根据新的父租户编码查询父租户信息
            LambdaQueryWrapper<SysTenant> parentWrapper = new LambdaQueryWrapper<SysTenant>()
                    .eq(SysTenant::getTenantCode, updateParam.getParentCode())
                    .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
            SysTenant parentTenant = this.getOne(parentWrapper);
            // 校验父租户是否存在
            if (parentTenant == null) {
                throw new BusinessException("父租户不存在");
            }
            // 根据父租户路径重新构建当前租户的物化路径
            if (updateParam.getPath() != null && !updateParam.getPath().isEmpty()) {
                entity.setPath(updateParam.getPath());
            } else {
                String parentPath = parentTenant.getPath();
                if (parentPath != null && !parentPath.isEmpty()) {
                    entity.setPath(parentPath + "/" + entity.getTenantCode());
                } else {
                    entity.setPath(parentTenant.getTenantCode() + "/" + entity.getTenantCode());
                }
            }
            // TODO 更新所有后代节点的物化路径
        }

        // 设置审计字段 更新人
        entity.setUpdateBy(updateParam.getUpdateByCode() != null ? updateParam.getUpdateByCode() : "system");
        // 设置审计字段 更新时间
        entity.setUpdateAt(updateParam.getUpdateTime() != null ? updateParam.getUpdateTime() : LocalDateTime.now());

        // 执行更新
        boolean updated = this.updateById(entity);
        if (!updated) {
            throw new BusinessException("修改租户失败");
        }

        return 1;

    }

    /**
     * <p>更新租户状态</p>
     * <p>更新指定租户的状态（启用/停用/过期），停用后租户下所有用户无法登录</p>
     *
     * @param id 租户ID
     * @param status 租户状态编码
     * @return 更新结果行数
     * @throws BusinessException 租户不存在、状态编码无效时抛出业务异常
     */
    @Override
    public Integer updateTenantStatus(String id, String status) {

        // 校验状态编码是否合法
        if (!GlobalEnum.TenantStatus.isValidCode(status)) {
            throw new BusinessException("无效的租户状态编码");
        }

        // 根据ID查询租户
        SysTenant entity = this.getById(id);
        // 校验租户是否存在
        if (entity == null || GlobalEnum.Deleted.DELETED.getCode().equals(entity.getIsDeleted())) {
            throw new BusinessException("租户不存在");
        }

        // 构建更新条件 仅更新状态和更新时间
        LambdaUpdateWrapper<SysTenant> wrapper = new LambdaUpdateWrapper<SysTenant>()
                .eq(SysTenant::getId, id)
                .set(SysTenant::getStatus, status)
                .set(SysTenant::getUpdateAt, LocalDateTime.now());
        // 执行更新
        return this.baseMapper.update(wrapper);

    }

    /**
     * <p>删除租户</p>
     * <p>逻辑删除指定租户，设置删除标记和删除时间</p>
     *
     * @param id 租户ID
     * @return 删除结果行数
     * @throws BusinessException 租户不存在时抛出业务异常
     */
    @Override
    public Integer deleteTenant(String id) {

        // 根据ID查询租户
        SysTenant entity = this.getById(id);
        // 校验租户是否存在或已被删除
        if (entity == null || GlobalEnum.Deleted.DELETED.getCode().equals(entity.getIsDeleted())) {
            throw new BusinessException("租户不存在或已被删除");
        }

        // 构建逻辑删除更新条件 设置删除标记和删除时间
        LambdaUpdateWrapper<SysTenant> wrapper = new LambdaUpdateWrapper<SysTenant>()
                .eq(SysTenant::getId, id)
                .set(SysTenant::getIsDeleted, GlobalEnum.Deleted.DELETED.getCode())
                .set(SysTenant::getDeletedAt, LocalDateTime.now());
        // 执行逻辑删除
        return this.baseMapper.update(wrapper);

    }

    /**
     * <p>批量新增租户</p>
     * <p>批量新增多个租户信息，任一租户新增失败则全部回滚</p>
     *
     * @param addParamList 批量新增租户参数集合
     * @return 成功新增的租户数量
     * @throws BusinessException 任一租户编码已存在、父租户不存在时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchAddTenant(List<SysTenantAddRTO> addParamList) {

        // 遍历新增参数列表 逐个调用新增方法 事务保证原子性
        int count = 0;
        for (SysTenantAddRTO addParam : addParamList) {
            count += addTenant(addParam);
        }
        return count;

    }

    /**
     * <p>批量修改租户</p>
     * <p>批量修改多个租户信息，任一租户修改失败则全部回滚</p>
     *
     * @param updateParamList 批量修改租户参数集合
     * @return 成功修改的租户数量
     * @throws BusinessException 任一租户不存在、父租户不存在时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdateTenant(List<SysTenantUpdateRTO> updateParamList) {

        // 遍历修改参数列表 逐个调用修改方法 事务保证原子性
        int count = 0;
        for (SysTenantUpdateRTO updateParam : updateParamList) {
            count += updateTenant(updateParam);
        }
        return count;

    }

    /**
     * <p>批量更新租户状态</p>
     * <p>批量更新多个指定租户的状态，任一更新失败则全部回滚</p>
     *
     * @param ids 租户ID集合
     * @param status 租户状态编码
     * @return 更新结果行数
     * @throws BusinessException 状态编码无效时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdateTenantStatus(List<String> ids, String status) {

        // 校验状态编码是否合法
        if (!GlobalEnum.TenantStatus.isValidCode(status)) {
            throw new BusinessException("无效的租户状态编码");
        }

        // 构建批量更新条件 仅更新状态和更新时间
        LambdaUpdateWrapper<SysTenant> wrapper = new LambdaUpdateWrapper<SysTenant>()
                .in(SysTenant::getId, ids)
                .set(SysTenant::getStatus, status)
                .set(SysTenant::getUpdateAt, LocalDateTime.now());
        // 执行批量更新
        return this.baseMapper.update(wrapper);

    }

    /**
     * <p>批量删除租户</p>
     * <p>批量逻辑删除多个指定租户，任一删除失败则全部回滚</p>
     *
     * @param ids 租户ID集合
     * @return 删除结果行数
     * @throws BusinessException 租户ID集合为空时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchDeleteTenant(List<String> ids) {

        // 校验租户ID集合不能为空
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException("租户ID集合不能为空");
        }

        // 构建批量逻辑删除更新条件 设置删除标记和删除时间
        LambdaUpdateWrapper<SysTenant> wrapper = new LambdaUpdateWrapper<SysTenant>()
                .in(SysTenant::getId, ids)
                .set(SysTenant::getIsDeleted, GlobalEnum.Deleted.DELETED.getCode())
                .set(SysTenant::getDeletedAt, LocalDateTime.now());
        // 执行批量逻辑删除
        return this.baseMapper.update(wrapper);

    }

    /**
     * <p>分配子租户</p>
     * <p>为指定父租户分配子租户，自动更新子租户的父租户信息和物化路径</p>
     *
     * @param assignParam 分配参数，包含父租户编码和子租户编码集合
     * @return 更新子租户行数
     * @throws BusinessException 父租户不存在、子租户不存在时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer assignSubTenant(SysTenantAssignRTO assignParam) {

        // 根据父租户编码查询父租户信息
        LambdaQueryWrapper<SysTenant> parentWrapper = new LambdaQueryWrapper<SysTenant>()
                .eq(SysTenant::getTenantCode, assignParam.getParentCode())
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysTenant parentTenant = this.getOne(parentWrapper);

        // 校验父租户是否存在
        if (parentTenant == null) {
            throw new BusinessException("父租户不存在");
        }

        // 根据子租户编码集合查询所有子租户
        LambdaQueryWrapper<SysTenant> subWrapper = new LambdaQueryWrapper<SysTenant>()
                .in(SysTenant::getTenantCode, assignParam.getSubCode())
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        List<SysTenant> subTenants = this.list(subWrapper);

        // 校验子租户是否全部存在
        if (subTenants.size() != assignParam.getSubCode().size()) {
            throw new BusinessException("部分子租户不存在");
        }

        // 构建父租户的物化路径前缀 用于生成子租户的新路径
        String parentPath = parentTenant.getPath();
        String pathPrefix;
        if (parentPath != null && !parentPath.isEmpty()) {
            pathPrefix = parentPath;
        } else {
            // 父租户为根节点 路径前缀为父租户编码
            pathPrefix = parentTenant.getTenantCode();
        }

        // 遍历更新每个子租户的父租户信息和物化路径
        int count = 0;
        for (SysTenant subTenant : subTenants) {
            // 设置新的父租户ID
            subTenant.setParentId(String.valueOf(parentTenant.getId()));
            // 设置新的父租户名称
            subTenant.setParentName(parentTenant.getTenantName());
            // 重新构建物化路径
            subTenant.setPath(pathPrefix + "/" + subTenant.getTenantCode());
            // 设置更新时间
            subTenant.setUpdateAt(LocalDateTime.now());
            // 执行更新
            this.updateById(subTenant);
            count++;
        }

        // 更新父租户的hasChildren标记为true
        if (!Boolean.TRUE.equals(parentTenant.getHasChildren())) {
            parentTenant.setHasChildren(true);
            this.updateById(parentTenant);
        }

        return count;

    }

    /**
     * <p>分配父租户</p>
     * <p>为指定子租户分配新的父租户，处理层级关系调整及物化路径更新，
     * 会进行循环层级验证，避免形成环状结构</p>
     *
     * @param assignParam 分配参数，包含父租户编码和子租户编码集合
     * @return 更新子租户行数
     * @throws BusinessException 父租户不存在、子租户不存在、存在循环引用时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer assignParentTenant(SysTenantAssignRTO assignParam) {

        // 根据父租户编码查询父租户信息
        LambdaQueryWrapper<SysTenant> parentWrapper = new LambdaQueryWrapper<SysTenant>()
                .eq(SysTenant::getTenantCode, assignParam.getParentCode())
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysTenant parentTenant = this.getOne(parentWrapper);

        // 校验父租户是否存在
        if (parentTenant == null) {
            throw new BusinessException("父租户不存在");
        }

        // 根据子租户编码集合查询所有子租户
        LambdaQueryWrapper<SysTenant> subWrapper = new LambdaQueryWrapper<SysTenant>()
                .in(SysTenant::getTenantCode, assignParam.getSubCode())
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        List<SysTenant> subTenants = this.list(subWrapper);

        // 校验子租户是否全部存在
        if (subTenants.size() != assignParam.getSubCode().size()) {
            throw new BusinessException("部分子租户不存在");
        }

        // 循环层级验证 检查父租户是否是任一子租户的后代 避免形成环状结构
        String parentPath = parentTenant.getPath();
        for (SysTenant subTenant : subTenants) {
            // 若父租户的物化路径包含子租户的编码 则说明父租户是该子租户的后代 会形成循环
            if (parentPath != null && parentPath.contains(subTenant.getTenantCode())) {
                throw new BusinessException("分配父租户失败：存在循环引用，父租户[" + parentTenant.getTenantCode() + "]是子租户[" + subTenant.getTenantCode() + "]的后代");
            }
        }

        // 构建父租户的物化路径前缀 用于生成子租户的新路径
        String pathPrefix;
        if (parentPath != null && !parentPath.isEmpty()) {
            pathPrefix = parentPath;
        } else {
            // 父租户为根节点 路径前缀为父租户编码
            pathPrefix = parentTenant.getTenantCode();
        }

        // 遍历更新每个子租户的父租户信息和物化路径
        int count = 0;
        for (SysTenant subTenant : subTenants) {
            // 设置新的父租户ID
            subTenant.setParentId(String.valueOf(parentTenant.getId()));
            // 设置新的父租户名称
            subTenant.setParentName(parentTenant.getTenantName());
            // 重新构建物化路径
            subTenant.setPath(pathPrefix + "/" + subTenant.getTenantCode());
            // 设置更新时间
            subTenant.setUpdateAt(LocalDateTime.now());
            // 执行更新
            this.updateById(subTenant);
            count++;
        }

        // 更新父租户的hasChildren标记为true
        if (!Boolean.TRUE.equals(parentTenant.getHasChildren())) {
            parentTenant.setHasChildren(true);
            this.updateById(parentTenant);
        }

        return count;

    }

    /**
     * <p>构建租户树形结构</p>
     * <p>根据父租户ID将平铺的租户列表组装为树形结构</p>
     *
     * @param tenantList 平铺的租户实体列表
     * @return 树形结构的VO列表
     */
    private List<SysTenantTreeVO> buildTenantTree(List<SysTenant> tenantList) {

        // 构建父租户ID到子租户列表的映射
        Map<String, List<SysTenant>> childrenMap = new HashMap<>();
        // 根节点列表
        List<SysTenant> rootEntities = new ArrayList<>();

        for (SysTenant tenant : tenantList) {
            String parentId = tenant.getParentId();
            if (parentId == null || parentId.isEmpty() || "0".equals(parentId)) {
                // 无父租户 为根节点
                rootEntities.add(tenant);
            } else {
                // 有父租户 加入对应父节点的子列表
                childrenMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(tenant);
            }
        }

        // 递归构建树形结构
        List<SysTenantTreeVO> result = new ArrayList<>();
        for (SysTenant root : rootEntities) {
            result.add(buildTreeNode(root, childrenMap));
        }
        return result;

    }

    /**
     * <p>递归构建树节点</p>
     * <p>将单个租户实体转换为树形VO，并递归构建其子节点</p>
     *
     * @param entity 当前租户实体
     * @param childrenMap 父租户ID到子租户列表的映射
     * @return 当前节点的树形VO
     */
    private SysTenantTreeVO buildTreeNode(SysTenant entity, Map<String, List<SysTenant>> childrenMap) {

        // 将当前实体转换为树形VO
        SysTenantTreeVO vo = sysTenantConverter.toTreeVO(entity);
        // 查找当前节点的子租户列表
        String entityId = String.valueOf(entity.getId());
        List<SysTenant> children = childrenMap.get(entityId);
        if (children != null && !children.isEmpty()) {
            // 递归构建子节点
            List<SysTenantTreeVO> childVOs = new ArrayList<>();
            for (SysTenant child : children) {
                childVOs.add(buildTreeNode(child, childrenMap));
            }
            vo.setChildTenant(childVOs);
        }
        return vo;

    }

    /**
     * <p>在树形结构中递归查找指定租户编码的节点</p>
     *
     * @param nodes 树形节点列表
     * @param tenantCode 目标租户编码
     * @return 匹配的树形VO节点，未找到返回null
     */
    private SysTenantTreeVO findTreeNode(List<SysTenantTreeVO> nodes, String tenantCode) {

        for (SysTenantTreeVO node : nodes) {
            // 当前节点匹配则直接返回
            if (tenantCode.equals(node.getTenantCode())) {
                return node;
            }
            // 递归查找子节点
            if (node.getChildTenant() != null) {
                SysTenantTreeVO found = findTreeNode(node.getChildTenant(), tenantCode);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;

    }

    /**
     * <p>应用时间范围条件到查询包装器</p>
     * <p>根据时间范围对象的起始和结束时间，添加between条件到查询中</p>
     *
     * @param wrapper Lambda查询包装器
     * @param columnGetter 实体字段的getter方法引用
     * @param timeRange 时间范围对象，包含startTime和endTime
     */
    private <T> void applyTimeRange(LambdaQueryWrapper<SysTenant> wrapper,
                                     com.baomidou.mybatisplus.core.toolkit.support.SFunction<SysTenant, T> columnGetter,
                                     TimeRangeCommonRTO timeRange) {

        // 时间范围对象为空则跳过
        if (timeRange == null) {
            return;
        }
        // 同时存在起始和结束时间 使用between条件
        if (timeRange.getStartTime() != null && timeRange.getEndTime() != null) {
            wrapper.between(columnGetter, timeRange.getStartTime(), timeRange.getEndTime());
        }
        // 仅存在起始时间 使用大于等于条件
        else if (timeRange.getStartTime() != null) {
            wrapper.ge(columnGetter, timeRange.getStartTime());
        }
        // 仅存在结束时间 使用小于等于条件
        else if (timeRange.getEndTime() != null) {
            wrapper.le(columnGetter, timeRange.getEndTime());
        }

    }

}
