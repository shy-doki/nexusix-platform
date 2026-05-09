package com.shy.nexusix.iam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.enums.GlobalEnum.PolicyAction;
import com.shy.nexusix.common.enums.GlobalEnum.TargetType;
import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.common.rto.TimeRangeCommonRTO;
import com.shy.nexusix.iam.converter.SysPermissionPolicyConverter;
import com.shy.nexusix.iam.entity.SysPermissionPolicy;
import com.shy.nexusix.iam.mapper.SysPermissionPolicyMapper;
import com.shy.nexusix.iam.rto.SysPermissionPolicyAddRTO;
import com.shy.nexusix.iam.rto.SysPermissionPolicyQueryRTO;
import com.shy.nexusix.iam.rto.SysPermissionPolicyUpdateRTO;
import com.shy.nexusix.iam.service.ISysPermissionPolicyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shy.nexusix.iam.vo.SysPermissionPolicyCommonVO;
import com.shy.nexusix.iam.vo.SysPermissionPolicyDetailVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 权限策略控制表 - 实现四层权限及禁用继承逻辑 服务实现类
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Service
public class SysPermissionPolicyServiceImpl extends ServiceImpl<SysPermissionPolicyMapper, SysPermissionPolicy> implements ISysPermissionPolicyService {

    @Autowired
    private SysPermissionPolicyConverter sysPermissionPolicyConverter;

    /**
     * <p>
     * 查询权限策略列表
     * </p>
     * <p>
     * 返回所有权限策略的平铺列表。
     * 需要登录并具备权限策略查看权限才能访问。
     * </p>
     *
     * @return 权限策略列表
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    public List<SysPermissionPolicyCommonVO> queryPolicyList() {

        // 构建查询条件：仅查询未删除的策略，按优先级降序、创建时间倒序排列
        LambdaQueryWrapper<SysPermissionPolicy> wrapper = new LambdaQueryWrapper<SysPermissionPolicy>()
                .eq(SysPermissionPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .orderByDesc(SysPermissionPolicy::getPriority)
                .orderByDesc(SysPermissionPolicy::getCreateTime);

        // 执行查询获取策略列表
        List<SysPermissionPolicy> policyList = this.list(wrapper);

        // 转换为VO对象并返回
        return sysPermissionPolicyConverter.toVoList(policyList);
    }

    /**
     * <p>
     * 分页查询权限策略列表
     * </p>
     * <p>
     * 返回分页后的权限策略列表。
     * 需要登录并具备权限策略查看权限才能访问。
     * </p>
     *
     * @param page 分页参数
     * @return 分页后的权限策略列表
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限
     * @author shy
     * @since 2026-05-06
     */
    @Override
    public IPage<SysPermissionPolicyCommonVO> queryPolicyPage(PageCommonRTO page) {

        // 构建分页参数
        Page<SysPermissionPolicy> pageParam = new Page<>(page.getPageNum(), page.getPageSize());

        // 构建查询条件：仅查询未删除的策略，按优先级降序、创建时间倒序排列
        LambdaQueryWrapper<SysPermissionPolicy> wrapper = new LambdaQueryWrapper<SysPermissionPolicy>()
                .eq(SysPermissionPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .orderByDesc(SysPermissionPolicy::getPriority)
                .orderByDesc(SysPermissionPolicy::getCreateTime);

        // 执行分页查询
        IPage<SysPermissionPolicy> policyPage = this.page(pageParam, wrapper);

        // 转换为VO分页对象并返回
        return sysPermissionPolicyConverter.toVOPage(policyPage);
    }

    /**
     * <p>
     * 条件查询\筛选权限策略列表
     * </p>
     * <p>
     * 返回满足条件的权限策略列表。
     * 需要登录并具备权限策略条件查询权限才能访问。
     * </p>
     *
     * @param queryParam 查询条件
     * @return 满足条件的权限策略列表
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    public IPage<SysPermissionPolicyCommonVO> queryPolicy(SysPermissionPolicyQueryRTO queryParam) {

        // 构建分页参数
        Page<SysPermissionPolicy> pageParam = new Page<>(queryParam.getPageNum(), queryParam.getPageSize());

        // 构建动态查询条件
        LambdaQueryWrapper<SysPermissionPolicy> wrapper = new LambdaQueryWrapper<>();

        // 目标类型条件查询
        if (StringUtils.isNotBlank(queryParam.getTargetType())) {
            TargetType targetType = TargetType.parse(queryParam.getTargetType());
            if (targetType != null) {
                wrapper.eq(SysPermissionPolicy::getTargetType, targetType.getCode());
            }
        }

        // 目标ID条件查询
        if (StringUtils.isNotBlank(queryParam.getTargetId())) {
            wrapper.eq(SysPermissionPolicy::getTargetId, Long.parseLong(queryParam.getTargetId()));
        }

        // 目标名称模糊查询
        wrapper.like(StringUtils.isNotBlank(queryParam.getTargetName()),
                SysPermissionPolicy::getTargetName, queryParam.getTargetName());

        // 关联权限ID条件查询
        if (StringUtils.isNotBlank(queryParam.getPermissionId())) {
            wrapper.eq(SysPermissionPolicy::getPermissionId, Long.parseLong(queryParam.getPermissionId()));
        }

        // 关联权限名称模糊查询
        wrapper.like(StringUtils.isNotBlank(queryParam.getPermName()),
                SysPermissionPolicy::getPermName, queryParam.getPermName());

        // 动作条件查询
        if (StringUtils.isNotBlank(queryParam.getAction())) {
            PolicyAction action = PolicyAction.parse(queryParam.getAction());
            if (action != null) {
                wrapper.eq(SysPermissionPolicy::getAction, action.getCode());
            }
        }

        // 是否向下继承条件查询
        wrapper.eq(queryParam.getInheritanceEnabled() != null,
                SysPermissionPolicy::getInheritanceEnabled, queryParam.getInheritanceEnabled());

        // 创建人姓名精确查询
        wrapper.eq(StringUtils.isNotBlank(queryParam.getCreateByName()),
                SysPermissionPolicy::getCreateByName, queryParam.getCreateByName());

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
                // 两者都有 between查询
                wrapper.between(SysPermissionPolicy::getCreateTime, startTime, endTime);
            } else if (startTime != null) {
                // 只有开始时间 大于等于查询
                wrapper.ge(SysPermissionPolicy::getCreateTime, startTime);
            } else if (endTime != null) {
                // 只有结束时间 小于等于查询
                wrapper.le(SysPermissionPolicy::getCreateTime, endTime);
            }
        }

        // 仅查询未删除的策略，按优先级降序、创建时间倒序排列
        wrapper.eq(SysPermissionPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        wrapper.orderByDesc(SysPermissionPolicy::getPriority);
        wrapper.orderByDesc(SysPermissionPolicy::getCreateTime);

        // 执行条件分页查询
        IPage<SysPermissionPolicy> policyQueryPage = this.page(pageParam, wrapper);

        // 转换为VO分页对象并返回
        return sysPermissionPolicyConverter.toVOPage(policyQueryPage);
    }

    /**
     * <p>
     * 查询权限策略详情
     * </p>
     * <p>
     * 返回指定权限策略的详情信息。
     * 需要登录并具备权限策略详情查询权限才能访问。
     * </p>
     *
     * @param id 策略ID
     * @return 权限策略详情信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    public SysPermissionPolicyDetailVO queryPolicyDetail(String id) {

        // 参数校验 策略ID不能为空
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(400, "策略ID不能为空");
        }

        // 构建查询条件：根据策略ID查询未删除的策略
        LambdaQueryWrapper<SysPermissionPolicy> wrapper = new LambdaQueryWrapper<SysPermissionPolicy>()
                .eq(SysPermissionPolicy::getId, Long.parseLong(id))
                .eq(SysPermissionPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());

        // 执行查询获取策略详情
        SysPermissionPolicy policyDetail = this.getOne(wrapper);

        // 策略不存在时抛出异常
        if (policyDetail == null) {
            throw new BusinessException(404, "权限策略不存在");
        }

        // 转换为详情VO对象并返回
        return sysPermissionPolicyConverter.toDetailVO(policyDetail);
    }

    /**
     * <p>
     * 新增权限策略
     * </p>
     * <p>
     * 新增权限策略信息，需要登录并具备权限策略新增权限才能访问。
     * </p>
     *
     * @param addParam 新增权限策略信息
     * @return 新增影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或新增失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    public Integer addPolicy(SysPermissionPolicyAddRTO addParam) {

        // 校验同一目标是否已存在相同权限的策略
        LambdaQueryWrapper<SysPermissionPolicy> wrapper = new LambdaQueryWrapper<SysPermissionPolicy>()
                .eq(SysPermissionPolicy::getTargetType, addParam.getTargetType().getCode())
                .eq(SysPermissionPolicy::getTargetId, Long.parseLong(addParam.getTargetId()))
                .eq(SysPermissionPolicy::getPermissionId, Long.parseLong(addParam.getPermissionId()))
                .eq(SysPermissionPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());

        long count = this.count(wrapper);

        if (count > 0) {
            throw new BusinessException(400, "该目标已存在相同权限的策略");
        }

        // 转换并保存策略信息
        boolean result = this.save(sysPermissionPolicyConverter.toEntityAdd(addParam));

        if (!result) {
            throw new BusinessException(500, "新增权限策略失败");
        }

        return 1;
    }

    /**
     * <p>
     * 修改权限策略
     * </p>
     * <p>
     * 修改权限策略信息，需要登录并具备权限策略修改权限才能访问。
     * </p>
     *
     * @param updateParam 修改权限策略信息
     * @return 修改影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、策略不存在或修改失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updatePolicy(SysPermissionPolicyUpdateRTO updateParam) {

        // 查询待修改的策略是否存在
        LambdaQueryWrapper<SysPermissionPolicy> wrapper = new LambdaQueryWrapper<SysPermissionPolicy>()
                .eq(SysPermissionPolicy::getId, updateParam.getId())
                .eq(SysPermissionPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysPermissionPolicy existPolicy = this.getOne(wrapper);

        if (existPolicy == null) {
            throw new BusinessException(404, "权限策略不存在");
        }

        // 如果修改了目标类型/目标ID/权限ID的组合, 需校验新组合是否已被其他策略使用
        boolean targetTypeChanged = !existPolicy.getTargetType().equals(updateParam.getTargetType().getCode());
        boolean targetIdChanged = !existPolicy.getTargetId().equals(Long.parseLong(updateParam.getTargetId()));
        boolean permissionIdChanged = !existPolicy.getPermissionId().equals(Long.parseLong(updateParam.getPermissionId()));

        if (targetTypeChanged || targetIdChanged || permissionIdChanged) {
            LambdaQueryWrapper<SysPermissionPolicy> dupWrapper = new LambdaQueryWrapper<SysPermissionPolicy>()
                    .eq(SysPermissionPolicy::getTargetType, updateParam.getTargetType().getCode())
                    .eq(SysPermissionPolicy::getTargetId, Long.parseLong(updateParam.getTargetId()))
                    .eq(SysPermissionPolicy::getPermissionId, Long.parseLong(updateParam.getPermissionId()))
                    .eq(SysPermissionPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                    .ne(SysPermissionPolicy::getId, updateParam.getId());
            long dupCount = this.count(dupWrapper);

            if (dupCount > 0) {
                throw new BusinessException(400, "该目标已存在相同权限的策略");
            }
        }

        // 转换并更新策略信息
        SysPermissionPolicy policy = sysPermissionPolicyConverter.toEntityUpdate(updateParam);

        boolean result = this.updateById(policy);

        if (!result) {
            throw new BusinessException(500, "修改权限策略失败");
        }

        return 1;
    }

    /**
     * <p>
     * 更新权限策略动作
     * </p>
     * <p>
     * 更新指定权限策略的动作（允许/拒绝）。
     * 需要登录并具备权限策略修改权限才能访问。
     * </p>
     *
     * @param id 策略ID
     * @param action 动作（允许/拒绝）
     * @return 更新结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、策略不存在或更新失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    public Integer updatePolicyAction(String id, String action) {

        // 参数校验：策略ID不能为空
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(400, "策略ID不能为空");
        }

        // 参数校验：动作不能为空
        if (StringUtils.isBlank(action)) {
            throw new BusinessException(400, "动作不能为空");
        }

        // 校验动作值是否合法
        GlobalEnum.PolicyAction policyAction = GlobalEnum.PolicyAction.parse(action);
        if (policyAction == null) {
            throw new BusinessException(400, "动作值不合法，仅支持：允许、拒绝");
        }

        // 查询待更新动作的策略是否存在
        LambdaQueryWrapper<SysPermissionPolicy> wrapper = new LambdaQueryWrapper<SysPermissionPolicy>()
                .eq(SysPermissionPolicy::getId, Long.parseLong(id))
                .eq(SysPermissionPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysPermissionPolicy existPolicy = this.getOne(wrapper);

        if (existPolicy == null) {
            throw new BusinessException(400, "权限策略不存在");
        }

        // 如果动作未变更则直接返回
        if (policyAction.getCode().equals(existPolicy.getAction())) {
            throw new BusinessException(400, "权限策略动作未变更");
        }

        // 执行动作更新
        SysPermissionPolicy updatePolicy = new SysPermissionPolicy();
        updatePolicy.setId(Long.parseLong(id));
        updatePolicy.setAction(policyAction.getCode());

        boolean result = this.updateById(updatePolicy);

        if (!result) {
            throw new BusinessException(500, "更新权限策略动作失败");
        }

        return 1;
    }

    /**
     * <p>
     * 删除权限策略
     * </p>
     * <p>
     * 删除指定权限策略信息，需要登录并具备权限策略删除权限才能访问。
     * </p>
     *
     * @param id 策略ID
     * @return 删除影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、策略不存在或删除失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    public Integer deletePolicy(String id) {

        // 查询待删除的策略是否存在
        LambdaQueryWrapper<SysPermissionPolicy> wrapper = new LambdaQueryWrapper<SysPermissionPolicy>()
                .eq(SysPermissionPolicy::getId, id)
                .eq(SysPermissionPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysPermissionPolicy existPolicy = this.getOne(wrapper);

        if (existPolicy == null) {
            throw new BusinessException(400, "权限策略不存在");
        }

        // 执行逻辑删除：设置is_deleted标志位
        SysPermissionPolicy policy = new SysPermissionPolicy();
        policy.setId(Long.parseLong(id));
        policy.setIsDeleted(GlobalEnum.Deleted.DELETED.getCode());

        boolean result = this.updateById(policy);

        if (!result) {
            throw new BusinessException(500, "删除权限策略失败");
        }

        return 1;
    }

    /**
     * <p>
     * 批量新增权限策略
     * </p>
     * <p>
     * 批量新增多个权限策略信息，需要登录并具备权限策略新增权限才能访问。
     * 批量操作支持事务回滚，任一策略新增失败则全部失败。
     * </p>
     *
     * @param addParamList 批量新增权限策略信息集合
     * @return 新增影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、参数校验失败或新增失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchAddPolicy(List<SysPermissionPolicyAddRTO> addParamList) {

        // 校验批量新增数量限制
        if (addParamList.size() > 100) {
            throw new BusinessException(400, "单次批量新增数量不能超过100条");
        }

        // 校验批量数据中是否有重复的目标+权限组合
        Set<String> combinationSet = new HashSet<>();
        for (SysPermissionPolicyAddRTO param : addParamList) {
            String combination = param.getTargetType().getCode() + ":" + param.getTargetId() + ":" + param.getPermissionId();
            if (combinationSet.contains(combination)) {
                throw new BusinessException(400, "批量新增中存在重复的目标+权限组合: 目标=" + param.getTargetName() + ", 权限=" + param.getPermName());
            }
            combinationSet.add(combination);
        }

        // 批量保存策略信息
        boolean batch = this.saveBatch(sysPermissionPolicyConverter.toEntityListAdd(addParamList));

        if (!batch) {
            throw new BusinessException(500, "批量新增权限策略失败");
        }

        return addParamList.size();
    }

    /**
     * <p>
     * 批量修改权限策略
     * </p>
     * <p>
     * 批量修改多个权限策略信息，需要登录并具备权限策略修改权限才能访问。
     * </p>
     *
     * @param updateParamList 批量修改权限策略信息集合
     * @return 修改影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、策略不存在或修改失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdatePolicy(List<SysPermissionPolicyUpdateRTO> updateParamList) {

        // 校验批量修改数量限制
        if (updateParamList.size() > 100) {
            throw new BusinessException(400, "单次批量修改数量不能超过100条");
        }

        // 校验批量数据的合法性：ID不能为空、不能有重复的ID
        Set<Long> idSet = new HashSet<>();
        for (SysPermissionPolicyUpdateRTO item : updateParamList) {
            if (item.getId() == null) {
                throw new BusinessException(400, "批量修改中存在ID为空的记录");
            }
            if (idSet.contains(item.getId())) {
                throw new BusinessException(400, "批量修改中存在重复的策略ID: " + item.getId());
            }
            idSet.add(item.getId());
        }

        // 校验策略ID是否全部存在且未被删除
        LambdaQueryWrapper<SysPermissionPolicy> idWrapper = new LambdaQueryWrapper<SysPermissionPolicy>()
                .in(SysPermissionPolicy::getId, idSet)
                .eq(SysPermissionPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        long existIdCount = this.count(idWrapper);

        if (existIdCount != idSet.size()) {
            throw new BusinessException(400, "部分策略ID不存在或已删除，请检查后重试");
        }

        // 批量更新策略信息
        boolean batch = this.updateBatchById(sysPermissionPolicyConverter.toEntityListUpdate(updateParamList));

        if (!batch) {
            throw new BusinessException(500, "批量更新权限策略失败");
        }

        return updateParamList.size();
    }

    /**
     * <p>
     * 批量更新权限策略动作
     * </p>
     * <p>
     * 批量更新多个指定权限策略的动作（允许/拒绝）。
     * 批量操作支持事务回滚，任一策略更新失败则全部失败。
     * 需要登录并具备权限策略修改权限才能访问。
     * </p>
     *
     * @param ids 策略ID集合
     * @param action 动作（允许/拒绝）
     * @return 更新结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、策略不存在或更新失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdatePolicyAction(List<String> ids, String action) {

        // 校验批量更新数量限制
        if (ids.size() > 100) {
            throw new BusinessException(400, "单次批量更新数量不能超过100条");
        }

        // 参数校验：动作不能为空
        if (StringUtils.isBlank(action)) {
            throw new BusinessException(400, "动作不能为空");
        }

        // 校验动作值是否合法
        GlobalEnum.PolicyAction policyAction = GlobalEnum.PolicyAction.parse(action);
        if (policyAction == null) {
            throw new BusinessException(400, "动作值不合法，仅支持：允许、拒绝");
        }

        // 校验ID格式并转换为Long类型
        Set<Long> idSet = new HashSet<>();
        for (String id : ids) {
            if (StringUtils.isBlank(id)) {
                throw new BusinessException(400, "策略ID不能为空");
            }
            idSet.add(Long.parseLong(id));
        }

        // 查询待更新动作的策略是否存在且未被删除
        LambdaQueryWrapper<SysPermissionPolicy> existWrapper = new LambdaQueryWrapper<SysPermissionPolicy>()
                .in(SysPermissionPolicy::getId, idSet)
                .eq(SysPermissionPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        List<SysPermissionPolicy> existPolicies = this.list(existWrapper);

        if (existPolicies.isEmpty()) {
            throw new BusinessException(404, "未找到可更新动作的权限策略");
        }

        // 构建批量动作更新的数据列表
        List<SysPermissionPolicy> updateList = new ArrayList<>();
        for (SysPermissionPolicy policy : existPolicies) {
            // 跳过动作未变更的策略
            if (policyAction.getCode().equals(policy.getAction())) {
                continue;
            }
            SysPermissionPolicy updatePolicy = new SysPermissionPolicy();
            updatePolicy.setId(policy.getId());
            updatePolicy.setAction(policyAction.getCode());
            updateList.add(updatePolicy);
        }

        if (updateList.isEmpty()) {
            throw new BusinessException(400, "所有权限策略动作均未变更");
        }

        // 执行批量动作更新
        boolean batch = this.updateBatchById(updateList);

        if (!batch) {
            throw new BusinessException(500, "批量更新权限策略动作失败");
        }

        return updateList.size();
    }

    /**
     * <p>
     * 批量删除权限策略
     * </p>
     * <p>
     * 批量删除多个指定权限策略信息，需要登录并具备权限策略删除权限才能访问。
     * </p>
     *
     * @param ids 策略ID集合
     * @return 删除影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、策略不存在或删除失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchDeletePolicy(List<String> ids) {

        // 校验批量删除数量限制
        if (ids.size() > 100) {
            throw new BusinessException(400, "单次批量删除数量不能超过100条");
        }

        // 校验ID格式并转换为Long类型
        Set<Long> idSet = new HashSet<>();
        for (String id : ids) {
            if (StringUtils.isBlank(id)) {
                throw new BusinessException(400, "策略ID不能为空");
            }
            idSet.add(Long.parseLong(id));
        }

        // 查询待删除的策略是否存在且未被删除
        LambdaQueryWrapper<SysPermissionPolicy> existWrapper = new LambdaQueryWrapper<SysPermissionPolicy>()
                .in(SysPermissionPolicy::getId, idSet)
                .eq(SysPermissionPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        List<SysPermissionPolicy> existPolicies = this.list(existWrapper);

        if (existPolicies.isEmpty()) {
            throw new BusinessException(404, "未找到可删除的权限策略");
        }

        // 构建批量逻辑删除的数据列表
        List<SysPermissionPolicy> policyList = new ArrayList<>();
        for (SysPermissionPolicy policy : existPolicies) {
            SysPermissionPolicy deletePolicy = new SysPermissionPolicy();
            deletePolicy.setId(policy.getId());
            deletePolicy.setIsDeleted(GlobalEnum.Deleted.DELETED.getCode());
            policyList.add(deletePolicy);
        }

        // 执行批量逻辑删除
        boolean batch = this.updateBatchById(policyList);

        if (!batch) {
            throw new BusinessException(500, "批量删除权限策略失败");
        }

        return ids.size();
    }

}
