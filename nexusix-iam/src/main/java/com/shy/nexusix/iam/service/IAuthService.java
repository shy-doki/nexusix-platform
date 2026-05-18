package com.shy.nexusix.iam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shy.nexusix.iam.entity.SysUser;
import com.shy.nexusix.iam.rto.LoginRTO;
import com.shy.nexusix.iam.rto.RegisterRTO;
import com.shy.nexusix.iam.vo.LoginVO;
import com.shy.nexusix.iam.vo.RegisterVO;

import java.util.List;

/**
 * <p>
 * 认证服务接口 - 提供登录、注册等认证功能
 * </p>
 *
 * @author shy
 * @since 2026-05-17
 */
public interface IAuthService extends IService<SysUser> {

    /**
     * <p>
     * 用户登录
     * </p>
     * <p>
     * 验证用户名密码，加载权限角色信息到Sa-Token Session，
     * 自动选择默认租户，返回Token及用户信息。
     * </p>
     *
     * @param loginRTO 登录请求参数
     * @return 登录响应结果，包含Token、用户信息、权限和角色
     * @throws com.shy.nexusix.common.exception.BusinessException 用户不存在、密码错误、用户被禁用等情况
     * @author shy
     * @since 2026-05-17
     */
    String login(LoginRTO loginRTO);

    /**
     * <p>
     * 用户注册
     * </p>
     * <p>
     * 校验用户名唯一性，BCrypt加密密码，创建用户记录。
     * </p>
     *
     * @param registerRTO 注册请求参数
     * @return 注册响应结果，包含用户ID和用户名
     * @throws com.shy.nexusix.common.exception.BusinessException 用户名已存在等情况
     * @author shy
     * @since 2026-05-17
     */
    RegisterVO register(RegisterRTO registerRTO);

}
