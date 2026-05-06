package com.shy.nexusix.iam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.common.rto.TimeRangeCommonRTO;
import com.shy.nexusix.iam.converter.SysUserConverter;
import com.shy.nexusix.iam.entity.SysUser;
import com.shy.nexusix.iam.mapper.SysUserMapper;
import com.shy.nexusix.iam.rto.SysUserAddRTO;
import com.shy.nexusix.iam.rto.SysUserQueryRTO;
import com.shy.nexusix.iam.rto.SysUserUpdateRTO;
import com.shy.nexusix.iam.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shy.nexusix.iam.vo.SysUserCommonVO;
import com.shy.nexusix.iam.vo.SysUserDetailVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 用户基础表 - 存储全局用户信息 (不区分租户) 服务实现类
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private SysUserConverter sysUserConverter;

    /**
     * <p>
     * 查询用户列表
     * </p>
     * <p>
     * 返回所有用户的平铺列表。
     * 需要登录并具备用户查看权限才能访问。
     * </p>
     *
     * @return 用户列表
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    public List<SysUserCommonVO> queryUserList() {

        // 构建查询条件：仅查询未删除的用户，按创建时间倒序排列
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .orderByDesc(SysUser::getCreateTime);

        // 执行查询获取用户列表
        List<SysUser> userList = this.list(wrapper);

        // 转换为VO对象并返回
        return sysUserConverter.toVoList(userList);
    }

    /**
     * <p>
     * 分页查询用户列表
     * </p>
     * <p>
     * 返回分页后的用户列表。
     * 需要登录并具备用户查看权限才能访问。
     * </p>
     *
     * @param page 分页参数
     * @return 分页后的用户列表
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限
     * @author shy
     * @since 2026-05-06
     */
    @Override
    public IPage<SysUserCommonVO> queryUserPage(PageCommonRTO page) {

        // 构建分页参数
        Page<SysUser> pageParam = new Page<>(page.getPageNum(), page.getPageSize());

        // 构建查询条件：仅查询未删除的用户，按创建时间倒序排列
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .orderByDesc(SysUser::getCreateTime);

        // 执行分页查询
        IPage<SysUser> userPage = this.page(pageParam, wrapper);

        // 转换为VO分页对象并返回
        return sysUserConverter.toVOPage(userPage);
    }

    /**
     * <p>
     * 条件查询\筛选用户列表
     * </p>
     * <p>
     * 返回满足条件的用户列表。
     * 需要登录并具备用户条件查询权限才能访问。
     * </p>
     *
     * @param queryParam 查询条件
     * @return 满足条件的用户列表
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    public IPage<SysUserCommonVO> queryUser(SysUserQueryRTO queryParam) {

        // 构建分页参数
        Page<SysUser> pageParam = new Page<>(queryParam.getPageNum(), queryParam.getPageSize());

        // 构建动态查询条件
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();

        // 用户名模糊查询
        wrapper.like(StringUtils.isNotBlank(queryParam.getUsername()),
                SysUser::getUsername, queryParam.getUsername());

        // 昵称模糊查询
        wrapper.like(StringUtils.isNotBlank(queryParam.getNickname()),
                SysUser::getNickname, queryParam.getNickname());

        // 邮箱精确查询
        wrapper.eq(StringUtils.isNotBlank(queryParam.getEmail()),
                SysUser::getEmail, queryParam.getEmail());

        // 手机号精确查询
        wrapper.eq(StringUtils.isNotBlank(queryParam.getPhone()),
                SysUser::getPhone, queryParam.getPhone());

        // 状态条件查询
        if (StringUtils.isNotBlank(queryParam.getStatus())) {
            GlobalEnum.Status userStatus = GlobalEnum.Status.getByCode(Integer.parseInt(queryParam.getStatus()));
            if (userStatus != null) {
                wrapper.eq(SysUser::getStatus, userStatus.getCode());
            }
        }

        // 创建人姓名精确查询
        wrapper.eq(StringUtils.isNotBlank(queryParam.getCreateByName()),
                SysUser::getCreateByName, queryParam.getCreateByName());

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
                wrapper.between(SysUser::getCreateTime, startTime, endTime);
            } else if (startTime != null) {
                // 只有开始时间 大于等于查询
                wrapper.ge(SysUser::getCreateTime, startTime);
            } else if (endTime != null) {
                // 只有结束时间 小于等于查询
                wrapper.le(SysUser::getCreateTime, endTime);
            }
        }

        // 仅查询未删除的用户，按创建时间倒序排列
        wrapper.eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        wrapper.orderByDesc(SysUser::getCreateTime);

        // 执行条件分页查询
        IPage<SysUser> userQueryPage = this.page(pageParam, wrapper);

        // 转换为VO分页对象并返回
        return sysUserConverter.toVOPage(userQueryPage);
    }

    /**
     * <p>
     * 查询用户详情
     * </p>
     * <p>
     * 返回指定用户的详情信息。
     * 需要登录并具备用户详情查询权限才能访问。
     * </p>
     *
     * @param username 用户名
     * @return 用户详情信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    public SysUserDetailVO queryUserDetail(String username) {

        // 参数校验 用户名不能为空
        if (StringUtils.isBlank(username)) {
            throw new BusinessException(400, "用户名不能为空");
        }

        // 构建查询条件：根据用户名查询未删除的用户
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, username)
                .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());

        // 执行查询获取用户详情
        SysUser userDetail = this.getOne(wrapper);

        // 用户不存在时抛出异常
        if (userDetail == null) {
            throw new BusinessException(404, "用户不存在");
        }

        // 转换为详情VO对象并返回
        return sysUserConverter.toDetailVO(userDetail);
    }

    /**
     * <p>
     * 新增用户
     * </p>
     * <p>
     * 新增用户信息，需要登录并具备用户新增权限才能访问。
     * 密码将进行加密处理后存储。
     * </p>
     *
     * @param addParam 新增用户信息
     * @return 新增影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或新增失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    public Integer addUser(SysUserAddRTO addParam) {

        // 校验用户名是否已存在
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, addParam.getUsername());

        long count = this.count(wrapper);

        if (count > 0) {
            throw new BusinessException(400, "用户名已存在");
        }

        // 校验邮箱是否已存在
        LambdaQueryWrapper<SysUser> emailWrapper = new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getEmail, addParam.getEmail());
        long emailCount = this.count(emailWrapper);

        if (emailCount > 0) {
            throw new BusinessException(400, "邮箱已被注册");
        }

        // 校验手机号是否已存在
        LambdaQueryWrapper<SysUser> phoneWrapper = new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getPhone, addParam.getPhone());
        long phoneCount = this.count(phoneWrapper);

        if (phoneCount > 0) {
            throw new BusinessException(400, "手机号已被注册");
        }

        // 转换并保存用户信息
        // TODO 后续接入密码加密：BCryptPasswordEncoder.encode(addParam.getPassword())
        SysUser user = sysUserConverter.toEntityAdd(addParam);

        boolean result = this.save(user);

        if (!result) {
            throw new BusinessException(500, "新增用户失败");
        }

        return 1;
    }

    /**
     * <p>
     * 修改用户
     * </p>
     * <p>
     * 修改用户信息，需要登录并具备用户修改权限才能访问。
     * 仅允许修改指定用户的有效配置信息，不允许修改用户唯一标识。
     * 密码为空则不修改，不为空则加密后更新。
     * </p>
     *
     * @param updateParam 修改用户信息
     * @return 修改影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、用户不存在或修改失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateUser(SysUserUpdateRTO updateParam) {

        // 查询待修改的用户是否存在
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getId, updateParam.getId())
                .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysUser existUser = this.getOne(wrapper);

        if (existUser == null) {
            throw new BusinessException(404, "用户不存在");
        }

        // 如果修改了用户名, 需校验新用户名是否已被其他用户使用
        if (!existUser.getUsername().equals(updateParam.getUsername())) {
            LambdaQueryWrapper<SysUser> usernameWrapper = new LambdaQueryWrapper<SysUser>()
                    .eq(SysUser::getUsername, updateParam.getUsername())
                    .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
            long usernameCount = this.count(usernameWrapper);
            if (usernameCount > 0) {
                throw new BusinessException(400, "用户名已被其他用户使用");
            }
        }

        // 如果修改了邮箱, 需校验新邮箱是否已被其他用户使用
        if (!existUser.getEmail().equals(updateParam.getEmail())) {
            LambdaQueryWrapper<SysUser> emailWrapper = new LambdaQueryWrapper<SysUser>()
                    .eq(SysUser::getEmail, updateParam.getEmail())
                    .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
            long emailCount = this.count(emailWrapper);
            if (emailCount > 0) {
                throw new BusinessException(400, "邮箱已被其他用户使用");
            }
        }

        // 如果修改了手机号, 需校验新手机号是否已被其他用户使用
        if (!existUser.getPhone().equals(updateParam.getPhone())) {
            LambdaQueryWrapper<SysUser> phoneWrapper = new LambdaQueryWrapper<SysUser>()
                    .eq(SysUser::getPhone, updateParam.getPhone())
                    .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
            long phoneCount = this.count(phoneWrapper);
            if (phoneCount > 0) {
                throw new BusinessException(400, "手机号已被其他用户使用");
            }
        }

        // 转换并更新用户信息（Converter中password字段已ignore）
        SysUser user = sysUserConverter.toEntityUpdate(updateParam);

        // 如果密码不为空，则加密后更新密码
        // TODO 后续接入密码加密：BCryptPasswordEncoder.encode(updateParam.getPassword())
        if (StringUtils.isNotBlank(updateParam.getPassword())) {
            user.setPassword(updateParam.getPassword());
        }

        boolean result = this.updateById(user);

        if (!result) {
            throw new BusinessException(500, "修改用户失败");
        }

        return 1;
    }

    /**
     * <p>
     * 更新用户状态
     * </p>
     * <p>
     * 更新指定用户的状态（启用/禁用），禁用后该用户将无法登录。
     * 需要登录并具备用户修改权限才能访问。
     * </p>
     *
     * @param id 用户ID
     * @param status 用户状态（启用/禁用）
     * @return 更新结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、用户不存在或更新失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    public Integer updateUserStatus(String id, String status) {

        // 参数校验：用户ID不能为空
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(400, "用户ID不能为空");
        }

        // 参数校验：状态不能为空
        if (StringUtils.isBlank(status)) {
            throw new BusinessException(400, "状态不能为空");
        }

        // 校验状态值是否合法
        GlobalEnum.Status userStatus = GlobalEnum.Status.getByCode(Integer.parseInt(status));
        if (userStatus == null) {
            // 尝试通过描述解析状态
            if ("启用".equals(status)) {
                userStatus = GlobalEnum.Status.ENABLE;
            } else if ("禁用".equals(status)) {
                userStatus = GlobalEnum.Status.DISABLE;
            } else {
                throw new BusinessException(400, "状态值不合法，仅支持：启用、禁用");
            }
        }

        // 查询待更新状态的用户是否存在
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getId, Long.parseLong(id))
                .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysUser existUser = this.getOne(wrapper);

        if (existUser == null) {
            throw new BusinessException(400, "用户不存在");
        }

        // 如果状态未变更则直接返回
        if (userStatus.getCode().equals(existUser.getStatus())) {
            throw new BusinessException(400, "用户状态未变更");
        }

        // 执行状态更新
        SysUser updateUser = new SysUser();
        updateUser.setId(Long.parseLong(id));
        updateUser.setStatus(userStatus.getCode());

        boolean result = this.updateById(updateUser);

        if (!result) {
            throw new BusinessException(500, "更新用户状态失败");
        }

        return 1;
    }

    /**
     * <p>
     * 删除用户
     * </p>
     * <p>
     * 删除指定用户信息，需要登录并具备用户删除权限才能访问。
     * 删除操作不可逆，删除后用户相关数据将同步清理。
     * </p>
     *
     * @param id 用户ID
     * @return 删除影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、用户不存在或删除失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    public Integer deleteUser(String id) {

        // 查询待删除的用户是否存在
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getId, id)
                .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysUser existUser = this.getOne(wrapper);

        if (existUser == null) {
            throw new BusinessException(400, "用户不存在");
        }

        // 执行逻辑删除：设置is_deleted标志位
        SysUser user = new SysUser();
        user.setId(Long.parseLong(id));
        user.setIsDeleted(GlobalEnum.Deleted.DELETED.getCode());

        boolean result = this.updateById(user);

        if (!result) {
            throw new BusinessException(500, "删除用户失败");
        }

        return 1;
    }

    /**
     * <p>
     * 批量新增用户
     * </p>
     * <p>
     * 批量新增多个用户信息，需要登录并具备用户新增权限才能访问。
     * 批量操作支持事务回滚，任一用户新增失败则全部失败。
     * </p>
     *
     * @param addParamList 批量新增用户信息集合
     * @return 新增影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、参数校验失败或新增失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchAddUser(List<SysUserAddRTO> addParamList) {

        // 校验批量新增数量限制
        if (addParamList.size() > 100) {
            throw new BusinessException(400, "单次批量新增数量不能超过100条");
        }

        // 校验批量数据中是否有重复的用户名/邮箱/手机号
        Set<String> usernameSet = new HashSet<>();
        Set<String> emailSet = new HashSet<>();
        Set<String> phoneSet = new HashSet<>();
        for (SysUserAddRTO param : addParamList) {
            if (usernameSet.contains(param.getUsername())) {
                throw new BusinessException(400, "批量新增中存在重复的用户名: " + param.getUsername());
            }
            if (emailSet.contains(param.getEmail())) {
                throw new BusinessException(400, "批量新增中存在重复的邮箱: " + param.getEmail());
            }
            if (phoneSet.contains(param.getPhone())) {
                throw new BusinessException(400, "批量新增中存在重复的手机号: " + param.getPhone());
            }
            usernameSet.add(param.getUsername());
            emailSet.add(param.getEmail());
            phoneSet.add(param.getPhone());
        }

        // 校验用户名是否已在数据库中存在
        LambdaQueryWrapper<SysUser> usernameWrapper = new LambdaQueryWrapper<SysUser>()
                .in(SysUser::getUsername, usernameSet);
        long existUsernameCount = this.count(usernameWrapper);

        if (existUsernameCount > 0) {
            throw new BusinessException(400, "部分用户名已存在，请检查后重试");
        }

        // 校验邮箱是否已在数据库中存在
        LambdaQueryWrapper<SysUser> emailWrapper = new LambdaQueryWrapper<SysUser>()
                .in(SysUser::getEmail, emailSet);
        long existEmailCount = this.count(emailWrapper);

        if (existEmailCount > 0) {
            throw new BusinessException(400, "部分邮箱已被注册，请检查后重试");
        }

        // 校验手机号是否已在数据库中存在
        LambdaQueryWrapper<SysUser> phoneWrapper = new LambdaQueryWrapper<SysUser>()
                .in(SysUser::getPhone, phoneSet);
        long existPhoneCount = this.count(phoneWrapper);

        if (existPhoneCount > 0) {
            throw new BusinessException(400, "部分手机号已被注册，请检查后重试");
        }

        // 批量保存用户信息
        boolean batch = this.saveBatch(sysUserConverter.toEntityListAdd(addParamList));

        if (!batch) {
            throw new BusinessException(500, "批量新增用户失败");
        }

        return addParamList.size();
    }

    /**
     * <p>
     * 批量修改用户
     * </p>
     * <p>
     * 批量修改多个用户信息，需要登录并具备用户修改权限才能访问。
     * </p>
     *
     * @param updateParamList 批量修改用户信息集合
     * @return 修改影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、用户不存在或修改失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdateUser(List<SysUserUpdateRTO> updateParamList) {

        // 校验批量修改数量限制
        if (updateParamList.size() > 100) {
            throw new BusinessException(400, "单次批量修改数量不能超过100条");
        }

        // 校验批量数据的合法性：ID不能为空、不能有重复的ID
        Set<Long> idSet = new HashSet<>();
        Set<String> usernameSet = new HashSet<>();
        for (SysUserUpdateRTO item : updateParamList) {
            if (item.getId() == null) {
                throw new BusinessException(400, "批量修改中存在ID为空的记录");
            }
            if (idSet.contains(item.getId())) {
                throw new BusinessException(400, "批量修改中存在重复的用户ID: " + item.getId());
            }
            if (usernameSet.contains(item.getUsername())) {
                throw new BusinessException(400, "批量修改中存在重复的用户名: " + item.getUsername());
            }
            idSet.add(item.getId());
            usernameSet.add(item.getUsername());
        }

        // 校验用户ID是否全部存在且未被删除
        LambdaQueryWrapper<SysUser> idWrapper = new LambdaQueryWrapper<SysUser>()
                .in(SysUser::getId, idSet)
                .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        long existIdCount = this.count(idWrapper);

        if (existIdCount != idSet.size()) {
            throw new BusinessException(400, "部分用户ID不存在或已删除，请检查后重试");
        }

        // 校验用户名是否已被其他用户使用（排除自身）
        LambdaQueryWrapper<SysUser> usernameWrapper = new LambdaQueryWrapper<SysUser>()
                .in(SysUser::getUsername, usernameSet)
                .notIn(SysUser::getId, idSet)
                .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        long existUsernameCount = this.count(usernameWrapper);

        if (existUsernameCount > 0) {
            throw new BusinessException(400, "部分用户名已被其他用户使用，请检查后重试");
        }

        // 批量更新用户信息（密码单独处理）
        List<SysUser> entityList = new ArrayList<>();
        for (SysUserUpdateRTO updateParam : updateParamList) {
            SysUser user = sysUserConverter.toEntityUpdate(updateParam);
            // 如果密码不为空，则设置密码（后续加密）
            if (StringUtils.isNotBlank(updateParam.getPassword())) {
                user.setPassword(updateParam.getPassword());
            }
            entityList.add(user);
        }

        boolean batch = this.updateBatchById(entityList);

        if (!batch) {
            throw new BusinessException(500, "批量更新用户失败");
        }

        return updateParamList.size();
    }

    /**
     * <p>
     * 批量更新用户状态
     * </p>
     * <p>
     * 批量更新多个指定用户的状态（启用/禁用），禁用后该用户将无法登录。
     * 批量操作支持事务回滚，任一用户更新失败则全部失败。
     * 需要登录并具备用户修改权限才能访问。
     * </p>
     *
     * @param ids 用户ID集合
     * @param status 用户状态（启用/禁用）
     * @return 更新结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、用户不存在或更新失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdateUserStatus(List<String> ids, String status) {

        // 校验批量更新数量限制
        if (ids.size() > 100) {
            throw new BusinessException(400, "单次批量更新数量不能超过100条");
        }

        // 参数校验：状态不能为空
        if (StringUtils.isBlank(status)) {
            throw new BusinessException(400, "状态不能为空");
        }

        // 校验状态值是否合法
        GlobalEnum.Status userStatus = GlobalEnum.Status.getByCode(Integer.parseInt(status));
        if (userStatus == null) {
            // 尝试通过描述解析状态
            if ("启用".equals(status)) {
                userStatus = GlobalEnum.Status.ENABLE;
            } else if ("禁用".equals(status)) {
                userStatus = GlobalEnum.Status.DISABLE;
            } else {
                throw new BusinessException(400, "状态值不合法");
            }
        }

        // 校验ID格式并转换为Long类型
        Set<Long> idSet = new HashSet<>();
        for (String id : ids) {
            if (StringUtils.isBlank(id)) {
                throw new BusinessException(400, "用户ID不能为空");
            }
            idSet.add(Long.parseLong(id));
        }

        // 查询待更新状态的用户是否存在且未被删除
        LambdaQueryWrapper<SysUser> existWrapper = new LambdaQueryWrapper<SysUser>()
                .in(SysUser::getId, idSet)
                .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        List<SysUser> existUsers = this.list(existWrapper);

        if (existUsers.isEmpty()) {
            throw new BusinessException(404, "未找到可更新状态的用户");
        }

        // 构建批量状态更新的数据列表
        List<SysUser> updateList = new ArrayList<>();
        for (SysUser user : existUsers) {
            // 跳过状态未变更的用户
            if (userStatus.getCode().equals(user.getStatus())) {
                continue;
            }
            SysUser updateUser = new SysUser();
            updateUser.setId(user.getId());
            updateUser.setStatus(userStatus.getCode());
            updateList.add(updateUser);
        }

        if (updateList.isEmpty()) {
            throw new BusinessException(400, "所有用户状态均未变更");
        }

        // 执行批量状态更新
        boolean batch = this.updateBatchById(updateList);

        if (!batch) {
            throw new BusinessException(500, "批量更新用户状态失败");
        }

        return updateList.size();
    }

    /**
     * <p>
     * 批量删除用户
     * </p>
     * <p>
     * 批量删除多个指定用户信息，需要登录并具备用户删除权限才能访问。
     * </p>
     *
     * @param ids 用户ID集合
     * @return 删除影响的行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、用户不存在或删除失败时抛出
     * @author shy
     * @since 2026-05-06
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchDeleteUser(List<String> ids) {

        // 校验批量删除数量限制
        if (ids.size() > 100) {
            throw new BusinessException(400, "单次批量删除数量不能超过100条");
        }

        // 校验ID格式并转换为Long类型
        Set<Long> idSet = new HashSet<>();
        for (String id : ids) {
            if (StringUtils.isBlank(id)) {
                throw new BusinessException(400, "用户ID不能为空");
            }
            idSet.add(Long.parseLong(id));
        }

        // 查询待删除的用户是否存在且未被删除
        LambdaQueryWrapper<SysUser> existWrapper = new LambdaQueryWrapper<SysUser>()
                .in(SysUser::getId, idSet)
                .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        List<SysUser> existUsers = this.list(existWrapper);

        if (existUsers.isEmpty()) {
            throw new BusinessException(404, "未找到可删除的用户");
        }

        // 构建批量逻辑删除的数据列表
        List<SysUser> userList = new ArrayList<>();
        for (SysUser user : existUsers) {
            SysUser deleteUser = new SysUser();
            deleteUser.setId(user.getId());
            deleteUser.setIsDeleted(GlobalEnum.Deleted.DELETED.getCode());
            userList.add(deleteUser);
        }

        // 执行批量逻辑删除
        boolean batch = this.updateBatchById(userList);

        if (!batch) {
            throw new BusinessException(500, "批量删除用户失败");
        }

        return ids.size();
    }

}
