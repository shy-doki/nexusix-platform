package com.shy.nexusix.iam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.common.rto.TimeRangeCommonRTO;
import com.shy.nexusix.iam.converter.SysPermissionConverter;
import com.shy.nexusix.iam.entity.SysPermission;
import com.shy.nexusix.iam.mapper.SysPermissionMapper;
import com.shy.nexusix.iam.rto.SysPermissionAddRTO;
import com.shy.nexusix.iam.rto.SysPermissionQueryRTO;
import com.shy.nexusix.iam.rto.SysPermissionUpdateRTO;
import com.shy.nexusix.iam.service.ISysPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shy.nexusix.iam.vo.SysPermissionCommonVO;
import com.shy.nexusix.iam.vo.SysPermissionDetailVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 权限/资源表 - 定义系统所有可授权资源 服务实现类
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {

    @Autowired
    private SysPermissionConverter sysPermissionConverter;

    /**
     * <p>
     * 查询权限列表
     * </p>
     * <p>
     * 返回所有权限的平铺列表。
     * 需要登录并具备权限查看权限才能访问。
     * </p>
     *
     * @return 权限列表，包含权限名称、权限标识、类型、状态等信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    public List<SysPermissionCommonVO> queryPermissionList() {

        // 构建查询条件：仅查询未删除的权限，按创建时间倒序排列
        LambdaQueryWrapper<SysPermission> wrapper = new LambdaQueryWrapper<SysPermission>()
                .eq(SysPermission::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .orderByDesc(SysPermission::getCreateTime);

        // 执行查询获取权限列表
        List<SysPermission> permissionList = this.list(wrapper);

        // 转换为VO对象并返回
        return sysPermissionConverter.toVoList(permissionList);
    }

    /**
     * <p>
     * 分页查询权限列表
     * </p>
     * <p>
     * 返回分页后的权限列表。
     * 需要登录并具备权限查看权限才能访问。
     * </p>
     *
     * @param page 分页参数
     * @return 分页后的权限列表，包含权限名称、权限标识、类型、状态等信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限
     * @author shy
     * @since 2026-05-06
     */
    @Override
    public IPage<SysPermissionCommonVO> queryPermissionPage(PageCommonRTO page) {

        // 构建分页参数
        Page<SysPermission> pageParam = new Page<>(page.getPageNum(), page.getPageSize());

        // 构建查询条件：仅查询未删除的权限，按创建时间倒序排列
        LambdaQueryWrapper<SysPermission> wrapper = new LambdaQueryWrapper<SysPermission>()
                .eq(SysPermission::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .orderByDesc(SysPermission::getCreateTime);

        // 执行分页查询
        IPage<SysPermission> permissionPage = this.page(pageParam, wrapper);

        // 转换为VO分页对象并返回
        return sysPermissionConverter.toVOPage(permissionPage);
    }

    /**
     * <p>
     * 条件查询\筛选权限列表
     * </p>
     * <p>
     * 返回满足条件的权限列表。
     * 需要登录并具备权限条件查询权限才能访问。
     * </p>
     *
     * @param queryParam 查询条件
     * @return 满足条件的权限列表，包含权限名称、权限标识、类型、状态等信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    public IPage<SysPermissionCommonVO> queryPermission(SysPermissionQueryRTO queryParam) {

        // 构建分页参数
        Page<SysPermission> pageParam = new Page<>(queryParam.getPageNum(), queryParam.getPageSize());

        // 构建动态查询条件
        LambdaQueryWrapper<SysPermission> wrapper = new LambdaQueryWrapper<>();

        // 权限名称模糊查询
        wrapper.like(StringUtils.isNotBlank(queryParam.getPermName()),
                SysPermission::getPermName, queryParam.getPermName());

        // 权限标识精确查询
        wrapper.eq(StringUtils.isNotBlank(queryParam.getPermCode()),
                SysPermission::getPermCode, queryParam.getPermCode());

        // 权限类型条件查询
        wrapper.eq(queryParam.getPermType() != null,
                SysPermission::getPermType, queryParam.getPermType());

        // 父权限ID条件查询
        wrapper.eq(StringUtils.isNotBlank(queryParam.getParentId()),
                SysPermission::getParentId, Long.parseLong(queryParam.getParentId()));

        // 状态条件查询
        if (StringUtils.isNotBlank(queryParam.getStatus())) {
            GlobalEnum.Status permStatus = GlobalEnum.Status.getByCode(Integer.parseInt(queryParam.getStatus()));
            if (permStatus != null) {
                wrapper.eq(SysPermission::getStatus, permStatus.getCode());
            }
        }

        // 创建人姓名精确查询
        wrapper.eq(StringUtils.isNotBlank(queryParam.getCreateByName()),
                SysPermission::getCreateByName, queryParam.getCreateByName());

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
                // 两者都有 between查询
                wrapper.between(SysPermission::getCreateTime, startTime, endTime);
            } else if (startTime != null) {
                // 只有开始时间 大于等于查询
                wrapper.ge(SysPermission::getCreateTime, startTime);
            } else if (endTime != null) {
                // 只有结束时间 小于等于查询
                wrapper.le(SysPermission::getCreateTime, endTime);
            }
        }

        // 仅查询未删除的权限，按创建时间倒序排列
        wrapper.eq(SysPermission::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        wrapper.orderByDesc(SysPermission::getCreateTime);

        // 执行条件分页查询
        IPage<SysPermission> permissionQueryPage = this.page(pageParam, wrapper);

        // 转换为VO分页对象并返回
        return sysPermissionConverter.toVOPage(permissionQueryPage);
    }

    /**
     * <p>
     * 查询权限详情
     * </p>
     * <p>
     * 返回指定权限的详情信息。
     * 需要登录并具备权限详情查询权限才能访问。
     * </p>
     *
     * @param permCode 权限标识
     * @return 权限详情信息，包含权限名称、权限标识、类型、状态等信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    public SysPermissionDetailVO queryPermissionDetail(String permCode) {

        // 参数校验 权限标识不能为空
        if (StringUtils.isBlank(permCode)) {
            throw new BusinessException(400, "权限标识不能为空");
        }

        // 构建查询条件：根据权限标识查询未删除的权限
        LambdaQueryWrapper<SysPermission> wrapper = new LambdaQueryWrapper<SysPermission>()
                .eq(SysPermission::getPermCode, permCode)
                .eq(SysPermission::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());

        // 执行查询获取权限详情
        SysPermission permissionDetail = this.getOne(wrapper);

        // 权限不存在时抛出异常
        if (permissionDetail == null) {
            throw new BusinessException(404, "权限不存在");
        }

        // 转换为详情VO对象并返回
        return sysPermissionConverter.toDetailVO(permissionDetail);
    }

    /**
     * <p>
     * 新增权限
     * </p>
     * <p>
     * 新增权限信息，需要登录并具备权限新增权限才能访问。
     * </p>
     *
     * @param addParam 新增权限信息
     * @return 新增影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或新增失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    public Integer addPermission(SysPermissionAddRTO addParam) {

        // 校验权限标识是否已存在
        LambdaQueryWrapper<SysPermission> wrapper = new LambdaQueryWrapper<SysPermission>()
                .eq(SysPermission::getPermCode, addParam.getPermCode());

        long count = this.count(wrapper);

        if (count > 0) {
            throw new BusinessException(400, "权限标识已存在");
        }

        // 转换并保存权限信息
        boolean result = this.save(sysPermissionConverter.toEntityAdd(addParam));

        if (!result) {
            throw new BusinessException(500, "新增权限失败");
        }

        return 1;
    }

    /**
     * <p>
     * 修改权限
     * </p>
     * <p>
     * 修改权限信息，需要登录并具备权限修改权限才能访问。
     * 仅允许修改指定权限的有效配置信息，不允许修改权限唯一标识。
     * </p>
     *
     * @param updateParam 修改权限信息
     * @return 修改影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、权限不存在或修改失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updatePermission(SysPermissionUpdateRTO updateParam) {

        // 查询待修改的权限是否存在
        LambdaQueryWrapper<SysPermission> wrapper = new LambdaQueryWrapper<SysPermission>()
                .eq(SysPermission::getId, updateParam.getId())
                .eq(SysPermission::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysPermission existPermission = this.getOne(wrapper);

        if (existPermission == null) {
            throw new BusinessException(404, "权限不存在");
        }

        // 如果修改了权限标识, 需校验新标识是否已被其他权限使用
        if (!existPermission.getPermCode().equals(updateParam.getPermCode())) {
            LambdaQueryWrapper<SysPermission> codeWrapper = new LambdaQueryWrapper<SysPermission>()
                    .eq(SysPermission::getPermCode, updateParam.getPermCode())
                    .eq(SysPermission::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
            long count = this.count(codeWrapper);
            if (count > 0) {
                throw new BusinessException(400, "权限标识已被其他权限使用");
            }
        }

        // 转换并更新权限信息
        SysPermission permission = sysPermissionConverter.toEntityUpdate(updateParam);

        boolean result = this.updateById(permission);

        if (!result) {
            throw new BusinessException(500, "修改权限失败");
        }

        return 1;
    }

    /**
     * <p>
     * 更新权限状态
     * </p>
     * <p>
     * 更新指定权限的状态（启用/禁用），禁用后该权限将不可被分配。
     * 需要登录并具备权限修改权限才能访问。
     * </p>
     *
     * @param id 权限ID
     * @param status 权限状态（启用/禁用）
     * @return 更新结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、权限不存在或更新失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    public Integer updatePermissionStatus(String id, String status) {

        // 参数校验：权限ID不能为空
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(400, "权限ID不能为空");
        }

        // 参数校验：状态不能为空
        if (StringUtils.isBlank(status)) {
            throw new BusinessException(400, "状态不能为空");
        }

        // 校验状态值是否合法
        GlobalEnum.Status permStatus = GlobalEnum.Status.getByCode(Integer.parseInt(status));
        if (permStatus == null) {
            // 尝试通过描述解析状态
            if ("启用".equals(status)) {
                permStatus = GlobalEnum.Status.ENABLE;
            } else if ("禁用".equals(status)) {
                permStatus = GlobalEnum.Status.DISABLE;
            } else {
                throw new BusinessException(400, "状态值不合法，仅支持：启用、禁用");
            }
        }

        // 查询待更新状态的权限是否存在
        LambdaQueryWrapper<SysPermission> wrapper = new LambdaQueryWrapper<SysPermission>()
                .eq(SysPermission::getId, Long.parseLong(id))
                .eq(SysPermission::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysPermission existPermission = this.getOne(wrapper);

        if (existPermission == null) {
            throw new BusinessException(400, "权限不存在");
        }

        // 如果状态未变更则直接返回
        if (permStatus.getCode().equals(existPermission.getStatus())) {
            throw new BusinessException(400, "权限状态未变更");
        }

        // 执行状态更新
        SysPermission updatePermission = new SysPermission();
        updatePermission.setId(Long.parseLong(id));
        updatePermission.setStatus(permStatus.getCode());

        boolean result = this.updateById(updatePermission);

        if (!result) {
            throw new BusinessException(500, "更新权限状态失败");
        }

        return 1;
    }

    /**
     * <p>
     * 删除权限
     * </p>
     * <p>
     * 删除指定权限信息，需要登录并具备权限删除权限才能访问。
     * 删除操作不可逆，删除后权限相关数据将同步清理。
     * </p>
     *
     * @param id 权限ID
     * @return 删除影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、权限不存在或删除失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    public Integer deletePermission(String id) {

        // 查询待删除的权限是否存在
        LambdaQueryWrapper<SysPermission> wrapper = new LambdaQueryWrapper<SysPermission>()
                .eq(SysPermission::getId, id)
                .eq(SysPermission::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysPermission existPermission = this.getOne(wrapper);
        if (existPermission == null) {
            throw new BusinessException(400, "权限不存在");
        }

        // 执行逻辑删除：设置is_deleted标志位
        SysPermission permission = new SysPermission();
        permission.setId(Long.parseLong(id));
        permission.setIsDeleted(GlobalEnum.Deleted.DELETED.getCode());

        boolean result = this.updateById(permission);

        if (!result) {
            throw new BusinessException(500, "删除权限失败");
        }

        return 1;
    }

    /**
     * <p>
     * 批量新增权限
     * </p>
     * <p>
     * 批量新增多个权限信息，需要登录并具备权限新增权限才能访问。
     * 批量操作支持事务回滚，任一权限新增失败则全部失败。
     * </p>
     *
     * @param addParamList 批量新增权限信息集合
     * @return 新增影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、参数校验失败或新增失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchAddPermission(List<SysPermissionAddRTO> addParamList) {

        // 校验批量新增数量限制
        if (addParamList.size() > 100) {
            throw new BusinessException(400, "单次批量新增数量不能超过100条");
        }

        // 校验批量数据中是否有重复的权限标识
        Set<String> codeSet = new HashSet<>();
        for (SysPermissionAddRTO param : addParamList) {
            if (codeSet.contains(param.getPermCode())) {
                throw new BusinessException(400, "批量新增中存在重复的权限标识: " + param.getPermCode());
            }
            codeSet.add(param.getPermCode());
        }

        // 校验权限标识是否已在数据库中存在
        LambdaQueryWrapper<SysPermission> wrapper = new LambdaQueryWrapper<SysPermission>()
                .in(SysPermission::getPermCode, codeSet);
        long existCount = this.count(wrapper);

        if (existCount > 0) {
            throw new BusinessException(400, "部分权限标识已存在，请检查后重试");
        }

        // 批量保存权限信息
        boolean batch = this.saveBatch(sysPermissionConverter.toEntityListAdd(addParamList));

        if (!batch) {
            throw new BusinessException(500, "批量新增权限失败");
        }

        return addParamList.size();
    }

    /**
     * <p>
     * 批量修改权限
     * </p>
     * <p>
     * 批量修改多个权限信息，需要登录并具备权限修改权限才能访问。
     * 仅允许修改指定权限的有效配置信息，不允许修改权限唯一标识。
     * </p>
     *
     * @param updateParamList 批量修改权限信息集合
     * @return 修改影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、权限不存在或修改失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdatePermission(List<SysPermissionUpdateRTO> updateParamList) {

        // 校验批量修改数量限制
        if (updateParamList.size() > 100) {
            throw new BusinessException(400, "单次批量修改数量不能超过100条");
        }

        // 校验批量数据的合法性：ID不能为空、不能有重复的ID和权限标识
        Set<Long> idSet = new HashSet<>();
        Set<String> codeSet = new HashSet<>();
        for (SysPermissionUpdateRTO item : updateParamList) {
            if (item.getId() == null) {
                throw new BusinessException(400, "批量修改中存在ID为空的记录");
            }
            if (idSet.contains(item.getId())) {
                throw new BusinessException(400, "批量修改中存在重复的权限ID: " + item.getId());
            }
            if (codeSet.contains(item.getPermCode())) {
                throw new BusinessException(400, "批量修改中存在重复的权限标识: " + item.getPermCode());
            }
            idSet.add(item.getId());
            codeSet.add(item.getPermCode());
        }

        // 校验权限ID是否全部存在且未被删除
        LambdaQueryWrapper<SysPermission> idWrapper = new LambdaQueryWrapper<SysPermission>()
                .in(SysPermission::getId, idSet)
                .eq(SysPermission::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        long existIdCount = this.count(idWrapper);

        if (existIdCount != idSet.size()) {
            throw new BusinessException(400, "部分权限ID不存在或已删除，请检查后重试");
        }

        // 校验权限标识是否已被其他权限使用（排除自身）
        LambdaQueryWrapper<SysPermission> codeWrapper = new LambdaQueryWrapper<SysPermission>()
                .in(SysPermission::getPermCode, codeSet)
                .notIn(SysPermission::getId, idSet)
                .eq(SysPermission::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        long existCodeCount = this.count(codeWrapper);

        if (existCodeCount > 0) {
            throw new BusinessException(400, "部分权限标识已被其他权限使用，请检查后重试");
        }

        // 批量更新权限信息
        boolean batch = this.updateBatchById(sysPermissionConverter.toEntityListUpdate(updateParamList));

        if (!batch) {
            throw new BusinessException(500, "批量更新权限失败");
        }

        return updateParamList.size();
    }

    /**
     * <p>
     * 批量更新权限状态
     * </p>
     * <p>
     * 批量更新多个指定权限的状态（启用/禁用），禁用后该权限将不可被分配。
     * 批量操作支持事务回滚，任一权限更新失败则全部失败。
     * 需要登录并具备权限修改权限才能访问。
     * </p>
     *
     * @param ids 权限ID集合
     * @param status 权限状态（启用/禁用）
     * @return 更新结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、权限不存在或更新失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdatePermissionStatus(List<String> ids, String status) {

        // 校验批量更新数量限制
        if (ids.size() > 100) {
            throw new BusinessException(400, "单次批量更新数量不能超过100条");
        }

        // 参数校验：状态不能为空
        if (StringUtils.isBlank(status)) {
            throw new BusinessException(400, "状态不能为空");
        }

        // 校验状态值是否合法
        GlobalEnum.Status permStatus = GlobalEnum.Status.getByCode(Integer.parseInt(status));
        if (permStatus == null) {
            // 尝试通过描述解析状态
            if ("启用".equals(status)) {
                permStatus = GlobalEnum.Status.ENABLE;
            } else if ("禁用".equals(status)) {
                permStatus = GlobalEnum.Status.DISABLE;
            } else {
                throw new BusinessException(400, "状态值不合法");
            }
        }

        // 校验ID格式并转换为Long类型
        Set<Long> idSet = new HashSet<>();
        for (String id : ids) {
            if (StringUtils.isBlank(id)) {
                throw new BusinessException(400, "权限ID不能为空");
            }
            idSet.add(Long.parseLong(id));
        }

        // 查询待更新状态的权限是否存在且未被删除
        LambdaQueryWrapper<SysPermission> existWrapper = new LambdaQueryWrapper<SysPermission>()
                .in(SysPermission::getId, idSet)
                .eq(SysPermission::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        List<SysPermission> existPermissions = this.list(existWrapper);

        if (existPermissions.isEmpty()) {
            throw new BusinessException(404, "未找到可更新状态的权限");
        }

        // 构建批量状态更新的数据列表
        List<SysPermission> updateList = new ArrayList<>();
        for (SysPermission permission : existPermissions) {
            // 跳过状态未变更的权限
            if (permStatus.getCode().equals(permission.getStatus())) {
                continue;
            }
            SysPermission updatePermission = new SysPermission();
            updatePermission.setId(permission.getId());
            updatePermission.setStatus(permStatus.getCode());
            updateList.add(updatePermission);
        }

        if (updateList.isEmpty()) {
            throw new BusinessException(400, "所有权限状态均未变更");
        }

        // 执行批量状态更新
        boolean batch = this.updateBatchById(updateList);

        if (!batch) {
            throw new BusinessException(500, "批量更新权限状态失败");
        }

        return updateList.size();
    }

    /**
     * <p>
     * 批量删除权限
     * </p>
     * <p>
     * 批量删除多个指定权限信息，需要登录并具备权限删除权限才能访问。
     * 删除操作不可逆，删除后权限相关数据将同步清理。
     * </p>
     *
     * @param ids 权限ID集合
     * @return 删除影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、权限不存在或删除失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchDeletePermission(List<String> ids) {

        // 校验批量删除数量限制
        if (ids.size() > 100) {
            throw new BusinessException(400, "单次批量删除数量不能超过100条");
        }

        // 校验ID格式并转换为Long类型
        Set<Long> idSet = new HashSet<>();
        for (String id : ids) {
            if (StringUtils.isBlank(id)) {
                throw new BusinessException(400, "权限ID不能为空");
            }
            idSet.add(Long.parseLong(id));
        }

        // 查询待删除的权限是否存在且未被删除
        LambdaQueryWrapper<SysPermission> existWrapper = new LambdaQueryWrapper<SysPermission>()
                .in(SysPermission::getId, idSet)
                .eq(SysPermission::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        List<SysPermission> existPermissions = this.list(existWrapper);

        if (existPermissions.isEmpty()) {
            throw new BusinessException(404, "未找到可删除的权限");
        }

        // 构建批量逻辑删除的数据列表
        List<SysPermission> permissionList = new ArrayList<>();
        for (SysPermission permission : existPermissions) {
            SysPermission deletePermission = new SysPermission();
            deletePermission.setId(permission.getId());
            deletePermission.setIsDeleted(GlobalEnum.Deleted.DELETED.getCode());
            permissionList.add(deletePermission);
        }

        // 执行批量逻辑删除
        boolean batch = this.updateBatchById(permissionList);

        if (!batch) {
            throw new BusinessException(500, "批量删除权限失败");
        }

        return ids.size();
    }

}
