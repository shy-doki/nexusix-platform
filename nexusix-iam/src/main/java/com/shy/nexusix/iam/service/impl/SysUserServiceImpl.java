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
import com.shy.nexusix.iam.converter.SysUserConverter;
import com.shy.nexusix.iam.entity.SysUser;
import com.shy.nexusix.iam.mapper.SysUserMapper;
import com.shy.nexusix.iam.rto.SysUserAddRTO;
import com.shy.nexusix.iam.rto.SysUserQueryRTO;
import com.shy.nexusix.iam.rto.SysUserUpdateRTO;
import com.shy.nexusix.iam.service.ISysUserService;
import com.shy.nexusix.iam.vo.SysUserCommonVO;
import com.shy.nexusix.iam.vo.SysUserDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>系统用户服务实现类</p>
 *
 * @author shy
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private SysUserConverter sysUserConverter;

    // 密码编码器 暂无Spring Security Bean配置 直接实例化
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * <p>获取查询操作的可操作字段</p>
     *
     * @return 可操作字段列表，null表示无限制
     */
    private List<String> getQueryOperableFields() {
        // 暂无IAM字段权限配置 返回null表示无限制
        return null;
    }

    /**
     * <p>获取指定操作类型的字段权限</p>
     *
     * @param operationType 操作类型："query"、"create"、"update"
     * @return 可操作字段列表，null表示无权限或无限制
     */
    private List<String> getTableFieldPermission(String operationType) {
        // 暂无IAM字段权限配置 返回null表示无限制
        return null;
    }

    /**
     * <p>查询用户列表</p>
     *
     * @return 用户通用VO列表
     * @throws BusinessException 用户上下文为空时抛出业务异常
     */
    @Override
    public List<SysUserCommonVO> queryUserList() {

        // 获取字段权限
        List<String> visibleFields = getQueryOperableFields();

        // 构建查询条件 仅选择用户有权限查看的列，并排除已删除的用户记录
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<SysUser>();

        // 如果有字段级权限限制，则只选择可操作字段
        if (visibleFields != null && !visibleFields.isEmpty()) {
            wrapper.select(SysUser.class, entity -> visibleFields.contains(entity.getColumn()));
        }

        wrapper.eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());

        List<SysUser> userList = this.list(wrapper);
        // 通过 MapStruct 转换器将实体列表转换为 VO 列表
        return sysUserConverter.entityListToCommonVoList(userList);

    }

    /**
     * <p>分页查询用户列表</p>
     *
     * @param page 分页参数，包含页码和每页数量
     * @return 分页后的用户通用VO列表
     */
    @Override
    public IPage<SysUserCommonVO> queryUserPage(PageCommonRTO page) {

        // 获取字段权限
        List<String> visibleFields = getQueryOperableFields();

        // 构建分页查询条件 仅选择用户有权限查看的列，并排除已删除的用户记录
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<SysUser>();

        // 如果有字段级权限限制，则只选择可操作字段
        if (visibleFields != null && !visibleFields.isEmpty()) {
            wrapper.select(SysUser.class, entity -> visibleFields.contains(entity.getColumn()));
        }

        wrapper.eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());

        // 执行分页查询
        IPage<SysUser> entityPage = this.page(new Page<>(page.getPageNum(), page.getPageSize()), wrapper);

        // 构建VO分页对象 保留原始分页信息
        IPage<SysUserCommonVO> voPage = new Page<>(entityPage.getCurrent(), entityPage.getSize(), entityPage.getTotal());
        // 通过 MapStruct 转换器将实体分页记录转换为 VO 列表
        voPage.setRecords(sysUserConverter.entityListToCommonVoList(entityPage.getRecords()));
        return voPage;

    }

    /**
     * <p>条件查询用户列表</p>
     *
     * @param queryParam 查询条件，包含用户编码、用户名、昵称等筛选条件
     * @return 满足条件的用户分页列表
     */
    @Override
    public IPage<SysUserCommonVO> queryUser(SysUserQueryRTO queryParam) {

        // 获取字段权限
        List<String> visibleFields = getQueryOperableFields();

        // 构建条件查询 仅选择用户有权限查看的列
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<SysUser>();
        if (visibleFields != null && !visibleFields.isEmpty()) {
            wrapper.select(SysUser.class, entity -> visibleFields.contains(entity.getColumn()));
        }
        wrapper.eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());

        // 用户编码精确匹配
        if (queryParam.getUserCode() != null && !queryParam.getUserCode().isEmpty()) {
            wrapper.eq(SysUser::getUserCode, queryParam.getUserCode());
        }
        // 用户名模糊匹配
        if (queryParam.getUserName() != null && !queryParam.getUserName().isEmpty()) {
            wrapper.like(SysUser::getUserName, queryParam.getUserName());
        }
        // 昵称模糊匹配
        if (queryParam.getNickName() != null && !queryParam.getNickName().isEmpty()) {
            wrapper.like(SysUser::getNickName, queryParam.getNickName());
        }
        // 真实姓名模糊匹配
        if (queryParam.getRealName() != null && !queryParam.getRealName().isEmpty()) {
            wrapper.like(SysUser::getRealName, queryParam.getRealName());
        }
        // 电子邮箱精确匹配
        if (queryParam.getEmail() != null && !queryParam.getEmail().isEmpty()) {
            wrapper.eq(SysUser::getEmail, queryParam.getEmail());
        }
        // 手机号码精确匹配
        if (queryParam.getPhone() != null && !queryParam.getPhone().isEmpty()) {
            wrapper.eq(SysUser::getPhone, queryParam.getPhone());
        }
        // 性别精确匹配
        if (queryParam.getGender() != null && !queryParam.getGender().isEmpty()) {
            wrapper.eq(SysUser::getGender, queryParam.getGender());
        }
        // 状态精确匹配
        if (queryParam.getStatus() != null && !queryParam.getStatus().isEmpty()) {
            wrapper.eq(SysUser::getStatus, queryParam.getStatus());
        }
        // 创建人编码精确匹配 通过用户编码查找创建人ID
        if (queryParam.getCreateByCode() != null && !queryParam.getCreateByCode().isEmpty()) {
            SysUser createUser = this.getOne(new LambdaQueryWrapper<SysUser>()
                    .eq(SysUser::getUserCode, queryParam.getCreateByCode())
                    .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
            if (createUser != null) {
                wrapper.eq(SysUser::getCreateBy, createUser.getId());
            } else {
                // 创建人不存在 限制查询结果为空
                wrapper.eq(SysUser::getCreateBy, -1L);
            }
        }
        // 更新人编码精确匹配 通过用户编码查找更新人ID
        if (queryParam.getUpdateByCode() != null && !queryParam.getUpdateByCode().isEmpty()) {
            SysUser updateUser = this.getOne(new LambdaQueryWrapper<SysUser>()
                    .eq(SysUser::getUserCode, queryParam.getUpdateByCode())
                    .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
            if (updateUser != null) {
                wrapper.eq(SysUser::getUpdateBy, updateUser.getId());
            } else {
                // 更新人不存在 限制查询结果为空
                wrapper.eq(SysUser::getUpdateBy, -1L);
            }
        }
        // 创建时间范围查询
        TimeRangeCommonRTO createTimeRange = queryParam.getCreateTimeRange();
        if (createTimeRange != null) {
            if (createTimeRange.getStartTime() != null) {
                wrapper.ge(SysUser::getCreateAt, createTimeRange.getStartTime());
            }
            if (createTimeRange.getEndTime() != null) {
                wrapper.le(SysUser::getCreateAt, createTimeRange.getEndTime());
            }
        }
        // 更新时间范围查询
        TimeRangeCommonRTO updateTimeRange = queryParam.getUpdateTimeRange();
        if (updateTimeRange != null) {
            if (updateTimeRange.getStartTime() != null) {
                wrapper.ge(SysUser::getUpdateAt, updateTimeRange.getStartTime());
            }
            if (updateTimeRange.getEndTime() != null) {
                wrapper.le(SysUser::getUpdateAt, updateTimeRange.getEndTime());
            }
        }

        // 执行分页查询
        IPage<SysUser> entityPage = this.page(new Page<>(queryParam.getPageNum(), queryParam.getPageSize()), wrapper);

        // 构建VO分页对象 保留原始分页信息
        IPage<SysUserCommonVO> voPage = new Page<>(entityPage.getCurrent(), entityPage.getSize(), entityPage.getTotal());
        // 通过 MapStruct 转换器将实体分页记录转换为 VO 列表
        voPage.setRecords(sysUserConverter.entityListToCommonVoList(entityPage.getRecords()));
        return voPage;

    }

    /**
     * <p>查询用户详情</p>
     *
     * @param userCode 用户编码，用于定位唯一用户
     * @return 用户详情VO，包含完整的用户信息
     * @throws BusinessException 用户不存在时抛出业务异常
     */
    @Override
    public SysUserDetailVO queryUserDetail(String userCode) {

        // 参数校验 用户编码不能为空
        if (userCode == null || userCode.trim().isEmpty()) {
            throw new BusinessException("用户编码不能为空");
        }

        // 获取字段权限
        List<String> visibleFields = getQueryOperableFields();

        // 根据用户编码查询 仅选择用户有权限查看的列
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<SysUser>();
        if (visibleFields != null && !visibleFields.isEmpty()) {
            wrapper.select(SysUser.class, entity -> visibleFields.contains(entity.getColumn()));
        }
        wrapper.eq(SysUser::getUserCode, userCode)
               .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());

        SysUser user = this.getOne(wrapper);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 通过 MapStruct 转换器将实体转换为详情VO
        return sysUserConverter.toDetailVO(user);

    }

    /**
     * <p>新增用户</p>
     *
     * @param addParam 新增用户信息
     * @return 新增结果行数
     * @throws BusinessException 用户编码已存在或字段权限不足时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer addUser(SysUserAddRTO addParam) {

        // 获取创建操作的字段权限
        List<String> visibleFields = getTableFieldPermission("create");
        if (visibleFields == null || visibleFields.isEmpty()) {
            throw new BusinessException("无权新增用户");
        }

        // 校验用户编码唯一性
        LambdaQueryWrapper<SysUser> codeCheckWrapper = new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUserCode, addParam.getUserCode())
                .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        if (this.count(codeCheckWrapper) > 0) {
            throw new BusinessException("用户编码已存在");
        }

        // 通过 MapStruct 转换器将RTO转换为实体
        SysUser entity = sysUserConverter.toEntityFromAdd(addParam);

        // 密码加密 converter的toEntityFromAdd忽略了password字段 需要手动设置加密后的密码
        if (addParam.getPassword() != null && !addParam.getPassword().isEmpty()) {
            entity.setPassword(passwordEncoder.encode(addParam.getPassword()));
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
        if (!visibleFields.contains("user_name")) entity.setUserName(null);
        if (!visibleFields.contains("nick_name")) entity.setNickName(null);
        if (!visibleFields.contains("real_name")) entity.setRealName(null);
        if (!visibleFields.contains("email")) entity.setEmail(null);
        if (!visibleFields.contains("phone")) entity.setPhone(null);
        if (!visibleFields.contains("avatar_url")) entity.setAvatarUrl(null);
        if (!visibleFields.contains("gender")) entity.setGender(null);
        if (!visibleFields.contains("birthday")) entity.setBirthday(null);
        if (!visibleFields.contains("status")) entity.setStatus(null);
        if (!visibleFields.contains("disable_reason")) entity.setDisableReason(null);

        // 保存用户信息
        this.save(entity);
        return 1;

    }

    /**
     * <p>修改用户</p>
     *
     * @param updateParam 修改用户信息
     * @return 修改结果行数
     * @throws BusinessException 用户不存在或字段权限不足时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateUser(SysUserUpdateRTO updateParam) {

        // 获取更新操作的字段权限
        List<String> visibleFields = getTableFieldPermission("update");
        if (visibleFields == null || visibleFields.isEmpty()) {
            throw new BusinessException("无权修改用户");
        }

        // 查询待更新的用户 确保用户存在且未删除
        SysUser existingUser = this.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUserCode, updateParam.getUserCode())
                .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
        if (existingUser == null) {
            throw new BusinessException("用户不存在");
        }

        // 通过 MapStruct 转换器将RTO转换为实体
        SysUser entity = sysUserConverter.toEntityFromUpdate(updateParam);

        // 设置实体ID用于更新条件
        entity.setId(existingUser.getId());

        boolean isSuperAdmin = StpUtil.hasRole(GlobalConstant.Role.SUPER_ADMIN_ROLE);
        if (isSuperAdmin) {
            // 超级管理员：若明确填写了审核字段值则以填写值为准，若未填写则保留原值或自动应用默认值
            if (entity.getCreateBy() == null) {
                entity.setCreateBy(existingUser.getCreateBy());
            }
            if (entity.getCreateAt() == null) {
                entity.setCreateAt(existingUser.getCreateAt());
            }
            if (entity.getUpdateBy() == null) {
                entity.setUpdateBy(Long.valueOf(StpUtil.getLoginIdAsString()));
            }
            if (entity.getUpdateAt() == null) {
                entity.setUpdateAt(LocalDateTime.now());
            }
            // isDeleted未填写时保留原值
            if (entity.getIsDeleted() == null) {
                entity.setIsDeleted(existingUser.getIsDeleted());
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
        if (!visibleFields.contains("user_name")) entity.setUserName(null);
        if (!visibleFields.contains("nick_name")) entity.setNickName(null);
        if (!visibleFields.contains("real_name")) entity.setRealName(null);
        if (!visibleFields.contains("email")) entity.setEmail(null);
        if (!visibleFields.contains("phone")) entity.setPhone(null);
        if (!visibleFields.contains("avatar_url")) entity.setAvatarUrl(null);
        if (!visibleFields.contains("gender")) entity.setGender(null);
        if (!visibleFields.contains("birthday")) entity.setBirthday(null);
        if (!visibleFields.contains("status")) entity.setStatus(null);
        if (!visibleFields.contains("disable_reason")) entity.setDisableReason(null);

        // 不可修改字段
        entity.setUserCode(null);
        entity.setPassword(null);
        entity.setId(existingUser.getId());

        // 执行更新操作 使用updateById仅更新非null字段
        this.updateById(entity);
        return 1;

    }

    /**
     * <p>更新用户状态</p>
     *
     * @param id 用户ID
     * @param status 目标状态（ENABLED/DISABLED/LOCKED）
     * @return 更新结果行数
     * @throws BusinessException 用户不存在或状态无效时抛出业务异常
     */
    @Override
    public Integer updateUserStatus(String id, String status) {

        // 获取更新操作的字段权限
        List<String> visibleFields = getTableFieldPermission("update");
        if (visibleFields == null || !visibleFields.contains("status")) {
            throw new BusinessException("无权修改用户状态字段");
        }

        // 校验状态值合法性
        if (!GlobalEnum.UserStatus.isValidCode(status)) {
            throw new BusinessException("无效的用户状态");
        }

        // 查询待更新状态的用户
        SysUser user = this.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getId, id)
                .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 校验用户是否已处于目标状态
        if (status.equals(user.getStatus())) {
            throw new BusinessException("用户已处于该状态，无需重复操作");
        }

        // 更新用户状态 审核字段updateBy和updateAt由系统自动设置
        // 停用时记录禁用原因，启用时清除禁用原因
        LambdaUpdateWrapper<SysUser> updateWrapper = new LambdaUpdateWrapper<SysUser>()
                .eq(SysUser::getId, id)
                .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .set(SysUser::getStatus, status)
                .set(SysUser::getUpdateBy, Long.valueOf(StpUtil.getLoginIdAsString()))
                .set(SysUser::getUpdateAt, LocalDateTime.now());
        if (GlobalEnum.UserStatus.DISABLED.getCode().equals(status)) {
            updateWrapper.set(SysUser::getDisableReason, "ADMIN_DISABLE");
        } else if (GlobalEnum.UserStatus.ENABLED.getCode().equals(status)) {
            updateWrapper.set(SysUser::getDisableReason, null);
        }
        this.update(updateWrapper);
        return 1;

    }

    /**
     * <p>删除用户</p>
     *
     * @param id 用户ID
     * @return 删除结果行数
     * @throws BusinessException 用户不存在或字段权限不足时抛出业务异常
     */
    @Override
    public Integer deleteUser(String id) {

        // 获取更新操作的字段权限
        List<String> visibleFields = getTableFieldPermission("update");
        if (visibleFields == null || !visibleFields.contains("is_deleted")) {
            throw new BusinessException("无权删除用户");
        }

        // 查询待删除的用户
        SysUser user = this.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getId, id)
                .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 执行逻辑删除 审核字段isDeleted和deletedAt由系统自动设置
        this.update(new LambdaUpdateWrapper<SysUser>()
                .eq(SysUser::getId, id)
                .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .set(SysUser::getIsDeleted, GlobalEnum.Deleted.DELETED.getCode())
                .set(SysUser::getDeletedAt, LocalDateTime.now())
                .set(SysUser::getUpdateBy, Long.valueOf(StpUtil.getLoginIdAsString()))
                .set(SysUser::getUpdateAt, LocalDateTime.now()));
        return 1;

    }

    /**
     * <p>批量新增用户</p>
     *
     * @param addParamList 批量新增用户信息集合
     * @return 成功新增的用户数量
     * @throws BusinessException 任一用户编码重复或字段权限不足时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchAddUser(List<SysUserAddRTO> addParamList) {

        // 获取创建操作的字段权限
        List<String> visibleFields = getTableFieldPermission("create");
        if (visibleFields == null || visibleFields.isEmpty()) {
            throw new BusinessException("无权新增用户");
        }

        // 审核字段权限控制 通过Sa-Token判断当前用户是否为超级管理员
        boolean isSuperAdmin = StpUtil.hasRole(GlobalConstant.Role.SUPER_ADMIN_ROLE);

        // 通过 MapStruct 转换器批量将RTO列表转换为实体列表
        List<SysUser> entityList = sysUserConverter.addRTOListToEntityList(addParamList);

        // 批量内重复编码检查 同一批次中用户编码不得重复
        Set<String> batchCodeSet = new HashSet<>();
        for (SysUser entity : entityList) {
            if (!batchCodeSet.add(entity.getUserCode())) {
                throw new BusinessException("批量新增中存在重复的用户编码: " + entity.getUserCode());
            }
        }

        // 遍历处理每个用户实体 校验编码唯一性、密码加密、设置默认值、清除不可操作字段
        for (int i = 0; i < entityList.size(); i++) {
            SysUser entity = entityList.get(i);

            // 校验用户编码唯一性
            long codeCount = this.count(new LambdaQueryWrapper<SysUser>()
                    .eq(SysUser::getUserCode, entity.getUserCode())
                    .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
            if (codeCount > 0) {
                throw new BusinessException("用户编码已存在: " + entity.getUserCode());
            }

            // 密码加密
            String rawPassword = addParamList.get(i).getPassword();
            if (rawPassword != null && !rawPassword.isEmpty()) {
                entity.setPassword(passwordEncoder.encode(rawPassword));
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
            if (!visibleFields.contains("user_name")) entity.setUserName(null);
            if (!visibleFields.contains("nick_name")) entity.setNickName(null);
            if (!visibleFields.contains("real_name")) entity.setRealName(null);
            if (!visibleFields.contains("email")) entity.setEmail(null);
            if (!visibleFields.contains("phone")) entity.setPhone(null);
            if (!visibleFields.contains("avatar_url")) entity.setAvatarUrl(null);
            if (!visibleFields.contains("gender")) entity.setGender(null);
            if (!visibleFields.contains("birthday")) entity.setBirthday(null);
            if (!visibleFields.contains("status")) entity.setStatus(null);
            if (!visibleFields.contains("disable_reason")) entity.setDisableReason(null);
        }

        // 批量保存所有用户
        this.saveBatch(entityList);
        return entityList.size();

    }

    /**
     * <p>批量修改用户</p>
     *
     * @param updateParamList 批量修改用户信息集合
     * @return 成功修改的用户数量
     * @throws BusinessException 任一用户不存在或字段权限不足时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdateUser(List<SysUserUpdateRTO> updateParamList) {

        // 获取更新操作的字段权限
        List<String> visibleFields = getTableFieldPermission("update");
        if (visibleFields == null || visibleFields.isEmpty()) {
            throw new BusinessException("无权修改用户");
        }

        // 审核字段权限控制 通过Sa-Token判断当前用户是否为超级管理员
        boolean isSuperAdmin = StpUtil.hasRole(GlobalConstant.Role.SUPER_ADMIN_ROLE);

        // 遍历处理每个用户更新
        for (SysUserUpdateRTO updateParam : updateParamList) {
            // 查询待更新的用户 确保用户存在且未删除
            SysUser existingUser = this.getOne(new LambdaQueryWrapper<SysUser>()
                    .eq(SysUser::getUserCode, updateParam.getUserCode())
                    .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
            if (existingUser == null) {
                throw new BusinessException("用户不存在: " + updateParam.getUserCode());
            }

            // 通过 MapStruct 转换器将RTO转换为实体
            SysUser entity = sysUserConverter.toEntityFromUpdate(updateParam);
            entity.setId(existingUser.getId());

            // 审核字段权限控制
            if (isSuperAdmin) {
                // 超级管理员：若明确填写了审核字段值则以填写值为准，若未填写则保留原值或自动应用默认值
                if (entity.getCreateBy() == null) {
                    entity.setCreateBy(existingUser.getCreateBy());
                }
                if (entity.getCreateAt() == null) {
                    entity.setCreateAt(existingUser.getCreateAt());
                }
                if (entity.getUpdateBy() == null) {
                    entity.setUpdateBy(Long.valueOf(StpUtil.getLoginIdAsString()));
                }
                if (entity.getUpdateAt() == null) {
                    entity.setUpdateAt(LocalDateTime.now());
                }
                if (entity.getIsDeleted() == null) {
                    entity.setIsDeleted(existingUser.getIsDeleted());
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
            if (!visibleFields.contains("user_name")) entity.setUserName(null);
            if (!visibleFields.contains("nick_name")) entity.setNickName(null);
            if (!visibleFields.contains("real_name")) entity.setRealName(null);
            if (!visibleFields.contains("email")) entity.setEmail(null);
            if (!visibleFields.contains("phone")) entity.setPhone(null);
            if (!visibleFields.contains("avatar_url")) entity.setAvatarUrl(null);
            if (!visibleFields.contains("gender")) entity.setGender(null);
            if (!visibleFields.contains("birthday")) entity.setBirthday(null);
            if (!visibleFields.contains("status")) entity.setStatus(null);
            if (!visibleFields.contains("disable_reason")) entity.setDisableReason(null);

            // 不可修改字段
            entity.setUserCode(null);
            entity.setPassword(null);

            // 执行更新
            this.updateById(entity);
        }
        return updateParamList.size();

    }

    /**
     * <p>批量更新用户状态</p>
     *
     * @param ids 用户ID集合
     * @param status 目标状态
     * @return 更新结果行数
     * @throws BusinessException 状态无效或字段权限不足时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdateUserStatus(List<String> ids, String status) {

        // 获取更新操作的字段权限
        List<String> visibleFields = getTableFieldPermission("update");
        if (visibleFields == null || !visibleFields.contains("status")) {
            throw new BusinessException("无权修改用户状态字段");
        }

        // 校验状态值合法性
        if (!GlobalEnum.UserStatus.isValidCode(status)) {
            throw new BusinessException("无效的用户状态");
        }

        int totalUpdated = 0;

        // 遍历每个用户ID 更新状态
        for (String id : ids) {
            // 查询用户信息
            SysUser user = this.getOne(new LambdaQueryWrapper<SysUser>()
                    .eq(SysUser::getId, id)
                    .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
            if (user == null) {
                continue;
            }

            // 校验用户是否已处于目标状态
            if (status.equals(user.getStatus())) {
                continue;
            }

            // 更新用户状态 审核字段updateBy和updateAt由系统自动设置
            // 停用时记录禁用原因，启用时清除禁用原因
            LambdaUpdateWrapper<SysUser> batchUpdateWrapper = new LambdaUpdateWrapper<SysUser>()
                    .eq(SysUser::getId, id)
                    .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                    .set(SysUser::getStatus, status)
                    .set(SysUser::getUpdateBy, Long.valueOf(StpUtil.getLoginIdAsString()))
                    .set(SysUser::getUpdateAt, LocalDateTime.now());
            if (GlobalEnum.UserStatus.DISABLED.getCode().equals(status)) {
                batchUpdateWrapper.set(SysUser::getDisableReason, "ADMIN_DISABLE");
            } else if (GlobalEnum.UserStatus.ENABLED.getCode().equals(status)) {
                batchUpdateWrapper.set(SysUser::getDisableReason, null);
            }
            this.update(batchUpdateWrapper);
            totalUpdated++;
        }
        return totalUpdated;

    }

    /**
     * <p>批量删除用户</p>
     *
     * @param ids 用户ID集合
     * @return 删除结果行数
     * @throws BusinessException 字段权限不足时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchDeleteUser(List<String> ids) {

        // 获取更新操作的字段权限
        List<String> visibleFields = getTableFieldPermission("update");
        if (visibleFields == null || !visibleFields.contains("is_deleted")) {
            throw new BusinessException("无权删除用户");
        }

        int totalDeleted = 0;

        // 遍历每个用户ID 执行逻辑删除
        for (String id : ids) {
            // 查询待删除的用户
            SysUser user = this.getOne(new LambdaQueryWrapper<SysUser>()
                    .eq(SysUser::getId, id)
                    .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
            if (user == null) {
                continue;
            }

            // 执行逻辑删除 审核字段isDeleted和deletedAt由系统自动设置
            this.update(new LambdaUpdateWrapper<SysUser>()
                    .eq(SysUser::getId, id)
                    .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                    .set(SysUser::getIsDeleted, GlobalEnum.Deleted.DELETED.getCode())
                    .set(SysUser::getDeletedAt, LocalDateTime.now())
                    .set(SysUser::getUpdateBy, Long.valueOf(StpUtil.getLoginIdAsString()))
                    .set(SysUser::getUpdateAt, LocalDateTime.now()));
            totalDeleted++;
        }
        return totalDeleted;

    }

}
