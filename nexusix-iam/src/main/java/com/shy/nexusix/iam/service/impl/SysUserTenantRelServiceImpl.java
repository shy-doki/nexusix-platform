package com.shy.nexusix.iam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.common.rto.TimeRangeCommonRTO;
import com.shy.nexusix.iam.converter.SysUserTenantRelConverter;
import com.shy.nexusix.iam.entity.SysUserTenantRel;
import com.shy.nexusix.iam.mapper.SysUserTenantRelMapper;
import com.shy.nexusix.iam.rto.SysUserTenantRelAddRTO;
import com.shy.nexusix.iam.rto.SysUserTenantRelQueryRTO;
import com.shy.nexusix.iam.rto.SysUserTenantRelUpdateRTO;
import com.shy.nexusix.iam.service.ISysUserTenantRelService;
import com.shy.nexusix.iam.vo.SysUserTenantRelCommonVO;
import com.shy.nexusix.iam.vo.SysUserTenantRelDetailVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 用户 - 租户关联表 - 实现用户与多租户绑定 服务实现类
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Service
public class SysUserTenantRelServiceImpl extends ServiceImpl<SysUserTenantRelMapper, SysUserTenantRel> implements ISysUserTenantRelService {

    @Autowired
    private SysUserTenantRelConverter sysUserTenantRelConverter;

    @Override
    public List<SysUserTenantRelCommonVO> queryUserTenantRelList() {
        // 构建查询条件：仅查询未删除的记录，按创建时间降序排列
        LambdaQueryWrapper<SysUserTenantRel> wrapper = new LambdaQueryWrapper<SysUserTenantRel>()
                .eq(SysUserTenantRel::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .orderByDesc(SysUserTenantRel::getCreateTime);
        List<SysUserTenantRel> relList = this.list(wrapper);
        // 通过转换器将实体列表转换为通用VO列表
        return sysUserTenantRelConverter.toVoList(relList);
    }

    @Override
    public IPage<SysUserTenantRelCommonVO> queryUserTenantRelPage(PageCommonRTO page) {
        // 构建分页参数
        Page<SysUserTenantRel> pageParam = new Page<>(page.getPageNum(), page.getPageSize());
        // 构建查询条件：仅查询未删除的记录，按创建时间降序排列
        LambdaQueryWrapper<SysUserTenantRel> wrapper = new LambdaQueryWrapper<SysUserTenantRel>()
                .eq(SysUserTenantRel::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .orderByDesc(SysUserTenantRel::getCreateTime);
        IPage<SysUserTenantRel> relPage = this.page(pageParam, wrapper);
        // 通过转换器将实体分页转换为通用VO分页
        return sysUserTenantRelConverter.toVOPage(relPage);
    }

    @Override
    public IPage<SysUserTenantRelCommonVO> queryUserTenantRel(SysUserTenantRelQueryRTO queryParam) {
        // 构建分页参数
        Page<SysUserTenantRel> pageParam = new Page<>(queryParam.getPageNum(), queryParam.getPageSize());
        LambdaQueryWrapper<SysUserTenantRel> wrapper = new LambdaQueryWrapper<>();

        // 用户ID精确查询
        if (StringUtils.isNotBlank(queryParam.getUserId())) {
            wrapper.eq(SysUserTenantRel::getUserId, Long.parseLong(queryParam.getUserId()));
        }

        if (StringUtils.isNotBlank(queryParam.getTenantId())) {
            wrapper.eq(SysUserTenantRel::getTenantId, Long.parseLong(queryParam.getTenantId()));
        }

        // 是否租户管理员条件查询
        wrapper.eq(queryParam.getIsAdmin() != null,
                SysUserTenantRel::getIsAdmin, queryParam.getIsAdmin());

        // 加入时间范围查询
        TimeRangeCommonRTO joinTime = queryParam.getJoinTime();
        if (joinTime != null) {
            LocalDateTime joinStartTime = joinTime.getStartTime();
            LocalDateTime joinEndTime = joinTime.getEndTime();
            // 校验时间范围合法性
            if (joinStartTime != null && joinEndTime != null && joinStartTime.isAfter(joinEndTime)) {
                throw new BusinessException(400, "加入时间范围不合法，开始时间不能晚于结束时间");
            }
            if (joinStartTime != null && joinEndTime != null) {
                wrapper.between(SysUserTenantRel::getJoinTime, joinStartTime, joinEndTime);
            } else if (joinStartTime != null) {
                wrapper.ge(SysUserTenantRel::getJoinTime, joinStartTime);
            } else if (joinEndTime != null) {
                wrapper.le(SysUserTenantRel::getJoinTime, joinEndTime);
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
                wrapper.between(SysUserTenantRel::getCreateTime, createStartTime, createEndTime);
            } else if (createStartTime != null) {
                wrapper.ge(SysUserTenantRel::getCreateTime, createStartTime);
            } else if (createEndTime != null) {
                wrapper.le(SysUserTenantRel::getCreateTime, createEndTime);
            }
        }

        // 逻辑删除条件：仅查询未删除的记录
        wrapper.eq(SysUserTenantRel::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        // 按创建时间降序排列
        wrapper.orderByDesc(SysUserTenantRel::getCreateTime);

        IPage<SysUserTenantRel> relQueryPage = this.page(pageParam, wrapper);
        // 通过转换器将实体分页转换为通用VO分页
        return sysUserTenantRelConverter.toVOPage(relQueryPage);
    }

    @Override
    public SysUserTenantRelDetailVO queryUserTenantRelDetail(String id) {
        // 参数校验
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(400, "关联记录ID不能为空");
        }
        // 根据ID查询未删除的关联记录
        LambdaQueryWrapper<SysUserTenantRel> wrapper = new LambdaQueryWrapper<SysUserTenantRel>()
                .eq(SysUserTenantRel::getId, Long.parseLong(id))
                .eq(SysUserTenantRel::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysUserTenantRel relDetail = this.getOne(wrapper);
        if (relDetail == null) {
            throw new BusinessException(404, "用户租户关联记录不存在");
        }
        // 通过转换器将实体转换为详情VO
        return sysUserTenantRelConverter.toDetailVO(relDetail);
    }

    @Override
    public Integer addUserTenantRel(SysUserTenantRelAddRTO addParam) {
        // 校验同一用户不能重复绑定同一租户
        LambdaQueryWrapper<SysUserTenantRel> wrapper = new LambdaQueryWrapper<SysUserTenantRel>()
                .eq(SysUserTenantRel::getUserId, Long.parseLong(addParam.getUserId()))
                .eq(SysUserTenantRel::getTenantId, Long.parseLong(addParam.getTenantId()))
                .eq(SysUserTenantRel::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        long count = this.count(wrapper);
        if (count > 0) {
            throw new BusinessException(400, "该用户已绑定此租户");
        }
        // 通过转换器将RTO转换为实体并保存
        boolean result = this.save(sysUserTenantRelConverter.toEntityAdd(addParam));
        if (!result) {
            throw new BusinessException(500, "新增用户租户关联失败");
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateUserTenantRel(SysUserTenantRelUpdateRTO updateParam) {
        // 查询待更新的关联记录是否存在
        LambdaQueryWrapper<SysUserTenantRel> wrapper = new LambdaQueryWrapper<SysUserTenantRel>()
                .eq(SysUserTenantRel::getId, updateParam.getId())
                .eq(SysUserTenantRel::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysUserTenantRel existRel = this.getOne(wrapper);
        if (existRel == null) {
            throw new BusinessException(404, "用户租户关联记录不存在");
        }
        // 校验用户+租户组合是否重复（排除自身）
        boolean userChanged = !existRel.getUserId().equals(Long.parseLong(updateParam.getUserId()));
        boolean tenantChanged = !existRel.getTenantId().equals(Long.parseLong(updateParam.getTenantId()));
        if (userChanged || tenantChanged) {
            LambdaQueryWrapper<SysUserTenantRel> dupWrapper = new LambdaQueryWrapper<SysUserTenantRel>()
                    .eq(SysUserTenantRel::getUserId, Long.parseLong(updateParam.getUserId()))
                    .eq(SysUserTenantRel::getTenantId, Long.parseLong(updateParam.getTenantId()))
                    .eq(SysUserTenantRel::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                    .ne(SysUserTenantRel::getId, updateParam.getId());
            long dupCount = this.count(dupWrapper);
            if (dupCount > 0) {
                throw new BusinessException(400, "该用户已绑定此租户");
            }
        }
        // 通过转换器将RTO转换为实体并更新
        SysUserTenantRel rel = sysUserTenantRelConverter.toEntityUpdate(updateParam);
        boolean result = this.updateById(rel);
        if (!result) {
            throw new BusinessException(500, "修改用户租户关联失败");
        }
        return 1;
    }

    @Override
    public Integer deleteUserTenantRel(String id) {
        // 查询关联记录是否存在且未删除
        LambdaQueryWrapper<SysUserTenantRel> wrapper = new LambdaQueryWrapper<SysUserTenantRel>()
                .eq(SysUserTenantRel::getId, id)
                .eq(SysUserTenantRel::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysUserTenantRel existRel = this.getOne(wrapper);
        if (existRel == null) {
            throw new BusinessException(400, "用户租户关联记录不存在");
        }
        // 执行逻辑删除：将isDeleted标记为已删除
        SysUserTenantRel rel = new SysUserTenantRel();
        rel.setId(Long.parseLong(id));
        rel.setIsDeleted(GlobalEnum.Deleted.DELETED.getCode());
        boolean result = this.updateById(rel);
        if (!result) {
            throw new BusinessException(500, "删除用户租户关联失败");
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchAddUserTenantRel(List<SysUserTenantRelAddRTO> addParamList) {
        // 校验批量新增数量上限
        if (addParamList.size() > 100) {
            throw new BusinessException(400, "单次批量新增数量不能超过100条");
        }
        // 校验批量新增中是否存在重复的用户+租户组合
        Set<String> combinationSet = new HashSet<>();
        for (SysUserTenantRelAddRTO param : addParamList) {
            String combination = param.getUserId() + ":" + param.getTenantId();
            if (combinationSet.contains(combination)) {
                throw new BusinessException(400, "批量新增中存在重复的用户+租户组合");
            }
            combinationSet.add(combination);
        }
        // 通过转换器批量转换并保存
        boolean batch = this.saveBatch(sysUserTenantRelConverter.toEntityListAdd(addParamList));
        if (!batch) {
            throw new BusinessException(500, "批量新增用户租户关联失败");
        }
        return addParamList.size();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdateUserTenantRel(List<SysUserTenantRelUpdateRTO> updateParamList) {
        // 校验批量修改数量上限
        if (updateParamList.size() > 100) {
            throw new BusinessException(400, "单次批量修改数量不能超过100条");
        }
        // 校验批量修改中ID不为空且不重复
        Set<Long> idSet = new HashSet<>();
        for (SysUserTenantRelUpdateRTO item : updateParamList) {
            if (item.getId() == null) {
                throw new BusinessException(400, "批量修改中存在ID为空的记录");
            }
            if (idSet.contains(item.getId())) {
                throw new BusinessException(400, "批量修改中存在重复的关联记录ID: " + item.getId());
            }
            idSet.add(item.getId());
        }
        // 校验所有ID对应的记录存在且未删除
        LambdaQueryWrapper<SysUserTenantRel> idWrapper = new LambdaQueryWrapper<SysUserTenantRel>()
                .in(SysUserTenantRel::getId, idSet)
                .eq(SysUserTenantRel::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        long existIdCount = this.count(idWrapper);
        if (existIdCount != idSet.size()) {
            throw new BusinessException(400, "部分关联记录ID不存在或已删除，请检查后重试");
        }
        // 通过转换器批量转换并更新
        boolean batch = this.updateBatchById(sysUserTenantRelConverter.toEntityListUpdate(updateParamList));
        if (!batch) {
            throw new BusinessException(500, "批量更新用户租户关联失败");
        }
        return updateParamList.size();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchDeleteUserTenantRel(List<String> ids) {
        // 校验批量删除数量上限
        if (ids.size() > 100) {
            throw new BusinessException(400, "单次批量删除数量不能超过100条");
        }
        // 收集并去重ID集合
        Set<Long> idSet = new HashSet<>();
        for (String id : ids) {
            if (StringUtils.isBlank(id)) {
                throw new BusinessException(400, "关联记录ID不能为空");
            }
            idSet.add(Long.parseLong(id));
        }
        // 查询所有存在的未删除关联记录
        LambdaQueryWrapper<SysUserTenantRel> existWrapper = new LambdaQueryWrapper<SysUserTenantRel>()
                .in(SysUserTenantRel::getId, idSet)
                .eq(SysUserTenantRel::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        List<SysUserTenantRel> existRels = this.list(existWrapper);
        if (existRels.isEmpty()) {
            throw new BusinessException(404, "未找到可删除的关联记录");
        }
        // 构建逻辑删除更新列表
        List<SysUserTenantRel> relList = new ArrayList<>();
        for (SysUserTenantRel rel : existRels) {
            SysUserTenantRel deleteRel = new SysUserTenantRel();
            deleteRel.setId(rel.getId());
            deleteRel.setIsDeleted(GlobalEnum.Deleted.DELETED.getCode());
            relList.add(deleteRel);
        }
        // 批量执行逻辑删除
        boolean batch = this.updateBatchById(relList);
        if (!batch) {
            throw new BusinessException(500, "批量删除用户租户关联失败");
        }
        return ids.size();
    }

}
