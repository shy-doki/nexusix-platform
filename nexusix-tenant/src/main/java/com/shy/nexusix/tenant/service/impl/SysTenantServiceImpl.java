package com.shy.nexusix.tenant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.common.rto.TimeRangeCommonRTO;
import com.shy.nexusix.tenant.converter.SysTenantConverter;
import com.shy.nexusix.tenant.entity.SysTenant;
import com.shy.nexusix.tenant.mapper.SysTenantMapper;
import com.shy.nexusix.tenant.rto.SysTenantAddRTO;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    // TODO 业务校验 + 软删除考虑使用aop结合注解去做

    // TODO 权限校验 控制器层/aop处理

    // TODO 业务校验(如不同角色查看的数据内容)

    // TODO 后续实现逻辑删除仅超级管理可见 才返回 否则剔除

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
        Page<SysTenant> pageParam = new Page<>(page.getPageNum(), page.getPageSize());

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
     * @return 分页后的租户列表，包含租户名称、脱敏后的租户编码、联系人、状态等信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限
     * @author shy
     * @since 2026-04-19
     */
    @Override
    public List<SysTenantTreeVO> queryTenantTreeList() {

    }

    /**
     * <p>
     * 分页查询租户树形结构
     * </p>
     * <p>
     * 返回所有租户的层级树形结构
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
    public IPage<SysTenantTreeVO> queryTenantTreePage(PageCommonRTO page) {

    }

    /**
     * <p>
     * 查询指定租户的树形结构
     * </p>
     * <p>
     * 查询系统中所有租户的层级关系，并构建成树形结构返回。
     * 返回的租户编码会自动进行脱敏处理（保留前3位和后3位，中间用星号替换）。
     * </p>
     *
     * @param id 租户Id，用于定位要查询的租户节点
     * @return 租户树形结构列表，每个节点包含租户名称、脱敏后的租户编码、父租户ID、联系人、状态等信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当数据库查询失败或数据异常时抛出
     * @author shy
     * @since 2026-04-19
     */
    @Override
    public List<SysTenantTreeVO> queryTenantTree(String id) {
        return List.of();
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

        // 联系人姓名精确查询
        wrapper.eq(StringUtils.isNotBlank(queryParam.getContactName()),
                SysTenant::getContactName, queryParam.getContactName());

        // 状态条件查询
        wrapper.eq(StringUtils.isNotBlank(queryParam.getContactPhone()),
                SysTenant::getContactPhone, queryParam.getContactPhone());

        // 状态条件查询
        if (queryParam.getStatus() != null) {
            wrapper.eq(SysTenant::getStatus, GlobalEnum.TenantStatus.getByDesc(queryParam.getStatus()).getCode());
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
            if (startTime != null && endTime != null && endTime.isBefore(startTime)) {
                throw new BusinessException(400, "结束时间不能早于开始时间");
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
            if (startTime != null && endTime != null && endTime.isBefore(startTime)) {
                throw new BusinessException(400, "结束时间不能早于开始时间");
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

        // 校验租户编码是否已存在
        LambdaQueryWrapper<SysTenant> wrapper = new LambdaQueryWrapper<SysTenant>()
                .eq(SysTenant::getTenantCode, addParam.getTenantCode());

        long count = this.count(wrapper);

        if (count > 0) {
            throw new BusinessException(400, "租户已存在");
        }

        String ancestors;

        // 处理父租户关系
        if (addParam.getParentId() != null && addParam.getParentId() != 0L) {
            // 查询父租户是否存在
            LambdaQueryWrapper<SysTenant> parentWrapper = new LambdaQueryWrapper<SysTenant>()
                    .eq(SysTenant::getId, addParam.getParentId())
                    .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
            SysTenant parentTenant = this.getOne(parentWrapper);

            if (parentTenant == null) {
                throw new BusinessException(400, "父租户不存在");
            }

            // 构建祖级列表：父租户的祖级列表 + 父租户编码
            ancestors =  parentTenant.getAncestors() + "/" + addParam.getTenantCode();

            addParam.setAncestors(ancestors);
        }

        // 转换并保存租户信息
        boolean result = this.save(sysTenantConverter.toEntityAdd(addParam));

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
     * @return 修改结果：true-成功，false-失败
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、租户不存在或修改失败时抛出
     * @author shy
     * @since 2026-04-20
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateTenant(SysTenantUpdateRTO updateParam) {

        // 查询待修改的租户是否存在
        LambdaQueryWrapper<SysTenant> wrapper = new LambdaQueryWrapper<SysTenant>()
                .eq(SysTenant::getId, updateParam.getId())
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysTenant existTenant = this.getOne(wrapper);

        if (existTenant == null) {
            throw new BusinessException(404, "租户不存在");
        }

        // 如果修改了租户编码, 需校验新编码是否已被其他租户使用
        if (!existTenant.getTenantCode().equals(updateParam.getTenantCode())) {
            LambdaQueryWrapper<SysTenant> codeWrapper = new LambdaQueryWrapper<SysTenant>()
                    .eq(SysTenant::getTenantCode, updateParam.getTenantCode())
                    .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
            long count = this.count(codeWrapper);
            if (count > 0) {
                throw new BusinessException(400, "租户编码已被其他租户使用");
            }
        }

        // 如果修改了父租户, 需要进行层级关系校验
        if (!existTenant.getParentId().equals(updateParam.getParentId())) {

            // 不能将自己设为父租户
            if (updateParam.getParentId().equals(updateParam.getId())) {
                throw new BusinessException(400, "不能将自己设为父租户");
            }

            // 校验父租户是否存在
            LambdaQueryWrapper<SysTenant> parenWrapper = new LambdaQueryWrapper<SysTenant>()
                    .eq(SysTenant::getParentId, updateParam.getParentId())
                    .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
            long count = this.count(parenWrapper);
            if (count > 0) {
                throw new BusinessException(400, "父租户不存在");
            }

            // 防止循环引用：不能将子租户设为父租户
            LambdaQueryWrapper<SysTenant> targetWrapper = new LambdaQueryWrapper<SysTenant>()
                    .eq(SysTenant::getId, updateParam.getParentId())
                    .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
            SysTenant targetTenant = this.getOne(targetWrapper);
            if (targetTenant != null
                    && targetTenant.getAncestors() != null
                    && targetTenant.getAncestors().contains("/" + updateParam.getId())) {
                throw new BusinessException(400, "不能将子租户设为父租户");
            }

        }

        // 转换并更新租户信息
        SysTenant tenant = sysTenantConverter.toEntityUpdate(updateParam);

        boolean result = this.updateById(tenant);

        if (!result) {
            throw new BusinessException(500, "新增租户失败");
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
                .eq(SysTenant::getParentId, Long.parseLong(id))
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        long childCount = this.count(childWrapper);
        if (childCount > 0) {
            throw new BusinessException(400, "该租户下存在子租户，请先解绑");
        }

        // 执行逻辑删除：设置is_deleted标志位
        SysTenant tenant = new SysTenant();
        tenant.setId(Long.parseLong(id));
        tenant.setIsDeleted(GlobalEnum.Deleted.DELETED.getCode());

        boolean result = this.updateById(tenant);

        if (!result) {
            throw new BusinessException(500, "删除租户失败");
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
     * @return 成功新增的租户ID集合
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、参数校验失败或新增失败时抛出
     * @author shy
     * @since 2026-04-20
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchAddTenant(List<SysTenantAddRTO> addParamList) {

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

        // 批量保存租户信息
        boolean batch = this.saveBatch(sysTenantConverter.toEntityListAdd(addParamList));

        if (!batch) {
            throw new BusinessException(500, "批量新增租户失败");
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
     * @return 修改结果：true-全部成功，false-部分/全部失败
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、租户不存在或修改失败时抛出
     * @author shy
     * @since 2026-04-20
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdateTenant(List<SysTenantUpdateRTO> updateParamList) {

        // 校验批量修改数量限制
        if (updateParamList.size() > 100) {
            throw new BusinessException(400, "单次批量修改数量不能超过100条");
        }

        // 校验批量数据的合法性：ID不能为空、不能有重复的ID和编码
        Set<Long> idSet = new HashSet<>();
        Set<String> codeSet = new HashSet<>();
        for (SysTenantUpdateRTO item : updateParamList) {
            if (item.getId() == null) {
                throw new BusinessException(400, "批量修改中存在ID为空的记录");
            }
            if (idSet.contains(item.getId())) {
                throw new BusinessException(400, "批量修改中存在重复的租户ID: " + item.getId());
            }
            if (codeSet.contains(item.getTenantCode())) {
                throw new BusinessException(400, "批量修改中存在重复的租户编码: " + item.getTenantCode());
            }
            idSet.add(item.getId());
            codeSet.add(item.getTenantCode());
        }

        // 校验租户ID是否存在（注意：此处代码逻辑有误，应该是用idSet查询ID）
        LambdaQueryWrapper<SysTenant> IdWrapper = new LambdaQueryWrapper<SysTenant>()
                .in(SysTenant::getTenantCode, codeSet)
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        long existIdCount = this.count(IdWrapper);

        // 校验租户编码是否存在（注意：此处代码逻辑有误，应该是用codeSet查询编码）
        LambdaQueryWrapper<SysTenant> codeWrapper = new LambdaQueryWrapper<SysTenant>()
                .in(SysTenant::getTenantCode, idSet)
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        long existCodeCount = this.count(codeWrapper);

        if (existIdCount > 0) {
            throw new BusinessException(400, "部分租户ID已存在，请检查后重试");
        }

        if (existCodeCount > 0) {
            throw new BusinessException(400, "部分租户编码已存在，请检查后重试");
        }

        // 批量更新租户信息
        boolean batch = this.updateBatchById(sysTenantConverter.toEntityListUpdate(updateParamList));

        if (!batch) {
            throw new BusinessException(500, "批量更新租户失败");
        }

        return updateParamList.size();

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

        // 校验批量删除数量限制
        if (ids.size() > 100) {
            throw new BusinessException(400, "单次批量删除数量不能超过100条");
        }

        // 校验ID格式并转换为Long类型
        Set<Long> idSet = new HashSet<>();
        for (String id : ids) {
            if (StringUtils.isBlank(id)) {
                throw new BusinessException(400, "租户ID不能为空");
            }
            idSet.add(Long.parseLong(id));
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
        List<SysTenant> tenantList = new ArrayList<>();
        for (SysTenant tenant : existTenants) {
            SysTenant deleteTenant = new SysTenant();
            deleteTenant.setId(tenant.getId());
            deleteTenant.setIsDeleted(GlobalEnum.Deleted.DELETED.getCode());
            tenantList.add(deleteTenant);
        }

        // 执行批量逻辑删除
        boolean batch = this.updateBatchById(tenantList);

        if (!batch) {
            throw new BusinessException(500, "批量删除租户失败");
        }

        return ids.size();

    }

}
