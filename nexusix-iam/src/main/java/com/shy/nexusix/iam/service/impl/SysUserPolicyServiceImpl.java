package com.shy.nexusix.iam.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shy.nexusix.common.constant.GlobalConstant;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.common.rto.TimeRangeCommonRTO;
import com.shy.nexusix.core.context.UserContext;
import com.shy.nexusix.core.entity.dto.UserContextDTO;
import com.shy.nexusix.iam.converter.SysUserPolicyConverter;
import com.shy.nexusix.iam.entity.SysUserPolicy;
import com.shy.nexusix.iam.mapper.SysUserPolicyMapper;
import com.shy.nexusix.iam.rto.SysUserPolicyAddRTO;
import com.shy.nexusix.iam.rto.SysUserPolicyBatchBindRTO;
import com.shy.nexusix.iam.rto.SysUserPolicyBatchUnbindRTO;
import com.shy.nexusix.iam.rto.SysUserPolicyQueryRTO;
import com.shy.nexusix.iam.rto.SysUserPolicyUpdateRTO;
import com.shy.nexusix.iam.service.ISysUserPolicyService;
import com.shy.nexusix.iam.vo.SysUserPolicyCommonVO;
import com.shy.nexusix.iam.vo.SysUserPolicyDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>用户策略服务实现类</p>
 *
 * @author shy
 */
@Service
public class SysUserPolicyServiceImpl extends ServiceImpl<SysUserPolicyMapper, SysUserPolicy> implements ISysUserPolicyService {

    @Autowired
    private SysUserPolicyConverter sysUserPolicyConverter;

    /**
     * <p>获取查询操作的可操作字段</p>
     *
     * @return 可操作字段列表，null表示无限制
     */
    private List<String> getQueryOperableFields() {
        // 直接从UserContext获取字段权限
        UserContextDTO userContext = UserContext.getUserContext();
        if (userContext == null) {
            return null;
        }

        UserContextDTO.FieldPermission fieldPerm =
            com.shy.nexusix.core.context.UserContext.getCurrentTenantFieldPermission();

        if (fieldPerm == null || fieldPerm.getQuery() == null) {
            return null;
        }

        UserContextDTO.TableFieldPermission tableFieldPerm =
            fieldPerm.getQuery().get(GlobalConstant.Table.USER_POLICY);

        if (tableFieldPerm == null || tableFieldPerm.getOperable() == null
            || tableFieldPerm.getOperable().isEmpty()) {
            return null;  // null表示无限制
        }

        return tableFieldPerm.getOperable();
    }

    /**
     * <p>获取指定操作类型的字段权限</p>
     *
     * @param operationType 操作类型："query"、"create"、"update"
     * @return 可操作字段列表，null表示无权限或无限制
     * @throws BusinessException 用户上下文为空或操作类型不支持时抛出
     */
    private List<String> getTableFieldPermission(String operationType) {
        UserContextDTO userContext = UserContext.getUserContext();
        if (userContext == null) {
            throw new BusinessException("无法获取用户上下文");
        }

        UserContextDTO.FieldPermission fieldPerm = com.shy.nexusix.core.context.UserContext.getCurrentTenantFieldPermission();
        if (fieldPerm == null) {
            return null;
        }

        UserContextDTO.TableFieldPermission tableFieldPerm = null;
        if ("query".equalsIgnoreCase(operationType)) {
            if (fieldPerm.getQuery() != null) {
                tableFieldPerm = fieldPerm.getQuery().get(GlobalConstant.Table.USER_POLICY);
            }
        } else if ("create".equalsIgnoreCase(operationType)) {
            if (fieldPerm.getCreate() != null) {
                tableFieldPerm = fieldPerm.getCreate().get(GlobalConstant.Table.USER_POLICY);
            }
        } else if ("update".equalsIgnoreCase(operationType)) {
            if (fieldPerm.getUpdate() != null) {
                tableFieldPerm = fieldPerm.getUpdate().get(GlobalConstant.Table.USER_POLICY);
            }
        } else {
            throw new BusinessException("不支持的操作类型: " + operationType);
        }

        if (tableFieldPerm == null || tableFieldPerm.getOperable() == null) {
            return null;
        }

        return tableFieldPerm.getOperable();
    }

    /**
     * <p>查询用户策略列表</p>
     *
     * @return 用户策略通用VO列表
     * @throws BusinessException 用户上下文为空、字段权限配置异常时抛出业务异常
     */
    @Override
    public List<SysUserPolicyCommonVO> queryPolicyList() {

        // 使用工具类获取字段权限
        List<String> visibleFields = getQueryOperableFields();

        // 构建查询条件 仅选择用户有权限查看的列，并排除已删除的记录
        LambdaQueryWrapper<SysUserPolicy> wrapper = new LambdaQueryWrapper<SysUserPolicy>();

        // 如果有字段级权限限制，则只选择可操作字段
        if (visibleFields != null && !visibleFields.isEmpty()) {
            wrapper.select(SysUserPolicy.class, entity -> visibleFields.contains(entity.getColumn()));
        }

        wrapper.eq(SysUserPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());

        List<SysUserPolicy> policyList = this.list(wrapper);
        // 通过 MapStruct 转换器将实体列表转换为 VO 列表
        return sysUserPolicyConverter.entityListToCommonVoList(policyList);

    }

    /**
     * <p>分页查询用户策略列表</p>
     *
     * @param page 分页参数，包含页码和每页数量
     * @return 分页后的用户策略通用VO列表
     * @throws BusinessException 用户上下文为空、字段权限配置异常时抛出业务异常
     */
    @Override
    public IPage<SysUserPolicyCommonVO> queryPolicyPage(PageCommonRTO page) {

        // 使用工具类获取字段权限
        List<String> visibleFields = getQueryOperableFields();

        // 构建分页查询条件 仅选择用户有权限查看的列，并排除已删除的记录
        LambdaQueryWrapper<SysUserPolicy> wrapper = new LambdaQueryWrapper<SysUserPolicy>();

        // 如果有字段级权限限制，则只选择可操作字段
        if (visibleFields != null && !visibleFields.isEmpty()) {
            wrapper.select(SysUserPolicy.class, entity -> visibleFields.contains(entity.getColumn()));
        }

        wrapper.eq(SysUserPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());

        // 执行分页查询
        IPage<SysUserPolicy> entityPage = this.page(new Page<>(page.getPageNum(), page.getPageSize()), wrapper);

        // 构建VO分页对象 保留原始分页信息
        IPage<SysUserPolicyCommonVO> voPage = new Page<>(entityPage.getCurrent(), entityPage.getSize(), entityPage.getTotal());
        // 通过 MapStruct 转换器将实体分页记录转换为 VO 列表
        voPage.setRecords(sysUserPolicyConverter.entityListToCommonVoList(entityPage.getRecords()));
        return voPage;

    }

    /**
     * <p>条件查询用户策略列表</p>
     *
     * @param queryParam 查询条件，包含策略编码、名称、目标类型、用户ID、目标ID、状态等筛选条件
     * @return 满足条件的用户策略分页列表
     * @throws BusinessException 用户上下文为空、字段权限配置异常时抛出业务异常
     */
    @Override
    public IPage<SysUserPolicyCommonVO> queryPolicy(SysUserPolicyQueryRTO queryParam) {

        // 使用工具类获取字段权限
        List<String> visibleFields = getQueryOperableFields();

        // 构建条件查询 仅选择用户有权限查看的列
        LambdaQueryWrapper<SysUserPolicy> wrapper = new LambdaQueryWrapper<SysUserPolicy>();
        if (visibleFields != null && !visibleFields.isEmpty()) {
            wrapper.select(SysUserPolicy.class, entity -> visibleFields.contains(entity.getColumn()));
        }
        wrapper.eq(SysUserPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());

        // 策略编码模糊匹配
        if (queryParam.getPolicyCode() != null && !queryParam.getPolicyCode().isEmpty()) {
            wrapper.like(SysUserPolicy::getPolicyCode, queryParam.getPolicyCode());
        }
        // 策略名称模糊匹配
        if (queryParam.getPolicyName() != null && !queryParam.getPolicyName().isEmpty()) {
            wrapper.like(SysUserPolicy::getPolicyName, queryParam.getPolicyName());
        }
        // 目标类型精确匹配
        if (queryParam.getTargetType() != null && !queryParam.getTargetType().isEmpty()) {
            wrapper.eq(SysUserPolicy::getTargetType, queryParam.getTargetType());
        }
        // 用户ID精确匹配
        if (queryParam.getUserId() != null) {
            wrapper.eq(SysUserPolicy::getUserId, queryParam.getUserId());
        }
        // 目标ID精确匹配
        if (queryParam.getTargetId() != null) {
            wrapper.eq(SysUserPolicy::getTargetId, queryParam.getTargetId());
        }
        // 状态精确匹配
        if (queryParam.getStatus() != null && !queryParam.getStatus().isEmpty()) {
            wrapper.eq(SysUserPolicy::getStatus, queryParam.getStatus());
        }
        // 创建时间范围查询
        TimeRangeCommonRTO createTimeRange = queryParam.getCreateTimeRange();
        if (createTimeRange != null) {
            if (createTimeRange.getStartTime() != null) {
                wrapper.ge(SysUserPolicy::getCreateAt, createTimeRange.getStartTime());
            }
            if (createTimeRange.getEndTime() != null) {
                wrapper.le(SysUserPolicy::getCreateAt, createTimeRange.getEndTime());
            }
        }
        // 更新时间范围查询
        TimeRangeCommonRTO updateTimeRange = queryParam.getUpdateTimeRange();
        if (updateTimeRange != null) {
            if (updateTimeRange.getStartTime() != null) {
                wrapper.ge(SysUserPolicy::getUpdateAt, updateTimeRange.getStartTime());
            }
            if (updateTimeRange.getEndTime() != null) {
                wrapper.le(SysUserPolicy::getUpdateAt, updateTimeRange.getEndTime());
            }
        }

        // 执行分页查询
        IPage<SysUserPolicy> entityPage = this.page(new Page<>(queryParam.getPageNum(), queryParam.getPageSize()), wrapper);

        // 构建VO分页对象 保留原始分页信息
        IPage<SysUserPolicyCommonVO> voPage = new Page<>(entityPage.getCurrent(), entityPage.getSize(), entityPage.getTotal());
        // 通过 MapStruct 转换器将实体分页记录转换为 VO 列表
        voPage.setRecords(sysUserPolicyConverter.entityListToCommonVoList(entityPage.getRecords()));
        return voPage;

    }

    /**
     * <p>查询用户策略详情</p>
     *
     * @param policyCode 策略编码，用于定位唯一策略
     * @return 用户策略详情VO，包含完整的策略信息
     * @throws BusinessException 策略不存在时抛出业务异常
     */
    @Override
    public SysUserPolicyDetailVO queryPolicyDetail(String policyCode) {

        // 参数校验 策略编码不能为空
        if (policyCode == null || policyCode.trim().isEmpty()) {
            throw new BusinessException("策略编码不能为空");
        }

        // 使用工具类获取字段权限
        List<String> visibleFields = getQueryOperableFields();

        // 根据策略编码查询 仅选择用户有权限查看的列
        LambdaQueryWrapper<SysUserPolicy> wrapper = new LambdaQueryWrapper<SysUserPolicy>();
        if (visibleFields != null && !visibleFields.isEmpty()) {
            wrapper.select(SysUserPolicy.class, entity -> visibleFields.contains(entity.getColumn()));
        }
        wrapper.eq(SysUserPolicy::getPolicyCode, policyCode)
               .eq(SysUserPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());

        SysUserPolicy policy = this.getOne(wrapper);
        if (policy == null) {
            throw new BusinessException("用户策略不存在");
        }

        // 通过 MapStruct 转换器将实体转换为详情VO
        return sysUserPolicyConverter.toDetailVO(policy);

    }

    /**
     * <p>新增用户策略</p>
     *
     * @param addParam 新增用户策略信息
     * @return 新增结果行数
     * @throws BusinessException 策略编码已存在、绑定关系已存在或字段权限不足时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer addPolicy(SysUserPolicyAddRTO addParam) {

        // 获取创建操作的字段权限
        List<String> visibleFields = getTableFieldPermission("create");
        if (visibleFields == null || visibleFields.isEmpty()) {
            throw new BusinessException("无权新增用户策略");
        }

        // 校验策略编码唯一性
        LambdaQueryWrapper<SysUserPolicy> codeCheckWrapper = new LambdaQueryWrapper<SysUserPolicy>()
                .eq(SysUserPolicy::getPolicyCode, addParam.getPolicyCode())
                .eq(SysUserPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        if (this.count(codeCheckWrapper) > 0) {
            throw new BusinessException("策略编码已存在");
        }

        // 校验是否已存在相同的绑定关系
        LambdaQueryWrapper<SysUserPolicy> bindCheckWrapper = new LambdaQueryWrapper<SysUserPolicy>()
                .eq(SysUserPolicy::getUserId, addParam.getUserId())
                .eq(SysUserPolicy::getTargetType, addParam.getTargetType())
                .eq(SysUserPolicy::getTargetId, addParam.getTargetId())
                .eq(SysUserPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        if (this.count(bindCheckWrapper) > 0) {
            throw new BusinessException("该绑定关系已存在");
        }

        // 通过 MapStruct 转换器将RTO转换为实体
        SysUserPolicy entity = sysUserPolicyConverter.toEntityFromAdd(addParam);

        // 设置默认状态
        if (entity.getStatus() == null || entity.getStatus().isEmpty()) {
            entity.setStatus("ACTIVE");
        }

        boolean isSuperAdmin = StpUtil.hasRole(GlobalConstant.Role.SUPER_ADMIN_ROLE);
        if (isSuperAdmin) {
            // 超级管理员：若明确填写了审核字段值则以填写值为准，若未填写则自动应用默认值
            if (entity.getCreateBy() == null) {
                entity.setCreateBy(Long.valueOf(StpUtil.getLoginIdAsString()));
            }
            if (entity.getCreateAt() == null) {
                entity.setCreateAt(LocalDateTime.now());
            }
            if (entity.getUpdateBy() == null) {
                entity.setUpdateBy(Long.valueOf(StpUtil.getLoginIdAsString()));
            }
            if (entity.getUpdateAt() == null) {
                entity.setUpdateAt(LocalDateTime.now());
            }
            if (entity.getIsDeleted() == null) {
                entity.setIsDeleted(GlobalEnum.Deleted.NOT_DELETED.getCode());
            }
        } else {
            // 非超级管理员：严格禁止设置审核字段，系统自动填充默认值，忽略前端传递的审核字段参数
            entity.setCreateBy(Long.valueOf(StpUtil.getLoginIdAsString()));
            entity.setCreateAt(LocalDateTime.now());
            entity.setUpdateBy(Long.valueOf(StpUtil.getLoginIdAsString()));
            entity.setUpdateAt(LocalDateTime.now());
            entity.setIsDeleted(GlobalEnum.Deleted.NOT_DELETED.getCode());
            entity.setDeletedAt(null);
        }

        // 根据字段权限清除不可操作的字段值 确保用户只能设置有权限的字段
        if (!visibleFields.contains("policy_name")) entity.setPolicyName(null);
        if (!visibleFields.contains("is_primary")) entity.setIsPrimary(null);
        if (!visibleFields.contains("status")) entity.setStatus(null);
        if (!visibleFields.contains("disable_reason")) entity.setDisableReason(null);

        // 保存用户策略信息
        this.save(entity);
        return 1;

    }

    /**
     * <p>修改用户策略</p>
     *
     * @param updateParam 修改用户策略信息
     * @return 修改结果行数
     * @throws BusinessException 策略不存在或字段权限不足时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updatePolicy(SysUserPolicyUpdateRTO updateParam) {

        // 获取更新操作的字段权限
        List<String> visibleFields = getTableFieldPermission("update");
        if (visibleFields == null || visibleFields.isEmpty()) {
            throw new BusinessException("无权修改用户策略");
        }

        // 查询待更新的策略 确保策略存在且未删除
        SysUserPolicy existingPolicy = this.getOne(new LambdaQueryWrapper<SysUserPolicy>()
                .eq(SysUserPolicy::getPolicyCode, updateParam.getPolicyCode())
                .eq(SysUserPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
        if (existingPolicy == null) {
            throw new BusinessException("用户策略不存在");
        }

        // 通过 MapStruct 转换器将RTO转换为实体
        SysUserPolicy entity = sysUserPolicyConverter.toEntityFromUpdate(updateParam);

        // 设置实体ID用于更新条件
        entity.setId(existingPolicy.getId());

        boolean isSuperAdmin = StpUtil.hasRole(GlobalConstant.Role.SUPER_ADMIN_ROLE);
        if (isSuperAdmin) {
            // 超级管理员：若明确填写了审核字段值则以填写值为准，若未填写则自动应用默认值
            if (entity.getCreateBy() == null) {
                entity.setCreateBy(existingPolicy.getCreateBy());
            }
            if (entity.getCreateAt() == null) {
                entity.setCreateAt(existingPolicy.getCreateAt());
            }
            if (entity.getUpdateBy() == null) {
                entity.setUpdateBy(Long.valueOf(StpUtil.getLoginIdAsString()));
            }
            if (entity.getUpdateAt() == null) {
                entity.setUpdateAt(LocalDateTime.now());
            }
            if (entity.getIsDeleted() == null) {
                entity.setIsDeleted(existingPolicy.getIsDeleted());
            }
        } else {
            // 非超级管理员：严格禁止修改审核字段，系统自动填充更新人信息和更新时间，保留原创建信息
            entity.setCreateBy(null);
            entity.setCreateAt(null);
            entity.setUpdateBy(Long.valueOf(StpUtil.getLoginIdAsString()));
            entity.setUpdateAt(LocalDateTime.now());
            entity.setIsDeleted(null);
            entity.setDeletedAt(null);
        }

        // 根据字段权限清除不可操作的字段值 确保用户只能更新有权限的字段
        if (!visibleFields.contains("policy_name")) entity.setPolicyName(null);
        if (!visibleFields.contains("is_primary")) entity.setIsPrimary(null);
        if (!visibleFields.contains("status")) entity.setStatus(null);
        if (!visibleFields.contains("disable_reason")) entity.setDisableReason(null);

        // 策略编码、用户ID、目标类型、目标ID不可修改 清除这些字段
        entity.setPolicyCode(null);
        entity.setUserId(null);
        entity.setTargetType(null);
        entity.setTargetId(null);

        // 执行更新操作 使用updateById仅更新非null字段
        this.updateById(entity);
        return 1;

    }

    /**
     * <p>更新用户策略状态</p>
     *
     * @param policyCode 策略编码
     * @param status 目标状态（ACTIVE/DISABLED）
     * @return 更新结果行数
     * @throws BusinessException 策略不存在或状态无效时抛出业务异常
     */
    @Override
    public Integer updatePolicyStatus(String policyCode, String status) {

        // 获取更新操作的字段权限
        List<String> visibleFields = getTableFieldPermission("update");
        if (visibleFields == null || !visibleFields.contains("status")) {
            throw new BusinessException("无权修改用户策略状态字段");
        }

        // 校验状态值合法性
        if (!"ACTIVE".equals(status) && !"DISABLED".equals(status)) {
            throw new BusinessException("无效的策略状态");
        }

        // 查询待更新状态的策略
        SysUserPolicy policy = this.getOne(new LambdaQueryWrapper<SysUserPolicy>()
                .eq(SysUserPolicy::getPolicyCode, policyCode)
                .eq(SysUserPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
        if (policy == null) {
            throw new BusinessException("用户策略不存在");
        }

        // 校验策略是否已处于目标状态
        if (status.equals(policy.getStatus())) {
            throw new BusinessException("策略已处于该状态，无需重复操作");
        }

        // 更新当前策略状态
        LambdaUpdateWrapper<SysUserPolicy> updateWrapper = new LambdaUpdateWrapper<SysUserPolicy>()
                .eq(SysUserPolicy::getPolicyCode, policyCode)
                .eq(SysUserPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .set(SysUserPolicy::getStatus, status)
                .set(SysUserPolicy::getUpdateBy, StpUtil.getLoginIdAsString())
                .set(SysUserPolicy::getUpdateAt, LocalDateTime.now());
        if ("DISABLED".equals(status)) {
            updateWrapper.set(SysUserPolicy::getDisableReason, "ADMIN_DISABLE");
        } else if ("ACTIVE".equals(status)) {
            updateWrapper.set(SysUserPolicy::getDisableReason, null);
        }
        this.update(updateWrapper);
        return 1;

    }

    /**
     * <p>删除用户策略</p>
     *
     * @param policyCode 策略编码
     * @return 删除结果行数
     * @throws BusinessException 策略不存在或字段权限不足时抛出业务异常
     */
    @Override
    public Integer deletePolicy(String policyCode) {

        // 获取更新操作的字段权限
        List<String> visibleFields = getTableFieldPermission("update");
        if (visibleFields == null || !visibleFields.contains("is_deleted")) {
            throw new BusinessException("无权删除用户策略");
        }

        // 查询待删除的策略
        SysUserPolicy policy = this.getOne(new LambdaQueryWrapper<SysUserPolicy>()
                .eq(SysUserPolicy::getPolicyCode, policyCode)
                .eq(SysUserPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
        if (policy == null) {
            throw new BusinessException("用户策略不存在");
        }

        // 执行逻辑删除
        this.update(new LambdaUpdateWrapper<SysUserPolicy>()
                .eq(SysUserPolicy::getPolicyCode, policyCode)
                .eq(SysUserPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .set(SysUserPolicy::getIsDeleted, GlobalEnum.Deleted.DELETED.getCode())
                .set(SysUserPolicy::getDeletedAt, LocalDateTime.now())
                .set(SysUserPolicy::getUpdateBy, StpUtil.getLoginIdAsString())
                .set(SysUserPolicy::getUpdateAt, LocalDateTime.now()));
        return 1;

    }

    /**
     * <p>批量新增用户策略</p>
     *
     * @param addParamList 批量新增用户策略信息集合
     * @return 成功新增的策略数量
     * @throws BusinessException 任一策略编码重复或字段权限不足时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchAddPolicy(List<SysUserPolicyAddRTO> addParamList) {

        // 获取创建操作的字段权限
        List<String> visibleFields = getTableFieldPermission("create");
        if (visibleFields == null || visibleFields.isEmpty()) {
            throw new BusinessException("无权新增用户策略");
        }

        // 审核字段权限控制 通过Sa-Token判断当前用户是否为超级管理员
        boolean isSuperAdmin = StpUtil.hasRole(GlobalConstant.Role.SUPER_ADMIN_ROLE);

        // 通过 MapStruct 转换器批量将RTO列表转换为实体列表
        List<SysUserPolicy> entityList = sysUserPolicyConverter.addRTOListToEntityList(addParamList);

        // 批量内重复编码检查 同一批次中策略编码不得重复
        Set<String> batchCodeSet = new HashSet<>();
        for (SysUserPolicy entity : entityList) {
            if (!batchCodeSet.add(entity.getPolicyCode())) {
                throw new BusinessException("批量新增中存在重复的策略编码: " + entity.getPolicyCode());
            }
        }

        // 遍历处理每个策略实体 校验编码唯一性、绑定关系唯一性、设置默认值、清除不可操作字段
        for (SysUserPolicy entity : entityList) {
            // 校验策略编码唯一性
            long codeCount = this.count(new LambdaQueryWrapper<SysUserPolicy>()
                    .eq(SysUserPolicy::getPolicyCode, entity.getPolicyCode())
                    .eq(SysUserPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
            if (codeCount > 0) {
                throw new BusinessException("策略编码已存在: " + entity.getPolicyCode());
            }

            // 校验绑定关系唯一性
            long bindCount = this.count(new LambdaQueryWrapper<SysUserPolicy>()
                    .eq(SysUserPolicy::getUserId, entity.getUserId())
                    .eq(SysUserPolicy::getTargetType, entity.getTargetType())
                    .eq(SysUserPolicy::getTargetId, entity.getTargetId())
                    .eq(SysUserPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
            if (bindCount > 0) {
                throw new BusinessException("绑定关系已存在: " + entity.getPolicyCode());
            }

            // 设置默认值
            if (entity.getStatus() == null || entity.getStatus().isEmpty()) {
                entity.setStatus("ACTIVE");
            }

            // 审核字段权限控制
            if (isSuperAdmin) {
                // 超级管理员：若明确填写了审核字段值则以填写值为准，若未填写则自动应用默认值
                if (entity.getCreateBy() == null) {
                    entity.setCreateBy(Long.valueOf(StpUtil.getLoginIdAsString()));
                }
                if (entity.getCreateAt() == null) {
                    entity.setCreateAt(LocalDateTime.now());
                }
                if (entity.getUpdateBy() == null) {
                    entity.setUpdateBy(Long.valueOf(StpUtil.getLoginIdAsString()));
                }
                if (entity.getUpdateAt() == null) {
                    entity.setUpdateAt(LocalDateTime.now());
                }
                if (entity.getIsDeleted() == null) {
                    entity.setIsDeleted(GlobalEnum.Deleted.NOT_DELETED.getCode());
                }
            } else {
                // 非超级管理员：严格禁止设置审核字段，系统自动填充默认值
                entity.setCreateBy(Long.valueOf(StpUtil.getLoginIdAsString()));
                entity.setCreateAt(LocalDateTime.now());
                entity.setUpdateBy(Long.valueOf(StpUtil.getLoginIdAsString()));
                entity.setUpdateAt(LocalDateTime.now());
                entity.setIsDeleted(GlobalEnum.Deleted.NOT_DELETED.getCode());
                entity.setDeletedAt(null);
            }

            // 根据字段权限清除不可操作的字段值
            if (!visibleFields.contains("policy_name")) entity.setPolicyName(null);
            if (!visibleFields.contains("is_primary")) entity.setIsPrimary(null);
            if (!visibleFields.contains("status")) entity.setStatus(null);
            if (!visibleFields.contains("disable_reason")) entity.setDisableReason(null);
        }

        // 批量保存所有策略
        this.saveBatch(entityList);
        return entityList.size();

    }

    /**
     * <p>批量修改用户策略</p>
     *
     * @param updateParamList 批量修改用户策略信息集合
     * @return 成功修改的策略数量
     * @throws BusinessException 任一策略不存在或字段权限不足时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdatePolicy(List<SysUserPolicyUpdateRTO> updateParamList) {

        // 获取更新操作的字段权限
        List<String> visibleFields = getTableFieldPermission("update");
        if (visibleFields == null || visibleFields.isEmpty()) {
            throw new BusinessException("无权修改用户策略");
        }

        // 审核字段权限控制
        boolean isSuperAdmin = StpUtil.hasRole(GlobalConstant.Role.SUPER_ADMIN_ROLE);

        // 遍历处理每个策略更新
        for (SysUserPolicyUpdateRTO updateParam : updateParamList) {
            // 查询待更新的策略
            SysUserPolicy existingPolicy = this.getOne(new LambdaQueryWrapper<SysUserPolicy>()
                    .eq(SysUserPolicy::getPolicyCode, updateParam.getPolicyCode())
                    .eq(SysUserPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
            if (existingPolicy == null) {
                throw new BusinessException("用户策略不存在: " + updateParam.getPolicyCode());
            }

            // 转换为实体
            SysUserPolicy entity = sysUserPolicyConverter.toEntityFromUpdate(updateParam);
            entity.setId(existingPolicy.getId());

            // 审核字段权限控制
            if (isSuperAdmin) {
                if (entity.getCreateBy() == null) {
                    entity.setCreateBy(existingPolicy.getCreateBy());
                }
                if (entity.getCreateAt() == null) {
                    entity.setCreateAt(existingPolicy.getCreateAt());
                }
                if (entity.getUpdateBy() == null) {
                    entity.setUpdateBy(Long.valueOf(StpUtil.getLoginIdAsString()));
                }
                if (entity.getUpdateAt() == null) {
                    entity.setUpdateAt(LocalDateTime.now());
                }
                if (entity.getIsDeleted() == null) {
                    entity.setIsDeleted(existingPolicy.getIsDeleted());
                }
            } else {
                entity.setCreateBy(null);
                entity.setCreateAt(null);
                entity.setUpdateBy(Long.valueOf(StpUtil.getLoginIdAsString()));
                entity.setUpdateAt(LocalDateTime.now());
                entity.setIsDeleted(null);
                entity.setDeletedAt(null);
            }

            // 根据字段权限清除不可操作的字段值
            if (!visibleFields.contains("policy_name")) entity.setPolicyName(null);
            if (!visibleFields.contains("is_primary")) entity.setIsPrimary(null);
            if (!visibleFields.contains("status")) entity.setStatus(null);
            if (!visibleFields.contains("disable_reason")) entity.setDisableReason(null);

            // 策略编码、用户ID、目标类型、目标ID不可修改
            entity.setPolicyCode(null);
            entity.setUserId(null);
            entity.setTargetType(null);
            entity.setTargetId(null);

            // 执行更新
            this.updateById(entity);
        }
        return updateParamList.size();

    }

    /**
     * <p>批量更新用户策略状态</p>
     *
     * @param policyCodeList 策略编码集合
     * @param status 目标状态
     * @return 更新结果行数
     * @throws BusinessException 状态无效或字段权限不足时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdatePolicyStatus(List<String> policyCodeList, String status) {

        // 获取更新操作的字段权限
        List<String> visibleFields = getTableFieldPermission("update");
        if (visibleFields == null || !visibleFields.contains("status")) {
            throw new BusinessException("无权修改用户策略状态字段");
        }

        // 校验状态值合法性
        if (!"ACTIVE".equals(status) && !"DISABLED".equals(status)) {
            throw new BusinessException("无效的策略状态");
        }

        int totalUpdated = 0;

        // 遍历每个策略编码 更新状态
        for (String policyCode : policyCodeList) {
            // 查询策略信息
            SysUserPolicy policy = this.getOne(new LambdaQueryWrapper<SysUserPolicy>()
                    .eq(SysUserPolicy::getPolicyCode, policyCode)
                    .eq(SysUserPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
            if (policy == null) {
                continue;
            }

            // 校验策略是否已处于目标状态
            if (status.equals(policy.getStatus())) {
                continue;
            }

            // 更新状态
            LambdaUpdateWrapper<SysUserPolicy> updateWrapper = new LambdaUpdateWrapper<SysUserPolicy>()
                    .eq(SysUserPolicy::getPolicyCode, policyCode)
                    .eq(SysUserPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                    .set(SysUserPolicy::getStatus, status)
                    .set(SysUserPolicy::getUpdateBy, StpUtil.getLoginIdAsString())
                    .set(SysUserPolicy::getUpdateAt, LocalDateTime.now());
            if ("DISABLED".equals(status)) {
                updateWrapper.set(SysUserPolicy::getDisableReason, "ADMIN_DISABLE");
            } else if ("ACTIVE".equals(status)) {
                updateWrapper.set(SysUserPolicy::getDisableReason, null);
            }
            this.update(updateWrapper);
            totalUpdated++;
        }
        return totalUpdated;

    }

    /**
     * <p>批量删除用户策略</p>
     *
     * @param policyCodeList 策略编码集合
     * @return 删除结果行数
     * @throws BusinessException 字段权限不足时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchDeletePolicy(List<String> policyCodeList) {

        // 获取更新操作的字段权限
        List<String> visibleFields = getTableFieldPermission("update");
        if (visibleFields == null || !visibleFields.contains("is_deleted")) {
            throw new BusinessException("无权删除用户策略");
        }

        int totalDeleted = 0;

        // 遍历每个策略编码 执行逻辑删除
        for (String policyCode : policyCodeList) {
            // 查询策略信息
            SysUserPolicy policy = this.getOne(new LambdaQueryWrapper<SysUserPolicy>()
                    .eq(SysUserPolicy::getPolicyCode, policyCode)
                    .eq(SysUserPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
            if (policy == null) {
                continue;
            }

            // 执行逻辑删除
            this.update(new LambdaUpdateWrapper<SysUserPolicy>()
                    .eq(SysUserPolicy::getPolicyCode, policyCode)
                    .eq(SysUserPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                    .set(SysUserPolicy::getIsDeleted, GlobalEnum.Deleted.DELETED.getCode())
                    .set(SysUserPolicy::getDeletedAt, LocalDateTime.now())
                    .set(SysUserPolicy::getUpdateBy, StpUtil.getLoginIdAsString())
                    .set(SysUserPolicy::getUpdateAt, LocalDateTime.now()));
            totalDeleted++;
        }
        return totalDeleted;

    }

    /**
     * <p>批量绑定用户到目标</p>
     *
     * @param bindParam 批量绑定参数
     * @return 成功绑定的策略数量
     * @throws BusinessException 字段权限不足时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchBindToTarget(SysUserPolicyBatchBindRTO bindParam) {

        // 获取创建操作的字段权限
        List<String> visibleFields = getTableFieldPermission("create");
        if (visibleFields == null || visibleFields.isEmpty()) {
            throw new BusinessException("无权新增用户策略");
        }

        int totalBound = 0;

        // 遍历目标ID列表 为每个目标创建绑定策略
        for (Long targetId : bindParam.getTargetIdList()) {
            // 检查是否已存在绑定关系
            long existCount = this.count(new LambdaQueryWrapper<SysUserPolicy>()
                    .eq(SysUserPolicy::getUserId, bindParam.getUserId())
                    .eq(SysUserPolicy::getTargetType, bindParam.getTargetType())
                    .eq(SysUserPolicy::getTargetId, targetId)
                    .eq(SysUserPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
            if (existCount > 0) {
                continue;  // 已存在则跳过
            }

            // 生成策略编码
            String policyCode = "UP_" + bindParam.getTargetType() + "_" + bindParam.getUserId() + "_" + targetId;

            // 创建策略实体
            SysUserPolicy entity = new SysUserPolicy();
            entity.setPolicyCode(policyCode);
            entity.setPolicyName("用户策略绑定");
            entity.setUserId(bindParam.getUserId());
            entity.setTargetType(bindParam.getTargetType());
            entity.setTargetId(targetId);
            entity.setStatus("ACTIVE");
            entity.setCreateBy(Long.valueOf(StpUtil.getLoginIdAsString()));
            entity.setCreateAt(LocalDateTime.now());
            entity.setUpdateBy(Long.valueOf(StpUtil.getLoginIdAsString()));
            entity.setUpdateAt(LocalDateTime.now());
            entity.setIsDeleted(GlobalEnum.Deleted.NOT_DELETED.getCode());

            // 保存策略
            this.save(entity);
            totalBound++;
        }

        return totalBound;

    }

    /**
     * <p>批量解绑用户与目标</p>
     *
     * @param unbindParam 批量解绑参数
     * @return 成功解绑的策略数量
     * @throws BusinessException 字段权限不足时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUnbindFromTarget(SysUserPolicyBatchUnbindRTO unbindParam) {

        // 获取更新操作的字段权限
        List<String> visibleFields = getTableFieldPermission("update");
        if (visibleFields == null || !visibleFields.contains("is_deleted")) {
            throw new BusinessException("无权删除用户策略");
        }

        int totalUnbound = 0;

        // 遍历策略编码列表 执行逻辑删除
        for (String policyCode : unbindParam.getPolicyCodeList()) {
            // 查询策略信息
            SysUserPolicy policy = this.getOne(new LambdaQueryWrapper<SysUserPolicy>()
                    .eq(SysUserPolicy::getPolicyCode, policyCode)
                    .eq(SysUserPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
            if (policy == null) {
                continue;
            }

            // 执行逻辑删除
            this.update(new LambdaUpdateWrapper<SysUserPolicy>()
                    .eq(SysUserPolicy::getPolicyCode, policyCode)
                    .eq(SysUserPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                    .set(SysUserPolicy::getIsDeleted, GlobalEnum.Deleted.DELETED.getCode())
                    .set(SysUserPolicy::getDeletedAt, LocalDateTime.now())
                    .set(SysUserPolicy::getUpdateBy, StpUtil.getLoginIdAsString())
                    .set(SysUserPolicy::getUpdateAt, LocalDateTime.now()));
            totalUnbound++;
        }

        return totalUnbound;

    }

}
