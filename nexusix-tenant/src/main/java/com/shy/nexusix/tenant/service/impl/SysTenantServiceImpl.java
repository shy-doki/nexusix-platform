package com.shy.nexusix.tenant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shy.nexusix.common.constant.GlobalConstant;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.common.rto.TimeRangeCommonRTO;
import com.shy.nexusix.core.context.UserContext;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    /**
     * <p>
     * 查询租户列表
     * </p>
     * <p>
     * 返回所有租户的平铺列表，租户编码会自动进行脱敏处理（保留前3位和后3位）。
     * 需要登录并具备租户查看权限才能访问。
     * </p>
     *
     * @return 租户列表，包含租户名称、脱敏后的租户编码、联系人、状态等信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-04-19
     */
    @Override
    public List<SysTenantCommonVO> queryTenantList() {

        // 构建查询条件：仅查询未删除的租户，按创建时间倒序排列
        LambdaQueryWrapper<SysTenant> wrapper = new LambdaQueryWrapper<SysTenant>()
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .orderByDesc(SysTenant::getCreateTime);

//        if (!UserContext.isSuperAdmin()) {
//
//        }

        // 获取该用户所能操作的列
        Set<String> unOperableColumnSet = UserContext.getUnOperableColumns(GlobalConstant.Table.TENANT, GlobalConstant.OperableType.QUERY_TYPE);

        if (!unOperableColumnSet.isEmpty()) {
            // 构建查询条件 仅查询可操作列
            // wrapper.select(SysTenant.class, column -> !unOperableColumnSet.contains(column.getProperty()));
            wrapper.select(SysTenant.class, column -> {
                String columnName = column.getColumn();
                return !unOperableColumnSet.contains(columnName);
            });
        }

        // 执行查询获取租户列表
        List<SysTenant> tenantList = this.list(wrapper);

        // 转换为VO对象并返回
        return sysTenantConverter.toVoList(tenantList);
    }

    /**
     * <p>
     * 分页查询租户列表
     * </p>
     * <p>
     * 返回分页后的租户列表，租户编码会自动进行脱敏处理（保留前3位和后3位）。
     * 需要登录并具备租户查看权限才能访问。
     * </p>
     *
     * @param page 分页参数
     * @return 分页后的租户列表，包含租户名称、脱敏后的租户编码、联系人、状态等信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限
     * @author shy
     * @since 2026-04-19
     */
    @Override
    public IPage<SysTenantCommonVO> queryTenantPage(PageCommonRTO page) {

        // 构建分页参数
        IPage<SysTenant> pageParam = new Page<>(page.getPageNum(), page.getPageSize());

        // 构建查询条件：仅查询未删除的租户，按创建时间倒序排列
        LambdaQueryWrapper<SysTenant> wrapper = new LambdaQueryWrapper<SysTenant>()
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .orderByDesc(SysTenant::getCreateTime);

        // 执行分页查询
        IPage<SysTenant> tenantPage = this.page(pageParam, wrapper);

        // 转换为VO分页对象并返回
        return sysTenantConverter.toVOPage(tenantPage);
    }

    /**
     * <p>
     * 查询租户树形结构
     * </p>
     * <p>
     * 返回所有租户的层级树形结构
     * 需要登录并具备租户查看权限才能访问。
     * </p>
     *
     * @return 树形结构列表，包含租户名称、脱敏后的租户编码、联系人、状态等信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限
     * @author shy
     * @since 2026-04-19
     */
    @Override
    public List<SysTenantTreeVO> queryTenantTreeList() {

        // 查询所有未删除的租户，按创建时间倒序排列
        LambdaQueryWrapper<SysTenant> wrapper = new LambdaQueryWrapper<SysTenant>()
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .orderByDesc(SysTenant::getCreateTime);

        List<SysTenant> tenantList = this.list(wrapper);

        // 转换为树形视图对象列表
        List<SysTenantTreeVO> treeVOList = sysTenantConverter.toTreeVOList(tenantList);

        // 构建树形结构并返回
        return buildTree(treeVOList);

    }

    /**
     * <p>
     * 分页查询租户树形结构
     * </p>
     * <p>
     * 对根租户进行分页，每页返回根节点及其完整子树。
     * 分页参数仅作用于根节点层级，子树数据完整展开。
     * 需要登录并具备租户查看权限才能访问。
     * </p>
     *
     * @param page 分页参数
     * @return 分页后的租户树形结构
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限
     * @author shy
     * @since 2026-04-19
     */
    @Override
    public IPage<SysTenantTreeVO> queryTenantTreePage(PageCommonRTO page) {

        // 构建分页参数
        Page<SysTenant> pageParam = new Page<>(page.getPageNum(), page.getPageSize());

        // 仅分页查询根租户（parentId = 0）
        LambdaQueryWrapper<SysTenant> rootWrapper = new LambdaQueryWrapper<SysTenant>()
                .and(w -> w.eq(SysTenant::getParentId, "rootTenant").or().isNull(SysTenant::getParentId))
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .orderByDesc(SysTenant::getCreateTime);

        IPage<SysTenant> rootPage = this.page(pageParam, rootWrapper);

        // 收集当前页根租户的所有 ID，用于查询子树
        List<Long> rootIds = rootPage.getRecords().stream()
                .map(SysTenant::getId)
                .toList();

        if (rootIds.isEmpty()) {
            return new Page<>(page.getPageNum(), page.getPageSize(), 0);
        }

        // 查询所有根租户的后代节点（利用 ancestors 前缀匹配优化）
        // 对于每个根租户，其后代的 ancestors 必然以该根租户的路径为前缀
        List<SysTenant> allDescendants = new ArrayList<>();
        for (SysTenant root : rootPage.getRecords()) {
            if (Boolean.TRUE.equals(root.getHasChildren())) {
                List<SysTenant> descendants = findDescendantsByAncestors(
                        root.getTenantCode(),
                        root.getAncestors() != null ? root.getAncestors() : "root"
                );
                allDescendants.addAll(descendants);
            }
        }

        // 合并根租户和后代租户
        List<SysTenant> allTenants = new ArrayList<>(rootPage.getRecords());
        allTenants.addAll(allDescendants);

        // 转换并构建树形结构
        List<SysTenantTreeVO> treeVOList = sysTenantConverter.toTreeVOList(allTenants);
        List<SysTenantTreeVO> tree = buildTree(treeVOList);

        // 构建分页返回对象
        IPage<SysTenantTreeVO> resultPage = new Page<>(
                rootPage.getCurrent(),
                rootPage.getSize(),
                rootPage.getTotal()
        );
        resultPage.setRecords(tree);

        return resultPage;

    }

    /**
     * <p>
     * 查询指定租户的树形结构
     * </p>
     * <p>
     * 查询系统中指定租户及其所有后代的层级关系，并构建成树形结构返回。
     * 利用 ancestors 字段前缀匹配优化查询，避免全表扫描。
     * 返回的租户编码会自动进行脱敏处理（保留前3位和后3位，中间用星号替换）。
     * </p>
     *
     * @param id 租户Id，用于定位要查询的租户节点
     * @return 租户树形结构，包含该租户及其所有后代节点
     * @throws com.shy.nexusix.common.exception.BusinessException 当数据库查询失败或数据异常时抛出
     * @author shy
     * @since 2026-04-19
     */
    @Override
    public SysTenantTreeVO queryTenantTree(String id) {

        // 参数校验：租户ID不能为空
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(400, "租户ID不能为空");
        }

        // 查询目标租户是否存在
        LambdaQueryWrapper<SysTenant> targetWrapper = new LambdaQueryWrapper<SysTenant>()
                .eq(SysTenant::getId, id)
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysTenant targetTenant = this.getOne(targetWrapper);

        if (targetTenant == null) {
            throw new BusinessException(404, "租户不存在");
        }

        // 利用 ancestors 前缀匹配查询所有后代节点（优化点：避免全表扫描）
        List<SysTenant> descendants = findDescendantsByAncestors(
                targetTenant.getTenantCode(),
                targetTenant.getAncestors() != null ? targetTenant.getAncestors() : "root"
        );

        // 合并目标租户和后代租户
        List<SysTenant> subtreeTenants = new ArrayList<>();
        subtreeTenants.add(targetTenant);
        subtreeTenants.addAll(descendants);

        // 转换为树形视图对象列表
        List<SysTenantTreeVO> treeVOList = sysTenantConverter.toTreeVOList(subtreeTenants);

        // 构建树形结构
        List<SysTenantTreeVO> tree = buildTree(treeVOList);

        // 目标租户为根节点，取第一个即为所求
        // 如果目标租户本身是根节点（parentId=0），则直接返回
        // 否则需从树中找到目标租户节点
        if (targetTenant.getParentId() == null || "rootTenant".equals(targetTenant.getParentId())) {
            return tree.isEmpty() ? null : tree.get(0);
        }

        // 目标租户非根节点，需从构建的树中查找
        return findNodeInTree(tree, targetTenant.getTenantCode());

    }

    /**
     * <p>
     * 条件查询\筛选租户列表
     * </p>
     * <p>
     * 返回满足条件的租户列表，租户编码会自动进行脱敏处理（保留前3位和后3位）。
     * 需要登录并具备租户条件查询权限才能访问。
     * </p>
     *
     * @param queryParam 查询条件
     * @return 满足条件的租户列表，包含租户名称、脱敏后的租户编码、联系人、状态等信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-04-19
     */
    @Override
    public IPage<SysTenantCommonVO> queryTenant(SysTenantQueryRTO queryParam) {

        // 构建分页参数
        Page<SysTenant> pageParam = new Page<>(queryParam.getPageNum(), queryParam.getPageSize());

        // 构建动态查询条件
        LambdaQueryWrapper<SysTenant> wrapper = new LambdaQueryWrapper<>();

        // 租户名称模糊查询
        wrapper.like(StringUtils.isNotBlank(queryParam.getTenantName()),
                SysTenant::getTenantName, queryParam.getTenantName());

        // 租户类型查询
        wrapper.eq(StringUtils.isNotBlank(queryParam.getTenantType()),
                SysTenant::getTenantType, queryParam.getTenantType());

        // 联系人姓名精确查询
        wrapper.eq(StringUtils.isNotBlank(queryParam.getContactName()),
                SysTenant::getContactName, queryParam.getContactName());

        // 联系人电话条件查询
        wrapper.eq(StringUtils.isNotBlank(queryParam.getContactPhone()),
                SysTenant::getContactPhone, queryParam.getContactPhone());

        // 状态条件查询
        if (StringUtils.isNotBlank(queryParam.getStatus())) {
            wrapper.eq(SysTenant::getStatus, GlobalEnum.TenantStatus.parse(queryParam.getStatus()).getCode());
        }

        // 父租户条件查询
        if (queryParam.getTenantCode() != null) {
            wrapper.eq(SysTenant::getTenantCode, queryParam.getTenantCode());
        }

        // 服务过期时间范围查询
        TimeRangeCommonRTO expireTime = queryParam.getExpireTime();
        if (expireTime != null) {
            LocalDateTime startTime = expireTime.getStartTime();
            LocalDateTime endTime = expireTime.getEndTime();

            // 校验时间范围合法性
            if (startTime != null && endTime != null && startTime.isAfter(endTime)) {
                throw new BusinessException(400, "开始时间不能晚于结束时间");
            }

            // 根据时间范围构建查询条件
            if (startTime != null && endTime != null) {
                // 两者都有 between查询
                wrapper.between(SysTenant::getExpireTime, startTime, endTime);
            } else if (startTime != null) {
                // 只有开始时间 大于等于查询
                wrapper.ge(SysTenant::getExpireTime, startTime);
            } else if (endTime != null) {
                // 只有结束时间 小于等于查询
                wrapper.le(SysTenant::getExpireTime, endTime);
            }
        }

        // 创建人姓名精确查询
        wrapper.eq(StringUtils.isNotBlank(queryParam.getCreateByName()),
                SysTenant::getCreateByName, queryParam.getCreateByName());

        // 创建时间范围查询
        TimeRangeCommonRTO createTime = queryParam.getCreateTime();
        if (createTime != null) {
            LocalDateTime startTime = createTime.getStartTime();
            LocalDateTime endTime = createTime.getEndTime();

            // 校验时间范围合法性
            if (startTime != null && endTime != null && startTime.isAfter(endTime)) {
                throw new BusinessException(400, "开始时间不能晚于结束时间");
            }

            // 根据时间范围构建查询条件
            if (startTime != null && endTime != null) {
                wrapper.between(SysTenant::getCreateTime, startTime, endTime);
            } else if (startTime != null) {
                wrapper.ge(SysTenant::getCreateTime, startTime);
            } else if (endTime != null) {
                wrapper.le(SysTenant::getCreateTime, endTime);
            }
        }

        // 超级管理员
        if (UserContext.isSuperAdmin()) {
            wrapper.eq(StringUtils.isNotBlank(queryParam.getTenantCode()),
                    SysTenant::getTenantCode, queryParam.getTenantCode());
        }

        // 条件分页查询
        IPage<SysTenant> tenantQueryPage = this.page(pageParam, wrapper);

        // 转换为VO分页对象并返回
        return sysTenantConverter.toVOPage(tenantQueryPage);

    }

    /**
     * <p>
     * 查询租户详情
     * </p>
     * <p>
     * 返回指定租户的详情信息，租户敏感会自动进行脱敏处理（保留前3位和后3位）。
     * 需要登录并具备租户详情查询权限才能访问。
     * </p>
     *
     * @param tenantCode 租户编码
     * @return 租户详情信息，包含租户名称、租户编码、联系人、状态等信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-04-19
     */
    @Override
    public SysTenantDetailVO queryTenantDetail(String tenantCode) {

        // 参数校验 租户编码不能为空
        if (StringUtils.isBlank(tenantCode)) {
            throw new BusinessException(400, "租户编码不能为空");
        }

        // 构建查询条件：根据租户编码查询未删除的租户
        LambdaQueryWrapper<SysTenant> wrapper = new LambdaQueryWrapper<SysTenant>()
                .eq(SysTenant::getTenantCode, tenantCode)
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());

        // 执行查询获取租户详情
        SysTenant tenantDetail = this.getOne(wrapper);

        // 租户不存在时抛出异常
        if (tenantDetail == null) {
            throw new BusinessException(404, "租户不存在");
        }

        // 转换为详情VO对象并返回
        return sysTenantConverter.toDetailVO(tenantDetail);

    }

    /**
     * <p>
     * 新增租户
     * </p>
     * <p>
     * 新增租户信息，需要登录并具备租户新增权限才能访问。
     * </p>
     *
     * @param addParam 新增租户信息
     * @return 新增租户的ID
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或新增失败时抛出
     * @author shy
     * @since 2026-04-19
     */
    @Override
    public Integer addTenant(SysTenantAddRTO addParam) {

        if (!UserContext.isSuperAdmin()) {
            Set<String> unOperableColumnSet = UserContext.getUnOperableColumns(GlobalConstant.Table.TENANT, GlobalConstant.OperableType.CREATE_TYPE);

            if (!unOperableColumnSet.isEmpty()) {
                try {
                    for (String fieldName : unOperableColumnSet) {
                        Field field = SysTenantAddRTO.class.getDeclaredField(fieldName);
                        field.setAccessible(true);
                        field.set(addParam, null);
                    }
                } catch (NoSuchFieldException e) {
                    throw new BusinessException(500, "不可操作字段不存在: " + e.getMessage());
                } catch (IllegalAccessException e) {
                    throw new BusinessException(500, "参数错误");
                }
            }
        }

        LambdaQueryWrapper<SysTenant> wrapper = new LambdaQueryWrapper<SysTenant>()
                .eq(SysTenant::getTenantCode, addParam.getTenantCode());

        long count = this.count(wrapper);

        if (count > 0) {
            throw new BusinessException(400, "租户已存在");
        }

        SysTenant parentTenant = null;
        String ancestors;

        if (StringUtils.isNotBlank(addParam.getParentCode())) {
            LambdaQueryWrapper<SysTenant> parentWrapper = new LambdaQueryWrapper<SysTenant>()
                    .eq(SysTenant::getTenantCode, addParam.getParentCode())
                    .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
            parentTenant = this.getOne(parentWrapper);

            if (parentTenant == null) {
                throw new BusinessException(400, "父租户不存在");
            }

            if (GlobalEnum.TenantStatus.DISABLED.getCode().equals(parentTenant.getStatus())) {
                throw new BusinessException(400, "父租户已冻结，不能作为父级: " + parentTenant.getTenantName());
            }

            String parentAncestors = parentTenant.getAncestors() != null ? parentTenant.getAncestors() : "rootTenant";
            ancestors = parentAncestors + "/" + parentTenant.getTenantCode();
            addParam.setAncestors(ancestors);
        } else {
            ancestors = "rootTenant";
            addParam.setAncestors(ancestors);
        }

        SysTenant tenant = sysTenantConverter.toEntityAdd(addParam);

        if (parentTenant != null) {
            tenant.setParentId(String.valueOf(parentTenant.getId()));
        }

        if (UserContext.isSuperAdmin()) {
            tenant.setCreateById(StringUtils.isNotBlank(addParam.getCreateByCode()) ? addParam.getCreateByCode() : UserContext.getCurrentUserId());
            tenant.setCreateByCode(StringUtils.isNotBlank(addParam.getCreateByCode()) ? addParam.getCreateByCode() : UserContext.getCurrentUserId());
            tenant.setCreateByName(StringUtils.isNotBlank(addParam.getCreateByName()) ? addParam.getCreateByName() : UserContext.getCurrentUserName());
            tenant.setCreateTime(addParam.getCreateTime() != null ? addParam.getCreateTime() : LocalDateTime.now());

            tenant.setUpdateById(StringUtils.isNotBlank(addParam.getUpdateCode()) ? addParam.getUpdateCode() : UserContext.getCurrentUserId());
            tenant.setUpdateByCode(StringUtils.isNotBlank(addParam.getUpdateCode()) ? addParam.getUpdateCode() : UserContext.getCurrentUserId());
            tenant.setUpdateByName(StringUtils.isNotBlank(addParam.getUpdateByName()) ? addParam.getUpdateByName() : UserContext.getCurrentUserName());
            tenant.setUpdateTime(addParam.getUpdateTime() != null ? addParam.getUpdateTime() : LocalDateTime.now());
        } else {
            tenant.setCreateById(UserContext.getCurrentUserId());
            tenant.setCreateByCode(UserContext.getCurrentUserId());
            tenant.setCreateByName(UserContext.getCurrentUserName());
            tenant.setCreateTime(LocalDateTime.now());

            tenant.setUpdateById(UserContext.getCurrentUserId());
            tenant.setUpdateByCode(UserContext.getCurrentUserId());
            tenant.setUpdateByName(UserContext.getCurrentUserName());
            tenant.setUpdateTime(LocalDateTime.now());
        }

        boolean result = this.save(tenant);

        if (!result) {
            throw new BusinessException(500, "新增租户失败");
        }

        return 1;

    }

    /**
     * <p>
     * 修改租户
     * </p>
     * <p>
     * 修改租户信息，需要登录并具备租户修改权限才能访问。
     * 仅允许修改指定租户的有效配置信息，不允许修改租户唯一标识。
     * </p>
     *
     * @param updateParam 修改租户信息
     * @return 修改结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、租户不存在或修改失败时抛出
     * @author shy
     * @since 2026-04-20
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateTenant(SysTenantUpdateRTO updateParam) {

        if (StringUtils.isBlank(updateParam.getId())) {
            throw new BusinessException(400, "租户ID不能为空");
        }

        LambdaQueryWrapper<SysTenant> wrapper = new LambdaQueryWrapper<SysTenant>()
                .eq(SysTenant::getId, Long.parseLong(updateParam.getId()))
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysTenant existTenant = this.getOne(wrapper);

        if (existTenant == null) {
            throw new BusinessException(404, "租户不存在");
        }

        if (!existTenant.getTenantCode().equals(updateParam.getTenantCode())) {
            LambdaQueryWrapper<SysTenant> codeWrapper = new LambdaQueryWrapper<SysTenant>()
                    .eq(SysTenant::getTenantCode, updateParam.getTenantCode())
                    .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
            long count = this.count(codeWrapper);
            if (count > 0) {
                throw new BusinessException(400, "租户编码已被其他租户使用");
            }
        }

        SysTenant newParentTenant = null;
        if (!existTenant.getParentCode().equals(updateParam.getParentCode())) {

            if (updateParam.getParentCode().equals(updateParam.getTenantCode())) {
                throw new BusinessException(400, "不能将自己设为父租户");
            }

            LambdaQueryWrapper<SysTenant> newParentWrapper = new LambdaQueryWrapper<SysTenant>()
                    .eq(SysTenant::getTenantCode, updateParam.getParentCode())
                    .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
            newParentTenant = this.getOne(newParentWrapper);

            if (newParentTenant == null) {
                throw new BusinessException(400, "父租户不存在");
            }

            if (GlobalEnum.TenantStatus.DISABLED.getCode().equals(newParentTenant.getStatus())) {
                throw new BusinessException(400, "父租户已冻结，不能作为父级: " + newParentTenant.getTenantName());
            }

            if (newParentTenant.getAncestors() != null
                    && (newParentTenant.getAncestors().contains("/" + existTenant.getTenantCode() + "/")
                    || newParentTenant.getAncestors().endsWith("/" + existTenant.getTenantCode()))) {
                throw new BusinessException(400, "不能将子租户设为父租户，将形成循环层级");
            }

            String parentAncestors = newParentTenant.getAncestors() != null ? newParentTenant.getAncestors() : "rootTenant";
            updateParam.setAncestors(parentAncestors + "/" + newParentTenant.getTenantCode());
        }

        SysTenant tenant = sysTenantConverter.toEntityUpdate(updateParam);

        if (newParentTenant != null) {
            tenant.setParentId(String.valueOf(newParentTenant.getId()));
            tenant.setParentCode(newParentTenant.getTenantCode());
        }

        if (UserContext.isSuperAdmin()) {
            tenant.setCreateById(StringUtils.isNotBlank(updateParam.getCreateByCode()) ? updateParam.getCreateByCode() : existTenant.getCreateById());
            tenant.setCreateByCode(StringUtils.isNotBlank(updateParam.getCreateByCode()) ? updateParam.getCreateByCode() : existTenant.getCreateByCode());
            tenant.setCreateByName(StringUtils.isNotBlank(updateParam.getCreateByName()) ? updateParam.getCreateByName() : existTenant.getCreateByName());
            tenant.setCreateTime(updateParam.getCreateTime() != null ? updateParam.getCreateTime() : existTenant.getCreateTime());

            tenant.setUpdateById(StringUtils.isNotBlank(updateParam.getUpdateByCode()) ? updateParam.getUpdateByCode() : UserContext.getCurrentUserId());
            tenant.setUpdateByCode(StringUtils.isNotBlank(updateParam.getUpdateByCode()) ? updateParam.getUpdateByCode() : UserContext.getCurrentUserId());
            tenant.setUpdateByName(StringUtils.isNotBlank(updateParam.getUpdateByName()) ? updateParam.getUpdateByName() : UserContext.getCurrentUserName());
            tenant.setUpdateTime(updateParam.getUpdateTime() != null ? updateParam.getUpdateTime() : LocalDateTime.now());
        } else {
            tenant.setCreateById(existTenant.getCreateById());
            tenant.setCreateByCode(existTenant.getCreateByCode());
            tenant.setCreateByName(existTenant.getCreateByName());
            tenant.setCreateTime(existTenant.getCreateTime());
            tenant.setUpdateById(UserContext.getCurrentUserId());
            tenant.setUpdateByCode(UserContext.getCurrentUserId());
            tenant.setUpdateByName(UserContext.getCurrentUserName());
            tenant.setUpdateTime(LocalDateTime.now());
        }

        boolean result = this.updateById(tenant);

        if (!result) {
            throw new BusinessException(500, "修改租户失败");
        }

        return 1;
    }

    /**
     * <p>
     * 更新租户状态
     * </p>
     * <p>
     * 更新指定租户的状态（正常/冻结），冻结后租户下所有用户无法登录。
     * 需要登录并具备租户修改权限才能访问。
     * </p>
     *
     * @param id 租户ID
     * @param status 租户状态（正常/冻结）
     * @return 更新结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、租户不存在或更新失败时抛出
     * @author shy
     * @since 2026-05-05
     */
    @Override
    public Integer updateTenantStatus(String id, String status) {

        // 参数校验：租户ID不能为空
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(400, "租户ID不能为空");
        }

        // 参数校验：状态不能为空
        if (StringUtils.isBlank(status)) {
            throw new BusinessException(400, "状态不能为空");
        }

        // 校验状态值是否合法
        GlobalEnum.TenantStatus tenantStatus = GlobalEnum.TenantStatus.parse(status);
        if (tenantStatus == null) {
            throw new BusinessException(400, "状态值不合法，仅支持：正常、冻结");
        }

        // 查询待更新状态的租户是否存在
        LambdaQueryWrapper<SysTenant> wrapper = new LambdaQueryWrapper<SysTenant>()
                .eq(SysTenant::getId, id)
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysTenant existTenant = this.getOne(wrapper);

        if (existTenant == null) {
            throw new BusinessException(400, "租户不存在");
        }

        // 如果状态未变更则直接返回
        if (tenantStatus.getCode().equals(existTenant.getStatus())) {
            throw new BusinessException(400, "租户状态未变更");
        }

        // 冻结租户时，校验该租户下是否存在正常状态的子租户
        if (GlobalEnum.TenantStatus.DISABLED.equals(tenantStatus)) {
            LambdaQueryWrapper<SysTenant> childWrapper = new LambdaQueryWrapper<SysTenant>()
                    .eq(SysTenant::getParentId, existTenant.getId())
                    .eq(SysTenant::getStatus, GlobalEnum.TenantStatus.ENABLED.getCode())
                    .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
            long childCount = this.count(childWrapper);
            if (childCount > 0) {
                throw new BusinessException(400, "该租户下存在正常状态的子租户，请先冻结子租户");
            }
        }

        SysTenant updateTenant = new SysTenant();
        updateTenant.setId(Long.parseLong(id));
        updateTenant.setStatus(tenantStatus.getCode());
        updateTenant.setUpdateById(UserContext.getCurrentUserId());
        updateTenant.setUpdateByCode(UserContext.getCurrentUserId());
        updateTenant.setUpdateByName(UserContext.getCurrentUserName());
        updateTenant.setUpdateTime(LocalDateTime.now());

        boolean result = this.updateById(updateTenant);

        if (!result) {
            throw new BusinessException(500, "更新租户状态失败");
        }

        return 1;
    }

    /**
     * <p>
     * 删除租户
     * </p>
     * <p>
     * 删除指定租户信息，需要登录并具备租户删除权限才能访问。
     * 删除操作不可逆，删除后租户相关数据将同步清理。
     * </p>
     *
     * @param id 租户ID
     * @return 删除结果：true-成功，false-失败
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、租户不存在或删除失败时抛出
     * @author shy
     * @since 2026-04-20
     */
    @Override
    public Integer deleteTenant(String id) {

        // 查询待删除的租户是否存在
        LambdaQueryWrapper<SysTenant> wrapper =new LambdaQueryWrapper<SysTenant>()
                .eq(SysTenant::getId, id)
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysTenant existTenant = this.getOne(wrapper);
        if (existTenant == null) {
            throw new BusinessException(400, "租户不存在");
        }

        // 检查是否存在子租户，有子租户则不允许删除
        LambdaQueryWrapper<SysTenant> childWrapper = new LambdaQueryWrapper<SysTenant>()
                .eq(SysTenant::getParentId, id)
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        long childCount = this.count(childWrapper);
        if (childCount > 0) {
            throw new BusinessException(400, "该租户下存在子租户，请先解绑");
        }

        SysTenant tenant = new SysTenant();
        tenant.setId(Long.parseLong(id));
        tenant.setIsDeleted(GlobalEnum.Deleted.DELETED.getCode());
        tenant.setUpdateById(UserContext.getCurrentUserId());
        tenant.setUpdateByCode(UserContext.getCurrentUserId());
        tenant.setUpdateByName(UserContext.getCurrentUserName());
        tenant.setUpdateTime(LocalDateTime.now());

        boolean result = this.updateById(tenant);

        if (!result) {
            throw new BusinessException(500, "删除租户失败");
        }

        if (existTenant.getParentId() != null && !"rootTenant".equals(existTenant.getParentId())) {
            LambdaQueryWrapper<SysTenant> siblingWrapper = new LambdaQueryWrapper<SysTenant>()
                    .eq(SysTenant::getParentId, existTenant.getParentId())
                    .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                    .ne(SysTenant::getId, Long.parseLong(id));
            long siblingCount = this.count(siblingWrapper);
            if (siblingCount == 0) {
                SysTenant updateOldParent = new SysTenant();
                updateOldParent.setId(Long.parseLong(existTenant.getParentId()));
                updateOldParent.setHasChildren(false);
                updateOldParent.setUpdateById(UserContext.getCurrentUserId());
                updateOldParent.setUpdateByCode(UserContext.getCurrentUserId());
                updateOldParent.setUpdateByName(UserContext.getCurrentUserName());
                updateOldParent.setUpdateTime(LocalDateTime.now());
                this.updateById(updateOldParent);
            }
        }

        return 1;
    }

    /**
     * <p>
     * 批量新增租户
     * </p>
     * <p>
     * 批量新增多个租户信息，需要登录并具备租户新增权限才能访问。
     * 批量操作支持事务回滚，任一租户新增失败则全部失败。
     * </p>
     *
     * @param addParamList 批量新增租户信息集合
     * @return 成功新增的租户数量
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、参数校验失败或新增失败时抛出
     * @author shy
     * @since 2026-04-20
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchAddTenant(List<SysTenantAddRTO> addParamList) {

        // 校验参数集合非空
        if (addParamList == null || addParamList.isEmpty()) {
            throw new BusinessException(400, "批量新增租户列表不能为空");
        }

        // 校验批量新增数量限制
        if (addParamList.size() > 100) {
            throw new BusinessException(400, "单次批量新增数量不能超过100条");
        }

        // 校验批量数据中是否有重复的租户编码
        Set<String> codeSet = new HashSet<>();
        for (SysTenantAddRTO param : addParamList) {
            if (codeSet.contains(param.getTenantCode())) {
                throw new BusinessException(400, "批量新增中存在重复的租户编码: " + param.getTenantCode());
            }
            codeSet.add(param.getTenantCode());
        }

        // 校验租户编码是否已在数据库中存在
        LambdaQueryWrapper<SysTenant> wrapper = new LambdaQueryWrapper<SysTenant>()
                .in(SysTenant::getTenantCode, codeSet);
        long existCount = this.count(wrapper);

        if (existCount > 0) {
            throw new BusinessException(400, "部分租户编码已存在，请检查后重试");
        }

        Set<String> parentCodes = addParamList.stream()
                .map(SysTenantAddRTO::getParentCode)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toSet());

        Map<String, SysTenant> parentTenantMap = new HashMap<>();
        if (!parentCodes.isEmpty()) {
            LambdaQueryWrapper<SysTenant> parentWrapper = new LambdaQueryWrapper<SysTenant>()
                    .in(SysTenant::getTenantCode, parentCodes)
                    .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
            List<SysTenant> parentTenants = this.list(parentWrapper);

            for (SysTenant parent : parentTenants) {
                parentTenantMap.put(parent.getTenantCode(), parent);
            }

            for (String parentCode : parentCodes) {
                if (!parentTenantMap.containsKey(parentCode)) {
                    throw new BusinessException(400, "父租户不存在: " + parentCode);
                }
            }

            // 校验父租户状态（冻结的父租户不能作为父级）
            for (SysTenant parent : parentTenantMap.values()) {
                if (GlobalEnum.TenantStatus.DISABLED.getCode().equals(parent.getStatus())) {
                    throw new BusinessException(400, "父租户已冻结，不能作为父级: " + parent.getTenantName());
                }
            }
        }

        // 为每个租户填充 ancestors 和审计参数
        LocalDateTime now = LocalDateTime.now();
        String currentUserId = UserContext.getCurrentUserId();
        String currentUserName = UserContext.getCurrentUserName();
        boolean isSuperAdmin = UserContext.isSuperAdmin();

        List<SysTenant> entityList = new ArrayList<>(addParamList.size());

        for (SysTenantAddRTO param : addParamList) {
            String ancestors;

            if (StringUtils.isNotBlank(param.getParentCode())) {
                SysTenant parentTenant = parentTenantMap.get(param.getParentCode());

                String parentAncestors = parentTenant.getAncestors() != null ? parentTenant.getAncestors() : "rootTenant";
                if (parentAncestors.contains(param.getTenantCode())
                        || parentTenant.getTenantCode().equals(param.getTenantCode())) {
                    throw new BusinessException(400, "不能将租户作为自身的后代: " + param.getTenantCode());
                }

                ancestors = parentAncestors + "/" + parentTenant.getTenantCode();
                param.setAncestors(ancestors);
            } else {
                ancestors = "rootTenant";
                param.setAncestors(ancestors);
            }

            SysTenant tenant = sysTenantConverter.toEntityAdd(param);

            if (StringUtils.isNotBlank(param.getParentCode())) {
                SysTenant parentTenant = parentTenantMap.get(param.getParentCode());
                tenant.setParentId(String.valueOf(parentTenant.getId()));
            }

            if (isSuperAdmin) {
                tenant.setCreateById(StringUtils.isNotBlank(param.getCreateByCode()) ? param.getCreateByCode() : currentUserId);
                tenant.setCreateByCode(StringUtils.isNotBlank(param.getCreateByCode()) ? param.getCreateByCode() : currentUserId);
                tenant.setCreateByName(StringUtils.isNotBlank(param.getCreateByName()) ? param.getCreateByName() : currentUserName);
                tenant.setCreateTime(param.getCreateTime() != null ? param.getCreateTime() : now);

                tenant.setUpdateById(StringUtils.isNotBlank(param.getUpdateCode()) ? param.getUpdateCode() : currentUserId);
                tenant.setUpdateByCode(StringUtils.isNotBlank(param.getUpdateCode()) ? param.getUpdateCode() : currentUserId);
                tenant.setUpdateByName(StringUtils.isNotBlank(param.getUpdateByName()) ? param.getUpdateByName() : currentUserName);
                tenant.setUpdateTime(param.getUpdateTime() != null ? param.getUpdateTime() : now);
            } else {
                tenant.setCreateById(currentUserId);
                tenant.setCreateByCode(currentUserId);
                tenant.setCreateByName(currentUserName);
                tenant.setCreateTime(now);

                tenant.setUpdateById(currentUserId);
                tenant.setUpdateByCode(currentUserId);
                tenant.setUpdateByName(currentUserName);
                tenant.setUpdateTime(now);
            }

            entityList.add(tenant);
        }

        // 批量保存租户信息
        boolean result = this.saveBatch(entityList);

        if (!result) {
            throw new BusinessException(500, "批量新增租户失败");
        }

        if (!parentTenantMap.isEmpty()) {
            List<Long> parentIds = parentTenantMap.values().stream()
                    .map(SysTenant::getId)
                    .collect(Collectors.toList());
            LambdaQueryWrapper<SysTenant> parentUpdateWrapper = new LambdaQueryWrapper<SysTenant>()
                    .in(SysTenant::getId, parentIds)
                    .and(w -> w.eq(SysTenant::getHasChildren, false).or().isNull(SysTenant::getHasChildren));
            List<SysTenant> needUpdateParents = this.list(parentUpdateWrapper);
            if (!needUpdateParents.isEmpty()) {
                List<SysTenant> parentUpdateList = new ArrayList<>();
                for (SysTenant p : needUpdateParents) {
                    SysTenant updateP = new SysTenant();
                    updateP.setId(p.getId());
                    updateP.setHasChildren(true);
                    parentUpdateList.add(updateP);
                }
                this.updateBatchById(parentUpdateList);
            }
        }

        return addParamList.size();

    }

    /**
     * <p>
     * 批量修改租户
     * </p>
     * <p>
     * 批量修改多个租户信息，需要登录并具备租户修改权限才能访问。
     * 仅允许修改指定租户的有效配置信息，不允许修改租户唯一标识。
     * </p>
     *
     * @param updateParamList 批量修改租户信息集合
     * @return 成功修改的租户数量
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、租户不存在或修改失败时抛出
     * @author shy
     * @since 2026-04-20
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdateTenant(List<SysTenantUpdateRTO> updateParamList) {

        // 校验参数集合非空
        if (updateParamList == null || updateParamList.isEmpty()) {
            throw new BusinessException(400, "批量修改租户列表不能为空");
        }

        // 校验批量修改数量限制
        if (updateParamList.size() > 100) {
            throw new BusinessException(400, "单次批量修改数量不能超过100条");
        }

        // 校验批量数据的合法性：ID不能为空、不能有重复的ID和编码
        Set<Long> idSet = new HashSet<>();
        Set<String> codeSet = new HashSet<>();
        for (SysTenantUpdateRTO item : updateParamList) {
            if (StringUtils.isBlank(item.getId())) {
                throw new BusinessException(400, "批量修改中存在ID为空的记录");
            }
            Long idLong = Long.parseLong(item.getId());
            if (idSet.contains(idLong)) {
                throw new BusinessException(400, "批量修改中存在重复的租户ID: " + item.getId());
            }
            if (codeSet.contains(item.getTenantCode())) {
                throw new BusinessException(400, "批量修改中存在重复的租户编码: " + item.getTenantCode());
            }
            idSet.add(idLong);
            codeSet.add(item.getTenantCode());
        }

        LambdaQueryWrapper<SysTenant> existWrapper = new LambdaQueryWrapper<SysTenant>()
                .in(SysTenant::getId, idSet)
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        List<SysTenant> existTenants = this.list(existWrapper);

        if (existTenants.size() != idSet.size()) {
            Set<Long> existIdSet = existTenants.stream().map(SysTenant::getId).collect(Collectors.toSet());
            List<Long> missingIds = idSet.stream().filter(id -> !existIdSet.contains(id)).toList();
            throw new BusinessException(400, "部分租户ID不存在: " + missingIds);
        }

        // 构建现有租户映射：ID -> 实体
        Map<String, SysTenant> existTenantMap = existTenants.stream()
                .collect(Collectors.toMap(t -> String.valueOf(t.getId()), t -> t));

        // 校验租户编码唯一性（仅校验非自身的编码冲突）
        for (SysTenantUpdateRTO param : updateParamList) {
            SysTenant existTenant = existTenantMap.get(param.getId());
            if (!existTenant.getTenantCode().equals(param.getTenantCode())) {
                LambdaQueryWrapper<SysTenant> codeWrapper = new LambdaQueryWrapper<SysTenant>()
                        .eq(SysTenant::getTenantCode, param.getTenantCode())
                        .ne(SysTenant::getId, param.getId())
                        .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
                long count = this.count(codeWrapper);
                if (count > 0) {
                    throw new BusinessException(400, "租户编码已被其他租户使用: " + param.getTenantCode());
                }
            }
        }

        Set<String> newParentCodes = new HashSet<>();
        for (SysTenantUpdateRTO param : updateParamList) {
            SysTenant existTenant = existTenantMap.get(param.getId());
            if (!existTenant.getParentCode().equals(param.getParentCode())) {
                newParentCodes.add(param.getParentCode());
            }
        }

        Map<String, SysTenant> newParentTenantMap = new HashMap<>();
        if (!newParentCodes.isEmpty()) {
            LambdaQueryWrapper<SysTenant> parentWrapper = new LambdaQueryWrapper<SysTenant>()
                    .in(SysTenant::getTenantCode, newParentCodes)
                    .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
            List<SysTenant> newParentTenants = this.list(parentWrapper);

            for (SysTenant parent : newParentTenants) {
                newParentTenantMap.put(parent.getTenantCode(), parent);
            }

            for (String parentCode : newParentCodes) {
                if (!newParentTenantMap.containsKey(parentCode)) {
                    throw new BusinessException(400, "父租户不存在: " + parentCode);
                }
            }

            // 校验新父租户状态
            for (SysTenant parent : newParentTenantMap.values()) {
                if (GlobalEnum.TenantStatus.DISABLED.getCode().equals(parent.getStatus())) {
                    throw new BusinessException(400, "父租户已冻结，不能作为父级: " + parent.getTenantName());
                }
            }
        }

        // 逐条校验层级关系并填充数据
        boolean isSuperAdmin = UserContext.isSuperAdmin();
        LocalDateTime now = LocalDateTime.now();
        String currentUserId = UserContext.getCurrentUserId();
        String currentUserName = UserContext.getCurrentUserName();

        List<SysTenant> entityList = new ArrayList<>(updateParamList.size());

        for (SysTenantUpdateRTO param : updateParamList) {
            SysTenant existTenant = existTenantMap.get(param.getId());

            SysTenant newParentTenant = null;
            if (!existTenant.getParentCode().equals(param.getParentCode())) {

                if (param.getParentCode().equals(param.getTenantCode())) {
                    throw new BusinessException(400, "不能将自己设为父租户: " + param.getTenantCode());
                }

                newParentTenant = newParentTenantMap.get(param.getParentCode());

                if (newParentTenant.getAncestors() != null
                        && (newParentTenant.getAncestors().contains("/" + existTenant.getTenantCode() + "/")
                        || newParentTenant.getAncestors().endsWith("/" + existTenant.getTenantCode()))) {
                    throw new BusinessException(400, "不能将子租户设为父租户，将形成循环层级: " + param.getTenantCode());
                }

                String parentAncestors = newParentTenant.getAncestors() != null ? newParentTenant.getAncestors() : "rootTenant";
                param.setAncestors(parentAncestors + "/" + newParentTenant.getTenantCode());
            }

            SysTenant tenant = sysTenantConverter.toEntityUpdate(param);

            if (newParentTenant != null) {
                tenant.setParentId(String.valueOf(newParentTenant.getId()));
                tenant.setParentCode(newParentTenant.getTenantCode());
            }

            if (isSuperAdmin) {
                tenant.setCreateById(StringUtils.isNotBlank(param.getCreateByCode()) ? param.getCreateByCode() : existTenant.getCreateById());
                tenant.setCreateByCode(StringUtils.isNotBlank(param.getCreateByCode()) ? param.getCreateByCode() : existTenant.getCreateByCode());
                tenant.setCreateByName(StringUtils.isNotBlank(param.getCreateByName()) ? param.getCreateByName() : existTenant.getCreateByName());
                tenant.setCreateTime(param.getCreateTime() != null ? param.getCreateTime() : existTenant.getCreateTime());
                tenant.setUpdateById(StringUtils.isNotBlank(param.getUpdateByCode()) ? param.getUpdateByCode() : currentUserId);
                tenant.setUpdateByCode(StringUtils.isNotBlank(param.getUpdateByCode()) ? param.getUpdateByCode() : currentUserId);
                tenant.setUpdateByName(StringUtils.isNotBlank(param.getUpdateByName()) ? param.getUpdateByName() : currentUserName);
                tenant.setUpdateTime(param.getUpdateTime() != null ? param.getUpdateTime() : now);
            } else {
                tenant.setCreateById(existTenant.getCreateById());
                tenant.setCreateByCode(existTenant.getCreateByCode());
                tenant.setCreateByName(existTenant.getCreateByName());
                tenant.setCreateTime(existTenant.getCreateTime());
                tenant.setUpdateById(currentUserId);
                tenant.setUpdateByCode(currentUserId);
                tenant.setUpdateByName(currentUserName);
                tenant.setUpdateTime(now);
            }

            entityList.add(tenant);
        }

        // 批量更新租户信息
        boolean result = this.updateBatchById(entityList);

        if (!result) {
            throw new BusinessException(500, "批量修改租户失败");
        }

        return updateParamList.size();

    }

    /**
     * <p>
     * 批量更新租户状态
     * </p>
     * <p>
     * 批量更新多个指定租户的状态（正常/冻结），冻结后租户下所有用户无法登录。
     * 批量操作支持事务回滚，任一租户更新失败则全部失败。
     * 需要登录并具备租户修改权限才能访问。
     * </p>
     *
     * @param ids 租户ID集合
     * @param status 租户状态（正常/冻结）
     * @return 更新结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、租户不存在或更新失败时抛出
     * @author shy
     * @since 2026-05-05
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdateTenantStatus(List<String> ids, String status) {

        // 校验参数集合非空
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException(400, "租户ID列表不能为空");
        }

        // 校验批量更新数量限制
        if (ids.size() > 100) {
            throw new BusinessException(400, "单次批量更新数量不能超过100条");
        }

        // 参数校验：状态不能为空
        if (StringUtils.isBlank(status)) {
            throw new BusinessException(400, "状态不能为空");
        }

        // 校验状态值是否合法
        GlobalEnum.TenantStatus tenantStatus = GlobalEnum.TenantStatus.parse(status);
        if (tenantStatus == null) {
            throw new BusinessException(400, "状态值不合法");
        }

        // 校验ID格式
        Set<String> idSet = new HashSet<>();
        for (String id : ids) {
            if (StringUtils.isBlank(id)) {
                throw new BusinessException(400, "租户ID不能为空");
            }
            idSet.add(id);
        }

        // 查询待更新状态的租户是否存在且未被删除
        LambdaQueryWrapper<SysTenant> existWrapper = new LambdaQueryWrapper<SysTenant>()
                .in(SysTenant::getId, idSet)
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        List<SysTenant> existTenants = this.list(existWrapper);

        if (existTenants.isEmpty()) {
            throw new BusinessException(404, "未找到可更新状态的租户");
        }

        // 冻结租户时，校验这些租户下是否存在正常状态的子租户
        if (GlobalEnum.TenantStatus.DISABLED.equals(tenantStatus)) {
            LambdaQueryWrapper<SysTenant> childWrapper = new LambdaQueryWrapper<SysTenant>()
                    .in(SysTenant::getParentId, idSet)
                    .eq(SysTenant::getStatus, GlobalEnum.TenantStatus.ENABLED.getCode())
                    .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
            long childCount = this.count(childWrapper);
            if (childCount > 0) {
                throw new BusinessException(400, "部分租户下存在正常状态的子租户，请先冻结子租户");
            }
        }

        // 构建批量状态更新的数据列表
        LocalDateTime now = LocalDateTime.now();
        String currentUserId = UserContext.getCurrentUserId();
        String currentUserName = UserContext.getCurrentUserName();
        List<SysTenant> updateList = new ArrayList<>();
        for (SysTenant tenant : existTenants) {
            // 跳过状态未变更的租户
            if (tenantStatus.getCode().equals(tenant.getStatus())) {
                continue;
            }
            SysTenant updateTenant = new SysTenant();
            updateTenant.setId(tenant.getId());
            updateTenant.setStatus(tenantStatus.getCode());
            updateTenant.setUpdateById(currentUserId);
            updateTenant.setUpdateByCode(currentUserId);
            updateTenant.setUpdateByName(currentUserName);
            updateTenant.setUpdateTime(now);
            updateList.add(updateTenant);
        }

        if (updateList.isEmpty()) {
            throw new BusinessException(400, "所有租户状态均未变更");
        }

        // 执行批量状态更新
        boolean batch = this.updateBatchById(updateList);

        if (!batch) {
            throw new BusinessException(500, "批量更新租户状态失败");
        }

        return updateList.size();
    }

    /**
     * <p>
     * 批量删除租户
     * </p>
     * <p>
     * 批量删除多个指定租户信息，需要登录并具备租户删除权限才能访问。
     * 删除操作不可逆，删除后租户相关数据将同步清理。
     * </p>
     *
     * @param ids 租户ID集合
     * @return 删除结果：true-全部成功，false-部分/全部失败
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、租户不存在或删除失败时抛出
     * @author shy
     * @since 2026-04-20
     */
    @Override
    public Integer batchDeleteTenant(List<String> ids) {

        // 校验参数集合非空
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException(400, "租户ID列表不能为空");
        }

        // 校验批量删除数量限制
        if (ids.size() > 100) {
            throw new BusinessException(400, "单次批量删除数量不能超过100条");
        }

        // 校验ID格式
        Set<String> idSet = new HashSet<>();
        for (String id : ids) {
            if (StringUtils.isBlank(id)) {
                throw new BusinessException(400, "租户ID不能为空");
            }
            idSet.add(id);
        }

        // 查询待删除的租户是否存在且未被删除
        LambdaQueryWrapper<SysTenant> existWrapper = new LambdaQueryWrapper<SysTenant>()
                .in(SysTenant::getId, idSet)
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        List<SysTenant> existTenants = this.list(existWrapper);

        if (existTenants.isEmpty()) {
            throw new BusinessException(404, "未找到可删除的租户");
        }

        // 检查这些租户下是否存在子租户
        LambdaQueryWrapper<SysTenant> wrapper = new LambdaQueryWrapper<SysTenant>()
                .in(SysTenant::getParentId, idSet)
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        long childCount = this.count(wrapper);
        if (childCount > 0) {
            throw new BusinessException(400, "部分租户下存在子租户，请先解绑");
        }

        // 构建批量逻辑删除的数据列表
        LocalDateTime now = LocalDateTime.now();
        String currentUserId = UserContext.getCurrentUserId();
        String currentUserName = UserContext.getCurrentUserName();
        List<SysTenant> tenantList = new ArrayList<>();
        Set<String> parentIdsToCheck = new HashSet<>();
        for (SysTenant tenant : existTenants) {
            SysTenant deleteTenant = new SysTenant();
            deleteTenant.setId(tenant.getId());
            deleteTenant.setIsDeleted(GlobalEnum.Deleted.DELETED.getCode());
            deleteTenant.setUpdateById(currentUserId);
            deleteTenant.setUpdateByCode(currentUserId);
            deleteTenant.setUpdateByName(currentUserName);
            deleteTenant.setUpdateTime(now);
            tenantList.add(deleteTenant);
            // 收集需要检查 hasChildren 的父租户ID
            if (tenant.getParentId() != null && !"rootTenant".equals(tenant.getParentId())) {
                parentIdsToCheck.add(tenant.getParentId());
            }
        }

        // 执行批量逻辑删除
        boolean batch = this.updateBatchById(tenantList);

        if (!batch) {
            throw new BusinessException(500, "批量删除租户失败");
        }

        // 检查原父租户是否还有其他子租户，更新 hasChildren 标记
        for (String parentId : parentIdsToCheck) {
            LambdaQueryWrapper<SysTenant> siblingWrapper = new LambdaQueryWrapper<SysTenant>()
                    .eq(SysTenant::getParentId, parentId)
                    .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
            long siblingCount = this.count(siblingWrapper);
            if (siblingCount == 0) {
                SysTenant updateOldParent = new SysTenant();
                updateOldParent.setId(Long.parseLong(parentId));
                updateOldParent.setHasChildren(false);
                updateOldParent.setUpdateById(currentUserId);
                updateOldParent.setUpdateByCode(currentUserId);
                updateOldParent.setUpdateByName(currentUserName);
                updateOldParent.setUpdateTime(now);
                this.updateById(updateOldParent);
            }
        }

        return ids.size();

    }

    /**
     * <p>
     * 分配子租户
     * </p>
     * <p>
     * 为指定父租户分配子租户，自动处理层级关系和ancestors字段更新。
     * 操作逻辑：将 subCode 中的租户设置为 parentCode 中对应父租户的子租户。
     * 当 parentCode 和 subCode 数量不一致时，每个子租户将分配给所有父租户（多对多）。
     * 需要登录并具备租户分配权限才能访问。
     * </p>
     *
     * @param assignParam 子租户分配参数
     * @return 更新子租户行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、父租户不存在或分配失败时抛出
     * @author shy
     * @since 2026-05-04
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer assignSubTenant(SysTenantAssignRTO assignParam) {

        validateTenantCode(assignParam.getParentCode(), "父租户编码");
        validateTenantCode(assignParam.getSubCode(), "子租户编码");

        if (assignParam.getSubCode().contains(assignParam.getParentCode())) {
            throw new BusinessException(400, "父租户编码和子租户编码不能相同: " + assignParam.getParentCode());
        }

        LambdaQueryWrapper<SysTenant> parentWrapper = new LambdaQueryWrapper<SysTenant>()
                .eq(SysTenant::getTenantCode, assignParam.getParentCode())
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysTenant parentTenant = this.getOne(parentWrapper);

        if (parentTenant == null) {
            throw new BusinessException(400, "父租户不存在: " + assignParam.getParentCode());
        }

        // 校验父租户状态（冻结的父租户不能作为父级）
        if (GlobalEnum.TenantStatus.DISABLED.getCode().equals(parentTenant.getStatus())) {
            throw new BusinessException(400, "父租户已冻结，不能作为父级: " + parentTenant.getTenantName());
        }

        // 查询所有子租户
        LambdaQueryWrapper<SysTenant> subWrapper = new LambdaQueryWrapper<SysTenant>()
                .in(SysTenant::getTenantCode, assignParam.getSubCode())
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        List<SysTenant> subTenants = this.list(subWrapper);

        // 校验子租户是否全部存在
        if (subTenants.size() != assignParam.getSubCode().size()) {
            Set<String> foundCodes = subTenants.stream()
                    .map(SysTenant::getTenantCode)
                    .collect(Collectors.toSet());
            List<String> missingCodes = assignParam.getSubCode().stream()
                    .filter(code -> !foundCodes.contains(code))
                    .toList();
            throw new BusinessException(400, "子租户不存在: " + missingCodes);
        }

        // 校验子租户不能是父租户的祖先（避免循环层级）
        for (SysTenant sub : subTenants) {
            // 子租户不能是父租户自身
            if (parentTenant.getId().equals(sub.getId())) {
                throw new BusinessException(400, "不能将租户分配为自身的子租户: " + sub.getTenantCode());
            }
            // 利用 ancestors 检测循环：如果父租户的 ancestors 包含子租户的编码，则会产生循环
            if (parentTenant.getAncestors() != null
                    && (parentTenant.getAncestors().contains("/" + sub.getTenantCode() + "/")
                    || parentTenant.getAncestors().endsWith("/" + sub.getTenantCode()))) {
                throw new BusinessException(400, "分配子租户会形成循环层级，子租户 " + sub.getTenantCode()
                        + " 是父租户 " + parentTenant.getTenantCode() + " 的祖先");
            }
        }

        // 执行分配：更新每个子租户的父租户信息
        int totalAffected = 0;
        LocalDateTime now = LocalDateTime.now();
        String currentUserId = UserContext.getCurrentUserId();
        String currentUserName = UserContext.getCurrentUserName();
        for (SysTenant subTenant : subTenants) {
            // 记录旧的 ancestors 路径用于级联更新
            String oldAncestors = subTenant.getAncestors() != null ? subTenant.getAncestors() : "root";
            String oldPath = oldAncestors + "/" + subTenant.getTenantCode();

            // 构建新的 ancestors 路径
            String parentAncestors = parentTenant.getAncestors() != null
                    ? parentTenant.getAncestors() : "root";
            String newAncestors = parentAncestors + "/" + parentTenant.getTenantCode();
            String newPath = newAncestors + "/" + subTenant.getTenantCode();

            // 更新子租户信息
            SysTenant updateTenant = new SysTenant();
            updateTenant.setId(subTenant.getId());
            updateTenant.setParentId(String.valueOf(parentTenant.getId()));
            updateTenant.setParentCode(parentTenant.getTenantCode());
            updateTenant.setParentName(parentTenant.getTenantName());
            updateTenant.setAncestors(newAncestors);
            updateTenant.setUpdateById(currentUserId);
            updateTenant.setUpdateByCode(currentUserId);
            updateTenant.setUpdateByName(currentUserName);
            updateTenant.setUpdateTime(now);

            boolean result = this.updateById(updateTenant);
            if (!result) {
                throw new BusinessException(500, "分配子租户失败: " + subTenant.getTenantCode());
            }

            totalAffected++;

            // 更新父租户的 hasChildren 标记
            if (!Boolean.TRUE.equals(parentTenant.getHasChildren())) {
                SysTenant updateParent = new SysTenant();
                updateParent.setId(parentTenant.getId());
                updateParent.setHasChildren(true);
                updateParent.setUpdateById(currentUserId);
                updateParent.setUpdateByCode(currentUserId);
                updateParent.setUpdateByName(currentUserName);
                updateParent.setUpdateTime(now);
                this.updateById(updateParent);
            }

            // 级联更新后代节点的 ancestors（利用 ancestors 优化定位后代）
            if (Boolean.TRUE.equals(subTenant.getHasChildren())) {
                // 查找后代并批量更新
                List<SysTenant> descendants = findDescendantsByAncestors(
                        subTenant.getTenantCode(), oldAncestors);

                if (!descendants.isEmpty()) {
                    List<SysTenant> updateList = new ArrayList<>();
                    for (SysTenant descendant : descendants) {
                        SysTenant update = new SysTenant();
                        update.setId(descendant.getId());
                        update.setAncestors(descendant.getAncestors().replace(oldPath, newPath));
                        update.setUpdateById(currentUserId);
                        update.setUpdateByCode(currentUserId);
                        update.setUpdateByName(currentUserName);
                        update.setUpdateTime(now);
                        updateList.add(update);
                    }
                    this.updateBatchById(updateList);
                    totalAffected += updateList.size();
                }
            }

            // 如果子租户原来有父租户，检查原父租户是否还有其他子租户
            if (subTenant.getParentId() != null && !"rootTenant".equals(subTenant.getParentId())) {
                LambdaQueryWrapper<SysTenant> siblingWrapper = new LambdaQueryWrapper<SysTenant>()
                        .eq(SysTenant::getParentId, subTenant.getParentId())
                        .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                        .ne(SysTenant::getId, subTenant.getId());
                long siblingCount = this.count(siblingWrapper);
                if (siblingCount == 0) {
                    SysTenant updateOldParent = new SysTenant();
                    updateOldParent.setId(Long.parseLong(subTenant.getParentId()));
                    updateOldParent.setHasChildren(false);
                    updateOldParent.setUpdateById(currentUserId);
                    updateOldParent.setUpdateByCode(currentUserId);
                    updateOldParent.setUpdateByName(currentUserName);
                    updateOldParent.setUpdateTime(now);
                    this.updateById(updateOldParent);
                }
            }
        }

        return totalAffected;

    }

    /**
     * <p>
     * 分配父租户
     * </p>
     * <p>
     * 为指定租户分配一个新的父租户，处理层级关系调整及数据关联更新。
     * 会进行循环层级验证，避免形成环状结构。
     * 操作逻辑：将 subCode 中的租户重新挂载到 parentCode 中对应的父租户下。
     * 需要登录并具备租户分配权限才能访问。
     * </p>
     *
     * @param assignParam 父租户分配参数
     * @return 更新租户行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、参数非法或分配失败时抛出
     * @author shy
     * @since 2026-05-04
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer assignParentTenant(SysTenantAssignRTO assignParam) {

        validateTenantCode(assignParam.getParentCode(), "父租户编码");
        validateTenantCode(assignParam.getSubCode(), "子租户编码");

        if (assignParam.getSubCode().contains(assignParam.getParentCode())) {
            throw new BusinessException(400, "父租户编码和子租户编码不能相同: " + assignParam.getParentCode());
        }

        LambdaQueryWrapper<SysTenant> parentWrapper = new LambdaQueryWrapper<SysTenant>()
                .eq(SysTenant::getTenantCode, assignParam.getParentCode())
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysTenant newParent = this.getOne(parentWrapper);

        if (newParent == null) {
            throw new BusinessException(400, "父租户不存在: " + assignParam.getParentCode());
        }

        // 查询所有待分配租户
        LambdaQueryWrapper<SysTenant> subWrapper = new LambdaQueryWrapper<SysTenant>()
                .in(SysTenant::getTenantCode, assignParam.getSubCode())
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        List<SysTenant> subTenants = this.list(subWrapper);

        // 校验待分配租户是否全部存在
        if (subTenants.size() != assignParam.getSubCode().size()) {
            Set<String> foundCodes = subTenants.stream()
                    .map(SysTenant::getTenantCode)
                    .collect(Collectors.toSet());
            List<String> missingCodes = assignParam.getSubCode().stream()
                    .filter(code -> !foundCodes.contains(code))
                    .toList();
            throw new BusinessException(400, "待分配租户不存在: " + missingCodes);
        }

        int totalAffected = 0;
        LocalDateTime now = LocalDateTime.now();
        String currentUserId = UserContext.getCurrentUserId();
        String currentUserName = UserContext.getCurrentUserName();

        for (SysTenant subTenant : subTenants) {
            // 不能将自己设为父租户
            if (newParent.getId().equals(subTenant.getId())) {
                throw new BusinessException(400, "不能将租户设为自身的父租户: " + subTenant.getTenantCode());
            }

            // 循环层级验证：新父租户的 ancestors 不能包含当前租户的编码
            // 否则当前租户会成为新父租户的祖先，形成环
            if (newParent.getAncestors() != null
                    && (newParent.getAncestors().contains("/" + subTenant.getTenantCode() + "/")
                    || newParent.getAncestors().endsWith("/" + subTenant.getTenantCode()))) {
                throw new BusinessException(400, "分配父租户会形成循环层级，租户 "
                        + subTenant.getTenantCode() + " 是新父租户 " + newParent.getTenantCode()
                        + " 的祖先");
            }

            // 校验新父租户状态（冻结的租户不能作为父租户）
            if (GlobalEnum.TenantStatus.DISABLED.getCode().equals(newParent.getStatus())) {
                throw new BusinessException(400, "新父租户已冻结，不能作为父租户: " + newParent.getTenantCode());
            }

            // 记录旧的 ancestors 路径
            String oldAncestors = subTenant.getAncestors() != null ? subTenant.getAncestors() : "root";
            String oldPath = oldAncestors + "/" + subTenant.getTenantCode();

            // 构建新的 ancestors 路径
            String parentAncestors = newParent.getAncestors() != null
                    ? newParent.getAncestors() : "root";
            String newAncestors = parentAncestors + "/" + newParent.getTenantCode();
            String newPath = newAncestors + "/" + subTenant.getTenantCode();

            // 更新当前租户的父租户信息
            SysTenant updateTenant = new SysTenant();
            updateTenant.setId(subTenant.getId());
            updateTenant.setParentId(String.valueOf(newParent.getId()));
            updateTenant.setParentCode(newParent.getTenantCode());
            updateTenant.setParentName(newParent.getTenantName());
            updateTenant.setAncestors(newAncestors);
            updateTenant.setUpdateById(currentUserId);
            updateTenant.setUpdateByCode(currentUserId);
            updateTenant.setUpdateByName(currentUserName);
            updateTenant.setUpdateTime(now);

            boolean result = this.updateById(updateTenant);
            if (!result) {
                throw new BusinessException(500, "分配父租户失败: " + subTenant.getTenantCode());
            }
            totalAffected++;

            // 更新新父租户的 hasChildren 标记
            if (!Boolean.TRUE.equals(newParent.getHasChildren())) {
                SysTenant updateNewParent = new SysTenant();
                updateNewParent.setId(newParent.getId());
                updateNewParent.setHasChildren(true);
                updateNewParent.setUpdateById(currentUserId);
                updateNewParent.setUpdateByCode(currentUserId);
                updateNewParent.setUpdateByName(currentUserName);
                updateNewParent.setUpdateTime(now);
                this.updateById(updateNewParent);
            }

            if (Boolean.TRUE.equals(subTenant.getHasChildren())) {
                List<SysTenant> descendants = findDescendantsByAncestors(
                        subTenant.getTenantCode(), oldAncestors);

                if (!descendants.isEmpty()) {
                    List<SysTenant> updateList = new ArrayList<>();
                    for (SysTenant descendant : descendants) {
                        SysTenant update = new SysTenant();
                        update.setId(descendant.getId());
                        update.setAncestors(descendant.getAncestors().replace(oldPath, newPath));
                        update.setUpdateById(currentUserId);
                        update.setUpdateByCode(currentUserId);
                        update.setUpdateByName(currentUserName);
                        update.setUpdateTime(now);
                        updateList.add(update);
                    }
                    this.updateBatchById(updateList);
                    totalAffected += updateList.size();
                }
            }

            // 检查原父租户是否还有其他子租户，更新 hasChildren 标记
            if (subTenant.getParentId() != null && !"rootTenant".equals(subTenant.getParentId())) {
                LambdaQueryWrapper<SysTenant> siblingWrapper = new LambdaQueryWrapper<SysTenant>()
                        .eq(SysTenant::getParentId, subTenant.getParentId())
                        .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                        .ne(SysTenant::getId, subTenant.getId());
                long siblingCount = this.count(siblingWrapper);
                if (siblingCount == 0) {
                    SysTenant updateOldParent = new SysTenant();
                    updateOldParent.setId(Long.parseLong(subTenant.getParentId()));
                    updateOldParent.setHasChildren(false);
                    updateOldParent.setUpdateById(currentUserId);
                    updateOldParent.setUpdateByCode(currentUserId);
                    updateOldParent.setUpdateByName(currentUserName);
                    updateOldParent.setUpdateTime(now);
                    this.updateById(updateOldParent);
                }
            }
        }

        return totalAffected;

    }

    /**
     * <p>
     * 将租户列表构建为树形结构
     * </p>
     * <p>
     * 基于 parentId 字段进行父子关系匹配，将平铺列表组装为嵌套树结构。
     * 根节点的 parentId 为 0 或 null。
     * </p>
     *
     * @param treeVOList 租户树形视图对象平铺列表
     * @return 树形结构列表，仅包含根节点（子节点嵌套在 chileTenant 中）
     * @author shy
     * @since 2026-05-04
     */
    private List<SysTenantTreeVO> buildTree(List<SysTenantTreeVO> treeVOList) {

        Map<String, List<SysTenantTreeVO>> parentMap = treeVOList.stream()
                .filter(vo -> vo.getParentCode() != null && !vo.getParentCode().isEmpty())
                .collect(Collectors.groupingBy(SysTenantTreeVO::getParentCode));

        for (SysTenantTreeVO vo : treeVOList) {
            List<SysTenantTreeVO> children = parentMap.get(vo.getTenantCode());
            vo.setChildTenant(children);
        }

        return treeVOList.stream()
                .filter(vo -> vo.getParentCode() == null || vo.getParentCode().isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * <p>
     * 基于 ancestors 字段查找指定租户的所有后代节点
     * </p>
     * <p>
     * 利用物化路径前缀匹配查询，仅需一次 SQL 即可获取全部后代，
     * 避免递归查询或全表加载。查询模式为 LIKE 'prefix/%'，可利用 B-tree 索引。
     * </p>
     *
     * @param tenantCode 目标租户编码
     * @param tenantAncestors 目标租户的 ancestors 值
     * @return 后代租户实体列表
     * @author shy
     * @since 2026-05-04
     */
    private List<SysTenant> findDescendantsByAncestors(String tenantCode, String tenantAncestors) {

        // 构建后代匹配前缀：当前租户的完整路径
        String descendantPrefix = tenantAncestors + "/" + tenantCode;

        // 前缀匹配查询所有后代（可利用 B-tree 索引）
        LambdaQueryWrapper<SysTenant> wrapper = new LambdaQueryWrapper<SysTenant>()
                .likeRight(SysTenant::getAncestors, descendantPrefix + "/")
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());

        return this.list(wrapper);
    }

    /**
     * <p>
     * 校验租户编码格式
     * </p>
     * <p>
     * 校验编码仅包含字母和数字，与 RegexConstant.Character.ALPHANUMERIC 一致。
     * 用于 Service 层对 List 中元素的逐一校验。
     * </p>
     *
     * @param code 租户编码
     * @param fieldName 字段名称（用于异常提示）
     * @author shy
     * @since 2026-05-04
     */
    private void validateTenantCode(String code, String fieldName) {
        if (StringUtils.isBlank(code)) {
            throw new BusinessException(400, fieldName + "不能为空");
        }
        if (!code.matches("^[a-zA-Z0-9]+$")) {
            throw new BusinessException(400, fieldName + "格式不正确: " + code);
        }
    }

    /**
     * <p>
     * 校验租户编码格式
     * </p>
     * <p>
     * 校验编码仅包含字母和数字，与 RegexConstant.Character.ALPHANUMERIC 一致。
     * 用于 Service 层对 List 中元素的逐一校验。
     * </p>
     *
     * @param codes 租户编码集合
     * @param fieldName 字段名称（用于异常提示）
     * @author shy
     * @since 2026-05-04
     */
    private void validateTenantCode(List<String> codes, String fieldName) {
        if (codes == null || codes.isEmpty()) {
            throw new BusinessException(400, fieldName + "不能为空");
        }
        for (String code : codes) {
            if (StringUtils.isBlank(code)) {
                throw new BusinessException(400, fieldName + "中存在空值");
            }
            if (!code.matches("^[a-zA-Z0-9]+$")) {
                throw new BusinessException(400, fieldName + "格式不正确: " + code);
            }
        }
    }

    /**
     * <p>
     * 在树形结构中递归查找指定ID的节点
     * </p>
     *
     * @param treeList 树形结构列表
     * @param tenantId 目标租户ID
     * @return 目标节点，未找到返回 null
     * @author shy
     * @since 2026-05-04
     */
    private SysTenantTreeVO findNodeInTree(List<SysTenantTreeVO> treeList, String tenantId) {
        if (treeList == null || treeList.isEmpty()) {
            return null;
        }
        for (SysTenantTreeVO node : treeList) {
            if (node.getTenantCode().equals(tenantId)) {
                return node;
            }
            SysTenantTreeVO found = findNodeInTree(node.getChildTenant(), tenantId);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

}
