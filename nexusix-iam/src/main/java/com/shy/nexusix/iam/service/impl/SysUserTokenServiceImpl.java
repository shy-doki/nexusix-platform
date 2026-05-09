package com.shy.nexusix.iam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.enums.GlobalEnum.TokenStatus;
import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.common.rto.TimeRangeCommonRTO;
import com.shy.nexusix.iam.converter.SysUserTokenConverter;
import com.shy.nexusix.iam.entity.SysUserToken;
import com.shy.nexusix.iam.mapper.SysUserTokenMapper;
import com.shy.nexusix.iam.rto.SysUserTokenAddRTO;
import com.shy.nexusix.iam.rto.SysUserTokenQueryRTO;
import com.shy.nexusix.iam.rto.SysUserTokenUpdateRTO;
import com.shy.nexusix.iam.service.ISysUserTokenService;
import com.shy.nexusix.iam.vo.SysUserTokenCommonVO;
import com.shy.nexusix.iam.vo.SysUserTokenDetailVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 用户 Token 记录表 - 用于多端登录管理和强制下线 服务实现类
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Service
public class SysUserTokenServiceImpl extends ServiceImpl<SysUserTokenMapper, SysUserToken> implements ISysUserTokenService {

    @Autowired
    private SysUserTokenConverter sysUserTokenConverter;

    @Override
    public List<SysUserTokenCommonVO> queryTokenList() {
        // 构建查询条件：仅查询未删除的记录，按登录时间降序排列
        LambdaQueryWrapper<SysUserToken> wrapper = new LambdaQueryWrapper<SysUserToken>()
                .eq(SysUserToken::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .orderByDesc(SysUserToken::getLoginTime);
        List<SysUserToken> tokenList = this.list(wrapper);
        // 通过转换器将实体列表转换为通用VO列表
        return sysUserTokenConverter.toVoList(tokenList);
    }

    @Override
    public IPage<SysUserTokenCommonVO> queryTokenPage(PageCommonRTO page) {
        // 构建分页参数
        Page<SysUserToken> pageParam = new Page<>(page.getPageNum(), page.getPageSize());
        // 构建查询条件：仅查询未删除的记录，按登录时间降序排列
        LambdaQueryWrapper<SysUserToken> wrapper = new LambdaQueryWrapper<SysUserToken>()
                .eq(SysUserToken::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .orderByDesc(SysUserToken::getLoginTime);
        IPage<SysUserToken> tokenPage = this.page(pageParam, wrapper);
        // 通过转换器将实体分页转换为通用VO分页
        return sysUserTokenConverter.toVOPage(tokenPage);
    }

    @Override
    public IPage<SysUserTokenCommonVO> queryToken(SysUserTokenQueryRTO queryParam) {
        // 构建分页参数
        Page<SysUserToken> pageParam = new Page<>(queryParam.getPageNum(), queryParam.getPageSize());
        LambdaQueryWrapper<SysUserToken> wrapper = new LambdaQueryWrapper<>();

        // 用户ID精确查询
        if (StringUtils.isNotBlank(queryParam.getUserId())) {
            wrapper.eq(SysUserToken::getUserId, Long.parseLong(queryParam.getUserId()));
        }

        // 用户名称模糊查询
        wrapper.like(StringUtils.isNotBlank(queryParam.getUserName()),
                SysUserToken::getUserName, queryParam.getUserName());

        // 租户ID精确查询
        if (StringUtils.isNotBlank(queryParam.getTenantId())) {
            wrapper.eq(SysUserToken::getTenantId, Long.parseLong(queryParam.getTenantId()));
        }

        // 状态条件查询：通过parse方法支持多种输入格式
        if (StringUtils.isNotBlank(queryParam.getStatus())) {
            TokenStatus tokenStatus = TokenStatus.parse(queryParam.getStatus());
            if (tokenStatus != null) {
                wrapper.eq(SysUserToken::getStatus, tokenStatus.getCode());
            }
        }

        // 最后登录IP模糊查询
        wrapper.like(StringUtils.isNotBlank(queryParam.getLoginIp()),
                SysUserToken::getLoginIp, queryParam.getLoginIp());

        // 登录时间范围查询
        TimeRangeCommonRTO loginTime = queryParam.getLoginTime();
        if (loginTime != null) {
            LocalDateTime loginStartTime = loginTime.getStartTime();
            LocalDateTime loginEndTime = loginTime.getEndTime();
            // 校验时间范围合法性
            if (loginStartTime != null && loginEndTime != null && loginStartTime.isAfter(loginEndTime)) {
                throw new BusinessException(400, "登录时间范围不合法，开始时间不能晚于结束时间");
            }
            if (loginStartTime != null && loginEndTime != null) {
                wrapper.between(SysUserToken::getLoginTime, loginStartTime, loginEndTime);
            } else if (loginStartTime != null) {
                wrapper.ge(SysUserToken::getLoginTime, loginStartTime);
            } else if (loginEndTime != null) {
                wrapper.le(SysUserToken::getLoginTime, loginEndTime);
            }
        }

        // 过期时间范围查询
        TimeRangeCommonRTO expireTime = queryParam.getExpireTime();
        if (expireTime != null) {
            LocalDateTime expireStartTime = expireTime.getStartTime();
            LocalDateTime expireEndTime = expireTime.getEndTime();
            // 校验时间范围合法性
            if (expireStartTime != null && expireEndTime != null && expireStartTime.isAfter(expireEndTime)) {
                throw new BusinessException(400, "过期时间范围不合法，开始时间不能晚于结束时间");
            }
            if (expireStartTime != null && expireEndTime != null) {
                wrapper.between(SysUserToken::getExpireTime, expireStartTime, expireEndTime);
            } else if (expireStartTime != null) {
                wrapper.ge(SysUserToken::getExpireTime, expireStartTime);
            } else if (expireEndTime != null) {
                wrapper.le(SysUserToken::getExpireTime, expireEndTime);
            }
        }

        // 逻辑删除条件：仅查询未删除的记录
        wrapper.eq(SysUserToken::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        // 按登录时间降序排列
        wrapper.orderByDesc(SysUserToken::getLoginTime);

        IPage<SysUserToken> tokenQueryPage = this.page(pageParam, wrapper);
        // 通过转换器将实体分页转换为通用VO分页
        return sysUserTokenConverter.toVOPage(tokenQueryPage);
    }

    @Override
    public SysUserTokenDetailVO queryTokenDetail(String id) {
        // 参数校验
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(400, "Token记录ID不能为空");
        }
        // 根据ID查询未删除的Token记录
        LambdaQueryWrapper<SysUserToken> wrapper = new LambdaQueryWrapper<SysUserToken>()
                .eq(SysUserToken::getId, Long.parseLong(id))
                .eq(SysUserToken::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysUserToken tokenDetail = this.getOne(wrapper);
        if (tokenDetail == null) {
            throw new BusinessException(404, "Token记录不存在");
        }
        // 通过转换器将实体转换为详情VO
        return sysUserTokenConverter.toDetailVO(tokenDetail);
    }

    @Override
    public Integer addToken(SysUserTokenAddRTO addParam) {
        // 校验同一用户在同一租户下不能有重复的有效Token
        LambdaQueryWrapper<SysUserToken> wrapper = new LambdaQueryWrapper<SysUserToken>()
                .eq(SysUserToken::getUserId, Long.parseLong(addParam.getUserId()))
                .eq(SysUserToken::getTenantId, Long.parseLong(addParam.getTenantId()))
                .eq(SysUserToken::getToken, addParam.getToken())
                .eq(SysUserToken::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        long count = this.count(wrapper);
        if (count > 0) {
            throw new BusinessException(400, "该用户在此租户下已存在相同的Token记录");
        }
        // 通过转换器将RTO转换为实体并保存
        boolean result = this.save(sysUserTokenConverter.toEntityAdd(addParam));
        if (!result) {
            throw new BusinessException(500, "新增Token记录失败");
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateToken(SysUserTokenUpdateRTO updateParam) {
        // 查询待更新的Token记录是否存在
        LambdaQueryWrapper<SysUserToken> wrapper = new LambdaQueryWrapper<SysUserToken>()
                .eq(SysUserToken::getId, updateParam.getId())
                .eq(SysUserToken::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysUserToken existToken = this.getOne(wrapper);
        if (existToken == null) {
            throw new BusinessException(404, "Token记录不存在");
        }
        // 校验用户+租户+Token组合是否重复（排除自身）
        boolean userChanged = !existToken.getUserId().equals(Long.parseLong(updateParam.getUserId()));
        boolean tenantChanged = !existToken.getTenantId().equals(Long.parseLong(updateParam.getTenantId()));
        boolean tokenChanged = !existToken.getToken().equals(updateParam.getToken());
        if (userChanged || tenantChanged || tokenChanged) {
            LambdaQueryWrapper<SysUserToken> dupWrapper = new LambdaQueryWrapper<SysUserToken>()
                    .eq(SysUserToken::getUserId, Long.parseLong(updateParam.getUserId()))
                    .eq(SysUserToken::getTenantId, Long.parseLong(updateParam.getTenantId()))
                    .eq(SysUserToken::getToken, updateParam.getToken())
                    .eq(SysUserToken::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                    .ne(SysUserToken::getId, updateParam.getId());
            long dupCount = this.count(dupWrapper);
            if (dupCount > 0) {
                throw new BusinessException(400, "该用户在此租户下已存在相同的Token记录");
            }
        }
        // 通过转换器将RTO转换为实体并更新
        SysUserToken token = sysUserTokenConverter.toEntityUpdate(updateParam);
        boolean result = this.updateById(token);
        if (!result) {
            throw new BusinessException(500, "修改Token记录失败");
        }
        return 1;
    }

    @Override
    public Integer updateTokenStatus(String id, String status) {
        // 参数校验
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(400, "Token记录ID不能为空");
        }
        if (StringUtils.isBlank(status)) {
            throw new BusinessException(400, "状态不能为空");
        }
        // 通过parse方法解析状态值，支持多种输入格式
        TokenStatus tokenStatus = TokenStatus.parse(status);
        if (tokenStatus == null) {
            throw new BusinessException(400, "状态值不合法，仅支持：有效、无效");
        }
        // 查询Token记录是否存在
        LambdaQueryWrapper<SysUserToken> wrapper = new LambdaQueryWrapper<SysUserToken>()
                .eq(SysUserToken::getId, Long.parseLong(id))
                .eq(SysUserToken::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysUserToken existToken = this.getOne(wrapper);
        if (existToken == null) {
            throw new BusinessException(400, "Token记录不存在");
        }
        // 校验状态是否发生变化
        if (tokenStatus.getCode().equals(existToken.getStatus())) {
            throw new BusinessException(400, "Token状态未变更");
        }
        // 构建更新对象并更新状态
        SysUserToken updateToken = new SysUserToken();
        updateToken.setId(Long.parseLong(id));
        updateToken.setStatus(tokenStatus.getCode());
        boolean result = this.updateById(updateToken);
        if (!result) {
            throw new BusinessException(500, "更新Token状态失败");
        }
        return 1;
    }

    @Override
    public Integer deleteToken(String id) {
        // 查询Token记录是否存在且未删除
        LambdaQueryWrapper<SysUserToken> wrapper = new LambdaQueryWrapper<SysUserToken>()
                .eq(SysUserToken::getId, id)
                .eq(SysUserToken::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysUserToken existToken = this.getOne(wrapper);
        if (existToken == null) {
            throw new BusinessException(400, "Token记录不存在");
        }
        // 执行逻辑删除：将isDeleted标记为已删除
        SysUserToken token = new SysUserToken();
        token.setId(Long.parseLong(id));
        token.setIsDeleted(GlobalEnum.Deleted.DELETED.getCode());
        boolean result = this.updateById(token);
        if (!result) {
            throw new BusinessException(500, "删除Token记录失败");
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchAddToken(List<SysUserTokenAddRTO> addParamList) {
        // 校验批量新增数量上限
        if (addParamList.size() > 100) {
            throw new BusinessException(400, "单次批量新增数量不能超过100条");
        }
        // 校验批量新增中是否存在重复的用户+租户+Token组合
        Set<String> combinationSet = new HashSet<>();
        for (SysUserTokenAddRTO param : addParamList) {
            String combination = param.getUserId() + ":" + param.getTenantId() + ":" + param.getToken();
            if (combinationSet.contains(combination)) {
                throw new BusinessException(400, "批量新增中存在重复的用户+租户+Token组合");
            }
            combinationSet.add(combination);
        }
        // 通过转换器批量转换并保存
        boolean batch = this.saveBatch(sysUserTokenConverter.toEntityListAdd(addParamList));
        if (!batch) {
            throw new BusinessException(500, "批量新增Token记录失败");
        }
        return addParamList.size();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdateToken(List<SysUserTokenUpdateRTO> updateParamList) {
        // 校验批量修改数量上限
        if (updateParamList.size() > 100) {
            throw new BusinessException(400, "单次批量修改数量不能超过100条");
        }
        // 校验批量修改中ID不为空且不重复
        Set<Long> idSet = new HashSet<>();
        for (SysUserTokenUpdateRTO item : updateParamList) {
            if (item.getId() == null) {
                throw new BusinessException(400, "批量修改中存在ID为空的记录");
            }
            if (idSet.contains(item.getId())) {
                throw new BusinessException(400, "批量修改中存在重复的Token记录ID: " + item.getId());
            }
            idSet.add(item.getId());
        }
        // 校验所有ID对应的记录存在且未删除
        LambdaQueryWrapper<SysUserToken> idWrapper = new LambdaQueryWrapper<SysUserToken>()
                .in(SysUserToken::getId, idSet)
                .eq(SysUserToken::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        long existIdCount = this.count(idWrapper);
        if (existIdCount != idSet.size()) {
            throw new BusinessException(400, "部分Token记录ID不存在或已删除，请检查后重试");
        }
        // 通过转换器批量转换并更新
        boolean batch = this.updateBatchById(sysUserTokenConverter.toEntityListUpdate(updateParamList));
        if (!batch) {
            throw new BusinessException(500, "批量更新Token记录失败");
        }
        return updateParamList.size();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdateTokenStatus(List<String> ids, String status) {
        // 校验批量更新数量上限
        if (ids.size() > 100) {
            throw new BusinessException(400, "单次批量更新数量不能超过100条");
        }
        if (StringUtils.isBlank(status)) {
            throw new BusinessException(400, "状态不能为空");
        }
        // 通过parse方法解析状态值
        TokenStatus tokenStatus = TokenStatus.parse(status);
        if (tokenStatus == null) {
            throw new BusinessException(400, "状态值不合法，仅支持：有效、无效");
        }
        // 收集并去重ID集合
        Set<Long> idSet = new HashSet<>();
        for (String id : ids) {
            if (StringUtils.isBlank(id)) {
                throw new BusinessException(400, "Token记录ID不能为空");
            }
            idSet.add(Long.parseLong(id));
        }
        // 查询所有存在的未删除Token记录
        LambdaQueryWrapper<SysUserToken> existWrapper = new LambdaQueryWrapper<SysUserToken>()
                .in(SysUserToken::getId, idSet)
                .eq(SysUserToken::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        List<SysUserToken> existTokens = this.list(existWrapper);
        if (existTokens.isEmpty()) {
            throw new BusinessException(404, "未找到可更新状态的Token记录");
        }
        // 过滤出状态需要变更的Token记录
        List<SysUserToken> updateList = new ArrayList<>();
        for (SysUserToken token : existTokens) {
            // 跳过状态未变更的记录
            if (tokenStatus.getCode().equals(token.getStatus())) {
                continue;
            }
            SysUserToken updateToken = new SysUserToken();
            updateToken.setId(token.getId());
            updateToken.setStatus(tokenStatus.getCode());
            updateList.add(updateToken);
        }
        if (updateList.isEmpty()) {
            throw new BusinessException(400, "所有Token状态均未变更");
        }
        // 批量更新状态
        boolean batch = this.updateBatchById(updateList);
        if (!batch) {
            throw new BusinessException(500, "批量更新Token状态失败");
        }
        return updateList.size();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchDeleteToken(List<String> ids) {
        // 校验批量删除数量上限
        if (ids.size() > 100) {
            throw new BusinessException(400, "单次批量删除数量不能超过100条");
        }
        // 收集并去重ID集合
        Set<Long> idSet = new HashSet<>();
        for (String id : ids) {
            if (StringUtils.isBlank(id)) {
                throw new BusinessException(400, "Token记录ID不能为空");
            }
            idSet.add(Long.parseLong(id));
        }
        // 查询所有存在的未删除Token记录
        LambdaQueryWrapper<SysUserToken> existWrapper = new LambdaQueryWrapper<SysUserToken>()
                .in(SysUserToken::getId, idSet)
                .eq(SysUserToken::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        List<SysUserToken> existTokens = this.list(existWrapper);
        if (existTokens.isEmpty()) {
            throw new BusinessException(404, "未找到可删除的Token记录");
        }
        // 构建逻辑删除更新列表
        List<SysUserToken> tokenList = new ArrayList<>();
        for (SysUserToken token : existTokens) {
            SysUserToken deleteToken = new SysUserToken();
            deleteToken.setId(token.getId());
            deleteToken.setIsDeleted(GlobalEnum.Deleted.DELETED.getCode());
            tokenList.add(deleteToken);
        }
        // 批量执行逻辑删除
        boolean batch = this.updateBatchById(tokenList);
        if (!batch) {
            throw new BusinessException(500, "批量删除Token记录失败");
        }
        return ids.size();
    }

}
