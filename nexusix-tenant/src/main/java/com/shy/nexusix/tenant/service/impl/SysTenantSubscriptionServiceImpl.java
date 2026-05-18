package com.shy.nexusix.tenant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.enums.GlobalEnum.SubscriptionStatus;
import com.shy.nexusix.common.enums.GlobalEnum.SubscriptionType;
import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.common.rto.TimeRangeCommonRTO;
import com.shy.nexusix.tenant.converter.SysTenantSubscriptionConverter;
import com.shy.nexusix.tenant.entity.SysTenantSubscription;
import com.shy.nexusix.tenant.mapper.SysTenantSubscriptionMapper;
import com.shy.nexusix.tenant.rto.SysTenantSubscriptionAddRTO;
import com.shy.nexusix.tenant.rto.SysTenantSubscriptionQueryRTO;
import com.shy.nexusix.tenant.rto.SysTenantSubscriptionUpdateRTO;
import com.shy.nexusix.tenant.service.ISysTenantSubscriptionService;
import com.shy.nexusix.tenant.vo.SysTenantSubscriptionCommonVO;
import com.shy.nexusix.tenant.vo.SysTenantSubscriptionDetailVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 租户套餐订阅表 - 记录租户购买的套餐及订阅状态 服务实现类
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Service
public class SysTenantSubscriptionServiceImpl extends ServiceImpl<SysTenantSubscriptionMapper, SysTenantSubscription> implements ISysTenantSubscriptionService {

    @Autowired
    private SysTenantSubscriptionConverter sysTenantSubscriptionConverter;

    @Override
    public List<SysTenantSubscriptionCommonVO> querySubscriptionList() {
        // 构建查询条件：仅查询未删除的记录，按创建时间降序排列
        LambdaQueryWrapper<SysTenantSubscription> wrapper = new LambdaQueryWrapper<SysTenantSubscription>()
                .eq(SysTenantSubscription::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .orderByDesc(SysTenantSubscription::getCreateTime);
        List<SysTenantSubscription> subscriptionList = this.list(wrapper);
        // 通过转换器将实体列表转换为通用VO列表
        return sysTenantSubscriptionConverter.toVoList(subscriptionList);
    }

    @Override
    public IPage<SysTenantSubscriptionCommonVO> querySubscriptionPage(PageCommonRTO page) {
        // 构建分页参数
        Page<SysTenantSubscription> pageParam = new Page<>(page.getPageNum(), page.getPageSize());
        // 构建查询条件：仅查询未删除的记录，按创建时间降序排列
        LambdaQueryWrapper<SysTenantSubscription> wrapper = new LambdaQueryWrapper<SysTenantSubscription>()
                .eq(SysTenantSubscription::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .orderByDesc(SysTenantSubscription::getCreateTime);
        IPage<SysTenantSubscription> subscriptionPage = this.page(pageParam, wrapper);
        // 通过转换器将实体分页转换为通用VO分页
        return sysTenantSubscriptionConverter.toVOPage(subscriptionPage);
    }

    @Override
    public IPage<SysTenantSubscriptionCommonVO> querySubscription(SysTenantSubscriptionQueryRTO queryParam) {
        // 构建分页参数
        Page<SysTenantSubscription> pageParam = new Page<>(queryParam.getPageNum(), queryParam.getPageSize());
        LambdaQueryWrapper<SysTenantSubscription> wrapper = new LambdaQueryWrapper<>();

        // 租户ID精确查询
        if (StringUtils.isNotBlank(queryParam.getTenantId())) {
            wrapper.eq(SysTenantSubscription::getTenantId, Long.parseLong(queryParam.getTenantId()));
        }

        if (StringUtils.isNotBlank(queryParam.getPackageCode())) {
            wrapper.eq(SysTenantSubscription::getPackageId, Long.parseLong(queryParam.getPackageCode()));
        }

        // 订阅类型条件查询：通过parse方法支持多种输入格式
        if (StringUtils.isNotBlank(queryParam.getSubscriptionType())) {
            SubscriptionType subscriptionType = SubscriptionType.parse(queryParam.getSubscriptionType());
            if (subscriptionType != null) {
                wrapper.eq(SysTenantSubscription::getSubscriptionType, subscriptionType.getCode());
            }
        }

        // 状态条件查询：通过parse方法支持多种输入格式
        if (StringUtils.isNotBlank(queryParam.getStatus())) {
            SubscriptionStatus subscriptionStatus = SubscriptionStatus.parse(queryParam.getStatus());
            if (subscriptionStatus != null) {
                wrapper.eq(SysTenantSubscription::getStatus, subscriptionStatus.getCode());
            }
        }

        // 是否自动续费条件查询
        wrapper.eq(queryParam.getIsAutoRenew() != null,
                SysTenantSubscription::getIsAutoRenew, queryParam.getIsAutoRenew());

        // 订阅开始时间范围查询
        TimeRangeCommonRTO startTime = queryParam.getStartTime();
        if (startTime != null) {
            LocalDateTime startStartTime = startTime.getStartTime();
            LocalDateTime startEndTime = startTime.getEndTime();
            // 校验时间范围合法性
            if (startStartTime != null && startEndTime != null && startStartTime.isAfter(startEndTime)) {
                throw new BusinessException(400, "订阅开始时间范围不合法，开始时间不能晚于结束时间");
            }
            if (startStartTime != null && startEndTime != null) {
                wrapper.between(SysTenantSubscription::getStartTime, startStartTime, startEndTime);
            } else if (startStartTime != null) {
                wrapper.ge(SysTenantSubscription::getStartTime, startStartTime);
            } else if (startEndTime != null) {
                wrapper.le(SysTenantSubscription::getStartTime, startEndTime);
            }
        }

        // 订阅结束时间范围查询
        TimeRangeCommonRTO endTime = queryParam.getEndTime();
        if (endTime != null) {
            LocalDateTime endStartTime = endTime.getStartTime();
            LocalDateTime endEndTime = endTime.getEndTime();
            // 校验时间范围合法性
            if (endStartTime != null && endEndTime != null && endStartTime.isAfter(endEndTime)) {
                throw new BusinessException(400, "订阅结束时间范围不合法，开始时间不能晚于结束时间");
            }
            if (endStartTime != null && endEndTime != null) {
                wrapper.between(SysTenantSubscription::getEndTime, endStartTime, endEndTime);
            } else if (endStartTime != null) {
                wrapper.ge(SysTenantSubscription::getEndTime, endStartTime);
            } else if (endEndTime != null) {
                wrapper.le(SysTenantSubscription::getEndTime, endEndTime);
            }
        }

        // 创建时间范围查询
        TimeRangeCommonRTO createTime = queryParam.getCreateTime();
        if (createTime != null) {
            LocalDateTime createStartTime = createTime.getStartTime();
            LocalDateTime createEndTime = createTime.getEndTime();
            // 校验时间范围合法性
            if (createStartTime != null && createEndTime != null && createStartTime.isAfter(createEndTime)) {
                throw new BusinessException(400, "创建时间范围不合法，开始时间不能晚于结束时间");
            }
            if (createStartTime != null && createEndTime != null) {
                wrapper.between(SysTenantSubscription::getCreateTime, createStartTime, createEndTime);
            } else if (createStartTime != null) {
                wrapper.ge(SysTenantSubscription::getCreateTime, createStartTime);
            } else if (createEndTime != null) {
                wrapper.le(SysTenantSubscription::getCreateTime, createEndTime);
            }
        }

        // 逻辑删除条件：仅查询未删除的记录
        wrapper.eq(SysTenantSubscription::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        // 按创建时间降序排列
        wrapper.orderByDesc(SysTenantSubscription::getCreateTime);

        IPage<SysTenantSubscription> subscriptionQueryPage = this.page(pageParam, wrapper);
        // 通过转换器将实体分页转换为通用VO分页
        return sysTenantSubscriptionConverter.toVOPage(subscriptionQueryPage);
    }

    @Override
    public SysTenantSubscriptionDetailVO querySubscriptionDetail(String id) {
        // 参数校验
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(400, "订阅ID不能为空");
        }
        // 根据ID查询未删除的订阅记录
        LambdaQueryWrapper<SysTenantSubscription> wrapper = new LambdaQueryWrapper<SysTenantSubscription>()
                .eq(SysTenantSubscription::getId, Long.parseLong(id))
                .eq(SysTenantSubscription::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysTenantSubscription subscriptionDetail = this.getOne(wrapper);
        if (subscriptionDetail == null) {
            throw new BusinessException(404, "订阅不存在");
        }
        // 通过转换器将实体转换为详情VO
        return sysTenantSubscriptionConverter.toDetailVO(subscriptionDetail);
    }

    @Override
    public Integer addSubscription(SysTenantSubscriptionAddRTO addParam) {
        // 校验同一租户不能重复订阅同一套餐
        LambdaQueryWrapper<SysTenantSubscription> wrapper = new LambdaQueryWrapper<SysTenantSubscription>()
                .eq(SysTenantSubscription::getTenantId, Long.parseLong(addParam.getTenantId()))
                .eq(SysTenantSubscription::getPackageId, Long.parseLong(addParam.getPackageCode()))
                .eq(SysTenantSubscription::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        long count = this.count(wrapper);
        if (count > 0) {
            throw new BusinessException(400, "该租户已订阅此套餐");
        }
        // 校验订阅时间合法性：开始时间不能晚于结束时间
        if (addParam.getStartTime() != null && addParam.getEndTime() != null
                && addParam.getStartTime().isAfter(addParam.getEndTime())) {
            throw new BusinessException(400, "订阅开始时间不能晚于结束时间");
        }
        // 通过转换器将RTO转换为实体并保存
        boolean result = this.save(sysTenantSubscriptionConverter.toEntityAdd(addParam));
        if (!result) {
            throw new BusinessException(500, "新增订阅失败");
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateSubscription(SysTenantSubscriptionUpdateRTO updateParam) {
        // 查询待更新的订阅是否存在
        LambdaQueryWrapper<SysTenantSubscription> wrapper = new LambdaQueryWrapper<SysTenantSubscription>()
                .eq(SysTenantSubscription::getId, updateParam.getId())
                .eq(SysTenantSubscription::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysTenantSubscription existSubscription = this.getOne(wrapper);
        if (existSubscription == null) {
            throw new BusinessException(404, "订阅不存在");
        }
        // 校验租户和套餐组合是否重复（排除自身）
        boolean tenantChanged = !existSubscription.getTenantId().equals(Long.parseLong(updateParam.getTenantId()));
        boolean packageChanged = !existSubscription.getPackageId().equals(Long.parseLong(updateParam.getPackageCode()));
        if (tenantChanged || packageChanged) {
            LambdaQueryWrapper<SysTenantSubscription> dupWrapper = new LambdaQueryWrapper<SysTenantSubscription>()
                    .eq(SysTenantSubscription::getTenantId, Long.parseLong(updateParam.getTenantId()))
                    .eq(SysTenantSubscription::getPackageId, Long.parseLong(updateParam.getPackageCode()))
                    .eq(SysTenantSubscription::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                    .ne(SysTenantSubscription::getId, updateParam.getId());
            long dupCount = this.count(dupWrapper);
            if (dupCount > 0) {
                throw new BusinessException(400, "该租户已订阅此套餐");
            }
        }
        // 校验订阅时间合法性：开始时间不能晚于结束时间
        if (updateParam.getStartTime() != null && updateParam.getEndTime() != null
                && updateParam.getStartTime().isAfter(updateParam.getEndTime())) {
            throw new BusinessException(400, "订阅开始时间不能晚于结束时间");
        }
        // 通过转换器将RTO转换为实体并更新
        SysTenantSubscription subscription = sysTenantSubscriptionConverter.toEntityUpdate(updateParam);
        boolean result = this.updateById(subscription);
        if (!result) {
            throw new BusinessException(500, "修改订阅失败");
        }
        return 1;
    }

    @Override
    public Integer updateSubscriptionStatus(String id, String status) {
        // 参数校验
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(400, "订阅ID不能为空");
        }
        if (StringUtils.isBlank(status)) {
            throw new BusinessException(400, "状态不能为空");
        }
        // 通过parse方法解析状态值，支持多种输入格式
        SubscriptionStatus subscriptionStatus = SubscriptionStatus.parse(status);
        if (subscriptionStatus == null) {
            throw new BusinessException(400, "状态值不合法，仅支持：生效、过期");
        }
        // 查询订阅是否存在
        LambdaQueryWrapper<SysTenantSubscription> wrapper = new LambdaQueryWrapper<SysTenantSubscription>()
                .eq(SysTenantSubscription::getId, Long.parseLong(id))
                .eq(SysTenantSubscription::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysTenantSubscription existSubscription = this.getOne(wrapper);
        if (existSubscription == null) {
            throw new BusinessException(400, "订阅不存在");
        }
        // 校验状态是否发生变化
        if (subscriptionStatus.getCode().equals(existSubscription.getStatus())) {
            throw new BusinessException(400, "订阅状态未变更");
        }
        // 构建更新对象并更新状态
        SysTenantSubscription updateSubscription = new SysTenantSubscription();
        updateSubscription.setId(id);
        updateSubscription.setStatus(subscriptionStatus.getCode());
        boolean result = this.updateById(updateSubscription);
        if (!result) {
            throw new BusinessException(500, "更新订阅状态失败");
        }
        return 1;
    }

    @Override
    public Integer deleteSubscription(String id) {
        // 查询订阅是否存在且未删除
        LambdaQueryWrapper<SysTenantSubscription> wrapper = new LambdaQueryWrapper<SysTenantSubscription>()
                .eq(SysTenantSubscription::getId, id)
                .eq(SysTenantSubscription::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysTenantSubscription existSubscription = this.getOne(wrapper);
        if (existSubscription == null) {
            throw new BusinessException(400, "订阅不存在");
        }
        // 执行逻辑删除：将isDeleted标记为已删除
        SysTenantSubscription subscription = new SysTenantSubscription();
        subscription.setId(id);
        subscription.setIsDeleted(GlobalEnum.Deleted.DELETED.getCode());
        boolean result = this.updateById(subscription);
        if (!result) {
            throw new BusinessException(500, "删除订阅失败");
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchAddSubscription(List<SysTenantSubscriptionAddRTO> addParamList) {
        // 校验批量新增数量上限
        if (addParamList.size() > 100) {
            throw new BusinessException(400, "单次批量新增数量不能超过100条");
        }
        // 校验批量新增中是否存在重复的租户+套餐组合
        Set<String> combinationSet = new HashSet<>();
        for (SysTenantSubscriptionAddRTO param : addParamList) {
            String combination = param.getTenantId() + ":" + param.getPackageCode();
            if (combinationSet.contains(combination)) {
                throw new BusinessException(400, "批量新增中存在重复的租户+套餐组合: 租户=" + param.getTenantId() + ", 套餐=" + param.getPackageCode());
            }
            combinationSet.add(combination);
        }
        // 通过转换器批量转换并保存
        boolean batch = this.saveBatch(sysTenantSubscriptionConverter.toEntityListAdd(addParamList));
        if (!batch) {
            throw new BusinessException(500, "批量新增订阅失败");
        }
        return addParamList.size();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdateSubscription(List<SysTenantSubscriptionUpdateRTO> updateParamList) {
        // 校验批量修改数量上限
        if (updateParamList.size() > 100) {
            throw new BusinessException(400, "单次批量修改数量不能超过100条");
        }
        // 校验批量修改中ID不为空且不重复
        Set<String> idSet = new HashSet<>();
        for (SysTenantSubscriptionUpdateRTO item : updateParamList) {
            if (item.getId() == null) {
                throw new BusinessException(400, "批量修改中存在ID为空的记录");
            }
            if (idSet.contains(item.getId())) {
                throw new BusinessException(400, "批量修改中存在重复的订阅ID: " + item.getId());
            }
            idSet.add(item.getId());
        }
        // 校验所有ID对应的记录存在且未删除
        LambdaQueryWrapper<SysTenantSubscription> idWrapper = new LambdaQueryWrapper<SysTenantSubscription>()
                .in(SysTenantSubscription::getId, idSet)
                .eq(SysTenantSubscription::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        long existIdCount = this.count(idWrapper);
        if (existIdCount != idSet.size()) {
            throw new BusinessException(400, "部分订阅ID不存在或已删除，请检查后重试");
        }
        // 通过转换器批量转换并更新
        boolean batch = this.updateBatchById(sysTenantSubscriptionConverter.toEntityListUpdate(updateParamList));
        if (!batch) {
            throw new BusinessException(500, "批量更新订阅失败");
        }
        return updateParamList.size();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdateSubscriptionStatus(List<String> ids, String status) {
        // 校验批量更新数量上限
        if (ids.size() > 100) {
            throw new BusinessException(400, "单次批量更新数量不能超过100条");
        }
        if (StringUtils.isBlank(status)) {
            throw new BusinessException(400, "状态不能为空");
        }
        // 通过parse方法解析状态值
        SubscriptionStatus subscriptionStatus = SubscriptionStatus.parse(status);
        if (subscriptionStatus == null) {
            throw new BusinessException(400, "状态值不合法，仅支持：生效、过期");
        }
        // 收集并去重ID集合
        Set<Long> idSet = new HashSet<>();
        for (String id : ids) {
            if (StringUtils.isBlank(id)) {
                throw new BusinessException(400, "订阅ID不能为空");
            }
            idSet.add(Long.parseLong(id));
        }
        // 查询所有存在的未删除订阅记录
        LambdaQueryWrapper<SysTenantSubscription> existWrapper = new LambdaQueryWrapper<SysTenantSubscription>()
                .in(SysTenantSubscription::getId, idSet)
                .eq(SysTenantSubscription::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        List<SysTenantSubscription> existSubscriptions = this.list(existWrapper);
        if (existSubscriptions.isEmpty()) {
            throw new BusinessException(404, "未找到可更新状态的订阅");
        }
        // 过滤出状态需要变更的订阅记录
        List<SysTenantSubscription> updateList = new ArrayList<>();
        for (SysTenantSubscription subscription : existSubscriptions) {
            // 跳过状态未变更的记录
            if (subscriptionStatus.getCode().equals(subscription.getStatus())) {
                continue;
            }
            SysTenantSubscription updateSubscription = new SysTenantSubscription();
            updateSubscription.setId(subscription.getId());
            updateSubscription.setStatus(subscriptionStatus.getCode());
            updateList.add(updateSubscription);
        }
        if (updateList.isEmpty()) {
            throw new BusinessException(400, "所有订阅状态均未变更");
        }
        // 批量更新状态
        boolean batch = this.updateBatchById(updateList);
        if (!batch) {
            throw new BusinessException(500, "批量更新订阅状态失败");
        }
        return updateList.size();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchDeleteSubscription(List<String> ids) {
        // 校验批量删除数量上限
        if (ids.size() > 100) {
            throw new BusinessException(400, "单次批量删除数量不能超过100条");
        }
        // 收集并去重ID集合
        Set<Long> idSet = new HashSet<>();
        for (String id : ids) {
            if (StringUtils.isBlank(id)) {
                throw new BusinessException(400, "订阅ID不能为空");
            }
            idSet.add(Long.parseLong(id));
        }
        // 查询所有存在的未删除订阅记录
        LambdaQueryWrapper<SysTenantSubscription> existWrapper = new LambdaQueryWrapper<SysTenantSubscription>()
                .in(SysTenantSubscription::getId, idSet)
                .eq(SysTenantSubscription::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        List<SysTenantSubscription> existSubscriptions = this.list(existWrapper);
        if (existSubscriptions.isEmpty()) {
            throw new BusinessException(404, "未找到可删除的订阅");
        }
        // 构建逻辑删除更新列表
        List<SysTenantSubscription> subscriptionList = new ArrayList<>();
        for (SysTenantSubscription subscription : existSubscriptions) {
            SysTenantSubscription deleteSubscription = new SysTenantSubscription();
            deleteSubscription.setId(subscription.getId());
            deleteSubscription.setIsDeleted(GlobalEnum.Deleted.DELETED.getCode());
            subscriptionList.add(deleteSubscription);
        }
        // 批量执行逻辑删除
        boolean batch = this.updateBatchById(subscriptionList);
        if (!batch) {
            throw new BusinessException(500, "批量删除订阅失败");
        }
        return ids.size();
    }

}
