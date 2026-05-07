package com.shy.nexusix.iam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.common.rto.TimeRangeCommonRTO;
import com.shy.nexusix.iam.converter.SysUserRoleRelConverter;
import com.shy.nexusix.iam.entity.SysUserRoleRel;
import com.shy.nexusix.iam.mapper.SysUserRoleRelMapper;
import com.shy.nexusix.iam.rto.SysUserRoleRelAddRTO;
import com.shy.nexusix.iam.rto.SysUserRoleRelQueryRTO;
import com.shy.nexusix.iam.rto.SysUserRoleRelUpdateRTO;
import com.shy.nexusix.iam.service.ISysUserRoleRelService;
import com.shy.nexusix.iam.vo.SysUserRoleRelCommonVO;
import com.shy.nexusix.iam.vo.SysUserRoleRelDetailVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 用户角色关联表 - 用户与角色的绑定关系 服务实现类
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Service
public class SysUserRoleRelServiceImpl extends ServiceImpl<SysUserRoleRelMapper, SysUserRoleRel> implements ISysUserRoleRelService {

    @Autowired
    private SysUserRoleRelConverter sysUserRoleRelConverter;

    @Override
    public List<SysUserRoleRelCommonVO> queryUserRoleRelList() {
        // 构建查询条件：仅查询未删除的记录，按创建时间降序排列
        LambdaQueryWrapper<SysUserRoleRel> wrapper = new LambdaQueryWrapper<SysUserRoleRel>()
                .eq(SysUserRoleRel::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .orderByDesc(SysUserRoleRel::getCreateTime);
        List<SysUserRoleRel> relList = this.list(wrapper);
        // 通过转换器将实体列表转换为通用VO列表
        return sysUserRoleRelConverter.toVoList(relList);
    }

    @Override
    public IPage<SysUserRoleRelCommonVO> queryUserRoleRelPage(PageCommonRTO page) {
        // 构建分页参数
        Page<SysUserRoleRel> pageParam = new Page<>(page.getPageNum(), page.getPageSize());
        // 构建查询条件：仅查询未删除的记录，按创建时间降序排列
        LambdaQueryWrapper<SysUserRoleRel> wrapper = new LambdaQueryWrapper<SysUserRoleRel>()
                .eq(SysUserRoleRel::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .orderByDesc(SysUserRoleRel::getCreateTime);
        IPage<SysUserRoleRel> relPage = this.page(pageParam, wrapper);
        // 通过转换器将实体分页转换为通用VO分页
        return sysUserRoleRelConverter.toVOPage(relPage);
    }

    @Override
    public IPage<SysUserRoleRelCommonVO> queryUserRoleRel(SysUserRoleRelQueryRTO queryParam) {
        // 构建分页参数
        Page<SysUserRoleRel> pageParam = new Page<>(queryParam.getPageNum(), queryParam.getPageSize());
        LambdaQueryWrapper<SysUserRoleRel> wrapper = new LambdaQueryWrapper<>();

        // 用户ID精确查询
        wrapper.eq(StringUtils.isNotBlank(queryParam.getUserId()),
                SysUserRoleRel::getUserId, Long.parseLong(queryParam.getUserId()));

        // 角色ID精确查询
        wrapper.eq(StringUtils.isNotBlank(queryParam.getRoleId()),
                SysUserRoleRel::getRoleId, Long.parseLong(queryParam.getRoleId()));

        // 租户ID精确查询
        wrapper.eq(StringUtils.isNotBlank(queryParam.getTenantId()),
                SysUserRoleRel::getTenantId, Long.parseLong(queryParam.getTenantId()));

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
                wrapper.between(SysUserRoleRel::getCreateTime, createStartTime, createEndTime);
            } else if (createStartTime != null) {
                wrapper.ge(SysUserRoleRel::getCreateTime, createStartTime);
            } else if (createEndTime != null) {
                wrapper.le(SysUserRoleRel::getCreateTime, createEndTime);
            }
        }

        // 逻辑删除条件：仅查询未删除的记录
        wrapper.eq(SysUserRoleRel::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        // 按创建时间降序排列
        wrapper.orderByDesc(SysUserRoleRel::getCreateTime);

        IPage<SysUserRoleRel> relQueryPage = this.page(pageParam, wrapper);
        // 通过转换器将实体分页转换为通用VO分页
        return sysUserRoleRelConverter.toVOPage(relQueryPage);
    }

    @Override
    public SysUserRoleRelDetailVO queryUserRoleRelDetail(String id) {
        // 参数校验
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(400, "关联记录ID不能为空");
        }
        // 根据ID查询未删除的关联记录
        LambdaQueryWrapper<SysUserRoleRel> wrapper = new LambdaQueryWrapper<SysUserRoleRel>()
                .eq(SysUserRoleRel::getId, Long.parseLong(id))
                .eq(SysUserRoleRel::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysUserRoleRel relDetail = this.getOne(wrapper);
        if (relDetail == null) {
            throw new BusinessException(404, "用户角色关联记录不存在");
        }
        // 通过转换器将实体转换为详情VO
        return sysUserRoleRelConverter.toDetailVO(relDetail);
    }

    @Override
    public Integer addUserRoleRel(SysUserRoleRelAddRTO addParam) {
        // 校验同一用户在同一租户下不能重复绑定同一角色
        LambdaQueryWrapper<SysUserRoleRel> wrapper = new LambdaQueryWrapper<SysUserRoleRel>()
                .eq(SysUserRoleRel::getUserId, Long.parseLong(addParam.getUserId()))
                .eq(SysUserRoleRel::getRoleId, Long.parseLong(addParam.getRoleId()))
                .eq(SysUserRoleRel::getTenantId, Long.parseLong(addParam.getTenantId()))
                .eq(SysUserRoleRel::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        long count = this.count(wrapper);
        if (count > 0) {
            throw new BusinessException(400, "该用户在此租户下已绑定此角色");
        }
        // 通过转换器将RTO转换为实体并保存
        boolean result = this.save(sysUserRoleRelConverter.toEntityAdd(addParam));
        if (!result) {
            throw new BusinessException(500, "新增用户角色关联失败");
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateUserRoleRel(SysUserRoleRelUpdateRTO updateParam) {
        // 查询待更新的关联记录是否存在
        LambdaQueryWrapper<SysUserRoleRel> wrapper = new LambdaQueryWrapper<SysUserRoleRel>()
                .eq(SysUserRoleRel::getId, updateParam.getId())
                .eq(SysUserRoleRel::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysUserRoleRel existRel = this.getOne(wrapper);
        if (existRel == null) {
            throw new BusinessException(404, "用户角色关联记录不存在");
        }
        // 校验用户+角色+租户组合是否重复（排除自身）
        boolean userChanged = !existRel.getUserId().equals(Long.parseLong(updateParam.getUserId()));
        boolean roleChanged = !existRel.getRoleId().equals(Long.parseLong(updateParam.getRoleId()));
        boolean tenantChanged = !existRel.getTenantId().equals(Long.parseLong(updateParam.getTenantId()));
        if (userChanged || roleChanged || tenantChanged) {
            LambdaQueryWrapper<SysUserRoleRel> dupWrapper = new LambdaQueryWrapper<SysUserRoleRel>()
                    .eq(SysUserRoleRel::getUserId, Long.parseLong(updateParam.getUserId()))
                    .eq(SysUserRoleRel::getRoleId, Long.parseLong(updateParam.getRoleId()))
                    .eq(SysUserRoleRel::getTenantId, Long.parseLong(updateParam.getTenantId()))
                    .eq(SysUserRoleRel::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                    .ne(SysUserRoleRel::getId, updateParam.getId());
            long dupCount = this.count(dupWrapper);
            if (dupCount > 0) {
                throw new BusinessException(400, "该用户在此租户下已绑定此角色");
            }
        }
        // 通过转换器将RTO转换为实体并更新
        SysUserRoleRel rel = sysUserRoleRelConverter.toEntityUpdate(updateParam);
        boolean result = this.updateById(rel);
        if (!result) {
            throw new BusinessException(500, "修改用户角色关联失败");
        }
        return 1;
    }

    @Override
    public Integer deleteUserRoleRel(String id) {
        // 查询关联记录是否存在且未删除
        LambdaQueryWrapper<SysUserRoleRel> wrapper = new LambdaQueryWrapper<SysUserRoleRel>()
                .eq(SysUserRoleRel::getId, id)
                .eq(SysUserRoleRel::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysUserRoleRel existRel = this.getOne(wrapper);
        if (existRel == null) {
            throw new BusinessException(400, "用户角色关联记录不存在");
        }
        // 执行逻辑删除：将isDeleted标记为已删除
        SysUserRoleRel rel = new SysUserRoleRel();
        rel.setId(Long.parseLong(id));
        rel.setIsDeleted(GlobalEnum.Deleted.DELETED.getCode());
        boolean result = this.updateById(rel);
        if (!result) {
            throw new BusinessException(500, "删除用户角色关联失败");
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchAddUserRoleRel(List<SysUserRoleRelAddRTO> addParamList) {
        // 校验批量新增数量上限
        if (addParamList.size() > 100) {
            throw new BusinessException(400, "单次批量新增数量不能超过100条");
        }
        // 校验批量新增中是否存在重复的用户+角色+租户组合
        Set<String> combinationSet = new HashSet<>();
        for (SysUserRoleRelAddRTO param : addParamList) {
            String combination = param.getUserId() + ":" + param.getRoleId() + ":" + param.getTenantId();
            if (combinationSet.contains(combination)) {
                throw new BusinessException(400, "批量新增中存在重复的用户+角色+租户组合");
            }
            combinationSet.add(combination);
        }
        // 通过转换器批量转换并保存
        boolean batch = this.saveBatch(sysUserRoleRelConverter.toEntityListAdd(addParamList));
        if (!batch) {
            throw new BusinessException(500, "批量新增用户角色关联失败");
        }
        return addParamList.size();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdateUserRoleRel(List<SysUserRoleRelUpdateRTO> updateParamList) {
        // 校验批量修改数量上限
        if (updateParamList.size() > 100) {
            throw new BusinessException(400, "单次批量修改数量不能超过100条");
        }
        // 校验批量修改中ID不为空且不重复
        Set<Long> idSet = new HashSet<>();
        for (SysUserRoleRelUpdateRTO item : updateParamList) {
            if (item.getId() == null) {
                throw new BusinessException(400, "批量修改中存在ID为空的记录");
            }
            if (idSet.contains(item.getId())) {
                throw new BusinessException(400, "批量修改中存在重复的关联记录ID: " + item.getId());
            }
            idSet.add(item.getId());
        }
        // 校验所有ID对应的记录存在且未删除
        LambdaQueryWrapper<SysUserRoleRel> idWrapper = new LambdaQueryWrapper<SysUserRoleRel>()
                .in(SysUserRoleRel::getId, idSet)
                .eq(SysUserRoleRel::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        long existIdCount = this.count(idWrapper);
        if (existIdCount != idSet.size()) {
            throw new BusinessException(400, "部分关联记录ID不存在或已删除，请检查后重试");
        }
        // 通过转换器批量转换并更新
        boolean batch = this.updateBatchById(sysUserRoleRelConverter.toEntityListUpdate(updateParamList));
        if (!batch) {
            throw new BusinessException(500, "批量更新用户角色关联失败");
        }
        return updateParamList.size();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchDeleteUserRoleRel(List<String> ids) {
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
        LambdaQueryWrapper<SysUserRoleRel> existWrapper = new LambdaQueryWrapper<SysUserRoleRel>()
                .in(SysUserRoleRel::getId, idSet)
                .eq(SysUserRoleRel::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        List<SysUserRoleRel> existRels = this.list(existWrapper);
        if (existRels.isEmpty()) {
            throw new BusinessException(404, "未找到可删除的关联记录");
        }
        // 构建逻辑删除更新列表
        List<SysUserRoleRel> relList = new ArrayList<>();
        for (SysUserRoleRel rel : existRels) {
            SysUserRoleRel deleteRel = new SysUserRoleRel();
            deleteRel.setId(rel.getId());
            deleteRel.setIsDeleted(GlobalEnum.Deleted.DELETED.getCode());
            relList.add(deleteRel);
        }
        // 批量执行逻辑删除
        boolean batch = this.updateBatchById(relList);
        if (!batch) {
            throw new BusinessException(500, "批量删除用户角色关联失败");
        }
        return ids.size();
    }

}
