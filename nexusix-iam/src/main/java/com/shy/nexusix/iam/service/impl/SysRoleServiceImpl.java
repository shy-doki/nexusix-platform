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
import com.shy.nexusix.iam.converter.SysRoleConverter;
import com.shy.nexusix.iam.entity.SysPerm;
import com.shy.nexusix.iam.entity.SysPermPolicy;
import com.shy.nexusix.iam.entity.SysRole;
import com.shy.nexusix.iam.mapper.SysPermMapper;
import com.shy.nexusix.iam.mapper.SysPermPolicyMapper;
import com.shy.nexusix.iam.mapper.SysRoleMapper;
import com.shy.nexusix.iam.rto.SysRoleAddRTO;
import com.shy.nexusix.iam.rto.SysRoleGrantPermRTO;
import com.shy.nexusix.iam.rto.SysRoleQueryRTO;
import com.shy.nexusix.iam.rto.SysRoleUpdateRTO;
import com.shy.nexusix.iam.service.ISysRoleService;
import com.shy.nexusix.iam.vo.SysPermCommonVO;
import com.shy.nexusix.iam.vo.SysRoleCommonVO;
import com.shy.nexusix.iam.vo.SysRoleDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>系统角色服务实现类</p>
 *
 * @author shy
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    private SysRoleConverter sysRoleConverter;

    @Autowired
    private SysPermPolicyMapper sysPermPolicyMapper;

    @Autowired
    private SysPermMapper sysPermMapper;

    /**
     * <p>获取查询操作的可操作字段</p>
     *
     * @return 可操作字段列表，null表示无限制
     */
    private List<String> getQueryOperableFields() {
        // 暂无IAM字段权限配置，返回null表示无限制
        return null;
    }

    /**
     * <p>获取指定操作类型的字段权限</p>
     *
     * @param operationType 操作类型："query"、"create"、"update"
     * @return 可操作字段列表，null表示无权限或无限制
     * @throws BusinessException 用户上下文为空或操作类型不支持时抛出
     */
    private List<String> getTableFieldPermission(String operationType) {
        // 暂无IAM字段权限配置，返回null表示无限制
        return null;
    }

    /**
     * <p>查询角色列表</p>
     *
     * @return 角色通用VO列表，封装用户有权查看的角色信息
     * @throws BusinessException 用户上下文为空、字段权限配置异常时抛出业务异常
     */
    @Override
    public List<SysRoleCommonVO> queryRoleList() {

        // 使用工具类获取字段权限
        List<String> visibleFields = getQueryOperableFields();

        // 构建查询条件 仅选择用户有权限查看的列，并排除已删除的角色记录
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<SysRole>();

        // 如果有字段级权限限制，则只选择可操作字段
        if (visibleFields != null && !visibleFields.isEmpty()) {
            wrapper.select(SysRole.class, entity -> visibleFields.contains(entity.getProperty()));
        }

        wrapper.eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());

        List<SysRole> roleList = this.list(wrapper);
        // 通过 MapStruct 转换器将实体列表转换为 VO 列表，同时完成状态码到描述的转换
        return sysRoleConverter.entityListToCommonVoList(roleList);

    }

    /**
     * <p>分页查询角色列表</p>
     *
     * @param page 分页参数，包含页码和每页数量
     * @return 分页后的角色通用VO列表
     * @throws BusinessException 用户上下文为空、字段权限配置异常时抛出业务异常
     */
    @Override
    public IPage<SysRoleCommonVO> queryRolePage(PageCommonRTO page) {

        // 使用工具类获取字段权限
        List<String> visibleFields = getQueryOperableFields();

        // 构建分页查询条件 仅选择用户有权限查看的列，并排除已删除的角色记录
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<SysRole>();

        // 如果有字段级权限限制，则只选择可操作字段
        if (visibleFields != null && !visibleFields.isEmpty()) {
            wrapper.select(SysRole.class, entity -> visibleFields.contains(entity.getProperty()));
        }

        wrapper.eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());

        // 执行分页查询
        IPage<SysRole> entityPage = this.page(new Page<>(page.getPageNum(), page.getPageSize()), wrapper);

        // 构建VO分页对象 保留原始分页信息
        IPage<SysRoleCommonVO> voPage = new Page<>(entityPage.getCurrent(), entityPage.getSize(), entityPage.getTotal());
        // 通过 MapStruct 转换器将实体分页记录转换为 VO 列表
        voPage.setRecords(sysRoleConverter.entityListToCommonVoList(entityPage.getRecords()));
        return voPage;

    }

    /**
     * <p>条件查询角色列表</p>
     *
     * @param queryParam 查询条件，包含角色编码、名称、状态等筛选条件
     * @return 满足条件的角色分页列表
     * @throws BusinessException 用户上下文为空、字段权限配置异常时抛出业务异常
     */
    @Override
    public IPage<SysRoleCommonVO> queryRole(SysRoleQueryRTO queryParam) {

        // 使用工具类获取字段权限
        List<String> visibleFields = getQueryOperableFields();

        // 构建条件查询 仅选择用户有权限查看的列
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<SysRole>();
        if (visibleFields != null && !visibleFields.isEmpty()) {
            wrapper.select(SysRole.class, entity -> visibleFields.contains(entity.getProperty()));
        }
        wrapper.eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());

        // 角色编码精确匹配
        if (queryParam.getRoleCode() != null && !queryParam.getRoleCode().isEmpty()) {
            wrapper.eq(SysRole::getRoleCode, queryParam.getRoleCode());
        }
        // 角色名称模糊匹配
        if (queryParam.getRoleName() != null && !queryParam.getRoleName().isEmpty()) {
            wrapper.like(SysRole::getRoleName, queryParam.getRoleName());
        }
        // 状态精确匹配
        if (queryParam.getStatus() != null && !queryParam.getStatus().isEmpty()) {
            wrapper.eq(SysRole::getStatus, queryParam.getStatus());
        }
        // 创建时间范围查询
        TimeRangeCommonRTO createTimeRange = queryParam.getCreateTimeRange();
        if (createTimeRange != null) {
            if (createTimeRange.getStartTime() != null) {
                wrapper.ge(SysRole::getCreateAt, createTimeRange.getStartTime());
            }
            if (createTimeRange.getEndTime() != null) {
                wrapper.le(SysRole::getCreateAt, createTimeRange.getEndTime());
            }
        }
        // 更新时间范围查询
        TimeRangeCommonRTO updateTimeRange = queryParam.getUpdateTimeRange();
        if (updateTimeRange != null) {
            if (updateTimeRange.getStartTime() != null) {
                wrapper.ge(SysRole::getUpdateAt, updateTimeRange.getStartTime());
            }
            if (updateTimeRange.getEndTime() != null) {
                wrapper.le(SysRole::getUpdateAt, updateTimeRange.getEndTime());
            }
        }

        // 执行分页查询
        IPage<SysRole> entityPage = this.page(new Page<>(queryParam.getPageNum(), queryParam.getPageSize()), wrapper);

        // 构建VO分页对象 保留原始分页信息
        IPage<SysRoleCommonVO> voPage = new Page<>(entityPage.getCurrent(), entityPage.getSize(), entityPage.getTotal());
        // 通过 MapStruct 转换器将实体分页记录转换为 VO 列表
        voPage.setRecords(sysRoleConverter.entityListToCommonVoList(entityPage.getRecords()));
        return voPage;

    }

    /**
     * <p>查询角色详情</p>
     *
     * @param roleCode 角色编码，用于定位唯一角色
     * @return 角色详情VO，包含完整的角色信息
     * @throws BusinessException 角色不存在时抛出业务异常
     */
    @Override
    public SysRoleDetailVO queryRoleDetail(String roleCode) {

        // 参数校验 角色编码不能为空
        if (roleCode == null || roleCode.trim().isEmpty()) {
            throw new BusinessException("角色编码不能为空");
        }

        // 使用工具类获取字段权限
        List<String> visibleFields = getQueryOperableFields();

        // 根据角色编码查询 仅选择用户有权限查看的列
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<SysRole>();
        if (visibleFields != null && !visibleFields.isEmpty()) {
            wrapper.select(SysRole.class, entity -> visibleFields.contains(entity.getProperty()));
        }
        wrapper.eq(SysRole::getRoleCode, roleCode)
               .eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());

        SysRole role = this.getOne(wrapper);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }

        // 通过 MapStruct 转换器将实体转换为详情VO
        return sysRoleConverter.toDetailVO(role);

    }

    /**
     * <p>新增角色</p>
     *
     * @param addParam 新增角色信息
     * @return 新增结果行数
     * @throws BusinessException 角色编码已存在或字段权限不足时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer addRole(SysRoleAddRTO addParam) {

        // 获取创建操作的字段权限
        List<String> visibleFields = getTableFieldPermission("create");
        if (visibleFields == null || visibleFields.isEmpty()) {
            throw new BusinessException("无权新增角色");
        }

        // 校验角色编码唯一性
        LambdaQueryWrapper<SysRole> codeCheckWrapper = new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getRoleCode, addParam.getRoleCode())
                .eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        if (this.count(codeCheckWrapper) > 0) {
            throw new BusinessException("角色编码已存在");
        }

        // 通过 MapStruct 转换器将RTO转换为实体
        SysRole entity = sysRoleConverter.toEntityFromAdd(addParam);

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
        if (!visibleFields.contains("roleName")) entity.setRoleName(null);
        if (!visibleFields.contains("roleDesc")) entity.setRoleDesc(null);
        if (!visibleFields.contains("status")) entity.setStatus(null);
        if (!visibleFields.contains("disableReason")) entity.setDisableReason(null);

        // 保存角色信息
        this.save(entity);
        return 1;

    }

    /**
     * <p>修改角色</p>
     *
     * @param updateParam 修改角色信息
     * @return 修改结果行数
     * @throws BusinessException 角色不存在或字段权限不足时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateRole(SysRoleUpdateRTO updateParam) {

        // 获取更新操作的字段权限
        List<String> visibleFields = getTableFieldPermission("update");
        if (visibleFields == null || visibleFields.isEmpty()) {
            throw new BusinessException("无权修改角色");
        }

        // 查询待更新的角色 确保角色存在且未删除
        SysRole existingRole = this.getOne(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getRoleCode, updateParam.getRoleCode())
                .eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
        if (existingRole == null) {
            throw new BusinessException("角色不存在");
        }

        // 通过 MapStruct 转换器将RTO转换为实体
        SysRole entity = sysRoleConverter.toEntityFromUpdate(updateParam);

        // 设置实体ID用于更新条件
        entity.setId(existingRole.getId());

        boolean isSuperAdmin = StpUtil.hasRole(GlobalConstant.Role.SUPER_ADMIN_ROLE);
        if (isSuperAdmin) {
            // 超级管理员：若明确填写了审核字段值则以填写值为准，若未填写则保留原值或自动应用默认值
            if (entity.getCreateBy() == null) {
                entity.setCreateBy(existingRole.getCreateBy());
            }
            if (entity.getCreateAt() == null) {
                entity.setCreateAt(existingRole.getCreateAt());
            }
            if (entity.getUpdateBy() == null) {
                entity.setUpdateBy(Long.valueOf(StpUtil.getLoginIdAsString()));
            }
            if (entity.getUpdateAt() == null) {
                entity.setUpdateAt(LocalDateTime.now());
            }
            // isDeleted未填写时保留原值
            if (entity.getIsDeleted() == null) {
                entity.setIsDeleted(existingRole.getIsDeleted());
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
        // 将不可见字段设为null MyBatis-Plus更新时将跳过null字段
        if (!visibleFields.contains("roleName")) entity.setRoleName(null);
        if (!visibleFields.contains("roleDesc")) entity.setRoleDesc(null);
        if (!visibleFields.contains("status")) entity.setStatus(null);
        if (!visibleFields.contains("disableReason")) entity.setDisableReason(null);

        // 角色编码不可修改 清除该字段
        entity.setRoleCode(null);

        // 执行更新操作 使用updateById仅更新非null字段
        this.updateById(entity);
        return 1;

    }

    /**
     * <p>更新角色状态</p>
     *
     * @param id 角色ID
     * @param status 目标状态（ENABLED/DISABLED）
     * @return 更新结果行数
     * @throws BusinessException 角色不存在或状态无效时抛出业务异常
     */
    @Override
    public Integer updateRoleStatus(String id, String status) {

        // 获取更新操作的字段权限
        List<String> visibleFields = getTableFieldPermission("update");
        if (visibleFields == null || !visibleFields.contains("status")) {
            throw new BusinessException("无权修改角色状态字段");
        }

        // 校验状态值合法性
        if (!GlobalEnum.RoleStatus.isValidCode(status)) {
            throw new BusinessException("无效的角色状态");
        }

        // 查询待更新状态的角色
        SysRole role = this.getOne(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getId, id)
                .eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
        if (role == null) {
            throw new BusinessException("角色不存在");
        }

        // 校验角色是否已处于目标状态
        if (status.equals(role.getStatus())) {
            throw new BusinessException("角色已处于该状态，无需重复操作");
        }

        // 更新当前角色状态 审核字段updateBy和updateAt由系统自动设置
        // 停用时记录禁用原因为管理员主动停用，启用时清除禁用原因
        LambdaUpdateWrapper<SysRole> updateWrapper = new LambdaUpdateWrapper<SysRole>()
                .eq(SysRole::getId, id)
                .eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .set(SysRole::getStatus, status)
                .set(SysRole::getUpdateBy, StpUtil.getLoginIdAsString())
                .set(SysRole::getUpdateAt, LocalDateTime.now());
        if (GlobalEnum.RoleStatus.DISABLED.getCode().equals(status)) {
            updateWrapper.set(SysRole::getDisableReason, "ADMIN_DISABLE");
        } else if (GlobalEnum.RoleStatus.ENABLED.getCode().equals(status)) {
            updateWrapper.set(SysRole::getDisableReason, null);
        }
        this.update(updateWrapper);
        return 1;

    }

    /**
     * <p>删除角色</p>
     *
     * @param id 角色ID
     * @return 删除结果行数
     * @throws BusinessException 角色不存在或字段权限不足时抛出业务异常
     */
    @Override
    public Integer deleteRole(String id) {

        // 获取更新操作的字段权限
        List<String> visibleFields = getTableFieldPermission("update");
        if (visibleFields == null || !visibleFields.contains("isDeleted")) {
            throw new BusinessException("无权删除角色");
        }

        // 查询待删除的角色
        SysRole role = this.getOne(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getId, id)
                .eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
        if (role == null) {
            throw new BusinessException("角色不存在");
        }

        // 执行逻辑删除 审核字段isDeleted和deletedAt由系统自动设置
        this.update(new LambdaUpdateWrapper<SysRole>()
                .eq(SysRole::getId, id)
                .eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .set(SysRole::getIsDeleted, GlobalEnum.Deleted.DELETED.getCode())
                .set(SysRole::getDeletedAt, LocalDateTime.now())
                .set(SysRole::getUpdateBy, StpUtil.getLoginIdAsString())
                .set(SysRole::getUpdateAt, LocalDateTime.now()));
        return 1;

    }

    /**
     * <p>批量新增角色</p>
     *
     * @param addParamList 批量新增角色信息集合
     * @return 成功新增的角色数量
     * @throws BusinessException 任一角色编码重复或字段权限不足时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchAddRole(List<SysRoleAddRTO> addParamList) {

        // 获取创建操作的字段权限
        List<String> visibleFields = getTableFieldPermission("create");
        if (visibleFields == null || visibleFields.isEmpty()) {
            throw new BusinessException("无权新增角色");
        }

        // 审核字段权限控制 通过Sa-Token判断当前用户是否为超级管理员
        boolean isSuperAdmin = StpUtil.hasRole(GlobalConstant.Role.SUPER_ADMIN_ROLE);

        // 通过 MapStruct 转换器批量将RTO列表转换为实体列表
        List<SysRole> entityList = sysRoleConverter.addRTOListToEntityList(addParamList);

        // 批量内重复编码检查 同一批次中角色编码不得重复
        Set<String> batchCodeSet = new HashSet<>();
        for (SysRole entity : entityList) {
            if (!batchCodeSet.add(entity.getRoleCode())) {
                throw new BusinessException("批量新增中存在重复的角色编码: " + entity.getRoleCode());
            }
        }

        // 遍历处理每个角色实体 校验编码唯一性、设置默认值、清除不可操作字段
        for (SysRole entity : entityList) {
            // 校验角色编码唯一性
            long codeCount = this.count(new LambdaQueryWrapper<SysRole>()
                    .eq(SysRole::getRoleCode, entity.getRoleCode())
                    .eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
            if (codeCount > 0) {
                throw new BusinessException("角色编码已存在: " + entity.getRoleCode());
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
            if (!visibleFields.contains("roleName")) entity.setRoleName(null);
            if (!visibleFields.contains("roleDesc")) entity.setRoleDesc(null);
            if (!visibleFields.contains("status")) entity.setStatus(null);
            if (!visibleFields.contains("disableReason")) entity.setDisableReason(null);
        }

        // 批量保存所有角色
        this.saveBatch(entityList);
        return entityList.size();

    }

    /**
     * <p>批量修改角色</p>
     *
     * @param updateParamList 批量修改角色信息集合
     * @return 成功修改的角色数量
     * @throws BusinessException 任一角色不存在或字段权限不足时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdateRole(List<SysRoleUpdateRTO> updateParamList) {

        // 获取更新操作的字段权限
        List<String> visibleFields = getTableFieldPermission("update");
        if (visibleFields == null || visibleFields.isEmpty()) {
            throw new BusinessException("无权修改角色");
        }

        // 审核字段权限控制 通过Sa-Token判断当前用户是否为超级管理员
        boolean isSuperAdmin = StpUtil.hasRole(GlobalConstant.Role.SUPER_ADMIN_ROLE);

        // 遍历处理每个角色更新
        for (SysRoleUpdateRTO updateParam : updateParamList) {
            // 查询待更新的角色 确保角色存在且未删除
            SysRole existingRole = this.getOne(new LambdaQueryWrapper<SysRole>()
                    .eq(SysRole::getRoleCode, updateParam.getRoleCode())
                    .eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
            if (existingRole == null) {
                throw new BusinessException("角色不存在: " + updateParam.getRoleCode());
            }

            // 通过 MapStruct 转换器将RTO转换为实体
            SysRole entity = sysRoleConverter.toEntityFromUpdate(updateParam);
            entity.setId(existingRole.getId());

            // 审核字段权限控制
            if (isSuperAdmin) {
                // 超级管理员：若明确填写了审核字段值则以填写值为准，若未填写则保留原值或自动应用默认值
                if (entity.getCreateBy() == null) {
                    entity.setCreateBy(existingRole.getCreateBy());
                }
                if (entity.getCreateAt() == null) {
                    entity.setCreateAt(existingRole.getCreateAt());
                }
                if (entity.getUpdateBy() == null) {
                    entity.setUpdateBy(Long.valueOf(StpUtil.getLoginIdAsString()));
                }
                if (entity.getUpdateAt() == null) {
                    entity.setUpdateAt(LocalDateTime.now());
                }
                if (entity.getIsDeleted() == null) {
                    entity.setIsDeleted(existingRole.getIsDeleted());
                }
            } else {
                // 非超级管理员：严格禁止修改审核字段，系统自动填充更新人信息和更新时间
                entity.setCreateBy(null);
                entity.setCreateAt(null);
                entity.setUpdateBy(Long.valueOf(StpUtil.getLoginIdAsString()));
                entity.setUpdateAt(LocalDateTime.now());
                entity.setIsDeleted(null);
                entity.setDeletedAt(null);
            }

            // 根据字段权限清除不可操作的字段值
            if (!visibleFields.contains("roleName")) entity.setRoleName(null);
            if (!visibleFields.contains("roleDesc")) entity.setRoleDesc(null);
            if (!visibleFields.contains("status")) entity.setStatus(null);
            if (!visibleFields.contains("disableReason")) entity.setDisableReason(null);

            // 角色编码不可修改
            entity.setRoleCode(null);

            // 执行更新
            this.updateById(entity);
        }
        return updateParamList.size();

    }

    /**
     * <p>批量更新角色状态</p>
     *
     * @param ids 角色ID集合
     * @param status 目标状态
     * @return 更新结果行数
     * @throws BusinessException 状态无效或字段权限不足时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdateRoleStatus(List<String> ids, String status) {

        // 获取更新操作的字段权限
        List<String> visibleFields = getTableFieldPermission("update");
        if (visibleFields == null || !visibleFields.contains("status")) {
            throw new BusinessException("无权修改角色状态字段");
        }

        // 校验状态值合法性
        if (!GlobalEnum.RoleStatus.isValidCode(status)) {
            throw new BusinessException("无效的角色状态");
        }

        int totalUpdated = 0;

        // 遍历每个角色ID 更新状态
        for (String id : ids) {
            // 查询角色信息
            SysRole role = this.getOne(new LambdaQueryWrapper<SysRole>()
                    .eq(SysRole::getId, id)
                    .eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
            if (role == null) {
                continue;
            }

            // 校验角色是否已处于目标状态
            if (status.equals(role.getStatus())) {
                continue;
            }

            // 更新当前角色状态 审核字段updateBy和updateAt由系统自动设置
            // 停用时记录禁用原因，启用时清除禁用原因
            LambdaUpdateWrapper<SysRole> batchUpdateWrapper = new LambdaUpdateWrapper<SysRole>()
                    .eq(SysRole::getId, id)
                    .eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                    .set(SysRole::getStatus, status)
                    .set(SysRole::getUpdateBy, StpUtil.getLoginIdAsString())
                    .set(SysRole::getUpdateAt, LocalDateTime.now());
            if (GlobalEnum.RoleStatus.DISABLED.getCode().equals(status)) {
                batchUpdateWrapper.set(SysRole::getDisableReason, "ADMIN_DISABLE");
            } else if (GlobalEnum.RoleStatus.ENABLED.getCode().equals(status)) {
                batchUpdateWrapper.set(SysRole::getDisableReason, null);
            }
            this.update(batchUpdateWrapper);
            totalUpdated++;
        }
        return totalUpdated;

    }

    /**
     * <p>批量删除角色</p>
     *
     * @param ids 角色ID集合
     * @return 删除结果行数
     * @throws BusinessException 字段权限不足时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchDeleteRole(List<String> ids) {

        // 获取更新操作的字段权限
        List<String> visibleFields = getTableFieldPermission("update");
        if (visibleFields == null || !visibleFields.contains("isDeleted")) {
            throw new BusinessException("无权删除角色");
        }

        int totalDeleted = 0;

        // 遍历每个角色ID 执行逻辑删除
        for (String id : ids) {
            // 查询待删除的角色
            SysRole role = this.getOne(new LambdaQueryWrapper<SysRole>()
                    .eq(SysRole::getId, id)
                    .eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
            if (role == null) {
                continue;
            }

            // 执行逻辑删除 审核字段isDeleted和deletedAt由系统自动设置
            this.update(new LambdaUpdateWrapper<SysRole>()
                    .eq(SysRole::getId, id)
                    .eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                    .set(SysRole::getIsDeleted, GlobalEnum.Deleted.DELETED.getCode())
                    .set(SysRole::getDeletedAt, LocalDateTime.now())
                    .set(SysRole::getUpdateBy, StpUtil.getLoginIdAsString())
                    .set(SysRole::getUpdateAt, LocalDateTime.now()));
            totalDeleted++;
        }
        return totalDeleted;

    }

    /**
     * <p>查询角色权限</p>
     *
     * @param roleCode 角色编码
     * @return 角色拥有的权限通用VO列表
     * @throws BusinessException 角色不存在时抛出
     */
    @Override
    public List<SysPermCommonVO> queryRolePerms(String roleCode) {

        // 通过 roleCode 查询角色获取 roleId
        SysRole role = this.getOne(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getRoleCode, roleCode)
                .eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        Long roleId = role.getId();

        // 查询 SysPermPolicy 获取 permId 列表
        List<SysPermPolicy> policyList = sysPermPolicyMapper.selectList(new LambdaQueryWrapper<SysPermPolicy>()
                .eq(SysPermPolicy::getTargetType, GlobalEnum.PermPolicyTargetType.ROLE.getCode())
                .eq(SysPermPolicy::getTargetId, roleId)
                .eq(SysPermPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));

        // 如果 permId 列表为空，返回空列表
        if (policyList.isEmpty()) {
            return new ArrayList<>();
        }

        List<Long> permIdList = policyList.stream().map(SysPermPolicy::getPermId).collect(Collectors.toList());

        // 查询 SysPerm
        List<SysPerm> permList = sysPermMapper.selectList(new LambdaQueryWrapper<SysPerm>()
                .in(SysPerm::getId, permIdList)
                .eq(SysPerm::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));

        // 构建 SysPermCommonVO 列表返回 手动映射字段
        return permList.stream().map(perm -> {
            SysPermCommonVO vo = new SysPermCommonVO();
            vo.setPermCode(perm.getPermCode());
            vo.setPermName(perm.getPermName());
            vo.setPermType(perm.getPermType());
            vo.setResourceType(perm.getResourceType());
            vo.setResourcePath(perm.getResourcePath());
            vo.setResourceMethod(perm.getResourceMethod());
            vo.setIcon(perm.getIcon());
            vo.setSortOrder(perm.getSortOrder());
            vo.setIsVisible(perm.getIsVisible());
            vo.setStatus(perm.getStatus());
            vo.setDisableReason(perm.getDisableReason());
            return vo;
        }).collect(Collectors.toList());

    }

    /**
     * <p>为角色授予权限</p>
     *
     * @param roleCode 角色编码
     * @param param 授权请求参数，包含权限编码列表
     * @return 新创建的权限策略数量
     * @throws BusinessException 角色不存在或权限编码不存在时抛出
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer grantPermissions(String roleCode, SysRoleGrantPermRTO param) {

        // 通过 roleCode 查询角色获取 roleId
        SysRole role = this.getOne(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getRoleCode, roleCode)
                .eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        Long roleId = role.getId();

        // 查询权限ID列表
        List<String> permCodeList = param.getPermCodeList();
        List<SysPerm> permList = sysPermMapper.selectList(new LambdaQueryWrapper<SysPerm>()
                .in(SysPerm::getPermCode, permCodeList)
                .eq(SysPerm::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));

        // 校验权限编码是否存在 不存在则抛出业务异常
        Set<String> foundCodes = permList.stream().map(SysPerm::getPermCode).collect(Collectors.toSet());
        for (String permCode : permCodeList) {
            if (!foundCodes.contains(permCode)) {
                throw new BusinessException("权限编码 " + permCode + " 不存在");
            }
        }

        // 遍历创建 SysPermPolicy 记录
        int count = 0;
        int index = 0;
        for (SysPerm perm : permList) {
            SysPermPolicy policy = new SysPermPolicy();
            policy.setPolicyCode("PERM_POLICY_" + System.currentTimeMillis() + "_" + index);
            policy.setPolicyName("角色 " + roleCode + " 权限 " + perm.getPermCode());
            policy.setPermId(perm.getId());
            policy.setTargetType(GlobalEnum.PermPolicyTargetType.ROLE.getCode());
            policy.setTargetId(roleId);
            policy.setStatus(GlobalEnum.PermPolicyStatus.ACTIVE.getCode());
            policy.setIsDeleted(GlobalEnum.Deleted.NOT_DELETED.getCode());
            sysPermPolicyMapper.insert(policy);
            count++;
            index++;
        }
        return count;

    }

    /**
     * <p>撤销角色权限</p>
     *
     * @param roleCode 角色编码
     * @param permCodeList 权限编码列表
     * @return 删除的权限策略数量
     * @throws BusinessException 角色不存在时抛出
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer revokePermissions(String roleCode, List<String> permCodeList) {

        // 通过 roleCode 查询角色获取 roleId
        SysRole role = this.getOne(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getRoleCode, roleCode)
                .eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        Long roleId = role.getId();

        // 查询权限ID列表
        List<SysPerm> permList = sysPermMapper.selectList(new LambdaQueryWrapper<SysPerm>()
                .in(SysPerm::getPermCode, permCodeList)
                .eq(SysPerm::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
        List<Long> permIdList = permList.stream().map(SysPerm::getId).collect(Collectors.toList());

        if (permIdList.isEmpty()) {
            return 0;
        }

        // 逻辑删除对应的 SysPermPolicy 记录
        int deletedCount = sysPermPolicyMapper.update(null, new LambdaUpdateWrapper<SysPermPolicy>()
                .eq(SysPermPolicy::getTargetType, GlobalEnum.PermPolicyTargetType.ROLE.getCode())
                .eq(SysPermPolicy::getTargetId, roleId)
                .in(SysPermPolicy::getPermId, permIdList)
                .eq(SysPermPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .set(SysPermPolicy::getIsDeleted, GlobalEnum.Deleted.DELETED.getCode())
                .set(SysPermPolicy::getDeletedAt, LocalDateTime.now()));
        return deletedCount;

    }

}
