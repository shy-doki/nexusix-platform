package com.shy.nexusix.iam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.enums.GlobalEnum.DataScope;
import com.shy.nexusix.common.enums.GlobalEnum.RoleLevel;
import com.shy.nexusix.common.enums.GlobalEnum.Status;
import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.common.rto.TimeRangeCommonRTO;
import com.shy.nexusix.iam.converter.SysRoleConverter;
import com.shy.nexusix.iam.entity.SysRole;
import com.shy.nexusix.iam.mapper.SysRoleMapper;
import com.shy.nexusix.iam.rto.SysRoleAddRTO;
import com.shy.nexusix.iam.rto.SysRoleQueryRTO;
import com.shy.nexusix.iam.rto.SysRoleUpdateRTO;
import com.shy.nexusix.iam.service.ISysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shy.nexusix.iam.vo.SysRoleCommonVO;
import com.shy.nexusix.iam.vo.SysRoleDetailVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 角色表 - 定义系统/租户/用户级角色 服务实现类
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    private SysRoleConverter sysRoleConverter;

    /**
     * <p>
     * 查询角色列表
     * </p>
     * <p>
     * 返回所有角色的平铺列表。
     * 需要登录并具备角色查看权限才能访问。
     * </p>
     *
     * @return 角色列表
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    public List<SysRoleCommonVO> queryRoleList() {

        // 构建查询条件：仅查询未删除的角色，按创建时间倒序排列
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .orderByDesc(SysRole::getCreateTime);

        // 执行查询获取角色列表
        List<SysRole> roleList = this.list(wrapper);

        // 转换为VO对象并返回
        return sysRoleConverter.toVoList(roleList);
    }

    /**
     * <p>
     * 分页查询角色列表
     * </p>
     * <p>
     * 返回分页后的角色列表。
     * 需要登录并具备角色查看权限才能访问。
     * </p>
     *
     * @param page 分页参数
     * @return 分页后的角色列表
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限
     * @author shy
     * @since 2026-05-06
     */
    @Override
    public IPage<SysRoleCommonVO> queryRolePage(PageCommonRTO page) {

        // 构建分页参数
        Page<SysRole> pageParam = new Page<>(page.getPageNum(), page.getPageSize());

        // 构建查询条件：仅查询未删除的角色，按创建时间倒序排列
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .orderByDesc(SysRole::getCreateTime);

        // 执行分页查询
        IPage<SysRole> rolePage = this.page(pageParam, wrapper);

        // 转换为VO分页对象并返回
        return sysRoleConverter.toVOPage(rolePage);
    }

    /**
     * <p>
     * 条件查询\筛选角色列表
     * </p>
     * <p>
     * 返回满足条件的角色列表。
     * 需要登录并具备角色条件查询权限才能访问。
     * </p>
     *
     * @param queryParam 查询条件
     * @return 满足条件的角色列表
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    public IPage<SysRoleCommonVO> queryRole(SysRoleQueryRTO queryParam) {

        // 构建分页参数
        Page<SysRole> pageParam = new Page<>(queryParam.getPageNum(), queryParam.getPageSize());

        // 构建动态查询条件
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();

        // 角色名称模糊查询
        wrapper.like(StringUtils.isNotBlank(queryParam.getRoleName()),
                SysRole::getRoleName, queryParam.getRoleName());

        // 角色编码精确查询
        wrapper.eq(StringUtils.isNotBlank(queryParam.getRoleCode()),
                SysRole::getRoleCode, queryParam.getRoleCode());

        // 角色层级条件查询
        if (StringUtils.isNotBlank(queryParam.getRoleLevel())) {
            RoleLevel roleLevel = RoleLevel.parse(queryParam.getRoleLevel());
            if (roleLevel != null) {
                wrapper.eq(SysRole::getRoleLevel, roleLevel.getCode());
            }
        }

        // 所属租户ID条件查询
        wrapper.eq(StringUtils.isNotBlank(queryParam.getTenantId()),
                SysRole::getTenantId, Long.parseLong(queryParam.getTenantId()));

        // 所属租户名称模糊查询
        wrapper.like(StringUtils.isNotBlank(queryParam.getTenantName()),
                SysRole::getTenantName, queryParam.getTenantName());

        // 数据范围条件查询
        if (StringUtils.isNotBlank(queryParam.getDataScope())) {
            DataScope dataScope = DataScope.parse(queryParam.getDataScope());
            if (dataScope != null) {
                wrapper.eq(SysRole::getDataScope, dataScope.getCode());
            }
        }

        // 状态条件查询
        if (StringUtils.isNotBlank(queryParam.getStatus())) {
            Status roleStatus = Status.parse(queryParam.getStatus());
            if (roleStatus != null) {
                wrapper.eq(SysRole::getStatus, roleStatus.getCode());
            }
        }

        // 创建人姓名精确查询
        wrapper.eq(StringUtils.isNotBlank(queryParam.getCreateByName()),
                SysRole::getCreateByName, queryParam.getCreateByName());

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
                wrapper.between(SysRole::getCreateTime, startTime, endTime);
            } else if (startTime != null) {
                // 只有开始时间 大于等于查询
                wrapper.ge(SysRole::getCreateTime, startTime);
            } else if (endTime != null) {
                // 只有结束时间 小于等于查询
                wrapper.le(SysRole::getCreateTime, endTime);
            }
        }

        // 仅查询未删除的角色，按创建时间倒序排列
        wrapper.eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        wrapper.orderByDesc(SysRole::getCreateTime);

        // 执行条件分页查询
        IPage<SysRole> roleQueryPage = this.page(pageParam, wrapper);

        // 转换为VO分页对象并返回
        return sysRoleConverter.toVOPage(roleQueryPage);
    }

    /**
     * <p>
     * 查询角色详情
     * </p>
     * <p>
     * 返回指定角色的详情信息。
     * 需要登录并具备角色详情查询权限才能访问。
     * </p>
     *
     * @param roleCode 角色编码
     * @return 角色详情信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    public SysRoleDetailVO queryRoleDetail(String roleCode) {

        // 参数校验 角色编码不能为空
        if (StringUtils.isBlank(roleCode)) {
            throw new BusinessException(400, "角色编码不能为空");
        }

        // 构建查询条件：根据角色编码查询未删除的角色
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getRoleCode, roleCode)
                .eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());

        // 执行查询获取角色详情
        SysRole roleDetail = this.getOne(wrapper);

        // 角色不存在时抛出异常
        if (roleDetail == null) {
            throw new BusinessException(404, "角色不存在");
        }

        // 转换为详情VO对象并返回
        return sysRoleConverter.toDetailVO(roleDetail);
    }

    /**
     * <p>
     * 新增角色
     * </p>
     * <p>
     * 新增角色信息，需要登录并具备角色新增权限才能访问。
     * </p>
     *
     * @param addParam 新增角色信息
     * @return 新增影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或新增失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    public Integer addRole(SysRoleAddRTO addParam) {

        // 校验角色编码是否已存在
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getRoleCode, addParam.getRoleCode());

        long count = this.count(wrapper);

        if (count > 0) {
            throw new BusinessException(400, "角色编码已存在");
        }

        // 转换并保存角色信息
        boolean result = this.save(sysRoleConverter.toEntityAdd(addParam));

        if (!result) {
            throw new BusinessException(500, "新增角色失败");
        }

        return 1;
    }

    /**
     * <p>
     * 修改角色
     * </p>
     * <p>
     * 修改角色信息，需要登录并具备角色修改权限才能访问。
     * 仅允许修改指定角色的有效配置信息，不允许修改角色唯一编码。
     * </p>
     *
     * @param updateParam 修改角色信息
     * @return 修改影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、角色不存在或修改失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateRole(SysRoleUpdateRTO updateParam) {

        // 查询待修改的角色是否存在
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getId, updateParam.getId())
                .eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysRole existRole = this.getOne(wrapper);

        if (existRole == null) {
            throw new BusinessException(404, "角色不存在");
        }

        // 如果修改了角色编码, 需校验新编码是否已被其他角色使用
        if (!existRole.getRoleCode().equals(updateParam.getRoleCode())) {
            LambdaQueryWrapper<SysRole> codeWrapper = new LambdaQueryWrapper<SysRole>()
                    .eq(SysRole::getRoleCode, updateParam.getRoleCode())
                    .eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
            long count = this.count(codeWrapper);
            if (count > 0) {
                throw new BusinessException(400, "角色编码已被其他角色使用");
            }
        }

        // 转换并更新角色信息
        SysRole role = sysRoleConverter.toEntityUpdate(updateParam);

        boolean result = this.updateById(role);

        if (!result) {
            throw new BusinessException(500, "修改角色失败");
        }

        return 1;
    }

    /**
     * <p>
     * 更新角色状态
     * </p>
     * <p>
     * 更新指定角色的状态（启用/禁用），禁用后该角色将不可被分配。
     * 需要登录并具备角色修改权限才能访问。
     * </p>
     *
     * @param id 角色ID
     * @param status 角色状态（启用/禁用）
     * @return 更新结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、角色不存在或更新失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    public Integer updateRoleStatus(String id, String status) {

        // 参数校验：角色ID不能为空
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(400, "角色ID不能为空");
        }

        // 参数校验：状态不能为空
        if (StringUtils.isBlank(status)) {
            throw new BusinessException(400, "状态不能为空");
        }

        // 校验状态值是否合法
        GlobalEnum.Status roleStatus = GlobalEnum.Status.getByCode(Integer.parseInt(status));
        if (roleStatus == null) {
            // 尝试通过描述解析状态
            if ("启用".equals(status)) {
                roleStatus = GlobalEnum.Status.ENABLE;
            } else if ("禁用".equals(status)) {
                roleStatus = GlobalEnum.Status.DISABLE;
            } else {
                throw new BusinessException(400, "状态值不合法，仅支持：启用、禁用");
            }
        }

        // 查询待更新状态的角色是否存在
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getId, Long.parseLong(id))
                .eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysRole existRole = this.getOne(wrapper);

        if (existRole == null) {
            throw new BusinessException(400, "角色不存在");
        }

        // 如果状态未变更则直接返回
        if (roleStatus.getCode().equals(existRole.getStatus())) {
            throw new BusinessException(400, "角色状态未变更");
        }

        // 执行状态更新
        SysRole updateRole = new SysRole();
        updateRole.setId(Long.parseLong(id));
        updateRole.setStatus(roleStatus.getCode());

        boolean result = this.updateById(updateRole);

        if (!result) {
            throw new BusinessException(500, "更新角色状态失败");
        }

        return 1;
    }

    /**
     * <p>
     * 删除角色
     * </p>
     * <p>
     * 删除指定角色信息，需要登录并具备角色删除权限才能访问。
     * 删除操作不可逆，删除后角色相关数据将同步清理。
     * </p>
     *
     * @param id 角色ID
     * @return 删除影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、角色不存在或删除失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    public Integer deleteRole(String id) {

        // 查询待删除的角色是否存在
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getId, id)
                .eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysRole existRole = this.getOne(wrapper);

        if (existRole == null) {
            throw new BusinessException(400, "角色不存在");
        }

        // 执行逻辑删除：设置is_deleted标志位
        SysRole role = new SysRole();
        role.setId(Long.parseLong(id));
        role.setIsDeleted(GlobalEnum.Deleted.DELETED.getCode());

        boolean result = this.updateById(role);

        if (!result) {
            throw new BusinessException(500, "删除角色失败");
        }

        return 1;
    }

    /**
     * <p>
     * 批量新增角色
     * </p>
     * <p>
     * 批量新增多个角色信息，需要登录并具备角色新增权限才能访问。
     * 批量操作支持事务回滚，任一角色新增失败则全部失败。
     * </p>
     *
     * @param addParamList 批量新增角色信息集合
     * @return 新增影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、参数校验失败或新增失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchAddRole(List<SysRoleAddRTO> addParamList) {

        // 校验批量新增数量限制
        if (addParamList.size() > 100) {
            throw new BusinessException(400, "单次批量新增数量不能超过100条");
        }

        // 校验批量数据中是否有重复的角色编码
        Set<String> codeSet = new HashSet<>();
        for (SysRoleAddRTO param : addParamList) {
            if (codeSet.contains(param.getRoleCode())) {
                throw new BusinessException(400, "批量新增中存在重复的角色编码: " + param.getRoleCode());
            }
            codeSet.add(param.getRoleCode());
        }

        // 校验角色编码是否已在数据库中存在
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<SysRole>()
                .in(SysRole::getRoleCode, codeSet);
        long existCount = this.count(wrapper);

        if (existCount > 0) {
            throw new BusinessException(400, "部分角色编码已存在，请检查后重试");
        }

        // 批量保存角色信息
        boolean batch = this.saveBatch(sysRoleConverter.toEntityListAdd(addParamList));

        if (!batch) {
            throw new BusinessException(500, "批量新增角色失败");
        }

        return addParamList.size();
    }

    /**
     * <p>
     * 批量修改角色
     * </p>
     * <p>
     * 批量修改多个角色信息，需要登录并具备角色修改权限才能访问。
     * </p>
     *
     * @param updateParamList 批量修改角色信息集合
     * @return 修改影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、角色不存在或修改失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdateRole(List<SysRoleUpdateRTO> updateParamList) {

        // 校验批量修改数量限制
        if (updateParamList.size() > 100) {
            throw new BusinessException(400, "单次批量修改数量不能超过100条");
        }

        // 校验批量数据的合法性：ID不能为空、不能有重复的ID和角色编码
        Set<Long> idSet = new HashSet<>();
        Set<String> codeSet = new HashSet<>();
        for (SysRoleUpdateRTO item : updateParamList) {
            if (item.getId() == null) {
                throw new BusinessException(400, "批量修改中存在ID为空的记录");
            }
            if (idSet.contains(item.getId())) {
                throw new BusinessException(400, "批量修改中存在重复的角色ID: " + item.getId());
            }
            if (codeSet.contains(item.getRoleCode())) {
                throw new BusinessException(400, "批量修改中存在重复的角色编码: " + item.getRoleCode());
            }
            idSet.add(item.getId());
            codeSet.add(item.getRoleCode());
        }

        // 校验角色ID是否全部存在且未被删除
        LambdaQueryWrapper<SysRole> idWrapper = new LambdaQueryWrapper<SysRole>()
                .in(SysRole::getId, idSet)
                .eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        long existIdCount = this.count(idWrapper);

        if (existIdCount != idSet.size()) {
            throw new BusinessException(400, "部分角色ID不存在或已删除，请检查后重试");
        }

        // 校验角色编码是否已被其他角色使用（排除自身）
        LambdaQueryWrapper<SysRole> codeWrapper = new LambdaQueryWrapper<SysRole>()
                .in(SysRole::getRoleCode, codeSet)
                .notIn(SysRole::getId, idSet)
                .eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        long existCodeCount = this.count(codeWrapper);

        if (existCodeCount > 0) {
            throw new BusinessException(400, "部分角色编码已被其他角色使用，请检查后重试");
        }

        // 批量更新角色信息
        boolean batch = this.updateBatchById(sysRoleConverter.toEntityListUpdate(updateParamList));

        if (!batch) {
            throw new BusinessException(500, "批量更新角色失败");
        }

        return updateParamList.size();
    }

    /**
     * <p>
     * 批量更新角色状态
     * </p>
     * <p>
     * 批量更新多个指定角色的状态（启用/禁用），禁用后该角色将不可被分配。
     * 批量操作支持事务回滚，任一角色更新失败则全部失败。
     * 需要登录并具备角色修改权限才能访问。
     * </p>
     *
     * @param ids 角色ID集合
     * @param status 角色状态（启用/禁用）
     * @return 更新结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、角色不存在或更新失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdateRoleStatus(List<String> ids, String status) {

        // 校验批量更新数量限制
        if (ids.size() > 100) {
            throw new BusinessException(400, "单次批量更新数量不能超过100条");
        }

        // 参数校验：状态不能为空
        if (StringUtils.isBlank(status)) {
            throw new BusinessException(400, "状态不能为空");
        }

        // 校验状态值是否合法
        GlobalEnum.Status roleStatus = GlobalEnum.Status.getByCode(Integer.parseInt(status));
        if (roleStatus == null) {
            // 尝试通过描述解析状态
            if ("启用".equals(status)) {
                roleStatus = GlobalEnum.Status.ENABLE;
            } else if ("禁用".equals(status)) {
                roleStatus = GlobalEnum.Status.DISABLE;
            } else {
                throw new BusinessException(400, "状态值不合法");
            }
        }

        // 校验ID格式并转换为Long类型
        Set<Long> idSet = new HashSet<>();
        for (String id : ids) {
            if (StringUtils.isBlank(id)) {
                throw new BusinessException(400, "角色ID不能为空");
            }
            idSet.add(Long.parseLong(id));
        }

        // 查询待更新状态的角色是否存在且未被删除
        LambdaQueryWrapper<SysRole> existWrapper = new LambdaQueryWrapper<SysRole>()
                .in(SysRole::getId, idSet)
                .eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        List<SysRole> existRoles = this.list(existWrapper);

        if (existRoles.isEmpty()) {
            throw new BusinessException(404, "未找到可更新状态的角色");
        }

        // 构建批量状态更新的数据列表
        List<SysRole> updateList = new ArrayList<>();
        for (SysRole role : existRoles) {
            // 跳过状态未变更的角色
            if (roleStatus.getCode().equals(role.getStatus())) {
                continue;
            }
            SysRole updateRole = new SysRole();
            updateRole.setId(role.getId());
            updateRole.setStatus(roleStatus.getCode());
            updateList.add(updateRole);
        }

        if (updateList.isEmpty()) {
            throw new BusinessException(400, "所有角色状态均未变更");
        }

        // 执行批量状态更新
        boolean batch = this.updateBatchById(updateList);

        if (!batch) {
            throw new BusinessException(500, "批量更新角色状态失败");
        }

        return updateList.size();
    }

    /**
     * <p>
     * 批量删除角色
     * </p>
     * <p>
     * 批量删除多个指定角色信息，需要登录并具备角色删除权限才能访问。
     * </p>
     *
     * @param ids 角色ID集合
     * @return 删除影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、角色不存在或删除失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchDeleteRole(List<String> ids) {

        // 校验批量删除数量限制
        if (ids.size() > 100) {
            throw new BusinessException(400, "单次批量删除数量不能超过100条");
        }

        // 校验ID格式并转换为Long类型
        Set<Long> idSet = new HashSet<>();
        for (String id : ids) {
            if (StringUtils.isBlank(id)) {
                throw new BusinessException(400, "角色ID不能为空");
            }
            idSet.add(Long.parseLong(id));
        }

        // 查询待删除的角色是否存在且未被删除
        LambdaQueryWrapper<SysRole> existWrapper = new LambdaQueryWrapper<SysRole>()
                .in(SysRole::getId, idSet)
                .eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        List<SysRole> existRoles = this.list(existWrapper);

        if (existRoles.isEmpty()) {
            throw new BusinessException(404, "未找到可删除的角色");
        }

        // 构建批量逻辑删除的数据列表
        List<SysRole> roleList = new ArrayList<>();
        for (SysRole role : existRoles) {
            SysRole deleteRole = new SysRole();
            deleteRole.setId(role.getId());
            deleteRole.setIsDeleted(GlobalEnum.Deleted.DELETED.getCode());
            roleList.add(deleteRole);
        }

        // 执行批量逻辑删除
        boolean batch = this.updateBatchById(roleList);

        if (!batch) {
            throw new BusinessException(500, "批量删除角色失败");
        }

        return ids.size();
    }

}
