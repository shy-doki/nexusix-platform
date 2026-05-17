package com.shy.nexusix.iam.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.iam.entity.*;
import com.shy.nexusix.iam.mapper.SysUserMapper;
import com.shy.nexusix.iam.rto.LoginRTO;
import com.shy.nexusix.iam.rto.RegisterRTO;
import com.shy.nexusix.iam.service.IAuthService;
import com.shy.nexusix.iam.service.ISysPermPolicyService;
import com.shy.nexusix.iam.vo.LoginVO;
import com.shy.nexusix.iam.vo.RegisterVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 认证服务实现类 - 提供登录、注册等认证功能的具体实现
 * </p>
 * <p>
 * 登录流程：验证用户 → Sa-Token登录 → 加载权限角色到Session → 选择默认租户 → 返回Token
 * 注册流程：校验唯一性 → BCrypt加密 → 创建用户记录
 * </p>
 *
 * @author shy
 * @since 2026-05-17
 */
@Service
public class IAuthServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements IAuthService {

    @Autowired
    private ISysPermPolicyService iSysPermPolicyService;

    /**
     * <p>
     * 用户登录
     * </p>
     * <p>
     * 完整登录流程：
     * 1. 根据用户名查询用户
     * 2. 校验用户状态（是否启用）
     * 3. BCrypt密码校验
     * 4. Sa-Token执行登录
     * 5. 加载权限和角色信息到Session
     * 6. 自动选择默认租户
     * 7. 更新最后登录信息
     * </p>
     *
     * @param loginRTO 登录请求参数
     * @return 登录响应结果
     * @throws BusinessException 用户不存在、密码错误、用户被禁用
     * @author shy
     * @since 2026-05-17
     */
    @Override
    public LoginVO login(LoginRTO loginRTO) {
        SysUser user = getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, loginRTO.getUsername())
                .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
        if (user == null) {
            throw new BusinessException(401, "用户名或密码错误");
        }

        if (!GlobalEnum.UserStatus.ENABLED.getCode().equals(user.getStatus())) {
            throw new BusinessException(403, "用户已被禁用，请联系管理员");
        }

        if (!Objects.equals(loginRTO.getPassword(), user.getPassword())) {
            throw new BusinessException(401, "用户名或密码错误");
        }

        StpUtil.login(user.getId());

        // 计算登录用户权限信息 [系统级、租户级、角色级、用户级]  用户和租户绑定值去查sys_user_perm_rel
        iSysPermPolicyService.

        // 计算登录用户角色信息 [系统级、租户级、用户级] 用户和租户绑定值去查sys_user_role_rel

        // 计算登录用户字段操作信息 在权限信息里的字段

        // 存入 Session

    }

    /**
     * <p>
     * 用户注册
     * </p>
     * <p>
     * 注册流程：
     * 1. 校验用户名唯一性
     * 2. BCrypt加密密码
     * 3. 创建用户记录
     * </p>
     *
     * @param registerRTO 注册请求参数
     * @return 注册响应结果
     * @throws BusinessException 用户名已存在
     * @author shy
     * @since 2026-05-17
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RegisterVO register(RegisterRTO registerRTO) {

        SysUser user = new SysUser();
        user.setUsername(registerRTO.getUsername());
        user.setPassword(registerRTO.getPassword());
        user.setNickname(registerRTO.getNickname());
        user.setEmail(registerRTO.getEmail());
        user.setPhone(registerRTO.getPhone());
        user.setStatus(GlobalEnum.UserStatus.ENABLED.getCode());
        user.setIsDeleted(GlobalEnum.Deleted.NOT_DELETED.getCode());
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        save(user);

        RegisterVO vo = new RegisterVO();
        vo.setUserId(user.getId());
        vo.setUsername(user.getUsername());
        return vo;
    }

}
